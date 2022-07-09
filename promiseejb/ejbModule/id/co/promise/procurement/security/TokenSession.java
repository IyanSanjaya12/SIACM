package id.co.promise.procurement.security;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.utils.AbstractFacade;

@LocalBean
@Stateless
public class TokenSession extends AbstractFacade<Token> {
	final static Logger log = Logger.getLogger(TokenSession.class);
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

	public TokenSession() {
		super(Token.class);
	}
	
	public int getOnlineUser(){
		Query q = em.createNamedQuery("Token.findByActive")
				.setParameter("now", new Date());
		Object data = q.getSingleResult();
		return (data != null) ? Integer.parseInt(data.toString()): 0;
	}

	public Token findByToken(String token) {
		Query q = em.createNamedQuery("Token.findByToken").setParameter(
				"token", token);
		try {
			return (Token) q.getSingleResult();
		} catch (Exception e) {	
			return null;
		}
	}

	public Token getAuthentification(String strToken) {
		Query q = em.createNamedQuery("Token.findByToken").setParameter(
				"token", strToken);
		try {
			Token token = (Token) q.getSingleResult();
			Date date = new Date();
			if (date.getTime() > token.getTimeout().getTime()) {
				token.setLogout(date);
				super.edit(token);
				return null;
			} else if (token.getLogout() != null) {
				return null;
			} else {
				token.setTimeout(new Date(date.getTime()
						+ (30 * ONE_MINUTE_IN_MILLIS)));
				return token;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("Token error : "+strToken);
			return null;
		}
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
