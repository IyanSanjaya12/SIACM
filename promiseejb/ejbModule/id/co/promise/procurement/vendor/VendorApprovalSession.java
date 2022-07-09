package id.co.promise.procurement.vendor;

import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.VendorApproval;
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

@Stateless
@LocalBean
public class VendorApprovalSession extends AbstractFacadeWithAudit<VendorApproval> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private VendorProfileSession vendorProfileSession;
	@EJB
	private EmailNotificationSession emailNotificationSession;

	public VendorApprovalSession() {
		super(VendorApproval.class);
	}

	public VendorApproval get(int id) {

		return super.find(id);
	}

	public VendorApproval insertVendorApproval(VendorApproval vendorApproval, Token token) {
		vendorApproval.setCreated(new Date());
		vendorApproval.setIsDelete(0);
		super.create(vendorApproval, AuditHelper.OPERATION_CREATE, token);
				
		return vendorApproval;
	}

	public VendorApproval updateVendorApproval(VendorApproval vendorApproval, Token token) {
		vendorApproval.setUpdated(new Date());
		super.edit(vendorApproval, AuditHelper.OPERATION_UPDATE, token);
		return vendorApproval;
	}

	public VendorApproval deleteVendorApproval(int id, Token token) {
		VendorApproval vendorApproval = super.find(id);
		vendorApproval.setIsDelete(1);
		vendorApproval.setDeleted(new Date());

		super.edit(vendorApproval, AuditHelper.OPERATION_DELETE, token);
		return vendorApproval;
	}

	public VendorApproval deleteRowVendorApproval(int id, Token token) {
		VendorApproval vendorApproval = super.find(id);
		super.remove(vendorApproval, AuditHelper.OPERATION_ROW_DELETE, token);
		return vendorApproval;
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
