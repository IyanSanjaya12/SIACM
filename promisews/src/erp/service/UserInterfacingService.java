package erp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import erp.entity.LoginInterfacingConsume;
import erp.entity.UserInterfacingExpose;
import erp.interfacing.entity.GetNipActiveERPRequest;
import erp.interfacing.entity.GetNipActiveERPResponse;
import erp.interfacing.entity.GetPOByPRERPRequest;
import erp.interfacing.entity.GetPOByPRERPResponse;
import erp.interfacing.entity.GetUserActiveERPResponse;
import erp.interfacing.entity.PostLoginUserDetailERPResponse;
import erp.interfacing.entity.PostLoginUserERPRequest;
import erp.interfacing.entity.PostLoginUserERPResponse;
import erp.interfacing.entity.PostLoginVendorDetailERPResponse;
import erp.interfacing.entity.PostLoginVendorERPRequest;
import erp.interfacing.entity.PostLoginVendorERPResponse;
import erp.interfacing.entity.PostPurchaseOrderERPResponse;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.RoleJabatan;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.RoleJabatanSession;
import id.co.promise.procurement.master.SubBidangUsahaSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;
import id.co.promise.procurement.vendor.BankVendorSession;
import id.co.promise.procurement.vendor.SegmentasiVendorSession;
import id.co.promise.procurement.vendor.VendorSession;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/catalog/interfacing/userInterfacingService")
@Produces(MediaType.APPLICATION_JSON)
public class UserInterfacingService {
	
	final static Logger log = Logger.getLogger(UserInterfacingService.class);
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	PurchaseRequestSession purchaseRequestSession;

	@EJB
	PurchaseRequestItemSession purchaseRequestItemSession;

	@EJB
	UserSession userSession;

	@EJB
	SyncSession syncSession;

	@EJB
	OrganisasiSession organiasiSession;

	@EJB
	VendorSession vendorSession;

	@EJB
	JabatanSession jabatanSession;

	@EJB
	RoleUserSession roleUserSession;

	@EJB
	RoleJabatanSession roleJabatanSession;

	@EJB
	OrganisasiSession organisasiSession;

	@EJB
	BankVendorSession bankVendorSession;
	@EJB
	SegmentasiVendorSession segmentasiVendorSession;
	@EJB
	SubBidangUsahaSession subBidangUsahaSession;

	@EJB
	VendorInterfacingService vendorInterfacingService;

	@EJB
	id.co.promise.procurement.master.ProcedureSession procedureSession;

	@Resource
	private UserTransaction userTransaction;

