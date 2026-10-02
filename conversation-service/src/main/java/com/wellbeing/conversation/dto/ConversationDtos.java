package com.wellbeing.conversation.dto;

import com.wellbeing.conversation.model.Message;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public class ConversationDtos {

    public record StartConversationRequest(@NotBlank String employeeId) {}

    public record StartConversationResponse(UUID conversationId, String employeeId, Instant createdAt) {}

    public record AppendMessageRequest(
            @NotBlank String role,          // "USER" or "ASSISTANT"
            @NotBlank String content,
            String referencedClaimId        // optional
    ) {}

    public record MessageResponse(
            UUID id,
            Message.Role role,
            String content,
            String referencedClaimId,
            Instant createdAt
    ) {}
}
