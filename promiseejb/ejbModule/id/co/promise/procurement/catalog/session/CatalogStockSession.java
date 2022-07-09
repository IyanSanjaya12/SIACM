package id.co.promise.procurement.catalog.session;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogStock;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogStockSession extends AbstractFacadeWithAudit<CatalogStock> {
	
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
	public CatalogStockSession() {
		super(CatalogStock.class);
	}
	public List<CatalogStock> getCatalogStockListByCatalog(Catalog catalog){
		Query q = em.createNamedQuery("CatalogStock.getByCatalog");
		q.setParameter("catalog", catalog);
		return q.getResultList();	
	}
	
	public List<CatalogStock> getCatalogStockList() {
		Query q = em.createNamedQuery("CatalogStock.getCatalogStockList");
		return q.getResultList();
	}
	public List<CatalogStock> getCatalogStockById(Integer id) {
		Query q = em.createNamedQuery("CatalogStock.getCatalogStockById");
		q.setParameter("id",id);
		return q.getResultList();
	}
	
	public CatalogStock insertCatalogStock(CatalogStock catalogStock, Token token) {
		catalogStock.setIsDelete(0);
		catalogStock.setCreated(new Date());
		super.create(catalogStock, AuditHelper.OPERATION_CREATE, token);
		return catalogStock;
	}
	
	public CatalogStock updateCatalogStockByCatalog(CatalogStock catalogStock, Token token) {
		Query q=em.createNamedQuery("CatalogStock.updateCatalogStock");
		q.setParameter("catalogId", catalogStock.getCatalog().getId());
		q.executeUpdate();
		return catalogStock;
	}

	public CatalogStock updateCatalogStock(CatalogStock catalogStock, Token token) {
		super.edit(catalogStock, AuditHelper.OPERATION_UPDATE, token);
		return catalogStock;
	}

	public CatalogStock deleteCatalogStock(int id, Token token) {
		CatalogStock entity = super.find(id);
		entity.setIsDelete(1);
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	public CatalogStock deleteRowCatalogStock(int id, Token token) {
		CatalogStock entity = super.find(id);
		super.remove(entity, AuditHelper.OPERATION_ROW_DELETE, token);
		return entity;
	}
	public Integer deleteCatalogStock(int catalogId) {
		Query query = em.createQuery("CatalogStock.deleteByIdCatalog");
		query.setParameter("catalogId", catalogId);
		return query.executeUpdate();
	}

	
}
