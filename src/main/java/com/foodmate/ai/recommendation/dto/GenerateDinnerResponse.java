package com.foodmate.ai.recommendation.dto;

import java.util.List;

public record GenerateDinnerResponse(Long sessionId, String profileSummary, List<DinnerRecommendationDTO> recommendations) {
}
