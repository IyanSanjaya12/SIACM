package id.co.promise.procurement.entity;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestImportDTO {

	PurchaseRequest purchaseRequest;
	Approval approval;

	List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
	List<ShippingToPR> shippingToPRList = new ArrayList<ShippingToPR>();

	public PurchaseRequest getPurchaseRequest() {
		return purchaseRequest;
	}

	public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
		this.purchaseRequest = purchaseRequest;
	}

	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
	}

	public List<PurchaseRequestItem> getPurchaseRequestItemList() {
		return purchaseRequestItemList;
	}

	public void setPurchaseRequestItemList(List<PurchaseRequestItem> purchaseRequestItemList) {
		this.purchaseRequestItemList = purchaseRequestItemList;
	}

	public List<ShippingToPR> getShippingToPRList() {
		return shippingToPRList;
	}

	public void setShippingToPRList(List<ShippingToPR> shippingToPRList) {
		this.shippingToPRList = shippingToPRList;
	}

}
