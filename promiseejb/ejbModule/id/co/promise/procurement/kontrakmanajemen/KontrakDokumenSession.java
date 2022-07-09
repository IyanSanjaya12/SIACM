package id.co.promise.procurement.kontrakmanajemen;

import id.co.promise.procurement.entity.KontrakDokumen;
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
public class KontrakDokumenSession extends AbstractFacadeWithAudit<KontrakDokumen>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public KontrakDokumenSession() {
		super(KontrakDokumen.class);
	}
	
	public List<KontrakDokumen> getKontrakDokumenListBykontrakId(int kontrakId) {
		Query q = em.createNamedQuery("KontrakDokumen.findKontrakDokumenByKontrakId");
		q.setParameter("kontrakId", kontrakId);
		return q.getResultList();
	}

	public KontrakDokumen insertKontrakDokumen(KontrakDokumen kontrakDokumen, Token token) {
		kontrakDokumen.setCreated(new Date());
		kontrakDokumen.setIsDelete(0);
		super.create(kontrakDokumen, AuditHelper.OPERATION_CREATE, token);
		return kontrakDokumen;
	}

	public KontrakDokumen updateKontrakDokumen(KontrakDokumen kontrakDokumen, Token token) {
		kontrakDokumen.setUpdated(new Date());
		super.edit(kontrakDokumen, AuditHelper.OPERATION_UPDATE, token);
		return kontrakDokumen;
	}

	public KontrakDokumen deleteKontrakDokumen(int id, Token token) {
		KontrakDokumen kontrakDokumen = super.find(id);
		kontrakDokumen.setIsDelete(1);
		kontrakDokumen.setDeleted(new Date());

		super.edit(kontrakDokumen, AuditHelper.OPERATION_DELETE, token);
		return kontrakDokumen;
	}

	public KontrakDokumen deleteRowKontrakDokumen(int id, Token token) {
		KontrakDokumen kontrakDokumen = super.find(id);
		super.remove(kontrakDokumen, AuditHelper.OPERATION_ROW_DELETE, token);
		return kontrakDokumen;
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