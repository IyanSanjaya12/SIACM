package erp.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
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

import erp.entity.BankVendorInterfacingExpose;
import erp.entity.BidangUsahaInterfacingExpose;
import erp.entity.VendorInterfacingExpose;
import erp.interfacing.entity.DataBankVendorERPResponse;
import erp.interfacing.entity.DataVendorProductsERPResponse;
import erp.interfacing.entity.GetVendorListActiveERPResponse;
import erp.interfacing.entity.PostLoginVendorDetailERPResponse;
import erp.interfacing.entity.PostLoginVendorERPResponse;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BlacklistVendor;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SubBidangUsaha;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.BidangUsahaSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.master.RoleSession;
import id.co.promise.procurement.master.SubBidangUsahaSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;
import id.co.promise.procurement.vendor.BankVendorSession;
import id.co.promise.procurement.vendor.SegmentasiVendorSession;
import id.co.promise.procurement.vendor.VendorSession;
import id.co.promise.procurement.vendor.management.BlacklistVendorSession;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/catalog/interfacing/vendorInterfacingService")
@Produces(MediaType.APPLICATION_JSON)
public class VendorInterfacingService {
	
	final static Logger log = Logger.getLogger(VendorInterfacingService.class);
	@EJB
	private UserSession userSession;
	@EJB
	private VendorSession vendorSession;
	@EJB
	private BlacklistVendorSession blacklistVendorSession;
	@EJB
	private OrganisasiSession organiasiSession;
	@EJB
	private TokenSession tokenSession;
	@EJB
	private BankVendorSession bankVendorSession;
	@EJB
	private BidangUsahaSession bidangUsahaSession;
	@EJB
	private SegmentasiVendorSession segmentasiVendorSession;
	@EJB
	private SubBidangUsahaSession subBidangUsahaSession;
	@EJB
	private SyncSession syncSession;
	@EJB
	private RoleSession roleSession;
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB ProcedureSession procedureSession;

	@Resource private UserTransaction userTransaction;
	
