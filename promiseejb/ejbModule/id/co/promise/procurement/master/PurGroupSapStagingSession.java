package id.co.promise.procurement.master;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import id.co.promise.procurement.entity.PurGroupSap;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class PurGroupSapStagingSession extends AbstractFacadeWithAudit<PurGroupSap> {
	/*
	 * @PersistenceContext(unitName = "promiseStaging") private EntityManager
	 * emStaging;
	 * 
	 * @PersistenceContext(unitName = "promiseStagingPU") private EntityManager ema;
	 */
	
	public PurGroupSapStagingSession() {
		super(PurGroupSap.class);
	}

	public PurGroupSap findId(int id) {
		return super.find(id);
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) public
	 * List<PurGroupSap> getStagingList() { Query q = emStaging.
	 * createQuery("SELECT purGroup FROM PurGroupSap purGroup Where purGroup.isDelete = 0"
	 * ); List<PurGroupSap> costCenterSapStagingList = q.getResultList(); return
	 * costCenterSapStagingList; }
	 * 
	 * @Override protected EntityManager getEntityManager() { return emStaging; }
	 * 
	 * @Override protected EntityManager getEntityManagerAudit() { // TODO
	 * Auto-generated method stub return ema; }
	 */
	

}
