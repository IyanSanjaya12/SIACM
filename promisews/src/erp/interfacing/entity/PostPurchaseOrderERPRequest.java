package erp.interfacing.entity;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PostPurchaseOrderERPRequest {
	
	@SerializedName("orgCode")
	private String orgCode;
	
	@SerializedName("poID")
	private String poID;
	
	@SerializedName("requisitionHeaderID")
	private String requisitionHeaderID;
	
	@SerializedName("requisitionNumber")
	private String requisitionNumber;
	
	@SerializedName("department")
	private String department;
	
	@SerializedName("totalAmount")
	private String totalAmount;
	
	@SerializedName("shippingFee")
	private String shippingFee;
	
	@SerializedName("asuransiFee")
	private String asuransiFee;
	
	@SerializedName("poApproveDate")
	private String poApproveDate;
	
	@SerializedName("addressBookID")
	private String addressBookID;
	
	@SerializedName("vendorNo")
	private String vendorNo;
	
	@SerializedName("preparerName")
	private String preparerName ;
	
	@SerializedName("preparerNum")
	private String preparerNum ;
	
	@SerializedName("deliveryDate")
	private String deliveryDate ;
	
	@SerializedName("creationDate")
	private String creationDate;
	
	@SerializedName("lastUpdatedate")
	private String lastUpdatedate;

	@SerializedName("flagProcess")
	private String flagProcess = "I";
	
	@SerializedName("poDetailList")
	List<PostPurchaseOrderDetailERPRequest> poDetailList;

	public String getOrgCode() {
		return orgCode;
	}

	public String getPoID() {
		return poID;
	}

	public String getRequisitionHeaderID() {
		return requisitionHeaderID;
	}

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public String getDepartment() {
		return department;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public String getShippingFee() {
		return shippingFee;
	}

	public String getAsuransiFee() {
		return asuransiFee;
	}

	public String getPoApproveDate() {
		return poApproveDate;
	}

	public String getAddressBookID() {
		return addressBookID;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public String getPreparerName() {
		return preparerName;
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

	public String getLastUpdatedate() {
		return lastUpdatedate;
	}

	public String getFlagProcess() {
		return flagProcess;
	}

	public List<PostPurchaseOrderDetailERPRequest> getPoDetailList() {
		return poDetailList;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public void setPoID(String poID) {
		this.poID = poID;
	}

	public void setRequisitionHeaderID(String requisitionHeaderID) {
		this.requisitionHeaderID = requisitionHeaderID;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setShippingFee(String shippingFee) {
		this.shippingFee = shippingFee;
	}

	public void setAsuransiFee(String asuransiFee) {
		this.asuransiFee = asuransiFee;
	}

	public void setPoApproveDate(String poApproveDate) {
		this.poApproveDate = poApproveDate;
	}

	public void setAddressBookID(String addressBookID) {
		this.addressBookID = addressBookID;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public void setPreparerName(String preparerName) {
		this.preparerName = preparerName;
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

	public void setLastUpdatedate(String lastUpdatedate) {
		this.lastUpdatedate = lastUpdatedate;
	}

	public void setFlagProcess(String flagProcess) {
		this.flagProcess = flagProcess;
	}

	public void setPoDetailList(List<PostPurchaseOrderDetailERPRequest> poDetailList) {
		this.poDetailList = poDetailList;
	}
}
