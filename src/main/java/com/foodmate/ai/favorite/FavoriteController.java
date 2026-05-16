package com.foodmate.ai.favorite;

import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import com.foodmate.ai.common.api.PageResponse;
import com.foodmate.ai.dinnerplan.TodayDinnerPlanItem;
import com.foodmate.ai.recommendation.dto.GenerateDinnerResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ApiResponse<PageResponse<Favorite>> list(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "20") int pageSize,
                                                    @RequestParam(required = false) String tag) {
        return ApiResponse.success(favoriteService.list(UserContext.requireUserId(), tag, page, pageSize));
    }

    @DeleteMapping("/{favoriteId}")
    public ApiResponse<Boolean> remove(@PathVariable Long favoriteId) {
        return ApiResponse.success(favoriteService.remove(UserContext.requireUserId(), favoriteId));
    }

    @PostMapping("/{favoriteId}/similar")
    public ApiResponse<GenerateDinnerResponse> similar(@PathVariable Long favoriteId) {
        return ApiResponse.success(favoriteService.similar(UserContext.requireUserId(), favoriteId));
    }

    @PostMapping("/{favoriteId}/add-to-today")
    public ApiResponse<TodayDinnerPlanItem> addToToday(@PathVariable Long favoriteId) {
        return ApiResponse.success(favoriteService.addToToday(UserContext.requireUserId(), favoriteId));
    }
}
