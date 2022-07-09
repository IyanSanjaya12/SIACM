/*
* File: PerencanaanPengadaanSession.java
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
* Aug 30, 2016 6:05:30 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.inisialisasi;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.PerencanaanPengadaan;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Mamat
 *
 */

@Stateless
@LocalBean
public class PerencanaanPengadaanSession extends AbstractFacadeWithAudit<PerencanaanPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PerencanaanPengadaanSession(){
		super(PerencanaanPengadaan.class);
	}
	
	public List<PerencanaanPengadaan> getList(){
		Query q = em.createNamedQuery("PerencanaanPengadaan.find");
		return q.getResultList();
	}

	public List<PerencanaanPengadaan> getListByPerencanaanId(int perencanaanId){
		Query q = em.createNamedQuery("PerencanaanPengadaan.findByPerencanaanId").setParameter("perencanaanId", perencanaanId);
		return q.getResultList();
	}
	
	public List<PerencanaanPengadaan> getListByPengadaanId(int pengadaanId){
		Query q = em.createNamedQuery("PerencanaanPengadaan.findByPengadaanId").setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public PerencanaanPengadaan createPerencanaanPengadaan(PerencanaanPengadaan perencanaanPengadaan, id.co.promise.procurement.entity.Token token){
		perencanaanPengadaan.setCreated(new Date());
		perencanaanPengadaan.setIsDelete(0);
		super.create(perencanaanPengadaan, AuditHelper.OPERATION_CREATE, token);
		return perencanaanPengadaan;
	}
	
	public PerencanaanPengadaan editPerencanaanPengadaan(PerencanaanPengadaan perencanaanPengadaan, id.co.promise.procurement.entity.Token token){
		perencanaanPengadaan.setUpdated(new Date());
		super.edit(perencanaanPengadaan, AuditHelper.OPERATION_CREATE, token);
		return perencanaanPengadaan;
	}
	
	public PerencanaanPengadaan deletePerencanaanPengadaan(int id, id.co.promise.procurement.entity.Token token){
		PerencanaanPengadaan perencanaanPengadan = super.find(id);
		perencanaanPengadan.setDeleted(new Date());
		perencanaanPengadan.setIsDelete(1);
		super.edit(perencanaanPengadan, AuditHelper.OPERATION_DELETE, token);
		return perencanaanPengadan;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
}
