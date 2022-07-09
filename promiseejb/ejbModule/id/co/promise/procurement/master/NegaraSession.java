package id.co.promise.procurement.master;

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
public class NegaraSession extends AbstractFacadeWithAudit<Negara> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public NegaraSession() {
		super(Negara.class);
	}

	public Negara getNegara(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<Negara> getNegaraList() {
		Query q = em.createNamedQuery("Negara.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaNegara(String nama, String toDo, Integer negaraId) {
		Query q = em.createNamedQuery("Negara.findByNama");
		q.setParameter("nama", nama);
		List<Negara> negaraList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (negaraList != null && negaraList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (negaraList != null && negaraList.size() > 0) {
				if (negaraId.equals(negaraList.get(0).getId())) {
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

	public Negara insertNegara(Negara negara, Token token) {
		negara.setCreated(new Date());
		negara.setIsDelete(0);
		super.create(negara, AuditHelper.OPERATION_CREATE, token);
		return negara;
	}

	public Negara updateNegara(Negara negara, Token token) {
		negara.setUpdated(new Date());
		super.edit(negara, AuditHelper.OPERATION_UPDATE, token);
		return negara;
	}

	public Negara deleteNegara(int id, Token token) {
		Negara negara = super.find(id);
		negara.setIsDelete(1);
		negara.setDeleted(new Date());
		super.edit(negara, AuditHelper.OPERATION_DELETE, token);
		return negara;
	}

	public Negara deleteRowNegara(int id, Token token) {
		Negara negara = super.find(id);
		super.remove(negara, AuditHelper.OPERATION_ROW_DELETE, token);
		return negara;
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
