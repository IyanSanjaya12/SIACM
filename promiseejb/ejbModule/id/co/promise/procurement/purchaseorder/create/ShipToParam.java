package id.co.promise.procurement.purchaseorder.create;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.master.AddressBookDTO;

public class ShipToParam {
	private Integer companyId;
	private String companyName;
	private Integer addressId;
	private String address;
	private String fullName;
	private String telephone;
	private List<ItemPOParam> itemPOList;
	private List<AddressBookDTO> companyAddressList;
	private Afco companySelected;
	// private AddressBookDTO companyAddress;
	private List<Afco> companyList;
	private Date deliveryTime;
	private Boolean saveAddress = false;


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

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<ItemPOParam> getItemPOList() {
		return itemPOList;
	}

	public void setItemPOList(List<ItemPOParam> itemPOList) {
		this.itemPOList = itemPOList;
	}

	public List<AddressBookDTO> getCompanyAddressList() {
		return companyAddressList;
	}

	public void setCompanyAddressList(List<AddressBookDTO> companyAddressList) {
		this.companyAddressList = companyAddressList;
	}

	public Afco getCompanySelected() {
		return companySelected;
	}

	public void setCompanySelected(Afco companySelected) {
		this.companySelected = companySelected;
	}

	public List<Afco> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Afco> companyList) {
		this.companyList = companyList;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Boolean getSaveAddress() {
		return saveAddress;
	}

	public void setSaveAddress(Boolean saveAddress) {
		this.saveAddress = saveAddress;
	}

}
