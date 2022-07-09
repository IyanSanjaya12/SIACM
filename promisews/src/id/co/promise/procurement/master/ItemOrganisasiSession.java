package id.co.promise.procurement.master;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemOrganisasi;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean

public class ItemOrganisasiSession extends AbstractFacadeWithAudit<ItemOrganisasi> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	
	
	public ItemOrganisasiSession() {
		super(ItemOrganisasi.class);
	}

	public ItemOrganisasi getItemOrganisasi(int id) {
		return super.find(id);
	}

	
	public List<ItemOrganisasi> getItemOrganisasiList() {
		Query q = em.createNamedQuery("ItemOrganisasi.find");
		return q.getResultList();
	}

	
	//list item organisasi byId
	public List<ItemOrganisasi> getItemOrganisasiListByItemId(Integer itemId, Integer organsiasiId) {
		Query q = em.createNamedQuery("ItemOrganisasi.findByItemId");
		q.setParameter("itemId", itemId);
		q.setParameter("organsiasiId", organsiasiId);
		return q.getResultList();
	}
	
	public ItemOrganisasi getItemOrganisasiByItemCodeAndOrgCode(String itemCode, String orgCode) {
		List<ItemOrganisasi> itemOrganisasiList = new ArrayList<ItemOrganisasi>();
		Query q = em.createNamedQuery("ItemOrganisasi.findByItemCodeAndOrgCode");
		q.setParameter("itemCode", itemCode);
		q.setParameter("orgCode", orgCode);
		itemOrganisasiList = q.getResultList();
		if(itemOrganisasiList.size() > 0) {
			return itemOrganisasiList.get(0);
		}
		else {
			return null;
		}
		
	}
	
	public List<ItemOrganisasi> getItemOrganisasiListByItemKode(String itemKode) {
		Query q = em.createNamedQuery("ItemOrganisasi.findByItemKode");
		q.setParameter("itemKode", itemKode);
		return q.getResultList();
	}
	
	//insert data
	public ItemOrganisasi insertItemOrganisasi(ItemOrganisasi itemOrganisasi, Token token) {
		itemOrganisasi.setCreated(new Date());
		itemOrganisasi.setIsDelete(0);
		super.create(itemOrganisasi, AuditHelper.OPERATION_CREATE, token);
		return itemOrganisasi;
	}

	
	//update data
	public ItemOrganisasi updateItemOrganisasi(ItemOrganisasi itemOrganisasi, Token token) {
		itemOrganisasi.setUpdated(new Date());
		super.edit(itemOrganisasi, AuditHelper.OPERATION_UPDATE, token);
		return itemOrganisasi;
	}

	
	public ItemOrganisasi deleteItemOrganisasi(int id, Token token) {
		ItemOrganisasi itemOrganisasi = super.find(id);
		itemOrganisasi.setIsDelete(1);
		itemOrganisasi.setDeleted(new Date());
		super.edit(itemOrganisasi, AuditHelper.OPERATION_DELETE, token);
		return itemOrganisasi;
	}

	public ItemOrganisasi deleteRowItemOrganisasi(int id, Token token) {
		ItemOrganisasi itemOrganisasi = super.find(id);
		super.remove(itemOrganisasi, AuditHelper.OPERATION_ROW_DELETE, token);
		return itemOrganisasi;
	}

	
	//delete isDelete = 1	
	public List <ItemOrganisasi> deleteItemOrganisasiByItemId(int itemId) {
		Query q = em.createNamedQuery("ItemOrganisasi.deleteItemOrganisasi");
		q.setParameter("itemId", itemId);
		q.executeUpdate();
		return null;
	}
	

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

}
