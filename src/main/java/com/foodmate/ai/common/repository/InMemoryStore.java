package com.foodmate.ai.common.repository;

import com.foodmate.ai.analytics.AnalyticsEvent;
import com.foodmate.ai.dinnerplan.TodayDinnerPlanItem;
import com.foodmate.ai.favorite.Favorite;
import com.foodmate.ai.feedback.FeedbackEvent;
import com.foodmate.ai.profile.UserProfile;
import com.foodmate.ai.recommendation.Recommendation;
import com.foodmate.ai.recommendation.RecommendationSession;
import com.foodmate.ai.user.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryStore {
    private final AtomicLong userId = new AtomicLong(10000);
    private final AtomicLong profileId = new AtomicLong(1);
    private final AtomicLong sessionId = new AtomicLong(90000);
    private final AtomicLong recommendationId = new AtomicLong(80000);
    private final AtomicLong feedbackId = new AtomicLong(1);
    private final AtomicLong favoriteId = new AtomicLong(1);
    private final AtomicLong todayDinnerPlanItemId = new AtomicLong(1);
    private final AtomicLong analyticsId = new AtomicLong(1);

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<String, Long> openidToUserId = new ConcurrentHashMap<>();
    private final Map<Long, UserProfile> profiles = new ConcurrentHashMap<>();
    private final Map<Long, RecommendationSession> sessions = new ConcurrentHashMap<>();
    private final Map<Long, Recommendation> recommendations = new ConcurrentHashMap<>();
    private final Map<Long, FeedbackEvent> feedbackEvents = new ConcurrentHashMap<>();
    private final Map<String, Favorite> favorites = new ConcurrentHashMap<>();
    private final Map<Long, TodayDinnerPlanItem> todayDinnerPlanItems = new ConcurrentHashMap<>();
    private final Map<Long, AnalyticsEvent> analyticsEvents = new ConcurrentHashMap<>();

    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setId(userId.incrementAndGet());
        }
        users.put(user.getId(), user);
        openidToUserId.put(user.getOpenid(), user.getId());
        return user;
    }

    public Optional<User> findUserByOpenid(String openid) {
        return Optional.ofNullable(openidToUserId.get(openid)).map(users::get);
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public UserProfile saveProfile(UserProfile profile) {
        if (profile.getId() == null) {
            profile.setId(profileId.incrementAndGet());
        }
        profiles.put(profile.getUserId(), profile);
        return profile;
    }

    public Optional<UserProfile> findProfileByUserId(Long userId) {
        return Optional.ofNullable(profiles.get(userId));
    }

    public RecommendationSession saveSession(RecommendationSession session) {
        if (session.getId() == null) {
            session.setId(sessionId.incrementAndGet());
        }
        sessions.put(session.getId(), session);
        return session;
    }

    public Recommendation saveRecommendation(Recommendation recommendation) {
        if (recommendation.getId() == null) {
            recommendation.setId(recommendationId.incrementAndGet());
        }
        recommendations.put(recommendation.getId(), recommendation);
        return recommendation;
    }

    public Optional<Recommendation> findRecommendation(Long id) {
        return Optional.ofNullable(recommendations.get(id));
    }

    public Optional<RecommendationSession> findSession(Long id) {
        return Optional.ofNullable(sessions.get(id));
    }

    public List<Recommendation> findRecommendationsByUser(Long userId) {
        return recommendations.values().stream()
                .filter(item -> item.getUserId().equals(userId))
                .sorted(Comparator.comparing(Recommendation::getCreatedAt).reversed())
                .toList();
    }

    public List<RecommendationSession> findSessionsByUser(Long userId) {
        return sessions.values().stream()
                .filter(item -> item.getUserId().equals(userId))
                .sorted(Comparator.comparing(RecommendationSession::getCreatedAt).reversed())
                .toList();
    }

    public FeedbackEvent saveFeedback(FeedbackEvent event) {
        if (event.getId() == null) {
            event.setId(feedbackId.incrementAndGet());
        }
        feedbackEvents.put(event.getId(), event);
        return event;
    }

    public List<FeedbackEvent> findRecentFeedback(Long userId, int limit) {
        return feedbackEvents.values().stream()
                .filter(item -> item.getUserId().equals(userId))
                .sorted(Comparator.comparing(FeedbackEvent::getCreatedAt).reversed())
                .limit(limit)
                .toList();
    }

    public Favorite saveFavorite(Favorite favorite) {
        String key = favorite.getUserId() + ":" + favorite.getRecommendationId();
        if (favorite.getId() == null) {
            favorite.setId(favoriteId.incrementAndGet());
        }
        favorites.put(key, favorite);
        return favorite;
    }

    public void removeFavorite(Long userId, Long recommendationId) {
        favorites.remove(userId + ":" + recommendationId);
    }

    public Optional<Favorite> findFavorite(Long userId, Long favoriteId) {
        return favorites.values().stream()
                .filter(item -> item.getUserId().equals(userId) && item.getId().equals(favoriteId))
                .findFirst();
    }

    public void removeFavoriteById(Long userId, Long favoriteId) {
        findFavorite(userId, favoriteId).ifPresent(item -> favorites.remove(item.getUserId() + ":" + item.getRecommendationId()));
    }

    public List<Favorite> findFavorites(Long userId) {
        return new ArrayList<>(favorites.values()).stream()
                .filter(item -> item.getUserId().equals(userId))
                .sorted(Comparator.comparing(Favorite::getCreatedAt).reversed())
                .toList();
    }

    public TodayDinnerPlanItem saveTodayDinnerPlanItem(TodayDinnerPlanItem item) {
        if (item.getId() == null) {
            item.setId(todayDinnerPlanItemId.incrementAndGet());
        }
        todayDinnerPlanItems.put(item.getId(), item);
        return item;
    }

    public List<TodayDinnerPlanItem> findTodayDinnerPlanItems(Long userId, LocalDate planDate) {
        return todayDinnerPlanItems.values().stream()
                .filter(item -> item.getUserId().equals(userId) && item.getPlanDate().equals(planDate))
                .sorted(Comparator.comparing(TodayDinnerPlanItem::getCreatedAt))
                .toList();
    }

    public AnalyticsEvent saveAnalytics(AnalyticsEvent event) {
        if (event.getId() == null) {
            event.setId(analyticsId.incrementAndGet());
        }
        analyticsEvents.put(event.getId(), event);
        return event;
    }
}
