package id.co.promise.procurement.master;


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
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorDraft;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorPICDraft;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.entity.VendorProfileDraft;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.Wilayah;


import java.util.List;

public class VendorDaftarDTO {
	
	private Vendor vendor;
	private User vendorUserBean;
	private VendorProfile vendorProfileBean;
	private String businessArea;
	private Organisasi businessAreaOrganisasi;
	private Integer status;
	private VendorProfileDraft vendorProfileBeanDraft;
	private VendorDraft vendorDraft;
	private List<VendorPICDraft> vendorPICDraftList;
	private Negara negaraNpwp;
	private Negara negaraPerusahaan;
	private Wilayah provinsiNpwp;
	private Wilayah provinsiPerusahaan;
	private Wilayah kotaNPWP;
	private Wilayah kotaPerusahaan;
	
	List<BidangUsaha> bidangUsahaList;
	List<MataUang> mataUangList;
	List<Jabatan> jabatanPenanggungJawabList;
	List<KualifikasiVendor> kualifikasiVendorList;
	List<Organisasi> unitTerdaftarList;
	List<Wilayah> provinsiPerusahaanList;
	List<Wilayah> kotaPerusahaanList;
	List<Wilayah> wilayahList;
	List<BankVendor> dataBankList;
	List<SegmentasiVendor> dataSegmentasiList;
	List<VendorPIC> penanggungJawabList;
	List<DokumenRegistrasiVendor> dataHistorySKBList;
	List<DokumenRegistrasiVendor> dataDokRegFormList;
	List<DokumenRegistrasiVendor> dataDokAkteList;
	List<DokumenRegistrasiVendor> dataDokSalinanList;
	List<DokumenRegistrasiVendor> dataDokSiupList;
	List<DokumenRegistrasiVendor> dataDokPKSList;
	List<DokumenRegistrasiVendor> dataDokSPRList;
	List<DokumenRegistrasiVendor> dataDokSPBList;
	List<DokumenRegistrasiVendor> dataDokBuktiFisikList;
	List<DokumenRegistrasiVendor> dataDokQualityList;
	List<DokumenRegistrasiVendor> dataDokTeknikList;
	List<DokumenRegistrasiVendor> dataDokSKBList;
	List<VendorSKD> dataDokSKDList;
	List<PeralatanVendor> peralatanListVendor;
	List<KeuanganVendor> keuanganListVendor;
	List<PengalamanVendor> pengalamanPelangganVendorList;
	List<PengalamanVendor> pengalamanMitraVendorList;
	List<PengalamanVendor> pengalamanInProgressVendorList;
	List<KondisiPeralatanVendor> kondisiPeralatanVendorList;
	List<BuktiKepemilikan> buktiKepemilikanList;
	List<Negara> negaraList;
	
	
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public User getVendorUserBean() {
		return vendorUserBean;
	}
	public void setVendorUserBean(User vendorUserBean) {
		this.vendorUserBean = vendorUserBean;
	}
	public VendorProfile getVendorProfileBean() {
		return vendorProfileBean;
	}
	public void setVendorProfileBean(VendorProfile vendorProfileBean) {
		this.vendorProfileBean = vendorProfileBean;
	}
	public String getBusinessArea() {
		return businessArea;
	}
	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
	}
	public Organisasi getBusinessAreaOrganisasi() {
		return businessAreaOrganisasi;
	}
	public void setBusinessAreaOrganisasi(Organisasi businessAreaOrganisasi) {
		this.businessAreaOrganisasi = businessAreaOrganisasi;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public VendorProfileDraft getVendorProfileBeanDraft() {
		return vendorProfileBeanDraft;
	}
	public void setVendorProfileBeanDraft(VendorProfileDraft vendorProfileBeanDraft) {
		this.vendorProfileBeanDraft = vendorProfileBeanDraft;
	}
	public VendorDraft getVendorDraft() {
		return vendorDraft;
	}
	public void setVendorDraft(VendorDraft vendorDraft) {
		this.vendorDraft = vendorDraft;
	}
	public List<VendorPICDraft> getVendorPICDraftList() {
		return vendorPICDraftList;
	}
	public void setVendorPICDraftList(List<VendorPICDraft> vendorPICDraftList) {
		this.vendorPICDraftList = vendorPICDraftList;
	}
	public List<BidangUsaha> getBidangUsahaList() {
		return bidangUsahaList;
	}
	public void setBidangUsahaList(List<BidangUsaha> bidangUsahaList) {
		this.bidangUsahaList = bidangUsahaList;
	}
	public List<MataUang> getMataUangList() {
		return mataUangList;
	}
	public void setMataUangList(List<MataUang> mataUangList) {
		this.mataUangList = mataUangList;
	}
	public List<Jabatan> getJabatanPenanggungJawabList() {
		return jabatanPenanggungJawabList;
	}
	public void setJabatanPenanggungJawabList(
			List<Jabatan> jabatanPenanggungJawabList) {
		this.jabatanPenanggungJawabList = jabatanPenanggungJawabList;
	}
	public List<KualifikasiVendor> getKualifikasiVendorList() {
		return kualifikasiVendorList;
	}
	public void setKualifikasiVendorList(
			List<KualifikasiVendor> kualifikasiVendorList) {
		this.kualifikasiVendorList = kualifikasiVendorList;
	}
	public List<Organisasi> getUnitTerdaftarList() {
		return unitTerdaftarList;
	}
	public void setUnitTerdaftarList(List<Organisasi> unitTerdaftarList) {
		this.unitTerdaftarList = unitTerdaftarList;
	}
	public List<Wilayah> getProvinsiPerusahaanList() {
		return provinsiPerusahaanList;
	}
	public void setProvinsiPerusahaanList(List<Wilayah> provinsiPerusahaanList) {
		this.provinsiPerusahaanList = provinsiPerusahaanList;
	}
	public List<Wilayah> getKotaPerusahaanList() {
		return kotaPerusahaanList;
	}
	public void setKotaPerusahaanList(List<Wilayah> kotaPerusahaanList) {
		this.kotaPerusahaanList = kotaPerusahaanList;
	}
	public List<Wilayah> getWilayahList() {
		return wilayahList;
	}
	public void setWilayahList(List<Wilayah> wilayahList) {
		this.wilayahList = wilayahList;
	}
	public List<BankVendor> getDataBankList() {
		return dataBankList;
	}
	public void setDataBankList(List<BankVendor> dataBankList) {
		this.dataBankList = dataBankList;
	}
	public List<SegmentasiVendor> getDataSegmentasiList() {
		return dataSegmentasiList;
	}
	public void setDataSegmentasiList(List<SegmentasiVendor> dataSegmentasiList) {
		this.dataSegmentasiList = dataSegmentasiList;
	}
	public List<VendorPIC> getPenanggungJawabList() {
		return penanggungJawabList;
	}
	public void setPenanggungJawabList(List<VendorPIC> penanggungJawabList) {
		this.penanggungJawabList = penanggungJawabList;
	}
	public List<DokumenRegistrasiVendor> getDataDokRegFormList() {
		return dataDokRegFormList;
	}
	public void setDataDokRegFormList(
			List<DokumenRegistrasiVendor> dataDokRegFormList) {
		this.dataDokRegFormList = dataDokRegFormList;
	}
	public List<DokumenRegistrasiVendor> getDataDokAkteList() {
		return dataDokAkteList;
	}
	public void setDataDokAkteList(List<DokumenRegistrasiVendor> dataDokAkteList) {
		this.dataDokAkteList = dataDokAkteList;
	}
	public List<DokumenRegistrasiVendor> getDataDokSalinanList() {
		return dataDokSalinanList;
	}
	public void setDataDokSalinanList(
			List<DokumenRegistrasiVendor> dataDokSalinanList) {
		this.dataDokSalinanList = dataDokSalinanList;
	}
	public List<DokumenRegistrasiVendor> getDataDokSiupList() {
		return dataDokSiupList;
	}
	public void setDataDokSiupList(List<DokumenRegistrasiVendor> dataDokSiupList) {
		this.dataDokSiupList = dataDokSiupList;
	}
	public List<DokumenRegistrasiVendor> getDataDokPKSList() {
		return dataDokPKSList;
	}
	public void setDataDokPKSList(List<DokumenRegistrasiVendor> dataDokPKSList) {
		this.dataDokPKSList = dataDokPKSList;
	}
	public List<DokumenRegistrasiVendor> getDataDokSPRList() {
		return dataDokSPRList;
	}
	public void setDataDokSPRList(List<DokumenRegistrasiVendor> dataDokSPRList) {
		this.dataDokSPRList = dataDokSPRList;
	}
	public List<DokumenRegistrasiVendor> getDataDokSPBList() {
		return dataDokSPBList;
	}
	public void setDataDokSPBList(List<DokumenRegistrasiVendor> dataDokSPBList) {
		this.dataDokSPBList = dataDokSPBList;
	}
	public List<DokumenRegistrasiVendor> getDataDokBuktiFisikList() {
		return dataDokBuktiFisikList;
	}
	public void setDataDokBuktiFisikList(
			List<DokumenRegistrasiVendor> dataDokBuktiFisikList) {
		this.dataDokBuktiFisikList = dataDokBuktiFisikList;
	}
	public List<DokumenRegistrasiVendor> getDataDokQualityList() {
		return dataDokQualityList;
	}
	public void setDataDokQualityList(
			List<DokumenRegistrasiVendor> dataDokQualityList) {
		this.dataDokQualityList = dataDokQualityList;
	}
	public List<DokumenRegistrasiVendor> getDataDokTeknikList() {
		return dataDokTeknikList;
	}
	public void setDataDokTeknikList(List<DokumenRegistrasiVendor> dataDokTeknikList) {
		this.dataDokTeknikList = dataDokTeknikList;
	}
	public List<VendorSKD> getDataDokSKDList() {
		return dataDokSKDList;
	}
	public void setDataDokSKDList(List<VendorSKD> dataDokSKDList) {
		this.dataDokSKDList = dataDokSKDList;
	}
	public List<PeralatanVendor> getPeralatanListVendor() {
		return peralatanListVendor;
	}
	public void setPeralatanListVendor(List<PeralatanVendor> peralatanListVendor) {
		this.peralatanListVendor = peralatanListVendor;
	}
	public List<KeuanganVendor> getKeuanganListVendor() {
		return keuanganListVendor;
	}
	public void setKeuanganListVendor(List<KeuanganVendor> keuanganListVendor) {
		this.keuanganListVendor = keuanganListVendor;
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
	public List<Negara> getNegaraList() {
		return negaraList;
	}
	public void setNegaraList(List<Negara> negaraList) {
		this.negaraList = negaraList;
	}
	public Negara getNegaraNpwp() {
		return negaraNpwp;
	}
	public void setNegaraNpwp(Negara negaraNpwp) {
		this.negaraNpwp = negaraNpwp;
	}
	public Negara getNegaraPerusahaan() {
		return negaraPerusahaan;
	}
	public void setNegaraPerusahaan(Negara negaraPerusahaan) {
		this.negaraPerusahaan = negaraPerusahaan;
	}
	public Wilayah getProvinsiNpwp() {
		return provinsiNpwp;
	}
	public void setProvinsiNpwp(Wilayah provinsiNpwp) {
		this.provinsiNpwp = provinsiNpwp;
	}
	public Wilayah getProvinsiPerusahaan() {
		return provinsiPerusahaan;
	}
	public void setProvinsiPerusahaan(Wilayah provinsiPerusahaan) {
		this.provinsiPerusahaan = provinsiPerusahaan;
	}
	public Wilayah getKotaNPWP() {
		return kotaNPWP;
	}
	public void setKotaNPWP(Wilayah kotaNPWP) {
		this.kotaNPWP = kotaNPWP;
	}
	public Wilayah getKotaPerusahaan() {
		return kotaPerusahaan;
	}
	public void setKotaPerusahaan(Wilayah kotaPerusahaan) {
		this.kotaPerusahaan = kotaPerusahaan;
	}
	public List<DokumenRegistrasiVendor> getDataDokSKBList() {
		return dataDokSKBList;
	}
	public void setDataDokSKBList(List<DokumenRegistrasiVendor> dataDokSKBList) {
		this.dataDokSKBList = dataDokSKBList;
	}
	public List<DokumenRegistrasiVendor> getDataHistorySKBList() {
		return dataHistorySKBList;
	}
	public void setDataHistorySKBList(
			List<DokumenRegistrasiVendor> dataHistorySKBList) {
		this.dataHistorySKBList = dataHistorySKBList;
	}
	
}
