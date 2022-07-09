package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.AttributeGroup;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.StaticProperties;
import id.co.promise.procurement.utils.StaticUtility;
import id.co.promise.procurement.utils.audit.AuditHelper;

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

@Stateless
@LocalBean
public class AttributeGroupSession extends AbstractFacadeWithAudit<AttributeGroup> {
	
	@EJB private AttributeGroupSession attributeGroupSession;
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public AttributeGroupSession() {
		super(AttributeGroup.class);
	}
	
	public AttributeGroup find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AttributeGroup> criteria = builder.createQuery( AttributeGroup.class );
		Root<AttributeGroup> entityRoot = criteria.from( AttributeGroup.class );
		
		criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<AttributeGroup> attributeGroupList =  em.createQuery(criteria).getResultList();
        	if (attributeGroupList != null && attributeGroupList.size() > 0) {
        		return attributeGroupList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;   
	}
	
	public AttributeGroup findAttributeGroup(Integer id) {
		Query q = em.createQuery("SELECT ag FROM AttributeGroup ag WHERE ag.isDelete = 0 AND ag.id = :id");
		q.setParameter("id", id);
		List<AttributeGroup> agList = q.getResultList();
		
		return agList.get(0);
		
	}
	
	
	public List<AttributeGroup> getAttributeGroupList() {
		return getAttributeGroupList(null, null, null, null, null);
	}
	
	public List<AttributeGroup> getAttributeGroupList(AttributeGroup entity) {
		return getAttributeGroupList(entity, null, null, null, null);
	}
	
	public List<AttributeGroup> getAttributeGroupList(AttributeGroup entity, Integer startRow, Integer rowSize) {
		return getAttributeGroupList(entity, startRow, rowSize, null, null);
	}
	
	public List<AttributeGroup> getAttributeGroupList(AttributeGroup entity, String fieldName, OrderTypeEnum orderType) {
		return getAttributeGroupList(entity, null, null, fieldName, orderType);
	}
	
	public List<AttributeGroup> getAttributeGroupList(AttributeGroup entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<AttributeGroup> criteria = builder.createQuery( AttributeGroup.class );
        Root<AttributeGroup> entityRoot = criteria.from( AttributeGroup.class );
        
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
        	ex.printStackTrace();
            return null;
        }
	}
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<AttributeGroup> entityRoot, AttributeGroup entity) {
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
	
	public AttributeGroup insertAttributeGroup(AttributeGroup attributeGroup,
			Token token) {
		attributeGroup.setIsDelete(0);
		super.create(attributeGroup, AuditHelper.OPERATION_CREATE, token);
		return attributeGroup;
	}

	public AttributeGroup updateAttributeGroup(AttributeGroup attributeGroup,
			Token token) {
		super.edit(attributeGroup, AuditHelper.OPERATION_UPDATE, token);
		return attributeGroup;
	}

	public AttributeGroup deleteAttributeGroup(int id, Token token) {
		AttributeGroup attributeGroup = super.find(id);
		attributeGroup.setIsDelete(1);
		super.edit(attributeGroup, AuditHelper.OPERATION_DELETE, token);
		return attributeGroup;
	}

	public AttributeGroup deleteRowAttributeGroup(int id, Token token) {
		AttributeGroup attributeGroup = super.find(id);
		super.remove(attributeGroup, AuditHelper.OPERATION_ROW_DELETE, token);
		return attributeGroup;
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
