package id.co.promise.procurement.invoicepayment;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.InvoicePayment;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;


@Stateless
@LocalBean
public class InvoicePaymentSession extends AbstractFacadeWithAudit<InvoicePayment>{
	
	public InvoicePaymentSession() {
		super(InvoicePayment.class);
		// TODO Auto-generated constructor stub
	}

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
	

	public InvoicePayment getPaymentMethod(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public InvoicePayment getByNumber(String number) {
		try {
			Query q = em.createNamedQuery("InvoicePayment.findByNumber");
			q.setParameter("number", number);
			
			List<InvoicePayment> invoicePayment = q.getResultList();
			if (invoicePayment != null && invoicePayment.size() > 0) {
				return invoicePayment.get(0);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	public InvoicePayment insertInvoicePayment(InvoicePayment invoicePayment, Token token) {
		invoicePayment.setCreated(new Date());
		super.create(invoicePayment, AuditHelper.OPERATION_CREATE, token);
		return invoicePayment;
	}

	public InvoicePayment updateInvoicePayment(InvoicePayment invoicePayment, Token token) {
		invoicePayment.setUpdated(new Date());
		super.edit(invoicePayment, AuditHelper.OPERATION_UPDATE, token);
		return invoicePayment;
	}
	
	public InvoicePayment cancelInvoicePayment(int id, Token token) {
		InvoicePayment invoicePayment = super.find(id);
		invoicePayment.setCanceledDate(new Date());
		super.edit(invoicePayment, AuditHelper.OPERATION_DELETE, token);
		return invoicePayment;
	}
	
	public InvoicePayment deleteInvoicePayment(int id, Token token) {
		InvoicePayment invoicePayment = super.find(id);
		super.remove(invoicePayment, AuditHelper.OPERATION_ROW_DELETE, token);
		return invoicePayment;
	}

}
