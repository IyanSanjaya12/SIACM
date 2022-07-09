package id.co.promise.procurement.catalog.session;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogBulkPriceHistory;
import id.co.promise.procurement.catalog.entity.CatalogFee;
import id.co.promise.procurement.catalog.entity.CatalogFeeHistory;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogFeeHistorySession extends AbstractFacadeWithAudit<CatalogFeeHistory>{
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogFeeHistorySession() {
		super(CatalogFeeHistory.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	
	public CatalogFeeHistory insertCatalogFeeHistory(CatalogFeeHistory catalogFeeHistory, Token token) {
		catalogFeeHistory.setIsDelete(0);
		catalogFeeHistory.setCreated(new Date());
		super.create(catalogFeeHistory, AuditHelper.OPERATION_CREATE, token);
		return catalogFeeHistory;
	}
	
	public CatalogFeeHistory updateCatalogFeeHistory(CatalogFeeHistory catalogFeeHistory, Token token) {
		super.edit(catalogFeeHistory, AuditHelper.OPERATION_UPDATE, token);
		return catalogFeeHistory;
	}

	public CatalogFeeHistory deleteCatalogFeeHistory(int id, Token token) {
		CatalogFeeHistory entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogFeeHistory deleteRowCatalogFeeHistory(int id, Token token) {
		CatalogFeeHistory entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public CatalogFeeHistory getCatalogFeeHistoryByVersion(int catalogFeeId) {
		Query query =em.createNamedQuery("CatalogFeeHistory.getByVersion")
				.setParameter("catalogFeeId", catalogFeeId);
		List<CatalogFeeHistory> catalogFeeHistoryList = query.getResultList();
		if(catalogFeeHistoryList.size() > 0) {
			return catalogFeeHistoryList.get(0);
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogFeeHistory> getCatalogFeeHistoryByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogFeeHistory FROM CatalogFeeHistory catalogFeeHistory "
				+ "WHERE catalogFeeHistory.isDelete = 0 "
				+ "AND catalogFeeHistory.catalog.id = :catalogId");
		q.setParameter("catalogId", catalog.getId());
		
		List<CatalogFeeHistory> catalogFeeHistory = q.getResultList();
		return catalogFeeHistory;
	}
	
}
