package com.wellbeing.conversation.controller;

import com.wellbeing.conversation.dto.ConversationDtos.*;
import com.wellbeing.conversation.model.Conversation;
import com.wellbeing.conversation.model.Message;
import com.wellbeing.conversation.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    // POST /api/conversations  { "employeeId": "EMP-1001" }
    @PostMapping
    public StartConversationResponse startConversation(@Valid @RequestBody StartConversationRequest request) {
        Conversation conversation = conversationService.startConversation(request.employeeId());
        return new StartConversationResponse(conversation.getId(), conversation.getEmployeeId(), conversation.getCreatedAt());
    }

    // POST /api/conversations/{id}/messages  { "role": "USER", "content": "..." }
    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> appendMessage(@PathVariable UUID id,
                                                          @Valid @RequestBody AppendMessageRequest request) {
        try {
            Message.Role role = Message.Role.valueOf(request.role().toUpperCase());
            Message message = conversationService.appendMessage(id, role, request.content(), request.referencedClaimId());
            return ResponseEntity.ok(toResponse(message));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // invalid role value
        }
    }

    // GET /api/conversations/{id}/messages
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable UUID id) {
        try {
            List<MessageResponse> responses = conversationService.getMessages(id).stream()
                    .map(this::toResponse)
                    .toList();
            return ResponseEntity.ok(responses);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private MessageResponse toResponse(Message m) {
        return new MessageResponse(m.getId(), m.getRole(), m.getContent(), m.getReferencedClaimId(), m.getCreatedAt());
    }
}
