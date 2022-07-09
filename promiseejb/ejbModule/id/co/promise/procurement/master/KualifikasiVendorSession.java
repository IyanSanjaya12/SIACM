package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.KualifikasiVendor;
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
public class KualifikasiVendorSession extends AbstractFacadeWithAudit<KualifikasiVendor> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public KualifikasiVendorSession() {
		super(KualifikasiVendor.class);
	}

	public KualifikasiVendor getKualifikasiVendor(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<KualifikasiVendor> getKualifikasiVendorList() {
		Query q = em.createNamedQuery("KualifikasiVendor.find");
		return q.getResultList();
	}

	public KualifikasiVendor insertKualifikasiVendor(KualifikasiVendor kualifikasiVendor, Token token) {
		kualifikasiVendor.setCreated(new Date());
		kualifikasiVendor.setIsDelete(0);
		super.create(kualifikasiVendor, AuditHelper.OPERATION_CREATE, token);
		return kualifikasiVendor;
	}

	public KualifikasiVendor updateKualifikasiVendor(KualifikasiVendor kualifikasiVendor, Token token) {
		kualifikasiVendor.setUpdated(new Date());
		super.edit(kualifikasiVendor, AuditHelper.OPERATION_UPDATE, token);
		return kualifikasiVendor;
	}

	public KualifikasiVendor deleteKualifikasiVendor(int id, Token token) {
		KualifikasiVendor kv = super.find(id);
		kv.setIsDelete(1);
		kv.setDeleted(new Date());
		super.edit(kv, AuditHelper.OPERATION_DELETE, token);
		return kv;
	}

	public KualifikasiVendor deleteRowKualifikasiVendor(int id, Token token) {
		KualifikasiVendor kv = super.find(id);
		super.remove(kv, AuditHelper.OPERATION_ROW_DELETE, token);
		return kv;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaKualifikasiVendor(String nama, String toDo, Integer kualifikasiVendorId) {
		Query q = em.createNamedQuery("KualifikasiVendor.findNama");
		q.setParameter("nama", nama);
		List<KualifikasiVendor> kualifikasiVendorList = q.getResultList();
		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (kualifikasiVendorList != null && kualifikasiVendorList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (kualifikasiVendorList != null && kualifikasiVendorList.size() > 0) {
				if (kualifikasiVendorId.equals(kualifikasiVendorList.get(0).getId())) {
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