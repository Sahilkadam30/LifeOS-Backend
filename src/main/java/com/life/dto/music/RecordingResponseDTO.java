package com.life.dto.music;

import java.time.LocalDateTime;

import com.life.entity.music.MusicIdeaStatus;
import com.life.entity.music.Recording;
import com.life.entity.music.RecordingType;

public class RecordingResponseDTO {

    private Long id;
    private String title;
    private String description;
    private RecordingType recordingType;
    private String fileName;
    private String originalFileName;
    private String fileUrl;
    private Long fileSize;
    private Double duration;
    private String mimeType;
    private String thumbnailUrl;
    private boolean favorite;
    private MusicIdeaStatus status;
    private String practiceNotes;
    private Integer practiceMinutes;
    private Long projectId;
    private String projectName;
    private LocalDateTime recordedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecordingResponseDTO() {
    }

    public static RecordingResponseDTO fromEntity(Recording r) {
        if (r == null) return null;
        RecordingResponseDTO dto = new RecordingResponseDTO();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());
        dto.setRecordingType(r.getRecordingType());
        dto.setFileName(r.getFileName());
        dto.setOriginalFileName(r.getOriginalFileName());
        dto.setFileUrl(r.getFileUrl());
        dto.setFileSize(r.getFileSize());
        dto.setDuration(r.getDuration());
        dto.setMimeType(r.getMimeType());
        dto.setThumbnailUrl(r.getThumbnailUrl());
        dto.setFavorite(r.isFavorite());
        dto.setStatus(r.getStatus());
        dto.setPracticeNotes(r.getPracticeNotes());
        dto.setPracticeMinutes(r.getPracticeMinutes());
        if (r.getProject() != null) {
            dto.setProjectId(r.getProject().getId());
            dto.setProjectName(r.getProject().getName());
        }
        dto.setRecordedAt(r.getRecordedAt());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
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
