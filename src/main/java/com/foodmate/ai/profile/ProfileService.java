package com.foodmate.ai.profile;

import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.feedback.FeedbackEvent;
import com.foodmate.ai.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {
    private final InMemoryStore store;

    public ProfileService(InMemoryStore store) {
        this.store = store;
    }

    public UserProfile getOrCreateProfile(User user) {
        return store.findProfileByUserId(user.getId()).orElseGet(() -> {
            UserProfile profile = new UserProfile();
            profile.setUserId(user.getId());
            profile.setOpenid(user.getOpenid());
            profile.setPreferredTastes(List.of("热乎", "下饭"));
            profile.setCookingTools(List.of("一口炒锅"));
            profile.setPreferenceSummary("我会先按热乎、下饭、20分钟内完成来帮你推荐。");
            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());
            return store.saveProfile(profile);
        });
    }

    public UserProfile requireProfile(Long userId) {
        return store.findProfileByUserId(userId).orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));
    }

    public UserProfile update(Long userId, UserProfile request) {
        UserProfile profile = requireProfile(userId);
        profile.setSpicyLevel(request.getSpicyLevel());
        profile.setPreferredTastes(request.getPreferredTastes());
        profile.setDislikedTastes(request.getDislikedTastes());
        profile.setFavoriteIngredients(request.getFavoriteIngredients());
        profile.setDislikedIngredients(request.getDislikedIngredients());
        profile.setCommonIngredients(request.getCommonIngredients());
        profile.setPreferredCookingTimeMinutes(request.getPreferredCookingTimeMinutes());
        profile.setPreferredDifficulty(request.getPreferredDifficulty());
        profile.setCookingTools(request.getCookingTools());
        profile.setHealthGoal(request.getHealthGoal());
        profile.setPreferenceSummary(buildPreferenceSummary(profile));
        profile.setUpdatedAt(LocalDateTime.now());
        return store.saveProfile(profile);
    }

    public void increaseGenerated(Long userId) {
        UserProfile profile = requireProfile(userId);
        profile.setTotalGenerated(profile.getTotalGenerated() + 1);
        profile.setUpdatedAt(LocalDateTime.now());
        store.saveProfile(profile);
    }

    public UserProfile rebuildProfile(Long userId) {
        UserProfile profile = requireProfile(userId);
        List<FeedbackEvent> events = store.findRecentFeedback(userId, 50);
        Map<String, Integer> tasteScore = new HashMap<>();
        Map<String, Integer> ingredientScore = new HashMap<>();
        for (FeedbackEvent event : events) {
            int delta = getFeedbackDelta(event.getAction());
            if (event.getDishTags() != null) {
                event.getDishTags().forEach(tag -> tasteScore.merge(tag, delta, Integer::sum));
            }
            if (event.getDishIngredients() != null) {
                event.getDishIngredients().forEach(ingredient -> ingredientScore.merge(ingredient, delta, Integer::sum));
            }
        }
        profile.setPreferredTastes(topScores(tasteScore, true, 6));
        profile.setDislikedTastes(topScores(tasteScore, false, 4));
        profile.setFavoriteIngredients(topScores(ingredientScore, true, 8));
        profile.setPreferredDifficulty(events.stream().anyMatch(event -> "too_hard".equals(event.getAction())) ? "easy" : profile.getPreferredDifficulty());
        profile.setPreferenceSummary(buildPreferenceSummary(profile));
        profile.setUpdatedAt(LocalDateTime.now());
        return store.saveProfile(profile);
    }

    public void markFeedback(Long userId, String action) {
        UserProfile profile = requireProfile(userId);
        if ("like".equals(action)) {
            profile.setTotalLiked(profile.getTotalLiked() + 1);
        }
        if ("cooked".equals(action)) {
            profile.setTotalCooked(profile.getTotalCooked() + 1);
        }
        if ("save".equals(action)) {
            profile.setTotalSaved(profile.getTotalSaved() + 1);
        }
        store.saveProfile(profile);
    }

    private int getFeedbackDelta(String action) {
        return switch (action) {
            case "like", "save" -> 2;
            case "cooked" -> 3;
            case "dislike" -> -2;
            case "too_hard", "too_light", "too_oily" -> -1;
            default -> 0;
        };
    }

    private List<String> topScores(Map<String, Integer> scores, boolean positive, int limit) {
        Comparator<Map.Entry<String, Integer>> comparator = positive
                ? Map.Entry.<String, Integer>comparingByValue().reversed()
                : Comparator.comparingInt(Map.Entry::getValue);
        return scores.entrySet().stream()
                .filter(entry -> positive ? entry.getValue() > 0 : entry.getValue() < 0)
                .sorted(comparator)
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    private String buildPreferenceSummary(UserProfile profile) {
        String tastes = profile.getPreferredTastes().isEmpty() ? "快手家常" : String.join("、", profile.getPreferredTastes());
        String ingredients = profile.getFavoriteIngredients().isEmpty() ? "常见食材" : String.join("、", profile.getFavoriteIngredients());
        return "你最近更喜欢：" + tastes + "，常用/喜欢的食材有：" + ingredients + "，偏好"
                + profile.getPreferredCookingTimeMinutes() + "分钟内、" + profile.getPreferredDifficulty() + "难度。";
    }
}
