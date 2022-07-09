package id.co.promise.procurement.approval;

import java.sql.SQLException;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import erp.service.BookingOrderInterfacingService;
import id.co.promise.procurement.DTO.ApprovalDoProcessDTO;
import id.co.promise.procurement.DTO.VendorApprovalDTO;
import id.co.promise.procurement.catalog.MasterCatalogServices;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogContractDetail;
import id.co.promise.procurement.catalog.entity.CatalogHistory;
import id.co.promise.procurement.catalog.session.CatalogContractDetailSession;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BankVendorDraft;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.DokumenRegistrasiVendorDraft;
import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.KeuanganVendorDraft;
import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PengalamanVendorDraft;
import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.PeralatanVendorDraft;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseOrderTerm;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SegmentasiVendorDraft;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorDraft;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorPICDraft;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.entity.VendorProfileDraft;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.VendorSKDDraft;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderTermSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.vendor.BankVendorDraftSession;
import id.co.promise.procurement.vendor.BankVendorSession;
import id.co.promise.procurement.vendor.DokumenRegistrasiVendorDraftSession;
import id.co.promise.procurement.vendor.DokumenRegistrasiVendorSession;
import id.co.promise.procurement.vendor.KeuanganVendorDraftSession;
import id.co.promise.procurement.vendor.KeuanganVendorSession;
import id.co.promise.procurement.vendor.PengalamanVendorDraftSession;
import id.co.promise.procurement.vendor.PengalamanVendorSession;
import id.co.promise.procurement.vendor.PeralatanVendorDraftSession;
import id.co.promise.procurement.vendor.PeralatanVendorSession;
import id.co.promise.procurement.vendor.SegmentasiVendorDraftSession;
import id.co.promise.procurement.vendor.SegmentasiVendorSession;
import id.co.promise.procurement.vendor.VendorDraftSession;
import id.co.promise.procurement.vendor.VendorPICDraftSession;
import id.co.promise.procurement.vendor.VendorPICSession;
import id.co.promise.procurement.vendor.VendorProfileDraftSession;
import id.co.promise.procurement.vendor.VendorProfileSession;
import id.co.promise.procurement.vendor.VendorSKDDraftSession;
import id.co.promise.procurement.vendor.VendorSKDSession;
import id.co.promise.procurement.vendor.VendorSession;
import sap.interfacing.soap.pr.SapPrFunction;

@Stateless
@Path(value = "/procurement/approvalProcessServices")
@TransactionManagement(TransactionManagementType.BEAN)
@Produces(MediaType.APPLICATION_JSON)
public class ApprovalProcessServices {

	final static Logger log = Logger.getLogger(ApprovalProcessServices.class);
	final static CustomResponseMessage message = CustomResponse
			.getCustomResponseMessage();
	
	@Resource
	private UserTransaction userTransaction;
	
	@EJB
	ApprovalSession approvalSession;
	
	@EJB
	SapPrFunction sapPrFunction;

	@EJB
	ApprovalTypeSession approvalTypeSession;

	@EJB
	ApprovalLevelSession approvalLevelSession;

	@EJB
	ApprovalProcessSession approvalProcessSession;

	@EJB
	ApprovalProcessTypeSession approvalProcessTypeSession;

	@EJB
	OrganisasiSession organisasiSession;

	@EJB
	JabatanSession jabatanSession;

	@EJB
	TahapanSession tahapanSession;

	@EJB
	UserSession userSession;
	
	@EJB
	RoleUserSession roleUserSession;

	@EJB
	TokenSession tokenSession;
	
	@EJB
	VendorProfileDraftSession vendorProfileDraftSession;
	
	@EJB
	SegmentasiVendorDraftSession segmentasiVendorDraftSession;
	
	@EJB
	PengalamanVendorDraftSession pengalamanVendorDraftSession;
	
	@EJB
	VendorPICDraftSession vendorPICDraftSession;
	
	@EJB
	PurchaseOrderTermSession purchaseOrderTermSession;
	
	@EJB
	private VendorSession vendorSession;
	
	@EJB
	private BankVendorSession bankVendorSession;
	
	@EJB
	BankVendorDraftSession bankVendorDraftSession;
	
	@EJB
	VendorDraftSession vendorDraftSession;
	
	@EJB
	VendorPICSession vendorPICSession;
	
	@EJB
	VendorProfileSession vendorProfileSession;
	
	@EJB
	SegmentasiVendorSession segmentasiVendorSession;
	
	@EJB
	PengalamanVendorSession pengalamanVendorSession;
	
	@EJB
	KeuanganVendorSession keuanganVendorSession;
	
	@EJB
	KeuanganVendorDraftSession keuanganVendorDraftSession;
	
	@EJB
	PeralatanVendorSession peralatanVendorSession;
	
	@EJB
	EmailNotificationSession emailNotificationSession;
	
	@EJB
	PeralatanVendorDraftSession peralatanVendorDraftSession;
	
	@EJB
	DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;
	
	@EJB
	DokumenRegistrasiVendorDraftSession dokumenRegistrasiVendorDraftSession;
	
	@EJB
	private VendorSKDSession vendorSkdSession;
	
	@EJB
	private PurchaseOrderSession purchaseOrderSession;
	
	@EJB
	private VendorSKDDraftSession vendorSkdDraftSession;
	
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	
	@EJB 
	private BookingOrderInterfacingService bookingOrderInterfacingConsume;
	
	@EJB
	private MasterCatalogServices masterCatalogServices;
	
	@EJB
	private CatalogSession catalogSession;
	
	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;
	
	@EJB
	CatalogContractDetailSession catalogContractDetailSession;
	
//	Gson gson = new Gson();
	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
			.create();
	
	@Path("/findCostAllocation/{coa}")
	@GET
	public List<AlokasiAnggaran> findCostAllocation(@PathParam("coa")String coa){
		return approvalProcessSession.findCostAllocation(coa);
	}
	
	@Path("/findByUserGroupAndActive")
	@GET
	public List<ApprovalProcessDTO> findByUserGroupAndActive(@HeaderParam("Authorization") String token){
		return approvalProcessSession.findByUserGroupAndActive(tokenSession.findByToken(token));
	}
	
	@Path("/get-my-worklist")
	@GET
	public List<ApprovalProcessDTO> getMyWorklist( @HeaderParam("Authorization") String tokenStr){
		Token token = tokenSession.findByToken(tokenStr);
			return approvalProcessSession.getMyWorkList(token);
	}
	
	@Path("/getLogUserList")
	@POST
	public List<ApprovalProcess> getLogUserList(Integer bookingOrderId){
			return approvalProcessSession.getLogUserList(bookingOrderId);
	}
	
	@Path("/getApprovalInternalLogList")
	@POST
	public List<ApprovalProcess> getApprovalInternalLogList(Integer approvalProcessTypeId) {
		return approvalProcessSession.getApprovalInternalLogList(approvalProcessTypeId);
	}
	
