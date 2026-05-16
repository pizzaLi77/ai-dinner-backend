package com.foodmate.ai.analytics;

import com.foodmate.ai.analytics.dto.TrackEventRequest;
import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.user.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final InMemoryStore store;

    public AnalyticsController(AnalyticsService analyticsService, InMemoryStore store) {
        this.analyticsService = analyticsService;
        this.store = store;
    }

    @PostMapping("/events")
    public ApiResponse<Boolean> track(@Valid @RequestBody TrackEventRequest request) {
        User user = store.findUserById(UserContext.requireUserId()).orElse(null);
        analyticsService.track(user, request.getEventName(), request.getProperties());
        return ApiResponse.success(true);
    }
}
