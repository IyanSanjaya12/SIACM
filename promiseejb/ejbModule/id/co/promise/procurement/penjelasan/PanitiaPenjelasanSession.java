package id.co.promise.procurement.penjelasan;

import id.co.promise.procurement.entity.PanitiaPenjelasan;
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
 * File Name : PanitiaPenjelasanSession.java Description :
 * 
 * @author : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com Since : Sep
 *         12, 2015
 *
 */

@Stateless
@LocalBean
public class PanitiaPenjelasanSession extends
		AbstractFacadeWithAudit<PanitiaPenjelasan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PanitiaPenjelasanSession() {

		super(PanitiaPenjelasan.class);
	}

	public PanitiaPenjelasan get(int id) {

		return super.find(id);
	}

	public List<Penjelasan> getList() {

		Query q = em.createNamedQuery("PanitiaPenjelasan.getList");
		return q.getResultList();
	}

	public List<PanitiaPenjelasan> getListByPengadaan(int pengadaanId) {

		Query q = em.createNamedQuery("PanitiaPenjelasan.getListByPengadaan");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	public PanitiaPenjelasan insert(PanitiaPenjelasan panitiaPenjelasan,
			Token token) {

		panitiaPenjelasan.setCreated(new Date());
		panitiaPenjelasan.setIsDelete(0);
		super.create(panitiaPenjelasan, AuditHelper.OPERATION_CREATE, token);
		return panitiaPenjelasan;
	}

	public PanitiaPenjelasan update(PanitiaPenjelasan panitiaPenjelasan,
			Token token) {

		panitiaPenjelasan.setUpdated(new Date());
		super.edit(panitiaPenjelasan, AuditHelper.OPERATION_UPDATE, token);
		return panitiaPenjelasan;
	}

	public PanitiaPenjelasan delete(int id, Token token) {

		PanitiaPenjelasan panitiaPenjelasan = super.find(id);
		panitiaPenjelasan.setIsDelete(1);
		panitiaPenjelasan.setDeleted(new Date());
		super.edit(panitiaPenjelasan, AuditHelper.OPERATION_DELETE, token);
		return panitiaPenjelasan;
	}

	public PanitiaPenjelasan deleteRow(int id, Token token) {

		PanitiaPenjelasan st = super.find(id);
		super.remove(st, AuditHelper.OPERATION_ROW_DELETE, token);
		return st;
	}

	public int deleteRowByPengadaan(int pengadaanId, Token token) {

		List<PanitiaPenjelasan> panitiaPenjelasanList = getListByPengadaan(pengadaanId);
		for (PanitiaPenjelasan panitiaPenjelasan : panitiaPenjelasanList) {
			deleteRow(panitiaPenjelasan.getId(), token);
		}
		return panitiaPenjelasanList.size();
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