	@Path ("/getVendorData/{vendorId}")
	@GET
	public VendorApprovalDTO getVendorData (@PathParam ("vendorId") Integer vendorId) { 
		
		VendorApprovalDTO vendorApprovalDTO = new VendorApprovalDTO();

		Vendor vendor= vendorSession.getVendorById(vendorId);
		vendorApprovalDTO.setVendor(vendor);
		vendorApprovalDTO.setUser(userSession.getUser(vendor.getUser()));
		vendorApprovalDTO.setVendorProfile(vendorProfileSession.getVendorProfileByVendor(vendorId));
		vendorApprovalDTO.setVendorProfileDraft(vendorProfileDraftSession.getVendorProfileDraftByVendor(vendorId));
		vendorApprovalDTO.setVendorPIC(vendorPICSession.getVendorPICByVendorId(vendorId)); 
		vendorApprovalDTO.setVendorPICDraft(vendorPICDraftSession.getVendorPICDraftByVendorId(vendorId)); 
		vendorApprovalDTO.setJabatanPenanggungJawabList(jabatanSession.getJabatanList());
		vendorApprovalDTO.setBankVendorList(bankVendorSession.getBankVendorByVendorId(vendorId));
		vendorApprovalDTO.setBankVendorDraftList(bankVendorDraftSession.getBankVendorDraftByVendorId(vendorId));
		vendorApprovalDTO.setPengalamanVendorList(pengalamanVendorSession.getPengalamanVendorByVendorId(vendorId));
		vendorApprovalDTO.setSegmentasiVendorList(segmentasiVendorSession.getSegmentasiVendorByVendorId(vendorId));
		vendorApprovalDTO.setSegmentasiVendorDraft(segmentasiVendorDraftSession.getSegmentasiVendorDraftByVendorId(vendorId));
		vendorApprovalDTO.setPeralatanVendorList(peralatanVendorSession.getPeralatanVendorByVendorId(vendorId));
		vendorApprovalDTO.setPeralatanVendorDraft(peralatanVendorDraftSession.getPeralatanVendorDraftByVendorId(vendorId));
		vendorApprovalDTO.setKeuanganVendorList(keuanganVendorSession.getKeuanganVendorByVendorId(vendorId));
		vendorApprovalDTO.setKeuanganVendorDraft(keuanganVendorDraftSession.getKeuanganVendorDraftByVendorId(vendorId));
		
		
		
		String vendorRegistrasiForm = "Vendor Registrasi Form"; 
		String salinanAktePendirianPerusahaanDanPerubahan ="Salinan Akte Pendirian Perusahaan dan Perubahan - Perubahannya";
		String tandaDaftarPerusahaan ="Salinan Tanda Daftar Perusahaan (TDP)";
		String suratIjinUsaha = "Salinan Surat Ijin Usaha (SIUP SIUJK)";
		String dokumenPKS = "Dokumen PKS";
		String dokumenSPR = "Dokumen SPR";
		String dokumenSPB = "Dokumen SPB";
		String dokumenSKB = "Surat Keterangan Bebas";
		String buktiFisikPerusahaan ="Bukti Fisik Perusahaan"; 
		String dokumenQuality ="Dokumen Quality yang dimiliki";
		String dokumenTeknik  ="Dokumen Teknik";
		
		//check apakah dokumen draft hasil duplicate ato edit
		DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft=dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorId(vendorId).get(0);
		if(dokumenRegistrasiVendorDraft.getIsDuplicate().equals(1)){
			vendorApprovalDTO.setIsDraftDuplicate(true);
		}else{
			vendorApprovalDTO.setIsDraftDuplicate(false);
		}
		
		vendorApprovalDTO.setDataDokRegForm(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, vendorRegistrasiForm));
		vendorApprovalDTO.setDataDokAkte(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, salinanAktePendirianPerusahaanDanPerubahan));
		vendorApprovalDTO.setDataDokSalinan(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, tandaDaftarPerusahaan));
		vendorApprovalDTO.setDataDokSiup(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, suratIjinUsaha));
		vendorApprovalDTO.setDataDokPKS(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, dokumenPKS));
		vendorApprovalDTO.setDataDokSPR(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, dokumenSPR));
		vendorApprovalDTO.setDataDokSPB(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, dokumenSPB));
		vendorApprovalDTO.setDataDokBuktiFisik(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, buktiFisikPerusahaan ));
		vendorApprovalDTO.setDataDokQuality(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, dokumenQuality ));
		vendorApprovalDTO.setDataDokTeknik(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, dokumenTeknik ));
		vendorApprovalDTO.setDataDokSKB(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorAndSubject(vendorId, dokumenSKB ));
		vendorApprovalDTO.setSKBHistoryList(dokumenRegistrasiVendorSession.getHistoryDokumenRegistrasiVendor(vendorId, dokumenSKB));
		
		vendorApprovalDTO.setDataDokRegFormDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, vendorRegistrasiForm));
		vendorApprovalDTO.setDataDokAkteDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, salinanAktePendirianPerusahaanDanPerubahan));
		vendorApprovalDTO.setDataDokSalinanDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, tandaDaftarPerusahaan));
		vendorApprovalDTO.setDataDokSiupDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, suratIjinUsaha));
		vendorApprovalDTO.setDataDokPKSDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, dokumenPKS));
		vendorApprovalDTO.setDataDokSPRDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, dokumenSPR));
		vendorApprovalDTO.setDataDokSPBDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, dokumenSPB));
		vendorApprovalDTO.setDataDokBuktiFisikDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, buktiFisikPerusahaan ));
		vendorApprovalDTO.setDataDokQualityDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, dokumenQuality ));
		vendorApprovalDTO.setDataDokTeknikDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, dokumenTeknik ));
		vendorApprovalDTO.setDataDokSKBDraft(dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendorId, dokumenSKB ));

		vendorApprovalDTO.setVendorSkd(vendorSkdSession.getVendorSkdByVendor(vendorId));
		vendorApprovalDTO.setVendorSkdDraft(vendorSkdDraftSession.getVendorSkdDraftByVendor(vendorId));
		
		String pelanggan = "PELANGGAN";
		String mitra = "MITRA";
		String inProgress = "INPROGRESS";
		
		vendorApprovalDTO.setPengalamanPelangganVendorList(pengalamanVendorSession.getPengalamanVendorByVendorIdAndTipePengalaman(vendorId, pelanggan));
		vendorApprovalDTO.setPengalamanMitraVendorList(pengalamanVendorSession.getPengalamanVendorByVendorIdAndTipePengalaman(vendorId, mitra));
		vendorApprovalDTO.setPengalamanInProgressVendorList(pengalamanVendorSession.getPengalamanVendorByVendorIdAndTipePengalaman(vendorId, inProgress));
		
		vendorApprovalDTO.setPengalamanPelangganVendorDraftList(pengalamanVendorDraftSession.getPengalamanVendorDraftByVendorIdAndTipePengalaman(vendorId, pelanggan));
		vendorApprovalDTO.setPengalamanMitraVendorDraftList(pengalamanVendorDraftSession.getPengalamanVendorDraftByVendorIdAndTipePengalaman(vendorId, mitra));
		vendorApprovalDTO.setPengalamanInProgressVendorDraftList(pengalamanVendorDraftSession.getPengalamanVendorDraftByVendorIdAndTipePengalaman(vendorId, inProgress));
	
		return vendorApprovalDTO;
	}
	
	@Path("/findPurchaseRequest/{id}")
	@GET
	public PurchaseRequest findPurchaseRequest(@PathParam("id")int id){
		return approvalProcessSession.findPurchaseRequestById(id);
	}
	
	@Path("/findPurchaseRequestItemByPR/{prId}")
	@GET
	public List<ApprovalProcessPRItemCostValidationDTO> findPurchaseRequestItemByPR(@PathParam("prId")int prId){
		return approvalProcessSession.findPurchaseRequestItemByPR(prId);
	}
	
	@Path("/findPurchaseOrder/{id}")
	@GET
	public PurchaseOrder findPurchaseOrder(@PathParam("id")int id){
		return approvalProcessSession.findPurchaseOrderById(id);
	}
	
	@Path("/findPurchaseOrderItemByPO/{id}")
	@GET
	public List<PurchaseOrderItem> findPurchaseOrderItemByPO(@PathParam("id")int id){
		return approvalProcessSession.findPurchaseOrderItemByPO(id);
	}
	
	@Path("/findPurchaseOrderTermByPO/{id}")
	@GET
	public List<PurchaseOrderTerm> findPurchaseOrderTermByPO(@PathParam("id")int id){
		return purchaseOrderTermSession.getPurchaseOrderByPO(id);
	}
	
	@Path("/findVendor/{id}")
	@GET
	public Vendor findVendor(@PathParam("id")int id){
		return approvalProcessSession.findVendorById(id);
	}
	
	@Path("/findBlacklistVendor/{id}")
	@GET
	public Vendor findBlacklistVendor(@PathParam("id")int id){
		return vendorSession.find(id);
	}

	@Path("/findByTahapanAndId/{tahapan}/{id}")
	@GET
	public ApprovalProcessType findByTahapanAndId(@PathParam("tahapan")String tahapan, @PathParam("id")int id){
		return approvalProcessTypeSession.findByTahapanAndValueId(tahapan, id);
	}
	
	@Path("/findByTahapan/{tahapan}")
	@GET
	public ApprovalProcessType findByTahapan(@PathParam("tahapan")int tahapan){
		return approvalProcessTypeSession.findByTahapan(tahapan);
	}
	
	@Path("/findByApprovalProcessType/{id}")
	@GET
	public List<ApprovalProcess> findByApprovalProcessType(@PathParam("id")int id){
		return approvalProcessSession.findByApprovalProcessType(id);
	}
	
	@Path("/findByApprovalProcessTypeAndStatus/{id}")
	@GET
	public List<ApprovalProcess> findByApprovalProcessTypeAndStatus(@PathParam("id")int id){
		return approvalProcessSession.findByApprovalProcessTypeAndStatus(id);
	}
	
	@Path("/findByApprovalProcessTypeAndAllStatus/{id}")
	@GET
	public List<ApprovalProcess> findByApprovalProcessTypeAndAllStatus(@PathParam("id")int id){
		return approvalProcessSession.findByApprovalProcessTypeAndAllStatus(id);
	}
		
	@Path("/findRoleUser/{id}")
	@GET
	public List<RoleUser> findRoleUser(@PathParam("id")int id){
		return roleUserSession.getRoleUserByUserId(id);
	}
		
	@Path("/updateStatus")
	@POST
	public ApprovalProcess updateStatus(@FormParam("approvalProcessId")int approvalProcessId, @FormParam("status")int status, 
			@FormParam("note")String note,
			@HeaderParam("Authorization") String token){
		return approvalProcessSession.updateStatus(approvalProcessId, status, note, tokenSession.findByToken(token));
	}
	
	@Path("/getApprovalProcessList")
	@GET
	public List<ApprovalProcess> getList() throws ClassNotFoundException,
			SecurityException, NoSuchFieldException {
		// Gson gson = new GsonBuilder().setExclusionStrategies(
		// new PromiseCustomExclusionStrategy(
		// "id.co.promise.procurement.approval.ApprovalProcessServices"))
		// // .serializeNulls() <-- uncomment to serialize NULL fields as well
		// .create();

//		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
//				.create();

		List<ApprovalProcess> apl = approvalProcessSession.getList();
		log.info("JSONNNNNNNN   " + gson.toJson(apl));

		User user = userSession.find(1);
		log.info(user.getId()+" BABABABABA  " + gson.toJson(user));
		return apl;
	}

	@Path("/createByMasterApproval")
	@POST
	public Response createByMasterApproval(
			@FormParam("approvalId") Integer approvalId,
			@FormParam("nomorReferensi") Integer nomorReferensi,
			@FormParam("tahapanId") Integer tahapanId,
			@FormParam("keterangan") String keterangan,
			@HeaderParam("Authorization") String token) {
		Token tokenA = tokenSession.findByToken(token);
		Integer appUserId = tokenA.getUser().getId();

		ApprovalProcessType approvalProcessType = new ApprovalProcessType();

		try {

			if (approvalId == null || approvalId == 0) {
				throw new Exception("Bad request!");
			} else {
				try {
					Tahapan tahapan = tahapanSession.find(tahapanId);
					approvalProcessType.setValueId(nomorReferensi);
					approvalProcessType.setTahapan(tahapan);
					approvalProcessType.setCreated(new Date());
					approvalProcessType.setUserId(appUserId);
					approvalProcessType.setIsDelete(0);
					approvalProcessType = approvalProcessTypeSession.create(
							approvalProcessType, tokenA);

					// Menggunakan approval dari master approval
					List<ApprovalLevel> aplList = approvalLevelSession
							.findByApproval(approvalSession.find(approvalId));
					for (ApprovalLevel apl : aplList) {
						ApprovalProcess ap = new ApprovalProcess();
						ap.setApprovalLevel(apl);
						ap.setGroup(apl.getApproval().getOrganisasi());
						ap.setUser(apl.getUser());
						ap.setSequence(apl.getSequence());
						ap.setThreshold(apl.getThreshold());
						if (apl.getSequence() == 1) {
							ap.setStatus(1);
						} else {
							ap.setStatus(0);
						}
						// =========================================
						ap.setApprovalProcessType(approvalProcessType);
						ap.setCreated(new Date());
						ap.setKeterangan(keterangan);
						ap.setUserId(appUserId);
						ap.setIsDelete(0);
						// create Approval
						ap = approvalProcessSession.create(ap, tokenA);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					// kirim 500 Internal Server Error
					return Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity(message.error(e.getMessage())).build();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST)
					.entity(message.error(e.getMessage())).build();
		}

		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(approvalProcessType)
				.build();
	}

	@SuppressWarnings("unused")
	@Path("/createExcludingFromMasterApproval")
	@POST
	public Response createExcludingFromMasterApproval(String jsonString,
			@HeaderParam("Authorization") String token) {
		ApprovalProcess ap = new ApprovalProcess();
		ApprovalProcessParamHttp apParam;

		try {
			apParam = gson.fromJson(jsonString, ApprovalProcessParamHttp.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST)
					.entity(message.error(e.getMessage())).build();
		}

		try {
			Token tokenA = tokenSession.findByToken(token);
			Integer appUserId = tokenA.getUser().getId();
			ApprovalProcessType approvalProcessType = approvalProcessTypeSession
					.find(apParam.approvalProcessTypeId);
			ap.setApprovalProcessType(approvalProcessType);
			ap.setCreated(new Date());
			ap.setKeterangan(apParam.keterangan);

			ap.setUserId(appUserId);
			ap.setIsDelete(0);
			ap.setUserId(appUserId);

			// Tidak menggunakan approval dari master approval
			if (apParam.isOtherApproval) {
				if (apParam.isJabatan) {
					Jabatan jabatan = jabatanSession
							.find(apParam.idUserOrJabatan);
				} else {
					User user = userSession.find(apParam.idUserOrJabatan);
					ap.setUser(user);

				}
				ap.setStatus(1);
			}
			// else { // Menggunakan approval dari master approval
			// List<ApprovalLevel> aplList = approvalLevelSession
			// .findByApproval(approvalSession
			// .find(apParam.approvalId));
			// for (ApprovalLevel apl : aplList) {
			// ap.setApprovalLevel(apl);
			// ap.setGroup(apl.getGroup());
			// ap.setUser(apl.getUser());
			// ap.setSequence(apl.getSequence());
			// ap.setThreshold(apl.getThreshold());
			// if (apl.getSequence() == 1) {
			// ap.setStatus(1);
			// } else {
			// ap.setStatus(0);
			// }
			// }

			// if (apParam.approvalLevelId != null && apParam.approvalLevelId !=
			// 0) {
			// ApprovalLevel apl;
			// apl = approvalLevelSession.find(apParam.approvalLevelId);
			// ap.setApprovalLevel(apl);
			// ap.setGroup(apl.getGroup());
			// ap.setUser(apl.getUser());
			// ap.setSequence(apl.getSequence());
			// ap.setThreshold(apl.getThreshold());
			// } else if (apParam.userId != null && apParam.userId != 0) {
			// User user = userSession.find(apParam.userId);
			// ap.setUser(user);
			// }

			ap.setCreated(new Date());
			// create Approval
			ap = approvalProcessSession.create(ap, tokenA);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(ap).build();
	}

	@Path("/edit")
	@POST
	public Response edit(
			@FormParam("approvalProcessId") Integer approvalProcessId,
			@FormParam("approvalLevelId") Integer approvalLevelId,
			@FormParam("groupId") Integer groupId,
			@FormParam("userId") Integer userId,
			@FormParam("sequence") Integer sequence,
			@FormParam("threshold") Double threshold,
			@FormParam("keterangan") String keterangan,
			@FormParam("status") Integer status,
			@HeaderParam("Authorization") String token) {
		try {
			if (approvalProcessId == null || approvalProcessId == 0) {
				throw new Exception("Bad Request!!!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST)
					.entity(message.error(e.getMessage())).build();
		}
		ApprovalProcess approvalProcess = null;
		try {
			Token tokenA = tokenSession.findByToken(token);
			Integer appUserId = tokenA.getUser().getId();
			approvalProcess = approvalProcessSession.find(approvalProcessId);

			// Edit ApprovalProcess
			approvalProcess.setUpdated(new Date());
			approvalProcess.setUserId(appUserId);
			approvalProcess.setApprovalLevel(approvalLevelSession
					.find(approvalLevelId));
			approvalProcess.setGroup(organisasiSession.find(groupId));
			approvalProcess.setUser(userSession.find(userId));
			approvalProcess.setSequence(sequence);
			approvalProcess.setThreshold(threshold);
			approvalProcess.setKeterangan(keterangan);
			approvalProcess.setStatus(status);
			approvalProcess = approvalProcessSession.update(approvalProcess,
					tokenA);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(approvalProcess).build();
	}

	@Path("/delete")
	@POST
	public Response delete(
			@FormParam("approvalProcessId") Integer approvalProcessId,
			@HeaderParam("Authorization") String token) {
		try {
			if (approvalProcessId == null || approvalProcessId == 0) {
				throw new Exception("Bad Request!!!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST)
					.entity(message.error(e.getMessage())).build();
		}
		ApprovalProcess approvalProcess = null;
		try {
			Token tokenA = tokenSession.findByToken(token);
			Integer appUserId = tokenA.getUser().getId();
			approvalProcess = approvalProcessSession.find(approvalProcessId);

			// Delete ApprovalProcess
			approvalProcess.setDeleted(new Date());
			approvalProcess.setUserId(appUserId);
			approvalProcess.setIsDelete(1);
			approvalProcess = approvalProcessSession.delete(approvalProcess,
					tokenA);

			// Delete ApprovalProcessType
			ApprovalProcessType approvalProcessType = approvalProcess
					.getApprovalProcessType();
			approvalProcessType.setUserId(appUserId);
			approvalProcessType.setDeleted(new Date());
			approvalProcessType.setIsDelete(1);
			approvalProcessType = approvalProcessTypeSession.delete(
					approvalProcessType, tokenA);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(approvalProcess).build();
	}

	// Method-method operation ===============================================

	@Path("/approve")
	@POST
	public Response approve(
			@FormParam("approvalProcessId") Integer approvalProcessId,
			@HeaderParam("Authorization") String token) {
		ApprovalProcess ap = null;
		try {
			Token tokenA = tokenSession.findByToken(token);
			Integer appUserId = tokenA.getUser().getId();

			ap = approvalProcessSession.find(approvalProcessId);
			ApprovalProcessType approvalProcessType = ap
					.getApprovalProcessType();

			ap.setUserId(appUserId);
			ap.setUpdated(new Date());
			ap.setStatus(3); // Approval di-approve
			approvalProcessSession.update(ap, tokenA);

			Integer sequence = ap.getSequence();
			ApprovalProcess apNext = approvalProcessSession
					.findByApprovalProcessTypeAndSequence(approvalProcessType,
							++sequence);
			if (apNext != null) {
				apNext.setUserId(appUserId);
				apNext.setUpdated(new Date());
				apNext.setStatus(3); // Approval level berikutnya diset active
				approvalProcessSession.update(apNext, tokenA);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(ap).build();
	}
	
	@SuppressWarnings("unused")
	@Path("/hold")
	@POST
	public Response hold(
			@FormParam("approvalProcessId") Integer approvalProcessId,
			@HeaderParam("Authorization") String token) {
		ApprovalProcess ap = null;
		try {
			Token tokenA = tokenSession.findByToken(token);
			Integer appUserId = tokenA.getUser().getId();

			ap = approvalProcessSession.find(approvalProcessId);
			ApprovalProcessType approvalProcessType = ap.getApprovalProcessType();

			ap.setUserId(appUserId);
			ap.setUpdated(new Date());
			ap.setStatus(4); // Approval di-hold
			approvalProcessSession.update(ap, tokenA);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(ap).build();
	}

	@SuppressWarnings("unused")
	@Path("/reject")
	@POST
	public Response reject(
			@FormParam("approvalProcessId") Integer approvalProcessId,
			@HeaderParam("Authorization") String token) {
		ApprovalProcess ap = null;
		try {
			Token tokenA = tokenSession.findByToken(token);
			Integer appUserId = tokenA.getUser().getId();

			ap = approvalProcessSession.find(approvalProcessId);
			ApprovalProcessType approvalProcessType = ap.getApprovalProcessType();

			ap.setUserId(appUserId);
			ap.setUpdated(new Date());
			ap.setStatus(2); // Approval di-reject
			approvalProcessSession.update(ap, tokenA);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
		// kirim 201 Created (The request has been fulfilled, resulting in the
		// creation of a new resource)
		return Response.status(Status.CREATED).entity(ap).build();
	}

	@SuppressWarnings("unused")
	// Kelas helper
	class ApprovalProcessParamHttp {
		private Boolean isJabatan = false;
		private Boolean isOtherApproval = false;
		private Integer approvalId;
		private Integer idUserOrJabatan;

		// private Integer approvalProcessId;
		private Integer approvalProcessTypeId;

		// private Integer approvalLevelId;
		// protected Integer status;
		protected String keterangan;

		// protected Integer groupId;
		// protected Integer userId;
		// protected Double threshold;
		// protected Integer sequence;

		private ApprovalProcessParamHttp(Integer approvalProcessTypeId,
				Integer approvalId, String keterangan) {
			this.approvalProcessTypeId = approvalProcessTypeId;
			this.approvalId = approvalId;
			this.keterangan = keterangan;
		}

		/*
		 * otherApprovalGroup: 0 = pejabat 1 = nama user 2 = self approve
		 */
		private ApprovalProcessParamHttp(Integer approvalProcessTypeId,
				Integer otherApprovalGroup, Integer idUserOrJabatan,
				String keterangan) {
			this.approvalProcessTypeId = approvalProcessTypeId;
			this.isOtherApproval = true;
			if (otherApprovalGroup == 0) {
				this.isJabatan = true;
			}
			this.idUserOrJabatan = idUserOrJabatan;
			this.keterangan = keterangan;
		}
		// private ApprovalProcessParamHttp(Integer approvalId, Integer user)

		// private ApprovalProcessParamHttp(Integer approvalProcessId) {
		// if (approvalProcessId != 0) {
		// this.approvalProcessId = approvalProcessId;
		// }
		// }

		// private ApprovalProcessParamHttp(Integer approvalProcessId,
		// Integer approvalProcessType, Integer approvalLevelId,
		// Integer status, String keterangan, Integer groupId,
		// Integer userId, Double threshold, Integer sequence) {
		// this.approvalProcessId = approvalProcessId;
		// this.approvalProcessTypeId = approvalProcessType;
		// this.approvalLevelId = approvalLevelId;
		// this.status = status;
		// this.keterangan = keterangan;
		// this.groupId = groupId;
		// this.userId = userId;
		// this.threshold = threshold;
		// this.sequence = sequence;
		// }

	}
	
	@Path("/countByUserGroupAndActive")
	@GET
	public Response countByUserGroupAndActive(@HeaderParam("Authorization") String token){
	
		Map<String,Integer> result = new HashMap<String, Integer>();
		result.put("count", approvalProcessSession.countByUserGroupAndActive(tokenSession.findByToken(token)));
		return Response.status(Status.OK).entity(result).build();
	}

	@SuppressWarnings("unused")
	@Path("/updateEditApproval")
	@POST
	public ApprovalProcessDTO updateEditApproval(ApprovalProcessDTO approvalProcessDTO,
			@HeaderParam("Authorization") String token) {
		
		Integer id = approvalProcessDTO.getId();
		Integer status=approvalProcessDTO.getStatus();
		Integer approvalProcessId=approvalProcessDTO.getApprovalProcessId();
		String note =approvalProcessDTO.getNote();
		/** get token **/
		Token tkn = tokenSession.findByToken(token);
		// get Vendor
		Vendor vendor = vendorSession.find(id);
	
		List <DokumenRegistrasiVendorDraft> dokumenDraftListUpdateCekList=approvalProcessDTO.getDokumenRegistrasiVendorDraftList();
		
		//update dokumen
		for (DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraftUpdate :dokumenDraftListUpdateCekList) {
			DokumenRegistrasiVendorDraft dokumenUpdate =dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorAndSubject(vendor.getId(), dokumenRegistrasiVendorDraftUpdate.getSubject());
			dokumenUpdate.setStatusCeklist(dokumenRegistrasiVendorDraftUpdate.getStatusCeklist());
			dokumenRegistrasiVendorDraftSession.updateDokumenRegistrasiVendorDraft(dokumenUpdate, tokenSession.findByToken(token));		
		}
		//updateDokumenSKD
		VendorSKDDraft vendorSKDDraft = approvalProcessDTO.getVendorSKDDraft();
		if (vendorSKDDraft !=null){
			VendorSKDDraft  vendorSKDDraf= vendorSkdDraftSession .getVendorSkdDraftByVendor(vendor.getId());
			vendorSKDDraf.setStatusCeklist(vendorSKDDraf.getStatusCeklist());
			vendorSkdDraftSession.updateVendorSKDDraft(vendorSKDDraf, tokenSession.findByToken(token));
		}
		
		/** get approval process **/
		ApprovalProcess approvalProcess = approvalProcessSession.find(approvalProcessId);
		if (approvalProcess != null) {
			approvalProcess.setStatus(status);
			approvalProcess.setKeterangan(note);
			approvalProcessSession.update(approvalProcess, tkn);
			
			//Jika di reject, maka semua draft akan hilang
			if ( approvalProcess.getStatus() == 2) {
				List<VendorDraft> vendorDraftList = vendorDraftSession.getVendorDraftByVendorId(vendor.getId());
				VendorProfileDraft vendorProfileDraft= vendorProfileDraftSession.getVendorProfileDraftByVendor(vendor.getId());
				if(vendorDraftList.size()>=1)
				{
					VendorDraft vendorDraft= vendorDraftList.get(0);
					vendorDraft.setIsDelete(1);
					vendorDraftSession.updateVendorDraft(vendorDraft, tokenSession.findByToken(token));
				}
				if(vendorProfileDraft != null){
					vendorProfileDraft.setIsDelete(1);	
					vendorProfileDraftSession.updateVendorProfileDraft(vendorProfileDraft, tokenSession.findByToken(token));					
				}
		        vendor.setIsApproval(2);
				bankVendorDraftSession.deleteBankVendorDraftByVendorId(vendor.getId());	
				vendorPICDraftSession.deleteByVendorId(vendor.getId());
				segmentasiVendorDraftSession.deleteSegmentasiVendorDraftByVendorId(vendor.getId());
				pengalamanVendorDraftSession.deletePengalamanVendorDraftByVendorId(vendor.getId());
				vendorSkdDraftSession.deleteVendorSKDDraftByVendorId(vendor.getId());
		        dokumenRegistrasiVendorDraftSession.deleteDokumenRegistrasiVendorDraftByVendorId(vendor.getId());
		        peralatanVendorDraftSession.deletePeralatanVendorDraftByVendorId(vendor.getId());
		        keuanganVendorDraftSession.deleteKeuanganVendorDraftByVendorId(vendor.getId());
				vendorSession.updateVendor(vendor, tkn);
				List<ApprovalProcess> apStatus = approvalProcessSession.findApprovalProcessByStatus(approvalProcess.getApprovalProcessType().getId(), 1);
				for (ApprovalProcess approvalProcess2 : apStatus) {
					approvalProcess2.setStatus(2);
					vendor.setIsApproval(2);
					vendorSession.updateVendor(vendor, tkn);
					approvalProcessSession.update(approvalProcess2, tkn);
					vendorSession.updateVendor(vendor, tkn);
				}
				emailNotificationSession.getMailGeneratorEditVendorEndReject(vendor);
			}

			// jika status 3(approve), cek lagi level selanjutnya. dan jenis
			// serial
			if (status == 3 && approvalProcess.getApprovalProcessType().getJenis() == 0) {
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
					emailNotificationSession.getMailGeneratorEditVendorForApproval(vendor,apNext.getUser());
				}
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
				int vendorStatus = 1;//ketika udah approval semua
				vendor.setIsApproval(0);//set vendor isapproval jadi 0 untuk membuat jadi standby
				
				
				//Set Isdelete di table main
				
				List <BankVendorDraft> bankVendorDraftList = bankVendorDraftSession.getBankVendorDraftByVendorId(vendor.getId());
				/** Get BankList **/
				if(bankVendorDraftList.size()>=1){
					List <BankVendor> bankVendorList = bankVendorSession.getBankVendorByVendorId(vendor.getId());
					bankVendorSession.deleteBankVendorByVendorId(vendor.getId());

					for (BankVendorDraft bankVendorDraft : bankVendorDraftList){
						BankVendor bankVendorTemp = new BankVendor();
						bankVendorTemp.setAlamatBank(bankVendorDraft.getAlamatBank());
						bankVendorTemp.setBnkt(bankVendorDraft.getBnkt());
						bankVendorTemp.setBnktSequence(bankVendorDraft.getBnktSequence());
						bankVendorTemp.setCabangBank(bankVendorDraft.getCabangBank());
						bankVendorTemp.setCreated(bankVendorDraft.getCreated());
						bankVendorTemp.setKota(bankVendorDraft.getKota());
						bankVendorTemp.setMataUang(bankVendorDraft.getMataUang());
						bankVendorTemp.setNamaBank(bankVendorDraft.getNamaBank());
						bankVendorTemp.setNamaNasabah(bankVendorDraft.getNamaNasabah());
						bankVendorTemp.setNegara(bankVendorDraft.getNegara());
						bankVendorTemp.setNomorRekening(bankVendorDraft.getNomorRekening());
						bankVendorTemp.setVendor(bankVendorDraft.getVendor());
						bankVendorSession.insertBankVendor(bankVendorTemp, tokenSession.findByToken(token)).getId();
					}
					bankVendorDraftSession.deleteBankVendorDraftByVendorId(vendor.getId());	
				}
				/** Get VendorProfile**/
				List<VendorDraft> vendorDraftList = vendorDraftSession.getVendorDraftByVendorId(vendor.getId());
				VendorProfileDraft vendorProfileDraft= vendorProfileDraftSession.getVendorProfileDraftByVendor(vendor.getId());
				List<VendorPICDraft> vendorPICDraftList= vendorPICDraftSession.getVendorPICDraftByVendorId(vendor.getId());
				if(vendorDraftList.size()>=1){
					
					VendorDraft vendorDraft = vendorDraftList.get(0);
					vendor.setNama(vendorDraft.getNama());
					vendor.setEmail(vendorDraft.getEmail());
					vendor.setNpwp(vendorDraft.getNpwp());
					vendor.setNomorTelpon(vendorDraft.getNomorTelpon());
					vendor.setPenanggungJawab(vendorDraft.getPenanggungJawab());
					vendor.setDeskripsi(vendorDraft.getDeskripsi());		
					vendor.setKota(vendorDraft.getKota());
					vendor.setProvinsi(vendorDraft.getProvinsi());
					vendor.setIsPKS(vendorDraft.getIsPKS());
					vendor.setAlamat(vendorDraft.getAlamat());
					vendor.setAfco(vendorDraft.getAfco());
					
					vendorSession.updateVendor(vendor, tokenSession.findByToken(token));
					
					VendorProfile vendorProfile =vendorProfileSession.getVendorProfileByVendor(vendor.getId());
					vendorProfile.setAlamat(vendorProfileDraft.getAlamat());
					vendorProfile.setAlamatNPWP(vendorProfileDraft.getAlamatNPWP());
					vendorProfile.setBussinessArea(vendorProfileDraft.getBussinessArea());
					vendorProfile.setEmail(vendorProfileDraft.getEmail());
					vendorProfile.setEmailContactPerson(vendorProfileDraft.getEmailContactPerson());
					vendorProfile.setFaksimile(vendorProfileDraft.getFaksimile());
					vendorProfile.setHpContactPerson(vendorProfileDraft.getHpContactPerson());
					vendorProfile.setJenisPajakPerusahaan(vendorProfileDraft.getJenisPajakPerusahaan());
					vendorProfile.setJumlahKaryawan(vendorProfileDraft.getJumlahKaryawan());
					vendorProfile.setKodePos(vendorProfileDraft.getKodePos());
					vendorProfile.setKota(vendorProfileDraft.getKota());
					vendorProfile.setKotaNPWP(vendorProfileDraft.getKotaNPWP());
					vendorProfile.setKtpContactPerson(vendorProfileDraft.getKtpContactPerson());
					vendorProfile.setKualifikasiVendor(vendorProfileDraft.getKualifikasiVendor());
					vendorProfile.setNamaContactPerson(vendorProfileDraft.getNamaContactPerson());
					vendorProfile.setNamaNPWP(vendorProfileDraft.getNamaNPWP());
					vendorProfile.setNamaPerusahaan(vendorProfileDraft.getNamaPerusahaan());
					vendorProfile.setNamaSingkatan(vendorProfileDraft.getNamaSingkatan());
					vendorProfile.setNoAktaPendirian(vendorProfileDraft.getNoAktaPendirian());
					vendorProfile.setNoKK(vendorProfileDraft.getNoKK());
					vendorProfile.setNomorPKP(vendorProfileDraft.getNomorPKP());
					vendorProfile.setNpwpPerusahaan(vendorProfileDraft.getNpwpPerusahaan());
					vendorProfile.setPoBox(vendorProfileDraft.getPoBox());
					vendorProfile.setProvinsi(vendorProfileDraft.getProvinsi());
					vendorProfile.setProvinsiNPWP(vendorProfileDraft.getProvinsiNPWP());
					vendorProfile.setTanggalBerdiri(vendorProfileDraft.getTanggalBerdiri());
					vendorProfile.setTelephone(vendorProfileDraft.getTelephone());
					vendorProfile.setTipePerusahaan(vendorProfileDraft.getTipePerusahaan());
					vendorProfile.setTitle(vendorProfileDraft.getTitle());
					vendorProfile.setUnitTerdaftar(vendorProfileDraft.getUnitTerdaftar());
					vendorProfile.setWebsite(vendorProfileDraft.getWebsite());
					
					vendorProfileSession.updateVendorProfile(vendorProfile, tokenSession.findByToken(token));
					
					//delete existing di main
					vendorPICSession.deleteVendorPICByVendorId(vendor.getId());
					
					for(VendorPICDraft vendorPICDraft : vendorPICDraftList){
						VendorPIC vendorPIC = new VendorPIC();
						
						vendorPIC.setEmail(vendorPICDraft.getEmail());
						vendorPIC.setJabatan(vendorPICDraft.getJabatan());
						vendorPIC.setNama(vendorPICDraft.getNama());
						vendorPIC.setVendor(vendor);
						vendorPICSession.insertVendorPIC(vendorPIC, tokenSession.findByToken(token));			
						
					}
					
					vendorPICDraftSession.deleteByVendorId(vendor.getId());
					vendorDraft.setIsDelete(1);
					vendorDraftSession.updateVendorDraft(vendorDraft, tokenSession.findByToken(token));
					vendorProfileDraft.setIsDelete(1);
					vendorProfileDraftSession.updateVendorProfileDraft(vendorProfileDraft, tokenSession.findByToken(token));
					
				}
				/** Get Data Segmentasi**/
				
				List <SegmentasiVendorDraft> segmentasiVendorDraftList = segmentasiVendorDraftSession.getSegmentasiVendorDraftByVendorId(vendor.getId());
				if(segmentasiVendorDraftList.size()>=1){
					segmentasiVendorSession.deleteSegmentasiVendorByVendorId(vendor.getId());
					
					for(SegmentasiVendorDraft segmentasiVendorDraft:segmentasiVendorDraftList){
					SegmentasiVendor segmentasiVendor= new SegmentasiVendor();
					segmentasiVendor.setVendor(vendor);
					segmentasiVendor.setAsosiasi(segmentasiVendorDraft.getAsosiasi());
					segmentasiVendor.setEmail(segmentasiVendorDraft.getEmail());
					segmentasiVendor.setJabatan(segmentasiVendorDraft.getJabatan());
					segmentasiVendor.setNomor(segmentasiVendorDraft.getNomor());
					segmentasiVendor.setSubBidangUsaha(segmentasiVendorDraft.getSubBidangUsaha());
					segmentasiVendor.setTanggalBerakhir(segmentasiVendorDraft.getTanggalBerakhir());
					segmentasiVendor.setTanggalMulai(segmentasiVendorDraft.getTanggalMulai());
					
					segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor, tokenSession.findByToken(token));
					}
				
				segmentasiVendorDraftSession.deleteSegmentasiVendorDraftByVendorId(vendor.getId());
				}
				
				/** Get Data Pengalaman**/
				
				List<PengalamanVendorDraft> pengalamanVendorDraftList = pengalamanVendorDraftSession.getPengalamanVendorDraftById(vendor.getId());
				
				if(pengalamanVendorDraftList.size()>=1){
					
					pengalamanVendorSession.deletePengalamanVendorByVendorId(vendor.getId());
					
					for(PengalamanVendorDraft pengalamanVendorDraft:pengalamanVendorDraftList){
					PengalamanVendor pengalamanVendor = new PengalamanVendor();
					
						pengalamanVendor.setBidangUsaha(pengalamanVendorDraft.getBidangUsaha());
						pengalamanVendor.setFileName(pengalamanVendorDraft.getFileName());
						pengalamanVendor.setFileSize(pengalamanVendorDraft.getFileSize());
						pengalamanVendor.setLokasiPekerjaan(pengalamanVendorDraft.getLokasiPekerjaan());
						pengalamanVendor.setMataUang(pengalamanVendorDraft.getMataUang());
						pengalamanVendor.setMulaiKerjasama(pengalamanVendorDraft.getMulaiKerjasama());
						pengalamanVendor.setNamaPekerjaan(pengalamanVendorDraft.getNamaPekerjaan());
						pengalamanVendor.setNilaiKontrak(pengalamanVendorDraft.getNilaiKontrak());
						pengalamanVendor.setPathFile(pengalamanVendorDraft.getPathFile());
						pengalamanVendor.setTipePengalaman(pengalamanVendorDraft.getTipePengalaman());
						pengalamanVendor.setVendor(vendor);
						
						pengalamanVendorSession.insertPengalamanVendor(pengalamanVendor, tokenSession.findByToken(token));
					}
					pengalamanVendorDraftSession.deletePengalamanVendorDraftByVendorId(vendor.getId());
				}
				
						/**Get KeuanganList**/
						        List<KeuanganVendorDraft> keuanganVendorDraftList  = keuanganVendorDraftSession.getKeuanganVendorDraftByVendorId(vendor.getId());
						        if(keuanganVendorDraftList.size()>=1){
						          keuanganVendorSession.deleteKeuanganVendorByVendorId(vendor.getId());
						          
						          for (KeuanganVendorDraft keuanganVendorDraft : keuanganVendorDraftList){
						            KeuanganVendor keuanganVendorTemp = new KeuanganVendor();
						            keuanganVendorTemp.setAktivaLainnya(keuanganVendorDraft.getAktivaLainnya());
						            keuanganVendorTemp.setCreated(keuanganVendorDraft.getCreated());
						            keuanganVendorTemp.setBank(keuanganVendorDraft.getBank());
						            keuanganVendorTemp.setGedungGedung(keuanganVendorDraft.getGedungGedung());
						            keuanganVendorTemp.setHutangDagang(keuanganVendorDraft.getHutangDagang());
						            keuanganVendorTemp.setHutangJangkaPanjang(keuanganVendorDraft.getHutangJangkaPanjang());
						            keuanganVendorTemp.setHutangLainnya(keuanganVendorDraft.getHutangLainnya());
						            keuanganVendorTemp.setHutangPajak(keuanganVendorDraft.getHutangPajak());
						            keuanganVendorTemp.setInventaris(keuanganVendorDraft.getInventaris());
						            keuanganVendorTemp.setKas(keuanganVendorDraft.getKas());
						            keuanganVendorTemp.setKekayaanBersih(keuanganVendorDraft.getKekayaanBersih());
						            keuanganVendorTemp.setNamaAudit(keuanganVendorDraft.getNamaAudit());
						            keuanganVendorTemp.setNomorAudit(keuanganVendorDraft.getNomorAudit());
						            keuanganVendorTemp.setPekerjaanDalamProses(keuanganVendorDraft.getPekerjaanDalamProses());
						            keuanganVendorTemp.setPeralatanDanMesin(keuanganVendorDraft.getPeralatanDanMesin());
						            keuanganVendorTemp.setPersediaanBarang(keuanganVendorDraft.getPersediaanBarang());
						            keuanganVendorTemp.setTahunKeuangan(keuanganVendorDraft.getTahunKeuangan());
						            keuanganVendorTemp.setTanggalAudit(keuanganVendorDraft.getTanggalAudit());
						            keuanganVendorTemp.setTotalAktiva(keuanganVendorDraft.getTotalAktiva());
						            keuanganVendorTemp.setTotalAktivaLancar(keuanganVendorDraft.getTotalAktivaLancar());
						            keuanganVendorTemp.setTotalAktivaTetap(keuanganVendorDraft.getTotalAktivaTetap());
						            keuanganVendorTemp.setTotalHutangJangkaPendek(keuanganVendorDraft.getTotalHutangJangkaPendek());
						            keuanganVendorTemp.setTotalPasiva(keuanganVendorDraft.getTotalPasiva());
						            keuanganVendorTemp.setTotalPiutang(keuanganVendorDraft.getTotalPiutang());
						            keuanganVendorTemp.setUserId(keuanganVendorDraft.getUserId());
						            keuanganVendorTemp.setVendor(keuanganVendorDraft.getVendor());
						            keuanganVendorSession.insertKeuanganVendor(keuanganVendorTemp, tokenSession.findByToken(token)).getId();
						          }
						          keuanganVendorDraftSession.deleteKeuanganVendorDraftByVendorId(vendor.getId());
						        }
						        /**Get Peralatan Vendorlist**/
						        List<PeralatanVendorDraft> peralatanVendorDraftList = peralatanVendorDraftSession.getPeralatanVendorDraftByVendorId(vendor.getId());
						        if(peralatanVendorDraftList.size()>=1){
						          peralatanVendorSession.deletePeralatanVendorByVendorId(vendor.getId());
						          
						          for(PeralatanVendorDraft peralatanVendorDraft : peralatanVendorDraftList){
						            PeralatanVendor peralatanVendorTemp = new PeralatanVendor();
						            peralatanVendorTemp.setBuktiKepemilikan(peralatanVendorDraft.getBuktiKepemilikan());
						            peralatanVendorTemp.setCreated(peralatanVendorDraft.getCreated());
						            peralatanVendorTemp.setFileNameBuktiKepemilikan(peralatanVendorDraft.getFileNameBuktiKepemilikan());
						            peralatanVendorTemp.setFileNameGambarPeralatan(peralatanVendorDraft.getFileNameGambarPeralatan());
						            peralatanVendorTemp.setFileSizeBuktiKepemilikan(peralatanVendorDraft.getFileSizeBuktiKepemilikan());
						            peralatanVendorTemp.setFileSizeGambarPeralatan(peralatanVendorDraft.getFileSizeGambarPeralatan());

						
						            peralatanVendorTemp.setJenis(peralatanVendorDraft.getJenis());
						            peralatanVendorTemp.setJumlah(peralatanVendorDraft.getJumlah());
						            peralatanVendorTemp.setKapasitas(peralatanVendorDraft.getKapasitas());
						            peralatanVendorTemp.setKondisi(peralatanVendorDraft.getKondisi());
						            peralatanVendorTemp.setLokasi(peralatanVendorDraft.getLokasi());
						            peralatanVendorTemp.setMerk(peralatanVendorDraft.getMerk());
						            peralatanVendorTemp.setPathFileBuktiKepemilikan(peralatanVendorDraft.getPathFileBuktiKepemilikan());
						            peralatanVendorTemp.setPathFileGambarPeralatan(peralatanVendorDraft.getPathFileGambarPeralatan());
						            peralatanVendorTemp.setTahunPembuatan(peralatanVendorDraft.getTahunPembuatan());
						            peralatanVendorTemp.setUserId(peralatanVendorDraft.getUserId());
						            peralatanVendorTemp.setVendor(peralatanVendorDraft.getVendor());
						            peralatanVendorSession.insertPeralatanVendor(peralatanVendorTemp, tokenSession.findByToken(token)).getId();
						          }
						          peralatanVendorDraftSession.deletePeralatanVendorDraftByVendorId(vendor.getId());
						        }
						        /**Get Dokumen RegistrasiList**/
						        List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList = dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorId(vendor.getId());    
						        if(dokumenRegistrasiVendorDraftList.size()>=1){
						          dokumenRegistrasiVendorSession.deleteDokumenRegistrasiVendorByVendorId(vendor.getId());
						          
						          for(DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft : dokumenRegistrasiVendorDraftList){
						            DokumenRegistrasiVendor dokumenRegistrasiVendorTemp = new DokumenRegistrasiVendor();
						            dokumenRegistrasiVendorTemp.setCreated(dokumenRegistrasiVendorDraft.getCreated());
						            dokumenRegistrasiVendorTemp.setFileName(dokumenRegistrasiVendorDraft.getFileName());
						            dokumenRegistrasiVendorTemp.setFileSize(dokumenRegistrasiVendorDraft.getFileSize());
						            dokumenRegistrasiVendorTemp.setNamaDokumen(dokumenRegistrasiVendorDraft.getNamaDokumen());
						            dokumenRegistrasiVendorTemp.setPathFile(dokumenRegistrasiVendorDraft.getPathFile());
						            dokumenRegistrasiVendorTemp.setStatusCeklist(dokumenRegistrasiVendorDraft.getStatusCeklist());
						            dokumenRegistrasiVendorTemp.setSubject(dokumenRegistrasiVendorDraft.getSubject());
						            dokumenRegistrasiVendorTemp.setTanggalBerakhir(dokumenRegistrasiVendorDraft.getTanggalBerakhir());
						            dokumenRegistrasiVendorTemp.setTanggalTerbit(dokumenRegistrasiVendorDraft.getTanggalTerbit());
						            dokumenRegistrasiVendorTemp.setUserId(dokumenRegistrasiVendorDraft.getUserId());
						            dokumenRegistrasiVendorTemp.setVendor(dokumenRegistrasiVendorDraft.getVendor());
						            dokumenRegistrasiVendorSession.insertDokumenRegistrasiVendor(dokumenRegistrasiVendorTemp, tokenSession.findByToken(token)).getId();
						          }
						          dokumenRegistrasiVendorDraftSession.deleteDokumenRegistrasiVendorDraftByVendorId(vendor.getId());
						        
						        /**Get VendorSKD**/
						        VendorSKDDraft vendorSKDDraftList= vendorSkdDraftSession.getVendorSkdDraftByVendor(vendor.getId());   
						        if (vendorSKDDraftList != null){
						        	vendorSkdSession.deleteVendorSKDByVendorId(vendor.getId());
						        	VendorSKD vendorSKDTemp = new VendorSKD();
						        	vendorSKDTemp.setCreated(vendorSKDDraftList.getCreated());
						        	vendorSKDTemp.setAlamat(vendorSKDDraftList.getAlamat());
						        	vendorSKDTemp.setFileName(vendorSKDDraftList.getFileName());
						        	vendorSKDTemp.setFileSize(vendorSKDDraftList.getFileSize());
						        	vendorSKDTemp.setNamaDokumen(vendorSKDDraftList.getNamaDokumen());
						        	vendorSKDTemp.setPathFile(vendorSKDDraftList.getPathFile());
						        	vendorSKDTemp.setStatusCeklist(vendorSKDDraftList.getStatusCeklist());
						        	vendorSKDTemp.setTanggalBerakhir(vendorSKDDraftList.getTanggalBerakhir());
						        	vendorSKDTemp.setTanggalTerbit(vendorSKDDraftList.getTanggalTerbit());
						        	vendorSKDTemp.setVendor(vendorSKDDraftList.getVendor());

						        	vendorSkdSession.insertVendorSKD(vendorSKDTemp, tokenSession.findByToken(token));
						        	vendorSkdDraftSession.deleteVendorSKDDraftByVendorId(vendor.getId());
							        }
						        }
							    vendor.setCamelFlag(2);//untuk trigger camel update data vendor
						        vendorSession.updateVendor(vendor, tokenSession.findByToken(token));	
						        emailNotificationSession.getMailGeneratorEditVendorEndSuccess(vendor);
			}
		}

		return approvalProcessDTO;
	}
	
	@Path("/SyncronizeApprovedDate")
	@GET
	public void SyncronizePOApprovedDate() {
		purchaseOrderSession.SyncronizePOApprovedDate();
		purchaseRequestSession.SyncronizePRApprovedDate();
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@Path("/doProcess")
	@POST
	public id.co.promise.procurement.entity.api.Response testProcess(ApprovalDoProcessDTO approvalDoProcessDTO, @HeaderParam("Authorization") String tokenStr) throws SQLException, Exception {
		
		Token tokenObj = tokenSession.findByToken(tokenStr);
		id.co.promise.procurement.entity.api.Response response = null;
		try {
		
			userTransaction.begin();
			ApprovalProcess approvalProcess = approvalProcessSession.find(approvalDoProcessDTO.getApprovalProcessId());
			
			Boolean isValid = approvalProcessSession.doProcess(approvalProcess, approvalDoProcessDTO.getStatus(), tokenObj, approvalDoProcessDTO.getNote());
	
			String responseText=null;
			ApprovalProcess newApprovalProcess = approvalProcessSession.find(approvalDoProcessDTO.getApprovalProcessId());
			ApprovalProcessType approvalProcessType = approvalProcess.getApprovalProcessType();
			if (isValid) {
				if (approvalProcessType.getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_BO )) {
					PurchaseRequest purchaseRequest = purchaseRequestSession.find(approvalProcessType.getValueId());
					if(approvalProcessType.getStatus() == 3) {
						
						Date date =  new Date();
						purchaseRequest.setApprovedDate(date);
						
						// KAI - 20201124
						if (Constant.IS_INTERFACING_SAP) {
							
							response = sapPrFunction.submitPr(purchaseRequest, tokenObj);
							
						} else {
							purchaseRequest.setStatus(Constant.PR_STATUS_READY_TO_CREATE_PO);
							Random rand = new Random();
							Integer min = 1000;
							Integer max = 9999;
							Integer random = rand.nextInt((max - min) + 1) + min;
							
							purchaseRequest.setPrnumber("PR-"+random.toString());	
						}					
						
	//					response = bookingOrderInterfacingConsume.postBookingOrder(purchaseRequest,tokenObj);
	//					if(!response.getStatusText().equalsIgnoreCase("SUCCESS")) {
	//						responseText = response.getStatusText().toLowerCase();
	//						newApprovalProcess.setKeterangan("Response Error from EBS : "+response.getStatusText());
	//						purchaseRequest.setStatus(Constant.PR_STATUS_FAILED_TO_SEND_BO);
	//
	//						approvalProcessSession.update(newApprovalProcess, tokenObj);
	//					}
						
						purchaseRequestSession.update(purchaseRequest, tokenObj);
					}else{
						purchaseRequest.setStatus(Constant.PR_STATUS_REJECT);
						
						/* perubahan KAI 19/01/2021 */
	//					Integer tampQty = new Integer(0);
	//					Double qtyDouble = new Double(0);
	//					List<PurchaseRequestItem> prItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaseRequest.getId());
	//					for(PurchaseRequestItem prItem : prItemList) {
	//						Catalog catalogTamp = new Catalog();
	//						catalogTamp = prItem.getCatalog();
	//						qtyDouble = prItem.getQuantity();
	//						tampQty =catalogTamp.getCurrentStock() + (Integer)qtyDouble.intValue();
	//						catalogTamp.setCurrentStock(tampQty);
	//						
	//						catalogSession.updateCatalog(catalogTamp, tokenObj);
	//					}
						/* perubahan KAI 24/02/2021 [22288]*/
						List<PurchaseRequestItem> prItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaseRequest.getId());
						for(PurchaseRequestItem prItem : prItemList) {
							CatalogContractDetail catalogContractDetail = prItem.getCatalog().getCatalogContractDetail();
							if(catalogContractDetail.getQtyMaxOrder() != null) {
								catalogContractDetail.setQtyMaxOrder(catalogContractDetail.getQtyMaxOrder() + prItem.getQuantity());							
								catalogContractDetailSession.update(catalogContractDetail, tokenObj);
							}
						}
						purchaseRequestSession.update(purchaseRequest, tokenObj);
					}
				}
				else if (approvalProcessType.getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG )) {
					Catalog catalog = approvalDoProcessDTO.getCatalog();
					if (approvalDoProcessDTO.getStatus() == 2) {
						catalog.setIsDelete(1);
					}
					masterCatalogServices.approveNewCatalog(approvalDoProcessDTO.getCatalog(), tokenStr);
				}
				else if (approvalProcessType.getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG )) {
					CatalogHistory catalogHistory = approvalDoProcessDTO.getCatalogHistory();
					catalogHistory.setTodo(approvalDoProcessDTO.getTodo());
					if (approvalDoProcessDTO.getStatus() == 2) {
						catalogHistory.setIsDelete(1);
					}
					catalogHistory.setCategoryList(approvalDoProcessDTO.getCatalog().getCategoryList());
					masterCatalogServices.updateCatalogAfterApprove(catalogHistory, tokenStr);
				}
			}
			userTransaction.commit();
		} catch (Exception e) {
			userTransaction.rollback();
			e.printStackTrace();
		}
		return response;
	}

	@Path("/get-my-monitoring-worklist")
	@GET
	public List<ApprovalProcessDTO> getMyMonitoringWorkList(@HeaderParam("Authorization") String token) {
		return  approvalProcessSession.getMyMonitoringWorkList(tokenSession.findByToken(token), Constant.TAHAPAN_APPROVAL_BO);
	}
}
