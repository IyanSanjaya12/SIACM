package id.co.promise.procurement.purchaserequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.dashboard.DashboardPerencanaanDTO;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedLogSession;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedSession;
import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.PurchaseRequestPengadaan;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.inisialisasi.PurchaseRequestPengadaanSession;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.AfcoSession;
import id.co.promise.procurement.master.AutoNumberSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class PurchaseRequestSession extends AbstractFacadeWithAudit<PurchaseRequest> {

	@EJB
	PurchaseRequestItemSession purchaseRequestItemSession;

	@EJB
	ShippingToPRSession shippingToPRSession;

	@EJB
	AfcoSession afcoSesssion;

	@EJB
	ItemSession itemSession;

	@EJB
	MataUangSession mataUangSession;

	@EJB
	AddressBookSession addressBookSession;

	@EJB
	TahapanSession tahapanSession;

	@EJB
	ApprovalSession approvalSession;

	@EJB
	ApprovalLevelSession approvalLevelSession;

	@EJB
	ApprovalProcessSession approvalProcessSession;

	@EJB
	ApprovalProcessTypeSession approvalProcessTypeSession;

	@EJB
	private PurchaseRequestPengadaanSession prPengadaanSession;

	@EJB
	VendorSession vendorSession;

	@EJB
	AutoNumberSession autoNumberSession;

	@EJB
	private OrganisasiSession organisasiSession;

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	RoleUserSession roleUserSession;

	@EJB
	DeliveryReceivedLogSession deliveryReceivedLogSession;

	@EJB
	private SyncSession syncSession;

	@EJB
	PurchaseOrderSession purchaseOrderSession;

	@EJB
	PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB
	SatuanSession satuanSession;

	@EJB
	DeliveryReceivedSession deliveryReceivedSession;

	@EJB
	UserSession userSession;

	public PurchaseRequestSession() {
		super(PurchaseRequest.class);
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getList() {
		Query q = em.createNamedQuery("PurchaseRequest.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getPRListByStatusList(List<Integer> statusList) {
		Query q = em.createNamedQuery("PurchaseRequest.getPRListByStatusList");
		q.setParameter("statusList", statusList);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getPRListByStatusListAndHavePrNumber(List<Integer> statusList) {
		Query q = em.createNamedQuery("PurchaseRequest.getPRListByStatusListAndHavePrNumber");
		q.setParameter("statusList", statusList);
		return q.getResultList();
	}

	/*
	 * @SuppressWarnings("unchecked") public Boolean
	 * checkPurchaseRequestDeliveryStatus(Integer prId, String toDo, Integer
	 * currentdr) { Boolean valid = true; List<PurchaseOrder> purchaseOrderList =
	 * purchaseOrderSession.getPOByPRId(prId); int POICount = 0; int DRCount = 0;
	 * int index = 0; for (PurchaseOrder purchaseOrder : purchaseOrderList) {
	 * List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession
	 * .getPurchaseOrderItemByPoId(purchaseOrder.getId()); List<DeliveryReceived>
	 * deliveryReceivedList = deliveryReceivedSession
	 * .getDeliveryReceivedByPoId(purchaseOrder.getId()); POICount = POICount +
	 * purchaseOrderItemList.size(); DRCount = DRCount +
	 * deliveryReceivedList.size();
	 * 
	 * for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
	 * List<DeliveryReceived> deliveryReceivedListTemp = deliveryReceivedSession
	 * .getDeliveryReceivedByPoIdItemId(purchaseOrder.getId(),
	 * purchaseOrderItem.getId()); DeliveryReceived deliveryReceived = null; if
	 * (deliveryReceivedListTemp.size() > 0) { deliveryReceived =
	 * deliveryReceivedListTemp.get(0); }
	 * 
	 * if (deliveryReceived != null) { Double jmllog = null;
	 * 
	 * if(deliveryReceived.getId().equals(currentdr)){ if(toDo=="insert"){ jmllog=
	 * (double) 0; }else{
	 * jmllog=deliveryReceivedLogSession.getTotalPassItemLog(deliveryReceived.getId(
	 * )).doubleValue(); }
	 * 
	 * }else{
	 * 
	 * jmllog =
	 * deliveryReceivedLogSession.getTotalPassItemLog(deliveryReceived.getId()).
	 * doubleValue(); // }
	 * 
	 * if (deliveryReceived.getId().equals(currentdr)) { if
	 * (purchaseOrderItem.getQuantitySend() .doubleValue() !=
	 * deliveryReceived.getPass().doubleValue() + jmllog) { valid = false; break; }
	 * } else { if (purchaseOrderItem.getQuantitySend().doubleValue() != jmllog) {
	 * valid = false; break; } }
	 * 
	 * } }
	 * 
	 * } if (toDo == "insert") { if (DRCount != POICount - 1) { valid = false; } }
	 * else { if (DRCount != POICount) { valid = false; } }
	 * 
	 * return valid; }
	 */

	public boolean comparePrNumber(String prNumber, Token token) {
		boolean isvalid = true;
		List<PurchaseRequest> listPrNumber = getListByPRNumber(prNumber);

		for (PurchaseRequest pr : listPrNumber) {
			if (pr.getPrnumber().equalsIgnoreCase(prNumber)) {
				isvalid = false;
			}
		}

		return isvalid;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getListByPRNumber(String prNumber) {
		Query q = em.createNamedQuery("PurchaseRequest.findByPRNumber");
		q.setParameter("prnumber", "%" + prNumber + "%");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getListByEqualPRNumber(String prNumber) {
		Query q = em.createQuery("SELECT a FROM PurchaseRequest a WHERE  a.isDelete = 0 AND a.prnumber = :prnumber ");
		q.setParameter("prnumber", prNumber);
		return q.getResultList();
	}

	public PurchaseRequest get(int id) {
		return super.find(id);
	}

	public PurchaseRequest getAfcoByUserId(int id) {
		Query q = em.createQuery("SELECT");
		return super.find(id);
	}
	
	public Integer getTransaction(Integer catalogId, Integer itemId) {
		String query ="SELECT COUNT(prItem) FROM PurchaseRequestItem prItem "
				+ "WHERE prItem.catalog.id =:catalogId and prItem.item.id =:itemId ";
		
		Query q = em.createQuery(query);
		q.setParameter("catalogId", catalogId);
		q.setParameter("itemId", itemId);
		Object obj = q.getSingleResult();
	    return obj != null ? Integer.parseInt(obj.toString()) : 0;
	}
	
	public PurchaseRequest insert(PurchaseRequest object, Token token) {
		object.setCreated(new Date());
		object.setPostdate(object.getCreated());
		object.setIsDelete(0);
		object.setUserId(token.getUser().getId());
		// object.setPrnumber(getPrNumber(object, "PR", token));
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public PurchaseRequest update(PurchaseRequest object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public PurchaseRequest editDelegasiPengadaan(PurchaseRequest object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public PurchaseRequest delete(int id, Token token) {
		PurchaseRequest x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public PurchaseRequest deleteRow(int id, Token token) {
		PurchaseRequest x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequestListPagination getListPRinRange(Integer startIndex, Integer endIndex) {
		Query q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0 ");
		int totalData = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT o FROM PurchaseRequest o WHERE o.isDelete = 0  ");
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PurchaseRequest> listPurchaseRequest = q.getResultList();
		List<PurchaseRequestDTO> listPurchaseRequestDTOs = new ArrayList<PurchaseRequestDTO>();
		for (PurchaseRequest pr : listPurchaseRequest) {
			listPurchaseRequestDTOs.add(new PurchaseRequestDTO(pr));
		}
		return new PurchaseRequestListPagination(totalData, 0, 0, 0, 0, 0, startIndex, endIndex, listPurchaseRequestDTOs);
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequestListPagination getListPRinRange(Integer startIndex, Integer endIndex, Integer joinStatus, Token token) {
		// all count
		String clauseCount = "SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0 AND o.userId=:userId ";
		String clause = "SELECT o FROM PurchaseRequest o WHERE o.isDelete = 0 AND o.userId=:userId ";
		if (joinStatus != null) {
			clause += "AND o.isJoin=:joinStatus ";
			clauseCount += "AND o.isJoin=:joinStatus ";
		}
		

		Query q = em.createQuery(clauseCount);
		q.setParameter("userId", token.getUser().getId());
		if (joinStatus != null) {
			q.setParameter("joinStatus", joinStatus);
		}
		
		int totalData = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery(clause + " ORDER BY o.id DESC ");
		q.setParameter("userId", token.getUser().getId());
		if (joinStatus != null) {
			q.setParameter("joinStatus", joinStatus);
		}
		
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);

		List<PurchaseRequest> listPurchaseRequest = q.getResultList();
		List<PurchaseRequestDTO> listPurchaseRequestDTOs = new ArrayList<PurchaseRequestDTO>();
		for (PurchaseRequest pr : listPurchaseRequest) {
			listPurchaseRequestDTOs.add(new PurchaseRequestDTO(pr));
		}

		q = em.createQuery(clause);
		q.setParameter("userId", token.getUser().getId());
		if (joinStatus != null) {
			q.setParameter("joinStatus", joinStatus);
		}
		int totalItems = q.getResultList().size();

		return new PurchaseRequestListPagination(totalData, 0, 0, 0, 0, totalItems, startIndex, endIndex, listPurchaseRequestDTOs);
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequestListPagination getListPRinRangeByPRNumber(String search, Integer startIndex, Integer endIndex, Integer joinStatus,
			Token token) {
		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();
		// where
		JsonArray jarray = jobject.getAsJsonArray("filter");
		List<String> whereSearch = new ArrayList<String>();
		String whereStatus = "";
		int indexOr = 0;
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();

			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined")) {
				fixValue = "";
			}
			if (joWhere.get("key").toString().replaceAll("\"", "").equalsIgnoreCase("status")) {
				if (Integer.parseInt(fixValue) == 3) {
					whereStatus = " AND o." + joWhere.get("key").toString().replaceAll("\"", "") + "  IN(" + fixValue + ",4) ";
				} else {
					whereStatus = " AND o." + joWhere.get("key").toString().replaceAll("\"", "") + "  IN(" + fixValue + ") ";
				}
			} else {
				String whereClauseTemp = " o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%' ";
				whereSearch.add(whereClauseTemp);
			}
		}

		// where clause
		String where = "";
		if (whereSearch.size() > 0) {
			where = " AND(";
			for (int i = 0; i < whereSearch.size(); i++) {
				if (i > 0) {
					where += " OR " + whereSearch.get(i);
				} else {
					where += " " + whereSearch.get(i);
				}
			}
			where += ") ";
		}

		// add status
		where += whereStatus;

		// add where user id
		String userId = Integer.toString(token.getUser().getId());
		String whereId = " AND o.userId=" + userId;

		where += whereId;

		String sort = " ";
		try {
			sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "") + " DESC";
		} catch (Exception e) {
		}

		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(Integer.parseInt(userId));

		String clauseCount = "SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0" + where + " AND o.organisasi.id=:afcoId";
		String clause = "SELECT o FROM PurchaseRequest o WHERE o.isDelete = 0" + where + " AND o.organisasi.id=:afcoId";

		if (joinStatus != null) {
			clause += " AND o.isJoin=:joinStatus ";
			clauseCount += " AND o.isJoin=:joinStatus ";
		}
		
		Query q = em.createQuery(clauseCount);
		q.setParameter("afcoId", afcoOrganisasi.getId());

		if (joinStatus != null) {
			q.setParameter("joinStatus", joinStatus);
		}
		int totalData = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery(clause + sort);

		q.setParameter("afcoId", afcoOrganisasi.getId());
		if (joinStatus != null) {
			q.setParameter("joinStatus", joinStatus);
		}
		
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PurchaseRequest> listPurchaseRequest = q.getResultList();

		List<PurchaseRequestDTO> lpr = new ArrayList<PurchaseRequestDTO>();
		for (PurchaseRequest pr : listPurchaseRequest) {
			lpr.add(new PurchaseRequestDTO(pr));
		}

		q = em.createQuery(clause);
		q.setParameter("afcoId", afcoOrganisasi.getId());

		if (joinStatus != null) {
			q.setParameter("joinStatus", joinStatus);
		}
		int totalItems = q.getResultList().size();

		return new PurchaseRequestListPagination(totalData, 0, 0, 0, 0, totalItems, startIndex, endIndex, lpr);
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequestListPagination getListPRVerificationinRange(Integer startIndex, Integer endIndex, Token token) {

		int userId = token.getUser().getId();
		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(userId);
		Query q = em.createQuery(
				"SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0 and o.status <> 3 and o.status <> 9 and o.status <> 2 and o.organisasi.id=:afcoId AND o.prnumber not like 'PR-Join%'");
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numAllstatus = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0 AND o.status = 8 and  o.organisasi.id=:afcoId "); // numNeedVerification
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numNeedVerification = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0  and o.status =  7 and  o.organisasi.id=:afcoId "); // numProcurementProcess
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numProcurementProcess = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0  and o.status =  5 and  o.organisasi.id=:afcoId "); // numOnProcess
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numOnProcess = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0  and o.status =  6 and  o.organisasi.id=:afcoId "); // numReceived
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numReceived = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery(
				"SELECT o FROM PurchaseRequest o WHERE o.isDelete = 0  and o.status <> 3 and o.status <> 9 and o.status <> 2 and o.organisasi.id=:afcoId ");// 8
		// need
		// verification
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);

		List<PurchaseRequest> listPurchaseRequest = q.getResultList();

		List<PurchaseRequestDTO> listPurchaseRequestDTOs = new ArrayList<PurchaseRequestDTO>();
		for (PurchaseRequest pr : listPurchaseRequest) {
			PurchaseRequestDTO newObj = new PurchaseRequestDTO(pr);
			// check sudah dibuat pengadaan atau belum
			List<PurchaseRequestPengadaan> prPengadaanList = prPengadaanSession.getListByPerencanaanId(pr.getId());
			if (prPengadaanList.size() > 0) {
				newObj.setPengadaanId(prPengadaanList.get(0).getPengadaan().getId());
			} else {
				newObj.setPengadaanId(0); // belum dibuat pengadaan
			}
			listPurchaseRequestDTOs.add(newObj);
		}

		q = em.createQuery(
				"SELECT o FROM PurchaseRequest o WHERE o.isDelete = 0 and o.status <> 3 and o.status <> 9 and o.status <> 2 and o.organisasi.id=:afcoId");// 8
		// need
		// verification
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int totalItems = q.getResultList().size();

		return new PurchaseRequestListPagination(numAllstatus, numNeedVerification, numProcurementProcess, numOnProcess, numReceived,
				totalItems, startIndex, endIndex, listPurchaseRequestDTOs);
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequestListPagination getListPRVerificationinRangeByPRNumber(String search, Integer startIndex, Integer endIndex,
			Token token) {
		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();
		// where
		JsonArray jarray = jobject.getAsJsonArray("filter");
		String where = "";
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();
			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined"))
				fixValue = "";
			where += " AND o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%'";
		}
		// sorting
		String sort = " ORDER BY o.postdate DESC";
		try {
			sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "");
		} catch (Exception e) {
			// No sorting
		}

		// status
		Integer status = null;
		try {
			status = Integer.parseInt(jobject.get("status").toString());
		} catch (Exception e) {
			// No sorting
		}

		int userId = token.getUser().getId();
		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(userId);

		Query q = em.createQuery(
				"SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0 and o.status <> 3 and o.status <> 9 and o.status <> 2 and o.organisasi.id=:afcoId AND o.prnumber not like 'PR-Join%'");
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numAllstatus = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0 AND o.status = 8 and  o.organisasi.id=:afcoId"); // numNeedVerification
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numNeedVerification = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0  and o.status =  7 and  o.organisasi.id=:afcoId"); // numProcurementProcess
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numProcurementProcess = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0  and o.status =  5 and  o.organisasi.id=:afcoId"); // numOnProcess
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numOnProcess = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT COUNT(o) FROM PurchaseRequest o WHERE o.isDelete = 0  and o.status =  6 and  o.organisasi.id=:afcoId"); // numReceived
		/* q.setParameter("userId", token.getUser().getId()); */
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int numReceived = Integer.parseInt(q.getSingleResult().toString());

		if (status != null) {
			where += " and o.status = " + status + " ";
		}

		q = em.createQuery(
				"SELECT o FROM PurchaseRequest o WHERE o.isDelete = 0 and o.status <> 3 and o.status <> 9 and o.status <> 2 and o.organisasi.id=:afcoId AND o.prnumber not like 'PR-Join%'"
						+ where + sort);
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PurchaseRequest> listPurchaseRequest = q.getResultList();

		q = em.createQuery(
				"SELECT o FROM PurchaseRequest o WHERE o.isDelete = 0 and o.status <> 3 and o.status <> 9 and o.status <> 2 and o.organisasi.id=:afcoId AND o.prnumber not like 'PR-Join%'"
						+ where + sort);
		q.setParameter("afcoId", afcoOrganisasi.getId());
		int totalItems = q.getResultList().size();

		List<PurchaseRequestDTO> lpr = new ArrayList<PurchaseRequestDTO>();
		for (PurchaseRequest pr : listPurchaseRequest) {
			PurchaseRequestDTO newObj = new PurchaseRequestDTO(pr);
			// check sudah dibuat pengadaan atau belum
			List<PurchaseRequestPengadaan> prPengadaanList = prPengadaanSession.getListByPerencanaanId(pr.getId());
			if (prPengadaanList.size() > 0) {
				newObj.setPengadaanId(prPengadaanList.get(0).getPengadaan().getId());
			} else {
				newObj.setPengadaanId(0); // belum dibuat pengadaan
			}
			lpr.add(newObj);
		}
		// return new PurchaseOrderListPagination(totalData, startIndex,
		// endIndex,lpo)

		return new PurchaseRequestListPagination(numAllstatus, numNeedVerification, numProcurementProcess, numOnProcess, numReceived,
				totalItems, startIndex, endIndex, lpr);
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getPagingList(String keywordSearch, int pageNo, int pageSize, String orderKeyword) {
		String vQuery = "select pr " + "from PurchaseRequest pr " + "where pr.isDelete = 0 ";
		if (keywordSearch != null) {
			vQuery = vQuery + "and (pr.prnumber like :keyword or pr.department like :keyword or pr.totalcost like :keyword "
					+ "or pr.daterequired like :keyword or pr.postdate like :keyword or pr.nextapproval like :keyword) ";
		}
		if (orderKeyword != null) {
			vQuery = vQuery + "order by pr." + orderKeyword + " asc";
		}
		Query query = em.createQuery(vQuery);
		if (keywordSearch != null) {
			query.setParameter("keyword", keywordSearch);
		}
		query.setFirstResult((pageNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getVerifiedPurchaseRequest(Token token) {
		User user = token.getUser();
		List<PurchaseRequest> hasilList = new ArrayList<PurchaseRequest>();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(user.getId());
		String namaDepartement = roleUserList.get(0).getOrganisasi().getNama();
		List<Integer> statuList = new ArrayList<Integer>();
		statuList.add(7);// 7=Verfied, sudah diapprove semua, liat design Class
							// Diagram
		// statuList.add(5); // 5=On Process
		statuList.add(9); // 9=Join PR

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr " + "WHERE pr.isDelete = :isDelete " + "AND pr.status IN (:status) "
				+ "AND pr.department LIKE :namaDepartement "
				+ "AND pr.id NOT IN ( SELECT prp.purchaseRequest.id FROM PurchaseRequestPengadaan prp WHERE prp.isDelete = :isDelete) ");

		q.setParameter("namaDepartement", namaDepartement);
		q.setParameter("status", statuList);
		q.setParameter("isDelete", 0);
		List<PurchaseRequest> prList = q.getResultList();

		for (PurchaseRequest pr : prList) {
			if (pr.getStatus() == 9) {
				hasilList.add(pr);

				// cek jika status nya == 7 dan belum ada di tabel purchase_request_pengadaan
			} else if (pr.getStatus() == 7) {
				List<PurchaseRequestPengadaan> prPengadaan = prPengadaanSession.getListByPerencanaanId(pr.getId());
				if (prPengadaan.size() == 0) {
					hasilList.add(pr);
				}
			} else {

				List<PurchaseRequestItem> itemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(pr.getId());
				if (itemList != null && itemList.size() > 0) {
					List<Boolean> joinPRList = new ArrayList<Boolean>();
					for (PurchaseRequestItem prItem : itemList) {
						if (prItem.getPriceafterjoin() == null) {
							joinPRList.add(true);
						} else {
							joinPRList.add(false);
						}
					}
					if (!joinPRList.contains(false)) {
						hasilList.add(pr);
					}
				}
			}
		}
		return hasilList;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	// Tambahan ical, untuk get pr di inisialisasi pengadaan
	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getVerifiedPurchaseRequestByStatus(Token token) {
		User user = token.getUser();
		List<Integer> organisasiIdList = new ArrayList<Integer>();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(user.getId());

		for (RoleUser roleUser : roleUserList) {
			organisasiIdList.add(roleUser.getOrganisasi().getParentId()); // panitia
		}
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(7);// 7=Verfied, sudah diapprove semua, liat design Class
		// Diagram

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr " + "WHERE pr.isDelete = 0 " + "AND pr.status = :status "
				+ "AND pr.organisasi.id IN (:organisasiId) "
				+ "AND pr.id NOT IN ( SELECT prp.purchaseRequest.id FROM PurchaseRequestPengadaan prp WHERE prp.isDelete = 0) ");

		q.setParameter("organisasiId", organisasiIdList);
		q.setParameter("status", statusList);
		List<PurchaseRequest> prList = q.getResultList();

		/*
		 * for (PurchaseRequest pr : prList) { if (pr.getStatus() == 9) {
		 * prList.add(pr); } else { List<PurchaseRequestItem> itemList =
		 * purchaseRequestItemSession
		 * .getPurchaseRequestItemByPurchaseRequestId(pr.getId()); if (itemList != null
		 * && itemList.size() > 0) { List<Boolean> joinPRList = new
		 * ArrayList<Boolean>(); for (PurchaseRequestItem prItem : itemList) { if
		 * (prItem.getPriceafterjoin() == null) { joinPRList.add(true); } else {
		 * joinPRList.add(false); } } if (!joinPRList.contains(false)) { prList.add(pr);
		 * } } } }
		 */

		return prList;

	}

	/**
	 * Note : pr verified, pr yg sudah ada vendor
	 */
	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getVerifiedPurchaseRequestByNumber(String keyword) {

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr " + "WHERE pr.isDelete = :isDelete " + "AND pr.status IN (:status) "
				+ "AND pr.prnumber like :prnumber "
				+ "AND 0 < (SELECT COUNT(pri.purchaserequest.id) FROM PurchaseRequestItem pri WHERE pri.purchaserequest.id = pr.id AND pri.vendor <> null AND pri.isDelete = :isDelete ) "
				+ "AND (SELECT COUNT(pri.vendor.id) FROM PurchaseRequestItem pri WHERE pri.purchaserequest.id = pr.id AND pri.vendor <> null AND pri.isDelete = :isDelete )"
				+ " > (SELECT COUNT(pri.vendor.id) FROM PurchaseRequestItem pri WHERE pri.purchaserequest.id = pr.id AND pri.vendor <> null AND pri.isDelete = :isDelete "
				+ "AND pri.vendor.id IN (SELECT poi.vendor.id FROM PurchaseOrderItem poi WHERE poi.purchaseOrder.isDelete = :isDelete AND poi.purchaseOrder.purchaseRequest.id = pr.id)"
				+ ") ");
		List<Integer> listStatus = new ArrayList<Integer>();
		// listStatus.add(7);
		listStatus.add(5);
		q.setParameter("status", listStatus);
		q.setParameter("prnumber", "%" + keyword + "%");
		q.setParameter("isDelete", 0);
		return q.getResultList();
	}

	public List<PurchaseRequest> getVerifiedPurchaseRequestByNumberAndToken(String keyword, Token token) {
		// getAfcoId
		int userId = token.getUser().getId();
		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(userId);

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr " + "WHERE pr.isDelete = :isDelete "
				+ "AND pr.status IN (:status) AND pr.organisasi.id=:afcoId AND pr.prnumber like :prnumber "
				+ "AND 0 < (SELECT COUNT(pri.purchaserequest.id) FROM PurchaseRequestItem pri WHERE pri.purchaserequest.id = pr.id AND pri.vendor <> null AND pri.isDelete = :isDelete ) "
				+ "AND (SELECT COUNT(pri.vendor.id) FROM PurchaseRequestItem pri WHERE pri.purchaserequest.id = pr.id AND pri.vendor <> null AND pri.isDelete = :isDelete )"
				+ " > (SELECT COUNT(pri.vendor.id) FROM PurchaseRequestItem pri WHERE pri.purchaserequest.id = pr.id AND pri.vendor <> null AND pri.isDelete = :isDelete "
				+ "AND pri.vendor.id IN (SELECT poi.vendor.id FROM PurchaseOrderItem poi WHERE poi.purchaseOrder.isDelete = :isDelete AND poi.purchaseOrder.purchaseRequest.id = pr.id AND poi.purchaseOrder.status <> 'Reject' )"
				+ ") ");
		List<Integer> listStatus = new ArrayList<Integer>();
		// listStatus.add(7);
		listStatus.add(5);
		q.setParameter("status", listStatus);
		q.setParameter("prnumber", "%" + keyword + "%");
		q.setParameter("isDelete", 0);
		q.setParameter("afcoId", afcoOrganisasi.getId());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getPurchaseRequestListByNumber(String keyword) {

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr " + "WHERE pr.isDelete = :isDelete " + "AND pr.prnumber = :prnumber ");
		q.setParameter("prnumber", keyword);
		q.setParameter("isDelete", 0);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequest getPurchaseRequestByNumber(String keyword) {

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr WHERE pr.isDelete = :isDelete " + "AND pr.prnumber = :prnumber ");
		q.setParameter("prnumber", keyword);
		q.setParameter("isDelete", 0);

		List<PurchaseRequest> purchaseRequestList = q.getResultList();
		try {
			return purchaseRequestList.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequest getPRNumberAndOrgCode(String keyword, String orgCode) {

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr WHERE pr.isDelete = :isDelete "
				+ "AND pr.prnumber = :prnumber and pr.organisasi.code = :orgCode ");
		q.setParameter("prnumber", keyword);
		q.setParameter("orgCode", orgCode);
		q.setParameter("isDelete", 0);

		List<PurchaseRequest> purchaseRequestList = q.getResultList();
		try {
			return purchaseRequestList.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PurchaseRequest getByBoNumber(String keyword) {

		Query q = em.createQuery("SELECT pr FROM PurchaseRequest pr WHERE pr.isDelete = :isDelete " + "AND pr.boNumber = :boNumber ");
		q.setParameter("boNumber", keyword);
		q.setParameter("isDelete", 0);

		List<PurchaseRequest> purchaseRequestList = q.getResultList();
		try {
			return purchaseRequestList.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public int countByStatus(int status, Token token) {
		int userId = token.getUser().getId();
		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(userId);

		/*
		 * if(status==3){ List<Integer> statusList = new ArrayList<Integer>();
		 * statusList.add(status); statusList.add(4); //hold pr status
		 * 
		 * return countStatusApprovalAndHold(statusList, userId); } else {
		 * 
		 * 
		 * }
		 */

		Query q = em.createNamedQuery("PurchaseRequest.countByStatus");
		q.setParameter("status", status);
		q.setParameter("userId", userId);
		q.setParameter("afcoId", afcoOrganisasi.getId());
		return ((Long) q.getSingleResult()).intValue();

	}

	public int countStatusApprovalAndHold(List<Integer> statusList, int userId) {

		Query q = em.createNamedQuery("PurchaseRequest.countByStatusList");
		q.setParameter("status", statusList);
		q.setParameter("userId", userId);
		try {
			return ((Long) q.getSingleResult()).intValue();
		} catch (Exception e) {
			return new Integer(0);
		}
	}

	public int countAll(Token token) {
		int userId = token.getUser().getId();
		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(userId);

		Query q = em.createNamedQuery("PurchaseRequest.countAll");
		q.setParameter("userId", token.getUser().getId());
		q.setParameter("afcoId", afcoOrganisasi.getId());
		return ((Long) q.getSingleResult()).intValue();
	}

	public Integer getAfcoByToken(Token token) {

		Integer afcoId = 0;
		List<Integer> organiasiIDList = new ArrayList<Integer>();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(token.getUser().getId());
		for (RoleUser roleUser : roleUserList) {
			organiasiIDList.add(roleUser.getOrganisasi().getId());
			// get child organisasi
			// level1 kebawah
			List<Organisasi> organisasiList = organisasiSession.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
			for (Organisasi organisasi : organisasiList) {
				organiasiIDList.add(organisasi.getId());
				// level2 kebawah
				List<Organisasi> organiasiChild01List = organisasiSession.getOrganisasiListByParentId(organisasi.getParentId());
				for (Organisasi organisasi2 : organiasiChild01List) {
					organiasiIDList.add(organisasi2.getId());
				}
			}

			// getParent organiasi
			// level 1 ke atas
			if (roleUser.getOrganisasi().getParentId() != 1) {
				// jika bukan top organisasi
				organiasiIDList.add(roleUser.getOrganisasi().getParentId());
				Organisasi organisasiParentL1 = organisasiSession.getOrganisasi(roleUser.getOrganisasi().getParentId());
				if (organisasiParentL1.getParentId() != 1) {
					// level 2
					organiasiIDList.add(organisasiParentL1.getParentId());
					Organisasi organisasiParentL2 = organisasiSession.getOrganisasi(organisasiParentL1.getParentId());
					// level 3
					if (organisasiParentL2.getParentId() != 1) {
						organiasiIDList.add(organisasiParentL2.getParentId());
					}
				}
			}
		}
		// grouping organisasiID
		Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
		List<Integer> organisasiIDNewList = new ArrayList<Integer>();
		int index = 0;
		for (Integer organisasiID : organiasiIDList) {
			if (!hashMap.containsValue(organisasiID)) {
				hashMap.put(index, organisasiID);
				organisasiIDNewList.add(organisasiID);
				index++;
			}
		}
		List<Afco> afcoList = afcoSesssion.findByOrganisasiList(organisasiIDNewList);
		if (afcoList.size() > 0) {
			afcoId = afcoList.get(0).getId();
		}

		return afcoId;

	}

	public List<DashboardPerencanaanDTO> countByMonth(Token token) {
		Query q = em.createQuery(
				"SELECT YEAR(pr.created) as year,MONTH(pr.created) as month,pr.status as status, count(*) FROM PurchaseRequest pr WHERE pr.isDelete = 0 AND pr.userId=:userId group by YEAR(pr.created),MONTH(pr.created),pr.status ORDER BY 1 ASC,2 ASC ");
		q.setParameter("userId", token.getUser().getId());
		Iterator iterator = q.getResultList().iterator();
		List<DashboardPerencanaanDTO> list = new ArrayList<DashboardPerencanaanDTO>();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			DashboardPerencanaanDTO dashboardPerencanaanDTO;
			boolean add = true;
			if (list.size() > 0) {
				DashboardPerencanaanDTO tempDashboardPerencanaanDTO = list.get(list.size() - 1);
				if (tempDashboardPerencanaanDTO.getYear() == (Integer) tuple[0]
						&& tempDashboardPerencanaanDTO.getMonth() == (Integer) tuple[1]) {
					dashboardPerencanaanDTO = tempDashboardPerencanaanDTO;
					add = false;
				} else {
					dashboardPerencanaanDTO = new DashboardPerencanaanDTO();
					dashboardPerencanaanDTO.setYear((Integer) tuple[0]);
					dashboardPerencanaanDTO.setMonth((Integer) tuple[1]);
				}
			}

			else {
				dashboardPerencanaanDTO = new DashboardPerencanaanDTO();
				dashboardPerencanaanDTO.setYear((Integer) tuple[0]);
				dashboardPerencanaanDTO.setMonth((Integer) tuple[1]);
			}

			int status = (Integer) tuple[2];
			if (status == 8) {
				dashboardPerencanaanDTO.setNeedVerification((Long) tuple[3]);
			} else if (status == 5) {
				dashboardPerencanaanDTO.setOnProgress((Long) tuple[3]);
			} else if (status == 6) {
				dashboardPerencanaanDTO.setReceived((Long) tuple[3]);
			} else if (status == 7) {
				dashboardPerencanaanDTO.setProcurementProcess((Long) tuple[3]);
			} else if (status == 3) {
				dashboardPerencanaanDTO.setApprovalProcess((Long) tuple[3]);
			}
			if (add) {
				list.add(dashboardPerencanaanDTO);
			} else {
				list.set(list.size() - 1, dashboardPerencanaanDTO);
			}

		}
		if (list.size() > 0) {
			return list;
		}
		// jika belum ada perencanaan
		else {
			List<DashboardPerencanaanDTO> list2 = new ArrayList<DashboardPerencanaanDTO>();
			DashboardPerencanaanDTO dashboardPerencanaanDTO2 = new DashboardPerencanaanDTO();
			dashboardPerencanaanDTO2.setYear(0);
			dashboardPerencanaanDTO2.setMonth(0);
			dashboardPerencanaanDTO2.setWeek(0);
			;
			list2.add(dashboardPerencanaanDTO2);
			return list2;
		}

	}

	public List<DashboardPerencanaanDTO> countByYear(Token token) {
		Query q = em.createQuery(
				"SELECT YEAR(pr.created) as year,pr.status as status, count(*) FROM PurchaseRequest pr WHERE pr.isDelete = 0 AND pr.userId=:userId group by YEAR(pr.created),pr.status ORDER BY 1 ASC");
		q.setParameter("userId", token.getUser().getId());
		Iterator iterator = q.getResultList().iterator();
		List<DashboardPerencanaanDTO> list = new ArrayList<DashboardPerencanaanDTO>();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			DashboardPerencanaanDTO dashboardPerencanaanDTO;
			boolean add = true;
			if (list.size() > 0) {
				DashboardPerencanaanDTO tempDashboardPerencanaanDTO = list.get(list.size() - 1);
				if (tempDashboardPerencanaanDTO.getYear() == (Integer) tuple[0]) {
					dashboardPerencanaanDTO = tempDashboardPerencanaanDTO;
					add = false;
				} else {
					dashboardPerencanaanDTO = new DashboardPerencanaanDTO();
					dashboardPerencanaanDTO.setYear((Integer) tuple[0]);
				}
			}

			else {
				dashboardPerencanaanDTO = new DashboardPerencanaanDTO();
				dashboardPerencanaanDTO.setYear((Integer) tuple[0]);
			}

			int status = (Integer) tuple[1];
			if (status == 8) {
				dashboardPerencanaanDTO.setNeedVerification((Long) tuple[2]);
			} else if (status == 5) {
				dashboardPerencanaanDTO.setOnProgress((Long) tuple[2]);
			} else if (status == 6) {
				dashboardPerencanaanDTO.setReceived((Long) tuple[2]);
			} else if (status == 7) {
				dashboardPerencanaanDTO.setProcurementProcess((Long) tuple[2]);
			} else if (status == 3) {
				dashboardPerencanaanDTO.setApprovalProcess((Long) tuple[2]);
			}
			if (add) {
				list.add(dashboardPerencanaanDTO);
			} else {
				list.set(list.size() - 1, dashboardPerencanaanDTO);
			}

		}
		if (list.size() > 0) {
			return list;
		}
		// jika belum ada perencanaan
		else {
			List<DashboardPerencanaanDTO> list2 = new ArrayList<DashboardPerencanaanDTO>();
			DashboardPerencanaanDTO dashboardPerencanaanDTO2 = new DashboardPerencanaanDTO();
			dashboardPerencanaanDTO2.setYear(0);
			dashboardPerencanaanDTO2.setMonth(0);
			dashboardPerencanaanDTO2.setWeek(0);
			;
			list2.add(dashboardPerencanaanDTO2);
			return list2;
		}
	}

	public List<DashboardPerencanaanDTO> countByWeek(Token token) {
		Query q = em.createQuery(
				"SELECT YEAR(pr.created) as year,WEEK(pr.created) as week,pr.status as status, count(*) FROM PurchaseRequest pr WHERE pr.isDelete = 0 AND pr.userId=:userId group by YEAR(pr.created),WEEK(pr.created),pr.status ORDER BY 1 ASC,2 ASC ");
		q.setParameter("userId", token.getUser().getId());
		Iterator iterator = q.getResultList().iterator();
		List<DashboardPerencanaanDTO> list = new ArrayList<DashboardPerencanaanDTO>();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			DashboardPerencanaanDTO dashboardPerencanaanDTO;
			boolean add = true;
			if (list.size() > 0) {
				DashboardPerencanaanDTO tempDashboardPerencanaanDTO = list.get(list.size() - 1);
				if (tempDashboardPerencanaanDTO.getYear() == (Integer) tuple[0]
						&& tempDashboardPerencanaanDTO.getWeek() == (Integer) tuple[1]) {
					dashboardPerencanaanDTO = tempDashboardPerencanaanDTO;
					add = false;
				} else {
					dashboardPerencanaanDTO = new DashboardPerencanaanDTO();
					dashboardPerencanaanDTO.setYear((Integer) tuple[0]);
					dashboardPerencanaanDTO.setWeek((Integer) tuple[1]);
				}
			}

			else {
				dashboardPerencanaanDTO = new DashboardPerencanaanDTO();
				dashboardPerencanaanDTO.setYear((Integer) tuple[0]);
				dashboardPerencanaanDTO.setWeek((Integer) tuple[1]);
			}

			int status = (Integer) tuple[2];
			if (status == 8) {
				dashboardPerencanaanDTO.setNeedVerification((Long) tuple[3]);
			} else if (status == 5) {
				dashboardPerencanaanDTO.setOnProgress((Long) tuple[3]);
			} else if (status == 6) {
				dashboardPerencanaanDTO.setReceived((Long) tuple[3]);
			} else if (status == 7) {
				dashboardPerencanaanDTO.setProcurementProcess((Long) tuple[3]);
			} else if (status == 3) {
				dashboardPerencanaanDTO.setApprovalProcess((Long) tuple[3]);
			}
			if (add) {
				list.add(dashboardPerencanaanDTO);
			} else {
				list.set(list.size() - 1, dashboardPerencanaanDTO);
			}

		}

		if (list.size() > 0) {
			return list;
		}
		// jika belum ada perencanaan
		else {
			List<DashboardPerencanaanDTO> list2 = new ArrayList<DashboardPerencanaanDTO>();
			DashboardPerencanaanDTO dashboardPerencanaanDTO2 = new DashboardPerencanaanDTO();
			dashboardPerencanaanDTO2.setYear(0);
			dashboardPerencanaanDTO2.setMonth(0);
			dashboardPerencanaanDTO2.setWeek(0);
			;
			list2.add(dashboardPerencanaanDTO2);
			return list2;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void SyncronizePRApprovedDate() {
		List<PurchaseRequest> purchaseRequestList = getList();
		for (PurchaseRequest pr : purchaseRequestList) {
			List<ApprovalProcess> approvalProcessList = approvalProcessSession.getApprovedDate(pr.getId(), 26);
			if (approvalProcessList.size() > 0) {
				pr.setApprovedDate(approvalProcessList.get(0).getUpdated());
			}
			update(pr, null);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getBookingOrderListPagination(String search, Integer status, Integer statusEbs, Integer pageNo,
			Integer pageSize, Date startDate, Date endDate, RoleUser roleUser, List<Integer> orgIdList) {

		String stringQuery = "SELECT purchaseRequest FROM PurchaseRequest purchaseRequest WHERE purchaseRequest.isDelete = 0 ";
		search = search == null ? "" : search.trim();

		if (!search.isEmpty()) {
			stringQuery = stringQuery + "and (purchaseRequest.boNumber like :search or purchaseRequest.prnumber like :search) ";
		}
		if (status != null) {
			stringQuery = stringQuery + "and purchaseRequest.status =:status ";
		}
		if (statusEbs != null) {
			stringQuery = stringQuery + "and purchaseRequest.statusEbs =:statusEbs ";
		}
		
		if (startDate != null && endDate !=null) {
			stringQuery = stringQuery + "and purchaseRequest.created >= :startDate and purchaseRequest.created <= :endDate ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			stringQuery = stringQuery + "and purchaseRequest.id IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		}else {
			if(orgIdList.size() > 0) {
				stringQuery += "AND purchaseRequest.organisasi.id IN (:orgIdList) ";
			}			
		}
		
//		else{
//			stringQuery = stringQuery + "and purchaseRequest.organisasi.id IN (:organisasiList) ";
//		} 
		
		stringQuery = stringQuery + "order by purchaseRequest.created DESC, purchaseRequest.updated DESC ";
		Query query = getEntityManager().createQuery(stringQuery);
		if (!search.isEmpty()) {
			query.setParameter("search", "%" + search + "%");
		}
		if (status != null) {
			query.setParameter("status", status);
		}
		if (statusEbs != null) {
			query.setParameter("statusEbs", statusEbs);
		}
		if (startDate != null && endDate != null) {
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			query.setParameter("vendorId", vendor.getId());	
		}else {
			if(orgIdList.size() > 0) {
				query.setParameter("orgIdList", orgIdList);
			}			
		}
//		else{
//			List<Integer> organisasiIdList = new ArrayList<Integer>();
//			List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
//			for(Organisasi organisasi : organisasiList) {
//				organisasiIdList.add(organisasi.getId());
//			}
//				
//			if(organisasiIdList.size() > 0) {
//				query.setParameter("organisasiList", organisasiIdList);
//			}else {
//				query.setParameter("organisasiList", 0);//Tidak dapat list organisasi
//			}
//		} 
		
		query.setFirstResult((pageNo - 1) * pageSize).
		setMaxResults(pageSize);
		return query.getResultList();
	}
	
	public Long getTotalList(String search, Integer status, Integer statusEbs, Date startDate, Date endDate, RoleUser roleUser, List<Integer> orgIdList) {
		String vQuery = "select COUNT (pr) from PurchaseRequest pr where pr.isDelete = 0 ";
		
		if (!search.isEmpty()) {
			vQuery = vQuery + "and (pr.prnumber like :keyword or pr.boNumber like :keyword) ";
		}
		if (status != null) {
			vQuery = vQuery + "and pr.status =:status ";
		}

		if (statusEbs != null) {
			vQuery = vQuery + "and pr.statusEbs =:statusEbs ";
		}
		
		if (startDate != null && endDate !=null) {
			vQuery = vQuery + "and pr.created >= :startDate and pr.created <= :endDate ";
		}
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			vQuery = vQuery + "and pr.id IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		}else {
			if(orgIdList.size() > 0) {
				vQuery += "AND pr.organisasi.id IN (:orgIdList) ";
			}			
		}
//		else{
//			vQuery = vQuery + "and pr.organisasi.id IN (:organisasiList) ";
//		} 

		Query query = em.createQuery(vQuery);
		if (!search.isEmpty()) {
			query.setParameter("keyword", "%" + search + "%");
		}
		if (status != null) {
			query.setParameter("status", status);
		}

		if (statusEbs != null) {
			query.setParameter("statusEbs", statusEbs);
		}
		if (startDate != null && endDate != null) {
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			query.setParameter("vendorId", vendor.getId());	
		}else {
			if(orgIdList.size() > 0) {
				query.setParameter("orgIdList", orgIdList);
			}
		}
//		else{
//			List<Integer> organisasiIdList = new ArrayList<Integer>();
//			List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
//			for(Organisasi organisasi : organisasiList) {
//				organisasiIdList.add(organisasi.getId());
//			}
//			
//			if(organisasiIdList.size() > 0) {
//				query.setParameter("organisasiList", organisasiIdList);
//			}else {
//				query.setParameter("organisasiList", 0);//Tidak dapat list organisasi
//			}
//		} 

		return (Long) query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseRequest> getListPagination(String keyword, Integer statusBo, Integer statusPr, Integer start,
			Integer length, Date startDate, Date endDate, RoleUser roleUser, List<Integer> orgIdList) {

		String stringQuery = "SELECT purchaseRequest FROM PurchaseRequest purchaseRequest WHERE purchaseRequest.isDelete = 0 ";

		if (!keyword.isEmpty()) {
			stringQuery = stringQuery + "AND (purchaseRequest.boNumber LIKE :search OR purchaseRequest.prnumber LIKE :search) ";
		}
		if (statusBo != null) {
			stringQuery = stringQuery + "AND purchaseRequest.status =:status ";
		}
		if (statusPr != null) {
			stringQuery = stringQuery + "AND purchaseRequest.statusEbs =:statusEbs ";
		}
		
		if (startDate != null && endDate !=null) {
			stringQuery = stringQuery + "AND purchaseRequest.created >= :startDate and purchaseRequest.created <= :endDate ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			stringQuery = stringQuery + "AND purchaseRequest.id IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		}else {
			if(orgIdList.size() > 0) {
				stringQuery += "AND purchaseRequest.organisasi.id IN (:orgIdList) ";
			}			
		}
		
		stringQuery = stringQuery + "ORDER BY purchaseRequest.created DESC ";
		Query query = em.createQuery(stringQuery);
		if (!keyword.isEmpty()) {
			query.setParameter("search", "%" + keyword + "%");
		}
		if (statusBo != null) {
			query.setParameter("status", statusBo);
		}
		if (statusPr != null) {
			query.setParameter("statusEbs", statusPr);
		}
		if (startDate != null && endDate != null) {
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			query.setParameter("vendorId", vendor.getId());	
		}else {
			if(orgIdList.size() > 0) {
				query.setParameter("orgIdList", orgIdList);
			}			
		}		
		query.setFirstResult(start);
		query.setMaxResults(length);
		return query.getResultList();
	}
	
	public Long getListPaginationSize(String keyword, Integer statusBo, Integer statusPr, Date startDate, Date endDate, RoleUser roleUser, List<Integer> orgIdList) {

		String stringQuery = "SELECT COUNT(purchaseRequest) FROM PurchaseRequest purchaseRequest WHERE purchaseRequest.isDelete = 0 ";

		if (!keyword.isEmpty()) {
			stringQuery = stringQuery + "AND (purchaseRequest.boNumber LIKE :search OR purchaseRequest.prnumber LIKE :search) ";
		}
		if (statusBo != null) {
			stringQuery = stringQuery + "AND purchaseRequest.status =:status ";
		}
		if (statusPr != null) {
			stringQuery = stringQuery + "AND purchaseRequest.statusEbs =:statusEbs ";
		}
		
		if (startDate != null && endDate !=null) {
			stringQuery = stringQuery + "AND purchaseRequest.created >= :startDate and purchaseRequest.created <= :endDate ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			stringQuery = stringQuery + "AND purchaseRequest.id IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		}else {
			if(orgIdList.size() > 0) {
				stringQuery += "AND purchaseRequest.organisasi.id IN (:orgIdList) ";
			}			
		}
		
		stringQuery = stringQuery + "ORDER BY purchaseRequest.created DESC ";
		Query query = em.createQuery(stringQuery);
		if (!keyword.isEmpty()) {
			query.setParameter("search", "%" + keyword + "%");
		}
		if (statusBo != null) {
			query.setParameter("status", statusBo);
		}
		if (statusPr != null) {
			query.setParameter("statusEbs", statusPr);
		}
		if (startDate != null && endDate != null) {
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			query.setParameter("vendorId", vendor.getId());	
		}else {
			if(orgIdList.size() > 0) {
				query.setParameter("orgIdList", orgIdList);
			}			
		}
		query.setMaxResults(1);
		return (Long) query.getSingleResult();
	}

}
