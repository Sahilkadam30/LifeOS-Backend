package com.life.dto.studytracker;

public class WeeklyStudyDto {

    private String day;
    private Double hours;

    public WeeklyStudyDto() {
    }

    public WeeklyStudyDto(
            String day,
            Double hours) {

        this.day = day;
        this.hours = hours;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
