package com.life.dto.explore;

import java.time.LocalDateTime;

public class ExploreCommentDTO {

    private Long id;
    private String username;
    private String text;
    private LocalDateTime createdAt;

    public ExploreCommentDTO() {
    }

    public ExploreCommentDTO(Long id, String username, String text, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
