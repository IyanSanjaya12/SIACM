package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PostPurchaseOrderERPResponse {
	
	@SerializedName("orgCode")
	@JsonProperty("orgCode")
	private String orgCode;
	
	@SerializedName("poHeaderID")
	@JsonProperty("poHeaderID")
	private Integer poHeaderID;
	
	@SerializedName("poNumber")
	@JsonProperty("poNumber")
	private String poNumber;
	
	@SerializedName("poID")
	@JsonProperty("poID")
	private String poID;
	
	@SerializedName("prNumber")
	@JsonProperty("prNumber")
	private String prNumber;
	
	@SerializedName("poStatus")
	@JsonProperty("poStatus")
	private String poStatus;

	public String getOrgCode() {
		return orgCode;
	}

	public Integer getPoHeaderID() {
		return poHeaderID;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public String getPoID() {
		return poID;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public String getPoStatus() {
		return poStatus;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public void setPoHeaderID(Integer poHeaderID) {
		this.poHeaderID = poHeaderID;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public void setPoID(String poID) {
		this.poID = poID;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	public void setPoStatus(String poStatus) {
		this.poStatus = poStatus;
	}
}
