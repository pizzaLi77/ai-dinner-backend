package com.foodmate.ai.history;

import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import com.foodmate.ai.common.api.PageResponse;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.history.dto.HistorySessionDTO;
import com.foodmate.ai.history.dto.HistoryGroupDTO;
import com.foodmate.ai.history.dto.HistoryGroupedResponse;
import com.foodmate.ai.history.dto.HistoryItemDTO;
import com.foodmate.ai.recommendation.Recommendation;
import com.foodmate.ai.recommendation.RecommendationSession;
import com.foodmate.ai.recommendation.dto.GenerateDinnerRequest;
import com.foodmate.ai.recommendation.dto.GenerateDinnerResponse;
import com.foodmate.ai.recommendation.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final InMemoryStore store;
    private final RecommendationService recommendationService;

    public HistoryController(InMemoryStore store, RecommendationService recommendationService) {
        this.store = store;
        this.recommendationService = recommendationService;
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

    @GetMapping("/grouped")
    public ApiResponse<HistoryGroupedResponse> grouped(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = UserContext.requireUserId();
        List<RecommendationSession> sessions = store.findSessionsByUser(userId);
        List<Recommendation> recommendations = store.findRecommendationsByUser(userId);
        int from = Math.min(Math.max(page - 1, 0) * pageSize, sessions.size());
        int to = Math.min(from + pageSize, sessions.size());
        Map<LocalDate, List<HistoryItemDTO>> grouped = sessions.subList(from, to).stream()
                .collect(Collectors.groupingBy(session -> session.getCreatedAt().toLocalDate(),
                        java.util.LinkedHashMap::new,
                        Collectors.mapping(session -> toHistoryItem(session, recommendations), Collectors.toList())));
        List<HistoryGroupDTO> groups = grouped.entrySet().stream()
                .map(entry -> new HistoryGroupDTO(entry.getKey().toString(), entry.getValue()))
                .toList();
        return ApiResponse.success(new HistoryGroupedResponse(groups, page, pageSize, sessions.size()));
    }

    @PostMapping("/{sessionId}/similar")
    public ApiResponse<GenerateDinnerResponse> similar(@PathVariable Long sessionId) {
        Long userId = UserContext.requireUserId();
        RecommendationSession session = store.findSession(sessionId)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() -> new com.foodmate.ai.common.exception.BusinessException(
                        com.foodmate.ai.common.api.ErrorCode.INVALID_INPUT, "session not found"));
        GenerateDinnerRequest request = new GenerateDinnerRequest();
        request.setFreeText("similar to " + session.getFreeText());
        request.setSelectedMoods(session.getSelectedMoods());
        request.setSelectedTastes(session.getSelectedTastes());
        request.setSelectedTime(session.getSelectedTime());
        request.setSelectedTools(session.getSelectedTools());
        return ApiResponse.success(recommendationService.generate(userId, request));
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

    private HistoryItemDTO toHistoryItem(RecommendationSession session, List<Recommendation> recommendations) {
        List<Recommendation> sessionRecommendations = recommendations.stream()
                .filter(item -> item.getSessionId().equals(session.getId()))
                .toList();
        String recommendationSummary = sessionRecommendations.stream()
                .map(Recommendation::getName)
                .limit(3)
                .collect(Collectors.joining(" + "));
        String feedbackSummary = sessionRecommendations.stream()
                .filter(Recommendation::isLiked)
                .findFirst()
                .map(item -> "liked " + item.getName())
                .orElse("");
        return new HistoryItemDTO(session.getId(), session.getFreeText(), recommendationSummary, feedbackSummary);
    }
}
