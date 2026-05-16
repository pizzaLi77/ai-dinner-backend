package com.foodmate.ai.recommendation;

import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import com.foodmate.ai.feedback.FeedbackService;
import com.foodmate.ai.recommendation.dto.FeedbackResponse;
import com.foodmate.ai.recommendation.dto.GenerateDinnerRequest;
import com.foodmate.ai.recommendation.dto.GenerateDinnerResponse;
import com.foodmate.ai.recommendation.dto.SubmitFeedbackRequest;
import com.foodmate.ai.recommendation.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final FeedbackService feedbackService;

    public RecommendationController(RecommendationService recommendationService, FeedbackService feedbackService) {
        this.recommendationService = recommendationService;
        this.feedbackService = feedbackService;
    }

    @PostMapping("/generate")
    public ApiResponse<GenerateDinnerResponse> generate(@Valid @RequestBody GenerateDinnerRequest request) {
        return ApiResponse.success(recommendationService.generate(UserContext.requireUserId(), request));
    }

    @PostMapping("/{recommendationId}/feedback")
    public ApiResponse<FeedbackResponse> feedback(@PathVariable Long recommendationId,
                                                  @Valid @RequestBody SubmitFeedbackRequest request) {
        return ApiResponse.success(feedbackService.submit(UserContext.requireUserId(), recommendationId,
                request.getSessionId(), request.getAction()));
    }
}
