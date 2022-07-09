package id.co.promise.procurement.vendor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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

import id.co.promise.procurement.DTO.ApprovalCalonVendorDTO;
import id.co.promise.procurement.DTO.ApprovalVendor;
import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.approval.ApprovalTahapanSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Parameter;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.SertifikatVendor;
import id.co.promise.procurement.entity.SertifikatVendorRiwayat;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorApproval;
import id.co.promise.procurement.entity.VendorDraft;
import id.co.promise.procurement.entity.VendorPICDraft;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.entity.VendorProfileDraft;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.BidangUsahaSession;
import id.co.promise.procurement.master.BuktiKepemilikanSession;
import id.co.promise.procurement.master.HariLiburSession;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.KondisiPeralatanVendorSession;
import id.co.promise.procurement.master.KualifikasiVendorSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ParameterSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.VendorDaftarDTO;
import id.co.promise.procurement.master.WilayahSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.JsonObject;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@Path(value = "/procurement/vendor/vendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class VendorServices {
	final static CustomResponseMessage message = CustomResponse
			.getCustomResponseMessage();

	@EJB
	private VendorSession vendorSession;
	
	@EJB
	private DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;
	
	@EJB
	private TokenSession tokenSession;
	
	@EJB
	private VendorApprovalSession vendorApprovalSession;
	
	@EJB
	private SertifikatVendorSession sertifikatVendorSession;
	
	@EJB
	private SertifikatVendorRiwayatSession sertifikatVendorRiwayatSession;
	
	@EJB
	private ParameterSession parameterSession;
	
	@EJB
	private UserSession userSession;	
	
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB
	private OrganisasiSession organisasiSession;
	@EJB
	private ApprovalTahapanSession approvalTahapanSession;
	
	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;
	
	@EJB
	private ApprovalSession approvalSession;
	
	@EJB
	private ApprovalLevelSession approvalLevelSession;
	
	@EJB
	private ApprovalProcessSession approvalProcessSession;
	
	@EJB
	private HariLiburSession hariLiburSession;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	@EJB
	private VendorProfileSession vendorProfileSession;
	
	@EJB
	private VendorSKDSession vendorSkdSession;
	
	@EJB
	private BidangUsahaSession bidangUsahaSession;
	
	@EJB
	private MataUangSession mataUangSession;
	
	@EJB
	private JabatanSession jabatanSession;
	
	@EJB
	private KualifikasiVendorSession kualifikasiVendorSession;
	
	@EJB
	private WilayahSession wilayahSession;
	
	@EJB
	private BankVendorSession bankVendorSession;
	
	@EJB
	private SegmentasiVendorSession segmentasiVendorSession;
	
	@EJB
	private PeralatanVendorSession peralatanVendorSession;
	
	@EJB
	private KeuanganVendorSession keuanganVendorSession;
	
	@EJB
	private PengalamanVendorSession pengalamanVendorSession;
	
	@EJB
	private VendorPICSession vendorPICSession;
	
	@EJB
	KondisiPeralatanVendorSession kondisiSession;
	
	@EJB
	BuktiKepemilikanSession buktiKepemilikanSession;
	
	@EJB
	private VendorDraftSession vendorDraftSession;
	
	@EJB 
	private DokumenRegistrasiVendorDraftSession dokumenRegistrasiVendorDraftSession;
	
	@EJB
	private VendorProfileDraftSession vendorProfileDraftSession;
	
	@EJB
	private VendorPICDraftSession vendorPICDraftSession;
	
	@EJB
	private TahapanSession tahapanSession;
	
	@Path("/updateVendorStatus")
	@POST
	public Vendor updateVendorStatus(@FormParam("id") int id,
			@FormParam("status") int status,
			@HeaderParam("Authorization") String token) {
		return vendorSession.updateVendorStatus(id, status,
				tokenSession.findByToken(token));
	}

	@Path("/updateVendorStatusDTO")
	@POST
	public Vendor updateVendorStatusDTO(ApprovalVendor approvalVendor,
			@HeaderParam("Authorization") String token) {

		Vendor vendor = vendorSession.getVendor(approvalVendor.getVendor().getId());
		
		if(vendor.getStatusBlacklist() != null && (vendor.getStatusBlacklist().equals(1) || vendor.getStatusBlacklist().equals(2))){
			}else{
			/** approval pendaftaran dan perpajangan sertifkat **/
			SertifikatVendor sertifikatVendor = new SertifikatVendor();
			Boolean createCertificate = true;
			
			Boolean registrasi = true;
			
			if(vendor.getStatusPerpanjangan() != null){
				if(vendor.getStatusPerpanjangan().equals(1)){
					sertifikatVendor = sertifikatVendorSession.getSertifikatVendorByVendor(approvalVendor.getVendor().getId());
					if(sertifikatVendor != null){
						if(sertifikatVendor.getStatus().equals(2)){
							createCertificate = false;
						}
					}				
					registrasi = false;
				} 
			}
			
			vendor.setStatusPerpanjangan(0); 
			if(sertifikatVendor == null){
				sertifikatVendor = new SertifikatVendor();
				
			}
			
			if(approvalVendor.getVendor().getStatus().equals(1)){
				vendor.setStatus(approvalVendor.getVendor().getStatus());
				sertifikatVendor.setStatus(1);
				
	 		}else if(approvalVendor.getVendor().getStatus().equals(5)){
	 			if(createCertificate == true){
	 		 		vendor.setStatus(approvalVendor.getVendor().getStatus());
	 				vendor.setIsDelete(1);
	 				vendor.setDeleted(new Date());
	 				
	 				User user = userSession.find(vendor.getUser());
	 				user.setReject(1);
	 				user.setIsDelete(1);
	 				userSession.updateUser(user, tokenSession.findByToken(token));
	 				
	 				List<VendorProfile> vpList = vendorProfileSession.getVendorProfileByVendorId(approvalVendor.getVendor().getId());
	 				if(vpList.size()>0) {
	 					emailNotificationSession.getMailGeneratorRejectVendor(vpList.get(0));
	 				}
	 					
	 			}else{
	 				sertifikatVendor.setStatus(3);
	 			}
			}
			
			Token tokenize = tokenSession.findByToken(token);
			if(approvalVendor.getDokumenRegistrasiVendorList() != null){
				if(approvalVendor.getDokumenRegistrasiVendorList().size() > 0){
					for(DokumenRegistrasiVendor dokumenRegistrasiVendor : approvalVendor.getDokumenRegistrasiVendorList()){
						if(dokumenRegistrasiVendor.getId() != null){
							DokumenRegistrasiVendor dok = dokumenRegistrasiVendorSession.find(dokumenRegistrasiVendor.getId());
							dok.setStatusCeklist(dokumenRegistrasiVendor.getStatusCeklist());
							dok.setVendor(vendor);
							dokumenRegistrasiVendorSession.updateDokumenRegistrasiVendor(dok, tokenize);
						}
					}
				}
			}
					
			VendorApproval vendorApproval = approvalVendor.getVendorApproval();
			vendorApproval.setUserId(tokenize.getUser().getId()); 
			vendorApproval.setVendor(vendor);  
			vendorApprovalSession.insertVendorApproval(vendorApproval, tokenize);

			if(approvalVendor.getVendor().getStatus().equals(1)){
				Calendar cals = Calendar.getInstance();
				cals.setTime(new Date());
				cals.set(Calendar.HOUR_OF_DAY, 0);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				
				sertifikatVendor.setTanggalMulai(cals.getTime());
				
				Calendar calsLast = Calendar.getInstance();
				calsLast.setTime(new Date());
				calsLast.set(Calendar.HOUR_OF_DAY, 0);
				calsLast.set(Calendar.MINUTE, 0);
				calsLast.set(Calendar.SECOND, 0);
		
				Parameter parameterLimit = parameterSession.getParameterByName("SERTIFIKAT_LIMIT");  
				calsLast.add(Calendar.YEAR, Integer.valueOf(parameterLimit.getNilai()));
				
				sertifikatVendor.setTanggalBerakhir(calsLast.getTime());
		
				Parameter parameter = parameterSession.getParameterByName("SERTIFIKAT_NO_URUT"); 
				Integer number = Integer.valueOf(parameter.getNilai()) + 1;
				parameter.setNilai(number.toString());
				parameterSession.updateParameter(parameter, tokenize);
				
				String format = parameterSession.getParameterByName("SERTIFIKAT_FORMAT").getNilai();
				String defaults = parameterSession.getParameterByName("SERTIFIKAT_DEFAULT").getNilai();
				
				String kodeVendor = defaults.substring(String.valueOf(number).length()) + String.valueOf(number);  
				
				sertifikatVendor.setNomor(format.replace("{NUMBER}", kodeVendor));
				sertifikatVendor.setVendor(vendor);
				sertifikatVendor.setUserId(tokenize.getUser().getId());
				
				if(createCertificate == true){
					sertifikatVendorSession.insertSertifikatVendor(sertifikatVendor, tokenize);
				}else{
					if(sertifikatVendor.getId() == null){
						
						sertifikatVendorSession.insertSertifikatVendor(sertifikatVendor, tokenize);
						
						//Emailnotification
						List<VendorProfile> vendorList = vendorProfileSession.getVendorProfileByVendorId(approvalVendor.getVendor().getId());
						if(vendorList.size()>0)
							emailNotificationSession.getMailGeneratorApprovalVendor(vendorList.get(0));
						
					}else{
						sertifikatVendorSession.updateSertifikatVendor(sertifikatVendor, tokenize);
						
						List<SertifikatVendor> svList = sertifikatVendorSession.getSertifikatVendorByVendorId(approvalVendor.getVendor().getId());
						List<VendorProfile> vpList = vendorProfileSession.getVendorProfileByVendorId(approvalVendor.getVendor().getId());
		 				if(svList.size()>0) {
		 					emailNotificationSession.getMailGeneratorPerpanjanganSertifikat(svList.get(0), vpList.get(0));
		 				}
						
					}
				}

				/** duplicate history certificate **/
				SertifikatVendorRiwayat sertifikatVendorRiwayat = new SertifikatVendorRiwayat();
				sertifikatVendorRiwayat.setTanggalMulai(cals.getTime());
				sertifikatVendorRiwayat.setTanggalBerakhir(calsLast.getTime()); 
				sertifikatVendorRiwayat.setNomor(format.replace("{NUMBER}", kodeVendor));
				sertifikatVendorRiwayat.setVendor(vendor);
				sertifikatVendorRiwayat.setUserId(tokenize.getUser().getId());
				sertifikatVendorRiwayat.setSertifikatVendor(sertifikatVendor);
				sertifikatVendorRiwayatSession.insertSertifikatVendorRiwayat(sertifikatVendorRiwayat, tokenize);
			}
		}
		
		return vendor;
	}
		
	@Path("/approvalCalonVendorDTO")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Vendor approvalCalonVendorDTO(ApprovalCalonVendorDTO approvalCalonVendorDTO,
			@HeaderParam("Authorization") String token) {

		/** get token **/
		Token tkn = tokenSession.findByToken(token);

		/** get vendor **/
		Vendor vendor = vendorSession.getVendor(approvalCalonVendorDTO.getVendor().getId());
		
		/** get approval process **/
		ApprovalProcess approvalProcess = approvalProcessSession.find(approvalCalonVendorDTO.getApprovalProcess().getId());
		if (approvalProcess != null) {
			int approvalStatus = approvalCalonVendorDTO.getApprovalProcess().getStatus();
			String note = approvalCalonVendorDTO.getVendorApproval().getKeterangan();
			
			approvalProcess.setStatus(approvalStatus);
			approvalProcess.setKeterangan(note);
			approvalProcessSession.update(approvalProcess, tkn);
			
			//Jika di reject, maka semua status yang aktif(1) akan menjadi reject(2)
			if ( approvalProcess.getStatus() == 2) {
				List<ApprovalProcess> apStatus = approvalProcessSession.findApprovalProcessByStatus(approvalProcess.getApprovalProcessType().getId(), 1);
				for (ApprovalProcess approvalProcess2 : apStatus) {
					approvalProcess2.setStatus(2);
					approvalProcessSession.update(approvalProcess2, tkn);
				}
			}
			
			//jika status 3(approve), cek lagi level selanjutnya. dan jenis serial
			if( approvalStatus == 3 && approvalProcess.getApprovalProcessType().getJenis() == 0 ){
			//update next approval
				Integer sequence = approvalProcess.getSequence();
				ApprovalProcess apNext = approvalProcessSession
						.findByApprovalProcessTypeAndSequence(approvalProcess.getApprovalProcessType(), ++sequence);
				if (apNext != null) {
					apNext.setUserId(tkn.getUser().getId());
					apNext.setUpdated(new Date());
					apNext.setStatus(1); // Approval level berikutnya diset active
					approvalProcessSession.update(apNext, tkn);
				}
			}
			
			/** get approval list **/
			ApprovalProcessType approvalProcessType = approvalProcessTypeSession
					.find(approvalProcess.getApprovalProcessType().getId());
			List<ApprovalProcess> approvalProcessList = approvalProcessSession
					.findByApprovalProcessType(approvalProcessType.getId()); 

			/** cek status approval **/
			int status = 0;
			int appSize = 0;
			int camelFlag = 0;
			for (ApprovalProcess approvalProcessCek : approvalProcessList) {
				if (approvalProcessCek.getStatus() != null) {
					if (approvalProcessCek.getStatus() == 3) {
						appSize++;
						if(appSize == 1) {
							User user = userSession.getUser(approvalProcessList.get(0).getUserId());
							vendor.setEmailRequestor(user.getEmail());
						}
					} else if (approvalProcessCek.getStatus() == 2) {
						status = 5;
						break;
					} else if (approvalProcessCek.getStatus() == 4 ){
						status = 0;
						break;
					}
				}
			}

			/** jika sudah approve semua **/
			if (appSize == approvalProcessList.size()) {
				status = 1;				
				note = "";
				vendor.setIsApproval(0);
				
				vendor.setCamelFlag(1);// camel flag untuk approved vendor
			}
			
			/** set status vendor **/
			vendor.setStatus(status);
			
			vendor.setUpdated(new Date());	
			vendorSession.updateVendor(vendor, tkn);
					
			VendorApproval vendorApproval = new VendorApproval();
			
			vendorApproval.setKeterangan(approvalCalonVendorDTO.getVendorApproval().getKeterangan());
			vendorApproval.setRealFileName(approvalCalonVendorDTO.getVendorApproval().getRealFileName());
			vendorApproval.setUserId(tkn.getUser().getId()); 
			vendorApproval.setVendor(vendor);  
			vendorApprovalSession.insertVendorApproval(vendorApproval, tkn);
			
			Token tokenize = tokenSession.findByToken(token);
			if(approvalCalonVendorDTO.getDokumenRegistrasiVendorList() != null){
				if(approvalCalonVendorDTO.getDokumenRegistrasiVendorList().size() > 0){
					for(DokumenRegistrasiVendor dokumenRegistrasiVendor : approvalCalonVendorDTO.getDokumenRegistrasiVendorList()){
						if(dokumenRegistrasiVendor != null){
							if(dokumenRegistrasiVendor.getId() != null){
								DokumenRegistrasiVendor dok = dokumenRegistrasiVendorSession.find(dokumenRegistrasiVendor.getId());
								dok.setStatusCeklist(dokumenRegistrasiVendor.getStatusCeklist());
								dok.setVendor(vendor);
								dokumenRegistrasiVendorSession.updateDokumenRegistrasiVendor(dok, tokenize);
							}
						}
					}
				}
			}
			
			if(approvalCalonVendorDTO.getVendorSkd() != null) {
				if(approvalCalonVendorDTO.getVendorSkd().size() > 0){
					if(approvalCalonVendorDTO.getVendorSkd().get(0).getId() == null) {
						//System.out.println("Masuk sini");
					} else {
						VendorSKD skd = vendorSkdSession.find(approvalCalonVendorDTO.getVendorSkd().get(0).getId());
						skd.setStatusCeklist(approvalCalonVendorDTO.getVendorSkd().get(0).getStatusCeklist());
						skd.setVendor(vendor);
						vendorSkdSession.updateVendorSKD(skd, tokenize);
					}
				}
			}
			
			if(vendor.getStatus() == 1) {
				
				//Emailnotification
				List<VendorProfile> vendorList = vendorProfileSession.getVendorProfileByVendorId(approvalCalonVendorDTO.getVendor().getId());
				if(vendorList.size()>0)
					emailNotificationSession.getMailGeneratorApprovalVendor(vendorList.get(0));
				
				/** buat sertifikat baru **/
				SertifikatVendor sertifikatVendor = new SertifikatVendor();
				Boolean createCertificate = true;
				Boolean registrasi = true;
				
				sertifikatVendor.setStatus(1);

				Calendar cals = Calendar.getInstance();
				cals.setTime(new Date());
				cals.set(Calendar.HOUR_OF_DAY, 0);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				
				sertifikatVendor.setTanggalMulai(cals.getTime());
				
				Calendar calsLast = Calendar.getInstance();
				calsLast.setTime(new Date());
				calsLast.set(Calendar.HOUR_OF_DAY, 0);
				calsLast.set(Calendar.MINUTE, 0);
				calsLast.set(Calendar.SECOND, 0);
		
				Parameter parameterLimit = parameterSession.getParameterByName("SERTIFIKAT_LIMIT");  
				calsLast.add(Calendar.YEAR, Integer.valueOf(parameterLimit.getNilai()));
				
				sertifikatVendor.setTanggalBerakhir(calsLast.getTime());
		
				Parameter parameter = parameterSession.getParameterByName("SERTIFIKAT_NO_URUT"); 
				Integer number = Integer.valueOf(parameter.getNilai()) + 1;
				parameter.setNilai(number.toString());
				parameterSession.updateParameter(parameter, tokenize);
				
				String format = parameterSession.getParameterByName("SERTIFIKAT_FORMAT").getNilai();
				String defaults = parameterSession.getParameterByName("SERTIFIKAT_DEFAULT").getNilai();
				
				String kodeVendor = defaults.substring(String.valueOf(number).length()) + String.valueOf(number);  
				
				sertifikatVendor.setNomor(format.replace("{NUMBER}", kodeVendor));
				sertifikatVendor.setVendor(vendor);
				sertifikatVendor.setUserId(tokenize.getUser().getId());
				
				if(createCertificate == true){
					sertifikatVendorSession.insertSertifikatVendor(sertifikatVendor, tokenize);
				}

				/** duplicate history certificate **/
				SertifikatVendorRiwayat sertifikatVendorRiwayat = new SertifikatVendorRiwayat();
				sertifikatVendorRiwayat.setTanggalMulai(cals.getTime());
				sertifikatVendorRiwayat.setTanggalBerakhir(calsLast.getTime()); 
				sertifikatVendorRiwayat.setNomor(format.replace("{NUMBER}", kodeVendor));
				sertifikatVendorRiwayat.setVendor(vendor);
				sertifikatVendorRiwayat.setUserId(tokenize.getUser().getId());
				sertifikatVendorRiwayat.setSertifikatVendor(sertifikatVendor);
				sertifikatVendorRiwayatSession.insertSertifikatVendorRiwayat(sertifikatVendorRiwayat, tokenize);

			} else if ( vendor.getStatus() == 5) {
				vendor.setStatus(status);
				vendor.setIsDelete(1);
 				vendor.setDeleted(new Date());
 				
 				User user = userSession.find(vendor.getUser());
 				user.setReject(1);
 				user.setIsDelete(1);
 				userSession.updateUser(user, tokenSession.findByToken(token));
 				
 				//Emailnotification
				List<VendorProfile> vendorList = vendorProfileSession.getVendorProfileByVendorId(approvalCalonVendorDTO.getVendor().getId());
				if(vendorList.size()>0){
					emailNotificationSession.getMailGeneratorRejectVendor(vendorList.get(0));
					//set vendor profile isdelete == 1 jika direject agar npwp bisa dipakai lagi
					VendorProfile vendorProfile = vendorList.get(0);
					vendorProfile.setIsDelete(1);
					vendorProfileSession.updateVendorProfile(vendorProfile, tokenSession.findByToken(token));
				}
					
			}	
		}

		return vendor;
	}
	

	@Path("/getCountVendor")
	@GET
	public int countVendor() {
		return vendorSession.countByStatus(1);
	}

	@Path("/getCountVendor/{status}")
	@GET
	public int countVendorByStatus(@PathParam("status") int status) {
		return vendorSession.countByStatus(status);
	}

	@Path("/getVendorList")
	@GET
	public List<Vendor> getVendorList() {
		return vendorSession.getVendorList();
	}

	@Path("/getVendorListByStatus/{status}")
	@GET
	public List<Vendor> getVendorListByStatus(@PathParam("status") int status) {
		return vendorSession.getVendorListByStatus(status);
	}

	@Path("/getVendorListByStatusForBlacklist/{status}")
	@GET
	public List<Vendor> getVendorListByStatusForBlacklist(@PathParam("status") int status) {
		return vendorSession.getVendorListByStatusForBlacklist(status);
	}

	@Path("/getVendorListByStatusApproal")
	@GET
	public List<Vendor> getVendorListByStatusApproal() {
		return vendorSession.getVendorListByStatusApproal();
	}
	
	@Path("/getVendorListByStatusApproalAndLevelOrganisasi")
	@GET
	public List<Vendor> getVendorListByStatusApproalAndLevelOrganisasi(@HeaderParam("Authorization") String token) {
		Token tokenusr = tokenSession.findByToken(token);
		
		List<RoleUser> roleUser = roleUserSession.getRoleUserByUserId(tokenusr.getUser().getId());
			
		List<String> organisasiListName = new ArrayList<String>();
		
		if(roleUser.get(0).getOrganisasi().getId() != 1 ){
			
			organisasiListName.add(roleUser.get(0).getOrganisasi().getNama());
			
			List<String> organisasiList = getOrganisasiByParentId(roleUser.get(0).getOrganisasi().getId());
			
			for(String orgNama : organisasiList ){
				organisasiListName.add(orgNama);	
			}	
		}
		return vendorSession.getVendorListByStatusApproalAndLevelOrganisasi(organisasiListName);
	}
	
	private List<String> getOrganisasiByParentId(Integer organisasiId){
		List<Organisasi> orgList = organisasiSession.getOrganisasiListByParentId(organisasiId);
		List<String> organisasiListId = new ArrayList<String>();
		
		if(orgList != null){
			for(Organisasi organisasi : orgList){
				organisasiListId.add(organisasi.getNama());
				getOrganisasiByParentId(organisasi.getId());
			}
		}		
		return organisasiListId;
		
	}

	@Path("/getVendor/{id}")
	@GET
	public Vendor getVendor(@PathParam("id") int id) {
		return vendorSession.getVendor(id);
	}

	@Path("/getVendorByUserId/{userId}")
	@GET
	public Vendor getVendorByUserId(@PathParam("userId") int userId) {
		return vendorSession.getVendorByUserId(userId);
	}
	
	@Path("/getVendorById/{id}")
	@GET
	public Vendor getVendorById(@PathParam("id") int id) {
		return vendorSession.getVendorById(id);
	}


	@Path("/getVendorByName/{nama}")
	@GET
	public List<Vendor> getVendorByNamaList(@PathParam("nama") String nama) {
		return vendorSession.getVendorByNamaList(nama);
	}

	@Path("/getVendorListCatalogByNama/{nama}")
	@GET
	public List<Vendor> getVendorListCatalogByNama(@PathParam("nama") String nama) {
		return vendorSession.getVendorListCatalogByNama(nama);
	}
	
	@Path("/create")
	@POST
	public Vendor insertVendor(@FormParam("nama") String nama,
			@FormParam("Alamat") String Alamat,
			@FormParam("nomorTelpon") String nomorTelpon,
			@FormParam("email") String email, @FormParam("npwp") String npwp,
			@FormParam("penanggungJawab") String penanggungJawab,
			@FormParam("user") int user,
			@FormParam("logoImage") String logoImage,
			@FormParam("logoImageSize") String logoImageSize,
			@FormParam("backgroundImage") String backgroundImage,
			@FormParam("backgroundImageSize") String backgroundImageSize,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("kota") String kota,
			@FormParam("provinsi") String provinsi,
			@HeaderParam("Authorization") String token) {
		Vendor vendor = new Vendor();

		if (nama != null && nama.length() > 0) {
			vendor.setNama(nama);
		}

		if (Alamat != null && Alamat.length() > 0) {
			vendor.setAlamat(Alamat);
		}

		if (nomorTelpon != null && nomorTelpon.length() > 0) {
			vendor.setNomorTelpon(nomorTelpon);
		}

		if (email != null && email.length() > 0) {
			vendor.setEmail(email);
		}

		if (npwp != null && npwp.length() > 0) {
			vendor.setNpwp(npwp);
		}

		if (penanggungJawab != null && penanggungJawab.length() > 0) {
			vendor.setPenanggungJawab(penanggungJawab);
		}

		if (user > 0) {
			vendor.setUser(user);
		}
		
		if (logoImage != null) {
			vendor.setLogoImage(logoImage);
		}

		if (logoImageSize != null) {
			vendor.setLogoImageSize(logoImageSize);
		}

		if (backgroundImage != null) {
			vendor.setBackgroundImage(backgroundImage);
		}
		
		if (backgroundImageSize != null) {
			vendor.setBackgroundImageSize(backgroundImageSize);
		}
		
		if (deskripsi != null) {
			vendor.setDeskripsi(deskripsi);
		}
		
		if (kota != null && kota.length() > 0) {
			vendor.setKota(kota);
		}
		
		if (provinsi != null && provinsi.length() > 0) {
			vendor.setProvinsi(provinsi);
		}

		return vendorSession.insertVendor(vendor,
				tokenSession.findByToken(token));
	}

	@Path("/update")
	@POST
	public Vendor editVendor(@FormParam("id") int vendorId,
			@FormParam("nama") String nama, @FormParam("Alamat") String Alamat,
			@FormParam("nomorTelpon") String nomorTelpon,
			@FormParam("email") String email, @FormParam("npwp") String npwp,
			@FormParam("penanggungJawab") String penanggungJawab,
			@FormParam("user") int user,
			@FormParam("logoImage") String logoImage,
			@FormParam("logoImageSize") String logoImageSize,
			@FormParam("backgroundImage") String backgroundImage,
			@FormParam("backgroundImageSize") String backgroundImageSize,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("kota") String kota,
			@FormParam("provinsi") String provinsi,
			@HeaderParam("Authorization") String token) {
		Vendor vendor = vendorSession.find(vendorId);

		if (nama != null && nama.length() > 0) {
			vendor.setNama(nama);
		}

		if (Alamat != null && Alamat.length() > 0) {
			vendor.setAlamat(Alamat);
		}

		if (nomorTelpon != null && nomorTelpon.length() > 0) {
			vendor.setNomorTelpon(nomorTelpon);
		}

		if (email != null && email.length() > 0) {
			vendor.setEmail(email);
		}

		if (npwp != null && npwp.length() > 0) {
			vendor.setNpwp(npwp);
		}

		if (penanggungJawab != null && penanggungJawab.length() > 0) {
			vendor.setPenanggungJawab(penanggungJawab);
		}

		if (user > 0) {
			vendor.setUser(user);
		}

		if (logoImage != null) {
			vendor.setLogoImage(logoImage);
		}

		if (logoImageSize != null) {
			vendor.setLogoImageSize(logoImageSize);
		}

		if (backgroundImage != null) {
			vendor.setBackgroundImage(backgroundImage);
		}
		
		if (backgroundImageSize != null) {
			vendor.setBackgroundImageSize(backgroundImageSize);
		}
		
		if (deskripsi != null) {
			vendor.setDeskripsi(deskripsi);
		}
		
		if (kota != null && kota.length() > 0) {
			vendor.setKota(kota);
		}
		
		if (provinsi != null && provinsi.length() > 0) {
			vendor.setProvinsi(provinsi);
		}
		
		vendorSession.updateVendor(vendor, tokenSession.findByToken(token));

		return vendor;
	}
	
	@Path("/editDataPerusahaan")
	@POST
	public VendorDaftarDTO editDataPerusahaan (VendorDaftarDTO vendorDaftarDTO,
			@HeaderParam("Authorization") String token) {
		
		Vendor vendor = vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		if(vendorDaftarDTO.getStatus()==0){
			
			VendorDraft vendorDraft = vendorDaftarDTO.getVendorDraft();
			vendorDraft.setVendor(vendor);
			vendorDraft.setAfco(organisasiSession.find(vendorDraft.getAfco().getId()));
			vendorDraft.setId(null);
			
			
			vendorDraftSession.insertVendorDraft(vendorDraft, tokenSession.findByToken(token));
			
			VendorProfileDraft vendorProfileDraft = vendorDaftarDTO.getVendorProfileBeanDraft();
			vendorProfileDraft.setUnitTerdaftar(vendorDraft.getAfco().getNama());
			vendorProfileDraft.setVendor(vendor);
			vendorProfileDraft.setId(null);
			
			vendorProfileDraftSession.insertVendorProfileDraft(vendorProfileDraft, tokenSession.findByToken(token));
			
			List<VendorPICDraft> vendorPICDraftList = vendorDaftarDTO.getVendorPICDraftList();
			
			for(VendorPICDraft vendorPICDraft : vendorPICDraftList){
				vendorPICDraft.setCreated(new Date());
				vendorPICDraft.setUpdated(null);
				vendorPICDraft.setVendor(vendor);
				vendorPICDraft.setId(null);
				vendorPICDraftSession.insertVendorPICDraft(vendorPICDraft, tokenSession.findByToken(token));
			}
			
		}else{
			
			VendorDraft vendorDraft = vendorDaftarDTO.getVendorDraft();
			vendorDraft.setVendor(vendor);
			vendorDraft.setAfco(organisasiSession.find(vendorDraft.getAfco().getId()));
			vendorDraft.setIsDelete(0);
			vendorDraftSession.updateVendorDraft(vendorDraft, tokenSession.findByToken(token));
			
			VendorProfileDraft vendorProfileDraft = vendorDaftarDTO.getVendorProfileBeanDraft();
			vendorProfileDraft.setVendor(vendor);
			vendorProfileDraft.setUnitTerdaftar(vendorDraft.getAfco().getNama());
			vendorProfileDraft.setIsDelete(0);
			vendorProfileDraftSession.updateVendorProfileDraft(vendorProfileDraft, tokenSession.findByToken(token));
			
			List<VendorPICDraft> vendorPICDraftList = vendorDaftarDTO.getVendorPICDraftList();
			
			vendorPICDraftSession.deleteByVendorId(vendor.getId());
			for(VendorPICDraft vendorPICDraft : vendorPICDraftList){
				vendorPICDraft.setCreated(new Date());
				vendorPICDraft.setVendor(vendor);
				vendorPICDraft.setId(null);
				vendorPICDraftSession.insertVendorPICDraft(vendorPICDraft, tokenSession.findByToken(token));
			}
			
		}
		
		return vendorDaftarDTO;
	}
	
		//update modul data perusahaan vendor
		@Path("/updateVendor")
		@POST
		public Vendor updateVendor(Vendor vendor, String token) {
			//Vendor vendor = vendorSession.find(vendor.getId());

			if (vendor.getNama() != null && vendor.getNama().length() > 0) {
				vendor.setNama(vendor.getNama());
			}

			if (vendor.getAlamat() != null && vendor.getAlamat().length() > 0) {
				vendor.setAlamat(vendor.getAlamat());
			}

			if (vendor.getNomorTelpon() != null && vendor.getNomorTelpon().length() > 0) {
				vendor.setNomorTelpon(vendor.getNomorTelpon());
			}

			if (vendor.getEmail() != null && vendor.getEmail().length() > 0) {
				vendor.setEmail(vendor.getEmail());
			}

			if (vendor.getNpwp() != null && vendor.getNpwp().length() > 0) {
				vendor.setNpwp(vendor.getNpwp());
			}

			if (vendor.getPenanggungJawab() != null && vendor.getPenanggungJawab().length() > 0) {
				vendor.setPenanggungJawab(vendor.getPenanggungJawab());
			}

			if (vendor.getUser() > 0) {
				vendor.setUser(vendor.getUser());
			}

			if (vendor.getLogoImage() != null) {
				vendor.setLogoImage(vendor.getLogoImage());
			}

			if (vendor.getLogoImageSize() != null) {
				vendor.setLogoImageSize(vendor.getLogoImageSize());
			}

			if (vendor.getBackgroundImage() != null) {
				vendor.setBackgroundImage(vendor.getBackgroundImage());
			}
			
			if (vendor.getBackgroundImageSize() != null) {
				vendor.setBackgroundImageSize(vendor.getBackgroundImageSize());
			}
			
			if (vendor.getDeskripsi() != null) {
				vendor.setDeskripsi(vendor.getDeskripsi());
			}
			
			if (vendor.getKota() != null && vendor.getKota().length() > 0) {
				vendor.setKota(vendor.getKota());
			}
			
			if (vendor.getProvinsi() != null && vendor.getProvinsi().length() > 0) {
				vendor.setProvinsi(vendor.getProvinsi());
			}
			
			vendorSession.updateVendor(vendor, tokenSession.findByToken(token));

			return vendor;
		}
	
	@Path("/indexPageList")
	@POST
	public Response indexPageList(
			@FormParam("keywordSearch") String keywordSearch,
			@FormParam("pageNo") int pageNo,
			@FormParam("pageSize") int pageSize,
			@FormParam("orderKeyword") String orderKeyword
			) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("jmlData", vendorSession.getTotalList(keywordSearch));
		map.put("dataList", vendorSession.getPagingList(keywordSearch, pageNo, pageSize, orderKeyword));
		
		return Response.ok(map).build();
	}
	
	@Path("/delete/{vendorId}")
	@GET
	public Vendor delete(@PathParam("vendorId")int vendorId, 
			@HeaderParam("Authorization") String token
			){
		return vendorSession.deleteVendor(vendorId, tokenSession.findByToken(token));
	}	
	
	@Path("/findAllVendorCatalogListForSearch")
	@POST
	public VendorListDTO findAllVendorCatalogListForSearch (
			@FormParam("bidangUsahaId") Integer bidangUsahaId,
			@FormParam("subBidangUsahaId") Integer subBidangUsahaId,
			@FormParam("vendorAndProductCategoryName") String vendorAndProductCategoryName,
			@FormParam("location") String location,
			@FormParam("ratings") Integer ratings,
			@FormParam("sortByColumn") Integer sortByColumn,
			@FormParam("maxBaris") Integer maxBaris,
			@FormParam("halamanKe") Integer halamanKe,
			@HeaderParam("Authorization") String token) {
		
		VendorListDTO vendorListDTO = vendorSession.getAllVendorCatalogListForSearch(bidangUsahaId, subBidangUsahaId, vendorAndProductCategoryName,location, ratings, sortByColumn,  maxBaris,  halamanKe);
		
		return vendorListDTO;
	}


	@Path("/getVendorPRRemains/{id}")
	@GET
	public List<Vendor> getVendorPRRemains(@PathParam("id") Integer id,
			@HeaderParam("Authorization") String token) {
		return vendorSession.getVendorPRRemains(id);		 
	}

	
	@Path("/getVendorPaggingList")
	@POST
	public JsonObject<Vendor> getVendorPaggingList(@Context HttpServletRequest httpServletRequest, @HeaderParam("Authorization") String token){
		
		String start = httpServletRequest.getParameter("start");
		String draw	 = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");
				
		
		
		List<Vendor> objList = vendorSession.getVendorPaggingList(Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order);
		//List<Vendor> vendorList1 = vendorSession.getVendorListForExportExcel(Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order);
		
	/*	int a = 1 ;
		for(Object[] obj : objList){
			Vendor vendor = new Vendor();
			vendor = (Vendor) obj[0];
			vendor.setPengadaanCount(obj[1] == null ? 0 : Integer.valueOf(obj[1].toString()));
			vendor.setPengadaanRunningCount(obj[2] == null ? 0 : Integer.valueOf(obj[2].toString()));
			vendor.setWinnerCount(obj[3] == null ? 0 : Integer.valueOf(obj[3].toString()));
			
			if(objList.size() == a){
				//vendor.setVendorList(vendorList1);
			}
			
			vendorList.add(vendor);
			a++;
		}*/

		Integer size = vendorSession.getVendorPaggingSize(search);
		
		JsonObject<Vendor> jo = new JsonObject<Vendor>();
		jo.setData(objList);
		jo.setRecordsTotal(size);
		jo.setRecordsFiltered(size);
		jo.setDraw(draw);
		return jo;
	}
	
	@Path("/findAllVendorListForInitial")
	@POST
	public VendorListDTO getAllVendorListForInitial (
			@FormParam("minPengalaman") Integer minPengalaman,
			@FormParam("kualifikasiId") Integer kualifikasiId,
			@FormParam("subBidangUsaha") String subBidangUsaha,
			@FormParam("tahunMulai") String tahunMulai,
			@FormParam("tahunAkhir") String tahunAkhir,
			@FormParam("minNilaiKontrak") Double minNilaiKontrak,
			@FormParam("maxBaris") Integer maxBaris,
			@FormParam("halamanKe") Integer halamanKe,
			@HeaderParam("Authorization") String token) {
		
		VendorListDTO vendorListDTO = vendorSession.getAllVendorListForInitial(minPengalaman, kualifikasiId, subBidangUsaha, tahunMulai, tahunAkhir, minNilaiKontrak, maxBaris, halamanKe);
		
		return vendorListDTO;
	}

	
	@Path("/getVendorPerformaPaggingList")
	@POST
	public JsonObject<Vendor> getVendorPerformaPaggingList(@Context HttpServletRequest httpServletRequest, @HeaderParam("Authorization") String token){
		
		String start = httpServletRequest.getParameter("start");
		String draw	 = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");
		String nomorAwal = httpServletRequest.getParameter("param[nomorAwal]");
		String nomorAkhir = httpServletRequest.getParameter("param[nomorAkhir]");
		String namaVendor = httpServletRequest.getParameter("param[namaVendor]");
		String bidangUsaha = httpServletRequest.getParameter("param[bidangUsaha]");
		String subBidangUsaha = httpServletRequest.getParameter("param[subBidangUsaha]");
		String pernahdinilai = httpServletRequest.getParameter("param[pernahdinilai]");
		String belumpernahdinilai = httpServletRequest.getParameter("param[belumpernahdinilai]");
		
		List<Vendor> vendorList = vendorSession.getVendorPerformaPaggingList(nomorAwal, nomorAkhir, namaVendor, bidangUsaha, subBidangUsaha, pernahdinilai, belumpernahdinilai, Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order);
		
		Integer size = vendorSession.getVendorPerformaPaggingSize(nomorAwal, nomorAkhir, namaVendor, bidangUsaha, subBidangUsaha, pernahdinilai, belumpernahdinilai, search);
		
		JsonObject<Vendor> jo = new JsonObject<Vendor>();
		jo.setData(vendorList);
		jo.setRecordsTotal(size);
		jo.setRecordsFiltered(size);
		jo.setDraw(draw);
		return jo;
	}
	
	@Path("/getVendorListByPagination")
	@POST 
	public Response getAlokasiAnggaranByPagination(
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token
			){
		
		//Integer userId = objToken.getUser().getId();
		
		String tempKeyword = "%" + keyword + "%";
		long countData = vendorSession.getVendorListCount(tempKeyword);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);		
		result.put("data", vendorSession.getVendorListWithPagination(start, length, tempKeyword, columnOrder, tipeOrder));
		
		return Response.ok(result).build();
	}
	
	@Path("/countVendorByDate")
	@GET
	public String countVendorByDate() {
		return vendorSession.countVendorByDate();
	}
	
	// get all vendorlist data with DTO
	@Path ("/getVendorData/{vendorId}")
	@GET
	public VendorDaftarDTO getVendorData (@PathParam ("vendorId") Integer vendorId) { 
		
		VendorDaftarDTO vendorDaftarDTO = new VendorDaftarDTO();
		
		Vendor vendor = vendorSession.find(vendorId);
		VendorProfile vendorProfile = vendorProfileSession.getVendorProfileByVendorId(vendorId).get(0);
		
		vendorDaftarDTO.setBidangUsahaList(bidangUsahaSession.getBidangUsahaList());
		vendorDaftarDTO.setMataUangList(mataUangSession.getMataUanglist());
		vendorDaftarDTO.setJabatanPenanggungJawabList(jabatanSession.getJabatanList());
		vendorDaftarDTO.setKualifikasiVendorList(kualifikasiVendorSession.getKualifikasiVendorList());
		vendorDaftarDTO.setProvinsiPerusahaanList(wilayahSession.getPropinsiList());
		vendorDaftarDTO.setVendor(vendor);
		vendorDaftarDTO.setVendorUserBean(userSession.getUser(vendor.getUser()));
		vendorDaftarDTO.setVendorProfileBean(vendorProfile);
		
		Organisasi organisasi = organisasiSession.find(vendorProfile.getBussinessArea());
		vendorDaftarDTO.setBusinessArea(organisasi.getNama());
		vendorDaftarDTO.setDataBankList(bankVendorSession.getBankVendorByVendorId(vendorId));
		vendorDaftarDTO.setDataSegmentasiList(segmentasiVendorSession.getSegmentasiVendorByVendorId(vendorId));
		vendorDaftarDTO.setPenanggungJawabList(vendorPICSession.getVendorPICByVendorId(vendorId));

		String vendorRegistrasiForm = "Vendor Registrasi Form"; 
		String salinanAktePendirianPerusahaanDanPerubahan ="Salinan Akte Pendirian Perusahaan dan Perubahan - Perubahannya";
		String tandaDaftarPerusahaan ="Salinan Tanda Daftar Perusahaan (TDP)";
		String suratIjinUsaha = "Salinan Surat Ijin Usaha (SIUP SIUJK)";
		String dokumenPKS = "Dokumen PKS";
		String dokumenSPR = "Dokumen SPR";
		String dokumenSPB = "Dokumen SPB";
		String buktiFisikPerusahaan ="Bukti Fisik Perusahaan"; 
		String dokumenQuality ="Dokumen Quality yang dimiliki";
		String dokumenTeknik  ="Dokumen Teknik";
		String dokumenSKB 	="Surat Keterangan Bebas";
		
		vendorDaftarDTO.setDataDokRegFormList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, vendorRegistrasiForm));
		vendorDaftarDTO.setDataDokAkteList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, salinanAktePendirianPerusahaanDanPerubahan));
		vendorDaftarDTO.setDataDokSalinanList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, tandaDaftarPerusahaan));
		vendorDaftarDTO.setDataDokSiupList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, suratIjinUsaha));
		vendorDaftarDTO.setDataDokPKSList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, dokumenPKS));
		vendorDaftarDTO.setDataDokSPRList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, dokumenSPR));
		vendorDaftarDTO.setDataDokSPBList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, dokumenSPB));
		vendorDaftarDTO.setDataDokBuktiFisikList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, buktiFisikPerusahaan ));
		vendorDaftarDTO.setDataDokQualityList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, dokumenQuality ));
		vendorDaftarDTO.setDataDokSKBList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, dokumenSKB ));
		vendorDaftarDTO.setDataHistorySKBList(dokumenRegistrasiVendorSession.getHistoryDokumenRegistrasiVendor(vendorId, dokumenSKB ));
		vendorDaftarDTO.setDataDokTeknikList(dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, dokumenTeknik ));
		vendorDaftarDTO.setDataDokSKDList(vendorSkdSession.getVendorSkdByVendorId(vendorId));
		vendorDaftarDTO.setPeralatanListVendor(peralatanVendorSession.getPeralatanVendorByVendorId(vendorId));
		vendorDaftarDTO.setKeuanganListVendor(keuanganVendorSession.getKeuanganVendorByVendorId(vendorId));
		
		String pelanggan = "PELANGGAN";
		String mitra = "MITRA";
		String inProgress = "INPROGRESS";
		
		vendorDaftarDTO.setPengalamanPelangganVendorList(pengalamanVendorSession.getPengalamanVendorByVendorIdAndTipePengalaman(vendorId, pelanggan));
		vendorDaftarDTO.setPengalamanMitraVendorList(pengalamanVendorSession.getPengalamanVendorByVendorIdAndTipePengalaman(vendorId, mitra));
		vendorDaftarDTO.setPengalamanInProgressVendorList(pengalamanVendorSession.getPengalamanVendorByVendorIdAndTipePengalaman(vendorId, inProgress));
		
	return vendorDaftarDTO;
	}
	
	// get all vendorlist data with DTO untuk modul data perusahaan di login vendor
	@Path ("/getVendorDataPerusahaan/{userId}")
	@GET
	public VendorDaftarDTO getVendorDataPerusahaan (@PathParam ("userId") Integer userId) { 
		
		VendorDaftarDTO vendorDaftarDTO = new VendorDaftarDTO();
		
		Vendor vendor = vendorSession.getVendorByUserId(userId);
				
		
		vendorDaftarDTO.setUnitTerdaftarList(organisasiSession.getOrganisasiAll());
		vendorDaftarDTO.setProvinsiPerusahaanList(wilayahSession.getPropinsiList());
		vendorDaftarDTO.setJabatanPenanggungJawabList(jabatanSession.getJabatanList());
		vendorDaftarDTO.setKualifikasiVendorList(kualifikasiVendorSession.getKualifikasiVendorList());
		vendorDaftarDTO.setWilayahList(wilayahSession.getList());
		List<VendorDraft> vendorDraftList=vendorDraftSession.getVendorDraftByVendorId(vendor.getId());
		
		
		if(vendorDraftList.size()<=0){
			VendorProfile vendorProfile = vendorProfileSession.getVendorProfileByVendorId(vendor.getId()).get(0);
			vendorDaftarDTO.setVendor(vendor);
			vendorDaftarDTO.setVendorProfileBean(vendorProfile);
			Organisasi organisasi = organisasiSession.find(vendorProfile.getBussinessArea());
			vendorDaftarDTO.setBusinessAreaOrganisasi(organisasi);
			vendorDaftarDTO.setPenanggungJawabList(vendorPICSession.getVendorPICByVendorId(vendor.getId()));
			vendorDaftarDTO.setStatus(0);
		}else{
			VendorProfileDraft vendorProfileDraft = vendorProfileDraftSession.getVendorProfileDraftByVendor(vendor.getId());
			VendorDraft vendorDraft = vendorDraftList.get(0);
			vendorDaftarDTO.setVendorDraft(vendorDraft);
			vendorDaftarDTO.setStatus(1);
			vendorDaftarDTO.setVendorProfileBeanDraft(vendorProfileDraft);
			Organisasi organisasi = organisasiSession.find(vendorProfileDraft.getBussinessArea());
			vendorDaftarDTO.setBusinessAreaOrganisasi(organisasi);
			vendorDaftarDTO.setVendorPICDraftList(vendorPICDraftSession.getVendorPICDraftByVendorId(vendor.getId()));
		}

	return vendorDaftarDTO;
	}
	
	// get all data peralatan vendor DTO
	@Path ("/getVendorDataPeralatan/{userId}")
	@GET
	public VendorDaftarDTO getVendorDataPeralatan (@PathParam ("userId") Integer userId) { 
			
			VendorDaftarDTO vendorDaftarDTO = new VendorDaftarDTO();
			
			Vendor vendor = vendorSession.getVendorByUserId(userId);
			
			vendorDaftarDTO.setVendor(vendor);
			vendorDaftarDTO.setPeralatanListVendor(peralatanVendorSession.getPeralatanVendorByVendorId(vendor.getId()));
			
			return vendorDaftarDTO;
	}	
}
