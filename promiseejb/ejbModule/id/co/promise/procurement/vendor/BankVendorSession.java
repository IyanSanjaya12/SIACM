package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class BankVendorSession extends AbstractFacadeWithAudit<BankVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public BankVendorSession() {
		super(BankVendor.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public List<BankVendor> getBankVendorByVendorId(int vendorId) {
		Query q = em.createNamedQuery("BankVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public List<BankVendor> getListBankVendorByBankNumber(String bankNumber,int vendorId) {
		Query q = em.createNamedQuery("BankVendor.findByBankNumber");
		q.setParameter("bankNumber", bankNumber);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public List<BankVendor> getListRekeningByBankNumber(String bankNumber,int vendorId) {
		Query q = em.createNamedQuery("BankVendor.findByBankNumber");
		q.setParameter("bankNumber", bankNumber);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public BankVendor getBankVendorByVendor(Integer vendorId){
		Query q = em.createNamedQuery("BankVendor.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<BankVendor> bankVendor = q.getResultList();
		if (bankVendor != null && bankVendor.size() > 0) {
			return bankVendor.get(0);
		}
		
		return null;
	}
	
	public Integer getBankVendorByBnktAndSequence(MataUang mataUang, Vendor vendor) {
		Query q = em.createNamedQuery("BankVendor.findByBnktAndSequence");
		q.setParameter("mataUang", mataUang);
		q.setParameter("vendor", vendor);
		
		//BankVendor bankVendor = (BankVendor) q.setMaxResults(1).setFirstResult(0);
		//Object resultQuery = q.setMaxResults(1).setFirstResult(0);
		Object resultQuery = q.getSingleResult();
		if (resultQuery != null) {
			return (Integer) resultQuery;
		}
		
		return 0;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public BankVendor getBankVendorBySequence(MataUang mataUang, Vendor vendor) {
		Query q = em.createNamedQuery("BankVendor.findSequence");
		q.setParameter("mataUang", mataUang);
		q.setParameter("vendor", vendor);
		List<BankVendor> bankVendor = q.getResultList();

		if (bankVendor != null && bankVendor.size() > 0) {
			return bankVendor.get(0);
		}
		
		return null;
		
	}

	public BankVendor insertBankVendor(BankVendor bankVendor, Token token) {
		bankVendor.setCreated(new Date());
		bankVendor.setIsDelete(0);

		super.create(bankVendor, AuditHelper.OPERATION_CREATE, token);
		return bankVendor;
	}

	public BankVendor updateBankVendor(BankVendor bankVendor, Token token) {
		bankVendor.setUpdated(new Date());
		super.edit(bankVendor, AuditHelper.OPERATION_UPDATE, token);
		return bankVendor;
	}

	public BankVendor deleteBankVendor(int id, Token token) {
		BankVendor bankVendor = super.find(id);
		bankVendor.setIsDelete(1);
		bankVendor.setDeleted(new Date());

		super.edit(bankVendor, AuditHelper.OPERATION_DELETE, token);
		return bankVendor;
	}

	public BankVendor deleteRowBankVendor(int id, Token token) {
		BankVendor bankVendor = super.find(id);

		super.remove(bankVendor, AuditHelper.OPERATION_ROW_DELETE, token);
		return bankVendor;
	}
	
	public List<BankVendor> deleteBankVendorByVendorId(int vendorId) {
		Query q = em.createNamedQuery("BankVendor.deleteBankVendor");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return null;
	}
}
