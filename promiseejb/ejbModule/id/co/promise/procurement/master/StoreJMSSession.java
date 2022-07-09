package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.StoreJMS;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class StoreJMSSession extends AbstractFacadeWithAudit<StoreJMS> {
	
	public StoreJMSSession(){
		super(StoreJMS.class);
	}
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public StoreJMS getStoreJMS(int id){
		return em.find(StoreJMS.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<StoreJMS> getStoreJMSList(){
		List<StoreJMS> results = (List<StoreJMS>) em.createQuery("SELECT t1 FROM StoreJMS t1 ORDER BY t1.storeJmsPk DESC ").getResultList();
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<StoreJMS> getStoreJMSListStatusFinalPurchaseOrderNewFailed(){
		List <Integer> statusProsesList = new ArrayList<Integer>();
		statusProsesList.add(Constant.ZERO_VALUE);
		statusProsesList.add(Constant.TWO_VALUE);
		List<StoreJMS> results = (List<StoreJMS>) em.createQuery("SELECT t1 FROM StoreJMS t1 WHERE t1.storeJmsStat IN(:statusProsesList) "
				+ " AND t1.storeJmsModule = :storeJmsModule "
				+ " ORDER BY t1.storeJmsPk DESC ")
				.setParameter("storeJmsModule",Constant.JMS_FINAL_PO)
				.setParameter("statusProsesList", statusProsesList).getResultList();
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<StoreJMS> getStoreJMSListStatusFinalContractItemNewFailed(){
		List <Integer> statusProsesList = new ArrayList<Integer>();
		statusProsesList.add(Constant.ZERO_VALUE);
		statusProsesList.add(Constant.TWO_VALUE);
		List<StoreJMS> results = (List<StoreJMS>) em.createQuery("SELECT t1 FROM StoreJMS t1 WHERE t1.storeJmsStat IN(:statusProsesList) "
				+ " AND t1.storeJmsModule = :storeJmsModule "
				+ " ORDER BY t1.storeJmsPk DESC ")
				.setParameter("storeJmsModule",Constant.JMS_FINAL_CONTRACT_ITEM)
				.setParameter("statusProsesList", statusProsesList).getResultList();
		return results;
	}
	
	public StoreJMS insertStoreJMS(StoreJMS storeJMS, Token token){
		super.create(storeJMS, AuditHelper.OPERATION_CREATE, token);
		return storeJMS;
	}
	
	public StoreJMS updateStoreJMS(StoreJMS storeJMS, Token token){
		super.edit(storeJMS, AuditHelper.OPERATION_UPDATE, token);
		return storeJMS;				
	}	
	
	public void deleteStoreJMS(Integer id, Token token){
		StoreJMS storeJMS = em.find(StoreJMS.class, id);
		super.remove(storeJMS, AuditHelper.OPERATION_DELETE, token);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

}
