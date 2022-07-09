package id.co.promise.procurement.catalog.session;

import java.util.ArrayList;
import java.util.Calendar;
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

import id.co.promise.procurement.approval.ApprovalProcessSession;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.CatalogVendor;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogCategorySession extends AbstractFacadeWithAudit<CatalogCategory> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private ApprovalProcessSession approvalProcessSession;
	
	public CatalogCategorySession() {
		super(CatalogCategory.class);
	}

	public CatalogCategory find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogCategory> criteria = builder.createQuery( CatalogCategory.class );
        Root<CatalogCategory> entityRoot = criteria.from( CatalogCategory.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<CatalogCategory> attributeTypeValueList =  em.createQuery(criteria).getResultList();
        	if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
        		return attributeTypeValueList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<CatalogCategory> getCatalogCategoryList() {
		return getCatalogCategoryList(null, null, null, null, null);
	}
	
	public List<CatalogCategory> getCatalogCategoryList(CatalogCategory entity) {
		return getCatalogCategoryList(entity, null, null, null, null);
	}
	
	public List<CatalogCategory> getCatalogCategoryList(CatalogCategory entity, Integer startRow, Integer rowSize) {
		return getCatalogCategoryList(entity, startRow, rowSize, null, null);
	}
	
	public List<CatalogCategory> getCatalogCategoryList(CatalogCategory entity, String fieldName, OrderTypeEnum orderType) {
		return getCatalogCategoryList(entity, null, null, fieldName, orderType);
	}
	
	public List<CatalogCategory> getCatalogCategoryList(CatalogCategory entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogCategory> criteria = builder.createQuery( CatalogCategory.class );
        Root<CatalogCategory> entityRoot = criteria.from( CatalogCategory.class );
        
        criteria.select( entityRoot );
        
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
        		return getEntityManager().createQuery(criteria).setFirstResult(startRow).setMaxResults(rowSize).getResultList();
        	} else {
        		return getEntityManager().createQuery(criteria).getResultList();
        	}
        } catch (Exception ex) {
            return null;
        }
	}
	
	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogListByVendorId(CatalogVendor catalogVendor){
		String queryString = "select distinct c from Catalog c";
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += ", CatalogKontrak ck "; 
		}
		Boolean isExist = false;
		if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getValue() != null) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			queryString += ", CatalogAttribute ca ";
		}
		
		queryString += " where c.isDelete = 0 ";
		
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += " AND ck.catalog.id = c.id "; 
		}		
		
		if (catalogVendor.getName() != null) {
			queryString += " AND (c.namaIND like :nama or c.deskripsiIND like :nama or c.vendor.Alamat like :nama) ";
		}
		if (catalogVendor.getVendor() != null) {
			queryString += " AND c.vendor.id =:vendorId ";
		}
		if (catalogVendor.getAttributeGroup() != null) {
			queryString += " AND c.attributeGroup =:attributeGroup ";
		}
		if (isExist) {
			queryString += " AND ca.catalog.id = c.id  ";
			if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
				String vQuery = " AND ( ";
				int counter = 0;
				for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
					if (attributePanelGroup.getValue() != null) {
						if (counter != 0) {
							vQuery += " OR ";
						}
						String inputTypeName = attributePanelGroup.getAttribute().getInputType().getName(); 
						if (inputTypeName.equals("Text") || inputTypeName.equals("Textarea") || inputTypeName.equals("Checkbox")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai like '%" + attributePanelGroup.getValue() + "%' )";
						}
						if (inputTypeName.equals("Number") || inputTypeName.equals("Selectbox") || inputTypeName.equals("Radio") || inputTypeName.equals("Date")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai = " + attributePanelGroup.getValue() + ") ";
						}
						counter = 1;
					}
				}
				if (counter != 0) {
					queryString += vQuery + " ) ";
				}
			}
		}
		
		if (catalogVendor.getStringOrder() != null){
			queryString += " order by c." + catalogVendor.getStringOrder();
		}
		
