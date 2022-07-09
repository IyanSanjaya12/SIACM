package id.co.promise.procurement.master;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.WebServicesManager;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class WebServicesManagerSession extends AbstractFacadeWithAudit<WebServicesManager> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public WebServicesManagerSession(){
		super(WebServicesManager.class);
	}
	
	public List<WebServicesManager> findAll(){
		Query q = em.createNamedQuery("WebServicesManager.findAll");
		return q.getResultList();
	}
	
	public WebServicesManager findById(int id){
		return super.find(id);
	}
	
	public Long countAll(){
		Query q = em.createNamedQuery("WebServicesManager.countAll");
		return (Long)q.getSingleResult();
	}
	
	public Map<String, Object> findWithPagging(Integer start, Integer length, String keyword, Integer columnOrder,
			String tipeOrder, Integer draw) {
		String queryUser = "SELECT o FROM WebServicesManager o WHERE o.isDelete=0 AND " 
				+ "(o.services like :keyword OR o.function like :keyword "
				+ "OR o.path like :keyword) ";
		
		String[] columnToView = {"nomor","services", "function", "path"};
		if (columnOrder > 0) {
			queryUser+=" ORDER BY o. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+=" ORDER BY o.id desc ";
		}
		
		Query query = em.createQuery(queryUser);	
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<WebServicesManager> wsmList = query.getResultList();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countAll());
		result.put("recordsFiltered", wsmList.size());
		result.put("data",wsmList);
		
		return result;
	}

	public WebServicesManager insertWebServicesManager(WebServicesManager wsm, Token token){
		wsm.setCreated(new Date());
		wsm.setIsDelete(0);
		wsm.setUserId(token.getUser().getId());
		super.create(wsm, AuditHelper.OPERATION_CREATE, token);
		return wsm;
	}
	
	public WebServicesManager updateWebServicesManager(WebServicesManager wsm, Token token){
		wsm.setUpdated(new Date());
		wsm.setUserId(token.getUser().getId());
		super.create(wsm, AuditHelper.OPERATION_CREATE, token);
		return wsm;
	}

	public WebServicesManager deleteWebServicesManager(int id, Token token){
		WebServicesManager wsm = findById(id);
		wsm.setDeleted(new Date());
		wsm.setIsDelete(1);
		super.edit(wsm, AuditHelper.OPERATION_DELETE, token);
		return wsm;
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