	@Path("/insert")
	@POST
	public Response insert(VendorInterfacingExpose vendorInterfacing, @HeaderParam("key") String token,
			@Context UriInfo path) throws SQLException, NotSupportedException, SystemException {
		// iniasaliasai
		if (token != null && token.equalsIgnoreCase(Constant.INTERFACING_KEY)) {

			User user = new User();
			userTransaction.begin();
			Vendor vendor = new Vendor();
			BlacklistVendor blacklistVendor = new BlacklistVendor();
			try {

				if (vendorInterfacing != null) {

					// maping User
					user.setUsername(vendorInterfacing.getVendorNo());
					user.setNamaPengguna(vendorInterfacing.getFullname());
					user.setEmail(vendorInterfacing.getEmail());
					Vendor checkVendor = vendorSession.getVendorIdEproc(vendorInterfacing.getVendorNo());
					if (checkVendor != null) {
						User checkUser = userSession.getUser(checkVendor.getUser());
						user.setId(checkUser.getId());
						userSession.updateUser(user, null);
					}

					// Maping Vendor
					vendor.setVendorIdEproc(vendorInterfacing.getVendorNo());
					vendor.setNama(vendorInterfacing.getFullname());
					vendor.setAlamat(vendorInterfacing.getAddress1());
					vendor.setNomorTelpon(vendorInterfacing.getPhoneNo());
					vendor.setEmail(vendorInterfacing.getEmail());
					vendor.setNpwp(vendorInterfacing.getTaxNo());
					//vendor.setTglRegistrasi(vendorInterfacing.getUpdDt());
					vendor.setKota(vendorInterfacing.getCity());
					vendor.setProvinsi(vendorInterfacing.getRegion());

					// vendor.setStatus(vendorInterfacing.getIS_ACTIVE());

					Organisasi organisasi = organiasiSession.find(vendorInterfacing.getWilayahDescr());
					vendor.setAfco(organisasi);
					Date startDate = new Date();//vendorInterfacing.getDtStart();
					Date endDate = new Date();//vendorInterfacing.getDtEnd();

					// Kondisi jika vendor masuk ke dalam list vendor blacklist
					if (startDate != null && endDate != null) {
						Date dateNow = new Date();
						blacklistVendor.setBlacklistVendorTglAwal(startDate);
						blacklistVendor.setBlacklistVendorTglAkhir(endDate);
						blacklistVendor.setBlacklistVendorUserId(vendor.getUser());
						blacklistVendor.setBlacklistVendorId(vendor.getId());
						if (dateNow.after(startDate) && dateNow.before(endDate)) {
							vendor.setStatus(3); // 3 Vendor Blacklist
						}
					}

					// update or create
					if (checkVendor != null) {// Kondisi jika data vendor lama sudah ada di promise (Update Data Vendor)
						vendor.setUser(user.getId());
						vendor.setId(checkVendor.getId());
						vendorSession.updateVendor(vendor, null);
						/* mapping Bank Vendor(insert or delete) */
						ArrayList<String> tempNoRekList = new ArrayList<String>();
						Integer index = new Integer(0);
						if (vendorInterfacing != null) {
							for (BankVendorInterfacingExpose bankVendorInf : vendorInterfacing.getBankVendorList()) {
								BankVendor bankVendorNew = new BankVendor();
								tempNoRekList.add(bankVendorInf.getBankNumber());
								List<BankVendor> bankVendorTemp = bankVendorSession
										.getListBankVendorByBankNumber(bankVendorInf.getBankNumber(), vendor.getId());
								if (bankVendorTemp.size() > 0) {
									bankVendorNew.setId(bankVendorTemp.get(0).getId());
									bankVendorNew.setNamaBank(bankVendorInf.getBankName());
									bankVendorNew.setNomorRekening(bankVendorInf.getBankNumber());
									bankVendorNew.setNamaNasabah(bankVendorInf.getBankOwnerName());
									bankVendorNew.setVendor(vendor);
									bankVendorNew.setUserId(user.getId());

									bankVendorSession.updateBankVendor(bankVendorNew, null);
								} else {
									bankVendorNew.setNamaBank(bankVendorInf.getBankName());
									bankVendorNew.setNomorRekening(bankVendorInf.getBankNumber());
									bankVendorNew.setNamaNasabah(bankVendorInf.getBankNumber());
									bankVendorNew.setVendor(vendor);
									bankVendorNew.setUserId(user.getId());

									bankVendorSession.insertBankVendor(bankVendorNew, null);
								}
							}
							/* delete when data in bank vendor is not same with bank vendor interfacing */
							List<BankVendor> bankVendorList = bankVendorSession.getBankVendorByVendorId(vendor.getId());
							for (BankVendor bankVendor : bankVendorList) {
								BankVendor bankVendorNew = new BankVendor();
								if (!bankVendor.getNomorRekening().equalsIgnoreCase(tempNoRekList.get(index))) {
									bankVendorNew.setIsDelete(1);
								}
								index++;
							}
						}

//						bankVendorSession.deleteBankVendorByVendorId(checkVendor.getId());
						segmentasiVendorSession.deleteSegmentasiVendorByVendorId(checkVendor.getId());
						blacklistVendorSession.createBlacklistVendor(blacklistVendor, null);
					} else {// Kondisi jika data vendor belum ada di promise (Vendor Baru)
						userSession.insertUser(user, null);
						vendor.setUser(user.getId());
						vendorSession.insertVendor(vendor, null);
					}

					// Maping Segmentasi Vendor
					if (vendorInterfacing.getBidangUsahaList() != null) {
						for (BidangUsahaInterfacingExpose bidangUsahaInterfacing : vendorInterfacing
								.getBidangUsahaList()) {
							SegmentasiVendor segmentasiVendor = new SegmentasiVendor();
							segmentasiVendor
									.setSubBidangUsaha(subBidangUsahaSession.find(bidangUsahaInterfacing.getGroupId()));
							segmentasiVendor.setVendor(vendor);
							segmentasiVendor.setUserId(user.getId());
							segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor, null);
						}
					}

				}

				userTransaction.commit();
				procedureSession.execute(Constant.SYNC_IN_UP_VEND_CAT_TO_CM, vendor.getId());

				// create log
				syncSession.create(path.getPath(), vendorInterfacing, Response.ok(), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
				return Response.ok();
			} catch (Exception e) {
				// create log
				syncSession.create(path.getPath(), vendorInterfacing, Response.error(e), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
				return Response.error(e);
			}

		} else {
			// create log
			syncSession.create(path.getPath(), vendorInterfacing, Response.error("Key Salah"), Constant.LOG_METHOD_POST,
					Constant.LOG_SERVICE_NAME_SEND_FROM_EBS);
			return Response.error("Key Salah");
		}

	}

	public Boolean updateDataVendor(JSONArray jsonBody) {
		Boolean isError = false;
		Gson gson = new Gson();
		try {

			List<PostLoginVendorERPResponse> postLoginVendorERPResponseList = new ArrayList<PostLoginVendorERPResponse>();
			postLoginVendorERPResponseList = gson.fromJson(jsonBody.toString(),
					new TypeToken<List<PostLoginVendorERPResponse>>() {
					}.getType());
			PostLoginVendorERPResponse vendorInterfacing = postLoginVendorERPResponseList.get(0);
			for (PostLoginVendorDetailERPResponse vendorDetailInterfacing : vendorInterfacing.getData()) {

				Vendor vendor = vendorSession.getVendorIdEproc(vendorDetailInterfacing.getVendorNo());
				if (vendor != null) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					List<Role> roleList = roleSession.getRoleByKode(null, "PENYEDIA");
					User user = userSession.find(vendor.getUser());
					user.setNamaPengguna(vendorDetailInterfacing.getFullname());
					user.setEmail(vendorDetailInterfacing.getEmail());
					userSession.updateUser(user, null);
					vendor.setVendorIdEproc(vendorDetailInterfacing.getVendorNo());
					vendor.setNama(vendorDetailInterfacing.getFullname());
					vendor.setAlamat(vendorDetailInterfacing.getAddress1());
					vendor.setNomorTelpon(vendorDetailInterfacing.getPhoneNo());
					vendor.setEmail(vendorDetailInterfacing.getEmail());
					vendor.setNpwp(vendorDetailInterfacing.getTaxNo());
					roleUserSession.deletebyUserId(user.getId());
					Organisasi organisasi = organiasiSession.getByNama(vendorDetailInterfacing.getWilayahDescr());
					for (Role role : roleList) {
						RoleUser roleUser = new RoleUser();
						roleUser.setRole(role);
						roleUser.setUser(user);
						roleUser.setOrganisasi(organisasi);
						roleUserSession.insertRoleUser(roleUser);
					}
					String updDtString = vendorDetailInterfacing.getUpdDt();
					Date updDt = format.parse(updDtString);
					vendor.setTglRegistrasi(updDt);
					vendor.setKota(vendorDetailInterfacing.getCity());
					vendor.setProvinsi(vendorDetailInterfacing.getRegion());
					vendor.setAfco(organisasi);
					String dtStartString = vendorDetailInterfacing.getDtStart();
					String dtEndString = vendorDetailInterfacing.getDtEnd();

					if (!dtStartString.equalsIgnoreCase("") && !dtEndString.equalsIgnoreCase("")) {
						Date startDate = format.parse(dtStartString);
						Date endDate = format.parse(dtEndString);
						Date dateNow = new Date();
						if (dateNow.after(startDate) && dateNow.before(endDate)) {
							vendor.setStatus(3); // 3 Vendor Blacklist
						}
					}
					vendorSession.updateVendor(vendor, null);
					bankVendorSession.deleteBankVendorByVendorId(vendor.getId());
					segmentasiVendorSession.deleteSegmentasiVendorByVendorId(vendor.getId());

					// Maping Bank Vendor
					for (DataBankVendorERPResponse bankVendorInf : vendorDetailInterfacing
							.getVendorBankAccount()) {
						BankVendor bankVendor = new BankVendor();
						bankVendor.setNamaBank(bankVendorInf.getBankName());
						bankVendor.setNomorRekening(bankVendorInf.getBankNumber());
						bankVendor.setNamaNasabah(bankVendorInf.getBankOwnerName());
						bankVendor.setVendor(vendor);
						bankVendor.setUserId(vendor.getUser());
						bankVendorSession.insertBankVendor(bankVendor, null);
					}

					for (DataVendorProductsERPResponse bidangUsahaInf : vendorDetailInterfacing
							.getVendorProducts()) {
						SegmentasiVendor segmentasiVendor = new SegmentasiVendor();
						SubBidangUsaha subBidangUsaha = subBidangUsahaSession
								.getSubBidangUsahaByNama(bidangUsahaInf.getCommDesc()); // mesti confirm lagi maping
																							// nya
						segmentasiVendor.setSubBidangUsaha(subBidangUsaha);
						segmentasiVendor.setVendor(vendor);
						segmentasiVendor.setUserId(vendor.getUser());
						segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor, null);
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
	@Path("/getVendorListActive")
	@POST
	public Response getVendorListActive() throws SQLException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException {
		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EPROC + "/getVendorListActive";
		String externalId = "";
		List<GetVendorListActiveERPResponse> vendorListActiveERPResponses = new ArrayList<GetVendorListActiveERPResponse>();
		Gson gson = new Gson();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			ResponseObject responseObj = Utils.doPost(urlInterface);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			JSONObject jsonObj = new JSONObject(responseString);
			
			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray listData = jsonObj.getJSONArray("body");
				vendorListActiveERPResponses = gson.fromJson(listData.toString(), new TypeToken<List<GetVendorListActiveERPResponse>>() {
				}.getType());
				if (listData != null ) {
					for(GetVendorListActiveERPResponse getVendorListActiveERPResponse : vendorListActiveERPResponses) {
						List<DataVendorProductsERPResponse> vendorProductsERPResponses = new ArrayList<DataVendorProductsERPResponse>();
						List<DataBankVendorERPResponse> bankVendorERPResponses = new ArrayList<DataBankVendorERPResponse>();
						Vendor vendor = new Vendor();
						BlacklistVendor blacklistVendor = new BlacklistVendor();
						User user = new User();
						userTransaction.begin();
						vendorProductsERPResponses = getVendorListActiveERPResponse.getVendorProducts();
						bankVendorERPResponses = getVendorListActiveERPResponse.getVendorBankAccount();
						if (getVendorListActiveERPResponse.getVendorNo() != null) {
							// maping User
							user.setUsername(getVendorListActiveERPResponse.getVendorNo());
							user.setNamaPengguna(getVendorListActiveERPResponse.getFullname());
							user.setEmail(getVendorListActiveERPResponse.getEmail());
							Vendor checkVendor = vendorSession.getVendorIdEproc(getVendorListActiveERPResponse.getVendorNo());
							if (checkVendor != null) {
								User checkUser = userSession.getUser(checkVendor.getUser());
								if (checkUser != null) {
									user.setId(checkUser.getId());
									userSession.updateUser(user, null);
									}
								else {
									userSession.insertUser(user, null);
									}
								}
	
							// Maping Vendor
							vendor.setVendorIdEproc(getVendorListActiveERPResponse.getVendorNo());
							vendor.setNama(getVendorListActiveERPResponse.getFullname());
							vendor.setAlamat(getVendorListActiveERPResponse.getAddress1());
							vendor.setNomorTelpon(getVendorListActiveERPResponse.getPhoneNo());
							vendor.setEmail(getVendorListActiveERPResponse.getEmail());
							vendor.setNpwp(getVendorListActiveERPResponse.getTaxNo());
							Date updDt = dateFormat.parse(getVendorListActiveERPResponse.getUpdDt());
							vendor.setTglRegistrasi(updDt);
							vendor.setKota(getVendorListActiveERPResponse.getCity());
							vendor.setProvinsi(getVendorListActiveERPResponse.getRegion());
							vendor.setStatus(getVendorListActiveERPResponse.getIsActive());
							vendor.setVendorRowstamCode(getVendorListActiveERPResponse.getRowstamp());
							vendor.setVendorKlasifikasi(getVendorListActiveERPResponse.getDesc());
							if(!getVendorListActiveERPResponse.getIdEbs().equals("")) {
								vendor.setVendorIdEbs(Integer.parseInt(getVendorListActiveERPResponse.getIdEbs()));
							}
							RoleUser roleUser = roleUserSession.getByUserId(user.getId());
							Organisasi organisasi = organiasiSession.getByNama(getVendorListActiveERPResponse.getWilayahDesc());
							vendor.setAfco(organisasi);
							
							// Kondisi jika vendor masuk ke dalam list vendor blacklist
							if (!getVendorListActiveERPResponse.getDtStart().isEmpty() && !getVendorListActiveERPResponse.getDtEnd().isEmpty()) {
								Date startDate = dateFormat.parse(getVendorListActiveERPResponse.getDtStart());
								Date endDate = dateFormat.parse(getVendorListActiveERPResponse.getDtEnd());
								Date dateNow = new Date();
								blacklistVendor.setBlacklistVendorTglAwal(startDate);
								blacklistVendor.setBlacklistVendorTglAkhir(endDate);
								blacklistVendor.setBlacklistVendorUserId(vendor.getUser());
								blacklistVendor.setBlacklistVendorId(vendor.getId());
								if (dateNow.after(startDate) && dateNow.before(endDate)) {
									String blacklistVendorTable= "BlacklistVendor";	
									vendor.setStatus(3); // 3 Vendor Blacklist
									blacklistVendorSession.deleteAllBlacklistVendor(blacklistVendorTable);
									blacklistVendorSession.createBlacklistVendor(blacklistVendor, null);
								}
							}
							
							// update or create
							if (checkVendor != null) {// Kondisi jika data vendor lama sudah ada di promise (Update Data Vendor)
								vendor.setUser(user.getId());
								vendor.setId(checkVendor.getId());
								vendorSession.updateVendor(vendor, null);
	//							bankVendorSession.deleteBankVendorByVendorId(checkVendor.getId());
								segmentasiVendorSession.deleteSegmentasiVendorByVendorId(checkVendor.getId());
							} else {// Kondisi jika data vendor belum ada di promise (Vendor Baru)
								userSession.insertUser(user, null);
								vendor.setUser(user.getId());
								vendorSession.insertVendor(vendor, null);
							}
							
							/* mapping Bank Vendor(insert or delete) */
							ArrayList<String> tempNoRekList = new ArrayList<String>();
							if (getVendorListActiveERPResponse != null && bankVendorERPResponses != null) {
								for (DataBankVendorERPResponse dataBankVendorERPResponse : bankVendorERPResponses) {
									BankVendor bankVendorNew = new BankVendor();
									tempNoRekList.add(dataBankVendorERPResponse.getBankNumber());
									List<BankVendor> bankVendorTemp = bankVendorSession.getListBankVendorByBankNumber(
											dataBankVendorERPResponse.getBankNumber(), vendor.getId());
									if (bankVendorTemp.size() > 0) {
										bankVendorNew = bankVendorTemp.get(0);
										bankVendorNew.setNamaBank(dataBankVendorERPResponse.getBankName());
										bankVendorNew.setNomorRekening(dataBankVendorERPResponse.getBankNumber());
										bankVendorNew.setNamaNasabah(dataBankVendorERPResponse.getBankOwnerName());
										bankVendorNew.setVendor(vendor);
										bankVendorNew.setUserId(user.getId());

										bankVendorSession.updateBankVendor(bankVendorNew, null);
									} else {
										bankVendorNew.setNamaBank(dataBankVendorERPResponse.getBankName());
										bankVendorNew.setNomorRekening(dataBankVendorERPResponse.getBankNumber());
										bankVendorNew.setNamaNasabah(dataBankVendorERPResponse.getBankOwnerName());
										bankVendorNew.setVendor(vendor);
										bankVendorNew.setUserId(user.getId());

										bankVendorSession.insertBankVendor(bankVendorNew, null);
									}
								}
								/* delete when data in bank vendor is not same with bank vendor interfacing */
								List<BankVendor> bankVendorList = bankVendorSession
										.getBankVendorByVendorId(vendor.getId());
								for (BankVendor bankVendor : bankVendorList) {
									if (!tempNoRekList.toString().contains(bankVendor.getNomorRekening())) {
										bankVendor.setIsDelete(1);
										bankVendorSession.updateBankVendor(bankVendor, null);
									}
								}
							}
							
	
							// Maping Segmentasi Vendor
							if (getVendorListActiveERPResponse.getVendorProducts() != null) {
								for (DataVendorProductsERPResponse dataVendorProductsERPResponse : vendorProductsERPResponses) {
									SegmentasiVendor segmentasiVendor = new SegmentasiVendor();
									SubBidangUsaha subBidangUsaha = subBidangUsahaSession.getSubBidangUsahaByCode(dataVendorProductsERPResponse.getCommodity());
									segmentasiVendor.setVendor(vendor);
									segmentasiVendor.setUserId(user.getId());
									
									if (subBidangUsaha != null) {
										segmentasiVendor.setSubBidangUsaha(subBidangUsaha);
										subBidangUsaha.setNama(dataVendorProductsERPResponse.getCommDesc());
										subBidangUsahaSession.updateSubBidangUsaha(subBidangUsaha, null);
									}
									else {
										SubBidangUsaha subBidangUsaha2 = new SubBidangUsaha();
										subBidangUsaha2.setSubBidangUsahaCode(dataVendorProductsERPResponse.getCommodity());
										subBidangUsaha2.setNama(dataVendorProductsERPResponse.getCommDesc());
										subBidangUsahaSession.insertSubBidangUsaha(subBidangUsaha2, null);
									}
									segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor, null);
									
								}
							}
						}
						else {
							String cause = "No Data Found";
							syncSession.create(urlInterface, null, Response.error(srcRespDesc, responseString), Constant.LOG_METHOD_POST,
									Constant.LOG_SERVICE_NAME_GET_USER, externalId);
							return Response.error(srcRespDesc);
						}
						userTransaction.commit();
						procedureSession.execute(Constant.SYNC_IN_UP_VEND_CAT_TO_CM, vendor.getId());
					}
				}
				// create log
				syncSession.create(urlInterface, null, Response.ok(responseString), Constant.LOG_METHOD_POST,
						Constant.LOG_SERVICE_NAME_GET_USER, externalId);
				return Response.ok("SUCCESS");
			}
			else {
				syncSession.create(urlInterface, null, Response.error("Error : " + srcRespDesc + " , " + responseString),
						Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_GET_USER, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
		} catch (Exception e) {
			// create log
			syncSession.create(urlInterface, null, Response.error(e), Constant.LOG_METHOD_POST,
					Constant.LOG_SERVICE_NAME_GET_USER, externalId);
			userTransaction.commit();
			return Response.error(e);
		}

	}
	
	public void test (VendorInterfacingExpose vendorInterfacingExpose) {
		Gson gson = new Gson();
		List<BankVendorInterfacingExpose> bankVendorInterfacingExposeList = new ArrayList<BankVendorInterfacingExpose>();
		List<BidangUsahaInterfacingExpose> bidangUsahaInterfacingExposeList = new ArrayList<BidangUsahaInterfacingExpose>();
		bankVendorInterfacingExposeList = vendorInterfacingExpose.getBankVendorList();
		bidangUsahaInterfacingExposeList = vendorInterfacingExpose.getBidangUsahaList();

		Vendor vendor = new Vendor();
		BlacklistVendor blacklistVendor = new BlacklistVendor();
		User user = new User();
		if (vendorInterfacingExpose != null) {
			// maping User
			user.setUsername(vendorInterfacingExpose.getVendorNo());
			user.setNamaPengguna(vendorInterfacingExpose.getFullname());
			user.setEmail(vendorInterfacingExpose.getEmail());
			Vendor checkVendor = vendorSession.getVendorIdEproc(vendorInterfacingExpose.getVendorNo());
			if (checkVendor != null) {
				User checkUser = userSession.getUser(checkVendor.getUser());
				user.setId(checkUser.getId());
				userSession.updateUser(user, null);
			}

			// Maping Vendor
			vendor.setVendorIdEproc(vendorInterfacingExpose.getVendorNo());
			vendor.setNama(vendorInterfacingExpose.getFullname());
			vendor.setAlamat(vendorInterfacingExpose.getAddress1());
			vendor.setNomorTelpon(vendorInterfacingExpose.getPhoneNo());
			vendor.setEmail(vendorInterfacingExpose.getEmail());
			vendor.setNpwp(vendorInterfacingExpose.getTaxNo());
			//vendor.setTglRegistrasi(vendorInterfacingExpose.getUpdDt());
			vendor.setKota(vendorInterfacingExpose.getCity());
			vendor.setProvinsi(vendorInterfacingExpose.getRegion());
//						vendor.setStatus(vendorInterfacingExpose.getIsActive());
			RoleUser roleUser = roleUserSession.getByUserId(user.getId());
			Organisasi organisasi = organiasiSession.getByNama(vendorInterfacingExpose.getWilayahDescr());
			vendor.setAfco(organisasi);
			Date startDate = new Date();//vendorInterfacingExpose.getDtStart();
			Date endDate = new Date();//vendorInterfacingExpose.getDtEnd();

			// Kondisi jika vendor masuk ke dalam list vendor blacklist
			if (startDate != null && endDate != null) {
				Date dateNow = new Date();
				blacklistVendor.setBlacklistVendorTglAwal(startDate);
				blacklistVendor.setBlacklistVendorTglAkhir(endDate);
				blacklistVendor.setBlacklistVendorUserId(vendor.getUser());
				blacklistVendor.setBlacklistVendorId(vendor.getId());
				if (dateNow.after(startDate) && dateNow.before(endDate)) {
					vendor.setStatus(3); // 3 Vendor Blacklist
				}
			}

			// update or create
			if (checkVendor != null) {// Kondisi jika data vendor lama sudah ada di promise (Update Data Vendor)
				vendor.setUser(user.getId());
				vendor.setId(checkVendor.getId());
				vendorSession.updateVendor(vendor, null);

				/* mapping Bank Vendor(insert or delete) */
				ArrayList<String> tempNoRekList = new ArrayList<String>();
				if (vendorInterfacingExpose != null && bankVendorInterfacingExposeList != null) {
					for (BankVendorInterfacingExpose bankVendorInterfacingExpose : bankVendorInterfacingExposeList) {
						BankVendor bankVendorNew = new BankVendor();
						tempNoRekList.add(bankVendorInterfacingExpose.getBankNumber());
						List<BankVendor> bankVendorTemp = bankVendorSession.getListBankVendorByBankNumber(
								bankVendorInterfacingExpose.getBankNumber(), vendor.getId());
						if (bankVendorTemp.size() > 0) {
							bankVendorNew = bankVendorTemp.get(0);
							bankVendorNew.setNamaBank(bankVendorInterfacingExpose.getBankName());
							bankVendorNew.setNomorRekening(bankVendorInterfacingExpose.getBankNumber());
							bankVendorNew.setNamaNasabah(bankVendorInterfacingExpose.getBankOwnerName());
							bankVendorNew.setVendor(vendor);
							bankVendorNew.setUserId(user.getId());

							bankVendorSession.updateBankVendor(bankVendorNew, null);
						} else {
							bankVendorNew.setNamaBank(bankVendorInterfacingExpose.getBankName());
							bankVendorNew.setNomorRekening(bankVendorInterfacingExpose.getBankNumber());
							bankVendorNew.setNamaNasabah(bankVendorInterfacingExpose.getBankNumber());
							bankVendorNew.setVendor(vendor);
							bankVendorNew.setUserId(user.getId());

							bankVendorSession.insertBankVendor(bankVendorNew, null);
						}
					}
					/* delete when data in bank vendor is not same with bank vendor interfacing */
					List<BankVendor> bankVendorList = bankVendorSession
							.getBankVendorByVendorId(vendor.getId());
					for (BankVendor bankVendor : bankVendorList) {
						if (!tempNoRekList.toString().contains(bankVendor.getNomorRekening())) {
							bankVendor.setIsDelete(1);
							bankVendorSession.updateBankVendor(bankVendor, null);
						}
					}
				}

//							bankVendorSession.deleteBankVendorByVendorId(checkVendor.getId());
				segmentasiVendorSession.deleteSegmentasiVendorByVendorId(checkVendor.getId());
				blacklistVendorSession.createBlacklistVendor(blacklistVendor, null);
			} else {// Kondisi jika data vendor belum ada di promise (Vendor Baru)
				userSession.insertUser(user, null);
				vendor.setUser(user.getId());
				vendorSession.insertVendor(vendor, null);
			}

			// Maping Segmentasi Vendor
			if (vendorInterfacingExpose.getBidangUsahaList() != null) {
				for (BidangUsahaInterfacingExpose bidangUsahaInterfacingExpose : bidangUsahaInterfacingExposeList) {
					SegmentasiVendor segmentasiVendor = new SegmentasiVendor();
					segmentasiVendor.setSubBidangUsaha(
							subBidangUsahaSession.getSubBidangUsahaByNama(bidangUsahaInterfacingExpose.getGroupId()));
					segmentasiVendor.setVendor(vendor);
					segmentasiVendor.setUserId(user.getId());
					segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor, null);
				}
			}
		}
	}

}
