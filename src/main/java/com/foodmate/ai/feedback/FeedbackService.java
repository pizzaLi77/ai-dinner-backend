package com.foodmate.ai.feedback;

import com.foodmate.ai.analytics.AnalyticsService;
import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.favorite.Favorite;
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
    private static final Set<String> ACTIONS = Set.of("like", "dislike", "too_hard", "too_light", "too_oily", "save", "unsave", "cooked");
    private final InMemoryStore store;
    private final ProfileService profileService;
    private final AnalyticsService analyticsService;

    public FeedbackService(InMemoryStore store, ProfileService profileService, AnalyticsService analyticsService) {
        this.store = store;
        this.profileService = profileService;
        this.analyticsService = analyticsService;
    }

    public FeedbackResponse submit(Long userId, Long recommendationId, Long sessionId, String action) {
        if (!ACTIONS.contains(action)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "不支持的反馈类型");
        }
        User user = store.findUserById(userId).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        Recommendation recommendation = store.findRecommendation(recommendationId)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT, "推荐不存在"));
        applyAction(recommendation, action);
        store.saveRecommendation(recommendation);
        if ("save".equals(action)) {
            store.saveFavorite(toFavorite(recommendation));
        } else if ("unsave".equals(action)) {
            store.removeFavorite(userId, recommendationId);
        }
        FeedbackEvent event = new FeedbackEvent();
        event.setUserId(userId);
        event.setOpenid(user.getOpenid());
        event.setSessionId(sessionId);
        event.setRecommendationId(recommendationId);
        event.setAction(action);
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
            case "dislike" -> recommendation.setDisliked(true);
            case "too_hard" -> recommendation.setTooHard(true);
            case "too_light" -> recommendation.setTooLight(true);
            case "too_oily" -> recommendation.setTooOily(true);
            case "save" -> recommendation.setSaved(true);
            case "unsave" -> recommendation.setSaved(false);
            case "cooked" -> recommendation.setCooked(true);
            default -> { }
        }
        recommendation.setUpdatedAt(LocalDateTime.now());
    }

    private Favorite toFavorite(Recommendation recommendation) {
        Favorite favorite = new Favorite();
        favorite.setUserId(recommendation.getUserId());
        favorite.setOpenid(recommendation.getOpenid());
        favorite.setRecommendationId(recommendation.getId());
        favorite.setName(recommendation.getName());
        favorite.setTags(recommendation.getTags());
        favorite.setEstimatedTimeMinutes(recommendation.getEstimatedTimeMinutes());
        favorite.setIngredientsUsed(recommendation.getIngredientsUsed());
        favorite.setSteps(recommendation.getSteps());
        favorite.setCreatedAt(LocalDateTime.now());
        return favorite;
    }
}
