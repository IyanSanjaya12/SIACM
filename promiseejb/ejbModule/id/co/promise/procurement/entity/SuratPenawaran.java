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
@NamedQueries({
    @NamedQuery(name="SuratPenawaran.find",
            query="SELECT sp FROM SuratPenawaran sp WHERE sp.isDelete = 0"),
    @NamedQuery(name="SuratPenawaran.findByVendorIdAndPengadaanId",
    		query="SELECT sp FROM SuratPenawaran sp WHERE sp.isDelete = 0 AND sp.vendor.id=:vendorId AND sp.pengadaan.id=:pengadaanId"),
    @NamedQuery(name="SuratPenawaran.findByPengadaanId",
    		query="SELECT sp FROM SuratPenawaran sp WHERE sp.isDelete = 0 AND sp.pengadaan.id=:pengadaanId"),
    @NamedQuery(name="SuratPenawaran.findByPengadaanIdNilaiJaminan",
    		query="SELECT sp FROM SuratPenawaran sp WHERE sp.isDelete = 0 AND sp.pengadaan.id=:pengadaanId ORDER BY sp.nilaiJaminan ASC")
})
@Table(name = "T5_SURAT_PENAWARAN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_SURAT_PENAWARAN", initialValue = 1, allocationSize = 1)
public class SuratPenawaran {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SURAT_PENAWARAN_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;
	
	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@OneToOne
	@JoinColumn (name = "MATA_UANG_ID", referencedColumnName="MATA_UANG_ID", nullable=true)
	private MataUang mataUang;
	
	@Column(name = "NOMOR", length = 100)
	private String nomor;
	
	@Column(name = "TANGGAL_AWAL")
	private Date tanggalAwal;
	
	@Column(name = "TANGGAL_BATAS")
	private Date tanggalBatas;
	
	@Column(name = "NAMA_JAMINAN", length = 250)
	private String namaJaminan;
	
	@ColumnDefault( value = "0" )
	@Column(name = "NILAI_JAMINAN")
	private Double nilaiJaminan;
	
	@Column(name = "TANGGAL_BATAS_JAMINAN")
	private Date tanggalBatasJaminan;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getNomor() {
		return nomor;
	}

	public void setNomor(String nomor) {
		this.nomor = nomor;
	}

	public Date getTanggalAwal() {
		return tanggalAwal;
	}

	public void setTanggalAwal(Date tanggalAwal) {
		this.tanggalAwal = tanggalAwal;
	}

	public Date getTanggalBatas() {
		return tanggalBatas;
	}

	public void setTanggalBatas(Date tanggalBatas) {
		this.tanggalBatas = tanggalBatas;
	}

	public String getNamaJaminan() {
		return namaJaminan;
	}

	public void setNamaJaminan(String namaJaminan) {
		this.namaJaminan = namaJaminan;
	}

	public Double getNilaiJaminan() {
		return nilaiJaminan;
	}

	public void setNilaiJaminan(Double nilaiJaminan) {
		this.nilaiJaminan = nilaiJaminan;
	}

	public Date getTanggalBatasJaminan() {
		return tanggalBatasJaminan;
	}

	public void setTanggalBatasJaminan(Date tanggalBatasJaminan) {
		this.tanggalBatasJaminan = tanggalBatasJaminan;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
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
}
