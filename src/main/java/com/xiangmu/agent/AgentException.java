package com.xiangmu.agent;

public class AgentException extends RuntimeException {
    private final int state;

    public AgentException(int state, String message) {
        super(message);
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
