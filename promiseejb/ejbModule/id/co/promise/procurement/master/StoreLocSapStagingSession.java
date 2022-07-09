package id.co.promise.procurement.master;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import id.co.promise.procurement.entity.StoreLocSap;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class StoreLocSapStagingSession extends AbstractFacadeWithAudit<StoreLocSap> {
	/*
	 * @PersistenceContext(unitName = "promiseStaging") private EntityManager
	 * emStaging;
	 * 
	 * @PersistenceContext(unitName = "promiseStagingPU") private EntityManager ema;
	 */
	
	public StoreLocSapStagingSession() {
		super(StoreLocSap.class);
	}

	public StoreLocSap findId(int id) {
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
	 * List<StoreLocSap> getStagingList() { Query q = emStaging.
	 * createQuery("SELECT storeLocSap FROM StoreLocSap storeLocSap Where storeLocSap.isDelete = 0"
	 * ); List<StoreLocSap> storeLocSapStagingList = q.getResultList(); return
	 * storeLocSapStagingList; }
	 * 
	 * @Override protected EntityManager getEntityManager() { return emStaging; }
	 * 
	 * @Override protected EntityManager getEntityManagerAudit() { // TODO
	 * Auto-generated method stub return ema; }
	 */
	

}
