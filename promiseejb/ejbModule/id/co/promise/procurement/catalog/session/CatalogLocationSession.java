package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogLocation;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.HariLibur;
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
public class CatalogLocationSession extends AbstractFacadeWithAudit<CatalogLocation> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogLocationSession() {
		super(CatalogLocation.class);
	}

	public CatalogLocation find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogLocation> criteria = builder.createQuery( CatalogLocation.class );
        Root<CatalogLocation> entityRoot = criteria.from( CatalogLocation.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<CatalogLocation> attributeTypeValueList =  em.createQuery(criteria).getResultList();
        	if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
        		return attributeTypeValueList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<CatalogLocation> getCatalogLocationList() {
		return getCatalogLocationList(null, null, null, null, null);
	}
	
	public List<CatalogLocation> getCatalogLocationList(CatalogLocation entity) {
		return getCatalogLocationList(entity, null, null, null, null);
	}
	
	public List<CatalogLocation> getCatalogLocationList(CatalogLocation entity, Integer startRow, Integer rowSize) {
		return getCatalogLocationList(entity, startRow, rowSize, null, null);
	}
	
	public List<CatalogLocation> getCatalogLocationList(CatalogLocation entity, String fieldName, OrderTypeEnum orderType) {
		return getCatalogLocationList(entity, null, null, fieldName, orderType);
	}
	
	public List<CatalogLocation> getCatalogLocationList(CatalogLocation entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogLocation> criteria = builder.createQuery( CatalogLocation.class );
        Root<CatalogLocation> entityRoot = criteria.from( CatalogLocation.class );
        
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<CatalogLocation> entityRoot, CatalogLocation entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getCatalog() != null) {
        		if (entity.getCatalog().getId() != null) {
        			whereClause = builder.and(whereClause, builder.equal(entityRoot.get("catalog").get("id"), entity.getCatalog().getId()));
        		}
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
            }
        }
		whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
        
        return whereClause;
	}
	
	public CatalogLocation insertCatalogLocation(CatalogLocation entity, Token token) {
		entity.setIsDelete(0);
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogLocation updateCatalogLocation(CatalogLocation entity, Token token) {
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogLocation deleteCatalogLocation(int id, Token token) {
		CatalogLocation entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogLocation deleteRowCatalogLocation(int id, Token token) {
		CatalogLocation entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	
	public Integer deleteCatalogLocationList(int catalogId) {
		Query query = em.createQuery("delete from CatalogLocation cl where cl.catalog.id = :catalogId ");
		query.setParameter("catalogId", catalogId);
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public CatalogLocation getCatalogLocationByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogLocation FROM CatalogLocation catalogLocation WHERE catalogLocation.isDelete = 0 AND catalogLocation.catalog = :catalog");
		q.setParameter("catalog", catalog);
		
		List<CatalogLocation> CatalogLocation = q.getResultList();
		if (CatalogLocation != null && CatalogLocation.size() > 0) {
			return CatalogLocation.get(0);
		}
		
		return null;
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
