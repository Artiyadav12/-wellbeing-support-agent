package com.wellbeing.agent.controller;

import com.wellbeing.agent.chunking.Chunk;
import com.wellbeing.agent.chunking.DocumentChunker;
import com.wellbeing.agent.dto.ChunkPreviewRequest;
import com.wellbeing.agent.embedding.EmbeddingClient;
import com.wellbeing.agent.storage.PolicyChunkStore;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Ties your three already-working pieces together: chunk the document,
 * embed each chunk, store each (chunk, embedding) pair. This orchestration
 * is plumbing - the interesting logic already lives in your chunker,
 * embedder, and store implementations.
 */
@RestController
@RequestMapping("/api/ingest")
public class IngestionController {

    private final DocumentChunker documentChunker;
    private final EmbeddingClient embeddingClient;
    private final PolicyChunkStore policyChunkStore;

    public IngestionController(DocumentChunker documentChunker,
                                EmbeddingClient embeddingClient,
                                PolicyChunkStore policyChunkStore) {
        this.documentChunker = documentChunker;
        this.embeddingClient = embeddingClient;
        this.policyChunkStore = policyChunkStore;
    }

    // POST /api/ingest  { "documentName": "wellness-program-faq.md", "rawText": "...full file contents..." }
    @PostMapping
    public Map<String, Object> ingest(@Valid @RequestBody ChunkPreviewRequest request) {
        List<Chunk> chunks = documentChunker.chunk(request.documentName(), request.rawText());

        for (Chunk chunk : chunks) {
            List<Float> embedding = embeddingClient.embed(chunk.text());
            policyChunkStore.save(chunk, embedding);
        }

        return Map.of(
                "documentName", request.documentName(),
                "chunksIngested", chunks.size()
        );
    }
}
