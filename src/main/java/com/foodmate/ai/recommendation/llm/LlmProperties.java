package com.foodmate.ai.recommendation.llm;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.llm")
public record LlmProperties(String provider, String model, String apiKey, String baseUrl,
                            int timeoutSeconds, String promptVersion) {
}
