package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class ProvinsiSession extends AbstractFacadeWithAudit<Provinsi> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public ProvinsiSession() {
		super(Provinsi.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
	
	@SuppressWarnings("unchecked")
	public List<Provinsi> getProvinsiAll() {
		Query q = em.createNamedQuery("Provinsi.findAll");
		return q.getResultList();
	}
	
	public Provinsi getProvinsi(int id) {
		return super.find(id);
	}
}
