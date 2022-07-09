package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GetPOByPRERPResponse {
	
	@SerializedName("poNumber")
	@JsonProperty("poNumber")
	private String poNumber;
	
	@SerializedName("poHeaderID")
	@JsonProperty("poHeaderID")
	private String poHeaderID;

	@SerializedName("requisitionHeaderID")
	@JsonProperty("requisitionHeaderID")
	private String requisitionHeaderID;
	
	@SerializedName("poStatus")
	@JsonProperty("poStatus")
	private String poStatus;

	public String getPoNumber() {
		return poNumber;
	}

	public String getPoHeaderID() {
		return poHeaderID;
	}

	public String getRequisitionHeaderID() {
		return requisitionHeaderID;
	}

	public String getPoStatus() {
		return poStatus;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public void setPoHeaderID(String poHeaderID) {
		this.poHeaderID = poHeaderID;
	}

	public void setRequisitionHeaderID(String requisitionHeaderID) {
		this.requisitionHeaderID = requisitionHeaderID;
	}

	public void setPoStatus(String poStatus) {
		this.poStatus = poStatus;
	}

}
