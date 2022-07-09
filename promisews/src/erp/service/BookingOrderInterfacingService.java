package erp.service;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import erp.interfacing.entity.DataBoDetailListERPRequest;
import erp.interfacing.entity.GetExpenseAccountERPRequest;
import erp.interfacing.entity.GetExpenseAccountERPResponse;
import erp.interfacing.entity.GetKodePuspelERPRequest;
import erp.interfacing.entity.GetKodePuspelERPResponse;
import erp.interfacing.entity.PostBookingOrderERPRequest;
import erp.interfacing.entity.PostBookingOrderERPResponse;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.ApprovalProcessVendor;
import id.co.promise.procurement.entity.ItemOrganisasi;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.ApprovalProcessVendorSession;
import id.co.promise.procurement.master.ItemOrganisasiSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;
import sap.interfacing.soap.pr.SapPrFunction;

@Stateless
@Path(value = "/catalog/interfacing/bookingOrderInterfacingService")
@TransactionManagement(TransactionManagementType.BEAN)
@Produces(MediaType.APPLICATION_JSON)
public class BookingOrderInterfacingService {
	final static Logger log = Logger.getLogger(BookingOrderInterfacingService.class);
	@EJB
	PurchaseRequestSession purchaseRequestSession;

	@EJB
	PurchaseRequestItemSession purchaseRequestItemSession;

	@EJB
	UserSession userSession;

	@EJB
	SyncSession syncSession;
	
	@EJB
	OrganisasiSession organisasiSession;

	@EJB
	TokenSession tokenSession;
	
	@EJB
	ApprovalProcessTypeSession approvalProcessTypeSession;
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	EmailNotificationSession emailNotificationSession;
	
	@EJB
	ItemOrganisasiSession itemOrganisasiSession;
	
	@EJB
	ApprovalProcessVendorSession approvalProcessVendorSession;
	
	@EJB
	SapPrFunction sapPrFunction;
	
