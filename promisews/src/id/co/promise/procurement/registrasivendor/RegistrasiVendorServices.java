package id.co.promise.procurement.registrasivendor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.approval.ApprovalTahapanSession;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.Asosiasi;
import id.co.promise.procurement.entity.Bank;
import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BidangUsaha;
import id.co.promise.procurement.entity.BuktiKepemilikan;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.KondisiPeralatanVendor;
import id.co.promise.procurement.entity.KualifikasiVendor;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Negara;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SubBidangUsaha;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.Wilayah;
import id.co.promise.procurement.master.AsosiasiSession;
import id.co.promise.procurement.master.BankSession;
import id.co.promise.procurement.master.BidangUsahaSession;
import id.co.promise.procurement.master.BuktiKepemilikanSession;
import id.co.promise.procurement.master.HariLiburSession;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.KondisiPeralatanVendorSession;
import id.co.promise.procurement.master.KualifikasiVendorSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.NegaraSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.RegistrasiVendorDTO;
import id.co.promise.procurement.master.RoleSession;
import id.co.promise.procurement.master.SubBidangUsahaSession;
import id.co.promise.procurement.master.WilayahSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.KeyGenHelper;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.BankVendorSession;
import id.co.promise.procurement.vendor.VendorSKDSession;
import id.co.promise.procurement.vendor.VendorSession;

