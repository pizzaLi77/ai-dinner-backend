package com.foodmate.ai.recommendation.dto;

import java.util.ArrayList;
import java.util.List;

public class DinnerRecommendationDTO {
    private Long id;
    private Long sessionId;
    private String type;
    private String typeLabel;
    private String name;
    private String reason;
    private Integer estimatedTimeMinutes;
    private String difficulty;
    private List<String> ingredientsUsed = new ArrayList<>();
    private List<MissingIngredientDTO> missingIngredients = new ArrayList<>();
    private List<String> steps = new ArrayList<>();
    private List<String> substitutions = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String coverImageUrl;
    private String caution = "";
    private RecommendationFeedbackSummary feedbackSummary;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
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

    public Integer getEstimatedTimeMinutes() {
        return estimatedTimeMinutes;
    }

    public void setEstimatedTimeMinutes(Integer estimatedTimeMinutes) {
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
        this.ingredientsUsed = ingredientsUsed == null ? new ArrayList<>() : ingredientsUsed;
    }

    public List<MissingIngredientDTO> getMissingIngredients() {
        return missingIngredients;
    }

    public void setMissingIngredients(List<MissingIngredientDTO> missingIngredients) {
        this.missingIngredients = missingIngredients == null ? new ArrayList<>() : missingIngredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps == null ? new ArrayList<>() : steps;
    }

    public List<String> getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(List<String> substitutions) {
        this.substitutions = substitutions == null ? new ArrayList<>() : substitutions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags == null ? new ArrayList<>() : tags;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public RecommendationFeedbackSummary getFeedbackSummary() {
        return feedbackSummary;
    }

    public void setFeedbackSummary(RecommendationFeedbackSummary feedbackSummary) {
        this.feedbackSummary = feedbackSummary;
    }
}
