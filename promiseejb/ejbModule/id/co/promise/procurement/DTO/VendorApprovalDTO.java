package id.co.promise.procurement.DTO;

import java.util.List;

import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BankVendorDraft;
import id.co.promise.procurement.entity.BuktiKepemilikan;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.DokumenRegistrasiVendorDraft;
import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.KeuanganVendorDraft;
import id.co.promise.procurement.entity.KondisiPeralatanVendor;
import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PengalamanVendorDraft;
import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.PeralatanVendorDraft;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SegmentasiVendorDraft;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorDraft;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorPICDraft;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.entity.VendorProfileDraft;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.VendorSKDDraft;

public class VendorApprovalDTO {
	
	private User user;
	private Vendor vendor;

	private VendorSKD vendorSkd;
	private VendorSKDDraft vendorSkdDraft;
	private Boolean isDraftDuplicate;
	private VendorDraft vendorDraft;
	private VendorProfile vendorProfile;
	private VendorProfileDraft vendorProfileDraft;
	
	List <BankVendor> bankVendorList;
	List <PengalamanVendor> pengalamanVendorList;
	List <SegmentasiVendor> segmentasiVendorList;	 
	List <PeralatanVendor> peralatanVendorList;
	List <KeuanganVendor> keuanganVendorList;
	List <VendorPIC> vendorPIC;
	List <VendorPICDraft> vendorPICDraft;
	List <Jabatan> jabatanPenanggungJawabList;
	List <BankVendorDraft> bankVendorDraftList;
	
	List <PengalamanVendorDraft> pengalamanVendorDraft;
	List <SegmentasiVendorDraft> segmentasiVendorDraft;	 
	List <PeralatanVendorDraft> peralatanVendorDraft;
	List <KeuanganVendorDraft> keuanganVendorDraft;
	List <DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList;
	List <DokumenRegistrasiVendor> dokumenRegistrasiVendorList;
	
	//pengalaman vendor
	List<PengalamanVendor> pengalamanPelangganVendorList;
	List<PengalamanVendor> pengalamanMitraVendorList;
	List<PengalamanVendor> pengalamanInProgressVendorList;
	
	//pengalaman vendor Draft
	List<PengalamanVendorDraft> pengalamanPelangganVendorDraftList;
	List<PengalamanVendorDraft> pengalamanMitraVendorDraftList;
	List<PengalamanVendorDraft> pengalamanInProgressVendorDraftList;
		
	List<KondisiPeralatanVendor> kondisiPeralatanVendorList;
	List<BuktiKepemilikan> buktiKepemilikanList;
	
	List<DokumenRegistrasiVendor> SKBHistoryList;
	
	//dokumen vendor main
	private DokumenRegistrasiVendor dataDokRegForm;
	private DokumenRegistrasiVendor dataDokAkte;
	private DokumenRegistrasiVendor dataDokSalinan;
	private DokumenRegistrasiVendor dataDokSiup;
	private DokumenRegistrasiVendor dataDokPKS;
	private DokumenRegistrasiVendor dataDokSPR;
	private DokumenRegistrasiVendor dataDokSPB;
	private DokumenRegistrasiVendor dataDokSKB;
	private DokumenRegistrasiVendor dataDokBuktiFisik;
	private DokumenRegistrasiVendor dataDokQuality;
	private DokumenRegistrasiVendor dataDokTeknik;
	
	//dokumen vendor draft
	private DokumenRegistrasiVendorDraft dataDokRegFormDraft;
	private DokumenRegistrasiVendorDraft dataDokAkteDraft;
	private DokumenRegistrasiVendorDraft dataDokSalinanDraft;
	private DokumenRegistrasiVendorDraft dataDokSiupDraft;
	private DokumenRegistrasiVendorDraft dataDokPKSDraft;
	private DokumenRegistrasiVendorDraft dataDokSPRDraft;
	private DokumenRegistrasiVendorDraft dataDokSPBDraft;
	private DokumenRegistrasiVendorDraft dataDokSKBDraft;
	private DokumenRegistrasiVendorDraft dataDokBuktiFisikDraft;
	private DokumenRegistrasiVendorDraft dataDokQualityDraft;
	private DokumenRegistrasiVendorDraft dataDokTeknikDraft;
	
	
	
	
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public VendorProfile getVendorProfile() {
		return vendorProfile;
	}
	public void setVendorProfile(VendorProfile vendorProfile) {
		this.vendorProfile = vendorProfile;
	}
	
	public List<BankVendor> getBankVendorList() {
		return bankVendorList;
	}
	public void setBankVendorList(List<BankVendor> bankVendorList) {
		this.bankVendorList = bankVendorList;
	}
	
