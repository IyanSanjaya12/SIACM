package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Organisasi;

public class AddressBookDTO {
	private Integer id;
	private Organisasi organisasi;
	private String addressLabel;
	private String fullName;
	private String telephone1;
	private String telephone2;
	private String fax;
	private String streetAddress;
	private String country;
	private String city;
	private String district;
	private String subDistrict;
	private String postalCode;
	private Boolean defaultBillingAddress;
	private Boolean defaultShippingAddress;
	private Boolean status;

	public AddressBookDTO(AddressBook a) {
		this.id = a.getId();
		this.organisasi = a.getOrganisasi();
		this.addressLabel = a.getAddressLabel();
		this.fullName = a.getFullName();
		this.telephone1 = a.getTelephone();
		this.fax = a.getFax();
		this.streetAddress = a.getStreetAddress();
		this.country = a.getCountry();
		this.city = a.getCity();
		this.district = a.getDistrict();
		this.subDistrict = a.getSubDistrict();
		this.postalCode = a.getPostalCode();
		this.defaultBillingAddress = a.getDefaultBillingAddress();
		this.defaultShippingAddress = a.getDefaultShippingAddress();
		this.status = a.getStatus();
	}

	public Integer getId() {
		return id;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public String getAddressLabel() {
		return addressLabel;
	}

	public String getFullName() {
		return fullName;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public String getFax() {
		return fax;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getDistrict() {
		return district;
	}

	public String getSubDistrict() {
		return subDistrict;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public Boolean getDefaultBillingAddress() {
		return defaultBillingAddress;
	}

	public Boolean getDefaultShippingAddress() {
		return defaultShippingAddress;
	}

	public Boolean getStatus() {
		return status;
	}

}
