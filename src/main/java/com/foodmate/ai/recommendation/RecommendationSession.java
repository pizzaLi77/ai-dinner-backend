package com.foodmate.ai.recommendation;

import com.foodmate.ai.recommendation.dto.GenerateDinnerRequest;

import java.time.LocalDateTime;
import java.util.List;

public class RecommendationSession {
    private Long id;
    private Long userId;
    private String openid;
    private String freeText;
    private List<String> selectedMoods;
    private List<String> selectedTastes;
    private String selectedTime;
    private List<String> selectedTools;
    private String profileSnapshot;
    private String llmProvider;
    private String llmModel;
    private String promptVersion;
    private String status = "success";
    private String errorMessage;
    private LocalDateTime createdAt;

    public static RecommendationSession from(Long userId, String openid, GenerateDinnerRequest request,
                                             String profileSnapshot, String llmProvider, String llmModel,
                                             String promptVersion) {
        RecommendationSession session = new RecommendationSession();
        session.setUserId(userId);
        session.setOpenid(openid);
        session.setFreeText(request.getFreeText());
        session.setSelectedMoods(request.getSelectedMoods());
        session.setSelectedTastes(request.getSelectedTastes());
        session.setSelectedTime(request.getSelectedTime());
        session.setSelectedTools(request.getSelectedTools());
        session.setProfileSnapshot(profileSnapshot);
        session.setLlmProvider(llmProvider);
        session.setLlmModel(llmModel);
        session.setPromptVersion(promptVersion);
        session.setCreatedAt(LocalDateTime.now());
        return session;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public List<String> getSelectedMoods() {
        return selectedMoods;
    }

    public void setSelectedMoods(List<String> selectedMoods) {
        this.selectedMoods = selectedMoods;
    }

    public List<String> getSelectedTastes() {
        return selectedTastes;
    }

    public void setSelectedTastes(List<String> selectedTastes) {
        this.selectedTastes = selectedTastes;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public List<String> getSelectedTools() {
        return selectedTools;
    }

    public void setSelectedTools(List<String> selectedTools) {
        this.selectedTools = selectedTools;
    }

    public String getProfileSnapshot() {
        return profileSnapshot;
    }

    public void setProfileSnapshot(String profileSnapshot) {
        this.profileSnapshot = profileSnapshot;
    }

    public String getLlmProvider() {
        return llmProvider;
    }

    public void setLlmProvider(String llmProvider) {
        this.llmProvider = llmProvider;
    }

    public String getLlmModel() {
        return llmModel;
    }

    public void setLlmModel(String llmModel) {
        this.llmModel = llmModel;
    }

    public String getPromptVersion() {
        return promptVersion;
    }

    public void setPromptVersion(String promptVersion) {
        this.promptVersion = promptVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
