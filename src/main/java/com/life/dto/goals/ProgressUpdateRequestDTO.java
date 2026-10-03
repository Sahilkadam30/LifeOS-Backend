package com.life.dto.goals;

public class ProgressUpdateRequestDTO {

    private String note;

    public ProgressUpdateRequestDTO() {
    }

    public ProgressUpdateRequestDTO(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
