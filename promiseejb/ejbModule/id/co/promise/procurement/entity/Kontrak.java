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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@NamedQueries({
	@NamedQuery(name = "Kontrak.find", query = "SELECT k FROM Kontrak k WHERE k.isDelete = 0"),
	@NamedQuery(name = "Kontrak.findLastKontrak", query = "SELECT k FROM Kontrak k WHERE k.isDelete = 0 AND k.id = (SELECT MAX(k2.id) from Kontrak k2 where k2.pengadaan.id = k.pengadaan.id ) "),
	@NamedQuery(name = "Kontrak.findKontrakByPengadaanId", query = "SELECT k FROM Kontrak k WHERE k.isDelete = 0 AND k.pengadaan.id = :pengadaanId order by k.id desc"),
	@NamedQuery(name = "Kontrak.findKontrakByPanitiaId", 
		query = "SELECT k FROM Kontrak k WHERE k.isDelete = 0 AND k.pengadaan.panitia.id = :panitiaId order by k.id desc"),
	@NamedQuery(name = "Kontrak.findKontrakByVendorId", query = "SELECT k FROM Kontrak k WHERE k.isDelete = 0 AND k.vendor.id = :vendorId order by k.id desc")
})
@Table(name = "T6_KONTRAK")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T6_KONTRAK", initialValue = 1, allocationSize = 1)
public class Kontrak {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KONTRAK_ID")
	private Integer id;
	
	@Column(name="NOMOR")
	private String nomor;
	
	@Column(name="NOMOR_SURAT_PENAWARAN")
	private String nomorSuratPenawaran;
	
	@Column(name="TGL_SURAT_PENAWARAN")
	private Date tglSuratPenawaran;
	
	@Column(name="nomorKeputusanSuratPenawaran")
	private String nomorKeputusanSuratPenawaran;
	
	@Column(name="tglKeputusanSuratPenawaran")
	private Date tglKeputusanSuratPenawaran;
	
	@ColumnDefault( value = "0" )
	@Column(name="NILAI")
	private Double nilai;
	
	@OneToOne
	@JoinColumn(name="MATA_UANG_ID", referencedColumnName="MATA_UANG_ID")
	private MataUang mataUang;
	
	@Column(name="TGL_MULAI_KONTRAK")
	private Date tglMulaiKontrak;
	
	@Column(name="TGL_SELESAI_KONTRAK")
	private Date tglSelesaiKontrak;
	
	@ColumnDefault( value = "0" )
	@Column(name="NILAI_KURS_USD")
	private Double nilaiKursUSD;
	
	@Column(name="TGL_SPK")
	private Date tglSPK;
	
	@Column(name="TGL_BATAS_PENGIRIMAN")
	private Date tglBatasPengiriman;
	
	@Column(name="NAMA_JAMINAN_PELAKSANA")
	private String namaJaminanPelaksana;
	
	@ColumnDefault( value = "0" )
	@Column(name="NILAI_JAMINAN_PELAKSANA")
	private Double nilaiJaminanPelaksana;
	
	@Column(name="TGL_BATAS_JAMINAN")
	private Date tglBatasJaminan;
	
	@OneToOne
	@JoinColumn(name="PENGADAAN_ID", referencedColumnName="PENGADAAN_ID")
	private Pengadaan pengadaan;
	
	@OneToOne
	@JoinColumn(name="VENDOR_ID", referencedColumnName="VENDOR_ID")
	private Vendor vendor;
	
	@OneToOne
	@JoinColumn(name="ORGANISASI_ID", referencedColumnName="ORGANISASI_ID")
	private Organisasi organisasi;
	
	@Column(name="STATUS_KONTRAK")
	private String statusKontrak;
	
	@OneToOne
	@JoinColumn(name="USERID", referencedColumnName="USER_ID", insertable=false, updatable=false)
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;	
	
	@Column(name="USERID")
	private Integer userId;	
	
	@ColumnDefault( value = "0" )
	@Column(name="ISDELETE")
	private Integer isDelete;
	
	@ColumnDefault( value = "0" )
	@Column(name="PINALTY")
	private Double Pinalty;
	
