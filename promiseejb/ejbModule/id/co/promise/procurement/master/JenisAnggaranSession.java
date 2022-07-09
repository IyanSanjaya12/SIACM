package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.JenisAnggaran;
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
public class JenisAnggaranSession extends AbstractFacadeWithAudit<JenisAnggaran>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	/**
	 * 
	 */
	public JenisAnggaranSession() {
		super(JenisAnggaran.class);
	}

	public List<JenisAnggaran> getList() {
		Query q = em.createNamedQuery("JenisAnggaran.find");
		return q.getResultList();
	}

	public JenisAnggaran getJenisAnggaran(int id) {
		return super.find(id);
	}

	public JenisAnggaran createJenisAnggaran(JenisAnggaran object, Token token) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public JenisAnggaran editJenisAnggaran(JenisAnggaran object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public JenisAnggaran deleteJenisAnggaran(int id, Token token) {
		JenisAnggaran x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public JenisAnggaran deleteRowJenisAnggaran(int id, Token token) {
		JenisAnggaran x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
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
