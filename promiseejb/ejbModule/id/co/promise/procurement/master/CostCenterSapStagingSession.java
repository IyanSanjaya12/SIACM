package id.co.promise.procurement.master;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import id.co.promise.procurement.entity.CostCenterSap;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class CostCenterSapStagingSession extends AbstractFacadeWithAudit<CostCenterSap> {
	/*
	 * @PersistenceContext(unitName = "promiseStaging") private EntityManager
	 * emStaging;
	 * 
	 * @PersistenceContext(unitName = "promiseStagingPU") private EntityManager ema;
	 */
	
	public CostCenterSapStagingSession() {
		super(CostCenterSap.class);
	}

	public CostCenterSap findId(int id) {
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
	 * List<CostCenterSap> getStagingList() { Query q = emStaging.
	 * createQuery("SELECT costCenter FROM CostCenterSap costCenter Where costCenter.isDelete = 0"
	 * ); List<CostCenterSap> costCenterSapStagingList = q.getResultList(); return
	 * costCenterSapStagingList; }
	 * 
	 * @Override protected EntityManager getEntityManager() { return emStaging; }
	 * 
	 * @Override protected EntityManager getEntityManagerAudit() { // TODO
	 * Auto-generated method stub return ema; }
	 */
	

}
