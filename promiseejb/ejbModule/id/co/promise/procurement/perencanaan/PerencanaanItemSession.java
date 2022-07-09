package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.entity.PerencanaanItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class PerencanaanItemSession extends
		AbstractFacadeWithAudit<PerencanaanItem> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	PerencanaanSession perencanaanSession;

	public PerencanaanItemSession() {

		super(PerencanaanItem.class);
	}

	public PerencanaanItem get(int id) {
		return super.find(id);
	}

	public List<PerencanaanItem> getList() {
		Query q = em.createNamedQuery("PerencanaanItem.find");
		return q.getResultList();
	}

	public List<PerencanaanItem> getListPerencanaanItemByPerencanaanAndItemType(
			int perencanaanId, int itemTypeId) {
		Query q = em.createNamedQuery("PerencanaanItem.findByPerencanaan");
		q.setParameter("perencanaanId", perencanaanId);
		q.setParameter("itemTypeId", itemTypeId);
		return q.getResultList();
	}

	public List<PerencanaanItem> getListByPerencanaanAndItem(int perencanaanId,
			int itemId) {
		Query q = em
				.createNamedQuery("PerencanaanItem.findByPerencanaanAndItem");
		q.setParameter("perencanaanId", perencanaanId);
		q.setParameter("itemId", itemId);
		return q.getResultList();
	}

	public PerencanaanItem insertPerencanaanItem(
			PerencanaanItem perencanaanItem, Token token) {
		perencanaanItem.setCreated(new Date());
		perencanaanItem.setIsDelete(0);
		super.create(perencanaanItem, AuditHelper.OPERATION_CREATE, token);
		return perencanaanItem;
	}

	public PerencanaanItem updatePerencanaanItem(
			PerencanaanItem perencanaanItem, Token token) {
		perencanaanItem.setUpdated(new Date());
		super.edit(perencanaanItem, AuditHelper.OPERATION_UPDATE, token);
		return perencanaanItem;
	}

	public PerencanaanItem deletePerencanaanItem(int id, Token token) {
		PerencanaanItem perencanaanItem = super.find(id);
		perencanaanItem.setIsDelete(1);
		perencanaanItem.setDeleted(new Date());
		super.edit(perencanaanItem, AuditHelper.OPERATION_DELETE, token);
		return perencanaanItem;
	}

	public PerencanaanItem deleteRowPerencanaanItem(int id, Token token) {
		PerencanaanItem perencanaanItem = super.find(id);
		super.remove(perencanaanItem, AuditHelper.OPERATION_ROW_DELETE, token);
		return perencanaanItem;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
}
