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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T5_PENGUMUMAN_K")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PENGUMUMAN_K", initialValue = 1, allocationSize = 1)
public class PengumumanKualifikasi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENGUMUMAN_KUALIFIKASI_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@Column(name = "TGL_MULAI")
	private Date tglMulai;

	@Column(name = "TGL_SELESAI")
	private Date tglSelesai;

	@Column(name = "TIPE")
	private Integer tipe;

	@Column(name = "JUDUL_PENGUMUMAN", length = 512)
	private String judulPengumuman;

	@Lob
	@Column(name = "ISI_PENGUMUMAN")
	private String isiPengumuman;

	@Transient
	private String snipet;

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

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	public Date getTglMulai() {
		return tglMulai;
	}

	public void setTglMulai(Date tglMulai) {
		this.tglMulai = tglMulai;
	}

	public Date getTglSelesai() {
		return tglSelesai;
	}

	public void setTglSelesai(Date tglSelesai) {
		this.tglSelesai = tglSelesai;
	}

	public Integer getTipe() {
		return tipe;
	}

	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}

	public String getJudulPengumuman() {
		return judulPengumuman;
	}

	public void setJudulPengumuman(String judulPengumuman) {
		this.judulPengumuman = judulPengumuman;
	}

	public String getIsiPengumuman() {
		return isiPengumuman;
	}

	public void setIsiPengumuman(String isiPengumuman) {
		this.isiPengumuman = isiPengumuman;
	}

	public String getSnipet() {
		return snipet;
	}

	public void setSnipet(String snipet) {
		this.snipet = snipet;
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
