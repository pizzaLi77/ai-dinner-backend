package com.foodmate.ai.analytics.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.HashMap;
import java.util.Map;

public class TrackEventRequest {
    @NotBlank
    private String eventName;
    private Map<String, Object> properties = new HashMap<>();

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties == null ? new HashMap<>() : properties;
    }
}
