package id.co.promise.procurement.catalog.session;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogAttribute;
import id.co.promise.procurement.catalog.entity.CatalogAttributeHistory;
import id.co.promise.procurement.catalog.entity.CatalogCategoryHistory;
import id.co.promise.procurement.catalog.entity.CatalogImageHistory;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogAttributeHistorySession extends AbstractFacadeWithAudit<CatalogAttributeHistory> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogAttributeHistorySession() {
		super(CatalogAttributeHistory.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public CatalogAttributeHistory insertCatalogAttributeHistory(CatalogAttributeHistory catalogAttributeHistory, Token token) {
		catalogAttributeHistory.setIsDelete(0);
		super.create(catalogAttributeHistory, AuditHelper.OPERATION_CREATE, token);
		return catalogAttributeHistory;
	}

	public CatalogAttributeHistory updateCatalogAttributeHistory(CatalogAttributeHistory catalogAttributeHistory, Token token) {
		super.edit(catalogAttributeHistory, AuditHelper.OPERATION_UPDATE, token);
		return catalogAttributeHistory;
	}

	public CatalogAttributeHistory deleteCatalogAttributeHistory(int id, Token token) {
		CatalogAttributeHistory catalogAttributeHistory = super.find(id);
		catalogAttributeHistory.setIsDelete(1);
		super.edit(catalogAttributeHistory, AuditHelper.OPERATION_UPDATE, token);
		return catalogAttributeHistory;
	}

	public CatalogAttributeHistory deleteRowCatalogAttributeHistory(int id, Token token) {
		CatalogAttributeHistory catalogAttributeHistory = super.find(id);
		super.remove(catalogAttributeHistory, AuditHelper.OPERATION_ROW_DELETE, token);
		return catalogAttributeHistory;
	}
	
	@SuppressWarnings("unchecked")
	public CatalogAttributeHistory getCatalogAttributeHistoryByVersion(int catalogAttributeId) {
		Query query =em.createNamedQuery("CatalogAttributeHistory.getByVersion")
				.setParameter("catalogAttributeId", catalogAttributeId);
		List<CatalogAttributeHistory> catalogAttributeHistoryList = (List<CatalogAttributeHistory>) query.getResultList();
		if(catalogAttributeHistoryList.size() > 0) {
			return catalogAttributeHistoryList.get(0);
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogAttributeHistory> getCatalogAttributeHistoryByCatalog(Catalog catalog) {
		Query query =em.createNamedQuery("CatalogAttributeHistory.getByCatalog")
				.setParameter("catalogId", catalog.getId());
		List<CatalogAttributeHistory> CatalogAttributeHistoryList = query.getResultList();
		return CatalogAttributeHistoryList;
		
	}
}
