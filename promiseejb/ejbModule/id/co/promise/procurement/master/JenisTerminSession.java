package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.JenisTermin;
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
public class JenisTerminSession extends AbstractFacadeWithAudit<JenisTermin> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public JenisTerminSession() {
		super(JenisTermin.class);
	}
	
	public JenisTermin getJenisTermin(int id){
		return super.find(id);
	}
	
	public List<JenisTermin> getJenisTerminList(){
		Query q = em.createNamedQuery("JenisTermin.find");
		return q.getResultList();
	}

	public JenisTermin insertJenisTermin(JenisTermin jenisTermin, Token token) {
		jenisTermin.setCreated(new Date());
		jenisTermin.setIsDelete(0);
		super.create(jenisTermin, AuditHelper.OPERATION_CREATE, token);
		return jenisTermin;
	}

	public JenisTermin updateJenisTermin(JenisTermin jenisTermin, Token token) {
		jenisTermin.setUpdated(new Date());
		super.edit(jenisTermin, AuditHelper.OPERATION_UPDATE, token);
		return jenisTermin;
	}

	public JenisTermin deleteJenisTermin(int id, Token token) {
		JenisTermin jenisTermin = super.find(id);
		jenisTermin.setIsDelete(1);
		jenisTermin.setDeleted(new Date());

		super.edit(jenisTermin, AuditHelper.OPERATION_DELETE, token);
		return jenisTermin;
	}

	public JenisTermin deleteRowJenisTermin(int id, Token token) {
		JenisTermin jenisTermin = super.find(id);
		super.remove(jenisTermin, AuditHelper.OPERATION_ROW_DELETE, token);
		return jenisTermin;
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
