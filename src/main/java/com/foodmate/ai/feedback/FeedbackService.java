package com.foodmate.ai.feedback;

import com.foodmate.ai.analytics.AnalyticsService;
import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.dinnerplan.DinnerPlanService;
import com.foodmate.ai.favorite.FavoriteService;
import com.foodmate.ai.profile.ProfileService;
import com.foodmate.ai.profile.UserProfile;
import com.foodmate.ai.recommendation.Recommendation;
import com.foodmate.ai.recommendation.dto.FeedbackResponse;
import com.foodmate.ai.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class FeedbackService {
    private static final Set<String> ACTIONS = Set.of("like", "neutral", "replace", "dislike", "too_hard",
            "too_light", "too_oily", "save", "unsave", "cooked", "add_to_today");
    private final InMemoryStore store;
    private final ProfileService profileService;
    private final AnalyticsService analyticsService;
    private final FavoriteService favoriteService;
    private final DinnerPlanService dinnerPlanService;

    public FeedbackService(InMemoryStore store, ProfileService profileService, AnalyticsService analyticsService,
                           FavoriteService favoriteService, DinnerPlanService dinnerPlanService) {
        this.store = store;
        this.profileService = profileService;
        this.analyticsService = analyticsService;
        this.favoriteService = favoriteService;
        this.dinnerPlanService = dinnerPlanService;
    }

    public FeedbackResponse submit(Long userId, Long recommendationId, Long sessionId, String action, String extraReason) {
        if (!ACTIONS.contains(action)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "unsupported feedback action");
        }
        User user = store.findUserById(userId).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        Recommendation recommendation = store.findRecommendation(recommendationId)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT, "recommendation not found"));
        applyAction(recommendation, action);
        store.saveRecommendation(recommendation);
        if ("save".equals(action)) {
            favoriteService.saveRecommendation(userId, recommendationId);
        } else if ("unsave".equals(action)) {
            store.removeFavorite(userId, recommendationId);
        } else if ("add_to_today".equals(action)) {
            dinnerPlanService.addRecommendation(userId, recommendation);
        }
        FeedbackEvent event = new FeedbackEvent();
        event.setUserId(userId);
        event.setOpenid(user.getOpenid());
        event.setSessionId(sessionId);
        event.setRecommendationId(recommendationId);
        event.setAction(action);
        event.setExtraReason(extraReason);
        event.setDishName(recommendation.getName());
        event.setDishTags(recommendation.getTags());
        event.setDishIngredients(recommendation.getIngredientsUsed());
        event.setCreatedAt(LocalDateTime.now());
        store.saveFeedback(event);
        profileService.markFeedback(userId, action);
        UserProfile profile = profileService.rebuildProfile(userId);
        analyticsService.track(user, "recommendation_feedback", Map.of(
                "sessionId", sessionId,
                "recommendationId", recommendationId,
                "action", action
        ));
        return new FeedbackResponse(true, profile.getPreferenceSummary());
    }

    private void applyAction(Recommendation recommendation, String action) {
        switch (action) {
            case "like" -> recommendation.setLiked(true);
            case "neutral" -> recommendation.setNeutral(true);
            case "dislike" -> recommendation.setDisliked(true);
            case "too_hard" -> recommendation.setTooHard(true);
            case "too_light" -> recommendation.setTooLight(true);
            case "too_oily" -> recommendation.setTooOily(true);
            case "save" -> recommendation.setSaved(true);
            case "unsave" -> recommendation.setSaved(false);
            case "cooked" -> recommendation.setCooked(true);
            case "add_to_today" -> recommendation.setAddToToday(true);
            default -> { }
        }
        recommendation.setUpdatedAt(LocalDateTime.now());
    }
}
