package id.co.promise.procurement.DTO;

import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.ShippingTo;

import java.util.List;

public class ShippingToDtl {

	private ShippingTo shippingTo;

	private List<PurchaseRequestItem> purchaseRequestItemList;

	private List<PurchaseOrderItem> purchaseOrderItemList;

	public ShippingTo getShippingTo() {
		return shippingTo;
	}

	public void setShippingTo(ShippingTo shippingTo) {
		this.shippingTo = shippingTo;
	}

	public List<PurchaseRequestItem> getPurchaseRequestItemList() {
		return purchaseRequestItemList;
	}

	public void setPurchaseRequestItemList(List<PurchaseRequestItem> purchaseRequestItemList) {
		this.purchaseRequestItemList = purchaseRequestItemList;
	}

	public List<PurchaseOrderItem> getPurchaseOrderItemList() {
		return purchaseOrderItemList;
	}

	public void setPurchaseOrderItemList(List<PurchaseOrderItem> purchaseOrderItemList) {
		this.purchaseOrderItemList = purchaseOrderItemList;
	}

}