//		System.out.println("Q check:"+queryString);
		
		Query query = getEntityManager().createQuery(queryString);

		if (catalogVendor.getName() != null) {
			query.setParameter("nama", "%"+catalogVendor.getName()+"%");
		}
		if (catalogVendor.getVendor() != null) {
			query.setParameter("vendorId", catalogVendor.getVendor().getId());
		}
		if (catalogVendor.getAttributeGroup() != null) {
			query.setParameter("attributeGroup", catalogVendor.getAttributeGroup());
		}
			
		if (catalogVendor.getCurrentPage() != null) {
			query.setFirstResult(catalogVendor.getCurrentPage()-1);
			query.setMaxResults(catalogVendor.getPageSize());
		}
		
		List<CatalogVendor> catalogVendorList = query.getResultList();
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogByVendorId(CatalogVendor catalogVendor){
		String queryString = "select distinct c from Catalog c";
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += ", CatalogKontrak ck "; 
		}
		Boolean isExist = false;
		if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getValue() != null) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			queryString += ", CatalogAttribute ca ";
		}
		
		queryString += " where c.isDelete = 0 AND c.status = 1";
		
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += " AND ck.catalog.id = c.id "; 
		}		
		
		if (catalogVendor.getName() != null) {
			queryString += " AND (c.namaIND like :nama or c.deskripsiIND like :nama or c.vendor.Alamat like :nama) ";
		}
		if (catalogVendor.getVendor() != null) {
			queryString += " AND c.vendor.id =:vendorId ";
		}
		if (catalogVendor.getAttributeGroup() != null) {
			queryString += " AND c.attributeGroup =:attributeGroup ";
		}
		if (isExist) {
			queryString += " AND ca.catalog.id = c.id  ";
			if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
				String vQuery = " AND ( ";
				int counter = 0;
				for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
					if (attributePanelGroup.getValue() != null) {
						if (counter != 0) {
							vQuery += " OR ";
						}
						String inputTypeName = attributePanelGroup.getAttribute().getInputType().getName(); 
						if (inputTypeName.equals("Text") || inputTypeName.equals("Textarea") || inputTypeName.equals("Checkbox")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai like '%" + attributePanelGroup.getValue() + "%' )";
						}
						if (inputTypeName.equals("Number") || inputTypeName.equals("Selectbox") || inputTypeName.equals("Radio") || inputTypeName.equals("Date")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai = " + attributePanelGroup.getValue() + ") ";
						}
						counter = 1;
					}
				}
				if (counter != 0) {
					queryString += vQuery + " ) ";
				}
			}
		}
		
		if (catalogVendor.getStringOrder() != null){
			queryString += " order by c." + catalogVendor.getStringOrder();
		}
		
