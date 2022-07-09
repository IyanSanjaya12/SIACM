package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogAttribute;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
@LocalBean
public class CatalogAttributeSession extends AbstractFacadeWithAudit<CatalogAttribute> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogAttributeSession() {
		super(CatalogAttribute.class);
	}

	public CatalogAttribute find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogAttribute> criteria = builder.createQuery( CatalogAttribute.class );
        Root<CatalogAttribute> entityRoot = criteria.from( CatalogAttribute.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<CatalogAttribute> attributeTypeValueList =  em.createQuery(criteria).getResultList();
        	if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
        		return attributeTypeValueList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<CatalogAttribute> getCatalogAttributeList() {
		return getCatalogAttributeList(null, null, null, null, null);
	}
	
	public List<CatalogAttribute> getCatalogAttributeList(CatalogAttribute entity) {
		return getCatalogAttributeList(entity, null, null, null, null);
	}
	
	public List<CatalogAttribute> getCatalogAttributeList(CatalogAttribute entity, Integer startRow, Integer rowSize) {
		return getCatalogAttributeList(entity, startRow, rowSize, null, null);
	}
	
	public List<CatalogAttribute> getCatalogAttributeList(CatalogAttribute entity, String fieldName, OrderTypeEnum orderType) {
		return getCatalogAttributeList(entity, null, null, fieldName, orderType);
	}
	
	public List<CatalogAttribute> getCatalogAttributeList(CatalogAttribute entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogAttribute> criteria = builder.createQuery( CatalogAttribute.class );
        Root<CatalogAttribute> entityRoot = criteria.from( CatalogAttribute.class );
        
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<CatalogAttribute> entityRoot, CatalogAttribute entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getCatalog() != null) {
        		if (entity.getCatalog().getId() != null) {
        			whereClause = builder.and(whereClause, builder.equal(entityRoot.get("catalog").get("id"), entity.getCatalog().getId()));
        		}
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
            }
        	if (entity.getAttributePanelGroup() != null) {
        		if (entity.getAttributePanelGroup().getId() != null) {
        			whereClause = builder.and(whereClause, builder.equal(entityRoot.get("attributePanelGroup").get("id"), entity.getAttributePanelGroup().getId()));
        		}
        	}
        }
		whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
        
        return whereClause;
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogAttribute> getCatalogAttributeListByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogAttribute FROM CatalogAttribute catalogAttribute WHERE catalogAttribute.isDelete = 0 AND catalogAttribute.catalog = :catalog");
		q.setParameter("catalog", catalog);
		
		List<CatalogAttribute> catalogAttributeList = q.getResultList();
		
		return catalogAttributeList;
	}
	
	public CatalogAttribute insertCatalogAttribute(CatalogAttribute catalog, Token token) {
		catalog.setIsDelete(0);
		super.create(catalog, AuditHelper.OPERATION_CREATE, token);
		return catalog;
	}

	public CatalogAttribute updateCatalogAttribute(CatalogAttribute catalog, Token token) {
		super.edit(catalog, AuditHelper.OPERATION_UPDATE, token);
		return catalog;
	}

	public CatalogAttribute deleteCatalogAttribute(int id, Token token) {
		CatalogAttribute catalog = super.find(id);
		catalog.setIsDelete(1);
		super.edit(catalog, AuditHelper.OPERATION_UPDATE, token);
		return catalog;
	}

	public CatalogAttribute deleteRowCatalogAttribute(int id, Token token) {
		CatalogAttribute catalog = super.find(id);
		super.remove(catalog, AuditHelper.OPERATION_ROW_DELETE, token);
		return catalog;
	}
	
	public Integer deleteCatalogAttributeList(int catalogId) {
		Query query = em.createQuery("delete from CatalogAttribute ca where ca.catalog.id = :catalogId ");
		query.setParameter("catalogId", catalogId);
		return query.executeUpdate();
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
