package id.co.promise.procurement.DTO;

import java.util.Date;

public class BillingMemoRequest {
	
	private Integer billMemoReqPk; 

	private Date billMemoReqDate;

	private String billMemoRevNotes;

	private Integer billMemoReqIsDeleted;
	
	private Integer billMemoReqFCFk;

	private Integer billMemoReqPaymentPk;
	
	private Integer billMemoReqPOId;
	
	private String billMemoReqPONo;
	
	private Date billMemoReqPODate;
	
	private Integer billMemoReqAfcoId;
	
	private Integer billMemoReqVendorFk;
	
	private Integer billMemoReqStatus;

	private Integer billMemoReqLoginRequester;
	
	private BillingMemoRequest billingMemoRequest;
	
	private String billMemoCurrency;
	
	private String billMemoAccountCode;
	
	private String billMemoDepartement;
	
	public String getBillMemoAccountCode() {
		return billMemoAccountCode;
	}

	public void setBillMemoAccountCode(String billMemoAccountCode) {
		this.billMemoAccountCode = billMemoAccountCode;
	}

	public String getBillMemoDepartement() {
		return billMemoDepartement;
	}

	public void setBillMemoDepartement(String billMemoDepartement) {
		this.billMemoDepartement = billMemoDepartement;
	}

	public Integer getBillMemoReqPk() {
		return billMemoReqPk;
	}

	public void setBillMemoReqPk(Integer billMemoReqPk) {
		this.billMemoReqPk = billMemoReqPk;
	}

	public Date getBillMemoReqDate() {
		return billMemoReqDate;
	}

	public void setBillMemoReqDate(Date billMemoReqDate) {
		this.billMemoReqDate = billMemoReqDate;
	}

	public String getBillMemoRevNotes() {
		return billMemoRevNotes;
	}

	public void setBillMemoRevNotes(String billMemoRevNotes) {
		this.billMemoRevNotes = billMemoRevNotes;
	}

	public Integer getBillMemoReqIsDeleted() {
		return billMemoReqIsDeleted;
	}

	public void setBillMemoReqIsDeleted(Integer billMemoReqIsDeleted) {
		this.billMemoReqIsDeleted = billMemoReqIsDeleted;
	}

	public Integer getBillMemoReqFCFk() {
		return billMemoReqFCFk;
	}

	public void setBillMemoReqFCFk(Integer billMemoReqFCFk) {
		this.billMemoReqFCFk = billMemoReqFCFk;
	}

	public Integer getBillMemoReqPaymentPk() {
		return billMemoReqPaymentPk;
	}

	public void setBillMemoReqPaymentPk(Integer billMemoReqPaymentPk) {
		this.billMemoReqPaymentPk = billMemoReqPaymentPk;
	}

	public Integer getBillMemoReqPOId() {
		return billMemoReqPOId;
	}

	public void setBillMemoReqPOId(Integer billMemoReqPOId) {
		this.billMemoReqPOId = billMemoReqPOId;
	}

	public String getBillMemoReqPONo() {
		return billMemoReqPONo;
	}

	public void setBillMemoReqPONo(String billMemoReqPONo) {
		this.billMemoReqPONo = billMemoReqPONo;
	}

	public Date getBillMemoReqPODate() {
		return billMemoReqPODate;
	}

	public void setBillMemoReqPODate(Date billMemoReqPODate) {
		this.billMemoReqPODate = billMemoReqPODate;
	}

	public Integer getBillMemoReqAfcoId() {
		return billMemoReqAfcoId;
	}

	public void setBillMemoReqAfcoId(Integer billMemoReqAfcoId) {
		this.billMemoReqAfcoId = billMemoReqAfcoId;
	}

	public Integer getBillMemoReqVendorFk() {
		return billMemoReqVendorFk;
	}

	public void setBillMemoReqVendorFk(Integer billMemoReqVendorFk) {
		this.billMemoReqVendorFk = billMemoReqVendorFk;
	}

	public Integer getBillMemoReqStatus() {
		return billMemoReqStatus;
	}

	public void setBillMemoReqStatus(Integer billMemoReqStatus) {
		this.billMemoReqStatus = billMemoReqStatus;
	}

	public Integer getBillMemoReqLoginRequester() {
		return billMemoReqLoginRequester;
	}

	public void setBillMemoReqLoginRequester(Integer billMemoReqLoginRequester) {
		this.billMemoReqLoginRequester = billMemoReqLoginRequester;
	}

	public BillingMemoRequest getBillingMemoRequest() {
		return billingMemoRequest;
	}

	public void setBillingMemoRequest(BillingMemoRequest billingMemoRequest) {
		this.billingMemoRequest = billingMemoRequest;
	}

	public String getBillMemoCurrency() {
		return billMemoCurrency;
	}

	public void setBillMemoCurrency(String billMemoCurrency) {
		this.billMemoCurrency = billMemoCurrency;
	}

}
