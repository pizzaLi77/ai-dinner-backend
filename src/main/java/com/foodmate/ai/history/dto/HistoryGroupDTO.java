package com.foodmate.ai.history.dto;

import java.util.List;

public record HistoryGroupDTO(String dateLabel, List<HistoryItemDTO> items) {
}
