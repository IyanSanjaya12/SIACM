package id.co.promise.procurement.vendor.management;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.UnblacklistVendorFile;
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
public class UnblacklistVendorFileSession  extends AbstractFacadeWithAudit<UnblacklistVendorFile> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public UnblacklistVendorFileSession() {
		super(UnblacklistVendorFile.class);
	}

	@SuppressWarnings("unchecked")
	public List<UnblacklistVendorFile> getUnblacklistVendorFileList() {
		Query q = em.createQuery("SELECT t1 FROM UnblacklistVendorFile t1 WHERE t1.unblacklistFileIsDelete = :unblacklistFileIsDelete ORDER BY t1.unblacklistFileId ASC");
		q.setParameter("unblacklistFileIsDelete", 0);
		return q.getResultList();
	} 

	@SuppressWarnings("unchecked")
	public List<UnblacklistVendorFile> getUnblacklistVendorFileListByUnblacklistId(Integer unblacklistVendorId) {
		Query q = em.createQuery("SELECT t1 FROM UnblacklistVendorFile t1 WHERE t1.unblacklistFileIsDelete = :unblacklistFileIsDelete "
				+ "AND t1.unblacklistVendor.unblacklistVendorId = :unblacklistVendorId "
				+ "ORDER BY t1.unblacklistFileId ASC");
		q.setParameter("unblacklistFileIsDelete", 0);
		q.setParameter("unblacklistVendorId", unblacklistVendorId);
		return q.getResultList();
	} 
	
	public UnblacklistVendorFile getUnblacklistVendorFileById(int UnblacklistVendorFileId) {
		return super.find(UnblacklistVendorFileId);
	}

	public UnblacklistVendorFile createUnblacklistVendorFile(UnblacklistVendorFile ap, Token token) {
		ap.setUnblacklistFileUpdated(new Date());
		ap.setUnblacklistFileIsDelete(0);
		ap.setUnblacklistFileCreated(new Date());
		super.create(ap, AuditHelper.OPERATION_CREATE, token);
		return ap;
	}

	public UnblacklistVendorFile editUnblacklistVendorFile(UnblacklistVendorFile ap, Token token) {
		ap.setUnblacklistFileUpdated(new Date());
		super.edit(ap, AuditHelper.OPERATION_UPDATE, token);
		return ap;
	}

	public UnblacklistVendorFile deleteUnblacklistVendorFile(int id, Token token) {
		UnblacklistVendorFile bu = super.find(id);
		bu.setUnblacklistFileIsDelete(1);
		bu.setUnblacklistFileDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
	}
	
	public UnblacklistVendorFile deleteUnblacklistVendorFile(UnblacklistVendorFile bu, Token token) {
		bu.setUnblacklistFileIsDelete(1);
		bu.setUnblacklistFileDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
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
