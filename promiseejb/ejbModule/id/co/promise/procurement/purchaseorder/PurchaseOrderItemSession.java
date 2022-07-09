/**
 * fdf
 */
package id.co.promise.procurement.purchaseorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class PurchaseOrderItemSession extends AbstractFacadeWithAudit<PurchaseOrderItem> {
	@EJB
	PurchaseOrderSession purchaseOrderSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	OrganisasiSession organisasiSession;
	
	@EJB
	private VendorSession vendorSession;

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public PurchaseOrderItemSession() {
		super(PurchaseOrderItem.class);
	}

	public PurchaseOrderItem getPurchaseOrderItem(Integer id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPurchaseOrderItemList() {
		Query q = em.createNamedQuery("PurchaseOrderItem.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPurchaseOrderItemByNumberPO(String poNumber) {
		Query q = em.createNamedQuery("PurchaseOrderItem.findByNumberPO");
		q.setParameter("poNumber", poNumber);
		List<PurchaseOrderItem> POIList = q.getResultList();

		return POIList;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItemDTO> getPurchaseOrderItemDTOByPoId(int poId) {
		Query q = em.createNamedQuery("PurchaseOrderItem.findByPOId");
		q.setParameter("purchaseOrderId", poId);
		List<PurchaseOrderItem> ll = q.getResultList();
		List<PurchaseOrderItemDTO> lpoi = new ArrayList<PurchaseOrderItemDTO>();
		for (PurchaseOrderItem purchaseOrderItem : ll) {
			lpoi.add(new PurchaseOrderItemDTO(purchaseOrderItem));
		}
		return lpoi;
	}

	public PurchaseOrderItem insertPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem, Token token) {
		purchaseOrderItem.setCreated(new Date());
		purchaseOrderItem.setIsDelete(0);
		super.create(purchaseOrderItem, AuditHelper.OPERATION_CREATE, token);
		return purchaseOrderItem;
	}

	public PurchaseOrderItem updatePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem, Token token) {
		purchaseOrderItem.setUpdated(new Date());
		super.edit(purchaseOrderItem, AuditHelper.OPERATION_UPDATE, token);
		return purchaseOrderItem;
	}

	public PurchaseOrderItem deletePurchaseOrderItem(int id, Token token) {
		PurchaseOrderItem purchaseOrderItem = super.find(id);
		purchaseOrderItem.setIsDelete(1);
		purchaseOrderItem.setDeleted(new Date());
		super.edit(purchaseOrderItem, AuditHelper.OPERATION_DELETE, token);
		return purchaseOrderItem;
	}

	public PurchaseOrderItem deleteRowPurchaseOrderItem(int id, Token token) {
		PurchaseOrderItem purchaseOrderItem = super.find(id);
		super.remove(purchaseOrderItem, AuditHelper.OPERATION_ROW_DELETE, token);
		return purchaseOrderItem;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPurchaseOrderItemByVendorName(String vendorNm) {
		String stringQuery = "SELECT t1 FROM PurchaseOrderItem t1 where UPPER(t1.vendorName) like :keyword ";
		Query query = em.createQuery(stringQuery);
		if (vendorNm == null) {
			vendorNm = "";
		}
		query.setParameter("keyword", "%" + vendorNm.toUpperCase() + "%");
		List<PurchaseOrderItem> list = query.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPurchaseOrderItemBItemName(String itemNm) {
		String stringQuery = "SELECT t1 FROM PurchaseOrderItem t1 where UPPER(t1.item.nama) like :keyword ";
		Query query = em.createQuery(stringQuery);
		if (itemNm == null) {
			itemNm = "";
		}
		query.setParameter("keyword", "%" + itemNm.toUpperCase() + "%");
		List<PurchaseOrderItem> list = query.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPurchaseOrderItemByPoIdItemId(Integer purchaseOrderId, Integer itemId) {
		List<PurchaseOrderItem> list = new ArrayList<PurchaseOrderItem>();
		String stringQuery = "SELECT t1 FROM PurchaseOrderItem t1 where t1.purchaseOrder.id = :purchaseOrderId AND t1.item.id = :itemId AND t1.isDelete = 0 ";
		Query query = em.createQuery(stringQuery);
		query.setParameter("purchaseOrderId", purchaseOrderId);
		query.setParameter("itemId", itemId);
		list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPurchaseOrderItemByPoId(Integer purchaseOrderId) {
		List<PurchaseOrderItem> list = new ArrayList<PurchaseOrderItem>();
		String stringQuery = "SELECT t1 FROM PurchaseOrderItem t1 where t1.purchaseOrder.id = :purchaseOrderId  AND t1.isDelete = 0 ";
		Query query = em.createQuery(stringQuery);
		query.setParameter("purchaseOrderId", purchaseOrderId);
		list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPurchaseOrderItemListByShipping(Integer id) {
		String stringQuery = "SELECT t1 FROM PurchaseOrderItem t1 where t1.shippingTo.id = :id AND t1.isDelete = :isDelete ";
		Query query = em.createQuery(stringQuery);
		query.setParameter("id", id);
		query.setParameter("isDelete", 0);
		List<PurchaseOrderItem> list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> findPurchaseOrderItemByPO(Integer poId) {
		Query q = em.createNamedQuery("PurchaseOrderItem.findByPOId");
		q.setParameter("purchaseOrderId", poId);
		return q.getResultList();
	}

	@SuppressWarnings({ "unchecked" })
	public List<PurchaseOrderItem> getListPagination(String noPo, String vendorName, Date startDate, Date endDate, String orderKeyword,
			Integer pageNo, Integer pageSize, String token) {
		
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		Vendor vendor = vendorSession.getVendorByUserId(user.getId());
		List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
		List<Integer> organisasiNewList = new ArrayList<Integer>();
		for(Organisasi org : organisasiList) {
			organisasiNewList.add(org.getId());
		}
		
		String query = "SELECT poi FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 "; 
		noPo = noPo == null ? "" : noPo.trim(); vendorName = vendorName == null ||
		vendorName.equals("undefined") || vendorName.equals("") ? null : vendorName;
		  // KAI 20201221 [19451]

		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and poi.vendor.id =:vendorId ";
		} else {
			query = query + "and poi.purchaseRequestItem.purchaserequest.organisasi.id in (:organisasiNewList) ";//KAI - 20201214 - [19451] 
		}
		
		if (!noPo.equalsIgnoreCase("")) {
			query = query + "and (poi.purchaseOrder.poNumber like :noPo) ";
		}
		if (vendorName != null) { 
			query = query + "and poi.vendorName = :vendorName "; 
		}

		if (startDate != null && endDate != null) {
			query += "AND poi.purchaseOrder.purchaseOrderDate >= :startDate and poi.purchaseOrder.purchaseOrderDate <= :endDate ";
		}

		if (!orderKeyword.isEmpty()) {
			query = query + "order by poi." + orderKeyword;
		}

		Query q = em.createQuery(query); 
		if (!noPo.equalsIgnoreCase("")) {
			q.setParameter("noPo", "%" + noPo + "%"); // KAI 20201221 [19451]
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			q.setParameter("vendorId", vendor.getId());
		} else {
			q.setParameter("organisasiNewList", organisasiNewList);
		}
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.getResultList();
	}

	public Long getTotalList(String noPo, String vendorName, Date startDate, Date endDate, String orderKeyword,
			Integer pageNo, Integer pageSize, String token) {
		
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		Vendor vendor = vendorSession.getVendorByUserId(user.getId());
		List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
		List<Integer> organisasiNewList = new ArrayList<Integer>();
		for(Organisasi org : organisasiList) {
			organisasiNewList.add(org.getId());
		}
		
		String query = "SELECT count (poi) FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 ";
		/* 
		 * noPo = noPo == null ? "" : noPo.trim(); vendorName = vendorName == null ||
		 * vendorName == "undefined" || vendorName == "" ? null : vendorName;
		 */ // KAI 20201221 [19451]

		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and poi.vendor.id =:vendorId ";
		} else {
			query = query + "and poi.purchaseRequestItem.purchaserequest.organisasi.id in (:organisasiNewList) ";//KAI - 20201214 - [19451] 
		}
		
		if (!noPo.equalsIgnoreCase("")) {
			query = query + "and (poi.purchaseOrder.poNumber like :noPo) ";
		}
		if (!vendorName.isEmpty()) { 
			query = query + "and poi.vendorName = :vendorName "; 
		}

		if (startDate != null && endDate != null) {
			query += "AND poi.purchaseOrder.purchaseOrderDate >= :startDate and poi.purchaseOrder.purchaseOrderDate <= :endDate ";
		}

		if (!orderKeyword.isEmpty()) {
			query = query + "order by poi." + orderKeyword;
		}

		Query q = em.createQuery(query); 
		if (!noPo.equalsIgnoreCase("")) {
			q.setParameter("noPo", "%" + noPo + "%"); // KAI 20201221 [19451]
		}
		if (!vendorName.isEmpty()) { 
			q.setParameter("vendorName", vendorName); 
		} 
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			q.setParameter("vendorId", vendor.getId());
		} else {
			q.setParameter("organisasiNewList", organisasiNewList);
		}

		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPoItemJoinCatalogKontrak(Integer purchaseOrderId) {
		List<PurchaseOrderItem> list = new ArrayList<PurchaseOrderItem>();
		String stringQuery = "SELECT poi, catalog FROM PurchaseOrderItem poi, Catalog catalog where poi.isDelete = 0 "
				+ "AND poi.purchaseRequestItem.catalog = catalog.id "
				+ "AND poi.purchaseOrder.id = :purchaseOrderId";
		Query query = em.createQuery(stringQuery);
		query.setParameter("purchaseOrderId", purchaseOrderId);
		list = query.getResultList();
		return list;
	}
	
	// KAI 20201223 [19451]
	@SuppressWarnings({ "unchecked" })
	public List<PurchaseOrderItem> getExel(String noPo, String vendorName, Date startDate, Date endDate, String orderKeyword,
			Integer pageNo, Integer pageSize, String token) { 
		
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		Vendor vendor = vendorSession.getVendorByUserId(user.getId());
		List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
		List<Integer> organisasiNewList = new ArrayList<Integer>();
		for(Organisasi org : organisasiList) {
			organisasiNewList.add(org.getId());
		}
		
		String query = "SELECT poi FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 "; 
		noPo = noPo == null ? "" : noPo.trim(); vendorName = vendorName == null ||
		vendorName.equals("undefined") || vendorName.equals("") ? null : vendorName;
		  // KAI 20201221 [19451]

		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and poi.vendor.id =:vendorId ";
		} else {
			query = query + "and poi.purchaseRequestItem.purchaserequest.organisasi.id in (:organisasiNewList) ";//KAI - 20201214 - [19451] 
		}
		
		if (!noPo.equalsIgnoreCase("")) {
			query = query + "and (poi.purchaseOrder.poNumber like :noPo) ";
		}
		if (vendorName != null) { 
			query = query + "and poi.vendorName = :vendorName "; 
		}

		if (startDate != null && endDate != null) {
			query += "AND poi.purchaseOrder.purchaseOrderDate >= :startDate and poi.purchaseOrder.purchaseOrderDate <= :endDate ";
		}

		if (!orderKeyword.isEmpty()) {
			query = query + "order by poi." + orderKeyword;
		}

		Query q = em.createQuery(query); 
		if (!noPo.equalsIgnoreCase("")) {
			q.setParameter("noPo", "%" + noPo + "%"); // KAI 20201221 [19451]
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			q.setParameter("vendorId", vendor.getId());
		} else {
			q.setParameter("organisasiNewList", organisasiNewList);
		}
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> getPoItemByPrItemId(Integer prItemId) {
		Query query = em.createQuery("select poItem from PurchaseOrderItem poItem where poItem.isDelete = 0 and poItem.purchaseRequestItem.id = :prItemId")
				.setParameter("prItemId", prItemId);
		return query.getResultList();
		
	}

}
