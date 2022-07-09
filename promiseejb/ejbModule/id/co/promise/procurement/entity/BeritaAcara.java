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

/**
 * 
 * Project Name : promiseejb
 * Package Name : id.co.promise.procurement.entity
 * File Name    : BeritaAcara.java
 * Description  : 
 * @author      : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com
 * Since        : Sep 12, 2015
 *
 */
@Entity
@Table(name = "T5_BERITA_ACARA")
@NamedQueries({ @NamedQuery(name = "BeritaAcara.getList", query = "SELECT ba FROM BeritaAcara ba WHERE ba.isDelete = 0"),
		@NamedQuery(name = "BeritaAcara.getListByPengadaanAndTahapan", query = "SELECT ba FROM BeritaAcara ba WHERE ba.isDelete = 0 and ba.pengadaan.id =:pengadaanId and ba.tahapan.id=:tahapanId ") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_BERITA_ACARA", initialValue = 1, allocationSize = 1)
public class BeritaAcara {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BERITA_ACARA_ID")
	private Integer id;

	@Column(name = "NOMOR")
	private String nomor;

	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@ManyToOne
	@JoinColumn(name = "TAHAPAN_ID", referencedColumnName = "TAHAPAN_ID")
	private Tahapan tahapan;

	@Column(name = "TANGGAL")
	private Date tanggal;

	@Column(name = "KETERANGAN")
	private String keterangan;

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

	public String getNomor() {

		return nomor;
	}

	public void setNomor(String nomor) {

		this.nomor = nomor;
	}

	public Pengadaan getPengadaan() {

		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {

		this.pengadaan = pengadaan;
	}

	public Tahapan getTahapan() {

		return tahapan;
	}

	public void setTahapan(Tahapan tahapan) {

		this.tahapan = tahapan;
	}

	public Date getTanggal() {

		return tanggal;
	}

	public void setTanggal(Date tanggal) {

		this.tanggal = tanggal;
	}

	public String getKeterangan() {

		return keterangan;
	}

	public void setKeterangan(String keterangan) {

		this.keterangan = keterangan;
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
