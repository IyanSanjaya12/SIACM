package id.co.promise.procurement.alokasianggaran;

import id.co.promise.procurement.entity.AnggaranPengadaan;
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

@Stateless
@LocalBean
public class AnggaranPengadaanSession extends
		AbstractFacadeWithAudit<AnggaranPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	/**
	 * 
	 */
	public AnggaranPengadaanSession() {
		super(AnggaranPengadaan.class);
	}

	public List<AnggaranPengadaan> getList() {
		Query q = em.createNamedQuery("AnggaranPengadaan.find");
		return q.getResultList();
	}

	public List<AnggaranPengadaan> getListByAnggaran(Integer anggaranId) {
		Query q = em.createNamedQuery("AnggaranPengadaan.findByAnggaran");
		q.setParameter("anggaranId", anggaranId);
		return q.getResultList();
	}

	public List<AnggaranPengadaan> getAnggaranPengadaanListByPengadaanId(
			int pengadaanId) {
		Query q = em.createNamedQuery("AnggaranPengadaan.findByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	public AnggaranPengadaan getAnggaranPengadaan(int id) {
		return super.find(id);
	}

	public AnggaranPengadaan createAnggaranPengadaan(AnggaranPengadaan object,
			Token token) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public AnggaranPengadaan updateAnggaranPengadaan(AnggaranPengadaan object,
			Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public AnggaranPengadaan editAnggaranPengadaan(AnggaranPengadaan object,
			Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public AnggaranPengadaan deleteAnggaranPengadaan(int id, Token token) {
		AnggaranPengadaan x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public AnggaranPengadaan deleteRowAnggaranPengadaan(int id, Token token) {
		AnggaranPengadaan x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

}
