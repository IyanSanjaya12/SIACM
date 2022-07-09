package id.co.promise.procurement.purchaserequest.create;

import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Afco;

public class ShippingToPRParam {
	private Afco companySelected;
	private AddressBook[] companyAddresslist;
	private AddressBook companyAddressSelected;
	private String fullName;
	private String telephone1;
	private String streetAddress;
	private Integer afcoId;
	private Double quantity;

	public Afco getCompanySelected() {
		return companySelected;
	}

	public void setCompanySelected(Afco companySelected) {
		this.companySelected = companySelected;
	}

	public AddressBook[] getCompanyAddresslist() {
		return companyAddresslist;
	}

	public void setCompanyAddresslist(AddressBook[] companyAddresslist) {
		this.companyAddresslist = companyAddresslist;
	}

	public AddressBook getCompanyAddressSelected() {
		return companyAddressSelected;
	}

	public void setCompanyAddressSelected(AddressBook companyAddressSelected) {
		this.companyAddressSelected = companyAddressSelected;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public Integer getAfcoId() {
		return afcoId;
	}

	public void setAfcoId(Integer afcoId) {
		this.afcoId = afcoId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}
