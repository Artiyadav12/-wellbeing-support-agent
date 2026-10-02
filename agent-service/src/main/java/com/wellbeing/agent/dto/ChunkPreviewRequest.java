package com.wellbeing.agent.dto;

import jakarta.validation.constraints.NotBlank;

public record ChunkPreviewRequest(
        @NotBlank String documentName,
        @NotBlank String rawText
) {
}
