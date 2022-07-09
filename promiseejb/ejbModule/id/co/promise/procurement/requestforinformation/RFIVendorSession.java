package id.co.promise.procurement.requestforinformation;

import id.co.promise.procurement.entity.RFIVendor;
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
public class RFIVendorSession extends AbstractFacadeWithAudit<RFIVendor> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public RFIVendorSession() {
		super(RFIVendor.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public RFIVendor insert(RFIVendor object, Token token) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public RFIVendor update(RFIVendor object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public RFIVendor delete(int id, Token token) {
		RFIVendor x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public RFIVendor deleteRow(int id, Token token) {
		RFIVendor x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}
}
