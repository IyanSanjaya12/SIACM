package id.co.promise.procurement.master;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import id.co.promise.procurement.entity.DeliveryReceivedFiles;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class DeliveryReceivedFilesSession extends AbstractFacadeWithAudit<DeliveryReceivedFiles>{
	
	public DeliveryReceivedFilesSession() {
		super(DeliveryReceivedFiles.class);
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
	
	public DeliveryReceivedFiles insertDeliveryReceivedFiles(DeliveryReceivedFiles deliveryReceivedFiles, Token token) {
		deliveryReceivedFiles.setCreated(new Date());
		deliveryReceivedFiles.setIsDelete(0);
		super.create(deliveryReceivedFiles, AuditHelper.OPERATION_CREATE, token);
		return deliveryReceivedFiles;
	}

	public DeliveryReceivedFiles updateDeliveryReceivedFiles(DeliveryReceivedFiles deliveryReceivedFiles, Token token) {
		deliveryReceivedFiles.setUpdated(new Date());
		deliveryReceivedFiles.setIsDelete(0);
		super.edit(deliveryReceivedFiles, AuditHelper.OPERATION_UPDATE, token);
		return deliveryReceivedFiles;
	}
}