//		System.out.println("Q check:"+queryString);
		
		Query query = getEntityManager().createQuery(queryString);

		if (catalogVendor.getName() != null) {
			query.setParameter("nama", "%"+catalogVendor.getName()+"%");
		}
		if (catalogVendor.getVendor() != null) {
			query.setParameter("vendorId", catalogVendor.getVendor().getId());
		}
		if (catalogVendor.getAttributeGroup() != null) {
			query.setParameter("attributeGroup", catalogVendor.getAttributeGroup());
		}
			
		if (catalogVendor.getCurrentPage() != null) {
			query.setFirstResult(catalogVendor.getCurrentPage()-1);
			query.setMaxResults(catalogVendor.getPageSize());
		}
		
		List<CatalogVendor> catalogVendorList = query.getResultList();
		
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Catalog> getActiveCatalogList(CatalogVendor catalogVendor) {
		Date today=new Date();
		Calendar date=Calendar.getInstance();
		today= date.getTime();
		String queryString = "select DISTINCT c from Catalog c join c.vendor v ";
		
		Boolean isExist = false;
		if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && 
				catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getValue() != null) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			queryString += ", CatalogAttribute ca ";
		}
		
		queryString += " where "
				+ "c.isDelete = 0 and c.status = 1 and c.catalogKontrak.tglMulailKontrak <= :today and c.catalogKontrak.tglAkhirKontrak >= :today "
				+ "And c.catalogKontrak.statusKontrak = 1 ";
		
		if(catalogVendor.getStringOrder() != null && (catalogVendor.getStringOrder().contains("kota") || catalogVendor.getStringOrder().contains("kota asc") )){
			queryString += " and c in ( "
					+" select k.catalog from CatalogLocation k) ";
		}
			
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			queryString += " AND c in (select cc.catalog from CatalogCategory cc where cc.category in :categoryList) ";
		}
		if (catalogVendor.getName() != null) {
			queryString += " AND (c.namaIND like :nama or c.deskripsiIND like :nama or v.nama like :nama or v.alamat like :nama) ";
		}
		if (catalogVendor.getVendor() != null) {
			queryString += " AND v.id =:vendorId ";
		}
		if (catalogVendor.getAttributeGroup() != null) {
			queryString += " AND c.attributeGroup =:attributeGroup ";
		}
		if (catalogVendor.getUserId() != null) {
			queryString += " AND c.user in :userList ";
		}
		if (isExist) {
			queryString += " AND ca.catalog.id = c.id  ";
			if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
				String vQuery = " AND ( ";
				int counter = 0;
				for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
					if (attributePanelGroup.getValue() != null) {
						if (counter != 0) {
							vQuery += " OR ";
						}
						String inputTypeName = attributePanelGroup.getAttribute().getInputType().getName(); 
						if (inputTypeName.equals("Text") || inputTypeName.equals("Textarea") || inputTypeName.equals("Checkbox")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai like '%" + attributePanelGroup.getValue() + "%' )";
						}
						if (inputTypeName.equals("Number") || inputTypeName.equals("Selectbox") || inputTypeName.equals("Radio") || inputTypeName.equals("Date")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai = " + attributePanelGroup.getValue() + ") ";
						}
						counter = 1;
					}
				}
				if (counter != 0) {
					queryString += vQuery + " ) ";
				}
			}
		}
		
		Boolean kotakali = false;
		if (catalogVendor.getStringOrder() != null){
			
			if(catalogVendor.getStringOrder().contains("kota") || catalogVendor.getStringOrder().contains("kota asc") ){
				queryString +=" ORDER BY c.vendor.kota asc";
			}
			else{
				queryString += " order by c." + catalogVendor.getStringOrder();
			}
		}
		Query query = null;
		if(kotakali == true){
		query = getEntityManager().createNativeQuery(queryString, Catalog.class);
		}
		else{
		query = getEntityManager().createQuery(queryString);
		}
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			query.setParameter("categoryList", catalogVendor.getCategoryList());
		}
		if (catalogVendor.getName() != null) {
			query.setParameter("nama", "%"+catalogVendor.getName()+"%");
		}
		if (catalogVendor.getVendor() != null) {
			query.setParameter("vendorId", catalogVendor.getVendor().getId());
		}
		if (catalogVendor.getAttributeGroup() != null) {
			query.setParameter("attributeGroup", catalogVendor.getAttributeGroup());
		}
		if (catalogVendor.getUserList() != null && catalogVendor.getUserList().size() > 0) {
			query.setParameter("userList", catalogVendor.getUserList());
		}

		query.setParameter("today", today);
		//query.setParameter("idRootParent", idRootParent);
		
		/*if (catalogVendor.getCurrentPage() != null) {
			query.setFirstResult(catalogVendor.getCurrentPage());
			query.setMaxResults(catalogVendor.getPageSize());
		}*/

		List<Catalog> catalogList = query.getResultList();
		List<Catalog> catalogList1 = new ArrayList<Catalog>();
		Integer index = 0;
		for(Catalog catalog : catalogList) {
			try {
				ApprovalProcess approvalProcess = approvalProcessSession.findByCatlog(catalog.getId());
				if(approvalProcess!=null) {
					catalogList.get(index).setApprovalProcess(approvalProcess);
				}else {
					catalogList.get(index).setApprovalProcess(null);
				}
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		for(Catalog catalog1 : catalogList) {
			if(catalog1.getApprovalProcess().getStatus() == 3) {
				catalogList1.add(catalog1);
			}
		}
		
		return  catalogList;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getActiveCatalogListCount(CatalogVendor catalogVendor) {
		Date today=new Date();
		Calendar date=Calendar.getInstance();
		today= date.getTime();
		String queryString = "select DISTINCT c from Catalog c join c.vendor v";
		
		Boolean isExist = false;
		if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && 
				catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getValue() != null) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			queryString += ", CatalogAttribute ca ";
		}
		
		queryString += " where "
				+ "c.isDelete = 0 and c.status = 1 and c.catalogKontrak.tglMulailKontrak <= :today and c.catalogKontrak.tglAkhirKontrak >= :today "
				+ "And c.catalogKontrak.statusKontrak = 1 ";
		
		if(catalogVendor.getStringOrder() != null && (catalogVendor.getStringOrder().contains("kota") || catalogVendor.getStringOrder().contains("kota asc") )){
			queryString += " and c in ( "
					+" select k.catalog from CatalogLocation k) ";
		}
			
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			queryString += " AND c in (select cc.catalog from CatalogCategory cc where cc.category in :categoryList) ";
		}
		if (catalogVendor.getName() != null) {
			queryString += " AND (c.namaIND like :nama or c.deskripsiIND like :nama or v.nama like :nama or v.alamat like :nama) ";
		}
		if (catalogVendor.getVendor() != null) {
			queryString += " AND v.id =:vendorId ";
		}
		if (catalogVendor.getAttributeGroup() != null) {
			queryString += " AND c.attributeGroup =:attributeGroup ";
		}
		if (catalogVendor.getUserId() != null) {
			queryString += " AND c.user in :userList ";
		}
		if (isExist) {
			queryString += " AND ca.catalog.id = c.id  ";
			if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
				String vQuery = " AND ( ";
				int counter = 0;
				for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
					if (attributePanelGroup.getValue() != null) {
						if (counter != 0) {
							vQuery += " OR ";
						}
						String inputTypeName = attributePanelGroup.getAttribute().getInputType().getName(); 
						if (inputTypeName.equals("Text") || inputTypeName.equals("Textarea") || inputTypeName.equals("Checkbox")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai like '%" + attributePanelGroup.getValue() + "%' )";
						}
						if (inputTypeName.equals("Number") || inputTypeName.equals("Selectbox") || inputTypeName.equals("Radio") || inputTypeName.equals("Date")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai = " + attributePanelGroup.getValue() + ") ";
						}
						counter = 1;
					}
				}
				if (counter != 0) {
					queryString += vQuery + " ) ";
				}
			}
		}
		
		Boolean kotakali = false;
		if (catalogVendor.getStringOrder() != null){
			
			if(catalogVendor.getStringOrder().contains("kota") || catalogVendor.getStringOrder().contains("kota asc") ){
				queryString +=" ORDER BY c.vendor.kota asc";
			}
			else{
				queryString += " order by c." + catalogVendor.getStringOrder();
			}
		}
		Query query = null;
		if(kotakali == true){
		query = getEntityManager().createNativeQuery(queryString, Catalog.class);
		}
		else{
		query = getEntityManager().createQuery(queryString);
		}
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			query.setParameter("categoryList", catalogVendor.getCategoryList());
		}
		if (catalogVendor.getName() != null) {
			query.setParameter("nama", "%"+catalogVendor.getName()+"%");
		}
		if (catalogVendor.getVendor() != null) {
			query.setParameter("vendorId", catalogVendor.getVendor().getId());
		}
		if (catalogVendor.getAttributeGroup() != null) {
			query.setParameter("attributeGroup", catalogVendor.getAttributeGroup());
		}
		if (catalogVendor.getUserList() != null && catalogVendor.getUserList().size() > 0) {
			query.setParameter("userList", catalogVendor.getUserList());
		}

		query.setParameter("today", today);
		//query.setParameter("idRootParent", idRootParent);
		
		/*if (catalogVendor.getCurrentPage() != null) {
			query.setFirstResult(catalogVendor.getCurrentPage());
			query.setMaxResults(catalogVendor.getPageSize());
		}*/
	
		List<Catalog> catalogList = query.getResultList();
		List<Catalog> catalogList1 = new ArrayList<Catalog>();
		Integer index = 0;
		for(Catalog catalog : catalogList) {
			try {
				ApprovalProcess approvalProcess = approvalProcessSession.findByCatlog(catalog.getId());
				if(approvalProcess!=null) {
					catalogList.get(index).setApprovalProcess(approvalProcess);
				}else {
					catalogList.get(index).setApprovalProcess(null);
				}
				index++;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		for(Catalog catalog1 : catalogList) {
			if(catalog1.getApprovalProcess().getStatus() == 3) {
				catalogList1.add(catalog1);
			}
		}
		
		return  catalogList1.size();
	}
	
	public Long getCatalogListCount(CatalogVendor catalogVendor) {
		String queryString = "select count(distinct c) from CatalogCategory cc right join cc.catalog c left join c.vendor v ";
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += ", CatalogKontrak ck "; 
		}
		Boolean isExist = false;
		if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getValue() != null) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			queryString += ", CatalogAttribute ca ";
		}
		
		queryString += " where c.isDelete = 0 and c.status = 1 ";
		
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += " AND ck.catalog.id = c.id "; 
		}		
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			queryString += " AND cc.category in :categoryList ";
		}
		if (catalogVendor.getName() != null) {
			queryString += " AND (c.namaIND like :nama or c.deskripsiIND like :nama or v.Alamat like :nama) ";
		}
		if (catalogVendor.getVendor() != null) {
			queryString += " AND v.id =:vendorId ";
		}
		if (catalogVendor.getAttributeGroup() != null) {
			queryString += " AND c.attributeGroup =:attributeGroup ";
		}
		if (catalogVendor.getUserId() != null) {
			queryString += " AND c.user in :userList ";
		}
		if (isExist) {
			queryString += " AND ca.catalog.id = c.id  ";
			if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
				String vQuery = " AND ( ";
				int counter = 0;
				for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
					if (attributePanelGroup.getValue() != null) {
						if (counter != 0) {
							vQuery += " OR ";
						}
						String inputTypeName = attributePanelGroup.getAttribute().getInputType().getName(); 
						if (inputTypeName.equals("Text") || inputTypeName.equals("Textarea") || inputTypeName.equals("Checkbox")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai like '%" + attributePanelGroup.getValue() + "%' )";
						}
						if (inputTypeName.equals("Number") || inputTypeName.equals("Selectbox") || inputTypeName.equals("Radio") || inputTypeName.equals("Date")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai = " + attributePanelGroup.getValue() + ") ";
						}
						counter = 1;
					}
				}
				if (counter != 0) {
					queryString += vQuery + " ) ";
				}
			}
		}
		Boolean kotakali = false;
		if (catalogVendor.getStringOrder() != null){
			
			if(catalogVendor.getStringOrder().contains("kota") || catalogVendor.getStringOrder().contains("kota asc") ){
				
				kotakali = true;
				
				queryString ="select cat.* from promise_t3_catalog as cat LEFT JOIN promise_t2_vendor as vv on vv.VENDOR_ID = cat.VENDOR_ID"
							+" where cat.CATALOG_ID in ( "
							+" select k.CATALOG_ID from "
							+" (SELECT * from promise_t4_catalog_location) as k)"
							+" ORDER BY vv.KOTA asc";
			}
			else{
				queryString += " order by c." + catalogVendor.getStringOrder();
			}
		}
		
		//Query query = getEntityManager().createQuery(queryString);
		Query query = null;
		if(kotakali == true){
		query = getEntityManager().createNativeQuery(queryString, Catalog.class);
		}
		else{
		query = getEntityManager().createQuery(queryString);
		}
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			query.setParameter("categoryList", catalogVendor.getCategoryList());
		}
		if (catalogVendor.getName() != null) {
			query.setParameter("nama", "%"+catalogVendor.getName()+"%");
		}
		if (catalogVendor.getVendor() != null) {
			query.setParameter("vendorId", catalogVendor.getVendor().getId());
		}
		if (catalogVendor.getAttributeGroup() != null) {
			query.setParameter("attributeGroup", catalogVendor.getAttributeGroup());
		}
		if (catalogVendor.getUserList() != null && catalogVendor.getUserList().size() > 0) {
			query.setParameter("userList", catalogVendor.getUserList());
		}

