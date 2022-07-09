package id.co.promise.procurement.vendor.management;

import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.UnblacklistVendor;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class UnblacklistVendorSession  extends AbstractFacadeWithAudit<UnblacklistVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public UnblacklistVendorSession() {
		super(UnblacklistVendor.class);
	} 
	
	public UnblacklistVendor getUnblacklistVendorById(int UnblacklistVendorId) {
		return super.find(UnblacklistVendorId);
	}
	
	public UnblacklistVendor getUnblacklistVendorMax(Integer id) {
		Query q = em.createQuery("SELECT t1 FROM UnblacklistVendor t1 " 
				+ "WHERE t1.unblacklistVendorIsDelete = :unblacklistVendorIsDelete " 
				+ "AND t1.unblacklistVendorStatus = :unblacklistVendorStatus "
				+ "AND t1.vendor.id = :id " 
				+ "AND t1.unblacklistVendorId = (" 
					+ "SELECT MAX(x1.unblacklistVendorId) FROM UnblacklistVendor x1 "
					+ "WHERE x1.unblacklistVendorIsDelete = :unblacklistVendorIsDelete " 
					+ "AND x1.vendor.id = :id " 
				+ ") ");
		q.setParameter("unblacklistVendorIsDelete", 0);
		q.setParameter("unblacklistVendorStatus", 0);
		q.setParameter("id", id);
		return (UnblacklistVendor) q.getSingleResult();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public UnblacklistVendor getUnblacklistVendorMaxAndBlacklistId(Integer id, Integer blackInteger) {
		Query qm = em.createQuery("SELECT MAX(x1.unblacklistVendorId) FROM UnblacklistVendor x1 "
				+ "WHERE x1.unblacklistVendorIsDelete = :unblacklistVendorIsDelete " 
				+ "AND x1.blacklistVendor.blacklistVendorId = :blackInteger "
				+ "AND x1.vendor.id = :id");

		qm.setParameter("unblacklistVendorIsDelete", 0); 
		qm.setParameter("blackInteger", blackInteger);
		qm.setParameter("id", id);
		
		List<Integer> IntegerList = qm.getResultList();
		if(IntegerList == null){
			IntegerList = new ArrayList();
			IntegerList.add(0);
		}
		if(IntegerList.size() == 0){
			IntegerList.add(0);
		}
		int val = 0;
		int index = 0;
		for(Integer in : IntegerList){
			if(in == null){
				IntegerList.set(index, 0);
				val++;
			}
			index++;
		} 
		
		if(val != index){
			Query q = em.createQuery("SELECT t1 FROM UnblacklistVendor t1 " 
					+ "WHERE t1.unblacklistVendorIsDelete = :unblacklistVendorIsDelete " 
					+ "AND t1.unblacklistVendorStatus = :unblacklistVendorStatus "
					+ "AND t1.blacklistVendor.blacklistVendorId = :blackInteger "
					+ "AND t1.vendor.id = :id "
					+ "AND t1.unblacklistVendorId IN (:arr) ");
			
			q.setParameter("unblacklistVendorIsDelete", 0);
			q.setParameter("unblacklistVendorStatus", 0);
			q.setParameter("blackInteger", blackInteger);
			q.setParameter("id", id);
			q.setParameter("arr", IntegerList);
			
			List<UnblacklistVendor> unblacklistVendorList = q.getResultList();
			if(unblacklistVendorList != null){
				if(unblacklistVendorList.size() > 0){
					return unblacklistVendorList.get(0);
				}
			}
			return null;
		}else{
			return null;
		}
	}

	public UnblacklistVendor createUnblacklistVendor(UnblacklistVendor ap, Token token) {
		ap.setUnblacklistVendorUpdated(new Date());
		ap.setUnblacklistVendorIsDelete(0);
		ap.setUnblacklistVendorCreated(new Date());
		super.create(ap, AuditHelper.OPERATION_CREATE, token);
		return ap;
	}

	public UnblacklistVendor editUnblacklistVendor(UnblacklistVendor ap, Token token) {
		ap.setUnblacklistVendorUpdated(new Date());
		super.edit(ap, AuditHelper.OPERATION_UPDATE, token);
		return ap;
	}

	public UnblacklistVendor deleteUnblacklistVendor(int id, Token token) {
		UnblacklistVendor bu = super.find(id);
		bu.setUnblacklistVendorIsDelete(1);
		bu.setUnblacklistVendorDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
	}
	
	public UnblacklistVendor deleteUnblacklistVendor(UnblacklistVendor bu, Token token) {
		bu.setUnblacklistVendorIsDelete(1);
		bu.setUnblacklistVendorDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
	}
	
	@SuppressWarnings("unchecked")
	public void deleteUnblacklistVendorByVendorId(int vendorId) {
		Query q = em.createNamedQuery("UnblacklistVendor.deleteByVendorId");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
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
