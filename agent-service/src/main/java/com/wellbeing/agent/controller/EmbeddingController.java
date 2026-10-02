package com.wellbeing.agent.controller;

import com.wellbeing.agent.dto.EmbedPreviewRequest;
import com.wellbeing.agent.embedding.EmbeddingClient;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Lets you test your embedding client through the Spring app itself, not
 * just directly against Ollama - confirms the wiring (DTOs, RestClient,
 * bean injection) all works before this gets used inside real ingestion.
 */
@RestController
@RequestMapping("/api/embedding")
public class EmbeddingController {

    private final EmbeddingClient embeddingClient;

    public EmbeddingController(EmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    // POST /api/embedding/preview  { "text": "hello world" }
    @PostMapping("/preview")
    public Map<String, Object> preview(@Valid @RequestBody EmbedPreviewRequest request) {
        List<Float> vector = embeddingClient.embed(request.text());
        // Returning the dimension count alongside the vector - easier to
        // sanity-check "is this 768 numbers?" without scrolling through them all.
        return Map.of(
                "dimensions", vector.size(),
                "embedding", vector
        );
    }
}
