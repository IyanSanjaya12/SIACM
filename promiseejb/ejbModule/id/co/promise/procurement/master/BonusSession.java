package id.co.promise.procurement.master;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Bonus;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class BonusSession extends AbstractFacadeWithAudit<Bonus> {
	public BonusSession() {
		super(Bonus.class);
	}

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
	
	public Bonus getbonus(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Bonus> getbonusList(String startDate, String endDate) throws ParseException{
		Date startDateNew = new Date();
		Date endDateNew = new Date();
		
		String strQuery = "SELECT bonus FROM Bonus bonus WHERE bonus.isDelete = 0 ";
		if(!(startDate.equals("") && endDate.contentEquals(""))) {
			startDateNew = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			endDateNew = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			strQuery += " AND bonus.penjualan.tanggal > :startDate AND bonus.penjualan.tanggal <= :endDate ";
		}
		Query query = em.createQuery(strQuery);
		if(!(startDate.equals("") && endDate.contentEquals(""))) {
			query.setParameter("startDate", startDateNew);
			query.setParameter("endDate", endDateNew);
		}
		
		return query.getResultList();
	}
	
	public Bonus insert(Bonus bonus, Token token) {
		bonus.setCreated(new Date());
		bonus.setIsDelete(0);
		super.create(bonus, AuditHelper.OPERATION_CREATE, token);
		return bonus;
	}

	public Bonus update(Bonus bonus, Token token) {
		bonus.setUpdated(new Date());
		super.edit(bonus, AuditHelper.OPERATION_UPDATE, token);
		return bonus;
	}

	public Bonus delete(int id, Token token) {
		Bonus bonus = super.find(id);
		bonus.setIsDelete(1);
		bonus.setDeleted(new Date());
		super.edit(bonus, AuditHelper.OPERATION_DELETE, token);
		return bonus;
	}
}
