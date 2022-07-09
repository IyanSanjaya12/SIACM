package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class DataBankVendorERPResponse {

	@SerializedName("bankNumber")
	private String bankNumber;
	
	@SerializedName("bankName")
	private String bankName;
	
	@SerializedName("bankOwnerName")
	private String bankOwnerName;

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankOwnerName() {
		return bankOwnerName;
	}

	public void setBankOwnerName(String bankOwnerName) {
		this.bankOwnerName = bankOwnerName;
	}

}
