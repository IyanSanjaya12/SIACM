package id.co.promise.procurement.pemasukanpenawaran;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.DokumenPenawaranAdmin;
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
public class DokumenPenawaranAdminSession extends
		AbstractFacadeWithAudit<DokumenPenawaranAdmin> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public DokumenPenawaranAdminSession() {
		super(DokumenPenawaranAdmin.class);
	}

	public DokumenPenawaranAdmin getDokumenPenawaranAdmin(int id) {
		return super.find(id);
	}

	public List<DokumenPenawaranAdmin> getDokumenPenawaranAdminList() {
		Query q = em.createNamedQuery("DokumenPenawaranAdmin.find");
		return q.getResultList();
	}

	public List<DokumenPenawaranAdmin> getListDokumenPenawaranAdminByVendorAndPengadaan(
			int vendorId, int pengadaanId) {
		Query q = em
				.createNamedQuery("DokumenPenawaranAdmin.findByVendorIdAndPengadaanId");
		q.setParameter("vendorId", vendorId);
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	public DokumenPenawaranAdmin insertDokumenPenawaranAdmin(
			DokumenPenawaranAdmin dokumenPenawaranAdmin, Token token) {
		dokumenPenawaranAdmin.setCreated(new Date());
		dokumenPenawaranAdmin.setIsDelete(0);
		dokumenPenawaranAdmin.setUserId(token.getUser().getId());
		super.create(dokumenPenawaranAdmin, AuditHelper.OPERATION_CREATE, token);
		return dokumenPenawaranAdmin;
	}

	public DokumenPenawaranAdmin updateDokumenPenawaranAdmin(
			DokumenPenawaranAdmin dokumenPenawaranAdmin, Token token) {
		dokumenPenawaranAdmin.setUpdated(new Date());
		dokumenPenawaranAdmin.setUserId(token.getUser().getId());
		super.edit(dokumenPenawaranAdmin, AuditHelper.OPERATION_UPDATE, token);
		return dokumenPenawaranAdmin;
	}

	public DokumenPenawaranAdmin deleteDokumenPenawaranAdmin(int id, Token token) {
		DokumenPenawaranAdmin dokumenPenawaranAdmin = super.find(id);
		dokumenPenawaranAdmin.setIsDelete(1);
		dokumenPenawaranAdmin.setDeleted(new Date());
		dokumenPenawaranAdmin.setUserId(token.getUser().getId());
		super.edit(dokumenPenawaranAdmin, AuditHelper.OPERATION_DELETE, token);
		return dokumenPenawaranAdmin;
	}

	public DokumenPenawaranAdmin deleteRowDokumenPenawaranAdmin(int id,
			Token token) {
		DokumenPenawaranAdmin dokumenPenawaranAdmin = super.find(id);
		dokumenPenawaranAdmin.setUserId(token.getUser().getId());
		super.remove(dokumenPenawaranAdmin, AuditHelper.OPERATION_ROW_DELETE,
				token);
		return dokumenPenawaranAdmin;
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
