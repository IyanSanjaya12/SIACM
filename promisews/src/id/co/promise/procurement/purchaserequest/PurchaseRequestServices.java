package id.co.promise.procurement.purchaserequest;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.HttpServletRequest;
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

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import erp.interfacing.entity.GetInvoicePaymentERPRequest;
import erp.service.InvoicePaymentInterfacingService;
import erp.service.PurchaseRequestInterfacingService;
import id.co.promise.procurement.DTO.BookingOrderDTO;
import id.co.promise.procurement.DTO.BookingOrderItemDTO;
import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessServices;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.approval.ApprovalTahapanSession;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogBulkPrice;
import id.co.promise.procurement.catalog.entity.CatalogContractDetail;
import id.co.promise.procurement.catalog.entity.CatalogFee;
import id.co.promise.procurement.catalog.session.CatalogBulkPriceSession;
import id.co.promise.procurement.catalog.session.CatalogContractDetailSession;
import id.co.promise.procurement.catalog.session.CatalogFeeSession;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.CostCenterSap;
import id.co.promise.procurement.entity.GLAccountSap;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.OrgApprovalPath;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.PurchaseRequestPengadaan;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.UploadPurchaseRequest;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.inisialisasi.PurchaseRequestPengadaanSession;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.AfcoSession;
import id.co.promise.procurement.master.ApprovalProcessVendorService;
import id.co.promise.procurement.master.AutoNumberSession;
import id.co.promise.procurement.master.CatalogDTO;
import id.co.promise.procurement.master.CostCenterSapSession;
import id.co.promise.procurement.master.GLAccountSapSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.OrgApprovalPathSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.RequestQtyDTO;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.TermAndConditionSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.BuatParsingJSONDateTypeAdapter;
import id.co.promise.procurement.utils.Constant;

