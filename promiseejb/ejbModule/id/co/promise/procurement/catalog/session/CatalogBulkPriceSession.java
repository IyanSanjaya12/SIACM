package id.co.promise.procurement.catalog.session;

import java.util.Date;
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

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogBulkPrice;
import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogBulkPriceSession extends AbstractFacadeWithAudit<CatalogBulkPrice> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public CatalogBulkPriceSession() {
		super(CatalogBulkPrice.class);
	}
	public List<CatalogBulkPrice> getCatalogBulkPriceListByCatalog(Catalog catalog){
		Query q = em.createNamedQuery("CatalogBulkPrice.getByCatalog");
		q.setParameter("catalog", catalog);
		return q.getResultList();	
	}
	public List<CatalogBulkPrice> getCatalogBulkPriceListByCatalogId(Integer catalogId){
		Query q = em.createNamedQuery("CatalogBulkPrice.getByCatalogId");
		q.setParameter("catalogId", catalogId);
		return q.getResultList();	
	}
	
	
	public List<CatalogBulkPrice> getCatalogBulkPriceList() {
		Query q = em.createNamedQuery("CatalogBulkPrice.getCatalogBulkPriceList");
		return q.getResultList();
	}
	
	public List<CatalogBulkPrice> getCatalogBulkPriceList(CatalogBulkPrice entity) {
		return getCatalogBulkPriceList(entity, null, null, null, null);
	}
	
	public List<CatalogBulkPrice> getCatalogBulkPriceList(CatalogBulkPrice entity, Integer startRow, Integer rowSize) {
		return getCatalogBulkPriceList(entity, startRow, rowSize, null, null);
	}
	
	public List<CatalogBulkPrice> getCatalogBulkPriceList(CatalogBulkPrice entity, String fieldName, OrderTypeEnum orderType) {
		return getCatalogBulkPriceList(entity, null, null, fieldName, orderType);
	}
	
	public List<CatalogBulkPrice> getCatalogBulkPriceList(CatalogBulkPrice entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogBulkPrice> criteria = builder.createQuery( CatalogBulkPrice.class );
        Root<CatalogBulkPrice> entityRoot = criteria.from( CatalogBulkPrice.class );
        
        criteria.select( entityRoot );
        
        Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);
        
        criteria.where(whereClause);
                
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<CatalogBulkPrice> entityRoot, CatalogBulkPrice entity) {
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
	
	public List<CatalogBulkPrice> getCatalogBulkPriceListById(Integer id) {
		Query q = em.createNamedQuery("CatalogBulkPrice.getCatalogBulkPriceListById");
		q.setParameter("id",id);
		return q.getResultList();
	}
	
	public CatalogBulkPrice insertCatalogBulkPrice(CatalogBulkPrice entity, Token token) {
		
		entity.setIsDelete(0);
		entity.setCreated(new Date());
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogBulkPrice updateCatalogBulkPrice(CatalogBulkPrice entity, Token token) {
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogBulkPrice deleteCatalogBulkPrice(int id, Token token) {
		CatalogBulkPrice entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogBulkPrice deleteRowCatalogBulkPrice(int id, Token token) {
		CatalogBulkPrice entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	public Integer deleteCatalogBulkPriceList(int catalogId) {
		Query query = em.createQuery("delete from CatalogBulkPrice cbk where cbk.catalog.id = :catalogId ");
		query.setParameter("catalogId", catalogId);
		return query.executeUpdate();
	}
	
}
