package id.co.promise.procurement.inisialisasi;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.BidangUsahaPengadaan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
@Singleton
@LocalBean
public class BidangUsahaPengadaanSession extends AbstractFacadeWithAudit<BidangUsahaPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public BidangUsahaPengadaanSession(){
		super(BidangUsahaPengadaan.class);
	}
	
	public BidangUsahaPengadaan insertBidangUsahaPengadaan(BidangUsahaPengadaan bidangUsahaPengadaan, Token token){
		bidangUsahaPengadaan.setIsDelete(0);
		bidangUsahaPengadaan.setCreated(new Date());
		super.create(bidangUsahaPengadaan, AuditHelper.OPERATION_CREATE, token);
		return bidangUsahaPengadaan;
	}
	
	public BidangUsahaPengadaan getBidangUsahaPengadaanById(Integer id){
		return super.find(id);
	}
	
	public List<BidangUsahaPengadaan> getBidangUsahaPengadaanByPengadaanId(Integer pengadaanId){
		Query query = em.createNamedQuery("BidangUsahaPengadaan.findBidangUsahaPengadaanByPengadaanId");
		query.setParameter("pengadaanId", pengadaanId);
		return query.getResultList();
	}
	
	public BidangUsahaPengadaan getBidangUsahaPengadaanByPengadaanIdSubBidangId(Integer pengadaanId, Integer subBidangId){
		Query query = em.createNamedQuery("BidangUsahaPengadaan.findBidangUsahaPengadaanByPengadaanIdSubBidangId");
		query.setParameter("pengadaanId", pengadaanId);
		query.setParameter("subBidangId", subBidangId);
		return (BidangUsahaPengadaan) query.getSingleResult();
	}
	
	public BidangUsahaPengadaan updateBidangUsahaPengadaan(BidangUsahaPengadaan bidangUsahaPengadaan, Token token){
		bidangUsahaPengadaan.setUpdated(new Date());
		super.edit(bidangUsahaPengadaan, AuditHelper.OPERATION_UPDATE, token);
		return bidangUsahaPengadaan;
	}
	
	public BidangUsahaPengadaan deleteBidangUsahaPengadaan(Integer id, Token token){
		BidangUsahaPengadaan bup = super.find(id);
		bup.setIsDelete(1);
		super.create(bup, AuditHelper.OPERATION_DELETE, token);
		return bup;
	}
	
	public BidangUsahaPengadaan deleteRowBidangUsahaPengadaan(Integer id, Token token){
		BidangUsahaPengadaan bup = super.find(id);
		super.remove(bup, AuditHelper.OPERATION_ROW_DELETE, token);
		return bup;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
}
