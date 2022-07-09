package id.co.promise.procurement.vendor.management;

import id.co.promise.procurement.entity.BlacklistVendorDetail;
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
public class BlacklistVendorDetailSession  extends AbstractFacadeWithAudit<BlacklistVendorDetail> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public BlacklistVendorDetailSession() {
		super(BlacklistVendorDetail.class);
	}

	@SuppressWarnings("unchecked")
	public List<BlacklistVendorDetail> getBlacklistVendorDetailList() {
		Query q = em.createQuery("SELECT t1 FROM BlacklistVendorDetail t1 WHERE t1.blacklistDtlIsDelete = :blacklistDtlIsDelete ORDER BY t1.blacklistDtlId ASC");
		q.setParameter("blacklistDtlIsDelete", 0);
		return q.getResultList();
	} 

	@SuppressWarnings("unchecked")
	public List<BlacklistVendorDetail> getBlacklistVendorDetailListByBlacklistId(Integer blacklistVendorId) {
		Query q = em.createQuery("SELECT t1 FROM BlacklistVendorDetail t1 WHERE t1.blacklistDtlIsDelete = :blacklistDtlIsDelete "
				+ "AND t1.blacklistVendor.blacklistVendorId = :blacklistVendorId "
				+ "ORDER BY t1.blacklistDtlId ASC");
		q.setParameter("blacklistDtlIsDelete", 0);
		q.setParameter("blacklistVendorId", blacklistVendorId);
		return q.getResultList();
	} 
	
	public BlacklistVendorDetail getBlacklistVendorDetailById(int BlacklistVendorDetailId) {
		return super.find(BlacklistVendorDetailId);
	}

	public BlacklistVendorDetail createBlacklistVendorDetail(BlacklistVendorDetail blacklistVendorDetail, Token token) {
		blacklistVendorDetail.setBlacklistDtlCreated(new Date());
		blacklistVendorDetail.setBlacklistDtlIsDelete(0);
		super.create(blacklistVendorDetail, AuditHelper.OPERATION_CREATE, token);
		return blacklistVendorDetail;
	}

	public BlacklistVendorDetail editBlacklistVendorDetail(BlacklistVendorDetail blacklistVendorDetail, Token token) {
		blacklistVendorDetail.setBlacklistDtlUpdated(new Date());
		super.edit(blacklistVendorDetail, AuditHelper.OPERATION_UPDATE, token);
		return blacklistVendorDetail;
	}

	public BlacklistVendorDetail deleteBlacklistVendorDetail(int id, Token token) {
		BlacklistVendorDetail blacklistVendorDetail = super.find(id);
		blacklistVendorDetail.setBlacklistDtlIsDelete(1);
		blacklistVendorDetail.setBlacklistDtlDeleted(new Date());
		super.edit(blacklistVendorDetail, AuditHelper.OPERATION_DELETE, token);
		return blacklistVendorDetail;
	}
	
	public BlacklistVendorDetail deleteBlacklistVendorDetail(BlacklistVendorDetail blacklistVendorDetail, Token token) {
		blacklistVendorDetail.setBlacklistDtlIsDelete(1);
		blacklistVendorDetail.setBlacklistDtlDeleted(new Date());
		super.edit(blacklistVendorDetail, AuditHelper.OPERATION_DELETE, token);
		return blacklistVendorDetail;
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
