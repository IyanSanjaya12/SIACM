package id.co.promise.procurement.pengambilandokumen;

import id.co.promise.procurement.entity.PengambilanDokumen;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author nanang
 *
 */
@Singleton
@LocalBean
public class PengambilanDokumenSession extends AbstractFacadeWithAudit<PengambilanDokumen> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PengambilanDokumenSession() {
		super(PengambilanDokumen.class);
	}

	public PengambilanDokumen get(int id) {
		return super.find(id);
	}

	public List<PengambilanDokumen> getListByPengadaan(int pengadaanId) {
		Query q = em.createNamedQuery("PengambilanDok.getByPengadaan");
		q.setParameter("pengadaanId", pengadaanId);
		return q.getResultList();
	}

	public List<PengambilanDokumen> getListByPengadaanVendor(int pengadaanId,
			int vendorId) {
		Query q = em.createNamedQuery("PengambilanDok.getByPengadaanVendor");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("vendorId", vendorId);
		return q.getResultList();
	}

	public List<PengambilanDokumen> getList() {
		Query q = em.createNamedQuery("PengambilanDok.getList");
		return q.getResultList();
	}

	public PengambilanDokumen insert(PengambilanDokumen data, Token token) {
		data.setCreated(new Date());
		data.setIsDelete(0);
		super.create(data, AuditHelper.OPERATION_CREATE, token);
		return data;
	}

	public PengambilanDokumen update(PengambilanDokumen data, Token token) {
		data.setUpdated(new Date());
		super.edit(data, AuditHelper.OPERATION_UPDATE, token);
		return data;
	}

	public PengambilanDokumen delete(int id, Token token) {
		PengambilanDokumen data = super.find(id);
		data.setIsDelete(1);
		data.setDeleted(new Date());
		super.edit(data, AuditHelper.OPERATION_DELETE, token);
		return data;
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
