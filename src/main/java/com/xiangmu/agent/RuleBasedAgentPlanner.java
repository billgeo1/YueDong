package com.xiangmu.agent;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class RuleBasedAgentPlanner {
    private static final Pattern RANGE_PATTERN = Pattern.compile(
            "(\\d+)\\s*(?:元)?\\s*(?:到|至|-|~|—)\\s*(\\d+)\\s*(?:元)?");
    private static final Pattern MAX_PRICE_PATTERN = Pattern.compile(
            "(?:预算|不超过|低于|最多|价格)\\s*[¥￥]?\\s*(\\d+)");
    private static final Pattern PRICE_UNDER_PATTERN = Pattern.compile(
            "(\\d+)\\s*(?:元)?\\s*(?:以内|以下|之内)");
    private static final Pattern QUANTITY_PATTERN = Pattern.compile(
            "(\\d+)\\s*(?:件|个|双|支|只|把|盒)");
    private static final Pattern PRODUCT_ID_PATTERN = Pattern.compile(
            "(?:商品|编号|id|ID|#)\\s*#?(\\d+)");
    private static final Pattern ORDER_ID_PATTERN = Pattern.compile(
            "(?:订单|编号|id|ID|#)\\s*#?(\\d+)");

    public AgentPlan plan(String message, String mode) {
        String text = message == null ? "" : message.trim();
        AgentPlan plan = new AgentPlan();
        if (text.length() == 0) {
            throw new AgentException(400, "请输入你想咨询的内容");
        }
        if (text.length() > 300) {
            text = text.substring(0, 300);
        }

        if ("admin".equals(mode)) {
            return planAdmin(text, plan);
        }
        return planCustomer(text, plan);
    }

    private AgentPlan planCustomer(String text, AgentPlan plan) {
        plan.getSuggestions().addAll(Arrays.asList(
                "200元以内篮球装备", "查一下我的订单", "把商品23加购2件", "羽毛球拍怎么选"));

        if (containsAny(text, "帮助", "能做什么", "怎么用", "使用说明")) {
            plan.setIntent("agent.help");
            plan.setDirectAnswer("我可以基于商城实时数据帮你检索体育用品、比较价格和库存、解释商品卖点、查询本人订单，"
                    + "也可以先生成加购草案，确认后再写入购物车。涉及写操作时，我不会自动执行。");
            return plan;
        }

        if (containsAny(text, "怎么选", "如何选择", "选购建议", "购买建议", "适合什么")) {
            plan.setIntent("selection.advice");
            plan.setDirectAnswer(buildSelectionAdvice(text));
            return plan;
        }

        if (containsAny(text, "加入购物车", "加购", "放进购物车", "放进购物袋")) {
            plan.setIntent("cart.add");
            plan.setToolName("cart.add");
            int quantity = firstNumber(QUANTITY_PATTERN, text, 1);
            int productId = firstNumber(PRODUCT_ID_PATTERN, text, 0);
            if (quantity < 1 || quantity > 10) {
                quantity = 1;
            }
            plan.getArguments().put("quantity", quantity);
            if (productId > 0) {
                plan.getArguments().put("productId", productId);
            }
            String keyword = extractSearchKeyword(text, null);
            if (productId <= 0 && keyword.length() > 0) {
                plan.getArguments().put("keyword", keyword);
            }
            return plan;
        }

        if (containsAny(text, "我的订单", "订单状态", "查订单", "查询订单", "物流", "到哪了", "发货了吗")) {
            plan.setIntent("order.mine");
            plan.setToolName("order.mine");
            int orderId = firstNumber(ORDER_ID_PATTERN, text, 0);
            if (orderId > 0) {
                plan.getArguments().put("orderId", orderId);
            }
            return plan;
        }

        Integer categoryId = resolveCategory(text);
        int productId = firstNumber(PRODUCT_ID_PATTERN, text, 0);
        if (productId > 0 && containsAny(text, "详情", "介绍", "怎么样", "库存", "多少钱", "价格")) {
            plan.setIntent("product.detail");
            plan.setToolName("product.detail");
            plan.getArguments().put("productId", productId);
            return plan;
        }

        plan.setIntent("product.search");
        plan.setToolName("product.search");
        plan.getArguments().put("keyword", extractSearchKeyword(text, categoryId));
        if (categoryId != null) {
            plan.getArguments().put("categoryId", categoryId);
        }
        applyBudget(text, plan.getArguments());
        plan.getArguments().put("limit", 6);
        return plan;
    }

    private AgentPlan planAdmin(String text, AgentPlan plan) {
        plan.getSuggestions().addAll(Arrays.asList(
                "给我一份经营概览", "库存低于95的商品", "销量最好的商品", "把订单32发货"));

        if (containsAny(text, "帮助", "能做什么", "怎么用", "使用说明")) {
            plan.setIntent("agent.help");
            plan.setDirectAnswer("我可以汇总商品、用户、订单和成交金额，分析低库存与销量排行，并生成发货草案。"
                    + "发货属于高风险写操作，必须由你确认后才会执行。");
            return plan;
        }

        if (containsAny(text, "发货", "标记已发", "处理订单")) {
            plan.setIntent("order.ship");
            plan.setToolName("order.ship");
            int orderId = firstPositiveOrderId(text);
            if (orderId <= 0) {
                throw new AgentException(400, "请提供要发货的订单编号，例如“把订单32发货”");
            }
            plan.getArguments().put("orderId", orderId);
            return plan;
        }

        if (containsAny(text, "低库存", "库存预警", "缺货", "补货", "库存紧张")) {
            plan.setIntent("inventory.low_stock");
            plan.setToolName("inventory.low_stock");
            plan.getArguments().put("threshold", firstNumber(
                    Pattern.compile("(?:低于|库存|阈值)\\s*(\\d+)"), text, 95));
            plan.getArguments().put("limit", 8);
            return plan;
        }

        if (containsAny(text, "销量", "热销", "畅销", "卖得好", "排行")) {
            plan.setIntent("sales.ranking");
            plan.setToolName("sales.ranking");
            plan.getArguments().put("limit", 6);
            return plan;
        }

        plan.setIntent("analytics.overview");
        plan.setToolName("analytics.overview");
        return plan;
    }

    private void applyBudget(String text, java.util.Map<String, Object> arguments) {
        Matcher range = RANGE_PATTERN.matcher(text);
        if (range.find()) {
            int min = Integer.parseInt(range.group(1));
            int max = Integer.parseInt(range.group(2));
            arguments.put("priceMin", Math.min(min, max));
            arguments.put("priceMax", Math.max(min, max));
            return;
        }

        Matcher maxPrice = MAX_PRICE_PATTERN.matcher(text);
        if (maxPrice.find()) {
            arguments.put("priceMax", Integer.parseInt(maxPrice.group(1)));
            return;
        }

        Matcher priceUnder = PRICE_UNDER_PATTERN.matcher(text);
        if (priceUnder.find()) {
            arguments.put("priceMax", Integer.parseInt(priceUnder.group(1)));
        }
    }

    private String buildSelectionAdvice(String text) {
        if (containsAny(text, "篮球", "足球")) {
            return "选篮球或足球时，先看使用场景：校园室外训练优先耐磨 PU/PVC 材质，室内比赛更重视抓地与手感。"
                    + "篮球常用 7 号，足球常用 5 号；预算有限可先保证材质和场地的匹配，再看品牌。"
                    + "你也可以直接告诉我预算，我会从商城实时商品中筛选。";
        }
        if (containsAny(text, "网球")) {
            return "网球入门先分清楚是单人回弹训练还是对打：校园自学可选带线回弹训练器，"
                    + "有搭档对打则优先考虑拍面大小、拍重和是否已穿线。初学者通常不需要追求高磅数。"
                    + "告诉我预算和训练方式，我可以继续筛选。";
        }
        if (containsAny(text, "乒乓")) {
            return "乒乓球拍入门先看握法（横拍或直拍）和胶面类型。零基础训练可选已粘好的成品拍，"
                    + "更重视容错和耐用；预算提高后再考虑底板与胶皮的组合。把预算告诉我，我可以查实时商品。";
        }
        if (containsAny(text, "羽毛球")) {
            return "选羽毛球拍先看重量与平衡点：入门和双打更常用较轻、易挥动的 3U/4U 球拍，"
                    + "攻击型球拍通常更重、拍头更偏。校园日常训练要确认是否已穿线，并优先选择耐用材质。"
                    + "告诉我预算，我可以从商城中筛选。";
        }
        return "选体育用品先确定运动项目、训练频率和预算，再优先关注材质、尺寸和耐用性。"
                + "你可以这样问我：“200元以内篮球装备”“商品23还有库存吗”或“把商品23加购2件”。";
    }

    private String extractSearchKeyword(String text, Integer categoryId) {
        String keyword = text;
        String[] removable = {
                "请帮我", "帮我", "我想要", "我想", "想买", "想找", "推荐一下", "推荐", "找一下", "找找",
                "一下", "有没有", "有哪些", "看看", "查询", "查", "搜索", "买", "适合大学生", "适合学生",
                "大学生", "学生", "体育用品", "运动装备", "装备", "商品", "编号", "价格", "多少钱",
                "库存", "详情", "介绍", "怎么样", "加入购物车", "加购", "放进购物车", "放进购物袋",
                "预算", "不超过", "低于", "最多", "以内", "以下", "之内", "元左右", "元"
        };
        for (String word : removable) {
            keyword = keyword.replace(word, " ");
        }
        keyword = keyword.replaceAll("(?i)\\b(?:id|商品|订单|#)\\s*\\d+", " ");
        keyword = keyword.replaceAll("\\d+\\s*(?:件|个|双|支|只|把|盒)", " ");
        keyword = keyword.replaceAll("[0-9¥￥,，。.!！?？:：;；/、\\-—~]+", " ");
        keyword = keyword.replaceAll("\\s+", " ").trim();
        if (keyword.length() > 40) {
            keyword = keyword.substring(0, 40).trim();
        }
        if (keyword.length() == 0 && categoryId != null) {
            return categoryName(categoryId);
        }
        return keyword;
    }

    private Integer resolveCategory(String text) {
        if (containsAny(text, "篮球", "足球", "球场")) {
            return 26;
        }
        if (containsAny(text, "网球")) {
            return 27;
        }
        if (containsAny(text, "乒乓", "兵乓")) {
            return 28;
        }
        if (containsAny(text, "羽毛球", "羽拍")) {
            return 29;
        }
        return null;
    }

    private String categoryName(int categoryId) {
        switch (categoryId) {
            case 26:
                return "篮球 足球";
            case 27:
                return "网球";
            case 28:
                return "乒乓球";
            case 29:
                return "羽毛球";
            default:
                return "";
        }
    }

    private int firstPositiveOrderId(String text) {
        int orderId = firstNumber(ORDER_ID_PATTERN, text, 0);
        if (orderId > 0) {
            return orderId;
        }
        Matcher matcher = Pattern.compile("(?:发货|订单)\\s*#?(\\d+)").matcher(text);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    private int firstNumber(Pattern pattern, String text, int fallback) {
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find()) {
            return fallback;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    private boolean containsAny(String text, String... words) {
        for (String word : words) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
