package id.co.promise.procurement.approval;

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

import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.ApprovalProcessVendor;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.ApprovalTahapanDetail;
import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Threshold;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.ApprovalProcessVendorSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.ThresholdSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class ApprovalProcessSession extends AbstractFacadeWithAudit<ApprovalProcess> {

	final static Logger log = Logger.getLogger(ApprovalProcessSession.class);

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

	@EJB
	private TokenSession tokenSession;
	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;
	@EJB
	private PurchaseRequestSession prSession;
	@EJB
	private OrganisasiSession organisasiSession;
	
	@EJB
	private ApprovalTahapanSession approvalTahapanSession;
	@EJB
	private ApprovalTahapanDetailSession approvalTahapanDtlSession;
	@EJB
	private ApprovalLevelSession approvalLevelSession;
	
	@EJB
	private ThresholdSession thresholdSession;
	
	@EJB
	private ApprovalProcessSession approvalProcessSession;
	
	@EJB
	private PurchaseOrderSession poSession;
	@EJB
	private RoleUserSession roleUserSession;
	@EJB
	private EmailNotificationSession emailNotificationSession;
	@EJB
	private VendorSession vendorSession;
	@EJB
	private UserSession userSession;
	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;
	@EJB 
	private PurchaseRequestItemSession prItemSession;
	
	@EJB 
	private PurchaseRequestSession purchaseRequestSession;
	
	@EJB
	private TahapanSession tahapanSession;

	@EJB
	CatalogSession catalogSession;
	
	@EJB
	ApprovalProcessVendorSession approvalProcessVendorSession;
	
	public PurchaseRequest findPurchaseRequestById(int id) {
		return prSession.find(id);
	}
	
	public ApprovalProcessSession() {
		super(ApprovalProcess.class);
	}
	
	@SuppressWarnings("unchecked") 
	public List <ApprovalProcess> getByApprovalProcess(Integer approvalProcessId) { 
			 Query q = em.createNamedQuery("ApprovalProcess.findByApprovalProcess");
		 q.setParameter("approvalProcessTypeId", approvalProcessId); 
			 return q.getResultList(); 
		 }
	 
	public List<ApprovalProcessPRItemCostValidationDTO> findPurchaseRequestItemByPR(int prId) {
		List<ApprovalProcessPRItemCostValidationDTO> resultList = new ArrayList<ApprovalProcessPRItemCostValidationDTO>();
		List<PurchaseRequestItem> prItemList = prItemSession.getPurchaseRequestItemByPurchaseRequestId(prId);

		Map<String, Double> costMap = new HashMap<String, Double>();
		int index = 0;
		for (PurchaseRequestItem purchaseRequestItem : prItemList) {
			ApprovalProcessPRItemCostValidationDTO newPRItem = new ApprovalProcessPRItemCostValidationDTO();
			// get anggaran
			List<AlokasiAnggaran> alokasiAnggaranList = alokasiAnggaranSession
					.findByNomorDraft(purchaseRequestItem.getCostcenter());
			if (alokasiAnggaranList.size() > 0) {
				if (index == 0) {
					costMap.put(purchaseRequestItem.getCostcenter(), purchaseRequestItem.getTotal());
					if (purchaseRequestItem.getTotal() > alokasiAnggaranList.get(0).getSisaAnggaran()) {
						newPRItem.setCostAvailable(false);
					} else {
						newPRItem.setCostAvailable(true);
					}
				} else {
					// jika sudah ada
					if (costMap.containsKey(purchaseRequestItem.getCostcenter())) {
						double newTotalItem = costMap.get(purchaseRequestItem.getCostcenter())
								+ purchaseRequestItem.getTotal();
						costMap.put(purchaseRequestItem.getCostcenter(), newTotalItem);
						if (newTotalItem > alokasiAnggaranList.get(0).getSisaAnggaran()) {
							newPRItem.setCostAvailable(false);
						} else {
							newPRItem.setCostAvailable(true);
						}
					} else {
						costMap.put(purchaseRequestItem.getCostcenter(), purchaseRequestItem.getTotal());
						if (purchaseRequestItem.getTotal() > alokasiAnggaranList.get(0).getSisaAnggaran()) {
							newPRItem.setCostAvailable(false);
						} else {
							newPRItem.setCostAvailable(true);
						}
					}
				}
				newPRItem.setPurchaseRequestItem(purchaseRequestItem);
				resultList.add(newPRItem);
			} else {
				newPRItem.setPurchaseRequestItem(purchaseRequestItem);
				newPRItem.setCostAvailable(false);
				resultList.add(newPRItem);
			}

			index++;
		}

		return resultList;
	}
	
	public PurchaseOrder findPurchaseOrderById(int id) {
		return poSession.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<PurchaseOrderItem> findPurchaseOrderItemByPO(int poId) {
		Query q = em.createNamedQuery("PurchaseOrderItem.findByPOId");
		q.setParameter("purchaseOrderId", poId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> getLogUserList(Integer bookingOerderId) {
		Query q = em.createNamedQuery("ApprovalProcess.findApprovalLogUserList");
		q.setParameter("valueId", bookingOerderId);
		//KAI - 20201125 - ##20503
		q.setParameter("namaTahapan", Constant.TAHAPAN_APPROVAL_BO);
		return q.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> getApprovalInternalLogList(Integer approvalProcessTypeId) {
		Query q = em.createNamedQuery("ApprovalProcess.findApproveProcessId");
		q.setParameter("approvalProcessTypeId", approvalProcessTypeId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> getList() {
		Query q = em.createNamedQuery("ApprovalProcess.find");
		return q.getResultList();
	}

	public ApprovalProcess getById(Integer id) {
		return super.find(id);
	}
	
	public List<AlokasiAnggaran> findCostAllocation(String coa) {
		return alokasiAnggaranSession.findByNomorDraft(coa);
	}

	public ApprovalProcess create(ApprovalProcess approvalProcess, Token token) {
		approvalProcess.setCreated(new Date());
		approvalProcess.setIsDelete(0);
		if ( approvalProcess.getUserId() == null) {
			approvalProcess.setUserId(token.getUser().getId());
		}
		super.create(approvalProcess, AuditHelper.OPERATION_CREATE, token);
		return approvalProcess;
	}

	public ApprovalProcess update(ApprovalProcess approvalProcess, Token token) {
		approvalProcess.setUpdated(new Date());
		approvalProcess.setUserId(token.getUser().getId());
		super.edit(approvalProcess, AuditHelper.OPERATION_CREATE, token);
		return approvalProcess;
	}

	public ApprovalProcess delete(ApprovalProcess approvalProcess, Token token) {
		approvalProcess.setDeleted(new Date());
		approvalProcess.setIsDelete(1);
		approvalProcess.setUserId(token.getUser().getId());
		super.remove(approvalProcess, AuditHelper.OPERATION_CREATE, token);
		return approvalProcess;
	}

	public void deleteByApprovalProcessType(int approvalProcessTypeId, Token token) {
		List<ApprovalProcess> appProcessList = findByApprovalProcessType(approvalProcessTypeId);
		for (ApprovalProcess approvalProcess : appProcessList) {
			delete(approvalProcess, token);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> findByApprovalProcessType(int approvalProcessTypeId) {
		Query q = em.createNamedQuery("ApprovalProcess.findByApprovalProcessType");
		q.setParameter("approvalProcessTypeId", approvalProcessTypeId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> findByApprovalProcessTypeAndStatus(int approvalProcessTypeId) {
		Query q = em.createNamedQuery("ApprovalProcess.findByApprovalProcessTypeStatus");
		q.setParameter("approvalProcessTypeId", approvalProcessTypeId);
		/* q.setParameter("status", status); */

		List<ApprovalProcess> appProcessList = q.getResultList();

		int index = 0;
		for (ApprovalProcess approvalProcess : appProcessList) {
			// get user
			User userObj = new User();
			if (approvalProcess.getUser() == null) {
				User user = userSession.getUser(approvalProcess.getUserId());

				userObj.setId(user.getId());
				userObj.setNamaPengguna(user.getNamaPengguna());
				approvalProcess.setUser(userObj);
				appProcessList.set(index, approvalProcess);
			} else {
				userObj = approvalProcess.getUser();
			}

			if (approvalProcess.getGroup() == null) {
				List<RoleUser> roleUser = roleUserSession.getRoleUserByUserId(userObj.getId());
				if (roleUser.size() > 0) {
					approvalProcess.setGroup(roleUser.get(0).getOrganisasi());
				}
			}

			index++;
		}

		return appProcessList;
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> findByApprovalProcessTypeAndAllStatus(int approvalProcessTypeId) {
		List<ApprovalProcess> appProcessList = findByApprovalProcessType(approvalProcessTypeId);
		int index = 0;
		for (ApprovalProcess approvalProcess : appProcessList) {
			// get user
			User userObj = new User();
			if (approvalProcess.getUser() == null) {
				User user = userSession.getUser(approvalProcess.getUserId());

				userObj.setId(user.getId());
				userObj.setNamaPengguna(user.getNamaPengguna());
				approvalProcess.setUser(userObj);
				appProcessList.set(index, approvalProcess);
			} else {
				userObj = approvalProcess.getUser();
			}

			if (approvalProcess.getGroup() == null) {
				List<RoleUser> roleUser = roleUserSession.getRoleUserByUserId(userObj.getId());
				if (roleUser.size() > 0) {
					approvalProcess.setGroup(roleUser.get(0).getOrganisasi());
				}
			}
		}

		return appProcessList;
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> findApprovalProcessByStatus(int approvalProcessTypeId, int status) {
		Query q = em.createNamedQuery("ApprovalProcess.findByApprovalProcessTypeAndStatus");
		q.setParameter("status", status);
		q.setParameter("approvalProcessTypeId", approvalProcessTypeId);
		return q.getResultList();
	}

	public ApprovalProcess findByApprovalProcessTypeAndSequence(ApprovalProcessType approvalProcessType,
			Integer sequence) {
		Query q = em.createNamedQuery("ApprovalProcess.findByApprovalProcessTypeAndSequence");
		q.setParameter("sequence", sequence);
		q.setParameter("approvalProcessType", approvalProcessType);
		try {
			return (ApprovalProcess) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcessDTO> findByUserGroupAndActive(Token token) {
		User user = token.getUser();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(user.getId());
		List<Integer> organisasiIdList = new ArrayList<Integer>();
		List<Integer> roleIdList = new ArrayList<Integer>();
		for (RoleUser ru : roleUserList) {
			organisasiIdList.add(ru.getOrganisasi().getId());
			roleIdList.add(ru.getRole().getId());
		}
		String query = "SELECT " + "T1.* " + "FROM " + "T2_APPROVAL_PROCESS T1 "
				+ "LEFT JOIN T2_APPROV_PROCESS_TYPE T2 ON T2.APPROVAL_PROCESS_TYPE_ID = T1.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN T2_APPROVAL T3 ON T3.APPROVAL_ID = T2.APPROVAL_ID "
				+ "LEFT JOIN T3_APPROVAL_LEVEL T4 ON T4.APPROVAL_LEVEL_ID = T1.APPROVAL_LEVEL_ID " + "WHERE "
				+ "T1.ISDELETE = 0 " + "AND T1.STATUS IN (1, 4) " +
				// "AND T2.TAHAPAN_ID IN (26, 27) " + // PO & PR
				"AND T1.APPROVAL_PROCESS_ID = " + "( " + "CASE " + "WHEN T2.JENIS = 0 THEN " + // SERIAL
				"( " + "SELECT " + "MIN(X1.APPROVAL_PROCESS_ID) " + "FROM " + "T2_APPROVAL_PROCESS X1 "
				+ "LEFT JOIN T2_APPROV_PROCESS_TYPE X2 ON X2.APPROVAL_PROCESS_TYPE_ID = X1.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN T2_APPROVAL X3 ON X3.APPROVAL_ID = X2.APPROVAL_ID " + "WHERE " + "X1.ISDELETE = 0 "
				+ "AND X1.STATUS IN (1, 4) " +
				// "AND X2.TAHAPAN_ID IN (26, 27) " +
				"AND X2.VALUE_ID = T2.VALUE_ID AND X2.APPROVAL_PROCESS_TYPE_ID=T2.APPROVAL_PROCESS_TYPE_ID " + ") "
				+ "ELSE " + // PARALEL
				"T1.APPROVAL_PROCESS_ID " + "END " + ") " + "AND ( " + "T1.ORGANISASI_ID IN (:organisasiId) "
				+ "OR T1.USER_ID = :userId " + "OR T4.ROLE_ID IN (:roleId) " + ") " + "ORDER BY T1.CREATED DESC";

		/*
		 * for (Integer roleId : roleIdList) {
		 * 
		 * System.out.println("Role id = " + roleId); }
		 * 
		 * for(Integer organisasiId: organisasiIdList) {
		 * System.out.println("organisasi id = " + organisasiId); }
		 * 
		 * System.out.println("user id = " + user.getId());
		 */

		Query queryes = em.createNativeQuery(query, "findByUserGroupAndActiveParameter")
				.setParameter("organisasiId", organisasiIdList).setParameter("userId", user.getId())
				.setParameter("roleId", roleIdList);

		List<ApprovalProcess> rsApprovalList = queryes.getResultList();

		// filter
		// cek type approval role/organisasi/user
		try {
			List<ApprovalProcess> approvalListFilter = new ArrayList<ApprovalProcess>();
			for (ApprovalProcess approvalProcess : rsApprovalList) {
				String approvalType = approvalProcess.getApprovalProcessType().getApproval().getApprovalType().getName()
						.toUpperCase();
				if (approvalType.equalsIgnoreCase("USER")) { // 2= user
					if (approvalProcess.getUser().getId().equals( user.getId())) {
						approvalListFilter.add(approvalProcess);
					}
				} else {
					approvalListFilter.add(approvalProcess);
				}
			}

			rsApprovalList = approvalListFilter;
		} catch (Exception e) {
			System.err.println("error : " + e);
		}

		List<ApprovalProcessDTO> newApprovalList = new ArrayList<ApprovalProcessDTO>();
		for (ApprovalProcess approvalProcess : rsApprovalList) {
			// System.out.println("Approval Process " + approvalProcess.getId());

			ApprovalProcessDTO newApprovalProcess = new ApprovalProcessDTO();
			newApprovalProcess.setApprovalProcess(approvalProcess);

			if (approvalProcess.getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_BO)) { // 26
				// Purchase
				// Request
				try {
					String description;
					PurchaseRequest pr = prSession.find(approvalProcess.getApprovalProcessType().getValueId());
					if (pr.getDescription() == null) {
						description = "";
					} else {
						description = " - " + pr.getDescription();
					}
					newApprovalProcess.setKeteranganIndex(pr.getPrnumber() + description);
					newApprovalList.add(newApprovalProcess);
				} catch (Exception e) {
					System.err.println("No Data PR with ID : " + approvalProcess.getApprovalProcessType().getValueId());
				}
			} else if (approvalProcess.getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_CREATE_PO)) { // Purchase
				// Order
				PurchaseOrder po = poSession.find(approvalProcess.getApprovalProcessType().getValueId());
				if (po != null) {
					if (po.getNotes() == null) {
						newApprovalProcess.setKeteranganIndex(po.getPoNumber());
					} else {
						newApprovalProcess.setKeteranganIndex(po.getPoNumber() + " - " + po.getNotes());
					}
					newApprovalList.add(newApprovalProcess);
				}
			}

			/*
			 * else if (approvalProcess.getApprovalProcessType().getTahapan().getId() == 29)
			 * { // Registrasi Vendoir // Vendor // get Data Vendor Vendor vendor =
			 * vendorSession.getVendor(approvalProcess.getApprovalProcessType().getValueId()
			 * ); newApprovalProcess.setKeteranganIndex(vendor.getNama());
			 * newApprovalList.add(newApprovalProcess); } else if
			 * (approvalProcess.getApprovalProcessType().getTahapan().getId() == 31) { //
			 * Blacklist Vendor // get Data Vendor Vendor vendor =
			 * vendorSession.getVendor(approvalProcess.getApprovalProcessType().getValueId()
			 * ); newApprovalProcess.setKeteranganIndex(vendor.getNama());
			 * newApprovalList.add(newApprovalProcess); } else if
			 * (approvalProcess.getApprovalProcessType().getTahapan().getId() == 32) { //
			 * Unblacklist Vendor // get Data Vendor Vendor vendor =
			 * vendorSession.getVendor(approvalProcess.getApprovalProcessType().getValueId()
			 * ); newApprovalProcess.setKeteranganIndex(vendor.getNama());
			 * newApprovalList.add(newApprovalProcess); } else if
			 * (approvalProcess.getApprovalProcessType().getTahapan().getId() == 36) { //
			 * Perpanjangan Tdr // get Data Vendor Vendor vendor =
			 * vendorSession.getVendor(approvalProcess.getApprovalProcessType().getValueId()
			 * ); newApprovalProcess.setKeteranganIndex(vendor.getNama());
			 * newApprovalList.add(newApprovalProcess); } else { Vendor vendor =
			 * vendorSession.getVendor(approvalProcess.getApprovalProcessType().getValueId()
			 * ); newApprovalProcess.setKeteranganIndex(vendor.getNama());
			 * newApprovalList.add(newApprovalProcess); }
			 */
		}

		return newApprovalList;
	}

	public Vendor findVendorById(int id) {
		return vendorSession.find(id);
	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException if element is detected null
	 */
	public ApprovalProcess updateStatus(int approvalProcessId, int status, String note, Token token) {
		ApprovalProcess ap = super.find(approvalProcessId);
		ap.setStatus(status);
		ap.setUpdated(new Date());
		ap.setKeterangan(note);
		ap.setUserId(token.getUser().getId());
		super.edit(ap, AuditHelper.OPERATION_UPDATE, token);

		if (status == 3) { // approve (3), next squence update aktif
			// find is has squence
			List<ApprovalProcess> approvalProcessList = findByApprovalProcessType(ap.getApprovalProcessType().getId());
			if (approvalProcessList.size() > 0) {
				if (approvalProcessList.size() == ap.getSequence()) {
					// Email Notification
					ApprovalProcessType approvalProcessType = ap.getApprovalProcessType();
				}
				for (ApprovalProcess approvalProcess : approvalProcessList) {
					if (approvalProcess.getSequence() > ap.getSequence()) { // jika
																			// masih
																			// ada
																			// squence
																			// aktifkan
						ApprovalProcess apNew = super.find(approvalProcess.getId());
						apNew.setStatus(1); // 1 aktif
						apNew.setUpdated(new Date());
						apNew.setKeterangan("");
						apNew.setUserId(token.getUser().getId());
						super.edit(apNew, AuditHelper.OPERATION_UPDATE, token);
						break;
					}

				}
			}
		}
		return ap;
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcess> getApprovedDate(int value, int tahapanId) {
		Query q = em.createNamedQuery("ApprovalProcess.getApprovalDate");
		q.setParameter("value", value);
		q.setParameter("tahapanId", tahapanId);
		q.setMaxResults(1);
		return q.getResultList();
	}

	public int countByUserGroupAndActive(Token token) {
		return findByUserGroupAndActive(token).size();
	}
	
	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException if element is detected null
	 */

	/* untuk KAI 2-11-2020 */
	public List<ApprovalProcessDTO> getMyWorkList(Token token){
		User user = token.getUser();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(user.getId());
		List<ApprovalProcessDTO> approvalProcessDTOList = new ArrayList<ApprovalProcessDTO>();
		/* Jabatan jabatan = new Jabatan(); */
		Organisasi organisasi = new Organisasi();
		try{
			if(roleUserList.size() > 0) {
				organisasi = roleUserList.get(0).getOrganisasi();
				/* jabatan = user.getJabatan(); */
				/* untuk KAI 29-01-2021 [21865] */
				approvalProcessDTOList=getByUserAndOrganisasi(organisasi,user);
				List<ApprovalProcessDTO> approvalProcessDTOListNew = new ArrayList<>();
				for(ApprovalProcessDTO approvalProcessDTO : approvalProcessDTOList) {
					if (approvalProcessDTO.getApprovalProcess().getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG)
							|| approvalProcessDTO.getApprovalProcess().getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG)) {
						Catalog catalog = catalogSession.getCatalog(approvalProcessDTO.getApprovalProcess().getApprovalProcessType().getValueId());
						Date today = new Date();
						if(catalog.getIsDelete().equals(0)) {
							if(!(catalog.getCatalogKontrak().getTglMulailKontrak().after(today) || catalog.getCatalogKontrak().getTglAkhirKontrak().before(today))) {
								approvalProcessDTOListNew.add(approvalProcessDTO);
							}							
						}
					}else {
						approvalProcessDTOListNew.add(approvalProcessDTO);
					}
				}
				approvalProcessDTOList = approvalProcessDTOListNew;
			}
		}catch(Exception e) {
			log.error(e.getCause());
		}
		return approvalProcessDTOList;
	}
	
	/* untuk KAI 2-11-2020 */
	@SuppressWarnings("unchecked")
	private List<ApprovalProcessDTO> getByUserAndOrganisasi(Organisasi organisasi,User user) {	
		String strQuery = "select ap from ApprovalProcess ap where ap.status = 1 "
				+ "and ap.isDelete = 0 and ap.user =:user and ap.group =:organisasi "
				//KAI 07052021
				+ "and ap.approvalProcessType.id = ( SELECT max(t3.id) FROM ApprovalProcessType t3 where t3.valueId = ap.approvalProcessType.valueId) "
				+ "order by ap.created DESC, ap.updated DESC";
		
		Query query = em.createQuery(strQuery)
					.setParameter("organisasi", organisasi)
					.setParameter("user", user);

		List<ApprovalProcess> approvalProcessList = query.getResultList();
		
		List<ApprovalProcessDTO> approvalProcessDTOList = new ArrayList<ApprovalProcessDTO>();
		for (ApprovalProcess approvalProcess : approvalProcessList) {
			// System.out.println("Approval Process " + approvalProcess.getId());

			ApprovalProcessDTO newApprovalProcess = new ApprovalProcessDTO();
			newApprovalProcess.setApprovalProcess(approvalProcess);

			if (approvalProcess.getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_BO)) {
				try {
					String description;
					PurchaseRequest pr = prSession.find(approvalProcess.getApprovalProcessType().getValueId());
					if (pr.getDescription() == null) {
						description = "";
					} else {
						description = " - " + pr.getDescription();
					}
					newApprovalProcess.setKeteranganIndex(pr.getBoNumber() + description);
					approvalProcessDTOList.add(newApprovalProcess);
				} catch (Exception e) {
					System.err.println("No Data PR with ID : " + approvalProcess.getApprovalProcessType().getValueId());
				}
			}else {
				if (approvalProcess.getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG)) {
					try {
						String description;
						String descriptionKode;
						Catalog catalog = catalogSession.getCatalog(approvalProcess.getApprovalProcessType().getValueId());
						if (catalog.getCatalogKontrak().getStatusKontrak() == Constant.CATALOG_KONTRAK_STATUS_ACTIVE) {
							if (catalog.getNamaIND() == null) {
								description = "";
							} else {
								description = catalog.getNamaIND();
							}
							/* KAI - 20201130 - #19643 */
							if (catalog.getKodeProduk() == null) {
								descriptionKode = "";
							} else {
								descriptionKode = catalog.getKodeProduk() + " - ";
							}
							newApprovalProcess.setKeteranganIndex(descriptionKode + description);
							approvalProcessDTOList.add(newApprovalProcess);
						}
					} catch (Exception e) {
						System.err.println("No Data Catalog with ID : " + approvalProcess.getApprovalProcessType().getValueId());
					}
				}else if(approvalProcess.getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG)) {
					try {
						String description;
						String descriptionKode;
						Catalog catalog = catalogSession.getCatalog(approvalProcess.getApprovalProcessType().getValueId());
						if (catalog.getCatalogKontrak().getStatusKontrak() == Constant.CATALOG_KONTRAK_STATUS_ACTIVE) {
							if (catalog.getNamaIND() == null) {
								description = "";
							} else {
								description = catalog.getNamaIND();
							}
							/* KAI - 20201130 - #19643 */
							if (catalog.getKodeProduk() == null) {
								descriptionKode = "";
							} else {
								descriptionKode = catalog.getKodeProduk() + " - ";
							}
							newApprovalProcess.setKeteranganIndex(descriptionKode + description);
							approvalProcessDTOList.add(newApprovalProcess);
						}
					} catch (Exception e) {
						System.err.println("No Data Catalog with ID : " + approvalProcess.getApprovalProcessType().getValueId());
					}
				}
			}
		}

		return approvalProcessDTOList;
	}

	@SuppressWarnings("unchecked")
	public Boolean doCreateApproval(int idObject, String tahapan, Token token, Double harga) {
		RoleUser roleUser = roleUserSession.getByToken(token);
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(token);
		
		List<PurchaseRequestItem> prItemList = new ArrayList();
		Catalog catalog = new Catalog();
		PurchaseRequest purchaseRequest = new PurchaseRequest();
		
		//Penyesuaian flow, karena ada penambahan approval di sisi vendor
		//Jika tahapan approval BO maka user dan organisasi nya diambil dari user yg membuat Booking Order (BO)/ Purchase Order
		if(tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_BO)) {
			purchaseRequest = purchaseRequestSession.get(idObject);
			
			roleUser = roleUserSession.getByUserId(purchaseRequest.getUserId());
			organisasi = organisasiSession.getOrganisasi(purchaseRequest.getOrganisasi().getId());
		} else if (tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG) || tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG)) {
			catalog = catalogSession.find(idObject);
		}
		
		Approval approval = new Approval();
		ApprovalTahapan approvalTahapan = new ApprovalTahapan();
		if(!roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR) ) {
			approvalTahapan = approvalTahapanSession.getApprovalTahapanByOrganisasiAndTahapan(organisasi.getId(), tahapan);
		}else{
			approvalTahapan = approvalTahapanSession.getApprovalTahapanByTahapan(tahapanSession.getByName(tahapan));
		}
		
		if ( approvalTahapan != null ) {
			List <ApprovalTahapanDetail> approvalTahapanDetailList = approvalTahapanDtlSession.getListByApprovalTahapanId(approvalTahapan.getId());
			//Approval Threshold
			Integer jenis=null;
			if (harga != null) {
				Double hargaThreshold = harga;
				Double start = null;
				int index = 0;
				for (ApprovalTahapanDetail approvalTahapanDetail : approvalTahapanDetailList ) {
					jenis = approvalTahapanDetail.getApproval().getJenis();
					if (index == 0 & approvalTahapanDetail.getEndRange() != null ) {
						start = 0.0d;
					}
			
					Double end = approvalTahapanDetail.getEndRange();					
					
					if ( hargaThreshold >= start ) {
						approval = approvalTahapanDetail.getApproval();
						break;
					}
					
					start = approvalTahapanDetail.getEndRange();
					index++;
				}	
			} else {//Approval Normal
				 approval = approvalTahapanDetailList.get(0).getApproval();	
				 jenis = approvalTahapanDetailList.get(0).getApproval().getJenis();
			}
			
			ApprovalProcessType AppProcType = new ApprovalProcessType();
			AppProcType.setValueId(idObject);
			AppProcType.setTahapan(approvalTahapan.getTahapan());
			AppProcType.setApproval( approval);
			AppProcType.setJenis(jenis);
			
			AppProcType = approvalProcessTypeSession.create(AppProcType, token);

			List<ApprovalLevel> approvalLevelList = approvalLevelSession.findByApproval( approval);
				for (ApprovalLevel approvalLevel : approvalLevelList) {
					ApprovalProcess ap = new ApprovalProcess();
					ap.setApprovalProcessType(AppProcType);
					ap.setApprovalLevel(approvalLevel);

					// cek paralel / serial
					if (AppProcType.getJenis() == 0) {
						// serial
						if (approvalLevel.getSequence() == 1) {
							ap.setStatus(1); // set status = aktif
							if(tahapan.equals(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG)) {
								emailNotificationSession.getMailGeneratorAddEditManageCatalog(catalog, approvalLevel.getUser(), Constant.NOTIF_EMAIL_AKSI_ADD_CATALOG);	
							}else if(tahapan.equals(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG)) {
								emailNotificationSession.getMailGeneratorAddEditManageCatalog(catalog, approvalLevel.getUser(), Constant.NOTIF_EMAIL_AKSI_EDIT_CATALOG);
							}
						} else {
							ap.setStatus(0); // set status = non aktif (0)
						}
					} else if (AppProcType.getJenis() == 1) {
						// paralel
						ap.setStatus(1);
					}

					// ap.setKeterangan();
					ap.setGroup(approvalLevel.getGroup());
					ap.setUser(approvalLevel.getUser());
					ap.setUserId(approvalLevel.getUser().getId());
					// ap.setRole(apl.getRole());
					ap.setThreshold(approvalLevel.getThreshold());
					ap.setSequence(approvalLevel.getSequence());
//					ap.setJabatan(approvalLevel.getJabatan());
					approvalProcessSession.create(ap, token);
					
					if(tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_BO)) {
						try {
							String statusEmail ="";
							String note="";
							User userLogin = token.getUser();
							if(approvalLevel.getUser() != null && organisasi != null) {								
								purchaseRequest = purchaseRequestSession.get(idObject);
								if(purchaseRequest != null) {
									ApprovalProcessVendor approvalProcessVendor = approvalProcessVendorSession.getApprovalProcessVendorByPurchaseRequestId(purchaseRequest.getId());
									if(approvalProcessVendor.getStatus() == 3) {
										statusEmail = "Disetujui";
										note = approvalProcessVendor.getKeterangan();
										List<RoleUser> roleUserList = roleUserSession.getUserAndOrganisasi(approvalLevel.getUser().getId(), organisasi.getId());
										List<String> emailList = new ArrayList<String>();
										for(RoleUser roleU : roleUserList) {
											emailList.add(roleU.getUser().getEmail());
										}
										for(String email : emailList) {
											emailNotificationSession.getMailPersetujuanBO(purchaseRequest, statusEmail, note, userLogin.getNamaPengguna(), email);
										}
									}
								}
							}
						} catch (Exception e) {
							return false;
						}
					}
				}
			
		} else {
			//notif "seting duulu woy Approvalnya"
			log.info("seting duulu Approvalnya");
			return false;
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	public Boolean doProcess(ApprovalProcess approvalProcess, Integer status, Token token, String note) {
		Boolean isValid = true;
		List<PurchaseRequestItem> prItemList = new ArrayList();
		Catalog catalog = new Catalog();
		PurchaseRequest purchaseRequest = new PurchaseRequest();
		List <ApprovalProcess> approvalProcessList = findByApprovalProcessType(approvalProcess.getApprovalProcessType().getId());
		ApprovalProcessType approvalProcessType = approvalProcess.getApprovalProcessType();
		String tahapan = approvalProcessType.getTahapan().getNama();
			if (status == 1) { //yes
				// paralel
				approvalProcess.setStatus(3);
				approvalProcess.setKeterangan(note);
				approvalProcess.setUser(token.getUser());//User yang melakukan approve
				update(approvalProcess, token);
				
				try {
					ApprovalProcess nextApprovalProcess = approvalProcessList.get(approvalProcess.getSequence());
					if ( nextApprovalProcess.getStatus() == 0 ) { //serial
						nextApprovalProcess.setStatus(1);
						if(tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG) || tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG)  ) {
							purchaseRequest = purchaseRequestSession.get(approvalProcessType.getValueId());
							prItemList = prItemSession.getByPurchaseRequest(purchaseRequest);
							catalog = catalogSession.find(prItemList.get(0).getCatalog().getId());
							emailNotificationSession.getMailGeneratorApprovalMidleAndLast(catalog, status, approvalProcessList );
						}
						update(nextApprovalProcess, token);
						
						if(approvalProcessType.getTahapan().getId().equals(Constant.TAHAPAN_BOOKING_ORDER)) {
							try {
								if(status == 3) {
									String statusEmail = "Disetujui";
									User userLogin = token.getUser();
									purchaseRequest = purchaseRequestSession.get(approvalProcessType.getValueId());
									List<RoleUser> roleUserList = roleUserSession.getUserAndOrganisasi(nextApprovalProcess.getUser().getId(), nextApprovalProcess.getGroup().getId());
									List<String> emailList = new ArrayList<String>();
									for(RoleUser roleU : roleUserList) {
										emailList.add(roleU.getUser().getEmail());
									}
									for(String email : emailList) {
										emailNotificationSession.getMailPersetujuanBO(purchaseRequest, statusEmail, note, userLogin.getNamaPengguna(), email);
									}
								}
							} catch (Exception e) {
								isValid = false;
							}
						}
					}
					
				} catch (Exception e) {
					
				}

				Boolean isFinish = checkApprovalProces(approvalProcess);
				if (isFinish) {
					approvalProcess.setKeterangan(note);
					approvalProcessType.setStatus(3); //Approved Selesai
					if(tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG) || tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG)  ) {
						catalog = catalogSession.find(approvalProcessType.getValueId());
						emailNotificationSession.getMailGeneratorApprovalMidleAndLast(catalog, status, approvalProcessList);
					}
					approvalProcessTypeSession.update(approvalProcessType, token);
					
				} else {
					isValid = false;
				}
				
			} else {//no
				
				approvalProcess.setStatus(2);
				approvalProcess.setKeterangan(note);
				approvalProcess.setUser(token.getUser());//User yang melakukan reject
				approvalProcessType.setStatus(2);// Approved reject
				if(tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG) || tahapan.equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG)  ) {
					catalog = catalogSession.find(approvalProcessType.getValueId());
					emailNotificationSession.getMailGeneratorApprovalMidleAndLast(catalog, status, approvalProcessList );
				}
				update(approvalProcess, token);
				approvalProcessTypeSession.update(approvalProcessType, token);

			}
		return isValid;
		
	}
	
	private Boolean checkApprovalProces(ApprovalProcess approvalProcess) {
			Boolean isFinish = true;
			List <ApprovalProcess> approvalProcessList = findByApprovalProcessType(approvalProcess.getApprovalProcessType().getId());
			for (ApprovalProcess approvalProcessDb : approvalProcessList  ) {
				if ( approvalProcessDb.getStatus() != 3 ) {
					isFinish = false;
					break ;
				}
			}
			
			return isFinish;
	}

	public List<ApprovalProcessDTO> getMyMonitoringWorkList(Token token , String tahapan){
		
		User user = token.getUser();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(user.getId());
		List<ApprovalProcessDTO> approvalProcessDTOList = new ArrayList<ApprovalProcessDTO>();
		Jabatan jabatan = new Jabatan();
		Organisasi organisasi = new Organisasi();
		try{
			if(roleUserList.size() > 0) {
				organisasi = roleUserList.get(0).getOrganisasi();
				jabatan = user.getJabatan();
				approvalProcessDTOList=getRemainingApprover(organisasi,jabatan,tahapan);
			}
		}catch(Exception e) {
			log.error(e.getCause());
		}
		return approvalProcessDTOList;
	}
	
	@SuppressWarnings("unchecked")
	private List<ApprovalProcessDTO> getRemainingApprover(Organisasi organisasi,Jabatan jabatan,String tahapan) {
		
		String strWhere = "";
		if (!organisasi.getNama().equalsIgnoreCase(Constant.ORGANISASI_PUSAT)) {
			strWhere = " and ap.group =:organisasi";
		}
		
		String strQuery = "select ap from ApprovalProcess ap where ap.status = 1 and ap.isDelete = 0 and "
				+ " ap.approvalProcessType.tahapan.nama = :namaTahapan and ap.approvalProcessType.id in"
				+ "	(select ap.approvalProcessType.id from ApprovalProcess ap where ap.status = 3 and ap.isDelete = 0 and ap.jabatan =:jabatan "+strWhere+" )";
		
		Query query = em.createQuery(strQuery)
				.setParameter("namaTahapan", tahapan)
				.setParameter("jabatan", jabatan);
		
		if (!organisasi.getNama().equalsIgnoreCase(Constant.ORGANISASI_PUSAT)) {
			query.setParameter("organisasi", organisasi);
		}

		List<ApprovalProcess> approvalProcessList = query.getResultList();
		

		List<ApprovalProcessDTO> approvalProcessDTOList = new ArrayList<ApprovalProcessDTO>();
		for (ApprovalProcess approvalProcess : approvalProcessList) {
			// System.out.println("Approval Process " + approvalProcess.getId());

			ApprovalProcessDTO newApprovalProcess = new ApprovalProcessDTO();
			newApprovalProcess.setApprovalProcess(approvalProcess);

			if (approvalProcess.getApprovalProcessType().getTahapan().getNama().equalsIgnoreCase(Constant.TAHAPAN_APPROVAL_BO)) {
				try {
					String description;
					PurchaseRequest pr = prSession.find(approvalProcess.getApprovalProcessType().getValueId());
					if (pr.getDescription() == null) {
						description = "";
					} else {
						description = " - " + pr.getDescription();
					}
					newApprovalProcess.setKeteranganIndex(pr.getBoNumber() + description);
					approvalProcessDTOList.add(newApprovalProcess);
				} catch (Exception e) {
					System.err.println("No Data PR with ID : " + approvalProcess.getApprovalProcessType().getValueId());
				}
			}
		}

		return approvalProcessDTOList;
	}
	
	public ApprovalProcess findByCatlog(Integer idCatalog) {
		try {
			Query q = em.createNamedQuery("ApprovalProcess.findByCatlog");
			q.setParameter("idCatalog", idCatalog)
			.setMaxResults(1);
			return (ApprovalProcess)q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

}
