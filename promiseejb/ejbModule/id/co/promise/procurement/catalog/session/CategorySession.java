package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
@LocalBean
public class CategorySession extends AbstractFacadeWithAudit<Category> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CategorySession() {
		super(Category.class);
	}
	
	public Category getCategory(int categoryId) {
		return super.find(categoryId);
	}

	public Category find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Category> criteria = builder.createQuery( Category.class );
        Root<Category> entityRoot = criteria.from( Category.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<Category> attributeTypeValueList =  em.createQuery(criteria).getResultList();
        	if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
        		return attributeTypeValueList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<Category> getCategoryList() {
		return getCategoryList(null, null, null, null, null);
	}
	
	public List<Category> getCategoryList(Category entity) {
		return getCategoryList(entity, null, null, null, null);
	}
	
	public List<Category> getCategoryList(Category entity, Integer startRow, Integer rowSize) {
		return getCategoryList(entity, startRow, rowSize, null, null);
	}
	
	public List<Category> getCategoryList(Category entity, String fieldName, OrderTypeEnum orderType) {
		return getCategoryList(entity, null, null, fieldName, orderType);
	}
	
	public List<Category> getCategoryList(Category entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Category> criteria = builder.createQuery( Category.class );
        Root<Category> entityRoot = criteria.from( Category.class );
        
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
	
	public List<Category> getCategoryForTreeList(Category entity) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Category> criteria = builder.createQuery( Category.class );
        Root<Category> entityRoot = criteria.from( Category.class );
        
        criteria.select( entityRoot );
        
        Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);
        
        criteria.where(whereClause);
        
        List<Order> orderList = new ArrayList<Order>();
        orderList.add(builder.asc(entityRoot.get("id")));
        criteria.orderBy(orderList);
        
        try {
        	return getEntityManager().createQuery(criteria).getResultList();
        } catch (Exception ex) {
            return null;
        }
	}
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<Category> entityRoot, Category entity) {
		Predicate whereClause = builder.conjunction();
		if (entity != null) {
			if (entity.getId() != null) {
				whereClause = builder.and(whereClause, builder.equal(entityRoot.get("id"), entity.getId()));
			}
			if (entity.getParentCategory() != null && entity.getParentCategory().getId() != null) {
				whereClause = builder.and(whereClause, builder.equal(entityRoot.get("parentCategory").get("id"), entity.getParentCategory().getId()));
			}
			if (entity.getParentCategory() != null && entity.getParentCategory().getId() == null) {
				whereClause = builder.and(whereClause, builder.isNull(entityRoot.get("parentCategory").get("id")));
			}
			if (entity.getFlagEnabled() != null) {
				whereClause = builder.and(whereClause, builder.equal(entityRoot.get("flagEnabled"), entity.getFlagEnabled()));
			}
			if (entity.getDescription() != null) {
				whereClause = builder.and(whereClause, builder.equal(entityRoot.get("description"), entity.getDescription()));
			}
			if (entity.getAdminLabel() != null) {
				whereClause = builder.and(whereClause, builder.equal(entityRoot.get("adminLabel"), entity.getAdminLabel()));
			}
		}
		whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
        
        return whereClause;
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> getCategoryListByCatalog( Integer catalogId )
			 {
		Query q = em.createQuery("SELECT cc.category FROM CatalogCategory cc WHERE cc.isDelete = 0 and cc.catalog.id = :catalogId ORDER BY cc.category.id ASC");
		q.setParameter("catalogId", catalogId);
		return  q.getResultList();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Category> getCategoryListByParentId(Integer categoryParentId) {
		Query q = em.createQuery("SELECT category FROM Category category WHERE category.isDelete = 0 and category.parentCategory.id = :categoryParentId");
		q.setParameter("categoryParentId", categoryParentId);
		return  q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Category getCategoryByNameInd(String translateInd) {
		Query q = em.createQuery("SELECT category FROM Category category WHERE category.isDelete = 0 and category.translateInd = :translateInd");
		q.setParameter("translateInd", translateInd);
		
		List<Category> categoryList = q.getResultList();
		if (categoryList != null && categoryList.size() > 0) {
			return categoryList.get(0);
		}
		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Category> getActiveCategory() {
		Query q = em.createNamedQuery("category.getActiveCategory");
		List<Category> categoryList = q.getResultList();		
		return categoryList;
		
	}
	
	public Category insertCategory(Category category, Token token) {
		category.setIsDelete(0);
		super.create(category, AuditHelper.OPERATION_CREATE, token);
		return category;
	}

	public Category updateCategory(Category category,
			Token token) {
		super.edit(category, AuditHelper.OPERATION_UPDATE, token);
		return category;
	}

	public Category deleteCategory(int id, Token token) {
		Category category = super.find(id);
		category.setIsDelete(1);
		super.edit(category, AuditHelper.OPERATION_DELETE, token);
		return category;
	}

	public Category deleteRowCategory(int id, Token token) {
		Category category = super.find(id);
		super.remove(category, AuditHelper.OPERATION_ROW_DELETE, token);
		return category;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkTranslateId(String translateInd, String toDo, Integer categoryId) {
		Query q = em.createNamedQuery("category.findTranslateId");
		q.setParameter("translateInd", translateInd);
		List<Category> categoryList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (categoryList != null && categoryList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (categoryList != null && categoryList.size() > 0) {
				if (categoryId.equals( categoryList.get(0).getId())) {
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
	
	
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

}
