/*
 * File: PerolehanPengadaanSatuanSession.java
 * This class is created to handle all processing involved
 * in a PerolehanPengadaanSatuanSession.
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
 * Dec 14, 2016 8:33:30 PM 	Mamat 		1.0 		Created
 * method
 */
package id.co.promise.procurement.perolehan;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.PerolehanPengadaanSatuan;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

/**
 * @author Mamat
 *
 */
@LocalBean
@Stateless
public class PerolehanPengadaanSatuanSession extends
		AbstractFacadeWithAudit<PerolehanPengadaanSatuan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PerolehanPengadaanSatuanSession() {
		super(PerolehanPengadaanSatuan.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	// cari perolehan berdasarkan pengadaan dan vendor
	public List<PerolehanPengadaanSatuan> findByPengadaan(int pengadaan) {
		Query q = em
				.createNamedQuery("PerolehanPengadaanSatuan.findByPengadaan");
		q.setParameter("pengadaanId", pengadaan);
		return q.getResultList();

	}

	// cari perolehan berdasarkan pengadaan dan vendor
	public List<PerolehanPengadaanSatuan> findByPengadaanAndVendor(
			int pengadaan, int vendor) {
		Query q = em
				.createNamedQuery("PerolehanPengadaanSatuan.findByPengadaanAndVendor");
		q.setParameter("pengadaanId", pengadaan);
		q.setParameter("vendorId", vendor);

		return q.getResultList();

	}

	// cari perolehan berdasarkan pengadaan, vendor dan itempengadaan
	public PerolehanPengadaanSatuan findByPengadaanVendorAndItemPengadaan(
			int pengadaan, int vendor, int itemPengadaan) {
		Query q = em
				.createNamedQuery("PerolehanPengadaanSatuan.findByPengadaanVendorAndItemPengadaan");
		q.setParameter("pengadaanId", pengadaan);
		q.setParameter("vendorId", vendor);
		q.setParameter("itemPengadaanId", itemPengadaan);
		try {
			return (PerolehanPengadaanSatuan) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	// cari perolehan berdasarkan pengadaan dan itempengadaan
	public PerolehanPengadaanSatuan findByPengadaanVendorAndItem(
			int pengadaanId, int vendor, int itemId) {
		Query q = em
				.createNamedQuery("PerolehanPengadaanSatuan.findByPengadaanVendorAndItemId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("vendorId", vendor);
		q.setParameter("itemId", itemId);
		try {
			return (PerolehanPengadaanSatuan) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<PerolehanPengadaanSatuan> findByPengadaanAndItem(int pengadaanId, int itemId) {
		Query q = em
				.createNamedQuery("PerolehanPengadaanSatuan.findByPengadaanAndItemId");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("itemId", itemId);
		return q.getResultList();
	}
}
