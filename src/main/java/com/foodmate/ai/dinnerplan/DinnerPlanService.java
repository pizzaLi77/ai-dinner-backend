package com.foodmate.ai.dinnerplan;

import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.favorite.Favorite;
import com.foodmate.ai.recommendation.Recommendation;
import com.foodmate.ai.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DinnerPlanService {
    private final InMemoryStore store;

    public DinnerPlanService(InMemoryStore store) {
        this.store = store;
    }

    public TodayDinnerPlanItem add(Long userId, AddDinnerPlanItemRequest request) {
        User user = store.findUserById(userId).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        TodayDinnerPlanItem item = new TodayDinnerPlanItem();
        item.setUserId(userId);
        item.setOpenid(user.getOpenid());
        item.setPlanDate(LocalDate.now());
        item.setSourceType(request.getSourceType());
        item.setSourceId(request.getSourceId());
        item.setDishName(request.getDishName());
        item.setTags(resolveTags(userId, request.getSourceType(), request.getSourceId()));
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        return store.saveTodayDinnerPlanItem(item);
    }

    public TodayDinnerPlanItem addFavorite(Long userId, Favorite favorite) {
        AddDinnerPlanItemRequest request = new AddDinnerPlanItemRequest();
        request.setSourceType("favorite");
        request.setSourceId(favorite.getId());
        request.setDishName(favorite.getName());
        return add(userId, request);
    }

    public TodayDinnerPlanItem addRecommendation(Long userId, Recommendation recommendation) {
        AddDinnerPlanItemRequest request = new AddDinnerPlanItemRequest();
        request.setSourceType("recommendation");
        request.setSourceId(recommendation.getId());
        request.setDishName(recommendation.getName());
        TodayDinnerPlanItem item = add(userId, request);
        recommendation.setAddToToday(true);
        recommendation.setUpdatedAt(LocalDateTime.now());
        store.saveRecommendation(recommendation);
        return item;
    }

    public List<TodayDinnerPlanItem> today(Long userId) {
        return store.findTodayDinnerPlanItems(userId, LocalDate.now());
    }

    private List<String> resolveTags(Long userId, String sourceType, Long sourceId) {
        if (sourceId == null) {
            return List.of();
        }
        if ("favorite".equals(sourceType)) {
            return store.findFavorite(userId, sourceId).map(Favorite::getTags).orElse(List.of());
        }
        if ("recommendation".equals(sourceType)) {
            return store.findRecommendation(sourceId)
                    .filter(item -> item.getUserId().equals(userId))
                    .map(Recommendation::getTags)
                    .orElse(List.of());
        }
        return List.of();
    }
}
