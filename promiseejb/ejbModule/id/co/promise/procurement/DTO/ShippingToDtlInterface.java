package id.co.promise.procurement.DTO;

import java.util.List;

public class ShippingToDtlInterface {

	private ShippingToInterface shippingToInterface;

	private List<PurchaseOrderItemInterface> purchaseOrderItemInterfaceList;

	public ShippingToInterface getShippingToInterface() {
		return shippingToInterface;
	}

	public void setShippingToInterface(ShippingToInterface shippingToInterface) {
		this.shippingToInterface = shippingToInterface;
	}

	public List<PurchaseOrderItemInterface> getPurchaseOrderItemInterfaceList() {
		return purchaseOrderItemInterfaceList;
	}

	public void setPurchaseOrderItemInterfaceList(List<PurchaseOrderItemInterface> purchaseOrderItemInterfaceList) {
		this.purchaseOrderItemInterfaceList = purchaseOrderItemInterfaceList;
	}

}
