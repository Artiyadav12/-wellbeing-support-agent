package com.wellbeing.conversation.service;

import com.wellbeing.conversation.model.Conversation;
import com.wellbeing.conversation.model.Message;
import com.wellbeing.conversation.repository.ConversationRepository;
import com.wellbeing.conversation.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationService(ConversationRepository conversationRepository,
                                MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    public Conversation startConversation(String employeeId) {
        return conversationRepository.save(new Conversation(employeeId));
    }

    public Message appendMessage(UUID conversationId, Message.Role role, String content, String referencedClaimId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with id " + conversationId));

        Message message = new Message(conversation, role, content);
        message.setReferencedClaimId(referencedClaimId);
        return messageRepository.save(message);
    }

    public List<Message> getMessages(UUID conversationId) {
        // Confirms the conversation exists, so callers get a clean 404 instead of an empty list on a typo'd id.
        if (!conversationRepository.existsById(conversationId)) {
            throw new NoSuchElementException("No conversation found with id " + conversationId);
        }
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
