package com.foodmate.ai.favorite;

import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.api.PageResponse;
import com.foodmate.ai.common.exception.BusinessException;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.dinnerplan.DinnerPlanService;
import com.foodmate.ai.dinnerplan.TodayDinnerPlanItem;
import com.foodmate.ai.recommendation.Recommendation;
import com.foodmate.ai.recommendation.dto.GenerateDinnerRequest;
import com.foodmate.ai.recommendation.dto.GenerateDinnerResponse;
import com.foodmate.ai.recommendation.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {
    private final InMemoryStore store;
    private final DinnerPlanService dinnerPlanService;
    private final RecommendationService recommendationService;

    public FavoriteService(InMemoryStore store, DinnerPlanService dinnerPlanService,
                           RecommendationService recommendationService) {
        this.store = store;
        this.dinnerPlanService = dinnerPlanService;
        this.recommendationService = recommendationService;
    }

    public PageResponse<Favorite> list(Long userId, String tag, int page, int pageSize) {
        List<Favorite> all = store.findFavorites(userId).stream()
                .filter(item -> tag == null || tag.isBlank() || item.getTags() != null && item.getTags().contains(tag))
                .toList();
        int from = Math.min(Math.max(page - 1, 0) * pageSize, all.size());
        int to = Math.min(from + pageSize, all.size());
        return new PageResponse<>(all.subList(from, to), page, pageSize, all.size());
    }

    public Favorite saveRecommendation(Long userId, Long recommendationId) {
        Recommendation recommendation = store.findRecommendation(recommendationId)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT, "recommendation not found"));
        recommendation.setSaved(true);
        recommendation.setUpdatedAt(LocalDateTime.now());
        store.saveRecommendation(recommendation);
        return store.saveFavorite(toFavorite(recommendation));
    }

    public boolean remove(Long userId, Long favoriteId) {
        Favorite favorite = requireFavorite(userId, favoriteId);
        if (favorite.getRecommendationId() != null) {
            store.findRecommendation(favorite.getRecommendationId())
                    .filter(item -> item.getUserId().equals(userId))
                    .ifPresent(item -> {
                        item.setSaved(false);
                        item.setUpdatedAt(LocalDateTime.now());
                        store.saveRecommendation(item);
                    });
        }
        store.removeFavoriteById(userId, favoriteId);
        return true;
    }

    public GenerateDinnerResponse similar(Long userId, Long favoriteId) {
        Favorite favorite = requireFavorite(userId, favoriteId);
        GenerateDinnerRequest request = new GenerateDinnerRequest();
        request.setFreeText("similar to " + favorite.getName());
        request.setSelectedTastes(favorite.getTags());
        return recommendationService.generate(userId, request);
    }

    public TodayDinnerPlanItem addToToday(Long userId, Long favoriteId) {
        return dinnerPlanService.addFavorite(userId, requireFavorite(userId, favoriteId));
    }

    private Favorite requireFavorite(Long userId, Long favoriteId) {
        return store.findFavorite(userId, favoriteId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT, "favorite not found"));
    }

    private Favorite toFavorite(Recommendation recommendation) {
        Favorite favorite = new Favorite();
        favorite.setUserId(recommendation.getUserId());
        favorite.setOpenid(recommendation.getOpenid());
        favorite.setRecommendationId(recommendation.getId());
        favorite.setSourceSessionId(recommendation.getSessionId());
        favorite.setName(recommendation.getName());
        favorite.setSummary(recommendation.getReason());
        favorite.setTags(recommendation.getTags());
        favorite.setEstimatedTimeMinutes(recommendation.getEstimatedTimeMinutes());
        favorite.setIngredientsUsed(recommendation.getIngredientsUsed());
        favorite.setSteps(recommendation.getSteps());
        favorite.setCoverImageUrl(recommendation.getCoverImageUrl());
        favorite.setCreatedAt(LocalDateTime.now());
        return favorite;
    }
}
