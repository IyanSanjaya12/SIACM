package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class PostBookingOrderERPResponse {

	@SerializedName("orgCode")
	private String orgCode;
	
	@SerializedName("prNumber")
	private String prNumber;
	
	@SerializedName("requisitionHeaderID")
	private String requisitionHeaderID;
	
	@SerializedName("boID")
	private String boID;
	
	@SerializedName("prStatus")
	private String prStatus;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	public String getRequisitionHeaderID() {
		return requisitionHeaderID;
	}

	public void setRequisitionHeaderID(String requisitionHeaderID) {
		this.requisitionHeaderID = requisitionHeaderID;
	}

	public String getBoID() {
		return boID;
	}

	public void setBoID(String boID) {
		this.boID = boID;
	}

	public String getPrStatus() {
		return prStatus;
	}

	public void setPrStatus(String prStatus) {
		this.prStatus = prStatus;
	}
	
	
}
