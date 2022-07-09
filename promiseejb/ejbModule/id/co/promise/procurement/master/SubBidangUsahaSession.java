package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.entity.SubBidangUsaha;
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
public class SubBidangUsahaSession extends AbstractFacadeWithAudit<SubBidangUsaha> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public SubBidangUsahaSession() {
		super(SubBidangUsaha.class);
	}

	public SubBidangUsaha getSubBidangUsaha(int id) {
		return super.find(id);
	}

	public List<SubBidangUsaha> getSubBidangUsahaList() {
		Query q = em.createNamedQuery("SubBidangUsaha.find");
		return q.getResultList();
	}

	public List<SubBidangUsaha> getSubBidangUsahaByBidangUsahaId(int bidangUsahaId) {
		Query q = em.createNamedQuery("SubBidangUsaha.findByBidangUsahaId");
		q.setParameter("id", bidangUsahaId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public SubBidangUsaha getSubBidangUsahaByNama(String nama) {
		Query q = em.createQuery("SELECT subBidangUsaha FROM SubBidangUsaha subBidangUsaha WHERE subBidangUsaha.isDelete = 0 and subBidangUsaha.nama = :nama");
		q.setParameter("nama", nama);

		List<SubBidangUsaha> subBidangUsahaList = q.getResultList();
		if (subBidangUsahaList != null && subBidangUsahaList.size() > 0) {
			return subBidangUsahaList.get(0);
		}
		return null;
	}
	
	public SubBidangUsaha getSubBidangUsahaByCode(String code) {
		Query q = em.createQuery("SELECT subBidangUsaha FROM SubBidangUsaha subBidangUsaha WHERE subBidangUsaha.isDelete = 0 and subBidangUsaha.subBidangUsahaCode = :code");
		q.setParameter("code", code);
		return (SubBidangUsaha) q.getSingleResult();
	}

	public SubBidangUsaha insertSubBidangUsaha(SubBidangUsaha subBidangUsaha, Token token) {
		subBidangUsaha.setCreated(new Date());
		subBidangUsaha.setIsDelete(0);
		super.create(subBidangUsaha, AuditHelper.OPERATION_CREATE, token);
		return subBidangUsaha;
	}

	public SubBidangUsaha updateSubBidangUsaha(SubBidangUsaha subBidangUsaha, Token token) {
		subBidangUsaha.setUpdated(new Date());
		super.edit(subBidangUsaha, AuditHelper.OPERATION_UPDATE, token);
		return subBidangUsaha;
	}

	public SubBidangUsaha deleteSubBidangUsaha(int id, Token token) {
		SubBidangUsaha subBidangUsaha = super.find(id);
		subBidangUsaha.setIsDelete(1);
		subBidangUsaha.setDeleted(new Date());
		super.edit(subBidangUsaha, AuditHelper.OPERATION_DELETE, token);
		return subBidangUsaha;
	}

	public SubBidangUsaha deleteRowSubBidangUsaha(int id, Token token) {
		SubBidangUsaha subBidangUsaha = super.find(id);
		super.remove(subBidangUsaha, AuditHelper.OPERATION_ROW_DELETE, token);
		return subBidangUsaha;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaSubBidangUsaha(String nama, String toDo, Integer subBidangUsahaId) {
		Query q = em.createNamedQuery("SubBidangUsaha.findNama");
		q.setParameter("nama", nama);
		List<SubBidangUsaha> subBidangUsahaList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (subBidangUsahaList != null && subBidangUsahaList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (subBidangUsahaList != null && subBidangUsahaList.size() > 0) {
				if (subBidangUsahaId.equals(subBidangUsahaList.get(0).getId())) {
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
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
	
	@SuppressWarnings("unchecked")
	public List<SubBidangUsaha> getActiveSubBidangUsaha() {
		Query q = em.createNamedQuery("SubBidangUsaha.getActiveSubBidangUsaha");
		List<SubBidangUsaha> subBidangUsahaList = q.getResultList();		
		return subBidangUsahaList;
		
	}

}
