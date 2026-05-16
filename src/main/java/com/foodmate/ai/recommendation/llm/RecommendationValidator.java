package com.foodmate.ai.recommendation.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodmate.ai.common.api.ErrorCode;
import com.foodmate.ai.common.exception.BusinessException;
import com.foodmate.ai.recommendation.dto.DinnerRecommendationDTO;
import com.foodmate.ai.recommendation.dto.MissingIngredientDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RecommendationValidator {
    private static final Set<String> REQUIRED_TYPES = Set.of("easy", "satisfying", "healthy");
    private static final Set<String> DIFFICULTIES = Set.of("easy", "medium", "hard");
    private final ObjectMapper objectMapper;

    public RecommendationValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<DinnerRecommendationDTO> validateAndNormalize(String content) {
        try {
            JsonNode root = objectMapper.readTree(content);
            JsonNode items = root.path("recommendations");
            if (!items.isArray() || items.size() != 3) {
                throw new BusinessException(ErrorCode.LLM_INVALID_OUTPUT);
            }
            List<DinnerRecommendationDTO> result = new ArrayList<>();
            for (JsonNode item : items) {
                DinnerRecommendationDTO dto = new DinnerRecommendationDTO();
                dto.setType(truncate(requiredText(item, "type"), 32));
                dto.setTypeLabel(typeLabel(dto.getType(), truncate(item.path("typeLabel").asText(""), 32)));
                dto.setName(truncate(requiredText(item, "name"), 128));
                dto.setReason(truncate(requiredText(item, "reason"), 255));
                dto.setEstimatedTimeMinutes(Math.max(1, item.path("estimatedTimeMinutes").asInt(20)));
                dto.setDifficulty(DIFFICULTIES.contains(item.path("difficulty").asText()) ? item.path("difficulty").asText() : "easy");
                dto.setIngredientsUsed(readTextArray(item.path("ingredientsUsed"), 12, 32));
                dto.setMissingIngredients(readMissingIngredients(item.path("missingIngredients")));
                dto.setSteps(readTextArray(item.path("steps"), 8, 120));
                dto.setSubstitutions(readTextArray(item.path("substitutions"), 5, 120));
                dto.setTags(readTextArray(item.path("tags"), 8, 32));
                dto.setCoverImageUrl(truncate(item.path("coverImageUrl").asText(""), 512));
                dto.setCaution(truncate(item.path("caution").asText(""), 255));
                if (!REQUIRED_TYPES.contains(dto.getType()) || dto.getSteps().isEmpty()) {
                    throw new BusinessException(ErrorCode.LLM_INVALID_OUTPUT);
                }
                result.add(dto);
            }
            Set<String> types = result.stream().map(DinnerRecommendationDTO::getType).collect(java.util.stream.Collectors.toSet());
            if (!types.containsAll(REQUIRED_TYPES)) {
                throw new BusinessException(ErrorCode.LLM_INVALID_OUTPUT);
            }
            return result;
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.LLM_INVALID_OUTPUT);
        }
    }

    private String requiredText(JsonNode node, String field) {
        String value = node.path(field).asText("");
        if (value.isBlank()) {
            throw new BusinessException(ErrorCode.LLM_INVALID_OUTPUT);
        }
        return value;
    }

    private List<String> readTextArray(JsonNode node, int maxItems, int maxLength) {
        if (!node.isArray()) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        for (JsonNode item : node) {
            if (values.size() >= maxItems) {
                break;
            }
            String value = truncate(item.asText(""), maxLength);
            if (!value.isBlank()) {
                values.add(value);
            }
        }
        return values;
    }

    private List<MissingIngredientDTO> readMissingIngredients(JsonNode node) {
        if (!node.isArray()) {
            return List.of();
        }
        List<MissingIngredientDTO> values = new ArrayList<>();
        for (JsonNode item : node) {
            if (values.size() >= 8) {
                break;
            }
            String name = truncate(item.path("name").asText(""), 32);
            if (!name.isBlank()) {
                values.add(new MissingIngredientDTO(name, item.path("optional").asBoolean(true)));
            }
        }
        return values;
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private String typeLabel(String type, String fallback) {
        if (!fallback.isBlank()) {
            return fallback;
        }
        return switch (type) {
            case "easy" -> "\u6700\u7701\u4e8b";
            case "satisfying" -> "\u6700\u6ee1\u8db3";
            case "healthy" -> "\u6700\u5065\u5eb7";
            default -> type;
        };
    }
}
