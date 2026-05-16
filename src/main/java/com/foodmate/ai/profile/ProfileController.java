package com.foodmate.ai.profile;

import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfile> me() {
        return ApiResponse.success(profileService.requireProfile(UserContext.requireUserId()));
    }

    @PutMapping("/me")
    public ApiResponse<UserProfile> update(@RequestBody UserProfile request) {
        return ApiResponse.success(profileService.update(UserContext.requireUserId(), request));
    }
}
