package erp.entity;

import com.google.gson.annotations.SerializedName;

public class InvoicePaymentInterfacingConsume {
	
	@SerializedName("ORG_CODE")
	private String orgCode;
	
	@SerializedName("INVOICE_ID")
	private String invoiceId;
	
	@SerializedName("INVOICE_NUM")
	private String invoiceNum;
	
	@SerializedName("INVOICE_DATE")
	private String invoiceDate;
	
	@SerializedName("INVOICE_AMOUNT")
	private Double invoiceAmount;
	
	@SerializedName("AMOUNT_PAID")
	private Double amountPaid;
	
	@SerializedName("CANCELLED_DATE")
	private String cancelledDate;
	
	@SerializedName("APPROVAL_STATUS")
	private String approvalStatus;
	
	@SerializedName("PO_NUMBER")
	private String poNumber;
	
	@SerializedName("NOMOR_CONTRACT")
	private String nomorContract;
	
	@SerializedName("CREATION_DATE")
	private String creationDate;
	
	@SerializedName("LAST_UPDATE_DATE")
	private String lastUpdateDate;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
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
