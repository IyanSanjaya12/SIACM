package id.co.promise.procurement.catalog.session;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogTempHarga;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class CatalogTempHargaSession extends AbstractFacadeWithAudit<CatalogTempHarga>  {
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CatalogTempHargaSession() {
		super(CatalogTempHarga.class);
	}
	
	public CatalogTempHarga getCatalogTempHargaByCatalaog(Catalog catalog) {
		Query query=em.createNamedQuery("CatalogTempHarga.findByCatalog");
		query.setParameter("isDelete", 0)
			 .setParameter("catalogId", catalog.getId());
		query.setMaxResults(1);
		return (CatalogTempHarga)query.getSingleResult();
	}
	
	public CatalogTempHarga insertCatalogTempHarga(CatalogTempHarga entity, Token token) {
		entity.setIsDelete(0);
		entity.setCreated(new Date());
		super.create(entity, AuditHelper.OPERATION_CREATE, token);
		return entity;
	}

	public CatalogTempHarga updateCatalogTempHarga(CatalogTempHarga entity, Token token) {
		entity.setUpdated(new Date());
		entity.setIsDelete(0);
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
