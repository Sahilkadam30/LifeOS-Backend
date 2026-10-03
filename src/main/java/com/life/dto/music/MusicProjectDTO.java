package com.life.dto.music;

import java.time.LocalDateTime;

import com.life.entity.music.MusicIdeaStatus;
import com.life.entity.music.MusicProject;

public class MusicProjectDTO {

    private Long id;
    private String name;
    private String description;
    private MusicIdeaStatus status;
    private int recordingsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MusicProjectDTO() {
    }

    public static MusicProjectDTO fromEntity(MusicProject p, int count) {
        if (p == null) return null;
        MusicProjectDTO dto = new MusicProjectDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setStatus(p.getStatus());
        dto.setRecordingsCount(count);
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MusicIdeaStatus getStatus() {
        return status;
    }

    public void setStatus(MusicIdeaStatus status) {
        this.status = status;
    }

    public int getRecordingsCount() {
        return recordingsCount;
    }

    public void setRecordingsCount(int recordingsCount) {
        this.recordingsCount = recordingsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
