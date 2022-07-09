/**
 * fdf
 */
package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.TermAndCondition;
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

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class TermAndConditionSession extends AbstractFacadeWithAudit<TermAndCondition>{
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
	
	public TermAndConditionSession() {
		super(TermAndCondition.class);
	}
	
	public TermAndCondition getTermAndCondition(int id) {
		return super.find(id);
	}
	
	public List <TermAndCondition> getTermAndConditionList() {
		Query q = em.createNamedQuery("TermAndCondition.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List <TermAndCondition> getListById(int id) {
		Query q = em.createNamedQuery("TermAndConditionList.byId");
		q.setParameter("id", id);
		return q.getResultList();
	}
	
	
	
	
	public TermAndCondition insertTermAndCondition(TermAndCondition termAndCondition, Token token) {
		termAndCondition.setCreated(new Date());
		termAndCondition.setIsDelete(0);
		super.create(termAndCondition, AuditHelper.OPERATION_CREATE, token);
		return termAndCondition;
	}
	
	public TermAndCondition updateTermAndCondition(TermAndCondition termAndCondition, Token token) {
		termAndCondition.setUpdated(new Date());
		super.edit(termAndCondition, AuditHelper.OPERATION_UPDATE, token);
		return termAndCondition;
	}

	public TermAndCondition deleteTermAndCondition(int id, Token token) {
		TermAndCondition termAndCondition = super.find(id);
		termAndCondition.setIsDelete(1);
		termAndCondition.setDeleted(new Date());
		super.edit(termAndCondition, AuditHelper.OPERATION_DELETE, token);
		return termAndCondition;
	}
	

	public TermAndCondition deleteRowTermAndCondition(int id, Token token) {
		TermAndCondition termAndCondition = super.find(id);
		super.remove(termAndCondition, AuditHelper.OPERATION_ROW_DELETE, token);
		return termAndCondition;
	}

	

}
