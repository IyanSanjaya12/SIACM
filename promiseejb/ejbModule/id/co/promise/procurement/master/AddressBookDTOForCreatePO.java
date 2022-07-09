package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequestItem;

public class AddressBookDTOForCreatePO {
	private AddressBook addressBook;
	
	private PurchaseRequestItem purchaseRequestItem;
	
	private List<PurchaseRequestItem> purchaseRequestItemList;
	
	private List<Double> subTotalList;
	
	private List<Double> ppnList;
	
	private Double subTotal;
	
	private Double ppn;
	
	private Double hasilAkhir;
	
	private Double grandTotal;
	
	private Boolean poDateOpened;
	
	private Date deliveryTime;
	
	private List<PurchaseOrderItem> purchaseOrderItemList;

	public AddressBook getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	}

	public PurchaseRequestItem getPurchaseRequestItem() {
		return purchaseRequestItem;
	}

	public void setPurchaseRequestItem(PurchaseRequestItem purchaseRequestItem) {
		this.purchaseRequestItem = purchaseRequestItem;
	}

	public List<PurchaseRequestItem> getPurchaseRequestItemList() {
		return purchaseRequestItemList;
	}

	public void setPurchaseRequestItemList(List<PurchaseRequestItem> purchaseRequestItemList) {
		this.purchaseRequestItemList = purchaseRequestItemList;
	}

	public Boolean getPoDateOpened() {
		return poDateOpened;
	}

	public void setPoDateOpened(Boolean poDateOpened) {
		this.poDateOpened = poDateOpened;
	}

	public List<Double> getSubTotalList() {
		return subTotalList;
	}

	public void setSubTotalList(List<Double> subTotalList) {
		this.subTotalList = subTotalList;
	}

	public List<Double> getPpnList() {
		return ppnList;
	}

	public void setPpnList(List<Double> ppnList) {
		this.ppnList = ppnList;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getPpn() {
		return ppn;
	}

	public void setPpn(Double ppn) {
		this.ppn = ppn;
	}

	public Double getHasilAkhir() {
		return hasilAkhir;
	}

	public void setHasilAkhir(Double hasilAkhir) {
		this.hasilAkhir = hasilAkhir;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public List<PurchaseOrderItem> getPurchaseOrderItemList() {
		return purchaseOrderItemList;
	}

	public void setPurchaseOrderItemList(List<PurchaseOrderItem> purchaseOrderItemList) {
		this.purchaseOrderItemList = purchaseOrderItemList;
	}
	
	
	
}
