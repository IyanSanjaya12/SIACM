package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.InputType;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.StaticProperties;
import id.co.promise.procurement.utils.StaticUtility;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
@LocalBean
public class InputTypeSession extends AbstractFacadeWithAudit<InputType> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public InputTypeSession() {
		super(InputType.class);
	}

	public InputType find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<InputType> criteria = builder.createQuery( InputType.class );
        Root<InputType> entityRoot = criteria.from( InputType.class );
        
        criteria.select( entityRoot );
        
        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        List<InputType> inputTypeList = em.createQuery(criteria).getResultList();
        if (inputTypeList != null && inputTypeList.size() > 0) {
        	return inputTypeList.get(0);
        }
        return null;
	}
	
	public List<InputType> getInputTypeList() {
		return getInputTypeList(null, null, null, null, null);
	}
	
	public List<InputType> getInputTypeList(InputType entity) {
		return getInputTypeList(entity, null, null, null, null);
	}
	
	public List<InputType> getInputTypeList(InputType entity, Integer startRow, Integer rowSize) {
		return getInputTypeList(entity, startRow, rowSize, null, null);
	}
	
	public List<InputType> getInputTypeList(InputType entity, String fieldName, OrderTypeEnum orderType) {
		return getInputTypeList(entity, null, null, fieldName, orderType);
	}
	
	public List<InputType> getInputTypeList(InputType entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<InputType> criteria = builder.createQuery( InputType.class );
        Root<InputType> entityRoot = criteria.from( InputType.class );
        
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<InputType> entityRoot, InputType entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getName() != null && entity.getName().equals(StaticProperties.BLANK) == false) {
                whereClause = builder.and(whereClause, builder.like(entityRoot.get("name").as(String.class), StaticUtility.setStringMatchAnywhere(entity.getName())));
            }
            if (entity.getDescription() != null && entity.getDescription().equals(StaticProperties.BLANK) == false) {
                whereClause = builder.and(whereClause, builder.like(entityRoot.get("description").as(String.class), StaticUtility.setStringMatchAnywhere(entity.getDescription())));
            }
        }
        whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
        
        return whereClause;
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
