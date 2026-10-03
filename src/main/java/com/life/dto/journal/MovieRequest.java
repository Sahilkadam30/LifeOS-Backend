package com.life.dto.journal;

import com.life.entity.journal.MediaType;
import com.life.entity.journal.MovieStatus;

public class MovieRequest {
    private String title;
    private MediaType type;
    private MovieStatus status;
    private String description;

    public MovieRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
