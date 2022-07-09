package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.AnggotaPanitia;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class AnggotaPanitiaSession extends AbstractFacadeWithAudit<AnggotaPanitia>{
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public AnggotaPanitiaSession(){
		super(AnggotaPanitia.class);
	}
	
	public AnggotaPanitia getAnggotaPanitiaId(int id){
		return super.find(id);
	}
	
	public List<AnggotaPanitia> getAnggotaPanitiaList(){
		Query q = em.createNamedQuery("AnggotaPanitia.find");
		return q.getResultList();
	}
	
	public List<AnggotaPanitia> getAnggotaPanitiaByTimPanitiaList(int id){
		Query q = em.createNamedQuery("AnggotaPanitia.findByTimPanitia");
		q.setParameter("timPanitiaId", id);
		return q.getResultList();
	}
	
	public List<AnggotaPanitia> getAnggotaPanitiaByRoleUserList(int id){
		Query q = em.createNamedQuery("AnggotaPanitia.findByRoleUser");
		q.setParameter("userId", id);
		return q.getResultList();
	}
	
	public AnggotaPanitia insertAnggotaPanitia(AnggotaPanitia x, Token token){
		x.setCreated(new Date());
		x.setIsDelete(0);
		super.create(x, AuditHelper.OPERATION_CREATE, token);
		return x;
	}

	public AnggotaPanitia updateAnggotaPanitia(AnggotaPanitia x, Token token){
		x.setUpdated(new Date());
		super.edit(x, AuditHelper.OPERATION_UPDATE, token);
		return x;
	}
	
	public AnggotaPanitia deleteAnggotaPanitia(int id, Token token){
		AnggotaPanitia x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}
	
	public AnggotaPanitia deleteRowAnggotaPanitia(int id, Token token){
		AnggotaPanitia x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}
	
	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
}
