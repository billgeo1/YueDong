package com.xiangmu.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangmu.agent.record.AgentConfirmationRecord;
import com.xiangmu.agent.record.AgentSessionRecord;
import com.xiangmu.agent.record.AgentToolCallRecord;
import com.xiangmu.mappers.AgentAuditMapper;
import com.xiangmu.mappers.AgentMapper;
import com.xiangmu.mappers.CartMapper;
import com.xiangmu.mappers.OrderMapper;
import com.xiangmu.pojo.Admin;
import com.xiangmu.pojo.Cart;
import com.xiangmu.pojo.Order;
import com.xiangmu.pojo.User;
import com.xiangmu.service.CartService;

@Service
public class AgentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentService.class);

    @Autowired
    private RuleBasedAgentPlanner rulePlanner;

    @Autowired
    private LlmAgentPlanner llmPlanner;

    @Autowired
    private AgentToolRegistry toolRegistry;

    @Autowired
    private AgentConfirmationService confirmationService;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private AgentAuditMapper auditMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartService cartService;

    public Map<String, Object> chat(AgentChatRequest request, HttpSession session) {
        String mode = normalizeMode(request == null ? null : request.getMode());
        AgentToolContext context = createContext(
                mode,
                session,
                cleanSessionId(request == null ? null : request.getSessionId()),
                newTraceId());

        auditSession(context);
        String message = request == null ? null : request.getMessage();
        boolean llmAvailable = llmPlanner.isReady();
        AgentPlan plan = null;
        if (llmAvailable) {
            try {
                plan = llmPlanner.plan(message, mode, request == null ? null : request.getHistory());
            } catch (AgentModelException ex) {
                LOGGER.warn("LLM planner unavailable, falling back to rules: {}", ex.getMessage());
            }
        }
        if (plan == null) {
            plan = rulePlanner.plan(message, mode);
            plan.setPlanner("rules");
            plan.setProvider(null);
            plan.setModelVersion(null);
        }
        Map<String, Object> response = baseResponse(context, plan);
        response.put("llmFallback", llmAvailable && !"llm".equals(plan.getPlanner()));
        if (plan.getDirectAnswer() != null) {
            response.put("answer", plan.getDirectAnswer());
            response.put("answerPlanner", plan.getPlanner());
            return response;
        }

        AgentToolDefinition definition = toolRegistry.getRequired(plan.getToolName(), mode);
        assertToolPermission(definition, context);
        if (definition.getRiskLevel().requiresConfirmation()) {
            return prepareConfirmation(context, definition, plan, response);
        }

        long startedAt = System.currentTimeMillis();
        AgentToolResult result = executeReadTool(definition.getName(), plan.getArguments(), context);
        recordToolCall(context, definition, plan.getArguments(), result,
                result.isSuccess() ? "SUCCESS" : "FAILED",
                System.currentTimeMillis() - startedAt, null, null);
        appendToolResult(response, result);
        if ("llm".equals(plan.getPlanner()) && result.isSuccess()) {
            try {
                String composed = llmPlanner.composeAnswer(
                        message, mode, definition.getName(), result);
                if (composed != null && composed.trim().length() > 0) {
                    response.put("answer", composed);
                    response.put("answerPlanner", "llm");
                }
            } catch (AgentModelException ex) {
                LOGGER.warn("LLM answer composition failed; using tool summary: {}", ex.getMessage());
            }
        }
        return response;
    }

    public Map<String, Object> confirm(AgentConfirmRequest request, HttpSession session) {
        if (request == null || request.getToken() == null || request.getToken().trim().length() == 0) {
            throw new AgentException(400, "确认令牌不能为空");
        }
        String mode = normalizeMode(request.getMode());
        AgentToolContext context = createContext(mode, session, null, newTraceId());
        AgentConfirmationRecord record = confirmationService.claim(request.getToken(), context);
        if ("EXECUTED".equals(record.getStatus())) {
            return confirmationResult(record, true);
        }

        AgentToolDefinition definition = toolRegistry.getRequired(record.getToolName(), mode);
        assertToolPermission(definition, context);
        Map<String, Object> arguments = confirmationService.parseArguments(record);
        long startedAt = System.currentTimeMillis();

        try {
            AgentToolResult result = executeWriteTool(record.getToolName(), arguments, context);
            AgentToolCallRecord call = recordToolCall(
                    context,
                    definition,
                    arguments,
                    result,
                    result.isSuccess() ? "SUCCESS" : "FAILED",
                    System.currentTimeMillis() - startedAt,
                    record.getToken(),
                    record.getIdempotencyKey());
            record.setToolCallId(call == null ? null : call.getId());
            record.setResultSummary(result.getSummary());
            finishSafely(record, result.isSuccess() ? "EXECUTED" : "FAILED", result.getSummary());
            if (!result.isSuccess()) {
                throw new AgentException(409, result.getSummary());
            }
            return confirmationResult(record, false);
        } catch (AgentException ex) {
            record.setResultSummary(ex.getMessage());
            finishSafely(record, "FAILED", ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            LOGGER.error("Agent confirmed tool execution failed", ex);
            String message = "操作执行失败，未产生新的业务变更";
            record.setResultSummary(message);
            finishSafely(record, "FAILED", message);
            throw new AgentException(500, message);
        }
    }

    public List<AgentToolDefinition> listTools(String mode, HttpSession session) {
        String normalizedMode = normalizeMode(mode);
        if ("admin".equals(normalizedMode) && !(session.getAttribute("admin") instanceof Admin)) {
            throw new AgentException(403, "请先登录管理员后台");
        }
        return toolRegistry.list(normalizedMode);
    }

    private Map<String, Object> prepareConfirmation(AgentToolContext context,
                                                    AgentToolDefinition definition,
                                                    AgentPlan plan,
                                                    Map<String, Object> response) {
        Map<String, Object> arguments = new LinkedHashMap<>(plan.getArguments());
        String title;
        String impact;
        if ("cart.add".equals(definition.getName())) {
            requireCustomerLogin(context);
            AgentProductView product = resolveProduct(arguments);
            int quantity = integerValue(arguments.get("quantity"), 1);
            if (quantity < 1 || quantity > 10) {
                throw new AgentException(400, "单次加购数量需在 1 到 10 件之间");
            }
            validateCartStock(context.getUserId(), product, quantity);
            arguments.put("productId", product.getId());
            arguments.put("quantity", quantity);
            title = "确认加入购物车";
            impact = "将“" + product.getTitle() + "”加入你的购物车，数量 " + quantity
                    + " 件。当前库存 " + product.getStock() + " 件，本操作不会扣减库存。";
        } else if ("order.ship".equals(definition.getName())) {
            requireAdminLogin(context);
            int orderId = integerValue(arguments.get("orderId"), 0);
            Order order = orderId > 0 ? orderMapper.findById(orderId) : null;
            if (order == null) {
                throw new AgentException(404, "没有找到订单 #" + orderId);
            }
            if (!"待发货".equals(order.getZt())) {
                throw new AgentException(409, "订单 #" + orderId + " 当前状态为“" + order.getZt() + "”，只有待发货订单可以执行发货");
            }
            arguments.put("orderId", orderId);
            arguments.put("expectedStatus", "待发货");
            title = "确认订单发货";
            impact = "将订单 #" + orderId + "（编号 " + order.getBianhao() + "，金额 ¥"
                    + order.getZongjine() + "）的状态从“待发货”更新为“已发货”。";
        } else {
            throw new AgentException(400, "该工具不支持确认操作");
        }

        AgentConfirmationRecord confirmation = confirmationService.create(
                context, definition, arguments, title, impact);
        response.put("requiresConfirmation", true);
        response.put("answer", "我已核对实时数据并生成操作草案。请确认以下内容后再执行；确认令牌 10 分钟内有效。");
        response.put("confirmation", confirmationView(confirmation));
        response.put("arguments", arguments);
        return response;
    }

    private AgentToolResult executeReadTool(String toolName, Map<String, Object> arguments,
                                            AgentToolContext context) {
        switch (toolName) {
            case "product.search":
                return searchProducts(arguments);
            case "product.detail":
                return productDetail(arguments);
            case "order.mine":
                return myOrders(arguments, context);
            case "analytics.overview":
                return businessOverview(context);
            case "inventory.low_stock":
                return lowStock(arguments, context);
            case "sales.ranking":
                return salesRanking(arguments, context);
            default:
                return AgentToolResult.fail("暂不支持该工具");
        }
    }

    private AgentToolResult executeWriteTool(String toolName, Map<String, Object> arguments,
                                             AgentToolContext context) {
        if ("cart.add".equals(toolName)) {
            return addToCart(arguments, context);
        }
        if ("order.ship".equals(toolName)) {
            return shipOrder(arguments, context);
        }
        return AgentToolResult.fail("未知的写操作");
    }

    private AgentToolResult searchProducts(Map<String, Object> arguments) {
        String keyword = stringValue(arguments.get("keyword"));
        int limit = boundedInt(arguments.get("limit"), 6, 1, 20);
        List<AgentProductView> products = agentMapper.searchProducts(
                keyword,
                nullableInt(arguments.get("categoryId")),
                nullableInt(arguments.get("priceMin")),
                nullableInt(arguments.get("priceMax")),
                limit);
        if (products.isEmpty()) {
            return AgentToolResult.ok("没有找到完全匹配的装备，可以换一个运动类别或放宽预算试试。", products);
        }
        return AgentToolResult.ok("根据商城实时商品库找到 " + products.size()
                + " 件匹配装备，价格和库存均为当前数据。", products);
    }

    private AgentToolResult productDetail(Map<String, Object> arguments) {
        int productId = integerValue(arguments.get("productId"), 0);
        AgentProductView product = productId > 0 ? agentMapper.findProductView(productId) : null;
        if (product == null) {
            return AgentToolResult.fail("没有找到这件商品，请确认商品编号");
        }
        String stockText = product.getStock() > 0
                ? "当前库存 " + product.getStock() + " 件"
                : "当前暂时无货";
        return AgentToolResult.ok("“" + product.getTitle() + "”售价 ¥" + product.getPrice()
                + "，" + stockText + "。商品介绍和实时状态如下。", product);
    }

    private AgentToolResult myOrders(Map<String, Object> arguments, AgentToolContext context) {
        requireCustomerLogin(context);
        int orderId = integerValue(arguments.get("orderId"), 0);
        List<Order> orders = orderMapper.selectByUserId(context.getUserId());
        List<Map<String, Object>> views = new ArrayList<>();
        for (Order order : orders) {
            if (orderId > 0 && order.getId() != orderId) {
                continue;
            }
            Map<String, Object> view = new LinkedHashMap<>();
            view.put("id", order.getId());
            view.put("bianhao", order.getBianhao());
            view.put("xiadanshi", order.getXiadanshi());
            view.put("zongjine", order.getZongjine());
            view.put("fkzt", order.getFkzt());
            view.put("fkfs", order.getFkfs());
            view.put("zt", order.getZt());
            views.add(view);
        }
        if (orderId > 0 && views.isEmpty()) {
            return AgentToolResult.fail("没有在你的订单中找到 #" + orderId + "，请核对订单编号");
        }
        if (views.isEmpty()) {
            return AgentToolResult.ok("你目前还没有订单。选好装备后可以在购物车结算。", views);
        }
        return AgentToolResult.ok("已从你的账户实时查到 " + views.size()
                + " 条订单记录，仅展示当前登录用户的数据。", views);
    }

    private AgentToolResult businessOverview(AgentToolContext context) {
        requireAdminLogin(context);
        Map<String, Object> overview = agentMapper.businessOverview();
        return AgentToolResult.ok("经营概览已汇总商品、校园会员、订单、待发货和累计成交金额。", overview);
    }

    private AgentToolResult lowStock(Map<String, Object> arguments, AgentToolContext context) {
        requireAdminLogin(context);
        int threshold = boundedInt(arguments.get("threshold"), 95, 0, 100000);
        int limit = boundedInt(arguments.get("limit"), 8, 1, 30);
        List<Map<String, Object>> rows = agentMapper.lowStockProducts(threshold, limit);
        return AgentToolResult.ok("库存不高于 " + threshold + " 件的商品共找到 " + rows.size()
                + " 条，按库存从低到高排列。", rows);
    }

    private AgentToolResult salesRanking(Map<String, Object> arguments, AgentToolContext context) {
        requireAdminLogin(context);
        int limit = boundedInt(arguments.get("limit"), 6, 1, 30);
        List<Map<String, Object>> rows = agentMapper.salesRanking(limit);
        return AgentToolResult.ok("已按商品累计成交件数生成实时销量排行。", rows);
    }

    private AgentToolResult addToCart(Map<String, Object> arguments, AgentToolContext context) {
        requireCustomerLogin(context);
        int productId = integerValue(arguments.get("productId"), 0);
        int quantity = integerValue(arguments.get("quantity"), 0);
        AgentProductView product = agentMapper.findProductView(productId);
        if (product == null) {
            return AgentToolResult.fail("商品不存在或已经下架");
        }
        if (quantity < 1 || quantity > 10) {
            return AgentToolResult.fail("加购数量需在 1 到 10 件之间");
        }
        validateCartStock(context.getUserId(), product, quantity);

        Cart cart = new Cart();
        cart.setUserId(context.getUserId());
        cart.setProductId(productId);
        cart.setShuliang(quantity);
        cartService.cartJiaruProduct(cart, context.getUserId());
        return AgentToolResult.ok("已将“" + product.getTitle() + "”× " + quantity
                + " 加入购物车，请前往购物车确认后结算。", cart);
    }

    private AgentToolResult shipOrder(Map<String, Object> arguments, AgentToolContext context) {
        requireAdminLogin(context);
        int orderId = integerValue(arguments.get("orderId"), 0);
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            return AgentToolResult.fail("订单不存在");
        }
        if (!"待发货".equals(order.getZt())) {
            return AgentToolResult.fail("订单当前状态为“" + order.getZt() + "”，不能重复发货");
        }
        int rows = agentMapper.shipOrderIfPending(orderId);
        if (rows != 1) {
            return AgentToolResult.fail("订单状态已发生变化，请刷新后重试");
        }
        return AgentToolResult.ok("订单 #" + orderId + " 已标记为已发货。", orderId);
    }

    private AgentProductView resolveProduct(Map<String, Object> arguments) {
        int productId = integerValue(arguments.get("productId"), 0);
        if (productId > 0) {
            AgentProductView view = agentMapper.findProductView(productId);
            if (view == null) {
                throw new AgentException(404, "没有找到商品 #" + productId);
            }
            return view;
        }

        String keyword = stringValue(arguments.get("keyword"));
        List<AgentProductView> matches = agentMapper.searchProducts(keyword, null, null, null, 2);
        if (matches.isEmpty()) {
            throw new AgentException(404, "没有找到匹配的商品，请提供商品名称或编号");
        }
        if (matches.size() > 1) {
            throw new AgentException(409, "找到多件匹配商品，请提供商品编号后再加购，例如“把商品23加购2件”");
        }
        return matches.get(0);
    }

    private void validateCartStock(int userId, AgentProductView product, int quantity) {
        if (product.getStock() <= 0) {
            throw new AgentException(409, "该商品暂时无货");
        }
        Cart existing = cartMapper.selectByUserAndProduct(userId, product.getId());
        int existingQuantity = existing == null ? 0 : existing.getShuliang();
        if (existingQuantity + quantity > product.getStock()) {
            throw new AgentException(409, "库存不足。当前库存 " + product.getStock()
                    + " 件，购物车已有 " + existingQuantity + " 件");
        }
    }

    private AgentToolContext createContext(String mode, HttpSession session, String sessionId, String traceId) {
        User user = session.getAttribute("user") instanceof User
                ? (User) session.getAttribute("user") : null;
        int userId = user == null ? sessionInt(session, "uid") : user.getUid();
        String username = user == null ? sessionString(session, "username") : user.getUsername();
        Admin admin = session.getAttribute("admin") instanceof Admin
                ? (Admin) session.getAttribute("admin") : null;
        int adminId = admin == null ? 0 : admin.getId();
        String adminUsername = admin == null ? null : admin.getUsername();
        if ("admin".equals(mode) && admin == null) {
            throw new AgentException(403, "请先登录管理员后台");
        }
        return new AgentToolContext(
                traceId,
                sessionId == null ? cleanSessionId(null) : sessionId,
                mode,
                userId,
                username,
                adminId,
                adminUsername);
    }

    private void assertToolPermission(AgentToolDefinition definition, AgentToolContext context) {
        if ("admin".equals(definition.getChannel())) {
            requireAdminLogin(context);
        }
    }

    private void requireCustomerLogin(AgentToolContext context) {
        if (!context.isCustomerLoggedIn()) {
            throw new AgentException(401, "请先登录普通用户账号，再查询订单或使用加购功能");
        }
    }

    private void requireAdminLogin(AgentToolContext context) {
        if (!context.isAdminLoggedIn()) {
            throw new AgentException(403, "请先登录管理员后台");
        }
        if (!"admin".equals(context.getMode())) {
            throw new AgentException(403, "经营工具仅允许管理员通道调用");
        }
    }

    private Map<String, Object> baseResponse(AgentToolContext context, AgentPlan plan) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("traceId", context.getTraceId());
        response.put("sessionId", context.getSessionId());
        response.put("mode", context.getMode());
        response.put("planner", plan.getPlanner());
        response.put("provider", plan.getProvider());
        response.put("model", plan.getModelVersion());
        response.put("intent", plan.getIntent());
        response.put("suggestions", plan.getSuggestions());
        response.put("steps", buildSteps(plan));
        response.put("requiresConfirmation", false);
        return response;
    }

    private List<Map<String, String>> buildSteps(AgentPlan plan) {
        List<Map<String, String>> steps = new ArrayList<>();
        steps.add(step("llm".equals(plan.getPlanner()) ? "大模型理解需求" : "理解你的问题", "done"));
        if (plan.getDirectAnswer() == null) {
            steps.add(step("读取商城实时数据", "done"));
        }
        return steps;
    }

    private Map<String, String> step(String label, String status) {
        Map<String, String> step = new LinkedHashMap<>();
        step.put("label", label);
        step.put("status", status);
        return step;
    }

    private void appendToolResult(Map<String, Object> response, AgentToolResult result) {
        response.put("answer", result.getSummary());
        if (result.isSuccess()) {
            Object data = result.getData();
            if (data instanceof List && "product.search".equals(response.get("intent"))) {
                response.put("products", data);
            } else if (data instanceof List && "order.mine".equals(response.get("intent"))) {
                response.put("orders", data);
            } else if (data instanceof List) {
                response.put("items", data);
            } else {
                response.put("data", data);
            }
        } else {
            response.put("error", true);
        }
    }

    private Map<String, Object> confirmationView(AgentConfirmationRecord record) {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("token", record.getToken());
        view.put("toolName", record.getToolName());
        view.put("riskLevel", record.getRiskLevel());
        view.put("title", record.getTitle());
        view.put("impact", record.getImpact());
        view.put("expiresAt", record.getExpiresAt());
        return view;
    }

    private Map<String, Object> confirmationResult(AgentConfirmationRecord record, boolean alreadyExecuted) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("traceId", record.getTraceId());
        response.put("sessionId", record.getSessionId());
        response.put("intent", record.getToolName());
        response.put("requiresConfirmation", false);
        response.put("alreadyExecuted", alreadyExecuted);
        response.put("answer", alreadyExecuted
                ? "该操作此前已经执行，本次重复确认未再次执行。结果：" + record.getResultSummary()
                : record.getResultSummary());
        response.put("confirmationStatus", record.getStatus());
        return response;
    }

    private AgentToolCallRecord recordToolCall(AgentToolContext context,
                                               AgentToolDefinition definition,
                                               Map<String, Object> arguments,
                                               AgentToolResult result,
                                               String status,
                                               long latencyMs,
                                               String confirmationId,
                                               String idempotencyKey) {
        AgentToolCallRecord call = new AgentToolCallRecord();
        call.setTraceId(context.getTraceId());
        call.setSessionId(context.getSessionId());
        call.setActorType(context.getActorType());
        call.setActorId(context.getActorId());
        call.setActorName(context.getActorName());
        call.setToolName(definition.getName());
        call.setToolVersion(definition.getVersion());
        call.setRiskLevel(definition.getRiskLevel().getCode());
        call.setRequestSummary(limit(String.valueOf(arguments), 1000));
        call.setResponseSummary(limit(result.getSummary(), 1000));
        call.setStatus(status);
        call.setLatencyMs(latencyMs);
        call.setConfirmationId(confirmationId);
        call.setIdempotencyKey(idempotencyKey);
        try {
            auditMapper.insertToolCall(call);
            return call;
        } catch (RuntimeException ex) {
            LOGGER.warn("Agent audit write failed; business operation remains available", ex);
            return null;
        }
    }

    private void auditSession(AgentToolContext context) {
        try {
            AgentSessionRecord record = new AgentSessionRecord();
            record.setSessionId(context.getSessionId());
            record.setTraceId(context.getTraceId());
            record.setMode(context.getMode());
            record.setActorType(context.getActorType());
            record.setActorId(context.getActorId());
            record.setActorName(context.getActorName());
            auditMapper.upsertSession(record);
        } catch (RuntimeException ex) {
            LOGGER.warn("Agent session audit write failed; chat remains available", ex);
        }
    }

    private void finishSafely(AgentConfirmationRecord record, String status, String summary) {
        try {
            confirmationService.finish(record, status, summary);
        } catch (RuntimeException ex) {
            LOGGER.error("Agent confirmation finalization failed", ex);
        }
    }

    private String normalizeMode(String mode) {
        if (mode == null || mode.trim().length() == 0 || "customer".equalsIgnoreCase(mode.trim())) {
            return "customer";
        }
        if ("admin".equalsIgnoreCase(mode.trim())) {
            return "admin";
        }
        throw new AgentException(400, "未知的智能体通道");
    }

    private String cleanSessionId(String sessionId) {
        if (sessionId != null && sessionId.matches("[A-Za-z0-9_-]{8,64}")) {
            return sessionId;
        }
        return "ags_" + UUID.randomUUID().toString().replace("-", "");
    }

    private String newTraceId() {
        return "agt_" + UUID.randomUUID().toString().replace("-", "");
    }

    private int sessionInt(HttpSession session, String name) {
        Object value = session.getAttribute(name);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return value == null ? 0 : Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String sessionString(HttpSession session, String name) {
        Object value = session.getAttribute(name);
        return value == null ? null : value.toString();
    }

    private int integerValue(Object value, int fallback) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return value == null ? fallback : Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private Integer nullableInt(Object value) {
        if (value == null || value.toString().trim().length() == 0) {
            return null;
        }
        return integerValue(value, 0);
    }

    private int boundedInt(Object value, int fallback, int min, int max) {
        int parsed = integerValue(value, fallback);
        return Math.max(min, Math.min(max, parsed));
    }

    private String stringValue(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private String limit(String value, int max) {
        return value == null || value.length() <= max ? value : value.substring(0, max);
    }
}
