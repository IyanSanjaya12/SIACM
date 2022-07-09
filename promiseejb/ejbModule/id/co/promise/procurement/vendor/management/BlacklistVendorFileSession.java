package id.co.promise.procurement.vendor.management;

import id.co.promise.procurement.entity.BlacklistVendorFile;
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
public class BlacklistVendorFileSession  extends AbstractFacadeWithAudit<BlacklistVendorFile> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public BlacklistVendorFileSession() {
		super(BlacklistVendorFile.class);
	}

	@SuppressWarnings("unchecked")
	public List<BlacklistVendorFile> getBlacklistVendorFileList() {
		Query q = em.createQuery("SELECT t1 FROM BlacklistVendorFile t1 WHERE t1.blacklistFileIsDelete = :blacklistFileIsDelete ORDER BY t1.blacklistFileId ASC");
		q.setParameter("blacklistFileIsDelete", 0);
		return q.getResultList();
	} 

	@SuppressWarnings("unchecked")
	public List<BlacklistVendorFile> getBlacklistVendorFileListByBlacklistId(Integer blacklistVendorId) {
		Query q = em.createQuery("SELECT t1 FROM BlacklistVendorFile t1 WHERE t1.blacklistFileIsDelete = :blacklistFileIsDelete "
				+ "AND t1.blacklistVendor.blacklistVendorId = :blacklistVendorId "
				+ "ORDER BY t1.blacklistFileId ASC");
		q.setParameter("blacklistFileIsDelete", 0);
		q.setParameter("blacklistVendorId", blacklistVendorId);
		return q.getResultList();
	} 
	
	public BlacklistVendorFile getBlacklistVendorFileById(int BlacklistVendorFileId) {
		return super.find(BlacklistVendorFileId);
	}

	public BlacklistVendorFile createBlacklistVendorFile(BlacklistVendorFile blacklistVendorFile, Token token) {
		blacklistVendorFile.setBlacklistFileCreated(new Date());
		blacklistVendorFile.setBlacklistFileIsDelete(0);
		super.create(blacklistVendorFile, AuditHelper.OPERATION_CREATE, token);
		return blacklistVendorFile;
	}

	public BlacklistVendorFile editBlacklistVendorFile(BlacklistVendorFile blacklistVendorFile, Token token) {
		blacklistVendorFile.setBlacklistFileUpdated(new Date());
		super.edit(blacklistVendorFile, AuditHelper.OPERATION_UPDATE, token);
		return blacklistVendorFile;
	}

	public BlacklistVendorFile deleteBlacklistVendorFile(int id, Token token) {
		BlacklistVendorFile blacklistVendorFile = super.find(id);
		blacklistVendorFile.setBlacklistFileIsDelete(1);
		blacklistVendorFile.setBlacklistFileDeleted(new Date());
		super.edit(blacklistVendorFile, AuditHelper.OPERATION_DELETE, token);
		return blacklistVendorFile;
	}
	
	public BlacklistVendorFile deleteBlacklistVendorFile(BlacklistVendorFile blacklistVendorFile, Token token) {
		blacklistVendorFile.setBlacklistFileIsDelete(1);
		blacklistVendorFile.setBlacklistFileDeleted(new Date());
		super.edit(blacklistVendorFile, AuditHelper.OPERATION_DELETE, token);
		return blacklistVendorFile;
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
