package id.co.promise.procurement.master;

import java.util.List;

public class DashboardDTO {
	String title;
	List<String> labels;
	List<String> dataLabels;
	List<List<Double>> data;
	
	public DashboardDTO(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<String> getDataLabels() {
		return dataLabels;
	}

	public void setDataLabels(List<String> dataLabels) {
		this.dataLabels = dataLabels;
	}

	public List<List<Double>> getData() {
		return data;
	}

	public void setData(List<List<Double>> data) {
		this.data = data;
	}
	
}
