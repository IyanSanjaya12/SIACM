package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PostDeliveryReceiptERPResponse {

	@SerializedName("receiptNum")
	@JsonProperty("receiptNum")
	private String receiptNum;

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	
}
