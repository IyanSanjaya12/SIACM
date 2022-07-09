package id.co.promise.procurement.requestquotation;

import id.co.promise.procurement.entity.RequestQuotationVendor;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class RequestQuotationVendorSession extends AbstractFacadeWithAudit<RequestQuotationVendor> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public RequestQuotationVendorSession() {
		super(RequestQuotationVendor.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public RequestQuotationVendor insert(RequestQuotationVendor object, Token token) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public RequestQuotationVendor update(RequestQuotationVendor object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public RequestQuotationVendor delete(int id, Token token) {
		RequestQuotationVendor x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public RequestQuotationVendor deleteRow(int id, Token token) {
		RequestQuotationVendor x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}
}
