package id.co.promise.procurement.email;

import id.co.promise.procurement.entity.MasterEmailNotification;
import id.co.promise.procurement.utils.AbstractFacade;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class MasterEmailNotificationVer02Session extends AbstractFacade<MasterEmailNotification> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	public MasterEmailNotificationVer02Session() {
		super(MasterEmailNotification.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
