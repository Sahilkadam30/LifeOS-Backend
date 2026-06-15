package com.life.dto.studytracker;

import java.util.List;

public class SkillDashboardResponse {

    private Long totalSubjects;
    private Double totalStudyHours;
    private Long journalEntries;
    private Double averageProgress;
    private Long achievements;
    private Long todayStudySessions;

    private List<WeeklyStudyDto> weeklyStudy;
    private List<SubjectProgressDto> subjectProgress;
    private List<SkillSummaryDto> skillSummary;

    public SkillDashboardResponse() {
    }

    public Long getTotalSubjects() {
        return totalSubjects;
    }

    public void setTotalSubjects(Long totalSubjects) {
        this.totalSubjects = totalSubjects;
    }

    public Double getTotalStudyHours() {
        return totalStudyHours;
    }

    public void setTotalStudyHours(Double totalStudyHours) {
        this.totalStudyHours = totalStudyHours;
    }

    public Long getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(Long journalEntries) {
        this.journalEntries = journalEntries;
    }

    public Double getAverageProgress() {
        return averageProgress;
    }

    public void setAverageProgress(Double averageProgress) {
        this.averageProgress = averageProgress;
    }

    public Long getAchievements() {
        return achievements;
    }

    public void setAchievements(Long achievements) {
        this.achievements = achievements;
    }

    public Long getTodayStudySessions() {
        return todayStudySessions;
    }

    public void setTodayStudySessions(Long todayStudySessions) {
        this.todayStudySessions = todayStudySessions;
    }

    public List<WeeklyStudyDto> getWeeklyStudy() {
        return weeklyStudy;
    }

    public void setWeeklyStudy(List<WeeklyStudyDto> weeklyStudy) {
        this.weeklyStudy = weeklyStudy;
    }

    public List<SubjectProgressDto> getSubjectProgress() {
        return subjectProgress;
    }

    public void setSubjectProgress(
            List<SubjectProgressDto> subjectProgress) {
        this.subjectProgress = subjectProgress;
    }

    public List<SkillSummaryDto> getSkillSummary() {
        return skillSummary;
    }

    public void setSkillSummary(
            List<SkillSummaryDto> skillSummary) {
        this.skillSummary = skillSummary;
    }
}
