package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.UploadPurchaseRequest;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class UploadPurchaseRequestSession extends AbstractFacadeWithAudit<UploadPurchaseRequest> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	RoleUserSession roleUserSession;

	public UploadPurchaseRequestSession() {
		super(UploadPurchaseRequest.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<UploadPurchaseRequest> getList() {
		Query q = em.createNamedQuery("UploadPurchaseRequest.find");
		return q.getResultList();
	}
	
	/*@SuppressWarnings("unchecked")
	public List<UploadPurchaseRequest> getList(Integer purchaseRequestId) {
		Query q = em.createNamedQuery("UploadPurchaseRequest.getListByPurchaseRequest").
				setParameter("purchaseRequestId", purchaseRequestId);
		return q.getResultList();
	}*/
	
	public UploadPurchaseRequest get(int id) {
		return super.find(id);
	}

	public UploadPurchaseRequest insert(UploadPurchaseRequest object, Token token) {

		object.setIsDelete(0);
		object.setUserId(token.getUser().getId());
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	@SuppressWarnings("unchecked")
	public List<UploadPurchaseRequest> getListByPurchaseRequest(int purchaseRequestId) {
		Query q = em.createNamedQuery("UploadPurchaseRequest.getListByPurchaseRequest");
		q.setParameter("purchaseRequestId", purchaseRequestId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public int deleteRowByByPurchaseRequest(Integer purchaseRequestId) {
		Query q = em.createNamedQuery("UploadPurchaseRequest.deleteRowByByPurchaseRequest");
		q.setParameter("purchaseRequestId", purchaseRequestId);
		return q.executeUpdate();
	}

	public UploadPurchaseRequest update(UploadPurchaseRequest object, Token token) {

		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public UploadPurchaseRequest delete(int id, Token token) {
		UploadPurchaseRequest x = super.find(id);
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public UploadPurchaseRequest deleteRow(int id, Token token) {
		UploadPurchaseRequest x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
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
