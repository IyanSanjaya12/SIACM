package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.JenisKontrak;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class JenisKontrakSession extends AbstractFacadeWithAudit<JenisKontrak> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public JenisKontrakSession(){
		super(JenisKontrak.class);
	}
	
	public List<JenisKontrak> findAll(){
		Query q = em.createNamedQuery("JenisKontrak.findAll");
		return q.getResultList();
	}
	
	public JenisKontrak createJenisKontrak(JenisKontrak jenisKontrak, Token token){
		jenisKontrak.setCreated(new Date());
		jenisKontrak.setIsDelete(0);
		super.create(jenisKontrak, AuditHelper.OPERATION_CREATE, token);
		return jenisKontrak;
	}
}
