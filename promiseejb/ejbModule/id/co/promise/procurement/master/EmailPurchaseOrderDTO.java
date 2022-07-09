package id.co.promise.procurement.master;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Vendor;

public class EmailPurchaseOrderDTO {

	private PurchaseRequest purchaseRequest;
	
	private PurchaseOrder purchaseOrder;
	
	private DeliveryReceived deliveryReceived;
	
	private PurchaseOrderItem purchaseOrderItem;
	
	private Vendor vendor;

	private Item item;
	
	private Catalog catalog;
	
	private Satuan satuan;
	
	private AddressBook addressBook;

	public PurchaseRequest getPurchaseRequest() {
		return purchaseRequest;
	}

	public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
		this.purchaseRequest = purchaseRequest;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public DeliveryReceived getDeliveryReceived() {
		return deliveryReceived;
	}

	public void setDeliveryReceived(DeliveryReceived deliveryReceived) {
		this.deliveryReceived = deliveryReceived;
	}

	public PurchaseOrderItem getPurchaseOrderItem() {
		return purchaseOrderItem;
	}

	public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		this.purchaseOrderItem = purchaseOrderItem;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public Satuan getSatuan() {
		return satuan;
	}

	public void setSatuan(Satuan satuan) {
		this.satuan = satuan;
	}

	public AddressBook getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	} 

}
