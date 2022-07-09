package id.co.promise.procurement.purchaseorder.create;

import java.util.Date;
import java.util.List;

public class ShippingToCreatePOParamDTO {

	private Integer addressBookId;
	private Boolean saveShippingToAddress = false;

	private Integer afcoId;
	private String poNumber;
	private String fullName;
	private String streetAddress;
	private String addressLabel;
	private String telephone1;
	private Date deliveryTime;
	private List<PurchaseOrderItemCreatePOParamDTO> listPurchaseOrderItemCreatePOParamDTO;

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(Integer addressBookId) {
		this.addressBookId = addressBookId;
	}

	public Boolean getSaveShippingToAddress() {
		return saveShippingToAddress;
	}

	public void setSaveShippingToAddress(Boolean saveShippingToAddress) {
		this.saveShippingToAddress = saveShippingToAddress;
	}

	public Integer getAfcoId() {
		return afcoId;
	}

	public void setAfcoId(Integer afcoId) {
		this.afcoId = afcoId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getAddressLabel() {
		return addressLabel;
	}

	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public List<PurchaseOrderItemCreatePOParamDTO> getListPurchaseOrderItemCreatePOParamDTO() {
		return listPurchaseOrderItemCreatePOParamDTO;
	}

	public void setListPurchaseOrderItemCreatePOParamDTO(List<PurchaseOrderItemCreatePOParamDTO> listPurchaseOrderItemCreatePOParamDTO) {
		this.listPurchaseOrderItemCreatePOParamDTO = listPurchaseOrderItemCreatePOParamDTO;
	}

}
