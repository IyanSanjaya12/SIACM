package id.co.promise.procurement.master;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import id.co.promise.procurement.entity.GLAccountSap;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class GLAccountSapStagingSession extends AbstractFacadeWithAudit<GLAccountSap> {
	/*
	 * @PersistenceContext(unitName = "promiseStaging") private EntityManager
	 * emStaging;
	 * 
	 * @PersistenceContext(unitName = "promiseStagingPU") private EntityManager ema;
	 */
	
	public GLAccountSapStagingSession() {
		super(GLAccountSap.class);
	}

	public GLAccountSap findId(int id) {
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
	 * List<GLAccountSap> getStagingList() { Query q = emStaging.
	 * createQuery("SELECT gLAccountSap FROM GLAccountSap gLAccountSap Where gLAccountSap.isDelete = 0"
	 * ); List<GLAccountSap> gLAccountSapStagingList = q.getResultList(); return
	 * gLAccountSapStagingList; }
	 * 
	 * @Override protected EntityManager getEntityManager() { return emStaging; }
	 * 
	 * @Override protected EntityManager getEntityManagerAudit() { // TODO
	 * Auto-generated method stub return ema; }
	 */
	

}
