package id.co.promise.procurement.requestforinformation;

import id.co.promise.procurement.entity.RFIItem;
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
public class RFIItemSession  extends AbstractFacadeWithAudit<RFIItem> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public RFIItemSession() {
		super(RFIItem.class);
	}

	public RFIItem insert(RFIItem object, Token token) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public RFIItem update(RFIItem object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public RFIItem delete(int id, Token token) {
		RFIItem x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public RFIItem deleteRow(int id, Token token) {
		RFIItem x = super.find(id);
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
