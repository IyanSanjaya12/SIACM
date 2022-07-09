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
@NamedQueries({
		@NamedQuery(name = "DokumenPengadaan.find", query = "SELECT dokumenPengadaan FROM DokumenPengadaan dokumenPengadaan WHERE dokumenPengadaan.isDelete = 0"),
		@NamedQuery(name = "DokumenPengadaan.findByPengadaanId", query = "SELECT dokumenPengadaan FROM DokumenPengadaan dokumenPengadaan WHERE dokumenPengadaan.pengadaan.id = :pengadaanId AND dokumenPengadaan.isDelete=0"),
		@NamedQuery(name = "DokumenPengadaan.deleteByPengadaanId", query = "DELETE FROM DokumenPengadaan dokumenPengadaan WHERE dokumenPengadaan.pengadaan.id = :pengadaanId AND dokumenPengadaan.isDelete=0")})
@Table(name = "T5_DOKUMEN_PENGADAAN")
public class DokumenPengadaan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DOKUMEN_PENGADAAN_ID")
	private Integer id;

	@Column(name = "NAMA", length = 100)
	private String nama;

	@OneToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@Column(name = "TGL_UPLOAD")
	private Date tanggalUpload;

	@Column(name = "PATH", length = 100)
	private String path;

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
	
	@Column(name = "FILE_SIZE")
	private Long fileSize;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	public Date getTanggalUpload() {
		return tanggalUpload;
	}

	public void setTanggalUpload(Date tanggalUpload) {
		this.tanggalUpload = tanggalUpload;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
