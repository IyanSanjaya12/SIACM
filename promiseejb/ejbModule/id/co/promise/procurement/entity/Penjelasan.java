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
/**
 * 
 * Project Name : promiseejb
 * Package Name : id.co.promise.procurement.entity
 * File Name    : Penjelasan.java
 * Description  : 
 * @author      : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com
 * Since        : Sep 12, 2015
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Penjelasan.getList", query = "SELECT sp FROM Penjelasan sp WHERE sp.isDelete = 0"), @NamedQuery(name = "Penjelasan.getListByPengadaan", query = "SELECT sp FROM Penjelasan sp WHERE sp.isDelete = 0 AND sp.pengadaan.id=:pengadaanId") })
@Table(name = "T5_PENJELASAN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PENJELASAN", initialValue = 1, allocationSize = 1)
public class Penjelasan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PENJELASAN_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;

	@Column(name = "IS_ONLINE")
	private Integer isOnline;

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

	public Integer getIsOnline() {

		return isOnline;
	}

	public void setIsOnline(Integer isOnline) {

		this.isOnline = isOnline;
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
