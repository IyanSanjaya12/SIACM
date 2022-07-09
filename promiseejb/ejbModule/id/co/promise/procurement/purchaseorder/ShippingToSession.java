/**
 * fdf
 */
package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.entity.ShippingTo;
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
public class ShippingToSession extends AbstractFacadeWithAudit<ShippingTo>{
	
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
	
	public ShippingToSession() {
		super(ShippingTo.class);
	}
	
	public ShippingTo getShippingTo(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<ShippingTo> getShippingToList() {
		Query q = em.createNamedQuery("ShippingTo.find");
		return q.getResultList();
	}
	
	public ShippingTo inserShippingTo(ShippingTo shippingTo, Token token) {
		shippingTo.setCreated(new Date());
		shippingTo.setIsDelete(0);
		super.create(shippingTo, AuditHelper.OPERATION_CREATE, token);
		return shippingTo;
	}
	
	public ShippingTo updateShippingTo(ShippingTo shippingTo, Token token) {
		shippingTo.setUpdated(new Date());
		super.edit(shippingTo, AuditHelper.OPERATION_UPDATE, token);
		return shippingTo;
	}
	
	public ShippingTo deleteShippingTo(int id, Token token) {
		ShippingTo shippingTo = super.find(id);
		shippingTo.setIsDelete(1);
		shippingTo.setDeleted(new Date());
		super.edit(shippingTo, AuditHelper.OPERATION_DELETE, token);
		return shippingTo;
	}
	
	public ShippingTo deleteRowShippingTo(int id, Token token) {
		ShippingTo shippingTo = super.find(id);
		super.remove(shippingTo, AuditHelper.OPERATION_ROW_DELETE, token);
		return shippingTo;
	}
	
	@SuppressWarnings("unchecked")
	public List<ShippingTo> findShippingByPO(int id){
		Query q = em.createQuery("SELECT s "
				+ "FROM ShippingTo s " 
				+ "WHERE s.purchaseOrder.id = :id "
				+ "AND s.isDelete = :isDelete "
				+ "ORDER BY s.id ASC");
		q.setParameter("id", id);
		q.setParameter("isDelete", 0);
		return q.getResultList();
	}

}
