package com.wellbeing.agent.embedding;

import java.util.List;

/**
 * Matches Ollama's /api/embeddings request and response JSON shape exactly -
 * the same shape you just saw in Postman. Field names must match Ollama's
 * JSON exactly for Jackson to deserialize it automatically.
 */
class OllamaEmbeddingDtos {

    record EmbeddingRequest(String model, String prompt) {
    }

    record EmbeddingResponse(List<Float> embedding) {
    }
}
