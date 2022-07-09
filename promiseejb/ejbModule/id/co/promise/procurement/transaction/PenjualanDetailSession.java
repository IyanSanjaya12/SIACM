package id.co.promise.procurement.transaction;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.PenjualanDetail;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class PenjualanDetailSession extends AbstractFacadeWithAudit<PenjualanDetail> {
	
	public PenjualanDetailSession() {
		super(PenjualanDetail.class);
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
	
	public PenjualanDetail getpenjualanDetail(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<PenjualanDetail> getpenjualanDetailList(){
		Query query = em.createQuery("SELECT penjualanDetail FROM PenjualanDetail penjualanDetail WHERE penjualanDetail.isDelete = 0");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<PenjualanDetail> getListByPenjualanId(Integer penjualanId){
		Query query = em.createQuery("SELECT penjualanDetail FROM PenjualanDetail penjualanDetail WHERE penjualanDetail.isDelete = :isDelete AND penjualanDetail.penjualan.id = :penjualanId");
		query.setParameter("isDelete", 0).setParameter("penjualanId", penjualanId);
		return query.getResultList();
	}
	
	public PenjualanDetail insert(PenjualanDetail penjualanDetail, Token token) {
		penjualanDetail.setCreated(new Date());
		penjualanDetail.setIsDelete(0);
		super.create(penjualanDetail, AuditHelper.OPERATION_CREATE, token);
		return penjualanDetail;
	}

	public PenjualanDetail update(PenjualanDetail penjualanDetail, Token token) {
		penjualanDetail.setUpdated(new Date());
		super.edit(penjualanDetail, AuditHelper.OPERATION_UPDATE, token);
		return penjualanDetail;
	}

	public PenjualanDetail delete(int id, Token token) {
		PenjualanDetail penjualanDetail = super.find(id);
		penjualanDetail.setIsDelete(1);
		penjualanDetail.setDeleted(new Date());
		super.edit(penjualanDetail, AuditHelper.OPERATION_DELETE, token);
		return penjualanDetail;
	}
}
