package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorPICDraft;
import id.co.promise.procurement.entity.VendorProfileDraft;
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
public class VendorPICDraftSession extends AbstractFacadeWithAudit<VendorPICDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public VendorPICDraftSession(){
		super(VendorPICDraft.class);
	}
	
	public List<VendorPICDraft> getVendorPICDraftByVendorList(Integer vendorId){
	Query q = em.createNamedQuery("VendorPICDraft.findByVendorList");
	q.setParameter("vendorId", vendorId);
	return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public VendorPICDraft getVendorPICDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("VendorPICDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<VendorPICDraft> vendorPICDraft = q.getResultList();
		if (vendorPICDraft != null && vendorPICDraft.size() > 0) {
			return vendorPICDraft.get(0);
		}
		return null;
	}
	
	public List<VendorPICDraft> deleteByVendorId(Integer vendorId){
		Query q = em.createNamedQuery("VendorPICDraft.deleteByVendorId");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return null;
		}
	
	public VendorPICDraft getVendorPICDraftByVendorAndVendorPIC(Integer vendorId, Integer vendorPICId){
		Query q = em.createNamedQuery("VendorPICDraft.findByVendorAndVendorPIC");
		q.setParameter("vendorId", vendorId);
		q.setParameter("vendorPICId", vendorPICId);
		
		List<VendorPICDraft> VendorPICDraft = q.getResultList();
		if (VendorPICDraft != null && VendorPICDraft.size() > 0) {
			return VendorPICDraft.get(0);
		}
		
		return null;
	}
	
	public List<VendorPICDraft> getVendorPICDraftByVendorId(int vendorId){
		Query q = em.createNamedQuery("VendorPICDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<VendorPICDraft> VendorPICDraft = q.getResultList();
		
		
		return VendorPICDraft;
	}

	public VendorPICDraft insertVendorPICDraft(VendorPICDraft vendorPICDraft, Token token) {
		vendorPICDraft.setCreated(new Date());
		vendorPICDraft.setIsDelete(0);
		super.create(vendorPICDraft, AuditHelper.OPERATION_CREATE, token);
		return vendorPICDraft;
	}

	public VendorPICDraft updateVendorPICDraft(VendorPICDraft vendorPICDraft, Token token) {
		vendorPICDraft.setUpdated(new Date());
		super.edit(vendorPICDraft, AuditHelper.OPERATION_UPDATE, token);
		return vendorPICDraft;
	}

	public VendorPICDraft deleteVendorPICDraft(int id, Token token) {
		VendorPICDraft vendorPICDraft = super.find(id);
		vendorPICDraft.setIsDelete(1);
		vendorPICDraft.setDeleted(new Date());

		super.edit(vendorPICDraft, AuditHelper.OPERATION_DELETE, token);
		return vendorPICDraft;
	}

	public VendorPICDraft deleteRowVendorPICDraft(int id, Token token) {
		VendorPICDraft vendorPICDraft = super.find(id);
		super.remove(vendorPICDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return vendorPICDraft;
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
