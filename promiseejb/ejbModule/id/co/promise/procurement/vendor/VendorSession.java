package id.co.promise.procurement.vendor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import org.jboss.logging.Logger;

import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.dashboard.DashboardOrderDTO;
import id.co.promise.procurement.entity.BidangUsaha;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class VendorSession extends AbstractFacadeWithAudit<Vendor> {

	final static Logger logger = Logger.getLogger(VendorSession.class);

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB UserSession userSession;

	@EJB CatalogSession catalogSession;

	@EJB CategorySession categorySession;
	
	@EJB OrganisasiSession organisasiSession;
	
	@EJB SegmentasiVendorSession segmentasiVendorSession;
	
	@EJB VendorProfileSession vendorProfileSession;
	
	@EJB VendorSKDSession vendorSKDSession;
	
	@EJB SertifikatVendorSession sertifikatVendorSession;
	
	@EJB VendorPICSession vendorPICSession;
	
	@EJB PengalamanVendorSession PengalamanVendorSession;
	
	@EJB KeuanganVendorSession keuanganVendorSession;
	
	@EJB DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;
	
	@EJB BankVendorSession bankVendorSession;
	
	@EJB PeralatanVendorSession peralatanVendorSession;

	public VendorSession() {

		super(Vendor.class);
	}

	public int countByStatus(int status) {

		Query q = em.createNamedQuery("Vendor.countByStatus").setParameter("status", status);
		Object data = q.getSingleResult();
		return (data != null) ? Integer.parseInt(data.toString()) : 0;
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorList() {
		Query q = em.createNamedQuery("Vendor.find");
		return q.getResultList();
	}
	
	public List<Vendor> getList() {
		Query q = em.createNamedQuery("Vendor.getList");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Vendor getVendorById(int id) {

		Query q = em.createNamedQuery("Vendor.findById");
		q.setParameter("id", id);
		List<Vendor> vendorList = q.getResultList();
		if (vendorList != null && vendorList.size() > 0) {
			return vendorList.get(0);
		}
		return null;
		
	}
	public List<Vendor> getVendorListById(int id){
		Query q = em.createNamedQuery("Vendor.findById");
		q.setParameter("id", id);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListByStatus(Integer status) {

		Query q = em.createNamedQuery("Vendor.findByStatus");
		q.setParameter("status", status);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListByStatusForBlacklist(Integer status) {

		Query q = em.createQuery("SELECT vendor FROM Vendor vendor WHERE vendor.status =:status AND vendor.statusBlacklist = :statusBlacklist ");
		q.setParameter("status", status);
		q.setParameter("statusBlacklist", 0);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListByStatusApproal() {
		Query q = em.createQuery("SELECT vendor FROM Vendor vendor WHERE vendor.isDelete = :isDelete "
				+ "AND (vendor.status = :status OR vendor.statusPerpanjangan = :statusPerpanjangan OR vendor.statusBlacklist = :statusBlacklist OR vendor.statusUnblacklist = :statusUnblacklist) ");
		q.setParameter("isDelete", 0);
		q.setParameter("status", 0);
		q.setParameter("statusPerpanjangan", 1);
		q.setParameter("statusBlacklist", 1);
		q.setParameter("statusUnblacklist", 1);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListByStatusApproalAndLevelOrganisasi(List<String> organisasiList) {
		String query ="SELECT vendorProfile.vendor FROM VendorProfile vendorProfile WHERE"
				+ " vendorProfile.vendor.isDelete = :isDelete "
				+ "AND (vendorProfile.vendor.status = :status OR vendorProfile.vendor.statusPerpanjangan = :statusPerpanjangan OR vendorProfile.vendor.statusBlacklist = :statusBlacklist OR vendorProfile.vendor.statusUnblacklist = :statusUnblacklist) ";
		
		
		if(organisasiList.size()>0){
			query += " AND vendorProfile.unitTerdaftar IN (:organisasiList) ";
		}
		
		Query q = em.createQuery(query);
		
		if(organisasiList.size()>0){
			q.setParameter("organisasiList", organisasiList);
		}
		
		q.setParameter("isDelete", 0);
		q.setParameter("status", 0);
		q.setParameter("statusPerpanjangan", 1);
		q.setParameter("statusBlacklist", 1);
		q.setParameter("statusUnblacklist", 1);
		return q.getResultList();
	}

	public Vendor getVendor(int id) {

		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorByNamaList(String nama) {

		Query q = em.createNamedQuery("Vendor.findByNama");
		q.setParameter("nama", "%" + nama.toLowerCase() + "%");
		q.setMaxResults(10);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListCatalogByNama(String nama) {
		String query = "SELECT v FROM Vendor v WHERE v IN (SELECT c.vendor FROM Catalog c WHERE c.isDelete = 0) AND v.isDelete = 0 ";
		if (nama.equalsIgnoreCase("null")) {
			 //getVendorListCatalogWithoutName
		}
		else {
			query += "AND v.nama = :nama";
		}
		Query q = em.createQuery(query);
		if (nama.equalsIgnoreCase("null")) {
			 //getVendorListCatalogWithoutName
		}
		else {
			q.setParameter("nama", nama);
		}
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListByNameEqual(String pNama) {

		Query q = em.createQuery("select v from Vendor v where v.nama = :pNama");
		q.setParameter("pNama", pNama);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Vendor getVendorByUserId(int userId) {

		Query q = em.createNamedQuery("Vendor.findByUserId");
		q.setParameter("userId", userId);
		List<Vendor> vendorList = q.getResultList();
		if (vendorList != null && vendorList.size() > 0) {
			return vendorList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListByCatalog(List<BidangUsaha> bidangUsahaList,
			Integer startRow,
			Integer rowSize) {

		String queryString = "select distinct c.vendor from Catalog c where c.isDelete = 0 ";
		if (bidangUsahaList != null && bidangUsahaList.size() > 0) {

		}
		queryString += " order by c.vendor.id desc ";
		Query query = getEntityManager().createQuery(queryString);
		if (bidangUsahaList != null && bidangUsahaList.size() > 0) {
			// query.setParameter("categoryList", bidangUsahaList);
		}
		if (startRow != null) {
			query.setFirstResult(startRow);
			query.setMaxResults(rowSize);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public VendorListDTO getAllVendorCatalogListForSearch(Integer bidangUsahaId,
			Integer subBidangUsahaId,
			String vendorAndProductCategoryName,
			String location,
			Integer ratings,
			Integer sortByColumn,
			Integer maxBaris,
			Integer halamanKe) {

		String queryString = "select vendor from Vendor vendor join SegmentasiVendor segmentasiVendor on segmentasiVendor.vendor = vendor " 
				+ " where vendor.isDelete = 0 and vendor.status = 1 and segmentasiVendor.isDelete = 0";

		vendorAndProductCategoryName = vendorAndProductCategoryName == null ? "" : vendorAndProductCategoryName.trim();
		location = location == null ? "" : location.trim();

		if (!vendorAndProductCategoryName.isEmpty())
		{
			queryString += " and lower(vendor.nama) like :nama ";
		}

		if (!location.isEmpty()) /* location */
		{
			queryString += " and lower(vendor.Alamat) like :alamat ";
		}

		if ((bidangUsahaId != null) && (bidangUsahaId != 0)) /* bidangUsahaId */
		{
			queryString += " and segmentasiVendor.subBidangUsaha.bidangUsaha.id = :bidangUsahaId";
		}

		if ((subBidangUsahaId != null) && (subBidangUsahaId != 0)) /* subBidangUsahaId */
		{
			queryString += " and segmentasiVendor.subBidangUsaha.id = :subBidangUsahaId ";
		}

		if ((ratings != null)) /* ratings */
		{
			queryString += " and FLOOR(vendor.performance_avg) = :rating ";
		}

		/* Sorting */
		String orderColumn = "";

		sortByColumn = sortByColumn == null ? 1 : sortByColumn;
		switch (sortByColumn) {
		case 1:
			orderColumn = "vendor.nama";
			break;
		case 2:
			orderColumn = "vendor.performanceAvg DESC";
			break;
		case 3:
			orderColumn = "transaksi DESC";
			break;
		default:
			orderColumn = "vendor.id DESC";
			break;
		}

		queryString += " order by " + orderColumn;
		
		Query query = getEntityManager().createQuery(queryString);
		
		if (!vendorAndProductCategoryName.isEmpty())
		{
			query.setParameter("nama", "%"+vendorAndProductCategoryName.toLowerCase()+"%");
		}

		if (!location.isEmpty())
		{
			query.setParameter("alamat", "%"+location.toLowerCase()+"%");
		}

		if ((bidangUsahaId != null) && (bidangUsahaId != 0)) /* bidangUsahaId */
		{
			query.setParameter("bidangUsahaId", bidangUsahaId);
		}

		if ((subBidangUsahaId != null) && (subBidangUsahaId != 0)) /* subBidangUsahaId */
		{
			query.setParameter("subBidangUsahaId", subBidangUsahaId);
		}

		if ((ratings != null)) /* ratings */
		{
			query.setParameter("ratings", ratings);
		}

		
		List<Vendor> vendorResultListForSize = query.getResultList();
		Integer totalRow = vendorResultListForSize.size();

		query = getEntityManager().createQuery(queryString);
		
		if (!vendorAndProductCategoryName.isEmpty())
		{
			query.setParameter("nama", "%"+vendorAndProductCategoryName.toLowerCase()+"%");
		}

		if (!location.isEmpty())
		{
			query.setParameter("alamat", "%"+location.toLowerCase()+"%");
		}

		if ((bidangUsahaId != null) && (bidangUsahaId != 0)) /* bidangUsahaId */
		{
			query.setParameter("bidangUsahaId", bidangUsahaId);
		}

		if ((subBidangUsahaId != null) && (subBidangUsahaId != 0)) /* subBidangUsahaId */
		{
			query.setParameter("subBidangUsahaId", subBidangUsahaId);
		}

		if ((ratings != null)) /* ratings */
		{
			query.setParameter("ratings", ratings);
		}
		
		int startRow = (halamanKe-1)*maxBaris;
		List<Vendor> vendorResultList = query.setFirstResult(startRow).setMaxResults(maxBaris).getResultList();
		VendorListDTO vendorListDTO = new VendorListDTO();
		vendorListDTO.setVendorList(vendorResultList);
		vendorListDTO.setTotalRow(totalRow);
		return vendorListDTO;
	}

	public Vendor insertVendor(Vendor vendor,
			Token token) {

		vendor.setCreated(new Date());
		vendor.setIsDelete(0);
		vendor.setStatus(0);
		super.create(vendor, AuditHelper.OPERATION_CREATE, token);
		return vendor;
	}

	public Vendor updateVendor(Vendor vendor,
			Token token) {

		vendor.setUpdated(new Date());
		if(vendor.getIsDelete() == null) {
			vendor.setIsDelete(0);
		}
		super.edit(vendor, AuditHelper.OPERATION_UPDATE, token);
		return vendor;
	}

	public Vendor updateVendorStatus(int vendorId,
			int status,
			Token token) {

		Vendor vendor = super.find(vendorId);
		vendor.setStatus(status);
		vendor.setUpdated(new Date());
		super.edit(vendor, AuditHelper.OPERATION_UPDATE, token);
		return vendor;
	}

	public Vendor deleteVendor(int vendorId,
			Token token) {

		Vendor vendor = super.find(vendorId);
		vendor.setIsDelete(1);
		vendor.setDeleted(new Date());
		super.edit(vendor, AuditHelper.OPERATION_DELETE, token);

		// delete user too
		User user = userSession.getUser(vendor.getUser());
		userSession.deleteUserClasic(user.getId(), token);
		return vendor;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {

		return ema;
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getPagingList(String keywordSearch,
			int pageNo,
			int pageSize,
			String orderKeyword) {

		String vQuery = "select vdr " + "from Vendor vdr " + "where vdr.isDelete = 0 ";
		if (keywordSearch != null) {
			vQuery = vQuery + "and (vdr.nama like :keyword or vdr.Alamat like :keyword or vdr.nomorTelpon like :keyword) ";
		}
		if (orderKeyword != null) {
			vQuery = vQuery + "order by vdr." + orderKeyword + " asc";
		}
		Query query = em.createQuery(vQuery);
		if (keywordSearch != null) {
			query.setParameter("keyword", keywordSearch);
		}
		query.setFirstResult((pageNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public Long getTotalList(String keywordSearch) {

		String vQuery = "select COUNT (vdr) " + "from Vendor vdr " + "where vdr.isDelete = 0 ";
		if (keywordSearch != null) {
			vQuery = vQuery + "and (vdr.nama like :keyword or vdr.Alamat like :keyword or vdr.nomorTelpon like :keyword) ";
		}
		Query query = em.createQuery(vQuery);
		if (keywordSearch != null) {
			query.setParameter("keyword", keywordSearch);
		}
		return (Long) query.getSingleResult();
	}

	private String columnSortList(int index){
		String[] colomnTbl = new String[7];
		colomnTbl[0] = "t1.id";
		colomnTbl[1] = "t1.nama";
		colomnTbl[2] = "t1.email";
		colomnTbl[3] = "t1.performanceDate";
		colomnTbl[4] = "t1.performanceAvg";
		colomnTbl[5] = "t1.id"; 
		
		return colomnTbl[index].toString();
	}
	
	private String columnSort(int index){
		String[] colomnTbl = new String[7];
		colomnTbl[0] = "vendor.VENDOR_ID";
		colomnTbl[1] = "vendor.NAMA";
		colomnTbl[2] = "vendor.EMAIL";
		colomnTbl[3] = "vendor.PERFORMANCE_DATE";
		colomnTbl[4] = "vendor.PERFORMANCE_AVG";
		//colomnTbl[5] = "vendor.VENDOR_ID"; 
		
		return colomnTbl[index].toString();
	}


	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorPerformaPaggingList(String nomorAwal,
			String nomorAkhir,
			String namaVendor,
			String bidangUsaha,
			String subBidangUsaha,
			String pernahdinilai,
			String belumpernahdinilai,
			Integer iDisplayStart, Integer iDisplayLength, String keyword,
			Integer column, String sort) {
		
		//String query = "SELECT t1.vendor FROM SegmentasiVendor t1 WHERE t1.isDelete = 0 AND " + "(t1.vendor.nama like :keyword OR t1.vendor.email like :keyword) ";
		
		String query = "SELECT vendor.VENDOR_ID, vendor.NAMA, vendor.EMAIL,vendor.PERFORMANCE_DATE, vendor.PERFORMANCE_AVG "
				+ "from promise_t2_vendor vendor LEFT JOIN promise_t3_Segmentasi_vendor sv ON vendor.VENDOR_ID = sv.VENDOR_ID "
				+ "LEFT JOIN promise_t2_sub_bidang_usaha sbu on sv.SUB_BIDANG_USAHA_ID = sbu.SUB_BIDANG_USAHA_ID "
				+ "WHERE vendor.ISDELETE = 0 AND " + "(vendor.NAMA like :keyword OR vendor.EMAIL like :keyword) ";
		
		if (namaVendor != null) {
			query = query + "AND vendor.NAMA LIKE :nama ";
		}

		
		if ((pernahdinilai != null && !pernahdinilai.equals("")) && (belumpernahdinilai != null && !belumpernahdinilai.equals(""))) {
			query = query + "AND (vendor.PERFORMANCE_AVG > 0 OR vendor.PERFORMANCE_AVG = 0) ";
		} else {
			if (pernahdinilai != null && !pernahdinilai.equals("")) {
				query = query + "AND vendor.PERFORMANCE_AVG > 0 ";
			} else if (belumpernahdinilai != null && !belumpernahdinilai.equals("")) {
				query = query + "AND vendor.PERFORMANCE_AVG = 0 ";
			}
		}

		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			query = query + "AND sv.SUB_BIDANG_USAHA_ID = :subBidangUsaha ";
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				query = query + "AND sbu.BIDANG_USAHA_ID = :bidangUsaha ";
			}
		}
		
		query += " GROUP BY vendor.VENDOR_ID, vendor.NAMA, vendor.EMAIL,vendor.PERFORMANCE_DATE, vendor.PERFORMANCE_AVG";
				
		query += " ORDER BY " + this.columnSort(column) + " " + sort;

		Query q = em.createNativeQuery(query);
		q.setParameter("keyword", "%" + keyword + "%");
		if (namaVendor != null) {
			q.setParameter("nama", "%" + namaVendor + "%");
		}
		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			q.setParameter("subBidangUsaha", Integer.valueOf(subBidangUsaha));
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				q.setParameter("bidangUsaha", Integer.valueOf(bidangUsaha));
			}
		}
		
		List<Object[]> arrObjList = q.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
		
		List<Vendor> vendorList = new ArrayList<Vendor>();
		
		List<String> listString = new ArrayList<String>();
		listString.add(namaVendor);
		listString.add(bidangUsaha);
		listString.add(subBidangUsaha);
		listString.add(pernahdinilai);
		listString.add(belumpernahdinilai);
		listString.add(keyword);
		listString.add(this.columnSort(column));
		listString.add(sort);
		
		for (Object[] obj : arrObjList) {
			Vendor vendorNew = new Vendor();
			vendorNew.setId((Integer) obj[0]);
			vendorNew.setNama((String) obj[1]);
			vendorNew.setEmail((String) obj[2]);
			vendorNew.setPerformanceDate((Date) obj[3]);
			vendorNew.setPerformanceAvg((Double) obj[4]);
			vendorNew.setParamDataTable(listString);

			vendorList.add(vendorNew);
		}
		
		return vendorList;
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Integer getVendorPerformaPaggingSize(String nomorAwal,
			String nomorAkhir,
			String namaVendor,
			String bidangUsaha,
			String subBidangUsaha,
			String pernahdinilai,
			String belumpernahdinilai, String keyword) {

		/*String query = "SELECT COUNT( DISTINCT t1.vendor) FROM SegmentasiVendor t1 WHERE t1.isDelete = 0 AND " + "(t1.vendor.nama like :keyword OR t1.vendor.email like :keyword) ";
		if (namaVendor != null) {
			query = query + "AND t1.vendor.nama LIKE :nama ";
		}

		if (bidangUsaha != null) {
			if (subBidangUsaha != null) {

			}
		}
		
		if ((pernahdinilai != null && !pernahdinilai.equals("")) && (belumpernahdinilai != null && !belumpernahdinilai.equals(""))) {
			query = query + "AND (t1.vendor.performanceAvg > 0 OR ( t1.vendor.performanceAvg = 0 OR t1.vendor.performanceAvg is null) ) ";
		} else {
			if (pernahdinilai != null && !pernahdinilai.equals("")) {
				query = query + "AND t1.vendor.performanceAvg > 0 ";
			} else if (belumpernahdinilai != null && !belumpernahdinilai.equals("")) {
				query = query + "AND t1.vendor.performanceAvg = 0 OR t1.vendor.performanceAvg is null ";
			}
		}

		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			query = query + " AND t1.subBidangUsaha.id = :subBidangUsaha ";
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				query = query + "AND t1.subBidangUsaha.bidangUsaha.id = :bidangUsaha " + ")";
			}
		}

		Query q = em.createQuery(query);
		q.setParameter("keyword", "%" + keyword + "%");
		if (namaVendor != null) {
			q.setParameter("nama", "%" + namaVendor + "%");
		}
		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			q.setParameter("subBidangUsaha", Integer.valueOf(subBidangUsaha));
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				q.setParameter("bidangUsaha", Integer.valueOf(bidangUsaha));
			}
		}
		
		List list = q.getResultList();*/
		
		
		String query = "SELECT count(distinct vendor.VENDOR_ID) "
				+ "from promise_t2_vendor vendor LEFT JOIN promise_t3_Segmentasi_vendor sv ON vendor.VENDOR_ID = sv.VENDOR_ID "
				+ "LEFT JOIN promise_t2_sub_bidang_usaha sbu on sv.SUB_BIDANG_USAHA_ID = sbu.SUB_BIDANG_USAHA_ID "
				+ "WHERE vendor.ISDELETE = 0 AND (vendor.NAMA like :keyword OR vendor.EMAIL like :keyword) ";
		
		if (namaVendor != null) {
			query = query + "AND vendor.NAMA LIKE :nama ";
		}

		
		if ((pernahdinilai != null && !pernahdinilai.equals("")) && (belumpernahdinilai != null && !belumpernahdinilai.equals(""))) {
			query = query + "AND (vendor.PERFORMANCE_AVG > 0 OR vendor.PERFORMANCE_AVG = 0) ";
		} else {
			if (pernahdinilai != null && !pernahdinilai.equals("")) {
				query = query + "AND vendor.PERFORMANCE_AVG > 0 ";
			} else if (belumpernahdinilai != null && !belumpernahdinilai.equals("")) {
				query = query + "AND vendor.PERFORMANCE_AVG = 0 ";
			}
		}

		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			query = query + "AND sv.SUB_BIDANG_USAHA_ID = :subBidangUsaha ";
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				query = query + "AND sbu.BIDANG_USAHA_ID = :bidangUsaha ";
			}
		}

		Query q = em.createNativeQuery(query);
		q.setParameter("keyword", "%" + keyword + "%");
		if (namaVendor != null) {
			q.setParameter("nama", "%" + namaVendor + "%");
		}
		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			q.setParameter("subBidangUsaha", Integer.valueOf(subBidangUsaha));
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				q.setParameter("bidangUsaha", Integer.valueOf(bidangUsaha));
			}
		}
		
		List list = q.getResultList();
	
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorPRRemains(Integer id) {
		 
		Query q = em.createQuery(
				"SELECT distinct vendor FROM PurchaseRequestItem pri WHERE pri.purchaserequest.id = :id AND pri.vendor <> null AND pri.isDelete = :isDelete "
				+ "AND pri.vendor.id NOT IN (SELECT poi.vendor.id FROM PurchaseOrderItem poi WHERE poi.purchaseOrder.isDelete = :isDelete AND poi.purchaseOrder.purchaseRequest.id = pri.purchaserequest.id AND poi.purchaseOrder.status <> 'Reject' ) "
				//+ "GROUP BY pri.vendor "
		);

 		q.setParameter("id", id);
 		q.setParameter("isDelete", 0);
		return q.getResultList();
	}

	private String columnPurchaseRequestItemPaggingList(int index){
		String[] colomnTbl = new String[7];
		colomnTbl[0] = "T1.id";
		colomnTbl[1] = "T1.nama";
		colomnTbl[2] = "T1.performanceAvg";
		colomnTbl[3] = "pengadaanCount";
		colomnTbl[4] = "pengadaanRunningCount";
		colomnTbl[5] = "winnerCount";
		
		return colomnTbl[index].toString();
	}
	
	//fungsi untuk menampilkan hasil pengadaan vendor(pernah mengikuti, sedang mengikuti, menang) di tabel index modul rekaphistoryvendor 
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorPaggingList(
			Integer iDisplayStart, Integer iDisplayLength, String keyword,
			Integer column, String sort) {
		
		String query = "SELECT T1, "
				+ "(SELECT COUNT(o) FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0 AND o.vendor.id = T1.id AND pengadaan.tahapanPengadaan.tahapan.id = 21 AND NOT pengadaan.tahapanPengadaan.tahapan.id = 22 ) AS pengadaanCount,  "
				+ "(SELECT COUNT(pendaftaranVendor) FROM PendaftaranVendor pendaftaranVendor WHERE pendaftaranVendor.isDelete = 0 AND pendaftaranVendor.vendor.id = T1.id AND NOT pendaftaranVendor.pengadaan.tahapanPengadaan.tahapan.id = 21 AND NOT pendaftaranVendor.pengadaan.tahapanPengadaan.tahapan.id = 22) +  "
				+ "(SELECT COUNT(x) FROM PendaftaranVendorPraKualifikasi x WHERE x.isDelete = 0 AND x.vendor.id = T1.id AND pengadaan.tahapanPengadaan.tahapan.id NOT IN (10050000, 10120000))  AS pengadaanRunningCount, "
				+ "(SELECT count(p) FROM PenetapanPemenangTotal p WHERE p.isDelete = 0 AND p.vendor.id = T1.id ) AS winnerCount  "
				+ "FROM Vendor T1 WHERE T1.isDelete = :isDelete AND T1.nama LIKE :keyword "
				+ "ORDER BY " + this.columnPurchaseRequestItemPaggingList(column) + " " + sort;
		
		Query q  = em.createQuery(query)
				.setParameter("isDelete", 0) 
				.setParameter("keyword", "%" + keyword + "%");
		
		
		List<Object[]> result = (List<Object[]>) q.setFirstResult(iDisplayStart)
				.setMaxResults(iDisplayLength)
				.getResultList();
		
		List<Vendor> vendorList = new ArrayList<Vendor>();
		
		List<String> listString = new ArrayList<String>();
		listString.add(keyword);
		listString.add(this.columnPurchaseRequestItemPaggingList(column));
		listString.add(sort);

		for(Object[] obj : result){
			Vendor vendor = new Vendor();
			vendor = (Vendor) obj[0];
			vendor.setPengadaanCount(obj[1] == null ? 0 : Integer.valueOf(obj[1].toString()));
			vendor.setPengadaanRunningCount(obj[2] == null ? 0 : Integer.valueOf(obj[2].toString()));
			vendor.setWinnerCount(obj[3] == null ? 0 : Integer.valueOf(obj[3].toString()));
			vendor.setParamDataTable(listString);			
			vendorList.add(vendor);
		}
		

		return vendorList;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public Integer getVendorPaggingSize(String keyword) {

		String query = 
			"SELECT COUNT(T1) "
			+ " FROM Vendor T1 WHERE T1.isDelete = :isDelete AND T1.nama LIKE :keyword ";
		Query q  = em.createQuery(query)
				.setParameter("isDelete", 0) 
				.setParameter("keyword", "%" + keyword + "%");
		 
		List list = q.getResultList();
		
		Integer result = 0;
		for (Object p : list) {
			result = Integer.valueOf(p.toString());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public VendorListDTO getAllVendorListForInitial(
			Integer minPengalaman,
			Integer kualifikasiId,
			String subBidangUsaha,
			String tahunMulai,
			String tahunAkhir,
			Double minNilaiKontrak,
			Integer maxBaris,
			Integer halamanKe) {
		
		String queryString = " SELECT * FROM promise_t2_vendor b WHERE VENDOR_ID IN ( SELECT DISTINCT(VENDOR_ID) FROM ( SELECT a.*,d.MULAI_KERJASAMA FROM promise_t2_vendor a " 
							+ " LEFT JOIN promise_t3_vendor_profile b ON a.VENDOR_ID = b.VENDOR_ID " 
							+ " LEFT JOIN promise_t3_segmentasi_vendor c ON a.VENDOR_ID = c.VENDOR_ID "
							+ " LEFT JOIN promise_t3_pengalaman_vendor d ON a.VENDOR_ID = d.VENDOR_ID "
							+ " LEFT JOIN (SELECT VENDOR_ID, COUNT(*)lama FROM promise_t3_pengalaman_vendor GROUP BY VENDOR_ID) e "
							+ " ON a.VENDOR_ID = e.VENDOR_ID "
							+ " WHERE a.ISDELETE = 0 AND a.STATUS = 1 AND (a.STATUS_BLACKLIST=0 OR a.STATUS_BLACKLIST IS NULL) ";
		
		if (minPengalaman != null && minPengalaman > 0) {
			queryString += " AND e.lama >= " + minPengalaman;
		}
		
		if (kualifikasiId != null && kualifikasiId > 0) {
			if(kualifikasiId==3){ //tidak dikualifikasikan
				//no query filter, loss
			} else {
				queryString += " AND b.KUALIFIKASI_VENDOR_ID = " + kualifikasiId;
			}
			
		}
		
		if(!subBidangUsaha.equals("")) {
			queryString += " AND c.SUB_BIDANG_USAHA_ID IN ("+ subBidangUsaha +") ";
		}
		
		if(!tahunMulai.equals("") && !tahunAkhir.equals("")) {
			queryString += " AND (YEAR(d.MULAI_KERJASAMA) BETWEEN '"+ tahunMulai +"' AND '"+ tahunAkhir +"') ";
		}
		
		if(minNilaiKontrak > new Double(0)) {
			queryString += " AND d.NILAI_KONTRAK >= " + minNilaiKontrak;
		}
		
		/*queryString += " GROUP BY a.VENDOR_ID,d.MULAI_KERJASAMA  ORDER BY d.MULAI_KERJASAMA ) b )";*/
		queryString += " GROUP BY a.VENDOR_ID,d.MULAI_KERJASAMA,a.TGL_REGISTRASI,a.IS_APPROVAL,"
				+ " a.STATUS_UNBLACKLIST, a.STATUS_BLACKLIST, a.STATUS_PERPANJANGAN,a.CAMEL_FLAG,"
				+ " a.KOTA, a.PROVINSI, a.DESKRIPSI, a.VENDOR_COMMENT, a.PERFORMANCE_AVG,"
				+ " a.USER_ID,a.USERID, a.UPDATED, a.STATUS, a.RATING, a.PERFORMANCE_DATE,"
				+ " a.PENANGGUNG_JAWAB, a.EMAIL,a.DELETED,a.NPWP, a.NOMOR_TELEPON, a.NAMA,"
				+ " a.ALAMAT, a.BACKGROUND_IMAGE, a.BACKGROUND_IMAGE_SIZE, a.CREATED,"
				+ " a.ISDELETE, a.LOGO_IMAGE, a.LOGO_IMAGE_SIZE, a.GUID_NO, a.AFCO_ID,"
				+ " a.IS_PKS, a.EMAIL_REQUESTOR  ) b )";	//penyesuaian ke mssql order by dihilangkan
		
		
		Query query = getEntityManager().createNativeQuery(queryString, Vendor.class);
		List<Vendor> vendorResultListForSize = query.getResultList();
		Integer totalRow = vendorResultListForSize.size();

		if(maxBaris > 0) {
			queryString += " LIMIT " + maxBaris + " OFFSET " + (((halamanKe - 1) * maxBaris)) ;
		}
		query = getEntityManager().createNativeQuery(queryString, Vendor.class);
		List<Vendor> vendorResultList = query.getResultList();

		logger.info("Query Search = " + queryString);
		
		logger.info("totalRow     = " + totalRow);
		
		VendorListDTO vendorListDTO = new VendorListDTO();
		vendorListDTO.setVendorList(vendorResultList);
		vendorListDTO.setTotalRow(totalRow);
		return vendorListDTO;
	}
	
	@SuppressWarnings("unchecked")
	public VendorListDTO getAllVendorListForInitialWithVendorId(
			Integer minPengalaman,
			Integer kualifikasiId,
			String subBidangUsaha,
			String tahunMulai,
			String tahunAkhir,
			Double minNilaiKontrak,
			Integer maxBaris,
			Integer halamanKe, 
			Integer vendorId) {
		
		String queryString = "SELECT a.*,d.MULAI_KERJASAMA FROM promise_t2_vendor a " 
							+ " LEFT JOIN promise_t3_vendor_profile b ON a.VENDOR_ID = b.VENDOR_ID " 
							+ " LEFT JOIN promise_t3_segmentasi_vendor c ON a.VENDOR_ID = c.VENDOR_ID "
							+ " LEFT JOIN promise_t3_pengalaman_vendor d ON a.VENDOR_ID = d.VENDOR_ID "
							+ " LEFT JOIN (SELECT VENDOR_ID, COUNT(*)lama FROM promise_t3_pengalaman_vendor GROUP BY VENDOR_ID) e "
							+ " ON a.VENDOR_ID = e.VENDOR_ID "
							+ " WHERE a.ISDELETE = 0 AND a.STATUS = 1 AND (a.STATUS_BLACKLIST=0 OR a.STATUS_BLACKLIST IS NULL) "
							+ " AND a.VENDOR_ID="+vendorId+" ";
		
		if (minPengalaman != null && minPengalaman > 0) {
			queryString += " AND e.lama >= " + minPengalaman;
		}
		
		if (kualifikasiId != null && kualifikasiId > 0) {
			if(kualifikasiId==3){ //tidak dikualifikasikan
				//no query filter, loss
			} else {
				queryString += " AND b.KUALIFIKASI_VENDOR_ID = " + kualifikasiId;
			}
			
		}
		
		if(!subBidangUsaha.equals("")) {
			queryString += " AND c.SUB_BIDANG_USAHA_ID IN ("+ subBidangUsaha +") ";
		}
		
		if(!tahunMulai.equals("") && !tahunAkhir.equals("")) {
			queryString += " AND (YEAR(d.MULAI_KERJASAMA) BETWEEN '"+ tahunMulai +"' AND '"+ tahunAkhir +"') ";
		}
		
		if(minNilaiKontrak > new Double(0)) {
			queryString += " AND d.NILAI_KONTRAK >= " + minNilaiKontrak;
		}
		//GROUP BY a.VENDOR_ID,d.MULAI_KERJASAMA
		queryString += " ORDER BY d.MULAI_KERJASAMA ";
		
		Query query = getEntityManager().createNativeQuery(queryString, Vendor.class);
		List<Vendor> vendorResultListForSize = query.getResultList();
		Integer totalRow = vendorResultListForSize.size();

		if(maxBaris > 0) {
			queryString += " OFFSET " + (((halamanKe - 1) * maxBaris)) + " ROWS FETCH NEXT " + maxBaris + " ROWS ONLY ";
			//queryString += " LIMIT " + maxBaris + " OFFSET " + (((halamanKe - 1) * maxBaris)) ;
		}
		query = getEntityManager().createNativeQuery(queryString, Vendor.class);
		List<Vendor> vendorResultList = query.getResultList();

		logger.info("Query Search = " + queryString);
		
		logger.info("totalRow     = " + totalRow);
		
		VendorListDTO vendorListDTO = new VendorListDTO();
		vendorListDTO.setVendorList(vendorResultList);
		vendorListDTO.setTotalRow(totalRow);
		return vendorListDTO;
	}
	
	public Map<String,Long> countStatusApproval(Token token){
		
		Map<String, Long> result = new HashMap<String, Long>();
		
		String qString = "SELECT count(vendor) FROM Vendor vendor WHERE vendor.isDelete = :isDelete "
				+ "AND vendor.statusBlacklist = :statusBlacklist" ;
		Query q = em.createQuery(qString);
		q.setParameter("isDelete", 0);
		q.setParameter("statusBlacklist", 1);
		result.put("blacklist", (Long) q.getSingleResult());
		
		qString = "SELECT count(vendor) FROM Vendor vendor WHERE vendor.isDelete = :isDelete "
				+ "AND vendor.statusUnblacklist = :statusUnblacklist" ;
		q = em.createQuery(qString);
		q.setParameter("isDelete", 0);
		q.setParameter("statusUnblacklist", 1);		
		result.put("unblacklist", (Long) q.getSingleResult());
		
		qString = "SELECT count(vendor) FROM Vendor vendor WHERE vendor.isDelete = :isDelete "
				+ "AND vendor.status = :status" ;
		q = em.createQuery(qString);
		q.setParameter("isDelete", 0);
		q.setParameter("status", 0);
		result.put("calon", (Long) q.getSingleResult());
		
		qString = "SELECT count(vendor) FROM Vendor vendor WHERE vendor.isDelete = :isDelete "
				+ "AND vendor.status = :status AND vendor.afco IN "
				+ "(SELECT roleUser.organisasi.id from RoleUser roleUser where roleUser.isDelete = :isDelete AND roleUser.user.id = :userId)" ;
		q = em.createQuery(qString);
		q.setParameter("isDelete", 0);
		q.setParameter("status", 0);
		int userId = token.getUser().getId(); //get user id from token
		q.setParameter("userId", userId);
		result.put("calonPerAfco", (Long) q.getSingleResult());
		
		qString = "SELECT count(vendor) FROM Vendor vendor WHERE vendor.isDelete = :isDelete "
				+ "AND vendor.statusPerpanjangan = :statusPerpanjangan" ;
		q = em.createQuery(qString);
		q.setParameter("isDelete", 0);
		q.setParameter("statusPerpanjangan", 1);
		result.put("perpanjangan", (Long) q.getSingleResult());
		return result;
		
	}
	
	public Map<String,Integer> countApprovalByUser (Token token){
		
		Integer userId = token.getUser().getId();
		
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		//Count Registrasi
		String qStringApprovalRegUser = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID WHERE "
				+ "APL.USER_ID IS NOT NULL AND AP.STATUS = 1 AND AP.ISDELETE = 0 AND APT.ISDELETE = 0 AND APL.ISDELETE = 0 AND "
				+ "APT.TAHAPAN_ID = 29 AND AP.USER_ID = " + userId;
		
		Query query1 = getEntityManager().createNativeQuery(qStringApprovalRegUser);
		
		String qStringApprovalRegRole = "SELECT COUNT(*) FROM promise_t2_approval_process AP  LEFT JOIN promise_t2_approval_process_type APT "
				+ "ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN promise_t2_role_user RU ON RU.ROLE_ID = APL.ROLE_ID WHERE "
				+ "APL.ROLE_ID IS NOT NULL AND AP.ISDELETE = 0 AND AP.STATUS = 1 AND "
				+ "APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 29 AND APL.ISDELETE = 0 AND RU.ISDELETE = 0 AND RU.USER_ID = " + userId;
		
		Query query2 = getEntityManager().createNativeQuery(qStringApprovalRegRole);
		
		String qStringApprovalRegOrg = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN (SELECT COUNT(*) 'JML', USER_ID, ORGANISASI_ID FROM "
					+ "promise_t2_role_user WHERE isdelete = 0 GROUP BY USER_ID, ORGANISASI_ID )"
				+ "RU ON RU.ORGANISASI_ID = APL.ORGANISASI_ID WHERE APL.ORGANISASI_ID IS NOT NULL AND "
				+ "AP.ISDELETE = 0 AND AP.STATUS = 1 AND APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 29 AND APL.ISDELETE = 0 AND "
				+ "RU.USER_ID = " + userId;
		Query query3 = getEntityManager().createNativeQuery(qStringApprovalRegOrg);
		
		Integer resultRegVendor = (Integer) query1.getSingleResult() + (Integer) query2.getSingleResult() + (Integer) query3.getSingleResult();
		result.put("resultRegVendor", resultRegVendor);
		
		//Count Blacklist
		String qStringApprovalBlacklistUser = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID WHERE "
				+ "APL.USER_ID IS NOT NULL AND AP.STATUS = 1 AND AP.ISDELETE = 0 AND APT.ISDELETE = 0 AND APL.ISDELETE = 0 AND "
				+ "APT.TAHAPAN_ID = 31 AND AP.USER_ID = " + userId;
		
		Query query4 = getEntityManager().createNativeQuery(qStringApprovalBlacklistUser);
		
		String qStringApprovalBlacklistRole = "SELECT COUNT(*) FROM promise_t2_approval_process AP  LEFT JOIN promise_t2_approval_process_type APT "
				+ "ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN promise_t2_role_user RU ON RU.ROLE_ID = APL.ROLE_ID WHERE "
				+ "APL.ROLE_ID IS NOT NULL AND AP.ISDELETE = 0 AND AP.STATUS = 1 AND "
				+ "APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 31 AND APL.ISDELETE = 0 AND RU.ISDELETE = 0 AND RU.USER_ID = " + userId;
		
		Query query5 = getEntityManager().createNativeQuery(qStringApprovalBlacklistRole);
		
		String qStringApprovalBlacklistOrg = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN (SELECT COUNT(*) 'JML', USER_ID, ORGANISASI_ID FROM "
					+ "promise_t2_role_user WHERE isdelete = 0 GROUP BY USER_ID, ORGANISASI_ID )"
				+ "RU ON RU.ORGANISASI_ID = APL.ORGANISASI_ID WHERE APL.ORGANISASI_ID IS NOT NULL AND "
				+ "AP.ISDELETE = 0 AND AP.STATUS = 1 AND APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 31 AND APL.ISDELETE = 0 AND "
				+ "RU.USER_ID = " + userId;
		Query query6 = getEntityManager().createNativeQuery(qStringApprovalBlacklistOrg);
		
		Integer resultBlacklistVendor = (Integer) query4.getSingleResult() + (Integer) query5.getSingleResult() + (Integer) query6.getSingleResult();
		result.put("resultBlacklistVendor", resultBlacklistVendor);
		
		//Count Unblacklist
		String qStringApprovalUnblacklisttUser = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID WHERE "
				+ "APL.USER_ID IS NOT NULL AND AP.STATUS = 1 AND AP.ISDELETE = 0 AND APT.ISDELETE = 0 AND APL.ISDELETE = 0 AND "
				+ "APT.TAHAPAN_ID = 32 AND AP.USER_ID = " + userId;
		
		Query query7 = getEntityManager().createNativeQuery(qStringApprovalUnblacklisttUser);
		
		String qStringApprovalUnblacklistRole = "SELECT COUNT(*) FROM promise_t2_approval_process AP  LEFT JOIN promise_t2_approval_process_type APT "
				+ "ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN promise_t2_role_user RU ON RU.ROLE_ID = APL.ROLE_ID WHERE "
				+ "APL.ROLE_ID IS NOT NULL AND AP.ISDELETE = 0 AND AP.STATUS = 1 AND "
				+ "APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 32 AND APL.ISDELETE = 0 AND RU.ISDELETE = 0 AND RU.USER_ID = " + userId;
		
		Query query8 = getEntityManager().createNativeQuery(qStringApprovalUnblacklistRole);
		
		String qStringApprovalUnblacklistOrg = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN (SELECT COUNT(*) 'JML', USER_ID, ORGANISASI_ID FROM "
					+ "promise_t2_role_user WHERE isdelete = 0 GROUP BY USER_ID, ORGANISASI_ID )"
				+ "RU ON RU.ORGANISASI_ID = APL.ORGANISASI_ID WHERE APL.ORGANISASI_ID IS NOT NULL AND "
				+ "AP.ISDELETE = 0 AND AP.STATUS = 1 AND APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 32 AND APL.ISDELETE = 0 AND "
				+ "RU.USER_ID = " + userId;
		Query query9 = getEntityManager().createNativeQuery(qStringApprovalUnblacklistOrg);
		
		Integer resultUnblacklistVendor = (Integer) query7.getSingleResult() + (Integer) query8.getSingleResult() + (Integer) query9.getSingleResult();
		result.put("resultUnblacklistVendor", resultUnblacklistVendor);
		
		//Count Perpanjangan TDR
		String qStringApprovalPerpanjanganTdrUser = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID WHERE "
				+ "APL.USER_ID IS NOT NULL AND AP.STATUS = 1 AND AP.ISDELETE = 0 AND APT.ISDELETE = 0 AND APL.ISDELETE = 0 AND "
				+ "APT.TAHAPAN_ID = 36 AND AP.USER_ID = " + userId;
		
		Query query10 = getEntityManager().createNativeQuery(qStringApprovalPerpanjanganTdrUser);
		
		String qStringApprovalPerpanjanganTdrRole = "SELECT COUNT(*) FROM promise_t2_approval_process AP  LEFT JOIN promise_t2_approval_process_type APT "
				+ "ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN promise_t2_role_user RU ON RU.ROLE_ID = APL.ROLE_ID WHERE "
				+ "APL.ROLE_ID IS NOT NULL AND AP.ISDELETE = 0 AND AP.STATUS = 1 AND "
				+ "APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 36 AND APL.ISDELETE = 0 AND RU.ISDELETE = 0 AND RU.USER_ID = " + userId;
		
		Query query11 = getEntityManager().createNativeQuery(qStringApprovalPerpanjanganTdrRole);
		
		String qStringApprovalPerpanjanganTdrOrg = "SELECT COUNT(*) FROM promise_t2_approval_process AP "
				+ "LEFT JOIN promise_t2_approval_process_type APT ON AP.APPROVAL_PROCESS_TYPE_ID = APT.APPROVAL_PROCESS_TYPE_ID "
				+ "LEFT JOIN promise_t3_approval_level APL ON AP.APPROVAL_LEVEL_ID = APL.APPROVAL_LEVEL_ID "
				+ "LEFT JOIN (SELECT COUNT(*) 'JML', USER_ID, ORGANISASI_ID FROM "
					+ "promise_t2_role_user WHERE isdelete = 0 GROUP BY USER_ID, ORGANISASI_ID )"
				+ "RU ON RU.ORGANISASI_ID = APL.ORGANISASI_ID WHERE APL.ORGANISASI_ID IS NOT NULL AND "
				+ "AP.ISDELETE = 0 AND AP.STATUS = 1 AND APT.ISDELETE = 0 AND APT.TAHAPAN_ID = 36 AND APL.ISDELETE = 0 AND "
				+ "RU.USER_ID = " + userId;
		Query query12 = getEntityManager().createNativeQuery(qStringApprovalPerpanjanganTdrOrg);
		
		Integer resultPerpanjanganTdrVendor = (Integer) query10.getSingleResult() + (Integer) query11.getSingleResult() + (Integer) query12.getSingleResult();
		result.put("resultPerpanjanganTdrVendor", resultPerpanjanganTdrVendor);
		
		
		return result;
		
	}

	@SuppressWarnings("rawtypes")
	public List<DashboardOrderDTO> countByMonth(Token token) {
		
		Query q = em.createQuery("SELECT YEAR(v.created) as year,MONTH(v.created) as month,v.status as status, count(*) FROM Vendor v WHERE v.isDelete = 0  group by YEAR(v.created),MONTH(v.created),v.status ORDER BY 1 ASC,2 ASC ");
		
		Iterator iterator = q.getResultList().iterator();
		List<DashboardOrderDTO> list = new ArrayList<DashboardOrderDTO>();
		while(iterator.hasNext()){
			Object[] tuple = (Object[]) iterator.next();
			DashboardOrderDTO dashboardOrderDTO;
			Boolean add = true;
			if(list.size() > 0){
				DashboardOrderDTO tempDashboardOrderDTO = list.get(list.size()-1);
				if(tempDashboardOrderDTO.getYear() == (Integer) tuple[0] && tempDashboardOrderDTO.getMonth() == (Integer) tuple[1]){
					dashboardOrderDTO = tempDashboardOrderDTO;
					add = false;
				}
				else{
					dashboardOrderDTO = new DashboardOrderDTO();
					dashboardOrderDTO.setYear((Integer) tuple[0]);
					dashboardOrderDTO.setMonth((Integer) tuple[1]);
				}
			}
			
			else {
				dashboardOrderDTO = new DashboardOrderDTO();
				dashboardOrderDTO.setYear((Integer) tuple[0]);
				dashboardOrderDTO.setMonth((Integer) tuple[1]);
			}
			
			
			int status = (Integer) tuple[2];
			if (status == 1){
				dashboardOrderDTO.setVendor((Long) tuple[3]);
			}
			else if (status == 0){
				dashboardOrderDTO.setCalonVendor((Long) tuple[3]);
			}
			
			if (add){
				list.add(dashboardOrderDTO);
			}
			else{
				list.set(list.size()-1, dashboardOrderDTO);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<DashboardOrderDTO> countByYear(Token findByToken) {
		Query q = em.createQuery("SELECT YEAR(v.created) as year , v.status as status, count(*) FROM Vendor v WHERE v.isDelete = 0  group by YEAR(v.created),v.status ORDER BY 1 ASC ");
		
		Iterator iterator = q.getResultList().iterator();
		List<DashboardOrderDTO> list = new ArrayList<DashboardOrderDTO>();
		while(iterator.hasNext()){
			Object[] tuple = (Object[]) iterator.next();
			DashboardOrderDTO dashboardOrderDTO;
			Boolean add = true;
			if(list.size() > 0){
				DashboardOrderDTO tempDashboardOrderDTO = list.get(list.size()-1);
				if(tempDashboardOrderDTO.getYear() == (Integer) tuple[0]){
					dashboardOrderDTO = tempDashboardOrderDTO;
					add = false;
				}
				else{
					dashboardOrderDTO = new DashboardOrderDTO();
					dashboardOrderDTO.setYear((Integer) tuple[0]);
				}
			}
			
			else {
				dashboardOrderDTO = new DashboardOrderDTO();
				dashboardOrderDTO.setYear((Integer) tuple[0]);
			}
			
			
			int status = (Integer) tuple[1];
			if (status == 1){
				dashboardOrderDTO.setVendor((Long) tuple[2]);
			}
			else if (status == 0){
				dashboardOrderDTO.setCalonVendor((Long) tuple[2]);
			}
			
			if (add){
				list.add(dashboardOrderDTO);
			}
			else{
				list.set(list.size()-1, dashboardOrderDTO);
			}
			
			
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List<DashboardOrderDTO> countByWeek(Token findByToken) {
		//Query q = em.createQuery("SELECT YEAR(v.created) as year, week(v.created) as week, v.status as status, count(*) FROM Vendor v WHERE v.isDelete = 0  group by YEAR(v.created), week(v.created), v.status ORDER BY 1 ASC,2 ASC ");
		
		//Ubah ke native query
		String query = "SELECT YEAR (v.created) AS YEAR, DATEPART(week, v.CREATED) as WEEK, v.status AS status, COUNT (*) "
				+ "FROM promise_t2_vendor v WHERE v.isDelete = 0 GROUP BY YEAR (v.created), DATEPART(week, v.CREATED), v.status "
				+ "ORDER BY 1 ASC, 2 ASC";
			
		Query q  = em.createNativeQuery(query);
		
		Iterator iterator = q.getResultList().iterator();
		List<DashboardOrderDTO> list = new ArrayList<DashboardOrderDTO>();
		while(iterator.hasNext()){
			Object[] tuple = (Object[]) iterator.next();
			DashboardOrderDTO dashboardOrderDTO;
			Boolean add = true;
			if(list.size() > 0){
				DashboardOrderDTO tempDashboardOrderDTO = list.get(list.size()-1);
				if(tempDashboardOrderDTO.getYear() == (Integer) tuple[0] && tempDashboardOrderDTO.getWeek() == (Integer) tuple[1]){
					dashboardOrderDTO = tempDashboardOrderDTO;
					add = false;
				}
				else{
					dashboardOrderDTO = new DashboardOrderDTO();
					dashboardOrderDTO.setYear((Integer) tuple[0]);
					dashboardOrderDTO.setWeek((Integer) tuple[1]);
				}
			}
			
			else {
				dashboardOrderDTO = new DashboardOrderDTO();
				dashboardOrderDTO.setYear((Integer) tuple[0]);
				dashboardOrderDTO.setWeek((Integer) tuple[1]);
			}
			
			
			int status = (Integer) tuple[2];
			if (status == 1){
				dashboardOrderDTO.setVendor((Integer) tuple[3]);
			}
			else if (status == 0){
				dashboardOrderDTO.setCalonVendor((Integer) tuple[3]);
			}
			
			if (add){
				list.add(dashboardOrderDTO);
			}
			else{
				list.set(list.size()-1, dashboardOrderDTO);
			}
			
			
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListWithPagination(Integer start, Integer length, String keyword, Integer columnOrder, String tipeOrder) {

		String queryUser = "SELECT vendor FROM Vendor vendor WHERE vendor.isDelete =:isDelete AND vendor.status =:status AND "
				+ "(vendor.nama like :keyword OR vendor.nomorTelpon like :keyword OR vendor.email like :keyword OR vendor.created like :keyword OR "
				+ "vendor.status like :keyword OR vendor.updated like :keyword) ";

		String[] columnToView = { "id", "nama", "nomorTelpon", "email", "created", "status", "updated"};
		if (columnOrder > 0) {
			queryUser+="ORDER BY vendor. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+="ORDER BY vendor.id desc ";
		}

		Query query = em.createQuery(queryUser);
		query.setParameter("isDelete", 0);
		query.setParameter("status", 1);
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<Vendor> vendorList = query.getResultList();
		return vendorList;
	}

	public long getVendorListCount(String keyword) {
		String queryCountUser = "SELECT COUNT(vendor) FROM Vendor vendor WHERE vendor.isDelete = :isDelete AND vendor.status = :status AND "
				+ "(vendor.nama like :keyword OR vendor.nomorTelpon like :keyword OR vendor.email like :keyword OR vendor.created like :keyword OR "
				+ "vendor.status like :keyword OR vendor.updated like :keyword) ";

		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", keyword);
		query.setParameter("isDelete", 0);
		query.setParameter("status", 1);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}
	
	//fungsi vendor statistik
	@SuppressWarnings("unchecked")
	public String countVendorByDate() {
		
		String query = "Select count(*), promise_t2_vendor.TGL_REGISTRASI from promise_t2_vendor group by "
				+ "CAST(promise_t2_vendor.TGL_REGISTRASI AS DATE) order by promise_t2_vendor.TGL_REGISTRASI desc";
			
		Query q  = em.createNativeQuery(query).setFirstResult(0).setMaxResults(20);
		
		String content = "";
		List<Object[]> objList = q.getResultList();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		content += "[";
		for (int i = objList.size() - 1; i >= 0; i--) {
			Object[] result = (Object[]) objList.get(i);
			Date tgl = (Date) result[1];
			int objVendor = ((Number) result[0]).intValue();
			if (i == 0) {
				content += "{\"tgl\":\"" + df.format(tgl).toString() + "\", \"vendor\":" + objVendor + "}";
			} else {
				content += "{\"tgl\":\"" + df.format(tgl).toString() + "\", \"vendor\":" + objVendor + "},";
			}
		}
		content += "]";
		return content;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getVendorListForExportExcel() {
		
		String query = "SELECT T1, "
				+ "(SELECT COUNT(o) FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0 AND o.vendor.id = T1.id ) AS pengadaanCount,  "
				+ "(SELECT COUNT(pendaftaranVendor) FROM PendaftaranVendor pendaftaranVendor WHERE pendaftaranVendor.isDelete = 0 AND pendaftaranVendor.vendor.id = T1.id) +  "
				+ "(SELECT COUNT(x) FROM PendaftaranVendorPraKualifikasi x WHERE x.isDelete = 0 AND x.vendor.id = T1.id AND pengadaan.tahapanPengadaan.tahapan.id NOT IN (10050000, 10120000))  AS pengadaanRunningCount, "
				+ "(SELECT count(k) FROM Kontrak k WHERE k.isDelete = 0 AND k.vendor.id = T1.id ) AS winnerCount  "
				+ "FROM Vendor T1 WHERE T1.isDelete = :isDelete "
				+ "ORDER BY T1.id asc ";
		
		Query q  = em.createQuery(query)
				.setParameter("isDelete", 0);
		 
		List<Object[]> result = (List<Object[]>) q.getResultList();
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListForExportExcel(String keyword,
			String column, String sort) {
		
		String query = "SELECT T1, "
				+ "(SELECT COUNT(o) FROM PerolehanPengadaanTotal o WHERE o.isDelete = 0 AND o.vendor.id = T1.id ) AS pengadaanCount,  "
				+ "(SELECT COUNT(pendaftaranVendor) FROM PendaftaranVendor pendaftaranVendor WHERE pendaftaranVendor.isDelete = 0 AND pendaftaranVendor.vendor.id = T1.id) +  "
				+ "(SELECT COUNT(x) FROM PendaftaranVendorPraKualifikasi x WHERE x.isDelete = 0 AND x.vendor.id = T1.id AND pengadaan.tahapanPengadaan.tahapan.id NOT IN (10050000, 10120000))  AS pengadaanRunningCount, "
				+ "(SELECT count(k) FROM Kontrak k WHERE k.isDelete = 0 AND k.vendor.id = T1.id ) AS winnerCount  "
				+ "FROM Vendor T1 WHERE T1.isDelete = :isDelete AND T1.nama LIKE :keyword "
				+ "ORDER BY " + column + " " + sort;
		
		Query q  = em.createQuery(query)
				.setParameter("isDelete", 0) 
				.setParameter("keyword", "%" + keyword + "%");
		 
		List<Object[]> result = (List<Object[]>) q.getResultList();
		
		List<Vendor> vendorList = new ArrayList<Vendor>();
		for(Object[] obj : result){
			Vendor vendor = new Vendor();
			vendor = (Vendor) obj[0];
			vendor.setPengadaanCount(obj[1] == null ? 0 : Integer.valueOf(obj[1].toString()));
			vendor.setPengadaanRunningCount(obj[2] == null ? 0 : Integer.valueOf(obj[2].toString()));
			vendor.setWinnerCount(obj[3] == null ? 0 : Integer.valueOf(obj[3].toString()));
			vendorList.add(vendor);
		}
		
		
		return vendorList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorPerformaForExcel(
			String namaVendor,
			String bidangUsaha,
			String subBidangUsaha,
			String pernahdinilai,
			String belumpernahdinilai,
			String keyword,
			String column, 
			String sort) {
		
		String query = "SELECT vendor.VENDOR_ID, vendor.NAMA, vendor.EMAIL,vendor.PERFORMANCE_DATE, vendor.PERFORMANCE_AVG "
				+ "from promise_t2_vendor vendor LEFT JOIN promise_t3_Segmentasi_vendor sv ON vendor.VENDOR_ID = sv.VENDOR_ID "
				+ "LEFT JOIN promise_t2_sub_bidang_usaha sbu on sv.SUB_BIDANG_USAHA_ID = sbu.SUB_BIDANG_USAHA_ID "
				+ "WHERE vendor.ISDELETE = 0 AND " + "(vendor.NAMA like :keyword OR vendor.EMAIL like :keyword) ";
		
		if (namaVendor != null) {
			query = query + "AND vendor.NAMA LIKE :nama ";
		}

		
		if ((pernahdinilai != null && !pernahdinilai.equals("")) && (belumpernahdinilai != null && !belumpernahdinilai.equals(""))) {
			query = query + "AND (vendor.PERFORMANCE_AVG > 0 OR vendor.PERFORMANCE_AVG = 0) ";
		} else {
			if (pernahdinilai != null && !pernahdinilai.equals("")) {
				query = query + "AND vendor.PERFORMANCE_AVG > 0 ";
			} else if (belumpernahdinilai != null && !belumpernahdinilai.equals("")) {
				query = query + "AND vendor.PERFORMANCE_AVG = 0 ";
			}
		}

		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			query = query + "AND sv.SUB_BIDANG_USAHA_ID = :subBidangUsaha ";
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				query = query + "AND sbu.BIDANG_USAHA_ID = :bidangUsaha ";
			}
		}
		
		query += " GROUP BY vendor.VENDOR_ID, vendor.NAMA, vendor.EMAIL,vendor.PERFORMANCE_DATE, vendor.PERFORMANCE_AVG";
				
		query += " ORDER BY " + column + " " + sort;

		Query q = em.createNativeQuery(query);
		q.setParameter("keyword", "%" + keyword + "%");
		if (namaVendor != null) {
			q.setParameter("nama", "%" + namaVendor + "%");
		}
		if (subBidangUsaha != null && !subBidangUsaha.equals("")) {
			q.setParameter("subBidangUsaha", Integer.valueOf(subBidangUsaha));
		} else {
			if (bidangUsaha != null && !bidangUsaha.equals("")) {
				q.setParameter("bidangUsaha", Integer.valueOf(bidangUsaha));
			}
		}
		
		List<Object[]> arrObjList = q.getResultList();
		
		List<Vendor> vendorList = new ArrayList<Vendor>();
		
		for (Object[] obj : arrObjList) {
			Vendor vendorNew = new Vendor();
			vendorNew.setId((Integer) obj[0]);
			vendorNew.setNama((String) obj[1]);
			vendorNew.setEmail((String) obj[2]);
			vendorNew.setPerformanceDate((Date) obj[3]);
			vendorNew.setPerformanceAvg((Double) obj[4]);

			vendorList.add(vendorNew);
		}
		
		return vendorList;		
	}

	public Vendor getVendorIdEproc(String vendorIdEproc) {
		Query q = em.createNamedQuery("Vendor.findByVendorIdEproc");
		q.setParameter("vendorIdEproc", vendorIdEproc);
		List<Vendor> vendorList = q.getResultList();
		if (vendorList != null && vendorList.size() > 0) {
			return vendorList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Vendor> getListPagination(String search, String vendorName, String orderKeyword, Integer pageNo, Integer pageSize, String asc, Date sysDate,
			Date startDate, Date endDate) {
		String query = "SELECT vendor, sv from Vendor vendor right JOIN SegmentasiVendor sv ON vendor.id = sv.vendor "
				+ "WHERE vendor.isDelete = 0 and sv.isDelete = 0 and vendor.status = 1 ";
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("")? null
				: vendorName;
		if (!search.isEmpty()) {
			query = query + "and (vendor.nama like :search) ";
		}
		if (vendorName != null) {
			query = query + "and sv.subBidangUsaha.nama =:vendorName ";
		}

		if (sysDate !=null) {
			query = query + " and vendor.id in(select ck.vendor from CatalogKontrak ck where ck.isDelete = 0 and :date < ck.tglAkhirKontrak and :date > ck.tglMulailKontrak) ";
		}
		if (startDate != null && endDate != null) {
			query = query + " and vendor.id in "
					+ "( "
					+ "	select ck.vendor from CatalogKontrak ck "
					+ "		where ((ck.tglMulailKontrak >= :startDate and ck.tglMulailKontrak <= :endDate ) " 
					+ "				or (ck.tglAkhirKontrak >= :startDate and ck.tglAkhirKontrak <= :endDate ) " 
					+ "				or (ck.tglMulailKontrak <= :startDate and ck.tglAkhirKontrak >= :startDate ) " 
					+ "				or (ck.tglMulailKontrak <= :endDate and ck.tglAkhirKontrak >= :endDate )) "
					+ ") ";
		}
		if (!orderKeyword.isEmpty() ) {
			query = query + "order by sv." + orderKeyword +" " + asc ;
		}

		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (sysDate !=null) {
			q.setParameter("date", sysDate);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		  q.setFirstResult((pageNo - 1) * pageSize);
		  q.setMaxResults(pageSize);
		 
		return q.getResultList();
	}
	
	public Long getTotalList(String search, String vendorName,
			Integer pageNo, Integer pageSize, Date sysDate, Date startDate, Date endDate) {
		String query = "SELECT count (vendor) FROM Vendor vendor  JOIN SegmentasiVendor sv on vendor.id = sv.vendor "
				+ "where vendor.isDelete = 0 and sv.isDelete = 0 ";
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null
				: vendorName;
		if (!search.isEmpty()) {
			query = query + "and (vendor.nama like :search) ";
		}
		if (vendorName != null) {
			query = query + "and sv.subBidangUsaha.bidangUsaha.nama =:vendorName ";
		}
		if (sysDate !=null) {
			query = query + "and vendor.id in(select ck.vendor from CatalogKontrak ck where ck.isDelete = 0 and :date < ck.tglAkhirKontrak and :date > ck.tglMulailKontrak) ";
		}
		if (startDate != null && endDate != null) {
			query = query + "and vendor.id in (select ck.vendor from CatalogKontrak ck where ck.tglAkhirKontrak >= :startDate and ck.tglAkhirKontrak <= :endDate ) ";
		}

		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (sysDate !=null) {
			q.setParameter("date", sysDate);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Object[]> getExcel(String search, String vendorName, String orderKeyword, Integer pageNo, Integer pageSize) {
		String query = "SELECT vendor, sv from Vendor vendor right JOIN SegmentasiVendor sv ON vendor.id = sv.vendor "
				+ "WHERE vendor.isDelete = 0 and sv.isDelete = 0 ";
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null
				: vendorName;
		if (!search.isEmpty()) {
			query = query + "and (vendor.nama like :search) ";
		}
		if (vendorName != null) {
			query = query + "and sv.subBidangUsaha.bidangUsaha.nama =:vendorName ";
		}

		if (!orderKeyword.isEmpty() ) {
			query = query + "order by sv." + orderKeyword ;
		}

		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		 
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getReportEvaluasiKinerjaVendor( String vendorName, String orderKeyword, Integer pageNo,
			Integer pageSize, String sort) {
			String query = "SELECT vendor FROM Vendor vendor "
					+ "WHERE vendor IN (SELECT ck.vendor FROM CatalogKontrak ck WHERE ck.isDelete = 0) "
					+ "AND vendor.isDelete = 0 "
					+ "AND vendor.status = 1 ";
		vendorName = vendorName == null ? "" : vendorName.trim();
		orderKeyword = orderKeyword == null || orderKeyword.equals("undefined") || orderKeyword.equals("") ? null : orderKeyword;
		
		if (!vendorName.isEmpty()) {
			query = query + "AND (vendor.nama like :vendorName) ";
		}
		if (orderKeyword != null) {
			query = query + "AND vendor.nama =:orderKeyword ";
		}
		query = query + "ORDER BY vendor.nama " + sort;
	
		Query q = getEntityManager().createQuery(query);
		if (!vendorName.isEmpty()) {
			q.setParameter("vendorName", "%" + vendorName + "%");		
		}
		if (orderKeyword != null) {
			q.setParameter("orderKeyword", orderKeyword);
		}
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);
			
		return q.getResultList();
	}
		
		
		@SuppressWarnings("unchecked")
	public List<Vendor> getExcelReportEvaluasiKinerjaVendor( String vendorName, String orderKeyword, String sort) {
			String query = "SELECT vendor FROM Vendor vendor "
					+ "WHERE vendor IN (SELECT ck.vendor FROM CatalogKontrak ck WHERE ck.isDelete = 0) "
					+ "AND vendor.isDelete = 0 "
					+ "AND vendor.status = 1 ";
		vendorName = vendorName == null ? "" : vendorName.trim();
		orderKeyword = orderKeyword == null || orderKeyword.equals("undefined") || orderKeyword.equals("") ? null : orderKeyword;
		
		if (!vendorName.isEmpty()) {
			query = query + "AND (vendor.nama like :vendorName) ";
		}
		if (orderKeyword != null) {
			query = query + "AND vendor.nama =:orderKeyword ";
		}
		query = query + "ORDER BY vendor.nama " + sort;
	
		Query q = getEntityManager().createQuery(query);
		if (!vendorName.isEmpty()) {
			q.setParameter("vendorName", "%" + vendorName + "%");		
		}
		if (orderKeyword != null) {
			q.setParameter("orderKeyword", orderKeyword);
		}
			
		return q.getResultList();
	}
	
	public Long getTotalListEvaluasi(String vendorName,String orderKeyword) {
		String query = "SELECT count (vendor) FROM Vendor vendor "
				+ "WHERE vendor IN (SELECT ck.vendor FROM CatalogKontrak ck WHERE ck.isDelete = 0) "
				+ "AND vendor.isDelete = 0 "
				+ "AND vendor.status = 1 ";
		vendorName = vendorName == null ? "" : vendorName.trim();
		orderKeyword = orderKeyword == null || orderKeyword.equals("undefined") || orderKeyword.equals("") ? null : orderKeyword;
	
		if (vendorName.equalsIgnoreCase("")) {
			query = query + "and (vendor.nama like :vendorName) ";
		}
		if (orderKeyword != null ) {
			query = query + "and vendor.nama =:orderKeyword ";
		}
	
		Query q = getEntityManager().createQuery(query);
		if (vendorName.equalsIgnoreCase("")) {
			q.setParameter("vendorName", "%" + vendorName + "%");
		}
		if (orderKeyword != null) {
			q.setParameter("orderKeyword", orderKeyword);
		}
	
		return (Long) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListForReportCatalog() {
	
		Query q = em.createQuery("SELECT vendor FROM Vendor vendor "
				+ "WHERE vendor IN (SELECT catalog.vendor FROM Catalog catalog WHERE catalog.isDelete = 0) "
				+ "AND vendor.isDelete = 0 "
				+ "AND vendor.status = 1 ");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Vendor> getVendorListForCatalogContract() {	
		Query q = em.createQuery("SELECT vendor FROM Vendor vendor "
				+ "WHERE vendor IN (SELECT ck.vendor FROM CatalogKontrak ck WHERE ck.isDelete = 0) "
				+ "AND vendor.isDelete = 0 "
				+ "AND vendor.status = 1 ");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	  public List<Vendor> getVendorListForPurchaseOrderIndex() {  
	    Query q = em.createQuery("SELECT vendor FROM Vendor vendor "
	        + "WHERE vendor IN (SELECT ck.vendor FROM CatalogKontrak ck, "
	        + "Catalog catalog, "
	        + "PurchaseRequestItem purchaseRequestItem, "
	        + "PurchaseRequest purchaseRequest, "
	        + "PurchaseOrder purchaseOrder Where "
	        + "ck.id = catalog.catalogKontrak "	      
	        + "And purchaseRequestItem.catalog = catalog.id "	     
	        + "And purchaseRequest.id = purchaseRequestItem.purchaserequest "	      
	        + "And purchaseOrder.purchaseRequest = purchaseRequest.id "
	        + "And ck.isDelete = 0) "
	        + "AND vendor.isDelete = 0 "
	        + "AND vendor.status = 1 ");
	    return q.getResultList();
	  }
}
