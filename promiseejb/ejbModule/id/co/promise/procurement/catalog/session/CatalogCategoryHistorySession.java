package id.co.promise.procurement.catalog.session;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.CatalogCategoryHistory;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogCategoryHistorySession extends AbstractFacadeWithAudit<CatalogCategoryHistory>{
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
	
	public CatalogCategoryHistorySession() {
		super(CatalogCategoryHistory.class);
	}
	
	public CatalogCategoryHistory insertCatalogCategoryHistory(CatalogCategoryHistory catalogCategoryHistory, Token token) {
		catalogCategoryHistory.setIsDelete(0);
		super.create(catalogCategoryHistory, AuditHelper.OPERATION_CREATE, token);
		return catalogCategoryHistory;
	}

	public CatalogCategoryHistory updateCatalogCategoryHistory(CatalogCategoryHistory catalogCategoryHistory, Token token) {
		super.edit(catalogCategoryHistory, AuditHelper.OPERATION_UPDATE, token);
		return catalogCategoryHistory;
	}

	public CatalogCategoryHistory deleteCatalogCategoryHistory(int id, Token token) {
		CatalogCategoryHistory catalogCategoryHistory = super.find(id);
		catalogCategoryHistory.setIsDelete(1);
		super.edit(catalogCategoryHistory, AuditHelper.OPERATION_UPDATE, token);
		return catalogCategoryHistory;
	}

	public CatalogCategoryHistory deleteRowCatalogCategoryHistory(int id, Token token) {
		CatalogCategoryHistory catalogCategoryHistory = super.find(id);
		super.remove(catalogCategoryHistory, AuditHelper.OPERATION_ROW_DELETE, token);
		return catalogCategoryHistory;
	}
	
	@SuppressWarnings("unchecked")
	public CatalogCategoryHistory getCatalogCategoryHistoryByVersion(int catalogCategoryId) {
		Query query =em.createNamedQuery("CatalogCategoryHistory.getByVersion")
				.setParameter("catalogCategoryId", catalogCategoryId);
		List<CatalogCategoryHistory> catalogCategoryHistoryList = query.getResultList();
		if(catalogCategoryHistoryList.size() > 0) {
			return catalogCategoryHistoryList.get(0);
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogCategoryHistory> getCatalogCategoryHistoryByCatalogId(Catalog catalog) {
		Query q = em.createQuery("SELECT catalogCategoryHistory FROM CatalogCategoryHistory catalogCategoryHistory "
				+ "WHERE catalogCategoryHistory.isDelete = 0 "
				+ "AND catalogCategoryHistory.catalog = :catalog");
		q.setParameter("catalog", catalog);
		
		List<CatalogCategoryHistory> catalogCategoryHistory = q.getResultList();
		return catalogCategoryHistory;
	}

}
