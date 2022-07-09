package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;

public class DeliveryReceivedDetailDTO {

	private PurchaseOrder purchaseOrder;
	private PurchaseOrderItem purchaseOrderItem;
	private DeliveryReceived deliveryReceived;
	private Integer dikirim;
	
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public PurchaseOrderItem getPurchaseOrderItem() {
		return purchaseOrderItem;
	}
	public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
		this.purchaseOrderItem = purchaseOrderItem;
	}
	public Integer getDikirim() {
		return dikirim;
	}
	public void setDikirim(Integer dikirim) {
		this.dikirim = dikirim;
	}
	public DeliveryReceived getDeliveryReceived() {
		return deliveryReceived;
	}
	public void setDeliveryReceived(DeliveryReceived deliveryReceived) {
		this.deliveryReceived = deliveryReceived;
	}
	
}
