package id.co.promise.procurement.catalog.session;

import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.entity.CategoryAttribute;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

@Stateless
@LocalBean
public class CategoryAttributeSession extends AbstractFacadeWithAudit<CategoryAttribute> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public CategoryAttributeSession() {
		super(CategoryAttribute.class);
	}
	
	public List<CategoryAttribute> getCategoryAttributeList(Category category) {
		Query q = em.createQuery("SELECT categoryAttribute FROM CategoryAttribute categoryAttribute WHERE categoryAttribute.isDelete = 0 "
				+ "AND categoryAttribute.category = :category");
		q.setParameter("category", category);

		return q.getResultList();
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
