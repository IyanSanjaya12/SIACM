package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.OrgApprovalPath;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class OrgApprovalPathSession extends AbstractFacadeWithAudit<OrgApprovalPath> {

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

	public OrgApprovalPathSession() {
		super(OrgApprovalPath.class);
	}

	public OrgApprovalPath getOrgApprovalPath(int id) {
		return super.find(id);
	}
	
	public OrgApprovalPath insert(OrgApprovalPath orgApprovalPath, Token token){
		orgApprovalPath.setCreated(new Date());
		orgApprovalPath.setIsDelete(0);
		super.create(orgApprovalPath, AuditHelper.OPERATION_CREATE, token);
		return orgApprovalPath;
	}
	
	public OrgApprovalPath update(OrgApprovalPath orgApprovalPath, Token token){
		orgApprovalPath.setUpdated(new Date());
		super.edit(orgApprovalPath, AuditHelper.OPERATION_UPDATE, token);
		return orgApprovalPath;
	}
	
	public OrgApprovalPath delete(int approvalPathId, Token token) {
		OrgApprovalPath orgApprovalPath = super.find(approvalPathId);
		orgApprovalPath.setIsDelete(1);
		super.edit(orgApprovalPath, AuditHelper.OPERATION_DELETE, token);
		return orgApprovalPath;
	}

	@SuppressWarnings("unchecked")
	public List<OrgApprovalPath> getByOrganisasiId(Integer organisasiId) {
		Query q = em.createNamedQuery("OrgApprovalPath.getByOrganisasiId");
		q.setParameter("organisasiId", organisasiId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<OrgApprovalPath> getList(){
		Query q = em.createNamedQuery("OrgApprovalPath.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkPathId(Integer pathId, String toDo, Integer id) {
		Query q = em.createNamedQuery("OrgApprovalPath.getByAppPathId");
		q.setParameter("approvalPathId", pathId);
		List<OrgApprovalPath> orgApprovalPathList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (orgApprovalPathList != null && orgApprovalPathList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (orgApprovalPathList != null && orgApprovalPathList.size() > 0) {
				if (id.equals(orgApprovalPathList.get(0).getId())) {
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
	public Boolean checkPathName(String approvalPathName, String toDo, Integer id) {
		Query q = em.createNamedQuery("OrgApprovalPath.getByAppPathName");
		q.setParameter("approvalPathName", approvalPathName);
	
		List<OrgApprovalPath> orgApprovalPathList = q.getResultList();
		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (orgApprovalPathList != null && orgApprovalPathList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (orgApprovalPathList != null && orgApprovalPathList.size() > 0) {
				if (id.equals(orgApprovalPathList.get(0).getId())) {
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
}
