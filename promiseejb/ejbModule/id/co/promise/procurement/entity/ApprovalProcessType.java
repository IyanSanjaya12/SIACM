/*
* File: ApprovalProcessType.java
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
* Aug 12, 2016 3:53:52 PM 	Mamat 		1.0 		Created
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
@Table(name = "T2_APPROV_PROCESS_TYPE")
@NamedQueries({
	@NamedQuery(name="ApprovalProcessType.find",
			query="SELECT au FROM ApprovalProcessType au WHERE au.isDelete = 0"),
	@NamedQuery(name="ApprovalProcessType.findByValueId",
			query="SELECT au FROM ApprovalProcessType au WHERE au.isDelete = 0 and au.valueId = :valueId"),
	@NamedQuery(name="ApprovalProcessType.findByTahapanAndValueId",
	query="SELECT au FROM ApprovalProcessType au WHERE au.isDelete = 0 and au.tahapan.nama=:tahapanName and au.valueId = :valueId"),
	@NamedQuery(name="ApprovalProcessType.findByTahapan",
	query="SELECT au FROM ApprovalProcessType au WHERE au.isDelete = 0 and au.tahapan.id=:tahapanId"),
	@NamedQuery(name="ApprovalProcessType.findApprovalForApprovalBookingOrder",
		query="SELECT apt FROM ApprovalProcessType apt WHERE apt.isDelete = 0 AND apt.tahapan.id = :tahapanId "
				+ "AND apt.valueId = :valueId order by apt.id DESC"),
	@NamedQuery(name="ApprovalProcessType.findByCatalogId",
	query="SELECT apt FROM ApprovalProcessType apt WHERE apt.isDelete = 0 AND apt.valueId =:catalogId AND tahapan.nama IN (:tahapanNama) ORDER BY apt.id DESC")
	
	})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_APPROV_PROCESS_TYPE", initialValue = 1, allocationSize = 1)
public class ApprovalProcessType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPROVAL_PROCESS_TYPE_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="TAHAPAN_ID", referencedColumnName="TAHAPAN_ID")
	private Tahapan tahapan;
	@Column(name="VALUE_ID")
	private Integer valueId;
	@ManyToOne
	@JoinColumn(name="APPROVAL_ID", referencedColumnName="APPROVAL_ID", nullable=true)
	private Approval approval;
	
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
	@Column(name = "STATUS")
	private Integer status;
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	/**
	 *  0 = SERIAL
	 *  1 = PARALEL
	 */
	@Column(name = "JENIS")
	private Integer jenis;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the tahapan
	 */
	public Tahapan getTahapan() {
		return tahapan;
	}
	/**
	 * @param tahapan the tahapan to set
	 */
	public void setTahapan(Tahapan tahapan) {
		this.tahapan = tahapan;
	}
	/**
	 * @return the valueId
	 */
	public Integer getValueId() {
		return valueId;
	}
	/**
	 * @param valueId the valueId to set
	 */
	public void setValueId(Integer valueId) {
		this.valueId = valueId;
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
	/**
	 * @return the approval
	 */
	public Approval getApproval() {
		return approval;
	}
	/**
	 * @param approval the approval to set
	 */
	public void setApproval(Approval approval) {
		this.approval = approval;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	
	
}
