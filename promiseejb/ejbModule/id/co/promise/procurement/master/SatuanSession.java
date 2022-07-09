package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Satuan;
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
public class SatuanSession extends AbstractFacadeWithAudit<Satuan>{
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public SatuanSession(){
		super(Satuan.class);
	}
	
	public Satuan getSatuan(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Satuan> getSatuanList(){
		Query q = em.createNamedQuery("Satuan.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Satuan> getSatuanListByNameEqual(String pNama) {
		Query q = em.createQuery("select s from Satuan s where s.nama = :pNama");
		q.setParameter("pNama", pNama);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Satuan getSatuanByNama(String namaSatuan){
		Query q = em.createQuery("select s from Satuan s where s.nama = :namaSatuan and s.isDelete = 0");
		q.setParameter("namaSatuan", namaSatuan);
		List<Satuan> satuanList=q.getResultList();
		if (satuanList != null && satuanList.size() > 0) {
			return satuanList.get(0);
		}
		return null;
	}


	public Satuan insertSatuan(Satuan st, Token token){
		st.setCreated(new Date());
		st.setIsDelete(0);
		super.create(st, AuditHelper.OPERATION_CREATE, token);
		return st;
	}
	
	public Satuan updateSatuan(Satuan st, Token token){
		st.setUpdated(new Date());
		super.edit(st, AuditHelper.OPERATION_UPDATE, token);
		return st;
	}
	
	public Satuan deleteSatuan(int id, Token token){
		Satuan st = super.find(id);
		st.setIsDelete(1);
		st.setDeleted(new Date());
		super.edit(st, AuditHelper.OPERATION_DELETE, token);
		return st;
	}
	
	@SuppressWarnings("unchecked")
	public Satuan getByCode(String code) {
		Query q = em.createNamedQuery("Satuan.findByCode");
		q.setParameter("code", code);
		List<Satuan> satuanList = q.getResultList();
		if (satuanList != null && satuanList.size() > 0) {
			return satuanList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaSatuan(String nama, String toDo, Integer satuanId) {
		Query q = em.createNamedQuery("Satuan.findNama");
		q.setParameter("nama", nama);
		List<Satuan> satuan = q.getResultList();
		  
		Boolean isSave = false;
		if(toDo.equalsIgnoreCase("insert")) {
			if(satuan != null && satuan.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
		   
		} else if (toDo.equalsIgnoreCase("update")) {
		   if(satuan != null && satuan.size() > 0) {
			   if(satuanId.equals( satuan.get(0).getId())) {
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
	
	public Satuan deleteRowSatuan(int id, Token token){
		Satuan st = super.find(id);
		super.remove(st, AuditHelper.OPERATION_ROW_DELETE, token);
		return st;
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
