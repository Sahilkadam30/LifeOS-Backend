package com.life.dto.finance;

public class InvestmentChartDto {

    private String month;

    private Double investedAmount;

    private Double currentValue;

    public InvestmentChartDto() {}

    public InvestmentChartDto(
            String month,
            Double investedAmount,
            Double currentValue) {

        this.month = month;
        this.investedAmount = investedAmount;
        this.currentValue = currentValue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(
            Double investedAmount) {

        this.investedAmount =
                investedAmount;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(
            Double currentValue) {

        this.currentValue =
                currentValue;
    }
}