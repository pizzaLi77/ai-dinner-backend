package com.foodmate.ai.dinnerplan;

import jakarta.validation.constraints.NotBlank;

public class AddDinnerPlanItemRequest {
    @NotBlank
    private String sourceType;
    private Long sourceId;
    @NotBlank
    private String dishName;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
}
