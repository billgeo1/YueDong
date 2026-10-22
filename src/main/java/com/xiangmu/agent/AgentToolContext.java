package com.xiangmu.agent;

public class AgentToolContext {
    private final String traceId;
    private final String sessionId;
    private final String mode;
    private final int userId;
    private final String username;
    private final int adminId;
    private final String adminUsername;

    public AgentToolContext(String traceId, String sessionId, String mode, int userId, String username,
                            int adminId, String adminUsername) {
        this.traceId = traceId;
        this.sessionId = sessionId;
        this.mode = mode;
        this.userId = userId;
        this.username = username;
        this.adminId = adminId;
        this.adminUsername = adminUsername;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMode() {
        return mode;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public boolean isCustomerLoggedIn() {
        return userId > 0;
    }

    public boolean isAdminLoggedIn() {
        return adminId > 0;
    }

    public String getActorType() {
        return "admin".equals(mode) ? "ADMIN" : "USER";
    }

    public int getActorId() {
        return "admin".equals(mode) ? adminId : userId;
    }

    public String getActorName() {
        return "admin".equals(mode) ? adminUsername : username;
    }
}
