package com.foodmate.ai.recommendation.service;

import com.foodmate.ai.profile.UserProfile;
import com.foodmate.ai.recommendation.dto.DinnerRecommendationDTO;
import com.foodmate.ai.recommendation.dto.GenerateDinnerRequest;
import com.foodmate.ai.recommendation.dto.MissingIngredientDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FallbackRecommendationFactory {
    public List<DinnerRecommendationDTO> create(GenerateDinnerRequest request, UserProfile profile) {
        boolean wantsSpicy = request.getSelectedTastes().contains("微辣") || profile.getPreferredTastes().contains("微辣");
        return List.of(
                build("easy", "番茄豆腐鸡蛋汤饭", "热乎省事，一个锅完成，适合今天累的时候。", 15, "easy",
                        List.of("鸡蛋", "番茄", "豆腐", "剩米饭"),
                        List.of(new MissingIngredientDTO("葱花", true)),
                        List.of("番茄切块，豆腐切小块。", "锅里少油炒软番茄。", "加水煮开后放入豆腐。", "淋入蛋液形成蛋花。", "加入剩米饭煮2分钟，盐和生抽调味。"),
                        List.of("没有豆腐可以换成青菜或菌菇。"),
                        List.of("热乎", "少洗锅", "快手", "一锅出")),
                build("satisfying", wantsSpicy ? "微辣番茄鸡蛋盖饭" : "酱香番茄鸡蛋盖饭",
                        "比普通番茄炒蛋更下饭，适合想吃满足一点。", 18, "easy",
                        List.of("鸡蛋", "番茄", "剩米饭"),
                        List.of(new MissingIngredientDTO(wantsSpicy ? "辣椒酱" : "黄豆酱", true)),
                        List.of("鸡蛋炒熟盛出。", "番茄炒出汁。", "加入鸡蛋回锅。", "加少量生抽和酱料。", "盖在热米饭上。"),
                        List.of("不想吃辣可以不放辣椒酱。"),
                        wantsSpicy ? List.of("下饭", "微辣", "快手") : List.of("下饭", "酱香", "快手")),
                build("healthy", "豆腐蛋花番茄汤", "清爽少油但有热汤，适合累的时候舒服吃一顿。", 15, "easy",
                        List.of("豆腐", "鸡蛋", "番茄"),
                        List.of(),
                        List.of("番茄切块煮出汤底。", "加入豆腐煮3分钟。", "淋入蛋液。", "加盐和白胡椒调味。"),
                        List.of("想更饱可以配剩米饭。"),
                        List.of("清淡", "少油", "热乎"))
        );
    }

    private DinnerRecommendationDTO build(String type, String name, String reason, int minutes, String difficulty,
                                          List<String> ingredients, List<MissingIngredientDTO> missing,
                                          List<String> steps, List<String> substitutions, List<String> tags) {
        DinnerRecommendationDTO dto = new DinnerRecommendationDTO();
        dto.setType(type);
        dto.setTypeLabel(typeLabel(type));
        dto.setName(name);
        dto.setReason(reason);
        dto.setEstimatedTimeMinutes(minutes);
        dto.setDifficulty(difficulty);
        dto.setIngredientsUsed(ingredients);
        dto.setMissingIngredients(missing);
        dto.setSteps(steps);
        dto.setSubstitutions(substitutions);
        dto.setTags(tags);
        dto.setCaution("");
        return dto;
    }

    private String typeLabel(String type) {
        return switch (type) {
            case "easy" -> "\u6700\u7701\u4e8b";
            case "satisfying" -> "\u6700\u6ee1\u8db3";
            case "healthy" -> "\u6700\u5065\u5eb7";
            default -> type;
        };
    }
}
