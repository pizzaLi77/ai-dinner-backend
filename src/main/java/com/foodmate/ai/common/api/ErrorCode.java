package com.foodmate.ai.common.api;

public enum ErrorCode {
    SUCCESS(0, "success"),
    UNAUTHORIZED(40001, "登录已过期"),
    INVALID_INPUT(40002, "参数不合法"),
    PROFILE_NOT_FOUND(40003, "用户画像不存在"),
    LLM_FAILED(50001, "大模型调用失败"),
    LLM_INVALID_OUTPUT(50002, "大模型输出格式错误"),
    DB_FAILED(50003, "数据库操作失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
