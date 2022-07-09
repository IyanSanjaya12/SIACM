package id.co.promise.procurement.catalog.session;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.co.promise.procurement.DTO.LaporanItemKatalogPoDTO;
import id.co.promise.procurement.approval.ApprovalProcessSession;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.AttributeGroup;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogAttribute;
import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.catalog.entity.CatalogLocation;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.entity.CategoryAttribute;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.StaticProperties;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class CatalogSession extends AbstractFacadeWithAudit<Catalog> {
	
	final static Logger log = Logger.getLogger(CatalogSession.class);

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	private CategorySession categorySession;
	
	@EJB
	private VendorSession vendorSession;
	
	@EJB
	private SatuanSession satuanSession;
	
	@EJB
	private MataUangSession mataUangSession;
	
	@EJB
	private CatalogCategorySession catalogCategorySession;
	
	@EJB
	private CatalogImageSession catalogImageSession;
	
	@EJB
	private CatalogLocationSession catalogLocationSession;
	
	@EJB
	private CatalogAttributeSession catalogAttributeSession;
	
	@EJB
	private AttributeGroupSession attributeGroupSession;
	
	@EJB 
	private AttributeSession attributeSession;
	
	@EJB 
	private CategoryAttributeSession categoryAttributeSession;
	
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	@EJB
	private ApprovalProcessSession approvalProcessSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	OrganisasiSession organisasiSession;
	
	
	public CatalogSession() {
		super(Catalog.class);
	}
	
	public Boolean checkKodePanitiaCatalog(String kodeProdukPanitia, String toDo, Integer catalogId) {
		List<Catalog> catalogList = this.getCatalogBykodeProdukPanitia(kodeProdukPanitia);
		  
		Boolean isSave = false;
		if(toDo.equalsIgnoreCase("insert")) {
			if(catalogList != null && catalogList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
		   
		} else if (toDo.equalsIgnoreCase("update")) {
		   if(catalogList != null && catalogList.size() > 0) {
			   if(catalogId.equals( catalogList.get(0).getId())) {
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

	public Catalog find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Catalog> criteria = builder.createQuery(Catalog.class);
		Root<Catalog> entityRoot = criteria.from(Catalog.class);

		criteria.select(entityRoot);

		criteria.where(builder.equal(entityRoot.get("id"), id));

		try {
			List<Catalog> attributeTypeValueList = em.createQuery(criteria)
					.getResultList();
			if (attributeTypeValueList != null
					&& attributeTypeValueList.size() > 0) {
				return attributeTypeValueList.get(0);
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogBykodeProdukPanitia(String kodeProdukPanitia) {
		Query q = em.createQuery("SELECT catalog FROM Catalog catalog WHERE catalog.isDelete = 0 AND catalog.kodeProdukPanitia = :kodeProdukPanitia");
		q.setParameter("kodeProdukPanitia", kodeProdukPanitia);		
		List<Catalog> catalog = q.getResultList();
		return catalog;
	}
	
	@SuppressWarnings("unchecked")
	public Catalog getCatalogByKodeProduk(String kodeProduk) {
		Query q = em.createQuery("SELECT catalog FROM Catalog catalog WHERE catalog.isDelete = 0 AND catalog.kodeProduk = :kodeProduk");
		q.setParameter("kodeProduk", kodeProduk);
		
		List<Catalog> catalog = q.getResultList();
		if (catalog != null && catalog.size() > 0) {
			return catalog.get(0);
		}
		
		return null;
	}

	public List<Catalog> getCatalogList() {
		return getCatalogList(null, null, null, null, null);
	}

	public List<Catalog> getCatalogList(Catalog entity) {
		return getCatalogList(entity, null, null, null, null);
	}

	public List<Catalog> getCatalogList(Catalog entity, Integer startRow,
			Integer rowSize) {
		return getCatalogList(entity, startRow, rowSize, null, null);
	}

	public List<Catalog> getCatalogList(Catalog entity, String fieldName,
			OrderTypeEnum orderType) {
		return getCatalogList(entity, null, null, fieldName, orderType);
	}

	public List<Catalog> getCatalogList(Catalog entity, Integer startRow,
			Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Catalog> criteria = builder.createQuery(Catalog.class);
		Root<Catalog> entityRoot = criteria.from(Catalog.class);

		criteria.select(entityRoot);

		Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);

		criteria.where(whereClause);
		

		if (fieldName != null) {
			if (orderType == OrderTypeEnum.ASC) {
				criteria.orderBy(builder.asc(entityRoot.get(fieldName)));
			} else {
				criteria.orderBy(builder.desc(entityRoot.get(fieldName)));
			}
		}

		try {
			if (startRow != null) {
				return getEntityManager().createQuery(criteria)
						.setFirstResult(startRow).setMaxResults(rowSize)
						.getResultList();
			} else {
				return getEntityManager().createQuery(criteria).getResultList();
			}
		} catch (Exception ex) {
			return null;
		}
	}

	private Predicate setDefaultPredicate(CriteriaBuilder builder,
			Root<Catalog> entityRoot, Catalog entity) {
		Predicate whereClause = builder.conjunction();
		if (entity != null) {
			if (entity.getKodeProduk() != null
					&& entity.getKodeProduk().equals(StaticProperties.BLANK) == false
					&& entity.getKodeProduk().indexOf("%") == -1) {
				whereClause = builder.and(
						whereClause,
						builder.equal(entityRoot.get("kodeProduk"),
								entity.getKodeProduk()));
			}
			if (entity.getKodeProduk() != null
					&& entity.getKodeProduk().equals(StaticProperties.BLANK) == false
					&& entity.getKodeProduk().indexOf("%") >= 0) {
				whereClause = builder.and(whereClause, builder.like(entityRoot
						.get("kodeProduk").as(String.class), entity
						.getKodeProduk()));
			}
			if (entity.getKodeProdukPanitia() != null
					&& entity.getKodeProdukPanitia().equals(
							StaticProperties.BLANK) == false
					&& entity.getKodeProdukPanitia().indexOf("%") == -1) {
				whereClause = builder.and(whereClause, builder.equal(
						entityRoot.get("kodeProdukPanitia"),
						entity.getKodeProdukPanitia()));
			}
			if (entity.getKodeProdukPanitia() != null
					&& entity.getKodeProdukPanitia().equals(
							StaticProperties.BLANK) == false
					&& entity.getKodeProdukPanitia().indexOf("%") >= 0) {
				whereClause = builder.and(
						whereClause,
						builder.like(
								entityRoot.get("kodeProdukPanitia").as(
										String.class),
								entity.getKodeProdukPanitia()));
			}
			if (entity.getVendor() != null) {
				whereClause = builder.and(
						whereClause,
						builder.equal(entityRoot.get("vendor"),
								entity.getVendor()));
			}
			if (entity.getStatus() != null ) {
				whereClause = builder.and(
						whereClause,
						builder.equal(entityRoot.get("status"),
								entity.getStatus()));
			}
			if (entity.getIsVendor() != null) {
				whereClause = builder.and(
						whereClause,
						builder.equal(entityRoot.get("isVendor"),
								entity.getIsVendor()));
			}
		}
		whereClause = builder.and(whereClause,
				builder.equal(entityRoot.get("isDelete"), 0));

		return whereClause;
	}

	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogListByVendor(Integer vendorId) {
		Query q = em
				.createQuery("SELECT c FROM Catalog c WHERE catalog.isDelete = 0 and catalog.vendor.id = :vendorId ORDER BY catalog.id ASC");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogListByKontrak(String keyword,
			Integer vendorId, Boolean isKontrak, Integer iDisplayStart,
			Integer iDisplayLength, String fieldName, String orderType) {
		String vQuery = "select distinct c from CatalogKontrak ck ";
		if (isKontrak) {
			vQuery += " inner ";
		} else {
			vQuery += " right ";
		}
		vQuery += " join ck.catalog c left join catalog.vendor v left join catalog.attributeGroup ag left join catalog.satuan s where catalog.isDelete = 0 and catalog.namaIND != null";
		if (vendorId != null) {
			vQuery += " and ck.id is null and catalog.isVendor = 1 and v.id = :vendorId ";
		}
		if (keyword != null) {
			vQuery += " and (catalog.namaIND like :keyword or v.nama like :keyword or ag.name like :keyword or catalog.harga like :keyword or s.nama like :keyword) ";
		}
		if (fieldName != null) {
			vQuery += " order by catalog." + fieldName + " " + orderType;
		}
		Query query = getEntityManager().createQuery(vQuery);
		if (keyword != null) {
			query.setParameter("keyword", "%" + keyword + "%");
		}
		if (vendorId != null) {
			query.setParameter("vendorId", vendorId);
		}
		query.setFirstResult(iDisplayStart);
		if (iDisplayLength != null) {
			query.setMaxResults(iDisplayLength);
		}

		return query.getResultList();
	}

	public Long countCatalogListByKontrak(String keyword, Integer vendorId,
			Boolean isKontrak) {
		String vQuery = "select count(distinct c) from CatalogKontrak ck ";
		if (isKontrak) {
			vQuery += " inner ";
		} else {
			vQuery += " right ";
		}
		vQuery += " join ck.catalog c left join catalog.vendor v left join catalog.attributeGroup ag left join catalog.satuan s where catalog.isDelete = 0 and catalog.namaIND != null";
		if (vendorId != null) {
			vQuery += " and ck.id is null and catalog.isVendor = 1 and v.id = :vendorId ";
		}
		if (keyword != null) {
			vQuery += " and (catalog.namaIND like :keyword or v.nama like :keyword or ag.name like :keyword or catalog.harga like :keyword or s.nama like :keyword) ";
		}
		Query query = getEntityManager().createQuery(vQuery);
		if (keyword != null) {
			query.setParameter("keyword", "%" + keyword + "%");
		}
		if (vendorId != null) {
			query.setParameter("vendorId", vendorId);
		}
		return (Long) query.getSingleResult();
	}

	// Session untuk manage catalog.
	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogListForManageCatalog(String keyword,
			Integer vendorId, Boolean isKontrak, Integer iDisplayStart,
			Integer iDisplayLength, String fieldName, String orderType) {
		String vQuery = "select catalog from Catalog catalog";
		
		if(isKontrak != null && isKontrak) {
			vQuery += " left join CatalogKontrak ck on catalog.id = ck.catalog where ck.isDelete = 0 and catalog.isDelete = 0 and catalog.namaIND != null and ck.statusKontrak = 1";
		} else {
			vQuery += " where catalog.isDelete = 0 and catalog.namaIND != null and catalog.catalogKontrak.statusKontrak = 1";
		}
		
		if (vendorId != null) {
			vQuery += " and catalog.isVendor = 1 and catalog.vendor.id = :vendorId ";
		}
		if (keyword != null) {
			vQuery += " and (catalog.namaIND like :keyword or catalog.vendor.nama like :keyword or catalog.attributeGroup.name like :keyword or catalog.harga like :keyword or catalog.satuan.nama like :keyword) ";
		}
		if (fieldName != null) {
			vQuery += " order by catalog." + fieldName + " " + orderType;
		}else {
			vQuery += " order by catalog.created DESC, catalog.updated DESC";
		}
		Query query = getEntityManager().createQuery(vQuery);
		if (keyword != null) {
			query.setParameter("keyword", "%" + keyword + "%");
		}
		if (vendorId != null) {
			query.setParameter("vendorId", vendorId);
		}
		query.setFirstResult(iDisplayStart);
		if (iDisplayLength != null) {
			query.setMaxResults(iDisplayLength);
		}
		
		List<Catalog> lisCatlog = query.getResultList();
		for(Catalog catalog : lisCatlog) {
			try {
				ApprovalProcess approvalProcess = approvalProcessSession.findByCatlog(catalog.getId());
				if(approvalProcess!=null) {
					catalog.setApprovalProcess(approvalProcess);					
				}else {
					catalog.setApprovalProcess(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return lisCatlog;
	}

	public Long countCatalogListForManageCatalog(String keyword,
			Integer vendorId, Boolean isKontrak) {
		String vQuery = "select count(catalog) from Catalog catalog";
		
		if(isKontrak != null && isKontrak) {
			vQuery += " left join CatalogKontrak ck on catalog = ck.catalog where ck.isDelete = 0 and catalog.isDelete = 0 and catalog.namaIND != null";
		} else {
			vQuery += " where catalog.isDelete = 0 and catalog.namaIND != null";
		}
		
		if (vendorId != null) {
			vQuery += " and catalog.isVendor = 1 and catalog.vendor.id = :vendorId ";
		}
		if (keyword != null) {
			vQuery += " and (catalog.namaIND like :keyword or catalog.vendor.nama like :keyword or catalog.attributeGroup.name like :keyword or catalog.harga like :keyword or catalog.satuan.nama like :keyword) ";
		}
		Query query = getEntityManager().createQuery(vQuery);
		if (keyword != null) {
			query.setParameter("keyword", "%" + keyword + "%");
		}
		if (vendorId != null) {
			query.setParameter("vendorId", vendorId);
		}
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogListWithKontrak(Catalog entity) {
		String vQuery = "select c from CatalogKontrak ck inner join ck.catalog c where catalog.isDelete = 0 AND catalog.isVendor = 0 AND catalog.kodeItem <> null";
		Query query = getEntityManager().createQuery(vQuery);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogListWithPagination(Integer start,
			Integer length, String keyword, Integer columnOrder,
			String tipeOrder) {

		String queryUser = "Select catalog from CatalogKontrak ck inner join ck.catalog catalog where catalog.status = 1 AND catalog.isDelete = 0 AND catalog.isVendor = 0 AND catalog.kodeItem <> null AND "
				+ "(catalog.kodeItem like :keyword OR catalog.namaIND like :keyword OR catalog.deskripsiIND like :keyword "
				+ "OR catalog.harga like :keyword OR catalog.vendor.Alamat like :keyword OR catalog.vendor.nomorTelpon like :keyword OR catalog.vendor.email like :keyword) ";

		String[] columnToView = { "kodeItem", "namaIND", "deskripsiIND",
				"harga", "vendor.alamat" };
		if (columnOrder > 0) {
			queryUser += "ORDER BY catalog. " + columnToView[columnOrder] + " "
					+ tipeOrder;
		} else {
			queryUser += "ORDER BY catalog.id desc ";
		}

		Query query = em.createQuery(queryUser);
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<Catalog> catalogList = query.getResultList();
		return catalogList;
	}

	public long getCatalogListCount(String keyword) {

		String queryCountUser = "Select COUNT(catalog) from CatalogKontrak ck inner join ck.catalog catalog where catalog.isDelete = 0 AND catalog.isVendor = 0 AND catalog.kodeItem <> null AND "
				+ "(catalog.kodeItem like :keyword OR catalog.namaIND like :keyword OR catalog.deskripsiIND like :keyword "
				+ "OR catalog.harga like :keyword OR catalog.vendor.Alamat like :keyword OR catalog.vendor.nomorTelpon like :keyword OR catalog.vendor.email like :keyword) ";

		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", keyword);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogListByCategory(Catalog catalogParam) {

		String where = "";
		if (catalogParam != null) {
			if (catalogParam.getCategoryId() != null)
				where = "WHERE ccatalog.category.id=:categoryId";
		}
		String qCatalogByCategori = "SELECT cc FROM CatalogCategory cc "
				+ where;
		Query qCatalog = getEntityManager().createQuery(qCatalogByCategori);

		if (catalogParam != null) {
			if (catalogParam.getCategoryId() != null)
				qCatalog.setParameter("categoryId",
						catalogParam.getCategoryId());
		}

		List<CatalogCategory> ccList = qCatalog.getResultList();
		List<Integer> catalogIdList = new ArrayList<Integer>();
		for (CatalogCategory cc : ccList) {
			catalogIdList.add(cc.getCatalog().getId());
		}

		String vQuery = "select c from CatalogKontrak ck inner join ck.catalog c WHERE catalog.isDelete = 0 AND catalog.isVendor = 0  AND catalog.kodeItem <> null AND catalog.id IN :catalogIdList ";
		Query query = getEntityManager().createQuery(vQuery);
		query.setParameter("catalogIdList", catalogIdList);
		List<Catalog> catalogList = query.getResultList();
		for (int i = 0; i < catalogList.size(); i++) {
			Catalog newCatalog = catalogList.get(i);
			// get category
			List<Category> categoryList = categorySession
					.getCategoryListByCatalog(newCatalog.getId());
			newCatalog.setCategoryList(categoryList);

			catalogList.set(i, newCatalog);
		}
		return catalogList;
	}

	public Catalog insertCatalog(Catalog entity, Token token) {
		entity.setIsDelete(0);
		entity.setCreated(new Date());
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public Catalog updateCatalog(Catalog entity, Token token) {
		entity.setUpdated(new Date());
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public Catalog deleteCatalog(int id, Token token) {
		Catalog entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public Catalog deleteRowCatalog(int id, Token token) {
		Catalog entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	
	public Catalog getCatalog(int id){
		return super.find(id);
	}
	
	public Integer getTotalPagesAxiqoeFull() throws ClientProtocolException, IOException, JSONException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		
		String url = "http://103.28.163.204:9910/svc/epro/updated_produk?secretkey=998a23d846f79b69637a39f5d67a3abac335fc2ddb7d9&start_date=2018-01-01&end_date=" + date +"&per_page=250";
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		response = client.execute(request);
		
		Integer totalPages;
		
		try {
			response = client.execute(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ResponseHandler<String> handler = new BasicResponseHandler();
			String body = handler.handleResponse(response);
			JSONObject obj = new JSONObject(body);
			
			totalPages = obj.getInt("total_page");
			//System.out.println(totalPages);
		}
		
		return totalPages;

	}
	
	public Integer getTotalPagesAxiqoeToday() throws ClientProtocolException, IOException, JSONException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		
		String url = "https://eprocai.axiqoe.com/svc/epro/updated_produk?secretkey=998a23d846f79b69637a39f5d67a3abac335fc2ddb7d9&start_date=" + date + "&end_date=" + date +"&per_page=250";
		log.info(url);
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		Integer totalPages = null;
		
		try {
			response = client.execute(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("URL TIDAK BISA DI AKSES");
			log.error("ERRORLCLMSG: "+ e.getLocalizedMessage());
			log.error("ERRORMSG: "+ e.getMessage());
			Constant.AXIQOE_SYNC_INPROGRESS = false;
		} finally {
			try{
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
				JSONObject obj = new JSONObject(body);
				
				totalPages = obj.getInt("total_page");
			}catch(IOException e){
				log.error("ERRORLCLMSG: "+ e.getLocalizedMessage());
				log.error("ERRORMSG: "+ e.getMessage());
			}
			
			//System.out.println(totalPages);
		}
		
		return totalPages;

	}
	
	public void insertCatalogFromAxiqoeToPromiseFull(Integer startPage, Integer endPage, String startDate, String endDate) throws ClientProtocolException, IOException, JSONException {
		
		// Todo: panggil http://103.28.163.204:9910/svc/epro/updated_produk?secretkey=998a23d846f79b69637a39f5d67a3abac335fc2ddb7d9&start_date=2018-01-01&end_date=2018-01-09
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		Integer berhasilInsert = 0;
		Integer berhasilUpdate = 0;
		Integer totalData= 0;
		List<String>insertedSKUList = new ArrayList<String>();
		List<String>UpdatedSKUList = new ArrayList<String>();
		
		for (int j = startPage; j <= endPage; j++) {
			String url = "https://eprocai.axiqoe.com/svc/epro/updated_produk?secretkey=998a23d846f79b69637a39f5d67a3abac335fc2ddb7d9&start_date="+ startDate +"&end_date=" + endDate + "&page=" + j +"&per_page=250";
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);

			// add request header
			/*request.addHeader("User-Agent", USER_AGENT);*/
			HttpResponse response = null;
			response = client.execute(request);
			
			try {
				response = client.execute(request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
				//System.out.println("body          : "  + body);
				log.info(">> Response Code : "  + response.getStatusLine().getStatusCode());
				
				JSONObject obj = new JSONObject(body);
				JSONArray arr = obj.getJSONArray("produk");
				
				for (int i = 0; i < arr.length(); i++)
				{
					JSONObject produk = arr.getJSONObject(i);
					
					JSONObject informasi = produk.getJSONObject("informasi");
					String nama_produk = informasi.getString("nama_produk");
					String deskripsi_produk= informasi.getString("deskripsi_produk");
					String kode_produk = informasi.getString("kode_produk");
					Integer satuan = informasi.getInt("satuan");
					Integer produk_aktif = informasi.getInt("produk_aktif");
					String image = informasi.getString("image");
					Integer category = informasi.getInt("category");
					
					Double stock = informasi.getDouble("stock");
					
					Double berat = informasi.getDouble("berat");
					Double satuan_berat = 0.0;
					Double panjang = informasi.getDouble("panjang");
					Double satuan_panjang = 0.0;
					Double lebar = informasi.getDouble("lebar");
					Double satuan_lebar = 0.0;
					Double tinggi = informasi.getDouble("tinggi");
					Double satuan_tinggi = 0.0;
					
					JSONObject harga = produk.getJSONObject("harga");
					Double harga_axiqoe = harga.getDouble("harga");
					//Double harga_eproc = harga.getDouble("harga_eproc");
					Integer mata_uang = harga.getInt("mata_uang");
					
					ArrayList<String> labelList = new ArrayList<String>();
					ArrayList<String> deskripsiList = new ArrayList<String>();
					JSONObject spesifikasi = produk.getJSONObject("spesifikasi");
					JSONArray arrItem = spesifikasi.getJSONArray("item");

					if(arrItem.length() > 0) {
						for(int k = 0; k < arrItem.length(); k++) {
							String label = arrItem.getJSONObject(k).getString("label");
							String deskripsi = arrItem.getJSONObject(k).getString("deskripsi");
							labelList.add(label);
							deskripsiList.add(deskripsi);
						}
						
					}

					//Fungsi untuk insert dan update
					Catalog catalog = getCatalogByKodeProduk(kode_produk);
					if (catalog == null) {
						log.info(">> Insert catalog");
						log.info(">> hal " + j + ", no " + i + ", kode produk : " + kode_produk + ", nama_produk : " + nama_produk);
						
						insertCatalogFromAxiqoe(nama_produk, deskripsi_produk,
								 kode_produk, satuan, harga_axiqoe, mata_uang, produk_aktif,  
								 berat, satuan_berat, panjang, satuan_panjang, lebar, satuan_lebar, tinggi, satuan_tinggi, 
								 category, stock, image, labelList, deskripsiList);
						berhasilInsert++;
						insertedSKUList.add(kode_produk);
						
					} else {
						log.info(">> Update catalog");
						log.info(">> hal " + j + ", no " + i + ", kode produk : " + kode_produk + ", nama_produk : " + nama_produk);
						
						updateCatalogFromAxiqoe(catalog.getId(),
								nama_produk, deskripsi_produk,
								kode_produk, satuan, harga_axiqoe, mata_uang, produk_aktif,  
								berat, satuan_berat, panjang, satuan_panjang, lebar, satuan_lebar, tinggi, satuan_tinggi, 
								category, stock, image, labelList, deskripsiList);
						berhasilUpdate++;
						UpdatedSKUList.add(kode_produk);
					}
	
				}
				
				log.info(">> berhasil");
				
			}
		
		}

		emailNotificationSession.getMailGeneratorSyncCatalog(insertedSKUList,UpdatedSKUList,totalData,berhasilInsert,berhasilUpdate);

	}
	
	
	public void insertCatalogFromAxiqoeToPromiseToday(Integer startPage, Integer endPage) throws ClientProtocolException, IOException, JSONException {
		
		// Todo: panggil http://103.28.163.204:9910/svc/epro/updated_produk?secretkey=998a23d846f79b69637a39f5d67a3abac335fc2ddb7d9&start_date=2018-01-01&end_date=2018-01-09
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		Integer berhasilInsert = 0;
		Integer berhasilUpdate = 0;
		Integer totalData= 0;
		List<String>insertedSKUList = new ArrayList<String>();
		List<String>UpdatedSKUList = new ArrayList<String>();
		for (int j = startPage; j <= endPage; j++) {
			String url = "https://eprocai.axiqoe.com/svc/epro/updated_produk?secretkey=998a23d846f79b69637a39f5d67a3abac335fc2ddb7d9&start_date="+ date +"&end_date=" + date + "&page=" + j +"&per_page=250";
			if(j == 1) {
				log.info(">> URL :"+url);
			}
			
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);

			// add request header
			/*request.addHeader("User-Agent", USER_AGENT);*/
			HttpResponse response = null;
			
			try {
				response = client.execute(request);
			} catch (IOException e) {
				log.error("URL TIDAK BISA DI AKSES");
				Constant.AXIQOE_SYNC_INPROGRESS = false;
			}
			finally {
				ResponseHandler<String> handler = new BasicResponseHandler();
				String body = handler.handleResponse(response);
				
				JSONObject obj = new JSONObject(body);
				JSONArray arr = obj.getJSONArray("produk");
				totalData = obj.getInt("total");
				
				for (int i = 0; i < arr.length(); i++)
				{
					JSONObject produk = arr.getJSONObject(i);
					
					JSONObject informasi = produk.getJSONObject("informasi");
					String nama_produk = informasi.getString("nama_produk");
					String deskripsi_produk= informasi.getString("deskripsi_produk");
					String kode_produk = informasi.getString("kode_produk");
					Integer satuan = informasi.getInt("satuan");
					Integer produk_aktif = informasi.getInt("produk_aktif");
					String image = informasi.getString("image");
					Integer category = informasi.getInt("category");
					
					Double stock = informasi.getDouble("stock");
					
					Double berat = informasi.getDouble("berat");
					Double satuan_berat = 0.0;
					Double panjang = informasi.getDouble("panjang");
					Double satuan_panjang = 0.0;
					Double lebar = informasi.getDouble("lebar");
					Double satuan_lebar = 0.0;
					Double tinggi = informasi.getDouble("tinggi");
					Double satuan_tinggi = 0.0;
					
					JSONObject harga = produk.getJSONObject("harga");
					Double harga_axiqoe = harga.getDouble("harga");
					//Double harga_eproc = harga.getDouble("harga_eproc");
					Integer mata_uang = harga.getInt("mata_uang");
					
					ArrayList<String> labelList = new ArrayList<String>();
					ArrayList<String> deskripsiList = new ArrayList<String>();
					JSONObject spesifikasi = produk.getJSONObject("spesifikasi");
					JSONArray arrItem = spesifikasi.getJSONArray("item");

					if(arrItem.length() > 0) {
						for(int k = 0; k < arrItem.length(); k++) {
							String label = arrItem.getJSONObject(k).getString("label");
							String deskripsi = arrItem.getJSONObject(k).getString("deskripsi");
							labelList.add(label);
							deskripsiList.add(deskripsi);
						}
						
					}

					//Fungsi untuk insert dan update
					Catalog catalog = getCatalogByKodeProduk(kode_produk);
					if (catalog == null) {
						log.info(">> Insert catalog");
						log.info(">> hal " + j + ", no " + i + ", kode produk : " + kode_produk + ", nama_produk : " + nama_produk);
						
						insertCatalogFromAxiqoe(nama_produk, deskripsi_produk,
								 kode_produk, satuan, harga_axiqoe, mata_uang, produk_aktif,  
								 berat, satuan_berat, panjang, satuan_panjang, lebar, satuan_lebar, tinggi, satuan_tinggi, 
								 category, stock, image, labelList, deskripsiList);
						berhasilInsert++;
						insertedSKUList.add(kode_produk);
					} else {
						log.info(">> Update catalog");
						log.info(">> hal " + j + ", no " + i + ", kode produk : " + kode_produk + ", nama_produk : " + nama_produk);
						
						updateCatalogFromAxiqoe(catalog.getId(),
								nama_produk, deskripsi_produk,
								kode_produk, satuan, harga_axiqoe, mata_uang, produk_aktif,  
								berat, satuan_berat, panjang, satuan_panjang, lebar, satuan_lebar, tinggi, satuan_tinggi, 
								category, stock, image, labelList, deskripsiList);
						berhasilUpdate++;
						UpdatedSKUList.add(kode_produk);
					}
				}	
			}
		
		}
		Constant.AXIQOE_SYNC_INPROGRESS = false;
		emailNotificationSession.getMailGeneratorSyncCatalog(insertedSKUList,UpdatedSKUList,totalData,berhasilInsert,berhasilUpdate);

	}
	
	public void insertCatalogFromAxiqoe(String nama_produk, 
			String deskripsi_produk, String kode_produk, 
			Integer satuan, Double harga_axiqoe, 
			Integer mata_uang, Integer produk_aktif,
			Double berat, Double satuan_berat, Double panjang, Double satuan_panjang, 
			Double lebar, Double satuan_lebar, Double tinggi, Double satuan_tinggi,
			Integer category, Double stock,
			String image, ArrayList<String> labelList, ArrayList<String> deskripsiList) {
		
		ArrayList<String> attributeNameList = new ArrayList<String>();
		ArrayList<String> catalogAttributeValueList = new ArrayList<String>();

		Integer attributeGroupId = 0;
		
		if (category == 54 || category == 55) {
			attributeGroupId = 38;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Antarmuka/Interface") || labelList.get(i).equals("Audio")
						|| labelList.get(i).equals("Card Reader") || labelList.get(i).equals("Dimension") || labelList.get(i).equals("Monitor") 
						|| labelList.get(i).equals("Optical Drive") || labelList.get(i).equals("Processor") || labelList.get(i).equals("I/O Ports") 
						|| labelList.get(i).equals("Hard Drive") || labelList.get(i).equals("Memory") || labelList.get(i).equals("Warranty") 
						|| labelList.get(i).equals("OS") || labelList.get(i).equals("Spesifikasi") || labelList.get(i).equals("Kelengkapan Paket") 
						|| labelList.get(i).equals("Platform")) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		} else if (category == 87) {
			attributeGroupId = 39;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Audio") || labelList.get(i).equals("Dimension")
						|| labelList.get(i).equals("I/O Ports")) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		} else if (category == 52) {
			attributeGroupId = 40;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Antarmuka/Interface") || labelList.get(i).equals("Audio")
						|| labelList.get(i).equals("Dimension") || labelList.get(i).equals("Warranty") || labelList.get(i).equals("Spesifikasi") 
						|| labelList.get(i).equals("Kelengkapan Paket") ) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		} else {
			attributeGroupId = 41;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Spesifikasi")) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		}

		AttributeGroup attributeGroup = attributeGroupSession.findAttributeGroup(attributeGroupId);
		List<AttributePanelGroup> attributePanelGroup = attributeGroup.getAttributePanelGroupList();
		
		Vendor vendor = vendorSession.getVendorById(1);
		Satuan satuan_promise = satuanSession.find(satuan);
		if (satuan_promise == null) {
			satuan_promise = satuanSession.find(1);
		}
		MataUang mataUang = mataUangSession.getMataUang(mata_uang);
		
		Catalog catalog = new Catalog();
		catalog.setVendor(vendor);
		catalog.setNamaIND(nama_produk.trim());
		catalog.setDeskripsiIND(deskripsi_produk);
		catalog.setKodeProduk(kode_produk);
		catalog.setSatuan(satuan_promise);
		catalog.setHarga(harga_axiqoe);
		catalog.setHarga_eproc(0.0);
		catalog.setMataUang(mataUang);
		catalog.setBerat(berat);
		catalog.setSatuanBerat(satuan_berat.toString());
		catalog.setPanjang(panjang);
		catalog.setSatuanPanjang(satuan_panjang.toString());
		catalog.setLebar(lebar);
		catalog.setSatuanLebar(satuan_lebar.toString());
		catalog.setTinggi(tinggi);
		catalog.setSatuanTinggi(satuan_tinggi.toString());
		catalog.setIsVendor(true);
		if(produk_aktif == 0) {
			catalog.setStatus(true);
		} else {
			catalog.setStatus(false);
		}
		catalog.setAttributeGroup(attributeGroup);
		insertCatalog(catalog, null);
		
		Category category_promise = categorySession.getCategory(category);
		if (category_promise == null) {
			category_promise = categorySession.getCategory(4);
		}
		
		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(category_promise);
		for (int i = 0; i < categoryList.size(); i++) {
			if(categoryList.get(i).getParentCategory() != null) {
				Category parentCategories = new Category();
				parentCategories.setId(categoryList.get(i).getParentCategory().getId());
				parentCategories.setAdminLabel(categoryList.get(i).getParentCategory().getAdminLabel());
				parentCategories.setDescription(categoryList.get(i).getParentCategory().getDescription());
				parentCategories.setFlagEnabled(categoryList.get(i).getParentCategory().getFlagEnabled());
				parentCategories.setIsDelete(categoryList.get(i).getParentCategory().getIsDelete());
				parentCategories.setTranslateEng(categoryList.get(i).getParentCategory().getTranslateEng());
				parentCategories.setTranslateInd(categoryList.get(i).getParentCategory().getTranslateInd());
				parentCategories.setValue(categoryList.get(i).getParentCategory().getValue());
				parentCategories.setParentCategory(categoryList.get(i).getParentCategory().getParentCategory());
				categoryList.add(parentCategories);
			}
		}
		
		Collections.reverse(categoryList);
		
		for (Category category2 : categoryList) {
			CatalogCategory catalogCategory = new CatalogCategory();
			catalogCategory.setCatalog(catalog);
			catalogCategory.setCategory(category2);
			catalogCategorySession.insertCatalogCategory(catalogCategory, null);
		}
		
		
		Double stock_promise = stock;
		
		CatalogLocation catalogLocation = new CatalogLocation();
		catalogLocation.setCatalog(catalog);
		catalogLocation.setStockProduct(stock_promise);
		catalogLocationSession.insertCatalogLocation(catalogLocation, null);				
		
		CatalogImage catalogImage = new CatalogImage();
		catalogImage.setCatalog(catalog);
		catalogImage.setImagesFileName(image);
		catalogImage.setStatus(true);
		catalogImage.setUrutanPesanan(1);
		catalogImageSession.insertCatalogImage(catalogImage, null);
		
		if (attributeNameList.size() > 0) {
			
			for(int j=0; j < attributePanelGroup.size(); j++) { // 10
				
				Boolean isNew = true;
				
				for (int a=0; a < attributeNameList.size(); a++) { //5
					if(attributePanelGroup.get(j).getAttribute().getName().equals(attributeNameList.get(a))) {
						CatalogAttribute catalogAttribute = new CatalogAttribute();
						catalogAttribute.setCatalog(catalog);
						catalogAttribute.setNilai(catalogAttributeValueList.get(a));
						catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
						catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
						isNew = false;						
					}
				}
				
				if(isNew){
					CatalogAttribute catalogAttribute = new CatalogAttribute();
					catalogAttribute.setCatalog(catalog);
					catalogAttribute.setNilai(null);
					catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
					catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
				}
			}
			
		} else {
			
			for (int k = 0; k < attributePanelGroup.size(); k++) {
				CatalogAttribute catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalog);
				catalogAttribute.setNilai(null);
				catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(k));
				catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
			}
			
		}		

	}
	
	public void updateCatalogFromAxiqoe(Integer catalogId, String nama_produk, 
			String deskripsi_produk, String kode_produk, 
			Integer satuan, Double harga_axiqoe,
			Integer mata_uang, Integer produk_aktif,
			Double berat, Double satuan_berat, Double panjang, Double satuan_panjang, 
			Double lebar, Double satuan_lebar, Double tinggi, Double satuan_tinggi,
			Integer category, Double stock,
			String image, ArrayList<String> labelList, ArrayList<String> deskripsiList) {
		
		ArrayList<String> attributeNameList = new ArrayList<String>();
		ArrayList<String> catalogAttributeValueList = new ArrayList<String>();

		Integer attributeGroupId = 0;
		
		if (category == 54 || category == 55) {
			attributeGroupId = 38;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Antarmuka/Interface") || labelList.get(i).equals("Audio")
						|| labelList.get(i).equals("Card Reader") || labelList.get(i).equals("Dimension") || labelList.get(i).equals("Monitor") 
						|| labelList.get(i).equals("Optical Drive") || labelList.get(i).equals("Processor") || labelList.get(i).equals("I/O Ports") 
						|| labelList.get(i).equals("Hard Drive") || labelList.get(i).equals("Memory") || labelList.get(i).equals("Warranty") 
						|| labelList.get(i).equals("OS") || labelList.get(i).equals("Spesifikasi") || labelList.get(i).equals("Kelengkapan Paket") 
						|| labelList.get(i).equals("Platform")) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		} else if (category == 87) {
			attributeGroupId = 39;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Audio") || labelList.get(i).equals("Dimension")
						|| labelList.get(i).equals("I/O Ports")) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		} else if (category == 52) {
			attributeGroupId = 40;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Antarmuka/Interface") || labelList.get(i).equals("Audio")
						|| labelList.get(i).equals("Dimension") || labelList.get(i).equals("Warranty") || labelList.get(i).equals("Spesifikasi") 
						|| labelList.get(i).equals("Kelengkapan Paket") ) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		} else {
			attributeGroupId = 41;
			for (int i = 0; i < labelList.size(); i++) {
				if(labelList.get(i).equals("Brand") || labelList.get(i).equals("Spesifikasi")) {
					attributeNameList.add(labelList.get(i));
					catalogAttributeValueList.add(deskripsiList.get(i));
				}
			}
			
		}
		
		AttributeGroup attributeGroup = attributeGroupSession.findAttributeGroup(attributeGroupId);
		List<AttributePanelGroup> attributePanelGroup = attributeGroup.getAttributePanelGroupList();
		
		Vendor vendor = vendorSession.getVendorById(1);
		Satuan satuan_promise = satuanSession.find(satuan);
		if (satuan_promise == null) {
			satuan_promise = satuanSession.find(1);
		}
		MataUang mataUang = mataUangSession.getMataUang(mata_uang);
		
		Catalog catalog = find(catalogId);
		catalog.setVendor(vendor);
		catalog.setNamaIND(nama_produk.trim());
		catalog.setDeskripsiIND(deskripsi_produk);
		catalog.setKodeProduk(kode_produk);
		catalog.setSatuan(satuan_promise);
		catalog.setHarga(harga_axiqoe);
		catalog.setMataUang(mataUang);
		catalog.setBerat(berat);
		catalog.setSatuanBerat(satuan_berat.toString());
		catalog.setPanjang(panjang);
		catalog.setSatuanPanjang(satuan_panjang.toString());
		catalog.setLebar(lebar);
		catalog.setSatuanLebar(satuan_lebar.toString());
		catalog.setTinggi(tinggi);
		catalog.setSatuanTinggi(satuan_tinggi.toString());
		catalog.setIsVendor(true);
		if(produk_aktif == 0) {
			catalog.setStatus(true);
		} else {
			catalog.setStatus(false);
		}
		catalog.setAttributeGroup(attributeGroup);
		updateCatalog(catalog, null);
		
		List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(catalog);
		for (CatalogCategory catalogCategory2 : catalogCategoryList) {
			catalogCategorySession.deleteRowCatalogCategory(catalogCategory2.getId(), null);
		}
		
		Category category_promise = categorySession.getCategory(category);
		if (category_promise == null) {
			category_promise = categorySession.getCategory(4);
		}
			
		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(category_promise);
		for (int i = 0; i < categoryList.size(); i++) {
			if(categoryList.get(i).getParentCategory() != null) {
				Category parentCategories = new Category();
				parentCategories.setId(categoryList.get(i).getParentCategory().getId());
				parentCategories.setAdminLabel(categoryList.get(i).getParentCategory().getAdminLabel());
				parentCategories.setDescription(categoryList.get(i).getParentCategory().getDescription());
				parentCategories.setFlagEnabled(categoryList.get(i).getParentCategory().getFlagEnabled());
				parentCategories.setIsDelete(categoryList.get(i).getParentCategory().getIsDelete());
				parentCategories.setTranslateEng(categoryList.get(i).getParentCategory().getTranslateEng());
				parentCategories.setTranslateInd(categoryList.get(i).getParentCategory().getTranslateInd());
				parentCategories.setValue(categoryList.get(i).getParentCategory().getValue());
				parentCategories.setParentCategory(categoryList.get(i).getParentCategory().getParentCategory());
				categoryList.add(parentCategories);
			}
		}
		
		Collections.reverse(categoryList);

		for (Category category2 : categoryList) {
			CatalogCategory catalogCategory = new CatalogCategory();
			catalogCategory.setCatalog(catalog);
			catalogCategory.setCategory(category2);
			catalogCategorySession.insertCatalogCategory(catalogCategory, null);
		}

		Double stock_promise = stock;
		
		CatalogLocation catalogLocation = catalogLocationSession.getCatalogLocationByCatalogId(catalog);
		if(catalogLocation == null) {
			CatalogLocation catalogLocationNew = new CatalogLocation();
			catalogLocationNew.setCatalog(catalog);
			catalogLocationNew.setStockProduct(stock_promise);
			catalogLocationSession.insertCatalogLocation(catalogLocationNew, null);
		} else {
			catalogLocation.setCatalog(catalog);
			catalogLocation.setStockProduct(stock_promise);
			catalogLocationSession.updateCatalogLocation(catalogLocation, null);
		}

		CatalogImage catalogImage = catalogImageSession.getCatalogImageByCatalogId(catalog);
		if(catalogImage != null) {
			catalogImage.setCatalog(catalog);
			catalogImage.setImagesFileName(image);
			catalogImage.setStatus(true);
			catalogImage.setUrutanPesanan(1);
			catalogImageSession.updateCatalogImage(catalogImage, null);
		} else {
			CatalogImage catalogImageNew = new CatalogImage();
			catalogImageNew.setCatalog(catalog);
			catalogImageNew.setImagesFileName(image);
			catalogImageNew.setStatus(true);
			catalogImageNew.setUrutanPesanan(1);
			catalogImageSession.insertCatalogImage(catalogImageNew, null);
		}
		
		List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeListByCatalogId(catalog);
		if(catalogAttributeList.size() > 0) {
			for (CatalogAttribute catalogAttribute : catalogAttributeList) {
				catalogAttributeSession.deleteRowCatalogAttribute(catalogAttribute.getId(), null);
			}
		}
		
		if (attributeNameList.size() > 0) {
			
			for(int j=0; j < attributePanelGroup.size(); j++) { // 10
				
				Boolean isNew = true;
				
				for (int a=0; a < attributeNameList.size(); a++) { //5
					if(attributePanelGroup.get(j).getAttribute().getName().equals(attributeNameList.get(a))) {
						CatalogAttribute catalogAttribute = new CatalogAttribute();
						catalogAttribute.setCatalog(catalog);
						catalogAttribute.setNilai(catalogAttributeValueList.get(a));
						catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
						catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
						isNew = false;						
					}
				}
				
				if(isNew){
					CatalogAttribute catalogAttribute = new CatalogAttribute();
					catalogAttribute.setCatalog(catalog);
					catalogAttribute.setNilai(null);
					catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
					catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
				}
			}
			
		} else {
			
			for (int k = 0; k < attributePanelGroup.size(); k++) {
				CatalogAttribute catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalog);
				catalogAttribute.setNilai(null);
				catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(k));
				catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
			}
			
		}	
			
	}
	
	public void insertCatalogFromSevaToPromise() throws ClientProtocolException, IOException, JSONException {
		
		// Todo: panggil http://103.28.163.204:9910/svc/epro/updated_produk?secretkey=998a23d846f79b69637a39f5d67a3abac335fc2ddb7d9&start_date=2018-01-01&end_date=2018-01-09
		
		String url = "http://lms.seva.id/api/v1/product?is_marketing_unit=1";
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.setHeader("x-api-key", "59b9e731-e55d89-4cd75-bde8-ceb0353589c0");
		// add request header
		/*request.addHeader("User-Agent", USER_AGENT);*/
		HttpResponse response = null;
		response = client.execute(request);
		
		try {
			response = client.execute(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ResponseHandler<String> handler = new BasicResponseHandler();
			String body = handler.handleResponse(response);
			//System.out.println("body          : "  + body);
			log.info(">> Response Code : "  + response.getStatusLine().getStatusCode());
			
			JSONObject obj = new JSONObject(body);
			JSONArray arr = obj.getJSONArray("data");
			
			for (int i = 0; i < arr.length(); i++) {
				
				JSONObject jObj = arr.getJSONObject(i);
				
				Integer id = arr.getJSONObject(i).getInt("id");
				String name = arr.getJSONObject(i).getString("name");
				String marketing_name = arr.getJSONObject(i).getString("marketing_name");
				String code = arr.getJSONObject(i).getString("code");
				
				JSONObject category = arr.getJSONObject(i).getJSONObject("category");
				Integer category_id = category.getInt("id");
				String category_name = category.getString("name");
				
				JSONArray price = jObj.getJSONArray("price");
				Double amount = 0.00;
				if (price.length() > 0) {
					amount = price.getJSONObject(0).getDouble("amount");
				}
				
				JSONArray colors = jObj.getJSONArray("colors");
				ArrayList<String> imageListNew = new ArrayList<String>();
				if(colors.length() > 0) {
					JSONObject jObjColors = colors.getJSONObject(0);
					JSONArray images = jObjColors.getJSONArray("images");
					if (images.length() > 0) {
						JSONObject imgList = images.getJSONObject(0);
						imageListNew.add(imgList.getString("main_img"));
						//main_img = imgList.getString("main_img");
					}
				}
				
				ArrayList<String> attributePanelSevaList = new ArrayList<String>();
				ArrayList<String> attributeSevaList = new ArrayList<String>();
				ArrayList<String> valueAttributeSevaList = new ArrayList<String>();
				JSONArray specifications = jObj.getJSONArray("specifications");
				if (specifications.length() > 0) {
					for(int j = 0; j < specifications.length(); j++) {						
						if(!specifications.getJSONObject(j).isNull("category")) {
							String category2 = specifications.getJSONObject(j).getString("category");
							attributePanelSevaList.add(category2);
							
							String attribute = specifications.getJSONObject(j).getString("name");
							attributeSevaList.add(attribute);
							
							String value = specifications.getJSONObject(j).getString("value");
							valueAttributeSevaList.add(value);
						}
	
					}
				}
				
				JSONArray overview_images = jObj.getJSONArray("overview_images");
				if (overview_images.length() > 0) {
					JSONObject jObjOverviewImages = overview_images.getJSONObject(0);
					imageListNew.add(jObjOverviewImages.getString("image"));
				}
				
				Catalog catalog = getCatalogByKodeProduk(code.trim());
				if (catalog == null) {
					log.info(">> Insert Catalog, id : " + id);
					log.info(">> kode produk : " + code + ", nama_produk : " + name);
					insertCatalogFromSeva(name, marketing_name, 
							code, amount, 
							category_id, category_name,
							imageListNew, attributePanelSevaList,
							attributeSevaList, valueAttributeSevaList);
					
				} else {
					log.info(">> Update Catalog, id : " + id);
					log.info(">> kode produk : " + code + ", nama_produk : " + name);
					updateCatalogFromSeva(catalog.getId(),
							name, marketing_name, 
							code, amount, 
							category_id,category_name,
							imageListNew, attributePanelSevaList,
							attributeSevaList, valueAttributeSevaList);
				}

			}
			
			log.info(">> Berhasil");
			
		}

	}
	
	public void insertCatalogFromSeva(String name, String marketing_name, 
			String code,  Double amount, 
			Integer category_id, String category_name,
			ArrayList<String> imageList,
			ArrayList<String> attributePanelSevaList, 
			ArrayList<String> attributeSevaList,
			ArrayList<String> valueAttributeSevaList) {

		Vendor vendor = vendorSession.getVendorById(2);
		Satuan satuan = satuanSession.find(1);
		MataUang mataUang = mataUangSession.getMataUang(1);
		
		ArrayList<String> attributePanelNewList = new ArrayList<String>();
		ArrayList<String> attributeNameNewList = new ArrayList<String>();
		ArrayList<String> attributeValueNewList = new ArrayList<String>();
		
		Category categoryNew = categorySession.getCategoryByNameInd(category_name);
		AttributeGroup attributeGroup = new AttributeGroup();
		
		
		if(categoryNew == null) {
			categoryNew = categorySession.getCategory(15);
		}

		if(categoryNew != null) {
			
			List<CategoryAttribute> categoryAttributeList = categoryAttributeSession.getCategoryAttributeList(categoryNew);
			attributeGroup = attributeGroupSession.findAttributeGroup(categoryAttributeList.get(0).getAttributeGroup().getId());
			
			for (int i= 0; i < categoryAttributeList.size(); i++) {
				for (int j = 0; j <attributeSevaList.size(); j++) {
					if(categoryAttributeList.get(i).getAttribute().getTranslateInd().equals(attributeSevaList.get(j))) {
						attributePanelNewList.add(attributePanelSevaList.get(j));
						attributeNameNewList.add(attributeSevaList.get(j));
						attributeValueNewList.add(valueAttributeSevaList.get(j));
					}
				}
			}
			
		}

		Catalog catalog = new Catalog();
		catalog.setVendor(vendor);
		catalog.setNamaIND(name.trim());
		catalog.setDeskripsiIND(marketing_name.trim());
		catalog.setKodeProduk(code.trim());
		catalog.setSatuan(satuan);
		catalog.setHarga(amount);
		catalog.setMataUang(mataUang);
		catalog.setIsVendor(true);
		
		if(categoryNew.getId() == 36 || categoryNew.getId() == 37 || categoryNew.getId() == 38 || 
				categoryNew.getId() == 39 || categoryNew.getId() == 94) {
			catalog.setStatus(false);
		} else {
			catalog.setStatus(true);
		}
		
		catalog.setAttributeGroup(attributeGroup);
		insertCatalog(catalog, null);
		
		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(categoryNew);
		for (int i = 0; i < categoryList.size(); i++) {
			if(categoryList.get(i).getParentCategory() != null) {
				Category parentCategories = new Category();
				parentCategories.setId(categoryList.get(i).getParentCategory().getId());
				parentCategories.setAdminLabel(categoryList.get(i).getParentCategory().getAdminLabel());
				parentCategories.setDescription(categoryList.get(i).getParentCategory().getDescription());
				parentCategories.setFlagEnabled(categoryList.get(i).getParentCategory().getFlagEnabled());
				parentCategories.setIsDelete(categoryList.get(i).getParentCategory().getIsDelete());
				parentCategories.setTranslateEng(categoryList.get(i).getParentCategory().getTranslateEng());
				parentCategories.setTranslateInd(categoryList.get(i).getParentCategory().getTranslateInd());
				parentCategories.setValue(categoryList.get(i).getParentCategory().getValue());
				parentCategories.setParentCategory(categoryList.get(i).getParentCategory().getParentCategory());
				categoryList.add(parentCategories);
			}
		}
		
		Collections.reverse(categoryList);

		for (Category category2 : categoryList) {
			CatalogCategory catalogCategory = new CatalogCategory();
			catalogCategory.setCatalog(catalog);
			catalogCategory.setCategory(category2);
			catalogCategorySession.insertCatalogCategory(catalogCategory, null);
		}
		
		Double stock = new Double(0);
		
		CatalogLocation catalogLocation = new CatalogLocation();
		catalogLocation.setCatalog(catalog);
		catalogLocation.setStockProduct(stock);
		catalogLocationSession.insertCatalogLocation(catalogLocation, null);				
		
		Integer sequence = 1;
		for (int i = 0; i < imageList.size(); i++) {
			CatalogImage catalogImage = new CatalogImage();
			catalogImage.setCatalog(catalog);
			catalogImage.setStatus(true);
			catalogImage.setUrutanPesanan(sequence);
			catalogImage.setImagesFileName(imageList.get(i));
			catalogImageSession.insertCatalogImage(catalogImage, null);
			sequence++;
		}
		
		List<AttributePanelGroup> attributePanelGroup = attributeGroup.getAttributePanelGroupList();

		if (attributeNameNewList.size() > 0) {
				
			for(int j=0; j < attributePanelGroup.size(); j++) { // 10
				
				Boolean isNew = true;
				
				for (int a=0; a < attributeNameNewList.size(); a++) { //5
					if(attributePanelGroup.get(j).getAttribute().getName().equals(attributeNameNewList.get(a))) {
						CatalogAttribute catalogAttribute = new CatalogAttribute();
						catalogAttribute.setCatalog(catalog);
						String valueCatalogAttribute = attributeValueNewList.get(a).replaceAll("^\\s+", "");
						if(valueCatalogAttribute.equals("-") || valueCatalogAttribute.isEmpty()) {
							catalogAttribute.setNilai(null);
						} else {
							catalogAttribute.setNilai(valueCatalogAttribute);
						}
						catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
						catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
						isNew = false;						
					}
				}
				
				if(isNew){
					CatalogAttribute catalogAttribute = new CatalogAttribute();
					catalogAttribute.setCatalog(catalog);
					catalogAttribute.setNilai(null);
					catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
					catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
				}
			}
	
		} else {
			
			for (int k = 0; k < attributePanelGroup.size(); k++) {
				CatalogAttribute catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalog);
				catalogAttribute.setNilai(null);
				catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(k));
				catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
			}
			
			
			
		}
		
		
	}
	
	@SuppressWarnings("null")
	public void updateCatalogFromSeva(Integer catalogId, 
			String name, String marketing_name, 
			String code,  Double amount, 
			Integer category_id, String category_name,
			ArrayList<String> imageList,
			ArrayList<String> attributePanelSevaList, 
			ArrayList<String> attributeSevaList,
			ArrayList<String> valueAttributeSevaList) {
		
		Vendor vendor = vendorSession.getVendorById(2);
		Satuan satuan = satuanSession.find(1);
		MataUang mataUang = mataUangSession.getMataUang(1);
		
		ArrayList<String> attributePanelNewList = new ArrayList<String>();
		ArrayList<String> attributeNameNewList = new ArrayList<String>();
		ArrayList<String> attributeValueNewList = new ArrayList<String>();
		
		Category categoryNew = categorySession.getCategoryByNameInd(category_name);
		AttributeGroup attributeGroup = new AttributeGroup();
		
		
		if(categoryNew == null) {
			categoryNew = categorySession.getCategory(15);
		}
		
		if(categoryNew != null) {
			
			List<CategoryAttribute> categoryAttributeList = categoryAttributeSession.getCategoryAttributeList(categoryNew);
			attributeGroup = attributeGroupSession.findAttributeGroup(categoryAttributeList.get(0).getAttributeGroup().getId());
			
			for (int i= 0; i < categoryAttributeList.size(); i++) {
				for (int j = 0; j <attributeSevaList.size(); j++) {
					if(categoryAttributeList.get(i).getAttribute().getTranslateInd().equals(attributeSevaList.get(j))) {
						attributePanelNewList.add(attributePanelSevaList.get(j));
						attributeNameNewList.add(attributeSevaList.get(j));
						attributeValueNewList.add(valueAttributeSevaList.get(j));
					}
				}
			}
			
		}
		
		Catalog catalog = find(catalogId);
		catalog.setVendor(vendor);
		catalog.setNamaIND(name.trim());
		catalog.setDeskripsiIND(marketing_name.trim());
		catalog.setKodeProduk(code.trim());
		catalog.setSatuan(satuan);
		catalog.setHarga(amount);
		catalog.setMataUang(mataUang);
		catalog.setIsVendor(true);
		
		if(categoryNew.getId() == 36 || categoryNew.getId() == 37 || categoryNew.getId() == 38 || 
				categoryNew.getId() == 39 || categoryNew.getId() == 94) {
			catalog.setStatus(false);
		} else {
			catalog.setStatus(true);
		}
		
		catalog.setAttributeGroup(attributeGroup);
		updateCatalog(catalog, null);
	
		List<CatalogCategory> catalogCategoryList = catalogCategorySession.getCatalogCategoryList(catalog);
		for (CatalogCategory catalogCategory2 : catalogCategoryList) {
			catalogCategorySession.deleteRowCatalogCategory(catalogCategory2.getId(), null);
		}
		
		List<Category> categoryList = new ArrayList<Category>();
		categoryList.add(categoryNew);
		for (int i = 0; i < categoryList.size(); i++) {
			if(categoryList.get(i).getParentCategory() != null) {
				Category parentCategories = new Category();
				parentCategories.setId(categoryList.get(i).getParentCategory().getId());
				parentCategories.setAdminLabel(categoryList.get(i).getParentCategory().getAdminLabel());
				parentCategories.setDescription(categoryList.get(i).getParentCategory().getDescription());
				parentCategories.setFlagEnabled(categoryList.get(i).getParentCategory().getFlagEnabled());
				parentCategories.setIsDelete(categoryList.get(i).getParentCategory().getIsDelete());
				parentCategories.setTranslateEng(categoryList.get(i).getParentCategory().getTranslateEng());
				parentCategories.setTranslateInd(categoryList.get(i).getParentCategory().getTranslateInd());
				parentCategories.setValue(categoryList.get(i).getParentCategory().getValue());
				parentCategories.setParentCategory(categoryList.get(i).getParentCategory().getParentCategory());
				categoryList.add(parentCategories);
			}
		}
		
		Collections.reverse(categoryList);

		for (Category category2 : categoryList) {
			CatalogCategory catalogCategory = new CatalogCategory();
			catalogCategory.setCatalog(catalog);
			catalogCategory.setCategory(category2);
			catalogCategorySession.insertCatalogCategory(catalogCategory, null);
		}
		
		Double stock = new Double(0);
		
		CatalogLocation catalogLocation = catalogLocationSession.getCatalogLocationByCatalogId(catalog);
		if(catalogLocation == null) {
			CatalogLocation catalogLocationNew = new CatalogLocation();
			catalogLocationNew.setCatalog(catalog);
			catalogLocationNew.setStockProduct(stock);
			catalogLocationSession.insertCatalogLocation(catalogLocationNew, null);
		} else {
			catalogLocation.setCatalog(catalog);
			catalogLocation.setStockProduct(stock);
			catalogLocationSession.updateCatalogLocation(catalogLocation, null);	
		}
	
		List<CatalogImage> catalogImageList = catalogImageSession.getCatalogImageListByCatalogId(catalog);
		if(catalogImageList.size() > 0) {
			Integer imageListSequence = 0;
			for (CatalogImage catalogImage : catalogImageList) {
				catalogImage.setCatalog(catalog);
				catalogImage.setStatus(true);
				catalogImage.setImagesFileName(imageList.get(imageListSequence));
				catalogImageSession.updateCatalogImage(catalogImage, null);
				imageListSequence++;
			}
		} else {
			Integer sequence = 1;
			for (int i = 0; i < imageList.size(); i++) {
				CatalogImage catalogImage = new CatalogImage();
				catalogImage.setCatalog(catalog);
				catalogImage.setStatus(true);
				catalogImage.setUrutanPesanan(sequence);
				catalogImage.setImagesFileName(imageList.get(i));
				catalogImageSession.insertCatalogImage(catalogImage, null);
				sequence++;
			}
		}
		
		
		List<AttributePanelGroup> attributePanelGroup = attributeGroup.getAttributePanelGroupList();
		
		List<CatalogAttribute> catalogAttributeList = catalogAttributeSession.getCatalogAttributeListByCatalogId(catalog);
		if(catalogAttributeList.size() > 0) {
			for (CatalogAttribute catalogAttribute : catalogAttributeList) {
				catalogAttributeSession.deleteRowCatalogAttribute(catalogAttribute.getId(), null);
			}
		}
		
		if (attributeNameNewList.size() > 0) {
				
			for(int j=0; j < attributePanelGroup.size(); j++) { // 10
				
				Boolean isNew = true;
				
				for (int a=0; a < attributeNameNewList.size(); a++) { //5
					if(attributePanelGroup.get(j).getAttribute().getName().equals(attributeNameNewList.get(a))) {
						CatalogAttribute catalogAttribute = new CatalogAttribute();
						catalogAttribute.setCatalog(catalog);
						String valueCatalogAttribute = attributeValueNewList.get(a);
						if(valueCatalogAttribute.trim().equals("-") || valueCatalogAttribute.isEmpty()) {
							catalogAttribute.setNilai(null);
						} else {
							catalogAttribute.setNilai(valueCatalogAttribute.trim());
						}
						catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
						catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
						isNew = false;						
					}
				}
				
				if(isNew){
					CatalogAttribute catalogAttribute = new CatalogAttribute();
					catalogAttribute.setCatalog(catalog);
					catalogAttribute.setNilai(null);
					catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(j));
					catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
				}
			}
	
		} else {
			
			for (int k = 0; k < attributePanelGroup.size(); k++) {
				CatalogAttribute catalogAttribute = new CatalogAttribute();
				catalogAttribute.setCatalog(catalog);
				catalogAttribute.setNilai(null);
				catalogAttribute.setAttributePanelGroup(attributePanelGroup.get(k));
				catalogAttributeSession.insertCatalogAttribute(catalogAttribute, null);
			}
			
		}
	
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Catalog> getListPagination(String vendorName, String orderKeyword, Integer pageNo, Integer pageSize, String token ) {
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		
		String query = "SELECT catalogCategory, catalog FROM Catalog catalog JOIN CatalogCategory catalogCategory on catalog.id = catalogCategory.catalog.id "
				+ "WHERE catalog.isDelete = 0 "
				+ "AND catalogCategory.category = " 
					+ "(SELECT MAX(catalogCategory2.category) FROM CatalogCategory catalogCategory2 "
					+ "WHERE catalogCategory2.catalog.id = catalog.id)"; // KAI 20201221 [19451]
		vendorName = vendorName == null || vendorName.equalsIgnoreCase("undefined") || vendorName.equalsIgnoreCase("") ? null
				: vendorName;
		
		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and catalog.vendor.id =:vendorId ";
		}
		//KAI - 20201214 - [19451] 
		
		if (vendorName != null) {
			query = query + "and catalog.vendor.nama =:vendorName ";
		}

		if (!orderKeyword.isEmpty() ) {
			query = query + "order by catalog." + orderKeyword;
		}

		Query q = getEntityManager().createQuery(query);
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(user.getId());
			q.setParameter("vendorId", vendor.getId());
		}
		//KAI - 20201214 - [19451]
		  q.setFirstResult((pageNo - 1) * pageSize);
		  q.setMaxResults(pageSize);
		 
		return q.getResultList();
	}
	
	public Long getTotalList(String vendorName, String orderKeyword, Integer pageNo, Integer pageSize, String token ) {
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		String query = "SELECT count (catalog) FROM Catalog catalog JOIN CatalogCategory catalogCategory on catalog.id = catalogCategory.catalog.id "
				+ "WHERE catalog.isDelete = 0 "
				+ "AND catalogCategory.category = " 
					+ "(SELECT MAX(catalogCategory2.category) FROM CatalogCategory catalogCategory2 "
					+ "WHERE catalogCategory2.catalog.id = catalog.id)"; // KAI 20201221 [19451]
		vendorName = vendorName == null || vendorName.equalsIgnoreCase("undefined") || vendorName.equalsIgnoreCase("") ? null
				: vendorName;
		
		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and catalog.vendor.id =:vendorId ";
		}
		//KAI - 20201214 - [19451] 
		
		if (vendorName != null) {
			query = query + "and catalog.vendor.nama =:vendorName ";
		}

		if (!orderKeyword.isEmpty() ) {
			query = query + "order by catalog." + orderKeyword;
		}

		Query q = getEntityManager().createQuery(query);
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if(roleUser.getRole().getCode().equals(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(user.getId());
			q.setParameter("vendorId", vendor.getId());
		}
		//KAI - 20201214 - [19451]
		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Object[]> getExcel(String search, String vendorName, String orderKeyword, Integer pageNo, Integer pageSize) {
		String query = "SELECT catalogCategory, catalog FROM Catalog catalog JOIN CatalogCategory catalogCategory on catalog.id = catalogCategory.catalog "
				+ "where catalog.isDelete = 0 "
				+ "AND catalogCategory.category = " 
				+ "(SELECT MAX(catalogCategory2.category) FROM CatalogCategory catalogCategory2 "
				+ "WHERE catalogCategory2.catalog.id = catalog.id)"; // KAI 20201223 [19451]
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equalsIgnoreCase("undefined") || vendorName.equalsIgnoreCase("") ? null : vendorName;
		 
		if (!search.isEmpty()) {
			query = query + "and (catalog.namaIND like :search) ";
		}
		if (vendorName != null) {
			query = query + "and catalog.vendor.nama =:vendorName ";
		}

		if (!orderKeyword.isEmpty() ) {
			query = query + "order by catalog." + orderKeyword;
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
	
	public List<Catalog> getCatalogExisting(CatalogKontrak catalogKontrak, Item item){
		Query query =em.createNamedQuery("Catalog.getCatalogExisting");
		query.setParameter("isDelete", 0)
		.setParameter("item", item)
		.setParameter("catalogKontrak", catalogKontrak);
		List<Catalog> listCatalog = query.getResultList();
		return listCatalog;
	}
	
	@SuppressWarnings("unchecked")
	public List<LaporanItemKatalogPoDTO> getReportItemKatalogPo (String search, Integer type, Integer pageNo,
			Integer pageSize, String vendorName, Date startDate, Date endDate, String token) {
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		
		List<LaporanItemKatalogPoDTO> laporanItemKatalogPoDTOList = new ArrayList<LaporanItemKatalogPoDTO>();
		String searchQuery = " AND po.poNumber like :search "; 
		String sort = "";
		String order = "";
		String filter = "";
		String roleCode = "";
		if (type==1) {
			order = " Order By po.purchaseOrderDate ASC ";
		}
		else if (type==2) {
			order = " Order By po.purchaseOrderDate DESC ";
		}
		else if (type==3) {
			order = " Order By total ASC ";
		}
		else if (type==4) {
			order = " Order By total DESC ";
		}
		else {
			order = " Order By cc.category.description ASC ";
		}
		if (!vendorName.isEmpty()) {
			sort = " AND cc.catalog.vendor.nama =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			filter = " AND po.purchaseOrderDate >= :startDate and po.purchaseOrderDate <= :endDate ";
		}
		else if (startDate != null && endDate == null) {
			filter = " AND po.purchaseOrderDate >= :startDate ";
		}
		else if (startDate == null && endDate != null) {
			filter = " AND po.purchaseOrderDate <= :endDate ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			roleCode = " and cc.catalog.vendor.id =:vendorId ";
		} else {
			roleCode = " and po.purchaseRequest.organisasi.id IN (:organisasiList) ";
		}
		
		Query q = em.createQuery("SELECT  cc.category.description, cc.catalog.catalogContractDetail.itemDesc, prItem.vendor.nama, prItem.purchaserequest.boNumber, po.poNumber, "
				+ "po.purchaseOrderDate, prItem.quantity, prItem.price, (prItem.quantity)*(prItem.price) AS total, po.rating "
				+ "FROM PurchaseOrder po "
				+ "JOIN PurchaseRequestItem prItem ON po.purchaseRequest.id = prItem.purchaserequest.id "
				+ "JOIN CatalogCategory cc ON prItem.catalog.id = cc.catalog.id "
				+ "WHERE cc.category = (select max(ccc.category) from CatalogCategory ccc where cc.catalog = ccc.catalog)  "
				+ "AND cc.isDelete = 0 "
				+ "AND prItem.isDelete = 0 "
				+ "AND po.isDelete = 0 "
				+ roleCode 
				+ searchQuery 
				+ sort
				+ filter
				+ order );
		q.setParameter("search", "%" + search +"%");
		if (!vendorName.isEmpty()) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		else if (startDate != null && endDate == null) {
			q.setParameter("startDate", startDate);
		}
		else if (startDate == null && endDate != null) {
			q.setParameter("endDate", endDate);
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
		
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);
		
		List<Object[]> objList = q.getResultList();
	    
		for (Object[] obj : objList) 
		{
			LaporanItemKatalogPoDTO laporanItemKatalogPoDTO = new LaporanItemKatalogPoDTO();
			laporanItemKatalogPoDTO.setDescription(obj[0] != null ? obj[0].toString() : null);
			laporanItemKatalogPoDTO.setDeskripsiIND(obj[1] != null ? obj[1].toString() : null);
			laporanItemKatalogPoDTO.setNama(obj[2] != null ? obj[2].toString() : null);
			laporanItemKatalogPoDTO.setBoNumber(obj[3] != null ? obj[3].toString() : null);
			laporanItemKatalogPoDTO.setPoNumber(obj[4] != null ? obj[4].toString() : null);
			laporanItemKatalogPoDTO.setApprovedDate(obj[5] != null ? (Date)obj[5] : null);
			laporanItemKatalogPoDTO.setQuantity(obj[6] != null ? (Double)obj[6] : null);
			laporanItemKatalogPoDTO.setPrice(obj[7] != null ? (Double)obj[7] : null);
			laporanItemKatalogPoDTO.setTotal(obj[8] != null ? (Double)obj[8] : null);
			laporanItemKatalogPoDTO.setRating(obj[9] != null ? (Integer)obj[9] : null);
			laporanItemKatalogPoDTOList.add(laporanItemKatalogPoDTO);
		}

		return laporanItemKatalogPoDTOList;	 
	}
	
	public Long getTotalList(String search, Integer type, Integer pageNo,
			Integer pageSize, String vendorName, Date startDate, Date endDate, String token) {
		Token tempToken = tokenSession.findByToken(token);
		User user = tempToken.getUser();
		RoleUser roleUser = roleUserSession.getByUserId(user.getId());
		
		String searchQuery = " AND po.poNumber like :search "; 
		String sort = "";
		String filter = "";
		String order = "";
		String roleCode = "";
		if (type==1) {
			order = " Order By po.purchaseOrderDate ASC ";
		}
		else if (type==2) {
			order = " Order By po.purchaseOrderDate DESC ";
		}
		else if (type==3) {
			order = " Order By total ASC ";
		}
		else if (type==4) {
			order = " Order By total DESC ";
		}
		else {
			order = " Order By cc.category.description ASC ";
		}
		if (!vendorName.isEmpty()) {
			sort = " AND cc.catalog.vendor.nama =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			filter = " AND po.purchaseOrderDate >= :startDate and po.purchaseOrderDate <= :endDate ";
		}
		else if (startDate != null && endDate == null) {
			filter = " AND po.purchaseOrderDate >= :startDate ";
		}
		else if (startDate == null && endDate != null) {
			filter = " AND po.purchaseOrderDate <= :endDate ";
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			roleCode = " and cc.catalog.vendor.id =:vendorId ";
		} else {
			roleCode = " and po.purchaseRequest.organisasi.id IN (:organisasiList) ";
		}
		
		if (!vendorName.isEmpty()) {
			sort = " AND cc.catalog.vendor.nama =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			filter = " AND po.purchaseOrderDate >= :startDate and po.purchaseOrderDate <= :endDate ";
		}
		else if (startDate != null && endDate == null) {
			filter = " AND po.purchaseOrderDate >= :startDate ";
		}
		else if (startDate == null && endDate != null) {
			filter = " AND po.purchaseOrderDate <= :endDate ";
		}
		Query q = em.createQuery("SELECT count(po) "
				+ "FROM PurchaseOrder po "
				+ "JOIN PurchaseRequestItem prItem ON po.purchaseRequest.id = prItem.purchaserequest.id "
				+ "JOIN CatalogCategory cc ON prItem.catalog.id = cc.catalog.id "
				+ "WHERE cc.category = (select max(ccc.category) from CatalogCategory ccc where cc.catalog = ccc.catalog)  "
				+ "AND cc.isDelete = 0 "
				+ "AND prItem.isDelete = 0 "
				+ "AND po.isDelete = 0 "
				+ roleCode 
				+ searchQuery 
				+ sort
				+ filter
				+ order );
		q.setParameter("search", "%" + search +"%");
		if (!vendorName.isEmpty()) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		else if (startDate != null && endDate == null) {
			q.setParameter("startDate", startDate);
		}
		else if (startDate == null && endDate != null) {
			q.setParameter("endDate", endDate);
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		} else {
			List<Integer> organisasiIdList = new ArrayList<Integer>();
			List<Organisasi> organisasiList = organisasiSession.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
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
		
		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<LaporanItemKatalogPoDTO> getExcelReportItemKatalogPo (String search, Integer type, Integer pageNo,
			Integer pageSize, String vendorName, Date startDate, Date endDate) {
		List<LaporanItemKatalogPoDTO> laporanItemKatalogPoDTOList = new ArrayList<LaporanItemKatalogPoDTO>();
		String searchQuery = " AND po.poNumber like :search "; 
		String sort = "";
		String order = "";
		String filter = "";
		if (type==1) {
			order = " Order By po.purchaseOrderDate ASC ";
		}
		else if (type==2) {
			order = " Order By po.purchaseOrderDate DESC ";
		}
		else if (type==3) {
			order = " Order By total ASC ";
		}
		else if (type==4) {
			order = " Order By total DESC ";
		}
		else {
			order = " Order By cc.category.description ASC ";
		}
		if (!vendorName.isEmpty()) {
			sort = " AND catalog.vendor.nama =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			filter = " AND po.purchaseOrderDate >= :startDate and po.purchaseOrderDate <= :endDate ";
		}
		else if (startDate != null && endDate == null) {
			filter = " AND po.purchaseOrderDate >= :startDate ";
		}
		else if (startDate == null && endDate != null) {
			filter = " AND po.purchaseOrderDate <= :endDate ";
		}
		Query q = em.createQuery("SELECT cc.category.description, catalog.catalogContractDetail.itemDesc, catalog.vendor.nama, prItem.purchaserequest.boNumber, po.poNumber, "
				+ "po.purchaseOrderDate, SUM(prItem.quantity), SUM(prItem.price), ((SUM(prItem.quantity))*(SUM(prItem.price))) AS total "
				+ "FROM Catalog catalog JOIN CatalogCategory cc ON catalog.id = cc.catalog "
				+ "JOIN PurchaseRequestItem prItem ON catalog.id = prItem.catalog "
				+ "JOIN PurchaseOrder po ON prItem.purchaserequest = po.purchaseRequest "
				+ "JOIN PurchaseRequestItem prItem ON catalog.item = prItem.item "
				+ "WHERE catalog.isDelete = 0 "
				+ "AND cc.isDelete = 0 "
				+ "AND prItem.isDelete = 0 "
				+ "AND po.isDelete = 0 " 
				+ searchQuery 
				+ sort
				+ filter
				+ "GROUP BY cc.category.description, catalog.catalogContractDetail.itemDesc, catalog.vendor.nama, prItem.purchaserequest.boNumber, po.poNumber, po.purchaseOrderDate "
				+ order );
		q.setParameter("search", "%" + search +"%");
		if (!vendorName.isEmpty()) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		else if (startDate != null && endDate == null) {
			q.setParameter("startDate", startDate);
		}
		else if (startDate == null && endDate != null) {
			q.setParameter("endDate", endDate);
		}
		
		List<Object[]> objList = q.getResultList();
	    
		for (Object[] obj : objList) 
		{
			LaporanItemKatalogPoDTO laporanItemKatalogPoDTO = new LaporanItemKatalogPoDTO();
			laporanItemKatalogPoDTO.setDescription(obj[0] != null ? obj[0].toString() : null);
			laporanItemKatalogPoDTO.setDeskripsiIND(obj[1] != null ? obj[1].toString() : null);
			laporanItemKatalogPoDTO.setNama(obj[2] != null ? obj[2].toString() : null);
			laporanItemKatalogPoDTO.setBoNumber(obj[3] != null ? obj[3].toString() : null);
			laporanItemKatalogPoDTO.setPoNumber(obj[4] != null ? obj[4].toString() : null);
			laporanItemKatalogPoDTO.setApprovedDate(obj[5] != null ? (Date)obj[5] : null);
			laporanItemKatalogPoDTO.setQuantity(obj[6] != null ? (Double)obj[6] : null);
			laporanItemKatalogPoDTO.setPrice(obj[7] != null ? (Double)obj[7] : null);
			laporanItemKatalogPoDTO.setTotal(obj[8] != null ? (Double)obj[8] : null);
			laporanItemKatalogPoDTOList.add(laporanItemKatalogPoDTO);
		}

		return laporanItemKatalogPoDTOList;	 
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
