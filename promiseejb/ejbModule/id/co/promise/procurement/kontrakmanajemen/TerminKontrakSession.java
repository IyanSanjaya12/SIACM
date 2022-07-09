package id.co.promise.procurement.kontrakmanajemen;

import id.co.promise.procurement.entity.TerminKontrak;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class TerminKontrakSession extends AbstractFacadeWithAudit<TerminKontrak> {
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private KontrakSession kontrakSession;
	
	public TerminKontrakSession(){
		super(TerminKontrak.class);
	}	
	
	public TerminKontrak insertTerminKontrak(TerminKontrak terminKontrak, Token token){
		terminKontrak.setCreated(new Date());
		terminKontrak.setIsDelete(0);
		super.create(terminKontrak, AuditHelper.OPERATION_CREATE, token);
		return terminKontrak;
	}
	
	public TerminKontrak updateTerminKontrak(TerminKontrak terminKontrak, Token token) {
		terminKontrak.setUpdated(new Date());
		super.edit(terminKontrak, AuditHelper.OPERATION_UPDATE, token);
		return terminKontrak;
	}
	
	public TerminKontrak deleteTerminKontrak(int id, Token token){
		TerminKontrak terminKontrak = super.find(id);
		terminKontrak.setIsDelete(1);
		terminKontrak.setDeleted(new Date());
		super.edit(terminKontrak, AuditHelper.OPERATION_DELETE, token);
		return terminKontrak;
	}
	
	public List<TerminKontrak> getTerminKontrakList(){
		Query q = em.createNamedQuery("TerminKontrak.find");
		return q.getResultList();
	}
	
	public List<TerminKontrak> getTerminKontrakByVendor(Integer kontrakId) {
		Query q = em.createNamedQuery("TerminKontrak.findByKontrak").setParameter("kontrakId", kontrakId);
		return q.getResultList();
	}
	
	public TerminKontrak deleteRowTerminKontrak(int id, Token token){
		TerminKontrak terminKontrak = super.find(id);
		super.remove(terminKontrak, AuditHelper.OPERATION_ROW_DELETE, token);
		return terminKontrak;
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
