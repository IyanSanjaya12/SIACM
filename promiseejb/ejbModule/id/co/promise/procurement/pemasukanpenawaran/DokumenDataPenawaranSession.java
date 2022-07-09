package id.co.promise.procurement.pemasukanpenawaran;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.DokumenDataPenawaran;
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
public class DokumenDataPenawaranSession extends
		AbstractFacadeWithAudit<DokumenDataPenawaran> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public DokumenDataPenawaranSession() {
		super(DokumenDataPenawaran.class);
	}

	public DokumenDataPenawaran getDokumenDataPenawaran(int id) {
		return super.find(id);
	}

	public List<DokumenDataPenawaran> getDokumenDataPenawaranList() {
		Query q = em.createNamedQuery("DokumenDataPenawaran.find");
		return q.getResultList();
	}

	public List<DokumenDataPenawaran> getListDokumenDataPenawaranByVendorAndPengadaan(
			int vendorId, int pengadaanId) {
		Query q = em
				.createNamedQuery("DokumenDataPenawaran.findByVendorIdAndPengadaanId");
		q.setParameter("vendorId", vendorId);
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	public DokumenDataPenawaran insertDokumenDataPenawaran(
			DokumenDataPenawaran dokumenDataPenawaran, Token token) {
		dokumenDataPenawaran.setCreated(new Date());
		dokumenDataPenawaran.setIsDelete(0);
		dokumenDataPenawaran.setUserId(token.getUser().getId());
		super.create(dokumenDataPenawaran, AuditHelper.OPERATION_CREATE, token);
		return dokumenDataPenawaran;
	}

	public DokumenDataPenawaran updateDokumenDataPenawaran(
			DokumenDataPenawaran dokumenDataPenawaran, Token token) {
		dokumenDataPenawaran.setUpdated(new Date());
		dokumenDataPenawaran.setUserId(token.getUser().getId());
		super.edit(dokumenDataPenawaran, AuditHelper.OPERATION_UPDATE, token);
		return dokumenDataPenawaran;
	}

	public DokumenDataPenawaran deleteDokumenDataPenawaran(int id, Token token) {
		DokumenDataPenawaran dokumenDataPenawaran = super.find(id);
		dokumenDataPenawaran.setIsDelete(1);
		dokumenDataPenawaran.setDeleted(new Date());
		dokumenDataPenawaran.setUserId(token.getUser().getId());
		super.edit(dokumenDataPenawaran, AuditHelper.OPERATION_DELETE, token);
		return dokumenDataPenawaran;
	}

	public DokumenDataPenawaran deleteRowDokumenDataPenawaran(int id,
			Token token) {
		DokumenDataPenawaran dokumenDataPenawaran = super.find(id);
		dokumenDataPenawaran.setUserId(token.getUser().getId());
		super.remove(dokumenDataPenawaran, AuditHelper.OPERATION_ROW_DELETE,
				token);
		return dokumenDataPenawaran;
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
