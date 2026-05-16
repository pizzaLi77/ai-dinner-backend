package com.foodmate.ai.auth;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class, WechatProperties.class})
public class AuthConfig {
}
