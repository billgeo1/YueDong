package com.xiangmu.agent;

public class AgentModelException extends RuntimeException {
    public AgentModelException(String message) {
        super(message);
    }

    public AgentModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
