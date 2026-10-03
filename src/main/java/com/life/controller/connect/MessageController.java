package com.life.controller.connect;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.connect.MessageDTO;
import com.life.dto.connect.MessageRequest;
import com.life.service.connect.MessageService;

@RestController
@RequestMapping("/api/connect/messages")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(
            MessageService messageService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public MessageDTO sendMessage(
            @RequestHeader(value = "userId", required = false) Long userIdHeader,
            @RequestBody MessageRequest request
    ) {

        Long userId = (request != null && request.getSenderId() != null)
                ? request.getSenderId()
                : userIdHeader;

        MessageDTO savedMessage = messageService.sendMessage(
                userId,
                request
        );

        if (messagingTemplate != null && request.getReceiverId() != null) {
            try {
                messagingTemplate.convertAndSend(
                        "/topic/user/" + request.getReceiverId(),
                        savedMessage
                );
            } catch (Exception ignored) {
            }
        }

        return savedMessage;
    }

    @PostMapping("/call-log")
    public MessageDTO logCall(
            @RequestHeader(value = "userId", required = false) Long userIdHeader,
            @RequestBody MessageRequest request
    ) {
        if (request != null) {
            request.setMessageType("CALL_LOG");
        }
        return sendMessage(userIdHeader, request);
    }

    @DeleteMapping("/clear/{conversationId}")
    public Map<String, Object> clearChat(
            @RequestHeader("userId") Long userId,
            @PathVariable Long conversationId
    ) {
        messageService.clearConversationMessages(conversationId, userId);

        Map<String, Object> clearEvent = Map.of(
                "type", "CHAT_CLEARED",
                "conversationId", conversationId
        );

        if (messagingTemplate != null) {
            try {
                messagingTemplate.convertAndSend("/topic/user/" + userId, (Object) clearEvent);
            } catch (Exception ignored) {
            }
        }

        return Map.of("success", true, "message", "Chat cleared successfully", "conversationId", conversationId);
    }

    @GetMapping("/{conversationId}")
    public List<MessageDTO> getMessages(
            @RequestHeader("userId") Long userId,
            @PathVariable Long conversationId
    ) {

        return messageService.getMessages(
                userId,
                conversationId
        );
    }
}
