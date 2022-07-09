package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.DokumenPekerjaan;
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
public class DokumenPekerjaanSession extends AbstractFacadeWithAudit<DokumenPekerjaan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public DokumenPekerjaanSession() {
		super(DokumenPekerjaan.class);
	}

	public DokumenPekerjaan get(int id) {
		return super.find(id);
	}
	
	public List<DokumenPekerjaan> getList() {
		Query q = em.createNamedQuery("DokumenPekerjaan.find");
		return q.getResultList();
	}
	
	public List<DokumenPekerjaan> getDokumenPekerjaanByPerencanaan(int perencanaanId) {
		Query q = em.createNamedQuery("DokumenPekerjaan.findByPerencanaan");
		q.setParameter("perencanaanId", perencanaanId);
		return q.getResultList();
	}

	public DokumenPekerjaan insert(DokumenPekerjaan dokumenPekerjaan, Token token) {
		dokumenPekerjaan.setCreated(new Date());
		dokumenPekerjaan.setIsDelete(0);
		super.create(dokumenPekerjaan, AuditHelper.OPERATION_CREATE, token);
		return dokumenPekerjaan;
	}

	public DokumenPekerjaan update(DokumenPekerjaan dokumenPekerjaan, Token token) {
		dokumenPekerjaan.setUpdated(new Date());
		super.edit(dokumenPekerjaan, AuditHelper.OPERATION_UPDATE, token);
		return dokumenPekerjaan;
	}

	public DokumenPekerjaan delete(int id, Token token) {
		DokumenPekerjaan dokumenPekerjaan = super.find(id);
		dokumenPekerjaan.setIsDelete(1);
		dokumenPekerjaan.setDeleted(new Date());
		super.edit(dokumenPekerjaan, AuditHelper.OPERATION_DELETE, token);
		return dokumenPekerjaan;
	}

	public DokumenPekerjaan deleteRow(int id, Token token) {
		DokumenPekerjaan dokumenPekerjaan = super.find(id);
		super.remove(dokumenPekerjaan, AuditHelper.OPERATION_ROW_DELETE, token);
		return dokumenPekerjaan;
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
