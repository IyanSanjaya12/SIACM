package id.co.promise.procurement.catalog.session;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogBulkPrice;
import id.co.promise.procurement.catalog.entity.CatalogBulkPriceHistory;
import id.co.promise.procurement.catalog.entity.CatalogCategoryHistory;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogBulkPriceHistorySession extends AbstractFacadeWithAudit<CatalogBulkPriceHistory> {

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
	
	public CatalogBulkPriceHistorySession() {
		super(CatalogBulkPriceHistory.class);
	}
	public CatalogBulkPriceHistory insertCatalogBulkPriceHistory(CatalogBulkPriceHistory entity, Token token) {
		
		entity.setIsDelete(0);
		entity.setCreated(new Date());
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogBulkPriceHistory updateCatalogBulkPriceHistory(CatalogBulkPriceHistory entity, Token token) {
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogBulkPriceHistory deleteCatalogBulkPriceHistory(int id, Token token) {
		CatalogBulkPriceHistory entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}
	
	public CatalogBulkPriceHistory deleteRowCatalogBulkPriceHistory(int id, Token token) {
		CatalogBulkPriceHistory entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public CatalogBulkPriceHistory getCatalogBulkPriceHistoryByVersion(int catalogCategoryId) {
		Query query =em.createNamedQuery("CatalogBulkPriceHistory.getByVersion")
				.setParameter("catalogBulkPriceId", catalogCategoryId);
		List<CatalogBulkPriceHistory> catalogBulkPriceHistoryList = query.getResultList();
		if(catalogBulkPriceHistoryList.size() > 0) {
			return catalogBulkPriceHistoryList.get(0);
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogBulkPriceHistory> getCatalogBulkPriceHistoryByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogBulkPriceHistory FROM CatalogBulkPriceHistory catalogBulkPriceHistory "
				+ "WHERE catalogBulkPriceHistory.isDelete = 0 "
				+ "AND catalogBulkPriceHistory.catalog.id = :catalogId");
		q.setParameter("catalogId", catalog.getId());
		
		List<CatalogBulkPriceHistory> catalogBulkPriceHistory = q.getResultList();
		return catalogBulkPriceHistory;
	}
	
}
