package erp.entity;
import com.google.gson.annotations.SerializedName;


public class BankVendorInterfacingExpose {

	@SerializedName("BANK_NUMBER")
	private String bankNumber;
	
	@SerializedName("BANK_NAME")
	private String bankName;
	
	@SerializedName("BANK_OWNER_NAME")
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
