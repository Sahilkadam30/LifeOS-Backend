package com.life.dto.goals;

public class GoalStatsDTO {

    private long totalGoals;
    private long completed;
    private long inProgress;
    private long notStarted;
    private long paused;
    private long abandoned;
    private long shortTermGoals;
    private long longTermGoals;
    private double overallProgress;

    public GoalStatsDTO() {
    }

    public long getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(long totalGoals) {
        this.totalGoals = totalGoals;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public long getInProgress() {
        return inProgress;
    }

    public void setInProgress(long inProgress) {
        this.inProgress = inProgress;
    }

    public long getNotStarted() {
        return notStarted;
    }

    public void setNotStarted(long notStarted) {
        this.notStarted = notStarted;
    }

    public long getPaused() {
        return paused;
    }

    public void setPaused(long paused) {
        this.paused = paused;
    }

    public long getAbandoned() {
        return abandoned;
    }

    public void setAbandoned(long abandoned) {
        this.abandoned = abandoned;
    }

    public long getShortTermGoals() {
        return shortTermGoals;
    }

    public void setShortTermGoals(long shortTermGoals) {
        this.shortTermGoals = shortTermGoals;
    }

    public long getLongTermGoals() {
        return longTermGoals;
    }

    public void setLongTermGoals(long longTermGoals) {
        this.longTermGoals = longTermGoals;
    }

    public double getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(double overallProgress) {
        this.overallProgress = overallProgress;
    }
}
