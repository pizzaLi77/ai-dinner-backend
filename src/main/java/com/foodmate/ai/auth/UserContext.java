package com.foodmate.ai.auth;

import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;

public final class UserContext {
    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();

    private UserContext() {
    }

    public static void setUserId(Long userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static Long requireUserId() {
        Long userId = CURRENT_USER_ID.get();
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return userId;
    }

    public static void clear() {
        CURRENT_USER_ID.remove();
    }
}
