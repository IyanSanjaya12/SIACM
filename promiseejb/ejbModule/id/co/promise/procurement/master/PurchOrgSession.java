package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchOrg;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class PurchOrgSession extends AbstractFacadeWithAudit<PurchOrg> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	OrganisasiSession organisasiSession;

	public PurchOrgSession() {
		super(PurchOrg.class);
	}

	public PurchOrg findId(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<PurchOrg> getListByOrganisasi(Integer organisasiId) {
		Query q = em.createNamedQuery("PurchOrg.getByOrganisasiId");
		q.setParameter("organisasiId", organisasiId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchOrg> getAllCode() {
		Query q = em.createNamedQuery("PurchOrg.getAllCode");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchOrg> getListByParentId(Integer parentId) {
		Query q = em.createNamedQuery("PurchOrg.findByParentId")
				.setParameter("parentId", parentId);
		return q.getResultList();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<PurchOrg> getListByOrganisasiLevel2(Integer organisasiId) {
		
			Organisasi organisasi = organisasiSession.find(organisasiId);
					
			Organisasi organisasiLevel = organisasiSession.find(organisasi.getParentId());
			
			if(organisasiLevel.getParentId() != null) {
				organisasi=organisasiSession.find(organisasi.getParentId());
			}
		
		Query q = em.createNamedQuery("PurchOrg.getByOrganisasiId");
		q.setParameter("organisasiId", organisasi.getId());
		return q.getResultList();
	}



	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
}
