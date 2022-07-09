package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Bank;
import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class BankSession extends AbstractFacadeWithAudit<Bank> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public BankSession() {
		super(Bank.class);
	}

	public Bank getBank(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Bank> getBankList(){
		Query q = em.createNamedQuery("Bank.find");
		return q.getResultList();
	}
	
	public List<Bank> getBankListById(Integer id) {
		Query q = em.createNamedQuery("bank.findId");
		q.setParameter("id", id);
		return q.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public Bank getBankByNamaBank(String namaBank, String namaKantor) {
		Query q = em.createQuery("SELECT bank FROM Bank bank WHERE bank.isDelete = 0 AND bank.namaBank = :namaBank AND bank.namaKantor = :namaKantor");
		q.setParameter("namaBank", namaBank);
		q.setParameter("namaKantor", namaKantor);
		List<Bank> bankList = q.getResultList();
		if(bankList != null && bankList.size() > 0) {
			return bankList.get(0);
		}
		
		return null;
	}
	
	public Bank insertBank(Bank bank, Token token){
		bank.setCreated(new Date());
		bank.setIsDelete(0);
		super.create(bank, AuditHelper.OPERATION_CREATE, token);
		return bank;
	}
	
	public Bank updateBank(Bank bank, Token token){
		bank.setUpdated(new Date());
		super.edit(bank, AuditHelper.OPERATION_UPDATE, token);
		return bank;				
	}
	
	public Bank deleteBank(int id, Token token){
		Bank bank = super.find(id);
		bank.setIsDelete(1);
		bank.setDeleted(new Date());
		super.edit(bank, AuditHelper.OPERATION_DELETE, token);
		return bank;
	}
	
	public Bank deleteRowBank(int id, Token token){
		Bank bank = super.find(id);
		super.remove(bank, AuditHelper.OPERATION_ROW_DELETE, token);
		return bank;
	}
	
	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
	
	public Boolean checkKodeBank(String kodeBank, String toDo,
			Integer bankId) {
		Query q = em.createNamedQuery("bank.findKodeBank");
		q.setParameter("kodeBank", kodeBank);
		List<Bank> bankList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (bankList != null && bankList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			
			if (bankList != null && bankList.size() > 0) {
				if (bankId.equals(bankList.get(0).getId())) {
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
	public List<Bank> getBankListWithPagination(Integer start, Integer length, String keyword, Integer columnOrder, String tipeOrder){
		
		String queryUser = "SELECT bank FROM Bank bank WHERE bank.isDelete =:isDelete AND "
				+ "(bank.namaBank like :keyword OR bank.namaKantor like :keyword OR bank.kota like :keyword OR "
				+ " bank.alamat like :keyword) ";
		
		String[] columnToView = { "id", "namaBank", "namaKantor", "kota", "alamat"};
		if (columnOrder > 0) {
			queryUser+="ORDER BY bank. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+="ORDER BY bank.id desc ";
		}
		
		Query query = em.createQuery(queryUser);
		query.setParameter("isDelete", 0);
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<Bank> bankList = query.getResultList();
		return bankList;
	}
	
	public long getBankListCount(String keyword){
		String queryCountUser = "SELECT COUNT (bank) FROM Bank bank WHERE bank.isDelete =:isDelete AND  "
				+ "(bank.namaBank like :keyword OR bank.namaKantor like :keyword OR bank.kota like :keyword OR "
				+ " bank.alamat like :keyword) ";
		
		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", keyword);
		query.setParameter("isDelete", 0);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}	
}
