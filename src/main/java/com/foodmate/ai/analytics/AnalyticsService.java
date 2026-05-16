package com.foodmate.ai.analytics;

import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AnalyticsService {
    private final InMemoryStore store;

    public AnalyticsService(InMemoryStore store) {
        this.store = store;
    }

    public void track(User user, String eventName, Map<String, Object> properties) {
        AnalyticsEvent event = new AnalyticsEvent();
        event.setUserId(user == null ? null : user.getId());
        event.setOpenid(user == null ? null : user.getOpenid());
        event.setEventName(eventName);
        event.setProperties(properties);
        event.setCreatedAt(LocalDateTime.now());
        store.saveAnalytics(event);
    }
}
