package id.co.promise.procurement.email;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.EmailNotificationStatus;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@LocalBean
@Stateless
public class EmailNotificationStatusSession extends
		AbstractFacadeWithAudit<EmailNotificationStatus> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public EmailNotificationStatusSession() {
		super(EmailNotificationStatus.class);
	}

	public EmailNotificationStatus getEmailNotificationStatus(int id) {
		return super.find(id);
	}

	public List<EmailNotificationStatus> getEmailNotificationStatuslist() {
		Query q = em.createNamedQuery("EmailNotificationStatus.find");
		return q.getResultList();
	}

	public EmailNotificationStatus insertEmailNotificationStatus(
			EmailNotificationStatus ens, Token token) {
		ens.setCreated(new Date());
		ens.setIsDelete(0);
		super.create(ens, AuditHelper.OPERATION_CREATE, token);
		return ens;
	}

	public EmailNotificationStatus updateEmailNotificationStatus(
			EmailNotificationStatus ens, Token token) {
		ens.setUpdated(new Date());
		super.edit(ens, AuditHelper.OPERATION_UPDATE, token);
		return ens;
	}

	public EmailNotificationStatus deleteEmailNotificationStatus(
			int emailNotificationStatusId, Token token) {
		EmailNotificationStatus ens = super.find(emailNotificationStatusId);
		ens.setIsDelete(1);
		ens.setDeleted(new Date());
		super.edit(ens, AuditHelper.OPERATION_DELETE, token);
		return ens;
	}

	public EmailNotificationStatus deleteRowEmailNotificationStatus(
			int emailNotificationStatusId, Token token) {
		EmailNotificationStatus ens = super.find(emailNotificationStatusId);
		super.remove(ens, AuditHelper.OPERATION_ROW_DELETE, token);
		return ens;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

}
