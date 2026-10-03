package com.life.controller.connect;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.connect.ConversationDTO;
import com.life.dto.connect.UserSuggestionDTO;
import com.life.entity.connect.Message;
import com.life.repository.connect.MessageRepository;
import com.life.service.connect.ConnectService;
import com.life.service.connect.MessageService;

@RestController
@RequestMapping("/api/connect")
public class ConnectController {

    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final ConnectService connectservice;
    private final org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

    public ConnectController(
            MessageService messageService,
            MessageRepository messageRepository,
            ConnectService connectservice,
            org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate
    ) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
        this.connectservice = connectservice;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/messages/{conversationId}/search")
    public List<Message> searchMessages(
            @PathVariable Long conversationId,
            @RequestParam String keyword
    ) {

        return messageRepository
                .findByConversationIdAndContentContainingIgnoreCaseOrderByCreatedAtDesc(
                        conversationId,
                        keyword
                );
    }
    
    @GetMapping("/conversations")
    public List<ConversationDTO> getConversations(
            @RequestHeader("userId") Long userId
    ) {
        return connectservice.getMyConversations(userId);
    }

    @PostMapping("/conversation/{userId}")
    public ConversationDTO createConversation(
            @RequestHeader("userId") Long currentUserId,
            @PathVariable Long userId
    ) {
        return connectservice.createOrGetConversationDTO(currentUserId, userId);
    }

    @DeleteMapping("/conversations/{conversationId}")
    public Map<String, Object> deleteConversation(
            @PathVariable Long conversationId,
            @RequestHeader("userId") Long userId
    ) {
        Long otherUserId = connectservice.deleteConversation(conversationId, userId);

        if (messagingTemplate != null) {
            try {
                Map<String, Object> event = Map.of(
                        "type", "CONVERSATION_DELETED",
                        "conversationId", conversationId
                );
                messagingTemplate.convertAndSend("/topic/user/" + userId, (Object) event);
                if (otherUserId != null) {
                    messagingTemplate.convertAndSend("/topic/user/" + otherUserId, (Object) event);
                }
            } catch (Exception ignored) {
            }
        }

        return Map.of("success", true, "message", "Conversation deleted successfully", "conversationId", conversationId);
    }

    @PutMapping("/messages/{messageId}")
    public Message editMessage(
            @PathVariable Long messageId,
            @RequestHeader("userId") Long userId,
            @RequestBody Map<String, String> body
    ) {

        return messageService.editMessage(
                messageId,
                userId,
                body.get("content")
        );
    }

    @DeleteMapping("/messages/{messageId}")
    public Message deleteMessage(
            @PathVariable Long messageId,
            @RequestHeader("userId") Long userId
    ) {

        return messageService.deleteMessage(
                messageId,
                userId
        );
    }

    @PutMapping("/messages/{messageId}/delivered")
    public Message delivered(
            @PathVariable Long messageId
    ) {

        return messageService.markDelivered(
                messageId
        );
    }

    @PutMapping("/messages/{messageId}/read")
    public Message read(
            @PathVariable Long messageId
    ) {

        return messageService.markRead(
                messageId
        );
    }
    
    @GetMapping("/users")
    public List<UserSuggestionDTO> searchUsers(
            @RequestHeader("userId") Long userId,
            @RequestParam(defaultValue = "") String search
    ) {
        return connectservice.searchUsers(userId, search);
    }
}