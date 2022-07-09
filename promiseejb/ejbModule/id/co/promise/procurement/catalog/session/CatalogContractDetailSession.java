package id.co.promise.procurement.catalog.session;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogContractDetail;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class CatalogContractDetailSession  extends AbstractFacadeWithAudit<CatalogContractDetail> {
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	VendorSession vendorSession;

	public CatalogContractDetailSession() {
		super(CatalogContractDetail.class);
	}
	
	public CatalogContractDetail update(CatalogContractDetail entity, Token token) {
		entity.setUpdated(new Date());
		super.edit(entity, AuditHelper.OPERATION_UPDATE, token);
		return entity;
	}

	
	@SuppressWarnings("unchecked")
	public CatalogContractDetail getCatalogContractDetailByItem(Item item) {
		Query query=em.createNamedQuery("CatalogContractDetail.getByItem");
		query.setParameter("isDelete", 0)
		.setParameter("item", item)
		.setMaxResults(1);
		return (CatalogContractDetail)query.getSingleResult();
	}

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

	public List<CatalogContractDetail> getListByCatalogContractId(Integer catalogContractId, String token) {
//		Query q = em.createNamedQuery("CatalogContractDetail.findByCatalogContractId");
//		q.setParameter("catalogContractId", catalogContractId);
//		List<CatalogContractDetail> catalogContractList = q.getResultList();
//		if (catalogContractList != null && catalogContractList.size() > 0) {
//			return catalogContractList.get(0);
//		}
//		return null;
		Token tempToken = tokenSession.findByToken(token);
		User user=tempToken.getUser();
		Vendor vendor=vendorSession.getVendorByUserId(user.getId());
		
		Query q = em.createNamedQuery("CatalogContractDetail.findByCatalogContractId");
		q.setParameter("vendorId",vendor.getId());
		q.setParameter("catalogContractId", catalogContractId);
		q.setMaxResults(30);
		return q.getResultList();
		
		
		
	}
}
