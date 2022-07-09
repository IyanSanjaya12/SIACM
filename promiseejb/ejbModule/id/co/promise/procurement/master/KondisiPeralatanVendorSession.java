package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.KondisiPeralatanVendor;
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
public class KondisiPeralatanVendorSession extends AbstractFacadeWithAudit<KondisiPeralatanVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public KondisiPeralatanVendorSession() {
		super(KondisiPeralatanVendor.class);
	}
	
	public KondisiPeralatanVendor getKondisiPeralatanVendor(int id){
		return super.find(id);
	}
	
	public List<KondisiPeralatanVendor> getKondisiPeralatanVendorList(){
		Query q = em.createNamedQuery("KondisiPeralatanVendor.find");
		return q.getResultList();
	}

	public KondisiPeralatanVendor insertKondisiPeralatanVendor(KondisiPeralatanVendor kondisiPeralatanVendor, Token token) {
		kondisiPeralatanVendor.setCreated(new Date());
		kondisiPeralatanVendor.setIsDelete(0);
		super.create(kondisiPeralatanVendor, AuditHelper.OPERATION_CREATE, token);
		return kondisiPeralatanVendor;
	}

	public KondisiPeralatanVendor updateKondisiPeralatanVendor(KondisiPeralatanVendor kondisiPeralatanVendor, Token token) {
		kondisiPeralatanVendor.setUpdated(new Date());
		super.edit(kondisiPeralatanVendor, AuditHelper.OPERATION_UPDATE, token);
		return kondisiPeralatanVendor;
	}

	public KondisiPeralatanVendor deleteKondisiPeralatanVendor(int id, Token token) {
		KondisiPeralatanVendor kondisiPeralatanVendor = super.find(id);
		kondisiPeralatanVendor.setIsDelete(1);
		kondisiPeralatanVendor.setDeleted(new Date());

		super.edit(kondisiPeralatanVendor, AuditHelper.OPERATION_DELETE, token);
		return kondisiPeralatanVendor;
	}

	public KondisiPeralatanVendor deleteRowKondisiPeralatanVendor(int id, Token token) {
		KondisiPeralatanVendor kondisiPeralatanVendor = super.find(id);
		super.remove(kondisiPeralatanVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return kondisiPeralatanVendor;
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
