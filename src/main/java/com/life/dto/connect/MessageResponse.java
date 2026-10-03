package com.life.dto.connect;

import java.time.LocalDateTime;

public class MessageResponse {

	private Long id;
    private Long conversationId;

    private Long senderId;
    private String senderName;

    private Long receiverId;

    private String content;

    private boolean readStatus;

    private LocalDateTime createdAt;

    public MessageResponse(
            Long id,
            Long conversationId,
            Long senderId,
            String senderName,
            Long receiverId,
            String content,
            boolean readStatus,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.content = content;
        this.readStatus = readStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
