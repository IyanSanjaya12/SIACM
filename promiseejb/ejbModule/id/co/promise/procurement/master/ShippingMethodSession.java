/**
 * fdf
 */
package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.ShippingMethod;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
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
public class ShippingMethodSession extends AbstractFacadeWithAudit<ShippingMethod> {
	
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
	
	public ShippingMethodSession(){
		super(ShippingMethod.class);
	}
	
	public ShippingMethod getShippingMethod(int id) {
		return super.find(id);
	}
	
	public List<ShippingMethod> getShippingMethodList() {
		Query q = em.createNamedQuery("ShippingMethod.find");
		return q.getResultList();
	}

	public ShippingMethod insertShippingMethod(ShippingMethod shippingMethod, Token token) {
		shippingMethod.setCreated(new Date());
		shippingMethod.setIsDelete(0);
		super.create(shippingMethod, AuditHelper.OPERATION_CREATE, token);
		return shippingMethod;
	}
	
	public ShippingMethod updateShippingMethod(ShippingMethod shippingMethod, Token token) {
		shippingMethod.setUpdated(new Date());
		super.edit(shippingMethod, AuditHelper.OPERATION_UPDATE, token);
		return shippingMethod;
	}
	
	public ShippingMethod deleteShippingMethod(int id, Token token) {
		ShippingMethod shippingMethod = super.find(id);
		shippingMethod.setIsDelete(1);
		shippingMethod.setDeleted(new Date());
		super.edit(shippingMethod, AuditHelper.OPERATION_DELETE, token);
		return shippingMethod;
	}
	
	public ShippingMethod deleteRowShippingMethod(int id, Token token) {
		ShippingMethod shippingMethod = super.find(id);
		super.remove(shippingMethod, AuditHelper.OPERATION_ROW_DELETE, token);
		return shippingMethod;
	}

}
