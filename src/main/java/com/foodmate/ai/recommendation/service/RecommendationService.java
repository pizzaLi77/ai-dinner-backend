package com.foodmate.ai.recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodmate.ai.analytics.AnalyticsService;
import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import com.foodmate.ai.common.repository.InMemoryStore;
import com.foodmate.ai.profile.ProfileService;
import com.foodmate.ai.profile.UserProfile;
import com.foodmate.ai.recommendation.InputLimits;
import com.foodmate.ai.recommendation.Recommendation;
import com.foodmate.ai.recommendation.RecommendationSession;
import com.foodmate.ai.recommendation.dto.DinnerRecommendationDTO;
import com.foodmate.ai.recommendation.dto.GenerateDinnerRequest;
import com.foodmate.ai.recommendation.dto.GenerateDinnerResponse;
import com.foodmate.ai.recommendation.llm.LlmClient;
import com.foodmate.ai.recommendation.llm.LlmProperties;
import com.foodmate.ai.recommendation.llm.RecommendationValidator;
import com.foodmate.ai.recommendation.prompt.PromptBuilder;
import com.foodmate.ai.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {
    private final InMemoryStore store;
    private final ProfileService profileService;
    private final PromptBuilder promptBuilder;
    private final LlmClient llmClient;
    private final RecommendationValidator validator;
    private final FallbackRecommendationFactory fallbackFactory;
    private final LlmProperties llmProperties;
    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;

    public RecommendationService(InMemoryStore store, ProfileService profileService, PromptBuilder promptBuilder,
                                 LlmClient llmClient, RecommendationValidator validator,
                                 FallbackRecommendationFactory fallbackFactory, LlmProperties llmProperties,
                                 ObjectMapper objectMapper, AnalyticsService analyticsService) {
        this.store = store;
        this.profileService = profileService;
        this.promptBuilder = promptBuilder;
        this.llmClient = llmClient;
        this.validator = validator;
        this.fallbackFactory = fallbackFactory;
        this.llmProperties = llmProperties;
        this.objectMapper = objectMapper;
        this.analyticsService = analyticsService;
    }

    public GenerateDinnerResponse generate(Long userId, GenerateDinnerRequest request) {
        validateRequest(request);
        User user = store.findUserById(userId).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        UserProfile profile = profileService.requireProfile(userId);
        List<DinnerRecommendationDTO> dtos;
        RecommendationSession session = RecommendationSession.from(
                userId, user.getOpenid(), request, safeJson(profile),
                llmProperties.provider(), llmProperties.model(), llmProperties.promptVersion()
        );
        try {
            String content = llmClient.chat(promptBuilder.systemPrompt(), promptBuilder.build(request, profile));
            dtos = validator.validateAndNormalize(content);
        } catch (Exception exception) {
            session.setStatus("fallback");
            session.setErrorMessage(exception.getMessage());
            dtos = fallbackFactory.create(request, profile);
        }
        store.saveSession(session);
        List<DinnerRecommendationDTO> saved = dtos.stream()
                .map(dto -> store.saveRecommendation(Recommendation.from(userId, user.getOpenid(), session.getId(), dto)).toDto())
                .toList();
        profileService.increaseGenerated(userId);
        analyticsService.track(user, "generate_success", Map.of(
                "sessionId", session.getId(),
                "recommendationCount", saved.size(),
                "hasFreeText", request.getFreeText() != null && !request.getFreeText().isBlank(),
                "selectedMoodsCount", request.getSelectedMoods().size(),
                "selectedTastesCount", request.getSelectedTastes().size(),
                "fallback", "fallback".equals(session.getStatus())
        ));
        return new GenerateDinnerResponse(session.getId(), profile.getPreferenceSummary(), saved);
    }

    private void validateRequest(GenerateDinnerRequest request) {
        boolean emptyText = request.getFreeText() == null || request.getFreeText().isBlank();
        boolean emptyTags = request.getSelectedMoods().isEmpty()
                && request.getSelectedTastes().isEmpty()
                && request.getSelectedTime() == null
                && request.getSelectedTools().isEmpty();
        if (emptyText && emptyTags) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "告诉我家里有什么，或者今天想吃什么口味也可以。");
        }
        if (request.getFreeText() != null && request.getFreeText().length() > InputLimits.FREE_TEXT_MAX_LENGTH
                || request.getSelectedMoods().size() > InputLimits.MAX_SELECTED_MOODS
                || request.getSelectedTastes().size() > InputLimits.MAX_SELECTED_TASTES
                || request.getSelectedTools().size() > InputLimits.MAX_SELECTED_TOOLS) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
    }

    private String safeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception exception) {
            return "{}";
        }
    }
}
