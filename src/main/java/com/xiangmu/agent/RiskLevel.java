package com.xiangmu.agent;

public enum RiskLevel {
    READ("read", false),
    WRITE_LOW("write_low", true),
    WRITE_HIGH("write_high", true);

    private final String code;
    private final boolean requiresConfirmation;

    RiskLevel(String code, boolean requiresConfirmation) {
        this.code = code;
        this.requiresConfirmation = requiresConfirmation;
    }

    public String getCode() {
        return code;
    }

    public boolean requiresConfirmation() {
        return requiresConfirmation;
    }
}
