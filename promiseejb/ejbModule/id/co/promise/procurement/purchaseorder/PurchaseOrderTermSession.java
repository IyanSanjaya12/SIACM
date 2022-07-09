/**
 * fdf
 */
package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.entity.PurchaseOrderTerm;
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

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class PurchaseOrderTermSession extends AbstractFacadeWithAudit<PurchaseOrderTerm>{
	
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
	
	public PurchaseOrderTermSession() {
		super(PurchaseOrderTerm.class);
	} 
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrderTerm> getPurchaseOrderByPO(Integer id){
		String stringQuery= "SELECT t1 FROM PurchaseOrderTerm t1 where t1.isDelete = 0 AND t1.purchaseOrder.id = :id ";
		Query query = em.createQuery(stringQuery); 
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	public PurchaseOrderTerm inserPurchaseOrderTerm(PurchaseOrderTerm purchaseOrderTerm, Token token) {
		purchaseOrderTerm.setCreated(new Date());
		purchaseOrderTerm.setIsDelete(0);
		super.create(purchaseOrderTerm, AuditHelper.OPERATION_CREATE, token);
		return purchaseOrderTerm;
	}
	
	public PurchaseOrderTerm updatePurchaseOrderTerm(PurchaseOrderTerm purchaseOrderTerm, Token token) {
		purchaseOrderTerm.setUpdated(new Date());
		super.edit(purchaseOrderTerm, AuditHelper.OPERATION_UPDATE, token);
		return purchaseOrderTerm;
	}
	
	public PurchaseOrderTerm deletePurchaseOrderTerm(int id, Token token) {
		PurchaseOrderTerm purchaseOrderTerm = super.find(id);
		purchaseOrderTerm.setIsDelete(1);
		purchaseOrderTerm.setDeleted(new Date());
		super.edit(purchaseOrderTerm, AuditHelper.OPERATION_DELETE, token);
		return purchaseOrderTerm;
	}
	
	public PurchaseOrderTerm deleteRowPurchaseOrderTerm(int id, Token token) {
		PurchaseOrderTerm purchaseOrderTerm = super.find(id);
		super.remove(purchaseOrderTerm, AuditHelper.OPERATION_ROW_DELETE, token);
		return purchaseOrderTerm;
	}

}
