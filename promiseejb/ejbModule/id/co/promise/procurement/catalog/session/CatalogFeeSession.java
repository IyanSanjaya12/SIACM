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
import id.co.promise.procurement.catalog.entity.CatalogFee;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogFeeSession extends AbstractFacadeWithAudit<CatalogFee>{
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogFeeSession() {
		super(CatalogFee.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public List<CatalogFee> getListCatalogFeeByCatalog(Catalog catalog){
		Query q = em.createNamedQuery("catalogFee.getListByCatalog");
		q.setParameter("catalog", catalog);
		return q.getResultList();	
		
	}
	
	public CatalogFee insertCatalogFee(CatalogFee catalogFee, Token token) {
		catalogFee.setIsDelete(0);
		catalogFee.setCreated(new Date());
		super.create(catalogFee, AuditHelper.OPERATION_CREATE, token);
		return catalogFee;
	}
	
	public CatalogFee updateCatalogFee(CatalogFee catalogFee, Token token) {
		super.edit(catalogFee, AuditHelper.OPERATION_UPDATE, token);
		return catalogFee;
	}

	public CatalogFee deleteCatalogFee(int id, Token token) {
		CatalogFee entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogFee deleteRowCatalogFee(int id, Token token) {
		CatalogFee entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}

	public List<CatalogFee> getCatalogFeeList(CatalogFee entity) {
		return getCatalogFeeList(entity, null, null, null, null);
	}
	
	public List<CatalogFee> getCatalogFeeList(CatalogFee entity, Integer startRow, Integer rowSize) {
		return getCatalogFeeList(entity, startRow, rowSize, null, null);
	}
	
	public List<CatalogFee> getCatalogFeeList(CatalogFee entity, String fieldName, OrderTypeEnum orderType) {
		return getCatalogFeeList(entity, null, null, fieldName, orderType);
	}
	
	public List<CatalogFee> getCatalogFeeList(CatalogFee entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<CatalogFee> criteria = builder.createQuery( CatalogFee.class );
        Root<CatalogFee> entityRoot = criteria.from( CatalogFee.class );
        
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<CatalogFee> entityRoot, CatalogFee entity) {
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

	public List<CatalogFee> getListCatalogFeeByCatalogId(Integer catalogId) {
		Query q = em.createNamedQuery("catalogFee.getListByCatalogId");
		q.setParameter("catalogId", catalogId);
		return q.getResultList();
	}
	
	public CatalogFee getCatalogFeeByCatalogIdAndOrganisasiId(Integer catalogId, Integer organisasiId) {
		Query q = em.createQuery("SELECT cf FROM CatalogFee cf WHERE cf.isDelete = 0 " 
				+ "AND cf.catalog.id = :catalogId "
				+ "AND cf.organisasi.id = :organisasiId ");
		q.setParameter("catalogId", catalogId);
		q.setParameter("organisasiId", organisasiId);
		
		List<CatalogFee> catalogFeeList = q.getResultList();
		try {
			return catalogFeeList.get(0);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}	
	
}
