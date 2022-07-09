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
@Table(name = "T3_PENGALAMAN_VENDOR")
@NamedQueries({
	@NamedQuery(name="PengalamanVendor.findByVendor",
        	query="SELECT pengalamanVendor FROM PengalamanVendor pengalamanVendor WHERE pengalamanVendor.vendor.id =:vendorId AND pengalamanVendor.isDelete=0"),
	@NamedQuery(name="PengalamanVendor.findByVendorAndTipePengalaman",
			query="SELECT pengalamanVendor FROM PengalamanVendor pengalamanVendor WHERE pengalamanVendor.vendor.id =:vendorId AND pengalamanVendor.tipePengalaman =:tipePengalaman AND pengalamanVendor.isDelete=0"),
	@NamedQuery(name="PengalamanVendor.findNama", 
			query="select pengalamanVendor from PengalamanVendor pengalamanVendor where pengalamanVendor.isDelete=0 and pengalamanVendor.namaPekerjaan =:namaPekerjaan and pengalamanVendor.tipePengalaman =:tipePengalaman"),
	@NamedQuery(name="PengalamanVendor.deleteByVendorId",
  			query="UPDATE PengalamanVendor pengalamanVendor set pengalamanVendor.isDelete = 1 WHERE pengalamanVendor.vendor.id =:vendorId ")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_PENGALAMAN_VENDOR", initialValue = 1, allocationSize = 1)
public class PengalamanVendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENGALAMAN_VENDOR_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	// PELANGGAN, MITRA, dan INPROGRESS
	@Column(name = "TIPE_PENGALAMAN", length = 10)
	private String tipePengalaman;
	
	@Column(name = "NAMA_PEKERJAAN", length = 50)
	private String namaPekerjaan;
	
	@Column(name = "LOKASI_PEKERJAAN", length = 100)
	private String lokasiPekerjaan;
	
	@Column(name = "BIDANG_USAHA", length = 50)
	private String bidangUsaha;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MULAI_KERJASAMA")
	private Date mulaiKerjasama;
	
	@ColumnDefault( value = "0" )
	@Column(name = "NILAI_KONTRAK")
	private Double nilaiKontrak;

	@OneToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;

	@Column(name = "FILE_NAME", length = 128)
	private String fileName;

	@Column(name = "PATH_FILE", length = 128)
	private String pathFile;
	
	@Column(name = "FILE_SIZE")
	private Long fileSize;
	
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

	public String getTipePengalaman() {
		return tipePengalaman;
	}

	public void setTipePengalaman(String tipePengalaman) {
		this.tipePengalaman = tipePengalaman;
	}

	public String getNamaPekerjaan() {
		return namaPekerjaan;
	}

	public void setNamaPekerjaan(String namaPekerjaan) {
		this.namaPekerjaan = namaPekerjaan;
	}

	public String getLokasiPekerjaan() {
		return lokasiPekerjaan;
	}

	public void setLokasiPekerjaan(String lokasiPekerjaan) {
		this.lokasiPekerjaan = lokasiPekerjaan;
	}

	public String getBidangUsaha() {
		return bidangUsaha;
	}

	public void setBidangUsaha(String bidangUsaha) {
		this.bidangUsaha = bidangUsaha;
	}

	public Date getMulaiKerjasama() {
		return mulaiKerjasama;
	}

	public void setMulaiKerjasama(Date mulaiKerjasama) {
		this.mulaiKerjasama = mulaiKerjasama;
	}

	public Double getNilaiKontrak() {
		return nilaiKontrak;
	}

	public void setNilaiKontrak(Double nilaiKontrak) {
		this.nilaiKontrak = nilaiKontrak;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
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
