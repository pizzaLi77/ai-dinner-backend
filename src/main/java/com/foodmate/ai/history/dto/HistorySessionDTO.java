package com.foodmate.ai.history.dto;

import com.foodmate.ai.recommendation.dto.DinnerRecommendationDTO;

import java.time.LocalDateTime;
import java.util.List;

public record HistorySessionDTO(Long sessionId, String freeText, List<String> selectedMoods,
                                List<String> selectedTastes, String selectedTime,
                                List<String> selectedTools, LocalDateTime createdAt,
                                List<DinnerRecommendationDTO> recommendations) {
}
