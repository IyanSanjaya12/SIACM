package id.co.promise.procurement.DTO;

import java.util.Date;

public class ContractRequest {
	
private Integer reqPk;
	
	private Date  reqDate;
	
	private Integer  reqFinalContractFk;
	
	private Integer  reqLoginFk;
	
	private Integer  reqLoginVFk;
	
	private Integer  reqIsDeleted;
	
	private Integer  reqPaymentType;
	
	private Integer reqIsDelegation;
	
	private ContractRequestDetail contractRequestDetail;
	
	//private ULogin uLogin;
	
	//private ContractRequestRevision contractRequestRevision;
	
	//private CDraft cDraft; 

	//private CDraftApproval cDraftApproval;
	
	private Integer reqOrganizationFk;
	
	private Integer reqPengadaanFk;
	
	private Integer reqPengadaanPurchaseReqFk;
	
	private String reqCurrency;

	public Integer getReqPk() {
		return reqPk;
	}

	public void setReqPk(Integer reqPk) {
		this.reqPk = reqPk;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	public Integer getReqFinalContractFk() {
		return reqFinalContractFk;
	}

	public void setReqFinalContractFk(Integer reqFinalContractFk) {
		this.reqFinalContractFk = reqFinalContractFk;
	}

	public Integer getReqLoginFk() {
		return reqLoginFk;
	}

	public void setReqLoginFk(Integer reqLoginFk) {
		this.reqLoginFk = reqLoginFk;
	}

	public Integer getReqLoginVFk() {
		return reqLoginVFk;
	}

	public void setReqLoginVFk(Integer reqLoginVFk) {
		this.reqLoginVFk = reqLoginVFk;
	}

	public Integer getReqIsDeleted() {
		return reqIsDeleted;
	}

	public void setReqIsDeleted(Integer reqIsDeleted) {
		this.reqIsDeleted = reqIsDeleted;
	}

	public Integer getReqPaymentType() {
		return reqPaymentType;
	}

	public void setReqPaymentType(Integer reqPaymentType) {
		this.reqPaymentType = reqPaymentType;
	}

	public Integer getReqIsDelegation() {
		return reqIsDelegation;
	}

	public void setReqIsDelegation(Integer reqIsDelegation) {
		this.reqIsDelegation = reqIsDelegation;
	}

	public ContractRequestDetail getContractRequestDetail() {
		return contractRequestDetail;
	}

	public void setContractRequestDetail(ContractRequestDetail contractRequestDetail) {
		this.contractRequestDetail = contractRequestDetail;
	}

	public Integer getReqOrganizationFk() {
		return reqOrganizationFk;
	}

	public void setReqOrganizationFk(Integer reqOrganizationFk) {
		this.reqOrganizationFk = reqOrganizationFk;
	}

	public Integer getReqPengadaanFk() {
		return reqPengadaanFk;
	}

	public void setReqPengadaanFk(Integer reqPengadaanFk) {
		this.reqPengadaanFk = reqPengadaanFk;
	}

	public Integer getReqPengadaanPurchaseReqFk() {
		return reqPengadaanPurchaseReqFk;
	}

	public void setReqPengadaanPurchaseReqFk(Integer reqPengadaanPurchaseReqFk) {
		this.reqPengadaanPurchaseReqFk = reqPengadaanPurchaseReqFk;
	}

	public String getReqCurrency() {
		return reqCurrency;
	}

	public void setReqCurrency(String reqCurrency) {
		this.reqCurrency = reqCurrency;
	}


}
