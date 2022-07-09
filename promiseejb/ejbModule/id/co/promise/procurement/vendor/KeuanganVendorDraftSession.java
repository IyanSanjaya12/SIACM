package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.KeuanganVendorDraft;
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
public class KeuanganVendorDraftSession extends AbstractFacadeWithAudit<KeuanganVendorDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public KeuanganVendorDraftSession(){
		super(KeuanganVendorDraft.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public List<KeuanganVendorDraft> getKeuanganVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("KeuanganVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public KeuanganVendorDraft getKeuanganVendorDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("KeuanganVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<KeuanganVendorDraft> keuanganVendorDraft = q.getResultList();
		if (keuanganVendorDraft != null && keuanganVendorDraft.size() > 0) {
			return keuanganVendorDraft.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public KeuanganVendorDraft getKeuanganVendorDraftByVendorIdAndKeuanganVendor(Integer vendorId, Integer keuanganVendorId) {
		Query q = em.createNamedQuery("KeuanganVendorDraft.findByVendorAndKeuanganVendor");
		q.setParameter("vendorId", vendorId);
		q.setParameter("keuanganVendorId", keuanganVendorId);
    
		List<KeuanganVendorDraft> keuanganVendorDraft = q.getResultList();
		if (keuanganVendorDraft != null && keuanganVendorDraft.size() > 0) {
				return keuanganVendorDraft.get(0);
		}
			return null;
		}

	public KeuanganVendorDraft insertKeuanganVendorDraft(KeuanganVendorDraft keuanganVendorDraft, Token token) {
		keuanganVendorDraft.setCreated(new Date());
		keuanganVendorDraft.setIsDelete(0);
		super.create(keuanganVendorDraft, AuditHelper.OPERATION_CREATE, token);
		return keuanganVendorDraft;
	}

	public KeuanganVendorDraft updateKeuanganVendorDraft(KeuanganVendorDraft keuanganVendorDraft, Token token) {
		keuanganVendorDraft.setUpdated(new Date());
		super.edit(keuanganVendorDraft, AuditHelper.OPERATION_UPDATE, token);
		return keuanganVendorDraft;
	}

	public KeuanganVendorDraft deleteKeuanganVendorDraft(int id, Token token) {
		KeuanganVendorDraft keuanganVendorDraft = super.find(id);
		keuanganVendorDraft.setIsDelete(1);
		keuanganVendorDraft.setDeleted(new Date());

		super.edit(keuanganVendorDraft, AuditHelper.OPERATION_DELETE, token);
		return keuanganVendorDraft;
	}

	public KeuanganVendorDraft deleteRowKeuanganVendorDraft(int id, Token token) {
		KeuanganVendorDraft keuanganVendorDraft = super.find(id);
		super.remove(keuanganVendorDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return keuanganVendorDraft;
	}
	
	public KeuanganVendorDraft get(int id){
		return super.find(id);
	}
	
	public List<KeuanganVendor> deleteKeuanganVendorDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("KeuanganVendorDraft.deleteKeuanganVendorDraft");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return null;
	}
}
