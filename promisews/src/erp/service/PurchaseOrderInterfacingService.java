package erp.service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import erp.entity.PurchaseOrderInterfacingExpose;
import erp.interfacing.entity.GetPOByPRERPRequest;
import erp.interfacing.entity.GetPOByPRERPResponse;
import erp.interfacing.entity.PostPurchaseOrderDetailERPRequest;
import erp.interfacing.entity.PostPurchaseOrderERPRequest;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogStock;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.catalog.session.CatalogStockSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;
import id.co.promise.procurement.vendor.VendorSession;
import sap.interfacing.soap.po.SapPoFunction;
import sap.interfacing.soap.rfq.SapRfqFunction;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/catalog/interfacing/purchaseOrderInterfacingService")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseOrderInterfacingService {
	
	final static Logger log = Logger.getLogger(PurchaseOrderInterfacingService.class);
	@EJB
	private PurchaseOrderSession purchaseOrderSession;

	@EJB
	private PurchaseRequestSession purchaseRequestSession;

	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB
	private UserSession userSession;

	@EJB
	private CatalogSession catalogSession;

	@EJB
	private CatalogStockSession catalogStockSession;

	@EJB
	private TokenSession tokenSession;

	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;

	@EJB
	private SyncSession syncSession;

	@EJB
	private SatuanSession satuanSession;

	@EJB
	ProcedureSession procedureSession;
	
	@EJB
	OrganisasiSession organisasiSession;
	
	@EJB
	EmailNotificationSession emailNotificationSession;
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	VendorSession vendorSession;
	
	@EJB
	SapRfqFunction sapRfqFunction;
	
	@EJB
	SapPoFunction sapPoFunction;

	@Resource
	private UserTransaction userTransaction;

	@Path("/insert")
	@POST
	public Response insert(PurchaseOrderInterfacingExpose purchaseOrderInterfacing, @HeaderParam("key") String token,
			@Context UriInfo path) throws SQLException {
		// iniasaliasai
		if (token != null && token.equalsIgnoreCase(Constant.INTERFACING_KEY)) {
			try {
				if (purchaseOrderInterfacing != null) {
					PurchaseOrder purchaseOrder = purchaseOrderSession.getByPONumberAndOrgCode(
							purchaseOrderInterfacing.getPoNumber(), purchaseOrderInterfacing.getOrgCode());
					if (purchaseOrder != null) {
						PurchaseRequest purchaseRequest = purchaseOrder.getPurchaseRequest();
						if (purchaseOrderInterfacing.getPoStatus().equalsIgnoreCase("Approve")) {
							purchaseOrder.setApprovedDate(new Date());
							purchaseOrder.setStatusProses(8);
							purchaseRequest.setStatus(8);
							modifiedStock(purchaseOrder.getId());
						} else if (purchaseOrderInterfacing.getPoStatus().equalsIgnoreCase("Reject")) {
							purchaseOrder.setStatusProses(7);
							purchaseRequest.setStatus(7);
						} else {
							syncSession.create(path.getPath(), purchaseOrderInterfacing,
									Response.error("Status Undefined"), Constant.LOG_METHOD_POST,
									Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
							return Response.error("Status Undefined");
						}
						purchaseRequestSession.update(purchaseRequest, tokenSession.findByToken(token));
						purchaseOrderSession.updatePurchaseOrder(purchaseOrder, tokenSession.findByToken(token));
					} else {
						String cause = "No PO Found with Number : " + purchaseOrderInterfacing.getPoNumber()
								+ "in Organization : " + purchaseOrderInterfacing.getOrgCode();
						syncSession.create(path.getPath(), purchaseOrderInterfacing, Response.error(cause),
								Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
						return Response.error(cause);
					}
				}
				// create log
				syncSession.create(path.getPath(), purchaseOrderInterfacing, Response.ok(), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
				return Response.ok(Constant.RESPONSE_SUCCESS);
			} catch (Exception e) {
				// create log
				syncSession.create(path.getPath(), purchaseOrderInterfacing, Response.error(e),
						Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
				return Response.error(e);
			}

		} else {
			// create log
			syncSession.create(path.getPath(), purchaseOrderInterfacing, Response.error("Key Salah"),
					Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
			return Response.error("Key Salah");
		}

	}

	private void modifiedStock(Integer poId) {

		List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(poId);

		Integer currentStock;
		for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
			Catalog catalog = purchaseOrderItem.getPurchaseRequestItem().getCatalog();
			currentStock = catalog.getCurrentStock() - purchaseOrderItem.getQuantityPurchaseRequest().intValue();
			catalog.setCurrentStock(currentStock);
			catalog = catalogSession.updateCatalog(catalog, null);

			CatalogStock catalogStock = new CatalogStock();
			catalogStock.setNotes("Pembelian PO NO : " + purchaseOrderItem.getPurchaseOrder().getPoNumber());
			catalogStock.setCatalog(catalog);
			catalogStock.setTotalStock(currentStock);
			catalogStock.setQuantity(-purchaseOrderItem.getQuantityPurchaseRequest().intValue());
			catalogStock = catalogStockSession.insertCatalogStock(catalogStock, null);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/postPurchaseOrder")
	@POST
	public Response postPurchaseOrder(PurchaseOrder purchaseOrder, Double grandTotal, Token token) throws SQLException, Exception {
		PostPurchaseOrderERPRequest postPurchaseOrderERPRequest = new PostPurchaseOrderERPRequest();
		ArrayList postPoDetailERPList = new ArrayList();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		User user = userSession.find(purchaseOrder.getPurchaseRequest().getUserId());
		Organisasi organisasi = organisasiSession.getRootParentByOrganisasi(purchaseOrder.getPurchaseRequest().getOrganisasi());
		List<PurchaseOrderItem> poItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(purchaseOrder.getId());
		postPurchaseOrderERPRequest.setOrgCode(organisasi.getCode());
		postPurchaseOrderERPRequest.setPoID(purchaseOrder.getId().toString());
		postPurchaseOrderERPRequest.setRequisitionHeaderID(purchaseOrder.getPurchaseRequest().getPrIdEbs().toString());
		postPurchaseOrderERPRequest.setRequisitionNumber(purchaseOrder.getPurchaseRequest().getPrnumber());
		postPurchaseOrderERPRequest.setDepartment(purchaseOrder.getPurchaseRequest().getOrganisasi().getNama());
		postPurchaseOrderERPRequest.setTotalAmount(grandTotal.toString());
		postPurchaseOrderERPRequest.setShippingFee(purchaseOrder.getPurchaseRequest().getTotalHargaOngkir().toString());
		postPurchaseOrderERPRequest
				.setAsuransiFee(Constant.ASURANSI.toString());
		postPurchaseOrderERPRequest.setPoApproveDate(dateFormat.format(new Date()));
		postPurchaseOrderERPRequest.setAddressBookID(purchaseOrder.getAddressBook().getAddressBookEbs());
		postPurchaseOrderERPRequest
				.setVendorNo(poItemList.get(0).getPurchaseRequestItem().getVendor().getVendorIdEproc());
		postPurchaseOrderERPRequest.setPreparerName(user.getNamaPengguna());
		postPurchaseOrderERPRequest.setPreparerNum(user.getKode());
		if (purchaseOrder.getDeliveryTime() != null) {
			postPurchaseOrderERPRequest.setDeliveryDate(dateFormat.format(purchaseOrder.getDeliveryTime()));
		} else {
			postPurchaseOrderERPRequest.setDeliveryDate("");
		}
		if (purchaseOrder.getCreated() != null) {
			postPurchaseOrderERPRequest.setCreationDate(dateFormat.format(purchaseOrder.getCreated()));
		} else {
			postPurchaseOrderERPRequest.setCreationDate("");
		}
		postPurchaseOrderERPRequest.setLastUpdatedate(dateFormat.format(new Date()));
		for (PurchaseOrderItem poItem : poItemList) {
			Satuan satuan = satuanSession.getSatuanByNama(poItem.getPurchaseRequestItem().getUnit());
			PostPurchaseOrderDetailERPRequest postPurchaseOrderDetailERPRequest = new PostPurchaseOrderDetailERPRequest();
			postPurchaseOrderDetailERPRequest.setRequisitionNumber(purchaseOrder.getPurchaseRequest().getPrnumber());
			postPurchaseOrderDetailERPRequest.setRequisitionLineID(poItem.getPurchaseRequestItem().getId().toString());
			postPurchaseOrderDetailERPRequest.setPoID(purchaseOrder.getId().toString());
			postPurchaseOrderDetailERPRequest.setItemCode(poItem.getPurchaseRequestItem().getItem().getKode());
			postPurchaseOrderDetailERPRequest.setItemID(poItem.getPurchaseRequestItem().getItem().getId().toString());
			postPurchaseOrderDetailERPRequest.setQuantity(poItem.getPurchaseRequestItem().getQuantity().toString());
			postPurchaseOrderDetailERPRequest.setPrice(poItem.getPurchaseRequestItem().getTotal().toString());
			postPurchaseOrderDetailERPRequest.setPriceDiscount(poItem.getPurchaseRequestItem().getPrice().toString());
			postPurchaseOrderDetailERPRequest.setCurrency(poItem.getPurchaseRequestItem().getMataUang().getKode());
			postPurchaseOrderDetailERPRequest.setTotalAmount(poItem.getPurchaseRequestItem().getTotal().toString());
			postPurchaseOrderDetailERPRequest.setUom(satuan.getCode());
			postPurchaseOrderDetailERPRequest
					.setCatalogItemID(poItem.getPurchaseRequestItem().getCatalog().getId().toString());
			postPurchaseOrderDetailERPRequest.setDiscount(poItem.getPurchaseRequestItem().getDiscount().toString());
			postPurchaseOrderDetailERPRequest.setPoLineID(poItem.getId().toString());
			postPoDetailERPList.add(postPurchaseOrderDetailERPRequest);
		}
		postPurchaseOrderERPRequest.setPoDetailList(postPoDetailERPList);

//		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/postPurchaseOrder";
//		String externalId = "";
//		List<PostPurchaseOrderERPResponse> postPurchaseOrderERPResponseList = new ArrayList<PostPurchaseOrderERPResponse>();
//		Gson gson = new Gson();
		try {
//			ResponseObject responseObj = Utils.doPost(postPurchaseOrderERPRequest, urlInterface);
//			String responseString = responseObj.getResponseString();
//			String statusCode = responseObj.getStatusCode();
//			
//			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
//			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
//			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
//			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			
//			if (statusCode.equalsIgnoreCase("200")) {
//				JSONArray body = jsonObj.getJSONArray("body");
//				
//				if(srcRespDesc.contains("OK")) {
//					postPurchaseOrderERPResponseList = gson.fromJson(body.toString(), new TypeToken<List<PostPurchaseOrderERPResponse>>() {}.getType());
//					for (PostPurchaseOrderERPResponse postPurchaseOrderERPResponse : postPurchaseOrderERPResponseList) {
//						if (postPurchaseOrderERPResponse.getPoID() != null) {
//							purchaseOrder.setPoNumber(postPurchaseOrderERPResponse.getPoNumber());
//							purchaseOrder.setPoIdEbs(postPurchaseOrderERPResponse.getPoHeaderID());
							purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_PO_COMPLETE);
							purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
							
							//Email
							List <String> allEmail = new ArrayList<String>();
							User userEmail = userSession.getUser(purchaseOrder.getPurchaseRequest().getRequestorUserId());
							PurchaseOrderItem poItemEmailVendor = poItemList.get(0);
							RoleUser roleUserDVP = roleUserSession.getByUserId(purchaseOrder.getPurchaseRequest().getRequestorUserId());
							List<RoleUser> listRoleUserSVP = roleUserSession.getListRoleUserSvpByDvp(roleUserDVP);
							for(RoleUser roleUserSVP: listRoleUserSVP ) {
								allEmail.add(roleUserSVP.getUser().getEmail());
							}
							allEmail.add(userEmail.getEmail());
							allEmail.add(poItemEmailVendor.getVendor().getEmail());
							
							for(String email : allEmail) {
								emailNotificationSession.getMailPurchaseOrderToEbs(purchaseOrder, email, organisasi.getNama());
							}
//						} else {
//							String cause = "No Data Found";
//							syncSession.create(urlInterface, postPurchaseOrderERPRequest, Response.error(srcRespDesc, responseString),
//									Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_SEND_PO, externalId);
//							return Response.error(srcRespDesc);
//						}
//					}
					// create log
//					syncSession.create(urlInterface, postPurchaseOrderERPRequest, Response.ok(responseString),
//							Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_SEND_PO, externalId);
					return Response.ok("SUCCESS");
//				}else {
//					// create log
//					syncSession.create(urlInterface, postPurchaseOrderERPRequest, Response.error(srcRespDesc, responseString),
//							Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_SEND_PO, externalId);
//					return Response.error(srcRespDesc);
//				}
				
//			} else {
//				syncSession.create(urlInterface, postPurchaseOrderERPRequest,
//						Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
//						Constant.LOG_SERVICE_NAME_SEND_PO, externalId);
//				return Response.error("Error : " + srcRespDesc);
//			}
		} catch (Exception e) {
			// create log
//			syncSession.create(urlInterface, postPurchaseOrderERPRequest, Response.error(e), Constant.LOG_METHOD_POST,
//					Constant.LOG_SERVICE_NAME_SEND_PO, externalId);
			return Response.error(e);
		}
	}

	public void getIncompletePOStatus() throws SQLException {

		List<Integer> statusList = new ArrayList<Integer>();
		List<PurchaseOrder> incompletePurchaseOrderList = new ArrayList<PurchaseOrder>();

		// KAI - 20201123
		//statusList.add(Constant.PR_STATUS_PROCESSING_PO);
		incompletePurchaseOrderList = purchaseOrderSession.getPOListByStatusList(statusList);

		/*
		 * for (PurchaseOrder incompletePurchaseOrder : incompletePurchaseOrderList) {
		 * getPurchaseOrderbyPrIdAndOrgCode(incompletePurchaseOrder); }
		 */

		GetPOByPRERPRequest getPOByPRERPRequest = new GetPOByPRERPRequest();
		for (PurchaseOrder incompletePurchaseOrder : incompletePurchaseOrderList) {
			getPOByPRERPRequest
					.setRequisitionHeaderID(incompletePurchaseOrder.getPurchaseRequest().getPrIdEbs().toString());
			getPOByPRERPRequest.setOrgCode(incompletePurchaseOrder.getPurchaseRequest().getOrganisasi().getCode());
			getPurchaseOrderbyPrIdAndOrgCode(getPOByPRERPRequest);
		}
	}

	@SuppressWarnings("rawtypes")
	@Path("/getPOByPR")
	@POST
	public Response getPurchaseOrderbyPrIdAndOrgCode(GetPOByPRERPRequest getPOByPRERPRequest) throws SQLException {
		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getPOByPR";
		String cause = "";
		String externalId = "";

		List<GetPOByPRERPResponse> getPOByPRERPResponseList = new ArrayList<GetPOByPRERPResponse>();
		try {
			ResponseObject responseObj = Utils.doPost(getPOByPRERPRequest, urlInterface);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			
			externalId = responseObj.getExternalId();
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray jsonBody = jsonObj.getJSONArray("body");
				Gson gson = new Gson();
				getPOByPRERPResponseList = gson.fromJson(jsonBody.toString(), new TypeToken<List<GetPOByPRERPResponse>>() {}.getType());
				for (GetPOByPRERPResponse getPOByPRERPResponses : getPOByPRERPResponseList) {
					PurchaseOrder purchaseOrder = purchaseOrderSession.getPOByPRIdEbs(Integer.parseInt(getPOByPRERPResponses.getRequisitionHeaderID()));
					if (getPOByPRERPResponses.getRequisitionHeaderID() != null) {
						if (purchaseOrder != null) {
							PurchaseRequest purchaseRequest = purchaseOrder.getPurchaseRequest();
							purchaseOrder.setStatus(getPOByPRERPResponses.getPoStatus());
							purchaseOrder.setStatusEbs(getPOByPRERPResponses.getPoStatus());
							if (getPOByPRERPResponses.getPoStatus().equalsIgnoreCase("Approve")) {
								// KAI - 20201123
								//purchaseRequest.setStatus(Constant.PR_STATUS_PO_COMPLETE);
								//purchaseOrder.setStatusProses(Constant.PR_STATUS_PO_COMPLETE);
								cause = "SUCCESS";
							} else if (getPOByPRERPResponses.getPoStatus().equalsIgnoreCase("Reject")) {
								// KAI - 20201123 - [NO_TIKET (opsional)] 
								//purchaseRequest.setStatus(Constant.PR_STATUS_REJECT_PO);
								//purchaseOrder.setStatusProses(Constant.PR_STATUS_REJECT_PO);
								cause = "SUCCESS";
							} else {
								log.info(">> Status: " + getPOByPRERPResponses.getPoStatus() + " Undefined");
								cause = "Status: " + getPOByPRERPResponses.getPoStatus() + " Undefined";
							}
							purchaseOrderSession.updatePurchaseOrder(purchaseOrder, null);
						} else {
							System.out
									.println("PR ID: " + getPOByPRERPResponses.getRequisitionHeaderID() + " Undefined");
							cause = "PR ID: " + getPOByPRERPResponses.getRequisitionHeaderID() + " Undefined";
						}
					} else {
						cause = "PR ID Undefined";
						syncSession.create(urlInterface, getPOByPRERPRequest, Response.error(srcRespDesc, responseString),Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_GET_PO_BY_PR, externalId);
						return Response.error(srcRespDesc);
					}
				}
				// create log
				syncSession.create(urlInterface, getPOByPRERPRequest, Response.ok(responseString),Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PO_BY_PR, externalId);
				return Response.ok(responseString);
			} else {
				syncSession.create(urlInterface, getPOByPRERPResponseList, Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_GET_PO_BY_PR, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
		} catch (Exception e) {
			// create log
			syncSession.create(urlInterface, getPOByPRERPRequest, Response.error(e), Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PO_BY_PR, externalId);
			return Response.error(e);
		}
	}

	//KAI - 20201125 - SYNC PO
	@SuppressWarnings({ "rawtypes", "unused" })
	@Path("/send-data-po/{pk}")
	@GET
	public Response syncPO(@PathParam("pk") Integer pk, @HeaderParam("Authorization") String tokenStr) throws SQLException, Exception {
		PurchaseOrder purchaseOrder2 = purchaseOrderSession.getPurchaseOrder(pk);
		if (purchaseOrder2.getPoNumber() == null) {
			Token tokenObj = tokenSession.findByToken(tokenStr);
			List<PurchaseOrderItem> poItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(purchaseOrder2.getId()); 
			// KAI - 11/12/2020
			Response response = new Response<>();
			if (Constant.IS_INTERFACING_SAP) {
				response = sapPoFunction.submitPo(purchaseOrder2, tokenObj);
				
			}
			
			return response;
					//this.postPurchaseOrder(purchaseOrder2, grandTotal, tokenObj);
		} else {
			return Response.ok();
		}
	}

}
