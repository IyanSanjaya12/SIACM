package id.co.promise.procurement.DTO;

import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderTerm;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.purchaseorder.create.NewApprovalParam;

import java.util.List;

public class PurchaseOrderDtl {

	private PurchaseOrder purchaseOrder;

	private List<ShippingToDtl> shippingToDtlList;

	private PurchaseOrderTerm purchaseOrderTerm;

	private List<PurchaseRequestItem> purchaseRequestItemList;
	
	private Vendor vendor;	

	private Integer approvalId;
	
	private NewApprovalParam newApproval;

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public List<ShippingToDtl> getShippingToDtlList() {
		return shippingToDtlList;
	}

	public void setShippingToDtlList(List<ShippingToDtl> shippingToDtlList) {
		this.shippingToDtlList = shippingToDtlList;
	}

	public PurchaseOrderTerm getPurchaseOrderTerm() {
		return purchaseOrderTerm;
	}

	public void setPurchaseOrderTerm(PurchaseOrderTerm purchaseOrderTerm) {
		this.purchaseOrderTerm = purchaseOrderTerm;
	}

	public List<PurchaseRequestItem> getPurchaseRequestItemList() {
		return purchaseRequestItemList;
	}

	public void setPurchaseRequestItemList(List<PurchaseRequestItem> purchaseRequestItemList) {
		this.purchaseRequestItemList = purchaseRequestItemList;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Integer getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Integer approvalId) {
		this.approvalId = approvalId;
	}

	public NewApprovalParam getNewApproval() {
		return newApproval;
	}

	public void setNewApproval(NewApprovalParam newApproval) {
		this.newApproval = newApproval;
	}
	
	 
}
