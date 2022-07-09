package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.AnggaranPerencanaan;
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
public class AnggaranPerencanaanSession extends AbstractFacadeWithAudit<AnggaranPerencanaan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public AnggaranPerencanaanSession() {
		super(AnggaranPerencanaan.class);
	}

	public AnggaranPerencanaan get(int id) {
		return super.find(id);
	}
	
	public List<AnggaranPerencanaan> getList() {
		Query q = em.createNamedQuery("AnggaranPerencanaan.find");
		return q.getResultList();
	}
	
	public List<AnggaranPerencanaan> getListZC01AnggaranPerencanaanByAlokasiAnggaranId(int alokasiAnggaranId) {
		Query q = em.createNamedQuery("AnggaranPerencanaan.findByAlokasiAnggaran");
		q.setParameter("alokasiAnggaranId", alokasiAnggaranId);
		return q.getResultList();
	}
	
	public AnggaranPerencanaan getListSingleResultByAlokasiAnggaranId(int alokasiAnggaranId) {
		Query q = em.createNamedQuery("AnggaranPerencanaan.findByAlokasiAnggaran");
		q.setParameter("alokasiAnggaranId", alokasiAnggaranId);
		q.setMaxResults(1);
		try {
			return (AnggaranPerencanaan) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public AnggaranPerencanaan insert(AnggaranPerencanaan zc01AnggaranPerencanaan, Token token){
		zc01AnggaranPerencanaan.setCreated(new Date());
		zc01AnggaranPerencanaan.setIsDelete(0);
		super.create(zc01AnggaranPerencanaan, AuditHelper.OPERATION_CREATE, token);
		return zc01AnggaranPerencanaan;
	}

	public AnggaranPerencanaan update(AnggaranPerencanaan zC01AnggaranPerencanaan, Token token) {
		zC01AnggaranPerencanaan.setUpdated(new Date());
		super.edit(zC01AnggaranPerencanaan, AuditHelper.OPERATION_UPDATE, token);
		return zC01AnggaranPerencanaan;
	}

	public AnggaranPerencanaan delete(int id, Token token) {
		AnggaranPerencanaan zC01AnggaranPerencanaan = super.find(id);
		zC01AnggaranPerencanaan.setIsDelete(1);
		zC01AnggaranPerencanaan.setDeleted(new Date());
		super.edit(zC01AnggaranPerencanaan, AuditHelper.OPERATION_DELETE, token);
		return zC01AnggaranPerencanaan;
	}

	public AnggaranPerencanaan deleteRow(int id, Token token) {
		AnggaranPerencanaan zC01AnggaranPerencanaan = super.find(id);
		super.remove(zC01AnggaranPerencanaan, AuditHelper.OPERATION_ROW_DELETE, token);
		return zC01AnggaranPerencanaan;
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
