package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Pelanggan;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class PelangganSession extends AbstractFacadeWithAudit<Pelanggan>{

	public PelangganSession() {
		super(Pelanggan.class);
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
	
	public Pelanggan getPelanggan(int id) {
		return super.find(id);
	}
	
	public Pelanggan getpelanggan(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pelanggan> getpelangganList(){
		Query query = em.createQuery("SELECT pelanggan FROM Pelanggan pelanggan WHERE pelanggan.isDelete = 0");
		return query.getResultList();
	}
	
	public Pelanggan insert(Pelanggan pelanggan, Token token) {
		pelanggan.setCreated(new Date());
		pelanggan.setIsDelete(0);
		super.create(pelanggan, AuditHelper.OPERATION_CREATE, token);
		return pelanggan;
	}

	public Pelanggan update(Pelanggan pelanggan, Token token) {
		pelanggan.setUpdated(new Date());
		super.edit(pelanggan, AuditHelper.OPERATION_UPDATE, token);
		return pelanggan;
	}

	public Pelanggan delete(int id, Token token) {
		Pelanggan pelanggan = super.find(id);
		pelanggan.setIsDelete(1);
		pelanggan.setDeleted(new Date());
		super.edit(pelanggan, AuditHelper.OPERATION_DELETE, token);
		return pelanggan;
	}

}
