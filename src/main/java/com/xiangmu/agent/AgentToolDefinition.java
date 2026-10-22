package com.xiangmu.agent;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AgentToolDefinition {
    private final String name;
    private final String version;
    private final String description;
    private final RiskLevel riskLevel;
    private final String channel;
    private final Map<String, Object> inputSchema;

    public AgentToolDefinition(String name, String version, String description, RiskLevel riskLevel,
                               String channel, Map<String, Object> inputSchema) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.riskLevel = riskLevel;
        this.channel = channel;
        this.inputSchema = inputSchema == null
                ? Collections.<String, Object>emptyMap() : new LinkedHashMap<>(inputSchema);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getChannel() {
        return channel;
    }

    public Map<String, Object> getInputSchema() {
        return inputSchema;
    }
}
