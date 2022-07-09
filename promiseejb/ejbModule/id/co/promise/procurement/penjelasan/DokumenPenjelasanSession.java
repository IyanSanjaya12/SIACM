package id.co.promise.procurement.penjelasan;

import id.co.promise.procurement.entity.DokumenPenjelasan;
import id.co.promise.procurement.entity.Penjelasan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 
 * Project Name : promiseejb Package Name : id.co.promise.procurement.penjelasan
 * File Name : DokumenPenjelasanSession.java Description :
 * 
 * @author : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com Since : Sep
 *         12, 2015
 *
 */
@Stateless
@LocalBean
public class DokumenPenjelasanSession extends
		AbstractFacadeWithAudit<DokumenPenjelasan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public DokumenPenjelasanSession() {

		super(DokumenPenjelasan.class);
	}

	public DokumenPenjelasan get(int id) {

		return super.find(id);
	}

	public List<Penjelasan> getList() {

		Query q = em.createNamedQuery("DokumenPenjelasan.find");
		return q.getResultList();
	}

	public List<DokumenPenjelasan> getListByPengadaan(int pengadaanId) {

		Query q = em.createNamedQuery("DokumenPenjelasan.getListByPengadaan");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	public DokumenPenjelasan insert(DokumenPenjelasan dokumenPenjelasan,
			Token token) {

		dokumenPenjelasan.setCreated(new Date());
		dokumenPenjelasan.setIsDelete(0);
		super.create(dokumenPenjelasan, AuditHelper.OPERATION_CREATE, token);
		return dokumenPenjelasan;
	}

	public DokumenPenjelasan update(DokumenPenjelasan dokumenPenjelasan,
			Token token) {

		dokumenPenjelasan.setUpdated(new Date());
		super.edit(dokumenPenjelasan, AuditHelper.OPERATION_UPDATE, token);
		return dokumenPenjelasan;
	}

	public DokumenPenjelasan delete(int id, Token token) {

		DokumenPenjelasan dokumenPenjelasan = super.find(id);
		dokumenPenjelasan.setIsDelete(1);
		dokumenPenjelasan.setDeleted(new Date());
		super.edit(dokumenPenjelasan, AuditHelper.OPERATION_DELETE, token);
		return dokumenPenjelasan;
	}

	public DokumenPenjelasan deleteRow(int id, Token token) {

		DokumenPenjelasan st = super.find(id);
		super.remove(st, AuditHelper.OPERATION_ROW_DELETE, token);
		return st;
	}

	public int deleteRowByPengadaan(int pengadaanId, Token token) {

		List<DokumenPenjelasan> dokumenPenjelasanList = getListByPengadaan(pengadaanId);
		for (DokumenPenjelasan dokumenPenjelasan : dokumenPenjelasanList) {
			deleteRow(dokumenPenjelasan.getId(), token);
		}
		return dokumenPenjelasanList.size();
	}

	@Override
	protected EntityManager getEntityManager() {

		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

}