	@SuppressWarnings({ "rawtypes" })
	@Path("/getUserInterfacing")
	@GET
	public Response getUserInterfacing() throws SQLException {
		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EOFFICE + "/getUserActive";
		String externalId="";
		
		try {
			ResponseObject responseObj = Utils.doPost(urlInterface);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			List<GetUserActiveERPResponse> getUserActiveERPResponseList = new ArrayList<GetUserActiveERPResponse>() {
			};
			
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";

			if (statusCode.equalsIgnoreCase("200")) {
				Gson gson = new Gson();
				Object jsonBody = jsonObj.getJSONArray("body");
				getUserActiveERPResponseList = gson.fromJson(jsonBody.toString(),
						new TypeToken<List<GetUserActiveERPResponse>>() {
						}.getType());

				for (GetUserActiveERPResponse getUserActiveERPResponse : getUserActiveERPResponseList) {
					try {
						if(!getUserActiveERPResponse.getNipp().equalsIgnoreCase(null)) {
							
							User user = userSession.getUserByUsername(getUserActiveERPResponse.getNipp());
							
							if(user == null) {
								user = new User();
							}
							
							user.setNamaPengguna(getUserActiveERPResponse.getNama());
							user.setUsername(getUserActiveERPResponse.getNipp());
							user.setIdUserEOffice(getUserActiveERPResponse.getIdUser());
							user.setIdPerusahaan(Integer.parseInt(getUserActiveERPResponse.getIdPerusahaan()));
							user.setEmail(getUserActiveERPResponse.getEmail());
							user.setKode(getUserActiveERPResponse.getNipp());
							Jabatan jabatan = jabatanSession.getJabatanByJabatanIdEoffice(Integer.parseInt(getUserActiveERPResponse.getIdJabatan()));
							if (jabatan != null) {
								user.setJabatan(jabatan);
							}
							userTransaction.begin();
							// cek di dB sudah ada atau belum
							Integer countCM = 0;
							Integer countPM = 0;
							
							if (getUserActiveERPResponse.getIdJabatan().equalsIgnoreCase("0")) {
								log.info(">> ID Jabatan 0 Tidak Ada");
								user.setIsDelete(1);
								log.info(">> Lalu Delete : " + getUserActiveERPResponse.getNama());
							} else {
								if (jabatan == null) {
									log.error(getUserActiveERPResponse.getNmJabatan() + " : Nama Jabatan Tidak Ada");
									user.setIsDelete(1);
									log.info(">> Lalu Delete : " + getUserActiveERPResponse.getNama());
								}
							}
							
							if (user.getId() != null) {
								user.setIsDelete(0);
								userSession.updateUser(user, null);
								roleUserSession.deletebyUserId(user.getId());
								log.info(">> Update : " + getUserActiveERPResponse.getNama());
							} else {
								userSession.insertUser(user, null);
								log.info(">> Insert " + getUserActiveERPResponse.getNama());
							}
							
							Organisasi organisasi = organisasiSession.getByUnitIdEoffice(Integer.parseInt(getUserActiveERPResponse.getIdUnitKerja()));
							if (organisasi == null) {
								log.error("UNIT_ID_EOFFICE : " +getUserActiveERPResponse.getIdUnitKerja()+ " & Nama Organisasi : "+getUserActiveERPResponse.getNmUnit() + " Tidak Terdaftar");
							}
							
							if (jabatan != null && organisasi != null) {
								List<RoleJabatan> roleJabatanList = roleJabatanSession.getListByJabatanId(jabatan.getId());
								if (roleJabatanList != null) {
									for (RoleJabatan roleJabatan : roleJabatanList) {
										RoleUser roleUser = new RoleUser();
										roleUser.setRole(roleJabatan.getRole());
										roleUser.setUser(user);
										roleUser.setOrganisasi(organisasi);
										
										Boolean next = false;
										
										if (roleUser.getRole().getAppCode().equalsIgnoreCase("CM") && countCM == 0) {
											countCM = 1;
											next = true;
										} else if (roleUser.getRole().getAppCode().equalsIgnoreCase("PM") && countPM == 0) {
											countPM = 1;
											next = true;
										}
										
										if (next) {
											roleUserSession.insertRoleUser(roleUser);
										}
										
									}
									
								}
							}
							userTransaction.commit();
							if (organisasi != null) {
								procedureSession.execute(Constant.DO_INSERT_U_LOGIN, user.getId(),
								organisasiSession.getRootParentByOrganisasi(organisasi).getId());
							}
						}

					}

					catch (Exception e) {
					}
					// TODO: handle exception
				}
			}

			// create log
			syncSession.create(urlInterface, null, Response.ok(responseString), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_USER, externalId);
//			conn.disconnect();
			return Response.ok();
		} catch (Exception e) {
			// create log
			syncSession.create(urlInterface, null, Response.error(e), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_USER, externalId);
			return Response.error(e);
		}

	}
	
	
	public Boolean getAutentificationInterface(String url, String username, String password, Integer urlType)
			throws SQLException {
		Boolean isValid = true;
		String externalId = "";
		Gson gson = new Gson();
		// user
		// username = "274058081";
		// password = "123456";

		// vendor
		// username = "V000250";
		// password = "password2010";

		if (urlType == Constant.URL_TYPE_INTERNAL_USER) {
			PostLoginUserERPRequest postLoginUserERPRequest = new PostLoginUserERPRequest();
			postLoginUserERPRequest.setUsername(username);
			postLoginUserERPRequest.setPassword(password);
			List<PostLoginUserERPResponse> postLoginUserERPResponseList = new ArrayList<PostLoginUserERPResponse>();
			
			try {
				ResponseObject responseObj = Utils.doPost(postLoginUserERPRequest, url);
				String responseString = responseObj.getResponseString();
				String statusCode = responseObj.getStatusCode();
				
				JSONObject jsonObj = new JSONObject(responseString);
				
				//Get Response Description from API (Interfacing)
				JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
				String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
				externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";

				if (statusCode.equalsIgnoreCase("200")) {
					JSONArray jsonBody = jsonObj.getJSONArray("body");
					
					//Cek status authentication true/false from E-Office
					List<PostLoginUserERPResponse> postLoginUserERPResponses = new ArrayList<PostLoginUserERPResponse>();
					postLoginUserERPResponses = gson.fromJson(jsonBody.toString(), new TypeToken<List<PostLoginUserERPResponse>>() {}.getType());
					if(postLoginUserERPResponses == null || postLoginUserERPResponses.size() == 0) {
						isValid = false;
					} else {
						PostLoginUserERPResponse userInterfacing = postLoginUserERPResponses.get(0);
						isValid = Boolean.parseBoolean(userInterfacing.getStatus());
					}

					if (jsonBody.length() > 0) {
						updateDataUser(jsonBody);
						// create log
						syncSession.create(url, postLoginUserERPRequest, responseString, Constant.LOG_METHOD_POST,
								Constant.LOG_SERVICE_NAME_CHECK_LOGIN);
					} else {
						isValid = false;
						log.info(">> No Data Found");
					}
				} else {
					syncSession.create(url, postLoginUserERPRequest,
							Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
							Constant.LOG_SERVICE_NAME_CHECK_LOGIN, externalId);
					isValid = false;
				}
			} catch (Exception e) {
				// create log
				syncSession.create(url, postLoginUserERPRequest, Response.error(e), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_CHECK_LOGIN, externalId);
				isValid = false;
			}
		}

		else if (urlType == Constant.URL_TYPE_VENDOR) {
			PostLoginVendorERPRequest postLoginVendorERPRequest = new PostLoginVendorERPRequest();
			postLoginVendorERPRequest.setUsername(username);
			postLoginVendorERPRequest.setPassword(password);
			try {
				ResponseObject responseObj = Utils.doPost(postLoginVendorERPRequest, url);
				String responseString = responseObj.getResponseString();
				String statusCode = responseObj.getStatusCode();
				
				JSONObject jsonObj = new JSONObject(responseString);
				
				//Get Response Description from API (Interfacing)
				JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
				String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
				externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
				
				if (statusCode.equalsIgnoreCase("200")) {
					JSONArray jsonBody = jsonObj.getJSONArray("body");
					
					//Cek status authentication true/false from E-Office
					List<PostLoginVendorERPResponse> postLoginVendorERPResponses = new ArrayList<PostLoginVendorERPResponse>();
					postLoginVendorERPResponses = gson.fromJson(jsonBody.toString(), new TypeToken<List<PostLoginVendorERPResponse>>() {}.getType());
					if(postLoginVendorERPResponses == null || postLoginVendorERPResponses.size() == 0) {
						isValid = false;
					} else {
						PostLoginVendorERPResponse vendorInterfacing = postLoginVendorERPResponses.get(0);
						isValid = Boolean.parseBoolean(vendorInterfacing.getStatus());
					}
					
					if (jsonBody.length() > 0) {
							vendorInterfacingService.updateDataVendor(jsonBody);
						// create log
						syncSession.create(url, postLoginVendorERPRequest, Response.error(srcRespDesc, responseString), Constant.LOG_METHOD_POST,
								Constant.LOG_SERVICE_NAME_CHECK_LOGIN_VENDOR, externalId);
					} else {
						isValid = false;
						log.info(">> No Data Found");
					}
				} else {
					syncSession.create(url, postLoginVendorERPRequest,
							Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
							Constant.LOG_SERVICE_NAME_CHECK_LOGIN_VENDOR, externalId);
					isValid = false;
				}
			} catch (Exception e) {
				// create log
				syncSession.create(url, postLoginVendorERPRequest, Response.error(e), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_CHECK_LOGIN_VENDOR, externalId);
				isValid = false;
			}
		}
		else {
			log.info(">> URL Type not found");
		}
		return isValid;
	}

	
	private Boolean updateDataUser(JSONArray jsonBody) {
		Boolean isError = false;

		try {
			Gson gson = new Gson();

			List<PostLoginUserERPResponse> userInterfacingList = new ArrayList<PostLoginUserERPResponse>();
			userInterfacingList = gson.fromJson(jsonBody.toString(), new TypeToken<List<PostLoginUserERPResponse>>() {
			}.getType());
			PostLoginUserERPResponse userInterfacing = userInterfacingList.get(0);
			for (PostLoginUserDetailERPResponse userDetailInterfacing : userInterfacing.getData()) {

				User user = userSession.getUserByUsername(userDetailInterfacing.getNipp());
				// List <User> userList =
				// userSession.getUserByRealUserName(userInterfacing.getNipp());
				if (user != null) {
					Integer countCM = 0;
					Integer countPM = 0;

					user.setNamaPengguna(userDetailInterfacing.getNama());
					user.setUsername(userDetailInterfacing.getNipp());
					user.setIdUserEOffice(userDetailInterfacing.getIdUser());
					user.setKode(userDetailInterfacing.getNipp());
					Jabatan jabatan = jabatanSession.getJabatanByJabatanIdEoffice(Integer.parseInt(userDetailInterfacing.getIdJabatan()));
					if (jabatan != null) {
						user.setJabatan(jabatan);
					}
					user.setIsDelete(0);
					userSession.updateUser(user, null);
					roleUserSession.deletebyUserId(user.getId());
					if (jabatan != null) {
						List<RoleJabatan> roleJabatanList = roleJabatanSession.getListByJabatanId(jabatan.getId());
						Organisasi organisasi = organisasiSession.getByUnitIdEoffice(Integer.parseInt(userDetailInterfacing.getIdUnitKerja()));
						if (organisasi == null) {
							log.info(">> UNIT_ID_EOFFICE : " +userDetailInterfacing.getIdUnitKerja()+ " & Nama Organisasi : "+userDetailInterfacing.getNmUnit() + " Tidak Terdaftar");
						}
						if (roleJabatanList.size() > 0 && organisasi != null) {
							for (RoleJabatan roleJabatan : roleJabatanList) {
								RoleUser roleUser = new RoleUser();
								roleUser.setRole(roleJabatan.getRole());
								roleUser.setUser(user);
								roleUser.setOrganisasi(organisasi);
								Boolean next = false;

								if (roleUser.getRole().getAppCode().equalsIgnoreCase("CM") && countCM == 0) {
									countCM = 1;
									next = true;
								} else if (roleUser.getRole().getAppCode().equalsIgnoreCase("PM") && countPM == 0) {
									countPM = 1;
									next = true;
								}

								if (next) {
									roleUserSession.insertRoleUser(roleUser);
								}
							}

						} else {
							log.info(">> Position Not Found in list or Organization Not Found");
							isError = true;
						}
						procedureSession.execute(Constant.DO_INSERT_U_LOGIN, user.getId(),
								organisasiSession.getRootParentByOrganisasi(organisasi).getId());
					} else {
						log.info(userDetailInterfacing.getNmJabatan() + " : Nama Jabatan Tidak Ada");
						userSession.deleteUser(user.getId(), null);
						log.info("Lalu Delete : " + userDetailInterfacing.getNama());
					}
				}
			}
		} catch (Exception e) {
			log.info(">> Problem when updating user data");
			isError = true;
		}
		return isError;
	}

