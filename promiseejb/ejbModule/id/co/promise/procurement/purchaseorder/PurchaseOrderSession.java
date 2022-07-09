/**
 * fdf
 */
package id.co.promise.procurement.purchaseorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.DeliveryReceivedFiles;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseOrderTerm;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.ShippingTo;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.AfcoSession;
import id.co.promise.procurement.master.AutoNumberSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.TermAndConditionSession;
import id.co.promise.procurement.purchaseorder.create.PurchaseOrderCreatePOParamDTO;
import id.co.promise.procurement.purchaseorder.create.PurchaseOrderItemCreatePOParamDTO;
import id.co.promise.procurement.purchaseorder.create.ShippingToCreatePOParamDTO;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
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
public class PurchaseOrderSession extends AbstractFacadeWithAudit<PurchaseOrder> {
	final static Logger log = Logger.getLogger(PurchaseOrderSession.class);

	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB
	private AddressBookSession addressBookSession;

	@EJB
	private PurchaseRequestSession purchaseRequestSession;

	@EJB
	private AfcoSession afcoSesssion;

	@EJB
	private ShippingToSession shippingToSession;

	@EJB
	private ItemSession itemSession;

	@EJB
	private VendorSession vendorSession;

	@EJB
	private TermAndConditionSession termAndConditionSession;
	
	@EJB
	private SyncSession syncSession;
	
	@EJB
	private UserSession userSession;
	
	@EJB
	private ApprovalSession approvalSession;

	@EJB
	private ApprovalLevelSession approvalLevelSession;

	@EJB
	private ApprovalProcessSession approvalProcessSession;

	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;

	@EJB
	private PurchaseOrderTermSession purchaseOrderTermSession;
	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;
	@EJB
	private TahapanSession tahapanSession;
	@EJB
	private TokenSession tokenSession;
	@EJB
	private RoleUserSession roleUserSession;
	@EJB
	private OrganisasiSession organisasiSession;
	
	@EJB
	private SatuanSession satuanSession;
	
	@EJB
	private AutoNumberSession autoNumberSession;

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

	public PurchaseOrderSession() {
		super(PurchaseOrder.class);
	}

	public PurchaseOrder getPurchaseOrder(int id) {
		return super.find(id);
	}
	
	public List<PurchaseOrder> getPOListByStatusList(List<Integer> statusList){
		Query q = em.createNamedQuery("PurchaseOrder.getPOListByStatusList");
		q.setParameter("statusList", statusList);
		return q.getResultList();
	}

	@SuppressWarnings({ "unchecked" })
	public List<PurchaseOrder> getPurchaseOrderList() {
		Query q = em.createNamedQuery("PurchaseOrder.find");
		return q.getResultList();
	}

	public int countPOByStatus(String status) {
		Query q = em.createNamedQuery("PurchaseOrder.countPOByStatus");
		q.setParameter("status", status);
		return Integer.parseInt(q.getSingleResult().toString());
	}

	public List<PurchaseOrder> getPOByPRId(int prId) {
		Query q = em.createNamedQuery("PurchaseOrder.getPOByPRId");
		q.setParameter("prId", prId);
		return q.getResultList();
	}

	public PurchaseOrder getPurchaseOrderByPRId(int prId) {
		Query q = em.createNamedQuery("PurchaseOrder.getPOByPRId");
		q.setParameter("prId", prId);
		List<PurchaseOrder> purchaseOrderList = q.getResultList();

		if (purchaseOrderList.size() > 0) {
			return purchaseOrderList.get(0);
		} else {
			return null;
		}
	}
	
