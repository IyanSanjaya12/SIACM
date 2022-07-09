package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class DataReceiptListERPRequest {

	@SerializedName("itemCode")
	@JsonProperty("itemCode")
	private String itemCode;

	@SerializedName("quantity")
	@JsonProperty("quantity")
	private String quantity;
	
	@SerializedName("itemDescription")
	@JsonProperty("itemDescription")
	private String itemDescription;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
	
}
