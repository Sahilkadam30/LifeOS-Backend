package com.life.dto.music;

public class MusicStatsDTO {

    private long totalRecordings;
    private long totalPracticeMinutes;
    private String totalPracticeFormatted;
    private long songsCount;
    private long singingCount;
    private long instrumentsCount;
    private long musicIdeasCount;
    private long favoritesCount;
    private long practiceSessionsCount;
    private long weeklyPracticeSessions;
    private long monthlyPracticeSessions;
    private long completedProjectsCount;

    public MusicStatsDTO() {
    }

    public long getTotalRecordings() {
        return totalRecordings;
    }

    public void setTotalRecordings(long totalRecordings) {
        this.totalRecordings = totalRecordings;
    }

    public long getTotalPracticeMinutes() {
        return totalPracticeMinutes;
    }

    public void setTotalPracticeMinutes(long totalPracticeMinutes) {
        this.totalPracticeMinutes = totalPracticeMinutes;
    }

    public String getTotalPracticeFormatted() {
        return totalPracticeFormatted;
    }

    public void setTotalPracticeFormatted(String totalPracticeFormatted) {
        this.totalPracticeFormatted = totalPracticeFormatted;
    }

    public long getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(long songsCount) {
        this.songsCount = songsCount;
    }

    public long getSingingCount() {
        return singingCount;
    }

    public void setSingingCount(long singingCount) {
        this.singingCount = singingCount;
    }

    public long getInstrumentsCount() {
        return instrumentsCount;
    }

    public void setInstrumentsCount(long instrumentsCount) {
        this.instrumentsCount = instrumentsCount;
    }

    public long getMusicIdeasCount() {
        return musicIdeasCount;
    }

    public void setMusicIdeasCount(long musicIdeasCount) {
        this.musicIdeasCount = musicIdeasCount;
    }

    public long getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(long favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public long getPracticeSessionsCount() {
        return practiceSessionsCount;
    }

    public void setPracticeSessionsCount(long practiceSessionsCount) {
        this.practiceSessionsCount = practiceSessionsCount;
    }

    public long getWeeklyPracticeSessions() {
        return weeklyPracticeSessions;
    }

    public void setWeeklyPracticeSessions(long weeklyPracticeSessions) {
        this.weeklyPracticeSessions = weeklyPracticeSessions;
    }

    public long getMonthlyPracticeSessions() {
        return monthlyPracticeSessions;
    }

    public void setMonthlyPracticeSessions(long monthlyPracticeSessions) {
        this.monthlyPracticeSessions = monthlyPracticeSessions;
    }

    public long getCompletedProjectsCount() {
        return completedProjectsCount;
    }

    public void setCompletedProjectsCount(long completedProjectsCount) {
        this.completedProjectsCount = completedProjectsCount;
    }
}