	@Resource
	private UserTransaction userTransaction;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/postBookingOrder")
	@POST
	public Response postBookingOrder(PurchaseRequest purchaseRequest, Token token) throws SQLException, Exception {
		List<PurchaseRequestItem> purchaseRequestItemList = purchaseRequestItemSession
				.getPurchaseRequestItemByPurchaseRequestId(purchaseRequest.getId());
		User user = userSession.getUser(purchaseRequest.getUserId());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Organisasi orgRoot = organisasiSession.getRootParentByOrganisasi(purchaseRequest.getOrganisasi());
		PostBookingOrderERPRequest postBookingOrderERPRequest = new PostBookingOrderERPRequest();
		postBookingOrderERPRequest.setOrgCode(orgRoot.getCode());
		// KAI - 20210204 - #20867
		//	postBookingOrderERPRequest.setInvOrgID(purchaseRequest.getAddressBook().getAddressBookEbs());
		postBookingOrderERPRequest.setDepartment(purchaseRequest.getOrganisasi().getNama());
		postBookingOrderERPRequest.setTotalAmount(purchaseRequest.getTotalHarga().toString());
		if (purchaseRequest.getApprovedDate()!=null) {
			postBookingOrderERPRequest.setBoApproveDate(dateFormat.format(purchaseRequest.getApprovedDate()));
		}
		else {
			postBookingOrderERPRequest.setBoApproveDate("");
		}
		postBookingOrderERPRequest.setBoID(purchaseRequest.getId().toString());
		try {
			postBookingOrderERPRequest.setPrepareName(user.getNamaPengguna());
			postBookingOrderERPRequest.setPrepareNum(user.getKode());
		} catch (Exception e) {
			log.error("no user found for id " + user.getId());
		}
		postBookingOrderERPRequest.setPuspelCode(purchaseRequest.getPuspelCode());
		postBookingOrderERPRequest.setVendorNo(purchaseRequestItemList.get(0).getVendor().getVendorIdEproc());
		postBookingOrderERPRequest.setShippingFee(purchaseRequest.getTotalHargaOngkir().toString());
		//postBookingOrderERPRequest.setAddressBookID(purchaseRequest.getAddressBook().getAddressBookEbs());
		postBookingOrderERPRequest.setAsuransiFee(Constant.ASURANSI.toString());
		if (purchaseRequest.getCreated()!=null) {
			postBookingOrderERPRequest.setBoDate(dateFormat.format(purchaseRequest.getCreated()));
			postBookingOrderERPRequest.setCreationDate(dateFormat.format(purchaseRequest.getCreated()));
			postBookingOrderERPRequest.setDeliveryDate(dateFormat.format(purchaseRequest.getCreated()));
		}
		else {
			postBookingOrderERPRequest.setBoDate("");
			postBookingOrderERPRequest.setCreationDate("");
			postBookingOrderERPRequest.setDeliveryDate("");
		}
		if (purchaseRequest.getUpdated()!=null) {
			postBookingOrderERPRequest.setLastUpdatedate(dateFormat.format(purchaseRequest.getUpdated()));
		}
		else {
			postBookingOrderERPRequest.setLastUpdatedate("");
		}
		postBookingOrderERPRequest.setApprovalPath(purchaseRequest.getOrgApprovalPath() != null ? purchaseRequest.getOrgApprovalPath().getApprovalPathName(): "NULL");
		postBookingOrderERPRequest.setApprovalPathID(purchaseRequest.getOrgApprovalPath() != null && purchaseRequest.getOrgApprovalPath().getApprovalPathId() != null ? purchaseRequest.getOrgApprovalPath().getApprovalPathId().toString():"NULL");
		postBookingOrderERPRequest.setLinkLampiranPR(purchaseRequest.getLinkLampiranPr() != null ? purchaseRequest.getLinkLampiranPr(): "-"  );
		postBookingOrderERPRequest.setLinkLampiranKontrak(purchaseRequest.getLinkLampiranKontrak() != null ? purchaseRequest.getLinkLampiranKontrak() : "-"  );

		ArrayList BookingOrderDetailList = new ArrayList();
		for (PurchaseRequestItem purchaseRequestItem : purchaseRequestItemList) {
			DataBoDetailListERPRequest dataBoDetailListERPRequest = new DataBoDetailListERPRequest();
			dataBoDetailListERPRequest.setItemCode(purchaseRequestItem.getItem().getKode());
			dataBoDetailListERPRequest.setItemID(purchaseRequestItem.getItem().getId().toString());
			dataBoDetailListERPRequest.setDiscount(purchaseRequestItem.getDiscount().toString());// ok
			dataBoDetailListERPRequest.setBoID(purchaseRequest.getId().toString());
			dataBoDetailListERPRequest.setQuantity(purchaseRequestItem.getQuantity().toString());
			dataBoDetailListERPRequest.setUom(purchaseRequestItem.getItem().getSatuanId().getCode());
			dataBoDetailListERPRequest.setCurrency(purchaseRequestItem.getMataUang().getKodeEbs());
			dataBoDetailListERPRequest.setBoLineID(purchaseRequestItem.getId().toString());
			dataBoDetailListERPRequest.setPrice(purchaseRequestItem.getNormalPrice().toString());
			dataBoDetailListERPRequest.setPriceDiscount(purchaseRequestItem.getPrice().toString());// ok
			dataBoDetailListERPRequest.setCatalogItemID(purchaseRequestItem.getCatalog().getId().toString());
			dataBoDetailListERPRequest.setTotalAmount(purchaseRequestItem.getTotal().toString());
			dataBoDetailListERPRequest.setItemDescription(purchaseRequestItem.getCatalog().getCatalogContractDetail().getItemDesc());
			if (purchaseRequestItem.getCreated()!=null) {
				dataBoDetailListERPRequest.setCreationDate(dateFormat.format(purchaseRequestItem.getCreated()));
			}
			else {
				dataBoDetailListERPRequest.setCreationDate("");
			}
			if (purchaseRequestItem.getUpdated()!=null) {
				dataBoDetailListERPRequest.setLastUpdateDate(dateFormat.format(purchaseRequestItem.getUpdated()));
			}
			else {
				dataBoDetailListERPRequest.setLastUpdateDate("");
			}
			BookingOrderDetailList.add(dataBoDetailListERPRequest);
		}
		postBookingOrderERPRequest.setBoDetailList(BookingOrderDetailList);
		
		List<PostBookingOrderERPResponse> bookingOrderERPResponses = new ArrayList<PostBookingOrderERPResponse>();
		String url = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/postBookingOrder";
		String externalId = "";
		Gson gson = new Gson();
		try {
			ResponseObject responseObj = Utils.doPost(postBookingOrderERPRequest, url);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray listData = jsonObj.getJSONArray("body");
				bookingOrderERPResponses = gson.fromJson(listData.toString(), new TypeToken<List<PostBookingOrderERPResponse>>() {
				}.getType());
				for (PostBookingOrderERPResponse postBookingOrderERPResponse : bookingOrderERPResponses) {
					if (postBookingOrderERPResponse.getBoID()!=null) {
						purchaseRequest.setPrnumber(postBookingOrderERPResponse.getPrNumber());
						purchaseRequest.setPrIdEbs(Integer.parseInt(postBookingOrderERPResponse.getRequisitionHeaderID()));
						// KAI - 20201123
						//	purchaseRequest.setStatus(Constant.PR_STATUS_PROCESSING);
						
						//Email
						List <String> allEmail = new ArrayList<String>();
						User userEmail = userSession.getUser(purchaseRequest.getRequestorUserId());
						RoleUser roleUserDVP = roleUserSession.getByUserId(purchaseRequest.getRequestorUserId());
						List<RoleUser> listRoleUserSVP = roleUserSession.getListRoleUserSvpByDvp(roleUserDVP);
						for(RoleUser roleUserSVP: listRoleUserSVP ) {
							allEmail.add(roleUserSVP.getUser().getEmail());
						}
						if(userEmail != null) {
							allEmail.add(userEmail.getEmail());
						}
						for(String email : allEmail) {
							emailNotificationSession.getMailBookingOrderToEbs(purchaseRequest, email);
						}
					}
					else {
						purchaseRequest.setInterfacingNotes(srcRespDesc);
						purchaseRequestSession.update(purchaseRequest, token);
						syncSession.create(url, postBookingOrderERPRequest, Response.error(srcRespDesc, responseString), Constant.LOG_METHOD_POST,
								Constant.LOG_SERVICE_NAME_SEND_BO, externalId);
						return Response.error(srcRespDesc);
					}
				}
				purchaseRequest.setInterfacingNotes(srcRespDesc);
				purchaseRequestSession.update(purchaseRequest, token);
				syncSession.create(url, postBookingOrderERPRequest, Response.ok(responseString), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_SEND_BO, externalId);
				return Response.ok("SUCCESS");
			} else {
				purchaseRequest.setInterfacingNotes(srcRespDesc);
				purchaseRequestSession.update(purchaseRequest, token);
				syncSession.create(url, postBookingOrderERPRequest, Response.error("Error : " + statusCode + " , " + responseString),
						Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_SEND_BO, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
		} catch (Exception e) {
			// create log
			syncSession.create(url, postBookingOrderERPRequest, Response.error(e), Constant.LOG_METHOD_POST,
					Constant.LOG_SERVICE_NAME_SEND_BO, externalId);
			return Response.error(e);
		}
	}
	
	
	//KAI-20201124
	@SuppressWarnings("rawtypes")
	@Path("/send-data-bo/{pk}")
	@GET
	public Response syncBO(@PathParam("pk") Integer pk, @HeaderParam("Authorization") String tokenStr) throws SQLException, Exception {
		Token tokenObj = tokenSession.findByToken(tokenStr);
		PurchaseRequest purchaseRequestOld = purchaseRequestSession.get(pk);
		PurchaseRequest purchaseRequestNew = purchaseRequestOld;
		Response response = null;
		ApprovalProcessType approvalProcessType = approvalProcessTypeSession.findApprovalForApprovalBookingOrder(pk);
		ApprovalProcessVendor approvalProcessVendor = approvalProcessVendorSession.getApprovalProcessVendorByPurchaseRequestId(pk);
		
		userTransaction.begin();
		if(purchaseRequestOld != null) {
			List<PurchaseRequestItem> purchaseRequestItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaseRequestOld.getId());
			
			//Sof Delete data PR lama dan PR Item lama
			purchaseRequestItemSession.deletePurchaseRequestItemByPrId(purchaseRequestOld.getId(), tokenObj);
			purchaseRequestOld.setIsDelete(Constant.ONE_VALUE);
			purchaseRequestOld.setDeleted(new Date());
			purchaseRequestOld.setId(pk);
			purchaseRequestSession.update(purchaseRequestOld, tokenObj);
			
			//Insert data PR baru dan PR Item baru
			purchaseRequestNew.setId(null);
			purchaseRequestSession.insert(purchaseRequestNew, tokenObj);
			if(purchaseRequestItemList.size() > 0) {
				for(PurchaseRequestItem prItem : purchaseRequestItemList) {
					PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
					purchaseRequestItem.setAddressBook(prItem.getAddressBook());
					purchaseRequestItem.setAlamat(prItem.getAlamat());
					purchaseRequestItem.setAsuransi(prItem.getAsuransi());
					purchaseRequestItem.setAcctasscat(prItem.getAcctasscat());
					purchaseRequestItem.setCatalog(prItem.getCatalog());
					purchaseRequestItem.setCostcenter(prItem.getCostcenter());
					purchaseRequestItem.setCostCenterSap(prItem.getCostCenterSap());
					purchaseRequestItem.setCreated(new Date());
					purchaseRequestItem.setDiscount(prItem.getDiscount());
					purchaseRequestItem.setEstimatedDeliveryTime(prItem.getEstimatedDeliveryTime());
					purchaseRequestItem.setGrandTotal(prItem.getGrandTotal());
					purchaseRequestItem.setgLAccountSap(prItem.getgLAccountSap());
					purchaseRequestItem.setIsDelete(Constant.ZERO_VALUE);
					purchaseRequestItem.setItem(prItem.getItem());
					purchaseRequestItem.setItemname(prItem.getItemname());
					purchaseRequestItem.setKode(prItem.getKode());
					purchaseRequestItem.setMataUang(prItem.getMataUang());
					purchaseRequestItem.setNormalPrice(prItem.getNormalPrice());
					purchaseRequestItem.setOngkosKirim(prItem.getOngkosKirim());
					purchaseRequestItem.setPath(prItem.getPath());
					purchaseRequestItem.setPrice(prItem.getPrice());
					purchaseRequestItem.setPriceafterjoin(prItem.getPriceafterjoin());
					purchaseRequestItem.setPurchaserequest(prItem.getPurchaserequest());
					purchaseRequestItem.setPurchaserequestjoin(prItem.getPurchaserequestjoin());
					purchaseRequestItem.setPurGroupSap(prItem.getPurGroupSap());
					purchaseRequestItem.setQtyafterjoin(prItem.getQtyafterjoin());
					purchaseRequestItem.setQuantity(prItem.getQuantity());
					purchaseRequestItem.setQuantitybalance(prItem.getQuantitybalance());
					purchaseRequestItem.setQuantity(prItem.getQuantity());
					purchaseRequestItem.setPreqName(prItem.getPreqName());
					purchaseRequestItem.setSpecification(prItem.getSpecification());
					purchaseRequestItem.setStatus(prItem.getStatus());
					purchaseRequestItem.setStoreLocSap(prItem.getStoreLocSap());
					purchaseRequestItem.setTotal(prItem.getTotal());
					purchaseRequestItem.setUnit(prItem.getUnit());
					purchaseRequestItem.setVendor(prItem.getVendor());
					purchaseRequestItem.setVendorname(prItem.getVendorname());
					purchaseRequestItem.setPurchaserequest(purchaseRequestNew);
					purchaseRequestItem.setSlaDeliveryTime(prItem.getSlaDeliveryTime());
					purchaseRequestItemSession.createPurchaseRequestItem(purchaseRequestItem, tokenObj);
				}
				approvalProcessType.setValueId(purchaseRequestNew.getId());
				approvalProcessTypeSession.update(approvalProcessType, tokenObj);
				if(approvalProcessVendor != null) {					
					approvalProcessVendor.setPurchaseRequestId(purchaseRequestNew.getId());
					approvalProcessVendorSession.updateApprovalProcessVendor(approvalProcessVendor, tokenObj);
				}
			}
		}
		userTransaction.commit();
		//KAI-20201124
		if (purchaseRequestOld != null && purchaseRequestOld.getPrnumber() == null) {
			// response = postBookingOrder(purchaseRequestNew, tokenObj); hghgvhgvhv
			if (Constant.IS_INTERFACING_SAP) {
				response = sapPrFunction.submitPr(purchaseRequestNew, tokenObj);
				if(response.getStatusText().equalsIgnoreCase("SUCCESS")) {
					userTransaction.begin();
					//purchaseRequestNew.setStatus(Constant.PR_STATUS_PROCESSING);
					//purchaseRequestSession.update(purchaseRequestNew, tokenObj);
					userTransaction.commit();
				}
			} else {
				purchaseRequestNew.setStatus(Constant.PR_STATUS_READY_TO_CREATE_PO);
				Random rand = new Random(); 
				Integer min = 1000;
				Integer max = 9999;
				Integer random = rand.nextInt((max - min) + 1) + min;
				
				purchaseRequestNew.setPrnumber("PR-"+random.toString());
				purchaseRequestSession.update(purchaseRequestNew, tokenObj);
			}
			
		}
		
		return response;
	} 
	
	
	@Path("/getKodePuspel")
	@POST
	public Map getKodePuspel(@HeaderParam("Authorization") String token) throws SQLException, Exception {
		User user = tokenSession.findByToken(token).getUser();
		GetKodePuspelERPRequest getKodePuspelERPRequest = new GetKodePuspelERPRequest();
		getKodePuspelERPRequest.setEmployeeNumber(user.getKode());
		List<String> puspelCodeList = new ArrayList<String>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		List<GetKodePuspelERPResponse> getKodePuspelERPResponses = new ArrayList<GetKodePuspelERPResponse>();
		String url = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getKodePuspel";
		String externalId = "";
		Gson gson = new Gson();
		try {
			ResponseObject responseObj = Utils.doPost(getKodePuspelERPRequest, url);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			log.info("==================== Perubahan Rahmat ====================");
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray listData = jsonObj.getJSONArray("body");
				getKodePuspelERPResponses = gson.fromJson(listData.toString(), new TypeToken<List<GetKodePuspelERPResponse>>() {
				}.getType());
				for (GetKodePuspelERPResponse getKodePuspelERPResponse : getKodePuspelERPResponses) {
					if (getKodePuspelERPResponse.getKodePuspel()!=null) {
						puspelCodeList.add(getKodePuspelERPResponse.getKodePuspel());
					}
					else {
						syncSession.create(url, getKodePuspelERPResponses, Response.error(srcRespDesc, responseString), Constant.LOG_METHOD_GET,
								Constant.LOG_SERVICE_NAME_GET_PUSPEL_CODE, externalId);
						map.put("response", "NOT_FOUND");
						return map;
					}
				}
				syncSession.create(url, getKodePuspelERPResponses, Response.ok(responseString), Constant.LOG_METHOD_GET,
						Constant.LOG_SERVICE_NAME_GET_PUSPEL_CODE, externalId);
				map.put("kodePuspel", puspelCodeList);
				map.put("response", "SUCCESS");
				return map;
			} else {
				syncSession.create(url, getKodePuspelERPResponses, Response.error("Error : " + statusCode + " , " + responseString),
						Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PUSPEL_CODE, externalId);
				map.put("response", "ERROR");
				return map;
			}
		} catch (Exception e) {
			// create log
			syncSession.create(url, getKodePuspelERPResponses, Response.error(e), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_PUSPEL_CODE, externalId);
			map.put("response", "ERROR");
			return map;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getExpenseAccount")
	@POST
	public Response getExpenseAccount(String itemCode) throws SQLException, Exception {
		GetExpenseAccountERPRequest getExpenseAccountERPRequest = new GetExpenseAccountERPRequest();
		getExpenseAccountERPRequest.setItemCode(itemCode);
		List<GetExpenseAccountERPResponse> getExpenseAccountERPResponses = new ArrayList<GetExpenseAccountERPResponse>();
		String url = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getExpenseAccount";
		String externalId = "";
		Gson gson = new Gson();
		try {
			ResponseObject responseObj = Utils.doPost(getExpenseAccountERPRequest, url);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray listData = jsonObj.getJSONArray("body");
				getExpenseAccountERPResponses = gson.fromJson(listData.toString(), new TypeToken<List<GetExpenseAccountERPResponse>>() {
				}.getType());
				for (GetExpenseAccountERPResponse getExpenseAccountERPResponse : getExpenseAccountERPResponses) {
					if (getExpenseAccountERPResponse.getExpenseAccount()!=null) {
						ItemOrganisasi itemOrganisasi = itemOrganisasiSession.getItemOrganisasiByItemCodeAndOrgCode(getExpenseAccountERPResponse.getItemCode(), getExpenseAccountERPResponse.getOrgCode());
						if (itemOrganisasi != null){
							itemOrganisasi.setExpenseAccount(getExpenseAccountERPResponse.getExpenseAccount());
							itemOrganisasi.setExpenseAccountDescription(getExpenseAccountERPResponse.getSegmentDesc());
							itemOrganisasiSession.updateItemOrganisasi(itemOrganisasi, null);
						}
					}
					else {
						syncSession.create(url, getExpenseAccountERPResponses, Response.error(srcRespDesc, responseString), Constant.LOG_METHOD_GET,
								Constant.LOG_SERVICE_NAME_GET_EXPENSE_ACCOUNT, externalId);
						return Response.error(srcRespDesc);
					}
				}
				syncSession.create(url, getExpenseAccountERPResponses, Response.ok(responseString), Constant.LOG_METHOD_GET,
						Constant.LOG_SERVICE_NAME_GET_EXPENSE_ACCOUNT, externalId);
				return Response.ok("SUCCESS");
			} else {
				syncSession.create(url, getExpenseAccountERPResponses, Response.error("Error : " + statusCode + " , " + responseString),
						Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_EXPENSE_ACCOUNT, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
		} catch (Exception e) {
			// create log
			syncSession.create(url, getExpenseAccountERPResponses, Response.error(e), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_EXPENSE_ACCOUNT, externalId);
			return Response.error(e);
		}
	}
}
