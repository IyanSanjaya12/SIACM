package id.co.promise.procurement.dashboard;

public class DashboardOrderDTO {
	private int year;
	private int month;
	private int week;
	private long vendor;
	private long calonVendor;
	
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
	public long getVendor() {
		return vendor;
	}
	public void setVendor(long vendor) {
		this.vendor = vendor;
	}
	public long getCalonVendor() {
		return calonVendor;
	}
	public void setCalonVendor(long calonVendor) {
		this.calonVendor = calonVendor;
	}
	
}
