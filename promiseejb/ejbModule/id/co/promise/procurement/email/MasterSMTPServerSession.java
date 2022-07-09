package id.co.promise.procurement.email;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.MasterSMTPServer;
import id.co.promise.procurement.utils.AbstractFacade;

@Stateless
@LocalBean
public class MasterSMTPServerSession extends AbstractFacade<MasterSMTPServer> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	public MasterSMTPServerSession() {
		super(MasterSMTPServer.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MasterSMTPServer> getMasterSMTPServerList(){
		Query q = em.createNamedQuery("MasterSMTPServer.find");
		return q.getResultList();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
