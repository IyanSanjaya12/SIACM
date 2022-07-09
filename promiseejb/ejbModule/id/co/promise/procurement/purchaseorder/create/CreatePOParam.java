package id.co.promise.procurement.purchaseorder.create;

import id.co.promise.procurement.entity.PurchaseOrderTerm;

import java.util.List;

public class CreatePOParam {
	private BillToParam billTo;
	private List<ShipToParam> shipToList;
	private TermAndConditionParam termCondition;
	private ApprovalParam approval;
	private List<PurchaseOrderTerm> purchaseOrderTermList;

	public List<PurchaseOrderTerm> getPurchaseOrderTermList() {
		return purchaseOrderTermList;
	}

	public void setPurchaseOrderTermList(
			List<PurchaseOrderTerm> purchaseOrderTermList) {
		this.purchaseOrderTermList = purchaseOrderTermList;
	}

	public BillToParam getBillTo() {
		return billTo;
	}

	public void setBillTo(BillToParam billTo) {
		this.billTo = billTo;
	}

	public List<ShipToParam> getShipToList() {
		return shipToList;
	}

	public void setShipToList(List<ShipToParam> shipToList) {
		this.shipToList = shipToList;
	}

	public TermAndConditionParam getTermCondition() {
		return termCondition;
	}

	public void setTermCondition(TermAndConditionParam termCondition) {
		this.termCondition = termCondition;
	}

	public ApprovalParam getApproval() {
		return approval;
	}

	public void setApproval(ApprovalParam approval) {
		this.approval = approval;
	}

}
