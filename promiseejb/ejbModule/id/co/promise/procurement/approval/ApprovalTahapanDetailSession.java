package id.co.promise.procurement.approval;

import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.ApprovalTahapanDetail;
import id.co.promise.procurement.entity.ApprovalTahapanDetail;
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
public class ApprovalTahapanDetailSession extends AbstractFacadeWithAudit<ApprovalTahapanDetail>{
	
	@EJB
	private ApprovalSession approvalSession;
	
	public ApprovalTahapanDetailSession() {
		super(ApprovalTahapanDetail.class);
	}

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	public List<ApprovalTahapanDetail> getList() {
		Query q = em.createNamedQuery("ApprovalTahapanDetail.getList");
		return q.getResultList();
	}
	
	public void deleteByApprovalTahapanId(Integer approvalTahapanId) {
		Query q = em.createNamedQuery("ApprovalTahapanDetail.deleteByApprovalTahapanId");
		q.setParameter("approvalTahapanId", approvalTahapanId);
		q.executeUpdate();
	}

	public ApprovalTahapanDetail getById(Integer id) {
		return super.find(id);
	}

	public List<ApprovalTahapan> getListApprovalByTahapan(Tahapan tahapan){
	    Query q = em.createNamedQuery("ApprovalTahapanDetail.getListApprovalByTahapan");
	    q.setParameter("tahapan", tahapan);
	    return q.getResultList();
	}
	
	public List<ApprovalTahapanDetail> getListByApprovalTahapanId (Integer id){
		 Query q = em.createNamedQuery("ApprovalTahapanDetail.getListByApprovalTahapanId");
		    q.setParameter("id", id);
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
		Query q = em.createNamedQuery("ApprovalTahapanDetail.getApprovalTahapanByTahapan");
		q.setParameter("tahapan", tahapan);
		List<ApprovalTahapanDetail> approvalTahapanList = q.getResultList();

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
	public Boolean checkApprovalTahapan(Tahapan tahapan, Approval approval, String toDo, Integer tahapanId) {
		Query q = em.createNamedQuery("ApprovalTahapanDetail.getApprovalTahapanByApprovalAndTahapan");
		q.setParameter("tahapan", tahapan);
		q.setParameter("approval", approval);
		List<ApprovalTahapanDetail> approvalTahapanList = q.getResultList();

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
	public ApprovalTahapanDetail getApprovalTahapanByTahapan(Tahapan tahapan) {
		Query q = em.createQuery("SELECT approvalTahapanDetail FROM ApprovalTahapanDetail approvalTahapanDetail WHERE approvalTahapanDetail.isDelete = 0 and approvalTahapanDetail.approvalTahapan.tahapan =:tahapan");
		q.setParameter("tahapan", tahapan);

		List<ApprovalTahapanDetail> approvalTahapanList = q.getResultList();
		if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
			return approvalTahapanList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ApprovalTahapanDetail getApprovalTahapanByApprovalAndTahapan(Approval approval, Tahapan tahapan) {
		Query q = em.createQuery("SELECT approvalTahapanDetail FROM ApprovalTahapanDetail approvalTahapanDetail WHERE approvalTahapanDetail.isDelete = 0 "
				+ "and approvalTahapanDetail.tahapan = :tahapan and approvalTahapanDetail.approval = :approval");
		q.setParameter("tahapan", tahapan);
		q.setParameter("approval", approval);

		List<ApprovalTahapanDetail> approvalTahapanList = q.getResultList();
		if (approvalTahapanList != null && approvalTahapanList.size() > 0) {
			return approvalTahapanList.get(0);
		}
		return null;
	}	
	
	public ApprovalTahapanDetail insert(ApprovalTahapanDetail approvalTahapanDetail, Token token) {
		approvalTahapanDetail.setCreated(new Date());
		approvalTahapanDetail.setIsDelete(0);
		super.create(approvalTahapanDetail, AuditHelper.OPERATION_CREATE, token);
		return approvalTahapanDetail;
	}

	public ApprovalTahapanDetail update(ApprovalTahapanDetail approvalTahapanDetail, Token token) {
		super.edit(approvalTahapanDetail, AuditHelper.OPERATION_UPDATE, token);
		return approvalTahapanDetail;
	}

	public ApprovalTahapanDetail delete(int id, Token token) {
		ApprovalTahapanDetail approvalTahapanDetail = super.find(id);
		approvalTahapanDetail.setIsDelete(1);
		super.edit(approvalTahapanDetail, AuditHelper.OPERATION_DELETE, token);
		return approvalTahapanDetail;
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
