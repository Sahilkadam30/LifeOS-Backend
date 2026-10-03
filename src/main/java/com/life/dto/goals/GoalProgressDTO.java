package com.life.dto.goals;

public class GoalProgressDTO {

    private Integer progressPercentage;

    public GoalProgressDTO() {
    }

    public GoalProgressDTO(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}
