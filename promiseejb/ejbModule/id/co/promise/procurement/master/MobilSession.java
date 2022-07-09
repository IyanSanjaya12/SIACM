package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Mobil;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class MobilSession extends AbstractFacadeWithAudit<Mobil>{
	public MobilSession() {
		super(Mobil.class);
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
	
	public Mobil getMobil(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Mobil> getMobilList(){
		Query query = em.createQuery("SELECT mobil FROM Mobil mobil WHERE mobil.isDelete = 0");
		return query.getResultList();
	}
	
	public Mobil insert(Mobil mobil, Token token) {
		mobil.setCreated(new Date());
		mobil.setIsDelete(0);
		super.create(mobil, AuditHelper.OPERATION_CREATE, token);
		return mobil;
	}

	public Mobil update(Mobil mobil, Token token) {
		mobil.setUpdated(new Date());
		super.edit(mobil, AuditHelper.OPERATION_UPDATE, token);
		return mobil;
	}

	public Mobil delete(int id, Token token) {
		Mobil mobil = super.find(id);
		mobil.setIsDelete(1);
		mobil.setDeleted(new Date());
		super.edit(mobil, AuditHelper.OPERATION_DELETE, token);
		return mobil;
	}
}
