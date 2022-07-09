package id.co.promise.procurement.purchaseorder.create;

import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderTerm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurchaseOrderCreatePOParamDTO {

	private String poNumber;
	private Integer purchaseRequestId;
	private Integer addressBookId;
	private Integer pengadaanId;
	private Integer termAndConditionId;
	private String department;
	private String notes;
	private Double downPayment;
	private Double discount;
	private Double tax;
	private Double totalCost;
	private String status;
	private List<ShippingToCreatePOParamDTO> listShippingToCreatePOParamDTO;
	private Boolean shipToThisAddress;
	private Integer approvalId;
	private NewApprovalParam newApproval;
	private Integer companyId;
	private String companyAddrFullName;
	private String companyAddrAddress;
	private String companyAddrTelephone1;
	private Boolean isSaveTheNewAddressCompany = false;
	private List<PurchaseOrderTerm> purchaseOrderTermList;

	public PurchaseOrderCreatePOParamDTO() {

	}

	public PurchaseOrderCreatePOParamDTO(CreatePOParam cpop, Map<Integer, Integer> mapItemIDbyPRid) {
		this.companyId = cpop.getBillTo().getCompanyId();
		this.isSaveTheNewAddressCompany = cpop.getBillTo().getSaveAddress();
		this.companyAddrFullName = cpop.getBillTo().getFullName();
		this.companyAddrAddress = cpop.getBillTo().getAddress();
		this.companyAddrTelephone1 = cpop.getBillTo().getTelephone();

		this.poNumber = cpop.getBillTo().getPonumber();
		this.purchaseRequestId = cpop.getBillTo().getPr().getId();
		this.addressBookId = cpop.getBillTo().getAddressId();
		this.termAndConditionId = cpop.getTermCondition().getTermType();
		this.department = cpop.getBillTo().getCompanyName();
		this.notes = cpop.getTermCondition().getNote();
		this.downPayment = cpop.getTermCondition().getDp();
		this.discount = cpop.getTermCondition().getDiscount();
		this.tax = cpop.getTermCondition().getTax();
		this.totalCost = cpop.getBillTo().getPr().getTotalHarga();
		this.purchaseOrderTermList = cpop.getPurchaseOrderTermList();

		if(cpop.getApproval() != null){
			if(cpop.getApproval().getLevelList() != null){
				if (cpop.getApproval().getLevelList().size() > 0) {
					this.approvalId = cpop.getApproval().getLevelList().get(0).getApproval().getId();
				}
			}
		}
		this.newApproval = cpop.getApproval().getNewSelected();

		this.shipToThisAddress = cpop.getBillTo().getShipToThisAddress();
		List<ShippingToCreatePOParamDTO> listShippingToCreatePOParamDTO = new ArrayList<ShippingToCreatePOParamDTO>();
		for (ShipToParam stp : cpop.getShipToList()) {
			ShippingToCreatePOParamDTO stcpo = new ShippingToCreatePOParamDTO();
			stcpo.setAddressBookId(stp.getAddressId());

			stcpo.setAddressLabel(stp.getCompanyName());
			stcpo.setAfcoId(stp.getCompanySelected().getId());
			stcpo.setFullName(stp.getFullName());
			stcpo.setPoNumber(this.poNumber);
			stcpo.setSaveShippingToAddress(stp.getSaveAddress());
			stcpo.setStreetAddress(stp.getAddress());
			stcpo.setTelephone1(stp.getTelephone());
			stcpo.setDeliveryTime(stp.getDeliveryTime());

			List<PurchaseOrderItemCreatePOParamDTO> listPurchaseOrderItemCreatePOParamDTO = new ArrayList<PurchaseOrderItemCreatePOParamDTO>();
			for (ItemPOParam ipop : stp.getItemPOList()) {
				if(ipop.getShipquantity() != null){
					PurchaseOrderItemCreatePOParamDTO poi = new PurchaseOrderItemCreatePOParamDTO();
					poi.setDeliveryTime(stp.getDeliveryTime());
					poi.setPritemid(ipop.getPritemid());
					poi.setItemId(mapItemIDbyPRid.get(ipop.getPritemid()));
					poi.setItemName(ipop.getName());
					poi.setQuantityPurchaseRequest(ipop.getQuantity());
					poi.setQuantitySend(ipop.getShipquantity());
					poi.setTotalUnitPrices(ipop.getShipquantity() * ipop.getPrice());
					poi.setUnitPrice(ipop.getPrice());
					poi.setVendorId(cpop.getBillTo().getVendorObject().getId());
					poi.setVendorName(cpop.getBillTo().getVendorObject().getNama());
					listPurchaseOrderItemCreatePOParamDTO.add(poi);
				}
			}
			stcpo.setListPurchaseOrderItemCreatePOParamDTO(listPurchaseOrderItemCreatePOParamDTO);
			listShippingToCreatePOParamDTO.add(stcpo);
		}
		this.listShippingToCreatePOParamDTO = listShippingToCreatePOParamDTO;
	}

	public PurchaseOrderCreatePOParamDTO(PurchaseOrder po) {
		if (po.getAddressBook() != null) {
			this.addressBookId = po.getAddressBook().getId();
		}
		this.department = po.getDepartment();
		this.discount = po.getDiscount();
		this.downPayment = po.getDownPayment();
		this.notes = po.getNotes();
		this.poNumber = po.getPoNumber();
		this.purchaseRequestId = po.getPurchaseRequest().getId();
		if (po.getPengadaan() == null) {
			this.pengadaanId = null;
		} else {
			this.pengadaanId = po.getPengadaan().getId();
		}
		this.status = po.getStatus();
		this.tax = po.getTax();
		this.termAndConditionId = po.getTermandcondition().getId();
		this.totalCost = po.getTotalCost();

	}

	public List<PurchaseOrderTerm> getPurchaseOrderTermList() {
		return purchaseOrderTermList;
	}

	public void setPurchaseOrderTermList(
			List<PurchaseOrderTerm> purchaseOrderTermList) {
		this.purchaseOrderTermList = purchaseOrderTermList;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Integer getPurchaseRequestId() {
		return purchaseRequestId;
	}

	public void setPurchaseRequestId(Integer purchaseRequestId) {
		this.purchaseRequestId = purchaseRequestId;
	}

	public Integer getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(Integer addressBookId) {
		this.addressBookId = addressBookId;
	}

	public Integer getPengadaanId() {
		return pengadaanId;
	}

	public void setPengadaanId(Integer pengadaanId) {
		this.pengadaanId = pengadaanId;
	}

	public Integer getTermAndConditionId() {
		return termAndConditionId;
	}

	public void setTermAndConditionId(Integer termAndConditionId) {
		this.termAndConditionId = termAndConditionId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Double getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(Double downPayment) {
		this.downPayment = downPayment;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getShipToThisAddress() {
		return shipToThisAddress;
	}

	public void setShipToThisAddress(Boolean shipToThisAddress) {
		this.shipToThisAddress = shipToThisAddress;
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

	public List<ShippingToCreatePOParamDTO> getListShippingToCreatePOParamDTO() {
		return listShippingToCreatePOParamDTO;
	}

	public void setListShippingToCreatePOParamDTO(
			List<ShippingToCreatePOParamDTO> listShippingToCreatePOParamDTO) {
		this.listShippingToCreatePOParamDTO = listShippingToCreatePOParamDTO;
	}

	public String getCompanyAddrFullName() {
		return companyAddrFullName;
	}

	public void setCompanyAddrFullName(String companyAddrFullName) {
		this.companyAddrFullName = companyAddrFullName;
	}

	public String getCompanyAddrAddress() {
		return companyAddrAddress;
	}

	public void setCompanyAddrAddress(String companyAddrAddress) {
		this.companyAddrAddress = companyAddrAddress;
	}

	public String getCompanyAddrTelephone1() {
		return companyAddrTelephone1;
	}

	public void setCompanyAddrTelephone1(String companyAddrTelephone1) {
		this.companyAddrTelephone1 = companyAddrTelephone1;
	}

	public Boolean getIsSaveTheNewAddressCompany() {
		return isSaveTheNewAddressCompany;
	}

	public void setIsSaveTheNewAddressCompany(Boolean isSaveTheNewAddressCompany) {
		this.isSaveTheNewAddressCompany = isSaveTheNewAddressCompany;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
