package id.co.promise.procurement.master;

import java.util.List;

import id.co.promise.procurement.entity.Asosiasi;
import id.co.promise.procurement.utils.AbstractFacade;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class AsosiasiSession extends AbstractFacade<Asosiasi> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	public AsosiasiSession() {
		super(Asosiasi.class);
	}

	public Asosiasi getAsosiasi(int id){
		return super.find(id);
	}
	
	public List<Asosiasi> getAsosiasiList(){
		Query q = em.createNamedQuery("Asosiasi.find");
		return q.getResultList();
	}
	
	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

}
