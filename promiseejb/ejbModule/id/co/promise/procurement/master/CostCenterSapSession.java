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

import id.co.promise.procurement.entity.CostCenterSap;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CostCenterSapSession extends AbstractFacadeWithAudit<CostCenterSap> {

	@EJB 
	CostCenterSapStagingSession costCenterSapStagingSession;
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em1;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema1;
	

	public CostCenterSapSession() {
		super(CostCenterSap.class);
	}

	public CostCenterSap findId(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<CostCenterSap> getList() {
		Query q = em1.createNamedQuery("CostCenterSap.find");
		List<CostCenterSap> costList = q.getResultList();
		return costList;
	}
	
	@SuppressWarnings("unchecked")
	public List<CostCenterSap> getSyncList() {
		List<CostCenterSap> costCenterSapStagingList  = new ArrayList<>();//costCenterSapStagingSession.getStagingList();

		List<String> tempCodeList = new ArrayList<>();
		Token token = new Token();
		
		//kalau sudah ada, di update. kalau tidak ada di, insert
		for (CostCenterSap costCenterSapStaging : costCenterSapStagingList ) {
			tempCodeList.add(costCenterSapStaging.getCode());
			CostCenterSap cekCostCenterSap = getByCode(costCenterSapStaging.getCode());
			
			CostCenterSap costCenterSapNew = new CostCenterSap();
			
			if (cekCostCenterSap != null ) {
				costCenterSapNew.setId(cekCostCenterSap.getId());
				costCenterSapNew.setCreated(cekCostCenterSap.getCreated());
				costCenterSapNew.setCode(costCenterSapStaging.getCode());
				costCenterSapNew.setDescription(costCenterSapStaging.getDescription());
				update(costCenterSapNew, token);
			} else {
				costCenterSapNew.setCreated(new Date());
				costCenterSapNew.setCode(costCenterSapStaging.getCode());
				costCenterSapNew.setDescription(costCenterSapStaging.getDescription());
				insert(costCenterSapNew, token);
			}
		}
		
		//softdelete yang tidak ada di staging
		deleteByCodeList(tempCodeList);

		return costCenterSapStagingList;
	}
	
	public void deleteByCodeList(List<String> tempCodeList) {
		Query q = em1.createQuery("UPDATE CostCenterSap costCenter SET costCenter.isDelete = 1 Where costCenter.isDelete = 0 AND costCenter.code NOT IN (:codeStagingList)");
		q.setParameter("codeStagingList", tempCodeList);
		q.executeUpdate();	
	}
	
	@SuppressWarnings("unchecked")
	public CostCenterSap getByCode(String code) {
		Query q = em1.createQuery("SELECT costCenter FROM CostCenterSap costCenter Where costCenter.isDelete = 0 AND costCenter.code =:code");
		q.setParameter("code", code);
		List<CostCenterSap> costCenterSap = q.getResultList();
		if (costCenterSap != null && costCenterSap.size() > 0) {
			return costCenterSap.get(0);
		}
		return null;
	}
	
	public CostCenterSap insert(CostCenterSap costCenter, Token token) {
		costCenter.setCreated(new Date());
		if(costCenter.getIsDelete() == null) {
			costCenter.setIsDelete(0);
		}
		super.create(costCenter, AuditHelper.OPERATION_CREATE, token);
		return costCenter;
	}
	
	public CostCenterSap update(CostCenterSap costCenter, Token token) {
		costCenter.setUpdated(new Date());
		if(costCenter.getIsDelete() == null) {
			costCenter.setIsDelete(0);
		}
		super.edit(costCenter, AuditHelper.OPERATION_UPDATE, token);
		return costCenter;
	}
	
	public List<CostCenterSap> getListWithPagination(Integer start, Integer length, String keyword, Integer columnOrder, String tipeOrder){
		
		String queryUser = "SELECT costCenterSap FROM CostCenterSap costCenterSap WHERE costCenterSap.isDelete =:isDelete AND "
				+ "(costCenterSap.code like :keyword OR costCenterSap.description like :keyword) ";
		
		String[] columnToView = { "id", "code", "description"};
		if (columnOrder > 0) {
			queryUser+="ORDER BY costCenterSap. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+="ORDER BY costCenterSap.id desc ";
		}
		
		Query query = em1.createQuery(queryUser);
		query.setParameter("isDelete", 0);
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<CostCenterSap> costCenterSapList = query.getResultList();
		return costCenterSapList;
	}
	
	

	public long getListCount(String tempKeyword) {

		String queryCountUser = "SELECT COUNT (costCenterSap) FROM CostCenterSap costCenterSap WHERE costCenterSap.isDelete =:isDelete AND  "
				+ "(costCenterSap.code like :keyword OR costCenterSap.description like :keyword) ";
		
		Query query = em1.createQuery(queryCountUser);
		query.setParameter("keyword", tempKeyword);
		query.setParameter("isDelete", 0);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em1;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema1;
	}

	public CostCenterSap getId(int id) {
		return super.find(id);
	}

}
