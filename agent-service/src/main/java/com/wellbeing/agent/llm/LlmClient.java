package com.wellbeing.agent.llm;

public interface LlmClient {

    /**
     * Sends one system prompt + one user prompt to the model and returns
     * the model's text reply.
     */
    String chat(String systemPrompt, String userPrompt);
}
