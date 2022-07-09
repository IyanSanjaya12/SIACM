package id.co.promise.procurement.vendor;

import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorProfileDraft;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class VendorProfileDraftSession extends AbstractFacadeWithAudit<VendorProfileDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	public VendorProfileDraftSession(){
		super(VendorProfileDraft.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<VendorProfileDraft> getVendorProfileDraftByNoPKP(String nomorPKP){
		Query q = em.createNamedQuery("VendorProfileDraft.findByNoPKP");
		q.setParameter("nomorPKP", nomorPKP);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<VendorProfileDraft> getVendorProfileDraftByNoNPWP(String nomorNPWP){
		Query q = em.createNamedQuery("VendorProfileDraft.findByNoNPWP");
		q.setParameter("npwpPerusahaan", nomorNPWP);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public VendorProfileDraft getVendorProfileDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("VendorProfileDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<VendorProfileDraft> vendorProfileDraft = q.getResultList();
		if (vendorProfileDraft != null && vendorProfileDraft.size() > 0) {
			return vendorProfileDraft.get(0);
		}
		
		return null;
	}
	
	public VendorProfileDraft insertVendorProfileDraft(VendorProfileDraft vendorProfileDraft, Token token) {
		vendorProfileDraft.setCreated(new Date());
		vendorProfileDraft.setIsDelete(0);
		super.create(vendorProfileDraft, AuditHelper.OPERATION_CREATE, token);
		//EmailNotification
		
		return vendorProfileDraft;
	}

	public VendorProfileDraft updateVendorProfileDraft(VendorProfileDraft vendorProfileDraft, Token token) {
		vendorProfileDraft.setUpdated(new Date());
		super.edit(vendorProfileDraft, AuditHelper.OPERATION_UPDATE, token);
		return vendorProfileDraft;
	}

	public VendorProfileDraft deleteVendorProfileDraft(int id, Token token) {
		VendorProfileDraft vendorProfileDraft = super.find(id);
		vendorProfileDraft.setIsDelete(1);
		vendorProfileDraft.setDeleted(new Date());

		super.edit(vendorProfileDraft, AuditHelper.OPERATION_DELETE, token);
		return vendorProfileDraft;
	}

	public VendorProfileDraft deleteRowVendorProfileDraft(int id, Token token) {
		VendorProfileDraft vendorProfileDraft = super.find(id);
		super.remove(vendorProfileDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return vendorProfileDraft;
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
