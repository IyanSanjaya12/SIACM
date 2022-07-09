package erp.interfacing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class DataOrgListERPResponse {
	
	@SerializedName("organizationCode")
	@JsonProperty("organizationCode")
	private String organizationCode;
	
	@SerializedName("itemId")
	@JsonProperty("itemId")
	private String itemId ;
	
	@SerializedName("itemCode")
	@JsonProperty("itemCode")
	private String itemCode ;

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	
}
