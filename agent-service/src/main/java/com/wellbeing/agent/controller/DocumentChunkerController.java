package com.wellbeing.agent.controller;

import com.wellbeing.agent.chunking.Chunk;
import com.wellbeing.agent.chunking.DocumentChunker;
import com.wellbeing.agent.dto.ChunkPreviewRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Lets you test your chunking logic directly, before embeddings or pgvector
 * are wired in. Paste the raw content of one of the policy-docs files into
 * the request body and check the chunks that come back look right.
 */
@RestController
@RequestMapping("/api/chunking")
public class DocumentChunkerController {

    private final DocumentChunker documentChunker;

    public DocumentChunkerController(DocumentChunker documentChunker) {
        this.documentChunker = documentChunker;
    }

    // POST /api/chunking/preview  { "documentName": "wellness-program-faq.md", "rawText": "...paste file contents..." }
    @PostMapping("/preview")
    public List<Chunk> preview(@Valid @RequestBody ChunkPreviewRequest request) {
        return documentChunker.chunk(request.documentName(), request.rawText());
    }
}
