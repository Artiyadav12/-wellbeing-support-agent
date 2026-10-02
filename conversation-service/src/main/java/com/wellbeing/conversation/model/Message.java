package com.wellbeing.conversation.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Message {

    public enum Role { USER, ASSISTANT }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Lob
    @Column(nullable = false)
    private String content;

    // Optional: set when this turn referenced a specific claim, so you can
    // later query "which conversations touched claim CLM-4521".
    @Column(name = "referenced_claim_id")
    private String referencedClaimId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Message() {
    }

    public Message(Conversation conversation, Role role, String content) {
        this.conversation = conversation;
        this.role = role;
        this.content = content;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Conversation getConversation() { return conversation; }
    public void setConversation(Conversation conversation) { this.conversation = conversation; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getReferencedClaimId() { return referencedClaimId; }
    public void setReferencedClaimId(String referencedClaimId) { this.referencedClaimId = referencedClaimId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
