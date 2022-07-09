package id.co.promise.procurement.alokasianggaran;

import java.math.BigDecimal;

public class DashboardAnggaranDTO {
	private int year;
	private int month;
	private int week;
	private BigDecimal plannedBudget;
	private BigDecimal bookedBudget;
	private BigDecimal usedBudget;
	private BigDecimal availableBudget;
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public BigDecimal getPlannedBudget() {
		return plannedBudget;
	}
	public void setPlannedBudget(BigDecimal plannedBudget) {
		this.plannedBudget = plannedBudget;
	}
	public BigDecimal getBookedBudget() {
		return bookedBudget;
	}
	public void setBookedBudget(BigDecimal bookedBudget) {
		this.bookedBudget = bookedBudget;
	}
	public BigDecimal getUsedBudget() {
		return usedBudget;
	}
	public void setUsedBudget(BigDecimal usedBudget) {
		this.usedBudget = usedBudget;
	}
	public BigDecimal getAvailableBudget() {
		return availableBudget;
	}
	public void setAvailableBudget(BigDecimal availableBudget) {
		this.availableBudget = availableBudget;
	}
	
	
	
	
}
