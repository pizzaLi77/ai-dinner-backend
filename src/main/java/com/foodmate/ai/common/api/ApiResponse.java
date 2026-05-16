package com.foodmate.ai.common.api;

public record ApiResponse<T>(Integer code, String message, T data) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode.getCode(), message == null ? errorCode.getMessage() : message, null);
    }
}