	public List<BankVendorDraft> getBankVendorDraftList() {
		return bankVendorDraftList;
	}
	public void setBankVendorDraftList(List<BankVendorDraft> bankVendorDraftList) {
		this.bankVendorDraftList = bankVendorDraftList;
	}
	public List<PengalamanVendor> getPengalamanVendorList() {
		return pengalamanVendorList;
	}
	public void setPengalamanVendorList(List<PengalamanVendor> pengalamanVendorList) {
		this.pengalamanVendorList = pengalamanVendorList;
	}
	public List<SegmentasiVendor> getSegmentasiVendorList() {
		return segmentasiVendorList;
	}
	public void setSegmentasiVendorList(List<SegmentasiVendor> segmentasiVendorList) {
		this.segmentasiVendorList = segmentasiVendorList;
	}
	public List<PeralatanVendor> getPeralatanVendorList() {
		return peralatanVendorList;
	}
	public void setPeralatanVendorList(List<PeralatanVendor> peralatanVendorList) {
		this.peralatanVendorList = peralatanVendorList;
	}
	public List<KeuanganVendor> getKeuanganVendorList() {
		return keuanganVendorList;
	}
	public void setKeuanganVendorList(List<KeuanganVendor> keuanganVendorList) {
		this.keuanganVendorList = keuanganVendorList;
	}
	public VendorSKD getVendorSkd() {
		return vendorSkd;
	}
	public void setVendorSkd(VendorSKD vendorSkd) {
		this.vendorSkd = vendorSkd;
	}
	public VendorDraft getVendorDraft() {
		return vendorDraft;
	}
	public void setVendorDraft(VendorDraft vendorDraft) {
		this.vendorDraft = vendorDraft;
	}
	public VendorProfileDraft getVendorProfileDraft() {
		return vendorProfileDraft;
	}
	public void setVendorProfileDraft(VendorProfileDraft vendorProfileDraft) {
		this.vendorProfileDraft = vendorProfileDraft;
	}
	
	public VendorSKDDraft getVendorSkdDraft() {
		return vendorSkdDraft;
	}
	public void setVendorSkdDraft(VendorSKDDraft vendorSkdDraft) {
		this.vendorSkdDraft = vendorSkdDraft;
	}
	
