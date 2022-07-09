package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.JenisPenawaran;
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
public class JenisPenawaranSession extends AbstractFacadeWithAudit<JenisPenawaran> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public JenisPenawaranSession() {
		super(JenisPenawaran.class);
	}

	public JenisPenawaran getJenisPenawaran(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<JenisPenawaran> getJenisPenawaranList() {
		Query q = em.createNamedQuery("JenisPenawaran.find");
		return q.getResultList();
	}

	public JenisPenawaran insertJenisPenawaran(JenisPenawaran jenisPenawaran, Token token) {
		jenisPenawaran.setCreated(new Date());
		jenisPenawaran.setIsDelete(0);
		super.create(jenisPenawaran, AuditHelper.OPERATION_CREATE, token);
		return jenisPenawaran;
	}

	public JenisPenawaran updateJenisPenawaran(JenisPenawaran jenisPenawaran, Token token) {
		jenisPenawaran.setUpdated(new Date());
		super.edit(jenisPenawaran, AuditHelper.OPERATION_UPDATE, token);
		return jenisPenawaran;
	}

	public JenisPenawaran deleteJenisPenawaran(int id, Token token) {
		JenisPenawaran jenisPenawaran = super.find(id);
		jenisPenawaran.setIsDelete(1);
		jenisPenawaran.setDeleted(new Date());

		super.edit(jenisPenawaran, AuditHelper.OPERATION_DELETE, token);
		return jenisPenawaran;
	}

	public JenisPenawaran deleteRowJenisPenawaran(int id, Token token) {
		JenisPenawaran jenisPenawaran = super.find(id);
		super.remove(jenisPenawaran, AuditHelper.OPERATION_ROW_DELETE, token);
		return jenisPenawaran;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaJenisPenawaran(String nama, String toDo,
			Integer jenisPenawaranId) {
		Query q = em.createNamedQuery("JenisPenawaran.findNama");
		q.setParameter("nama", nama);
		List<JenisPenawaran> jenisPenawaranList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (jenisPenawaranList != null && jenisPenawaranList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (jenisPenawaranList != null && jenisPenawaranList.size() > 0) {
				if (jenisPenawaranId.equals(jenisPenawaranList.get(0).getId())) {
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
