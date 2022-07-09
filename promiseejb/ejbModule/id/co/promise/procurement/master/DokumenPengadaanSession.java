package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.DokumenPengadaan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class DokumenPengadaanSession extends
		AbstractFacadeWithAudit<DokumenPengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public DokumenPengadaanSession() {
		super(DokumenPengadaan.class);
	}

	public DokumenPengadaan getDokumenPengadaan(int id) {
		return super.find(new Integer(id));
	}

	public List<DokumenPengadaan> getDokumenPengadaanList() {
		Query q = em.createNamedQuery("DokumenPengadaan.find");
		return q.getResultList();
	}

	public List<DokumenPengadaan> getDokumenPengadaanByPengadaanIdList(
			int pengadaanId) {
		Query q = em.createNamedQuery("DokumenPengadaan.findByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}
	
	public void deleteDokumenPengadaanByPengadaanI(
			int pengadaanId) {
		Query q = em.createNamedQuery("DokumenPengadaan.deleteByPengadaanId");
		q.setParameter("pengadaanId", pengadaanId);
		q.executeUpdate();
	}

	public DokumenPengadaan insertDokumenPengadaan(DokumenPengadaan dp,
			Token token) {
		dp.setCreated(new Date());
		dp.setTanggalUpload(new Date());
		dp.setIsDelete(0);
		super.create(dp, AuditHelper.OPERATION_CREATE, token);
		return dp;
	}

	public DokumenPengadaan updateDokumenPengadaan(DokumenPengadaan dp,
			Token token) {
		dp.setUpdated(new Date());
		dp.setTanggalUpload(new Date());
		super.edit(dp, AuditHelper.OPERATION_UPDATE, token);
		return dp;
	}

	public DokumenPengadaan deleteDokumenPengadaan(int id, Token token) {
		DokumenPengadaan dp = super.find(id);
		dp.setIsDelete(1);
		dp.setDeleted(new Date());
		super.edit(dp, AuditHelper.OPERATION_DELETE, token);
		return dp;
	}

	public DokumenPengadaan deleteRowDokumenPengadaan(int id, Token token) {
		DokumenPengadaan dp = super.find(id);
		super.remove(dp, AuditHelper.OPERATION_ROW_DELETE, token);
		return dp;
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
