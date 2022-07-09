package erp.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import erp.interfacing.entity.GetInvoicePaymentERPRequest;
import erp.interfacing.entity.GetInvoicePaymentERPResponse;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.InvoicePayment;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.invoicepayment.InvoicePaymentSession;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;
import id.co.promise.procurement.vendor.VendorSession;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/catalog/interfacing/invoicePaymentInterfacingService")
@Produces(MediaType.APPLICATION_JSON)
public class InvoicePaymentInterfacingService {
	@EJB
	SyncSession syncSession;
	@EJB
	private InvoicePaymentSession invoicePaymentSession;

	@EJB
	private PurchaseOrderSession purchaseOrderSession;

	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	@EJB
	private UserSession userSession;
	
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB
	private TokenSession tokenSession;
	
	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;
	
	@EJB
	private VendorSession vendorSession;
	
	@EJB
	private DeliveryReceivedSession deliveryReceivedSession;
	
	@Resource
	private UserTransaction userTransaction;
	@EJB
	ProcedureSession procedureSession;

	@SuppressWarnings("rawtypes")
	@Path("/getInvoicePayment")
	@POST
	public Response getInvoicePayment(GetInvoicePaymentERPRequest getInvoicePaymentERPRequest) throws SQLException {
		String url = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getInvoicePayment";
		String externalId = "";
		Gson gson = new Gson();
		List<GetInvoicePaymentERPResponse> invoicePaymentInterfacingList = new ArrayList<GetInvoicePaymentERPResponse>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		try {
			ResponseObject responseObj = Utils.doPost(getInvoicePaymentERPRequest, url);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			externalId = responseObj.getExternalId();
			
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray listData = jsonObj.getJSONArray("body");
				invoicePaymentInterfacingList = gson.fromJson(listData.toString(),
						new TypeToken<List<GetInvoicePaymentERPResponse>>() {
						}.getType());
				for (GetInvoicePaymentERPResponse invoicePaymentERPResponse : invoicePaymentInterfacingList) {
					userTransaction.begin();
					if (invoicePaymentERPResponse.getInvoiceID() != null) {

						InvoicePayment invoicePayment = invoicePaymentSession.getByNumber(invoicePaymentERPResponse.getInvoiceNum());
						
						//Data Invoice Payment di DB sudah ada atau Belum
						if(invoicePayment != null) {//Jika data nya sudah ada sebelumnya di DB, maka lakukan update
							invoicePayment.setOrganizationCode(invoicePaymentERPResponse.getOrgCode());
							invoicePayment.setInvoiceIdEbs(invoicePaymentERPResponse.getInvoiceID());
							invoicePayment.setInvoiceNumber(invoicePaymentERPResponse.getInvoiceNum());
							if (invoicePaymentERPResponse.getInvoiceDate() != null) {
								invoicePayment.setInvoiceDate(dateFormat.parse(invoicePaymentERPResponse.getInvoiceDate()));
							}
							invoicePayment.setInvoiceAmount(invoicePaymentERPResponse.getInvoiceAmount());
							invoicePayment.setAmountPaid(invoicePaymentERPResponse.getAmountPaid());
							if (invoicePaymentERPResponse.getCancelledDate() != null) {
								invoicePayment
										.setCanceledDate(dateFormat.parse(invoicePaymentERPResponse.getCancelledDate()));
							}
							invoicePayment.setInvoiceStatus(invoicePaymentERPResponse.getApprovalStatus());
							invoicePayment.setPoNumber(invoicePaymentERPResponse.getPoNumber());
							invoicePayment.setNomorKontrak(invoicePaymentERPResponse.getNomorContract());
							if (invoicePaymentERPResponse.getCreationDate() != null) {
								invoicePayment.setCreated(dateFormat.parse(invoicePaymentERPResponse.getCreationDate()));
							}
							if (invoicePaymentERPResponse.getLastUpdateDate() != null) {
								invoicePayment.setUpdated(dateFormat.parse(invoicePaymentERPResponse.getLastUpdateDate()));
							}
							
							invoicePaymentSession.updateInvoicePayment(invoicePayment, null);
						}else {//Jika data nya belum ada sebelumnya di DB, maka lakukan insert
							invoicePayment = new InvoicePayment();
							
							invoicePayment.setOrganizationCode(invoicePaymentERPResponse.getOrgCode());
							invoicePayment.setInvoiceIdEbs(invoicePaymentERPResponse.getInvoiceID());
							invoicePayment.setInvoiceNumber(invoicePaymentERPResponse.getInvoiceNum());
							if (invoicePaymentERPResponse.getInvoiceDate() != null) {
								invoicePayment.setInvoiceDate(dateFormat.parse(invoicePaymentERPResponse.getInvoiceDate()));
							}
							invoicePayment.setInvoiceAmount(invoicePaymentERPResponse.getInvoiceAmount());
							invoicePayment.setAmountPaid(invoicePaymentERPResponse.getAmountPaid());
							if (invoicePaymentERPResponse.getCancelledDate() != null) {
								invoicePayment
										.setCanceledDate(dateFormat.parse(invoicePaymentERPResponse.getCancelledDate()));
							}
							invoicePayment.setInvoiceStatus(invoicePaymentERPResponse.getApprovalStatus());
							invoicePayment.setPoNumber(invoicePaymentERPResponse.getPoNumber());
							invoicePayment.setNomorKontrak(invoicePaymentERPResponse.getNomorContract());
							if (invoicePaymentERPResponse.getCreationDate() != null) {
								invoicePayment.setCreated(dateFormat.parse(invoicePaymentERPResponse.getCreationDate()));
							}
							if (invoicePaymentERPResponse.getLastUpdateDate() != null) {
								invoicePayment.setUpdated(dateFormat.parse(invoicePaymentERPResponse.getLastUpdateDate()));
							}
							
							invoicePaymentSession.insertInvoicePayment(invoicePayment, null);
						}
						
						PurchaseOrder purchaseOrder = purchaseOrderSession.getbyPONumberEbs(invoicePaymentERPResponse.getPoNumber());
						if(purchaseOrder != null) {
							PurchaseRequest purchaseRequest = purchaseRequestSession.get(purchaseOrder.getPurchaseRequest().getId());
							DeliveryReceived deliveryReceived = deliveryReceivedSession.getDeliveryReceivedByPoIdSingle(purchaseOrder.getId());
							
							purchaseRequest.setId(purchaseRequest.getId());
							//email
							List<String> emailTo = new ArrayList<String>();
							PurchaseOrderItem purchaseOrderItemNew = new PurchaseOrderItem();
							User user = userSession.getUser(purchaseRequest.getRequestorUserId());
							RoleUser roleUserDVP = roleUserSession.getByUserId(purchaseRequest.getRequestorUserId());
							List<RoleUser> listRoleUserSVP = roleUserSession.getListRoleUserSvpByDvp(roleUserDVP);
							for(RoleUser roleUserSVP: listRoleUserSVP ) {
								emailTo.add(roleUserSVP.getUser().getEmail());
							}
							List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getPurchaseOrderItemByNumberPO(invoicePaymentERPResponse.getPoNumber());
							purchaseOrderItemNew = purchaseOrderItemList.get(0);
							Vendor vendor = vendorSession.getVendor(purchaseOrderItemNew.getVendor().getId());
							
							if(user != null) {
								emailTo.add(user.getEmail()); //CreatorBO
							}
							if(vendor != null) {
								emailTo.add(vendor.getEmail());
							}
							
							if (invoicePaymentERPResponse.getAmountPaid() != null) {
								// KAI - 20201123
								//purchaseRequest.setStatus(Constant.PR_STATUS_PO_PAYMENT_COMPLETED);
								for(String email : emailTo) {
									emailNotificationSession.getMailPembayaranSelesai(purchaseOrder, deliveryReceived, invoicePayment, email);
								}
								
							} else {
								// KAI - 20201123
								//purchaseRequest.setStatus(Constant.PR_STATUS_PO_PUBLISHING_INVOICE);
								for(String email : emailTo) {
									emailNotificationSession.getMailPenerbitanInvoice(purchaseOrder, deliveryReceived, invoicePayment, email);
								}
							}
							purchaseRequestSession.update(purchaseRequest, null);
						}
						userTransaction.commit();
						//procedureSession.execute(Constant.SYNC_IN_UP_PO_INV_CAT_TO_CM, invoicePayment.getPoNumber());
					} else {
						userTransaction.rollback();
						String cause = "No Data Found";
						syncSession.create(url, getInvoicePaymentERPRequest, Response.error(srcRespDesc, responseString),
								Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_GET_INVOICE_PAYMENT, externalId);
						return Response.error(srcRespDesc);
					}
				}
				syncSession.create(url, getInvoicePaymentERPRequest, Response.ok(responseString),
						Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_GET_INVOICE_PAYMENT, externalId);
				return Response.ok("SUCCESS");
			} else {
				syncSession.create(url, getInvoicePaymentERPRequest,
						Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_GET_INVOICE_PAYMENT, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
		} catch (Exception e) {
			syncSession.create(url, getInvoicePaymentERPRequest, Response.error(e), Constant.LOG_METHOD_POST,
					Constant.LOG_SERVICE_NAME_GET_INVOICE_PAYMENT, externalId);
			return Response.error(e);
		}
	}

}
