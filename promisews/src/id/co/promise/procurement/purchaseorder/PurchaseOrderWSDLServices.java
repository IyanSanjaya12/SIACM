/*package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.dto.FinalPurchaseOrderInterface;
import id.co.promise.procurement.dto.PurchaseOrderDtl;
import id.co.promise.procurement.dto.PurchaseOrderDtlInterface;
import id.co.promise.procurement.dto.PurchaseOrderInterface;
import id.co.promise.procurement.dto.PurchaseOrderItemInterface;
import id.co.promise.procurement.dto.ShippingToDtl;
import id.co.promise.procurement.dto.ShippingToDtlInterface;
import id.co.promise.procurement.dto.ShippingToInterface;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseOrderTerm;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.ShippingTo;
import id.co.promise.procurement.entity.TermAndCondition;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.inisialisasi.PengadaanSession;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.AfcoSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.TermAndConditionSession;
import id.co.promise.procurement.purchaseorder.create.CreatePOParam;
import id.co.promise.procurement.purchaseorder.create.ItemPOParam;
import id.co.promise.procurement.purchaseorder.create.PurchaseOrderCreatePOParamDTO;
import id.co.promise.procurement.purchaseorder.create.ShipToParam;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.BuatParsingJSONDateTypeAdapter;
import id.co.promise.procurement.vendor.VendorSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Stateless
@WebService(serviceName="PurchaseOrder", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaseorder.PurchaseOrderWSDLImpl")
public class PurchaseOrderWSDLServices implements PurchaseOrderWSDLImpl {
	final static Logger log = Logger.getLogger(PurchaseOrderWSDLServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();

	final Gson gson;

	public PurchaseOrderWSDLServices() {
		// BuatParsingJSONDateTypeAdapter
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new BuatParsingJSONDateTypeAdapter());
		gson = builder.create();
	}

	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB
	private PurchaseOrderSession purchaseOrderSession;

	@EJB
	private PurchaseRequestSession purchaseRequestSession;

	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;

	@EJB
	private AfcoSession afcoSession;

	@EJB
	private AddressBookSession addressBookSession;

	@EJB
	private PengadaanSession pengadaanSession;

	@EJB
	private TermAndConditionSession termAndConditionSession;

	@EJB
	TokenSession tokenSession;

	@EJB
	AfcoSession afcoSesssion;

	@EJB
	ApprovalProcessTypeSession approvalProcessTypeSession;

	@EJB
	ApprovalProcessSession approvalProcessSession;

	@EJB
	private ShippingToSession shippingToSession;

	@EJB
	private PurchaseOrderTermSession purchaseOrderTermSession;

	@EJB
	TahapanSession tahapanSession;

	@EJB
	ApprovalSession approvalSession;

	@EJB
	ApprovalLevelSession approvalLevelSession;
	
	@EJB
	VendorSession vendorSession;

	private PurchaseRequest purchaseRequestList;

	public PurchaseOrder getPurchaseOrder(int id) {
		return purchaseOrderSession.getPurchaseOrder(id);
	}

	public Vendor getPurchaseOrderVendor(int id) {
		return purchaseOrderSession.getPurchaseOrderVendor(id);
	}

	public List<PurchaseOrder> getPurchaseOrderList() {
		return purchaseOrderSession.getPurchaseOrderList();
	}

	public int countByStatus(String status) {
		return purchaseOrderSession.countPOByStatus(status);
	}

	public Response getPurchaseOrderDTOList(Integer pageNumber, Integer numberOfRowPerPage,
			 String searchingKeyWord,  String token) {

		try {
			if (pageNumber == null || numberOfRowPerPage == null || pageNumber == 0 || numberOfRowPerPage == 0) {
				pageNumber = 1;
				numberOfRowPerPage = 10;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}

		try {
			Integer endIndex = pageNumber * numberOfRowPerPage;
			Integer startIndex = endIndex - numberOfRowPerPage + 1;
			if (searchingKeyWord == null || searchingKeyWord.trim().equals("")) {
				return Response.status(201).entity(purchaseOrderSession.getPurchaseOrderDTOListWithRange(startIndex, endIndex)).build();
			} else {
				return Response.status(201).entity(purchaseOrderSession.getPurchaseOrderDTOListWithRange(startIndex, endIndex, searchingKeyWord)).build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public Response getPurchaseRequestByNumber(String prNumber) {
		try {
			return Response.status(201).entity(purchaseRequestSession.getListByPRNumber(prNumber)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}
	}

	public Response getAfcoByCompanyName(String companyName) {
		try {
			return Response.status(201).entity(afcoSession.getAfcoByCompanyName(companyName)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}
	}

	public PurchaseOrder insertPurchaseOrder(String poNumber, Integer purchaseRequest, Integer afco,
			 Integer addressBook,  Integer pengadaan,  Integer termAndCondition,  String notes,
			 Double downPayment,  Double discount,  Double tax,  Double totalCost,
			 String status,  String token) {

		purchaseRequestList = purchaseRequestSession.get(purchaseRequest);

		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setPurchaseRequest(purchaseRequestSession.find(purchaseRequest));
		purchaseOrder.setAddressBook(addressBookSession.find(addressBook));
		purchaseOrder.setPengadaan(pengadaanSession.find(pengadaan));
		purchaseOrder.setTermandcondition(termAndConditionSession.find(termAndCondition));
		purchaseOrder.setDepartment(purchaseRequestList.getDepartment());
		purchaseOrder.setCreateDate(new Date());
		purchaseOrder.setPurchaseOrderDate(new Date());
		purchaseOrder.setNotes(notes);
		purchaseOrder.setDownPayment(downPayment);
		purchaseOrder.setDiscount(discount);
		purchaseOrder.setTax(tax);
		purchaseOrder.setTotalCost(totalCost);
		purchaseOrder.setStatus(status);
		purchaseOrder.setCreated(new Date());
		purchaseOrderSession.insertPurchaseOrder(purchaseOrder, tokenSession.findByToken(token));
		return purchaseOrder;
	}

	public Response getPurchaseOrderDTOListByPoId(Integer poId,  String token) {

		try {
			if (!(poId instanceof Integer)) {
				throw new RuntimeException("Parameter bukan Numerik");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}
		try {
			return Response.status(201).entity(purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(poId)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public PurchaseOrder updatePurchaseOrder(int id,  String poNumber,  Integer purchaseRequest,  Integer afco,
			 Integer addressBook,  Integer pengadaan,  Integer termAndCondition,  String notes,
			 Double downPayment, Double discount, Double tax, Double totalCost,
			 String status, String token) {

		PurchaseOrder purchaseOrder = purchaseOrderSession.find(id);
		purchaseOrder.setPurchaseRequest(purchaseRequestSession.find(purchaseRequest));
		purchaseOrder.setAddressBook(addressBookSession.find(addressBook));
		purchaseOrder.setPengadaan(pengadaanSession.find(pengadaan));
		purchaseOrder.setTermandcondition(termAndConditionSession.find(termAndCondition));
		purchaseOrder.setCreated(new Date());
		purchaseOrder.setPurchaseOrderDate(new Date());
		purchaseOrder.setNotes(notes);
		purchaseOrder.setDownPayment(downPayment);
		purchaseOrder.setDiscount(discount);
		purchaseOrder.setTax(tax);
		purchaseOrder.setTotalCost(totalCost);
		purchaseOrder.setStatus(status);
		purchaseOrder.setUpdated(new Date());
		purchaseOrderSession.updatePurchaseOrder(purchaseOrder, tokenSession.findByToken(token));
		return purchaseOrder;
	}

	public PurchaseOrder deletePurchaseOrder(int id, String token) {
		return purchaseOrderSession.deletePurchaseOrder(id, tokenSession.findByToken(token));
	}

	public PurchaseOrder deleteRowPurchaseOrder(int id, String token) {
		return purchaseOrderSession.deleteRowPurchaseOrder(id, tokenSession.findByToken(token));
	}

	public Response createPO(String jsonString, String token) {

		CreatePOParam cpop = new CreatePOParam();
		PurchaseOrderCreatePOParamDTO pto;
		try {
			cpop = gson.fromJson(jsonString, CreatePOParam.class);

			if (cpop.getBillTo().getAddressId() == null) {
				if (cpop.getBillTo().getSaveAddress()) {
					AddressBook billToAddress = new AddressBook();
					billToAddress.setAfco(afcoSession.getAfco(cpop.getBillTo().getCompanyId()));
					billToAddress.setFullName(cpop.getBillTo().getFullName());
					billToAddress.setStreetAddress(cpop.getBillTo().getAddress());
					billToAddress.setAddressLabel(cpop.getBillTo().getFullName());
					billToAddress.setTelephone(cpop.getBillTo().getTelephone());
					billToAddress = addressBookSession.insertAddressBook(billToAddress, tokenSession.findByToken(token));
					cpop.getBillTo().setAddressId(billToAddress.getId());
					log.info("ADDRESS ID bill TO: " + billToAddress.getId());
				}
			}

			Map<Integer, Integer> mapItemIDbyPRid = new HashMap<Integer, Integer>();
			for (ShipToParam stp : cpop.getShipToList()) {
				if (cpop.getBillTo().getShipToThisAddress() != null && cpop.getBillTo().getShipToThisAddress()) {
					stp.setAddressId(cpop.getBillTo().getAddressId());
				}
				for (ItemPOParam ipop : stp.getItemPOList()) {
					PurchaseRequestItem pri = purchaseRequestItemSession.getPurchaseRequestItem(ipop.getPritemid());
					mapItemIDbyPRid.put(pri.getId(), pri.getItem().getId());
				}

			}
			pto = new PurchaseOrderCreatePOParamDTO(cpop, mapItemIDbyPRid);
		} catch (Exception e) {
			log.error(e.getMessage() + " | " + jsonString, e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}

		try {
			return Response.status(201).entity(purchaseOrderSession.createPurchaseOrder(pto, tokenSession.findByToken(token))).build();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public Response updatePO(String jsonString, String token) {

		CreatePOParam cpop = new CreatePOParam();
		PurchaseOrderCreatePOParamDTO pto;
		try {
			cpop = gson.fromJson(jsonString, CreatePOParam.class);

			if (cpop.getBillTo().getAddressId() == null) {
				if (cpop.getBillTo().getSaveAddress()) {
					AddressBook billToAddress = new AddressBook();
					billToAddress.setAfco(afcoSession.getAfco(cpop.getBillTo().getCompanyId()));
					billToAddress.setFullName(cpop.getBillTo().getFullName());
					billToAddress.setStreetAddress(cpop.getBillTo().getAddress());
					billToAddress.setAddressLabel(cpop.getBillTo().getFullName());
					billToAddress.setTelephone(cpop.getBillTo().getTelephone());
					billToAddress = addressBookSession.insertAddressBook(billToAddress, tokenSession.findByToken(token));
					cpop.getBillTo().setAddressId(billToAddress.getId());
					log.info("ADDRESS ID bill TO: " + billToAddress.getId());
				}
			}

			Map<Integer, Integer> mapItemIDbyPRid = new HashMap<Integer, Integer>();
			for (ShipToParam stp : cpop.getShipToList()) {
				if (cpop.getBillTo().getShipToThisAddress() != null && cpop.getBillTo().getShipToThisAddress()) {
					stp.setAddressId(cpop.getBillTo().getAddressId());
				}
				for (ItemPOParam ipop : stp.getItemPOList()) {
					PurchaseRequestItem pri = purchaseRequestItemSession.getPurchaseRequestItem(ipop.getPritemid());
					mapItemIDbyPRid.put(pri.getId(), pri.getItem().getId());
				}

			}
			pto = new PurchaseOrderCreatePOParamDTO(cpop, mapItemIDbyPRid);
		} catch (Exception e) {
			log.error(e.getMessage() + " | " + jsonString, e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}

		try {
			return Response.status(201).entity(purchaseOrderSession.createPurchaseOrder(pto, tokenSession.findByToken(token))).build();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public PurchaseOrder updateStatusApproval(Integer id, String nextApproval, Integer status,
			String token) {

		Token tkn = tokenSession.findByToken(token);

		PurchaseOrder po = purchaseOrderSession.find(id);

		// find next approval
		ApprovalProcessType approvalProcessType = approvalProcessTypeSession.findByTahapanAndValueId(27, id); // 27
																												// :
																												// PO
		List<ApprovalProcess> approvalProcessList = approvalProcessSession.findByApprovalProcessType(approvalProcessType.getId());
		int index = 0;
		String statusPO = "Approval Process";
		for (ApprovalProcess approvalProcess : approvalProcessList) {
			// cek jika dia aktif ambil next process
			if (status == 3) { // status aprove di purchase request
				if (approvalProcess.getStatus() == 1) { // status di approval
														// process
					String approvalObject = "";
					if (approvalProcess.getUser() != null)
						approvalObject = approvalProcess.getUser().getNamaPengguna();
					if (approvalProcess.getGroup() != null)
						approvalObject = approvalProcess.getUser().getNamaPengguna();
					if (approvalProcess.getUser() != null && approvalProcess.getGroup() != null)
						approvalObject = approvalProcess.getUser().getNamaPengguna() + ", " + approvalProcess.getUser().getNamaPengguna();

					nextApproval = approvalObject;
					po.setStatusProses(3);
					break;
				}
			} else if (status == 4 || status == 2) { // hold or reject
				if (approvalProcess.getStatus() == 4 || approvalProcess.getStatus() == 2) {
					String approvalObject = "";
					if (approvalProcess.getUser() != null)
						approvalObject = approvalProcess.getUser().getNamaPengguna();
					if (approvalProcess.getGroup() != null)
						approvalObject = approvalProcess.getUser().getNamaPengguna();
					if (approvalProcess.getUser() != null && approvalProcess.getGroup() != null)
						approvalObject = approvalProcess.getUser().getNamaPengguna() + ", " + approvalProcess.getUser().getNamaPengguna();

					nextApproval = approvalObject + ", " + nextApproval;
					if (approvalProcess.getStatus().equals(2)) {
						statusPO = "Reject";
						po.setStatusProses(5);
					} else {
						statusPO = "Hold";
						po.setStatusProses(4);
					}
					break;
				}
			}
			index++;
		}

		// jika sudah diapprove semua
		if (index == approvalProcessList.size()) {
			statusPO = "On Process";
			nextApproval = "";
			po.setStatusProses(6);
		}

		// update pr
		po.setNextapproval(nextApproval);
		po.setStatus(statusPO);
		po.setUpdated(new Date());

		List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(po.getId());
		for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
			purchaseOrderItem.setStatus(statusPO);
			purchaseOrderItem.setPurchaseOrder(po);
			purchaseOrderItem.setStatusProses(po.getStatusProses());
			purchaseOrderItemSession.updatePurchaseOrderItem(purchaseOrderItem, tkn);
		}

		return po;
	}

	public Vendor getPurchaseOrderVendorById(int id) {
		return purchaseOrderSession.getPurchaseOrderVendorById(id);
	}

	public List<PurchaseOrder> getPurchaseOrderListByVendorForPerformance(Integer id) {
		return purchaseOrderSession.getPurchaseOrderListByVendorForPerformance(id);
	}

	public PurchaseOrderDtl getPurchaseOrderDetailById(Integer id) {
		PurchaseOrderDtl purchaseOrderDtl = new PurchaseOrderDtl();

		PurchaseOrder purchaseOrder = purchaseOrderSession.getPurchaseOrder(id);
		purchaseOrderDtl.setPurchaseOrder(purchaseOrder);

		List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
		if(purchaseOrder.getKontrakFk() == null){ 
			purchaseRequestItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaseOrder.getPurchaseRequest().getId());
			purchaseOrderDtl.setPurchaseRequestItemList(purchaseRequestItemList);
		}

		List<ShippingToDtl> shippingToDtlList = new ArrayList<ShippingToDtl>();

		List<ShippingTo> shippingToList = shippingToSession.findShippingByPO(id);
		for (ShippingTo shippingTo : shippingToList) {
			ShippingToDtl shippingToDtl = new ShippingToDtl();
			shippingToDtl.setShippingTo(shippingTo);

			List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getPurchaseOrderItemListByShipping(shippingTo.getId());
			shippingToDtl.setPurchaseOrderItemList(purchaseOrderItemList);
			
			if(purchaseOrder.getKontrakFk() != null){ 
				for(PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList){
					PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
					purchaseRequestItem = purchaseOrderItem.getPurchaseRequestItem();
					

					PurchaseRequestItem purchaseRequestItemNew = new PurchaseRequestItem();
					purchaseRequestItemNew.setPurchaserequest(purchaseRequestItem.getPurchaserequest());
					purchaseRequestItemNew.setVendor(purchaseRequestItem.getVendor());
					purchaseRequestItemNew.setItem(purchaseRequestItem.getItem());
					purchaseRequestItemNew.setMataUang(purchaseRequestItem.getMataUang());
					purchaseRequestItemNew.setItemname(purchaseRequestItem.getItemname());
					purchaseRequestItemNew.setKode(purchaseRequestItem.getKode());
					purchaseRequestItemNew.setVendorname(purchaseRequestItem.getVendorname());

					purchaseRequestItemNew.setQuantitybalance(purchaseRequestItem.getQuantitybalance());
					purchaseRequestItemNew.setPrice(purchaseRequestItem.getPrice());
					purchaseRequestItemNew.setCostcenter(purchaseRequestItem.getCostcenter());
					purchaseRequestItemNew.setStatus(purchaseRequestItem.getStatus());
					purchaseRequestItemNew.setTotal(purchaseRequestItem.getTotal());
					purchaseRequestItemNew.setUnit(purchaseRequestItem.getUnit());
					purchaseRequestItemNew.setSpecification(purchaseRequestItem.getSpecification());
					purchaseRequestItemNew.setPath(purchaseRequestItem.getPath());
					purchaseRequestItemNew.setCreated(purchaseRequestItem.getCreated());
					purchaseRequestItemNew.setUpdated(purchaseRequestItem.getUpdated());
					purchaseRequestItemNew.setDeleted(purchaseRequestItem.getDeleted());
					purchaseRequestItemNew.setUserId(purchaseRequestItem.getUserId());
					purchaseRequestItemNew.setIsDelete(purchaseRequestItem.getIsDelete());
					purchaseRequestItemNew.setId(purchaseRequestItem.getId());
					
					*//** set quantity PR berdasarkan data kontrak **//*
					purchaseRequestItemNew.setQuantity(purchaseOrderItem.getQuantityPurchaseRequest());
					
					if(purchaseRequestItemList.size() > 0){
						if(!purchaseRequestItemList.contains(purchaseRequestItemNew)){
							purchaseRequestItemList.add(purchaseRequestItemNew);
						}
					}else{
						purchaseRequestItemList.add(purchaseRequestItemNew);
					}
					purchaseOrderItem.setPurchaseRequestItem(purchaseRequestItemNew);
				}
				purchaseOrderDtl.setPurchaseRequestItemList(purchaseRequestItemList);
			}

			shippingToDtl.setPurchaseRequestItemList(purchaseRequestItemList);

			shippingToDtlList.add(shippingToDtl);

			if (purchaseOrderItemList.size() > 0) {
				if (purchaseOrderItemList.get(0).getVendor() != null) {
					Vendor vendor = purchaseOrderItemList.get(0).getVendor();
					purchaseOrderDtl.setVendor(vendor);
				}
			}
		}

		purchaseOrderDtl.setShippingToDtlList(shippingToDtlList);

		List<PurchaseOrderTerm> purchaseOrderTermList = purchaseOrderTermSession.getPurchaseOrderByPO(id);
		if (purchaseOrderTermList != null) {
			if (purchaseOrderTermList.size() > 0) {
				purchaseOrderDtl.setPurchaseOrderTerm(purchaseOrderTermList.get(0));
			}
		}

		return purchaseOrderDtl;
	}

	public PurchaseOrderDtl updatePurchaseOrderDtl(PurchaseOrderDtl purchaseOrderDtl, String authorization) {

		Token token = tokenSession.findByToken(authorization);

		PurchaseOrder purchaseOrder = purchaseOrderDtl.getPurchaseOrder();
		purchaseOrder.setStatus("Approval Process");
		purchaseOrder.setStatusProses(1);

		AddressBook addressBookPurchaseOrder = new AddressBook();
		addressBookPurchaseOrder = purchaseOrder.getAddressBook();

		if (purchaseOrder.getAddressBook().getId().equals(0)) {
			addressBookPurchaseOrder.setStreetAddress(purchaseOrder.getStreetAddress());
			addressBookPurchaseOrder.setTelephone(purchaseOrder.getTelephone1());
			addressBookPurchaseOrder.setFullName(purchaseOrder.getFullName());

			addressBookPurchaseOrder.setId(null);
			addressBookPurchaseOrder.setStatus(true);
			addressBookPurchaseOrder.setAfco(afcoSesssion.getAfco(addressBookPurchaseOrder.getAfco().getId()));
			addressBookPurchaseOrder = addressBookSession.insertAddressBook(addressBookPurchaseOrder, token);
			purchaseOrder.setAddressBook(addressBookPurchaseOrder);
		}
		purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);

		List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(purchaseOrder.getId());
		for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
			ShippingTo shippingTo = purchaseOrderItem.getShippingTo();
			shippingTo.setIsDelete(1);
			shippingTo.setPurchaseOrder(purchaseOrder);
			purchaseOrderItem.setPurchaseOrder(purchaseOrder);
			purchaseOrderItem.setShippingTo(shippingTo);
			purchaseOrderItem.setIsDelete(1);
			purchaseOrderItemSession.updatePurchaseOrderItem(purchaseOrderItem, token);
		}

		List<PurchaseOrderTerm> purchaseOrderTermList = purchaseOrderTermSession.getPurchaseOrderByPO(purchaseOrder.getId());
		for (PurchaseOrderTerm purchaseOrderTerm : purchaseOrderTermList) {
			purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
			purchaseOrderTerm.setIsDelete(1);
			purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
			purchaseOrderTermSession.updatePurchaseOrderTerm(purchaseOrderTerm, token);
		}

		List<ShippingToDtl> shippingToDtlList = purchaseOrderDtl.getShippingToDtlList();
		for (ShippingToDtl shippingToDtl : shippingToDtlList) {

			ShippingTo shippingToNew = new ShippingTo();
			shippingToNew = shippingToDtl.getShippingTo();

			AddressBook addressBookShipping = new AddressBook();
			addressBookShipping = shippingToNew.getAddressBook();

			if (shippingToNew.getAddressBook().getId().equals(0)) {
				addressBookShipping.setStreetAddress(shippingToNew.getStreetAddress());
				addressBookShipping.setTelephone(shippingToNew.getTelephone1());
				addressBookShipping.setFullName(shippingToNew.getFullName());

				addressBookShipping.setId(null);
				addressBookShipping.setStatus(true);
				addressBookShipping.setAfco(afcoSesssion.getAfco(addressBookShipping.getAfco().getId()));
				addressBookShipping = addressBookSession.insertAddressBook(addressBookShipping, token);

				shippingToNew.setAddressBook(addressBookShipping);
			}

			shippingToNew.setId(null);
			shippingToNew.setPurchaseOrder(purchaseOrder);
			shippingToNew = shippingToSession.inserShippingTo(shippingToNew, token);

			List<PurchaseOrderItem> purchaseOrderItemListNew = shippingToDtl.getPurchaseOrderItemList();
			for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemListNew) {
				PurchaseOrderItem purchaseOrderItemNew = new PurchaseOrderItem();
				if (purchaseOrderItem.getQuantitySend() != null && purchaseOrderItem.getQuantitySend() != 0) {
					purchaseOrderItemNew = purchaseOrderItem;
					purchaseOrderItemNew.setId(null);
					purchaseOrderItemNew.setShippingTo(shippingToNew);
					purchaseOrderItemNew.setPurchaseOrder(purchaseOrder);
					purchaseOrderItemNew.setStatus("Approval Process");
					purchaseOrderItemNew.setStatusProses(1);
					purchaseOrderItemNew.setPurchaseRequestItem(purchaseRequestItemSession.getPurchaseRequestItem(purchaseOrderItem.getPurchaseRequestItem().getId()));
					purchaseOrderItemSession.insertPurchaseOrderItem(purchaseOrderItemNew, token);
				}
			}
		}

		PurchaseOrderTerm purchaseOrderTerm = new PurchaseOrderTerm();
		purchaseOrderTerm = purchaseOrderDtl.getPurchaseOrderTerm();
		purchaseOrderTerm.setId(null);
		purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
		purchaseOrderTermSession.inserPurchaseOrderTerm(purchaseOrderTerm, token);

		List<ApprovalProcessType> approvalProcessTypeList = approvalProcessTypeSession.findByPurchaseOrder(purchaseOrder.getId());
		if(approvalProcessTypeList != null){
			if(approvalProcessTypeList.size() > 0){

				for (ApprovalProcessType approvalProcessType : approvalProcessTypeList) {
					approvalProcessType.setIsDelete(1);

					List<ApprovalProcess> approvalProcesseList = approvalProcessSession.findByApprovalProcessType(approvalProcessType.getId());
					if(approvalProcesseList != null){
						if(approvalProcesseList.size() > 0){
							for (ApprovalProcess approvalProcess : approvalProcesseList) {
								approvalProcess.setApprovalProcessType(approvalProcessType);
								approvalProcess.setIsDelete(1);
								approvalProcessSession.update(approvalProcess, token);
							}
						}else{
							approvalProcessTypeSession.delete(approvalProcessType, token);
						}
					}else{
						approvalProcessTypeSession.delete(approvalProcessType, token);
					}
				}
			}
		}

		ApprovalProcessType approvalProcessType = new ApprovalProcessType();
		approvalProcessType.setValueId(purchaseOrder.getId());
		approvalProcessType.setTahapan(tahapanSession.find(27));
		approvalProcessType = approvalProcessTypeSession.create(approvalProcessType, token);

		List<ApprovalLevel> approvalLevelList = approvalLevelSession.findByApproval(approvalSession.find(purchaseOrder.getApprovalId()));
		for (ApprovalLevel approvalLevel : approvalLevelList) {
			ApprovalProcess approvalProcess = new ApprovalProcess();
			approvalProcess.setApprovalProcessType(approvalProcessType);
			approvalProcess.setApprovalLevel(approvalLevel);
			approvalProcess.setStatus(1);
			approvalProcess.setGroup(approvalLevel.getGroup());
			approvalProcess.setUser(approvalLevel.getUser());
			approvalProcess.setThreshold(approvalLevel.getThreshold());
			approvalProcess.setSequence(approvalLevel.getSequence());
			approvalProcessSession.create(approvalProcess, token);
		}

		return purchaseOrderDtl;
	}

	public Response createPurchaseRequestServicesFromContract(String jsonRequest, String authorization) {

		Token token = tokenSession.findByToken(authorization);
		
		try {
			Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
			
			FinalPurchaseOrderInterface finalPurchaseOrderInterface = gson.fromJson(jsonRequest, FinalPurchaseOrderInterface.class);
			List<PurchaseOrderDtlInterface> purchaseOrderDtlInterfaceList = finalPurchaseOrderInterface.getPurchaseOrderDtlInterfaceList();
			
			for(PurchaseOrderDtlInterface purchaseOrderDtlInterface : purchaseOrderDtlInterfaceList){
				PurchaseOrderInterface purchaseOrderInterface =  purchaseOrderDtlInterface.getPurchaseOrderInterface();
				
				List<PurchaseOrder> purchaseOrderList = purchaseOrderSession.getPurchaseOrderListByTermin(purchaseOrderInterface.getTerminFk());
				if(purchaseOrderList.size() == 0){

					*//** set purchase order **//*
					PurchaseOrder purchaseOrder = new PurchaseOrder();
					purchaseOrder.setKontrakFk(purchaseOrderInterface.getKontrakFk());
					purchaseOrder.setTerminFk(purchaseOrderInterface.getTerminFk());
					
					if(purchaseOrderInterface.getPengadaanId() != null){
						Pengadaan pengadaan = pengadaanSession.getPengadaan(purchaseOrderInterface.getPengadaanId());
						if(pengadaan != null){
							purchaseOrder.setPengadaan(pengadaan);
						}
					}
					
					PurchaseRequest purchaseRequest = purchaseRequestSession.get(purchaseOrderInterface.getPurchaseRequestId());
					if(purchaseRequest!= null){
						purchaseOrder.setPurchaseRequest(purchaseRequest);
						purchaseOrder.setAddressLabel(purchaseRequest.getAfco().getCompanyName());
					}
					
					purchaseOrder.setStatus("Request From Contract");
					purchaseOrder.setStatusProses(1);
					
					TermAndCondition termAndCondition = termAndConditionSession.getTermAndCondition(purchaseOrderInterface.getTerminFk());
					if(termAndCondition != null){
						purchaseOrder.setTermandcondition(termAndCondition);
					}
					
					purchaseOrder.setPoNumber("");
					purchaseOrder.setSubTotal(purchaseOrderInterface.getSubTotal());
					purchaseOrder.setTotalCost(purchaseOrderInterface.getTotalCost());
			 		
			 		Afco afco = new Afco();
			 		if(purchaseOrderInterface.getAfcoPk() != null){
				 		afco = afcoSesssion.getAfco(purchaseOrderInterface.getAfcoPk());
				 		if(afco != null){
				 			purchaseOrder.setDepartment(afco.getCompanyName());
				 		}
			 		}
					
					purchaseOrder = purchaseOrderSession.insertPurchaseOrder(purchaseOrder, token);
					
				 	List<ShippingToDtlInterface> shippingToDtlInterfaceList = purchaseOrderDtlInterface.getShippingToDtlInterfaceList();
				 	
				 	for(ShippingToDtlInterface shippingToDtlInterface : shippingToDtlInterfaceList){
				 		ShippingToInterface shippingToInterface = shippingToDtlInterface.getShippingToInterface();
				 		
				 		ShippingTo shippingTo = new ShippingTo();
				 		shippingTo.setPurchaseOrder(purchaseOrder);
				 		shippingTo.setDeliveryTime(shippingToInterface.getDeliveryTime());
				 		
				 		if(afco != null){
					 		shippingTo.setAfco(afco);
				 		}
				 		
				 		shippingTo = shippingToSession.inserShippingTo(shippingTo, token);
				 					 		
				 		List<PurchaseOrderItemInterface> purchaseOrderItemInterfaceList = shippingToDtlInterface.getPurchaseOrderItemInterfaceList();
				 		for(PurchaseOrderItemInterface purchaseOrderItemInterface : purchaseOrderItemInterfaceList){
				 			
				 			PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
				 			purchaseOrderItem.setQuantityPurchaseRequest(purchaseOrderItemInterface.getTermQuantity());
				 			
				 			if(purchaseOrderItemInterface.getPurchaseRequestItemId() != null){
					 			PurchaseRequestItem purchaseRequestItem = purchaseRequestItemSession.getPurchaseRequestItem(purchaseOrderItemInterface.getPurchaseRequestItemId());
					 			if(purchaseRequestItem != null){
						 			purchaseOrderItem.setPurchaseRequestItem(purchaseRequestItem);
						 			purchaseOrderItem.setItem(purchaseRequestItem.getItem());
						 			purchaseOrderItem.setItemName(purchaseRequestItem.getItemname());
					 			}
				 			}
				 			
				 			purchaseOrderItem.setUnitPrice(purchaseOrderItemInterface.getUnitPrice());
				 			purchaseOrderItem.setTotalUnitPrices(purchaseOrderItemInterface.getTotalUnitPrices());
				 			purchaseOrderItem.setQuantitySend(purchaseOrderItemInterface.getTermQuantity());
				 			
				 			if(purchaseOrderInterface.getVendorPk()!= null){
					 			Vendor vendor = vendorSession.getVendor(purchaseOrderInterface.getVendorPk());
					 			if(vendor != null){
						 			purchaseOrderItem.setVendor(vendor);
						 			purchaseOrderItem.setVendorName(vendor.getNama());
					 			}
				 			}
				 			
				 			purchaseOrderItem.setPurchaseOrder(purchaseOrder);
				 			purchaseOrderItem.setShippingTo(shippingTo);
				 			purchaseOrderItem.setStatus("Request From Contract");
				 			purchaseOrderItem.setStatusProses(1);
				 			
				 			purchaseOrderItem = purchaseOrderItemSession.insertPurchaseOrderItem(purchaseOrderItem, token);
				 		}
				 	}
				 	
				 	PurchaseOrderTerm purchaseOrderTerm = new PurchaseOrderTerm();
				 	purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
				 	purchaseOrderTerm.setPoTermType(2); *//** defaultnya value **//*
				 	purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
				 	if(termAndCondition != null){
				 		purchaseOrderTerm.setTermandcondition(termAndCondition);
				 	}
				 	purchaseOrderTermSession.inserPurchaseOrderTerm(purchaseOrderTerm, token);
				}
				
			}
			
			finalPurchaseOrderInterface.setRequestId(0);
			
			String json = gson.toJson(finalPurchaseOrderInterface);

			return Response.ok().entity(json).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok().status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}
}
*/