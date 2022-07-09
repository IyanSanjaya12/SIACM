package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.JenisPajak;
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
public class JenisPajakSession extends AbstractFacadeWithAudit<JenisPajak> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public JenisPajakSession() {
		super(JenisPajak.class);
	}

	public JenisPajak getJenisPajak(int id) {
		return super.find(id);
	}

	public List<JenisPajak> getJenisPajakList() {
		Query q = em.createNamedQuery("JenisPajak.find");
		return q.getResultList();
	}

	public JenisPajak insertJenisPajak(JenisPajak jenisPajak, Token token) {
		jenisPajak.setCreated(new Date());
		jenisPajak.setIsDelete(0);
		super.create(jenisPajak, AuditHelper.OPERATION_CREATE, token);
		return jenisPajak;
	}

	public JenisPajak updateJenisPajak(JenisPajak jenisPajak, Token token) {
		jenisPajak.setUpdated(new Date());
		super.edit(jenisPajak, AuditHelper.OPERATION_UPDATE, token);
		return jenisPajak;
	}

	public JenisPajak deleteJenisPajak(int id, Token token) {
		JenisPajak jenisPajak = super.find(id);
		jenisPajak.setIsDelete(1);
		jenisPajak.setDeleted(new Date());

		super.edit(jenisPajak, AuditHelper.OPERATION_DELETE, token);
		return jenisPajak;
	}

	public JenisPajak deleteRowJenisPajak(int id, Token token) {
		JenisPajak jenisPajak = super.find(id);
		super.remove(jenisPajak, AuditHelper.OPERATION_ROW_DELETE, token);
		return jenisPajak;
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
