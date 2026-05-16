package com.foodmate.ai.dinnerplan;

import com.foodmate.ai.auth.UserContext;
import com.foodmate.ai.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dinner-plans")
public class DinnerPlanController {
    private final DinnerPlanService dinnerPlanService;

    public DinnerPlanController(DinnerPlanService dinnerPlanService) {
        this.dinnerPlanService = dinnerPlanService;
    }

    @GetMapping("/today/items")
    public ApiResponse<List<TodayDinnerPlanItem>> today() {
        return ApiResponse.success(dinnerPlanService.today(UserContext.requireUserId()));
    }

    @PostMapping("/today/items")
    public ApiResponse<TodayDinnerPlanItem> add(@Valid @RequestBody AddDinnerPlanItemRequest request) {
        return ApiResponse.success(dinnerPlanService.add(UserContext.requireUserId(), request));
    }
}
