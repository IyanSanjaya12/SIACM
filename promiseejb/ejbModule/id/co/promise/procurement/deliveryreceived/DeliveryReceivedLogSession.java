package id.co.promise.procurement.deliveryreceived;

import id.co.promise.procurement.entity.DeliveryReceivedLog;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

/**
 * @author F.H.K
 *
 */

@Stateless
@LocalBean
public class DeliveryReceivedLogSession extends AbstractFacadeWithAudit<DeliveryReceivedLog> {
	final static Logger log = Logger.getLogger(DeliveryReceivedLogSession.class);
	
	public DeliveryReceivedLogSession() {
		super(DeliveryReceivedLog.class);
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
	
	public DeliveryReceivedLog find(Object id) {
		CriteriaBuilder builder 					= getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryReceivedLog> criteria = builder.createQuery( DeliveryReceivedLog.class );
        Root<DeliveryReceivedLog> entityRoot 		= criteria.from( DeliveryReceivedLog.class );
        
        criteria.select( entityRoot);
        entityRoot.fetch("deliveryReceived", JoinType.LEFT);
        criteria.where(builder.equal(entityRoot.get("id"), id));
        
        try {
        	List<DeliveryReceivedLog> DeliveryReceivedLogList =  em.createQuery(criteria).getResultList();
        	if (DeliveryReceivedLogList != null && DeliveryReceivedLogList.size() > 0) {
        		return DeliveryReceivedLogList.get(0);
        	}
        } catch (Exception ex) {
            return null;
        }
        return null;        
	}
	
	public List<DeliveryReceivedLog> getDeliveryReceivedLogByCriteriaPaging(DeliveryReceivedLog entity, int pageNumber, int pageSize) {
		CriteriaBuilder builder 					= getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryReceivedLog> criteria = builder.createQuery( DeliveryReceivedLog.class );
        Root<DeliveryReceivedLog> entityRoot 		= criteria.from( DeliveryReceivedLog.class );
        
        criteria.select( entityRoot );
        entityRoot.fetch("deliveryReceived", JoinType.LEFT);
        Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);
        criteria.where(whereClause);
        try {
            return getEntityManager().createQuery(criteria).setFirstResult((pageNumber-1)  * pageSize).setMaxResults(pageSize).getResultList();
        } catch (Exception ex) {
            return null;
        }
	}
	
	public List<DeliveryReceivedLog> getDeliveryReceivedLogByCriteria(DeliveryReceivedLog entity) {
		CriteriaBuilder builder 					= getEntityManager().getCriteriaBuilder();
        CriteriaQuery<DeliveryReceivedLog> criteria = builder.createQuery( DeliveryReceivedLog.class );
        Root<DeliveryReceivedLog> entityRoot 		= criteria.from( DeliveryReceivedLog.class );
        
        criteria.select( entityRoot );
        entityRoot.fetch("deliveryReceived", JoinType.LEFT);
        Predicate whereClause = setDefaultPredicate(builder, entityRoot, entity);
        criteria.where(whereClause);
        try {
            return getEntityManager().createQuery(criteria).getResultList();
        } catch (Exception ex) {
            return null;
        }
	}
	
	private Predicate setDefaultPredicate(CriteriaBuilder builder, Root<DeliveryReceivedLog> entityRoot, DeliveryReceivedLog entity) {
		Predicate whereClause = builder.conjunction();
        if (entity != null) {
        	if (entity.getDeliveryReceived() != null) {
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("deliveryReceived").get("id"), entity.getDeliveryReceived().getId()));
            }if (entity.getDateReceived() != null) {
                whereClause = builder.and(whereClause, builder.equal(entityRoot.get("dateReceived").get("id"), entity.getDateReceived()));
            }if (entity.getIsDelete() != null) {
				whereClause = builder.and(whereClause, builder.equal(entityRoot.get("isDelete"), entity.getIsDelete()));
			}
        }
        return whereClause;
	}
	
	public DeliveryReceivedLog insertDeliveryReceivedLog(DeliveryReceivedLog deliveryReceivedLog,Token token) {
		deliveryReceivedLog.setDateReceived(deliveryReceivedLog.getDateReceived());
		deliveryReceivedLog.setIsDelete(0);
		super.create(deliveryReceivedLog, AuditHelper.OPERATION_CREATE, token);
		return deliveryReceivedLog;
	}

	public DeliveryReceivedLog updateDeliveryReceivedLog(DeliveryReceivedLog deliveryReceivedLog,Token token) {
		super.edit(deliveryReceivedLog, AuditHelper.OPERATION_UPDATE, token);
		return deliveryReceivedLog;
	}

	public DeliveryReceivedLog deleteDeliveryReceivedLog(int id, Token token) {
		DeliveryReceivedLog deliveryReceivedLog = super.find(id);
		deliveryReceivedLog.setIsDelete(1);
		super.edit(deliveryReceivedLog, AuditHelper.OPERATION_DELETE, token);
		return deliveryReceivedLog;
	}

	public DeliveryReceivedLog deleteRowDeliveryReceivedLog(int id, Token token) {
		DeliveryReceivedLog deliveryReceivedLog = super.find(id);
		super.remove(deliveryReceivedLog, AuditHelper.OPERATION_ROW_DELETE, token);
		return deliveryReceivedLog;
	}
	
	//count Received Item
		public Integer getTotalPassItemLog(Integer deliveryReceivedId) {
			Query q =em.createNamedQuery("DeliveryReceivedLog.getTotalPassItemLog");
			q.setParameter("deliveryReceivedId", deliveryReceivedId);
			try{
				String jumlahStr = q.getSingleResult().toString();
				Double jumlahDbl = Double.parseDouble(jumlahStr);
				Integer jumlah = jumlahDbl.intValue();
				return jumlah;
				
			}catch(Exception e){
				log.error("error sum "+ e);
				/*e.printStackTrace();*/
				return new Integer(0);
			}
			
		}
	
	//count Received Item
	public Integer getSumPassedItem(Integer poId) {
		Query q = em.createQuery("select sum(drl.pass) from DeliveryReceivedLog drl where drl.deliveryReceived.id in "
				+ "(select dr.id from DeliveryReceived dr where dr.purchaseOrder.id = :poId)");
		q.setParameter("poId", poId);
		try{
			String jumlahStr = q.getSingleResult().toString();
			Double jumlahDbl = Double.parseDouble(jumlahStr);
			Integer jumlah = jumlahDbl.intValue();
			return jumlah;
			
		}catch(Exception e){
			log.error("error sum "+ e);
			/*e.printStackTrace();*/
			return new Integer(0);
		}
		
	}
}
