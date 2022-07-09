
package id.co.promise.procurement.purchaseorder;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.DTO.ShippingToDtl;
import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderTerm;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.TermAndCondition;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.AddressBookDTO;
import id.co.promise.procurement.master.AddressBookDTOForCreatePO;
import id.co.promise.procurement.purchaseorder.create.NewApprovalParam;

public class PurchaseOrderAddEditDTO {

	List<TermAndCondition> termAndConditionList;
	List<Approval> approvalList;
	List<AddressBookDTO> addressBookDTOList;
	List<String> taxCodeList;

	private PurchaseOrder purchaseOrder;
	private PurchaseRequest purchaseRequest;
	private List<ShippingToDtl> shippingToDtlList;

	private PurchaseOrderTerm purchaseOrderTerm;

	private List<PurchaseRequestItem> purchaseRequestItemList;

	private Vendor vendor;

	private Integer approvalId;

	private NewApprovalParam newApproval;

	private RoleUser roleUserNewSelected;

	List<ApprovalLevel> approvalLevelList;

	private Integer prNumber;
	private Integer prItem;
	private Date deliveryTime;
	private Date poDate;
	private String vendorName;
	private Double grandTotal;
	private List<AddressBookDTOForCreatePO> addressBookDTOForCreatePOList;
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getPrItem() {
		return prItem;
	}

	public void setPrItem(Integer prItem) {
		this.prItem = prItem;
	}

	public PurchaseRequest getPurchaseRequest() {
		return purchaseRequest;
	}

	public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
		this.purchaseRequest = purchaseRequest;
	}

	public Integer getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(Integer prNumber) {
		this.prNumber = prNumber;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public List<ApprovalLevel> getApprovalLevelList() {
		return approvalLevelList;
	}

	public void setApprovalLevelList(List<ApprovalLevel> approvalLevelList) {
		this.approvalLevelList = approvalLevelList;
	}

	public List<AddressBookDTO> getAddressBookDTOList() {
		return addressBookDTOList;
	}

	public void setAddressBookDTOList(List<AddressBookDTO> addressBookDTOList) {
		this.addressBookDTOList = addressBookDTOList;
	}

	public List<TermAndCondition> getTermAndConditionList() {
		return termAndConditionList;
	}

	public void setTermAndConditionList(List<TermAndCondition> termAndConditionList) {
		this.termAndConditionList = termAndConditionList;
	}

	public List<Approval> getApprovalList() {
		return approvalList;
	}

	public void setApprovalList(List<Approval> approvalList) {
		this.approvalList = approvalList;
	}

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

	public RoleUser getRoleUserNewSelected() {
		return roleUserNewSelected;
	}

	public void setRoleUserNewSelected(RoleUser roleUserNewSelected) {
		this.roleUserNewSelected = roleUserNewSelected;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public List<String> getTaxCodeList() {
		return taxCodeList;
	}

	public void setTaxCodeList(List<String> taxCodeList) {
		this.taxCodeList = taxCodeList;
	}

	public List<AddressBookDTOForCreatePO> getAddressBookDTOForCreatePOList() {
		return addressBookDTOForCreatePOList;
	}

	public void setAddressBookDTOForCreatePOList(List<AddressBookDTOForCreatePO> addressBookDTOForCreatePOList) {
		this.addressBookDTOForCreatePOList = addressBookDTOForCreatePOList;
	}

}
