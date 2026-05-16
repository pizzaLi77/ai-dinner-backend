package com.foodmate.ai.common.exception;

import com.foodmate.ai.common.api.ApiResponse;
import com.foodmate.ai.common.api.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> handleBusiness(BusinessException exception) {
        return ApiResponse.error(exception.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> handleValidation(Exception exception) {
        return ApiResponse.error(ErrorCode.INVALID_INPUT, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> handleUnexpected(Exception exception) {
        return ApiResponse.error(ErrorCode.DB_FAILED, "服务暂时不可用，请稍后再试");
    }
}
