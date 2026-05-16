package com.foodmate.ai.recommendation;

import com.foodmate.ai.recommendation.dto.DinnerRecommendationDTO;
import com.foodmate.ai.recommendation.dto.MissingIngredientDTO;
import com.foodmate.ai.recommendation.dto.RecommendationFeedbackSummary;

import java.time.LocalDateTime;
import java.util.List;

public class Recommendation {
    private Long id;
    private Long sessionId;
    private Long userId;
    private String openid;
    private String type;
    private String name;
    private String reason;
    private int estimatedTimeMinutes;
    private String difficulty;
    private List<String> ingredientsUsed;
    private List<MissingIngredientDTO> missingIngredients;
    private List<String> steps;
    private List<String> substitutions;
    private List<String> tags;
    private String caution;
    private boolean liked;
    private boolean disliked;
    private boolean saved;
    private boolean cooked;
    private boolean tooHard;
    private boolean tooLight;
    private boolean tooOily;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Recommendation from(Long userId, String openid, Long sessionId, DinnerRecommendationDTO dto) {
        Recommendation recommendation = new Recommendation();
        recommendation.setUserId(userId);
        recommendation.setOpenid(openid);
        recommendation.setSessionId(sessionId);
        recommendation.setType(dto.getType());
        recommendation.setName(dto.getName());
        recommendation.setReason(dto.getReason());
        recommendation.setEstimatedTimeMinutes(dto.getEstimatedTimeMinutes());
        recommendation.setDifficulty(dto.getDifficulty());
        recommendation.setIngredientsUsed(dto.getIngredientsUsed());
        recommendation.setMissingIngredients(dto.getMissingIngredients());
        recommendation.setSteps(dto.getSteps());
        recommendation.setSubstitutions(dto.getSubstitutions());
        recommendation.setTags(dto.getTags());
        recommendation.setCaution(dto.getCaution());
        recommendation.setCreatedAt(LocalDateTime.now());
        recommendation.setUpdatedAt(LocalDateTime.now());
        return recommendation;
    }

    public DinnerRecommendationDTO toDto() {
        DinnerRecommendationDTO dto = new DinnerRecommendationDTO();
        dto.setId(id);
        dto.setSessionId(sessionId);
        dto.setType(type);
        dto.setName(name);
        dto.setReason(reason);
        dto.setEstimatedTimeMinutes(estimatedTimeMinutes);
        dto.setDifficulty(difficulty);
        dto.setIngredientsUsed(ingredientsUsed);
        dto.setMissingIngredients(missingIngredients);
        dto.setSteps(steps);
        dto.setSubstitutions(substitutions);
        dto.setTags(tags);
        dto.setCaution(caution);
        dto.setFeedbackSummary(new RecommendationFeedbackSummary(liked, disliked, saved, cooked, tooHard, tooLight, tooOily));
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getEstimatedTimeMinutes() {
        return estimatedTimeMinutes;
    }

    public void setEstimatedTimeMinutes(int estimatedTimeMinutes) {
        this.estimatedTimeMinutes = estimatedTimeMinutes;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getIngredientsUsed() {
        return ingredientsUsed;
    }

    public void setIngredientsUsed(List<String> ingredientsUsed) {
        this.ingredientsUsed = ingredientsUsed;
    }

    public List<MissingIngredientDTO> getMissingIngredients() {
        return missingIngredients;
    }

    public void setMissingIngredients(List<MissingIngredientDTO> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(List<String> substitutions) {
        this.substitutions = substitutions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isCooked() {
        return cooked;
    }

    public void setCooked(boolean cooked) {
        this.cooked = cooked;
    }

    public boolean isTooHard() {
        return tooHard;
    }

    public void setTooHard(boolean tooHard) {
        this.tooHard = tooHard;
    }

    public boolean isTooLight() {
        return tooLight;
    }

    public void setTooLight(boolean tooLight) {
        this.tooLight = tooLight;
    }

    public boolean isTooOily() {
        return tooOily;
    }

    public void setTooOily(boolean tooOily) {
        this.tooOily = tooOily;
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
