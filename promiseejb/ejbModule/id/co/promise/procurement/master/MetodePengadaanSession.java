package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.MetodePengadaan;
import id.co.promise.procurement.entity.Negara;
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
public class MetodePengadaanSession extends AbstractFacadeWithAudit<MetodePengadaan> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public MetodePengadaanSession() {
		super(MetodePengadaan.class);
	}

	public MetodePengadaan getMetodePengadaan(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<MetodePengadaan> getMetodePengadaanList() {
		Query q = em.createNamedQuery("MetodePengadaan.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaMetodePengadaan(String nama, String toDo, Integer metodePengadaanId) {
		Query q = em.createNamedQuery("MetodePengadaan.findByNama");
		q.setParameter("nama", nama);
		List<MetodePengadaan> metodePengadaanList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (metodePengadaanList != null && metodePengadaanList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (metodePengadaanList != null && metodePengadaanList.size() > 0) {
				if (metodePengadaanId.equals(metodePengadaanList.get(0).getId())) {
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

	public MetodePengadaan insertMetodePengadaan(MetodePengadaan mp, Token token) {
		mp.setCreated(new Date());
		mp.setIsDelete(0);
		super.create(mp, AuditHelper.OPERATION_CREATE, token);
		return mp;
	}

	public MetodePengadaan updateMetodePengadaan(MetodePengadaan mp, Token token) {
		mp.setUpdated(new Date());
		super.edit(mp, AuditHelper.OPERATION_UPDATE, token);
		return mp;
	}

	public MetodePengadaan deleteMetodePengadaan(int id, Token token) {
		MetodePengadaan mp = super.find(id);
		mp.setIsDelete(1);
		mp.setDeleted(new Date());
		super.edit(mp, AuditHelper.OPERATION_DELETE, token);
		return mp;
	}

	public MetodePengadaan deleteRowMetodePengadaan(int id, Token token) {
		MetodePengadaan mp = super.find(id);
		super.remove(mp, AuditHelper.OPERATION_ROW_DELETE, token);
		return mp;
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
