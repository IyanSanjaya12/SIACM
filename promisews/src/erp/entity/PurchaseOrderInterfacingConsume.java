package erp.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PurchaseOrderInterfacingConsume {

	@SerializedName("ORG_CODE")
	private String orgCode;
	
	@SerializedName("PO_ID")
	private String poId;
	
	@SerializedName("REQUISITION_HEADER_ID")
	private String requisitionHeaderId;
	
	@SerializedName("REQUISITION_NUMBER")
	private String requisitionNumber;
	
	@SerializedName("DEPARTMENT")
	private String departement;
	
	@SerializedName("TOTAL_AMOUNT")
	private Double totalAmount;
	
	@SerializedName("SHIPPING_FEE")
	private Double shippingFee;
	
	@SerializedName("ASURANSI_FEE")
	private Double asuransiFee;
	
	@SerializedName("DESCRIPTION")
	private String description;
	
	@SerializedName("PREPARER_NAME")
	private String preparerName;
	
	@SerializedName("PO_APPROVE_DATE")
	private String approvedDate;

	@SerializedName("ADDRESS_BOOK_ID")
	private String addressBookId;
	
	@SerializedName("VENDOR_NO")
	private String vendorNo;
	
	@SerializedName("PREPARER_NUM")
	private String preparerNum ;
	
	@SerializedName("DELIVERY_DATE")
	private String deliveryDate ;
	
	@SerializedName("CREATION_DATE")
	private String creationDate;
	
	@SerializedName("LAST_UPDATE_DATE")
	private String lastUpdateDate;

	@SerializedName("PO_DETAIL_LIST")
	List<PurchaseOrderDetailInterfacingConsume> poDetailList;
	
	@SerializedName("FLAG_PROCESS")
	private String flagProcess = "I";
	
	public String getFlagProcess() {
		return flagProcess;
	}

	public void setFlagProcess(String flagProcess) {
		this.flagProcess = flagProcess;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPoId() {
		return poId;
	}

	public String getPreparerNum() {
		return preparerNum;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setPoId(String poId) {
		this.poId = poId;
	}

	public String getRequisitionHeaderId() {
		return requisitionHeaderId;
	}

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public void setRequisitionHeaderId(String requisitionHeaderId) {
		this.requisitionHeaderId = requisitionHeaderId;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}

	public void setPreparerNum(String preparerNum) {
		this.preparerNum = preparerNum;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getDepartement() {
		return departement;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(Double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public Double getAsuransiFee() {
		return asuransiFee;
	}

	public void setAsuransiFee(Double asuransiFee) {
		this.asuransiFee = asuransiFee;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPreparerName() {
		return preparerName;
	}

	public void setPreparerName(String preparerName) {
		this.preparerName = preparerName;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(String addressBookId) {
		this.addressBookId = addressBookId;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public List<PurchaseOrderDetailInterfacingConsume> getPoDetailList() {
		return poDetailList;
	}

	public void setPoDetailList(List<PurchaseOrderDetailInterfacingConsume> poDetailList) {
		this.poDetailList = poDetailList;
	}	
}
