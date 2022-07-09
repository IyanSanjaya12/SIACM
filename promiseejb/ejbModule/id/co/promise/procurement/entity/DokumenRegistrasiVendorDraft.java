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
@Table(name = " T3_DOK_REG_V_DRAFT")
@NamedQueries({
	@NamedQuery(name="DokumenRegistrasiVendorDraft.findAll",
        	query="SELECT dokumenRegistrasiVendorDraft FROM DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft WHERE dokumenRegistrasiVendorDraft.isDelete=0"),
	@NamedQuery(name="DokumenRegistrasiVendorDraft.findByVendor",
        	query="SELECT dokumenRegistrasiVendorDraft FROM DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft WHERE dokumenRegistrasiVendorDraft.vendor.id =:vendorId AND dokumenRegistrasiVendorDraft.isDelete=0"),
    @NamedQuery(name="DokumenRegistrasiVendorDraft.deleteByVendor",
        	query="delete FROM DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft WHERE dokumenRegistrasiVendorDraft.vendor.id =:vendorId"),
    @NamedQuery(name="DokumenRegistrasiVendorDraft.findByVendorAndSubject",
			query="SELECT dokumenRegistrasiVendorDraft FROM DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft WHERE dokumenRegistrasiVendorDraft.vendor.id =:vendorId AND dokumenRegistrasiVendorDraft.subject =:subject AND dokumenRegistrasiVendorDraft.isDelete=0"),
	@NamedQuery(name="DokumenRegistrasiVendorDraft.findByVendorAndDokumenVendor",
			query="SELECT dokumenRegistrasiVendorDraft FROM DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft WHERE dokumenRegistrasiVendorDraft.vendor.id =:vendorId AND dokumenRegistrasiVendorDraft.dokumenRegistrasiVendor.id=:dokumenRegistrasiVendorId AND dokumenRegistrasiVendorDraft.isDelete = 0"),
	@NamedQuery(name ="DokumenRegistrasiVendorDraft.deleteDokumenRegistrasiVendorDraft", 
    		query ="UPDATE DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft set dokumenRegistrasiVendorDraft.isDelete = 1 WHERE dokumenRegistrasiVendorDraft.vendor.id =:vendorId ")		
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_DOK_REG_V_DRAFT", initialValue = 1, allocationSize = 1)
public class DokumenRegistrasiVendorDraft {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DOK_REGISTRASI_VENDOR_DRAFT_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "DOK_REGISTRASI_VENDOR_ID", referencedColumnName = "DOK_REGISTRASI_VENDOR_ID")
	private DokumenRegistrasiVendor dokumenRegistrasiVendor;
	
	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "SUBJECT", length = 100)
	private String subject;
	
	@Column(name = "NAMA_DOKUMEN", length = 100)
	private String namaDokumen;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TANGGAL_TERBIT")
	private Date tanggalTerbit;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TANGGAL_BERAKHIR")
	private Date tanggalBerakhir;

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

	@Column(name = "STATUS_CEKLIST")
	private Integer statusCeklist;
	
	@Column(name = "IS_DUPLICATE")
	private Integer isDuplicate;
	/*
	 *jika status 0 berarti data tidak di duplicate dari table Dokumen Registrasi Vendor 
	 *jika status 1 berarti saat edit data vendor, vendor tidak mengganti data dokumen jadi harus di duplicate agar saat di approval muncul data dokumennya
	 */

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getNamaDokumen() {
		return namaDokumen;
	}

	public void setNamaDokumen(String namaDokumen) {
		this.namaDokumen = namaDokumen;
	}

	public Date getTanggalTerbit() {
		return tanggalTerbit;
	}

	public void setTanggalTerbit(Date tanggalTerbit) {
		this.tanggalTerbit = tanggalTerbit;
	}

	public Date getTanggalBerakhir() {
		return tanggalBerakhir;
	}

	public void setTanggalBerakhir(Date tanggalBerakhir) {
		this.tanggalBerakhir = tanggalBerakhir;
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

	public Integer getStatusCeklist() {
		return statusCeklist;
	}

	public void setStatusCeklist(Integer statusCeklist) {
		this.statusCeklist = statusCeklist;
	}

	public DokumenRegistrasiVendor getDokumenRegistrasiVendor() {
		return dokumenRegistrasiVendor;
	}

	public void setDokumenRegistrasiVendor(
			DokumenRegistrasiVendor dokumenRegistrasiVendor) {
		this.dokumenRegistrasiVendor = dokumenRegistrasiVendor;
	}

	public Integer getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(Integer isDuplicate) {
		this.isDuplicate = isDuplicate;
	}
	
	
}
