package com.foodmate.ai.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth")
public record AuthProperties(String secret, long tokenTtlSeconds) {
}
