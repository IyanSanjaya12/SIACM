/*
* File: PurchaseRequestPengadaanSession.java
* This class is created to handle all processing involved
* in a shopping cart.
*
* Copyright (c) 2017 Mitra Mandiri Informatika
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
* Jan 16, 2017 5:02:58 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.inisialisasi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.AnggotaPanitia;
import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.entity.PurchaseRequestPengadaan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.master.AnggotaPanitiaSession;
import id.co.promise.procurement.master.PejabatPelaksanaPengadaanSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

/**
 * @author Mamat
 *
 */
@LocalBean
@Stateless
public class PurchaseRequestPengadaanSession extends AbstractFacadeWithAudit<PurchaseRequestPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PurchaseRequestPengadaanSession() {
		super(PurchaseRequestPengadaan.class);
	}	
	
	@EJB
	private AnggotaPanitiaSession anggotaPanitiaSession;
	
	@EJB
	private PejabatPelaksanaPengadaanSession pejabatPelaksanaSession;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestPengadaan> getList(){
		Query q = em.createNamedQuery("PurchaseRequestPengadaan.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequestPengadaan> getListByPerencanaanId(int purchaseRequestId){
		Query q = em.createNamedQuery("PurchaseRequestPengadaan.findByPurchaseRequestId").setParameter("purchaseRequestId", purchaseRequestId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestPengadaan> getListByPengadaanId(int pengadaanId){
		Query q = em.createNamedQuery("PurchaseRequestPengadaan.findByPengadaanId").setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public PurchaseRequestPengadaan createPurchaseRequestPengadaan(PurchaseRequestPengadaan prPengadaan, Token token){
		prPengadaan.setCreated(new Date());
		prPengadaan.setIsDelete(0);
		super.create(prPengadaan, AuditHelper.OPERATION_CREATE, token);
		return prPengadaan;
	}
	public PurchaseRequestPengadaan editPurchaseRequestPengadaan(PurchaseRequestPengadaan prPengadaan, Token token){
		prPengadaan.setUpdated(new Date());
		super.edit(prPengadaan, AuditHelper.OPERATION_UPDATE, token);
		return prPengadaan;
	}
	public PurchaseRequestPengadaan deletePurchaseRequestPengadaan(int id, Token token){
		PurchaseRequestPengadaan prPengadaan = super.find(id);
		prPengadaan.setDeleted(new Date());
		prPengadaan.setIsDelete(1);
		super.edit(prPengadaan, AuditHelper.OPERATION_DELETE, token);
		return prPengadaan;
	}
	
	public List<PurchaseRequestPengadaan> findByToken(Integer start, Integer length, String keyword, Token token, Integer columnOrder,
			String tipeOrder) {
		List<AnggotaPanitia> anggotaPanitiaList = anggotaPanitiaSession
				.getAnggotaPanitiaByRoleUserList(token.getUser().getId());
		Integer panitiaId = 0;
		if (anggotaPanitiaList != null && anggotaPanitiaList.size() > 0) {
			AnggotaPanitia anggotaPanitia = anggotaPanitiaList.get(0);
			panitiaId = anggotaPanitia.getTimPanitia().getPanitia().getId();

		} else {
			List<PejabatPelaksanaPengadaan> listPejabat = pejabatPelaksanaSession
					.getPejabatPelaksanaByRoleUserList(token.getUser().getId());
			if (listPejabat.size() > 0) {
				PejabatPelaksanaPengadaan pelaksanaPengadaan = listPejabat.get(0);
				panitiaId = pelaksanaPengadaan.getPanitia().getId();
			}
		}
		String queryUser = "SELECT o FROM PurchaseRequestPengadaan o WHERE o.pengadaan.panitia.id=:panitiaId AND "
				+ "(o.pengadaan.nomorNotaDinas like :keyword OR o.pengadaan.namaPengadaan like :keyword "
				+ "OR o.pengadaan.tahapanPengadaan.tahapan.nama like :keyword OR o.purchaseRequest.prnumber like :keyword"
				+ ") AND o.pengadaan.isDelete = :isDelete AND o.isDelete = :isDelete";

		String[] columnToView = { "nomor", "pengadaan.nomorNotaDinas", "pengadaan.namaPengadaan","purchaseRequest.prnumber", "pengadaan.tanggalNotaDinas",
				"pengadaan.tahapanPengadaan.tahapan.nama" };
		if (columnOrder > 0) {
			queryUser += " ORDER BY o. " + columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser += " ORDER BY o.id desc ";
		}

		Query query = em.createQuery(queryUser);
		query.setParameter("panitiaId", panitiaId);
		query.setParameter("keyword", keyword);
		query.setParameter("isDelete", 0);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<PurchaseRequestPengadaan> pgList = query.getResultList();
		return pgList;
	}
	
	public long countFindByToken(String keyword, Token token) {
		List<AnggotaPanitia> anggotaPanitiaList = anggotaPanitiaSession
				.getAnggotaPanitiaByRoleUserList(token.getUser().getId());
		Integer panitiaId = 0;
		if (anggotaPanitiaList != null && anggotaPanitiaList.size() > 0) {
			AnggotaPanitia anggotaPanitia = anggotaPanitiaList.get(0);
			panitiaId = anggotaPanitia.getTimPanitia().getPanitia().getId();

		} else {
			List<PejabatPelaksanaPengadaan> listPejabat = pejabatPelaksanaSession
					.getPejabatPelaksanaByRoleUserList(token.getUser().getId());
			if (listPejabat.size() > 0) {
				PejabatPelaksanaPengadaan pelaksanaPengadaan = listPejabat.get(0);
				panitiaId = pelaksanaPengadaan.getPanitia().getId();
			}
		}

		String queryCountUser = "SELECT COUNT(o) FROM PurchaseRequestPengadaan o WHERE o.pengadaan.panitia.id=:panitiaId AND "
				+ "(o.pengadaan.nomorNotaDinas like :keyword OR o.pengadaan.namaPengadaan like :keyword "
				+ "OR o.pengadaan.tahapanPengadaan.tahapan.nama like :keyword OR o.purchaseRequest.prnumber like :keyword"
				+ ") AND o.pengadaan.isDelete = 0 AND o.isDelete = 0";

		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", keyword);
		query.setParameter("panitiaId", panitiaId);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}
	
	public long countTotalFindByToken(Token token) {
		List<AnggotaPanitia> anggotaPanitiaList = anggotaPanitiaSession
				.getAnggotaPanitiaByRoleUserList(token.getUser().getId());
		Integer panitiaId = 0;
		if (anggotaPanitiaList != null && anggotaPanitiaList.size() > 0) {
			AnggotaPanitia anggotaPanitia = anggotaPanitiaList.get(0);
			panitiaId = anggotaPanitia.getTimPanitia().getPanitia().getId();

		} else {
			List<PejabatPelaksanaPengadaan> listPejabat = pejabatPelaksanaSession
					.getPejabatPelaksanaByRoleUserList(token.getUser().getId());
			if (listPejabat.size() > 0) {
				PejabatPelaksanaPengadaan pelaksanaPengadaan = listPejabat.get(0);
				panitiaId = pelaksanaPengadaan.getPanitia().getId();
			}
		}

		String queryCountUser = "SELECT COUNT(o.id) FROM PurchaseRequestPengadaan o WHERE o.pengadaan.panitia.id=:panitiaId "
				+ "AND o.isDelete = 0 AND o.pengadaan.isDelete = 0 ";

		Query query = em.createQuery(queryCountUser);
		query.setParameter("panitiaId", panitiaId);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findByPurchaseRequestIdContract(int purchaseRequestId){
		List<Object[]> list = new ArrayList<Object[]>();
		String stringQuery = " SELECT t1,t2 FROM PurchaseRequestPengadaan t1, Kontrak t2 "
							+ " WHERE t1.isDelete = :isDelete "
							+ " AND t2.isDelete = :isDelete "
							+ " AND t1.pengadaan.id = t2.pengadaan.id "
							+ " AND t1.purchaseRequest.id= :purchaseRequestId ";
		
		Query query = em.createQuery(stringQuery);
		query.setParameter("isDelete", 0);
		query.setParameter("purchaseRequestId", purchaseRequestId);
		list = query.getResultList();
		return list;
	}
}
