package id.co.promise.procurement.master;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import id.co.promise.procurement.entity.ThresholdRange;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class ThresholdRangeSession extends AbstractFacadeWithAudit<ThresholdRange> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public ThresholdRangeSession() {
		super(ThresholdRange.class);
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

}