	public PurchaseOrder getPOByPRIdEbs(int prIdEbs) {
		Query q = em.createNamedQuery("PurchaseOrder.getPOByPRIdEbs");
		q.setParameter("prIdEbs", prIdEbs);
		List<PurchaseOrder> purchaseOrderList = q.getResultList();

		if (purchaseOrderList.size() > 0) {
			return purchaseOrderList.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PurchaseOrder getPurchaseOrderByPoNumber(String poNumber) {
		Query q = em.createQuery(
				"SELECT purchaseOrder FROM PurchaseOrder purchaseOrder WHERE purchaseOrder.isDelete = 0 AND purchaseOrder.poNumber = :poNumber");
		q.setParameter("poNumber", poNumber);

		List<PurchaseOrder> purchaseOrder = q.getResultList();
		if (purchaseOrder != null && purchaseOrder.size() > 0) {
			return purchaseOrder.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public PurchaseOrder getByPONumberAndOrgCode(String poNumber,String orgCode) {
		Query q = em.createQuery(
				"SELECT purchaseOrder FROM PurchaseOrder purchaseOrder WHERE purchaseOrder.isDelete = 0 AND purchaseOrder.poNumber = :poNumber "
				+ " and purchaseOrder.purchaseRequest.organisasi.code = :orgCode ");
		q.setParameter("poNumber", poNumber);
		q.setParameter("orgCode", orgCode);

		List<PurchaseOrder> purchaseOrder = q.getResultList();
		if (purchaseOrder != null && purchaseOrder.size() > 0) {
			return purchaseOrder.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public PurchaseOrder getByPrNumber(String prnumber) {
		Query q = em.createQuery(
				"SELECT purchaseOrder FROM PurchaseOrder purchaseOrder WHERE purchaseOrder.isDelete = 0 AND purchaseOrder.purchaseRequest.prnumber = :prnumber");
		q.setParameter("prnumber", prnumber);

		List<PurchaseOrder> purchaseOrder = q.getResultList();
		if (purchaseOrder != null && purchaseOrder.size() > 0) {
			return purchaseOrder.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public PurchaseOrder getbyPONumberEbs(String poNumber) {
		Query q = em.createNamedQuery("PurchaseOrder.findByPONumberEbs");
		q.setParameter("poNumber", poNumber);
		List<PurchaseOrder> purchaseOrderList = q.getResultList();
		if (purchaseOrderList != null && purchaseOrderList.size() > 0) {
			return purchaseOrderList.get(0);
		}else {
			return null;
		}
	}
	
	public int countPOByStatusAndOrg(String status, Token token) {
		List<Integer> organiasiIDList = new ArrayList<Integer>();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(token.getUser().getId());
		for (RoleUser roleUser : roleUserList) {
			organiasiIDList.add(roleUser.getOrganisasi().getId());
			// get child organisasi
			// level1 kebawah
			List<Organisasi> organisasiList = organisasiSession
					.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
			for (Organisasi organisasi : organisasiList) {
				organiasiIDList.add(organisasi.getId());
				// level2 kebawah
				List<Organisasi> organiasiChild01List = organisasiSession
						.getOrganisasiListByParentId(organisasi.getParentId());
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

		Query q = em.createNamedQuery("PurchaseOrder.countPOByStatusAndOrg");
		q.setParameter("status", status);
		q.setParameter("organisasiIDNewList", organisasiIDNewList);
		return Integer.parseInt(q.getSingleResult().toString());
	}

	public Vendor getPurchaseOrderVendor(Integer id) {
		Query q = em.createQuery("SELECT poi.vendor FROM PurchaseOrderItem poi WHERE poi.purchaseOrder.id = :id");
		q.setParameter("id", id);
		return (Vendor) q.getResultList().get(0);
	}

	public int countPOByStatusAndOrgVendor(String status, Token token) {
		Integer userId = token.getUser().getId();
		Query q = em.createQuery(
				"SELECT COUNT (DISTINCT poi.purchaseOrder) FROM PurchaseOrderItem poi WHERE poi.isDelete = 0  "
						+ "AND poi.vendor.user=:userId AND poi.purchaseOrder.isDelete = 0 AND poi.purchaseOrder.status = :status");
		q.setParameter("userId", userId);
		q.setParameter("status", status);
		String jumlahData = "0";
		try {
			jumlahData = q.getSingleResult().toString();
		} catch (Exception e) {
			System.err.println("No Data PO vendor");
		}

		return Integer.parseInt(jumlahData);
	}

	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPurchaseOrderDTOListWithRangeVendor(Integer startIndex, Integer endIndex,
			Token token) {

		int[] range = { startIndex - 1, endIndex - 1 };
		Query q = em
				.createQuery("SELECT DISTINCT (poi.purchaseOrder) FROM PurchaseOrderItem poi WHERE poi.isDelete = 0  "
						+ "AND poi.vendor.user=:userId AND poi.purchaseOrder.status !='Request From Contract' AND poi.purchaseOrder.status !='Approval Process'");
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		q.setParameter("userId", token.getUser().getId());
		List<PurchaseOrder> listPurchaseOrder = q.getResultList();

		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}

		// get count data
		Query qCount = em.createQuery(
				"SELECT COUNT (DISTINCT poi.purchaseOrder) FROM PurchaseOrderItem poi WHERE poi.isDelete = 0  "
						+ "AND poi.vendor.user=:userId AND poi.purchaseOrder.isDelete = 0 AND  poi.purchaseOrder.status !='Request From Contract' AND poi.purchaseOrder.status !='Approval Process'");

		qCount.setParameter("userId", token.getUser().getId());

		String jumlahData = "0";
		try {
			jumlahData = qCount.getSingleResult().toString();
		} catch (Exception e) {
			System.err.println("No Data PO vendor");
		}
		return new PurchaseOrderListPagination(Integer.parseInt(jumlahData), startIndex, endIndex, lpo);
	}

	/*
	 * Tampilkan List PurchaseOrderDTO menggunakan paging
	 */
	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPurchaseOrderDTOListWithRangeVendor(Integer startIndex, Integer endIndex,
			String search, Token token) {
		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();

		JsonArray jarray = jobject.getAsJsonArray("filter");
		String where = "";
		Boolean isAllStatus = true;
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();
			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined")) {
				fixValue = "";
			}
			if (joWhere.get("key").toString().replaceAll("\"", "") == "status") {
				isAllStatus = false;
			}

			where += " AND purchaseOrder." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue
					+ "%'";
		}
		if (isAllStatus) {
			where += " AND (purchaseOrder.status like 'On Process' or purchaseOrder.status like 'DONE' ) ";
		}

		// String sort = "ORDER BY poi.purchaseOrder.purchaseOrderDate ASC ";

		String sort = "ORDER BY purchaseOrder.purchaseOrderDate ASC";
		try {
			sort = " ORDER BY purchaseOrder." + jobject.get("sort").toString().replaceAll("\"", "") + " DESC";
		} catch (Exception e) {

		}

		// get total data

		String strQuery = "SELECT COUNT (purchaseOrder) FROM PurchaseOrder purchaseOrder WHERE purchaseOrder.id in  "
				+ "(select Distinct(poi.purchaseOrder.id) from PurchaseOrderItem poi where poi.isDelete =0 AND poi.vendor.user=:userId ) "
				+ " AND purchaseOrder.isDelete = 0 " + where;

		Query q = em.createQuery(strQuery);

		q.setParameter("userId", token.getUser().getId());
		String jumlahData = "0";
		try {
			jumlahData = q.getSingleResult().toString();
		} catch (Exception e) {
			System.err.println("No Data PO vendor");
		}

		int totalData = Integer.parseInt(jumlahData);

		strQuery = "SELECT purchaseOrder FROM PurchaseOrder purchaseOrder WHERE purchaseOrder.id in"
				+ "(select Distinct(poi.purchaseOrder.id) from PurchaseOrderItem poi where poi.isDelete =0 AND poi.vendor.user=:userId ) "
				+ " AND purchaseOrder.isDelete = 0 " + where + " " + sort;

		// get data detil po
		q = em.createQuery(strQuery);
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		q.setParameter("userId", token.getUser().getId());
		List<PurchaseOrder> listPurchaseOrder = q.getResultList();

		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}
		return new PurchaseOrderListPagination(totalData, startIndex, endIndex, lpo);
	}

	/*
	 * Tampilkan List PurchaseOrderDTO tanpa paging
	 */
	@SuppressWarnings("unchecked")
	public List<PurchaseOrderDTO> getPurchaseOrderDTOList() {
		List<PurchaseOrder> listPurchaseOrder = em.createNamedQuery("PurchaseOrder.find").getResultList();
		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}
		return lpo;
	}

	/*
	 * Tampilkan List PurchaseOrderDTO menggunakan paging
	 */
	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPurchaseOrderDTOListWithRange(Integer startIndex, Integer endIndex,
			String search) {
		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();

		JsonArray jarray = jobject.getAsJsonArray("filter");
		String where = "";
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();
			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined")) {
				fixValue = "";
			}

			where += " AND o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%'";
		}

		String sort = " ";
		try {
			sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "") + " DESC";
		} catch (Exception e) {

		}

		Query q = em.createQuery("SELECT COUNT(o) FROM PurchaseOrder o WHERE o.isDelete = 0 " + where + sort);
		int totalData = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery("SELECT o FROM PurchaseOrder o WHERE o.isDelete = 0 " + where + sort);
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PurchaseOrder> listPurchaseOrder = q.getResultList();

		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}
		return new PurchaseOrderListPagination(totalData, startIndex, endIndex, lpo);
	}

	/*
	 * Tampilkan List PurchaseOrderDTO menggunakan paging
	 */
	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPurchaseOrderDTOListByOrgWithRange(Integer startIndex, Integer endIndex,
			String search, Token token) {
		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId());

		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();

		JsonArray jarray = jobject.getAsJsonArray("filter");
		String where = "";
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();
			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined")) {
				fixValue = "";
			}

