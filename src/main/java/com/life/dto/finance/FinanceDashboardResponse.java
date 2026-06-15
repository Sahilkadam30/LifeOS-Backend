package com.life.dto.finance;

import java.util.List;

public class FinanceDashboardResponse {

	private Double currentSavings;
    private Double monthlyExpenses;
    private Double investmentValue;
    private Double netGrowth;

    private List<SavingsChartDto> savingsTrend;

    private List<InvestmentChartDto> investmentTrend;

    private List<ExpenseChartDto> expenseBreakdown;

    public FinanceDashboardResponse() {
    }

    public Double getCurrentSavings() {
        return currentSavings;
    }

    public void setCurrentSavings(Double currentSavings) {
        this.currentSavings = currentSavings;
    }

    public Double getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(Double monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public Double getInvestmentValue() {
        return investmentValue;
    }

    public void setInvestmentValue(Double investmentValue) {
        this.investmentValue = investmentValue;
    }

    public Double getNetGrowth() {
        return netGrowth;
    }

    public void setNetGrowth(Double netGrowth) {
        this.netGrowth = netGrowth;
    }

    public List<SavingsChartDto> getSavingsTrend() {
        return savingsTrend;
    }

    public void setSavingsTrend(
            List<SavingsChartDto> savingsTrend) {

        this.savingsTrend = savingsTrend;
    }

    public List<InvestmentChartDto> getInvestmentTrend() {
        return investmentTrend;
    }

    public void setInvestmentTrend(
            List<InvestmentChartDto> investmentTrend) {

        this.investmentTrend = investmentTrend;
    }

    public List<ExpenseChartDto> getExpenseBreakdown() {
        return expenseBreakdown;
    }

    public void setExpenseBreakdown(
            List<ExpenseChartDto> expenseBreakdown) {

        this.expenseBreakdown = expenseBreakdown;
    }
}
