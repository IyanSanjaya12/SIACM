package erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PurchaseOrderInterfacingExpose {

	@SerializedName("ORG_CODE")
	@JsonProperty("ORG_CODE")
	private String orgCode;
	
	@SerializedName("PO_HEADER_ID")
	@JsonProperty("PO_HEADER_ID")
	private Integer poHeaderId;
	
	@SerializedName("PO_NUMBER")
	@JsonProperty("PO_NUMBER")
	private String poNumber;
	
	@SerializedName("PO_STATUS")
	@JsonProperty("PO_STATUS")
	private String poStatus;
	
	@SerializedName("REQUISITION_HEADER_ID")
	@JsonProperty("REQUISITION_HEADER_ID")
	private String prId;

	public String getOrgCode() {
		return orgCode;
	}

	public Integer getPoHeaderId() {
		return poHeaderId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public void setPoHeaderId(Integer poHeaderId) {
		this.poHeaderId = poHeaderId;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoStatus() {
		return poStatus;
	}

	public void setPoStatus(String poStatus) {
		this.poStatus = poStatus;
	}

	public String getPrId() {
		return prId;
	}

	public void setPrId(String prId) {
		this.prId = prId;
	}
	
}
	