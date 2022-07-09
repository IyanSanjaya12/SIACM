package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.IndikatorPenilaian;
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
public class IndikatorPenilaianSession extends AbstractFacadeWithAudit<IndikatorPenilaian> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public IndikatorPenilaianSession() {
		super(IndikatorPenilaian.class);
	}

	@SuppressWarnings("unchecked")
	public List<IndikatorPenilaian> getIndikatorPenilaianList() {
		Query q = em.createQuery("SELECT t1 FROM IndikatorPenilaian t1 WHERE iPIsDelete = :iPIsDelete ORDER BY t1.iPBobot DESC");
		q.setParameter("iPIsDelete", 0);
		return q.getResultList();
	}

	public IndikatorPenilaian getIndikatorPenilaianById(int IndikatorPenilaianId) {
		return super.find(IndikatorPenilaianId);
	}

	public IndikatorPenilaian createIndikatorPenilaian(IndikatorPenilaian ap, Token token) {
		ap.setiPCreated(new Date());
		ap.setiPIsDelete(0);
		super.create(ap, AuditHelper.OPERATION_CREATE, token);
		return ap;
	}

	public IndikatorPenilaian editIndikatorPenilaian(IndikatorPenilaian ap, Token token) {
		ap.setiPUpdated(new Date());
		super.edit(ap, AuditHelper.OPERATION_UPDATE, token);
		return ap;
	}

	public IndikatorPenilaian deleteIndikatorPenilaian(int id, Token token) {
		IndikatorPenilaian bu = super.find(id);
		bu.setiPIsDelete(1);
		bu.setiPDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkKodeIndikatorPenilaian(String kode, String toDo, Integer indikatorPenilaianId) {
		
		Query q = em.createNamedQuery("IndikatorPenilaian.findByKode");
		q.setParameter("kode", kode);
		
		List<IndikatorPenilaian> indikatorPenilaianList = q.getResultList();	  
		
		Boolean isSave = false;
	  
		if(toDo.equalsIgnoreCase("insert")) {
			if(indikatorPenilaianList != null && indikatorPenilaianList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
	   
		} else if (toDo.equalsIgnoreCase("update")) {
			if(indikatorPenilaianList!= null && indikatorPenilaianList.size() > 0) {
				if(indikatorPenilaianId.equals(indikatorPenilaianList.get(0).getiPId())) {
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

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() { 
		return ema;
	}

}
