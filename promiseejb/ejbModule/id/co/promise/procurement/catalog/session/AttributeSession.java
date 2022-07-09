package id.co.promise.procurement.catalog.session;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.Attribute;
import id.co.promise.procurement.catalog.entity.AttributeGroup;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.StaticProperties;
import id.co.promise.procurement.utils.StaticUtility;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
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
public class AttributeSession extends AbstractFacadeWithAudit<Attribute> {
	
	@EJB
	private AttributeGroupSession attributeGroupSession;
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public AttributeSession() {
		super(Attribute.class);
	}
	
	public Attribute find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Attribute> criteria = builder.createQuery( Attribute.class );
        Root<Attribute> entityRoot = criteria.from( Attribute.class );
        
        criteria.select( entityRoot);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<Attribute> attributeList =  em.createQuery(criteria).getResultList();
        	if (attributeList != null && attributeList.size() > 0) {
        		return attributeList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<Attribute> getAttributeList() {
		return getAttributeList(null, null, null, null, null);
	}
	
	public List<Attribute> getAttributeList(Attribute entity) {
		return getAttributeList(entity, null, null, null, null);
	}
	
	public List<Attribute> getAttributeList(Attribute entity, Integer startRow, Integer rowSize) {
		return getAttributeList(entity, startRow, rowSize, null, null);
	}
	
	public List<Attribute> getAttributeList(Attribute entity, String fieldName, OrderTypeEnum orderType) {
		return getAttributeList(entity, null, null, fieldName, orderType);
	}
	
	public List<Attribute> getAttributeList(Attribute entity, Integer startRow, Integer rowSize, String fieldName, OrderTypeEnum orderType) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Attribute> criteria = builder.createQuery( Attribute.class );
        Root<Attribute> entityRoot = criteria.from( Attribute.class );
        
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
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<Attribute> entityRoot, Attribute entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
            if (entity.getName() != null && entity.getName().equals(StaticProperties.BLANK) == false) {
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("name"), entity.getName()));
            }
            if (entity.getInputType() != null) {
                whereClause = builder.and(whereClause, builder.like(entityRoot.join("inputType").get("name").as(String.class), StaticUtility.setStringMatchAnywhere(entity.getInputType().getName())));
            }
            if (entity.getRequired() != null) {
            	whereClause = builder.and(whereClause, builder.equal(entityRoot.get("required"), entity.getRequired()));
            }
            if (entity.getUnique() != null) {
            	whereClause = builder.and(whereClause, builder.equal(entityRoot.get("unique"), entity.getUnique()));
            }
            if (entity.getSearchable() != null) {
            	whereClause = builder.and(whereClause, builder.equal(entityRoot.get("searchable"), entity.getSearchable()));
            }
            if (entity.getSortable() != null) {
            	whereClause = builder.and(whereClause, builder.equal(entityRoot.get("sortable"), entity.getSortable()));
            }
            if (entity.getTranslateInd() != null && entity.getTranslateInd().equals(StaticProperties.BLANK) == false) {
                whereClause = builder.and(whereClause, builder.like(entityRoot.get("translateInd").as(String.class), StaticUtility.setStringMatchAnywhere(entity.getTranslateInd())));
            }
            if (entity.getTranslateEng() != null && entity.getTranslateEng().equals(StaticProperties.BLANK) == false) {
                whereClause = builder.and(whereClause, builder.like(entityRoot.get("translateEng").as(String.class), StaticUtility.setStringMatchAnywhere(entity.getTranslateEng())));
            }
            if (entity.getFlagEnabled() != null) {
            	whereClause = builder.and(whereClause, builder.equal(entityRoot.get("flagEnabled"), entity.getFlagEnabled()));
            }
        }
        whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), 0));
        
        return whereClause;
	}
	
	@SuppressWarnings("unchecked")
	public List<Attribute> getAttributeListInput(Integer catalogId){
		List<Attribute> list = new ArrayList<Attribute>();
		
		String stringQuery = "SELECT t3 FROM CatalogAttribute t1, AttributePanelGroup t2, Attribute t3 " 
						   + " WHERE t1.attributePanelGroup.id = t2.id "
						   + " AND t1.catalog.id = :catalogId "
						   + " AND t2.attribute.id = t3.id "
						   + " AND t1.isDelete = :isDelete "
						   + " AND t2.isDelete = :isDelete "
						   + " AND t3.isDelete = :isDelete "
						   + " ORDER BY t1.catalog.id ASC ";
		
		Query query = em.createQuery(stringQuery);
		query.setParameter("catalogId", catalogId);
		query.setParameter("isDelete", 0);
		list = query.getResultList();
		
		return list;
	}

	public Attribute insertAttribute(Attribute attribute,
			Token token) {
		attribute.setIsDelete(0);
		super.create(attribute, AuditHelper.OPERATION_CREATE, token);
		return attribute;
	}

	public Attribute updateAttribute(Attribute attribute,
			Token token) {
		super.edit(attribute, AuditHelper.OPERATION_UPDATE, token);
		return attribute;
	}

	public Attribute deleteAttribute(int id, Token token) {
		Attribute attribute = super.find(id);
		attribute.setIsDelete(1);
		super.edit(attribute, AuditHelper.OPERATION_DELETE, token);
		return attribute;
	}

	public Attribute deleteRowAttribute(int id, Token token) {
		Attribute attribute = super.find(id);
		super.remove(attribute, AuditHelper.OPERATION_ROW_DELETE, token);
		return attribute;
	}
	
	@SuppressWarnings({ "unchecked"})
	public List<Attribute> getListPaging(String keyword, Integer start, Integer length, Integer columnOrder, String tipeOrder) {
		
		String strQuery = "SELECT DISTINCT attribute "
				+ " FROM Attribute attribute"
				+ " WHERE attribute.isDelete = :isDelete ";
				if (keyword.length() > 0) {
					strQuery+=" AND attribute.name LIKE :keyword OR attribute.inputType.name = :keyword";
				}
				strQuery+= " ORDER BY attribute.id DESC ";
		Query query = em.createQuery(strQuery);
		query.setParameter("isDelete", Constant.ZERO_VALUES);
		query.setFirstResult(start);
		query.setMaxResults(length);
		
		if (keyword.length() > 0) {
			query.setParameter("keyword", "%"+keyword+"%");
		}
		List<Attribute> attributeList = query.getResultList();
		return attributeList;
	}
	
	public Long getListPagingSize(String keyword) {
		String strQuery = "SELECT COUNT(attribute.id) "
				+ " FROM Attribute attribute"
				+ " WHERE attribute.isDelete = :isDelete ";
				if (keyword.length() > 0) {
					strQuery+=" AND attribute.name LIKE :keyword OR attribute.inputType.name = :keyword";
				}
				strQuery+= " ORDER BY attribute.id DESC ";
		Query query = em.createQuery(strQuery);
		query.setParameter("isDelete", Constant.ZERO_VALUES);
		query.setMaxResults(1);
		if (keyword.length() > 0) {
			query.setParameter("keyword", "%"+keyword+"%");
		}
		
		return (Long) query.getSingleResult();
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
