package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.MataUang;
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
public class ItemTypeSession extends AbstractFacadeWithAudit<ItemType> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public ItemTypeSession() {
		super(ItemType.class);
	}

	public ItemType getItemType(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<ItemType> getItemTypeList() {
		Query q = em.createNamedQuery("ItemType.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ItemType> getItemTypeListByNameEqual(String pNama) {
		Query q = em.createQuery("select it from ItemType it where it.nama = :pNama");
		q.setParameter("pNama", pNama);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaItemType(String nama, String toDo, Integer itemTypeId) {
		Query q = em.createNamedQuery("ItemType.findNama");
		q.setParameter("nama", nama);
		List<ItemType> itemTypeList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (itemTypeList != null && itemTypeList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (itemTypeList != null && itemTypeList.size() > 0) {
				if (itemTypeId.equals(itemTypeList.get(0).getId())) {
					isSave = true;
				} else {
					isSave = false;
				}
			} else {
				isSave = true;
			}
		}

		return isSave;

	}

	public ItemType insertItemType(ItemType itemType, Token token) {
		itemType.setCreated(new Date());
		itemType.setIsDelete(0);
		super.create(itemType, AuditHelper.OPERATION_CREATE, token);
		return itemType;
	}

	public ItemType updateItemType(ItemType itemType, Token token) {
		itemType.setUpdated(new Date());
		super.edit(itemType, AuditHelper.OPERATION_UPDATE, token);
		return itemType;
	}

	public ItemType deleteItemType(int id, Token token) {
		ItemType itemType = super.find(id);
		itemType.setIsDelete(1);
		itemType.setDeleted(new Date());
		super.edit(itemType, AuditHelper.OPERATION_DELETE, token);
		return itemType;
	}

	public ItemType deleteRowItemType(int id, Token token) {
		ItemType itemType = super.find(id);
		super.remove(itemType, AuditHelper.OPERATION_ROW_DELETE, token);
		return itemType;
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