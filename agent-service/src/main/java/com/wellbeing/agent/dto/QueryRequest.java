package com.wellbeing.agent.dto;

import jakarta.validation.constraints.NotBlank;

public record QueryRequest(@NotBlank String question) {
}
