package id.co.promise.procurement.requestquotation;

import id.co.promise.procurement.entity.RequestQuotationItem;
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
public class RequestQuotationItemSession  extends AbstractFacadeWithAudit<RequestQuotationItem> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public RequestQuotationItemSession() {
		super(RequestQuotationItem.class);
	}

	public RequestQuotationItem insert(RequestQuotationItem object, Token token) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public RequestQuotationItem update(RequestQuotationItem object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public RequestQuotationItem delete(int id, Token token) {
		RequestQuotationItem x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public RequestQuotationItem deleteRow(int id, Token token) {
		RequestQuotationItem x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}

	@Override
	protected EntityManager getEntityManager() {
		return null;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return null;
	}

}
