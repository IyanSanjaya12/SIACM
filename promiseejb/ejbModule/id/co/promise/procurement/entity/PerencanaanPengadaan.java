/*
* File: PerencanaanPengadaan.java
* This class is created to handle all processing involved
* in a shopping cart.
*
* Copyright (c) 2016 Mitra Mandiri Informatika
* Jl. Tebet Raya no. 11 B Jakarta Selatan
* All Rights Reserved.
*
* This software is the confidential and proprietary
* information of Mitra Mandiri Informatika ("Confidential
* Information").
*
* You shall not disclose such Confidential Information and
* shall use it only in accordance with the terms of the
* license agreement you entered into with MMI.
*
* Date 				Author 			Version 	Changes
* Aug 30, 2016 6:00:35 PM 	Mamat 		1.0 		Created
* method
*/
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
 * @author Mamat
 *
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name = "PerencanaanPengadaan.find", query = "SELECT x FROM PerencanaanPengadaan x WHERE x.isDelete = 0"),
	@NamedQuery(name = "PerencanaanPengadaan.findByPerencanaanId", query = "SELECT x FROM PerencanaanPengadaan x WHERE x.isDelete = 0 and x.perencanaan.id=:perencanaanId"),
	@NamedQuery(name = "PerencanaanPengadaan.findByPengadaanId", query = "SELECT x FROM PerencanaanPengadaan x WHERE x.isDelete = 0 AND x.pengadaan.id = :pengadaanId")
})
@Table(name = "T5_PERENCANAAN_P")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PERENCANAAN_P", initialValue = 1, allocationSize = 1)
public class PerencanaanPengadaan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PERENCANAAN_P_ID")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID")
	private Pengadaan pengadaan;
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

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the pengadaan
	 */
	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	/**
	 * @param pengadaan the pengadaan to set
	 */
	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	/**
	 * @return the perencanaan
	 */
	public Perencanaan getPerencanaan() {
		return perencanaan;
	}

	/**
	 * @param perencanaan the perencanaan to set
	 */
	public void setPerencanaan(Perencanaan perencanaan) {
		this.perencanaan = perencanaan;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the deleted
	 */
	public Date getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the isDelete
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * @param isDelete the isDelete to set
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
