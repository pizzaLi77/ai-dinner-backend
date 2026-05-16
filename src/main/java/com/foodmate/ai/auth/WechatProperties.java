package com.foodmate.ai.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.wechat")
public record WechatProperties(String appId, String appSecret) {
}
