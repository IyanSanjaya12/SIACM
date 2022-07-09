package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GetKodePuspelERPRequest {

	@SerializedName("employeeNumber")
	@JsonProperty("employeeNumber")
	private String employeeNumber;

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
}
