package com.foodmate.ai.recommendation.prompt;

import com.foodmate.ai.profile.UserProfile;
import com.foodmate.ai.recommendation.dto.GenerateDinnerRequest;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {
    public String systemPrompt() {
        return """
                你是一个懂中文家庭做饭场景的 AI 晚餐决策助手。
                你的目标不是展示很多菜谱，而是帮助用户快速决定今晚吃什么。
                推荐必须优先使用用户已有食材，符合今天心情和口味，不超过时间限制，不需要复杂厨具。
                不要推荐用户明确不喜欢或不能吃的食材。每次只输出 3 个方案。
                三个方案要有明显差异：最省事 easy、最满足 satisfying、最健康 healthy。
                步骤要简单，适合普通人下厨。你必须只输出合法 JSON，不要输出 Markdown。
                """;
    }

    public String build(GenerateDinnerRequest request, UserProfile profile) {
        return """
                请根据以下信息推荐 3 个今晚晚餐方案。

                【用户今天输入】
                自由输入：%s
                心情标签：%s
                口味标签：%s
                时间标签：%s
                厨具标签：%s

                【用户长期饮食画像】
                %s

                【结构化画像】
                偏好口味：%s
                不喜欢口味：%s
                常用食材：%s
                喜欢食材：%s
                不喜欢食材：%s
                偏好做饭时间：%d 分钟
                偏好难度：%s
                常用厨具：%s
                健康目标：%s

                【输出要求】
                请严格输出如下 JSON 结构：
                {
                  "recommendations": [
                    {
                      "type": "easy",
                      "name": "菜名",
                      "reason": "为什么适合用户今天的状态，控制在40字以内",
                      "estimatedTimeMinutes": 15,
                      "difficulty": "easy",
                      "ingredientsUsed": ["食材1", "食材2"],
                      "missingIngredients": [{ "name": "食材或调料", "optional": true }],
                      "steps": ["步骤1", "步骤2", "步骤3"],
                      "substitutions": ["没有A可以用B替代"],
                      "tags": ["少洗锅", "热乎", "一人食"],
                      "caution": ""
                    }
                  ]
                }
                """.formatted(
                nullToEmpty(request.getFreeText()),
                request.getSelectedMoods(),
                request.getSelectedTastes(),
                nullToEmpty(request.getSelectedTime()),
                request.getSelectedTools(),
                profile.getPreferenceSummary(),
                profile.getPreferredTastes(),
                profile.getDislikedTastes(),
                profile.getCommonIngredients(),
                profile.getFavoriteIngredients(),
                profile.getDislikedIngredients(),
                profile.getPreferredCookingTimeMinutes(),
                profile.getPreferredDifficulty(),
                profile.getCookingTools(),
                profile.getHealthGoals()
        );
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
