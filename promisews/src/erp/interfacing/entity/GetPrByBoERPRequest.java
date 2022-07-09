package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GetPrByBoERPRequest {

	@SerializedName("boID")
	@JsonProperty("boID")
	private String boID;
	
	@SerializedName("orgCode")
	@JsonProperty("orgCode")
	private String orgCode;

	public String getBoID() {
		return boID;
	}

	public void setBoID(String boID) {
		this.boID = boID;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
}
