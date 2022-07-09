package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.MataUang;
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
public class MataUangSession extends AbstractFacadeWithAudit<MataUang> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public MataUangSession() {
		super(MataUang.class);
	}

	public MataUang getMataUang(int mataUangId) {
		return super.find(mataUangId);
	}

	@SuppressWarnings("unchecked")
	public List<MataUang> getMataUanglist() {
		Query q = em.createNamedQuery("MataUang.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MataUang> getMataUangListByKodeEqual(String pKode) {
		Query q = em
				.createQuery("select mu from MataUang mu where mu.isDelete=0 and mu.kode = :pKode");
		q.setParameter("pKode", pKode);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public MataUang getMataUangByNama(String nama) {
		Query q = em.createQuery("select mu from MataUang mu where mu.isDelete=0 and mu.nama = :nama");
		q.setParameter("nama", nama);

		List<MataUang> mataUangList = q.getResultList();
		if (mataUangList != null && mataUangList.size() > 0) {
			return mataUangList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkKodeMataUang(String kode, String toDo, Integer mataUangId) {
		Query q = em.createNamedQuery("mataUang.findKode");
		q.setParameter("kode", kode);
		List<MataUang> mataUangList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (mataUangList != null && mataUangList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (mataUangList != null && mataUangList.size() > 0) {
				if (mataUangId.equals(mataUangList.get(0).getId())) {
					isSave = true;
				} else {
					isSave = false;
				}
			} else {
				isSave = true;
			}
		}

		return isSave;

	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaMataUang(String nama, String toDo, Integer mataUangId) {
		Query q = em.createNamedQuery("mataUang.findNama");
		q.setParameter("nama", nama);
		List<MataUang> mataUangList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (mataUangList != null && mataUangList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (mataUangList != null && mataUangList.size() > 0) {
				if (mataUangId.equals(mataUangList.get(0).getId())) {
					isSave = true;
				} else {
					isSave = false;
				}
			} else {
				isSave = true;
			}
		}

		return isSave;

	}

	@SuppressWarnings("unchecked")
	public MataUang getMataUangByKode(String pKode) {
		Query q = em
				.createQuery("select mu from MataUang mu where mu.isDelete=0 and mu.kode = :pKode");
		q.setParameter("pKode", pKode);

		List<MataUang> mataUangList = q.getResultList();
		if (mataUangList != null && mataUangList.size() > 0) {
			return mataUangList.get(0);
		}
		return null;
	}

	public MataUang insertMataUang(MataUang mu, Token token) {
		mu.setCreated(new Date());
		mu.setIsDelete(0);
		super.create(mu, AuditHelper.OPERATION_CREATE, token);
		return mu;
	}

	public MataUang updateMataUang(MataUang mu, Token token) {
		mu.setUpdated(new Date());
		super.edit(mu, AuditHelper.OPERATION_UPDATE, token);
		return mu;
	}

	public MataUang deleteMataUang(int matauangId, Token token) {
		MataUang mu = super.find(matauangId);
		mu.setIsDelete(1);
		mu.setDeleted(new Date());
		super.edit(mu, AuditHelper.OPERATION_DELETE, token);
		return mu;
	}

	public MataUang deleteRowMataUang(int mataUangId, Token token) {
		MataUang mu = super.find(mataUangId);
		super.remove(mu, AuditHelper.OPERATION_ROW_DELETE, token);
		return mu;
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
