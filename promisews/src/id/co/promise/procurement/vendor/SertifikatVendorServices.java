package id.co.promise.procurement.vendor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.DTO.ApprovalPerpanjanganSertifikatDTO;
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
import id.co.promise.procurement.entity.Parameter;
import id.co.promise.procurement.entity.SertifikatVendor;
import id.co.promise.procurement.entity.SertifikatVendorRiwayat;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorApproval;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.master.HariLiburSession;
import id.co.promise.procurement.master.ParameterSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.Converter;
import id.co.promise.procurement.utils.JsonObject;

@Stateless
@Path(value="/procurement/vendor/SertifikatVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class SertifikatVendorServices {
	
	@EJB private SertifikatVendorSession sertifikatVendorSession;
	
	@EJB private VendorSession vendorSession;
	
  	@EJB private TokenSession tokenSession;
	
	@EJB
	private HariLiburSession hariLiburSession;
	
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
	private VendorApprovalSession vendorApprovalSession;
	
	@EJB
	private DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;
	
	@EJB
	private SertifikatVendorRiwayatSession sertifikatVendorRiwayatSession;
	
	@EJB
	private ParameterSession parameterSession;
	
	@EJB
	private VendorProfileSession vendorProfileSession;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;

	
	@Path("/insertSertifikatVendor")
	@POST
	public SertifikatVendor insertSertifikatVendor(
		SertifikatVendor sertifikatVendor, 
		@HeaderParam("Authorization") String token) {
		return sertifikatVendorSession.insertSertifikatVendor(sertifikatVendor, tokenSession.findByToken(token));
	} 
	
	@Path("/updateSertifikatVendor")
	@POST
	public SertifikatVendor updateSertifikatVendor(
		SertifikatVendor sertifikatVendor, 
		@HeaderParam("Authorization") String token) {
		return sertifikatVendorSession.updateSertifikatVendor(sertifikatVendor, tokenSession.findByToken(token));
	}  

	@Path("/deleteSertifikatVendor/{id}")
	@GET
	public SertifikatVendor deleteSertifikatVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return sertifikatVendorSession.deleteSertifikatVendor(id, tokenSession.findByToken(token));
	}

	@Path("/getSertifikatVendorUserId/{id}")
	@GET	
	public SertifikatVendor getSertifikatVendorUserId(@PathParam("id") int id) {
		SertifikatVendor sertifikatVendor =  sertifikatVendorSession.getSertifikatVendorUserId(id);
		if(sertifikatVendor != null){
			if(sertifikatVendor.getStatus() != null){
				/** cek by status **/
				if(sertifikatVendor.getStatus().equals(4)){
					sertifikatVendor.setMustUpdate(true);
				}else if(sertifikatVendor.getStatus().equals(1) || sertifikatVendor.getStatus().equals(3)){
					/** cek by tanggal expired **/
					sertifikatVendor.setMustUpdate(Converter.checkUpdateSertifikat(sertifikatVendor));
				}
			}
		}
		
		return sertifikatVendor;
	}
	
	/*
	 * @Path("/requestPerpanjanganSertifikatVendor")
	 * 
	 * @POST public SertifikatVendor requestPerpanjanganSertifikatVendor(
	 * SertifikatVendor sertifikatVendor,
	 * 
	 * @Context HttpServletRequest httpServletRequest,
	 * 
	 * @HeaderParam("Authorization") String token) {
	 * 
	 * String captcha = (String)
	 * httpServletRequest.getSession().getAttribute(com.google.code.kaptcha.
	 * Constants.KAPTCHA_SESSION_KEY);
	 * if(captcha.equals(sertifikatVendor.getCaptcha())){ Vendor vendor =
	 * vendorSession.getVendor(sertifikatVendor.getVendor().getId());
	 * vendor.setStatusPerpanjangan(1);
	 * 
	 * sertifikatVendor.setVendor(vendor); sertifikatVendor.setStatus(2);
	 * sertifikatVendor.setMustUpdate(false);
	 * sertifikatVendorSession.updateSertifikatVendor(sertifikatVendor,
	 * tokenSession.findByToken(token));
	 * 
	 * //Insert Approval ApprovalProcessType apt = new ApprovalProcessType();
	 * apt.setValueId(vendor.getId());
	 * 
	 * Tahapan tahapan = new Tahapan(); tahapan.setId(36); // 36 Approval
	 * Perpanjangan TDR apt.setTahapan(tahapan);
	 * 
	 * List<ApprovalTahapan> approvalTahapan =
	 * approvalTahapanSession.getListApprovalByTahapan(tahapan); if
	 * (approvalTahapan.size() > 0) {
	 * apt.setApproval(approvalTahapan.get(0).getApproval());
	 * apt.setJenis(approvalTahapan.get(0).getApproval().getJenis());
	 * apt.setIsDelete(0);
	 * 
	 * approvalProcessTypeSession.create(apt, tokenSession.findByToken(token));
	 * 
	 * List<ApprovalLevel> aplList =
	 * approvalLevelSession.findByApproval(apt.getApproval()); int index = 0; for
	 * (ApprovalLevel apl : aplList) { ApprovalProcess ap = new ApprovalProcess();
	 * ap.setApprovalProcessType(apt); ap.setApprovalLevel(apl); if
	 * (apt.getApproval().getJenis() == 0) { // serial if (index == 0)
	 * ap.setStatus(1); else ap.setStatus(0); } else if
	 * (apt.getApproval().getJenis() == 1) { // paralel ap.setStatus(1); }
	 * 
	 * // ap.setKeterangan(); ap.setGroup(apl.getGroup());
	 * ap.setUser(apl.getUser()); // ap.setRole(apl.getRole());
	 * ap.setThreshold(apl.getThreshold()); ap.setSequence(apl.getSequence());
	 * ap.setIsDelete(0); approvalProcessSession.create(ap,
	 * tokenSession.findByToken(token)); index++; } } }else{
	 * sertifikatVendor.setCaptcha(null); } return sertifikatVendor; }
	 */
	
	@Path("/approvalPerpanjanganSertifikatDTO")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Vendor approvalCalonVendorDTO(ApprovalPerpanjanganSertifikatDTO approvalPerpanjanganSertifikatDTO,
			@HeaderParam("Authorization") String token) {

		/** get token **/
		Token tkn = tokenSession.findByToken(token);

		/** get vendor **/
		Vendor vendor = vendorSession.getVendor(approvalPerpanjanganSertifikatDTO.getVendor().getId());
		
		SertifikatVendor sertifikatVendor = new SertifikatVendor();
		
		/** get approval process **/
		ApprovalProcess approvalProcess = approvalProcessSession.find(approvalPerpanjanganSertifikatDTO.getApprovalProcess().getId());
		if (approvalProcess != null) {
			int approvalStatus = approvalPerpanjanganSertifikatDTO.getApprovalProcess().getStatus();
			String note = approvalPerpanjanganSertifikatDTO.getVendorApproval().getKeterangan();
			
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
			for (ApprovalProcess approvalProcessCek : approvalProcessList) {
				if (approvalProcessCek.getStatus() != null) {
					if (approvalProcessCek.getStatus() == 3) {
						appSize++;
						status = 2;
					} else if (approvalProcessCek.getStatus() == 2) {
						status = 3; //Direject
						vendor.setStatusPerpanjangan(0);
						break;
					} else if (approvalProcessCek.getStatus() == 4) {
						status = 5; //Di hold
						vendor.setStatusPerpanjangan(1);
						break;
					}
				}
			}

			/** jika sudah approve semua **/
			if (appSize == approvalProcessList.size()) {
				status = 1;
				vendor.setStatusPerpanjangan(0);
			}
	
			vendor.setUpdated(new Date());	
			vendorSession.updateVendor(vendor, tkn);
			
			Boolean createCertificate = true;

			sertifikatVendor = sertifikatVendorSession.getSertifikatVendorByVendor(approvalPerpanjanganSertifikatDTO.getVendor().getId());
			sertifikatVendor.setStatus(status);
			sertifikatVendorSession.updateSertifikatVendor(sertifikatVendor, tkn);
			
			List<SertifikatVendor> svList = sertifikatVendorSession.getSertifikatVendorByVendorId(approvalPerpanjanganSertifikatDTO.getVendor().getId());
			List<VendorProfile> vpList = vendorProfileSession.getVendorProfileByVendorId(approvalPerpanjanganSertifikatDTO.getVendor().getId());
				if(svList.size()>0) {
					emailNotificationSession.getMailGeneratorPerpanjanganSertifikat(svList.get(0), vpList.get(0));
				}
			
			if(sertifikatVendor.getStatus() == 1) { //Aktif
				createCertificate = true;
				/** buat sertifikat baru **/
				Boolean registrasi = false;

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
				parameterSession.updateParameter(parameter, tkn);
				
				String format = parameterSession.getParameterByName("SERTIFIKAT_FORMAT").getNilai();
				String defaults = parameterSession.getParameterByName("SERTIFIKAT_DEFAULT").getNilai();
				
				String kodeVendor = defaults.substring(String.valueOf(number).length()) + String.valueOf(number);  
				
				sertifikatVendor.setNomor(format.replace("{NUMBER}", kodeVendor));
				sertifikatVendor.setVendor(vendor);
				sertifikatVendor.setUserId(tkn.getUser().getId());
				
				if(sertifikatVendor.getId() == null){
					sertifikatVendorSession.insertSertifikatVendor(sertifikatVendor, tkn);
				}else{
					sertifikatVendorSession.updateSertifikatVendor(sertifikatVendor, tkn);
				}

				/** duplicate history certificate **/
				SertifikatVendorRiwayat sertifikatVendorRiwayat = new SertifikatVendorRiwayat();
				sertifikatVendorRiwayat.setTanggalMulai(cals.getTime());
				sertifikatVendorRiwayat.setTanggalBerakhir(calsLast.getTime()); 
				sertifikatVendorRiwayat.setNomor(format.replace("{NUMBER}", kodeVendor));
				sertifikatVendorRiwayat.setVendor(vendor);
				sertifikatVendorRiwayat.setUserId(tkn.getUser().getId());
				sertifikatVendorRiwayat.setSertifikatVendor(sertifikatVendor);
				sertifikatVendorRiwayatSession.insertSertifikatVendorRiwayat(sertifikatVendorRiwayat, tkn);
					
			

			} else if (sertifikatVendor.getStatus() == 3){
				createCertificate = false;
			}
			
			VendorApproval vendorApproval = new VendorApproval();
			
			vendorApproval.setKeterangan(approvalPerpanjanganSertifikatDTO.getVendorApproval().getKeterangan());
			vendorApproval.setRealFileName(approvalPerpanjanganSertifikatDTO.getVendorApproval().getRealFileName());
			vendorApproval.setUserId(tkn.getUser().getId()); 
			vendorApproval.setVendor(vendor);  
			vendorApprovalSession.insertVendorApproval(vendorApproval, tkn);
			
			Token tokenize = tokenSession.findByToken(token);
			if(approvalPerpanjanganSertifikatDTO.getDokumenRegistrasiVendorList() != null){
				if(approvalPerpanjanganSertifikatDTO.getDokumenRegistrasiVendorList().size() > 0){
					for(DokumenRegistrasiVendor dokumenRegistrasiVendor : approvalPerpanjanganSertifikatDTO.getDokumenRegistrasiVendorList()){
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

		return vendor;
	}
	
	@Path("/getSertifikatVendorPaggingList")
	@POST
	public JsonObject<SertifikatVendor> getSertifikatVendorPaggingList(@Context HttpServletRequest httpServletRequest, @HeaderParam("Authorization") String token){
		
		String start = httpServletRequest.getParameter("start");
		String draw	 = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");
		String nama = httpServletRequest.getParameter("param[nama]");
		String status = httpServletRequest.getParameter("param[status]");
				
		List<SertifikatVendor> vendorList = sertifikatVendorSession.getSertifikatVendorPaggingList(Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order, nama, status);
		
		Integer size = sertifikatVendorSession.getSertifikatVendorPaggingSize(search, nama, status);
		
		JsonObject<SertifikatVendor> jo = new JsonObject<SertifikatVendor>();
		jo.setData(vendorList);
		jo.setRecordsTotal(size);
		jo.setRecordsFiltered(size);
		jo.setDraw(draw);
		return jo;
	}
	
}
