package id.co.promise.procurement.security;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.LoginAttempt;
import id.co.promise.procurement.entity.Parameter;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.TokenRemember;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.master.ParameterSession;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.ParamContext;
import id.co.promise.procurement.utils.audit.AuditHelper;

@LocalBean
@Stateless

public class LoginAttemptSession extends AbstractFacadeWithAudit<LoginAttempt>{
	
	@EJB
	ParameterSession parameterSession;
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public LoginAttemptSession() {
		super(LoginAttempt.class);
	}
	
	@SuppressWarnings("unchecked")
	public LoginAttempt getLoginAttemptByUser(User user) {

		Query q = em.createNamedQuery("LoginAttempt.findByUserId");
		q.setParameter("user", user);
		
		List<LoginAttempt> loginAttempt = q.getResultList();
		if (loginAttempt != null && loginAttempt.size() > 0) {
			return loginAttempt.get(0);
		}
		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<LoginAttempt> getLoginAttemptList() {

		Query q = em.createNamedQuery("LoginAttempt.findByList");
		
		return q.getResultList();
		
	}
	
	public Integer getMinuteBetween(User user) {
		
		Query q = em.createNamedQuery("LoginAttempt.findByUserId");
		q.setParameter("user", user);
		
		LoginAttempt loginAttempt = getLoginAttemptByUser(user);
		if(loginAttempt != null) {
			
			Integer parameterLoginTimeout = ParamContext.getParameterIntegerByName("LOGIN_ATTEMPT_TIMEOUT");
			
			Date now = new Date();
			long result;
			
			if(loginAttempt.getSequence() == 1) {
				result = ((now.getTime()/60000) - (loginAttempt.getCreated().getTime()/60000));
			} else {
				result = ((now.getTime()/60000) - (loginAttempt.getUpdated().getTime()/60000));
			}
			
			Integer results = parameterLoginTimeout - (int) result;
			
			return results;
		}
		
		return null;
		
	}
	
	public void deleteLoginAttemptUser(User user, Integer diffMinute) {
		
		Integer parameterLoginAttempt = ParamContext.getParameterIntegerByName("LOGIN_ATTEMPT");
		
		LoginAttempt loginAttempt = getLoginAttemptByUser(user);
		if(loginAttempt != null) {

			if(loginAttempt.getSequence().equals(parameterLoginAttempt)) {
				if (diffMinute <= 0) {
					deleteRowLoginAttempt(loginAttempt.getId());
				}
			}
			
		}
		
	}
	
	public LoginAttempt insertLoginAttempt(LoginAttempt loginAttempt) {
		loginAttempt.setCreated(new Date());
		loginAttempt.setIsDelete(0);
		super.create(loginAttempt, AuditHelper.OPERATION_CREATE, null);
		return loginAttempt;
	}

	public LoginAttempt updateLoginAttempt(LoginAttempt loginAttempt) {
		loginAttempt.setUpdated(new Date());
		super.edit(loginAttempt, AuditHelper.OPERATION_UPDATE, null);
		return loginAttempt;
	}


	public LoginAttempt deleteLoginAttempt(Integer loginAttemptId) {
		LoginAttempt loginAttempt = super.find(loginAttemptId);
		loginAttempt.setDeleted(new Date());
		loginAttempt.setIsDelete(1);
		super.edit(loginAttempt, AuditHelper.OPERATION_DELETE, null);
		return loginAttempt;
	}

	public LoginAttempt deleteRowLoginAttempt(Integer loginAttemptId) {
		LoginAttempt loginAttempt = super.find(loginAttemptId);
		super.remove(loginAttempt, AuditHelper.OPERATION_ROW_DELETE, null);
		return loginAttempt;
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
