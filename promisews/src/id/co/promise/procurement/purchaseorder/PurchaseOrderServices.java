/**
 * fdf
 */
package id.co.promise.procurement.purchaseorder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import erp.service.PurchaseOrderInterfacingService;
import id.co.promise.procurement.DTO.PurchaseOrderDtl;
import id.co.promise.procurement.DTO.ShippingToDtl;
import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseOrderTerm;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.ShippingTo;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.TermAndCondition;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.AddressBookDTO;
import id.co.promise.procurement.master.AddressBookDTOForCreatePO;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.AfcoSession;
import id.co.promise.procurement.master.AutoNumberSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.TermAndConditionSession;
import id.co.promise.procurement.purchaseorder.create.CreatePOParam;
import id.co.promise.procurement.purchaseorder.create.ItemPOParam;
import id.co.promise.procurement.purchaseorder.create.PurchaseOrderCreatePOParamDTO;
import id.co.promise.procurement.purchaseorder.create.ShipToParam;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.BuatParsingJSONDateTypeAdapter;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.vendor.VendorSession;
import sap.interfacing.soap.po.SapPoFunction;
import sap.interfacing.soap.rfq.SapRfqFunction;

/**
 * @author User
 *
 */

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/procurement/purchaseorder/PurchaseOrderServices")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseOrderServices {
	final static Logger log = Logger.getLogger(PurchaseOrderServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();

	final Gson gson;

	public PurchaseOrderServices() {
		// BuatParsingJSONDateTypeAdapter
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new BuatParsingJSONDateTypeAdapter());
		gson = builder.create();
	}

	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;
	
	@EJB
	SapRfqFunction sapRfqFunction;

	@EJB
	private PurchaseOrderSession purchaseOrderSession;

	@EJB
	private PurchaseRequestSession purchaseRequestSession;

	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;

	@EJB
	private AddressBookSession addressBookSession;

	@EJB
	private TermAndConditionSession termAndConditionSession;

	@EJB
	private OrganisasiSession organisasiSession;

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

	@EJB
	AutoNumberSession autoNumberSession;

	@EJB
	EmailNotificationSession emailNotificationSession;

	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	private PurchaseOrderInterfacingService purchaseOrderInterfacingService ;
	
	@EJB
	private SapPoFunction sapPoFunction;

	private PurchaseRequest purchaseRequestList;

	@Resource private UserTransaction userTransaction;
	@EJB ProcedureSession procedureSession;
	
	@Path("/getPurchaseOrder/{id}")
	@GET
	public PurchaseOrder getPurchaseOrder(@PathParam("id") int id) {
		return purchaseOrderSession.getPurchaseOrder(id);
	}

	@Path("/getPurchaseOrderVendor/{id}")
	@GET
	public Vendor getPurchaseOrderVendor(@PathParam("id") int id) {
		return purchaseOrderSession.getPurchaseOrderVendor(id);
	}

	@Path("/getPurchaseOrderByPoNumber")
	@POST
	public PurchaseOrder getPurchaseOrderByPoNumber(@FormParam("poNumber") String poNumber) {
		return purchaseOrderSession.getPurchaseOrderByPoNumber(poNumber);
	}

	@Path("/getPurchaseOrderList")
	@GET
	public List<PurchaseOrder> getPurchaseOrderList() {
		return purchaseOrderSession.getPurchaseOrderList();
	}
	
	@Path("/get-vendor-list")
	@GET
	public List<Vendor> getVendorList() {
		return vendorSession.getList();
	}

	@Path("/countByStatus")
	@POST
	public int countByStatus(@FormParam("status") String status) {
		return purchaseOrderSession.countPOByStatus(status);
	}

	@Path("/countByStatusAndOrg")
	@POST
	public int countByStatusAndOrg(@FormParam("status") String status, @HeaderParam("Authorization") String token) {
		return purchaseOrderSession.countPOByStatusAndOrg(status, tokenSession.findByToken(token));
	}

	@Path("/countByStatusAndOrgVendor")
	@POST
	public int countByStatusAndOrgVendor(@FormParam("status") String status,
			@HeaderParam("Authorization") String token) {
		return purchaseOrderSession.countPOByStatusAndOrgVendor(status, tokenSession.findByToken(token));
	}

	@Path("/getponumber")
	@GET
	public Response getPoNumber(@HeaderParam("Authorization") String token) {
		Map<String, String> mapDTO = new HashMap<String, String>();
		mapDTO.put("po", autoNumberSession.generateNumber("PO", tokenSession.findByToken(token)));
		return Response.ok(mapDTO).build();
	}

	@Path("/getPurchaseOrderListWithPagination")
	@POST
	public Response getPurchaseOrderDTOList(@FormParam("pageNumber") Integer pageNumber,
			@FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("searchingKeyWord") String searchingKeyWord, @HeaderParam("Authorization") String token) {

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
				return Response.status(201)
						.entity(purchaseOrderSession.getPurchaseOrderDTOListWithRange(startIndex, endIndex)).build();
			} else {
				return Response.status(201).entity(
						purchaseOrderSession.getPurchaseOrderDTOListWithRange(startIndex, endIndex, searchingKeyWord))
						.build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/getPurchaseOrderListWithPaginationVendor")
	@POST
	public Response getPurchaseOrderDTOListVendor(@FormParam("pageNumber") Integer pageNumber,
			@FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("searchingKeyWord") String searchingKeyWord, @HeaderParam("Authorization") String token) {

		Token tokenObj = tokenSession.findByToken(token);

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
				return Response.status(201).entity(
						purchaseOrderSession.getPurchaseOrderDTOListWithRangeVendor(startIndex, endIndex, tokenObj))
						.build();
			} else {
				return Response.status(201).entity(purchaseOrderSession
						.getPurchaseOrderDTOListWithRangeVendor(startIndex, endIndex, searchingKeyWord, tokenObj))
						.build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/getPurchaseOrderListByOrgWithPagination")
	@POST
	public Response getPurchaseOrderListByOrgWithPagination(@FormParam("pageNumber") Integer pageNumber,
			@FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("searchingKeyWord") String searchingKeyWord, @HeaderParam("Authorization") String token) {

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
				return Response.status(201).entity(purchaseOrderSession
						.getPurchaseOrderDTOListByOrgWithRange(startIndex, endIndex, tokenSession.findByToken(token)))
						.build();
			} else {
				return Response.status(201)
						.entity(purchaseOrderSession.getPurchaseOrderDTOListByOrgWithRange(startIndex, endIndex,
								searchingKeyWord, tokenSession.findByToken(token)))
						.build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/getPurchaseRequestByNumber/{prNumber}")
	@GET
	public Response getPurchaseRequestByNumber(@PathParam("prNumber") String prNumber) {
		try {
			return Response.status(201).entity(purchaseRequestSession.getListByPRNumber(prNumber)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/insertPurchaseOrder")
	@POST
	public PurchaseOrder insertPurchaseOrder(@FormParam("poNumber") String poNumber,
			@FormParam("purchaseRequest") Integer purchaseRequest, @FormParam("afco") Integer afco,
			@FormParam("addressBook") Integer addressBook, @FormParam("pengadaan") Integer pengadaan,
			@FormParam("termAndCondition") Integer termAndCondition, @FormParam("notes") String notes,
			@FormParam("downPayment") Double downPayment, @FormParam("discount") Double discount,
			@FormParam("tax") Double tax, @FormParam("totalCost") Double totalCost, @FormParam("status") String status,
			@HeaderParam("Authorization") String token) {

		purchaseRequestList = purchaseRequestSession.get(purchaseRequest);

		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setPurchaseRequest(purchaseRequestSession.find(purchaseRequest));
		purchaseOrder.setAddressBook(addressBookSession.find(addressBook));
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

	@Path("/getPurchaseOrderListByPoId")
	@POST
	public Response getPurchaseOrderDTOListByPoId(@FormParam("poId") Integer poId,
			@HeaderParam("Authorization") String token) {

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

	@Path("/deletePurchaseOrder/{id}")
	@GET
	public PurchaseOrder deletePurchaseOrder(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return purchaseOrderSession.deletePurchaseOrder(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRowPurchaseOrder/{id}")
	@POST
	public PurchaseOrder deleteRowPurchaseOrder(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return purchaseOrderSession.deleteRowPurchaseOrder(id, tokenSession.findByToken(token));
	}
	
	@SuppressWarnings({"rawtypes", "unused"})
	@Path("/insert")
	@POST
	public Map insert(PurchaseOrderAddEditDTO purchaseOrderAddEditDTO,@HeaderParam("Authorization") String authorization, @Context UriInfo path ) throws Exception{
		userTransaction.begin();
		Token token = tokenSession.findByToken(authorization);
		Map<Object, Object> map = new HashMap<Object, Object>();
		PurchaseRequest purchaseRequest = purchaseRequestSession.find(purchaseOrderAddEditDTO.getPrNumber());
		PurchaseOrder purchaseOrder = new PurchaseOrder();	
		purchaseOrder.setVendorName(purchaseOrderAddEditDTO.getVendorName());
		purchaseOrder.setDepartment(purchaseRequest.getDepartment());
		// purchaseOrder.setStatus(purchaseRequest.getStatus());
		// KAI - 20210203 - #20867
		//purchaseOrder.setStreetAddress(purchaseRequest.getAlamat());
		purchaseOrder.setTotalCost(purchaseRequest.getTotalHarga());
		// KAI - 20210203 - #20867
		//purchaseOrder.setAddressBook(purchaseRequest.getAddressBook());
		purchaseOrder.setPurchaseOrderDate(purchaseOrderAddEditDTO.getPoDate());
		// KAI - 2021-02-08 - #20867
//		purchaseOrder.setDeliveryTime(purchaseOrderAddEditDTO.getDeliveryTime());
		purchaseOrder.setPurchaseRequest(purchaseRequestSession.find(purchaseOrderAddEditDTO.getPrNumber()));
		purchaseOrder.setRequestorUserId(token.getUser().getId());
		purchaseOrderSession.insertPurchaseOrder(purchaseOrder, token);		
		purchaseRequest.setStatus(Constant.PR_STATUS_PO_COMPLETE);
		purchaseRequestSession.update(purchaseRequest, token);
		List<PurchaseRequestItem> purchaseRequestItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaseRequest.getId());
		List<PurchaseRequestItem> prItemListFromFrontEnd = new ArrayList<>();
		for(AddressBookDTOForCreatePO addressBookDTOForCreatePO : purchaseOrderAddEditDTO.getAddressBookDTOForCreatePOList()) {
			for(PurchaseRequestItem prItem : addressBookDTOForCreatePO.getPurchaseRequestItemList()) {
				prItem.setEstimatedDeliveryTime(addressBookDTOForCreatePO.getDeliveryTime());
				prItemListFromFrontEnd.add(prItem);
			}
		}
		Integer index = 0;
		for(PurchaseRequestItem purchaseRequestItem :purchaseRequestItemList) {
			PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
			purchaseOrderItem.setCreated(new Date());
			purchaseOrderItem.setIsDelete(0);
			purchaseOrderItem.setItem(purchaseRequestItem.getItem());
			purchaseOrderItem.setItemName(purchaseRequestItem.getItemname());
			purchaseOrderItem.setPurchaseOrder(purchaseOrder);
			purchaseOrderItem.setVendor(purchaseRequestItem.getVendor());
			purchaseOrderItem.setVendorName(purchaseRequestItem.getVendor().getNama());
			purchaseOrderItem.setQuantityPurchaseRequest(purchaseRequestItem.getQuantity());
			purchaseOrderItem.setTotalUnitPrices(purchaseRequestItem.getTotal());
			purchaseOrderItem.setPurchaseRequestItem(purchaseRequestItem);
			purchaseOrderItem.setUnitPrice(purchaseRequestItem.getPrice());
			purchaseOrderItem.setUserId(token.getUser().getId());
			if(purchaseRequestItem.getAddressBook().getId().equals(prItemListFromFrontEnd.get(index).getAddressBook().getId())) {
				purchaseOrderItem.setDeliveryTime(prItemListFromFrontEnd.get(index).getEstimatedDeliveryTime());
			}
			purchaseOrderItem.setTaxCode(purchaseOrderAddEditDTO.getTaxCodeList().get(index));
			
			purchaseOrderItemSession.insertPurchaseOrderItem(purchaseOrderItem, token);
			index ++;
		}
		
		userTransaction.commit();
		id.co.promise.procurement.entity.api.Response response = new id.co.promise.procurement.entity.api.Response();
		//Interfacing External ke SAP
		if (Constant.IS_INTERFACING_SAP) {
//			response = sapRfqFunction.submitRfq(purchaseOrder, purchaseOrderAddEditDTO.getGrandTotal(), token);
			response = sapPoFunction.submitPo(purchaseOrder, token);
			map.put("response", response.getStatusText());
			
		} else {
			purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_PO_COMPLETE);
			// KAI - 20201124
			Random rand = new Random(); 
			Integer min = 1000;
			Integer max = 9999;
			Integer random = rand.nextInt((max - min) + 1) + min;
			purchaseOrder.setPoNumber("PO-"+random.toString());//sementara
			purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
			map.put("response", response);
		}
		map.put("PurchaseOrderAddEditDTO", purchaseOrderAddEditDTO);

		return map;
	}


	@Path("/createPO")
	@POST
	public Response createPO(String jsonString, @HeaderParam("Authorization") String token) {

		CreatePOParam cpop = new CreatePOParam();
		PurchaseOrderCreatePOParamDTO pto;
		try {
			cpop = gson.fromJson(jsonString, CreatePOParam.class);

			if (cpop.getBillTo().getAddressId() == null) {
				if (cpop.getBillTo().getSaveAddress()) {
					AddressBook billToAddress = new AddressBook();
					billToAddress.setOrganisasi(
							organisasiSession.getAfcoOrganisasiByOrganisasiId(cpop.getBillTo().getCompanyId()));
					billToAddress.setFullName(cpop.getBillTo().getFullName());
					billToAddress.setStreetAddress(cpop.getBillTo().getAddress());
					billToAddress.setAddressLabel(cpop.getBillTo().getFullName());
					billToAddress.setTelephone(cpop.getBillTo().getTelephone());
					billToAddress = addressBookSession.insertAddressBook(billToAddress,
							tokenSession.findByToken(token));
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
			return Response.status(201)
					.entity(purchaseOrderSession.createPurchaseOrder(pto, tokenSession.findByToken(token))).build();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/updatePO")
	@POST
	public Response updatePO(String jsonString, @HeaderParam("Authorization") String token) {

		CreatePOParam cpop = new CreatePOParam();
		PurchaseOrderCreatePOParamDTO pto;
		try {
			cpop = gson.fromJson(jsonString, CreatePOParam.class);

			if (cpop.getBillTo().getAddressId() == null) {
				if (cpop.getBillTo().getSaveAddress()) {
					AddressBook billToAddress = new AddressBook();
					billToAddress.setOrganisasi(
							organisasiSession.getAfcoOrganisasiByOrganisasiId(cpop.getBillTo().getCompanyId()));
					billToAddress.setFullName(cpop.getBillTo().getFullName());
					billToAddress.setStreetAddress(cpop.getBillTo().getAddress());
					billToAddress.setAddressLabel(cpop.getBillTo().getFullName());
					billToAddress.setTelephone(cpop.getBillTo().getTelephone());
					billToAddress = addressBookSession.insertAddressBook(billToAddress,
							tokenSession.findByToken(token));
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
			return Response.status(201)
					.entity(purchaseOrderSession.createPurchaseOrder(pto, tokenSession.findByToken(token))).build();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/updateStatusApproval")
	@POST
	public PurchaseOrder updateStatusApproval(@FormParam("id") Integer id, @FormParam("note") String note,
			@FormParam("status") Integer status, @FormParam("approvalProcessId") Integer approvalProcessId,
			@HeaderParam("Authorization") String token) {

		/** get token **/
		Token tkn = tokenSession.findByToken(token);

		/** get purchase order **/
		PurchaseOrder purchaseOrder = purchaseOrderSession.find(id);

		/** get approval process **/
		ApprovalProcess approvalProcess = approvalProcessSession.find(approvalProcessId);
		if (approvalProcess != null) {
			approvalProcess.setStatus(status);
			approvalProcess.setKeterangan(note);
			approvalProcessSession.update(approvalProcess, tkn);

			// Jika di reject, maka semua status akan menjadi reject(2)
			if (approvalProcess.getStatus() == 2) {
				List<ApprovalProcess> apStatus = approvalProcessSession
						.findApprovalProcessByStatus(approvalProcess.getApprovalProcessType().getId(), 1);
				for (ApprovalProcess approvalProcess2 : apStatus) {
					approvalProcess2.setStatus(2);
					approvalProcessSession.update(approvalProcess2, tkn);
				}
			}

			// jika status 3(approve), cek lagi level selanjutnya. dan jenis serial
			if (approvalProcess.getStatus() == 3 && approvalProcess.getApprovalProcessType().getJenis() == 0) {
				// update next approval
				Integer sequence = approvalProcess.getSequence();
				ApprovalProcess apNext = approvalProcessSession
						.findByApprovalProcessTypeAndSequence(approvalProcess.getApprovalProcessType(), ++sequence);
				if (apNext != null) {
					apNext.setUserId(tkn.getUser().getId());
					apNext.setUpdated(new Date());
					apNext.setStatus(1); // Approval level berikutnya diset active
					approvalProcessSession.update(apNext, tkn);
				}
			}

			// jika status 4(hold), cek lagi level selanjutnya diset jadi 0 untuk lebih
			// pasti. dan jenis
			// serial
			if (approvalProcess.getStatus() == 4 && approvalProcess.getApprovalProcessType().getJenis() == 0) {
				// update next approval
				Integer sequence = approvalProcess.getSequence();
				ApprovalProcess apNext = approvalProcessSession
						.findByApprovalProcessTypeAndSequence(approvalProcess.getApprovalProcessType(), ++sequence);
				if (apNext != null) {
					apNext.setUserId(tkn.getUser().getId());
					apNext.setUpdated(new Date());
					apNext.setStatus(0); // Approval level berikutnya diset
											// active
					approvalProcessSession.update(apNext, tkn);
				}
			}

			/** update proses approval **/
			String statusPO = "";

			if (status == 3) {
				statusPO = "Approval Process";
				purchaseOrder.setStatusProses(3);
			} else if (status == 4) {
				statusPO = "Hold";
				purchaseOrder.setStatusProses(4);
			} else if (status == 2) {
				statusPO = "Reject";
				purchaseOrder.setStatusProses(5);
			}

			if (note == null) {
				note = "";
			}

			/** get purchase order note **/
			if (approvalProcess.getUser() != null) {
				purchaseOrder.setNextapproval(approvalProcess.getUser().getNamaPengguna() + ", " + note);
			} else if (approvalProcess.getGroup() != null) {
				purchaseOrder.setNextapproval(approvalProcess.getGroup().getNama() + ", " + note);
			} else if (approvalProcess.getUser() != null && approvalProcess.getGroup() != null) {
				purchaseOrder.setNextapproval(approvalProcess.getUser().getNamaPengguna() + ", "
						+ approvalProcess.getUser().getNamaPengguna() + ", " + note);
			}

			/** get approval list **/
			ApprovalProcessType approvalProcessType = approvalProcessTypeSession
					.find(approvalProcess.getApprovalProcessType().getId());
			List<ApprovalProcess> approvalProcessList = approvalProcessSession
					.findByApprovalProcessType(approvalProcessType.getId());

			/** cek status approval **/
			int appSize = 0;
			for (ApprovalProcess approvalProcessCek : approvalProcessList) {
				if (approvalProcessCek.getStatus() != null) {
					if (approvalProcessCek.getStatus().intValue() == 3) {
						appSize++;
					}
				}
			}

			/** jika sudah approve semua **/
			if (appSize == approvalProcessList.size()) {
				statusPO = "On Process";
				purchaseOrder.setStatusProses(6);
				purchaseOrder.setApprovedDate(new Date());
				emailNotificationSession.getMailGeneratorApprovalPO(approvalProcessType.getValueId());
			}

			purchaseOrder.setStatus(statusPO);
			purchaseOrder.setUpdated(new Date());

			/** update purchase order and purchase order item **/
			List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession
					.getPurchaseOrderItemByPoId(purchaseOrder.getId());
			for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
				purchaseOrderItem.setStatus(statusPO);
				purchaseOrderItem.setPurchaseOrder(purchaseOrder);
				purchaseOrderItem.setStatusProses(purchaseOrder.getStatusProses());
				purchaseOrderItemSession.updatePurchaseOrderItem(purchaseOrderItem, tkn);
			}
		}
		return purchaseOrder;

	}

	@Path("/getPurchaseOrderVendorById/{id}")
	@GET
	public Vendor getPurchaseOrderVendorById(@PathParam("id") int id) {
		return purchaseOrderSession.getPurchaseOrderVendorById(id);
	}

	@Path("/getPurchaseOrderListByVendorForPerformance/{id}")
	@GET
	public List<PurchaseOrder> getPurchaseOrderListByVendorForPerformance(@PathParam("id") Integer id) {
		return purchaseOrderSession.getPurchaseOrderListByVendorForPerformance(id);
	}

	@Path("/getPurchaseOrderDetailById/{id}")
	@GET
	public PurchaseOrderDtl getPurchaseOrderDetailById(@PathParam("id") Integer id) {
		PurchaseOrderDtl purchaseOrderDtl = new PurchaseOrderDtl();

		PurchaseOrder purchaseOrder = purchaseOrderSession.getPurchaseOrder(id);
		purchaseOrderDtl.setPurchaseOrder(purchaseOrder);

		List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
		if (purchaseOrder.getKontrakFk() == null) {
			purchaseRequestItemList = purchaseRequestItemSession
					.getPurchaseRequestItemByPurchaseRequestId(purchaseOrder.getPurchaseRequest().getId());
			purchaseOrderDtl.setPurchaseRequestItemList(purchaseRequestItemList);
		}

		List<ShippingToDtl> shippingToDtlList = new ArrayList<ShippingToDtl>();

		List<ShippingTo> shippingToList = shippingToSession.findShippingByPO(id);
		for (ShippingTo shippingTo : shippingToList) {
			ShippingToDtl shippingToDtl = new ShippingToDtl();
			shippingToDtl.setShippingTo(shippingTo);

			List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession
					.getPurchaseOrderItemListByShipping(shippingTo.getId());
			shippingToDtl.setPurchaseOrderItemList(purchaseOrderItemList);

			if (purchaseOrder.getKontrakFk() != null) {
				for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
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

					/** set quantity PR berdasarkan data kontrak **/
					purchaseRequestItemNew.setQuantity(purchaseOrderItem.getQuantityPurchaseRequest());

					if (purchaseRequestItemList.size() > 0) {
						if (!purchaseRequestItemList.contains(purchaseRequestItemNew)) {
							purchaseRequestItemList.add(purchaseRequestItemNew);
						}
					} else {
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

	@Path("/updatePurchaseOrderDtl")
	@POST
	public PurchaseOrderDtl updatePurchaseOrderDtl(PurchaseOrderDtl purchaseOrderDtl,
			@HeaderParam("Authorization") String authorization) {
		Token token = tokenSession.findByToken(authorization);

		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId());

		PurchaseOrder purchaseOrder = purchaseOrderDtl.getPurchaseOrder();
		purchaseOrder.setStatus("Approval Process");
		purchaseOrder.setStatusProses(1);
		purchaseOrder.setUserId(token.getUser().getId());

		AddressBook addressBookPurchaseOrder = new AddressBook();
		addressBookPurchaseOrder = purchaseOrder.getAddressBook();

		if (purchaseOrder.getAddressBook().getId().equals(0)) {
			addressBookPurchaseOrder.setStreetAddress(purchaseOrder.getStreetAddress());
			addressBookPurchaseOrder.setTelephone(purchaseOrder.getTelephone1());
			addressBookPurchaseOrder.setFullName(purchaseOrder.getFullName());

			addressBookPurchaseOrder.setId(null);
			addressBookPurchaseOrder.setStatus(true);
			addressBookPurchaseOrder.setOrganisasi(afcoOrganisasi);
			addressBookPurchaseOrder = addressBookSession.insertAddressBook(addressBookPurchaseOrder, token);
			purchaseOrder.setAddressBook(addressBookPurchaseOrder);
		}
		purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);

		List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession
				.getPurchaseOrderItemByPoId(purchaseOrder.getId());
		for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
			ShippingTo shippingTo = purchaseOrderItem.getShippingTo();
			shippingTo.setIsDelete(1);
			shippingTo.setPurchaseOrder(purchaseOrder);
			purchaseOrderItem.setPurchaseOrder(purchaseOrder);
			purchaseOrderItem.setShippingTo(shippingTo);
			purchaseOrderItem.setIsDelete(1);
			purchaseOrderItemSession.updatePurchaseOrderItem(purchaseOrderItem, token);
		}

		List<PurchaseOrderTerm> purchaseOrderTermList = purchaseOrderTermSession
				.getPurchaseOrderByPO(purchaseOrder.getId());
		for (PurchaseOrderTerm purchaseOrderTerm : purchaseOrderTermList) {
			purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
			purchaseOrderTerm.setIsDelete(1);
			purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
			purchaseOrderTermSession.updatePurchaseOrderTerm(purchaseOrderTerm, token);
		}

		List<ShippingToDtl> shippingToDtlList = purchaseOrderDtl.getShippingToDtlList();
		for (ShippingToDtl shippingToDtl : shippingToDtlList) {

			ShippingTo shippingToNew = new ShippingTo();
			// shippingToNew = shippingToDtl.getShippingTo();

			AddressBook addressBookShipping = new AddressBook();
			if (shippingToDtl.getShippingTo().getAddressBook().getId().equals(0)) {
				addressBookShipping.setStreetAddress(shippingToNew.getStreetAddress());
				addressBookShipping.setTelephone(shippingToNew.getTelephone1());
				addressBookShipping.setFullName(shippingToNew.getFullName());

				addressBookShipping.setStatus(true);
				addressBookShipping.setOrganisasi(afcoOrganisasi);
				addressBookSession.insertAddressBook(addressBookShipping, token);

				shippingToNew.setAddressBook(addressBookShipping);
			} else {
				addressBookShipping = shippingToDtl.getShippingTo().getAddressBook();
				shippingToNew.setAddressBook(addressBookShipping);
			}

			// shippingToNew.setId(null);
			shippingToNew.setAddressLabel(shippingToDtl.getShippingTo().getAddressLabel());
			shippingToNew.setDeliveryTime(shippingToDtl.getShippingTo().getDeliveryTime());
			shippingToNew.setFullName(shippingToDtl.getShippingTo().getFullName());
			shippingToNew.setStreetAddress(shippingToDtl.getShippingTo().getStreetAddress());
			shippingToNew.setTelephone1(shippingToDtl.getShippingTo().getTelephone1());
			shippingToNew.setPurchaseOrder(purchaseOrder);
			shippingToNew.setOrganisasi(afcoOrganisasi);
			shippingToSession.inserShippingTo(shippingToNew, token);

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
					purchaseOrderItemNew.setPurchaseRequestItem(purchaseRequestItemSession
							.getPurchaseRequestItem(purchaseOrderItem.getPurchaseRequestItem().getId()));
					purchaseOrderItemSession.insertPurchaseOrderItem(purchaseOrderItemNew, token);
				}
			}
		}

		PurchaseOrderTerm purchaseOrderTerm = new PurchaseOrderTerm();
		purchaseOrderTerm = purchaseOrderDtl.getPurchaseOrderTerm();
		purchaseOrderTerm.setId(null);
		purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
		purchaseOrderTermSession.inserPurchaseOrderTerm(purchaseOrderTerm, token);

		List<ApprovalProcessType> approvalProcessTypeList = approvalProcessTypeSession
				.findByPurchaseOrder(purchaseOrder.getId());
		if (approvalProcessTypeList != null) {
			if (approvalProcessTypeList.size() > 0) {

				for (ApprovalProcessType approvalProcessType : approvalProcessTypeList) {
					approvalProcessType.setIsDelete(1);

					List<ApprovalProcess> approvalProcesseList = approvalProcessSession
							.findByApprovalProcessType(approvalProcessType.getId());
					if (approvalProcesseList != null) {
						if (approvalProcesseList.size() > 0) {
							for (ApprovalProcess approvalProcess : approvalProcesseList) {
								approvalProcess.setApprovalProcessType(approvalProcessType);
								approvalProcess.setIsDelete(1);
								approvalProcessSession.update(approvalProcess, token);
							}
						} else {
							approvalProcessTypeSession.delete(approvalProcessType, token);
						}
					} else {
						approvalProcessTypeSession.delete(approvalProcessType, token);
					}
				}
			}
		}

		ApprovalProcessType apt = new ApprovalProcessType();
		apt.setValueId(purchaseOrder.getId());
		Tahapan tahapan = tahapanSession.getByName(Constant.TAHAPAN_CREATE_PO);
		apt.setTahapan(tahapan);

		// cek approval baru / sudah ada dimaster
		Approval approvalPo = null;
		if (purchaseOrderDtl.getApprovalId() == null) {
			apt.setApproval(approvalPo);
			apt.setJenis(1); // 1 = serial
		} else {
			approvalPo = approvalSession.find(purchaseOrderDtl.getApprovalId());
			apt.setJenis(approvalPo.getJenis());
			apt.setApproval(approvalPo);
		}
		apt = approvalProcessTypeSession.create(apt, token);

		if (purchaseOrderDtl.getApprovalId() != null) {
			List<ApprovalLevel> aplList = approvalLevelSession
					.findByApproval(approvalSession.find(purchaseOrderDtl.getApprovalId()));
			for (ApprovalLevel apl : aplList) {
				ApprovalProcess ap = new ApprovalProcess();
				ap.setApprovalProcessType(apt);
				ap.setApprovalLevel(apl);

				// cek paralel / serial
				if (apt.getJenis() == 0) {
					// serial
					if (apl.getSequence() == 1) {
						ap.setStatus(1); // set status = aktif
					} else {
						ap.setStatus(0); // set status = non aktif (0)
					}
				} else if (apt.getJenis() == 1) {
					// paralel
					ap.setStatus(1);
				}

				// ap.setKeterangan();
				ap.setGroup(apl.getGroup());
				ap.setUser(apl.getUser());
				// ap.setRole(apl.getRole());
				ap.setThreshold(apl.getThreshold());
				ap.setSequence(apl.getSequence());
				approvalProcessSession.create(ap, token);
			}
		}

		if (purchaseOrderDtl.getNewApproval() != null) {
			ApprovalProcess ap = new ApprovalProcess();
			ap.setApprovalProcessType(apt);
			ap.setStatus(1);
			ap.setGroup(purchaseOrderDtl.getNewApproval().getOrganisasi());
			ap.setUser(purchaseOrderDtl.getNewApproval().getUser());
			ap.setSequence(1);
			approvalProcessSession.create(ap, token);
		}

		return purchaseOrderDtl;
	}

	/*
	 * @Path("/createPurchaseRequestServicesFromContract")
	 * 
	 * @POST public Response createPurchaseRequestServicesFromContract(String
	 * jsonRequest, @HeaderParam("Authorization") String authorization) {
	 * 
	 * Token token = tokenSession.findByToken(authorization);
	 * 
	 * try { Gson gson = new
	 * GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
	 * 
	 * FinalPurchaseOrderInterface finalPurchaseOrderInterface =
	 * gson.fromJson(jsonRequest, FinalPurchaseOrderInterface.class);
	 * List<PurchaseOrderDtlInterface> purchaseOrderDtlInterfaceList =
	 * finalPurchaseOrderInterface.getPurchaseOrderDtlInterfaceList();
	 * 
	 * for(PurchaseOrderDtlInterface purchaseOrderDtlInterface :
	 * purchaseOrderDtlInterfaceList){ PurchaseOrderInterface purchaseOrderInterface
	 * = purchaseOrderDtlInterface.getPurchaseOrderInterface();
	 * 
	 * List<PurchaseOrder> purchaseOrderList =
	 * purchaseOrderSession.getPurchaseOrderListByTermin(purchaseOrderInterface.
	 * getTerminFk()); if(purchaseOrderList.size() == 0){
	 * 
	 *//** set purchase order **/

	/*
	 * PurchaseOrder purchaseOrder = new PurchaseOrder();
	 * purchaseOrder.setKontrakFk(purchaseOrderInterface.getKontrakFk());
	 * purchaseOrder.setTerminFk(purchaseOrderInterface.getTerminFk());
	 * 
	 * if(purchaseOrderInterface.getPengadaanId() != null){ Pengadaan pengadaan =
	 * pengadaanSession.getPengadaan(purchaseOrderInterface.getPengadaanId());
	 * if(pengadaan != null){ purchaseOrder.setPengadaan(pengadaan); } }
	 * 
	 * PurchaseRequest purchaseRequest =
	 * purchaseRequestSession.get(purchaseOrderInterface.getPurchaseRequestId());
	 * if(purchaseRequest!= null){
	 * purchaseOrder.setPurchaseRequest(purchaseRequest);
	 * purchaseOrder.setAddressLabel(purchaseRequest.getOrganisasi().getNama()); }
	 * 
	 * purchaseOrder.setStatus("Request From Contract");
	 * purchaseOrder.setStatusProses(1);
	 * 
	 * TermAndCondition termAndCondition =
	 * termAndConditionSession.getTermAndCondition(purchaseOrderInterface.
	 * getTerminFk()); if(termAndCondition != null){
	 * purchaseOrder.setTermandcondition(termAndCondition); }
	 * 
	 * purchaseOrder.setPoNumber("");
	 * purchaseOrder.setSubTotal(purchaseOrderInterface.getSubTotal());
	 * purchaseOrder.setTotalCost(purchaseOrderInterface.getTotalCost());
	 * 
	 * Afco afco = new Afco(); if(purchaseOrderInterface.getAfcoPk() != null){ afco
	 * = afcoSesssion.getAfco(purchaseOrderInterface.getAfcoPk()); if(afco != null){
	 * purchaseOrder.setDepartment(afco.getCompanyName()); } }
	 * 
	 * purchaseOrder = purchaseOrderSession.insertPurchaseOrder(purchaseOrder,
	 * token);
	 * 
	 * List<ShippingToDtlInterface> shippingToDtlInterfaceList =
	 * purchaseOrderDtlInterface.getShippingToDtlInterfaceList();
	 * 
	 * for(ShippingToDtlInterface shippingToDtlInterface :
	 * shippingToDtlInterfaceList){ ShippingToInterface shippingToInterface =
	 * shippingToDtlInterface.getShippingToInterface();
	 * 
	 * ShippingTo shippingTo = new ShippingTo();
	 * shippingTo.setPurchaseOrder(purchaseOrder);
	 * shippingTo.setDeliveryTime(shippingToInterface.getDeliveryTime());
	 * 
	 * if(afco != null){ shippingTo.setAfco(afco); }
	 * 
	 * shippingTo = shippingToSession.inserShippingTo(shippingTo, token);
	 * 
	 * List<PurchaseOrderItemInterface> purchaseOrderItemInterfaceList =
	 * shippingToDtlInterface.getPurchaseOrderItemInterfaceList();
	 * for(PurchaseOrderItemInterface purchaseOrderItemInterface :
	 * purchaseOrderItemInterfaceList){
	 * 
	 * PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
	 * purchaseOrderItem.setQuantityPurchaseRequest(purchaseOrderItemInterface.
	 * getTermQuantity());
	 * 
	 * if(purchaseOrderItemInterface.getPurchaseRequestItemId() != null){
	 * PurchaseRequestItem purchaseRequestItem =
	 * purchaseRequestItemSession.getPurchaseRequestItem(purchaseOrderItemInterface.
	 * getPurchaseRequestItemId()); if(purchaseRequestItem != null){
	 * purchaseOrderItem.setPurchaseRequestItem(purchaseRequestItem);
	 * purchaseOrderItem.setItem(purchaseRequestItem.getItem());
	 * purchaseOrderItem.setItemName(purchaseRequestItem.getItemname()); } }
	 * 
	 * purchaseOrderItem.setUnitPrice(purchaseOrderItemInterface.getUnitPrice());
	 * purchaseOrderItem.setTotalUnitPrices(purchaseOrderItemInterface.
	 * getTotalUnitPrices());
	 * purchaseOrderItem.setQuantitySend(purchaseOrderItemInterface.getTermQuantity(
	 * ));
	 * 
	 * if(purchaseOrderInterface.getVendorPk()!= null){ Vendor vendor =
	 * vendorSession.getVendor(purchaseOrderInterface.getVendorPk()); if(vendor !=
	 * null){ purchaseOrderItem.setVendor(vendor);
	 * purchaseOrderItem.setVendorName(vendor.getNama()); } }
	 * 
	 * purchaseOrderItem.setPurchaseOrder(purchaseOrder);
	 * purchaseOrderItem.setShippingTo(shippingTo);
	 * purchaseOrderItem.setStatus("Request From Contract");
	 * purchaseOrderItem.setStatusProses(1);
	 * 
	 * purchaseOrderItem =
	 * purchaseOrderItemSession.insertPurchaseOrderItem(purchaseOrderItem, token); }
	 * }
	 * 
	 * PurchaseOrderTerm purchaseOrderTerm = new PurchaseOrderTerm();
	 * purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
	 * purchaseOrderTerm.setPoTermType(2);
	 *//** defaultnya value **//*
								 * purchaseOrderTerm.setPurchaseOrder(purchaseOrder); if(termAndCondition !=
								 * null){ purchaseOrderTerm.setTermandcondition(termAndCondition); }
								 * purchaseOrderTermSession.inserPurchaseOrderTerm(purchaseOrderTerm, token); }
								 * 
								 * }
								 * 
								 * finalPurchaseOrderInterface.setRequestId(0);
								 * 
								 * String json = gson.toJson(finalPurchaseOrderInterface);
								 * 
								 * return Response.ok().entity(json).build(); } catch (Exception e) {
								 * e.printStackTrace(); return
								 * Response.ok().status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).
								 * entity(e).build(); } }
								 */

	@Path("/getApprovalProcessByPO/{poId}")
	@GET
	public List<ApprovalProcess> getApprovalProcessByPO(@PathParam("poId") int poId) {
		return purchaseOrderSession.getApprovalProcessByPO(poId);
	}

	@Path("/add")
	@POST
	public PurchaseOrderAddEditDTO add(@HeaderParam("Authorization") String token) {
		User user = tokenSession.findByToken(token).getUser();
		Organisasi organisasiAfco = organisasiSession.getAfcoOrganisasiByUserId(user.getId());
		PurchaseOrderAddEditDTO purchaseOrderAddEditDTO = new PurchaseOrderAddEditDTO();

		List<TermAndCondition> termAndConditionList = termAndConditionSession.getListById(1);
		purchaseOrderAddEditDTO.setTermAndConditionList(termAndConditionList);
		List<AddressBookDTO> addressBookDTOList = addressBookSession
				.getAddressBookListByOrganisasi(organisasiAfco.getId());
		Tahapan tahapan = tahapanSession.getByName(Constant.TAHAPAN_CREATE_PO);
		List<Approval> approvalList = approvalSession.getListApprovalByTahapanAndOrganisasi(tahapan, organisasiAfco);

		purchaseOrderAddEditDTO.setTermAndConditionList(termAndConditionList);
		purchaseOrderAddEditDTO.setAddressBookDTOList(addressBookDTOList);
		purchaseOrderAddEditDTO.setApprovalList(approvalList);

	//	 purchaseRequestSession.getVerifiedPurchaseRequestByNumberAndToken(keyword,
			//		tokenSession.findByToken(token));
		
		return purchaseOrderAddEditDTO;
	}

	@Path("/edit")
	@POST
	public PurchaseOrderAddEditDTO edit(@HeaderParam("Authorization") String token, int id) {

		User user = tokenSession.findByToken(token).getUser();
		Organisasi organisasiAfco = organisasiSession.getAfcoOrganisasiByUserId(user.getId());
		PurchaseOrderAddEditDTO purchaseOrderAddEditDTO = new PurchaseOrderAddEditDTO();

		List<TermAndCondition> termAndConditionList = termAndConditionSession.getListById(1);
		purchaseOrderAddEditDTO.setTermAndConditionList(termAndConditionList);
		List<AddressBookDTO> addressBookDTOList = addressBookSession
				.getAddressBookListByOrganisasi(organisasiAfco.getId());

		PurchaseOrder purchaseOrder = purchaseOrderSession.getPurchaseOrder(id);

		List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
		if (purchaseOrder.getKontrakFk() == null) {
			purchaseRequestItemList = purchaseRequestItemSession
					.getPurchaseRequestItemByPurchaseRequestId(purchaseOrder.getPurchaseRequest().getId());
			purchaseOrderAddEditDTO.setPurchaseRequestItemList(purchaseRequestItemList);
		}

		List<ShippingToDtl> shippingToDtlList = new ArrayList<ShippingToDtl>();

		List<ShippingTo> shippingToList = shippingToSession.findShippingByPO(id);
		for (ShippingTo shippingTo : shippingToList) {
			ShippingToDtl shippingToDtl = new ShippingToDtl();
			shippingToDtl.setShippingTo(shippingTo);

			List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession
					.getPurchaseOrderItemListByShipping(shippingTo.getId());
			shippingToDtl.setPurchaseOrderItemList(purchaseOrderItemList);

			if (purchaseOrder.getKontrakFk() != null) {
				for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
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

					/** set quantity PR berdasarkan data kontrak **/
					purchaseRequestItemNew.setQuantity(purchaseOrderItem.getQuantityPurchaseRequest());

					if (purchaseRequestItemList.size() > 0) {
						if (!purchaseRequestItemList.contains(purchaseRequestItemNew)) {
							purchaseRequestItemList.add(purchaseRequestItemNew);
						}
					} else {
						purchaseRequestItemList.add(purchaseRequestItemNew);
					}
					purchaseOrderItem.setPurchaseRequestItem(purchaseRequestItemNew);
				}
				purchaseOrderAddEditDTO.setPurchaseRequestItemList(purchaseRequestItemList);
			}

			shippingToDtl.setPurchaseRequestItemList(purchaseRequestItemList);

			shippingToDtlList.add(shippingToDtl);

			if (purchaseOrderItemList.size() > 0) {
				if (purchaseOrderItemList.get(0).getVendor() != null) {
					Vendor vendor = purchaseOrderItemList.get(0).getVendor();
					purchaseOrderAddEditDTO.setVendor(vendor);
				}
			}
		}

		purchaseOrderAddEditDTO.setShippingToDtlList(shippingToDtlList);

		List<PurchaseOrderTerm> purchaseOrderTermList = purchaseOrderTermSession.getPurchaseOrderByPO(id);
		if (purchaseOrderTermList != null) {
			if (purchaseOrderTermList.size() > 0) {
				purchaseOrderAddEditDTO.setPurchaseOrderTerm(purchaseOrderTermList.get(0));
			}
		}

		Tahapan tahapan = tahapanSession.getByName(Constant.TAHAPAN_CREATE_PO);
		List<Approval> approvalList = approvalSession.getListApprovalByTahapanAndOrganisasi(tahapan, organisasiAfco);
		if (approvalList != null) {
			ApprovalProcessType approvalProcessType = approvalProcessTypeSession
					.findByTahapanAndValueId(Constant.TAHAPAN_CREATE_PO, id);
			if (approvalProcessType != null) {
				if (approvalProcessType.getApproval() == null) {
					purchaseOrder.setApprovalId(0);
					List<ApprovalProcess> approvalProcessList = approvalProcessSession
							.findByApprovalProcessType(approvalProcessType.getId());
					List<RoleUser> roleUserList = roleUserSession
							.getRoleUserByUserId(approvalProcessList.get(0).getUser().getId());
					purchaseOrderAddEditDTO.setRoleUserNewSelected(roleUserList.get(0));

				} else {
					purchaseOrder.setApprovalId(approvalProcessType.getApproval().getId());
					Approval approval = approvalSession.find(approvalProcessType.getApproval().getId());
					List<ApprovalLevel> approvalLevelList = approvalLevelSession.findByApproval(approval);
					purchaseOrderAddEditDTO.setApprovalLevelList(approvalLevelList);
				}

			}
		}

		purchaseOrderAddEditDTO.setTermAndConditionList(termAndConditionList);
		purchaseOrderAddEditDTO.setAddressBookDTOList(addressBookDTOList);
		purchaseOrderAddEditDTO.setApprovalList(approvalList);
		purchaseOrderAddEditDTO.setPurchaseOrder(purchaseOrder);

		return purchaseOrderAddEditDTO;
	}

	@Path("/get-list")
	@POST
	public Response getBookingOrderListPagination(@FormParam("search") String search,
			@HeaderParam("Authorization") String token,
			@FormParam("orderKeyword") String orderKeyword, 
			@FormParam("vendorName") String vendorName,
			@FormParam("pageNo") Integer pageNo, 
			@FormParam("pageSize") Integer pageSize, 
			@FormParam("startDate") Date startDate,
			@FormParam("endDate") Date endDate) {
		
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
		try {
			vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null : vendorName;

		} catch (Exception e) {
			// kalo undefined
			vendorName = null;

		}
		Calendar calender = Calendar.getInstance();
		if (endDate != null) {
			calender.setTime(endDate);
			calender.add(Calendar.DATE, 1);
		}
		Date currentDatePlusOne = calender.getTime();
		List<Integer> organisasiListId = new ArrayList<Integer>();
		//KAI - 20201125 - #20500
		if (!roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			if(roleUser.getOrganisasi().getId() > 0 ){			
				organisasiListId.add(roleUser.getOrganisasi().getId());	
				List<Integer> organisasiList = getOrganisasiByParentId(roleUser.getOrganisasi().getId());			
				for(Integer id : organisasiList ){
					organisasiListId.add(id);	
				}	
			}
		}
		List<PurchaseOrder> purchaseOrderList = purchaseOrderSession.getPurchaseOrderListWithPagination(search, orderKeyword, vendorName,
				pageNo, pageSize, organisasiListId, roleUser, startDate, currentDatePlusOne);
		for(PurchaseOrder po : purchaseOrderList) {
			String poNumber ; // kalo poNumbernya null
			poNumber = po.getPoNumber() !=null ? po.getPoNumber() : "-" ;
			po.setPoNumber(poNumber);
			
			//KAI - 20201125 - #20500
//			if(po.getPurchaseRequest().getOrganisasi().getId() == roleUser.getOrganisasi().getId()) {
//				po.setIsAvailable(true);
//			} else {
//				po.setIsAvailable(false);
//			}
			po.setIsAvailable(true);
		}	
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("jmlData", purchaseOrderSession.getTotalList(search, vendorName, pageNo, pageSize, organisasiListId, roleUser, startDate, currentDatePlusOne));
		map.put("dataList", purchaseOrderList);
		map.put("vendorList", vendorSession.getVendorListForPurchaseOrderIndex());

		
		return Response.ok(map).build();
	}
	
	@Path("/get-vendor-list-for-purchase-order-manager")
	@GET
	public List<Vendor> getVendorListForPurchaseOrderManager() {
		return vendorSession.getVendorListForPurchaseOrderIndex();
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/updateRatingPO")
	@POST
	public Map updateRatingPO(PurchaseOrder purchaseOrder, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		purchaseOrder.setRatingDate(new Date());
		purchaseOrderSession.updatePurchaseOrder(purchaseOrder,tokenSession.findByToken(token));
		
		Vendor vendor = purchaseOrderSession.getPOVendorById(purchaseOrder.getId());
		
		Double average =  purchaseOrderSession.getAverageRatingVendorPO(vendor.getId());
		
		vendor.setRating(average);
		vendor.setRatingDate(new Date());
		vendorSession.updateVendor(vendor, tokenSession.findByToken(token));
		
		map.put("PurchaseOrder", purchaseOrder);

		return map;	
	}
	
	private List<Integer> getOrganisasiByParentId(Integer organisasiId){
		List<Organisasi> orgList = organisasiSession.getAllChildListByOrganisasi(organisasiId);
		List<Integer> organisasiListId = new ArrayList<Integer>();	
		if(orgList != null){
			for(Organisasi organisasi : orgList){
				organisasiListId.add(organisasi.getId());
				getOrganisasiByParentId(organisasi.getId());
			}
		}		
		return organisasiListId;
		
	}
	
	@Path("/getListPagination")
	@POST
	public Response getListPagination(
			@FormParam("refresh") String refresh,
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,
			@FormParam("orderKeyword") String orderKeyword, 
			@FormParam("vendorName") String vendorName,
			@FormParam("startDate") String startDate,
			@FormParam("endDate") String endDate,
			@HeaderParam("Authorization") String token) {
		
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
		try {
			vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null : vendorName;

		} catch (Exception e) {
			// kalo undefined
			vendorName = null;

		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateStart = null;
		Date dateEnd = null;
		
		try {
			if(!startDate.equals("")) {
				dateStart = formatter.parse(startDate);				
			}
			if(!endDate.equals("")) {
				dateEnd = formatter.parse(endDate);				
			}
			Calendar calender = Calendar.getInstance();
			if (dateEnd != null) {
				calender.setTime(dateEnd);
				calender.add(Calendar.DATE, 1);
			}
			
			Date currentDatePlusOne = calender.getTime();
			List<Integer> organisasiListId = new ArrayList<Integer>();
			//KAI - 20201125 - #20500
			if (!roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
				if(roleUser.getOrganisasi().getId() > 0 ){			
					organisasiListId.add(roleUser.getOrganisasi().getId());	
					List<Integer> organisasiList = getOrganisasiByParentId(roleUser.getOrganisasi().getId());			
					for(Integer id : organisasiList ){
						organisasiListId.add(id);	
					}	
				}
			}
			List<PurchaseOrder> purchaseOrderList = purchaseOrderSession.getPurchaseOrderListWithPagination(keyword, orderKeyword, vendorName,
					start, length, organisasiListId, roleUser, dateStart, currentDatePlusOne);
			for(PurchaseOrder po : purchaseOrderList) {
				String poNumber ; // kalo poNumbernya null
				poNumber = po.getPoNumber() !=null ? po.getPoNumber() : "-" ;
				po.setPoNumber(poNumber);			
				po.setIsAvailable(true);
			}	
			Long size = purchaseOrderSession.getTotalList(keyword, vendorName, start, length, organisasiListId, roleUser, dateStart, currentDatePlusOne);
			
			Map<String, Object> result = new HashMap<>();
			result.put("draw", draw);
			result.put("data", purchaseOrderList);
			result.put("recordsTotal", size);
			result.put("recordsFiltered", size);
			result.put("vendorList", vendorSession.getVendorListForPurchaseOrderIndex());
			return Response.ok(result).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
}
