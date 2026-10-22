package com.xiangmu.agent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AgentLlmClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentLlmClient.class);

    private final ObjectMapper objectMapper;
    private final boolean enabled;
    private final String baseUrl;
    private final String apiKey;
    private final String model;
    private final int maxTokens;
    private final RestTemplate restTemplate;

    public AgentLlmClient(ObjectMapper objectMapper,
                          @Value("${agent.llm.enabled:false}") boolean enabled,
                          @Value("${agent.llm.base-url:https://api.deepseek.com}") String baseUrl,
                          @Value("${agent.llm.api-key:}") String apiKey,
                          @Value("${agent.llm.model:deepseek-chat}") String model,
                          @Value("${agent.llm.timeout-ms:12000}") int timeoutMs,
                          @Value("${agent.llm.max-tokens:700}") int maxTokens) {
        this.objectMapper = objectMapper;
        this.enabled = enabled;
        this.baseUrl = trimTrailingSlash(baseUrl);
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.model = model == null || model.trim().length() == 0 ? "deepseek-chat" : model.trim();
        this.maxTokens = Math.max(128, Math.min(maxTokens, 4096));

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        int boundedTimeout = Math.max(1000, Math.min(timeoutMs, 60000));
        requestFactory.setConnectTimeout(boundedTimeout);
        requestFactory.setReadTimeout(boundedTimeout);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    public boolean isReady() {
        return enabled
                && baseUrl.length() > 0
                && (apiKey.length() > 0 || isLocalEndpoint());
    }

    public String getModelName() {
        return model;
    }

    public String getProviderLabel() {
        String value = baseUrl.toLowerCase();
        if (value.contains("deepseek")) {
            return "DeepSeek";
        }
        if (value.contains("openai")) {
            return "OpenAI";
        }
        if (value.contains("localhost") || value.contains("127.0.0.1")) {
            return "本地模型";
        }
        return "OpenAI 兼容模型";
    }

    public ModelTurn complete(List<Map<String, Object>> messages,
                              List<Map<String, Object>> tools) {
        if (!isReady()) {
            throw new AgentModelException("大模型尚未配置");
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("temperature", tools.isEmpty() ? 0.35 : 0.15);
        body.put("max_tokens", maxTokens);
        if (!tools.isEmpty()) {
            body.put("tools", tools);
            body.put("tool_choice", "auto");
        }

        JsonNode root = post(body);
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.size() == 0) {
            throw new AgentModelException(readProviderError(root, "大模型没有返回候选结果"));
        }

        JsonNode message = choices.get(0).path("message");
        String content = message.path("content").isMissingNode()
                ? "" : message.path("content").asText("").trim();
        JsonNode toolCalls = message.path("tool_calls");
        if (!toolCalls.isArray() || toolCalls.size() == 0) {
            return new ModelTurn(content, null, new LinkedHashMap<String, Object>());
        }

        JsonNode function = toolCalls.get(0).path("function");
        String toolName = function.path("name").asText("").trim();
        if (toolName.length() == 0) {
            throw new AgentModelException("大模型返回了无效的工具调用");
        }

        Map<String, Object> arguments = new LinkedHashMap<>();
        JsonNode rawArguments = function.path("arguments");
        try {
            if (rawArguments.isObject()) {
                arguments = objectMapper.convertValue(
                        rawArguments, new TypeReference<LinkedHashMap<String, Object>>() { });
            } else if (rawArguments.isTextual() && rawArguments.asText().trim().length() > 0) {
                arguments = objectMapper.readValue(
                        rawArguments.asText(), new TypeReference<LinkedHashMap<String, Object>>() { });
            }
        } catch (Exception ex) {
            throw new AgentModelException("大模型生成的工具参数不是有效 JSON", ex);
        }
        return new ModelTurn(content, toolName, arguments);
    }

    private JsonNode post(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
        if (apiKey.length() > 0) {
            headers.setBearerAuth(apiKey);
        }

        try {
            JsonNode response = restTemplate.postForObject(
                    completionUrl(), new HttpEntity<Map<String, Object>>(body, headers), JsonNode.class);
            if (response == null) {
                throw new AgentModelException("大模型服务返回了空响应");
            }
            return response;
        } catch (RestClientResponseException ex) {
            LOGGER.warn("LLM API returned HTTP {}", ex.getRawStatusCode());
            throw new AgentModelException("大模型服务请求失败（HTTP " + ex.getRawStatusCode() + "）", ex);
        } catch (AgentModelException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            LOGGER.warn("LLM API request failed: {}", ex.getMessage());
            throw new AgentModelException("无法连接大模型服务", ex);
        }
    }

    private String completionUrl() {
        if (baseUrl.endsWith("/chat/completions")) {
            return baseUrl;
        }
        return baseUrl + "/chat/completions";
    }

    private String readProviderError(JsonNode root, String fallback) {
        String message = root.path("error").path("message").asText("").trim();
        return message.length() == 0 ? fallback : fallback + "：" + limit(message, 160);
    }

    private boolean isLocalEndpoint() {
        return baseUrl.contains("localhost") || baseUrl.contains("127.0.0.1");
    }

    private String trimTrailingSlash(String value) {
        String result = value == null ? "" : value.trim();
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String limit(String value, int max) {
        return value == null || value.length() <= max ? value : value.substring(0, max);
    }

    public static class ModelTurn {
        private final String content;
        private final String toolName;
        private final Map<String, Object> arguments;

        public ModelTurn(String content, String toolName, Map<String, Object> arguments) {
            this.content = content == null ? "" : content;
            this.toolName = toolName;
            this.arguments = arguments == null
                    ? new LinkedHashMap<String, Object>() : arguments;
        }

        public boolean hasToolCall() {
            return toolName != null && toolName.length() > 0;
        }

        public String getContent() {
            return content;
        }

        public String getToolName() {
            return toolName;
        }

        public Map<String, Object> getArguments() {
            return arguments;
        }
    }
}
