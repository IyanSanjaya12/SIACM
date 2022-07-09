package id.co.promise.procurement.deliveryreceived;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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

import id.co.promise.procurement.DTO.LaporanEvaluasiKinerjaVendorDTO;
import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

/**
 * @author F.H.K
 *
 */

@Stateless
@LocalBean
public class DeliveryReceivedSession extends AbstractFacadeWithAudit<DeliveryReceived> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	AlokasiAnggaranSession alokasiAnggaranSession;
	@EJB
	PurchaseRequestSession purchaseRequestSession;
	@EJB
	PurchaseRequestItemSession purchaseRequestItemSession;
	@EJB
	PurchaseOrderSession purchaseOrderSession;
	@EJB
	PurchaseOrderItemSession purchaseOrderItemSession;
	@EJB
	DeliveryReceivedLogSession deliveryReceivedLogSession;
	@EJB
	OrganisasiSession organisasiSession;
	@EJB
	VendorSession vendorSession;

	public DeliveryReceivedSession() {
		super(DeliveryReceived.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public DeliveryReceived getDeliveryReceived(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<DeliveryReceived> getListPagination(String search, Date startDate, Date endDate, String status, Integer pageNo, Integer pageSize, String sort, RoleUser roleUser ) throws ParseException {
		///DVP
		String roleCode = roleUser.getRole().getCode();
		Integer orgId = roleUser.getOrganisasi().getId();
		String query = "SELECT distinct deliveryReceived, poi.vendor FROM DeliveryReceived deliveryReceived left join PurchaseOrderItem poi "
				+ "on poi.purchaseOrder.id = deliveryReceived.purchaseOrder.id ";
		
		 
		search = search == null ? "" : search.trim();

		query = query + "Where deliveryReceived.isDelete = 0 And deliveryReceived.isFinish = 1 "; //
		
		 
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			query = query + "And poi.vendor.vendorIdEproc =:userName ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi.id =:organisasiId ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi IN (SELECT organisasi FROM Organisasi organisasi WHERE organisasi.parentId =:parentId) ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi IN (:organisasiList) ";
			}
		}
		 
		if (!search.isEmpty()) {
			query = query + "and (deliveryReceived.deliveryReceiptNum like :search) ";
		}
		
		if (startDate != null && endDate != null) {
			query += "and deliveryReceived.dateReceived >= :startDate and deliveryReceived.dateReceived <= :endDate ";
		} else if (startDate == null && endDate != null) {
			query += "and deliveryReceived.dateReceived <= :endDate ";
		}
		
		
		if (!status.isEmpty()) {
			if (status.equalsIgnoreCase("1")) {
			query = query + "and deliveryReceived.purchaseOrder.rating !=null ";
			} else if(status.equalsIgnoreCase("2")) {
				query = query + "and deliveryReceived.purchaseOrder.rating = null  ";
			}
		}

		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate );
		} else if  (startDate == null && endDate != null) {
			q.setParameter("endDate", endDate );
		}
		
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			String userName = roleUser.getUser().getUsername(); 
			q.setParameter("userName", userName); 
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			q.setParameter("organisasiId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			q.setParameter("parentId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				List<Organisasi> organisasiList = organisasiSession.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
				organisasiList.add(roleUser.getOrganisasi());
				q.setParameter("organisasiList", organisasiList);
			}
		}

		  q.setFirstResult((pageNo - 1) * pageSize);
		  q.setMaxResults(pageSize);
		 
		return q.getResultList();
	}
	
	public Long getTotalList(String search, Date startDate, Date endDate, String status,
			Integer pageNo, Integer pageSize, RoleUser roleUser) {
		String roleCode = roleUser.getRole().getCode();
		Integer orgId = roleUser.getOrganisasi().getId();
		String query = "SELECT distinct count (deliveryReceived) FROM DeliveryReceived deliveryReceived left join PurchaseOrderItem poi "
				+ "on poi.purchaseOrder.id = deliveryReceived.purchaseOrder.id ";
		
		 
		search = search == null ? "" : search.trim();

		query = query + "Where deliveryReceived.isDelete = 0 And deliveryReceived.isFinish = 1 "; //
		
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			query = query + "And poi.vendor.vendorIdEproc =:userName ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi.id =:organisasiId ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi IN (SELECT organisasi FROM Organisasi organisasi WHERE organisasi.parentId =:parentId) ";
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi IN (:organisasiList) ";
			}
		}
		
		if (!search.isEmpty()) {
			query = query + "and (deliveryReceived.deliveryReceiptNum like :search) ";
		}

		if (startDate != null && endDate != null) {
			query += "and deliveryReceived.dateReceived >= :startDate and deliveryReceived.dateReceived <= :endDate ";
		} else if (startDate == null && endDate != null) {
			query += "and deliveryReceived.dateReceived <= :endDate ";
		}
		
		if (!status.isEmpty()) {
			if (status.equalsIgnoreCase("1")) {
			query = query + "and deliveryReceived.purchaseOrder.rating !=null ";
			} else if(status.equalsIgnoreCase("2")) {
				query = query + "and deliveryReceived.purchaseOrder.rating = null  ";
			}
		}

		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}

		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate );
		} else if  (startDate == null && endDate != null) {
			q.setParameter("endDate", endDate );
		}
		if (roleCode.equals(Constant.ROLE_CODE_VENDOR)) {
			String userName = roleUser.getUser().getUsername(); 
			q.setParameter("userName", userName); 
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_DVP)) {
			q.setParameter("organisasiId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_PENGGUNA_SPV)) {
			q.setParameter("parentId", orgId);
		}
		else if (roleCode.equals(Constant.ROLE_CODE_DIREKTUR_PENGGUNA)) {
			if(orgId != 1) {
				List<Organisasi> organisasiList = organisasiSession.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
				organisasiList.add(roleUser.getOrganisasi());
				q.setParameter("organisasiList", organisasiList);
			}
		}
		
		return (Long) q.getSingleResult();
	}
	
	
	@SuppressWarnings("unchecked")
	public DeliveryReceived getDeliveryReceivedByPoIdSingle(Integer purchaseOrderId){
		Query q = em.createQuery("SELECT t1 from DeliveryReceived t1 where t1.isDelete = 0 AND t1.purchaseOrder.id = :purchaseOrderId");
		q.setParameter("purchaseOrderId", purchaseOrderId);
		List<DeliveryReceived> deliveryReceivedList = q.getResultList();
		try {
			if (deliveryReceivedList != null && deliveryReceivedList.size() > 0) {
				return deliveryReceivedList.get(0);
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public DeliveryReceived insertDeliveryReceived(DeliveryReceived deliveryReceived, Token token) {
		deliveryReceived.setCreated(new Date());
		deliveryReceived.setIsDelete(0);
		super.create(deliveryReceived, AuditHelper.OPERATION_CREATE, token);
		return deliveryReceived;
	}

	public DeliveryReceived updateDeliveryReceived(DeliveryReceived deliveryReceived, Token token) {
		deliveryReceived.setUpdated(new Date());
		deliveryReceived.setIsDelete(0);
		super.edit(deliveryReceived, AuditHelper.OPERATION_UPDATE, token);
		return deliveryReceived;
	}
	@SuppressWarnings("unchecked")
	public List<Map> getReportByVendor(Vendor vendor){
		List<Map> mapList = new ArrayList<Map>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		Query q = em.createQuery("select dr.dateReceived , dr.purchaseOrder.poNumber , dr.purchaseOrder.purchaseRequest.prnumber,dr.purchaseOrder.approvedDate from DeliveryReceived dr where dr.isDelete = 0");
		
		mapList = q.getResultList();
		
		return mapList;
	}
	
	@SuppressWarnings("unchecked")
	public List<LaporanEvaluasiKinerjaVendorDTO> getReportByVendorDetail(Vendor vendor, String poNumber, String prnumber, String search, Integer pageNo, Integer pageSize, Date filter){
		List<LaporanEvaluasiKinerjaVendorDTO> laporanEvaluasiKinerjaVendorDTOList = new ArrayList<LaporanEvaluasiKinerjaVendorDTO>();
		String searchQuery = " AND dr.purchaseOrder.purchaseRequest.prnumber like :search " + "AND dr.purchaseOrder.poNumber like :search "; 
		String periode = " ";
		if (filter != null) {
			periode = " and dr.purchaseOrder.ratingDate >= :filter ";
		}
		Query q = em.createQuery("SELECT dr.purchaseOrder.purchaseRequest.boNumber, dr.purchaseOrder.poNumber, dr.purchaseOrder.purchaseRequest.prnumber, "
				+ "dr.purchaseOrder.purchaseOrderDate, dr.dateReceived, dr.purchaseOrder.rating, dr.purchaseOrder.komen "
				+ " FROM DeliveryReceived dr WHERE dr.isDelete = 0 "
				+ "AND dr.dateReceived in (SELECT MAX(deliv.dateReceived) FROM DeliveryReceived deliv WHERE deliv.isDelete = 0 GROUP BY deliv.purchaseOrder)"
				+ "AND dr.purchaseOrder in (SELECT poi.purchaseOrder FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 "
				+ "AND poi.vendor = :vendor)" + searchQuery + periode );
		q.setParameter("vendor", vendor);
		q.setParameter("search", "%" + search +"%");
		if (filter != null) { 
			q.setParameter("filter", filter);
		}
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);
		
		List<Object[]> objList = q.getResultList();
			    
		for (Object[] obj : objList) 
		{
			LaporanEvaluasiKinerjaVendorDTO laporanEvaluasiKinerjaVendorDTO = new LaporanEvaluasiKinerjaVendorDTO();
			laporanEvaluasiKinerjaVendorDTO.setBoNumber(obj[0] != null ? obj[0].toString() : null);
			laporanEvaluasiKinerjaVendorDTO.setPoNumberEbs(obj[1] != null ? obj[1].toString() : null);
			laporanEvaluasiKinerjaVendorDTO.setPrNumber(obj[2] != null ? obj[2].toString() : null);
			laporanEvaluasiKinerjaVendorDTO.setVendorName(vendor.getNama() != null ? vendor.getNama() : null);
			laporanEvaluasiKinerjaVendorDTO.setApprovedDate(obj[3] != null ? (Date)obj[3] : null);
			laporanEvaluasiKinerjaVendorDTO.setDateReceived(obj[4] != null ? (Date)obj[4] : null);
			laporanEvaluasiKinerjaVendorDTO.setRating(obj[5] != null ? (Integer)obj[5] : null);
			laporanEvaluasiKinerjaVendorDTO.setKomen(obj[6] != null ? obj[6].toString() : null);
			laporanEvaluasiKinerjaVendorDTOList.add(laporanEvaluasiKinerjaVendorDTO);
		}

		
		return laporanEvaluasiKinerjaVendorDTOList;	    
	}						

	@SuppressWarnings("unchecked")
	public List<LaporanEvaluasiKinerjaVendorDTO> getExcelReportByVendorDetail(Vendor vendor, String poNumber, String prnumber, String search, Date filter){
		List<LaporanEvaluasiKinerjaVendorDTO> laporanEvaluasiKinerjaVendorDTOList = new ArrayList<LaporanEvaluasiKinerjaVendorDTO>();
		String searchQuery = " AND dr.purchaseOrder.purchaseRequest.prnumber like :search " + "AND dr.purchaseOrder.poNumber like :search "; 
		String periode = " ";
		if (filter != null) {
			periode = " and dr.purchaseOrder.ratingDate >= :filter ";
		}
		Query q = em.createQuery("SELECT dr.purchaseOrder.purchaseRequest.boNumber, dr.purchaseOrder.poNumber, dr.purchaseOrder.purchaseRequest.prnumber, "
				+ "dr.purchaseOrder.purchaseOrderDate, dr.dateReceived, dr.purchaseOrder.rating, dr.purchaseOrder.komen "
				+ " FROM DeliveryReceived dr WHERE dr.isDelete = 0 "
				+ "AND dr.dateReceived in (SELECT MAX(deliv.dateReceived) FROM DeliveryReceived deliv WHERE deliv.isDelete = 0 GROUP BY deliv.purchaseOrder)"
				+ "AND dr.purchaseOrder in (SELECT poi.purchaseOrder FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 "
				+ "AND poi.vendor = :vendor)" + searchQuery + periode );
		q.setParameter("vendor", vendor);
		q.setParameter("search", "%" + search +"%");
		if (filter != null) { 
			q.setParameter("filter", filter);
		}
		List<Object[]> objList = q.getResultList();
			    
		for (Object[] obj : objList) 
		{
			LaporanEvaluasiKinerjaVendorDTO laporanEvaluasiKinerjaVendorDTO = new LaporanEvaluasiKinerjaVendorDTO();
			laporanEvaluasiKinerjaVendorDTO.setBoNumber(obj[0] != null ? obj[0].toString() : null);
			laporanEvaluasiKinerjaVendorDTO.setPoNumberEbs(obj[1] != null ? obj[1].toString() : null);
			laporanEvaluasiKinerjaVendorDTO.setPrNumber(obj[2] != null ? obj[2].toString() : null);
			laporanEvaluasiKinerjaVendorDTO.setVendorName(vendor.getNama() != null ? vendor.getNama() : null);
			laporanEvaluasiKinerjaVendorDTO.setApprovedDate(obj[3] != null ? (Date)obj[3] : null);
			laporanEvaluasiKinerjaVendorDTO.setDateReceived(obj[4] != null ? (Date)obj[4] : null);
			laporanEvaluasiKinerjaVendorDTO.setRating(obj[5] != null ? (Integer)obj[5] : null);
			laporanEvaluasiKinerjaVendorDTO.setKomen(obj[6] != null ? obj[6].toString() : null);
			laporanEvaluasiKinerjaVendorDTOList.add(laporanEvaluasiKinerjaVendorDTO);
		}
		
		return laporanEvaluasiKinerjaVendorDTOList;	    
	}	
	
	public Integer getCountReportByVendor(Vendor vendor, String search){
		String searchQuery = " AND deliveryReceived.purchaseOrder.purchaseRequest.prnumber like :search " + "or deliveryReceived.purchaseOrder.poNumber like :search "; 
		Query q = em.createQuery("SELECT count(deliveryReceived) FROM DeliveryReceived deliveryReceived WHERE deliveryReceived.isDelete = 0 "
				+ "AND deliveryReceived.dateReceived in (SELECT MAX(deliv.dateReceived) FROM DeliveryReceived deliv WHERE deliv.isDelete = 0 GROUP BY deliv.purchaseOrder) "
				+ "AND deliveryReceived.purchaseOrder IN (SELECT poi.purchaseOrder FROM PurchaseOrderItem poi WHERE poi.isDelete = 0 AND poi.vendor = :vendor) " + searchQuery);   
		q.setParameter("search", "%" + search + "%");
		q.setParameter("vendor", vendor);
		Long jmlData = (Long) q.getSingleResult();
			    
		return jmlData.intValue();
	}
	
	@SuppressWarnings({"unchecked" })
	public List<DeliveryReceived> getReportProsesPembelian(String search, String orderKeyword, String sort,
			Date startDate, Date endDate, String status, Integer pageNo,
			Integer pageSize, RoleUser roleUser ){	
		Calendar calender = Calendar.getInstance();
		String query = "Select deliveryReceived, invoicePayment from DeliveryReceived deliveryReceived, " + 
				"InvoicePayment invoicePayment where deliveryReceived.isDelete = 0 " + 
				"AND deliveryReceived.purchaseOrder.poNumber = invoicePayment.poNumber ";
		status = status == null ? "" : status.trim();
		
		if (status != null && status.equals("")) {
			query = query + "and deliveryReceived.isFinish =:status ";
		}
		
		if (search != null && !search.equals("")) {
			query = query + "and deliveryReceived.purchaseOrder.poNumber like :search  ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		} else {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi.id IN (:organisasiList) ";
		}
		
		if (startDate != null) {
			query = query + "and deliveryReceived.dateReceived >= :startDate ";
		} 
		
		if (endDate != null) {
			query = query + "and deliveryReceived.dateReceived <= :endDate ";
		} 
		
		if (startDate != null && endDate != null) {
			calender.setTime(endDate);
			calender.add(Calendar.DATE, 1);
			endDate = calender.getTime();
			query = query + "and deliveryReceived.dateReceived >= :startDate and deliveryReceived.dateReceived <= :endDate ";
		}
		
		if (!orderKeyword.isEmpty()) {
			query = query + "order by "+orderKeyword ;
		}
		
		Query q = getEntityManager().createQuery(query);		
		
		
		if (search != null && !search.equals("")) {
			q.setParameter("search", "%" +search+"%");
		}
		
		if (startDate != null ) {
			q.setParameter("startDate", startDate);		
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		} else {
			List<Integer> organisasiIdList = new ArrayList<Integer>();
			List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
			for(Organisasi organisasi : organisasiList) {
				organisasiIdList.add(organisasi.getId());
			}
			organisasiIdList.add(roleUser.getOrganisasi().getId());
			
			if(organisasiIdList.size() > 0) {
				q.setParameter("organisasiList", organisasiIdList);
			}else {
				q.setParameter("organisasiList", 0);//Tidak dapat list organisasi
			}
		} 
		
		if (endDate != null ) {
			q.setParameter("endDate", endDate);		
		}
		
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		
		if (status != null && status.equals("")) {
			q.setParameter("status", Integer.valueOf(status));	
		}
		
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);		
		return q.getResultList();	    
	}	

	public Long getCountReportProsesPembelian(String search,
			Date startDate, Date endDate, String status, Integer pageNo,
			Integer pageSize, RoleUser roleUser ){		
		Calendar calender = Calendar.getInstance();
		String query = "Select count (deliveryReceived) from DeliveryReceived deliveryReceived, "  + 
				"InvoicePayment invoicePayment where deliveryReceived.isDelete = 0 " + 
				"AND deliveryReceived.purchaseOrder.poNumber = invoicePayment.poNumber ";
		
		status = status == null ? "" : status.trim();
		
		if (status != null && status.equals("")) {
			query = query + "and deliveryReceived.isFinish =:status ";
		}
		
		if (search != null && !search.equals("")) {
			query = query + "and deliveryReceived.purchaseOrder.poNumber like :search  ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest IN (Select prItem.purchaserequest from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.vendor.id =:vendorId) ";
		} else {
			query = query + "and deliveryReceived.purchaseOrder.purchaseRequest.organisasi.id IN (:organisasiList) ";
		}
		
		if (startDate != null) {
			query = query + "and deliveryReceived.dateReceived >= :startDate ";
		} 
		
		if (endDate != null) {
			query = query + "and deliveryReceived.dateReceived <= :endDate ";
		} 
		
		if (startDate != null && endDate != null) {
			calender.setTime(endDate);
			calender.add(Calendar.DATE, 1);
			endDate = calender.getTime();
			query = query + "and deliveryReceived.dateReceived >= :startDate and deliveryReceived.dateReceived <= :endDate ";
		}
		
		Query q = getEntityManager().createQuery(query);		
		
		
		if (search != null && !search.equals("")) {
			q.setParameter("search", "%" +search+"%");
		}
		
		if (startDate != null ) {
			q.setParameter("startDate", startDate);		
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		} else {
			List<Integer> organisasiIdList = new ArrayList<Integer>();
			List<Organisasi> organisasiList = organisasiSession.getSelfAndChildByParentId(roleUser.getOrganisasi().getId());
			for(Organisasi organisasi : organisasiList) {
				organisasiIdList.add(organisasi.getId());
			}
			organisasiIdList.add(roleUser.getOrganisasi().getId());
			
			if(organisasiIdList.size() > 0) {
				q.setParameter("organisasiList", organisasiIdList);
			}else {
				q.setParameter("organisasiList", 0);//Tidak dapat list organisasi
			}
		} 
		
		if (endDate != null ) {
			q.setParameter("endDate", endDate);		
		}
		
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		
		if (status != null && status.equals("")) {
			q.setParameter("status", Integer.valueOf(status));	
		}
		
		return (Long) q.getSingleResult();    
	}
	
}
