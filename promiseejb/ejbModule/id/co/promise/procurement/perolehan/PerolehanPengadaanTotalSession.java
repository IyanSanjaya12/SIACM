/*
* File: PerolehanPengadaanTotalSession.java
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
* Dec 9, 2016 1:06:39 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.perolehan;

import java.util.List;

import id.co.promise.procurement.entity.PerolehanPengadaanTotal;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

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
public class PerolehanPengadaanTotalSession extends AbstractFacadeWithAudit<PerolehanPengadaanTotal> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public PerolehanPengadaanTotalSession(){
		super(PerolehanPengadaanTotal.class);
	}

	/**
	* Insert element at front of the sequence.
	* @param pElement the element to add 
	* @return the element is inserted as the first element of
	* the sequence
	* @exception java.lang.NullPointerException
	* if element is detected null
	*/
	public PerolehanPengadaanTotal findByPengadaan(int pengadaan) {
		Query q = em.createNamedQuery("PerolehanPengadaanTotal.findByPengadaan");
		q.setParameter("pengadaanId", pengadaan);
		try{
			return (PerolehanPengadaanTotal) q.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Integer> getPengadaanId() {
		Query q = em.createNamedQuery("PerolehanPengadaanTotal.getPengadaanId");
		try{
			return q.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	//cari perolehan berdasarkan pengadaan dan vendor
	public PerolehanPengadaanTotal findByPengadaanAndVendor(int pengadaan, int vendor) {
		Query q = em.createNamedQuery("PerolehanPengadaanTotal.findByPengadaanAndVendor");
		q.setParameter("pengadaanId", pengadaan);
		q.setParameter("vendorId", vendor);
		try{
			return (PerolehanPengadaanTotal) q.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	//cari total perolehan pengadan by vendor
	public double findTotalPerolehanByVendorId(int vendorId){
		Query q = em.createNamedQuery("PerolehanPengadaanTotal.findTotalPerolehanByVendorId");
		q.setParameter("vendorId", vendorId);
		try{
			Double nilai = (Double) q.getSingleResult();
			return nilai.doubleValue();
		}catch(Exception e){
			return 0d;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PerolehanPengadaanTotal> findPerolehanTotalByVendorId(int vendorId){
		String stringQuery = "SELECT t1 FROM PerolehanPengadaanTotal t1 WHERE t1.isDelete= :isDelete AND t1.vendor.id = :vendorId ";
		Query query = em.createQuery(stringQuery);
		query.setParameter("isDelete", 0);
		query.setParameter("vendorId", vendorId);
		return query.getResultList();
	}
}
