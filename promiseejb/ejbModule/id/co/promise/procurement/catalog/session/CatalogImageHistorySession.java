package id.co.promise.procurement.catalog.session;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogCategoryHistory;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.CatalogImageHistory;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogImageHistorySession extends AbstractFacadeWithAudit<CatalogImageHistory>{
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogImageHistorySession() {
		super(CatalogImageHistory.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public CatalogImageHistory insertCatalogImageHistory(CatalogImageHistory entity, Token token) {
		entity.setIsDelete(0);
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogImageHistory updateCatalogImageHistory(CatalogImageHistory entity, Token token) {
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogImageHistory deleteCatalogImageHistory(int id, Token token) {
		CatalogImageHistory entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogImageHistory deleteRowCatalogImageHistory(int id, Token token) {
		CatalogImageHistory entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public CatalogImageHistory getCatalogImageHistoryByVersion(int catalogImageId) {
		Query query =em.createNamedQuery("CatalogImageHistory.getByVersion")
				.setParameter("catalogImageId", catalogImageId);
		List<CatalogImageHistory> catalogImageHistoryList = query.getResultList();
		if(catalogImageHistoryList.size() > 0) {
			return catalogImageHistoryList.get(0);
		}else {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CatalogImageHistory> getCatalogImageHistoryByCatalog(Catalog catalog) {
		Query query =em.createNamedQuery("CatalogImageHistory.getByCatalog")
				.setParameter("catalogId", catalog.getId());
		List<CatalogImageHistory> catalogImageHistoryList = query.getResultList();
		return catalogImageHistoryList;
		
	}
}
