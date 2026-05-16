package com.foodmate.ai.recommendation.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class OpenAiLlmClient implements LlmClient {
    private final LlmProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public OpenAiLlmClient(LlmProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        int timeoutMillis = Math.max(properties.timeoutSeconds(), 1) * 1000;
        requestFactory.setConnectTimeout(timeoutMillis);
        requestFactory.setReadTimeout(timeoutMillis);
        this.restClient = RestClient.builder().requestFactory(requestFactory).build();
    }

    @Override
    public String chat(String systemPrompt, String userPrompt) {
        if (properties.apiKey() == null || properties.apiKey().isBlank()) {
            throw new BusinessException(ErrorCode.LLM_FAILED, "未配置 LLM_API_KEY，使用本地 fallback");
        }
        try {
            Map<String, Object> body = Map.of(
                    "model", properties.model(),
                    "response_format", Map.of("type", "json_object"),
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userPrompt)
                    )
            );
            String response = restClient.post()
                    .uri(properties.baseUrl())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.apiKey())
                    .body(body)
                    .retrieve()
                    .body(String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.LLM_FAILED, "大模型调用失败，使用本地 fallback");
        }
    }
}
