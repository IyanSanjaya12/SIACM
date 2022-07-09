package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.KeuanganVendor;
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
public class KeuanganVendorSession extends AbstractFacadeWithAudit<KeuanganVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public KeuanganVendorSession(){
		super(KeuanganVendor.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public List<KeuanganVendor> getKeuanganVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("KeuanganVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public KeuanganVendor getKeuanganVendorByVendor(Integer vendorId){
		Query q = em.createNamedQuery("KeuanganVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<KeuanganVendor> keuanganVendor = q.getResultList();
		if (keuanganVendor != null && keuanganVendor.size() > 0) {
			return keuanganVendor.get(0);
		}
		return null;
	} 
	
	public KeuanganVendor insertKeuanganVendor(KeuanganVendor keuanganVendor, Token token) {
		keuanganVendor.setCreated(new Date());
		keuanganVendor.setIsDelete(0);
		super.create(keuanganVendor, AuditHelper.OPERATION_CREATE, token);
		return keuanganVendor;
	}

	public KeuanganVendor updateKeuanganVendor(KeuanganVendor keuanganVendor, Token token) {
		keuanganVendor.setUpdated(new Date());
		super.edit(keuanganVendor, AuditHelper.OPERATION_UPDATE, token);
		return keuanganVendor;
	}

	public KeuanganVendor deleteKeuanganVendor(int id, Token token) {
		KeuanganVendor keuanganVendor = super.find(id);
		keuanganVendor.setIsDelete(1);
		keuanganVendor.setDeleted(new Date());

		super.edit(keuanganVendor, AuditHelper.OPERATION_DELETE, token);
		return keuanganVendor;
	}

	public KeuanganVendor deleteRowKeuanganVendor(int id, Token token) {
		KeuanganVendor keuanganVendor = super.find(id);
		super.remove(keuanganVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return keuanganVendor;
	}
	
	public KeuanganVendor get(int id){
		return super.find(id);
	}
	
	public List<KeuanganVendor> deleteKeuanganVendorByVendorId(int vendorId){
		Query q = em.createNamedQuery("KeuanganVendor.deleteKeuanganVendor");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return null;
	}
}
