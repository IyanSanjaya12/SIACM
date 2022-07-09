package id.co.promise.procurement.vendor.management;

import id.co.promise.procurement.entity.BlacklistVendor;
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
public class BlacklistVendorSession  extends AbstractFacadeWithAudit<BlacklistVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public BlacklistVendorSession() {
		super(BlacklistVendor.class);
	}

	@SuppressWarnings("unchecked")
	public List<BlacklistVendor> getBlacklistVendorList() {
		Query q = em.createQuery("SELECT t1 FROM BlacklistVendor t1 "
				+ "WHERE t1.blacklistVendorIsDelete = :blacklistVendorIsDelete "
				+ "AND t1.vendor.statusBlacklist = :statusBlacklist "
				+ "AND t1.blacklistVendorStatus = :blacklistVendorStatus "
				+ "ORDER BY t1.vendor.id ASC");
		q.setParameter("blacklistVendorIsDelete", 0);
		q.setParameter("statusBlacklist", 2);
		q.setParameter("blacklistVendorStatus", 1);
		return q.getResultList();
	} 

	public BlacklistVendor getBlacklistVendorMax(Integer id) {
		Query q = em.createQuery("SELECT t1 FROM BlacklistVendor t1 "
				+ "WHERE t1.blacklistVendorIsDelete = :blacklistVendorIsDelete "
				+ "AND t1.blacklistVendorStatus = :blacklistVendorStatus "
				+ "AND t1.vendor.id = :id "
				+ "AND t1.blacklistVendorId = ("
					+ "SELECT MAX(x1.blacklistVendorId) FROM BlacklistVendor x1 "
					+ "WHERE x1.blacklistVendorIsDelete = :blacklistVendorIsDelete "
					+ "AND x1.vendor.id = :id "
				+ ") ");
		q.setParameter("blacklistVendorIsDelete", 0);
		q.setParameter("blacklistVendorStatus", 0);
		q.setParameter("id", id);
		return (BlacklistVendor) q.getSingleResult();
	} 
	
	public BlacklistVendor getBlacklistVendorMaxVendor(Integer id) {
		Query q = em.createQuery("SELECT t1 FROM BlacklistVendor t1 "
				+ "WHERE t1.blacklistVendorIsDelete = :blacklistVendorIsDelete " 
				+ "AND t1.vendor.id = :id "
				+ "AND t1.blacklistVendorId = ("
					+ "SELECT MAX(x1.blacklistVendorId) FROM BlacklistVendor x1 "
					+ "WHERE x1.blacklistVendorIsDelete = :blacklistVendorIsDelete "
					+ "AND x1.vendor.id = :id "
				+ ") ");
		q.setParameter("blacklistVendorIsDelete", 0); 
		q.setParameter("id", id);
		return (BlacklistVendor) q.getSingleResult();
	} 
	
	public int deleteAllBlacklistVendor(String blacklistVendorTable){
	    String delete = String.format("delete from %s",blacklistVendorTable);
	    Query query = em.createQuery(delete);
	    return query.executeUpdate();
	}
	
	public BlacklistVendor getBlacklistVendorById(int BlacklistVendorId) {
		return super.find(BlacklistVendorId);
	}

	public BlacklistVendor createBlacklistVendor(BlacklistVendor blacklistVendor, Token token) {
		blacklistVendor.setBlacklistVendorCreated(new Date());
		blacklistVendor.setBlacklistVendorIsDelete(0);
		super.create(blacklistVendor, AuditHelper.OPERATION_CREATE, token);
		return blacklistVendor;
	}

	public BlacklistVendor editBlacklistVendor(BlacklistVendor blacklistVendor, Token token) {
		blacklistVendor.setBlacklistVendorUpdated(new Date());
		super.edit(blacklistVendor, AuditHelper.OPERATION_UPDATE, token);
		return blacklistVendor;
	}

	public BlacklistVendor deleteBlacklistVendor(int id, Token token) {
		BlacklistVendor blacklistVendor = super.find(id);
		blacklistVendor.setBlacklistVendorIsDelete(1);
		blacklistVendor.setBlacklistVendorDeleted(new Date());
		super.edit(blacklistVendor, AuditHelper.OPERATION_DELETE, token);
		return blacklistVendor;
	}
	
	public BlacklistVendor deleteBlacklistVendor(BlacklistVendor blacklistVendor, Token token) {
		blacklistVendor.setBlacklistVendorIsDelete(1);
		blacklistVendor.setBlacklistVendorDeleted(new Date());
		super.edit(blacklistVendor, AuditHelper.OPERATION_DELETE, token);
		return blacklistVendor;
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
