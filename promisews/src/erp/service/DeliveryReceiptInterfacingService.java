package erp.service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import erp.interfacing.entity.DataReceiptListERPRequest;
import erp.interfacing.entity.GetDeliveryReceiptERPRequest;
import erp.interfacing.entity.GetDeliveryReceiptERPResponse;
import erp.interfacing.entity.PostDeliveryReceiptERPRequest;
import erp.interfacing.entity.PostDeliveryReceiptERPResponse;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedLogSession;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.DeliveryReceivedDetail;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.DeliveryReceivedDetailSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProcedureSession;
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

@Stateless
@Path(value = "/catalog/interfacing/deliveryReceiptInterfacingService")
@TransactionManagement(TransactionManagementType.BEAN)
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryReceiptInterfacingService {

	final static Logger log = Logger.getLogger(DeliveryReceiptInterfacingService.class);
	@EJB
	private SyncSession syncSession;

	@EJB
	private PurchaseOrderSession purchaseOrderSession;

	@EJB
	private DeliveryReceivedSession deliveryReceivedSession;

	@EJB
	private DeliveryReceivedDetailSession deliveryReceivedDetailSession;

	@EJB
	private DeliveryReceivedLogSession deliveryReceivedLogSession;
	
	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;
	
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	
	@EJB
	private OrganisasiSession organisasiSession;
	@EJB
	EmailNotificationSession emailNotificationSession;
	@EJB
	UserSession userSession;
	@EJB
	VendorSession vendorSession;
	@EJB
	RoleUserSession roleUserSession;
	@EJB
	TokenSession tokenSession;
	@EJB private PurchaseRequestItemSession purchaseRequestItemSession; 

	@Resource
	private UserTransaction userTransaction;
	@EJB
	ProcedureSession procedureSession;

	@Path("/getDeliveryReceipt")
	@POST
	@SuppressWarnings("rawtypes")
	public Response getDeliveryReceipt(GetDeliveryReceiptERPRequest getDeliveryReceiptERPRequest)
			throws SQLException {
		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getDeliveryReceipt";
		String externalId = "";

		try {
			ResponseObject responseObj = Utils.doPost(getDeliveryReceiptERPRequest, urlInterface);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			externalId = responseObj.getExternalId();
			List<GetDeliveryReceiptERPResponse> getDeliveryReceiptERPResponses = new ArrayList<GetDeliveryReceiptERPResponse>();
			Integer count = 0;
			
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";

			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray jsonBody = jsonObj.getJSONArray("body");

				Gson gson = new Gson();
				getDeliveryReceiptERPResponses = gson.fromJson(jsonBody.toString(),
						new TypeToken<List<GetDeliveryReceiptERPResponse>>() {
						}.getType());

				for (GetDeliveryReceiptERPResponse getDeliveryReceiptERPResponse : getDeliveryReceiptERPResponses) {
					PurchaseOrder purchaseOrder = purchaseOrderSession
							.getbyPONumberEbs(getDeliveryReceiptERPResponse.getPoNumber());
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

					if (purchaseOrder != null) {
						Date receiptDate = dateFormat.parse(getDeliveryReceiptERPResponse.getReceiptDate());
						DeliveryReceived deliveryReceived = new DeliveryReceived();
						userTransaction.begin();

						deliveryReceived.setPurchaseOrder(purchaseOrder); // getPoId
						deliveryReceived.setDeliveryReceiptNum(getDeliveryReceiptERPResponse.getReceiptNum());

						if (getDeliveryReceiptERPResponse.getReceivingStatus().equalsIgnoreCase("CLOSED")
								|| getDeliveryReceiptERPResponse.getReceivingStatus()
										.equalsIgnoreCase("CLOSED FOR RECEIVING")) {
							deliveryReceived.setIsFinish(1);
						} else {
							deliveryReceived.setIsFinish(0);
						}

						deliveryReceived.setDateReceived(receiptDate);

						DeliveryReceived deliveryReceivedTemp = deliveryReceivedSession
								.getDeliveryReceivedByPoIdSingle(purchaseOrder.getId());

						if (deliveryReceivedTemp != null) {
							deliveryReceived.setId(deliveryReceivedTemp.getId());
							deliveryReceived.setCreated(deliveryReceivedTemp.getCreated());
							deliveryReceivedSession.updateDeliveryReceived(deliveryReceived, null);
						} else {
							deliveryReceivedSession.insertDeliveryReceived(deliveryReceived, null);
						}

						count++;
						userTransaction.commit();
						procedureSession.execute(Constant.SYNC_IN_UP_DR_CAT_TO_CM, deliveryReceived.getId());
					} else {
						log.info(">> PO Number" + purchaseOrder + "Tidak Ditemukan ");
					}
				}
			} else {
				syncSession.create(urlInterface, getDeliveryReceiptERPRequest,
						Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_GET_DATA_RECEIPT, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
			// create log
			syncSession.create(urlInterface, getDeliveryReceiptERPRequest, Response.ok(responseString),
					Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_DATA_RECEIPT, externalId);
			return Response.ok(getDeliveryReceiptERPResponses, count);
		} catch (Exception e) {
			// create log
			syncSession.create(urlInterface, getDeliveryReceiptERPRequest, Response.error(e), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_DATA_RECEIPT, externalId);
			return Response.error(e);
		}

	}

	@Path("/postDeliveryReceipt")
	@POST
	@SuppressWarnings("rawtypes")
	public Response postDeliveryReceiptInterfacing(PostDeliveryReceiptERPRequest postDeliveryReceiptERPRequest)
			throws SQLException {
		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/postDeliveryReceipt";
		String externalId = "";

		try {
			ResponseObject responseObj = Utils.doPost(postDeliveryReceiptERPRequest, urlInterface);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			externalId = responseObj.getExternalId();
			List<PostDeliveryReceiptERPResponse> postDeliveryReceiptERPResponseList = new ArrayList<PostDeliveryReceiptERPResponse>();

			JSONObject jsonObj = new JSONObject(responseString);
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray jsonBody = jsonObj.getJSONArray("body");

				Gson gson = new Gson();
				postDeliveryReceiptERPResponseList = gson.fromJson(jsonBody.toString(), new TypeToken<List<PostDeliveryReceiptERPResponse>>() {
						}.getType());

				//PostDeliveryReceiptERPResponse postDeliveryReceiptERPResponse = postDeliveryReceiptERPResponseList.get(0);
				
				for (PostDeliveryReceiptERPResponse postDeliveryReceiptERPResponse : postDeliveryReceiptERPResponseList) {
					if(postDeliveryReceiptERPResponse.getReceiptNum() != null) {
						PurchaseOrder purchaseOrder = purchaseOrderSession.getbyPONumberEbs(postDeliveryReceiptERPRequest.getPoNumber());
						
						if (purchaseOrder != null) {
							DeliveryReceived deliveryReceived = deliveryReceivedSession.getDeliveryReceivedByPoIdSingle(purchaseOrder.getId());
							if (deliveryReceived != null) {
								deliveryReceived.setId(deliveryReceived.getId());
								deliveryReceived.setDeliveryReceiptNum(postDeliveryReceiptERPResponse.getReceiptNum());
								deliveryReceivedSession.updateDeliveryReceived(deliveryReceived, null);
								
								//email
								List<String> emailTo = new ArrayList<String>();
								PurchaseOrderItem purchaseOrderItemNew = new PurchaseOrderItem();
								Vendor vendor = new Vendor();
								RoleUser roleUserDVP = roleUserSession.getByUserId(purchaseOrder.getPurchaseRequest().getRequestorUserId());
								List<RoleUser> listRoleUserSVP = roleUserSession.getListRoleUserSvpByDvp(roleUserDVP);
								for(RoleUser roleUserSVP: listRoleUserSVP ) {
									emailTo.add(roleUserSVP.getUser().getEmail());
								}
								
								List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(purchaseOrder.getId());
								if(purchaseOrderItemList.size() > 0) {
									purchaseOrderItemNew = purchaseOrderItemList.get(0);
									vendor = vendorSession.getVendor(purchaseOrderItemNew.getVendor().getId());
								}
								else {
									vendor = null;
								}
								if(roleUserDVP != null) {
									emailTo.add(roleUserDVP.getUser().getEmail()); //CreatorBO
								}
								if(vendor != null) {
									emailTo.add(vendor.getEmail());
								}
								for(String email : emailTo) {
									emailNotificationSession.getMailDeliveryReceived(deliveryReceived.getId(), email);
								}
							}
							
						}else {
							String cause = "PO Number Not Found";
							syncSession.create(urlInterface, postDeliveryReceiptERPRequest, Response.error(srcRespDesc, responseString),
									Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_POST_DATA_RECEIPT, externalId);
							return Response.error(srcRespDesc);
						}
					}else {
						String cause = "Receipt Number Not Found";
						syncSession.create(urlInterface, postDeliveryReceiptERPRequest, Response.error(srcRespDesc, responseString),
								Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_POST_DATA_RECEIPT, externalId);
						return Response.error(srcRespDesc);
					}
				}

			} else {
				syncSession.create(urlInterface, postDeliveryReceiptERPRequest,
						Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_POST_DATA_RECEIPT, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
			// create log
			syncSession.create(urlInterface, postDeliveryReceiptERPRequest, Response.ok(responseString),
					Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_POST_DATA_RECEIPT, externalId);
			return Response.ok("SUCCESS");
		} catch (Exception e) {
			syncSession.create(urlInterface, postDeliveryReceiptERPRequest, Response.error(e), Constant.LOG_METHOD_POST,
					Constant.LOG_SERVICE_NAME_POST_DATA_RECEIPT, externalId);
			return Response.error(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/send-data-dr")
	@POST
	public Response resendDR(@FormParam("pk") Integer pk, @HeaderParam("Authorization") String tokenStr) throws SQLException, Exception {
		Response response = null;
		Token tokenObj = tokenSession.findByToken(tokenStr);
		User user = tokenObj.getUser();
		DeliveryReceived deliveryReceived = deliveryReceivedSession.getDeliveryReceived(pk);
		PostDeliveryReceiptERPRequest postDeliveryReceiptERPRequest = new PostDeliveryReceiptERPRequest();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		postDeliveryReceiptERPRequest.setPoNumber(deliveryReceived.getPurchaseOrder().getPoNumber());
		postDeliveryReceiptERPRequest.setBoId(deliveryReceived.getPurchaseOrder().getPurchaseRequest().getId().toString());
		postDeliveryReceiptERPRequest.setReceivedDate(dateFormat.format(deliveryReceived.getDateReceived()));
		if(deliveryReceived.getPurchaseOrder().getPurchaseOrderDate()!=null) {
			String poDate = deliveryReceived.getPurchaseOrder().getPurchaseOrderDate().toString();
			List<Integer> slaDeliveryTimeList = purchaseRequestItemSession.groupByPurchaseRequestId(deliveryReceived.getPurchaseOrder().getPurchaseRequest().getId());
			Integer slaDeliveryTime   = slaDeliveryTimeList.size()>0?slaDeliveryTimeList.get(0):null;
			for(Integer sla : slaDeliveryTimeList) {
				if(sla > slaDeliveryTime) {
					slaDeliveryTime = sla;
				}
			}
			if(slaDeliveryTime != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				
				calendar.setTime(sdf.parse(poDate));
				calendar.add(Calendar.DAY_OF_MONTH, slaDeliveryTime);  
				String newDate = sdf.format(calendar.getTime());  
				
				postDeliveryReceiptERPRequest.setExpectedReceivedDate(newDate);
			}else {
				postDeliveryReceiptERPRequest.setExpectedReceivedDate(dateFormat.format(deliveryReceived.getPurchaseOrder().getPurchaseOrderDate()));
			}
		}
		
		Organisasi rootParentOrganisasi = organisasiSession.getRootParentByOrganisasi(deliveryReceived.getPurchaseOrder().getPurchaseRequest().getOrganisasi());
		postDeliveryReceiptERPRequest.setOrgCode(rootParentOrganisasi.getCode());
		postDeliveryReceiptERPRequest.setPreparerNum(user.getKode());
		
		List<DeliveryReceivedDetail> deliveryReceivedDetailList = deliveryReceivedDetailSession.getbyDeliveryReceivedId(pk);
		DeliveryReceivedDetail deliveryReceivedDetailTemp = deliveryReceivedDetailList.get(0);
		Integer poLine = deliveryReceivedDetailTemp.getPoLineId();
		
		PurchaseOrderItem purchaseOrderItem = purchaseOrderItemSession.getPurchaseOrderItem(poLine);
		ArrayList<DataReceiptListERPRequest> DataReceiptListERPRequest = new ArrayList<DataReceiptListERPRequest>();
		for (DeliveryReceivedDetail deliveryReceivedDetail : deliveryReceivedDetailList) {
			DataReceiptListERPRequest dataReceiptListERPRequest = new DataReceiptListERPRequest();
			Integer qty = deliveryReceivedDetail.getQuantity();
			String strQty = NumberFormat.getInstance().format(qty);
			
			dataReceiptListERPRequest.setItemCode(purchaseOrderItem.getItem().getKode());
			dataReceiptListERPRequest.setQuantity(strQty);
			dataReceiptListERPRequest.setItemDescription(purchaseOrderItem.getPurchaseRequestItem().getCatalog().getCatalogContractDetail().getItemDesc());
			
			DataReceiptListERPRequest.add(dataReceiptListERPRequest);
		}
		postDeliveryReceiptERPRequest.setReceiptList(DataReceiptListERPRequest);
		
		response = postDeliveryReceiptInterfacing(postDeliveryReceiptERPRequest);
		
		if(response.getStatusText().equals("SUCCESS")) {
			userTransaction.begin();
			PurchaseRequest PurchaseRequest = purchaseRequestSession.get(deliveryReceived.getPurchaseOrder().getPurchaseRequest().getId());
			PurchaseOrder purchaseOrder = purchaseOrderSession.getPurchaseOrder(deliveryReceived.getPurchaseOrder().getId());
			
			// KAI - 20201123
			//PurchaseRequest.setStatus(Constant.PR_STATUS_PO_RECEIVED);
			//purchaseOrder.setStatus(Constant.PO_RECEIVED);
			
			userTransaction.commit();
			//procedure
			procedureSession.execute(Constant.SYNC_IN_UP_DR_CAT_TO_CM, deliveryReceived.getId(), deliveryReceived.getPurchaseOrder().getPoNumber());
		}
		return response;
	}
}
