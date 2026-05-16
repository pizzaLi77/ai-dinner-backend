package com.foodmate.ai.auth;

import com.foodmate.ai.auth.dto.LoginResponse;
import com.foodmate.ai.auth.dto.WechatLoginRequest;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.profile.ProfileService;
import com.foodmate.ai.profile.UserProfile;
import com.foodmate.ai.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final InMemoryStore store;
    private final TokenService tokenService;
    private final ProfileService profileService;

    public AuthService(InMemoryStore store, TokenService tokenService, ProfileService profileService) {
        this.store = store;
        this.tokenService = tokenService;
        this.profileService = profileService;
    }

    public LoginResponse loginByWechat(WechatLoginRequest request) {
        String openid = resolveOpenid(request.getCode());
        boolean[] isNewUser = {false};
        User user = store.findUserByOpenid(openid).orElseGet(() -> {
            isNewUser[0] = true;
            User created = new User();
            created.setOpenid(openid);
            created.setNickname(request.getNickname());
            created.setAvatarUrl(request.getAvatarUrl());
            created.setCreatedAt(LocalDateTime.now());
            created.setUpdatedAt(LocalDateTime.now());
            created.setLastActiveAt(LocalDateTime.now());
            return store.saveUser(created);
        });
        user.setLastActiveAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        store.saveUser(user);
        UserProfile profile = profileService.getOrCreateProfile(user);
        return new LoginResponse(tokenService.issue(user.getId()), user.getId(), isNewUser[0], profile);
    }

    private String resolveOpenid(String code) {
        return "wx_mock_" + Integer.toHexString(code.hashCode());
    }
}
