package id.co.promise.procurement.master;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.StoreLocSap;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class StoreLocSapSession extends AbstractFacadeWithAudit<StoreLocSap> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	StoreLocSapStagingSession storeLocSapStagingSession;

	public StoreLocSapSession() {
		super(StoreLocSap.class);
	}

	public StoreLocSap findId(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<StoreLocSap> getList() {
		Query q = em.createNamedQuery("StoreLocSap.find");
		return q.getResultList();
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
	
	public void getSyncList() {

		List<StoreLocSap> storeLocSapStagingList  = new ArrayList<>();//storeLocSapStagingSession.getStagingList();

		List<String> tempCodeList = new ArrayList<>();
		Token token = new Token();
		
		//kalau sudah ada, di update. kalau tidak ada di, insert
		for (StoreLocSap storeLocSapStaging : storeLocSapStagingList ) {
			tempCodeList.add(storeLocSapStaging.getCode());
			StoreLocSap cekStoreLocSap = getByCode(storeLocSapStaging.getCode());
			
			StoreLocSap storeLocSapNew = new StoreLocSap();
			
			if (cekStoreLocSap != null ) {
				storeLocSapNew.setId(cekStoreLocSap.getId());
				storeLocSapNew.setCreated(cekStoreLocSap.getCreated());
				storeLocSapNew.setCode(storeLocSapStaging.getCode());
				storeLocSapNew.setDescription(storeLocSapStaging.getDescription());
				update(storeLocSapNew, token);
			} else {
				storeLocSapNew.setCreated(new Date());
				storeLocSapNew.setCode(storeLocSapStaging.getCode());
				storeLocSapNew.setDescription(storeLocSapStaging.getDescription());
				insert(storeLocSapNew, token);
			}
		}
		
		//softdelete yang tidak ada di staging
		deleteByCodeList(tempCodeList);
	}
	
	@SuppressWarnings("unchecked")
	public StoreLocSap getByCode(String code) {
		Query q = em.createQuery("SELECT storeLocSap FROM StoreLocSap storeLocSap Where storeLocSap.isDelete = 0 AND storeLocSap.code =:code");
		q.setParameter("code", code);
		List<StoreLocSap> storeLocSap = q.getResultList();
		if (storeLocSap != null && storeLocSap.size() > 0) {
			return storeLocSap.get(0);
		}
		return null;
	}
	
	public StoreLocSap insert(StoreLocSap storeLocSap, Token token) {
		storeLocSap.setCreated(new Date());
		if(storeLocSap.getIsDelete() == null) {
			storeLocSap.setIsDelete(0);
		}
		super.create(storeLocSap, AuditHelper.OPERATION_CREATE, token);
		return storeLocSap;
	}
	
	public StoreLocSap update(StoreLocSap storeLocSap, Token token) {
		storeLocSap.setUpdated(new Date());
		if(storeLocSap.getIsDelete() == null) {
			storeLocSap.setIsDelete(0);
		}
		super.edit(storeLocSap, AuditHelper.OPERATION_UPDATE, token);
		return storeLocSap;
	}
	
	public void deleteByCodeList(List<String> tempCodeList) {
		Query q = em.createQuery("UPDATE StoreLocSap storeLocSap SET storeLocSap.isDelete = 1 Where storeLocSap.isDelete = 0 AND storeLocSap.code NOT IN (:codeStagingList)");
		q.setParameter("codeStagingList", tempCodeList);
		q.executeUpdate();	
	}
}
