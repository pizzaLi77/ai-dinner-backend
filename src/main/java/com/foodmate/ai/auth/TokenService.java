package com.foodmate.ai.auth;

import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class TokenService {
    private final AuthProperties properties;

    public TokenService(AuthProperties properties) {
        this.properties = properties;
    }

    public String issue(Long userId) {
        long expiresAt = Instant.now().getEpochSecond() + properties.tokenTtlSeconds();
        String payload = userId + ":" + expiresAt;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8))
                + "." + sign(payload);
    }

    public Long verify(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        String token = bearerToken.substring("Bearer ".length()).trim();
        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        if (!sign(payload).equals(parts[1])) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        String[] values = payload.split(":");
        if (values.length != 2 || Long.parseLong(values[1]) < Instant.now().getEpochSecond()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return Long.parseLong(values[0]);
    }

    private String sign(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(properties.secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to sign token", exception);
        }
    }
}
