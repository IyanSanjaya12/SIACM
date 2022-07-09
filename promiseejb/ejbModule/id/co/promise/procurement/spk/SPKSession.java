package id.co.promise.procurement.spk;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.SPK;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author nanang
 *
 */
@Stateless
@LocalBean
public class SPKSession extends AbstractFacadeWithAudit<SPK> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public SPKSession() {
		super(SPK.class);
	}

	public SPK get(int id) {
		return super.find(id);
	}

	public SPK getSPKByPengadaan(int pengadaanId) {
		Query q = em.createNamedQuery("SPK.getSPKByPengadaan");
		q.setParameter("pengadaanId", pengadaanId);
		return (SPK) q.getSingleResult();
	}

	public List<SPK> getList() {
		Query q = em.createNamedQuery("SPK.getList");
		return q.getResultList();
	}

	public SPK insert(SPK spk, Token token) {
		spk.setCreated(new Date());
		spk.setIsDelete(0);
		super.create(spk, AuditHelper.OPERATION_CREATE, token);
		return spk;
	}

	public SPK update(SPK spk, Token token) {
		spk.setUpdated(new Date());
		super.edit(spk, AuditHelper.OPERATION_UPDATE, token);
		return spk;
	}

	public SPK delete(int id, Token token) {
		SPK spk = super.find(id);
		spk.setIsDelete(1);
		spk.setDeleted(new Date());
		super.edit(spk, AuditHelper.OPERATION_DELETE, token);
		return spk;
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
