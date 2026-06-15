package com.life.dto.finance;

public class ExpenseChartDto {

    private String category;
    private Double amount;

    public ExpenseChartDto() {
    }

    public ExpenseChartDto(
            String category,
            Double amount) {

        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(
            String category) {

        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(
            Double amount) {

        this.amount = amount;
    }
}