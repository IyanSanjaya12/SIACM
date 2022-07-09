package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GetPOByPRERPRequest {
	
	@SerializedName("requisitionHeaderID")
	@JsonProperty("requisitionHeaderID")
	private String requisitionHeaderID;
	
	@SerializedName("orgCode")
	@JsonProperty("orgCode")
	private String orgCode;

	public String getRequisitionHeaderID() {
		return requisitionHeaderID;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setRequisitionHeaderID(String requisitionHeaderID) {
		this.requisitionHeaderID = requisitionHeaderID;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
}
