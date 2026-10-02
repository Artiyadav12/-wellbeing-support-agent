package com.wellbeing.agent.llm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Ollama /api/chat shapes with tool calling. Kept separate from
 * OllamaChatDtos so your working RAG path stays untouched.
 *
 * Model asks for a tool:
 *   message: { "role": "assistant", "content": "", "tool_calls": [
 *       { "function": { "name": "get_claim_status", "arguments": { "claimId": "CLM-4521" } } } ] }
 *
 * You reply with the result:
 *   { "role": "tool", "tool_name": "get_claim_status", "content": "...result text..." }
 */
public class OllamaToolDtos {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Message(String role,
                          String content,
                          @JsonProperty("tool_calls") List<ToolCall> toolCalls,
                          @JsonProperty("tool_name") String toolName) {

        public static Message system(String content) {
            return new Message("system", content, null, null);
        }

        public static Message user(String content) {
            return new Message("user", content, null, null);
        }

        public static Message tool(String toolName, String content) {
            return new Message("tool", content, null, toolName);
        }
    }

    public record ToolCall(FunctionCall function) {
    }

    public record FunctionCall(String name, Map<String, Object> arguments) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ChatRequest(String model,
                              List<Message> messages,
                              List<Map<String, Object>> tools,
                              boolean stream) {
    }

    public record ChatResponse(Message message) {
    }
}
