package com.wellbeing.agent.llm;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * One round trip to Ollama with tools attached. It does NOT loop - it returns
 * the model's next message, which either contains an answer or tool_calls.
 * Deciding what to do with that message is the agent loop's job (AgentService).
 */
@Component
public class OllamaAgentClient {

    private static final String MODEL = "llama3.2";

    private final RestClient restClient = RestClient.create("http://localhost:11434");

    public OllamaToolDtos.Message chat(List<OllamaToolDtos.Message> messages,
                                       List<Map<String, Object>> tools) {
        var request = new OllamaToolDtos.ChatRequest(MODEL, messages, tools, false);

        var response = restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OllamaToolDtos.ChatResponse.class);

        if (response == null || response.message() == null) {
            throw new IllegalStateException("Ollama returned no message");
        }
        return response.message();
    }
}
