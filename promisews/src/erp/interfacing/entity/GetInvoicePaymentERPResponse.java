package erp.interfacing.entity;

import com.google.gson.annotations.SerializedName;

public class GetInvoicePaymentERPResponse {
	
	@SerializedName("orgCode")
	private String orgCode;
	
	@SerializedName("invoiceID")
	private String invoiceID;
	
	@SerializedName("invoiceNum")
	private String invoiceNum;
	
	@SerializedName("invoiceDate")
	private String invoiceDate;
	
	@SerializedName("invoiceAmount")
	private Double invoiceAmount;
	
	@SerializedName("amountPaid")
	private Double amountPaid;
	
	@SerializedName("cancelledDate")
	private String cancelledDate;
	
	@SerializedName("approvalStatus")
	private String approvalStatus;
	
	@SerializedName("poNumber")
	private String poNumber;
	
	@SerializedName("nomorContract")
	private String nomorContract;
	
	@SerializedName("creationDate")
	private String creationDate;
	
	@SerializedName("lastUpdateDate")
	private String lastUpdateDate;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getInvoiceID() {
		return invoiceID;
	}

	public void setInvoiceID(String invoiceID) {
		this.invoiceID = invoiceID;
	}

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getCancelledDate() {
		return cancelledDate;
	}

	public void setCancelledDate(String cancelledDate) {
		this.cancelledDate = cancelledDate;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getNomorContract() {
		return nomorContract;
	}

	public void setNomorContract(String nomorContract) {
		this.nomorContract = nomorContract;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
