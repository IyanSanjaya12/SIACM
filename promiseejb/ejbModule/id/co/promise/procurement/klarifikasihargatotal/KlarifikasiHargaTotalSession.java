package id.co.promise.procurement.klarifikasihargatotal;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.KlarifikasiHargaTotal;
import id.co.promise.procurement.entity.KlarifikasiHargaTotal;
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
public class KlarifikasiHargaTotalSession extends
AbstractFacadeWithAudit<KlarifikasiHargaTotal> {
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	TokenSession tokenSession;
	public KlarifikasiHargaTotalSession() {
		super(KlarifikasiHargaTotal.class);
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
	
	public KlarifikasiHargaTotal get(int id) {
		return super.find(id);
	}
	
	public List<KlarifikasiHargaTotal> getList() {
		Query q = em.createNamedQuery("KlarifikasiHargaTotal.find");
		return q.getResultList();
	}

	public List<KlarifikasiHargaTotal> getKlarifikasiHargaTotalByPengadaan(
			Integer pengadaanId) {
		Query q = em
				.createNamedQuery("KlarifikasiHargaTotal.findByPengadaan");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public List<KlarifikasiHargaTotal> getKlarifikasiHargaTotalByVendor( 
			Integer vendorId) {
		Query q = em
				.createNamedQuery("KlarifikasiHargaTotal.findByVendor");
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public List<KlarifikasiHargaTotal> getKlarifikasiHargaTotalByPengadaanAndVendor(
			Integer pengadaanId, Integer vendorId) {
		Query q = em
				.createNamedQuery("KlarifikasiHargaTotal.findByPengadaanAndVendor");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}

	public KlarifikasiHargaTotal create(KlarifikasiHargaTotal kht, String token) {
		kht.setCreated(new Date());
		kht.setIsDelete(0);
		super.create(kht, AuditHelper.OPERATION_CREATE, tokenSession.findByToken(token));
		return kht;
	}

	public KlarifikasiHargaTotal update(KlarifikasiHargaTotal kht, String token) {
		kht.setUpdated(new Date());
		super.edit(kht, AuditHelper.OPERATION_UPDATE, tokenSession.findByToken(token));
		return kht;
	}

	public KlarifikasiHargaTotal delete(int id, String token) {
		KlarifikasiHargaTotal kht = super.find(id);
		kht.setIsDelete(1);
		kht.setDeleted(new Date());
		super.edit(kht, AuditHelper.OPERATION_DELETE, tokenSession.findByToken(token));
		return kht;
	}
	
	public KlarifikasiHargaTotal deleteRow(int id, String token) {
		KlarifikasiHargaTotal kht = super.find(id);
		super.remove(kht, AuditHelper.OPERATION_ROW_DELETE, tokenSession.findByToken(token));
		return kht;
	}

}
