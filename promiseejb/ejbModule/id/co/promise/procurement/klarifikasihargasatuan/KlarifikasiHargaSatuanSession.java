package id.co.promise.procurement.klarifikasihargasatuan;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.KlarifikasiHargaSatuan;
import id.co.promise.procurement.entity.PembuktianKualifikasi;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class KlarifikasiHargaSatuanSession extends
	AbstractFacadeWithAudit<KlarifikasiHargaSatuan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	TokenSession tokenSession;
	
	public KlarifikasiHargaSatuanSession() {
		super(KlarifikasiHargaSatuan.class);
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

	public KlarifikasiHargaSatuan get(int id) {
		return super.find(id);
	}
	
	public List<KlarifikasiHargaSatuan> getList() {
		Query q = em.createNamedQuery("KlarifikasiHargaSatuan.find");
		return q.getResultList();
	}

	public List<KlarifikasiHargaSatuan> getKlarifikasiHargaSatuanByPengadaan(
			Integer pengadaanId) {
		Query q = em
				.createNamedQuery("KlarifikasiHargaSatuan.findByPengadaan");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public List<KlarifikasiHargaSatuan> getKlarifikasiHargaSatuanByVendor( 
			Integer vendorId) {
		Query q = em
				.createNamedQuery("KlarifikasiHargaSatuan.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public List<KlarifikasiHargaSatuan> getKlarifikasiHargaSatuanByVendorAndPengadaanAndItem( 
			Integer vendorId, Integer pengadaanId, Integer itemId) {
		Query q = em
				.createNamedQuery("KlarifikasiHargaSatuan.findByVendorAndPengadaanAndItem");
		q.setParameter("vendorId", vendorId);
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("itemId", itemId);
		return q.getResultList();
	}

	public List<KlarifikasiHargaSatuan> getKlarifikasiHargaSatuanByPengadaanAndItem( 
			Integer pengadaanId, Integer itemId) {
		Query q = em
				.createNamedQuery("KlarifikasiHargaSatuan.findByPengadaanAndItem");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("itemId", itemId);
		return q.getResultList();
	}
	
	public KlarifikasiHargaSatuan create(KlarifikasiHargaSatuan khs, String token) {
		khs.setCreated(new Date());
		khs.setIsDelete(0);
		super.create(khs, AuditHelper.OPERATION_CREATE, tokenSession.findByToken(token));
		return khs;
	}

	public KlarifikasiHargaSatuan update(KlarifikasiHargaSatuan khs, String token) {
		khs.setUpdated(new Date());
		super.edit(khs, AuditHelper.OPERATION_UPDATE, tokenSession.findByToken(token));
		return khs;
	}

	public KlarifikasiHargaSatuan delete(int id, String token) {
		KlarifikasiHargaSatuan khs = super.find(id);
		khs.setIsDelete(1);
		khs.setDeleted(new Date());
		super.edit(khs, AuditHelper.OPERATION_DELETE, tokenSession.findByToken(token));
		return khs;
	}
	
	public KlarifikasiHargaSatuan deleteRow(int id, String token) {
		KlarifikasiHargaSatuan khs = super.find(id);
		super.remove(khs, AuditHelper.OPERATION_ROW_DELETE, tokenSession.findByToken(token));
		return khs;
	}
}