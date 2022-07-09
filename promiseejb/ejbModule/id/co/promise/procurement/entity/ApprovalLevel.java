/*
 * File: ApprovalLevel.java
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
 * Aug 12, 2016 3:47:28 PM 	Mamat 		1.0 		Created
 * method
 */
package id.co.promise.procurement.entity;

import java.io.Serializable;
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
@Table(name = "T3_APPROVAL_LEVEL")
@NamedQueries({
		@NamedQuery(name = "ApprovalLevel.find", query = "SELECT au FROM ApprovalLevel au WHERE au.isDelete = 0"),
		@NamedQuery(name = "ApprovalLevel.findByApproval", query = "SELECT a FROM ApprovalLevel a WHERE a.isDelete = 0 and a.approval = :approval ORDER BY a.sequence")
,@NamedQuery(name = "Approval.getMasterApprovalList", query = "SELECT new id.co.promise.procurement.approval.MasterApprovalDTO(a, b, c) "
		+ " FROM Approval a, ApprovalType b, ApprovalLevel c "
		+ " WHERE "
		+ " a.approvalType = b AND "
		+ " c.approval = a AND " + " a.isDelete = 0 ")		
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_APPROVAL_LEVEL", initialValue = 1, allocationSize = 1)
public class ApprovalLevel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPROVAL_LEVEL_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "APPROVAL_ID", referencedColumnName = "APPROVAL_ID")
	private Approval approval;
	
	@ManyToOne
	@JoinColumn(name = "JABATAN_ID", referencedColumnName = "JABATAN_ID")
	private Jabatan jabatan;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi group;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID", nullable = true)
	private Role role;
	
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	@ColumnDefault( value = "0" )
	@Column(name = "THRESHOLD")
	private Double threshold;

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

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the approval
	 */
	public Approval getApproval() {
		return approval;
	}

	/**
	 * @param approval
	 *            the approval to set
	 */
	public void setApproval(Approval approval) {
		this.approval = approval;
	}

	/**
	 * @return the group
	 */
	public Organisasi getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(Organisasi group) {
		this.group = group;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the threshold
	 */
	public Double getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold
	 *            the threshold to set
	 */
	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
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
	 * @param updated
	 *            the updated to set
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
	 * @param deleted
	 *            the deleted to set
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
	 * @param userId
	 *            the userId to set
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
	 * @param isDelete
	 *            the isDelete to set
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
