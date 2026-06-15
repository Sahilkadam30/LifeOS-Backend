package com.life.dto.studytracker;

public class SkillSummaryDto {

    private String skillName;
    private String level;
    private Double progressPercentage;

    public SkillSummaryDto() {
    }

    public SkillSummaryDto(
            String skillName,
            String level,
            Double progressPercentage) {

        this.skillName = skillName;
        this.level = level;
        this.progressPercentage =
                progressPercentage;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(
            String skillName) {

        this.skillName = skillName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(
            String level) {

        this.level = level;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(
            Double progressPercentage) {

        this.progressPercentage =
                progressPercentage;
    }
}
