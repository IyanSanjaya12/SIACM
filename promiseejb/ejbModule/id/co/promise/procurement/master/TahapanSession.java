package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.Negara;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 
 * Project Name : promiseejb Package Name : id.co.promise.procurement.master
 * File Name : TahapanSession.java Description :
 * 
 * @author : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com Since : Sep
 *         12, 2015
 *
 */
@Stateless
@LocalBean
public class TahapanSession extends AbstractFacadeWithAudit<Tahapan> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public TahapanSession() {
		super(Tahapan.class);
	}

	public Tahapan get(int id) {
		return super.find(id);
	}

	public List<Tahapan> getList() {
		Query q = em.createNamedQuery("Tahapan.getList");
		return q.getResultList();
	}
	
	public List<Tahapan> getUnregisteredList(Organisasi organisasi) {
		Query q = em.createNamedQuery("Tahapan.getUnregisteredList");
		q.setParameter("organisasi", organisasi);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Tahapan getByName(String name) {
		Query q = em.createQuery("SELECT tahapan FROM Tahapan tahapan WHERE tahapan.isDelete = 0 AND tahapan.nama = :name");
		q.setParameter("name", name);
		
		List<Tahapan> tahapan = q.getResultList();
		if (tahapan != null && tahapan.size() > 0) {
			return tahapan.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaTahapan(String nama, String toDo, Integer tahapanId) {
		Query q = em.createNamedQuery("Tahapan.findByNama");
		q.setParameter("nama", nama);
		List<Tahapan> tahapanList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (tahapanList != null && tahapanList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (tahapanList != null && tahapanList.size() > 0) {
				if (tahapanId.equals(tahapanList.get(0).getId())) {
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

	public Tahapan insertTahapan(Tahapan tahapan, Token token) {
		tahapan.setCreated(new Date());
		tahapan.setIsDelete(0);
		super.create(tahapan, AuditHelper.OPERATION_CREATE, token);
		return tahapan;
	}

	public Tahapan updateTahapan(Tahapan tahapan, Token token) {
		tahapan.setUpdated(new Date());
		super.edit(tahapan, AuditHelper.OPERATION_UPDATE, token);
		return tahapan;
	}

	public Tahapan deleteTahapan(int id, Token token) {
		Tahapan tahapan = super.find(id);
		tahapan.setIsDelete(1);
		tahapan.setDeleted(new Date());
		super.edit(tahapan, AuditHelper.OPERATION_DELETE, token);
		return tahapan;
	}

	public Tahapan deleteRow(int id, Token token) {
		Tahapan tahapan = super.find(id);
		super.remove(tahapan, AuditHelper.OPERATION_ROW_DELETE, token);
		return tahapan;
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
