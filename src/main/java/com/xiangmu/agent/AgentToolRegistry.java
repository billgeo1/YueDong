package com.xiangmu.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AgentToolRegistry {
    private final Map<String, AgentToolDefinition> tools = new LinkedHashMap<>();

    public AgentToolRegistry() {
        register(new AgentToolDefinition("product.search", "1.0",
                "按关键词、运动类别和预算检索在售体育用品", RiskLevel.READ, "customer",
                schema(
                        property("keyword", "string", "商品或运动关键词，例如篮球、羽毛球拍"),
                        property("categoryId", "integer", "分类编号：26=篮球/足球，27=网球，28=乒乓球，29=羽毛球",
                                "enum", Arrays.asList(26, 27, 28, 29)),
                        property("priceMin", "integer", "最低价格（元）", "minimum", 0),
                        property("priceMax", "integer", "最高价格（元）", "minimum", 0),
                        property("limit", "integer", "返回数量，默认6", "minimum", 1, "maximum", 20))));
        register(new AgentToolDefinition("product.detail", "1.0",
                "查询指定商品的介绍、价格和实时库存", RiskLevel.READ, "customer",
                schema(property("productId", "integer", "商城商品编号", "minimum", 1),
                        "productId")));
        register(new AgentToolDefinition("order.mine", "1.0",
                "查询当前登录用户本人的订单，只能返回本人数据", RiskLevel.READ, "customer",
                schema(property("orderId", "integer", "可选，指定订单编号", "minimum", 1))));
        register(new AgentToolDefinition("cart.add", "1.0",
                "生成将指定商品加入当前用户购物车的确认草案，确认后才会执行",
                RiskLevel.WRITE_LOW, "customer",
                schema(
                        property("productId", "integer", "商城商品编号", "minimum", 1),
                        property("keyword", "string", "不知道商品编号时可提供准确商品名称"),
                        property("quantity", "integer", "加购数量，默认1，最多10",
                                "minimum", 1, "maximum", 10))));
        register(new AgentToolDefinition("analytics.overview", "1.0",
                "汇总商品、用户、订单、待发货和累计成交金额", RiskLevel.READ, "admin",
                schema()));
        register(new AgentToolDefinition("inventory.low_stock", "1.0",
                "查询库存不高于指定阈值的商品", RiskLevel.READ, "admin",
                schema(
                        property("threshold", "integer", "库存阈值，默认95", "minimum", 0, "maximum", 100000),
                        property("limit", "integer", "返回数量，默认8", "minimum", 1, "maximum", 30))));
        register(new AgentToolDefinition("sales.ranking", "1.0",
                "按商品累计销量生成经营排行", RiskLevel.READ, "admin",
                schema(property("limit", "integer", "返回数量，默认6", "minimum", 1, "maximum", 30))));
        register(new AgentToolDefinition("order.ship", "1.0",
                "为待发货订单生成发货确认草案，确认后才会更新状态", RiskLevel.WRITE_HIGH, "admin",
                schema(property("orderId", "integer", "要发货的订单编号", "minimum", 1),
                        "orderId")));
    }

    private void register(AgentToolDefinition definition) {
        tools.put(definition.getName(), definition);
    }

    public AgentToolDefinition getRequired(String toolName, String mode) {
        AgentToolDefinition definition = tools.get(fromModelToolName(toolName));
        if (definition == null) {
            throw new IllegalArgumentException("未注册的工具：" + toolName);
        }
        if (!definition.getChannel().equals(mode)) {
            throw new SecurityException("当前 Agent 无权调用该工具");
        }
        return definition;
    }

    public List<AgentToolDefinition> list(String mode) {
        List<AgentToolDefinition> result = new ArrayList<>();
        for (AgentToolDefinition definition : tools.values()) {
            if (Arrays.asList("customer", "admin").contains(mode) && definition.getChannel().equals(mode)) {
                result.add(definition);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public String resolveAllowedName(String modelToolName, String mode) {
        AgentToolDefinition definition = tools.get(fromModelToolName(modelToolName));
        if (definition == null || !definition.getChannel().equals(mode)) {
            return null;
        }
        return definition.getName();
    }

    public List<Map<String, Object>> openAiTools(String mode) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (AgentToolDefinition definition : list(mode)) {
            Map<String, Object> function = new LinkedHashMap<>();
            function.put("name", toModelToolName(definition.getName()));
            function.put("description", definition.getDescription());
            function.put("parameters", definition.getInputSchema());

            Map<String, Object> tool = new LinkedHashMap<>();
            tool.put("type", "function");
            tool.put("function", function);
            result.add(tool);
        }
        return result;
    }

    private Map<String, Object> schema(Object... entries) {
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();
        for (Object entry : entries) {
            if (entry instanceof String) {
                required.add((String) entry);
            } else if (entry instanceof Map) {
                addProperty(properties, entry);
            }
        }
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", required);
        schema.put("additionalProperties", false);
        return schema;
    }

    @SuppressWarnings("unchecked")
    private void addProperty(Map<String, Object> target, Object rawEntry) {
        Map<String, Object> entry = (Map<String, Object>) rawEntry;
        if (entry.containsKey("__property")) {
            String name = String.valueOf(entry.remove("__property"));
            target.put(name, entry);
        }
    }

    private Map<String, Object> property(String name, String type, String description, Object... extras) {
        Map<String, Object> property = new LinkedHashMap<>();
        property.put("__property", name);
        property.put("type", type);
        property.put("description", description);
        for (int i = 0; i + 1 < extras.length; i += 2) {
            property.put(String.valueOf(extras[i]), extras[i + 1]);
        }
        return property;
    }

    private String toModelToolName(String name) {
        return name == null ? "" : name.replace('.', '_');
    }

    private String fromModelToolName(String name) {
        if (name == null) {
            return "";
        }
        for (String registered : tools.keySet()) {
            if (registered.equals(name) || toModelToolName(registered).equals(name)) {
                return registered;
            }
        }
        return name;
    }
}
