package id.co.promise.procurement.approval;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.ApprovalType;
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
public class ApprovalTypeSession extends AbstractFacadeWithAudit<ApprovalType> {
	final static Logger log = Logger.getLogger(ApprovalTypeSession.class);
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

	public ApprovalTypeSession() {
		super(ApprovalType.class);
	}

	public List<ApprovalType> getList() {
		Query q = em.createNamedQuery("ApprovalType.find");
		return q.getResultList();
	}

	public ApprovalType create(ApprovalType approvalType, Token token) {
		approvalType.setCreated(new Date());
		approvalType.setIsDelete(0);
		super.create(approvalType, AuditHelper.OPERATION_CREATE, token);
		return approvalType;
	}

	public ApprovalType update(ApprovalType approvalType, Token token){
		approvalType.setUpdated(new Date());
		super.edit(approvalType, AuditHelper.OPERATION_UPDATE, token);
		return approvalType;
	}
	
	public ApprovalType delete(int id, Token token){
		ApprovalType appType = super.find(id);
		appType.setDeleted(new Date());
		appType.setIsDelete(1);
		super.edit(appType, AuditHelper.OPERATION_DELETE, token);
		return appType;
	}
	
}
