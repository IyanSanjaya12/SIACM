package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DashboardParamDTO {
	@SerializedName("startDate")
	String startDate; 
	@SerializedName("endDate")
	String endDate;
	@SerializedName("organizationId")
	Integer organizationId;
	@SerializedName("dataIdList")
	List<Integer> dataIdList;
	@SerializedName("year")
	String year;
	

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public List<Integer> getDataIdList() {
		return dataIdList;
	}
	public void setDataIdList(List<Integer> dataIdList) {
		this.dataIdList = dataIdList;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
}
