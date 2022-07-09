package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.PeralatanVendorDraft;
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
public class PeralatanVendorDraftSession extends AbstractFacadeWithAudit<PeralatanVendorDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PeralatanVendorDraftSession(){
		super(PeralatanVendorDraft.class);
	}
	
	public List<PeralatanVendorDraft> getPeralatanVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("PeralatanVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public PeralatanVendorDraft getPeralatanVendorDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("PeralatanVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<PeralatanVendorDraft> peralatanVendorDraft = q.getResultList();
		if (peralatanVendorDraft != null && peralatanVendorDraft.size() > 0) {
			return peralatanVendorDraft.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public PeralatanVendorDraft getPeralatanVendorDraftByVendorIdAndPeralatanVendor(Integer vendorId, Integer peralatanVendorId) {
		Query q = em.createNamedQuery("PeralatanVendorDraft.findByVendorAndPeralatanVendor");
		q.setParameter("vendorId", vendorId);
		q.setParameter("peralatanVendorId", peralatanVendorId);
    
		List<PeralatanVendorDraft> peralatanVendorDraft = q.getResultList();
		if (peralatanVendorDraft != null && peralatanVendorDraft.size() > 0) {
				return peralatanVendorDraft.get(0);
		}
			return null;
		}

	public PeralatanVendorDraft insertPeralatanVendorDraft(PeralatanVendorDraft peralatanVendorDraft, Token token) {
		peralatanVendorDraft.setCreated(new Date());
		peralatanVendorDraft.setIsDelete(0);
		super.create(peralatanVendorDraft, AuditHelper.OPERATION_CREATE, token);
		return peralatanVendorDraft;
	}

	public PeralatanVendorDraft updatePeralatanVendorDraft(PeralatanVendorDraft peralatanVendorDraft, Token token) {
		peralatanVendorDraft.setUpdated(new Date());
		super.edit(peralatanVendorDraft, AuditHelper.OPERATION_UPDATE, token);
		return peralatanVendorDraft;
	}

	public PeralatanVendorDraft deletePeralatanVendorDraft(int id, Token token) {
		PeralatanVendorDraft peralatanVendorDraft = super.find(id);
		peralatanVendorDraft.setIsDelete(1);
		peralatanVendorDraft.setDeleted(new Date());

		super.edit(peralatanVendorDraft, AuditHelper.OPERATION_DELETE, token);
		return peralatanVendorDraft;
	}
	
	public PeralatanVendorDraft deleteRowPeralatanVendorDraft(int id, Token token) {
		PeralatanVendorDraft peralatanVendorDraft = super.find(id);
		super.remove(peralatanVendorDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return peralatanVendorDraft;
	}
	
	public void deletePeralatanVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("PeralatanVendorDraft.deletePeralatanVendorDraft");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		
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
