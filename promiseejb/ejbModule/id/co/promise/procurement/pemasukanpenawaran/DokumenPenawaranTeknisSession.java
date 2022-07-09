package id.co.promise.procurement.pemasukanpenawaran;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.DokumenPenawaranTeknis;
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
public class DokumenPenawaranTeknisSession extends
		AbstractFacadeWithAudit<DokumenPenawaranTeknis> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public DokumenPenawaranTeknisSession() {
		super(DokumenPenawaranTeknis.class);
	}

	public DokumenPenawaranTeknis getDokumenPenawaranTeknis(int id) {
		return super.find(id);
	}

	public List<DokumenPenawaranTeknis> getDokumenPenawaranTeknisList() {
		Query q = em.createNamedQuery("DokumenPenawaranTeknis.find");
		return q.getResultList();
	}

	public List<DokumenPenawaranTeknis> getListDokumenPenawaranTeknisByVendorAndPengadaan(
			int vendorId, int pengadaanId) {
		Query q = em
				.createNamedQuery("DokumenPenawaranTeknis.findByVendorIdAndPengadaanId");
		q.setParameter("vendorId", vendorId);
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	public DokumenPenawaranTeknis insertDokumenPenawaranTeknis(
			DokumenPenawaranTeknis dokumenPenawaranTeknis, Token token) {
		dokumenPenawaranTeknis.setCreated(new Date());
		dokumenPenawaranTeknis.setIsDelete(0);
		dokumenPenawaranTeknis.setUserId(token.getUser().getId());
		super.create(dokumenPenawaranTeknis, AuditHelper.OPERATION_CREATE,
				token);
		return dokumenPenawaranTeknis;
	}

	public DokumenPenawaranTeknis updateDokumenPenawaranTeknis(
			DokumenPenawaranTeknis dokumenPenawaranTeknis, Token token) {
		dokumenPenawaranTeknis.setUpdated(new Date());
		dokumenPenawaranTeknis.setUserId(token.getUser().getId());
		super.edit(dokumenPenawaranTeknis, AuditHelper.OPERATION_UPDATE, token);
		return dokumenPenawaranTeknis;
	}

	public DokumenPenawaranTeknis deleteDokumenPenawaranTeknis(int id,
			Token token) {
		DokumenPenawaranTeknis dokumenPenawaranTeknis = super.find(id);
		dokumenPenawaranTeknis.setIsDelete(1);
		dokumenPenawaranTeknis.setDeleted(new Date());
		dokumenPenawaranTeknis.setUserId(token.getUser().getId());
		super.edit(dokumenPenawaranTeknis, AuditHelper.OPERATION_DELETE, token);
		return dokumenPenawaranTeknis;
	}

	public DokumenPenawaranTeknis deleteRowDokumenPenawaranTeknis(int id,
			Token token) {
		DokumenPenawaranTeknis dokumenPenawaranTeknis = super.find(id);
		dokumenPenawaranTeknis.setUserId(token.getUser().getId());
		super.remove(dokumenPenawaranTeknis, AuditHelper.OPERATION_ROW_DELETE,
				token);
		return dokumenPenawaranTeknis;
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
