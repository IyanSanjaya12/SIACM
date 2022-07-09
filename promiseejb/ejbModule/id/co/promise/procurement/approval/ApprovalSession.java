/*
 * File: ApprovalSession.java
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
 * Aug 12, 2016 4:02:36 PM 	Mamat 		1.0 		Created
 * method
 */
package id.co.promise.procurement.approval;

import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

/**
 * @author Mamat
 *
 */
@Stateless
@LocalBean
public class ApprovalSession extends AbstractFacadeWithAudit<Approval> {
	final static Logger log = Logger.getLogger(ApprovalSession.class);
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	private ApprovalLevelSession approvalLevelSession;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public ApprovalSession() {
		super(Approval.class);
	}

	@SuppressWarnings("unchecked")
	public List<Approval> getList() {
		Query q = em.createNamedQuery("Approval.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Approval> getListByOrganisasi(Organisasi organisasi) {
		Query q = em.createNamedQuery("Approval.getListByOrganisasi");
		q.setParameter("organisasi", organisasi);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Approval> getNyName(String name) {
		Query q = em.createNamedQuery("Approval.findByName");
		q.setParameter("name", name);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Approval getApprovalByName(String name) {
		Query q = em.createQuery("SELECT approval FROM Approval approval WHERE approval.isDelete = 0 AND approval.name = :name");
		q.setParameter("name", name);
		
		List<Approval> approval = q.getResultList();
		if (approval != null && approval.size() > 0) {
			return approval.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Approval> getListApprovalByTahapanAndOrganisasi(Tahapan tahapan, Organisasi organisasi) {
		Query q = em.createNamedQuery("ApprovalTahapan.getListApprovalByTahapanAndOrganisasi");
		q.setParameter("tahapan",tahapan);
		q.setParameter("organisasi",organisasi);
		
		return q.getResultList();
	}

	public Approval getById(int id) {
		return super.find(id);
	}

	public Approval create(Approval approval, Token token) {
		approval.setCreated(new Date());
		approval.setIsDelete(0);
		super.create(approval, AuditHelper.OPERATION_CREATE, token);
		return approval;
	}

	public Approval update(Approval approval, Token token) {
		approval.setUpdated(new Date());
		super.edit(approval, AuditHelper.OPERATION_UPDATE, token);
		return approval;
	}

	public Approval delete(int id, Token token) {
		Approval app = super.find(id);
		app.setDeleted(new Date());
		app.setIsDelete(1);
		super.edit(app, AuditHelper.OPERATION_DELETE, token);
		return app;
	}

	@SuppressWarnings("unchecked")
	public List<MasterApprovalDTO> getMasterApproval() {
		Query q = em.createNamedQuery("Approval.getMasterApprovalList");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaApproval(String name, String toDo, Integer approvalId) {
		Query q = em.createNamedQuery("Approval.findByName");
		q.setParameter("name", name);
		List<Approval> approval = q.getResultList();
		  
		Boolean isSave = false;
		if(toDo.equalsIgnoreCase("insert")) {
			if(approval != null && approval.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
		   
		} else if (toDo.equalsIgnoreCase("update")) {
		   if(approval != null && approval.size() > 0) {
			   if(approvalId.equals(approval.get(0).getId())) {
				   isSave = true;
			   } else {
				   isSave = false;
			   }
		   } else {
			   isSave = true;
		   }
		}
		  
		return isSave;
		  
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getMasterApprovalDetil() {

		List masterApprovalDetil = new ArrayList();
		Query q = em.createNamedQuery("Approval.find");
		List<Approval> approvalList = q.getResultList();

		for (Approval approval : approvalList) {
			List joinApprovalAndLevel = new ArrayList();
			joinApprovalAndLevel.add(approval);

			List<ApprovalLevel> approvalLevel = approvalLevelSession.findByApproval(approval);

			joinApprovalAndLevel.add(approvalLevel);
			masterApprovalDetil.add(joinApprovalAndLevel);
		}

		return masterApprovalDetil;
	}

	public List getMasterApprovalDetilByOrganisasi(Organisasi organisasi) {
		List masterApprovalDetil = new ArrayList();
		Query q = em.createNamedQuery("Approval.getListByOrganisasi");
		q.setParameter("organisasi", organisasi);
		List<Approval> approvalList = q.getResultList();

		for (Approval approval : approvalList) {
			List joinApprovalAndLevel = new ArrayList();
			joinApprovalAndLevel.add(approval);

			List<ApprovalLevel> approvalLevel = approvalLevelSession.findByApproval(approval);

			joinApprovalAndLevel.add(approvalLevel);
			masterApprovalDetil.add(joinApprovalAndLevel);
		}

		return masterApprovalDetil;
	}
}
