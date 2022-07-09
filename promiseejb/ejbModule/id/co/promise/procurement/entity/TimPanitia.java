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
	@NamedQuery(name = "TimPanitia.find", 
		query = "SELECT x FROM TimPanitia x WHERE x.isDelete = 0"),
	@NamedQuery(name = "TimPanitia.findByPanitia",
		query = "SELECT x FROM TimPanitia x WHERE x.isDelete = 0 AND x.panitia.id =:panitiaId"),
	@NamedQuery(name = "TimPanitia.findByNama",
		query = "SELECT timPanitia FROM TimPanitia timPanitia WHERE timPanitia.isDelete = 0 AND timPanitia.nama =:nama")
})
@Table(name = "T1_TIM_PANITIA")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_TIM_PANITIA", initialValue = 1, allocationSize = 1)
public class TimPanitia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIM_PANITIA_ID")
	private Integer id;
	
	@Column(name = "NAMA", length = 200)
	private String nama;
	
	@Column(name = "NOMOR_SK_DIREKSI", length = 200)
	private String nomorSk;
	
	@Column(name = "NAMA_KEPUTUSAN", length = 200)
	private String namaKeputusan;
	
	@Column(name = "PENANGGUNG_JAWAB", length = 200)
	private String penanggungJawab;
	
	@Column(name = "TGL_SK_DIREKSI")
	private Date tanggalSk;
	
	@OneToOne
	@JoinColumn(name = "PANITIA_ID", referencedColumnName = "PANITIA_ID")
	private Panitia panitia;
	
	@OneToOne
	@JoinColumn(name = "PEMBUAT_KEPUTUSAN_ID", referencedColumnName = "PEMBUAT_KEPUTUSAN_ID")
	private PembuatKeputusan pembuatKeputusan;
	
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

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNomorSk() {
		return nomorSk;
	}

	public void setNomorSk(String nomorSk) {
		this.nomorSk = nomorSk;
	}

	public String getNamaKeputusan() {
		return namaKeputusan;
	}

	public void setNamaKeputusan(String namaKeputusan) {
		this.namaKeputusan = namaKeputusan;
	}

	public String getPenanggungJawab() {
		return penanggungJawab;
	}

	public void setPenanggungJawab(String penanggungJawab) {
		this.penanggungJawab = penanggungJawab;
	}

	public Date getTanggalSk() {
		return tanggalSk;
	}

	public void setTanggalSk(Date tanggalSk) {
		this.tanggalSk = tanggalSk;
	}

	public Panitia getPanitia() {
		return panitia;
	}

	public void setPanitia(Panitia panitia) {
		this.panitia = panitia;
	}

	public PembuatKeputusan getPembuatKeputusan() {
		return pembuatKeputusan;
	}

	public void setPembuatKeputusan(PembuatKeputusan pembuatKeputusan) {
		this.pembuatKeputusan = pembuatKeputusan;
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
