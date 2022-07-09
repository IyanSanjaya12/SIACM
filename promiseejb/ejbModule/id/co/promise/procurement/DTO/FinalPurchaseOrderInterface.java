package id.co.promise.procurement.DTO;

import java.util.List;

public class FinalPurchaseOrderInterface {

	private Integer requestId;
	
	private List<PurchaseOrderDtlInterface> purchaseOrderDtlInterfaceList;

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public List<PurchaseOrderDtlInterface> getPurchaseOrderDtlInterfaceList() {
		return purchaseOrderDtlInterfaceList;
	}

	public void setPurchaseOrderDtlInterfaceList(List<PurchaseOrderDtlInterface> purchaseOrderDtlInterfaceList) {
		this.purchaseOrderDtlInterfaceList = purchaseOrderDtlInterfaceList;
	}

}
