package erp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import erp.entity.PurchaseRequestInterfacingExpose;
import erp.interfacing.entity.GetPrByBoERPRequest;
import erp.interfacing.entity.GetPrByBoERPResponse;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;

@Stateless
@Path(value = "/catalog/interfacing/purchaseRequestInterfacingService")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseRequestInterfacingService {
	final static Logger log = Logger.getLogger(PurchaseRequestInterfacingService.class);
	@EJB 
	private UserSession userSession;
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	@EJB
	private TokenSession tokenSession;
	@EJB
	private SyncSession syncSession;
	@EJB
	private OrganisasiSession organisasiSession;

	@SuppressWarnings("rawtypes")
	@Path("/insert")
	@POST
	public Response insert(PurchaseRequestInterfacingExpose purchaseRequestInterfacing, @HeaderParam("key") String token,
			@Context UriInfo path) throws SQLException {
		// iniasaliasai
		if (token != null && token.equalsIgnoreCase(Constant.INTERFACING_KEY)) {

			try {
				if (purchaseRequestInterfacing != null) {
					PurchaseRequest purchaseRequest = purchaseRequestSession.getPRNumberAndOrgCode(
							purchaseRequestInterfacing.getPrNumber(), purchaseRequestInterfacing.getOrgCode());
					if (purchaseRequest != null) {
						if (purchaseRequestInterfacing.getPrStatus().equalsIgnoreCase("Approve")) {
							purchaseRequest.setStatus(5);
						} else if (purchaseRequestInterfacing.getPrStatus().equalsIgnoreCase("Reject")) {
							purchaseRequest.setStatus(4);
						} else {
							syncSession.create(path.getPath(), purchaseRequestInterfacing, Response.error("Status Undefined"),
									Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
							return Response.error("Status Undefined");
						}
						purchaseRequestSession.update(purchaseRequest, null);

					} 
					else {
						String cause = "No PR Found with Number : " + purchaseRequestInterfacing.getPrNumber()
								+ "in Organization : " + purchaseRequestInterfacing.getOrgCode();
						syncSession.create(path.getPath(), purchaseRequestInterfacing, Response.error(cause), Constant.LOG_METHOD_POST,
								Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
						return Response.error(cause);
					}
				}
				// create log
				syncSession.create(path.getPath(), purchaseRequestInterfacing, Response.ok(), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
				return Response.ok(Constant.RESPONSE_SUCCESS);
			} catch (Exception e) {
				// create log
				syncSession.create(path.getPath(), purchaseRequestInterfacing, Response.error(e), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
				return Response.error(e);
			}

		} 
		else {
			// create log
			syncSession.create(path.getPath(), purchaseRequestInterfacing, Response.error("Key Salah"), Constant.LOG_METHOD_POST,
					Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
			return Response.error("Key Salah");
		}

	}
	
	public void getIncompletePRStatus() throws SQLException {
		
		List<Integer> statusList = new ArrayList<Integer>();
		List<PurchaseRequest> incompletePurchaseRequestList = new ArrayList<PurchaseRequest>();
		// KAI - 20201123
		//statusList.add(Constant.PR_STATUS_PROCESSING);
		incompletePurchaseRequestList=purchaseRequestSession.getPRListByStatusList(statusList);
		
		GetPrByBoERPRequest getPrByBoERPRequest = new GetPrByBoERPRequest();
		for( PurchaseRequest incompletePurchaseRequest: incompletePurchaseRequestList) {
			Organisasi organisasi = organisasiSession.getRootParentByOrganisasi(incompletePurchaseRequest.getOrganisasi());
			getPrByBoERPRequest.setBoID(incompletePurchaseRequest.getId().toString());
			getPrByBoERPRequest.setOrgCode(organisasi.getCode());
			getPRByBO(getPrByBoERPRequest);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getPRByBO")
	@POST
	public Response getPRByBO(GetPrByBoERPRequest getPrByBoERPRequest) throws SQLException {
		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getPRByBO";
		String cause = "";
		String externalId = "";
		Gson gson = new Gson();
		List<GetPrByBoERPResponse> prByBoERPResponses = new ArrayList<GetPrByBoERPResponse>();
		try {
			ResponseObject responseObj = Utils.doPost(getPrByBoERPRequest, urlInterface);
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
				prByBoERPResponses = gson.fromJson(listData.toString(), new TypeToken<List<GetPrByBoERPResponse>>() {
				}.getType());
				for (GetPrByBoERPResponse getPrByBoERPResponse : prByBoERPResponses) {
					if (!getPrByBoERPResponse.getBoID().isEmpty()) {
						PurchaseRequest purchaseRequest = purchaseRequestSession.find(Integer.parseInt(getPrByBoERPResponse.getBoID()));
						if (purchaseRequest != null) {
							purchaseRequest.setStatusEbs(getPrByBoERPResponse.getPrStatus());
							if (getPrByBoERPResponse.getPrStatus().equalsIgnoreCase("Approved")) {
								// KAI - 20201123
								//purchaseRequest.setStatus(Constant.PR_STATUS_READY_TO_CREATE_PO);
								cause = "SUCCESS";
							} 
							else if (getPrByBoERPResponse.getPrStatus().equalsIgnoreCase("Reject")) {
								// KAI - 20201123
								//purchaseRequest.setStatus(Constant.PR_STATUS_REJECT_PR);
								cause = "SUCCESS";
							} 
							else {
								log.info(">> Status: " + getPrByBoERPResponse.getPrStatus() +" Undefined");
								cause = "Status: " + getPrByBoERPResponse.getPrStatus() +" Undefined";
							}
							purchaseRequestSession.update(purchaseRequest, null);
						} 
						else {
							log.info(">> BoID: " + getPrByBoERPResponse.getBoID() +" Tidak Ada");
							cause = "BoID: " + getPrByBoERPResponse.getBoID() +" Tidak Ada";
						}
					}
					else {
						cause = "No Data Found";
						syncSession.create(urlInterface, getPrByBoERPRequest, Response.error(srcRespDesc, responseString), Constant.LOG_METHOD_POST,
								Constant.LOG_SERVICE_NAME_GET_PR, externalId);
						return Response.error(srcRespDesc);
					}
				}
				// create log
				syncSession.create(urlInterface, getPrByBoERPRequest, Response.ok(responseString), Constant.LOG_METHOD_GET,
						Constant.LOG_SERVICE_NAME_GET_PR, externalId);
				return Response.ok(srcRespDesc);
			}
			else {
				syncSession.create(urlInterface, getPrByBoERPRequest, Response.error("Error : " + srcRespDesc + " , " + responseString),
						Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_GET_PR, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
		}
		catch (Exception e) {
			// create log
			syncSession.create(urlInterface, getPrByBoERPRequest, Response.error(e), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_PR, externalId);
			return Response.error(e);
		}

	}
				

}