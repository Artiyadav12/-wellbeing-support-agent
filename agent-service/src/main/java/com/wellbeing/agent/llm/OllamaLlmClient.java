package com.wellbeing.agent.llm;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OllamaLlmClient implements LlmClient {

    private static final String MODEL = "llama3.2";

    private final RestClient restClient = RestClient.create("http://localhost:11434");

    @Override
    public String chat(String systemPrompt, String userPrompt) {
        var request = new OllamaChatDtos.ChatRequest(
                MODEL,
                List.of(
                        new OllamaChatDtos.ChatMessage("system", systemPrompt),
                        new OllamaChatDtos.ChatMessage("user", userPrompt)
                ),
                false
        );

        var response = restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OllamaChatDtos.ChatResponse.class);

        if (response == null || response.message() == null) {
            throw new IllegalStateException("Ollama returned no message");
        }
        return response.message().content();
    }
}