package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.PembuatKeputusan;
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
public class PembuatKeputusanSession extends AbstractFacadeWithAudit<PembuatKeputusan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public PembuatKeputusanSession() {
		super(PembuatKeputusan.class);
	}

	public PembuatKeputusan getPembuatKeputusanId(int id) {
		return super.find(id);
	}

	public List<PembuatKeputusan> getPembuatKeputusanList() {
		Query q = em.createNamedQuery("PembuatKeputusan.find");
		return q.getResultList();
	}

	public PembuatKeputusan insertPembuatKeputusan(PembuatKeputusan pembuatKeputusan, Token token) {
		pembuatKeputusan.setCreated(new Date());
		pembuatKeputusan.setIsDelete(0);
		super.create(pembuatKeputusan, AuditHelper.OPERATION_CREATE, token);
		return pembuatKeputusan;
	}

	public PembuatKeputusan updatePembuatKeputusan(PembuatKeputusan pembuatKeputusan,
			Token token) {
		pembuatKeputusan.setUpdated(new Date());
		super.edit(pembuatKeputusan, AuditHelper.OPERATION_UPDATE, token);
		return pembuatKeputusan;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaPembuatKeputusan(String nama, String toDo, Integer pembuatKeputusanId) {
		Query q = em.createNamedQuery("PembuatKeputusan.findNama");
		q.setParameter("nama", nama);
		List<PembuatKeputusan> pembuatKeputusanList = q.getResultList();
		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (pembuatKeputusanList != null && pembuatKeputusanList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (pembuatKeputusanList != null && pembuatKeputusanList.size() > 0) {
				if (pembuatKeputusanId.equals(pembuatKeputusanList.get(0).getId())) {
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

	public PembuatKeputusan deletePembuatKeputusan(int id, Token token) {
		PembuatKeputusan pembuatKeputusan = super.find(id);
		pembuatKeputusan.setDeleted(new Date());
		pembuatKeputusan.setIsDelete(1);
		super.edit(pembuatKeputusan, AuditHelper.OPERATION_DELETE, token);
		return pembuatKeputusan;
	}

	public PembuatKeputusan deleteRowPembuatKeputusan(int id, Token token) {
		PembuatKeputusan pembuatKeputusan = super.find(id);
		super.remove(pembuatKeputusan, AuditHelper.OPERATION_ROW_DELETE, token);
		return pembuatKeputusan;
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
