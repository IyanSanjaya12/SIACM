package id.co.promise.procurement.catalog.session;

import java.util.ArrayList;
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
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;

import id.co.promise.procurement.catalog.entity.Catalog;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class CatalogKontrakSession extends AbstractFacadeWithAudit<CatalogKontrak> {
	
	@EJB
	UserSession userSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	VendorSession vendorSession;
	
	@EJB
	OrganisasiSession organisasiSession;

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	CatalogKontrakSession catalogKontrakSession;

	public CatalogKontrakSession() {
		super(CatalogKontrak.class);
	}

	public CatalogKontrak find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<CatalogKontrak> criteria = builder.createQuery(CatalogKontrak.class);
		Root<CatalogKontrak> entityRoot = criteria.from(CatalogKontrak.class);

		criteria.select(entityRoot);

		criteria.where(builder.equal(entityRoot.get("id"), id));

		try {
			List<CatalogKontrak> attributeTypeValueList = em.createQuery(criteria).getResultList();
			if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
				return attributeTypeValueList.get(0);
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}

	public List<CatalogKontrak> getCatalogKontrakListByCatalog(Catalog catalog) {
		Query q = em.createNamedQuery("CatalogKontrak.getByCatalog");
		q.setParameter("catalog", catalog);
		return q.getResultList();
	}

//	public CatalogKontrak getActiveCatalogKontrak(Catalog catalog) {
//		String queryString = "select ck from CatalogKontrak ck where ck.isDelete=0 and ck.catalog.id=:catalogId";
//		Query query = getEntityManager().createQuery(queryString);
//		query.setMaxResults(1);
//		query.setParameter("catalogId", catalog.getId());
//		try {
//			CatalogKontrak catalogKontrak = (CatalogKontrak) query.getSingleResult();
//			return catalogKontrak;
//		} catch (Exception e) {
//			return null;
//		}
//
//	}

	public CatalogKontrak insertCatalogKontrak(CatalogKontrak entity, Token token) {
		entity.setIsDelete(0);
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogKontrak updateCatalogKontrak(CatalogKontrak entity, Token token) {
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogKontrak deleteCatalogKontrak(int id, Token token) {
		CatalogKontrak entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogKontrak deleteRowCatalogKontrak(int id, Token token) {
		CatalogKontrak entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}

//	public Integer deleteCatalogKontrakList(int catalogId) {
//		Query query = em.createQuery("delete from CatalogKontrak ck where ck.catalog.id = :catalogId ");
//		query.setParameter("catalogId", catalogId);
//		return query.executeUpdate();
//	}

	@SuppressWarnings("unchecked")
	public List<CatalogKontrak> getReportCatalogKontrak(String namaPekerjaan, String vendorName, String orderKeyword, Integer pageNo,
			Integer pageSize, String sort, Date startDate, Date endDate, RoleUser roleUser) {
		String query = "SELECT catalogKontrak FROM CatalogKontrak catalogKontrak  where catalogKontrak.isDelete = 0 ";
		namaPekerjaan = namaPekerjaan == null ? "" : namaPekerjaan.trim();
		vendorName = vendorName == null || vendorName.equalsIgnoreCase("undefined") || vendorName.equalsIgnoreCase("") ? null : vendorName;
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and catalogKontrak.id IN (Select catalog.catalogKontrak.id from Catalog catalog Where catalog.isDelete = 0 And catalog.vendor.id =:vendorId) ";
		}else {
			query = query + "and catalogKontrak.id IN (Select catalog.catalogKontrak.id from Catalog catalog Where catalog.isDelete = 0 And catalog.id IN ( select prItem.catalog.id from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.purchaserequest.organisasi.id IN (:organisasiList) ) ) ";
		}
		
		if (!namaPekerjaan.equalsIgnoreCase("")) {
			query = query + "and (catalogKontrak.namaPekerjaan like :namaPekerjaan) ";
		}
		if (vendorName!=null) {
			query = query + "and catalogKontrak.vendor.nama =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			query = query + "and catalogKontrak.tglAkhirKontrak >= :startDate and catalogKontrak.tglAkhirKontrak <= :endDate ";
		}

		if (!orderKeyword.isEmpty()) {
			query = query + "order by catalogKontrak." + orderKeyword + " " + sort;
		}

		Query q = getEntityManager().createQuery(query);
		if (!namaPekerjaan.equalsIgnoreCase("")) {
			q.setParameter("namaPekerjaan", "%" + namaPekerjaan + "%");
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		} else if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_PENGGUNA_DVP)){
			q.setParameter("organisasiList", roleUser.getOrganisasi().getId());	
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
		
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);

		return q.getResultList();
	}

	public Long getTotalList(String namaPekerjaan, String vendorName, String orderKeyword, Integer pageNo, Integer pageSize, String sort,
			Date startDate, Date endDate, RoleUser roleUser) {
		String query = "SELECT count (catalogKontrak) FROM CatalogKontrak catalogKontrak  where catalogKontrak.isDelete = 0 ";
		namaPekerjaan = namaPekerjaan == null ? "" : namaPekerjaan.trim();
		vendorName = vendorName == null || vendorName.equalsIgnoreCase("undefined") || vendorName.equalsIgnoreCase("") ? null : vendorName;
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and catalogKontrak.id IN (Select catalog.catalogKontrak.id from Catalog catalog Where catalog.isDelete = 0 And catalog.vendor.id =:vendorId) ";
		}else {
			query = query + "and catalogKontrak.id IN (Select catalog.catalogKontrak.id from Catalog catalog Where catalog.isDelete = 0 And catalog.id IN ( select prItem.catalog.id from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.purchaserequest.organisasi.id IN (:organisasiList) ) ) ";
		}
		
		if (!namaPekerjaan.equalsIgnoreCase("")) {
			query = query + "and (catalogKontrak.namaPekerjaan like :namaPekerjaan) ";
		}
		if (vendorName!=null) {
			query = query + "and catalogKontrak.vendor.nama =:vendorName ";
		}
		if (startDate != null && endDate != null) {
			query = query + "and catalogKontrak.tglAkhirKontrak >= :startDate and catalogKontrak.tglAkhirKontrak <= :endDate ";
		}

		if (!orderKeyword.isEmpty()) {
			query = query + "order by catalogKontrak." + orderKeyword + " " + sort;
		}

		Query q = getEntityManager().createQuery(query);
		if (!namaPekerjaan.equalsIgnoreCase("")) {
			q.setParameter("namaPekerjaan", "%" + namaPekerjaan + "%");
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		} else if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_PENGGUNA_DVP)){
			q.setParameter("organisasiList", roleUser.getOrganisasi().getId());	
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
		
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		
		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public List<CatalogKontrak> getAllCatalogContractByVendor(String token){
		/* get vendor id */
		Token tempToken = tokenSession.findByToken(token);
		User user=tempToken.getUser();
		Vendor vendor=vendorSession.getVendorByUserId(user.getId());
		
		Query query=em.createNamedQuery("CatalogKontrak.findByVendorAndNotRelease");
		query.setParameter("vendorId", vendor.getId());
		List<CatalogKontrak> catalogKontrakList=query.getResultList();
		List<CatalogKontrak> catalogkontrakListNew = new ArrayList<>();
		for(CatalogKontrak catalogKontrak : catalogKontrakList) {
			if( !(catalogKontrak.getTglMulailKontrak().after(new Date()) || catalogKontrak.getTglAkhirKontrak().before(new Date())) ){
				catalogkontrakListNew.add(catalogKontrak);
			}
		}
		List<List<Integer>> indexDataSameListOld = new ArrayList<>();
		for(int a = 0; a < catalogkontrakListNew.size(); a++) {
			List<Integer> indexList = new ArrayList<>();
			for(int b = a+1; b < catalogkontrakListNew.size(); b++) {
				if(b < catalogkontrakListNew.size()) {
					if(catalogkontrakListNew.get(a).getNamaPekerjaan().equals(catalogkontrakListNew.get(b).getNamaPekerjaan())) {
						indexList.add(a);
					}				
				}
			}
			if(indexList.size() > 0) {
				indexDataSameListOld.add(indexList);
			}
		}
		int index =0;
		for(List<Integer> indexListTamp : indexDataSameListOld) {
			catalogkontrakListNew.remove(indexListTamp.get(indexListTamp.size()-1) - index);
			index ++;
		}
		return catalogkontrakListNew;
	}
	
	public List<CatalogKontrak> getAllCatalogContractByVendorAndNumber(String token, String number){
		/* get vendor id */
		Token tempToken = tokenSession.findByToken(token);
		User user=tempToken.getUser();
		Vendor vendor=vendorSession.getVendorByUserId(user.getId());
		
		Query query=em.createNamedQuery("CatalogKontrak.findByVendorAndNumberAndNotRelease");
		query.setParameter("vendorId", vendor.getId());
		query.setParameter("number", "%" + number.toLowerCase() + "%");
		List<CatalogKontrak> catalogKontrakList=query.getResultList();
		return catalogKontrakList;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	//KAI 20201223 tambah untuk excel
	@SuppressWarnings("unchecked")
	public List<CatalogKontrak> getReportCatalogKontrakExel(String namaPekerjaan, String vendorName, String orderKeyword, Integer pageNo,
			Integer pageSize, String sort, Date startDate, Date endDate, RoleUser roleUser) {
		String query = "SELECT catalogKontrak FROM CatalogKontrak catalogKontrak  where catalogKontrak.isDelete = 0 ";
		namaPekerjaan = namaPekerjaan == null ? "" : namaPekerjaan.trim();
		vendorName = vendorName == null || vendorName.equalsIgnoreCase("undefined") || vendorName.equalsIgnoreCase("") ? null : vendorName;
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			query = query + "and catalogKontrak.id IN (Select catalog.catalogKontrak.id from Catalog catalog Where catalog.isDelete = 0 And catalog.vendor.id =:vendorId) ";
		}else {
			query = query + "and catalogKontrak.id IN (Select catalog.catalogKontrak.id from Catalog catalog Where catalog.isDelete = 0 And catalog.id IN ( select prItem.catalog.id from PurchaseRequestItem prItem Where prItem.isDelete = 0 And prItem.purchaserequest.organisasi.id IN (:organisasiList) ) ) ";
		}
		
		if (!namaPekerjaan.equalsIgnoreCase("")) {
			query = query + "and (catalogKontrak.namaPekerjaan like :namaPekerjaan) ";
		}
		if (vendorName != null) {
			query = query + "and catalogKontrak.vendor.nama =:vendorName ";
		}
		if (startDate != null) {
			query = query + "and catalogKontrak.tglAkhirKontrak >= :startDate ";
		}
		if (endDate != null) {
			query = query + "and catalogKontrak.tglAkhirKontrak <= :endDate ";
		}

		if (!orderKeyword.isEmpty()) {
			query = query + "order by catalogKontrak." + orderKeyword + " " + sort;
		}

		Query q = getEntityManager().createQuery(query);
		if (!namaPekerjaan.equalsIgnoreCase("")) {
			q.setParameter("namaPekerjaan", "%" + namaPekerjaan + "%");
		}
		
		if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_VENDOR)) {
			Vendor vendor = vendorSession.getVendorByUserId(roleUser.getUser().getId());
			q.setParameter("vendorId", vendor.getId());	
		} else if (roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_PENGGUNA_DVP)){
			q.setParameter("organisasiList", roleUser.getOrganisasi().getId());	
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
		
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		if (startDate != null) {
			q.setParameter("startDate", startDate);
		}
		if (endDate != null) {
			q.setParameter("endDate", endDate);
		}
		q.setMaxResults(pageSize);
		
		return q.getResultList();
	}
}
