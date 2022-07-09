package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.DeliveryReceived;


public class DeliveryReceivedDTO {
	DeliveryReceived deliveryReceived;
	int totalItem;
	public DeliveryReceived getDeliveryReceived() {
		return deliveryReceived;
	}
	public void setDeliveryReceived(DeliveryReceived deliveryReceived) {
		this.deliveryReceived = deliveryReceived;
	}
	public int getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}
	
	

}
