package id.co.promise.procurement.vendor;

import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorProfile;
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
public class VendorProfileSession extends AbstractFacadeWithAudit<VendorProfile> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	public VendorProfileSession(){
		super(VendorProfile.class);
	}
	
	public List<VendorProfile> getVendorProfileByVendorId(int vendorId){
		Query q = em.createNamedQuery("VendorProfile.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public List<VendorProfile> getVendorProfileByNoPKP(String nomorPKP){
		Query q = em.createNamedQuery("VendorProfile.findByNoPKP");
		q.setParameter("nomorPKP", nomorPKP);
		return q.getResultList();
	}
	
	public List<VendorProfile> getVendorProfileByNoNPWP(String nomorNPWP){
		Query q = em.createNamedQuery("VendorProfile.findByNoNPWP");
		q.setParameter("npwpPerusahaan", nomorNPWP);
		return q.getResultList();
	}

	public VendorProfile insertVendorProfile(VendorProfile vendorProfile, Token token) {
		vendorProfile.setCreated(new Date());
		vendorProfile.setIsDelete(0);
		super.create(vendorProfile, AuditHelper.OPERATION_CREATE, token);
		//EmailNotification
		emailNotificationSession.getMailGeneratorRegistrasiVendor(vendorProfile);
		
		return vendorProfile;
	}

	public VendorProfile updateVendorProfile(VendorProfile vendorProfile, Token token) {
		vendorProfile.setUpdated(new Date());
		super.edit(vendorProfile, AuditHelper.OPERATION_UPDATE, token);
		return vendorProfile;
	}

	public VendorProfile deleteVendorProfile(int id, Token token) {
		VendorProfile vendorProfile = super.find(id);
		vendorProfile.setIsDelete(1);
		vendorProfile.setDeleted(new Date());

		super.edit(vendorProfile, AuditHelper.OPERATION_DELETE, token);
		return vendorProfile;
	}

	public VendorProfile deleteRowVendorProfile(int id, Token token) {
		VendorProfile vendorProfile = super.find(id);
		super.remove(vendorProfile, AuditHelper.OPERATION_ROW_DELETE, token);
		return vendorProfile;
	}
	
	@SuppressWarnings("unchecked")
	public VendorProfile getVendorProfileByVendor(Integer vendorId){
		Query q = em.createNamedQuery("VendorProfile.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<VendorProfile> vendorProfile = q.getResultList();
		if (vendorProfile != null && vendorProfile.size() > 0) {
			return vendorProfile.get(0);
		}
		
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