	/** PinaltyType == 0 = Persen || PinaltyType == 1 = Total **/
	@Column(name="PINALTY_TYPE")
	private Integer PinaltyType;
	
	@Transient
	private Double nilaiPerforma;
	
	public Double getNilaiPerforma() {
		return nilaiPerforma;
	}
	public void setNilaiPerforma(Double nilaiPerforma) {
		this.nilaiPerforma = nilaiPerforma;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public String getNomorKeputusanSuratPenawaran() {
		return nomorKeputusanSuratPenawaran;
	}
	public void setNomorKeputusanSuratPenawaran(String nomorKeputusanSuratPenawaran) {
		this.nomorKeputusanSuratPenawaran = nomorKeputusanSuratPenawaran;
	}
	public Date getTglKeputusanSuratPenawaran() {
		return tglKeputusanSuratPenawaran;
	}
	public void setTglKeputusanSuratPenawaran(Date tglKeputusanSuratPenawaran) {
		this.tglKeputusanSuratPenawaran = tglKeputusanSuratPenawaran;
	}
	public String getNomorSuratPenawaran() {
		return nomorSuratPenawaran;
	}
	public void setNomorSuratPenawaran(String nomorSuratPenawaran) {
		this.nomorSuratPenawaran = nomorSuratPenawaran;
	}
	public Date getTglSuratPenawaran() {
		return tglSuratPenawaran;
	}
	public void setTglSuratPenawaran(Date tglSuratPenawaran) {
		this.tglSuratPenawaran = tglSuratPenawaran;
	}
	public Double getNilai() {
		return nilai;
	}
	public void setNilai(Double nilai) {
		this.nilai = nilai;
	}
	public MataUang getMataUang() {
		return mataUang;
	}
	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}
	public Date getTglMulaiKontrak() {
		return tglMulaiKontrak;
	}
	public void setTglMulaiKontrak(Date tglMulaiKontrak) {
		this.tglMulaiKontrak = tglMulaiKontrak;
	}
	public Date getTglSelesaiKontrak() {
		return tglSelesaiKontrak;
	}
	public void setTglSelesaiKontrak(Date tglSelesaiKontrak) {
		this.tglSelesaiKontrak = tglSelesaiKontrak;
	}
	public Double getNilaiKursUSD() {
		return nilaiKursUSD;
	}
	public void setNilaiKursUSD(Double nilaiKursUSD) {
		this.nilaiKursUSD = nilaiKursUSD;
	}
	public Date getTglSPK() {
		return tglSPK;
	}
	public void setTglSPK(Date tglSPK) {
		this.tglSPK = tglSPK;
	}
	public Date getTglBatasPengiriman() {
		return tglBatasPengiriman;
	}
	public void setTglBatasPengiriman(Date tglBatasPengiriman) {
		this.tglBatasPengiriman = tglBatasPengiriman;
	}
	public String getNamaJaminanPelaksana() {
		return namaJaminanPelaksana;
	}
	public void setNamaJaminanPelaksana(String namaJaminanPelaksana) {
		this.namaJaminanPelaksana = namaJaminanPelaksana;
	}
	public Double getNilaiJaminanPelaksana() {
		return nilaiJaminanPelaksana;
	}
	public void setNilaiJaminanPelaksana(Double nilaiJaminanPelaksana) {
		this.nilaiJaminanPelaksana = nilaiJaminanPelaksana;
	}
	public Date getTglBatasJaminan() {
		return tglBatasJaminan;
	}
	public void setTglBatasJaminan(Date tglBatasJaminan) {
		this.tglBatasJaminan = tglBatasJaminan;
	}
	public Pengadaan getPengadaan() {
		return pengadaan;
	}
	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public Organisasi getOrganisasi() {
		return organisasi;
	}
	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
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
	public String getStatusKontrak() {
		return statusKontrak;
	}
	public void setStatusKontrak(String statusKontrak) {
		this.statusKontrak = statusKontrak;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Double getPinalty() {
		return Pinalty;
	}
	public void setPinalty(Double pinalty) {
		Pinalty = pinalty;
	}
	public Integer getPinaltyType() {
		return PinaltyType;
	}
	public void setPinaltyType(Integer pinaltyType) {
		PinaltyType = pinaltyType;
	}
}
