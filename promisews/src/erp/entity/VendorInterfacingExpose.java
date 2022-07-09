package erp.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VendorInterfacingExpose {
	
	@SerializedName("VENDOR_NO")
	private String vendorNo;
	
	@SerializedName("FULLNAME")
	//@JsonProperty("FULLNAME")
	private String fullname;
	
	//@JsonProperty("ADDRESS1")
	@SerializedName("ADDRESS1")
	private String address1;
	
	@SerializedName("phone_no")
	//@JsonProperty("PHONE_NO")
	private String phoneNo;
	
	@SerializedName("EMAIL")
	//@JsonProperty("EMAIL")
	private String email;
	
	@SerializedName("TAX_NO")
	//@JsonProperty("TAX_NO")
	private String taxNo;
	
	@SerializedName("upd_dt")
	//@JsonProperty("UPD_DT")
	private String updDt;
	
	@SerializedName("CITY")
	//@JsonProperty("CITY")
	private String city;
	
	@SerializedName("REGION")
	//@JsonProperty("REGION")
	private String region;
	
	@SerializedName("IS_ACTIVE")
	//@JsonProperty("IS_ACTIVE")
	private Integer isActive;
	
	@SerializedName("WILAYAH_DESCR")
	//@JsonProperty("WILAYAH_DESCR")
	private String wilayahDescr;
	
	@SerializedName("VENDOR_BANK_ACCOUNT")
	private List <BankVendorInterfacingExpose> bankVendorList;
	
	@SerializedName("VENDOR_PRODUCTS")
	private List <BidangUsahaInterfacingExpose> bidangUsahaList;
	
	@SerializedName("DT_START")
	//@JsonProperty("DT_START")
	private String dtStart;
	
	@SerializedName("DT_END")
	//@JsonProperty("DT_END")
	private String dtEnd;
	
	@SerializedName("VENDOR_BLACKLIST")
	//@JsonProperty("VENDOR_BLACKLIST")
	private Integer vendorBlacklist;

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

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getWilayahDescr() {
		return wilayahDescr;
	}

	public void setWilayahDescr(String wilayahDescr) {
		this.wilayahDescr = wilayahDescr;
	}

	public List<BankVendorInterfacingExpose> getBankVendorList() {
		return bankVendorList;
	}

	public void setBankVendorList(List<BankVendorInterfacingExpose> bankVendorList) {
		this.bankVendorList = bankVendorList;
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
