package id.co.promise.procurement.catalog.session;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.catalog.entity.CatalogComment;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

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
public class CatalogCommentSession extends AbstractFacadeWithAudit<CatalogComment> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogCommentSession() {
		super(CatalogComment.class);
	}

	public CatalogComment find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogComment> criteria = builder.createQuery( CatalogComment.class );
        Root<CatalogComment> entityRoot = criteria.from( CatalogComment.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<CatalogComment> attributeTypeValueList =  em.createQuery(criteria).getResultList();
        	if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
        		return attributeTypeValueList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<CatalogComment> getCatalogCommentList() {
		return getCatalogCommentList(null, null, null, null, null);
	}
	
	public List<CatalogComment> getCatalogCommentList(CatalogComment entity) {
		return getCatalogCommentList(entity, null, null, null, null);
	}
	
	public List<CatalogComment> getCatalogCommentList(CatalogComment entity, Integer startRow, Integer rowSize) {
		return getCatalogCommentList(entity, startRow, rowSize, null, null);
	}
	
	public List<CatalogComment> getCatalogCommentList(CatalogComment entity, String fieldName, OrderTypeEnum orderType) {
		return getCatalogCommentList(entity, null, null, fieldName, orderType);
	}
	
	public List<CatalogComment> getCatalogCommentList(CatalogComment entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogComment> criteria = builder.createQuery( CatalogComment.class );
        Root<CatalogComment> entityRoot = criteria.from( CatalogComment.class );
        
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<CatalogComment> entityRoot, CatalogComment entity) {
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
	
	public CatalogComment insertCatalogComment(CatalogComment entity, Token token) {
		entity.setIsDelete(0);
		entity.setCreated(new Date());
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogComment updateCatalogComment(CatalogComment entity, Token token) {
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogComment deleteCatalogComment(int id, Token token) {
		CatalogComment entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogComment deleteRowCatalogComment(int id, Token token) {
		CatalogComment entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	
	public Integer deleteCatalogCommentList(int catalogId) {
		Query query = em.createQuery("delete from CatalogComment cc where cc.catalog.id = :catalogId ");
		query.setParameter("catalogId", catalogId);
		return query.executeUpdate();
	}
	
	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

}
