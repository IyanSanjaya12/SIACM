package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.entity.TimPanitia;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class PejabatPelaksanaPengadaanSession extends AbstractFacadeWithAudit<PejabatPelaksanaPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public PejabatPelaksanaPengadaanSession(){
		super(PejabatPelaksanaPengadaan.class);
	}
	
	public PejabatPelaksanaPengadaan getPelaksanaPengadaanId(int id){
		return super.find(id);
	}
	
	public List<PejabatPelaksanaPengadaan> getPelaksanaPengadaanList(){
		Query q = em.createNamedQuery("PejabatPelaksanaPengadaan.find");
		return q.getResultList();
	}
	
	public List<PejabatPelaksanaPengadaan> getPelaksanaPengadaanByPanitiaList(int id){
		Query q = em.createNamedQuery("PejabatPelaksanaPengadaan.findByPanitia");
		q.setParameter("panitiaId", id);
		return q.getResultList();
	}

	public List<PejabatPelaksanaPengadaan> getPejabatPelaksanaByRoleUserList(int id){
		Query q = em.createNamedQuery("PejabatPelaksanaPengadaan.findByRoleUser");
		q.setParameter("userId", id);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaPejabatPelaksanaPengadaan(String nama, String toDo, Integer pejabatPelaksanaPengadaanId) {
		Query q = em.createNamedQuery("PejabatPelaksanaPengadaan.findByNama");
		q.setParameter("nama", nama);
		List<PejabatPelaksanaPengadaan> pejabatPelaksanaPengadaan = q.getResultList();
		  
		Boolean isSave = false;
		if(toDo.equalsIgnoreCase("insert")) {
			if(pejabatPelaksanaPengadaan != null && pejabatPelaksanaPengadaan.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
		   
		} else if (toDo.equalsIgnoreCase("update")) {
		   if(pejabatPelaksanaPengadaan != null && pejabatPelaksanaPengadaan.size() > 0) {
			   if(pejabatPelaksanaPengadaanId.equals( pejabatPelaksanaPengadaan.get(0).getId())) {
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
	
	public PejabatPelaksanaPengadaan insertPelaksanaPengadaan(PejabatPelaksanaPengadaan x, Token token){
		x.setCreated(new Date());
		x.setIsDelete(0);
		super.create(x, AuditHelper.OPERATION_CREATE, token);
		return x;
	}
	
	public PejabatPelaksanaPengadaan updatePelaksanaPengadaan(PejabatPelaksanaPengadaan x, Token token){
		x.setUpdated(new Date());
		super.edit(x, AuditHelper.OPERATION_UPDATE, token);
		return x;
	}
	
	public PejabatPelaksanaPengadaan deletePelaksanaPengadaan(int id, Token token){
		PejabatPelaksanaPengadaan x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}
	
	public PejabatPelaksanaPengadaan deleteRowPelaksanaPengadaan(int id, Token token){
		PejabatPelaksanaPengadaan x = super.find(id);
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
