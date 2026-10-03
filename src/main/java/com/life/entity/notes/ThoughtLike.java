package com.life.entity.notes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ThoughtLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long writingId;

    private String username;

    public ThoughtLike() {
    }

    public ThoughtLike(Long writingId, String username) {
        this.writingId = writingId;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWritingId() {
        return writingId;
    }

    public void setWritingId(Long writingId) {
        this.writingId = writingId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
