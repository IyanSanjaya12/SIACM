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
 * File Name    : PanitiaPenjelasan.java
 * Description  : 
 * @author      : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com
 * Since        : Sep 12, 2015
 *
 */

@Entity
@Table(name = "T6_PANITIA_PENJELASAN")
@NamedQueries({
	@NamedQuery(name = "PanitiaPenjelasan.getList", query = "SELECT a FROM PanitiaPenjelasan a WHERE a.isDelete = 0"),
	@NamedQuery(name = "PanitiaPenjelasan.getListByPengadaan", query = "SELECT a FROM PanitiaPenjelasan a WHERE a.isDelete =0 AND a.penjelasan.pengadaan.id = :pengadaanId") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T6_PANITIA_PENJELASAN", initialValue = 1, allocationSize = 1)
public class PanitiaPenjelasan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PANITIA_PENJELASAN_ID")
	private Integer id;

	@Column(name = "NAMA", length = 128)
	private String nama;

	@ManyToOne
	@JoinColumn(name = "PENJELASAN_ID", referencedColumnName = "PENJELASAN_ID")
	private Penjelasan penjelasan;

	@ManyToOne
	@JoinColumn(name = "JABATAN_ID", referencedColumnName = "JABATAN_ID")
	private Jabatan jabatan;

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

	public Penjelasan getPenjelasan() {

		return penjelasan;
	}

	public void setPenjelasan(Penjelasan penjelasan) {

		this.penjelasan = penjelasan;
	}

	public Jabatan getJabatan() {

		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {

		this.jabatan = jabatan;
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
