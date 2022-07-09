package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.ItemGroup;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
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
public class ItemGroupSession extends AbstractFacadeWithAudit<ItemGroup> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public ItemGroupSession() {
		super(ItemGroup.class);
	}

	public ItemGroup getItemGroup(int id) {
		return super.find(id);
	}

	public List<ItemGroup> getItemGroupList() {
		Query q = em.createNamedQuery("ItemGroup.find");
		return q.getResultList();
	}

	public ItemGroup insertItemGroup(ItemGroup itemGroup, Token token) {
		itemGroup.setCreated(new Date());
		itemGroup.setIsDelete(0);
		super.create(itemGroup, AuditHelper.OPERATION_CREATE, token);
		return itemGroup;
	}

	public ItemGroup updateItemGroup(ItemGroup itemGroup, Token token) {
		itemGroup.setUpdated(new Date());
		super.edit(itemGroup, AuditHelper.OPERATION_UPDATE, token);
		return itemGroup;
	}

	public ItemGroup deleteItemGroup(int id, Token token) {
		ItemGroup itemGroup = super.find(id);
		itemGroup.setIsDelete(1);
		itemGroup.setDeleted(new Date());
		super.edit(itemGroup, AuditHelper.OPERATION_DELETE, token);
		return itemGroup;
	}

	public ItemGroup deleteRowItemGroup(int id, Token token) {
		ItemGroup itemGroup = super.find(id);
		super.remove(itemGroup, AuditHelper.OPERATION_ROW_DELETE, token);
		return itemGroup;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public Boolean hasChildren(List<ItemGroup> listItemGroup, int id) {
		for (ItemGroup x : listItemGroup) {
			if (x.getParentId() == id) {
				return true;
			}
		}
		return false;
	}

	public String parentItemGroupStruktur(List<ItemGroup> listItemGroup, int id) {
		String str = "[";
		int indeks = 0;
		for (ItemGroup x : listItemGroup) {
			if (x.getIsDelete() == 0) {
				if (x.getParentId() == id) {
					if (indeks > 0) {
						str += ",";
					}
					str += "{\"id\":" + x.getId() + ",\"label\":\""
							+ x.getNama() + "\",\"deskripsi\":\""
							+ x.getDeskripsi() + "\",\"parentId\":\""
							+ x.getParentId() + "\",\"created\":\""
							+ x.getCreated() + "\"";
					if (hasChildren(listItemGroup, x.getId())) {
						str += ",\"children\":";
						str += parentItemGroupStruktur(listItemGroup, x.getId());
					}
					str += "}";
					indeks++;
				}
			}
		}
		str += "]";
		return str;
	}

	public String itemGroupGetAll() {
		String str = "{\"data\":";
		str += parentItemGroupStruktur(findAll(), 0);
		str += "}";
		return str;
	}
	
	@SuppressWarnings("unchecked")
	public ItemGroup getItemGroupByNama(String nama) {
		Query q = em.createQuery("SELECT itemGroup FROM ItemGroup itemGroup WHERE itemGroup.isDelete = 0 AND itemGroup.nama = :nama");
		q.setParameter("nama", nama);
		
		List<ItemGroup> itemGroup = q.getResultList();
		if (itemGroup != null && itemGroup.size() > 0) {
			return itemGroup.get(0);
		}
		
		return null;
	}
	
	public Boolean checkNamaItemGroup(String nama, String toDo, Integer itemGroupId) {
		  Query q = em.createNamedQuery("ItemGroup.findNama");
		  q.setParameter("nama", nama);
		  List<ItemGroup> itemGroup = q.getResultList();
		  
		  Boolean isSave = false;
		  if(toDo.equalsIgnoreCase("insert")) {
		   if(itemGroup != null && itemGroup.size() > 0) {
		    isSave = false;
		   } else {
		    isSave = true;
		   }
		   
		  } else if (toDo.equalsIgnoreCase("update")) {
		   if(itemGroup != null && itemGroup.size() > 0) {
		    if(itemGroupId.equals(itemGroup.get(0).getId())) {
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

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
}
