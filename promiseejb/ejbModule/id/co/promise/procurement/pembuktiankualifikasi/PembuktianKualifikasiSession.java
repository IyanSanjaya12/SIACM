package id.co.promise.procurement.pembuktiankualifikasi;

import id.co.promise.procurement.entity.PembuktianKualifikasi;
import id.co.promise.procurement.entity.Token;
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
public class PembuktianKualifikasiSession extends
		AbstractFacadeWithAudit<PembuktianKualifikasi> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PembuktianKualifikasiSession() {
		super(PembuktianKualifikasi.class);
	}

	public PembuktianKualifikasi get(int id) {
		return super.find(id);
	}

	public List<PembuktianKualifikasi> getListPembuktianKualifikasi() {
		Query q = em.createNamedQuery("PembuktianKualifikasi.find");
		return q.getResultList();
	}

	public List<PembuktianKualifikasi> getPembuktianKualifikasiByPengadaanAndVendor(
			Integer pengadaanId, Integer vendorId) {
		Query q = em
				.createNamedQuery("PembuktianKualifikasi.findByPengadaanAndVendor");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}
	
	public List<PembuktianKualifikasi> getPembuktianKualifikasiByVendor(
			Integer pengadaanId, Integer vendorId) {
		Query q = em
				.createNamedQuery("PembuktianKualifikasi.findByPengadaanAndVendor");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}

	public PembuktianKualifikasi createPembuktianKualifikasi(PembuktianKualifikasi dhv, Token token) {
		dhv.setCreated(new Date());
		dhv.setIsDelete(0);
		super.create(dhv, AuditHelper.OPERATION_CREATE, token);
		return dhv;
	}

	public PembuktianKualifikasi updatePembuktianKualifikasi(PembuktianKualifikasi dhv, Token token) {
		dhv.setUpdated(new Date());
		super.edit(dhv, AuditHelper.OPERATION_UPDATE, token);
		return dhv;
	}

	public PembuktianKualifikasi deletePembuktianKualifikasi(int id, Token token) {
		PembuktianKualifikasi dhv = super.find(id);
		dhv.setIsDelete(1);
		dhv.setDeleted(new Date());
		super.edit(dhv, AuditHelper.OPERATION_DELETE, token);
		return dhv;
	}

	public PembuktianKualifikasi deleteRowPembuktianKualifikasi(int id, Token token) {
		PembuktianKualifikasi dhv = super.find(id);
		super.remove(dhv, AuditHelper.OPERATION_ROW_DELETE, token);
		return dhv;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
}
