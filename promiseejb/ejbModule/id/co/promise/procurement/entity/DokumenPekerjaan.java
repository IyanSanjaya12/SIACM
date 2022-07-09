package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T2_DOKUMEN_PEKERJAAN")
@NamedQueries({ 
	@NamedQuery(name= "DokumenPekerjaan.find", query = "SELECT dp FROM DokumenPekerjaan dp WHERE dp.isDelete = 0"),
	@NamedQuery(name= "DokumenPekerjaan.findByPerencanaan", query = "SELECT dp FROM DokumenPekerjaan dp WHERE dp.isDelete = 0 AND dp.perencanaan.id=:perencanaanId")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_DOKUMEN_PEKERJAAN", initialValue = 1, allocationSize = 1)
public class DokumenPekerjaan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DOKUMEN_PEKERJAAN_ID")
	private Integer id;

	@Column(name = "file_name", length = 128)
	private String fileName;

	@Column(name = "real_file_name", length = 128)
	private String realFileName;
	
	@Column(name = "file_size")
	private Long fileSize;

	@ManyToOne
	@JoinColumn(name = "PERENCANAAN_ID", referencedColumnName = "PERENCANAAN_ID")
	private Perencanaan perencanaan;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRealFileName() {
		return realFileName;
	}

	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Perencanaan getPerencanaan() {
		return perencanaan;
	}

	public void setPerencanaan(Perencanaan perencanaan) {
		this.perencanaan = perencanaan;
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
