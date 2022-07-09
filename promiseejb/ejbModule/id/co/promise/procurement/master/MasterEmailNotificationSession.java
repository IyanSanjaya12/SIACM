package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.MasterEmailNotification;
import id.co.promise.procurement.entity.Satuan;
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
public class MasterEmailNotificationSession extends AbstractFacadeWithAudit<MasterEmailNotification> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public MasterEmailNotificationSession() {
		super(MasterEmailNotification.class);
	}

	public MasterEmailNotification getMasterEmailNotification(int id) {
		return super.find(id);
	}

	public MasterEmailNotification findByTahapanAndRole(int tahapanId, int roleId) {
		Query q = em.createNamedQuery("MasterEmailNotification.findByTahapanAndRole")
			.setParameter("roleId", roleId)
			.setParameter("tahapanId", tahapanId);
		try {
			return (MasterEmailNotification) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<MasterEmailNotification> getMasterEmailNotificationlist() {
		Query q = em.createNamedQuery("MasterEmailNotification.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public MasterEmailNotification getMasterEmailNotificationByNama(String nama) {
		Query q = em.createQuery("SELECT masterEmailNotification FROM MasterEmailNotification masterEmailNotification WHERE masterEmailNotification.isDelete = 0 "
				+ "AND masterEmailNotification.nama = :nama");
		q.setParameter("nama", nama);
		
		List<MasterEmailNotification> menList = q.getResultList();
		if (menList != null && menList.size() > 0) {
			return menList.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaMasterEmailNotification(String nama, String toDo, Integer masterEmailNotificationId) {
		Query q = em.createNamedQuery("MasterEmailNotification.findNama");
		q.setParameter("nama", nama);
		List<MasterEmailNotification> masterEmailNotification = q.getResultList();
		  
		Boolean isSave = false;
		if(toDo.equalsIgnoreCase("insert")) {
			if(masterEmailNotification != null && masterEmailNotification.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}
		   
		} else if (toDo.equalsIgnoreCase("update")) {
			if(masterEmailNotification != null && masterEmailNotification.size() > 0) {
				if(masterEmailNotificationId.equals(masterEmailNotification.get(0).getId())) {
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

	public MasterEmailNotification insertMasterEmailNotification(MasterEmailNotification mEn, Token token) {
		mEn.setCreated(new Date());
		mEn.setIsDelete(0);
		super.create(mEn, AuditHelper.OPERATION_CREATE, token);
		return mEn;
	}

	public MasterEmailNotification updateMasterEmailNotification(MasterEmailNotification mEn, Token token) {
		mEn.setUpdated(new Date());
		super.edit(mEn, AuditHelper.OPERATION_UPDATE, token);
		return mEn;
	}

	public MasterEmailNotification deleteMasterEmailNotification(int masterEmailNotificationId, Token token) {
		MasterEmailNotification mEn = super.find(masterEmailNotificationId);
		mEn.setIsDelete(1);
		mEn.setDeleted(new Date());
		super.edit(mEn, AuditHelper.OPERATION_DELETE, token);
		return mEn;
	}
	
	

	public MasterEmailNotification deleteRowMasterEmailNotification(int masterEmailNotificationId, Token token) {
		MasterEmailNotification mEn = super.find(masterEmailNotificationId);
		super.remove(mEn, AuditHelper.OPERATION_ROW_DELETE, token);
		return mEn;
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
