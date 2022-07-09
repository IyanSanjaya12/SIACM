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

import id.co.promise.procurement.entity.PurGroupSap;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class PurGroupSapSession extends AbstractFacadeWithAudit<PurGroupSap> {
	
	@EJB
	PurGroupSapStagingSession purGroupSapStagingSession;
	
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PurGroupSapSession() {
		super(PurGroupSap.class);
	}

	public PurGroupSap findId(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<PurGroupSap> getList() {
		Query q = em.createNamedQuery("PurGroupSap.find");
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

		List<PurGroupSap> purGroupSapStagingList  = new ArrayList<>();// purGroupSapStagingSession.getStagingList();

		List<String> tempCodeList = new ArrayList<>();
		Token token = new Token();
		
		//kalau sudah ada, di update. kalau tidak ada di, insert
		for (PurGroupSap purGroupSapStaging : purGroupSapStagingList ) {
			tempCodeList.add(purGroupSapStaging.getCode());
			PurGroupSap cekPurGroupSap = getByCode(purGroupSapStaging.getCode());
			
			PurGroupSap purGroupSapNew = new PurGroupSap();
			
			if (cekPurGroupSap != null ) {
				purGroupSapNew.setId(cekPurGroupSap.getId());
				purGroupSapNew.setCreated(cekPurGroupSap.getCreated());
				purGroupSapNew.setCode(purGroupSapStaging.getCode());
				purGroupSapNew.setDescription(purGroupSapStaging.getDescription());
				update(purGroupSapNew, token);
			} else {
				purGroupSapNew.setCreated(new Date());
				purGroupSapNew.setCode(purGroupSapStaging.getCode());
				purGroupSapNew.setDescription(purGroupSapStaging.getDescription());
				insert(purGroupSapNew, token);
			}
		}
		
		//softdelete yang tidak ada di staging
		deleteByCodeList(tempCodeList);
	}
	
	@SuppressWarnings("unchecked")
	public PurGroupSap getByCode(String code) {
		Query q = em.createQuery("SELECT purGroupSap FROM PurGroupSap purGroupSap Where purGroupSap.isDelete = 0 AND purGroupSap.code =:code");
		q.setParameter("code", code);
		List<PurGroupSap> purGroupSap = q.getResultList();
		if (purGroupSap != null && purGroupSap.size() > 0) {
			return purGroupSap.get(0);
		}
		return null;
	}
	
	public PurGroupSap insert(PurGroupSap purGroup, Token token) {
		purGroup.setCreated(new Date());
		if(purGroup.getIsDelete() == null) {
			purGroup.setIsDelete(0);
		}
		super.create(purGroup, AuditHelper.OPERATION_CREATE, token);
		return purGroup;
	}
	
	public PurGroupSap update(PurGroupSap purGroup, Token token) {
		purGroup.setUpdated(new Date());
		if(purGroup.getIsDelete() == null) {
			purGroup.setIsDelete(0);
		}
		super.edit(purGroup, AuditHelper.OPERATION_UPDATE, token);
		return purGroup;
	}
	
	public void deleteByCodeList(List<String> tempCodeList) {
		Query q = em.createQuery("UPDATE PurGroupSap purGroup SET purGroup.isDelete = 1 Where purGroup.isDelete = 0 AND purGroup.code NOT IN (:codeStagingList)");
		q.setParameter("codeStagingList", tempCodeList);
		q.executeUpdate();	
	}
}
