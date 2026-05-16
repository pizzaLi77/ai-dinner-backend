package com.foodmate.ai.recommendation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmitFeedbackRequest {
    @NotNull
    private Long sessionId;
    @NotBlank
    private String action;
    private String extraReason;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExtraReason() {
        return extraReason;
    }

    public void setExtraReason(String extraReason) {
        this.extraReason = extraReason;
    }
}
