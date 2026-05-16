package com.foodmate.ai.profile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private Long id;
    private Long userId;
    private String openid;
    private int spicyLevel = 2;
    private List<String> preferredTastes = new ArrayList<>();
    private List<String> dislikedTastes = new ArrayList<>();
    private List<String> favoriteIngredients = new ArrayList<>();
    private List<String> dislikedIngredients = new ArrayList<>();
    private List<String> commonIngredients = new ArrayList<>();
    private int preferredCookingTimeMinutes = 20;
    private String preferredDifficulty = "easy";
    private List<String> cookingTools = new ArrayList<>();
    private List<String> healthGoals = new ArrayList<>();
    private String preferenceSummary = "还在了解你的晚餐偏好。";
    private int totalGenerated;
    private int totalLiked;
    private int totalCooked;
    private int totalSaved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public int getSpicyLevel() {
        return spicyLevel;
    }

    public void setSpicyLevel(int spicyLevel) {
        this.spicyLevel = spicyLevel;
    }

    public List<String> getPreferredTastes() {
        return preferredTastes;
    }

    public void setPreferredTastes(List<String> preferredTastes) {
        this.preferredTastes = preferredTastes == null ? new ArrayList<>() : preferredTastes;
    }

    public List<String> getDislikedTastes() {
        return dislikedTastes;
    }

    public void setDislikedTastes(List<String> dislikedTastes) {
        this.dislikedTastes = dislikedTastes == null ? new ArrayList<>() : dislikedTastes;
    }

    public List<String> getFavoriteIngredients() {
        return favoriteIngredients;
    }

    public void setFavoriteIngredients(List<String> favoriteIngredients) {
        this.favoriteIngredients = favoriteIngredients == null ? new ArrayList<>() : favoriteIngredients;
    }

    public List<String> getDislikedIngredients() {
        return dislikedIngredients;
    }

    public void setDislikedIngredients(List<String> dislikedIngredients) {
        this.dislikedIngredients = dislikedIngredients == null ? new ArrayList<>() : dislikedIngredients;
    }

    public List<String> getCommonIngredients() {
        return commonIngredients;
    }

    public void setCommonIngredients(List<String> commonIngredients) {
        this.commonIngredients = commonIngredients == null ? new ArrayList<>() : commonIngredients;
    }

    public int getPreferredCookingTimeMinutes() {
        return preferredCookingTimeMinutes;
    }

    public void setPreferredCookingTimeMinutes(int preferredCookingTimeMinutes) {
        this.preferredCookingTimeMinutes = preferredCookingTimeMinutes;
    }

    public String getPreferredDifficulty() {
        return preferredDifficulty;
    }

    public void setPreferredDifficulty(String preferredDifficulty) {
        this.preferredDifficulty = preferredDifficulty;
    }

    public List<String> getCookingTools() {
        return cookingTools;
    }

    public void setCookingTools(List<String> cookingTools) {
        this.cookingTools = cookingTools == null ? new ArrayList<>() : cookingTools;
    }

    public List<String> getHealthGoals() {
        return healthGoals;
    }

    public void setHealthGoals(List<String> healthGoals) {
        this.healthGoals = healthGoals == null ? new ArrayList<>() : healthGoals;
    }

    public String getPreferenceSummary() {
        return preferenceSummary;
    }

    public void setPreferenceSummary(String preferenceSummary) {
        this.preferenceSummary = preferenceSummary;
    }

    public int getTotalGenerated() {
        return totalGenerated;
    }

    public void setTotalGenerated(int totalGenerated) {
        this.totalGenerated = totalGenerated;
    }

    public int getTotalLiked() {
        return totalLiked;
    }

    public void setTotalLiked(int totalLiked) {
        this.totalLiked = totalLiked;
    }

    public int getTotalCooked() {
        return totalCooked;
    }

    public void setTotalCooked(int totalCooked) {
        this.totalCooked = totalCooked;
    }

    public int getTotalSaved() {
        return totalSaved;
    }

    public void setTotalSaved(int totalSaved) {
        this.totalSaved = totalSaved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
