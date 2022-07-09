package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.ConditionalPrice;
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
public class ConditionalPriceSession extends
		AbstractFacadeWithAudit<ConditionalPrice> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public ConditionalPriceSession() {
		super(ConditionalPrice.class);
	}

	public ConditionalPrice getConditionalPrice(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<ConditionalPrice> getConditionalPriceList() {
		Query q = em.createNamedQuery("ConditionalPrice.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ConditionalPrice> getConditionalPriceListByTipe(int tipe) {
		Query q = em.createNamedQuery("ConditionalPrice.findConditionalPriceByTipe");
		q.setParameter("tipe", tipe);
		return q.getResultList();
	}

	public ConditionalPrice insertConditionalPrice(ConditionalPrice conditionalPrice, Token token) {
		conditionalPrice.setCreated(new Date());
		conditionalPrice.setIsDelete(0);
		super.create(conditionalPrice, AuditHelper.OPERATION_CREATE, token);
		return conditionalPrice;
	}

	public ConditionalPrice updateConditionalPrice(ConditionalPrice conditionalPrice, Token token) {
		conditionalPrice.setUpdated(new Date());
		super.edit(conditionalPrice, AuditHelper.OPERATION_UPDATE, token);
		return conditionalPrice;
	}

	public ConditionalPrice deleteConditionalPrice(int id, Token token) {
		ConditionalPrice conditionalPrice = super.find(id);
		conditionalPrice.setIsDelete(1);
		conditionalPrice.setDeleted(new Date());
		super.edit(conditionalPrice, AuditHelper.OPERATION_DELETE, token);
		return conditionalPrice;
	}

	public ConditionalPrice deleteRowConditionalPrice(int id, Token token) {
		ConditionalPrice conditionalPrice = super.find(id);
		super.remove(conditionalPrice, AuditHelper.OPERATION_ROW_DELETE, token);
		return conditionalPrice;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkKodeConditionalPrice(String kode, String toDo, Integer conditionalPriceId) {
		Query q = em.createNamedQuery("ConditionalPrice.findKode");
		q.setParameter("kode", kode);
		List<ConditionalPrice> conditionalPriceList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (conditionalPriceList != null && conditionalPriceList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (conditionalPriceList != null && conditionalPriceList.size() > 0) {
				if (conditionalPriceId.equals(conditionalPriceList.get(0).getId())) {
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
}
