package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class ItemSession extends AbstractFacadeWithAudit<Item> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	UserSession userSession;
	@EJB
	TokenSession tokenSession;
	@EJB
	VendorSession vendorSession;

	public ItemSession() {
		super(Item.class);
	}

	public Item getItem(int itemId) {
		return super.find(itemId);
	}

	@SuppressWarnings("unchecked")
	public List<Item> getItemList() {
		Query q = em.createNamedQuery("Item.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Item> getItemByNameList(String byName) {
		Query q = em.createNamedQuery("Item.findByName");
		q.setParameter("byName", "%" + byName.toUpperCase() + "%");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Item> getListByKode(String kode, String token) {
		/* get vendor id */
		Token tempToken = tokenSession.findByToken(token);
		User user=tempToken.getUser();
		Vendor vendor=vendorSession.getVendorByUserId(user.getId());
		
		Query q = em.createNamedQuery("Item.findByKode");
		q.setParameter("kode", "%" + kode.toUpperCase() + "%");q.setParameter("kode", "%" + kode.toUpperCase() + "%");
		q.setParameter("vendor",vendor.getId());
		q.setMaxResults(30);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Item> getListByKontrak(Integer contractId, String token) {
		/* get vendor id */
		Token tempToken = tokenSession.findByToken(token);
		User user=tempToken.getUser();
		Vendor vendor=vendorSession.getVendorByUserId(user.getId());
		
		Query q = em.createNamedQuery("Item.findByKontrak");
		q.setParameter("vendor",vendor.getId());
		q.setParameter("contractId", contractId);
		q.setMaxResults(30);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Item> getItemListByKodeEqual(String kode) {
		Query q = em.createQuery("SELECT item FROM Item item WHERE item.isDelete = 0 AND item.kode = :kode");
		q.setParameter("kode", kode);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Item> getItemListByExpenseAccount() {
		Query q = em.createQuery("SELECT item FROM Item item Join ItemOrganisasi itemOrg ON itemOrg.item.id = item.id WHERE item.isDelete = 0 AND itemOrg.expenseAccount IS NULL");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Item getItemByKode(String kode) {
		Query q = em
				.createQuery("SELECT item FROM Item item WHERE item.isDelete = 0 AND item.kode = :kode");
		q.setParameter("kode", kode);

		List<Item> item = q.getResultList();
		if (item != null && item.size() > 0) {
			return item.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Item getByItemKodeEbs(String kode) {
		Query q = em.createNamedQuery("Item.findByItemKodeEbs");
		q.setParameter("kode", kode);
		List<Item> itemList = q.getResultList();
		if (itemList != null && itemList.size() > 0) {
			return itemList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkKodeItem(String code, String toDo, Integer itemId) {
		Query q = em.createNamedQuery("Item.findByCode");
		q.setParameter("code", code);
		List<Item> itemList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (itemList != null && itemList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
		} else if (toDo.equalsIgnoreCase("update")) {
			if (itemList != null && itemList.size() > 0) {
				Integer itemIdFromData = itemList.get(0).getId();
				if (itemId.equals(itemIdFromData)) {
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

	public Item insertItem(Item item, Token token) {
		item.setCreated(new Date());
		item.setIsDelete(0);
		item.setSource("PROMISE");
		super.create(item, AuditHelper.OPERATION_CREATE, token);
		return item;
	}

	public Item updateItem(Item item, Token token) {
		item.setUpdated(new Date());
		super.edit(item, AuditHelper.OPERATION_UPDATE, token);
		return item;
	}

	public Item deleteItem(int itemId, Token token) {
		Item item = super.find(itemId);
		item.setIsDelete(1);
		item.setDeleted(new Date());
		super.edit(item, AuditHelper.OPERATION_DELETE, token);
		return item;
	}

	public Item deleteRowItem(int itemId, Token token) {
		Item item = super.find(itemId);
		super.remove(item, AuditHelper.OPERATION_ROW_DELETE, token);
		return item;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement
	 *            the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException
	 *                if element is detected null
	 */
	@SuppressWarnings("unchecked")
	public List<Item> getItemByType(int id) {
		Query q = em.createNamedQuery("Item.getItemByType");
		q.setParameter("itemTypeId", id);
		return q.getResultList();
	}
}