			where += " AND o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%'";
		}

		String sort = " ";
		try {
			sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "") + " DESC";
		} catch (Exception e) {

		}

		Query q = em.createQuery(
				"SELECT COUNT(o) FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.purchaseRequest.organisasi.id IN :organisasiId "
						+ where);
		q.setParameter("organisasiId", afcoOrganisasi.getId());
		int totalData = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery(
				"SELECT o FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.purchaseRequest.organisasi.id IN :organisasiId "
						+ where + sort);
		q.setParameter("organisasiId", afcoOrganisasi.getId());
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PurchaseOrder> listPurchaseOrder = q.getResultList();

		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}
		return new PurchaseOrderListPagination(totalData, startIndex, endIndex, lpo);
	}

	/*
	 * Tampilkan List PurchaseOrderDTO menggunakan paging
	 */
	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPurchaseOrderDTOListByOrgWithRangeVendor(Integer startIndex, Integer endIndex,
			String search, Token token) {
		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId());

		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();

		JsonArray jarray = jobject.getAsJsonArray("filter");
		String where = "";
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();
			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined")) {
				fixValue = "";
			}

			where += " AND o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%'";
		}

		String sort = " ";
		try {
			sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "") + " DESC";
		} catch (Exception e) {

		}

		Query q = em.createQuery(
				"SELECT COUNT(o) FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.purchaseRequest.organisasi.id IN :organisasiId "
						+ where + sort);
		q.setParameter("organisasiId", afcoOrganisasi.getId());
		int totalData = Integer.parseInt(q.getSingleResult().toString());

		q = em.createQuery(
				"SELECT o FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.purchaseRequest.organisasi.id IN :organisasiId "
						+ where + sort);
		q.setParameter("organisasiId", afcoOrganisasi.getId());
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PurchaseOrder> listPurchaseOrder = q.getResultList();

		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}
		return new PurchaseOrderListPagination(totalData, startIndex, endIndex, lpo);
	}

	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPurchaseOrderDTOListWithRange(Integer startIndex, Integer endIndex) {

		int[] range = { startIndex - 1, endIndex - 1 };
		Query q = em.createQuery("SELECT o FROM PurchaseOrder o WHERE o.isDelete = 0 ORDER BY o.id DESC");
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);

		List<PurchaseOrder> listPurchaseOrder = q.getResultList();

		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}
		return new PurchaseOrderListPagination(super.count(), startIndex, endIndex, lpo);
	}

	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPurchaseOrderDTOListByOrgWithRange(Integer startIndex, Integer endIndex,
			Token token) {

		Organisasi afcoOrganisasi = organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId());
		Query qCount = em.createQuery(
				"SELECT COUNT(o) FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.purchaseRequest.organisasi.id IN :organisasiId ");
		qCount.setParameter("organisasiId", afcoOrganisasi.getId());
		int totalData = Integer.parseInt(qCount.getSingleResult().toString());

		int[] range = { startIndex - 1, endIndex - 1 };
		Query q = em.createQuery(
				"SELECT o FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.purchaseRequest.organisasi.id IN :organisasiId ORDER BY o.purchaseOrderDate ASC");
		q.setParameter("organisasiId", afcoOrganisasi.getId());
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<PurchaseOrder> listPurchaseOrder = q.getResultList();

		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			lpo.add(new PurchaseOrderDTO(po, purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId())));
		}
		return new PurchaseOrderListPagination(totalData, startIndex, endIndex, lpo);
	}

	public PurchaseOrder insertPurchaseOrder(PurchaseOrder purchaseOrder, Token token) {
		purchaseOrder.setCreated(new Date());
		purchaseOrder.setIsDelete(0);
		purchaseOrder.setUserId(token.getUser().getId());
		super.create(purchaseOrder, AuditHelper.OPERATION_CREATE, token);
		return purchaseOrder;
	}
	public PurchaseOrder updatePurchaseOrder(PurchaseOrder purchaseOrder, Token token) {
		purchaseOrder.setUpdated(new Date());
		super.edit(purchaseOrder, AuditHelper.OPERATION_UPDATE, token);
		return purchaseOrder;
	}

	public PurchaseOrder deletePurchaseOrder(int id, Token token) {
		PurchaseOrder purchaseOrder = super.find(id);
		purchaseOrder.setIsDelete(1);
		purchaseOrder.setDeleted(new Date());
		purchaseOrder.setStatusProses(0);
		super.edit(purchaseOrder, AuditHelper.OPERATION_DELETE, token);
		return purchaseOrder;
	}

	public PurchaseOrder deleteRowPurchaseOrder(int id, Token token) {
		PurchaseOrder purchaseOrder = super.find(id);
		super.remove(purchaseOrder, AuditHelper.OPERATION_ROW_DELETE, token);
		return purchaseOrder;
	}

	public PurchaseOrderCreatePOParamDTO createPurchaseOrder(PurchaseOrderCreatePOParamDTO pto, Token token) {

		Double subTotalPO = 0d;
		try {
			PurchaseOrder purchaseOrder = new PurchaseOrder();
			if (pto.getIsSaveTheNewAddressCompany()) {
				AddressBook newAddressBookPO = new AddressBook();
				newAddressBookPO.setFullName(pto.getCompanyAddrFullName());
				newAddressBookPO.setTelephone(pto.getCompanyAddrTelephone1());
				newAddressBookPO.setStreetAddress(pto.getCompanyAddrAddress());
				newAddressBookPO.setOrganisasi(afcoSesssion.getAfco(pto.getCompanyId()).getOrganisasi());
				newAddressBookPO.setAddressLabel(pto.getCompanyAddrAddress());
				purchaseOrder.setAddressBook(newAddressBookPO);
			} else {
				if (pto.getAddressBookId() != null) {
					purchaseOrder.setAddressBook(addressBookSession.find(pto.getAddressBookId()));
				}
			}
			purchaseOrder.setUserId(token.getUser().getId());
			purchaseOrder.setCreated(new Date());
			purchaseOrder.setCreateDate(new Date());
			purchaseOrder.setIsDelete(0);
			purchaseOrder.setDepartment(pto.getDepartment());
			purchaseOrder.setDiscount(pto.getDiscount());
			purchaseOrder.setDownPayment(pto.getDownPayment());
			purchaseOrder.setNotes(pto.getNotes());
			purchaseOrder.setPoNumber(pto.getPoNumber());
			purchaseOrder.setPurchaseOrderDate(new Date());
			purchaseOrder.setTermandcondition(termAndConditionSession.find(pto.getTermAndConditionId()));
			// totalcost = hps diganti menjadi harga deal dengan vendor
			// purchaseOrder.setTotalCost(pto.getTotalCost());
			purchaseOrder.setTax(pto.getTax());

			purchaseOrder.setStatus("Approval Process");
			purchaseOrder.setStatusProses(1);
			purchaseOrder.setPurchaseRequest(purchaseRequestSession.find(pto.getPurchaseRequestId()));
			purchaseOrder.setFullName(pto.getCompanyAddrFullName());
			purchaseOrder.setStreetAddress(pto.getCompanyAddrAddress());
			purchaseOrder.setAddressLabel(organisasiSession.find(pto.getCompanyId()).getNama());
			purchaseOrder.setTelephone1(pto.getCompanyAddrTelephone1());

			purchaseOrder = insertPurchaseOrder(purchaseOrder, token);

			PurchaseOrderCreatePOParamDTO potResp = new PurchaseOrderCreatePOParamDTO(purchaseOrder);

			List<ShippingToCreatePOParamDTO> shipDTOListResp = new ArrayList<ShippingToCreatePOParamDTO>();
			List<ShippingToCreatePOParamDTO> stcDTOList = pto.getListShippingToCreatePOParamDTO();
			for (ShippingToCreatePOParamDTO stpco : stcDTOList) {

				AddressBook addressBookforShipTo = null;
				if (stpco.getSaveShippingToAddress()) {
					AddressBook newAddressBook = new AddressBook();
					newAddressBook.setOrganisasi(afcoSesssion.getAfco(stpco.getAfcoId()).getOrganisasi());
					newAddressBook.setAddressLabel(stpco.getAddressLabel());
					newAddressBook.setFullName(stpco.getFullName());
					newAddressBook.setStreetAddress(stpco.getStreetAddress());
					newAddressBook.setTelephone(stpco.getTelephone1());
					addressBookSession.insertAddressBook(newAddressBook, token);
					addressBookforShipTo = newAddressBook;
				} else {
					if (stpco.getAddressBookId() != null) {
						addressBookforShipTo = addressBookSession.getAddressBook(stpco.getAddressBookId());
					}
				}

				ShippingTo shippingTo = new ShippingTo();
				shippingTo.setAddressLabel(stpco.getAddressLabel());
				shippingTo.setOrganisasi(organisasiSession.find(stpco.getAfcoId()));
				shippingTo.setFullName(stpco.getFullName());
				shippingTo.setPoNumber(stpco.getPoNumber());
				shippingTo.setStreetAddress(stpco.getStreetAddress());
				shippingTo.setTelephone1(stpco.getTelephone1());
				shippingTo.setPurchaseOrder(purchaseOrder);
				shippingTo.setAddressBook(addressBookforShipTo);
				shippingTo.setDeliveryTime(stpco.getDeliveryTime());
				shippingToSession.inserShippingTo(shippingTo, token);

				List<PurchaseOrderItemCreatePOParamDTO> poiDTOList = stpco.getListPurchaseOrderItemCreatePOParamDTO();
				for (PurchaseOrderItemCreatePOParamDTO poiDTO : poiDTOList) {
					PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
					purchaseOrderItem.setCreated(new Date());
					purchaseOrderItem.setIsDelete(0);
					purchaseOrderItem.setDeliveryTime(poiDTO.getDeliveryTime());
					purchaseOrderItem.setItem(itemSession.getItem(poiDTO.getItemId()));
					purchaseOrderItem.setItemName(poiDTO.getItemName());
					purchaseOrderItem.setPurchaseOrder(purchaseOrder);
					purchaseOrderItem.setQuantityPurchaseRequest(poiDTO.getQuantityPurchaseRequest());
					purchaseOrderItem.setQuantitySend(poiDTO.getQuantitySend());
					purchaseOrderItem.setStatus(poiDTO.getStatus());
					purchaseOrderItem.setTotalUnitPrices(poiDTO.getTotalUnitPrices());
					purchaseOrderItem.setUnitPrice(poiDTO.getUnitPrice());
					purchaseOrderItem.setVendor(vendorSession.find(poiDTO.getVendorId()));
					purchaseOrderItem.setVendorName(poiDTO.getVendorName());
					purchaseOrderItem.setStatus("Approval Process");
					purchaseOrderItem.setStatusProses(1);

					PurchaseRequestItem purchaseRequestItem = purchaseRequestItemSession
							.getPurchaseRequestItem(poiDTO.getPritemid());
					purchaseOrderItem.setPurchaseRequestItem(purchaseRequestItem);

					purchaseOrderItem.setShippingTo(shippingTo);

					purchaseOrderItem = purchaseOrderItemSession.insertPurchaseOrderItem(purchaseOrderItem, token);
					subTotalPO = subTotalPO + (poiDTO.getQuantitySend() * poiDTO.getUnitPrice());
				}

				shipDTOListResp.add(stpco);

			}

			potResp.setListShippingToCreatePOParamDTO(shipDTOListResp);
			// ubah hps menjadi harga deal dengan vendor
			// purchaseOrder.setTotalCost(subTotalPO);
			purchaseOrder.setSubTotal(subTotalPO);

			List<PurchaseOrderTerm> purchaseOrderTermList = pto.getPurchaseOrderTermList();
			if (purchaseOrderTermList != null) {
				if (purchaseOrderTermList.size() > 0) {
					for (PurchaseOrderTerm purchaseOrderTerm : purchaseOrderTermList) {
						purchaseOrderTerm.setPurchaseOrder(purchaseOrder);
						if (purchaseOrderTerm.getPoTermType() == null) {
							purchaseOrderTerm.setPoTermType(2);
						}
						purchaseOrderTerm
								.setTermandcondition(termAndConditionSession.find(pto.getTermAndConditionId()));
						purchaseOrderTermSession.inserPurchaseOrderTerm(purchaseOrderTerm, token);
					}
				}
			}

			ApprovalProcessType apt = new ApprovalProcessType();
			apt.setValueId(purchaseOrder.getId());
			Tahapan tahapan = tahapanSession.getByName(Constant.TAHAPAN_CREATE_PO);
			apt.setTahapan(tahapan);

			// cek approval baru / sudah ada dimaster
			Approval approvalPo = null;
			if (pto.getApprovalId() == null) {
				apt.setApproval(approvalPo);
				apt.setJenis(1); // 1 = serial
			} else {
				approvalPo = approvalSession.find(pto.getApprovalId());
				apt.setJenis(approvalPo.getJenis());
				apt.setApproval(approvalPo);
			}

			apt = approvalProcessTypeSession.create(apt, token);
			if (pto.getApprovalId() != null) {
				if (pto.getApprovalId() != 0) {
					List<ApprovalLevel> aplList = approvalLevelSession
							.findByApproval(approvalSession.find(pto.getApprovalId()));
					for (ApprovalLevel apl : aplList) {
						ApprovalProcess ap = new ApprovalProcess();
						ap.setApprovalProcessType(apt);
						ap.setApprovalLevel(apl);
						// cek paralel / serial
						if (apt.getJenis() == 0) {
							// serial
							if (apl.getSequence() == 1) {
								ap.setStatus(1); // set status = aktif
							} else {
								ap.setStatus(0); // set status = non aktif (0)
							}
						} else if (apt.getJenis() == 1) {
							// paralel
							ap.setStatus(1);
						}

						ap.setGroup(apl.getGroup());
						ap.setUser(apl.getUser());
						ap.setThreshold(apl.getThreshold());
						ap.setSequence(apl.getSequence());
						approvalProcessSession.create(ap, token);
					}
				}
			}

			if (pto.getNewApproval() != null) {
				ApprovalProcess ap = new ApprovalProcess();
				ap.setApprovalProcessType(apt);
				ap.setStatus(1);
				ap.setGroup(pto.getNewApproval().getOrganisasi());
				ap.setUser(pto.getNewApproval().getUser());
				ap.setSequence(1);
				approvalProcessSession.create(ap, token);
			}

			return potResp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PurchaseOrderListPagination getPODTOListWithRange(Integer startIndex, Integer endIndex, String search,
			String sort, String searchingVendor, String searchingItem) {

		String poIdNumber = "";

		/** searching by vendor **/
		if (searchingVendor != null) {
			if (!searchingVendor.isEmpty()) {
				List<PurchaseOrderItem> orderItems = purchaseOrderItemSession
						.getPurchaseOrderItemByVendorName(searchingVendor);
				for (PurchaseOrderItem item : orderItems) {
					if (!poIdNumber.contains(item.getPurchaseOrder().getId().toString())) {
						poIdNumber += "," + item.getPurchaseOrder().getId();
					}
				}
			}
		}

		/** search by item **/
		if (searchingItem != null) {
			if (!searchingItem.isEmpty()) {
				List<PurchaseOrderItem> orderItems = purchaseOrderItemSession
						.getPurchaseOrderItemBItemName(searchingItem);
				for (PurchaseOrderItem item : orderItems) {
					if (!poIdNumber.contains(item.getPurchaseOrder().getId().toString())) {
						poIdNumber += "," + item.getPurchaseOrder().getId();
					}
				}
			}
		}

		String vQuery = "SELECT po FROM PurchaseOrder po WHERE po.isDelete = 0 AND po.statusProses IN (:statusProsesList)";
		String vQueryCount = "SELECT COUNT(po) FROM PurchaseOrder po WHERE po.isDelete = 0 AND po.statusProses IN (:statusProsesList) ";
		if (search != null) {
			if (!search.equals("")) {
				vQuery = vQuery
						+ " and (po.poNumber like :keyword or po.department like :keyword or po.purchaseOrderDate like :keyword) ";
				vQueryCount = vQueryCount
						+ " and (po.poNumber like :keyword or po.department like :keyword or po.purchaseOrderDate like :keyword) ";
			}
		}

		if ((searchingVendor != null && !searchingVendor.isEmpty())
				|| (searchingItem != null && !searchingItem.isEmpty())) {
			poIdNumber = "(0" + poIdNumber + ")";
			vQuery = vQuery + " AND po.id IN" + poIdNumber;
			vQueryCount = vQueryCount + " AND po.id IN" + poIdNumber;
		}

		if (sort != null) {
			vQuery = vQuery + " order by po." + sort + " asc";
			vQueryCount = vQueryCount + " group by po.id, po.poNumber, po.status, po.department, po.purchaseOrderDate"
					+ " order by po." + sort + " asc"; // ditambah group by untuk versi mssql
		} else {
			vQuery = vQuery + " order by po.id DESC";
			vQueryCount = vQueryCount + " group by po.id, po.poNumber" + " order by po.id DESC"; // ditambah group by
																									// untuk versi mssql
		}

		Query query = em.createQuery(vQuery);
		if (search != null) {
			if (!search.equals("")) {
				search = "%" + search + "%";
				query.setParameter("keyword", search);
			}
		}

		/** add po status in process & done only **/
		List<Integer> statusProsesList = new ArrayList<Integer>();
		statusProsesList.add(Constant.PO_STATUS_PROCESS);
		statusProsesList.add(Constant.PO_STATUS_DONE);
		query.setParameter("statusProsesList", statusProsesList);

		Query queryCount = em.createQuery(vQueryCount).setParameter("statusProsesList", statusProsesList);
		if (search != null) {
			if (!search.equals("")) {
				search = "%" + search + "%";
				queryCount.setParameter("keyword", search);
			}
		}
		int totalData = Integer.parseInt(queryCount.getSingleResult().toString());

		/** range result **/
		int[] range = { startIndex - 1, endIndex - 1 };
		query.setMaxResults(range[1] - range[0] + 1);
		query.setFirstResult(range[0]);

		List<PurchaseOrder> listPurchaseOrder = query.getResultList();
		List<PurchaseOrderDTO> lpo = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder po : listPurchaseOrder) {
			List<PurchaseOrderItemDTO> orderItems = purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(po.getId());
			lpo.add(new PurchaseOrderDTO(po, orderItems));
		}

		return new PurchaseOrderListPagination(totalData, startIndex, endIndex, lpo);
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> getPurchaseOrderByPONumber(String poNumber) {
		List<PurchaseOrder> list = new ArrayList<PurchaseOrder>();
		String stringQuery = "SELECT po FROM PurchaseOrder po WHERE po.isDelete = 0  AND UPPER(po.poNumber) like :keyword";
		Query query = em.createQuery(stringQuery);
		query.setParameter("keyword", "%" + poNumber.toUpperCase() + "%");
		list = query.getResultList();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrder> getPurchaseOrderByPONum(String poNumber) {
		List<PurchaseOrder> list = new ArrayList<PurchaseOrder>();
		String stringQuery = "SELECT po FROM PurchaseOrder po WHERE po.isDelete = 0  AND po.poNumber LIKE :keyword";
		Query query = em.createQuery(stringQuery);
		String keyword = poNumber.trim();
		query.setParameter("keyword", "%" + keyword + "%");
		list = query.getResultList();

		return list;
	}

	public Vendor getPurchaseOrderVendorById(Integer id) {
		String stringQuery = "SELECT DISTINCT vendor FROM PurchaseOrderItem poi WHERE poi.purchaseOrder.id =:id";
		Query query = em.createQuery(stringQuery);
		query.setParameter("id", id);
		Vendor ven = (Vendor) query.getSingleResult();
		return (Vendor) query.getSingleResult();
	}
	
	public Vendor getPOVendorById(Integer id) {
		String stringQuery = "SELECT DISTINCT poi.vendor FROM PurchaseOrderItem poi join PurchaseOrder po on po.id = poi.purchaseOrder.id WHERE po.id =:id";
		Query query = em.createQuery(stringQuery);
		query.setParameter("id", id);
		Vendor vendor = (Vendor) query.getSingleResult();
		return vendor;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PurchaseOrder> getPurchaseOrderListByVendorForPerformance(Integer vendorId) {
		Query qm = em.createQuery(
				"SELECT vp.purchaseOrder.id FROM PenilaianVendor vp WHERE vp.vPerfIsDelete=0 AND vp.vendor.id = :vendorId ");
		qm.setParameter("vendorId", vendorId);
		List<Integer> IntegerList = qm.getResultList();
		if (IntegerList == null) {
			IntegerList = new ArrayList();
			IntegerList.add(0);
		}
		if (IntegerList.size() == 0) {
			IntegerList.add(0);
		}
		int val = 0;
		int index = 0;
		for (Integer in : IntegerList) {
			if (in == null) {
				IntegerList.set(index, 0);
				val++;
			}
			index++;
		}

		String query = "";
		if (val == index) {
			query = "SELECT DISTINCT k.purchaseOrder FROM PurchaseOrderItem k " + "WHERE  k.purchaseOrder.isDelete = 0 "
					+ "AND k.vendor.id = :vendorId " + "AND k.purchaseOrder.status = 'DONE' "
					+ "ORDER BY k.purchaseOrder.id ASC";
		} else {
			query = "SELECT DISTINCT k.purchaseOrder FROM PurchaseOrderItem k "
					+ "WHERE k.purchaseOrder.id NOT IN (:arr) " + "AND k.purchaseOrder.isDelete = 0 "
					+ "AND k.purchaseOrder.status = 'DONE' " + "AND k.vendor.id = :vendorId "
					+ "ORDER BY k.purchaseOrder.id ASC";
		}

		Query q = em.createQuery(query);
		if (val != index) {
			q.setParameter("arr", IntegerList);
		}
		q.setParameter("vendorId", vendorId);
		return q.getResultList();

	}

	@SuppressWarnings({ "unchecked" })
	public List<PurchaseOrder> getPurchaseOrderListByTermin(Integer terminFk) {
		Query q = em.createQuery(
				"SELECT t1 FROM PurchaseOrder t1 WHERE t1.isDelete = :isDelete AND t1.terminFk = :terminFk ");
		return q.setParameter("isDelete", 0).setParameter("terminFk", terminFk).getResultList();
	}

	public List<PurchaseOrderDTO> getPurchaseOrderByVendor(int id, Token token) {
		Query q = em.createNamedQuery("PurchaseOrder.find");
		q.setParameter("purchaseOrderId", id);
		List<PurchaseOrder> ll = q.getResultList();
		List<PurchaseOrderDTO> lpoi = new ArrayList<PurchaseOrderDTO>();
		for (PurchaseOrder purchaseOrder : ll) {
			lpoi.add(new PurchaseOrderDTO(purchaseOrder, null));
		}
		return lpoi;
	}

	public List<ApprovalProcess> getApprovalProcessByPO(int po) {
		List<ApprovalProcess> approvalProcessList = new ArrayList<ApprovalProcess>();
		// po tahapan = 27
		ApprovalProcessType apt = approvalProcessTypeSession.findByTahapanAndValueId("PO", po);
		if (apt != null) {
			approvalProcessList = approvalProcessSession.findByApprovalProcessType(apt.getId());
			return approvalProcessList;
		} else {
			return approvalProcessList;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void SyncronizePOApprovedDate() {
		List<PurchaseOrder> purchaseOrderList = getPurchaseOrderList();
		for (PurchaseOrder po : purchaseOrderList) {
			List<ApprovalProcess> approvalProcessList = approvalProcessSession.getApprovedDate(po.getId(), 27);
			if (approvalProcessList.size() > 0) {
				po.setApprovedDate(approvalProcessList.get(0).getUpdated());
			}
			updatePurchaseOrder(po, null);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public List<PurchaseOrder> getPurchaseOrderListWithPagination(String search, String orderKeyword, String vendorName,
			Integer start, Integer length, List<Integer> idList, RoleUser roleUser, Date startDate, Date endDate) {
		String query = "SELECT po FROM PurchaseOrder po Where po.isDelete = 0 ";
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null
				: vendorName;
		if (!search.isEmpty()) {
			query = query + "and (po.poNumber like :search) ";
		}
		if (vendorName != null) {
			query = query + "and po.vendorName =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			query = query + "and po.purchaseOrderDate >= :startDate and po.purchaseOrderDate < :endDate ";
		}
		if (startDate != null && endDate == null) {
			query = query + "and po.purchaseOrderDate >= :startDate ";
		}
		if (startDate == null && endDate != null) {
			query = query + "and po.purchaseOrderDate < :endDate ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and po.purchaseRequest IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		} else {
			query = query + "and po.purchaseRequest.organisasi.id IN (:organisasiIdList) ";
		}
		
		if (!orderKeyword.isEmpty() ) {
			query = query + "order by po." + orderKeyword;
		}else {
			query = query + " order by po.created DESC, po.updated DESC ";			
		}
		
		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		if (startDate != null && endDate == null) {
			q.setParameter("startDate", startDate);
		}
		if (startDate == null && endDate != null) {
			q.setParameter("endDate", endDate);
		}
		List<Integer> organisasiIdList = new ArrayList<Integer>();
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		}
		/* perubahan KAI 18/12/2020 filter by organisasi[20869]*/
//		else if( roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_LOGISTIK) || roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_USER) ) {
//			organisasiIdList.add(roleUser.getOrganisasi().getId());
//		} 
		else{
			List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
			for(Organisasi organisasi : organisasiList) {
				organisasiIdList.add(organisasi.getId());
			}
		}
		if(organisasiIdList.size() > 0) {
			q.setParameter("organisasiIdList", organisasiIdList);
		}
		
		q.setFirstResult(start);
		q.setMaxResults(length);
		 
		return q.getResultList();
	}

	public Long getTotalList(String search, String vendorName,
			Integer start, Integer length, List<Integer> idList, RoleUser roleUser, Date startDate, Date endDate ) {
		String query = "SELECT COUNT (po) FROM PurchaseOrder po Where po.isDelete = 0 and po.poNumber!=null ";
		if (!search.isEmpty()) {
			query = query + "and (po.poNumber like :search) ";
		}
		if (vendorName != null) {
			query = query + "and po.vendorName =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			query = query + "and po.purchaseOrderDate >= :startDate and po.purchaseOrderDate <= :endDate ";
		}
		if (startDate != null && endDate == null) {
			query = query + "and po.purchaseOrderDate >= :startDate ";
		}
		if (startDate == null && endDate != null) {
			query = query + "and po.purchaseOrderDate < :endDate ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and po.purchaseRequest IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		} else {
			query = query + "and po.purchaseRequest.organisasi.id IN (:organisasiIdList) ";
		}
		
		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		if (startDate != null && endDate == null) {
			q.setParameter("startDate", startDate);
		}
		if (startDate == null && endDate != null) {
			q.setParameter("endDate", endDate);
		}
		List<Integer> organisasiIdList = new ArrayList<Integer>();
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		}
		/* perubahan KAI 18/12/2020 filter by organisasi[20869]*/
//		else if( roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_LOGISTIK) || roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_USER) ) {
//			organisasiIdList.add(roleUser.getOrganisasi().getId());
//		} 
		else {
			List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
			for(Organisasi organisasi : organisasiList) {
				organisasiIdList.add(organisasi.getId());
			}			
		}
		if(organisasiIdList.size() > 0) {
			q.setParameter("organisasiIdList", organisasiIdList);
		}

		return (Long) q.getSingleResult();
	}

	public Double getAverageRatingVendorPO(Integer vendorId) {
		
		String query = "SELECT AVG(po.rating) FROM PurchaseOrder po "
				+ "WHERE po.rating !=null " + "AND po.isDelete = 0 "
				+ "AND po.id in "
				+ "(SELECT poi.purchaseOrder.id FROM PurchaseOrderItem poi where poi.vendor.id =:vendorId ) "
				+ "ORDER BY po.id ASC";
		
		Query q = getEntityManager().createQuery(query);
		q.setParameter("vendorId", vendorId);
		
		Double average = (Double) q.getSingleResult();
		
		return average;

	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedListPagination> getDeliveryReceivedListPagination (String search, String orderKeyword, String vendorName,
			Integer pageNo, Integer pageSize, RoleUser roleUser) {
		List<DeliveryReceivedListPagination> drList = new ArrayList<DeliveryReceivedListPagination>();
		String query = 
						"SELECT po.id, dr.id, po.purchaseRequest.organisasi.id, po.poNumber, po.purchaseRequest.organisasi.nama, "
						+ "po.vendorName, po.updated, po.purchaseRequest.status, dr.deliveryReceiptNum "
						+ "FROM PurchaseOrder po "
						+ "LEFT JOIN DeliveryReceived dr ON dr.purchaseOrder.id = po.id "
						+ "WHERE po.poNumber IS NOT NULL "
						+ "AND po.isDelete = 0 ";
		
		String roleCode = roleUser.getRole().getCode();
		Integer orgId = roleUser.getOrganisasi().getId();
		Vendor vendor = null;
		List<Organisasi> organisasiList = null;
		
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			vendor = vendorSession.getVendorByUserId(roleUser.getUser().getUserId());
			query = query + "AND po IN (SELECT poi.purchaseOrder FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 AND poi.vendor =:vendor) ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			query = query + "AND po.purchaseRequest.organisasi.id =:orgId ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			query = query + "AND po.purchaseRequest.organisasi IN (SELECT organisasi FROM Organisasi organisasi WHERE organisasi.parentId =:parentId) ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				organisasiList = organisasiSession.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
				organisasiList.add(roleUser.getOrganisasi());
				query = query + "AND po.purchaseRequest.organisasi IN (:organisasiList) ";
			}
		}
		
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null
				: vendorName;
		if (!search.equalsIgnoreCase("")) {
			query = query + "AND (po.poNumber like :search) ";
		}
		if (vendorName != null) {
			query = query + "AND po.vendorName =:vendorName ";
		}

		if (!orderKeyword.isEmpty() ) {
			query = query + "ORDER BY " + orderKeyword;
		}else {
			query = query + "order by dr.created DESC, dr.updated DESC";
		}
			
		Query q  = em.createQuery(query);
		
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			q.setParameter("vendor", vendor);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			q.setParameter("orgId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			q.setParameter("parentId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				q.setParameter("organisasiList", organisasiList);
			}
		}
					
		if (!search.equalsIgnoreCase("")) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		 
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);
		 
		List<Object[]> objList = q.getResultList();
	    
		for (Object[] obj : objList) 
		{
			DeliveryReceivedListPagination deliveryReceived = new DeliveryReceivedListPagination();
			deliveryReceived.setPoId(obj[0] != null ? (Integer)obj[0] : null);
			deliveryReceived.setDrId(obj[1] != null ? (Integer)obj[1] : null);
			deliveryReceived.setOrgId(obj[2] != null ? (Integer)obj[2] : null);
			deliveryReceived.setPoNumber(obj[3] != null ? obj[3].toString() : null);
			deliveryReceived.setNama(obj[4] != null ? obj[4].toString() : null);
			deliveryReceived.setVendorName(obj[5] != null ? obj[5].toString() : null);
			deliveryReceived.setUpdated(obj[6] != null ? (Date)obj[6] : null);
			deliveryReceived.setStatus(obj[7] != null ? (Integer)obj[7] : null);
			deliveryReceived.setDeliveryReceiptNum(obj[8] != null ? (String)obj[8] : "-");
			drList.add(deliveryReceived);
		}
		
		return drList;
	}
	
	public Long getDrTotalList(String search, String vendorName, String orderKeyword, RoleUser roleUser) {
		String query = 
						"SELECT Count(po)"
						+ "FROM PurchaseOrder po "
						+ "LEFT JOIN DeliveryReceived dr ON dr.purchaseOrder.id = po.id "
						+ "WHERE po.poNumber IS NOT NULL "
						+ "AND po.isDelete = 0 ";
		
		String roleCode = roleUser.getRole().getCode();
		Integer orgId = roleUser.getOrganisasi().getId();
		Vendor vendor = null;
		List<Organisasi> organisasiList = null;
		
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			vendor = vendorSession.getVendorByUserId(roleUser.getUser().getUserId());
			query = query + "AND po IN (SELECT poi.purchaseOrder FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 AND poi.vendor =:vendor) ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			query = query + "AND po.purchaseRequest.organisasi.id =:orgId ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			query = query + "AND po.purchaseRequest.organisasi IN (SELECT organisasi FROM Organisasi organisasi WHERE organisasi.parentId =:parentId) ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				organisasiList = organisasiSession.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
				organisasiList.add(roleUser.getOrganisasi());
				query = query + "AND po.purchaseRequest.organisasi IN (:organisasiList) ";
			}
		}
		
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null
				: vendorName;
		if (!search.equalsIgnoreCase("")) {
			query = query + "AND (po.poNumber like :search) ";
		}
		if (vendorName != null) {
			query = query + "AND po.vendorName =:vendorName ";
		}

		if (!orderKeyword.isEmpty() ) {
			query = query + "ORDER BY " + orderKeyword;
		}
			
		Query q  = em.createQuery(query);
		
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			q.setParameter("vendor", vendor);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			q.setParameter("orgId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			q.setParameter("parentId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				q.setParameter("organisasiList", organisasiList);
			}
		}
					
		if (!search.equalsIgnoreCase("")) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedDetailDTO> getDeliveryReceivedDetailProcessList (Integer poId) {
		List<DeliveryReceivedDetailDTO> drdList = new ArrayList<DeliveryReceivedDetailDTO>();
		String query = 
						"SELECT po, poi "
						+ "FROM PurchaseOrder po JOIN PurchaseOrderItem poi ON poi.purchaseOrder = po.id "
						+ "WHERE po.id = :poId "
						+ "AND po.isDelete = 0 "
						+ "AND poi.isDelete = 0 "; 
		
			
		Query q  = em.createQuery(query);
			  q.setParameter("poId", poId);
		 
		List<Object[]> objList = q.getResultList();
			    
		for (Object[] obj : objList) {
			DeliveryReceivedDetailDTO deliveryReceivedDetail = new DeliveryReceivedDetailDTO();
			deliveryReceivedDetail.setPurchaseOrder(obj[0] != null ? (PurchaseOrder)obj[0] : null);
			deliveryReceivedDetail.setPurchaseOrderItem(obj[1] != null ? (PurchaseOrderItem)obj[1] : null);
			deliveryReceivedDetail.setDikirim(0);
			drdList.add(deliveryReceivedDetail);
		}
			  
		return drdList;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedDetailDTO> getDeliveryReceivedDetailReceivedList (Integer poId, Integer drId) {
		List<DeliveryReceivedDetailDTO> drdList = new ArrayList<DeliveryReceivedDetailDTO>();
		String query = 
						"SELECT dr, dr.purchaseOrder, poi, drd.quantity "
						+ "FROM DeliveryReceived dr JOIN DeliveryReceivedDetail drd ON drd.deliveryReceived = dr.id "
						+ "JOIN PurchaseOrderItem poi ON poi.purchaseOrder = dr.purchaseOrder "
						+ "WHERE drd.poLineId = poi.id "
						+ "AND dr.purchaseOrder.id = :poId "
						+ "AND dr.id = :drId "
						+ "AND dr.isDelete = 0 "
						+ "AND poi.isDelete = 0 "
						+ "AND drd.isDelete = 0 ";
		
		Query q  = em.createQuery(query);
			  q.setParameter("poId", poId);
			  q.setParameter("drId", drId);
		 
		List<Object[]> objList = q.getResultList();
			    
		for (Object[] obj : objList) {
			DeliveryReceivedDetailDTO deliveryReceivedDetail = new DeliveryReceivedDetailDTO();
			deliveryReceivedDetail.setDeliveryReceived(obj[0] != null ? (DeliveryReceived)obj[0] : null);
			deliveryReceivedDetail.setPurchaseOrder(obj[1] != null ? (PurchaseOrder)obj[1] : null);
			deliveryReceivedDetail.setPurchaseOrderItem(obj[2] != null ? (PurchaseOrderItem)obj[2] : null);
			deliveryReceivedDetail.setDikirim(obj[3] != null ? (Integer)obj[3] : null);
			drdList.add(deliveryReceivedDetail);
		}
			  
		return drdList;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedListPagination> getDeliveryReceivedDetailReceivedData (Integer poId, Integer drId) {
		List<DeliveryReceivedListPagination> drList = new ArrayList<DeliveryReceivedListPagination>();
		String query = 
						"SELECT dr.purchaseOrder.id, dr.id, dr.purchaseOrder.purchaseRequest.slaDeliveryTime, dr.purchaseOrder.updated, dr.dateReceived, dr.description, dr.deliveryOrderNum, dr.deliveryOrderDate "
						+ "FROM DeliveryReceived dr "
						+ "WHERE dr.purchaseOrder.id = :poId "
						+ "AND dr.id = :drId "
						+ "AND dr.isDelete = 0 ";
		
			
		Query q  = em.createQuery(query);
				q.setParameter("poId", poId);
				q.setParameter("drId", drId);
		 
		List<Object[]> objList = q.getResultList();
			    
		for (Object[] obj : objList) {
			DeliveryReceivedListPagination deliveryReceived = new DeliveryReceivedListPagination();
			deliveryReceived.setPoId(obj[0] != null ? (Integer)obj[0] : null);
			deliveryReceived.setDrId(obj[1] != null ? (Integer)obj[1] : null);
			deliveryReceived.setSlaDeliveryTime(obj[2] != null ? (Integer)obj[2] : null);
			deliveryReceived.setUpdated(obj[3] != null ? (Date)obj[3] : null);
			deliveryReceived.setDateReceived(obj[4] != null ? (Date)obj[4] : null);
			deliveryReceived.setDescription(obj[5] != null ? obj[5].toString() : null);
			deliveryReceived.setDeliveryOrderNum(obj[6] != null ? obj[6].toString() : null);
			deliveryReceived.setDeliveryOrderDate(obj[7] != null ? (Date)obj[7] : null);
			drList.add(deliveryReceived);
		}
			  
		return drList;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedListPagination> getDeliveryReceivedDetailProcessData (Integer poId) {
		List<DeliveryReceivedListPagination> drList = new ArrayList<DeliveryReceivedListPagination>();
		String query = 
						"SELECT po.id, po.purchaseRequest.slaDeliveryTime, po.updated "
						+ "FROM PurchaseOrder po "
						+ "WHERE po.id = :poId "
						+ "AND po.isDelete = 0 ";
		
			
		Query q  = em.createQuery(query);
			  q.setParameter("poId", poId);
		 
		List<Object[]> objList = q.getResultList();
			    
		for (Object[] obj : objList) {
			DeliveryReceivedListPagination deliveryReceived = new DeliveryReceivedListPagination();
			deliveryReceived.setPoId(obj[0] != null ? (Integer)obj[0] : null);
			deliveryReceived.setSlaDeliveryTime(obj[1] != null ? (Integer)obj[1] : null);
			deliveryReceived.setUpdated(obj[2] != null ? (Date)obj[2] : null);
			drList.add(deliveryReceived);
		}
			  
		return drList;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedFiles> getDeliveryReceivedDetailFileData (Integer poId, Integer drId) {
		List<DeliveryReceivedFiles> drfList = new ArrayList<DeliveryReceivedFiles>();
		String query = 
				"SELECT drf.receiptFileName, drf.id "
				+ "FROM DeliveryReceived dr "
				+ "JOIN DeliveryReceivedFiles drf ON drf.deliveryReceived.id = dr.id "
				+ "WHERE dr.purchaseOrder.id = :poId "
				+ "AND dr.id = :drId "
				+ "AND dr.isDelete = 0 ";
		
			
		Query q  = em.createQuery(query);
		q.setParameter("poId", poId);
		q.setParameter("drId", drId);
 
		List<Object[]> objList = q.getResultList();
		for (Object[] obj : objList) {
			DeliveryReceivedFiles deliveryReceivedFiles = new DeliveryReceivedFiles();
			deliveryReceivedFiles.setReceiptFileName(obj[0] != null ? obj[0].toString() : null);
			drfList.add(deliveryReceivedFiles);
		}
			  
		return drfList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getPurchaseOrderByPOId(Integer poId) {
		String stringQuery = "SELECT "
				+ "pr, po, dr, poItem, v, item, catalog, satuan, ab "
				+ "FROM PurchaseRequest pr, "
				+ "PurchaseOrder po, "
				+ "PurchaseOrderItem poItem, "
				+ "Vendor v, "
				+ "Item item, "
				+ "Catalog catalog, "
				+ "Satuan satuan, "
				+ "AddressBook ab "
				+ "LEFT JOIN DeliveryReceived dr ON dr.purchaseOrder.id = po.id "
				+ "WHERE po.purchaseRequest.id = pr.id "
				+ "AND poItem.purchaseOrder.id = po.id "
				+ "AND v.id = poItem.vendor.id "
				+ "AND item.id = poItem.item.id "
				+ "AND catalog.item.id = item.id "
				+ "AND satuan.id = item.satuanId.id "
				+ "AND ab.id = pr.addressBook.id "
				+ "AND po.isDelete = 0 "
				+ "AND po.id =:poId ";
		Query query = em.createQuery(stringQuery);
		query.setParameter("poId", poId);
		List<Object[]> result = (List<Object[]>) 
				query.getResultList();
		return result;
	}

}