	@SuppressWarnings("rawtypes")
	@Path("/getNIPActive")
	@POST
	public Response getNIPActive() throws SQLException {
		String url = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getNIPActive";
		String externalId = "";
		Gson gson = new Gson();
		GetNipActiveERPRequest getNipActiveERPRequest = new GetNipActiveERPRequest();
		List<GetNipActiveERPResponse> nipActiveERPResponses = new ArrayList<GetNipActiveERPResponse>();
		List<User> userList = userSession.getUserList();
		try {
			for (User user : userList) {
				getNipActiveERPRequest.setEmpNumber(user.getUsername());
				ResponseObject responseObj = Utils.doPost(getNipActiveERPRequest, url);
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
					nipActiveERPResponses = gson.fromJson(listData.toString(),
							new TypeToken<List<GetNipActiveERPResponse>>() {
							}.getType());
					for (GetNipActiveERPResponse getNipActiveERPResponse : nipActiveERPResponses) {
						if (getNipActiveERPResponse.getFlag() != null) {
							Integer flag = getNipActiveERPResponse.getFlag();
							if (flag.equals(1)) {
								user.setFlagLoginEbs(Constant.LOGIN_EBS_ALLOW);
							} else if (flag.equals(0)) {
								user.setFlagLoginEbs(Constant.LOGIN_EBS_NOT_ALLOW);
							}
							userSession.updateUser(user, null);
							syncSession.create(url, getNipActiveERPRequest, Response.ok(responseString),
									Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_CHECK_USER, externalId);
						} else {
							String cause = "No Data Found";
							syncSession.create(url, getNipActiveERPRequest, Response.error(srcRespDesc, responseString),
									Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_CHECK_USER, externalId);
							return Response.error(srcRespDesc);
						}
					}
				} else {
					syncSession.create(url, getNipActiveERPRequest,
							Response.error("Error : " + srcRespDesc + " , " + responseString), Constant.LOG_METHOD_POST,
							Constant.LOG_SERVICE_NAME_CHECK_USER, externalId);
					return Response.error("Error : " + srcRespDesc);
				}
			}
			return Response.ok("SUCCESS");
		} catch (Exception e) {
			// create log
			syncSession.create(url, getNipActiveERPRequest, Response.error(e), Constant.LOG_METHOD_POST,
					Constant.LOG_SERVICE_NAME_CHECK_USER, externalId);
			return Response.error(e);
		}
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

}
