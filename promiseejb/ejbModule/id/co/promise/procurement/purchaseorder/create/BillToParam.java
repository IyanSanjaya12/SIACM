package id.co.promise.procurement.purchaseorder.create;

import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.vendor.VendorDTO;

import java.util.Date;

public class BillToParam {
	private Object addressObject; // ini kayaknya harus objek
	private VendorDTO vendorObject;
	private String ponumber;
	private Date podate;
	private PurchaseRequest pr;
	private Integer companyId;
	private String companyName;
	// private String billingAddress;
	// private AddressBook billingAddress; // ini kayaknya harus objek tp yang
	// dikirim
	// adalah string
	private Integer addressId;
	private String fullName;
	private String address;
	private String telephone;
	private String vendor; // ini kayaknya harus objek tp yang dikirim adalah
							// string
	private String nama;
	private String alamat;
	private Boolean shipToThisAddress;
	private Boolean saveAddress = false;

	public Object getAddressObject() {
		return addressObject;
	}

	public void setAddressObject(Object addressObject) {
		this.addressObject = addressObject;
	}

	public VendorDTO getVendorObject() {
		return vendorObject;
	}

	public void setVendorObject(VendorDTO vendorObject) {
		this.vendorObject = vendorObject;
	}

	public String getPonumber() {
		return ponumber;
	}

	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}

	public Date getPodate() {
		return podate;
	}

	public void setPodate(Date podate) {
		this.podate = podate;
	}

	public PurchaseRequest getPr() {
		return pr;
	}

	public void setPr(PurchaseRequest pr) {
		this.pr = pr;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	// public AddressBook getBillingAddress() {
	// return billingAddress;
	// }
	//
	// public void setBillingAddress(AddressBook billingAddress) {
	// this.billingAddress = billingAddress;
	// }

	// public void setBillingAddress(String billingAddress) {
	// System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHhh setBillingAddress");
	// // this.billingAddress = billingAddress;
	// }

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public Boolean getShipToThisAddress() {
		return shipToThisAddress;
	}

	public void setShipToThisAddress(Boolean shipToThisAddress) {
		this.shipToThisAddress = shipToThisAddress;
	}

	public Boolean getSaveAddress() {
		return saveAddress;
	}

	public void setSaveAddress(Boolean saveAddress) {
		this.saveAddress = saveAddress;
	}

}
