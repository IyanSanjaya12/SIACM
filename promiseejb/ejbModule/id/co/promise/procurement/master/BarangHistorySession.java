package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.BarangHistory;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class BarangHistorySession extends AbstractFacadeWithAudit<BarangHistory>{
	
	public BarangHistorySession() {
		super(BarangHistory.class);
	}

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public BarangHistory getbarangHistory(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<BarangHistory> getbarangHistoryList(){
		Query query = em.createQuery("SELECT barangHistory FROM BarangHistory barangHistory WHERE barangHistory.isDelete = 0 AND (barangHistory.status IS NULL OR barangHistory.status=1)");
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<BarangHistory> getBarangHistoryRevisiList(){
		Query query = em.createQuery("SELECT barangHistory FROM BarangHistory barangHistory WHERE barangHistory.isDelete = 0 AND barangHistory.status = 2");
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<BarangHistory> getListNotSelect(List<Integer> idList){
		Query query = em.createQuery("SELECT barangHistory FROM BarangHistory barangHistory WHERE barangHistory.isDelete = 0 AND barangHistory.id NOT IN (:selectList)");
		if(idList.size() > 0) {
			query.setParameter("selectList", idList);			
		}else {
			query.setParameter("selectList", Constant.ZERO_VALUES);
		}
		return query.getResultList();
	}
	
	public BarangHistory insert(BarangHistory barangHistory, Token token) {
		barangHistory.setCreated(new Date());
		barangHistory.setIsDelete(0);
		super.create(barangHistory, AuditHelper.OPERATION_CREATE, token);
		return barangHistory;
	}

	public BarangHistory update(BarangHistory barangHistory, Token token) {
		barangHistory.setUpdated(new Date());
		barangHistory.setIsDelete(0);
		super.edit(barangHistory, AuditHelper.OPERATION_UPDATE, token);
		return barangHistory;
	}

	public BarangHistory delete(int id, Token token) {
		BarangHistory barangHistory = super.find(id);
		barangHistory.setIsDelete(1);
		barangHistory.setDeleted(new Date());
		super.edit(barangHistory, AuditHelper.OPERATION_DELETE, token);
		return barangHistory;
	}
}
