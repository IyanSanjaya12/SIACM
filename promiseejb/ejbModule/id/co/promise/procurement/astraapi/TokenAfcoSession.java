package id.co.promise.procurement.astraapi;

import id.co.promise.procurement.entity.TokenAfco;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class TokenAfcoSession{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@SuppressWarnings("unchecked")
	public TokenAfco getByCode(String kodeToken) {
			TokenAfco tokenAfco = new TokenAfco();
        	Query query = em.createNamedQuery("TokenAfco.findByCode");
    		query.setParameter("kodeToken",kodeToken);
    		try{
    		tokenAfco = (TokenAfco) query.getSingleResult();
    		}catch(NoResultException e){
    			return null;
    		}
		return tokenAfco;
	}
	
	@SuppressWarnings("unchecked")
	public List<TokenAfco> getList() {
		List<TokenAfco> tokenApiAfcoList = null;
        	Query query = em.createNamedQuery("TokenAfco.find");
    		tokenApiAfcoList = query.getResultList();
		return tokenApiAfcoList;
	}
}
