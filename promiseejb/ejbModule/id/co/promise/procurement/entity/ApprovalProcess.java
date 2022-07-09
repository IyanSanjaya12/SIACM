/*
 * File: ApprovalProcess.java
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
 * Aug 12, 2016 3:56:17 PM 	Mamat 		1.0 		Created
 * method
 */
package id.co.promise.procurement.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

import com.google.gson.annotations.Expose;

/**
 * @author Mamat
 *
 */

@Entity
@Table(name = "T2_APPROVAL_PROCESS")
@NamedQueries({ @NamedQuery(name = "ApprovalProcess.find", query = "SELECT au FROM ApprovalProcess au WHERE au.isDelete = 0"),
	@NamedQuery(name="ApprovalProcess.findByApprovalProcessType", query="SELECT a FROM ApprovalProcess a, ApprovalProcessType b WHERE a.isDelete =0 "
			+ "AND a.approvalProcessType = b AND a.approvalProcessType.id = :approvalProcessTypeId ORDER BY a.sequence"),
	@NamedQuery(name="ApprovalProcess.findByApprovalProcessTypeAndSequence", query="SELECT a FROM ApprovalProcess a, ApprovalProcessType b WHERE a.isDelete =0 "
			+ "AND a.approvalProcessType = b AND a.sequence = :sequence AND a.approvalProcessType = :approvalProcessType"),
	@NamedQuery(name="ApprovalProcess.findByUserGroupAndActive", query="SELECT a FROM ApprovalProcess a WHERE a.isDelete=0 AND a.status IN (1, 4) "
			+ " AND ( a.group.id IN (:organisasiId) OR a.user.id=:userId ) order by a.created desc"), 	
	@NamedQuery(name="ApprovalProcess.findByApprovalProcessTypeStatus", query="SELECT a FROM ApprovalProcess a, ApprovalProcessType b WHERE a.isDelete =0 "
			+ "AND (a.status = 3 OR a.status = 4) AND a.approvalProcessType = b AND a.approvalProcessType.id = :approvalProcessTypeId ORDER BY a.sequence"),//1 Approva/Aktif ; 4. Hold/Pending
	@NamedQuery(name="ApprovalProcess.findByApprovalProcessTypeAndStatus", query="SELECT a FROM ApprovalProcess a, ApprovalProcessType b WHERE a.isDelete =0 "
			+ "AND a.status =:status AND a.approvalProcessType = b AND a.approvalProcessType.id = :approvalProcessTypeId ORDER BY a.sequence"),
	@NamedQuery(name="ApprovalProcess.findByCatlog",
	query="SELECT approvalProcess FROM ApprovalProcess approvalProcess WHERE approvalProcess.isDelete =0 AND "
			+ "approvalProcess.approvalProcessType.valueId = :idCatalog order by approvalProcess.id DESC"),
	//KAI - 20201125 - #20503
	@NamedQuery(name="ApprovalProcess.findApprovalLogUserList",
	query="SELECT user.namaPengguna, apr.status, apr.keterangan, apr.updated FROM ApprovalProcess apr "
			+ "JOIN ApprovalProcessType apt ON apt.id = apr.approvalProcessType.id "
			+ "JOIN Tahapan aa ON aa.id = apt.tahapan.id "
			+ "JOIN User user ON user.id = apr.user.id WHERE apr.isDelete =0 "
			+ "AND apt.valueId =:valueId AND aa.nama =:namaTahapan "
			+ "ORDER BY apr.sequence DESC"),
	@NamedQuery(name="ApprovalProcess.findApproveProcessId", query="SELECT ap FROM ApprovalProcess ap WHERE "
    		+ "ap.isDelete =0 AND ap.status !=1 AND ap.approvalProcessType.id =:approvalProcessTypeId ORDER BY ap.sequence ASC")
	
})

@SqlResultSetMappings(value = {
		@SqlResultSetMapping(name = "findByUserGroupAndActiveParameter", entities = {
				@EntityResult(entityClass = ApprovalProcess.class),}),
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_APPROVAL_PROCESS", initialValue = 1, allocationSize = 1)
public class ApprovalProcess implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 187496033042459522L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPROVAL_PROCESS_ID")
	@Expose
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "APPROVAL_PROCESS_TYPE_ID", referencedColumnName = "APPROVAL_PROCESS_TYPE_ID")
	@Expose
	private ApprovalProcessType approvalProcessType;
	@ManyToOne
	@JoinColumn(name = "APPROVAL_LEVEL_ID", referencedColumnName = "APPROVAL_LEVEL_ID")
	private ApprovalLevel approvalLevel;
	
	@Column(name = "STATUS")// 0=not process, 1 = active, 2=reject, 3=approve (last update status)
	@Expose
	private Integer status;

	@Column(name = "KETERANGAN")
	@Lob
	private String keterangan;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	@Expose
	private Organisasi group;
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	@Expose
	private User user;
	@Column(name = "SEQUENCE")
	@Expose
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
	@ManyToOne
	@JoinColumn(name = "JABATAN_ID", referencedColumnName = "JABATAN_ID")
	private Jabatan jabatan;

	public Jabatan getJabatan() {
		return jabatan;
	}

	public void setJabatan(Jabatan jabatan) {
		this.jabatan = jabatan;
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
	 * @return the approvalProcessType
	 */
	public ApprovalProcessType getApprovalProcessType() {
		return approvalProcessType;
	}

	/**
	 * @param approvalProcessType
	 *            the approvalProcessType to set
	 */
	public void setApprovalProcessType(ApprovalProcessType approvalProcessType) {
		this.approvalProcessType = approvalProcessType;
	}

	/**
	 * @return the approvalLevel
	 */
	public ApprovalLevel getApprovalLevel() {
		return approvalLevel;
	}

	/**
	 * @param approvalLevel
	 *            the approvalLevel to set
	 */
	public void setApprovalLevel(ApprovalLevel approvalLevel) {
		this.approvalLevel = approvalLevel;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the keterangan
	 */
	public String getKeterangan() {
		return keterangan;
	}

	public Organisasi getGroup() {
		return group;
	}

	public void setGroup(Organisasi group) {
		this.group = group;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	/**
	 * @param keterangan
	 *            the keterangan to set
	 */
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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
