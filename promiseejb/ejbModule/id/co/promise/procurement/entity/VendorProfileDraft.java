package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_V_PROFILE_DRAFT")
@NamedQueries({
	@NamedQuery(name="VendorProfileDraft.findByVendor",
        	query="SELECT vendorProfileDraft FROM VendorProfileDraft vendorProfileDraft WHERE vendorProfileDraft.vendor.id=:vendorId "
        			+ " AND vendorProfileDraft.isDelete = 0 "),
    @NamedQuery(name="VendorProfileDraft.findByNoPKP",
        	query="SELECT vendorProfileDraft FROM VendorProfileDraft vendorProfileDraft WHERE vendorProfileDraft.nomorPKP=:nomorPKP AND vendorProfileDraft.isDelete = 0"),
    @NamedQuery(name="VendorProfileDraft.findByNoNPWP",
			query="SELECT vendorProfileDraft FROM VendorProfileDraft vendorProfileDraft WHERE vendorProfileDraft.npwpPerusahaan=:npwpPerusahaan AND vendorProfileDraft.isDelete = 0")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_V_PROFILE_DRAFT", initialValue = 1, allocationSize = 1)
public class VendorProfileDraft {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_PROFILE_DRAFT_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@OneToOne
	@JoinColumn (name = "VENDOR_PROFILE_ID", referencedColumnName = "VENDOR_PROFILE_ID")
	private VendorProfile vendorProfile;
	
	// PKP = 1 atau non PKP = 2
	@ColumnDefault( value = "1" )
	@Column(name = "JENIS_PAJAK_PERUSAHAAN")
	private Integer jenisPajakPerusahaan;
	
	@Column(name = "NOMOR_PKP")
	private String nomorPKP;
	
	@OneToOne
	@JoinColumn(name = "KUALIFIKASI_VENDOR_ID", referencedColumnName = "KUALIFIKASI_VENDOR_ID")
	private KualifikasiVendor kualifikasiVendor;
	
	@Column(name = "UNIT_TERDAFTAR")
	private String unitTerdaftar;
	
	@Column(name = "TIPE_PERUSAHAAN")
	private String tipePerusahaan;
	
	@Column(name = "NAMA_PERUSAHAAN")
	private String namaPerusahaan;
	
	@Column(name = "NPWP_PERUSAHAAN")
	private String npwpPerusahaan;
	
	@Column(name = "NAMA_SINGKATAN")
	private String namaSingkatan;
	
	@Column(name = "ALAMAT")
	private String alamat;
	
	@Column(name = "KOTA")
	private String kota;
	
	@Column(name = "KODE_POS")
	private String kodePos;
	
	@Column(name = "PROVINSI")
	private String provinsi;
	
	@Column(name = "TELEPHONE")
	private String telephone;
	
	@Column(name = "FAKSIMILE")
	private String faksimile;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "WEBSITE")
	private String website;
	
	@Column(name = "NAMA_CONTACT_PERSON")
	private String namaContactPerson;
	
	@Column(name = "HP_CONTACT_PERSON")
	private String hpContactPerson;
	
	@Column(name = "EMAIL_CONTACT_PERSON")
	private String emailContactPerson;
	
	@Column(name = "KTP_CONTACT_PERSON")
	private String ktpContactPerson;
	
	@Column(name = "PO_BOX")
	private String poBox;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Column(name = "EMPLOYEES")
	private String jumlahKaryawan;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TGL_BERDIRI")
	private Date tanggalBerdiri;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "BUSSINESS_AREA")
	private Integer bussinessArea;
	
	@Column(name = "NAMA_NPWP")
	private String namaNPWP;
	
	@Column(name = "ALAMAT_NPWP")
	private String alamatNPWP;
	
	@Column(name = "PROVINSI_NPWP")
	private String provinsiNPWP;
	
	@Column(name = "KOTA_NPWP")
	private String kotaNPWP;
	
	@Column(name = "NO_AKTA_PENDIRIAN")
	private String noAktaPendirian;
	
	@Column(name = "NO_KK")
	private String noKK;
		
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getProvinsiNPWP() {
		return provinsiNPWP;
	}

	public void setProvinsiNPWP(String provinsiNPWP) {
		this.provinsiNPWP = provinsiNPWP;
	}

	public Integer getBussinessArea() {
		return bussinessArea;
	}

	public void setBussinessArea(Integer bussinessArea) {
		this.bussinessArea = bussinessArea;
	}

	public String getNamaNPWP() {
		return namaNPWP;
	}

	public void setNamaNPWP(String namaNPWP) {
		this.namaNPWP = namaNPWP;
	}

	public String getAlamatNPWP() {
		return alamatNPWP;
	}

	public void setAlamatNPWP(String alamatNPWP) {
		this.alamatNPWP = alamatNPWP;
	}

	public String getKotaNPWP() {
		return kotaNPWP;
	}

	public void setKotaNPWP(String kotaNPWP) {
		this.kotaNPWP = kotaNPWP;
	}

	public String getNoAktaPendirian() {
		return noAktaPendirian;
	}

	public void setNoAktaPendirian(String noAktaPendirian) {
		this.noAktaPendirian = noAktaPendirian;
	}

	public String getNoKK() {
		return noKK;
	}

	public void setNoKK(String noKK) {
		this.noKK = noKK;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Integer getJenisPajakPerusahaan() {
		return jenisPajakPerusahaan;
	}

	public void setJenisPajakPerusahaan(Integer jenisPajakPerusahaan) {
		this.jenisPajakPerusahaan = jenisPajakPerusahaan;
	}

	public String getNomorPKP() {
		return nomorPKP;
	}

	public void setNomorPKP(String nomorPKP) {
		this.nomorPKP = nomorPKP;
	}

	public KualifikasiVendor getKualifikasiVendor() {
		return kualifikasiVendor;
	}

	public void setKualifikasiVendor(KualifikasiVendor kualifikasiVendor) {
		this.kualifikasiVendor = kualifikasiVendor;
	}

	public String getUnitTerdaftar() {
		return unitTerdaftar;
	}

	public void setUnitTerdaftar(String unitTerdaftar) {
		this.unitTerdaftar = unitTerdaftar;
	}

	public String getTipePerusahaan() {
		return tipePerusahaan;
	}

	public void setTipePerusahaan(String tipePerusahaan) {
		this.tipePerusahaan = tipePerusahaan;
	}

	public String getNamaSingkatan() {
		return namaSingkatan;
	}

	public void setNamaSingkatan(String namaSingkatan) {
		this.namaSingkatan = namaSingkatan;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public String getKodePos() {
		return kodePos;
	}

	public void setKodePos(String kodePos) {
		this.kodePos = kodePos;
	}

	public String getProvinsi() {
		return provinsi;
	}

	public void setProvinsi(String provinsi) {
		this.provinsi = provinsi;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFaksimile() {
		return faksimile;
	}

	public void setFaksimile(String faksimile) {
		this.faksimile = faksimile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getNamaContactPerson() {
		return namaContactPerson;
	}

	public void setNamaContactPerson(String namaContactPerson) {
		this.namaContactPerson = namaContactPerson;
	}

	public String getHpContactPerson() {
		return hpContactPerson;
	}

	public void setHpContactPerson(String hpContactPerson) {
		this.hpContactPerson = hpContactPerson;
	}

	public String getEmailContactPerson() {
		return emailContactPerson;
	}

	public void setEmailContactPerson(String emailContactPerson) {
		this.emailContactPerson = emailContactPerson;
	}

	public String getKtpContactPerson() {
		return ktpContactPerson;
	}

	public void setKtpContactPerson(String ktpContactPerson) {
		this.ktpContactPerson = ktpContactPerson;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getNamaPerusahaan() {
		return namaPerusahaan;
	}

	public void setNamaPerusahaan(String namaPerusahaan) {
		this.namaPerusahaan = namaPerusahaan;
	}

	public String getNpwpPerusahaan() {
		return npwpPerusahaan;
	}

	public void setNpwpPerusahaan(String npwpPerusahaan) {
		this.npwpPerusahaan = npwpPerusahaan;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getJumlahKaryawan() {
		return jumlahKaryawan;
	}

	public void setJumlahKaryawan(String jumlahKaryawan) {
		this.jumlahKaryawan = jumlahKaryawan;
	}

	public Date getTanggalBerdiri() {
		return tanggalBerdiri;
	}

	public void setTanggalBerdiri(Date tanggalBerdiri) {
		this.tanggalBerdiri = tanggalBerdiri;
	}

	public VendorProfile getVendorProfile() {
		return vendorProfile;
	}

	public void setVendorProfile(VendorProfile vendorProfile) {
		this.vendorProfile = vendorProfile;
	}
	
}
