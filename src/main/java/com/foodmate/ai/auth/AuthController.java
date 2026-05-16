package com.foodmate.ai.auth;

import com.foodmate.ai.auth.dto.LoginResponse;
import com.foodmate.ai.auth.dto.WechatLoginRequest;
import com.foodmate.ai.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("ok");
    }

    @PostMapping("/auth/wechat/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody WechatLoginRequest request) {
        return ApiResponse.success(authService.loginByWechat(request));
    }
}
