package erp.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BookingOrderInterfacingConsume {
	
	@SerializedName("ORG_CODE")
	private String orgCode;
	
	@SerializedName("BO_ID")
	private Integer boId;
	
	@SerializedName("DEPARTMENT")
	private String departement;
	
	@SerializedName("TOTAL_AMOUNT")
	private Double totalAmount;
	
	@SerializedName("SHIPPING_FEE")
	private Double shippingFee;
	
	@SerializedName("ASURANSI_FEE")
	private Double asuransiFee;
	
	@SerializedName("PREPARER_NAME")
	private String preparerName;
	
	@SerializedName("BO_APPROVE_DATE")
	private String approvedDate;

	@SerializedName("ADDRESS_BOOK_ID")
	private String addressBookId;
	
	@SerializedName("VENDOR_NO")
	private String vendorNo;
	
	@SerializedName("BO_DATE")
	private String boDate ;
	
	@SerializedName("CREATION_DATE")
	private String creationDate;
	
	@SerializedName("LAST_UPDATE_DATE")
	private String lastUpdateDate;
	
	@SerializedName("FLAG_PROCESS")
	private String flagProcess = "I";

	@SerializedName("PREPARER_NUM")
	private String preparerNum ;
	
	@SerializedName("BO_DETAIL_LIST")
	private List <BookingOrderDetailInterfacingConsume> bookingOrderDetailInterfacingList;
	
	public String getPreparerNum() {
		return preparerNum;
	}

	public void setPreparerNum(String preparerNum) {
		this.preparerNum = preparerNum;
	}

	public String getBoDate() {
		return boDate;
	}

	public void setBoDate(String boDate) {
		this.boDate = boDate;
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

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Integer getBoId() {
		return boId;
	}

	public void setBoId(Integer boId) {
		this.boId = boId;
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

	public List<BookingOrderDetailInterfacingConsume> getBookingOrderDetailInterfacingList() {
		return bookingOrderDetailInterfacingList;
	}

	public void setBookingOrderDetailInterfacingList(
			List<BookingOrderDetailInterfacingConsume> bookingOrderDetailInterfacingList) {
		this.bookingOrderDetailInterfacingList = bookingOrderDetailInterfacingList;
	}

	public String getFlagProcess() {
		return flagProcess;
	}

	public void setFlagProcess(String flagProcess) {
		this.flagProcess = flagProcess;
	}
}
