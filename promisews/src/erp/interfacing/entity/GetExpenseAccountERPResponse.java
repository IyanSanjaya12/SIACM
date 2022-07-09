package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class GetExpenseAccountERPResponse {

	@SerializedName("expenseAccount")
	private String expenseAccount;
	
	@SerializedName("itemCode")
	private String itemCode;
	
	@SerializedName("orgCode")
	private String orgCode;
	
	@SerializedName("segmentDesc")
	private String segmentDesc;

	public String getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(String expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getSegmentDesc() {
		return segmentDesc;
	}

	public void setSegmentDesc(String segmentDesc) {
		this.segmentDesc = segmentDesc;
	}
	
}
