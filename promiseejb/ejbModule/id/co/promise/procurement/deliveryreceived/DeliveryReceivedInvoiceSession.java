package id.co.promise.procurement.deliveryreceived;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.DeliveryReceivedInvoice;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
@LocalBean
public class DeliveryReceivedInvoiceSession extends AbstractFacadeWithAudit<DeliveryReceivedInvoice> {

	public DeliveryReceivedInvoiceSession() {
		super(DeliveryReceivedInvoice.class);
	}

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
	
	public DeliveryReceivedInvoice find(Object id) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryReceivedInvoice> criteria = builder.createQuery( DeliveryReceivedInvoice.class );
        Root<DeliveryReceivedInvoice> entityRoot = criteria.from( DeliveryReceivedInvoice.class );
        
        criteria.select( entityRoot);
        
        entityRoot.fetch("purchaseOrder", JoinType.LEFT);

        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<DeliveryReceivedInvoice> DeliveryReceivedInvoiceList =  em.createQuery(criteria).getResultList();
        	if (DeliveryReceivedInvoiceList != null && DeliveryReceivedInvoiceList.size() > 0) {
        		return DeliveryReceivedInvoiceList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<DeliveryReceivedInvoice> getDeliveryReceivedInvoiceByCriteriaPaging(DeliveryReceivedInvoice entity, int pageNumber, int pageSize) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryReceivedInvoice> criteria = builder.createQuery( DeliveryReceivedInvoice.class );
        Root<DeliveryReceivedInvoice> entityRoot = criteria.from( DeliveryReceivedInvoice.class );
        
        criteria.select( entityRoot );
        
        entityRoot.fetch("purchaseOrder", JoinType.LEFT);
        
        Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);
        
        criteria.where(whereClause);
        
        try {
            return getEntityManager().createQuery(criteria)
            		.setFirstResult((pageNumber-1)  * pageSize)
            		.setMaxResults(pageSize).getResultList();
        } catch (Exception ex) {
            return null;
        }
	}
	
	public List<DeliveryReceivedInvoice> getDeliveryReceivedInvoiceByCriteria(DeliveryReceivedInvoice entity) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryReceivedInvoice> criteria = builder.createQuery( DeliveryReceivedInvoice.class );
        Root<DeliveryReceivedInvoice> entityRoot = criteria.from( DeliveryReceivedInvoice.class );
        
        criteria.select( entityRoot );
        
        entityRoot.fetch("purchaseOrder", JoinType.LEFT);
        
        Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);
        
        criteria.where(whereClause);
        
        try {
            return getEntityManager().createQuery(criteria).getResultList();
        } catch (Exception ex) {
            return null;
        }
	}
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<DeliveryReceivedInvoice> entityRoot, DeliveryReceivedInvoice entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getPurchaseOrder() != null) {
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("purchaseOrder").get("id"), entity.getPurchaseOrder().getId()));
            }
        	
        	if (entity.getDateReceived() != null) {
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("dateReceived"), entity.getDateReceived()));
            }
        	
            if (entity.getIsDelete() != null) {
				whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), entity.getIsDelete()));
			}
        }
        
        return whereClause;
	}
	
	public DeliveryReceivedInvoice insertDeliveryReceivedInvoice(DeliveryReceivedInvoice deliveryReceivedInvoice,
			Token token) {
		deliveryReceivedInvoice.setDateReceived(new Date());
		deliveryReceivedInvoice.setIsDelete(0);
		super.create(deliveryReceivedInvoice, AuditHelper.OPERATION_CREATE, token);
		return deliveryReceivedInvoice;
	}

	public DeliveryReceivedInvoice updateDeliveryReceivedInvoice(DeliveryReceivedInvoice deliveryReceivedInvoice,
			Token token) {
		super.edit(deliveryReceivedInvoice, AuditHelper.OPERATION_UPDATE, token);
		return deliveryReceivedInvoice;
	}

	public DeliveryReceivedInvoice deleteDeliveryReceivedInvoice(int id, Token token) {
		DeliveryReceivedInvoice deliveryReceivedInvoice = super.find(id);
		deliveryReceivedInvoice.setIsDelete(1);
		super.edit(deliveryReceivedInvoice, AuditHelper.OPERATION_DELETE, token);
		return deliveryReceivedInvoice;
	}

	public DeliveryReceivedInvoice deleteRowDeliveryReceivedInvoice(int id, Token token) {
		DeliveryReceivedInvoice deliveryReceivedInvoice = super.find(id);
		super.remove(deliveryReceivedInvoice, AuditHelper.OPERATION_ROW_DELETE, token);
		return deliveryReceivedInvoice;
	}
}
