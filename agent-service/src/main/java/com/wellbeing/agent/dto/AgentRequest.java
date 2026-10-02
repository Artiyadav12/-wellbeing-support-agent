package com.wellbeing.agent.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * employeeId stands in for "who is logged in". In a real system it would come
 * from the auth token, not the request body.
 */
public record AgentRequest(@NotBlank String employeeId, @NotBlank String question) {
}
