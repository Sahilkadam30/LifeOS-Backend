package com.life.dto.music;

import java.time.LocalDateTime;

import com.life.entity.music.MusicIdeaStatus;
import com.life.entity.music.RecordingType;

public class RecordingRequestDTO {

    private String title;
    private String description;
    private RecordingType recordingType;
    private LocalDateTime recordedAt;
    private Double duration;
    private Boolean favorite;
    private MusicIdeaStatus status;
    private String practiceNotes;
    private Integer practiceMinutes;
    private Long projectId;

    public RecordingRequestDTO() {
    }

    public RecordingRequestDTO(String title, String description, RecordingType recordingType,
                             LocalDateTime recordedAt, Double duration, Boolean favorite,
                             MusicIdeaStatus status, String practiceNotes,
                             Integer practiceMinutes, Long projectId) {
        this.title = title;
        this.description = description;
        this.recordingType = recordingType;
        this.recordedAt = recordedAt;
        this.duration = duration;
        this.favorite = favorite;
        this.status = status;
        this.practiceNotes = practiceNotes;
        this.practiceMinutes = practiceMinutes;
        this.projectId = projectId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecordingType getRecordingType() {
        return recordingType;
    }

    public void setRecordingType(RecordingType recordingType) {
        this.recordingType = recordingType;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public MusicIdeaStatus getStatus() {
        return status;
    }

    public void setStatus(MusicIdeaStatus status) {
        this.status = status;
    }

    public String getPracticeNotes() {
        return practiceNotes;
    }

    public void setPracticeNotes(String practiceNotes) {
        this.practiceNotes = practiceNotes;
    }

    public Integer getPracticeMinutes() {
        return practiceMinutes;
    }

    public void setPracticeMinutes(Integer practiceMinutes) {
        this.practiceMinutes = practiceMinutes;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
