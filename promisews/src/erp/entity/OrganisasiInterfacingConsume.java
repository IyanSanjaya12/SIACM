package erp.entity;

import com.google.gson.annotations.SerializedName;

public class OrganisasiInterfacingConsume {
	
	@SerializedName("ORG_CODE")
	private String orgCode;
	
	@SerializedName("ITEM_CODE")
	private String itemCode;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
}
