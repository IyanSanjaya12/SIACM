package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class GetPrByBoERPResponse {

	@SerializedName("prNUmber")
	private String prNUmber;
	
	@SerializedName("prHeaderID")
	private String prHeaderID;
	
	@SerializedName("boID")
	private String boID;
	
	@SerializedName("prStatus")
	private String prStatus;

	public String getPrNUmber() {
		return prNUmber;
	}

	public void setPrNUmber(String prNUmber) {
		this.prNUmber = prNUmber;
	}

	public String getPrHeaderID() {
		return prHeaderID;
	}

	public void setPrHeaderID(String prHeaderID) {
		this.prHeaderID = prHeaderID;
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
