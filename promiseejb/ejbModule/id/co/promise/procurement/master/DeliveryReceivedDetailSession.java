package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.DeliveryReceivedDetail;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class DeliveryReceivedDetailSession extends AbstractFacadeWithAudit<DeliveryReceivedDetail>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public DeliveryReceivedDetailSession() {
		super(DeliveryReceivedDetail.class);
	}

	public DeliveryReceivedDetail getItem(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedDetail> getDeliveryReceivedDetailList() {
		Query q = em.createNamedQuery("DeliveryReceivedDetail.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public DeliveryReceivedDetail getbyPOLineId(Integer poLineId, Integer deliveryReceivedId) {
		Query q = em.createNamedQuery("DeliveryReceivedDetail.findByPOLineId");
		q.setParameter("poLineId", poLineId)
		 .setParameter("deliveryReceivedId", deliveryReceivedId);
		List<DeliveryReceivedDetail> deliveryReceivedDetailList = q.getResultList();
		if (deliveryReceivedDetailList != null && deliveryReceivedDetailList.size() > 0) {
			return deliveryReceivedDetailList.get(0);
		}else {			
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceivedDetail> getbyDeliveryReceivedId(Integer deliveryReceivedId) {
		Query q = em.createNamedQuery("DeliveryReceivedDetail.findByDeliveryId");
		q.setParameter("deliveryReceivedId", deliveryReceivedId);
		return q.getResultList();
	}
	
	public DeliveryReceivedDetail insertDeliveryReceivedDetail(DeliveryReceivedDetail deliveryReceivedDetail, Token token) {
		deliveryReceivedDetail.setCreated(new Date());
		deliveryReceivedDetail.setIsDelete(0);
		super.create(deliveryReceivedDetail, AuditHelper.OPERATION_CREATE, token);
		return deliveryReceivedDetail;
	}

	public DeliveryReceivedDetail updateDeliveryReceivedDetail(DeliveryReceivedDetail deliveryReceivedDetail, Token token) {
		deliveryReceivedDetail.setUpdated(new Date());
		deliveryReceivedDetail.setIsDelete(0);
		super.edit(deliveryReceivedDetail, AuditHelper.OPERATION_UPDATE, token);
		return deliveryReceivedDetail;
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
