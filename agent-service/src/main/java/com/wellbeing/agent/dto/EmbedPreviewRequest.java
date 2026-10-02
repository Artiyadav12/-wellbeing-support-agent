package com.wellbeing.agent.dto;

import jakarta.validation.constraints.NotBlank;

public record EmbedPreviewRequest(@NotBlank String text) {
}
