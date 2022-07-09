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

import id.co.promise.procurement.entity.GLAccountSap;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class GLAccountSapSession extends AbstractFacadeWithAudit<GLAccountSap> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	GLAccountSapStagingSession gLAccountSapStagingSession;

	public GLAccountSapSession() {
		super(GLAccountSap.class);
	}

	public GLAccountSap findId(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<GLAccountSap> getList() {
		Query q = em.createNamedQuery("GLAccountSap.find");
		List <GLAccountSap>  gLAccountSapList = q.getResultList();
		return gLAccountSapList;
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

		List<GLAccountSap> gLAccountSapStagingList  = new ArrayList<>(); //gLAccountSapStagingSession.getStagingList();

		List<String> tempCodeList = new ArrayList<>();
		Token token = new Token();
		
		//kalau sudah ada, di update. kalau tidak ada di, insert
		for (GLAccountSap gLAccountSapStaging : gLAccountSapStagingList ) {
			tempCodeList.add(gLAccountSapStaging.getCode());
			GLAccountSap cekGLAccountSap = getByCode(gLAccountSapStaging.getCode());
			
			GLAccountSap gLAccountSapNew = new GLAccountSap();
			
			if (cekGLAccountSap != null ) {
				gLAccountSapNew.setId(cekGLAccountSap.getId());
				gLAccountSapNew.setCreated(cekGLAccountSap.getCreated());
				gLAccountSapNew.setCode(gLAccountSapStaging.getCode());
				gLAccountSapNew.setDescription(gLAccountSapStaging.getDescription());
				update(gLAccountSapNew, token);
			} else {
				gLAccountSapNew.setCreated(new Date());
				gLAccountSapNew.setCode(gLAccountSapStaging.getCode());
				gLAccountSapNew.setDescription(gLAccountSapStaging.getDescription());
				insert(gLAccountSapNew, token);
			}
		}
		
		//softdelete yang tidak ada di staging
		deleteByCodeList(tempCodeList);
	}
	
	@SuppressWarnings("unchecked")
	public GLAccountSap getByCode(String code) {
		Query q = em.createQuery("SELECT gLAccountSap FROM GLAccountSap gLAccountSap Where gLAccountSap.isDelete = 0 AND gLAccountSap.code =:code");
		q.setParameter("code", code);
		List<GLAccountSap> gLAccountSap = q.getResultList();
		if (gLAccountSap != null && gLAccountSap.size() > 0) {
			return gLAccountSap.get(0);
		}
		return null;
	}
	
	public GLAccountSap insert(GLAccountSap gLAccountSap, Token token) {
		gLAccountSap.setCreated(new Date());
		if(gLAccountSap.getIsDelete() == null) {
			gLAccountSap.setIsDelete(0);
		}
		super.create(gLAccountSap, AuditHelper.OPERATION_CREATE, token);
		return gLAccountSap;
	}
	
	public GLAccountSap update(GLAccountSap gLAccountSap, Token token) {
		gLAccountSap.setUpdated(new Date());
		if(gLAccountSap.getIsDelete() == null) {
			gLAccountSap.setIsDelete(0);
		}
		super.edit(gLAccountSap, AuditHelper.OPERATION_UPDATE, token);
		return gLAccountSap;
	}
	
	public void deleteByCodeList(List<String> tempCodeList) {
		Query q = em.createQuery("UPDATE GLAccountSap gLAccountSap SET gLAccountSap.isDelete = 1 Where gLAccountSap.isDelete = 0 AND gLAccountSap.code NOT IN (:codeStagingList)");
		q.setParameter("codeStagingList", tempCodeList);
		q.executeUpdate();	
	}
	
	public List<GLAccountSap> getListWithPagination(Integer start, Integer length, String keyword, Integer columnOrder, String tipeOrder){
		
		String queryUser = "SELECT gLAccountSap FROM GLAccountSap gLAccountSap WHERE gLAccountSap.isDelete =:isDelete AND "
				+ "(gLAccountSap.code like :keyword OR gLAccountSap.description like :keyword) ";
		
		String[] columnToView = { "id", "code", "description"};
		if (columnOrder > 0) {
			queryUser+="ORDER BY gLAccountSap. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+="ORDER BY gLAccountSap.id desc ";
		}
		
		Query query = em.createQuery(queryUser);
		query.setParameter("isDelete", 0);
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<GLAccountSap> gLAccountSapList = query.getResultList();
		return gLAccountSapList;
	}
	
	

	public long getListCount(String tempKeyword) {

		String queryCountUser = "SELECT COUNT (gLAccountSap) FROM GLAccountSap gLAccountSap WHERE gLAccountSap.isDelete =:isDelete AND  "
				+ "(gLAccountSap.code like :keyword OR gLAccountSap.description like :keyword) ";
		
		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", tempKeyword);
		query.setParameter("isDelete", 0);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}
}
