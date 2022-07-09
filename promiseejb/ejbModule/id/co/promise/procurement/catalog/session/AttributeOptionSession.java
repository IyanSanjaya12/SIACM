package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.AttributeOption;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.StaticProperties;
import id.co.promise.procurement.utils.StaticUtility;
import id.co.promise.procurement.utils.audit.AuditHelper;

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
public class AttributeOptionSession extends AbstractFacadeWithAudit<AttributeOption> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public AttributeOptionSession() {
		super(AttributeOption.class);
	}

	public AttributeOption find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AttributeOption> criteria = builder.createQuery( AttributeOption.class );
        Root<AttributeOption> entityRoot = criteria.from( AttributeOption.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<AttributeOption> attributeTypeValueList =  em.createQuery(criteria).getResultList();
        	if (attributeTypeValueList != null && attributeTypeValueList.size() > 0) {
        		return attributeTypeValueList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<AttributeOption> getAttributeOptionList() {
		return getAttributeOptionList(null, null, null, null, null);
	}
	
	public List<AttributeOption> getAttributeOptionList(AttributeOption entity) {
		return getAttributeOptionList(entity, null, null, null, null);
	}
	
	public List<AttributeOption> getAttributeOptionList(AttributeOption entity, Integer startRow, Integer rowSize) {
		return getAttributeOptionList(entity, startRow, rowSize, null, null);
	}
	
	public List<AttributeOption> getAttributeOptionList(AttributeOption entity, String fieldName, OrderTypeEnum orderType) {
		return getAttributeOptionList(entity, null, null, fieldName, orderType);
	}
	
	public List<AttributeOption> getAttributeOptionList(AttributeOption entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AttributeOption> criteria = builder.createQuery( AttributeOption.class );
        Root<AttributeOption> entityRoot = criteria.from( AttributeOption.class );
        
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
	
	public List<AttributeOption> getAttributeByCriteria(AttributeOption entity) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AttributeOption> criteria = builder.createQuery( AttributeOption.class );
        Root<AttributeOption> entityRoot = criteria.from( AttributeOption.class );
        
        criteria.select( entityRoot );
        
        Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);
        
        criteria.where(whereClause);
        
        try {
            return getEntityManager().createQuery(criteria).getResultList();
        } catch (Exception ex) {
            return null;
        }
	}
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<AttributeOption> entityRoot, AttributeOption entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getName() != null && entity.getName().equals(StaticProperties.BLANK) == false) {
                whereClause = builder.and(whereClause, builder.like(entityRoot.get("name").as(String.class), StaticUtility.setStringMatchAnywhere(entity.getName())));
            }
        }
        
        return whereClause;
	}
	
	public AttributeOption insertAttributeTypeValue(AttributeOption attributeTypeValue,
			Token token) {
		super.create(attributeTypeValue, AuditHelper.OPERATION_CREATE, token);
		return attributeTypeValue;
	}

	public AttributeOption updateAttributeTypeValue(AttributeOption attributeTypeValue,
			Token token) {
		super.edit(attributeTypeValue, AuditHelper.OPERATION_UPDATE, token);
		return attributeTypeValue;
	}

	public AttributeOption deleteRowAttributeTypeValue(int id, Token token) {
		AttributeOption attributeTypeValue = super.find(id);
		super.remove(attributeTypeValue, AuditHelper.OPERATION_ROW_DELETE, token);
		return attributeTypeValue;
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
