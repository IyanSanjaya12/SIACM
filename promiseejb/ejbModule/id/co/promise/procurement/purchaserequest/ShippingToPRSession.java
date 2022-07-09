/**
 * fdf
 */
package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.ShippingToPR;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class ShippingToPRSession extends AbstractFacadeWithAudit<ShippingToPR>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	@EJB
	private PurchaseRequestItemSession prItemSession;
	
	public ShippingToPRSession() {
		super(ShippingToPR.class);
	}
	
	public ShippingToPR getShippingToPR(int id) {
		return super.find(id);
	}
	
	public List<ShippingToPR> getShippingToPRList() {
		Query q = em.createNamedQuery("ShippingToPR.find");
		return q.getResultList();
	}

	public List<ShippingToPR> findShippingByPR(int prid){
		Query q = em.createNamedQuery("ShippingToPR.findShippingByPR");
		List<Integer> prItemid = new ArrayList<Integer>();
		List<PurchaseRequestItem> prItemList = prItemSession.getPurchaseRequestItemByPurchaseRequestId(prid);
		for (PurchaseRequestItem purchaseRequestItem : prItemList) {
			prItemid.add(purchaseRequestItem.getId());
		}
		q.setParameter("prItemid", prItemid);
		return q.getResultList();
	}
	
	public List<ShippingToPR> findShippingByPRItemId(int prItemId){
		Query q = em.createNamedQuery("ShippingToPR.findShippingByPRItemId");
		q.setParameter("prItemId", prItemId);
		return q.getResultList();
	}
	
	public ShippingToPR inserShippingToPR(ShippingToPR shippingToPR, Token token) {
		shippingToPR.setCreated(new Date());
		shippingToPR.setIsDelete(0);
		shippingToPR.setUserId(token.getUser().getId());
		super.create(shippingToPR, AuditHelper.OPERATION_CREATE, token);
		return shippingToPR;
	}
	
	public ShippingToPR updateShippingToPR(ShippingToPR shippingToPR, Token token) {
		shippingToPR.setUpdated(new Date());
		shippingToPR.setUserId(token.getUser().getId());
		super.edit(shippingToPR, AuditHelper.OPERATION_UPDATE, token);
		return shippingToPR;
	}
	
	public ShippingToPR deleteShippingToPR(int id, Token token) {
		ShippingToPR shippingToPR = super.find(id);
		shippingToPR.setIsDelete(1);
		shippingToPR.setDeleted(new Date());
		shippingToPR.setUserId(token.getUser().getId());
		super.edit(shippingToPR, AuditHelper.OPERATION_DELETE, token);
		return shippingToPR;
	}
	
	public boolean deleteShippingToPRByPR(int prId, Token token){
		boolean rs = false;
		List<ShippingToPR> shipPR = findShippingByPR(prId);
		for (ShippingToPR shippingToPR : shipPR) {
			deleteShippingToPR(shippingToPR.getId(), token);
			rs = true;
		}
		return rs;
	}
	
	
	public boolean deleteRowShippingToPRByPR(int prId, Token token){
		boolean rs = false;
		List<ShippingToPR> shipPR = findShippingByPR(prId);
		for (ShippingToPR shippingToPR : shipPR) {
			deleteRowShippingToPR(shippingToPR.getId(), token);
			rs = true;
		}
		return rs;
	}
	
	public ShippingToPR deleteRowShippingToPR(int id, Token token) {
		ShippingToPR shippingToPR = super.find(id);
		super.remove(shippingToPR, AuditHelper.OPERATION_ROW_DELETE, token);
		return shippingToPR;
	}
}
