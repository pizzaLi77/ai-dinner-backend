package com.foodmate.ai.common.api;

import java.util.List;

public record PageResponse<T>(List<T> records, int page, int pageSize, long total) {
}
