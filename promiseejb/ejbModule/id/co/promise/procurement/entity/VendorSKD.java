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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_VENDOR_SKD")
@NamedQueries({
	@NamedQuery(name="VendorSKD.findAll",
        	query="SELECT vendorSKD FROM VendorSKD vendorSKD WHERE vendorSKD.isDelete=0"),
	@NamedQuery(name="VendorSKD.findByVendor",
			query="SELECT vendorSKD FROM VendorSKD vendorSKD WHERE vendorSKD.vendor.id =:vendorId AND vendorSKD.isDelete=0"),
	@NamedQuery(name ="VendorSKD.deleteVendorSKD", 
			query ="UPDATE VendorSKD vendorSKD set vendorSKD.isDelete = 1 WHERE vendorSKD.vendor.id =:vendorId ")

})
public class VendorSKD {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_SKD_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "ALAMAT", length = 200)
	private String alamat;
	
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

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@ColumnDefault( value = "0" )
	@Column(name = "STATUS_CEKLIST")
	private Integer statusCeklist;

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

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
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

}
