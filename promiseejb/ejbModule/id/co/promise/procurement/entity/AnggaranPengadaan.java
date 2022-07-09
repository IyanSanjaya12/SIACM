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
	@NamedQuery(name = "AnggaranPengadaan.find", query = "SELECT x FROM AnggaranPengadaan x WHERE x.isDelete = 0"),
	@NamedQuery(name = "AnggaranPengadaan.findByAnggaran", query = "SELECT x FROM AnggaranPengadaan x WHERE x.isDelete = 0 and x.alokasiAnggaran.id=:anggaranId"),
	@NamedQuery(name = "AnggaranPengadaan.findByPengadaanId", query = "SELECT x FROM AnggaranPengadaan x WHERE x.isDelete = 0 AND x.pengadaan.id = :pengadaanId")
})
@Table(name = "T5_ANGGARAN_PENGADAAN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_ANGGARAN_PENGADAAN", initialValue = 1, allocationSize = 1)
public class AnggaranPengadaan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ANGGARAN_PENGADAAN_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@ManyToOne
	@JoinColumn(name = "ALOKASI_ANGGARAN_ID", referencedColumnName = "ALOKASI_ANGGARAN_ID")
	private AlokasiAnggaran alokasiAnggaran;

	@ColumnDefault( value = "0" )
	@Column(name = "JUMLAH")
	private Double jumlah;

	@ColumnDefault( value = "0" )
	@Column(name = "SISA_ANGGARAN")
	private Double sisaAnggaran;

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

	public AlokasiAnggaran getAlokasiAnggaran() {
		return alokasiAnggaran;
	}

	public void setAlokasiAnggaran(AlokasiAnggaran alokasiAnggaran) {
		this.alokasiAnggaran = alokasiAnggaran;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Double getSisaAnggaran() {
		return sisaAnggaran;
	}

	public void setSisaAnggaran(Double sisaAnggaran) {
		this.sisaAnggaran = sisaAnggaran;
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
