package id.co.promise.procurement.transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Penjualan;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class PenjualanSession extends AbstractFacadeWithAudit<Penjualan>{

	public PenjualanSession() {
		super(Penjualan.class);
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
	
	public Penjualan getpenjualan(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Penjualan> getpenjualanList(){
		Date currentDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String date = simpleDateFormat.format(currentDate);
		Query query = em.createQuery("SELECT penjualan FROM Penjualan penjualan WHERE penjualan.isDelete = 0 AND CAST(penjualan.tanggal AS string) LIKE :currentDate");
		query.setParameter("currentDate", "%"+date+"%");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Penjualan> getLaporanPenjualanList(Date startDate, Date endDate){
		String stringQuery = "SELECT penjualan FROM Penjualan penjualan WHERE penjualan.isDelete = 0 ";
		if (startDate != null && endDate !=null) {
			stringQuery = stringQuery + "and penjualan.tanggal >= :startDate and penjualan.tanggal < :endDate ";
		}
		Query query = em.createQuery(stringQuery);
		if (startDate != null && endDate != null) {
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}
		List<Penjualan> penjualanList = query.getResultList();
		return penjualanList;
	}
	
	public Penjualan insert(Penjualan penjualan, Token token) {
		penjualan.setCreated(new Date());
		penjualan.setIsDelete(0);
		super.create(penjualan, AuditHelper.OPERATION_CREATE, token);
		return penjualan;
	}

	public Penjualan update(Penjualan penjualan, Token token) {
		penjualan.setUpdated(new Date());
		super.edit(penjualan, AuditHelper.OPERATION_UPDATE, token);
		return penjualan;
	}

	public Penjualan delete(int id, Token token) {
		Penjualan penjualan = super.find(id);
		penjualan.setIsDelete(1);
		penjualan.setDeleted(new Date());
		super.edit(penjualan, AuditHelper.OPERATION_DELETE, token);
		return penjualan;
	}
}
