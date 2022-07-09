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
@NamedQueries({ 
	@NamedQuery(name = "KontrakDokumen.findKontrakDokumenByKontrakId", query = "SELECT kd FROM KontrakDokumen kd WHERE kd.isDelete = 0 and kd.kontrak.id= :kontrakId")
})
@Table(name = "T6_KONTRAK_DOKUMEN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T6_KONTRAK_DOKUMEN", initialValue = 1, allocationSize = 1)
public class KontrakDokumen {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "KONTRAK_DOKUMEN_ID")
	private Integer id;

	@Column(name = "FILE_NAME", length = 128)
	private String fileName;

	@Column(name = "PATH_FILE", length = 128)
	private String pathFile;
	
	@Column(name = "FILE_SIZE")
	private Long fileSize;

	@ManyToOne
	@JoinColumn(name = "KONTRAK_ID", referencedColumnName = "KONTRAK_ID")
	private Kontrak kontrak;

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

	public Kontrak getKontrak() {
		return kontrak;
	}

	public void setKontrak(Kontrak kontrak) {
		this.kontrak = kontrak;
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