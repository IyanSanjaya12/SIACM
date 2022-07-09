package id.co.promise.procurement.DTO;

import java.util.List;

public class PurchaseOrderDtlInterface {

	private PurchaseOrderInterface purchaseOrderInterface;

	private List<ShippingToDtlInterface> shippingToDtlInterfaceList;

	public PurchaseOrderInterface getPurchaseOrderInterface() {
		return purchaseOrderInterface;
	}

	public void setPurchaseOrderInterface(PurchaseOrderInterface purchaseOrderInterface) {
		this.purchaseOrderInterface = purchaseOrderInterface;
	}

	public List<ShippingToDtlInterface> getShippingToDtlInterfaceList() {
		return shippingToDtlInterfaceList;
	}

	public void setShippingToDtlInterfaceList(List<ShippingToDtlInterface> shippingToDtlInterfaceList) {
		this.shippingToDtlInterfaceList = shippingToDtlInterfaceList;
	}

}
