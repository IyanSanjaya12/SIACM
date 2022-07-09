package erp.interfacing.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PostLoginVendorDetailERPResponse {
	
	@SerializedName("rowstamp")
	private String rowstamp;
	
	@SerializedName("vendorNo")
	private String vendorNo;
	
	@SerializedName("fullname")
	private String fullname;
	
	@SerializedName("email")
	private String email;
	
	@SerializedName("isActive")
	private String isActive;
	
	@SerializedName("taxNo")
	private String taxNo;
	
	@SerializedName("address1")
	private String address1;
	
	@SerializedName("city")
	private String city;
	
	@SerializedName("region")
	private String region;
	
	@SerializedName("wilayahDescr")
	private String wilayahDescr;
	
	@SerializedName("phoneNo")
	private String phoneNo;
	
	@SerializedName("updDt")
	private String updDt;
	
	@SerializedName("dtStart")
	private String dtStart;
	
	@SerializedName("dtEnd")
	private String dtEnd;
	
	@SerializedName("descr")
	private String descr;
	
	@SerializedName("vendorProducts")
	List<DataVendorProductsERPResponse> vendorProducts;
	
	@SerializedName("vendorBankAccount")
	List<DataBankVendorERPResponse> vendorBankAccount;

	public String getRowstamp() {
		return rowstamp;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public String getFullname() {
		return fullname;
	}

	public String getEmail() {
		return email;
	}

	public String getIsActive() {
		return isActive;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public String getAddress1() {
		return address1;
	}

	public String getCity() {
		return city;
	}

	public String getRegion() {
		return region;
	}

	public String getWilayahDescr() {
		return wilayahDescr;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getUpdDt() {
		return updDt;
	}

	public String getDtStart() {
		return dtStart;
	}

	public String getDtEnd() {
		return dtEnd;
	}

	public String getDescr() {
		return descr;
	}

	public List<DataVendorProductsERPResponse> getVendorProducts() {
		return vendorProducts;
	}

	public void setRowstamp(String rowstamp) {
		this.rowstamp = rowstamp;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setWilayahDescr(String wilayahDescr) {
		this.wilayahDescr = wilayahDescr;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void setUpdDt(String updDt) {
		this.updDt = updDt;
	}

	public void setDtStart(String dtStart) {
		this.dtStart = dtStart;
	}

	public void setDtEnd(String dtEnd) {
		this.dtEnd = dtEnd;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public void setVendorProducts(List<DataVendorProductsERPResponse> vendorProducts) {
		this.vendorProducts = vendorProducts;
	}

	public List<DataBankVendorERPResponse> getVendorBankAccount() {
		return vendorBankAccount;
	}

	public void setVendorBankAccount(List<DataBankVendorERPResponse> vendorBankAccount) {
		this.vendorBankAccount = vendorBankAccount;
	}
	
}