	public List<PengalamanVendorDraft> getPengalamanVendorDraft() {
		return pengalamanVendorDraft;
	}
	public void setPengalamanVendorDraft(
			List<PengalamanVendorDraft> pengalamanVendorDraft) {
		this.pengalamanVendorDraft = pengalamanVendorDraft;
	}
	public List<SegmentasiVendorDraft> getSegmentasiVendorDraft() {
		return segmentasiVendorDraft;
	}
	public void setSegmentasiVendorDraft(
			List<SegmentasiVendorDraft> segmentasiVendorDraft) {
		this.segmentasiVendorDraft = segmentasiVendorDraft;
	}
	public List<PeralatanVendorDraft> getPeralatanVendorDraft() {
		return peralatanVendorDraft;
	}
	public void setPeralatanVendorDraft(
			List<PeralatanVendorDraft> peralatanVendorDraft) {
		this.peralatanVendorDraft = peralatanVendorDraft;
	}
	public List<KeuanganVendorDraft> getKeuanganVendorDraft() {
		return keuanganVendorDraft;
	}
	public void setKeuanganVendorDraft(List<KeuanganVendorDraft> keuanganVendorDraft) {
		this.keuanganVendorDraft = keuanganVendorDraft;
	}
	public List<PengalamanVendor> getPengalamanPelangganVendorList() {
		return pengalamanPelangganVendorList;
	}
	public void setPengalamanPelangganVendorList(
			List<PengalamanVendor> pengalamanPelangganVendorList) {
		this.pengalamanPelangganVendorList = pengalamanPelangganVendorList;
	}
	public List<PengalamanVendor> getPengalamanMitraVendorList() {
		return pengalamanMitraVendorList;
	}
	public void setPengalamanMitraVendorList(
			List<PengalamanVendor> pengalamanMitraVendorList) {
		this.pengalamanMitraVendorList = pengalamanMitraVendorList;
	}
	public List<PengalamanVendor> getPengalamanInProgressVendorList() {
		return pengalamanInProgressVendorList;
	}
	public void setPengalamanInProgressVendorList(
			List<PengalamanVendor> pengalamanInProgressVendorList) {
		this.pengalamanInProgressVendorList = pengalamanInProgressVendorList;
	}
	public List<KondisiPeralatanVendor> getKondisiPeralatanVendorList() {
		return kondisiPeralatanVendorList;
	}
	public void setKondisiPeralatanVendorList(
			List<KondisiPeralatanVendor> kondisiPeralatanVendorList) {
		this.kondisiPeralatanVendorList = kondisiPeralatanVendorList;
	}
	public List<BuktiKepemilikan> getBuktiKepemilikanList() {
		return buktiKepemilikanList;
	}
	public void setBuktiKepemilikanList(List<BuktiKepemilikan> buktiKepemilikanList) {
		this.buktiKepemilikanList = buktiKepemilikanList;
	}
	public DokumenRegistrasiVendor getDataDokRegForm() {
		return dataDokRegForm;
	}
	public void setDataDokRegForm(DokumenRegistrasiVendor dataDokRegForm) {
		this.dataDokRegForm = dataDokRegForm;
	}
	public DokumenRegistrasiVendor getDataDokAkte() {
		return dataDokAkte;
	}
	public void setDataDokAkte(DokumenRegistrasiVendor dataDokAkte) {
		this.dataDokAkte = dataDokAkte;
	}
	public DokumenRegistrasiVendor getDataDokSalinan() {
		return dataDokSalinan;
	}
	public void setDataDokSalinan(DokumenRegistrasiVendor dataDokSalinan) {
		this.dataDokSalinan = dataDokSalinan;
	}
	public DokumenRegistrasiVendor getDataDokSiup() {
		return dataDokSiup;
	}
	public void setDataDokSiup(DokumenRegistrasiVendor dataDokSiup) {
		this.dataDokSiup = dataDokSiup;
	}
	public DokumenRegistrasiVendor getDataDokPKS() {
		return dataDokPKS;
	}
	public void setDataDokPKS(DokumenRegistrasiVendor dataDokPKS) {
		this.dataDokPKS = dataDokPKS;
	}
	public DokumenRegistrasiVendor getDataDokSPR() {
		return dataDokSPR;
	}
	public void setDataDokSPR(DokumenRegistrasiVendor dataDokSPR) {
		this.dataDokSPR = dataDokSPR;
	}
	public DokumenRegistrasiVendor getDataDokSPB() {
		return dataDokSPB;
	}
	public void setDataDokSPB(DokumenRegistrasiVendor dataDokSPB) {
		this.dataDokSPB = dataDokSPB;
	}
	public DokumenRegistrasiVendor getDataDokBuktiFisik() {
		return dataDokBuktiFisik;
	}
	public void setDataDokBuktiFisik(DokumenRegistrasiVendor dataDokBuktiFisik) {
		this.dataDokBuktiFisik = dataDokBuktiFisik;
	}
	public DokumenRegistrasiVendor getDataDokQuality() {
		return dataDokQuality;
	}
	public void setDataDokQuality(DokumenRegistrasiVendor dataDokQuality) {
		this.dataDokQuality = dataDokQuality;
	}
	public DokumenRegistrasiVendor getDataDokTeknik() {
		return dataDokTeknik;
	}
	public void setDataDokTeknik(DokumenRegistrasiVendor dataDokTeknik) {
		this.dataDokTeknik = dataDokTeknik;
	}
	public DokumenRegistrasiVendorDraft getDataDokRegFormDraft() {
		return dataDokRegFormDraft;
	}
	public void setDataDokRegFormDraft(
			DokumenRegistrasiVendorDraft dataDokRegFormDraft) {
		this.dataDokRegFormDraft = dataDokRegFormDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokAkteDraft() {
		return dataDokAkteDraft;
	}
	public void setDataDokAkteDraft(DokumenRegistrasiVendorDraft dataDokAkteDraft) {
		this.dataDokAkteDraft = dataDokAkteDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokSalinanDraft() {
		return dataDokSalinanDraft;
	}
	public void setDataDokSalinanDraft(
			DokumenRegistrasiVendorDraft dataDokSalinanDraft) {
		this.dataDokSalinanDraft = dataDokSalinanDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokSiupDraft() {
		return dataDokSiupDraft;
	}
	public void setDataDokSiupDraft(DokumenRegistrasiVendorDraft dataDokSiupDraft) {
		this.dataDokSiupDraft = dataDokSiupDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokPKSDraft() {
		return dataDokPKSDraft;
	}
	public void setDataDokPKSDraft(DokumenRegistrasiVendorDraft dataDokPKSDraft) {
		this.dataDokPKSDraft = dataDokPKSDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokSPRDraft() {
		return dataDokSPRDraft;
	}
	public void setDataDokSPRDraft(DokumenRegistrasiVendorDraft dataDokSPRDraft) {
		this.dataDokSPRDraft = dataDokSPRDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokSPBDraft() {
		return dataDokSPBDraft;
	}
	public void setDataDokSPBDraft(DokumenRegistrasiVendorDraft dataDokSPBDraft) {
		this.dataDokSPBDraft = dataDokSPBDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokBuktiFisikDraft() {
		return dataDokBuktiFisikDraft;
	}
	public void setDataDokBuktiFisikDraft(
			DokumenRegistrasiVendorDraft dataDokBuktiFisikDraft) {
		this.dataDokBuktiFisikDraft = dataDokBuktiFisikDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokQualityDraft() {
		return dataDokQualityDraft;
	}
	public void setDataDokQualityDraft(
			DokumenRegistrasiVendorDraft dataDokQualityDraft) {
		this.dataDokQualityDraft = dataDokQualityDraft;
	}
	public DokumenRegistrasiVendorDraft getDataDokTeknikDraft() {
		return dataDokTeknikDraft;
	}
	public void setDataDokTeknikDraft(
			DokumenRegistrasiVendorDraft dataDokTeknikDraft) {
		this.dataDokTeknikDraft = dataDokTeknikDraft;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public List<PengalamanVendorDraft> getPengalamanPelangganVendorDraftList() {
		return pengalamanPelangganVendorDraftList;
	}
	public void setPengalamanPelangganVendorDraftList(
			List<PengalamanVendorDraft> pengalamanPelangganVendorDraftList) {
		this.pengalamanPelangganVendorDraftList = pengalamanPelangganVendorDraftList;
	}
	public List<PengalamanVendorDraft> getPengalamanMitraVendorDraftList() {
		return pengalamanMitraVendorDraftList;
	}
	public void setPengalamanMitraVendorDraftList(
			List<PengalamanVendorDraft> pengalamanMitraVendorDraftList) {
		this.pengalamanMitraVendorDraftList = pengalamanMitraVendorDraftList;
	}
	public List<PengalamanVendorDraft> getPengalamanInProgressVendorDraftList() {
		return pengalamanInProgressVendorDraftList;
	}
	public void setPengalamanInProgressVendorDraftList(
			List<PengalamanVendorDraft> pengalamanInProgressVendorDraftList) {
		this.pengalamanInProgressVendorDraftList = pengalamanInProgressVendorDraftList;
	}
	public List<VendorPIC> getVendorPIC() {
		return vendorPIC;
	}
	public void setVendorPIC(List<VendorPIC> vendorPIC) {
		this.vendorPIC = vendorPIC;
	}
	public List<VendorPICDraft> getVendorPICDraft() {
		return vendorPICDraft;
	}
	public void setVendorPICDraft(List<VendorPICDraft> vendorPICDraft) {
		this.vendorPICDraft = vendorPICDraft;
	}
	public List<Jabatan> getJabatanPenanggungJawabList() {
		return jabatanPenanggungJawabList;
	}
	public void setJabatanPenanggungJawabList(
			List<Jabatan> jabatanPenanggungJawabList) {
		this.jabatanPenanggungJawabList = jabatanPenanggungJawabList;
	}
	public List<DokumenRegistrasiVendorDraft> getDokumenRegistrasiVendorDraftList() {
		return dokumenRegistrasiVendorDraftList;
	}
	public void setDokumenRegistrasiVendorDraftList(
			List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList) {
		this.dokumenRegistrasiVendorDraftList = dokumenRegistrasiVendorDraftList;
	}
	public List<DokumenRegistrasiVendor> getDokumenRegistrasiVendorList() {
		return dokumenRegistrasiVendorList;
	}
	public void setDokumenRegistrasiVendorList(
			List<DokumenRegistrasiVendor> dokumenRegistrasiVendorList) {
		this.dokumenRegistrasiVendorList = dokumenRegistrasiVendorList;
	}
	public DokumenRegistrasiVendor getDataDokSKB() {
		return dataDokSKB;
	}
	public void setDataDokSKB(DokumenRegistrasiVendor dataDokSKB) {
		this.dataDokSKB = dataDokSKB;
	}
	public DokumenRegistrasiVendorDraft getDataDokSKBDraft() {
		return dataDokSKBDraft;
	}
	public void setDataDokSKBDraft(DokumenRegistrasiVendorDraft dataDokSKBDraft) {
		this.dataDokSKBDraft = dataDokSKBDraft;
	}
	public List<DokumenRegistrasiVendor> getSKBHistoryList() {
		return SKBHistoryList;
	}
	public void setSKBHistoryList(List<DokumenRegistrasiVendor> sKBHistoryList) {
		SKBHistoryList = sKBHistoryList;
	}
	public Boolean getIsDraftDuplicate() {
		return isDraftDuplicate;
	}
	public void setIsDraftDuplicate(Boolean isDraftDuplicate) {
		this.isDraftDuplicate = isDraftDuplicate;
	}
	
	
}
