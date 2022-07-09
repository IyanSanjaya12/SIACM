package id.co.promise.procurement.approval;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

@Stateless
@LocalBean
public class ApprovalLevelSession extends
		AbstractFacadeWithAudit<ApprovalLevel> {
	
	final static Logger log = Logger.getLogger(ApprovalLevelSession.class);
	
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

	public ApprovalLevelSession() {
		super(ApprovalLevel.class);
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalLevel> getList() {
		return em.createNamedQuery("ApprovalLevel.find").getResultList();
	}

	public ApprovalLevel getById(Integer id) {
		return super.find(id);
	}

	public ApprovalLevel create(ApprovalLevel approvalLevel, Token token) {
		approvalLevel.setCreated(new Date());
		approvalLevel.setIsDelete(0);
		super.create(approvalLevel, AuditHelper.OPERATION_CREATE, token);
		return approvalLevel;
	}

	public ApprovalLevel update(ApprovalLevel approvalLevel, Token token) {
		approvalLevel.setUpdated(new Date());
		super.edit(approvalLevel, AuditHelper.OPERATION_UPDATE, token);
		return approvalLevel;
	}

	public ApprovalLevel delete(Integer id, Token token) {
		ApprovalLevel appLevel = super.find(id);
		appLevel.setDeleted(new Date());
		appLevel.setIsDelete(1);
		super.edit(appLevel, AuditHelper.OPERATION_DELETE, token);
		return appLevel;
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalLevel> findByApproval(Approval approval) {
		Query q = em.createNamedQuery("ApprovalLevel.findByApproval");
		q.setParameter("approval", approval);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalLevel> findByApprovalId(Integer id) {
		Query q = em.createQuery("SELECT a FROM ApprovalLevel a WHERE a.isDelete = 0 and a.approval.id = :id ORDER BY a.sequence ASC");
		q.setParameter("id", id);
		return q.getResultList();
	}
}
