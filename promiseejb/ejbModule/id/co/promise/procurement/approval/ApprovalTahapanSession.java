package id.co.promise.procurement.approval;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class ApprovalTahapanSession extends AbstractFacadeWithAudit<ApprovalTahapan>{
	
	@EJB
	private ApprovalSession approvalSession;
	
	public ApprovalTahapanSession() {
		super(ApprovalTahapan.class);
	}

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	public List<ApprovalTahapan> getList() {
		Query q = em.createNamedQuery("ApprovalTahapan.getList");
		return q.getResultList();
	}

	public ApprovalTahapan getById(Integer id) {
		return super.find(id);
	}

	public List<ApprovalTahapan> getListApprovalByTahapan(Tahapan tahapan){
	    Query q = em.createNamedQuery("ApprovalTahapan.getListApprovalByTahapan");
	    q.setParameter("tahapan", tahapan);
	    return q.getResultList();
	}
	
	public Boolean compareTahapan(Tahapan tahapan, Token token) {	
		Boolean isvalid = true;
		List<ApprovalTahapan> listTahapan = getListApprovalByTahapan(tahapan);
		
		for( ApprovalTahapan list : listTahapan){
			if(list.getTahapan() == tahapan ){
				isvalid = false;
			}
		}
		
		return isvalid;	
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkTahapan(Tahapan tahapan, String toDo, Integer tahapanId) {
		Query q = em.createNamedQuery("ApprovalTahapan.getApprovalTahapanByTahapan");
		q.setParameter("tahapan", tahapan);
		List<ApprovalTahapan> approvalTahapanList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
				if (tahapanId.equals(approvalTahapanList.get(0).getId())) {
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
	
	@SuppressWarnings("unchecked")
	public Boolean checkApprovalTahapan(Tahapan tahapan, Organisasi organisasi, String toDo, Integer tahapanId) {
		Query q = em.createNamedQuery("ApprovalTahapan.getApprovalTahapanByOrganisasiAndTahapan");
		q.setParameter("tahapan", tahapan);
		q.setParameter("organisasi", organisasi);
		List<ApprovalTahapan> approvalTahapanList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
				if (tahapanId.equals(approvalTahapanList.get(0).getId())) {
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
	
	
	@SuppressWarnings("unchecked")
	public ApprovalTahapan getApprovalTahapanByTahapan(Tahapan tahapan) {
		Query q = em.createQuery("SELECT approvalTahapan FROM ApprovalTahapan approvalTahapan WHERE approvalTahapan.isDelete = 0 and approvalTahapan.tahapan = :tahapan");
		q.setParameter("tahapan", tahapan);

		List<ApprovalTahapan> approvalTahapanList = q.getResultList();
		if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
			return approvalTahapanList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ApprovalTahapan getApprovalTahapanByTahapanAndOrganisasi(Tahapan tahapan, Organisasi organisasi) {
		Query q = em.createNamedQuery("ApprovalTahapan.getApprovalTahapanByOrganisasiAndTahapan");
		q.setParameter("tahapan", tahapan);
		q.setParameter("organisasi", organisasi);

		List<ApprovalTahapan> approvalTahapanList = q.getResultList();
		if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
			return approvalTahapanList.get(0);
		}
		return null;
	}	
	
	public ApprovalTahapan insert(ApprovalTahapan approvalTahapan, Token token) {
		approvalTahapan.setCreated(new Date());
		approvalTahapan.setIsDelete(0);
		super.create(approvalTahapan, AuditHelper.OPERATION_CREATE, token);
		return approvalTahapan;
	}

	public ApprovalTahapan update(ApprovalTahapan approvalTahapan, Token token) {
		super.edit(approvalTahapan, AuditHelper.OPERATION_UPDATE, token);
		return approvalTahapan;
	}

	public ApprovalTahapan delete(int id, Token token) {
		ApprovalTahapan approvalTahapan = super.find(id);
		approvalTahapan.setIsDelete(1);
		super.edit(approvalTahapan, AuditHelper.OPERATION_DELETE, token);
		return approvalTahapan;
	}
		
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public List<ApprovalTahapan> getListByOrganisasi(Organisasi organisasi) {
		Query q = em.createNamedQuery("ApprovalTahapan.getListByOrganisasi");
		q.setParameter("organisasi", organisasi);
		return q.getResultList();
	}

	public ApprovalTahapan getApprovalTahapanByOrganisasiAndTahapan(Integer organisasiId, String tahapanName) {
		Query q = em.createNamedQuery("ApprovalTahapan.getByOrganisasiAndTahapanName");
		q.setParameter("organisasiId", organisasiId);
		q.setParameter("tahapanName", tahapanName);
		List<ApprovalTahapan> approvalTahapan = q.getResultList();
		if (approvalTahapan != null && approvalTahapan.size() > 0) {
			return approvalTahapan.get(0);
		}
		
		return null;
	}
	
}
