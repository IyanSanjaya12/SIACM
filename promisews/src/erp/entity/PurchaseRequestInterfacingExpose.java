package erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PurchaseRequestInterfacingExpose {
	
	@SerializedName("BO_ID")
	@JsonProperty("BO_ID")
	private String boId;
	  
	@SerializedName("ORG_CODE")
	@JsonProperty("ORG_CODE")
	private String orgCode;
	
	@SerializedName("REQUISITION_HEADER_ID")
	@JsonProperty("REQUISITION_HEADER_ID")
	private Integer prHeaderId;
	  
	@SerializedName("PR_NUMBER")
	@JsonProperty("PR_NUMBER")
	private String prNumber;
		  
	@SerializedName("PR_STATUS")
	@JsonProperty("PR_STATUS")
	private String prStatus;

	public String getBoId() {
		return boId;
	}

	public void setBoId(String boId) {
		this.boId = boId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Integer getPrHeaderId() {
		return prHeaderId;
	}

	public void setPrHeaderId(Integer prHeaderId) {
		this.prHeaderId = prHeaderId;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	public String getPrStatus() {
		return prStatus;
	}

	public void setPrStatus(String prStatus) {
		this.prStatus = prStatus;
	}  
  
}