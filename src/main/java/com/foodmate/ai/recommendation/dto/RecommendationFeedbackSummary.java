package com.foodmate.ai.recommendation.dto;

public record RecommendationFeedbackSummary(boolean liked, boolean disliked, boolean saved, boolean cooked,
                                            boolean tooHard, boolean tooLight, boolean tooOily) {
}
