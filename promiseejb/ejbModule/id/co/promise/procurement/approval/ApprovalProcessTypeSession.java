package id.co.promise.procurement.approval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import id.co.promise.procurement.catalog.entity.CatalogHistory;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class ApprovalProcessTypeSession extends AbstractFacadeWithAudit<ApprovalProcessType> {

	final static Logger log = Logger.getLogger(ApprovalProcessTypeSession.class);

	public ApprovalProcessTypeSession() {
		super(ApprovalProcessType.class);
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

	@SuppressWarnings("unchecked")
	public List<ApprovalProcessType> getList() {
		return em.createNamedQuery("ApprovalProcessType.find").getResultList();
	}

	public ApprovalProcessType getById(Integer id) {
		return super.find(id);
	}

	public ApprovalProcessType create(ApprovalProcessType approvalProcessType, Token token) {
		approvalProcessType.setCreated(new Date());
		approvalProcessType.setIsDelete(0);
		approvalProcessType.setUserId(token.getUser().getId());
		super.create(approvalProcessType, AuditHelper.OPERATION_CREATE, token);
		return approvalProcessType;
	}

	public ApprovalProcessType update(ApprovalProcessType approvalProcessType, Token token) {
		approvalProcessType.setUpdated(new Date());
		approvalProcessType.setUserId(token.getUser().getId());
		super.edit(approvalProcessType, AuditHelper.OPERATION_UPDATE, token);
		return approvalProcessType;
	}

	public ApprovalProcessType delete(ApprovalProcessType approvalProcessType, Token token) {
		approvalProcessType.setDeleted(new Date());
		approvalProcessType.setIsDelete(1);
		approvalProcessType.setUserId(token.getUser().getId());
		super.edit(approvalProcessType, AuditHelper.OPERATION_DELETE, token);
		return approvalProcessType;
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcessType> findByValueId(Integer valueId) {
		Query q = em.createNamedQuery("ApprovalProcessType.findByValueId");
		q.setParameter("valueId", valueId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public ApprovalProcessType findByTahapanAndValueId(String tahapanName, int valueId) {
		Query q = em.createNamedQuery("ApprovalProcessType.findByTahapanAndValueId");
		q.setParameter("tahapanName", tahapanName);
		q.setParameter("valueId", valueId);
		List<ApprovalProcessType> approvalProcessTypeList = q.getResultList();
		if (approvalProcessTypeList != null && approvalProcessTypeList.size() > 0) {
			return approvalProcessTypeList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ApprovalProcessType findByTahapan(int tahapanId) {
		Query q = em.createNamedQuery("ApprovalProcessType.findByTahapan");
		q.setParameter("tahapanId", tahapanId);
		List<ApprovalProcessType> approvalProcessTypeList = q.getResultList();
		if (approvalProcessTypeList != null && approvalProcessTypeList.size() > 0) {
			return approvalProcessTypeList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ApprovalProcessType> findByPurchaseOrder(Integer id) {
		Query q = em.createQuery("SELECT T1 FROM ApprovalProcessType T1 WHERE T1.tahapan = 27 AND T1.valueId = :id");
		q.setParameter("id", id);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcessType> findByPurchaseRequest(Integer id) {
		Query q = em.createQuery("SELECT T1 FROM ApprovalProcessType T1 WHERE T1.tahapan = 26 AND T1.valueId = :id");
		q.setParameter("id", id);
		return q.getResultList();
	}
	
	public ApprovalProcessType findByPRId(Integer id) {
		Query q = em.createQuery("SELECT T1 FROM ApprovalProcessType T1 WHERE T1.tahapan.nama Like :tahapan AND T1.valueId = :id");
		q.setParameter("id", id);
		q.setParameter("tahapan", Constant.STAGES_PURCHASE_REQUEST);
		
		try{
			return (ApprovalProcessType) q.getSingleResult();
		}catch( Exception e){
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ApprovalProcessType findApprovalForApprovalBookingOrder(Integer valueId) {
		Query q = em.createNamedQuery("ApprovalProcessType.findApprovalForApprovalBookingOrder");
		q.setParameter("tahapanId", Constant.ONE_VALUE);
		q.setParameter("valueId", valueId);
		q.setMaxResults(1);
		return (ApprovalProcessType)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalProcessType> getListByCatalogId(Integer catalogId) {
		List<String> tahapanNama = new ArrayList<String>();
		tahapanNama.add(Constant.TAHAPAN_APPROVAL_EDIT_CATALOG);
		tahapanNama.add(Constant.TAHAPAN_APPROVAL_INSERT_CATALOG);
		Query query = em.createNamedQuery("ApprovalProcessType.findByCatalogId")
				.setParameter("catalogId", catalogId)
				.setParameter("tahapanNama", tahapanNama);
		List<ApprovalProcessType> aptList = (List<ApprovalProcessType>)query.getResultList();
		return aptList;
	}
}
