package com.life.service.connect;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.life.dto.connect.MessageDTO;
import com.life.dto.connect.MessageRequest;
import com.life.entity.connect.Conversation;
import com.life.entity.connect.Message;
import com.life.repository.connect.ConversationRepository;
import com.life.repository.connect.MessageRepository;

@Service
public class MessageService {

	private final MessageRepository messageRepository;
	private final ConversationRepository conversationRepository;

    public MessageService(MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    public MessageDTO sendMessage(
            Long senderId,
            MessageRequest request
    ) {

        if (request == null ||
                request.getReceiverId() == null ||
                request.getConversationId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Receiver and conversation are required"
            );
        }

        boolean isCallLog = "CALL_LOG".equalsIgnoreCase(request.getMessageType());

        if (!isCallLog && (request.getContent() == null || request.getContent().trim().isEmpty())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Content is required for chat messages"
            );
        }

        String content = request.getContent() != null ? request.getContent().trim() : "";
        if (isCallLog && content.isEmpty()) {
            String cType = "video".equalsIgnoreCase(request.getCallType()) ? "Video call" : "Audio call";
            String cStatus = request.getCallStatus() != null ? request.getCallStatus().toUpperCase() : "COMPLETED";
            if ("MISSED".equals(cStatus)) {
                content = "Missed " + cType.toLowerCase();
            } else if ("REJECTED".equals(cStatus)) {
                content = "Declined " + cType.toLowerCase();
            } else {
                int sec = request.getCallDuration() != null ? request.getCallDuration() : 0;
                String durStr = String.format("%02d:%02d", sec / 60, sec % 60);
                content = cType + " ended (" + durStr + ")";
            }
        }

        Message message = new Message();

        message.setConversationId(request.getConversationId());
        message.setSenderId(senderId);
        message.setReceiverId(request.getReceiverId());
        message.setContent(content);
        message.setStatus(Message.MessageStatus.SENT);
        message.setMessageType(isCallLog ? "CALL_LOG" : "TEXT");
        message.setCallType(request.getCallType());
        message.setCallStatus(request.getCallStatus());
        message.setCallDuration(request.getCallDuration() != null ? request.getCallDuration() : 0);

        Message savedMessage = messageRepository.save(message);

        // Update conversation's updatedAt timestamp
        conversationRepository.findById(request.getConversationId()).ifPresent(conv -> {
            conv.setUpdatedAt(LocalDateTime.now());
            conversationRepository.save(conv);
        });

        return convertToResponse(savedMessage);
    }

    public void clearConversationMessages(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"
                ));

        if (!conversation.getUserOne().getId().equals(userId) &&
                !conversation.getUserTwo().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not a participant in this conversation"
            );
        }

        messageRepository.deleteByConversationId(conversationId);

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);
    }

    public List<MessageDTO> getMessages(
            Long userId,
            Long conversationId
    ) {

        List<Message> messages =
                messageRepository
                        .findByConversationIdOrderByCreatedAtAsc(
                                conversationId
                        );

        return messages.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private MessageDTO convertToResponse(Message message) {

        MessageDTO response = new MessageDTO();

        response.setId(message.getId());
        response.setConversationId(message.getConversationId());
        response.setSenderId(message.getSenderId());
        response.setReceiverId(message.getReceiverId());
        response.setContent(message.getContent());
        response.setStatus(message.getStatus());
        response.setEdited(message.isEdited());
        response.setDeleted(message.isDeleted());
        response.setCreatedAt(message.getCreatedAt());
        response.setUpdatedAt(message.getUpdatedAt());
        response.setMessageType(message.getMessageType());
        response.setCallType(message.getCallType());
        response.setCallStatus(message.getCallStatus());
        response.setCallDuration(message.getCallDuration());

        return response;
    }
    public Message markDelivered(Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message not found with id: " + messageId
                ));

        message.setStatus(
                Message.MessageStatus.DELIVERED
        );

        return messageRepository.save(message);
    }

    public Message markRead(Long messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message not found with id: " + messageId
                ));

        message.setStatus(
                Message.MessageStatus.READ
        );

        return messageRepository.save(message);
    }

    public Message editMessage(
            Long messageId,
            Long userId,
            String content
    ) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message not found with id: " + messageId
                ));

        if (!message.getSenderId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot edit this message"
            );
        }

        if (message.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deleted message cannot be edited"
            );
        }

        message.setContent(content);
        message.setEdited(true);

        return messageRepository.save(message);
    }

    public Message deleteMessage(
            Long messageId,
            Long userId
    ) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message not found with id: " + messageId
                ));

        if (!message.getSenderId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot delete this message"
            );
        }

        message.setDeleted(true);
        message.setContent("This message was deleted");

        return messageRepository.save(message);
    }
}
