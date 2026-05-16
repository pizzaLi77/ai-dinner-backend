package com.foodmate.ai.feedback;

import java.time.LocalDateTime;
import java.util.List;

public class FeedbackEvent {
    private Long id;
    private Long userId;
    private String openid;
    private Long sessionId;
    private Long recommendationId;
    private String action;
    private String dishName;
    private List<String> dishTags;
    private List<String> dishIngredients;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public List<String> getDishTags() {
        return dishTags;
    }

    public void setDishTags(List<String> dishTags) {
        this.dishTags = dishTags;
    }

    public List<String> getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(List<String> dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
