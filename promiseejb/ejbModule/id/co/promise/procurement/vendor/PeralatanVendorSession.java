package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.SegmentasiVendorDraft;
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
public class PeralatanVendorSession extends AbstractFacadeWithAudit<PeralatanVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PeralatanVendorSession(){
		super(PeralatanVendor.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<PeralatanVendor> getPeralatanVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("PeralatanVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public PeralatanVendor getPeralatanVendorByVendor(Integer vendorId){
		Query q = em.createNamedQuery("PeralatanVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<PeralatanVendor> peralatanVendor = q.getResultList();
		if (peralatanVendor != null && peralatanVendor.size() > 0) {
			return peralatanVendor.get(0);
		}
		return null;
	}


	public PeralatanVendor insertPeralatanVendor(PeralatanVendor peralatanVendor, Token token) {
		peralatanVendor.setCreated(new Date());
		peralatanVendor.setIsDelete(0);
		super.create(peralatanVendor, AuditHelper.OPERATION_CREATE, token);
		return peralatanVendor;
	}

	public PeralatanVendor updatePeralatanVendor(PeralatanVendor peralatanVendor, Token token) {
		peralatanVendor.setUpdated(new Date());
		super.edit(peralatanVendor, AuditHelper.OPERATION_UPDATE, token);
		return peralatanVendor;
	}

	public PeralatanVendor deletePeralatanVendor(int id, Token token) {
		PeralatanVendor peralatanVendor = super.find(id);
		peralatanVendor.setIsDelete(1);
		peralatanVendor.setDeleted(new Date());

		super.edit(peralatanVendor, AuditHelper.OPERATION_DELETE, token);
		return peralatanVendor;
	}

	public PeralatanVendor deleteRowPeralatanVendor(int id, Token token) {
		PeralatanVendor peralatanVendor = super.find(id);
		super.remove(peralatanVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return peralatanVendor;
	}
	
	public List<PeralatanVendor> deletePeralatanVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("PeralatanVendor.deletePeralatanVendor");
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
