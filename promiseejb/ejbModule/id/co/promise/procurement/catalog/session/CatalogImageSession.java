package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.CatalogLocation;
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
public class CatalogImageSession extends AbstractFacadeWithAudit<CatalogImage> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogImageSession() {
		super(CatalogImage.class);
	}

	public CatalogImage find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogImage> criteria = builder.createQuery( CatalogImage.class );
        Root<CatalogImage> entityRoot = criteria.from( CatalogImage.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<CatalogImage> attributeTypeValueList =  em.createQuery(criteria).getResultList();
        	if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
        		return attributeTypeValueList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<CatalogImage> getCatalogImageList() {
		return getCatalogImageList(null, null, null, null, null);
	}
	
	public List<CatalogImage> getCatalogImageList(CatalogImage entity) {
		return getCatalogImageList(entity, null, null, null, null);
	}
	
	public List<CatalogImage> getCatalogImageList(CatalogImage entity, Integer startRow, Integer rowSize) {
		return getCatalogImageList(entity, startRow, rowSize, null, null);
	}
	
	public List<CatalogImage> getCatalogImageList(CatalogImage entity, String fieldName, OrderTypeEnum orderType) {
		return getCatalogImageList(entity, null, null, fieldName, orderType);
	}
	
	public List<CatalogImage> getCatalogImageList(CatalogImage entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogImage> criteria = builder.createQuery( CatalogImage.class );
        Root<CatalogImage> entityRoot = criteria.from( CatalogImage.class );
        
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<CatalogImage> entityRoot, CatalogImage entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getCatalog() != null) {
        		if (entity.getCatalog().getId() != null) {
        			whereClause = builder.and(whereClause, builder.equal(entityRoot.get("catalog").get("id"), entity.getCatalog().getId()));
        		}
            }
        }
		whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
        
        return whereClause;
	}
	
	public CatalogImage insertCatalogImage(CatalogImage entity, Token token) {
		entity.setIsDelete(0);
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogImage updateCatalogImage(CatalogImage entity, Token token) {
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogImage deleteCatalogImage(int id, Token token) {
		CatalogImage entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogImage deleteRowCatalogImage(int id, Token token) {
		CatalogImage entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	
	public Integer deleteCatalogImageList(int catalogId) {
		Query query = em.createQuery("delete from CatalogImage ci where ci.catalog.id = :catalogId ");
		query.setParameter("catalogId", catalogId);
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public CatalogImage getCatalogImageByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogImage FROM CatalogImage catalogImage WHERE catalogImage.isDelete = 0 AND catalogImage.catalog = :catalog");
		q.setParameter("catalog", catalog);
		
		List<CatalogImage> catalogImage = q.getResultList();
		if (catalogImage != null && catalogImage.size() > 0) {
			return catalogImage.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogImage> getCatalogImageListByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogImage FROM CatalogImage catalogImage WHERE catalogImage.isDelete = 0 AND catalogImage.catalog = :catalog");
		q.setParameter("catalog", catalog);
		
		List<CatalogImage> catalogImageList = q.getResultList();
		
		return catalogImageList;
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
