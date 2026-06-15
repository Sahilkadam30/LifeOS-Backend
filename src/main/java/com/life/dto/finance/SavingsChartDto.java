package com.life.dto.finance;

public class SavingsChartDto {

	private String month;

    private Double amount;

	public SavingsChartDto() {

	}

	public SavingsChartDto(String month, Double amount) {
		super();
		this.month = month;
		this.amount = amount;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
    
    
}
