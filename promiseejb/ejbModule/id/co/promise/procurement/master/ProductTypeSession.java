package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.ProductType;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class ProductTypeSession extends AbstractFacadeWithAudit<ProductType> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public ProductTypeSession() {
		super(ProductType.class);
	}

	public ProductType getProductType(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductType> getProductTypeList() {
		Query q = em.createNamedQuery("ProductType.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductType> getProductTypeByName(String byName) {
		Query q = em.createNamedQuery("ProductType.findByName");
		q.setParameter("byName", byName);
		return q.getResultList();
	}
	
	public ProductType insertProductType(ProductType productType, Token token) {
		productType.setCreated(new Date());
		productType.setIsDelete(0);
		super.create(productType, AuditHelper.OPERATION_CREATE, token);
		return productType;
	}

	public ProductType updateProductType(ProductType productType, Token token) {
		productType.setUpdated(new Date());
		super.edit(productType, AuditHelper.OPERATION_UPDATE, token);
		return productType;
	}

	public ProductType deleteProductType(int id, Token token) {
		ProductType productType = super.find(id);
		productType.setIsDelete(1);
		productType.setDeleted(new Date());
		super.edit(productType, AuditHelper.OPERATION_DELETE, token);
		return productType;
	}

	public ProductType deleteRowProductType(int id, Token token) {
		ProductType productType = super.find(id);
		super.remove(productType, AuditHelper.OPERATION_ROW_DELETE, token);
		return productType;
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
