package com.foodmate.ai.history.dto;

import java.util.List;

public record HistoryGroupedResponse(List<HistoryGroupDTO> groups, int page, int pageSize, long total) {
}
