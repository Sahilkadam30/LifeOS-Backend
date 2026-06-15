package com.life.dto.studytracker;

public class StudyHoursChartDto {

    private String label;
    private Double hours;

    public StudyHoursChartDto() {
    }

    public StudyHoursChartDto(
            String label,
            Double hours) {

        this.label = label;
        this.hours = hours;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
