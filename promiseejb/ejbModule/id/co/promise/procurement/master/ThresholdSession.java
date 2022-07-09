package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Threshold;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class ThresholdSession extends AbstractFacadeWithAudit<Threshold> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public ThresholdSession() {
		super(Threshold.class);
	}

	@Override
	protected EntityManager getEntityManager() {

		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

	public List<Threshold> getThresholdlist() {
		// TODO Auto-generated method stub
		Query q = em.createNamedQuery("Threshold.find");
		return q.getResultList();
	}

	public List<Threshold> getThresholdlistByType(Integer typeValue) {
		// TODO Auto-generated method stub
				Query q = em.createNamedQuery("Threshold.findByType");
				q.setParameter("type", typeValue);
				return q.getResultList();
	}
}