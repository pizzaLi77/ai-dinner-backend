package com.foodmate.ai.history;

import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import com.foodmate.ai.common.api.PageResponse;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.history.dto.HistorySessionDTO;
import com.foodmate.ai.recommendation.Recommendation;
import com.foodmate.ai.recommendation.RecommendationSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final InMemoryStore store;

    public HistoryController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping
    public ApiResponse<PageResponse<HistorySessionDTO>> list(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = UserContext.requireUserId();
        List<Recommendation> recommendations = store.findRecommendationsByUser(userId);
        List<HistorySessionDTO> all = store.findSessionsByUser(userId).stream()
                .map(session -> toDto(session, recommendations))
                .toList();
        int from = Math.min(Math.max(page - 1, 0) * pageSize, all.size());
        int to = Math.min(from + pageSize, all.size());
        return ApiResponse.success(new PageResponse<>(all.subList(from, to), page, pageSize, all.size()));
    }

    private HistorySessionDTO toDto(RecommendationSession session, List<Recommendation> recommendations) {
        return new HistorySessionDTO(
                session.getId(),
                session.getFreeText(),
                session.getSelectedMoods(),
                session.getSelectedTastes(),
                session.getSelectedTime(),
                session.getSelectedTools(),
                session.getCreatedAt(),
                recommendations.stream()
                        .filter(item -> item.getSessionId().equals(session.getId()))
                        .map(Recommendation::toDto)
                        .toList()
        );
    }
}
