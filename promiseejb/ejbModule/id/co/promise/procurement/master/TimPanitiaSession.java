package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.TimPanitia;
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
public class TimPanitiaSession extends AbstractFacadeWithAudit<TimPanitia> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public TimPanitiaSession(){
		super (TimPanitia.class);
	}
	
	public TimPanitia getPanitiaPengadaanId(int id){
		return super.find(id);
	}
	
	public List<TimPanitia> getPanitiaPengadaanList(){
		Query q = em.createNamedQuery("TimPanitia.find");
		return q.getResultList();
	}
	
	public List<TimPanitia> getPanitiaPengadaanByPanitiaList(int id){
		Query q = em.createNamedQuery("TimPanitia.findByPanitia");
		q.setParameter("panitiaId", id);
		return q.getResultList();
	}
	
	public TimPanitia insertPanitiaPengadaan(TimPanitia x, Token token){
		x.setCreated(new Date());
		x.setIsDelete(0);
		super.create(x, AuditHelper.OPERATION_CREATE, token);
		return x;
	}
	
	public TimPanitia updatePanitiaPengadaan(TimPanitia x, Token token){
		x.setUpdated(new Date());
		super.edit(x, AuditHelper.OPERATION_UPDATE, token);
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaTimPanitia(String nama, String toDo, Integer timPanitiaId) {
		Query q = em.createNamedQuery("TimPanitia.findByNama");
		q.setParameter("nama", nama);
		List<TimPanitia> timPanitia = q.getResultList();
		  
		Boolean isSave = false;
		if(toDo.equalsIgnoreCase("insert")) {
			if(timPanitia != null && timPanitia.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
		   
		} else if (toDo.equalsIgnoreCase("update")) {
		   if(timPanitia != null && timPanitia.size() > 0) {
			   if(timPanitiaId.equals( timPanitia.get(0).getId())) {
				   isSave = true;
			   } else {
				   isSave = false;
			   }
		   } else {
			   isSave = true;
		   }
		}
		  
		return isSave;
		  
	}
	
	public TimPanitia deletePanitiaPengadaan(int id, Token token){
		TimPanitia x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}
	
	public TimPanitia deleteRowPanitiaPengadaan(int id, Token token){
		TimPanitia x = super.find(id);
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