@Transactional
@Stateless
@Path(value = "/procurement/registrasivendor/registrasiVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class RegistrasiVendorServices {

	@EJB
	RegistrasiVendorSession registrasiVendorSession;

	@EJB
	TokenSession tokenSession;

	@EJB
	UserSession userSession;
	
	
	@EJB
	BankVendorSession bankVendorSession;
	
	@EJB
	VendorSKDSession vendorSKDSession;

	@EJB
	RoleUserSession roleUserSession;

	@EJB
	RoleSession roleSession;

	@EJB
	KualifikasiVendorSession kualifikasiVendorSession;

	@EJB
	OrganisasiSession organisasiSession;

	@EJB
	WilayahSession wilayahSession;

	@EJB
	JabatanSession jabatanSession;

	@EJB
	MataUangSession mataUangSession;

	@EJB
	NegaraSession negaraSession;

	@EJB
	AsosiasiSession asosiasiSession;

	@EJB
	BidangUsahaSession bidangUsahaSession;

	@EJB
	SubBidangUsahaSession subBidangUsahaSession;

	@EJB
	KondisiPeralatanVendorSession kondisiSession;

	@EJB
	BuktiKepemilikanSession buktiKepemilikanSession;
	
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
	private VendorSession vendorSession;

	@EJB
	BankSession bankSession;
	
	/*
	 * @Transactional
	 * 
	 * @Path("/vendor/createall")
	 * 
	 * @POST public RegistrasiVendorDTO insertVendorDTO(RegistrasiVendorDTO
	 * registrasiVendorDTO,
	 * 
	 * @HeaderParam("Authorization") String token) throws Exception{
	 * 
	 * //user
	 * 
	 * User user = registrasiVendorDTO.getUser(); String pass = user.getPassword();
	 * user.setPassword(KeyGenHelper.finalEncrypt(pass));
	 * userSession.insertUser(user, tokenSession.findByToken(token));
	 * 
	 * 
	 * // roleuser RoleUser roleUserPM = new RoleUser(); roleUserPM.setUser(user);
	 * System.out.println("idUser : " + user.getId());
	 * roleUserPM.setRole(roleSession.find(Constant.ROLE_USER_VENDOR_PM));
	 * roleUserSession.insertRoleUser(roleUserPM);
	 * 
	 * // roleuser RoleUser roleUserCM = new RoleUser(); roleUserCM.setUser(user);
	 * roleUserCM.setRole(roleSession.find(Constant.ROLE_USER_VENDOR_CM));
	 * roleUserSession.insertRoleUser(roleUserCM);
	 * 
	 * //vendor Vendor vendor= registrasiVendorDTO.getVendor();
	 * vendor.setUser(user.getId()); vendor.setTglRegistrasi(new Date());
	 * vendor.setStatusBlacklist(0); vendor.setStatusPerpanjangan(0);
	 * vendor.setStatusUnblacklist(0); vendor.setIsApproval(1);
	 * 
	 * registrasiVendorSession.insertVendor(vendor,
	 * tokenSession.findByToken(token));
	 * 
	 * //Insert Approval ApprovalProcessType apt = new ApprovalProcessType();
	 * apt.setValueId(vendor.getId());
	 * 
	 * Tahapan tahapan = new Tahapan(); tahapan.setId(29); // 29 Approval Registrasi
	 * Vendor apt.setTahapan(tahapan);
	 * 
	 * List<ApprovalTahapan> approvalTahapan =
	 * approvalTahapanSession.getListApprovalByTahapan(tahapan); if
	 * (approvalTahapan.size() > 0) { ApprovalTahapan approvalTahapanBaru = new
	 * ApprovalTahapan(); for(ApprovalTahapan at : approvalTahapan){
	 * if(at.getApproval().getBusinessArea() != null) { if
	 * (at.getApproval().getBusinessArea().equals(registrasiVendorDTO.
	 * getVendorProfile().getBussinessArea())){
	 * approvalTahapanBaru.setApproval(at.getApproval()); } }
	 * 
	 * }
	 * 
	 * if(approvalTahapanBaru.getApproval() == null) {
	 * approvalTahapanBaru.setApproval(approvalTahapan.get(0).getApproval()); }
	 * 
	 * apt.setApproval(approvalTahapanBaru.getApproval());
	 * apt.setJenis(approvalTahapanBaru.getApproval().getJenis());
	 * apt.setIsDelete(0); apt.setCreated(new Date());
	 * 
	 * approvalProcessTypeSession.create(apt, AuditHelper.OPERATION_CREATE, null);
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
	 * ap.setIsDelete(0); ap.setCreated(new Date());
	 * approvalProcessSession.create(ap, AuditHelper.OPERATION_CREATE, null);
	 * index++; } }
	 * 
	 * //vendor PIC List<VendorPIC>
	 * vendorPICList=registrasiVendorDTO.getVendorPICList();
	 * 
	 * for(VendorPIC vendorPIC : vendorPICList){ vendorPIC.setVendor(vendor);
	 * vendorPIC.setCreated(new Date()); vendorPIC.setIsDelete(0);
	 * 
	 * registrasiVendorSession.insertVendorPIC(vendorPIC,
	 * tokenSession.findByToken(token));
	 * 
	 * }
	 * 
	 * if(registrasiVendorDTO.getVendorProfile().getTitle().equalsIgnoreCase(
	 * "Company")) { VendorSKD vendorSKD = registrasiVendorDTO.getVendorSKD();
	 * vendorSKD.setVendor(vendor); vendorSKD.setCreated(new Date());
	 * vendorSKD.setIsDelete(0);
	 * 
	 * vendorSKDSession.insertVendorSKD(vendorSKD, tokenSession.findByToken(token));
	 * 
	 * 
	 * }
	 * 
	 * for(BankVendor bankVendor : registrasiVendorDTO.getBankVendorList()) {
	 * 
	 * bankVendor.setVendor(vendor);
	 * bankVendor.setMataUang(mataUangSession.find(bankVendor.getMataUang().getId())
	 * ); Integer bankVendorBnkt =
	 * bankVendorSession.getBankVendorByBnktAndSequence(bankVendor.getMataUang(),
	 * vendor);
	 * 
	 * System.out.println("BNKT Sequence : " + bankVendorBnkt); if (bankVendorBnkt
	 * == 0) { String sequence = "1";
	 * bankVendor.setBnkt(bankVendor.getMataUang().getKode() + sequence);
	 * bankVendor.setBnktSequence(1); } else { bankVendorBnkt++; String sequence =
	 * bankVendorBnkt.toString();
	 * bankVendor.setBnkt(bankVendor.getMataUang().getKode() + sequence);
	 * bankVendor.setBnktSequence(bankVendorBnkt); }
	 * 
	 * bankVendor.setIsDelete(0);
	 * 
	 * 
	 * bankVendorSession.insertBankVendor(bankVendor,
	 * tokenSession.findByToken(token));
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * for(SegmentasiVendor segmentasiVendor :
	 * registrasiVendorDTO.getSegmentasiVendorList()){
	 * segmentasiVendor.setVendor(vendor); segmentasiVendor.setCreated(new Date());
	 * segmentasiVendor.setIsDelete(0);
	 * 
	 * registrasiVendorSession.insertSegmentasiVendor(segmentasiVendor,
	 * tokenSession.findByToken(token)); }
	 * 
	 * for( DokumenRegistrasiVendor
	 * dokumenRegistrasiVendor:registrasiVendorDTO.getDokumenRegistrasiVendorList())
	 * { dokumenRegistrasiVendor.setVendor(vendor);
	 * dokumenRegistrasiVendor.setCreated(new Date());
	 * dokumenRegistrasiVendor.setIsDelete(0);
	 * registrasiVendorSession.insertDokumenRegistrasiVendor(
	 * dokumenRegistrasiVendor, tokenSession.findByToken(token)); }
	 * 
	 * for( PeralatanVendor
	 * peralatanVendor:registrasiVendorDTO.getPeralatanVendorList()){
	 * peralatanVendor.setVendor(vendor); peralatanVendor.setCreated(new Date());
	 * peralatanVendor.setIsDelete(0);
	 * 
	 * registrasiVendorSession.insertPeralatanVendor(peralatanVendor,
	 * tokenSession.findByToken(token)); }
	 * 
	 * for( KeuanganVendor
	 * keuanganVendor:registrasiVendorDTO.getKeuanganVendorList()){
	 * keuanganVendor.setVendor(vendor); keuanganVendor.setCreated(new Date());
	 * keuanganVendor.setIsDelete(0);
	 * 
	 * registrasiVendorSession.insertKeuanganVendor(keuanganVendor,
	 * tokenSession.findByToken(token));
	 * 
	 * }
	 * 
	 * for( PengalamanVendor
	 * pengalamanVendor:registrasiVendorDTO.getPengalamanVendorList()){
	 * pengalamanVendor.setVendor(vendor); pengalamanVendor.setCreated(new Date());
	 * pengalamanVendor.setIsDelete(0);
	 * 
	 * registrasiVendorSession.insertPengalamanVendor(pengalamanVendor,
	 * tokenSession.findByToken(token));
	 * 
	 * }
	 * 
	 * 
	 * registrasiVendorDTO.getVendorProfile().setCreated(new Date());
	 * registrasiVendorDTO.getVendorProfile().setIsDelete(0);
	 * registrasiVendorDTO.getVendorProfile().setVendor(vendor);
	 * registrasiVendorSession.insertVendorProfile(registrasiVendorDTO.
	 * getVendorProfile(), tokenSession.findByToken(token));
	 * 
	 * 
	 * return registrasiVendorDTO; }
	 */
	
	
	
	/*
	 * @Path("/vendor/create")
	 * 
	 * @POST public Vendor insertVendor(@FormParam("nama") String nama,
	 * 
	 * @FormParam("Alamat") String Alamat,
	 * 
	 * @FormParam("nomorTelpon") String nomorTelpon,
	 * 
	 * @FormParam("email") String email, @FormParam("npwp") String npwp,
	 * 
	 * @FormParam("penanggungJawab") String penanggungJawab,
	 * 
	 * @FormParam("user") int user,
	 * 
	 * @FormParam("logoImage") String logoImage,
	 * 
	 * @FormParam("logoImageSize") String logoImageSize,
	 * 
	 * @FormParam("backgroundImage") String backgroundImage,
	 * 
	 * @FormParam("backgroundImageSize") String backgroundImageSize,
	 * 
	 * @FormParam("deskripsi") String deskripsi,
	 * 
	 * @FormParam("kota") String kota,
	 * 
	 * @FormParam("provinsi") String provinsi,
	 * 
	 * @FormParam("afcoId") int afcoId,
	 * 
	 * @FormParam("isPKS") int isPKS,
	 * 
	 * @FormParam("bussinessArea") int bussinessAreaId,
	 * 
	 * @HeaderParam("Authorization") String token) { Vendor vendor = new Vendor();
	 * 
	 * if (nama != null && nama.length() > 0) { vendor.setNama(nama); }
	 * 
	 * if (Alamat != null && Alamat.length() > 0) { vendor.setAlamat(Alamat); }
	 * 
	 * if (nomorTelpon != null && nomorTelpon.length() > 0) {
	 * vendor.setNomorTelpon(nomorTelpon); }
	 * 
	 * if (email != null && email.length() > 0) { vendor.setEmail(email); }
	 * 
	 * if (npwp != null && npwp.length() > 0) { vendor.setNpwp(npwp); }
	 * 
	 * if (isPKS >= 0) { vendor.setIsPKS(isPKS); }
	 * 
	 * if (penanggungJawab != null && penanggungJawab.length() > 0) {
	 * vendor.setPenanggungJawab(penanggungJawab); }
	 * 
	 * if (user > 0) { vendor.setUser(user); }
	 * 
	 * if (logoImage != null) { vendor.setLogoImage(logoImage); }
	 * 
	 * if (logoImageSize != null) { vendor.setLogoImageSize(logoImageSize); }
	 * 
	 * if (backgroundImage != null) { vendor.setBackgroundImage(backgroundImage); }
	 * 
	 * if (backgroundImageSize != null) {
	 * vendor.setBackgroundImageSize(backgroundImageSize); }
	 * 
	 * if (deskripsi != null) { vendor.setDeskripsi(deskripsi); }
	 * 
	 * if (kota != null && kota.length() > 0) { vendor.setKota(kota); }
	 * 
	 * if (provinsi != null && provinsi.length() > 0) {
	 * vendor.setProvinsi(provinsi); }
	 * 
	 * if (afcoId > 0) { vendor.setAfco(organisasiSession.getOrganisasi(afcoId)); }
	 * 
	 * vendor.setTglRegistrasi(new Date()); vendor.setStatusBlacklist(0);
	 * vendor.setStatusPerpanjangan(0); vendor.setStatusUnblacklist(0);
	 * 
	 * vendor = registrasiVendorSession.insertVendor(vendor,
	 * tokenSession.findByToken(token));
	 * 
	 * //Insert Approval ApprovalProcessType apt = new ApprovalProcessType();
	 * apt.setValueId(vendor.getId());
	 * 
	 * Tahapan tahapan = new Tahapan(); tahapan.setId(29); // 29 Approval Registrasi
	 * Vendor apt.setTahapan(tahapan);
	 * 
	 * List<ApprovalTahapan> approvalTahapan =
	 * approvalTahapanSession.getListApprovalByTahapan(tahapan); if
	 * (approvalTahapan.size() > 0) { ApprovalTahapan approvalTahapanBaru = new
	 * ApprovalTahapan(); for(ApprovalTahapan at : approvalTahapan){
	 * if(at.getApproval().getBusinessArea() != null) { if
	 * (at.getApproval().getBusinessArea() == bussinessAreaId){
	 * approvalTahapanBaru.setApproval(at.getApproval()); } }
	 * 
	 * }
	 * 
	 * if(approvalTahapanBaru.getApproval() == null) {
	 * approvalTahapanBaru.setApproval(approvalTahapan.get(0).getApproval()); }
	 * 
	 * apt.setApproval(approvalTahapanBaru.getApproval());
	 * apt.setJenis(approvalTahapanBaru.getApproval().getJenis());
	 * apt.setIsDelete(0); apt.setCreated(new Date());
	 * 
	 * approvalProcessTypeSession.create(apt, AuditHelper.OPERATION_CREATE, null);
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
	 * ap.setIsDelete(0); ap.setCreated(new Date());
	 * approvalProcessSession.create(ap, AuditHelper.OPERATION_CREATE, null);
	 * index++; } } return vendor; }
	 */
	
	//namaNPWP
	
	@Path("/vendorprofile/createVendorProfile")
	@POST
	public VendorProfile createVendorProfile(@FormParam("vendor") int vendorId,
			@FormParam("jenisPajakPerusahaan") int jenisPajakPerusahaan,
			@FormParam("nomorPKP") String nomorPKP,
			@FormParam("kualifikasiVendor") int kualifikasiVendorId,
			@FormParam("title") String title,
			@FormParam("unitTerdaftar") String unitTerdaftar,
			@FormParam("bussinessArea") int bussinessAreaId,
			@FormParam("tipePerusahaan") String tipePerusahaan,
			@FormParam("namaPerusahaan") String namaPerusahaan,
			@FormParam("npwpPerusahaan") String npwpPerusahaan,
			@FormParam("namaNPWP") String namaNPWP,
			@FormParam("alamatNPWP") String alamatNPWP,
			@FormParam("provinsiNPWP") String provinsiNPWP,
			@FormParam("kotaNPWP") String kotaNPWP,
			@FormParam("namaSingkatan") String namaSingkatan,
			@FormParam("alamat") String alamat, @FormParam("kota") String kota,
			@FormParam("kodePos") String kodePos,
			@FormParam("poBox") String poBox,
			@FormParam("provinsi") String provinsi,
			@FormParam("telephone") String telephone,
			@FormParam("faksimile") String faksimile,
			@FormParam("email") String email,
			@FormParam("website") String website,
			@FormParam("namaContactPerson") String namaContactPerson,
			@FormParam("hpContactPerson") String hpContactPerson,
			@FormParam("emailContactPerson") String emailContactPerson,
			@FormParam("ktpContactPerson") String ktpContactPerson,
			@FormParam("jumlahKaryawan") String jumlahKaryawan,
			@FormParam("noAktaPendirian") String noAktaPendirian,
			@FormParam("tanggalBerdiri") Date tanggalBerdiri,
			@FormParam("nomorKKContactPerson") String noKK,
			@HeaderParam("Authorization") String token) {
		VendorProfile vendorProfile = new VendorProfile();

		if (vendorId > 0) {
			vendorProfile.setVendor(registrasiVendorSession
					.findVendor(vendorId));
		}

		if (jenisPajakPerusahaan > 0) {
			vendorProfile.setJenisPajakPerusahaan(jenisPajakPerusahaan);
		}

		if (nomorPKP != null && nomorPKP.length() > 0) {
			vendorProfile.setNomorPKP(nomorPKP);
		}

		if (kualifikasiVendorId > 0) {
			vendorProfile.setKualifikasiVendor(registrasiVendorSession
					.findKualifikasiVendor(kualifikasiVendorId));
		}
		
		//------------------------------tambahan-------------------------
		
				if (title != null && title.length() > 0) {
					vendorProfile.setTitle(title);
				}
				if (bussinessAreaId > 0) {
					vendorProfile.setBussinessArea(bussinessAreaId);
				}
				
				if (namaNPWP != null && namaNPWP.length() > 0) {
					vendorProfile.setNamaNPWP(namaNPWP);
				}
				
				if (alamatNPWP != null && alamatNPWP.length() > 0) {
					vendorProfile.setAlamatNPWP(alamatNPWP);
				}
				
				if (provinsiNPWP != null && provinsiNPWP.length() > 0) {
					vendorProfile.setProvinsiNPWP(provinsiNPWP);
				}
				
				if (kotaNPWP != null && kotaNPWP.length() > 0) {
					vendorProfile.setKotaNPWP(kotaNPWP);
				}
				
				if (noAktaPendirian != null && noAktaPendirian.length() > 0) {
					vendorProfile.setNoAktaPendirian(noAktaPendirian);
				}
				
				if (noKK != null && noKK.length() > 0) {
					vendorProfile.setNoKK(noKK);
				}
				
				//-----------------------------------------------------------------
				

		if (namaPerusahaan != null && namaPerusahaan.length() > 0) {
			vendorProfile.setNamaPerusahaan(namaPerusahaan);
		}

		if (npwpPerusahaan != null && npwpPerusahaan.length() > 0) {
			vendorProfile.setNpwpPerusahaan(npwpPerusahaan);
		}

		if (namaPerusahaan != null && namaPerusahaan.length() > 0) {
			vendorProfile.setNamaPerusahaan(namaPerusahaan);
		}

		if (unitTerdaftar != null && unitTerdaftar.length() > 0) {
			vendorProfile.setUnitTerdaftar(unitTerdaftar);
		}

		if (tipePerusahaan != null && tipePerusahaan.length() > 0) {
			vendorProfile.setTipePerusahaan(tipePerusahaan);
		}

		if (namaSingkatan != null && namaSingkatan.length() > 0) {
			vendorProfile.setNamaSingkatan(namaSingkatan);
		}

		if (alamat != null && alamat.length() > 0) {
			vendorProfile.setAlamat(alamat);
		}

		if (kota != null && kota.length() > 0) {
			vendorProfile.setKota(kota);
		}

		if (kodePos != null && kodePos.length() > 0) {
			vendorProfile.setKodePos(kodePos);
		}

		if (poBox != null && poBox.length() > 0) {
			vendorProfile.setPoBox(poBox);
		}

		if (provinsi != null && provinsi.length() > 0) {
			vendorProfile.setProvinsi(provinsi);
		}

		if (telephone != null && telephone.length() > 0) {
			vendorProfile.setTelephone(telephone);
		}

		if (faksimile != null && faksimile.length() > 0) {
			vendorProfile.setFaksimile(faksimile);
		}

		if (email != null && email.length() > 0) {
			vendorProfile.setEmail(email);
		}

		if (website != null && website.length() > 0) {
			vendorProfile.setWebsite(website);
		}

		if (namaContactPerson != null && namaContactPerson.length() > 0) {
			vendorProfile.setNamaContactPerson(namaContactPerson);
		}

		if (hpContactPerson != null && hpContactPerson.length() > 0) {
			vendorProfile.setHpContactPerson(hpContactPerson);
		}

		if (emailContactPerson != null && emailContactPerson.length() > 0) {
			vendorProfile.setEmailContactPerson(emailContactPerson);
		}

		if (ktpContactPerson != null && ktpContactPerson.length() > 0) {
			vendorProfile.setKtpContactPerson(ktpContactPerson);
		}
		
		if (jumlahKaryawan != null && jumlahKaryawan.length() > 0) {
			vendorProfile.setJumlahKaryawan(jumlahKaryawan);
		}
		
		if (tanggalBerdiri != null) {
			vendorProfile.setTanggalBerdiri(tanggalBerdiri);
		}

		vendorProfile.setCreated(new Date());
		vendorProfile.setIsDelete(0);

		registrasiVendorSession.insertVendorProfile(vendorProfile,
				tokenSession.findByToken(token));

		return vendorProfile;
	}
	
	@Path("/vendorprofile/getVendorProfileByNoPKP/{nomorPKP}")
	@GET
	public List<VendorProfile> getVendorProfileByNoPKP(
			@PathParam("nomorPKP") String nomorPKP) {
		return registrasiVendorSession.getVendorProfileByNoPKP(nomorPKP);
	}

	

	@Path("/vendorpic/createVendorPIC")
	@POST
	public VendorPIC createVendorPIC(@FormParam("vendor") int vendorId,
			@FormParam("nama") String nama,
			@FormParam("jabatan") int jabatanId,
			@FormParam("email") String email,
			@HeaderParam("Authorization") String token) {
		VendorPIC vendorPIC = new VendorPIC();

		if (vendorId > 0) {
			vendorPIC.setVendor(registrasiVendorSession.findVendor(vendorId));
		}

		if (jabatanId > 0) {
			vendorPIC
					.setJabatan(registrasiVendorSession.findJabatan(jabatanId));
		}

		if (nama != null && nama.length() > 0) {
			vendorPIC.setNama(nama);
		}

		if (email != null && email.length() > 0) {
			vendorPIC.setEmail(email);
		}

		vendorPIC.setCreated(new Date());
		vendorPIC.setIsDelete(0);

		registrasiVendorSession.insertVendorPIC(vendorPIC,
				tokenSession.findByToken(token));

		return vendorPIC;
	}
	@Path("/segmentasivendor/createSegmentasiVendor")
	@POST
	public SegmentasiVendor createSegmentasiVendor(
			@FormParam("vendor") int vendorId,
			@FormParam("subBidangUsaha") int subBidangUsahaId,
			@FormParam("asosiasi") String asosiasi,
			@FormParam("nomor") String nomor,
			@FormParam("tanggalMulai") Date tanggalMulai,
			@FormParam("tanggalBerakhir") Date tanggalBerakhir,
			@FormParam("jabatan") int jabatanId,
			@FormParam("email") String email,
			@HeaderParam("Authorization") String token) {
		SegmentasiVendor segmentasiVendor = new SegmentasiVendor();

		if (vendorId > 0) {
			segmentasiVendor.setVendor(registrasiVendorSession
					.findVendor(vendorId));
		}

		if (subBidangUsahaId > 0) {
			segmentasiVendor.setSubBidangUsaha(registrasiVendorSession
					.findSubBidangUsaha(subBidangUsahaId));
		}

		if (asosiasi != null && asosiasi.length() > 0) {
			segmentasiVendor.setAsosiasi(asosiasi);
		}

		if (nomor != null && nomor.length() > 0) {
			segmentasiVendor.setNomor(nomor);
		}

		if (tanggalMulai != null) {
			segmentasiVendor.setTanggalMulai(tanggalMulai);
		} else {
			segmentasiVendor.setTanggalMulai(new Date());
		}

		if (tanggalBerakhir != null) {
			segmentasiVendor.setTanggalBerakhir(tanggalBerakhir);
		} else {
			segmentasiVendor.setTanggalBerakhir(new Date());
		}

		if (jabatanId > 0) {
			segmentasiVendor.setJabatan(registrasiVendorSession
					.findJabatan(jabatanId));
		}

		if (email != null && email.length() > 0) {
			segmentasiVendor.setEmail(email);
		}

		segmentasiVendor.setCreated(new Date());
		segmentasiVendor.setIsDelete(0);

		registrasiVendorSession.insertSegmentasiVendor(segmentasiVendor,
				tokenSession.findByToken(token));

		return segmentasiVendor;
	}

	@Path("/dokumenregistrasivendor/createDokumenRegistrasiVendor")
	@POST
	public DokumenRegistrasiVendor createDokumenRegistrasiVendor(
			@FormParam("vendor") int vendorId,
			@FormParam("subject") String subject,
			@FormParam("namaDokumen") String namaDokumen,
			@FormParam("tanggalTerbit") Date tanggalTerbit,
			@FormParam("tanggalBerakhir") Date tanggalBerakhir,
			@FormParam("fileName") String fileName,
			@FormParam("pathFile") String pathFile,
			@FormParam("fileSize") long fileSize,
			@HeaderParam("Authorization") String token) {
		DokumenRegistrasiVendor dokumenRegistrasiVendor = new DokumenRegistrasiVendor();

		if (vendorId > 0) {
			dokumenRegistrasiVendor.setVendor(registrasiVendorSession
					.findVendor(vendorId));
		}

		if (subject != null && subject.length() > 0) {
			dokumenRegistrasiVendor.setSubject(subject);
		}

		if (namaDokumen != null && namaDokumen.length() > 0) {
			dokumenRegistrasiVendor.setNamaDokumen(namaDokumen);
		}

		if (tanggalTerbit != null) {
			dokumenRegistrasiVendor.setTanggalTerbit(tanggalTerbit);
		}

		if (tanggalBerakhir != null) {
			dokumenRegistrasiVendor.setTanggalBerakhir(tanggalBerakhir);
		}

		if (fileName != null && fileName.length() > 0) {
			dokumenRegistrasiVendor.setFileName(fileName);
		}

		if (pathFile != null && pathFile.length() > 0) {
			dokumenRegistrasiVendor.setPathFile(pathFile);
		}

		if (fileSize > 0) {
			dokumenRegistrasiVendor.setFileSize(fileSize);
		}

		dokumenRegistrasiVendor.setCreated(new Date());
		dokumenRegistrasiVendor.setIsDelete(0);

		registrasiVendorSession.insertDokumenRegistrasiVendor(
				dokumenRegistrasiVendor, tokenSession.findByToken(token));

		return dokumenRegistrasiVendor;
	}

	@Path("/peralatanvendor/createPeralatanVendor")
	@POST
	public PeralatanVendor createPeralatanVendor(
			@FormParam("vendor") int vendorId,
			@FormParam("jenis") String jenis,
			@FormParam("jumlah") double jumlah,
			@FormParam("kapasitas") double kapasitas,
			@FormParam("merk") String merk,
			@FormParam("tahunPembuatan") String tahunPembuatan,
			@FormParam("kondisi") Integer kondisiId,
			@FormParam("lokasi") String lokasi,
			@FormParam("buktiKepemilikan") Integer buktiKepemilikanId,
			@FormParam("fileNameBuktiKepemilikan") String fileNameBuktiKepemilikan,
			@FormParam("fileSizeBuktiKepemilikan") long fileSizeBuktiKepemilikan,
			@FormParam("pathFileBuktiKepemilikan") String pathFileBuktiKepemilikan,
			@FormParam("fileNameGambarPeralatan") String fileNameGambarPeralatan,
			@FormParam("fileSizeGambarPeralatan") long fileSizeGambarPeralatan,
			@FormParam("pathFileGambarPeralatan") String pathFileGambarPeralatan,
			@HeaderParam("Authorization") String token) {
		PeralatanVendor peralatanVendor = new PeralatanVendor();

		if (vendorId > 0) {
			peralatanVendor.setVendor(registrasiVendorSession
					.findVendor(vendorId));
		}

		if (jenis != null && jenis.length() > 0) {
			peralatanVendor.setJenis(jenis);
		}

		if (jumlah > 0) {
			peralatanVendor.setJumlah(jumlah);
		}

		if (kapasitas > 0) {
			peralatanVendor.setKapasitas(kapasitas);
		}

		if (merk != null && merk.length() > 0) {
			peralatanVendor.setMerk(merk);
		}

		if (tahunPembuatan != null && tahunPembuatan.length() > 0) {
			peralatanVendor.setTahunPembuatan(tahunPembuatan);
		}

		if (kondisiId != null) {
			peralatanVendor.setKondisi(registrasiVendorSession
					.findKondisiPeralatanVendor(kondisiId));
		}

		if (lokasi != null && lokasi.length() > 0) {
			peralatanVendor.setLokasi(lokasi);
		}

		if (buktiKepemilikanId != null) {
			peralatanVendor.setBuktiKepemilikan(registrasiVendorSession
					.findBuktiKepemilikan(buktiKepemilikanId));
		}

		if (fileNameBuktiKepemilikan != null
				&& fileNameBuktiKepemilikan.length() > 0) {
			peralatanVendor
					.setFileNameBuktiKepemilikan(fileNameBuktiKepemilikan);
		}

		if (fileSizeBuktiKepemilikan > 0) {
			peralatanVendor
					.setFileSizeBuktiKepemilikan(fileSizeBuktiKepemilikan);
		}

		if (pathFileBuktiKepemilikan != null
				&& pathFileBuktiKepemilikan.length() > 0) {
			peralatanVendor
					.setPathFileBuktiKepemilikan(pathFileBuktiKepemilikan);
		}

		if (fileNameGambarPeralatan != null
				&& fileNameGambarPeralatan.length() > 0) {
			peralatanVendor.setFileNameGambarPeralatan(fileNameGambarPeralatan);
		}

		if (fileSizeGambarPeralatan > 0) {
			peralatanVendor.setFileSizeGambarPeralatan(fileSizeGambarPeralatan);
		}

		if (pathFileGambarPeralatan != null
				&& pathFileGambarPeralatan.length() > 0) {
			peralatanVendor.setPathFileGambarPeralatan(pathFileGambarPeralatan);
		}

		peralatanVendor.setCreated(new Date());
		peralatanVendor.setIsDelete(0);

		registrasiVendorSession.insertPeralatanVendor(peralatanVendor,
				tokenSession.findByToken(token));

		return peralatanVendor;
	}

	@Path("/keuanganvendor/createKeuanganVendor")
	@POST
	public KeuanganVendor createKeuanganVendor(
			@FormParam("vendor") int vendorId,
			@FormParam("nomorAudit") String nomorAudit,
			@FormParam("tanggalAudit") Date tanggalAudit,
			@FormParam("namaAudit") String namaAudit,
			@FormParam("tahunKeuangan") String tahunKeuangan,
			@FormParam("kas") Double kas,
			@FormParam("bank") Double bank,
			@FormParam("totalPiutang") Double totalPiutang,
			@FormParam("persediaanBarang") Double persediaanBarang,
			@FormParam("pekerjaanDalamProses") Double pekerjaanDalamProses,
			@FormParam("totalAktivaLancar") Double totalAktivaLancar,
			@FormParam("peralatanDanMesin") Double peralatanDanMesin,
			@FormParam("inventaris") Double inventaris,
			@FormParam("gedungGedung") Double gedungGedung,
			@FormParam("totalAktivaTetap") Double totalAktivaTetap,
			@FormParam("aktivaLainnya") Double aktivaLainnya,
			@FormParam("totalAktiva") Double totalAktiva,
			@FormParam("hutangDagang") Double hutangDagang,
			@FormParam("hutangPajak") Double hutangPajak,
			@FormParam("hutangLainnya") Double hutangLainnya,
			@FormParam("totalHutangJangkaPendek") Double totalHutangJangkaPendek,
			@FormParam("hutangJangkaPanjang") Double hutangJangkaPanjang,
			@FormParam("kekayaanBersih") Double kekayaanBersih,
			@FormParam("totalPasiva") Double totalPasiva,
			@HeaderParam("Authorization") String token) {
		KeuanganVendor keuanganVendor = new KeuanganVendor();

		if (vendorId > 0) {
			keuanganVendor.setVendor(registrasiVendorSession
					.findVendor(vendorId));
		}

		if (nomorAudit != null && nomorAudit.length() > 0) {
			keuanganVendor.setNomorAudit(nomorAudit);
		}

		if (tanggalAudit != null) {
			keuanganVendor.setTanggalAudit(tanggalAudit);
		}

		if (namaAudit != null && namaAudit.length() > 0) {
			keuanganVendor.setNamaAudit(namaAudit);
		}

		if (tahunKeuangan != null && tahunKeuangan.length() > 0) {
			keuanganVendor.setTahunKeuangan(tahunKeuangan);
		}

		if (kas != null && kas > 0) {
			keuanganVendor.setKas(kas);
		}

		if (bank != null && bank > 0) {
			keuanganVendor.setBank(bank);
		}

		if (totalPiutang != null && totalPiutang > 0) {
			keuanganVendor.setTotalPiutang(totalPiutang);
		}

		if (persediaanBarang != null && persediaanBarang > 0) {
			keuanganVendor.setPersediaanBarang(persediaanBarang);
		}

		if (pekerjaanDalamProses != null && pekerjaanDalamProses > 0) {
			keuanganVendor.setPekerjaanDalamProses(pekerjaanDalamProses);
		}

		if (totalAktivaLancar != null && totalAktivaLancar > 0) {
			keuanganVendor.setTotalAktivaLancar(totalAktivaLancar);
		}

		if (peralatanDanMesin != null && peralatanDanMesin > 0) {
			keuanganVendor.setPeralatanDanMesin(peralatanDanMesin);
		}

		if (inventaris != null && inventaris > 0) {
			keuanganVendor.setInventaris(inventaris);
		}

		if (gedungGedung != null && gedungGedung > 0) {
			keuanganVendor.setGedungGedung(gedungGedung);
		}

		if (totalAktivaTetap != null && totalAktivaTetap > 0) {
			keuanganVendor.setTotalAktivaTetap(totalAktivaTetap);
		}

		if (aktivaLainnya != null && aktivaLainnya > 0) {
			keuanganVendor.setAktivaLainnya(aktivaLainnya);
		}

		if (totalAktiva != null && totalAktiva > 0) {
			keuanganVendor.setTotalAktiva(totalAktiva);
		}

		if (hutangDagang != null && hutangDagang > 0) {
			keuanganVendor.setHutangDagang(hutangDagang);
		}

		if (hutangPajak != null && hutangPajak > 0) {
			keuanganVendor.setHutangPajak(hutangPajak);
		}

		if (hutangLainnya != null && hutangLainnya > 0) {
			keuanganVendor.setHutangLainnya(hutangLainnya);
		}

		if (totalHutangJangkaPendek != null && totalHutangJangkaPendek > 0) {
			keuanganVendor.setTotalHutangJangkaPendek(totalHutangJangkaPendek);
		}

		if (hutangJangkaPanjang != null && hutangJangkaPanjang > 0) {
			keuanganVendor.setHutangJangkaPanjang(hutangJangkaPanjang);
		}

		if (kekayaanBersih != null && kekayaanBersih > 0) {
			keuanganVendor.setKekayaanBersih(kekayaanBersih);
		}

		if (totalPasiva != null && totalPasiva > 0) {
			keuanganVendor.setTotalPasiva(totalPasiva);
		}

		keuanganVendor.setCreated(new Date());
		keuanganVendor.setIsDelete(0);

		registrasiVendorSession.insertKeuanganVendor(keuanganVendor,
				tokenSession.findByToken(token));

		return keuanganVendor;
	}

	@Path("/pengalamanvendor/createPengalamanVendor")
	@POST
	public PengalamanVendor createPengalamanVendor(
			@FormParam("vendor") int vendorId,
			@FormParam("tipePengalaman") String tipePengalaman,
			@FormParam("namaPekerjaan") String namaPekerjaan,
			@FormParam("lokasiPekerjaan") String lokasiPekerjaan,
			@FormParam("bidangUsaha") String bidangUsaha,
			@FormParam("mulaiKerjasama") Date mulaiKerjasama,
			@FormParam("nilaiKontrak") double nilaiKontrak,
			@FormParam("mataUang") int mataUangId,
			@FormParam("fileName") String fileName,
			@FormParam("pathFile") String pathFile,
			@FormParam("fileSize") long fileSize,
			@HeaderParam("Authorization") String token) {
		PengalamanVendor pengalamanVendor = new PengalamanVendor();

		if (vendorId > 0) {
			pengalamanVendor.setVendor(registrasiVendorSession
					.findVendor(vendorId));
		}

		if (tipePengalaman != null && tipePengalaman.length() > 0) {
			pengalamanVendor.setTipePengalaman(tipePengalaman);
		}

		if (namaPekerjaan != null && namaPekerjaan.length() > 0) {
			pengalamanVendor.setNamaPekerjaan(namaPekerjaan);
		}

		if (lokasiPekerjaan != null && lokasiPekerjaan.length() > 0) {
			pengalamanVendor.setLokasiPekerjaan(lokasiPekerjaan);
		}

		if (bidangUsaha != null && bidangUsaha.length() > 0) {
			pengalamanVendor.setBidangUsaha(bidangUsaha);
		}

		if (mulaiKerjasama != null) {
			pengalamanVendor.setMulaiKerjasama(mulaiKerjasama);
		}

		if (nilaiKontrak > 0) {
			pengalamanVendor.setNilaiKontrak(nilaiKontrak);
		}

		if (mataUangId > 0) {
			pengalamanVendor.setMataUang(registrasiVendorSession
					.findMataUang(mataUangId));
		}

		if (fileName != null && fileName.length() > 0) {
			pengalamanVendor.setFileName(fileName);
		}

		if (pathFile != null && pathFile.length() > 0) {
			pengalamanVendor.setPathFile(pathFile);
		}

		if (fileSize > 0) {
			pengalamanVendor.setFileSize(fileSize);
		}

		pengalamanVendor.setCreated(new Date());
		pengalamanVendor.setIsDelete(0);

		registrasiVendorSession.insertPengalamanVendor(pengalamanVendor,
				tokenSession.findByToken(token));

		return pengalamanVendor;
	}

	@Path("/user/create")
	@POST
	public User insertUser(@FormParam("username") String username,
			@FormParam("namaPengguna") String namaPengguna,
			@FormParam("email") String email,
			@FormParam("password") String password,
			@FormParam("roleId") Integer roleId,
			@HeaderParam("Authorization") String token) throws Exception {
		User x = new User();
		x.setUsername(username);
		x.setNamaPengguna(namaPengguna);
		x.setEmail(email);
		x.setPassword(KeyGenHelper.finalEncrypt(password));
		x.setUserId(0);
		userSession.insertUser(x, tokenSession.findByToken(token));
		
		// roleuser PM
		RoleUser ruPM = new RoleUser();
		ruPM.setUser(userSession.find(x.getId()));
		ruPM.setRole(roleSession.find(Constant.ROLE_USER_VENDOR_PM));
		roleUserSession.insertRoleUser(ruPM);
		
		// roleuser CM
		RoleUser ruCM = new RoleUser();
		ruCM.setUser(userSession.find(x.getId()));
		ruCM.setRole(roleSession.find(Constant.ROLE_USER_VENDOR_CM));
		roleUserSession.insertRoleUser(ruCM);
		
		return x;
	}

	@Path("/getDataList")
	@GET
	public Map getDataList() {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		map.put("kualifikasiVendorList", kualifikasiVendorSession.getKualifikasiVendorList());
		map.put("organisasiList", organisasiSession.getOrganisasiListAfco());
		map.put("wilayahList", wilayahSession.getPropinsiList());
		map.put("jabatanList", jabatanSession.getJabatanList());
		map.put("mataUangList", mataUangSession.getMataUanglist());
		map.put("bidangUsahaList", bidangUsahaSession.getBidangUsahaList());
		
		return map;
	}
	
	@Path("/user/get-by-username/{email}")
	@GET
	public List<User> getUserByUserName(@PathParam("email") String email) {
		return userSession.getUserByUserName(email);
	}

	@Path("/kualifikasivendor/getKualifikasiVendorList")
	@GET
	public List<KualifikasiVendor> getKualifikasiVendorList() {
		return kualifikasiVendorSession.getKualifikasiVendorList();
	}

	@Path("/organisasi/get-list")
	@GET
	public List<Organisasi> getOrganisasiList() {
		return organisasiSession.getOrganisasiAll();
	}
	
	@Path("/organisasi/getByParentId/{id}")
	@GET
	public List<Organisasi> getOrganisasiListByParentId(@PathParam("id") int parentId) {
		return organisasiSession.getOrganisasiListByParentId(parentId);
	}

	@Path("/wilayah/getPropinsiList")
	@GET
	public List<Wilayah> getPropinsiList() {
		return wilayahSession.getPropinsiList();
	}

	@Path("/wilayah/getKotaList/{propinsiId}")
	@GET
	public List<Wilayah> getKotaList(@PathParam("propinsiId") Integer propinsiId) {
		return wilayahSession.getKotaList(propinsiId);
	}

	@Path("/jabatan/get-list")
	@GET
	public List<Jabatan> getJabatanList() {
		return jabatanSession.getJabatanList();
	}
	

	@Path("/matauang/getMataUangList")
	@GET
	public List<MataUang> getMataUanglist() {
		return mataUangSession.getMataUanglist();
	}

	@Path("/bidangusaha/getBidangUsahaList")
	@GET
	public List<BidangUsaha> getBidangUsahaList() {
		return bidangUsahaSession.getBidangUsahaList();
	}

	@Path("/negara/get-list")
	@GET
	public List<Negara> getNegaraList() {
		return negaraSession.getNegaraList();
	}

	@Path("/asosiasi/getList")
	@GET
	public List<Asosiasi> getAsosiasiList() {
		return asosiasiSession.getAsosiasiList();
	}

	@Path("/subbidangusaha/get-by-bidang-usaha-id/{id}")
	@GET
	public List<SubBidangUsaha> getSubBidangUsahaByBidangUsahaId(
			@PathParam("id") int bidangUsahaId) {
		return subBidangUsahaSession
				.getSubBidangUsahaByBidangUsahaId(bidangUsahaId);
	}

	@Path("/kondisiperalatanvendor/getList")
	@GET
	public List<KondisiPeralatanVendor> getKondisiPeralatanVendorList() {
		return kondisiSession.getKondisiPeralatanVendorList();
	}

	@Path("/buktikepemilikan/getList")
	@GET
	public List<BuktiKepemilikan> getBuktiKepemilikanList() {
		return buktiKepemilikanSession.getBuktiKepemilikanList();
	}
	
	@Path("/vendorSKD/getList")
	@GET
	public List<VendorSKD> getVendorSKDList() {
		return vendorSKDSession.getVendorSKDList();
	}

	@Path("/bank/getList")
	@GET
	public List<Bank> getBankList() {
		return bankSession.getBankList();
	}


}
