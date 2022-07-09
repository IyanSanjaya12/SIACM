package id.co.promise.procurement.catalog.session;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogHistory;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@LocalBean
@Stateless
public class CatalogHistorySession extends AbstractFacadeWithAudit<CatalogHistory>{
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogHistorySession() {
		super(CatalogHistory.class);
	}
	
	public CatalogHistory getCatalogHistoryByCatalaog(Catalog catalog) {
		try {
			Query query=em.createNamedQuery("CatalogHistory.findByCatalog");
			query.setParameter("isDelete", 0)
				 .setParameter("catalogId", catalog.getId());
			query.setMaxResults(1);
			return (CatalogHistory)query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public CatalogHistory insertCatalogHistory(CatalogHistory entity, Token token) {
		entity.setIsDelete(0);
		entity.setCreated(new Date());
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogHistory updateCatalogHistory(CatalogHistory entity, Token token) {
		entity.setUpdated(new Date());
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
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
