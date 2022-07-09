package id.co.promise.procurement.vendor;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.VendorDraft;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class VendorDraftSession extends AbstractFacadeWithAudit<VendorDraft> {

	final static Logger logger = Logger.getLogger(VendorDraftSession.class);

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB UserSession userSession;

	@EJB CatalogSession catalogSession;

	@EJB CategorySession categorySession;
	
	@EJB OrganisasiSession organisasiSession;
	
	@EJB SegmentasiVendorSession segmentasiVendorSession;


	public VendorDraftSession() {

		super(VendorDraft.class);
	}

	public int countByStatus(int status) {

		Query q = em.createNamedQuery("VendorDraft.countByStatus").setParameter("status", status);
		Object data = q.getSingleResult();
		return (data != null) ? Integer.parseInt(data.toString()) : 0;
	}

	@SuppressWarnings("unchecked")
	public List<VendorDraft> getVendorDraftList() {

		Query q = em.createNamedQuery("Vendor.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public VendorDraft getVendorDraftById(int id) {

		Query q = em.createNamedQuery("VendorDraft.findById");
		q.setParameter("id", id);
		List<VendorDraft> vendorList = q.getResultList();
		if (vendorList != null && vendorList.size() > 0) {
			return vendorList.get(0);
		}
		return null;
		
	}

	@SuppressWarnings("unchecked")
	public List<VendorDraft> getVendorListByStatus(Integer status) {

		Query q = em.createNamedQuery("VendorDraft.findByStatus");
		q.setParameter("status", status);
		return q.getResultList();
	}

	public VendorDraft getVendorDraft(int id) {

		return super.find(id);
	}

	public VendorDraft insertVendorDraft(VendorDraft vendorDraft,
			Token token) {

		vendorDraft.setCreated(new Date());
		vendorDraft.setIsDelete(0);
		vendorDraft.setStatus(0);
		super.create(vendorDraft, AuditHelper.OPERATION_CREATE, token);
		return vendorDraft;
	}

	public VendorDraft updateVendorDraft(VendorDraft vendorDraft,
			Token token) {

		vendorDraft.setUpdated(new Date());
		super.edit(vendorDraft, AuditHelper.OPERATION_UPDATE, token);
		return vendorDraft;
	}
	@SuppressWarnings("unchecked")
	public List<VendorDraft> getVendorDraftByVendorId(Integer id) {

		Query q = em.createNamedQuery("VendorDraft.findByVendorId");
		q.setParameter("id", id);
		return q.getResultList();
	}

	public VendorDraft deleteVendor(int vendorId,
			Token token) {

		VendorDraft vendorDraft = super.find(vendorId);
		vendorDraft.setIsDelete(1);
		vendorDraft.setDeleted(new Date());
		super.edit(vendorDraft, AuditHelper.OPERATION_DELETE, token);

		// delete user too
		User user = userSession.getUser(vendorDraft.getUser());
		userSession.deleteUserClasic(user.getId(), token);
		return vendorDraft;
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
