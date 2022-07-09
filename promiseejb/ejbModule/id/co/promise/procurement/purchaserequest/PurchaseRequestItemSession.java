package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class PurchaseRequestItemSession extends AbstractFacadeWithAudit<PurchaseRequestItem> {

	@PersistenceContext(unitName = "promisePU")
	EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PurchaseRequestItemSession() {
		super(PurchaseRequestItem.class);
	}

	public PurchaseRequestItem getPurchaseRequestItem(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemlist() {
		Query q = em.createNamedQuery("PurchaseRequestItem.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemByPurchaseRequestId(int purchaseRequestId) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPurchaseRequestId");
		q.setParameter("purchaseRequestId", purchaseRequestId);
		return q.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<Integer> groupByPurchaseRequestId(int purchaseRequestId) {
		Query q = em.createNamedQuery("PurchaseRequestItem.groupByPurchaseRequestId");
		q.setParameter("purchaseRequestId", purchaseRequestId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemByPurchaseRequestAndItem(int purchaseRequestId, int itemId) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPurchaseRequestAndItem");
		q.setParameter("purchaseRequestId", purchaseRequestId);
		q.setParameter("itemId", itemId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemByPurchaseRequestAndItemAndVendor(int purchaseRequestId, int itemId, int vendorId) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPurchaseRequestAndItemAndVendor");
		q.setParameter("purchaseRequestId", purchaseRequestId);
		q.setParameter("itemId", itemId);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}

	public PurchaseRequestItem createPurchaseRequestItem(PurchaseRequestItem x, Token token) {
		x.setCreated(new Date());
		x.setIsDelete(0);
		x.setUserId(token.getUser().getId());
		super.create(x, AuditHelper.OPERATION_CREATE, token);
		return x;
	}

	public PurchaseRequestItem updatePurchaseRequestItem(PurchaseRequestItem x, Token token) {
		x.setUpdated(new Date());
		x.setUserId(token.getUser().getId());
		super.edit(x, AuditHelper.OPERATION_UPDATE, token);
		return x;
	}

	public PurchaseRequestItem deletePurchaseRequestItem(int id, Token token) {
		PurchaseRequestItem x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		x.setUserId(token.getUser().getId());
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public PurchaseRequestItem deleteRowPurchaseRequestItem(int id, Token token) {
		PurchaseRequestItem x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItemDTO> getPurchaseRequestItemByPurchaseRequest(PurchaseRequest pr) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPurchaseRequest");
		q.setParameter("purchaserequestId", pr.getId());

		List<PurchaseRequestItem> priList = q.getResultList();
		List<PurchaseRequestItemDTO> priDTOList = new ArrayList<PurchaseRequestItemDTO>();
		for (PurchaseRequestItem pri : priList) {
			priDTOList.add(new PurchaseRequestItemDTO(pri));
		}
		return priDTOList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getByPurchaseRequest(PurchaseRequest pr) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPurchaseRequest");
		q.setParameter("purchaserequestId", pr.getId());

		List<PurchaseRequestItem> priList = q.getResultList();
		
		return priList;
	}
	

	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemByPRGroupByVendor(PurchaseRequest pr) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPRGroupByVendor");
		q.setParameter("purchaseRequestId", pr.getId());

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItemDTO> getPurchaseRequestItemByPRAndVendor(PurchaseRequest pr, Vendor vendor) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPRAndVendor");
		q.setParameter("purchaserequest", pr);
		q.setParameter("vendor", vendor);
		List<PurchaseRequestItem> priList = q.getResultList();
		List<PurchaseRequestItemDTO> priDTOList = new ArrayList<PurchaseRequestItemDTO>();
		for (PurchaseRequestItem pri : priList) {
			priDTOList.add(new PurchaseRequestItemDTO(pri));
		}
		return priDTOList;
	}

	public boolean deletePurchaseRequestItemByPrId(int prId, Token token) {
		boolean rValue = false;
		List<Boolean> tidakAdaJoin = new ArrayList<Boolean>();
		List<PurchaseRequestItem> itemList = getPurchaseRequestItemByPurchaseRequestId(prId);
		for (PurchaseRequestItem purchaseRequestItem : itemList) {
			if(purchaseRequestItem.getPriceafterjoin() != null) {
				tidakAdaJoin.add(false);
			} else {
				tidakAdaJoin.add(true);
			}
			
		}
		
		if(!tidakAdaJoin.contains(rValue)) {
			for (PurchaseRequestItem purchaseRequestItem : itemList) {
				deletePurchaseRequestItem(purchaseRequestItem.getId(), token);
			}
			rValue = true;
		}
		
		return rValue;
	}
	
	public boolean deleteRowPurchaseRequestItemByPrId(int prId, Token token) {
		boolean rValue = false;
		List<PurchaseRequestItem> itemList = getPurchaseRequestItemByPurchaseRequestId(prId);
		for (PurchaseRequestItem purchaseRequestItem : itemList) {
			deleteRowPurchaseRequestItem(purchaseRequestItem.getId(), token);
			rValue = true;
		}
		return rValue;
	}

	private String columnPurchaseRequestItemPaggingList(int index){
		String[] colomnTbl = new String[8];
		colomnTbl[0] = "QUANTITY";
		colomnTbl[1] = "QUANTITY";
		colomnTbl[2] = "T3.NAMA";
		colomnTbl[3] = "QUANTITY";
		colomnTbl[4] = "T4.NAMA";
		colomnTbl[5] = "T2.PRICE";
		colomnTbl[6] = "TOTAL";
		colomnTbl[7] = "TOTAL";
		return colomnTbl[index].toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPurchaseRequestItemPaggingList(
			Integer iDisplayStart, Integer iDisplayLength, String keyword,
			Integer column, String sort) {
		
		String query = 
			"SELECT " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA AS ITEM_NAME, " + 
			 	"SUM(T2.QUANTITY) AS QUANTITY, " + 
			 	"T4.NAMA AS UNIT, " + 
			 	"T2.PRICE, " + 
			 	"SUM(T2.TOTAL) AS TOTAL, " + 
			 	"T3.ITEM_ID " +
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " + 
				"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"INNER JOIN T2_ITEM T3 " + 
				"ON T2.ITEM_ID = T3.ITEM_ID " + 
			"INNER JOIN T1_SATUAN T4 " +  
				"ON T4.SATUAN_ID = T3.SATUAN_ID " + 
			"LEFT JOIN T5_PURCHASEREQUEST_PENGADAAN T5 " + 
				"ON T5.PURCHASE_REQUEST_ID = T1.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status " + 
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) " + 
			"AND (T3.KODE LIKE :keyword OR T3.NAMA LIKE :keyword) " +
			"GROUP BY " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA, " + 
				"T4.NAMA, " + 
			 	"T2.PRICE, " + 
				"T3.ITEM_ID " +
			"ORDER BY " + this.columnPurchaseRequestItemPaggingList(column) + " " + sort;
		
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("status", 7)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List<Object[]> result = (List<Object[]>) q.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPurchaseRequestItemPaggingListForPrConsolidation(
			Integer iDisplayStart, Integer iDisplayLength, String keyword,
			Integer column, String sort) {
		
		String query = 
			"SELECT " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA AS ITEM_NAME, " + 
			 	"SUM(T2.QUANTITY) AS QUANTITY, " + 
			 	"T4.NAMA AS UNIT, " + 
			 	"MIN(T2.PRICE) AS PRICE, " + 
			 	"(SUM(T2.QUANTITY) * MIN(T2.PRICE)) AS TOTAL, " + 
			 	"T3.ITEM_ID " +
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " + 
				"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"INNER JOIN T2_ITEM T3 " + 
				"ON T2.ITEM_ID = T3.ITEM_ID " + 
			"INNER JOIN T1_SATUAN T4 " +  
				"ON T4.SATUAN_ID = T3.SATUAN_ID " + 
			"LEFT JOIN T5_PURCHASEREQUEST_PENGADAAN T5 " + 
				"ON T5.PURCHASE_REQUEST_ID = T1.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status AND T1.IS_JOIN IN(1,2) " + 
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) " + 
			"AND (T3.KODE LIKE :keyword OR T3.NAMA LIKE :keyword) " +
			"AND T2.PR_JOIN_ID IS NULL " +
			"GROUP BY " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA, " + 
				"T4.NAMA, " +  
				"T3.ITEM_ID " +
			"ORDER BY " + this.columnPurchaseRequestItemPaggingList(column) + " " + sort;
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("status", 7)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List<Object[]> result = (List<Object[]>) q.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Integer getPurchaseRequestItemPaggingSize(String keyword) {
		
		String query = 
			"SELECT COUNT(E1.KODE) FROM (SELECT " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA, " + 
			 	"SUM(T2.QUANTITY) AS QUANTITY, " + 
			 	"T4.NAMA AS UNIT, " + 
			 	"T2.PRICE, " + 
			 	"SUM(T2.TOTAL) AS TOTAL " + 
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " + 
				"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"INNER JOIN T2_ITEM T3 " + 
				"ON T2.ITEM_ID = T3.ITEM_ID " + 
			"INNER JOIN T1_SATUAN T4 " +  
				"ON T4.SATUAN_ID = T3.SATUAN_ID " + 
			"LEFT JOIN T5_PURCHASEREQUEST_PENGADAAN T5 " + 
				"ON T5.PURCHASE_REQUEST_ID = T1.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status " + 
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) " + 
			"AND (T3.KODE LIKE :keyword OR T3.NAMA LIKE :keyword) " +
			"GROUP BY " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA, " + 
				"T4.NAMA, " + 
			 	"T2.PRICE, " + 
				"T3.ITEM_ID) E1 ";
		
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("status", 7)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List list = q.getResultList();
		
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		return result;
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	public Integer getPurchaseRequestItemPaggingSizeForPrConsolidation(String keyword) {
		
		String query = 
			"SELECT COUNT(E1.KODE) FROM (SELECT " +
			 	"T3.KODE, " + 
			 	"T3.NAMA AS ITEM_NAME, " + 
			 	"SUM(T2.QUANTITY) AS QUANTITY, " + 
			 	"T4.NAMA AS UNIT, " + 
			 	"MIN(T2.PRICE) AS PRICE, " + 
			 	"(SUM(T2.QUANTITY) * MIN(T2.PRICE)) AS TOTAL, " + 
			 	"T3.ITEM_ID " +
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " + 
				"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"INNER JOIN T2_ITEM T3 " + 
				"ON T2.ITEM_ID = T3.ITEM_ID " + 
			"INNER JOIN T1_SATUAN T4 " +  
				"ON T4.SATUAN_ID = T3.SATUAN_ID " + 
			"LEFT JOIN T5_PURCHASEREQUEST_PENGADAAN T5 " + 
				"ON T5.PURCHASE_REQUEST_ID = T1.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status AND T1.IS_JOIN IN(1,2) " + 
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) " + 
			"AND (T3.KODE LIKE :keyword OR T3.NAMA LIKE :keyword) " +
			"AND T2.PR_JOIN_ID IS NULL " +
			"GROUP BY " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA, " + 
				"T4.NAMA, " +  
				"T3.ITEM_ID) E1 ";
		
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("status", 7)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List list = q.getResultList();
		
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItem(Integer id) {
		String query = 
			"SELECT T2.* " + 
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " +
			"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T1.IS_JOIN IN (1,2) " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status " +
			"AND T2.ITEM_ID = :id " + 
			"AND T2.PR_JOIN_ID IS NULL " +
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) ";
		
		Query q = em.createNativeQuery(query, PurchaseRequestItem.class);
		q.setParameter("isDeleted", 0);
		q.setParameter("id", id);
		q.setParameter("status", 7);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItemAndPrId(Integer id, Integer prId) {
		String query = 
			"SELECT T2.* " + 
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " +
			"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T1.PURCHASE_REQUEST_ID=:prId "+
			"AND T1.IS_JOIN IN :isJoin " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status " +
			"AND T2.ITEM_ID = :id " + 
			"AND T2.PR_JOIN_ID IS NOT NULL " +
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) ";
		
		Query q = em.createNativeQuery(query, PurchaseRequestItem.class);
		q.setParameter("isDeleted", 0);
		q.setParameter("prId", prId);
		q.setParameter("id", id);
		q.setParameter("status", 7);
		List<Integer> isJoinList = new ArrayList<Integer>();
		isJoinList.add(1);//is join enable
		isJoinList.add(2);//was join
		q.setParameter("isJoin", isJoinList);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItemAndPrJoinId(Integer id, Integer prJointId) {
		String query = 
			"SELECT T2.* " + 
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " +
			"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T1.IS_JOIN IN :isJoin " + 
			"AND T2.PR_JOIN_ID = :prJointId "+
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status " +
			"AND T2.ITEM_ID = :id " + 
			"AND T2.PR_JOIN_ID IS NOT NULL " +
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) ";
		
		Query q = em.createNativeQuery(query, PurchaseRequestItem.class);
		q.setParameter("isDeleted", 0);
		q.setParameter("prJointId", prJointId);
		q.setParameter("id", id);
		q.setParameter("status", 7);
		List<Integer> isJoinList = new ArrayList<Integer>();
		isJoinList.add(1);//is join enable
		isJoinList.add(2);//was join
		q.setParameter("isJoin", isJoinList);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedList(Integer id) {
		String query = 
			"SELECT T2.* " + 
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " +
			"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T1.STATUS = :status " +
			"AND T2.ITEM_ID = :id " +
			"AND T2.PR_JOIN_ID IS NOT NULL " +
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) ";
		
		Query q = em.createNativeQuery(query, PurchaseRequestItem.class);
		q.setParameter("isDeleted", 0);
		q.setParameter("id", id);
		q.setParameter("status", 7);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPurchaseRequestItemListForJoinPr(List<Integer> item) {
		String query = 
				"SELECT " + 
				 	"T3.KODE, " + 
				 	"T3.NAMA AS ITEM_NAME, " + 
				 	"SUM(T2.QUANTITY) AS QUANTITY, " + 
				 	"T4.NAMA AS UNIT, " + 
				 	"MIN(T2.PRICE) AS PRICE, " + 
				 	"(SUM(T2.QUANTITY) * MIN(T2.PRICE)) AS TOTAL, " + 
				 	"T3.ITEM_ID " +
				"FROM " + 
					"T3_PURCHASE_REQUEST T1 " + 
				"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " + 
					"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
				"INNER JOIN T2_ITEM T3 " + 
					"ON T2.ITEM_ID = T3.ITEM_ID " + 
				"INNER JOIN T1_SATUAN T4 " +  
					"ON T4.SATUAN_ID = T3.SATUAN_ID " + 
				"LEFT JOIN T5_PURCHASEREQUEST_PENGADAAN T5 " + 
					"ON T5.PURCHASE_REQUEST_ID = T1.PURCHASE_REQUEST_ID " + 
				"WHERE T1.ISDELETE = :isDeleted " + 
				"AND T2.ISDELETE = :isDeleted " + 
				"AND T2.ITEM_ID IN ( :item ) " +
				"AND T1.STATUS = :status AND T1.IS_JOIN IN (1,2) " + 
				"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) " +
				"AND T2.PR_JOIN_ID IS NULL " +
				"GROUP BY " + 
				 	"T3.KODE, " + 
				 	"T3.NAMA, " + 
					"T4.NAMA, " +  
					"T3.ITEM_ID " +
				"ORDER BY T3.NAMA";
			
			Query q  = em.createNativeQuery(query)
					.setParameter("isDeleted", 0)
					.setParameter("status", 7)
					.setParameter("item", item);
			 
			List<Object[]> result = q.getResultList();
			
			return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPurchaseRequestItemPaggingListForJoinPr(
			Integer iDisplayStart, Integer iDisplayLength, String keyword,
			Integer column, String sort, List<Integer> item, String prId) {
		
		String query = 
			"SELECT " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA AS ITEM_NAME, " + 
			 	"SUM(T2.QUANTITY) AS QUANTITY, " + 
			 	"T4.NAMA AS UNIT, " + 
			 	"MIN(T2.PRICE) AS PRICE, " + 
			 	"(SUM(T2.QUANTITY) * MIN(T2.PRICE)) AS TOTAL, " + 
			 	"T3.ITEM_ID " +
			"FROM " + 
				"T3_PURCHASE_REQUEST T1 " + 
			"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " + 
				"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
			"INNER JOIN T2_ITEM T3 " + 
				"ON T2.ITEM_ID = T3.ITEM_ID " + 
			"INNER JOIN T1_SATUAN T4 " +  
				"ON T4.SATUAN_ID = T3.SATUAN_ID " + 
			"LEFT JOIN T5_PURCHASEREQUEST_PENGADAAN T5 " + 
				"ON T5.PURCHASE_REQUEST_ID = T1.PURCHASE_REQUEST_ID " + 
			"WHERE T1.ISDELETE = :isDeleted " + 
			"AND T2.ISDELETE = :isDeleted " + 
			"AND T2.ITEM_ID IN ( :item ) " +
			"AND T1.STATUS = :status AND T1.IS_JOIN IN(1,2) " + 
			"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) " + 
			"AND (T3.KODE LIKE :keyword OR T3.NAMA LIKE :keyword) ";
			if(!prId.equals("")) {
				query += " AND T2.PR_JOIN_ID = "+prId+" ";
			} else {
				query += " AND T2.PR_JOIN_ID IS NULL ";
			}
			query +="GROUP BY " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA, " + 
				"T4.NAMA, " +  
				"T3.ITEM_ID ";
		
		query += "ORDER BY " + this.columnPurchaseRequestItemPaggingList(column) + " " + sort;
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("status", 7)
				.setParameter("item", item)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List<Object[]> result = (List<Object[]>) q.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getPurchaseRequestItemPaggingSizeForJoinPr(String keyword, List<Integer> item, String prId) {
		
		String query = 
				"SELECT COUNT(E1.KODE) FROM (SELECT " +
				 	"T3.KODE, " + 
				 	"T3.NAMA AS ITEM_NAME, " + 
				 	"SUM(T2.QUANTITY) AS QUANTITY, " + 
				 	"T4.NAMA AS UNIT, " + 
				 	"MIN(T2.PRICE) AS PRICE, " + 
				 	"(SUM(T2.QUANTITY) * MIN(T2.PRICE)) AS TOTAL, " + 
				 	"T3.ITEM_ID " +
				"FROM " + 
					"T3_PURCHASE_REQUEST T1 " + 
				"INNER JOIN T4_PURCHASE_REQUEST_ITEM T2 " + 
					"ON T1.PURCHASE_REQUEST_ID = T2.PURCHASE_REQUEST_ID " + 
				"INNER JOIN T2_ITEM T3 " + 
					"ON T2.ITEM_ID = T3.ITEM_ID " + 
				"INNER JOIN T1_SATUAN T4 " +  
					"ON T4.SATUAN_ID = T3.SATUAN_ID " + 
				"LEFT JOIN T5_PURCHASEREQUEST_PENGADAAN T5 " + 
					"ON T5.PURCHASE_REQUEST_ID = T1.PURCHASE_REQUEST_ID " + 
				"WHERE T1.ISDELETE = :isDeleted " + 
				"AND T2.ISDELETE = :isDeleted " + 
				"AND T2.ITEM_ID IN ( :item ) " +
				"AND T1.STATUS = :status AND T1.IS_JOIN IN(1,2) " + 
				"AND T1.PURCHASE_REQUEST_ID NOT IN (SELECT X1.PURCHASE_REQUEST_ID FROM T5_PURCHASEREQUEST_PENGADAAN X1 WHERE X1.ISDELETE = :isDeleted) " + 
				"AND (T3.KODE LIKE :keyword OR T3.NAMA LIKE :keyword) ";
				
		
		if(!prId.equals("")) {
			query += " AND T2.PR_JOIN_ID = "+prId+" ";
		} else {
			query += " AND T2.PR_JOIN_ID IS NULL ";
		}
		
		query += "GROUP BY " + 
			 	"T3.KODE, " + 
			 	"T3.NAMA, " + 
				"T4.NAMA, " +  
				"T3.ITEM_ID) E1 ";
		
		Query q  = em.createNativeQuery(query)
				.setParameter("isDeleted", 0)
				.setParameter("status", 7)
				.setParameter("item", item)
				.setParameter("keyword", "%" + keyword + "%");
		 
		List list = q.getResultList();
		
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequestItem> getPurchaseRequestItemByPurchaseRequestJoinId(int purchaseRequestId) {
		Query q = em.createNamedQuery("PurchaseRequestItem.findByPurchaseRequestJoinId");
		q.setParameter("purchaseRequestId", purchaseRequestId);
		return q.getResultList();
	}
	
	public boolean deletePurchaseRequestItemJoinByPrId(int prId, Token token) {
		boolean rValue = false;
		List<PurchaseRequestItem> itemList = getPurchaseRequestItemByPurchaseRequestJoinId(prId);
		if(itemList != null && itemList.size() > 0) {
			for (PurchaseRequestItem purchaseRequestItem : itemList) {
				purchaseRequestItem.setPriceafterjoin(null);
				purchaseRequestItem.setPurchaserequestjoin(null);
				this.updatePurchaseRequestItem(purchaseRequestItem, token);
				rValue = true;
			}
		}
		return rValue;
	}	
	public Integer getTotalProductSold(Integer catalogId, Integer itemId) {
		String query ="SELECT SUM(prItem.quantity) FROM PurchaseRequestItem prItem "
				+ "WHERE prItem.catalog.id =:catalogId and prItem.item.id =:itemId ";
		
		Query q = em.createQuery(query);
		q.setParameter("catalogId", catalogId);
		q.setParameter("itemId", itemId);
		//Object obj = q.getSingleResult();
		Double qty = (Double) q.getSingleResult();
		Integer value = qty.intValue(); 
	    return value;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getPrForCatalogTransaction() {

		Query q = em.createQuery("SELECT Count (pri.purchaserequest) FROM PurchaseRequestItem pri WHERE pri.isDelete = 0 GROUP BY pri.catalog, pri.vendor, pri.item");

		try {
			return q.getResultList();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Integer getTotalProductSoldToday(Integer catalogId, Integer itemId) {
		String query ="SELECT prItem FROM PurchaseRequestItem prItem "
				+ "WHERE prItem.catalog.id =:catalogId and prItem.item.id =:itemId "
				+ "and prItem.purchaserequest.status != :status";
		
		Query q = em.createQuery(query);
		
		q.setParameter("catalogId", catalogId);
		q.setParameter("itemId", itemId);
		q.setParameter("status", Constant.PR_STATUS_REJECT);
		//Object obj = q.getSingleResult();
		List<PurchaseRequestItem> prItemList =  q.getResultList();
		Double qty = new Double(0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateAfterFormat= formatter.format(new Date());
		for(PurchaseRequestItem prItem : prItemList) {
			String created = formatter.format(prItem.getCreated());
			if(created.equalsIgnoreCase(dateAfterFormat)) {
				qty += prItem.getQuantity();
			}
		}
		Integer value = 0;
		value = qty.intValue(); 			
	    return value;
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
