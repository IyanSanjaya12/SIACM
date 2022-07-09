package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.BankVendorDraft;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
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
public class BankVendorDraftSession extends AbstractFacadeWithAudit<BankVendorDraft> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public BankVendorDraftSession() {
		super(BankVendorDraft.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	@SuppressWarnings("unchecked")
	public BankVendorDraft getBankVendorDraftByVendor(Integer vendorId){
		Query q = em.createNamedQuery("BankVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		
		List<BankVendorDraft> bankVendorDraft = q.getResultList();
		if (bankVendorDraft != null && bankVendorDraft.size() > 0) {
			return bankVendorDraft.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<BankVendorDraft> getBankVendorDraftByVendorId(int vendorId) {
		Query q = em.createNamedQuery("BankVendorDraft.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public BankVendorDraft getBankVendorDraftByVendorIdAndBankVendor(Integer vendorId, Integer bankVendorId) {
		Query q = em.createNamedQuery("BankVendorDraft.findByVendorAndBankVendor");
		q.setParameter("vendorId", vendorId);
		q.setParameter("bankVendorId", bankVendorId);
		List<BankVendorDraft> bankVendorDraft = q.getResultList();
		if (bankVendorDraft != null &&bankVendorDraft.size() > 0) {
				return bankVendorDraft.get(0);
		}
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<BankVendorDraft> getBankVendorByVendorIdAndStatusNotSaved(int vendorId) {
		Query q = em.createNamedQuery("BankVendorDraft.findByVendorAndStatusNotSaved");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<BankVendorDraft> getBankVendorByVendorIdAndStatusSaved(int vendorId) {
		Query q = em.createNamedQuery("BankVendorDraft.findByVendorAndStatusSaved");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public void deleteUselessData(int vendorId) {
		Query q = em.createNamedQuery("BankVendorDraft.deleteUselessData");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
	}
	
	public Integer getBankVendorDraftByBnktAndSequence(MataUang mataUang, Vendor vendor) {
		Query q = em.createNamedQuery("BankVendorDraft.findByBnktAndSequence");
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
	public BankVendorDraft getBankVendorDraftBySequence(MataUang mataUang, Vendor vendor) {
		Query q = em.createNamedQuery("BankVendorDraft.findSequence");
		q.setParameter("mataUang", mataUang);
		q.setParameter("vendor", vendor);
		List<BankVendorDraft> bankVendorDraft = q.getResultList();

		if (bankVendorDraft != null && bankVendorDraft.size() > 0) {
			return bankVendorDraft.get(0);
		}
		
		return null;
		
	}

	public BankVendorDraft insertBankVendorDraft(BankVendorDraft bankVendorDraft, Token token) {
		bankVendorDraft.setCreated(new Date());
		bankVendorDraft.setIsDelete(0);

		super.create(bankVendorDraft, AuditHelper.OPERATION_CREATE, token);
		return bankVendorDraft;
	}

	public BankVendorDraft updateBankVendorDraft(BankVendorDraft bankVendorDraft, Token token) {
		bankVendorDraft.setUpdated(new Date());

		super.edit(bankVendorDraft, AuditHelper.OPERATION_UPDATE, token);
		return bankVendorDraft;
	}

	public BankVendorDraft deleteBankVendorDraft(int id, Token token) {
		BankVendorDraft bankVendorDraft = super.find(id);
		bankVendorDraft.setIsDelete(1);
		bankVendorDraft.setDeleted(new Date());

		super.edit(bankVendorDraft, AuditHelper.OPERATION_DELETE, token);
		return bankVendorDraft;
	}

	public BankVendorDraft deleteRowBankVendorDraft(int id, Token token) {
		BankVendorDraft bankVendorDraft = super.find(id);

		super.remove(bankVendorDraft, AuditHelper.OPERATION_ROW_DELETE, token);
		return bankVendorDraft;
	}
	
	public List<BankVendorDraft> deleteBankVendorDraftByVendorId(int vendorId) {
		Query q = em.createNamedQuery("BankVendorDraft.deleteBankVendorDraft");
		q.setParameter("vendorId", vendorId);
		q.executeUpdate();
		return  null;
	}
}
