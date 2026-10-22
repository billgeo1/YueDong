package com.xiangmu.agent;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiangmu.agent.record.AgentConfirmationRecord;
import com.xiangmu.mappers.AgentAuditMapper;

@Service
public class AgentConfirmationService {
    private static final long EXPIRES_IN_MILLIS = 10 * 60 * 1000L;

    @Autowired
    private AgentAuditMapper auditMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public AgentConfirmationRecord create(AgentToolContext context,
                                          AgentToolDefinition definition,
                                          Map<String, Object> arguments,
                                          String title,
                                          String impact) {
        AgentConfirmationRecord record = new AgentConfirmationRecord();
        record.setToken(randomToken());
        record.setTraceId(context.getTraceId());
        record.setSessionId(context.getSessionId());
        record.setActorType(context.getActorType());
        record.setActorId(context.getActorId());
        record.setActorName(context.getActorName());
        record.setToolName(definition.getName());
        record.setRiskLevel(definition.getRiskLevel().getCode());
        record.setArgumentsJson(toJson(arguments));
        record.setTitle(limit(title, 200));
        record.setImpact(limit(impact, 1000));
        record.setStatus("PENDING");
        record.setIdempotencyKey(createIdempotencyKey(context, definition, arguments));
        record.setExpiresAt(new Date(System.currentTimeMillis() + EXPIRES_IN_MILLIS));

        try {
            auditMapper.insertConfirmation(record);
        } catch (RuntimeException ex) {
            throw new AgentException(503, "确认服务暂时不可用，请稍后重试");
        }
        return record;
    }

    public AgentConfirmationRecord claim(String token, AgentToolContext context) {
        if (token == null || token.trim().length() == 0) {
            throw new AgentException(400, "确认令牌不能为空");
        }
        AgentConfirmationRecord record = auditMapper.findConfirmationByToken(token.trim());
        if (record == null) {
            throw new AgentException(404, "确认请求不存在");
        }
        assertActor(record, context);

        if ("EXECUTED".equals(record.getStatus())) {
            return record;
        }
        if ("EXECUTING".equals(record.getStatus())) {
            throw new AgentException(409, "该操作正在执行，请勿重复提交");
        }
        if (!"PENDING".equals(record.getStatus())) {
            throw new AgentException(409, "该确认请求已失效，请重新发起操作");
        }
        if (record.getExpiresAt() != null && record.getExpiresAt().before(new Date())) {
            throw new AgentException(410, "确认请求已过期，请重新发起操作");
        }

        int claimed = auditMapper.claimConfirmation(record.getToken(), context.getActorType(), context.getActorId());
        if (claimed == 1) {
            record.setStatus("EXECUTING");
            return record;
        }

        AgentConfirmationRecord latest = auditMapper.findConfirmationByToken(record.getToken());
        if (latest != null && "EXECUTED".equals(latest.getStatus())) {
            return latest;
        }
        throw new AgentException(409, "确认请求已被处理，请勿重复提交");
    }

    public void finish(AgentConfirmationRecord record, String status, String summary) {
        auditMapper.finishConfirmation(
                record.getToken(),
                status,
                record.getToolCallId(),
                limit(summary, 1000));
    }

    public Map<String, Object> parseArguments(AgentConfirmationRecord record) {
        try {
            return objectMapper.readValue(record.getArgumentsJson(), Map.class);
        } catch (Exception ex) {
            throw new AgentException(500, "确认参数解析失败，请重新发起操作");
        }
    }

    private void assertActor(AgentConfirmationRecord record, AgentToolContext context) {
        if (!context.getActorType().equals(record.getActorType())
                || context.getActorId() != record.getActorId()) {
            throw new AgentException(403, "确认请求与当前登录身份不匹配");
        }
    }

    private String toJson(Map<String, Object> arguments) {
        try {
            return objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException ex) {
            throw new AgentException(500, "无法生成确认参数");
        }
    }

    private String createIdempotencyKey(AgentToolContext context,
                                        AgentToolDefinition definition,
                                        Map<String, Object> arguments) {
        String raw = context.getTraceId() + "|" + context.getActorType() + "|" + context.getActorId()
                + "|" + definition.getName() + "|" + toJson(arguments);
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder("agent_");
            for (int index = 0; index < 20; index++) {
                builder.append(String.format("%02x", digest[index]));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            return "agent_" + UUID.randomUUID().toString().replace("-", "");
        }
    }

    private String randomToken() {
        return UUID.randomUUID().toString().replace("-", "")
                + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String limit(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
