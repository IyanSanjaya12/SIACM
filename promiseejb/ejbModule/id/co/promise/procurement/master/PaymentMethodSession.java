/**
 * fdf
 */
package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.PaymentMethod;
import id.co.promise.procurement.entity.ShippingMethod;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class PaymentMethodSession extends AbstractFacadeWithAudit<PaymentMethod>{
	
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
	
	public PaymentMethodSession() {
		super(PaymentMethod.class);
	}

	public PaymentMethod getPaymentMethod(int id) {
		return super.find(id);
	}

	public List<PaymentMethod> getPaymentMethodList() {
		Query q = em.createNamedQuery("PaymentMethod.find");
		return q.getResultList();
	}
	
	public PaymentMethod insertPaymentMethod(PaymentMethod paymentMethod, Token token) {
		paymentMethod.setCreated(new Date());
		paymentMethod.setIsDelete(0);
		super.create(paymentMethod, AuditHelper.OPERATION_CREATE, token);
		return paymentMethod;
	}

	public PaymentMethod updatePaymentMethod(PaymentMethod paymentMethod, Token token) {
		paymentMethod.setUpdated(new Date());
		super.edit(paymentMethod, AuditHelper.OPERATION_UPDATE, token);
		return paymentMethod;
	}
	
	public PaymentMethod deletePaymentMethod(int id, Token token) {
		PaymentMethod paymentMethod = super.find(id);
		paymentMethod.setIsDelete(1);
		paymentMethod.setDeleted(new Date());
		super.edit(paymentMethod, AuditHelper.OPERATION_DELETE, token);
		return paymentMethod;
	}
	
	public PaymentMethod deleteRowPaymentMethod(int id, Token token) {
		PaymentMethod paymentMethod = super.find(id);
		super.remove(paymentMethod, AuditHelper.OPERATION_ROW_DELETE, token);
		return paymentMethod;
	}

}
