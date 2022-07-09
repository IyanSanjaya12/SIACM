package id.co.promise.procurement.vendor;



import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.VendorSKDDraft;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@LocalBean
public class VendorSKDDraftSession extends AbstractFacadeWithAudit<VendorSKDDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public VendorSKDDraftSession(){
		super(VendorSKDDraft.class);
	}

	@SuppressWarnings("unchecked")
	public List<VendorSKDDraft> getVendorSKDList(){
		Query q = em.createNamedQuery("VendorSKDDraft.findAll");
		return q.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<VendorSKDDraft> getVendorSkdDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("VendorSKDDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		List<VendorSKDDraft> vendorSKDDraft= q.getResultList();
		return vendorSKDDraft;
		
	}
	
	@SuppressWarnings("unchecked")
	public VendorSKDDraft getVendorSkdDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("VendorSKDDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<VendorSKDDraft> vendorSKDDraft = q.getResultList();
		if (vendorSKDDraft != null && vendorSKDDraft.size() > 0) {
			return vendorSKDDraft.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public VendorSKDDraft getVendorSKDDraftByVendorIdAndVendorSKD(Integer vendorId, Integer vendorSKDId) {
		Query q = em.createNamedQuery("VendorSKDDraft.findByVendorAndVendorSKD");
		q.setParameter("vendorId", vendorId);
		q.setParameter("vendorSKDId", vendorSKDId);
    
		List<VendorSKDDraft> vendorSKDDraft= q.getResultList();
		if (vendorSKDDraft!= null && vendorSKDDraft.size() > 0) {
				return vendorSKDDraft.get(0);
		}
			return null;
		}

	
	public VendorSKDDraft insertVendorSKDDraft(VendorSKDDraft vendorSKDDraft, Token token) {
		vendorSKDDraft.setCreated(new Date());
		vendorSKDDraft.setIsDelete(0);
		super.create(vendorSKDDraft,  AuditHelper.OPERATION_CREATE, token);
		return vendorSKDDraft;
	}
	
	public VendorSKDDraft updateVendorSKDDraft(VendorSKDDraft vendorSKDDraft, Token token) {
		vendorSKDDraft.setUpdated(new Date());
		super.edit(vendorSKDDraft, AuditHelper.OPERATION_UPDATE, token);
		return vendorSKDDraft;
	}
	
	public List<VendorSKDDraft> deleteVendorSKDDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("VendorSKDDraft.deleteVendorSKDDraft");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return null;
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
