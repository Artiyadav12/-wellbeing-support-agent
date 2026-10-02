package com.wellbeing.agent.controller;

import com.wellbeing.agent.dto.AskResponse;
import com.wellbeing.agent.dto.QueryRequest;
import com.wellbeing.agent.rag.RagService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ask")
public class AskController {

    private final RagService ragService;

    public AskController(RagService ragService) {
        this.ragService = ragService;
    }

    // POST /api/ask  { "question": "why is my claim stuck in review" }
    @PostMapping
    public AskResponse ask(@Valid @RequestBody QueryRequest request) {
        return ragService.answer(request.question());
    }
}
