package com.wellbeing.agent.llm;

import java.util.List;

/**
 * Matches Ollama's POST /api/chat request/response JSON.
 *
 * Request:  { "model": "...", "messages": [ {"role": "system", "content": "..."}, {"role": "user", "content": "..."} ], "stream": false }
 * Response: { "message": { "role": "assistant", "content": "..." }, ...other fields ignored... }
 *
 * "stream": false matters - without it Ollama sends the answer back as many
 * small JSON lines instead of one object, and parsing will break.
 */
class OllamaChatDtos {

    record ChatMessage(String role, String content) {
    }

    record ChatRequest(String model, List<ChatMessage> messages, boolean stream) {
    }

    record ChatResponse(ChatMessage message) {
    }
}
