/*package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.UploadPurchaseRequest;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.AfcoSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.TermAndConditionSession;
import id.co.promise.procurement.purchaserequest.create.PRCreateParam;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.BuatParsingJSONDateTypeAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@Stateless
@WebService(serviceName="PurchaseRequest", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaserequest.PurchaseRequestWSDLImpl")
public class PurchaseRequestWSDLServices implements PurchaseRequestWSDLImpl{
	final static Logger log = Logger.getLogger(PurchaseRequestWSDLServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	final Gson gson;

	public PurchaseRequestWSDLServices() {
		// BuatParsingJSONDateTypeAdapter
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new BuatParsingJSONDateTypeAdapter());
		gson = builder.create();
	}

	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;
	@EJB
	private TokenSession tokenSession;
	@EJB
	private AfcoSession afcoSession;
	@EJB
	private TermAndConditionSession termAndConditionSession;
	@EJB
	private RoleUserSession roleUserSession;
	@EJB
	private TahapanSession tahapanSession;
	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;
	@EJB
	private ApprovalProcessSession approvalProcessSession;
	@EJB
	private ApprovalLevelSession approvalLevelSession;
	@EJB
	private ApprovalSession approvalSession;
	@EJB
	private OrganisasiSession organisasiSession;
	@EJB
	private UserSession userSession;
	@EJB
	private ItemSession itemSession;
	@EJB
	private AddressBookSession addressBookSession;
	@EJB
	private UploadPurchaseRequestSession uploadPurchaseRequestSession;
	@EJB
	private ShippingToPRSession shippingToPRSession;
	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public List<PurchaseRequest> getList() {
		return purchaseRequestSession.getList();
	}
	
	public PurchaseRequest getPurchaseRequestById(int id) {
		return purchaseRequestSession.get(id);
	}
	
	public int countByStatus( int status,String token) {
		return purchaseRequestSession.countByStatus(status, tokenSession.findByToken(token));
	}
	
	public int countAll(String token) {
		return purchaseRequestSession.countAll(tokenSession.findByToken(token));
	}
	
	public PurchaseRequest insert( String prnumber,  String department,  int departmentId,
			 String costcenter,  String totalcost, String daterequired,  String approvalId,
			 boolean approvalIsNew, int approvalUser, int apparovalOrganisasi,
			 String nextapproval,  String procurementprocess,  String termandcondition,
			 String description, String tahapanId, String fileuploadList,Integer isJoin, String token) {

		Token tkn = tokenSession.findByToken(token);
		PurchaseRequest pr = new PurchaseRequest();
		pr.setPrnumber(prnumber);
		pr.setDepartment(department);
		pr.setCostcenter(costcenter);
		pr.setTotalcost(totalcost);
		try {
			pr.setDaterequired(sdf.parse(daterequired));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		pr.setAfco(afcoSession.findByOrganisasiId(departmentId));
		pr.setTermandcondition(termandcondition);
		pr.setProcurementprocess(procurementprocess);
		pr.setDescription(description);
		pr.setCreated(new Date());
		pr.setUserId(0);
		pr.setNextapproval(nextapproval);
		pr.setStatus(3);
		pr.setIsJoin(isJoin);
		pr = purchaseRequestSession.insert(pr, tkn);
		ApprovalProcessType apt = new ApprovalProcessType();
		apt.setValueId(pr.getId());
		apt.setTahapan(tahapanSession.find(Integer.parseInt(tahapanId))); // 26 purchase request
		if (!approvalIsNew)
			apt.setApproval(approvalSession.find(Integer.parseInt(approvalId)));
		apt = approvalProcessTypeSession.create(apt, tkn);
		if (!approvalIsNew) { // jika approval menggunakan template
			List<ApprovalLevel> aplList = approvalLevelSession.findByApproval(approvalSession.find(Integer.parseInt(approvalId)));
			for (ApprovalLevel apl : aplList) {
				ApprovalProcess ap = new ApprovalProcess();
				ap.setApprovalProcessType(apt);
				ap.setApprovalLevel(apl);
				if (apl.getSequence() == 1) {
					ap.setStatus(1); // set status = aktif
				} else {
					ap.setStatus(0); // set status = non aktif (0)
				}
				// ap.setKeterangan();
				ap.setGroup(apl.getGroup());
				ap.setUser(apl.getUser());
				// ap.setRole(apl.getRole());
				ap.setThreshold(apl.getThreshold());
				ap.setSequence(apl.getSequence());
				approvalProcessSession.create(ap, tkn);
			}
		} else {
			ApprovalProcess ap = new ApprovalProcess();
			ap.setApprovalProcessType(apt);
			ap.setStatus(1); // set status = aktif
			ap.setGroup(organisasiSession.find(apparovalOrganisasi));
			ap.setUser(userSession.find(approvalUser));
			ap.setSequence(1);
			approvalProcessSession.create(ap, tkn);
		}

		 UploadFileList 
		log.info("fileuploadList = " + fileuploadList.toString());
		JsonElement jelement = new JsonParser().parse(fileuploadList);
		JsonArray fileUploadArray = jelement.getAsJsonArray();
		log.info("fileUploadArray = " + fileUploadArray.toString());

		for (int i = 0; i < fileUploadArray.size(); i++) {
			UploadPurchaseRequest uploadPurchaseRequest = new UploadPurchaseRequest();
			JsonObject obj = fileUploadArray.get(i).getAsJsonObject();
			if (obj.get("fileRealName") != null) {
				uploadPurchaseRequest.setUploadPrRealName(obj.get("fileRealName").getAsString());
				uploadPurchaseRequest.setUploadPrFileName(obj.get("fileName").getAsString());
				uploadPurchaseRequest.setUploadPRFileSize(obj.get("fileSize").getAsLong());
				uploadPurchaseRequest.setPurchaseRequest(pr);
				uploadPurchaseRequestSession.insert(uploadPurchaseRequest, tkn);
			}
		}

		return pr;
	}
	
	public PurchaseRequest update(Integer id, String prnumber,  String department,  int departmentId,
			 String costcenter,String totalcost,  String daterequired,
			 boolean approvalIsChange, int approvalProcessTypeId,  String approvalId,
			 boolean approvalIsNew,  int approvalUser,  int apparovalOrganisasi,
			 String nextapproval, String procurementprocess, String termandcondition,
			 String description,  String tahapanId,  String fileuploadList,  Integer isJoin,  String token) {
		Token tkn = tokenSession.findByToken(token);
		PurchaseRequest pr = purchaseRequestSession.find(id);
		pr.setPrnumber(prnumber);
		pr.setDepartment(department);
		pr.setCostcenter(costcenter);
		pr.setTotalcost(totalcost);
		try {
			pr.setDaterequired(sdf.parse(daterequired));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		pr.setAfco(afcoSession.findByOrganisasiId(departmentId));
		pr.setTermandcondition(termandcondition);
		pr.setProcurementprocess(procurementprocess);
		pr.setDescription(description);
		pr.setUpdated(new Date());
		pr.setUserId(tkn.getUser().getId());
		pr.setNextapproval(nextapproval);
		pr.setStatus(3);
		pr.setIsJoin(isJoin);
		purchaseRequestSession.update(pr, tkn);
		if (approvalIsChange) {
			// approvalProcessTypeId jika 0, berarti data approval sebelumnya
			// belum ada, jadi tidak perlu dihapus (*soft delete)
			if (approvalProcessTypeId > 0) {
				// hapus approval process type awal
				approvalProcessTypeSession.delete(approvalProcessTypeSession.find(approvalProcessTypeId), tkn);
				// hapus approval process
				approvalProcessSession.deleteByApprovalProcessType(approvalProcessTypeId, tkn);
			}
			// create new approval
			ApprovalProcessType apt = new ApprovalProcessType();
			apt.setValueId(pr.getId());
			apt.setTahapan(tahapanSession.find(Integer.parseInt(tahapanId))); // 26
																				// purchase
																				// request
			if (!approvalIsNew)
				apt.setApproval(approvalSession.find(Integer.parseInt(approvalId)));
			apt = approvalProcessTypeSession.create(apt, tkn);
			if (!approvalIsNew) { // jika approval menggunakan template
				List<ApprovalLevel> aplList = approvalLevelSession.findByApproval(approvalSession.find(Integer.parseInt(approvalId)));
				for (ApprovalLevel apl : aplList) {
					ApprovalProcess ap = new ApprovalProcess();
					ap.setApprovalProcessType(apt);
					ap.setApprovalLevel(apl);
					if (apl.getSequence() == 1) {
						ap.setStatus(1); // set status = aktif
					} else {
						ap.setStatus(0); // set status = non aktif (0)
					}
					// ap.setKeterangan();
					ap.setGroup(apl.getGroup());
					ap.setUser(apl.getUser());
					// ap.setRole(apl.getRole());
					ap.setThreshold(apl.getThreshold());
					ap.setSequence(apl.getSequence());
					approvalProcessSession.create(ap, tkn);
				}
			} else {
				ApprovalProcess ap = new ApprovalProcess();
				ap.setApprovalProcessType(apt);
				ap.setStatus(1); // set status = aktif
				ap.setGroup(organisasiSession.find(apparovalOrganisasi));
				ap.setUser(userSession.find(approvalUser));
				ap.setSequence(1);
			}
		}

		uploadPurchaseRequestSession.deleteRowByByPurchaseRequest(pr.getId());

		 UploadFileList 
		log.info("fileuploadList = " + fileuploadList.toString());
		JsonElement jelement = new JsonParser().parse(fileuploadList);
		JsonArray fileUploadArray = jelement.getAsJsonArray();
		log.info("fileUploadArray = " + fileUploadArray.toString());

		for (int i = 0; i < fileUploadArray.size(); i++) {
			UploadPurchaseRequest uploadPurchaseRequest = new UploadPurchaseRequest();
			JsonObject obj = fileUploadArray.get(i).getAsJsonObject();
			if (obj.get("fileRealName") != null) {
				uploadPurchaseRequest.setUploadPrRealName(obj.get("fileRealName").getAsString());
				uploadPurchaseRequest.setUploadPrFileName(obj.get("fileName").getAsString());
				uploadPurchaseRequest.setUploadPRFileSize(obj.get("fileSize").getAsLong());
				uploadPurchaseRequest.setPurchaseRequest(pr);
				uploadPurchaseRequestSession.insert(uploadPurchaseRequest, tkn);
			}
		}

		return pr;
	}
	
	public PurchaseRequest delete(int id, String token) {
		PurchaseRequest pr = new PurchaseRequest();
		boolean deleteOk = purchaseRequestItemSession.deletePurchaseRequestItemByPrId(id, tokenSession.findByToken(token));
		if(deleteOk) {
			pr = purchaseRequestSession.delete(id, tokenSession.findByToken(token));
		}
		return pr;
	}
	
	public PurchaseRequest deleteRow(int id, String token) {
		return purchaseRequestSession.deleteRow(id, tokenSession.findByToken(token));
	}
	
	public Response getPurchaseRequestListByPRNumberWithPagination(
			Integer pageNumber, 
			Integer numberOfRowPerPage,
			String searchingKeyWord,
			Integer joinStatus,
			String token) {
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
				return Response.status(201).entity(purchaseRequestSession.getListPRinRange(startIndex, endIndex, joinStatus, tokenSession.findByToken(token))).build();
			} else {
				return Response.status(201).entity(purchaseRequestSession.getListPRinRangeByPRNumber(searchingKeyWord, startIndex, endIndex, joinStatus, tokenSession.findByToken(token))).build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public Response getPurchaseRequestVerificationListByPRNumberWithPagination(Integer pageNumber, Integer numberOfRowPerPage,
			String searchingKeyWord, String token) {
		try {
			if (pageNumber == null || numberOfRowPerPage == null || pageNumber == 0 || numberOfRowPerPage == 0) {
				pageNumber = 1;
				numberOfRowPerPage = 10;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}
		try {
			Integer endIndex = pageNumber * numberOfRowPerPage;
			Integer startIndex = endIndex - numberOfRowPerPage + 1;
			if (searchingKeyWord == null || searchingKeyWord.trim().equals("")) {
				return Response.status(201).entity(purchaseRequestSession.getListPRVerificationinRange(startIndex, endIndex, tokenSession.findByToken(token))).build();
			} else {
				return Response.status(201).entity(purchaseRequestSession.getListPRVerificationinRangeByPRNumber(searchingKeyWord, startIndex, endIndex, tokenSession.findByToken(token))).build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
	
	public Response indexPageList(String keywordSearch, int pageNo, int pageSize, String orderKeyword) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jmlData", purchaseRequestSession.getTotalList(keywordSearch));
		map.put("dataList", purchaseRequestSession.getPagingList(keywordSearch, pageNo, pageSize, orderKeyword));
		return Response.ok(map).build();
	}
	
	public Response createPurchaseRequest(String jsonString, String token) {
		PRCreateParam prc;
		try {
			prc = gson.fromJson(jsonString, PRCreateParam.class);
			// log.info("JSONSTRING = " + gson.toJson(prc));
		} catch (Exception e) {
			log.error(e.getMessage() + " | " + jsonString, e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}
		try {
			return Response.status(201).entity(purchaseRequestSession.createPR(prc, tokenSession.findByToken(token))).build();
			// return
			// Response.status(500).entity(message.info(gson.toJson(prc)))
			// .build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public PurchaseRequest updateStatusApproval(Integer id, String nextApproval, Integer status,
			String token) {
		Token tkn = tokenSession.findByToken(token);
		PurchaseRequest pr = purchaseRequestSession.find(id);
		// find next approval
		ApprovalProcessType approvalProcessType = approvalProcessTypeSession.findByTahapanAndValueId(26, id); // 26=purchase
																												// request
		List<ApprovalProcess> approvalProcessList = approvalProcessSession.findByApprovalProcessType(approvalProcessType.getId());
		int index = 0;
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
					break;
				}
			}
			index++;
		}
		// jika sudah diapprove semua
		if (index == approvalProcessList.size()) {
			status = 8;// 8 = Need Verification
			nextApproval = "";
		}
		// update pr
		pr.setNextapproval(nextApproval);
		pr.setStatus(status);
		pr.setUpdated(new Date());
		PurchaseRequest prq = purchaseRequestSession.update(pr, tkn);
		return prq;
	}
	
	public List<PurchaseRequest> getVerifiedPurchaseRequest(String token) {
		return purchaseRequestSession.getVerifiedPurchaseRequest(tokenSession.findByToken(token));
	}
	
	public List<PurchaseRequest> getVerifiedPurchaseRequestByNumber(String keyword, String token) {
		return purchaseRequestSession.getVerifiedPurchaseRequestByNumber(keyword);
	}
	
	public Response insertPRJoin(
			String prnumber,
			String totalcost, 
			 Date daterequired,
			String description,
			String itemsId,
			Double qtyJoin,
			String token) {
		
		Token tkn = tokenSession.findByToken(token);
		Integer userId = tkn.getUser().getId();
		//Find Organisasi by User from Token
		Organisasi org = new Organisasi();
		//1. Role User
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(userId);
		if(roleUserList != null && roleUserList.size() > 0) {
			//2. Find Organisasi for Departemen setting
			org = organisasiSession.getOrganisasi(roleUserList.get(0).getOrganisasi().getId());
		}
		PurchaseRequest prJoin = new PurchaseRequest();
		
		try {
			List<Integer> itemList = new ArrayList<Integer>();
			if(itemsId != null) {
				String[] parts = itemsId.split(",");
				for(int i=0;i<parts.length;i++) {
					itemList.add(Integer.valueOf(parts[i].trim()));
				}
			}
			
			//Save PR JOIN
			prJoin.setPrnumber(prnumber);
			prJoin.setTotalcost(totalcost);
			prJoin.setDaterequired(daterequired);
			
			List<Afco> afcoList = afcoSession.getAfcoByOrganisasiUserId(userId);
			if(afcoList != null && afcoList.size() > 0) {
				prJoin.setAfco(afcoList.get(0));
			}
			
			prJoin.setDepartment(org.getNama());
			prJoin.setDescription(description);
			prJoin.setStatus(9);
			prJoin.setIsJoin(2); // IsJoin = 1, NotIsJoin = 0, WasJoin = 2
			prJoin.setTermandcondition("JOIN PR from -"+itemList.size()+"- Item");
			prJoin = purchaseRequestSession.insert(prJoin, tkn);
			
			//Update PR Item
			if(itemsId != null) {
				
				List<Object[]> arrObjList = purchaseRequestItemSession.getPurchaseRequestItemListForJoinPr(itemList);
				List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
				if(arrObjList != null && arrObjList.size() > 0) {
					for (Object[] obj : arrObjList) {
						PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
						purchaseRequestItem.setKode((String) obj[0]);
						purchaseRequestItem.setQtyafterjoin((Double) obj[2]);
						purchaseRequestItem.setPrice((Double) obj[4]);

						Item item = new Item();
						item.setId((Integer) obj[6]);
						item.setKode((String) obj[0]);
						item.setNama((String) obj[1]);

						purchaseRequestItem.setItem(item);

						purchaseRequestItemList.add(purchaseRequestItem);
					}
				}
				
				for(int i=0;i<itemList.size();i++) {
					Integer itemId = itemList.get(i);
					
					List<PurchaseRequestItem> prItemList = purchaseRequestItemSession.getPurchaseRequestItemVerifiedListByItem(itemId);
					if(prItemList != null && prItemList.size() > 0) {
						for(PurchaseRequestItem prItem : prItemList) {
							for(PurchaseRequestItem bandingPRItem : purchaseRequestItemList) {
								if(prItem.getKode().equals(bandingPRItem.getKode()) && prItem.getItem().getId() == bandingPRItem.getItem().getId()) {
									prItem.setPurchaserequestjoin(prJoin);
									prItem.setPriceafterjoin(bandingPRItem.getPrice());
									prItem.setQtyafterjoin(bandingPRItem.getQtyafterjoin());
									try {
										purchaseRequestItemSession.updatePurchaseRequestItem(prItem, tkn);
									} catch (Exception e) {
										log.error(e.getMessage());
										// kirim 400 Bad Request
										return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
		
		return Response.ok(prJoin).build();
	}

	public PurchaseRequest deleteJoin(int id, String token) {
		purchaseRequestItemSession.deletePurchaseRequestItemJoinByPrId(id, tokenSession.findByToken(token));
		return purchaseRequestSession.delete(id, tokenSession.findByToken(token));
	}
}
*/