package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.entity.Perencanaan;
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

@Stateless
@LocalBean
public class PerencanaanSession extends AbstractFacadeWithAudit<Perencanaan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PerencanaanSession() {
		super(Perencanaan.class);
	}

	public List<Perencanaan> getList() {
		Query q = em.createNamedQuery("Perencanaan.find");
		return q.getResultList();
	}

	public Perencanaan get(int id) {
		return super.find(id);
	}

	public Perencanaan create(Perencanaan object, Token token) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public Perencanaan update(Perencanaan object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public Perencanaan editDelegasiPengadaan(Perencanaan object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public Perencanaan delete(int id, Token token) {
		Perencanaan x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public Perencanaan deleteRow(int id, Token token) {
		Perencanaan x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

}