//		System.out.println("Cek query getActiveCatalogListCount : "+queryString);
		
		return (Long) query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Catalog> getCatalogList(CatalogVendor catalogVendor) {
		String queryString = "select c from Catalog c join CatalogCategory cc on c = cc.catalog left join c.vendor v ";
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += ", CatalogKontrak ck "; 
		}
		Boolean isExist = false;
		if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getValue() != null) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			queryString += ", CatalogAttribute ca ";
		}
		
		queryString += " where c.isDelete = 0 ";
		
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += " AND ck.catalog.id = c.id "; 
		}		
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			queryString += " AND cc.category in :categoryList ";
		}
		if (catalogVendor.getName() != null) {
			queryString += " AND (c.namaIND like :nama or c.deskripsiIND like :nama or v.Alamat like :nama) ";
		}
		if (catalogVendor.getVendor() != null) {
			queryString += " AND v.id =:vendorId ";
		}
		if (catalogVendor.getAttributeGroup() != null) {
			queryString += " AND c.attributeGroup =:attributeGroup ";
		}
		if (catalogVendor.getUserId() != null) {
			queryString += " AND c.user in :userList ";
		}
		if (isExist) {
			queryString += " AND ca.catalog.id = c.id  ";
			if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
				String vQuery = " AND ( ";
				int counter = 0;
				for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
					if (attributePanelGroup.getValue() != null) {
						if (counter != 0) {
							vQuery += " OR ";
						}
						String inputTypeName = attributePanelGroup.getAttribute().getInputType().getName(); 
						if (inputTypeName.equals("Text") || inputTypeName.equals("Textarea") || inputTypeName.equals("Checkbox")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai like '%" + attributePanelGroup.getValue() + "%' )";
						}
						if (inputTypeName.equals("Number") || inputTypeName.equals("Selectbox") || inputTypeName.equals("Radio") || inputTypeName.equals("Date")) {
							vQuery += " (ca.attributePanelGroup.id = " + attributePanelGroup.getId() 
									+ " AND ca.nilai = " + attributePanelGroup.getValue() + ") ";
						}
						counter = 1;
					}
				}
				if (counter != 0) {
					queryString += vQuery + " ) ";
				}
			}
		}
		
		if (catalogVendor.getStringOrder() != null){
			queryString += " order by c." + catalogVendor.getStringOrder();
		}
		
		Query query = getEntityManager().createQuery(queryString);
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			query.setParameter("categoryList", catalogVendor.getCategoryList());
		}
		if (catalogVendor.getName() != null) {
			query.setParameter("nama", "%"+catalogVendor.getName()+"%");
		}
		if (catalogVendor.getVendor() != null) {
			query.setParameter("vendorId", catalogVendor.getVendor().getId());
		}
		if (catalogVendor.getAttributeGroup() != null) {
			query.setParameter("attributeGroup", catalogVendor.getAttributeGroup());
		}
		if (catalogVendor.getUserList() != null && catalogVendor.getUserList().size() > 0) {
			query.setParameter("userList", catalogVendor.getUserList());
		}
		
		if (catalogVendor.getCurrentPage() != null) {
			query.setFirstResult(catalogVendor.getCurrentPage());
			query.setMaxResults(catalogVendor.getPageSize());
		}
		return query.getResultList();
	}
	
	public Long countCatalogList(CatalogVendor catalogVendor) {
		String queryString = "select count(distinct c) from CatalogCategory cc right join cc.catalog c left join c.vendor v ";
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += ", CatalogKontrak ck "; 
		}
		Boolean isExist = false;
		if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
			for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
				if (attributePanelGroup.getValue() != null) {
					isExist = true;
					break;
				}
			}
		}
		if (isExist) {
			queryString += ", CatalogAttribute ca ";
		}
		
		queryString += " where c.isDelete = 0 ";
		
		if (catalogVendor.getContract() != null && catalogVendor.getContract() == true) {
			queryString += " AND ck.catalog.id = c.id "; 
		}		
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			queryString += " AND cc.category in :categoryList ";
		}
		if (catalogVendor.getName() != null) {
			queryString += " AND (c.namaIND like :nama or c.deskripsiIND like :nama or v.Alamat like :nama) ";
		}
		if (catalogVendor.getVendor() != null) {
			queryString += " AND v.id =:vendorId ";
		}
		if (isExist) {
			queryString += " AND ca.catalog.id = c.id ";
			if (catalogVendor.getAttributeGroup() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList() != null && catalogVendor.getAttributeGroup().getAttributePanelGroupList().size() > 0) {
				for (AttributePanelGroup attributePanelGroup : catalogVendor.getAttributeGroup().getAttributePanelGroupList()) {
					if (attributePanelGroup.getValue() != null) {
						String inputTypeName = attributePanelGroup.getAttribute().getInputType().getName(); 
						if (inputTypeName.equals("Text") || inputTypeName.equals("Textarea") || inputTypeName.equals("Checkbox")) {
							queryString += " AND ca.attributePanelGroup.attribute.id = " + attributePanelGroup.getAttribute().getId();
							queryString += " AND ca.nilai like '%" + attributePanelGroup.getValue() + "%' ";
						}
						if (inputTypeName.equals("Number") || inputTypeName.equals("Selectbox") || inputTypeName.equals("Radio") || inputTypeName.equals("Date")) {
							queryString += " AND ca.attributePanelGroup.attribute.id = " + attributePanelGroup.getAttribute().getId();
							queryString += " AND ca.nilai = " + attributePanelGroup.getValue() + " ";
						}
					}
				}
			}
		}
		Boolean kotakali = false;
		if (catalogVendor.getStringOrder() != null){
				if(catalogVendor.getStringOrder().contains("kota") || catalogVendor.getStringOrder().contains("kota asc") ){
				
				kotakali = true;
				
				queryString ="select cat.* from promise_t3_catalog as cat LEFT JOIN promise_t2_vendor as vv on vv.VENDOR_ID = cat.VENDOR_ID"
							+" where cat.CATALOG_ID in ( "
							+" select k.CATALOG_ID from "
							+" (SELECT * from promise_t4_catalog_location) as k)"
							+" ORDER BY vv.KOTA asc";
			}
			else{
				queryString += " order by c." + catalogVendor.getStringOrder();
			}
			
		}
		
		//Query query = getEntityManager().createQuery(queryString);
		Query query = null;
		if(kotakali == true){
		query = getEntityManager().createNativeQuery(queryString, Catalog.class);
		}
		else{
		query = getEntityManager().createQuery(queryString);
		}
		if (catalogVendor.getCategoryList() != null && catalogVendor.getCategoryList().size() > 0) {
			query.setParameter("categoryList", catalogVendor.getCategoryList());
		}
		if (catalogVendor.getName() != null) {
			query.setParameter("nama", "%"+catalogVendor.getName()+"%");
		}
		if (catalogVendor.getVendor() != null) {
			query.setParameter("vendorId", catalogVendor.getVendor().getId());
		}
		return (Long) query.getSingleResult();
	}
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<CatalogCategory> entityRoot, CatalogCategory entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getCatalog() != null) {
        		if (entity.getCatalog().getId() != null) {
        			whereClause = builder.and(whereClause, builder.equal(entityRoot.get("catalog").get("id"), entity.getCatalog().getId()));
        		}
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
            }
        	if (entity.getCategory() != null) {
        		if (entity.getCategory().getCategoryChildList() != null && entity.getCategory().getCategoryChildList().size() > 0) {
        			whereClause = builder.and(whereClause, entityRoot.get("category").in(entity.getCategory().getCategoryChildList()));
        		} else if(entity.getCategory().getId() != null){
        			whereClause = builder.and(whereClause, builder.equal(entityRoot.get("category").get("id"), entity.getCategory().getId()));
        		}
        	}
        }
		whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
        
        return whereClause;
	}
	
	public CatalogCategory insertCatalogCategory(CatalogCategory catalog, Token token) {
		catalog.setIsDelete(0);
		super.create(catalog, AuditHelper.OPERATION_CREATE, token);
		return catalog;
	}

	public CatalogCategory updateCatalogCategory(CatalogCategory catalog, Token token) {
		super.edit(catalog, AuditHelper.OPERATION_UPDATE, token);
		return catalog;
	}

	public CatalogCategory deleteCatalogCategory(int id, Token token) {
		CatalogCategory catalog = super.find(id);
		catalog.setIsDelete(1);
		super.edit(catalog, AuditHelper.OPERATION_UPDATE, token);
		return catalog;
	}

	public CatalogCategory deleteRowCatalogCategory(int id, Token token) {
		CatalogCategory catalog = super.find(id);
		super.remove(catalog, AuditHelper.OPERATION_ROW_DELETE, token);
		return catalog;
	}
	
	public Integer deleteCatalogCategoryList(int catalogId) {
		Query query = em.createQuery("delete from CatalogCategory cc where cc.catalog.id = :catalogId ");
		query.setParameter("catalogId", catalogId);
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public CatalogCategory getCatalogCategoryByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogCategory FROM CatalogCategory catalogCategory WHERE catalogCategory.isDelete = 0 AND catalogCategory.catalog = :catalog");
		q.setParameter("catalog", catalog);
		
		List<CatalogCategory> catalogCategory = q.getResultList();
		if (catalogCategory != null && catalogCategory.size() > 0) {
			return catalogCategory.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogCategory> getCatalogCategoryList(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogCategory FROM CatalogCategory catalogCategory WHERE catalogCategory.isDelete = 0 AND catalogCategory.catalog = :catalog");
		q.setParameter("catalog", catalog);
		
		List<CatalogCategory> catalogCategory = q.getResultList();
		return catalogCategory;
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
