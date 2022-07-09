package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorProfile;
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
public class VendorPICSession extends AbstractFacadeWithAudit<VendorPIC> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public VendorPICSession(){
		super(VendorPIC.class);
	}
	
	@SuppressWarnings("unchecked")
	public VendorPIC getVendorPICByVendor(Integer vendorId){
		Query q = em.createNamedQuery("VendorPIC.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<VendorPIC> vendorPIC = q.getResultList();
		if (vendorPIC != null && vendorPIC.size() > 0) {
			return vendorPIC.get(0);
		}
		return null;
	}
	
	public List<VendorPIC> getVendorPICByVendorId(int vendorId){
		Query q = em.createNamedQuery("VendorPIC.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}

	public VendorPIC insertVendorPIC(VendorPIC vendorPIC, Token token) {
		vendorPIC.setCreated(new Date());
		vendorPIC.setIsDelete(0);
		super.create(vendorPIC, AuditHelper.OPERATION_CREATE, token);
		return vendorPIC;
	}

	public VendorPIC updateVendorPIC(VendorPIC vendorPIC, Token token) {
		vendorPIC.setUpdated(new Date());
		super.edit(vendorPIC, AuditHelper.OPERATION_UPDATE, token);
		return vendorPIC;
	}

	public VendorPIC deleteVendorPIC(int id, Token token) {
		VendorPIC vendorPIC = super.find(id);
		vendorPIC.setIsDelete(1);
		vendorPIC.setDeleted(new Date());

		super.edit(vendorPIC, AuditHelper.OPERATION_DELETE, token);
		return vendorPIC;
	}

	public VendorPIC deleteRowVendorPIC(int id, Token token) {
		VendorPIC vendorPIC = super.find(id);
		super.remove(vendorPIC, AuditHelper.OPERATION_ROW_DELETE, token);
		return vendorPIC;
	}
	
	public void deleteVendorPICByVendorId(int vendorId){
		Query q = em.createNamedQuery("VendorPIC.deleteByVendorId");
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
