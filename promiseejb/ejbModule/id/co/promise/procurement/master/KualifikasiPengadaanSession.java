package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.KualifikasiPengadaan;
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
public class KualifikasiPengadaanSession extends AbstractFacadeWithAudit<KualifikasiPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public KualifikasiPengadaanSession(){
		super(KualifikasiPengadaan.class);
	}

	public KualifikasiPengadaan getKualifikasiPengadaan(int id){
		return super.find(id);
	}
	
	public List<KualifikasiPengadaan> getKualifikasiPengadaanList(){
		Query q = em.createNamedQuery("KualifikasiPengadaan.find");
		return q.getResultList();
	}
	
	public KualifikasiPengadaan insertKualifikasiPengadaan(KualifikasiPengadaan kualifikasiPengadaan, Token token){
		kualifikasiPengadaan.setCreated(new Date());
		kualifikasiPengadaan.setIsDelete(0);
		super.create(kualifikasiPengadaan, AuditHelper.OPERATION_CREATE, token);
		return kualifikasiPengadaan;
	}
	
	public KualifikasiPengadaan updateKualifikasiPengadaan(KualifikasiPengadaan kualifikasiPengadaan, Token token){
		kualifikasiPengadaan.setUpdated(new Date());
		super.edit(kualifikasiPengadaan, AuditHelper.OPERATION_UPDATE, token);
		return kualifikasiPengadaan;
	}
	
	public KualifikasiPengadaan deleteKualifikasiPengadaan(int id, Token token){
		KualifikasiPengadaan kualifikasiPengadaan = super.find(id);
		kualifikasiPengadaan.setIsDelete(1);
		kualifikasiPengadaan.setDeleted(new Date());
		super.edit(kualifikasiPengadaan, AuditHelper.OPERATION_DELETE, token);
		return kualifikasiPengadaan;
	}
	
	public KualifikasiPengadaan deleteRowKualifikasiPengadaan(int id, Token token){
		KualifikasiPengadaan kualifikasiPengadaan = super.find(id);
			super.remove(kualifikasiPengadaan, AuditHelper.OPERATION_ROW_DELETE, token);
		return kualifikasiPengadaan;
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
