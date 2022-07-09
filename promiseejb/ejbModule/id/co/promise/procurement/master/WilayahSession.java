package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Wilayah;
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
public class WilayahSession extends AbstractFacadeWithAudit<Wilayah> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public WilayahSession() {
		super(Wilayah.class);
	}

	public Wilayah getWilayah(int id){
		return super.find(id);
	}
	
	public List<Wilayah> getList() {
		Query q = em.createNamedQuery("Wilayah.findAll");
		return q.getResultList();
	}
	
	public List<Wilayah> getPropinsiList() {
		Query q = em.createNamedQuery("Wilayah.findPropinsi");
		return q.getResultList();
	}
	
	public List<Wilayah> getKotaList(Integer propinsiId) {
		Query q = em.createNamedQuery("Wilayah.findKota");
		q.setParameter("propinsiId", propinsiId);
		return q.getResultList();
	}
	
	public List<Wilayah> getPropinsiByNama(String nama) {
		Query q = em.createNamedQuery("Wilayah.findPropinsiByNama");
		q.setParameter("nama", nama);
		return q.getResultList();
	}
	public List<Wilayah> getKotaByNama(String nama) {
		Query q = em.createNamedQuery("Wilayah.findKotaByNama");
		q.setParameter("nama", nama);
		return q.getResultList();
	}
	
	public Wilayah insertWilayah(Wilayah wilayah, Token token){
		wilayah.setCreated(new Date());
		wilayah.setIsDelete(0);
		super.create(wilayah, AuditHelper.OPERATION_CREATE, token);
		return wilayah;		
	}
	
	public Wilayah updateWilayah(Wilayah wilayah, Token token){
		wilayah.setUpdated(new Date());
		super.edit(wilayah, AuditHelper.OPERATION_UPDATE, token);
		return wilayah;
	}
	
	public Wilayah deleteWilayah(Integer id, Token token){
		Wilayah wilayah = super.find(id);
		wilayah.setDeleted(new Date());
		wilayah.setIsDelete(1);
		super.edit(wilayah, AuditHelper.OPERATION_DELETE, token);
		return wilayah;
	}
	
	public Wilayah deleteRowWilayah(Integer id, Token token){
		Wilayah wilayah = super.find(id);
		super.remove(wilayah, AuditHelper.OPERATION_ROW_DELETE, token);
		return wilayah;
	}
	
	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}
	
	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

}
