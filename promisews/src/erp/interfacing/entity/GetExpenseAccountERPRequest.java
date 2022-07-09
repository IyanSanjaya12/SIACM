package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GetExpenseAccountERPRequest {

	@SerializedName("itemCode")
	@JsonProperty("itemCode")
	private String itemCode;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
}
