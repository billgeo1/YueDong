package com.xiangmu.agent;

public class AgentToolResult {
    private final boolean success;
    private final String summary;
    private final Object data;

    private AgentToolResult(boolean success, String summary, Object data) {
        this.success = success;
        this.summary = summary;
        this.data = data;
    }

    public static AgentToolResult ok(String summary, Object data) {
        return new AgentToolResult(true, summary, data);
    }

    public static AgentToolResult fail(String summary) {
        return new AgentToolResult(false, summary, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSummary() {
        return summary;
    }

    public Object getData() {
        return data;
    }
}
