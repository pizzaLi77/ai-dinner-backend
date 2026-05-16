package com.foodmate.ai.auth.dto;

import com.foodmate.ai.profile.UserProfile;

public record LoginResponse(String token, Long userId, boolean isNewUser, UserProfile profile) {
}
