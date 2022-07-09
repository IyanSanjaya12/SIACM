package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.JenisPengadaan;
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
public class JenisPengadaanSession extends
		AbstractFacadeWithAudit<JenisPengadaan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public JenisPengadaanSession() {
		super(JenisPengadaan.class);
	}

	public JenisPengadaan getJenisPengadaan(int id) {
		return super.find(id);
	}

	public List<JenisPengadaan> getJenisPengadaanList() {
		Query q = em.createNamedQuery("JenisPengadaan.find");
		return q.getResultList();
	}

	public JenisPengadaan insertJenisPengadaan(JenisPengadaan jenisPengadaan, Token token) {
		jenisPengadaan.setCreated(new Date());
		jenisPengadaan.setIsDelete(0);
		super.create(jenisPengadaan, AuditHelper.OPERATION_CREATE, token);
		return jenisPengadaan;
	}

	public JenisPengadaan updateJenisPengadaan(JenisPengadaan jenisPengadaan, Token token) {
		jenisPengadaan.setUpdated(new Date());
		super.edit(jenisPengadaan, AuditHelper.OPERATION_UPDATE, token);
		return jenisPengadaan;
	}

	public JenisPengadaan deleteJenisPengadaan(int id, Token token) {
		JenisPengadaan jenisPengadaan = super.find(id);
		jenisPengadaan.setIsDelete(1);
		jenisPengadaan.setDeleted(new Date());

		super.edit(jenisPengadaan, AuditHelper.OPERATION_DELETE, token);
		return jenisPengadaan;
	}

	public JenisPengadaan deleteRowJenisPengadaan(int id, Token token) {
		JenisPengadaan jenisPengadaan = super.find(id);
		super.remove(jenisPengadaan, AuditHelper.OPERATION_ROW_DELETE, token);
		return jenisPengadaan;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaJenisPengadaan(String nama, String toDo, Integer jenisPengadaanId) {
		Query q = em.createNamedQuery("JenisPengadaan.findNama");
		q.setParameter("nama", nama);
		List<JenisPengadaan> jenisPengadaanList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (jenisPengadaanList != null && jenisPengadaanList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (jenisPengadaanList != null && jenisPengadaanList.size() > 0) {
				if (jenisPengadaanId.equals(jenisPengadaanList.get(0).getId())) {
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
		return ema;
	}
}
