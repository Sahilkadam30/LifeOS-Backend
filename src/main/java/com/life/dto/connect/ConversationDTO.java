package com.life.dto.connect;

import java.time.LocalDateTime;

public class ConversationDTO {

	private Long id;

    private Long userId;
    private String username;
    private String firstName;
    private String lastName;

    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public ConversationDTO(
            Long id,
            Long userId,
            String username,
            String firstName,
            String lastName,
            String lastMessage,
            LocalDateTime lastMessageTime
    ) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }
}
