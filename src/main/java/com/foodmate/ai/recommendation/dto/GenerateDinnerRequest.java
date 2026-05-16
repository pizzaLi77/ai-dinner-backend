package com.foodmate.ai.recommendation.dto;

import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class GenerateDinnerRequest {
    @Size(max = 500)
    private String freeText;
    private List<String> selectedMoods = new ArrayList<>();
    private List<String> selectedTastes = new ArrayList<>();
    private String selectedTime;
    private List<String> selectedTools = new ArrayList<>();

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
        this.selectedMoods = selectedMoods == null ? new ArrayList<>() : selectedMoods;
    }

    public List<String> getSelectedTastes() {
        return selectedTastes;
    }

    public void setSelectedTastes(List<String> selectedTastes) {
        this.selectedTastes = selectedTastes == null ? new ArrayList<>() : selectedTastes;
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
        this.selectedTools = selectedTools == null ? new ArrayList<>() : selectedTools;
    }
}
