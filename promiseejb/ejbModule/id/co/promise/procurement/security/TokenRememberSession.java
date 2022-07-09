package id.co.promise.procurement.security;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.TokenRemember;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@LocalBean
@Stateless
public class TokenRememberSession extends AbstractFacadeWithAudit<TokenRemember> {
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public TokenRememberSession() {
		super(TokenRemember.class);
	}
	
	public TokenRemember findByToken(String token) {
		Query q = em.createNamedQuery("TokenRemember.findByToken").setParameter(
				"token", token);
		try {
			return (TokenRemember) q.getSingleResult();
		} catch (Exception e) {	
			return null;
		}
	}
	
	public TokenRemember findByUser(User userId) {
		Query q = em.createNamedQuery("TokenRemember.findByUser").setParameter(
				"userId", userId);
		q.setMaxResults(1);
		try {
			return (TokenRemember) q.getSingleResult();
		} catch (Exception e) {	
			return null;
		}
	}
	
	
	public TokenRemember createTokenRemember(TokenRemember tokenRemember, Token token) {
		tokenRemember.setCreated(new Date());
		tokenRemember.setIsDelete(0);
		super.create(tokenRemember, AuditHelper.OPERATION_CREATE, token);
		return tokenRemember;
	}

	public TokenRemember editTokenRemember(TokenRemember tokenRemember, Token token) {
		tokenRemember.setUpdated(new Date());
		super.edit(tokenRemember, AuditHelper.OPERATION_UPDATE, token);
		return tokenRemember;
	}

	public TokenRemember deleteTokenRemember(int id, Token token) {
		TokenRemember tokenRemember = super.find(id);
		tokenRemember.setIsDelete(1);
		super.edit(tokenRemember, AuditHelper.OPERATION_DELETE, token);
		return tokenRemember;
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

