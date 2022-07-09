package erp.entity;

import java.sql.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VendorInterfacingConsume {
	
	@SerializedName("VENDOR_NO")
	private String vendorNo;
	
	@SerializedName("FULLNAME")
	private String fullname;
	
	@SerializedName("ADDRESS1")
	private String address1;
	
	@SerializedName("phone_no")
	private String phoneNo;
	
	@SerializedName("EMAIL")
	private String email;
	
	@SerializedName("TAX_NO")
	private String taxNo;
	
	@SerializedName("upd_dt")
	private String updDt;
	
	@SerializedName("CITY")
	private String city;
	
	@SerializedName("REGION")
	private String region;
	
	@SerializedName("IS_ACTIVE")
	private String isActive;
	
	@SerializedName("WILAYAH_DESCR")
	private String wilayahDescr;
	

	@SerializedName("VENDOR_BANK_ACCOUNT")
	private List <BankVendorInterfacingExpose> bankVendorList;
	
	
	@SerializedName("VENDOR_PRODUCTS")
	private List <BidangUsahaInterfacingExpose> bidangUsahaList;
	///private List <SegmentasiVendorInterfacingExpose> segmentasiVendorList;
	

	@SerializedName("DT_START")
	private String dtStart;
	
	@SerializedName("DT_END")
	private String dtEnd;
	
	@SerializedName("VENDOR_BLACKLIST")
	private Integer vendorBlacklist;
	
	@SerializedName("CREATION_DATE")
	private String creationDate;
	
	@SerializedName("LAST_UPDATED_DATE")
	private String lastUpdatedDate;
	
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public List<BankVendorInterfacingExpose> getBankVendorList() {
		return bankVendorList;
	}
	public void setBankVendorList(List<BankVendorInterfacingExpose> bankVendorList) {
		this.bankVendorList = bankVendorList;
	}
	public String getVendorNo() {
		return vendorNo;
	}
	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getUpdDt() {
		return updDt;
	}
	public void setUpdDt(String updDt) {
		this.updDt = updDt;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getWilayahDescr() {
		return wilayahDescr;
	}
	public void setWilayahDescr(String wilayahDescr) {
		this.wilayahDescr = wilayahDescr;
	}

	public List<BidangUsahaInterfacingExpose> getBidangUsahaList() {
		return bidangUsahaList;
	}
	public void setBidangUsahaList(List<BidangUsahaInterfacingExpose> bidangUsahaList) {
		this.bidangUsahaList = bidangUsahaList;
	}
	public String getDtStart() {
		return dtStart;
	}
	public void setDtStart(String dtStart) {
		this.dtStart = dtStart;
	}
	public String getDtEnd() {
		return dtEnd;
	}
	public void setDtEnd(String dtEnd) {
		this.dtEnd = dtEnd;
	}
	public Integer getVendorBlacklist() {
		return vendorBlacklist;
	}
	public void setVendorBlacklist(Integer vendorBlacklist) {
		this.vendorBlacklist = vendorBlacklist;
	}
	
}
