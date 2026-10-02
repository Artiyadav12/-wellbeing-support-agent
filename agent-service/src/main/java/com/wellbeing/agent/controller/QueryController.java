package com.wellbeing.agent.controller;

import com.wellbeing.agent.dto.QueryRequest;
import com.wellbeing.agent.embedding.EmbeddingClient;
import com.wellbeing.agent.storage.PolicyChunkStore;
import com.wellbeing.agent.storage.RetrievedChunk;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Retrieval only, on purpose - no LLM call here yet. This lets you verify
 * the right chunks come back for a given question BEFORE adding an LLM on
 * top, so if an answer is ever wrong later, you'll already know whether
 * retrieval or generation is the culprit.
 */
@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final EmbeddingClient embeddingClient;
    private final PolicyChunkStore policyChunkStore;

    public QueryController(EmbeddingClient embeddingClient, PolicyChunkStore policyChunkStore) {
        this.embeddingClient = embeddingClient;
        this.policyChunkStore = policyChunkStore;
    }

    // POST /api/query  { "question": "what wellness expenses qualify" }
    @PostMapping
    public List<RetrievedChunk> query(@Valid @RequestBody QueryRequest request) {
        List<Float> queryEmbedding = embeddingClient.embed(request.question());
        return policyChunkStore.search(queryEmbedding, 3);
    }
}
