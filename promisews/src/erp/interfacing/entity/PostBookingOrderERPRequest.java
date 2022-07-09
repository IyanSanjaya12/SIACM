package erp.interfacing.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PostBookingOrderERPRequest {

	@SerializedName("orgCode")
	@JsonProperty("orgCode")
	private String orgCode;
	
	@SerializedName("invOrgID")
	@JsonProperty("invOrgID")
	private String invOrgID;
	
	@SerializedName("department")
	@JsonProperty("department")
	private String department;
	
	@SerializedName("totalAmount")
	@JsonProperty("totalAmount")
	private String totalAmount;
	
	@SerializedName("boApproveDate")
	@JsonProperty("boApproveDate")
	private String boApproveDate;
	
	@SerializedName("boID")
	@JsonProperty("boID")
	private String boID;
	
	@SerializedName("boDate")
	@JsonProperty("boDate")
	private String boDate;
	
	@SerializedName("prepareNum")
	@JsonProperty("prepareNum")
	private String prepareNum;
	
	@SerializedName("puspelCode")
	@JsonProperty("puspelCode")
	private String puspelCode;
	
	@SerializedName("prepareName")
	@JsonProperty("prepareName")
	private String prepareName;
	
	@SerializedName("shippingFee")
	@JsonProperty("shippingFee")
	private String shippingFee;
	
	@SerializedName("vendorNo")
	@JsonProperty("vendorNo")
	private String vendorNo;
	
	@SerializedName("deliveryDate")
	@JsonProperty("deliveryDate")
	private String deliveryDate;
	
	@SerializedName("asuransiFee")
	@JsonProperty("asuransiFee")
	private String asuransiFee;
	
	@SerializedName("flagProcess")
	@JsonProperty("flagProcess")
	private String flagProcess = "I";
	
	@SerializedName("addressBookID")
	@JsonProperty("addressBookID")
	private String addressBookID;
	
	@SerializedName("creationDate")
	@JsonProperty("creationDate")
	private String creationDate;
	
	@SerializedName("lastUpdatedate")
	@JsonProperty("lastUpdatedate")
	private String lastUpdatedate;
	
	@SerializedName("approvalPath")
	@JsonProperty("approvalPath")
	private String approvalPath;
	
	@SerializedName("approvalPathID")
	@JsonProperty("approvalPathID")
	private String approvalPathID;
	
	@SerializedName("linkLampiranPR")
	@JsonProperty("linkLampiranPR")
	private String linkLampiranPR;
	
	@SerializedName("linkLampiranKontrak")
	@JsonProperty("linkLampiranKontrak")
	private String linkLampiranKontrak;
	
	@SerializedName("boDetailList")
	@JsonProperty("boDetailList")
	private List<DataBoDetailListERPRequest> boDetailList;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getInvOrgID() {
		return invOrgID;
	}

	public void setInvOrgID(String invOrgID) {
		this.invOrgID = invOrgID;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getBoApproveDate() {
		return boApproveDate;
	}

	public void setBoApproveDate(String boApproveDate) {
		this.boApproveDate = boApproveDate;
	}

	public String getBoID() {
		return boID;
	}

	public void setBoID(String boID) {
		this.boID = boID;
	}

	public String getBoDate() {
		return boDate;
	}

	public void setBoDate(String boDate) {
		this.boDate = boDate;
	}

	public String getPrepareNum() {
		return prepareNum;
	}

	public void setPrepareNum(String prepareNum) {
		this.prepareNum = prepareNum;
	}

	public String getPrepareName() {
		return prepareName;
	}

	public void setPrepareName(String prepareName) {
		this.prepareName = prepareName;
	}

	public String getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(String shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getAsuransiFee() {
		return asuransiFee;
	}

	public void setAsuransiFee(String asuransiFee) {
		this.asuransiFee = asuransiFee;
	}

	public String getFlagProcess() {
		return flagProcess;
	}

	public void setFlagProcess(String flagProcess) {
		this.flagProcess = flagProcess;
	}

	public String getAddressBookID() {
		return addressBookID;
	}

	public void setAddressBookID(String addressBookID) {
		this.addressBookID = addressBookID;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedate() {
		return lastUpdatedate;
	}

	public void setLastUpdatedate(String lastUpdatedate) {
		this.lastUpdatedate = lastUpdatedate;
	}

	public String getApprovalPath() {
		return approvalPath;
	}

	public void setApprovalPath(String approvalPath) {
		this.approvalPath = approvalPath;
	}

	public String getApprovalPathID() {
		return approvalPathID;
	}

	public void setApprovalPathID(String approvalPathID) {
		this.approvalPathID = approvalPathID;
	}

	public String getLinkLampiranPR() {
		return linkLampiranPR;
	}

	public void setLinkLampiranPR(String linkLampiranPR) {
		this.linkLampiranPR = linkLampiranPR;
	}

	public String getLinkLampiranKontrak() {
		return linkLampiranKontrak;
	}

	public void setLinkLampiranKontrak(String linkLampiranKontrak) {
		this.linkLampiranKontrak = linkLampiranKontrak;
	}

	public List<DataBoDetailListERPRequest> getBoDetailList() {
		return boDetailList;
	}

	public void setBoDetailList(List<DataBoDetailListERPRequest> boDetailList) {
		this.boDetailList = boDetailList;
	}

	public String getPuspelCode() {
		return puspelCode;
	}

	public void setPuspelCode(String puspelCode) {
		this.puspelCode = puspelCode;
	}
	
	
}
