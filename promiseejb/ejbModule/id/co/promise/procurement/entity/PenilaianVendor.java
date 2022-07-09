package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_V_PERFORMANCE")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_V_PERFORMANCE", initialValue = 1, allocationSize = 1)
public class PenilaianVendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "V_PERF_ID")
	private Integer vPerfId;
	
	@Column(name = "TIPE") /** NOTE : 0 = KONTRAK MANUAL, 1 = KONTRAK PROMISE, 2. PENGADAAN MANUAL 3. PENGADAAN PROMISE **/
	private Integer vPerfTipe; 
	
	@Column(name = "NOMOR_KONTRAK")
	private String vPerfNomorKontrak;
	
	@Column(name = "TGL_KONTRAK")
	private Date vPerfTglKontrak;
	
	@Column(name = "NOMOR_PENGADAAN")
	private String vPerfNomorPengadaan;	

	@ManyToOne
	@JoinColumn(name = "KONTRAK_ID", referencedColumnName = "KONTRAK_ID")  
	private Kontrak kontrak;	

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")  
	private Pengadaan pengadaan;	

	@Lob
	@Column(name = "PAKET_PEKERJAAN")
	private String vPerfPaketPekerjaan;
	
	@Column(name = "PENGGUNA_HASIL_PEKERJAAN")
	private String vPerfPenggunaHasilPekerjaan;

	@ManyToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")  
	private MataUang mataUang;
	
	@Column(name = "NILAI_KONTRAK")
	private String vPerfNilaiKontrak;
	
	@Column(name = "KONTRAK_TGL_AWAL_KERJA")
	private Date vPerfKontrakTglAwalKerja;
	
	@Column(name = "KONTRAK_TGL_AKHIT_KERJA")
	private Date vPerfKontrakTglAkhirKerja;

	@ManyToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")  
	private Vendor vendor;
	
	@Column(name = "FILE_DOKUMEN_PENDUKUNG")
	private String vPerfFileDokumenPendukung;
	
	@ColumnDefault( value = "0" )
	@Column(name = "NILAI_AKHIR")
	private Double vPerfNilaiAkhir;
	
	@Column(name = "KESIMPULAN_PENILAIAN")
	private String vPerfKesimpulanPenilaian;
	
	@Column(name = "TGL_PENILAIAN")
	private Date vPerfTglPenilaian;

	@Column(name = "CATATAN_USER")
	private String vPerfCatatanUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date vPerfCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date vPerfUpdated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date vPerfDeleted;
	
	@Column(name="USERID")
	private Integer vPerfUserId;
	
	@ColumnDefault( value = "0" )
	@Column(name="ISDELETE")
	private Integer vPerfIsDelete;

	@ManyToOne
	@JoinColumn(name = "PURCHASE_ORDER_ID", referencedColumnName = "PURCHASE_ORDER_ID")  
	private PurchaseOrder purchaseOrder;

	public Integer getvPerfId() {
		return vPerfId;
	}

	public void setvPerfId(Integer vPerfId) {
		this.vPerfId = vPerfId;
	}

	public Integer getvPerfTipe() {
		return vPerfTipe;
	}

	public void setvPerfTipe(Integer vPerfTipe) {
		this.vPerfTipe = vPerfTipe;
	}

	public String getvPerfNomorKontrak() {
		return vPerfNomorKontrak;
	}

	public void setvPerfNomorKontrak(String vPerfNomorKontrak) {
		this.vPerfNomorKontrak = vPerfNomorKontrak;
	}

	public Date getvPerfTglKontrak() {
		return vPerfTglKontrak;
	}

	public void setvPerfTglKontrak(Date vPerfTglKontrak) {
		this.vPerfTglKontrak = vPerfTglKontrak;
	}

	public String getvPerfNomorPengadaan() {
		return vPerfNomorPengadaan;
	}

	public void setvPerfNomorPengadaan(String vPerfNomorPengadaan) {
		this.vPerfNomorPengadaan = vPerfNomorPengadaan;
	}

	public Kontrak getKontrak() {
		return kontrak;
	}

	public void setKontrak(Kontrak kontrak) {
		this.kontrak = kontrak;
	}

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	public String getvPerfPaketPekerjaan() {
		return vPerfPaketPekerjaan;
	}

	public void setvPerfPaketPekerjaan(String vPerfPaketPekerjaan) {
		this.vPerfPaketPekerjaan = vPerfPaketPekerjaan;
	}

	public String getvPerfPenggunaHasilPekerjaan() {
		return vPerfPenggunaHasilPekerjaan;
	}

	public void setvPerfPenggunaHasilPekerjaan(String vPerfPenggunaHasilPekerjaan) {
		this.vPerfPenggunaHasilPekerjaan = vPerfPenggunaHasilPekerjaan;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public String getvPerfNilaiKontrak() {
		return vPerfNilaiKontrak;
	}

	public void setvPerfNilaiKontrak(String vPerfNilaiKontrak) {
		this.vPerfNilaiKontrak = vPerfNilaiKontrak;
	}

	public Date getvPerfKontrakTglAwalKerja() {
		return vPerfKontrakTglAwalKerja;
	}

	public void setvPerfKontrakTglAwalKerja(Date vPerfKontrakTglAwalKerja) {
		this.vPerfKontrakTglAwalKerja = vPerfKontrakTglAwalKerja;
	}

	public Date getvPerfKontrakTglAkhirKerja() {
		return vPerfKontrakTglAkhirKerja;
	}

	public void setvPerfKontrakTglAkhirKerja(Date vPerfKontrakTglAkhirKerja) {
		this.vPerfKontrakTglAkhirKerja = vPerfKontrakTglAkhirKerja;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getvPerfFileDokumenPendukung() {
		return vPerfFileDokumenPendukung;
	}

	public void setvPerfFileDokumenPendukung(String vPerfFileDokumenPendukung) {
		this.vPerfFileDokumenPendukung = vPerfFileDokumenPendukung;
	}

	public Double getvPerfNilaiAkhir() {
		return vPerfNilaiAkhir;
	}

	public void setvPerfNilaiAkhir(Double vPerfNilaiAkhir) {
		this.vPerfNilaiAkhir = vPerfNilaiAkhir;
	}

	public String getvPerfKesimpulanPenilaian() {
		return vPerfKesimpulanPenilaian;
	}

	public void setvPerfKesimpulanPenilaian(String vPerfKesimpulanPenilaian) {
		this.vPerfKesimpulanPenilaian = vPerfKesimpulanPenilaian;
	}

	public Date getvPerfTglPenilaian() {
		return vPerfTglPenilaian;
	}

	public void setvPerfTglPenilaian(Date vPerfTglPenilaian) {
		this.vPerfTglPenilaian = vPerfTglPenilaian;
	}

	public String getvPerfCatatanUser() {
		return vPerfCatatanUser;
	}

	public void setvPerfCatatanUser(String vPerfCatatanUser) {
		this.vPerfCatatanUser = vPerfCatatanUser;
	}

	public Date getvPerfCreated() {
		return vPerfCreated;
	}

	public void setvPerfCreated(Date vPerfCreated) {
		this.vPerfCreated = vPerfCreated;
	}

	public Date getvPerfUpdated() {
		return vPerfUpdated;
	}

	public void setvPerfUpdated(Date vPerfUpdated) {
		this.vPerfUpdated = vPerfUpdated;
	}

	public Date getvPerfDeleted() {
		return vPerfDeleted;
	}

	public void setvPerfDeleted(Date vPerfDeleted) {
		this.vPerfDeleted = vPerfDeleted;
	}

	public Integer getvPerfUserId() {
		return vPerfUserId;
	}

	public void setvPerfUserId(Integer vPerfUserId) {
		this.vPerfUserId = vPerfUserId;
	}

	public Integer getvPerfIsDelete() {
		return vPerfIsDelete;
	}

	public void setvPerfIsDelete(Integer vPerfIsDelete) {
		this.vPerfIsDelete = vPerfIsDelete;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	
}
