package com.foodmate.ai.favorite;

import java.time.LocalDateTime;
import java.util.List;

public class Favorite {
    private Long id;
    private Long userId;
    private String openid;
    private Long recommendationId;
    private String name;
    private List<String> tags;
    private int estimatedTimeMinutes;
    private List<String> ingredientsUsed;
    private List<String> steps;
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

    public Long getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getEstimatedTimeMinutes() {
        return estimatedTimeMinutes;
    }

    public void setEstimatedTimeMinutes(int estimatedTimeMinutes) {
        this.estimatedTimeMinutes = estimatedTimeMinutes;
    }

    public List<String> getIngredientsUsed() {
        return ingredientsUsed;
    }

    public void setIngredientsUsed(List<String> ingredientsUsed) {
        this.ingredientsUsed = ingredientsUsed;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
