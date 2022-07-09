package erp.interfacing.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GetVendorListActiveERPResponse {

	@SerializedName("rowstamp")
	private String rowstamp;
	
	@SerializedName("vendorNo")
	private String vendorNo;
	
	@SerializedName("fullname")
	private String fullname;
	
	@SerializedName("email")
	private String email;
	
	@SerializedName("isActive")
	private Integer isActive;
	
	@SerializedName("taxNo")
	private String taxNo;
	
	@SerializedName("address1")
	private String address1;
	
	@SerializedName("city")
	private String city;
	
	@SerializedName("region")
	private String region;
	
	@SerializedName("wilayahDesc")
	private String wilayahDesc;
	
	@SerializedName("phoneNo")
	private String phoneNo;
	
	@SerializedName("updDt")
	private String updDt;
	
	@SerializedName("dtStart")
	private String dtStart;
	
	@SerializedName("dtEnd")
	private String dtEnd;
	
	@SerializedName("desc")
	private String desc;
	
	@SerializedName("idEbs")
	private String idEbs;
	
	@SerializedName("vendorProducts")
	private List <DataVendorProductsERPResponse> vendorProducts;
	
	@SerializedName("vendorBankAccount")
	private List <DataBankVendorERPResponse> vendorBankAccount;

	public String getRowstamp() {
		return rowstamp;
	}

	public void setRowstamp(String rowstamp) {
		this.rowstamp = rowstamp;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
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

	public String getWilayahDesc() {
		return wilayahDesc;
	}

	public void setWilayahDesc(String wilayahDesc) {
		this.wilayahDesc = wilayahDesc;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUpdDt() {
		return updDt;
	}

	public void setUpdDt(String updDt) {
		this.updDt = updDt;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIdEbs() {
		return idEbs;
	}

	public void setIdEbs(String idEbs) {
		this.idEbs = idEbs;
	}

	public List<DataVendorProductsERPResponse> getVendorProducts() {
		return vendorProducts;
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
