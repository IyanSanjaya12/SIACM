package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorSKD;
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
public class VendorSKDSession extends AbstractFacadeWithAudit<VendorSKD> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public VendorSKDSession(){
		super(VendorSKD.class);
	}

	@SuppressWarnings("unchecked")
	public List<VendorSKD> getVendorSKDList(){
		Query q = em.createNamedQuery("VendorSKD.findAll");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<VendorSKD> getVendorSkdByVendorId(int vendorId){
		Query q = em.createQuery("SELECT vendorSkd FROM VendorSKD vendorSkd WHERE VendorSKD.isDelete = 0 AND VendorSKD.vendor.id =:vendorId");
		q.setParameter("vendorId", vendorId);
		List<VendorSKD> vendorSkd = q.getResultList();
		return vendorSkd;
		
	}
	
	@SuppressWarnings("unchecked")
	public VendorSKD getVendorSkdByVendor(Integer vendorId){
		Query q = em.createNamedQuery("VendorSKD.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<VendorSKD> vendorSKD = q.getResultList();
		if (vendorSKD != null && vendorSKD.size() > 0) {
			return vendorSKD.get(0);
		}
		return null;
	}
	
	public VendorSKD insertVendorSKD(VendorSKD vendorSKD, Token token) {
		/*vendorSKD.setCreated(new Date());*/
		vendorSKD.setIsDelete(0);
		super.create(vendorSKD,  AuditHelper.OPERATION_CREATE, token);
		return vendorSKD;
	}
	
	public VendorSKD updateVendorSKD(VendorSKD vendorSKD, Token token) {
		vendorSKD.setUpdated(new Date());
		super.edit(vendorSKD, AuditHelper.OPERATION_UPDATE, token);
		return vendorSKD;
	}
	
	public List<VendorSKD> deleteVendorSKDByVendorId(int vendorId){
		Query q = em.createNamedQuery("VendorSKD.deleteVendorSKD");
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
