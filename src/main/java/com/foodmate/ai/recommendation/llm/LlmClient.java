package com.foodmate.ai.recommendation.llm;

public interface LlmClient {
    String chat(String systemPrompt, String userPrompt);
}
