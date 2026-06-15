package com.life.dto.studytracker;

public class SubjectProgressDto {

    private String subjectName;
    private Double progressPercentage;

    public SubjectProgressDto() {
    }

    public SubjectProgressDto(
            String subjectName,
            Double progressPercentage) {

        this.subjectName = subjectName;
        this.progressPercentage = progressPercentage;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(
            String subjectName) {

        this.subjectName = subjectName;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(
            Double progressPercentage) {

        this.progressPercentage = progressPercentage;
    }
}
