package id.co.promise.procurement.vendor.management;

import id.co.promise.procurement.entity.PenilaianVendor;
import id.co.promise.procurement.entity.Token;
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
public class PenilaianVendorSession extends AbstractFacadeWithAudit<PenilaianVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PenilaianVendorSession() {
		super(PenilaianVendor.class);
	}

	@SuppressWarnings("unchecked")
	public List<PenilaianVendor> getPenilaianVendorList() {
		Query q = em.createQuery("SELECT t1 FROM PenilaianVendor t1 WHERE t1.vPerfIsDelete = :vPerfIsDelete ORDER BY t1.vPerfId DESC");
		q.setParameter("vPerfIsDelete", 0);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PenilaianVendor> getPenilaianVendorListByVendor(int id) {
		Query q = em.createQuery("SELECT t1 FROM PenilaianVendor as t1 INNER JOIN t1.vendor as t2 WHERE t1.vPerfIsDelete = :vPerfIsDelete "
				+ "AND t2.id = :id ORDER BY t1.vPerfId DESC");
		q.setParameter("vPerfIsDelete", 0);
		q.setParameter("id", id);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PenilaianVendor> getPenilaianVendorListByVendorNotIn(int id, Integer vPerfIdArr) {
		Query q = em.createQuery("SELECT t1 FROM PenilaianVendor as t1 INNER JOIN t1.vendor as t2 WHERE t1.vPerfIsDelete = :vPerfIsDelete "
				+ "AND t2.id = :id "
				+ "AND t1.id != :vPerfId "
				+ "ORDER BY t1.vPerfId DESC");
		q.setParameter("vPerfIsDelete", 0);
		q.setParameter("id", id);
		q.setParameter("vPerfId", vPerfIdArr);
		return q.getResultList();
	}
	
	public PenilaianVendor getPenilaianVendorById(int PenilaianVendorId) {
		return super.find(PenilaianVendorId);
	}

	public PenilaianVendor createPenilaianVendor(PenilaianVendor ap, Token token) {
		ap.setvPerfCreated(new Date());
		ap.setvPerfIsDelete(0);
		super.create(ap, AuditHelper.OPERATION_CREATE, token);
		return ap;
	}

	public PenilaianVendor editPenilaianVendor(PenilaianVendor ap, Token token) {
		ap.setvPerfUpdated(new Date());
		super.edit(ap, AuditHelper.OPERATION_UPDATE, token);
		return ap;
	}

	public PenilaianVendor deletePenilaianVendor(int id, Token token) {
		PenilaianVendor bu = super.find(id);
		bu.setvPerfIsDelete(1);
		bu.setvPerfDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
	}
	
	public PenilaianVendor deletePenilaianVendor(PenilaianVendor bu, Token token) {
		bu.setvPerfIsDelete(1);
		bu.setvPerfDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
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