@Stateless
@Path("/procurement/purchaseRequestServices")
@TransactionManagement(TransactionManagementType.BEAN)
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseRequestServices {
	final static Logger log = Logger.getLogger(PurchaseRequestServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	final Gson gson;
	
	

	public PurchaseRequestServices() {
		// BuatParsingJSONDateTypeAdapter
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new BuatParsingJSONDateTypeAdapter());
		gson = builder.create();
	}
	
	@Resource
	private UserTransaction userTransaction;
	
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;
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
	@EJB
	private AutoNumberSession autoNumberSession;
	@EJB
	private PurchaseRequestPengadaanSession purchaseRequestPengadaanSession;
	@EJB
	private CatalogSession catalogSession;
	@EJB
	private OrgApprovalPathSession orgApprovalPathSession;
	@EJB
	private ApprovalProcessVendorService approvalProcessVendorService;
	@EJB
	private InvoicePaymentInterfacingService invoicePaymentInterfacingService;
	@EJB
	private PurchaseRequestInterfacingService purchaseRequestInterfacingService;
	@EJB
	ApprovalTahapanSession approvalTahapanSession;
	@EJB
	EmailNotificationSession emailNotificationSession;
	@EJB
	CatalogFeeSession catalogFeeSession;
	@EJB
	ApprovalProcessServices approvalProcessService;
	
	@EJB
	private CatalogBulkPriceSession catalogBulkPriceSession;
	
	@EJB
	GLAccountSapSession gLAccountSapSession;
	
	@EJB
	CostCenterSapSession costCenterSapSession;
	
	@EJB
	CatalogContractDetailSession catalogContractDetailSession;
	
	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Path("/getList")
	@GET
	public List<PurchaseRequest> getList() {
		return purchaseRequestSession.getList();
	}

	@Path("/getPurchaseRequestId/{id}")
	@GET
	public PurchaseRequest getId(@PathParam("id") int id) {
		return purchaseRequestSession.get(id);
	}

	@Path("/countByStatus/{status}")
	@GET
	public int countByStatus(@PathParam("status") int status, @HeaderParam("Authorization") String token) {
		return purchaseRequestSession.countByStatus(status, tokenSession.findByToken(token));
	}

	@Path("/countAll")
	@GET
	public int countAll(@HeaderParam("Authorization") String token) {
		return purchaseRequestSession.countAll(tokenSession.findByToken(token));
	}

	@Path("/getprnumber")
	@GET
	public Response getPrNumber(@HeaderParam("Authorization") String token) {
		Map<String, String> mapDTO = new HashMap<String, String>();
		mapDTO.put("pr", autoNumberSession.generateNumber("PR", tokenSession.findByToken(token)));
		return Response.ok(mapDTO).build();
	}
	
	@Path("/getBONumber")
	@GET
	public Response getBONumber(@HeaderParam("Authorization") String token) {
		Map<String, String> mapDTO = new HashMap<String, String>();
		mapDTO.put("co", autoNumberSession.generateNumber("CO", tokenSession.findByToken(token)));
		return Response.ok(mapDTO).build();
	}
	
	@Path("/getBONumberByOrganisasiId")
	@GET
	public Response getBONumberByOrganisasiId(@HeaderParam("Authorization") String token) {
		Map<String, String> mapDTO = new HashMap<String, String>();
		mapDTO.put("co", autoNumberSession.generateNumberByOrganisasiId("PR", tokenSession.findByToken(token)));
		return Response.ok(mapDTO).build();
	}

	@Path("/getprnumbervalidation")
	@POST
	public Response comparePrNumber(@FormParam("prnumber") String prNumber,
			@HeaderParam("Authorization") String token) {
		Map<String, Boolean> mapDTO = new HashMap<String, Boolean>();
		mapDTO.put("isnotexist", purchaseRequestSession.comparePrNumber(prNumber, tokenSession.findByToken(token)));
		return Response.ok(mapDTO).build();
	}

	@SuppressWarnings({ "unused"})
	@Path("/create")
	@POST
	public Response create(BookingOrderDTO bookingOrderDTO, @HeaderParam("Authorization") String token) throws SQLException, Exception {

		User user = tokenSession.findByToken(token).getUser();
		Token tokenObj = tokenSession.findByToken(token);
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(tokenObj);
		
		Double tempDiscount = new Double(0);
		//Double tempAsuransi = new Double(0);
		Double totalHargaNormal, hargaDiskon;
		Map<Object, Object> map = new HashMap<Object, Object>();
		List <String> allEmail = new ArrayList<String>();
		
		//Check Approval Tahapan
		Tahapan tahapan = tahapanSession.get(Constant.ONE_VALUE);
		ApprovalTahapan approvalTahapan = approvalTahapanSession.getApprovalTahapanByTahapanAndOrganisasi(tahapan, organisasi);
		if (approvalTahapan == null) {
			map.put("response", "NOT_FOUND");
			return Response.ok(map).build();
		}else {
			map.put("response", "SUCCESS");
		}
		PurchaseRequest purchaseRequest = new PurchaseRequest();
		try {
			userTransaction.begin();
			
			purchaseRequest.setStatus(1);
			purchaseRequest.setTermandcondition("-");
			purchaseRequest.setTotalHarga(bookingOrderDTO.getTotalHarga());
			purchaseRequest.setTotalHargaOngkir(bookingOrderDTO.getTotalOngkir());
			//purchaseRequest.setTotalHargaAsuransi(tempAsuransi);
			purchaseRequest.setTotalHargaDiscount(tempDiscount);
			purchaseRequest.setUserId(user.getId());
			purchaseRequest.setOrganisasi(organisasi);
			purchaseRequest.setDepartment(organisasi.getNama());
			purchaseRequest.setBoNumber(bookingOrderDTO.getBoNumber());
	
			purchaseRequest.setOrgApprovalPath(bookingOrderDTO.getOrgApprovalPath());
			purchaseRequest.setLinkLampiranPr(bookingOrderDTO.getLinkLampiranPr());
			purchaseRequest.setLinkLampiranKontrak(bookingOrderDTO.getLinkLampiranKontrak());
//			purchaseRequest.setSlaDeliveryTime(bookingOrderDTO.getSlaDeliveryTime());
			purchaseRequest.setRequestorUserId(user.getId());
			purchaseRequest.setPuspelCode(bookingOrderDTO.getPuspelCode());
			purchaseRequest.setGvDoctype(bookingOrderDTO.getGvDoctype());
			purchaseRequest.setGvHeadnote(bookingOrderDTO.getGvHeadnote());
			purchaseRequest.setGvIntermsoft(bookingOrderDTO.getGvIntermsoft());
			purchaseRequest.setGvAttachment(bookingOrderDTO.getGvAttachment());
			//KAI - 20201202 - [20595]
			purchaseRequest.setGvRequisitioner(bookingOrderDTO.getGvRequisitioner());
			purchaseRequest.setGvTestRun(bookingOrderDTO.getGvTestRun());
			purchaseRequest.setPurchOrg(bookingOrderDTO.getPurchOrg());
			
			Double totalHargaOngkir = new Double(0);
			//Double totalHargaAsuransi = new Double(0);
			Double totalHargaDiskon = new Double(0);
			int idx=0;
			//perubahan 22/2/2021[20867]
			List<BookingOrderItemDTO> bookingOrderItemDTOList = new ArrayList<>();
			for (BookingOrderItemDTO bOItemDTO : bookingOrderDTO.getBookingOrderItemDTOList()) {
				if(bOItemDTO.getQty() != 0) {
					bookingOrderItemDTOList.add(bOItemDTO);
				}
			}
			bookingOrderDTO.setBookingOrderItemDTOList(bookingOrderItemDTOList);
			for (BookingOrderItemDTO bookingOrderItemDTO : bookingOrderDTO.getBookingOrderItemDTOList()) {
				AddressBook addressBook = new AddressBook();
				addressBook = addressBookSession.getById(bookingOrderItemDTO.getAddressBookId());
	
				Catalog catalog = catalogSession.getCatalog(bookingOrderItemDTO.getCatalogId());
				Double discount = new Double (0);
				Double hargaWithDiscount = new Double (0);
				//Double asuransi = new Double (0);
				Double ongkosKirim = new Double(0);
				allEmail.add(catalog.getVendor().getEmail()); //setEmailVendor
				
				try {
					discount  = bookingOrderItemDTO.getDiscount();
				}catch(Exception e){
					log.info(">> no discount found");
				}
	
				try {
					ongkosKirim  = bookingOrderDTO.getOngkirList().get(idx) ;
				}catch(Exception e){
					log.info(">> no ongkos kirim found");
				}
				
				PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
				hargaWithDiscount = bookingOrderItemDTO.getHargaWithDiscount();
				
				purchaseRequestItem.setAddressBook(addressBook);
				purchaseRequestItem.setAlamat(addressBook.getAddressLabel() + " , " +
				addressBook.getStreetAddress());
				purchaseRequestItem.setItem(catalog.getItem());
				purchaseRequestItem.setItemname(catalog.getItem().getNama());
				purchaseRequestItem.setKode(catalog.getItem().getKode());
				purchaseRequestItem.setPrice(hargaWithDiscount);
				purchaseRequestItem.setQuantity(bookingOrderItemDTO.getQty());
				purchaseRequestItem.setSpecification(catalog.getDeskripsiIND());
				purchaseRequestItem.setStatus("Pending");
				purchaseRequestItem.setNormalPrice(catalog.getHarga());
				Double total;
				total =hargaWithDiscount * bookingOrderItemDTO.getQty() ;
				purchaseRequestItem.setTotal(total);
				purchaseRequestItem.setUnit(catalog.getSatuan().getNama());
				purchaseRequestItem.setVendorname(catalog.getVendor().getNama());
				purchaseRequestItem.setMataUang(catalog.getMataUang());
				purchaseRequestItem.setVendor(catalog.getVendor());
				purchaseRequestItem.setPurchaserequest(purchaseRequest);
				purchaseRequestItem.setDiscount(discount);
				//purchaseRequestItem.setAsuransi(asuransi);
				purchaseRequestItem.setAsuransi(Constant.ASURANSI);
				purchaseRequestItem.setOngkosKirim(bookingOrderItemDTO.getOngkosKirim());
				purchaseRequestItem.setGrandTotal(new Double(bookingOrderDTO.getGrandTotalWithQty()));
				purchaseRequestItem.setCatalog(catalog);
				// KAI - 2021/01/08 - [21483]
				CostCenterSap costCenterSap = costCenterSapSession.getByCode(bookingOrderDTO.getCostCenter().getCode());
				purchaseRequestItem.setCostCenterSap(costCenterSap);
				//KAI - 20201202 - [20595]
				//sap
				purchaseRequestItem.setPurGroupSap(bookingOrderDTO.getPurGroup());
				purchaseRequestItem.setStoreLocSap(bookingOrderDTO.getStoreLoc());
				purchaseRequestItem.setAcctasscat(bookingOrderDTO.getAcctasscat());
				// KAI - 2021/01/08 - [21483]
				GLAccountSap glAccountSap = gLAccountSapSession.getByCode(bookingOrderItemDTO.getgLAccount());
				purchaseRequestItem.setgLAccountSap(glAccountSap);
				purchaseRequestItem.setPreqName(bookingOrderDTO.getGvRequisitioner());
				
				purchaseRequestItem.setSlaDeliveryTime(bookingOrderItemDTO.getSlaDeliveryTime());
				totalHargaOngkir = totalHargaOngkir + ongkosKirim;
				totalHargaDiskon = totalHargaDiskon + hargaWithDiscount;
				//totalHargaAsuransi = totalHargaAsuransi + (asuransi == new Double(0) ? 0 : (discount/100) * total);
				idx++;
				purchaseRequestItemSession.createPurchaseRequestItem(purchaseRequestItem, tokenObj);
			}
	
			PurchaseRequest PR = purchaseRequestSession.insert(purchaseRequest, tokenObj);
			
		
			//notif email vendor
			for(String email : allEmail) {
				emailNotificationSession.getMailBookingOrderToVendor(purchaseRequest, user, email);
			}
			
			/*KAI - 20210119 - #21648*/
			/*create approvalProcessVendor*/
			List<PurchaseRequestItem> prItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(PR.getId());
			/*Boolean tampQty = false;
			for(PurchaseRequestItem prItem : prItemList) {
				Catalog catalogTamp = prItem.getCatalog();
				if(catalogTamp.getMaksimumQuantityPerOrder() != null && (prItem.getQuantity() <= catalogTamp.getMaksimumQuantityPerOrder())) {
					tampQty = true;
				}
			}
			if(tampQty) {
				approvalProcessSession.doCreateApproval(purchaseRequest.getId(), Constant.TAHAPAN_APPROVAL_BO, tokenObj, purchaseRequest.getTotalHarga());
				map.put("response", "SUCCESS");
			}else {
				approvalProcessVendorService.doCreateApprovalProcessVendor(token, purchaseRequest, bookingOrderDTO.getVendor());
				map.put("response", "SUCCESS");
			}*/
			approvalProcessSession.doCreateApproval(purchaseRequest.getId(), Constant.TAHAPAN_APPROVAL_BO, tokenObj, purchaseRequest.getTotalHarga());
			
			/* perubahan KAI 24/02/2021 [22288]*/
			for(PurchaseRequestItem prItem : prItemList) {
				CatalogContractDetail catalogContractDetail = prItem.getCatalog().getCatalogContractDetail();
				if(catalogContractDetail.getQtyMaxOrder() != null) {
					catalogContractDetail.setQtyMaxOrder(catalogContractDetail.getQtyMaxOrder() - prItem.getQuantity());							
					catalogContractDetailSession.update(catalogContractDetail, tokenObj);
				}
			}
			map.put("purchaseRequest", purchaseRequest);
			userTransaction.commit();
		} catch (Exception e) {
			userTransaction.rollback();
			e.printStackTrace();
			Response.serverError().build();
		}

		return Response.ok(map).build();
	}

	@Path("/insert")
	@POST
	public PurchaseRequest insert(@FormParam("prnumber") String prnumber, @FormParam("department") String department,
			@FormParam("departmentId") int departmentId, @FormParam("costcenter") String costcenter,
			@FormParam("totalcost") String totalcost, @FormParam("daterequired") String daterequired,
			@FormParam("approvalId") String approvalId, @FormParam("approvalIsNew") boolean approvalIsNew,
			@FormParam("approvalUser") int approvalUser, @FormParam("apparovalOrganisasi") int apparovalOrganisasi,
			@FormParam("nextapproval") String nextapproval, @FormParam("procurementprocess") String procurementprocess,
			@FormParam("termandcondition") String termandcondition, @FormParam("description") String description,
			@FormParam("fileuploadList") String fileuploadList, @FormParam("isJoin") Integer isJoin,
			@HeaderParam("Authorization") String token) {

		Token tkn = tokenSession.findByToken(token);
		PurchaseRequest pr = new PurchaseRequest();
		pr.setPrnumber(prnumber);
		pr.setDepartment(department);
		pr.setCostcenter(costcenter);
		//pr.setTotalcost(totalcost);
		try {
			pr.setDaterequired(sdf.parse(daterequired));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		pr.setOrganisasi(organisasiSession.getAfcoOrganisasiByOrganisasiId(departmentId));
		// pr.setAfco(afcoSession.getAfco(id));
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
		apt.setTahapan(tahapanSession.getByName(Constant.TAHAPAN_CREATE_PR));
		

		apt = approvalProcessTypeSession.create(apt, tkn);

			List<ApprovalLevel> aplList = approvalLevelSession
					.findByApproval(approvalSession.find(Integer.parseInt(approvalId)));
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
				ap.setGroup(apl.getApproval().getOrganisasi());
				ap.setUser(apl.getUser());
				// ap.setRole(apl.getRole());
				ap.setThreshold(apl.getThreshold());
				ap.setSequence(apl.getSequence());
				approvalProcessSession.create(ap, tkn);
			}
		

		/* UploadFileList */
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

	@Path("/update")
	@POST
	public PurchaseRequest update(@FormParam("id") Integer id, @FormParam("prnumber") String prnumber,
			@FormParam("department") String department, @FormParam("departmentId") int departmentId,
			@FormParam("costcenter") String costcenter, @FormParam("totalcost") String totalcost,
			@FormParam("daterequired") String daterequired, @FormParam("approvalIsChange") boolean approvalIsChange,
			@FormParam("approvalProcessTypeId") int approvalProcessTypeId, @FormParam("approvalId") String approvalId,
			@FormParam("approvalIsNew") boolean approvalIsNew, @FormParam("approvalUser") int approvalUser,
			@FormParam("apparovalOrganisasi") int apparovalOrganisasi, @FormParam("nextapproval") String nextapproval,
			@FormParam("procurementprocess") String procurementprocess,
			@FormParam("termandcondition") String termandcondition, @FormParam("description") String description,
			@FormParam("fileuploadList") String fileuploadList, @FormParam("isJoin") Integer isJoin,
			@HeaderParam("Authorization") String token) {
		Token tkn = tokenSession.findByToken(token);
		PurchaseRequest pr = purchaseRequestSession.find(id);
		//double prTotalCostOld = Double.parseDouble(pr.getTotalcost());

		// pr.setPrnumber(prnumber);
		pr.setDepartment(department);
		pr.setCostcenter(costcenter);
		//pr.setTotalcost(totalcost);
		try {
			pr.setDaterequired(sdf.parse(daterequired));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		pr.setOrganisasi(organisasiSession.getAfcoOrganisasiByOrganisasiId(departmentId));
		pr.setTermandcondition(termandcondition);
		pr.setProcurementprocess(procurementprocess);
		pr.setDescription(description);
		pr.setUpdated(new Date());
		pr.setUserId(tkn.getUser().getId());
		pr.setNextapproval(nextapproval);
		pr.setStatus(3);
		pr.setIsJoin(isJoin);
		purchaseRequestSession.update(pr, tkn);

		// hapus approval existing
		ApprovalProcessType approvalProcessType = approvalProcessTypeSession.findByTahapanAndValueId("CREATE_PR",
				pr.getId());
		if (approvalProcessType != null) {

			approvalProcessType.setIsDelete(1);

			List<ApprovalProcess> approvalProcesseList = approvalProcessSession
					.findByApprovalProcessType(approvalProcessType.getId());
			if (approvalProcesseList != null) {
				if (approvalProcesseList.size() > 0) {
					for (ApprovalProcess approvalProcess : approvalProcesseList) {
						approvalProcess.setApprovalProcessType(approvalProcessType);
						approvalProcess.setIsDelete(1);
						approvalProcessSession.update(approvalProcess, tkn);
					}
				} else {
					approvalProcessTypeSession.delete(approvalProcessType, tkn);
				}
			} else {
				approvalProcessTypeSession.delete(approvalProcessType, tkn);
			}
		}

		// create new approval
		ApprovalProcessType apt = new ApprovalProcessType();
		apt.setValueId(pr.getId());
		apt.setTahapan(tahapanSession.getByName(Constant.TAHAPAN_CREATE_PR));
		if (!approvalIsNew) {
			apt.setApproval(approvalSession.find(Integer.parseInt(approvalId)));
			apt.setJenis(apt.getApproval().getJenis());
		} else {
			apt.setJenis(1); // 1 = paralel , pasti 1
		}

		apt = approvalProcessTypeSession.create(apt, tkn);
		if (!approvalIsNew) { // jika approval menggunakan template
			List<ApprovalLevel> aplList = approvalLevelSession
					.findByApproval(approvalSession.find(Integer.parseInt(approvalId)));
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
				ap.setGroup(apl.getApproval().getOrganisasi());
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
			// ap.setGroup(organisasiSession.find(apparovalOrganisasi));
			ap.setUser(userSession.find(approvalUser));
			ap.setSequence(1);
			approvalProcessSession.create(ap, tkn);
		}

		uploadPurchaseRequestSession.deleteRowByByPurchaseRequest(pr.getId());

		/* UploadFileList */
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

	@Path("/delete/{id}")
	@GET
	public PurchaseRequest delete(@PathParam("id") int id, @HeaderParam("Authorization") String tokenStr) {
		PurchaseRequest pr = new PurchaseRequest();
		Token token = tokenSession.findByToken(tokenStr);
		boolean deleteOk = purchaseRequestItemSession.deletePurchaseRequestItemByPrId(id, token);

		if (deleteOk) {
			ApprovalProcessType approvalProcessType = approvalProcessTypeSession.findByPRId(id);

			if (approvalProcessType != null) {
				List<ApprovalProcess> approvalProcessList = approvalProcessSession
						.findByApprovalProcessType(approvalProcessType.getId());

				for (ApprovalProcess approvalProcess : approvalProcessList) {
					approvalProcess.setIsDelete(1);
					approvalProcessSession.update(approvalProcess, token);
				}
				approvalProcessTypeSession.update(approvalProcessType, token);
			}
			pr = purchaseRequestSession.delete(id, token);

			// update cost allocation
			/*
			 * List<AlokasiAnggaran> alokasiAnggaranList =
			 * alokasiAnggaranSession.findByNomorDraft(pr.getCostcenter()); if
			 * (alokasiAnggaranList.size() > 0) { double sisaAnggaran =
			 * alokasiAnggaranList.get(0).getSisaAnggaran(); double jumlahAnggaran =
			 * alokasiAnggaranList.get(0).getJumlah(); double bookingAnggaran =
			 * alokasiAnggaranList.get(0).getBookingAnggaran();
			 * 
			 * double newSisaAnggaran = sisaAnggaran +
			 * Double.parseDouble(pr.getTotalcost()); double newJumlahAnggaran =
			 * jumlahAnggaran; double newBookingAnggaran = bookingAnggaran -
			 * Double.parseDouble(pr.getTotalcost());
			 * 
			 * alokasiAnggaranList.get(0).setSisaAnggaran(newSisaAnggaran);
			 * alokasiAnggaranList.get(0).setJumlah(newJumlahAnggaran);
			 * alokasiAnggaranList.get(0).setBookingAnggaran(newBookingAnggaran);
			 * 
			 * alokasiAnggaranSession.updateAlokasiAnggaran(alokasiAnggaranList.get(0),
			 * tokenSession.findByToken(token)); }
			 */
		}
		return pr;
	}

	@Path("/deleteRow/{id}")
	@GET
	public PurchaseRequest deleteRow(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return purchaseRequestSession.deleteRow(id, tokenSession.findByToken(token));
	}

	@Path("/getPurchaseRequestListByPRNumberWithPagination")
	@POST
	public Response getPurchaseRequestListByPRNumberWithPagination(@FormParam("pageNumber") Integer pageNumber,
			@FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("searchingKeyWord") String searchingKeyWord, @FormParam("isJoin") Integer joinStatus,
			@HeaderParam("Authorization") String token) {
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
				return Response.status(201).entity(purchaseRequestSession.getListPRinRange(startIndex, endIndex,
						joinStatus, tokenSession.findByToken(token))).build();
			} else {
				return Response.status(201).entity(purchaseRequestSession.getListPRinRangeByPRNumber(searchingKeyWord,
						startIndex, endIndex, joinStatus, tokenSession.findByToken(token))).build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/getPurchaseRequestVerificationListByPRNumberWithPagination")
	@POST
	public Response getPurchaseRequestVerificationListByPRNumberWithPagination(
			@FormParam("pageNumber") Integer pageNumber, @FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("searchingKeyWord") String searchingKeyWord, @HeaderParam("Authorization") String token) {
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
				return Response.status(201).entity(purchaseRequestSession.getListPRVerificationinRange(startIndex,
						endIndex, tokenSession.findByToken(token))).build();
			} else {
				return Response.status(201)
						.entity(purchaseRequestSession.getListPRVerificationinRangeByPRNumber(searchingKeyWord,
								startIndex, endIndex, tokenSession.findByToken(token)))
						.build();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/updateStatusPurchaseRequest")
	@POST
	public PurchaseRequest updateStatusPurchaseRequest(@FormParam("id") Integer pengadaanId,
			@HeaderParam("Authorization") String token) {
		PurchaseRequestPengadaan purchaseRequestPengadaan = purchaseRequestPengadaanSession
				.getListByPengadaanId(pengadaanId).get(0);
		PurchaseRequest purchaseRequest = purchaseRequestPengadaan.getPurchaseRequest();
		purchaseRequest.setStatus(5);// onProcess
		purchaseRequest.setUpdated(new Date());

		purchaseRequestSession.update(purchaseRequest, tokenSession.findByToken(token));

		return purchaseRequest;

	}

	@Path("/updateStatusApproval")
	@POST
	public PurchaseRequest updateStatusApproval(@FormParam("id") Integer id, @FormParam("note") String note,
			@FormParam("status") Integer status, @FormParam("approvalProcessId") Integer approvalProcessId,
			@HeaderParam("Authorization") String token) {

		/** get token **/
		Token tkn = tokenSession.findByToken(token);

		/** get purchase request **/
		PurchaseRequest purchaseRequest = purchaseRequestSession.find(id);

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

			// jika status 3(approve), cek lagi level selanjutnya. dan jenis
			// serial
			if (approvalProcess.getStatus() == 3 && approvalProcess.getApprovalProcessType().getJenis() == 0) {
				// update next approval
				Integer sequence = approvalProcess.getSequence();
				ApprovalProcess apNext = approvalProcessSession
						.findByApprovalProcessTypeAndSequence(approvalProcess.getApprovalProcessType(), ++sequence);
				if (apNext != null) {
					apNext.setUserId(tkn.getUser().getId());
					apNext.setUpdated(new Date());
					apNext.setStatus(1); // Approval level berikutnya diset
											// active
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

			purchaseRequest.setStatus(status);

			if (note == null) {
				note = "";
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

			// jika status all approved
			if (appSize == approvalProcessList.size()) {
				status = 8;
				note = "";
				purchaseRequest.setApprovedDate(new Date());

				// update cost allocation
				List<AlokasiAnggaran> alokasiAnggaranList = alokasiAnggaranSession
						.findByNomorDraft(purchaseRequest.getCostcenter());
				if (alokasiAnggaranList.size() > 0) {
					double prTotalCostOld = Double.parseDouble(purchaseRequest.getTotalCostAfterApprove() == null ? "0"
							: purchaseRequest.getTotalCostAfterApprove());
					double sisaAnggaran = alokasiAnggaranList.get(0).getSisaAnggaran();
					double jumlahAnggaran = alokasiAnggaranList.get(0).getJumlah();
					double bookingAnggaran = alokasiAnggaranList.get(0).getBookingAnggaran();

					double newSisaAnggaran = sisaAnggaran - purchaseRequest.getTotalHarga()
							+ prTotalCostOld;
					double newJumlahAnggaran = jumlahAnggaran;
					double newBookingAnggaran = bookingAnggaran + purchaseRequest.getTotalHarga()
							- prTotalCostOld;

					alokasiAnggaranList.get(0).setSisaAnggaran(newSisaAnggaran);
					alokasiAnggaranList.get(0).setJumlah(newJumlahAnggaran);
					alokasiAnggaranList.get(0).setBookingAnggaran(newBookingAnggaran);

					alokasiAnggaranSession.updateAlokasiAnggaran(alokasiAnggaranList.get(0),
							tokenSession.findByToken(token));

					// update total cost after approve
					//purchaseRequest.setTotalCostAfterApprove(purchaseRequest.getTotalcost());
				}
			}

			// update pr
			purchaseRequest.setNextapproval(note);
			purchaseRequest.setStatus(status);
			purchaseRequest.setUpdated(new Date());
			purchaseRequestSession.update(purchaseRequest, tkn);

		}
		return purchaseRequest;
	}

	@Path("/getVerifiedPurchaseRequest")
	@GET
	public List<PurchaseRequest> getVerifiedPurchaseRequest(@HeaderParam("Authorization") String token) {
		return purchaseRequestSession.getVerifiedPurchaseRequest(tokenSession.findByToken(token));
	}

	@Path("/getVerifiedPurchaseRequestByStatus")
	@GET
	public List<PurchaseRequest> getVerifiedPurchaseRequestByStatus(@HeaderParam("Authorization") String token) {
		return purchaseRequestSession.getVerifiedPurchaseRequestByStatus(tokenSession.findByToken(token));
	}

	@Path("/getVerifiedPurchaseRequestByNumber")
	@POST
	public List<PurchaseRequest> getVerifiedPurchaseRequestByNumber(@FormParam("keyword") String keyword,
			@HeaderParam("Authorization") String token) {
		return purchaseRequestSession.getVerifiedPurchaseRequestByNumber(keyword);
	}

	@Path("/getVerifiedPurchaseRequestByNumberAndToken")
	@POST
	public List<PurchaseRequest> getVerifiedPurchaseRequestByNumberAndToken(@FormParam("keyword") String keyword,
			@HeaderParam("Authorization") String token) {
		return purchaseRequestSession.getVerifiedPurchaseRequestByNumberAndToken(keyword,
				tokenSession.findByToken(token));
	}

	@Path("/updatePRJoin")
	@POST
	public Response updatePRJoin(@FormParam("prId") int prId, @FormParam("prNumber") String prnumber,
			@FormParam("prDate") String daterequired, @FormParam("prGrandTotal") String totalcost,
			@FormParam("prDeskripsi") String description, @FormParam("prItemList") String itemsId,
			@FormParam("prQtyJoin") Double qtyJoin, @HeaderParam("Authorization") String token) {
		try {
			PurchaseRequest prJoin = purchaseRequestSession.find(prId);
			prJoin.setPrnumber(prnumber);
			prJoin.setDaterequired(sdf.parse(daterequired));
			//prJoin.setTotalcost(totalcost);
			prJoin.setDescription(description);
			purchaseRequestSession.update(prJoin, tokenSession.findByToken(token));
			return Response.ok(prJoin).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/getjoinprnumber")
	@GET
	public Response getPrJoinNumber(@HeaderParam("Authorization") String token) {
		Map<String, String> mapDTO = new HashMap<String, String>();
		mapDTO.put("prjoin", autoNumberSession.generateNumber("PRJOIN", tokenSession.findByToken(token)));
		return Response.ok(mapDTO).build();
	}

	@Path("/insertPRJoin")
	@POST
	public Response insertPRJoin(@FormParam("prNumber") String prnumber, @FormParam("prGrandTotal") String totalcost,
			@FormParam("prDate") Date daterequired, @FormParam("prDeskripsi") String description,
			@FormParam("prItemList") String itemsId, @FormParam("prQtyJoin") Double qtyJoin,
			@HeaderParam("Authorization") String token) {

		Token tkn = tokenSession.findByToken(token);
		Integer userId = tkn.getUser().getId();
		// Find Organisasi by User from Token
		Organisasi org = new Organisasi();
		// 1. Role User
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(userId);
		if (roleUserList != null && roleUserList.size() > 0) {
			// 2. Find Organisasi for Departemen setting
			org = organisasiSession.getOrganisasi(roleUserList.get(0).getOrganisasi().getId());
		}
		PurchaseRequest prJoin = new PurchaseRequest();

		try {
			List<Integer> itemList = new ArrayList<Integer>();
			if (itemsId != null) {
				String[] parts = itemsId.split(",");
				for (int i = 0; i < parts.length; i++) {
					itemList.add(Integer.valueOf(parts[i].trim()));
				}
			}

			// Save PR JOIN
			prJoin.setPrnumber(prnumber);
			//prJoin.setTotalcost(totalcost);
			prJoin.setDaterequired(daterequired);

			Organisasi organisasi = organisasiSession
					.getAfcoOrganisasiByUserId(tokenSession.findByToken(token).getUser().getId());
			if (organisasi != null) {
				prJoin.setOrganisasi(organisasi);
			}

			prJoin.setDepartment(org.getNama());
			prJoin.setDescription(description);
			prJoin.setStatus(9);
			prJoin.setIsJoin(2); // IsJoin = 1, NotIsJoin = 0, WasJoin = 2
			prJoin.setTermandcondition("JOIN PR from -" + itemList.size() + "- Item");
			purchaseRequestSession.insert(prJoin, tkn);

			// Update PR Item
			if (itemsId != null) {

				List<Object[]> arrObjList = purchaseRequestItemSession.getPurchaseRequestItemListForJoinPr(itemList);
				List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
				if (arrObjList != null && arrObjList.size() > 0) {
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
				// PurchaseRequest catch PR was join
				List<Integer> prIdList = new ArrayList<Integer>();

				for (int i = 0; i < itemList.size(); i++) {
					Integer itemId = itemList.get(i);

					List<PurchaseRequestItem> prItemList = purchaseRequestItemSession
							.getPurchaseRequestItemVerifiedListByItem(itemId);
					if (prItemList != null && prItemList.size() > 0) {
						for (PurchaseRequestItem prItem : prItemList) {
							// add pr was join
							prIdList.add(prItem.getPurchaserequest().getId());

							for (PurchaseRequestItem bandingPRItem : purchaseRequestItemList) {
								if (prItem.getItem().getId() == bandingPRItem.getItem().getId()) {
									prItem.setPurchaserequestjoin(prJoin);
									prItem.setPriceafterjoin(bandingPRItem.getPrice());
									prItem.setQtyafterjoin(bandingPRItem.getQtyafterjoin());
									try {
										purchaseRequestItemSession.updatePurchaseRequestItem(prItem, tkn);
									} catch (Exception e) {
										log.error(e.getMessage());
										// kirim 400 Bad Request
										return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage()))
												.build();
									}
								}
							}
						}
					}
				}

				// update PR was join, isJoin to 2(Was join)
				Map<Integer, Integer> groupPRJoinMap = new HashMap<Integer, Integer>();
				int index = 0;
				for (Integer prId : prIdList) {
					if (!groupPRJoinMap.containsValue(prId)) {
						groupPRJoinMap.put(index, prId);
						index++;
					}
				}
				for (int i = 0; i < groupPRJoinMap.size(); i++) {
					PurchaseRequest pr = purchaseRequestSession.find(groupPRJoinMap.get(i));
					if (pr != null) {
						pr.setIsJoin(2);
						purchaseRequestSession.update(pr, tokenSession.findByToken(token));
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

	@Path("/join/delete/{id}")
	@GET
	public PurchaseRequest deleteJoin(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		purchaseRequestItemSession.deletePurchaseRequestItemJoinByPrId(id, tokenSession.findByToken(token));
		return purchaseRequestSession.delete(id, tokenSession.findByToken(token));
	}

	@Path("/get-list")
	@POST
	public Response getBookingOrderListPagination(@FormParam("search") String search,
			@HeaderParam("Authorization") String token, @FormParam("statusBO") String status,
			@FormParam("statusPR") String statusEbs, @FormParam("pageNo") String pageNo,
			@FormParam("pageSize") Integer pageSize, @FormParam("startDate") Date startDate,
			@FormParam("endDate") Date endDate) {
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
		Integer intStatus;
		Integer intStatusEbs;
		Calendar calender = Calendar.getInstance();
		if (endDate != null) {
			calender.setTime(endDate);
			calender.add(Calendar.DATE, 1);
		}

		Date currentDatePlusOne = calender.getTime();
		try {
			
			intStatus = status == null || status.equals("undefined") || status.equals("") ? null
					: Integer.parseInt(status);

		} catch (Exception e) {
			// kalo undefined
			intStatus = null;

		}
		try {

			intStatusEbs = statusEbs == null || statusEbs.equals("undefined") || statusEbs.equals("") ? null
					: Integer.parseInt(statusEbs);
		} catch (Exception e) {
			// kalo undefined

			intStatusEbs = null;
		}
		Integer intpageNo = pageNo == null || pageNo.equals("undefined") ? null : Integer.parseInt(pageNo);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		/* perubahan KAI 7/12/2020 filter by organisasi*/
		List <Integer> ListorganisasiId = new ArrayList<Integer>();
		/* perubahan KAI 18/12/2020 filter by organisasi[20869]*/
//		if( roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_LOGISTIK) || roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_USER) ) {
//			ListorganisasiId.add(roleUser.getOrganisasi().getId());
//		}else {
			if(roleUser.getOrganisasi() != null) {
				for(Organisasi org : organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId())) {
					ListorganisasiId.add(org.getId());
				}				
			}
//		}
		List<PurchaseRequest> purchaseRequestList =purchaseRequestSession.getBookingOrderListPagination(search, intStatus, intStatusEbs,
			intpageNo, pageSize, startDate, currentDatePlusOne, roleUser, ListorganisasiId);
		
		for(PurchaseRequest pr : purchaseRequestList) {
			pr.setIsAvailable(true);
		}	
			
		map.put("jmlData", purchaseRequestSession.getTotalList(search, intStatus, intStatusEbs, startDate, currentDatePlusOne, roleUser, ListorganisasiId));
		map.put("dataList", purchaseRequestList);
		
//		if(roleUser.getOrganisasi()!=null) {
//			if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
//				organisasiListId = null;
//			} else {
//				if(roleUser.getOrganisasi().getId() > 0 ){			
//					organisasiListId.add(roleUser.getOrganisasi().getId());	
//					List<Integer> organisasiList = getOrganisasiByParentId(roleUser.getOrganisasi().getId());			
//					for(Integer id : organisasiList ){
//						organisasiListId.add(id);	
//					}	
//				}
//			}
//				
//			List<PurchaseRequest> purchaseRequestList =purchaseRequestSession.getBookingOrderListPagination(search, intStatus, intStatusEbs,
//					intpageNo, pageSize, organisasiListId, startDate, currentDatePlusOne, roleUser);
//			for(PurchaseRequest pr : purchaseRequestList) {
//				if(pr.getOrganisasi().getId() == roleUser.getOrganisasi().getId()) {
//					pr.setIsAvailable(true);
//				} else {
//					pr.setIsAvailable(false);
//				}
//			}	
//			
//			map.put("jmlData", purchaseRequestSession.getTotalList(search, intStatus, intStatusEbs, organisasiListId, startDate, currentDatePlusOne, roleUser));
//			map.put("dataList", purchaseRequestList);
//		}
		return Response.ok(map).build();
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
	
	@Path("/get-list-by-organisasi-id")
	@GET
	public List<OrgApprovalPath> getByOrganisasiId(@HeaderParam("Authorization") String token) {
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getRoleUserByUserId(user.getId()).get(0);
		Organisasi rootParentOrg = organisasiSession.getRootParentByOrganisasi(roleUser.getOrganisasi().getId());
		return orgApprovalPathSession.getByOrganisasiId(rootParentOrg.getId());
	}
	
	@Path("/get-purchase-request-by-id")
	@POST
	public PurchaseRequest getPurchaseRequestById(@HeaderParam("Authorization") String token, Integer id) {
		PurchaseRequest purchaseRequest = purchaseRequestSession.get(id);
		return purchaseRequest;
	}
	
	@Path("/getPurchaseRequestInterfacingService")
	@GET
	public Response getPurchaseRequestInterfacingService() throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
		GetInvoicePaymentERPRequest getInvoicePaymentERPRequest= new GetInvoicePaymentERPRequest();		
		getInvoicePaymentERPRequest.setLastUpdateDate(today);
		getInvoicePaymentERPRequest.setCreationDate(today);
		invoicePaymentInterfacingService.getInvoicePayment(getInvoicePaymentERPRequest);		
		purchaseRequestInterfacingService.getIncompletePRStatus();		
		return null;
	}
	
	@Path("/getTotalTransaction")
	@POST
	public Integer getTotalTransaction(@FormParam("catalogId") Integer catalogId, @FormParam("itemId") Integer itemId) {
		 
		return purchaseRequestSession.getTransaction(catalogId, itemId);
	}
	
	@Path("/get-catalog-fee/{id}")
	@GET
	public Response getCatalogFee(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
		Organisasi organisasi = organisasiSession.getAfcoOrganisasiByParentId(roleUser.getOrganisasi().getId());
		CatalogFee catalogFee = catalogFeeSession.getCatalogFeeByCatalogIdAndOrganisasiId(id, organisasi.getId());
		map.put("catalogFee", catalogFee);
		return Response.ok(map).build();
	}
	
	@Path("/get-booking-order-view")
	@POST
	public Response getBookingOrderView(CatalogDTO CatalogDto, @HeaderParam("Authorization") String token) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
		Organisasi organisasi = organisasiSession.getAfcoOrganisasiByParentId(roleUser.getOrganisasi().getId());
		
		Integer id = 1;
		CatalogFee catalogFee = catalogFeeSession.getCatalogFeeByCatalogIdAndOrganisasiId(id, organisasi.getId());
		map.put("catalogFee", catalogFee);
		return Response.ok(map).build();
	}
	
	@Path("/get-booking-order-view")
	@POST
	public Response getBookingOrderViewByList(List<Catalog> catalog, @HeaderParam("Authorization") String token) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		User user = tokenSession.findByToken(token).getUser();
		RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
		Organisasi organisasi = organisasiSession.getAfcoOrganisasiByParentId(roleUser.getOrganisasi().getId());
		
		Integer id = 1;
		CatalogFee catalogFee = catalogFeeSession.getCatalogFeeByCatalogIdAndOrganisasiId(id, organisasi.getId());
		map.put("catalogFee", catalogFee);
		return Response.ok(map).build();
	}
	
	// KAI - 20210204 - #20867
	@Path("/get-data-from-qty")
	@POST
	public Response getDataByQty(RequestQtyDTO requestQtyDTO, @HeaderParam("Authorization") String token) {
		
		Double totalOngkirItem = 0.0;
		Double totalHargaItem = 0.0;
		Double qtyCartItem= new Double(requestQtyDTO.getQty());
		
		Map<String, Object> map = new HashMap<String, Object>();
//		Catalog catalog = catalogSession.find(requestQtyDTO.getCatalogId());
//		User user = tokenSession.findByToken(token).getUser();
//		RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
//		Organisasi organisasi = organisasiSession.getAfcoOrganisasiByParentId(roleUser.getOrganisasi().getId());
		Organisasi organisasi = organisasiSession.getOrganisasi(requestQtyDTO.getOrganisasiId());
		CatalogFee catalogFee = catalogFeeSession.getCatalogFeeByCatalogIdAndOrganisasiId(requestQtyDTO.getCatalogId(), organisasi.getId());
		
		//menghitung ongkir
		if (catalogFee != null) {
			if (catalogFee.getOngkosKirim() != null) {
				
				if(requestQtyDTO.getQty() >= catalogFee.getQuantityBatch()){
					
					Double quantityBatch= new Double(catalogFee.getQuantityBatch());
					Double quantity= qtyCartItem/quantityBatch;
					Double totalBarang = Math.ceil(quantity);
					//tempOngkirList.add(catalogFee.getOngkosKirim()*totalBarang);
					totalOngkirItem = (catalogFee.getOngkosKirim()*totalBarang);						
				}
				else{
					Double ongkir= new Double(catalogFee.getOngkosKirim());
					//tempOngkirList.add(ongkir);
					totalOngkirItem = ongkir;
				}

			} 	
			
		}
		
		//menghitung total harga peritem
		Double harga = new Double(requestQtyDTO.getHarga());
		totalHargaItem = (requestQtyDTO.getQty()*harga)-(requestQtyDTO.getDiskon() * (qtyCartItem*harga));
		
		
		
		map.put("totalOngkirItem", totalOngkirItem);
		map.put("totalHargaItem", totalHargaItem);
		return Response.ok(map).build();

	}
	
	
	
	@Path("/get-booking-order-view-catalog")
	@POST
	public Response getBookingOrderViewByCatalog(List<CatalogDTO> catalogDtoList, @HeaderParam("Authorization") String token) {
		
		List<Double> tempBulkPriceDiskon = new ArrayList<Double>();
		List<Double> tempOngkirList = new ArrayList<Double>();
		List<Double> tempAsuransiList2 = new ArrayList<Double>();
		List<String> tempNomorKontrakList = new ArrayList<String>();
		
		Double totalOngkir= 0.0;
		Double grandTotalWithQty = 0.0;
		Double total = 0.0;
		Boolean isCatalogFee = true;
		Integer slaDeliveryTime = 0;
		Boolean isSame = false;
		String nomorKontrakList = "";
				
		Map<String, Object> map = new HashMap<String, Object>();
		Integer index = 0;
		for (CatalogDTO catalogDto : catalogDtoList ) {
			Catalog catalog = catalogSession.find(catalogDto.getId());
			tempBulkPriceDiskon.add(0.0);
			List<CatalogBulkPrice> catalogBulkPriceList = catalogBulkPriceSession.getCatalogBulkPriceListByCatalogId(catalogDto.getId());
			catalog.setCatalogBulkPriceList(catalogBulkPriceList);
			for (CatalogBulkPrice catalogBulkPrice : catalog.getCatalogBulkPriceList() ) {
				if((catalogDto.getQtyCartItem() >= catalogBulkPrice.getMinQuantity() && catalogDto.getQtyCartItem() <= catalogBulkPrice.getMaxQuantity()) || catalogDto.getQtyCartItem() > catalogBulkPrice.getMaxQuantity()){
					tempBulkPriceDiskon.set(index, catalogBulkPrice.getDiskon());
				}
			}
			
			User user = tokenSession.findByToken(token).getUser();
			RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
			Organisasi organisasi = organisasiSession.getAfcoOrganisasiByParentId(roleUser.getOrganisasi().getId());
			CatalogFee catalogFee = catalogFeeSession.getCatalogFeeByCatalogIdAndOrganisasiId(catalogDto.getId(), organisasi.getId());
			if (catalogFee != null) {
				if (catalogFee.getOngkosKirim() != null) {
					Double ongkirTotal = 0.0;
					if(catalogDto.getQtyCartItem() >= catalogFee.getQuantityBatch()){
						Double qtyCartItem= new Double(catalogDto.getQtyCartItem());
						Double quantityBatch= new Double(catalogFee.getQuantityBatch());
						Double quantity= qtyCartItem/quantityBatch;
						Double totalBarang = Math.ceil(quantity);
						tempOngkirList.add(catalogFee.getOngkosKirim()*totalBarang);
						ongkirTotal = (catalogFee.getOngkosKirim()*totalBarang);						
					}
					else{
						Double ongkir= new Double(catalogFee.getOngkosKirim());
						tempOngkirList.add(ongkir);
						ongkirTotal = ongkir;
					}
					totalOngkir += ongkirTotal;
					if(catalogDto.getHarga_eproc() == null || catalogDto.getHarga_eproc() == 0){
						grandTotalWithQty+=(catalogDto.getQtyCartItem() * catalogDto.getHarga())-((tempBulkPriceDiskon.get(index)/100)*(catalogDto.getHarga() * catalogDto.getQtyCartItem()))+tempOngkirList.get(index);
					}else{
						grandTotalWithQty+=(catalogDto.getQtyCartItem() * catalogDto.getHarga_eproc())-((tempBulkPriceDiskon.get(index)/100)*(catalogDto.getHarga_eproc() * catalogDto.getQtyCartItem()))+tempOngkirList.get(index);
//						console.log($scope.salesOrder.grandTotalWithQty);
					}
					total+=(catalogDto.getQtyCartItem() * catalogDto.getHarga())-((tempBulkPriceDiskon.get(index)/100)*(catalogDto.getHarga() * catalogDto.getQtyCartItem()));
					if(catalogFee.getAsuransi() !=null){
						tempAsuransiList2.add(catalogFee.getAsuransi());
					}	
					
				} else {
						isCatalogFee = false;
				}
				
				if(catalogFee.getSlaDeliveryTime() > slaDeliveryTime){
					slaDeliveryTime = catalogFee.getSlaDeliveryTime();
				}
				
				for (String tempNomorKontrak : tempNomorKontrakList ) {
					if ( tempNomorKontrak.equalsIgnoreCase(catalogFee.getCatalog().getCatalogKontrak().getNomorKontrak()) ) {
						isSame =  true;
					}
				}
				
				if (!isSame){
					
					tempNomorKontrakList.add(catalogFee.getCatalog().getCatalogKontrak().getNomorKontrak());
					
					for (String tempNomorKontrak : tempNomorKontrakList)
					{
						nomorKontrakList += tempNomorKontrak + ",\n";
					}

					//vm.nomorKontrakList = tempNomorKontrakList.join(",\n");
				}
				
				
			} else {
				isCatalogFee = false;
			}
			index++;
		}
		
		map.put("tempBulkPriceDiskon", tempBulkPriceDiskon);
		map.put("tempOngkirList", tempOngkirList);
		map.put("tempAsuransiList2", tempAsuransiList2);
		map.put("tempNomorKontrakList", tempNomorKontrakList);
		map.put("totalOngkir", totalOngkir);
		map.put("grandTotalWithQty", grandTotalWithQty);
		map.put("total", total);
		map.put("isCatalogFee", isCatalogFee);
		map.put("slaDeliveryTime", slaDeliveryTime);
		map.put("isSame", isSame);
		map.put("nomorKontrakList", nomorKontrakList);
		return Response.ok(map).build();
	}
	
	@Path("/getCatalogFeeByCatalogAndOrgId")
	@POST
	public Response getCatalogFeeByCatalogAndOrgId(RequestQtyDTO requestQtyDTO, @HeaderParam("Authorization") String token) {
		List<CatalogFee> catalogFeeList = new ArrayList<>();
		for(Integer catalogId : requestQtyDTO.getCatalogIdList()) {
			if(requestQtyDTO.getOrganisasiId() != null) {
				CatalogFee catalogFee = catalogFeeSession.getCatalogFeeByCatalogIdAndOrganisasiId(catalogId, requestQtyDTO.getOrganisasiId());
				catalogFeeList.add(catalogFee);
			}
		}
		if(catalogFeeList.size() > 0) {
			CatalogFee greatestCatalogFee = catalogFeeList.get(0);
			for(CatalogFee catalogFee : catalogFeeList) {
				if(catalogFee.getSlaDeliveryTime() > greatestCatalogFee.getSlaDeliveryTime()) {
					greatestCatalogFee = catalogFee;
				}
			}
			return Response.ok(greatestCatalogFee).build();			
		}else {
			return Response.ok(new CatalogFee()).build();
		}
	}
	@Path("/slaDeliveryTimeListByPurchaseRequest")
	@POST
	public List<Integer> getPurchaseRequestItemByPr(@HeaderParam("Authorization") String token, Integer id) {
		List<Integer> slaDeliveryTimeList = purchaseRequestItemSession.groupByPurchaseRequestId(id);
		
		return slaDeliveryTimeList;
	}
	
	@Path("/getListPaging")
	@POST
	public Response getListPaging(
			@FormParam("refresh") String refresh,
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("startDate") String startDate,
			@FormParam("endDate") String endDate,
			@FormParam("statusBO") String statusBO,
			@FormParam("statusPR") String statusPR,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String strToken
			) {
		try {
			User user = tokenSession.findByToken(strToken).getUser();
			RoleUser roleUser = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), "PM").get(0);
			List <Integer> ListorganisasiId = new ArrayList<Integer>();
			if(roleUser.getOrganisasi() != null) {
				for(Organisasi org : organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId())) {
					ListorganisasiId.add(org.getId());
				}				
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dateStart = null;
			Date dateEnd = null;
			if(!startDate.equals("")) {
				dateStart = formatter.parse(startDate);				
			}
			if(!endDate.equals("")) {
				dateEnd = formatter.parse(endDate);				
			}
			List<PurchaseRequest> purchaseRequestList = purchaseRequestSession.getListPagination(keyword, statusBO.equals("")?null:Integer.valueOf(statusBO),
					statusPR.equals("")?null:Integer.valueOf(statusPR), start, length, dateStart, dateEnd, roleUser, ListorganisasiId);
			Long size = purchaseRequestSession.getListPaginationSize(keyword, statusBO.equals("")?null:Integer.valueOf(statusBO), statusPR.equals("")?null:Integer.valueOf(statusPR), dateStart, dateEnd, roleUser, ListorganisasiId);
			for(PurchaseRequest pr : purchaseRequestList) {
				pr.setIsAvailable(true);
			}
			Map<String, Object> result = new HashMap<>();
			result.put("draw", draw);
			result.put("data", purchaseRequestList);
			result.put("recordsTotal", size);
			result.put("recordsFiltered", size);
			return Response.ok(result).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
}
