package com.foodmate.ai.favorite;

import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import com.foodmate.ai.common.api.PageResponse;
import com.foodmate.ai.common.repository.InMemoryStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final InMemoryStore store;

    public FavoriteController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping
    public ApiResponse<PageResponse<Favorite>> list(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "20") int pageSize) {
        List<Favorite> all = store.findFavorites(UserContext.requireUserId());
        return ApiResponse.success(page(all, page, pageSize));
    }

    private PageResponse<Favorite> page(List<Favorite> all, int page, int pageSize) {
        int from = Math.min(Math.max(page - 1, 0) * pageSize, all.size());
        int to = Math.min(from + pageSize, all.size());
        return new PageResponse<>(all.subList(from, to), page, pageSize, all.size());
    }
}
