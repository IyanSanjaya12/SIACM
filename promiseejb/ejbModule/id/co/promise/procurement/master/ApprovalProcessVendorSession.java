package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.ApprovalProcessVendor;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class ApprovalProcessVendorSession extends AbstractFacadeWithAudit<ApprovalProcessVendor>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	TokenSession tokenSession;
	
	@EJB
	ApprovalProcessVendorSession approvalProcessVendorSession;
	
	@EJB
	PurchaseRequestSession purchaseRequestSession;

	public ApprovalProcessVendorSession() {
		super(ApprovalProcessVendor.class);
	}

	public ApprovalProcessVendor getApprovalProcessVendor(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcessVendor> getApprovalProcessVendorList(Integer bookingOrderId) {
		Query q = em.createNamedQuery("ApprovalProcessVendor.findByPurchaseRequestId");
		q.setParameter("valueId", bookingOrderId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcessVendor> getApprovalProcessVendorStatus1List(String token, Integer bookingOrderId) {
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		
		Query q = em.createNamedQuery("ApprovalProcessVendor.findByStatus");
		q.setParameter("userId",user.getId());
		q.setParameter("purchaseRequest", bookingOrderId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcessVendor> getApprovalConfirmationVendorList(Integer valueId) {
		Query q = em.createNamedQuery("ApprovalProcessVendor.findByPurchaseRequestId");
		q.setParameter("valueId", valueId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcessVendor> findByApprovalProcessVendor(Integer id) {
		Query q = em.createNamedQuery("ApprovalProcessVendor.findByApprovalProcessVendor");
		q.setParameter("approvalProcessTypeId", id);
		return q.getResultList();
	}

	public ApprovalProcessVendor getApprovalProcessVendorByPurchaseRequestId(Integer prId) {
		Query q = em.createNamedQuery("ApprovalProcessVendor.getApprovalProcessVendorByPurchaseRequestId");
		q.setParameter("purchaseRequestId", prId);
		List<ApprovalProcessVendor> approvalProcessVendorList= q.getResultList();
		ApprovalProcessVendor approvalProcessVendor = null;
		if (approvalProcessVendorList.size() > 0) {
			approvalProcessVendor = approvalProcessVendorList.get(0);
		}
		return approvalProcessVendor;
	}
	
	public ApprovalProcessVendor insertApprovalProcessVendor(ApprovalProcessVendor approvalProcessVendor, Token token) {
		approvalProcessVendor.setCreated(new Date());
		approvalProcessVendor.setIsDelete(0);
		super.create(approvalProcessVendor, AuditHelper.OPERATION_CREATE, token);
		return approvalProcessVendor;
	}

	public ApprovalProcessVendor updateApprovalProcessVendor(ApprovalProcessVendor approvalProcessVendor, Token token) {
		approvalProcessVendor.setUpdated(new Date());
		super.edit(approvalProcessVendor, AuditHelper.OPERATION_UPDATE, token);
		return approvalProcessVendor;
	}

	public ApprovalProcessVendor deleteApprovalProcessVendor(int id, Token token) {
		ApprovalProcessVendor approvalProcessVendor = super.find(id);
		approvalProcessVendor.setIsDelete(1);
		approvalProcessVendor.setDeleted(new Date());
		super.edit(approvalProcessVendor, AuditHelper.OPERATION_DELETE, token);
		return approvalProcessVendor;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

}
