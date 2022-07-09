package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Karyawan;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class KaryawanSession extends AbstractFacadeWithAudit<Karyawan>{

	public KaryawanSession() {
		super(Karyawan.class);
	}

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	public Karyawan getKaryawan(int id) {
		return super.find(id);
	}
	
	public Karyawan getkaryawan(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Karyawan> getkaryawanList(){
		Query query = em.createQuery("SELECT karyawan FROM Karyawan karyawan WHERE karyawan.isDelete = 0");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Karyawan> getkaryawanListByJabatan(String kodeJabatan){
		Query query = em.createQuery("SELECT karyawan FROM Karyawan karyawan WHERE karyawan.isDelete = 0 AND karyawan.position.kode = :kodeJabatan");
		query.setParameter("kodeJabatan", kodeJabatan);
		return query.getResultList();
	}
	
	public Karyawan insert(Karyawan karyawan, Token token) {
		karyawan.setCreated(new Date());
		karyawan.setIsDelete(0);
		super.create(karyawan, AuditHelper.OPERATION_CREATE, token);
		return karyawan;
	}

	public Karyawan update(Karyawan karyawan, Token token) {
		karyawan.setUpdated(new Date());
		super.edit(karyawan, AuditHelper.OPERATION_UPDATE, token);
		return karyawan;
	}

	public Karyawan delete(int id, Token token) {
		Karyawan karyawan = super.find(id);
		karyawan.setIsDelete(1);
		karyawan.setDeleted(new Date());
		super.edit(karyawan, AuditHelper.OPERATION_DELETE, token);
		return karyawan;
	}

}
