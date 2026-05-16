package com.foodmate.ai.history.dto;

public record HistoryItemDTO(Long sessionId, String inputSummary, String recommendationSummary, String feedbackSummary) {
}
