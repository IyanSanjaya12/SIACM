package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.UserAdditional;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class UserAdditionalSession extends AbstractFacadeWithAudit<UserAdditional>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public UserAdditionalSession() {
		super(UserAdditional.class);
	}

	public UserAdditional getUserAdditional(int userAdditionalId) {
		return super.find(userAdditionalId);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserAdditional> getUserAdditionalListPagination (Integer pageNo, Integer pageSize, Integer status, String rolePlh, String nama) {
		String query = "SELECT userAdditional FROM UserAdditional userAdditional WHERE userAdditional.isDelete = 0 ";
		
		nama = nama == null ? "" : nama.trim();
		String namaStr = nama.toUpperCase();
		rolePlh = rolePlh == null || rolePlh.equals("undefined") || rolePlh.equals("") ? null	: rolePlh;
		if (!nama.equalsIgnoreCase("")) {
			query = query + "AND (userAdditional.user.namaPengguna like :nama) ";
		}
		if (rolePlh != null) {
			query = query + "AND userAdditional.role.nama =:rolePlh ";
		}
		if (status != null) {
			query = query + "AND userAdditional.isActive =:status ";
		}else {
			query = query + "order by userAdditional.created DESC, userAdditional.updated DESC ";
		}
			
		Query q  = em.createQuery(query);
		
		if (!nama.equalsIgnoreCase("")) {
			q.setParameter("nama", "%" + namaStr + "%");
		}
		if (rolePlh != null) {
			q.setParameter("rolePlh", rolePlh);
		}
		if (status != null) {
			q.setParameter("status", status);
		}
		 
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);
		 
		List<UserAdditional> userAdditionalList = q.getResultList();
	    
		return userAdditionalList;
	}
	
	public Long getuserAdditionalTotalList (Integer status, String rolePlh, String nama) {
		String query = "SELECT Count(userAdditional) FROM UserAdditional userAdditional WHERE userAdditional.isDelete = 0 ";
		
		nama = nama == null ? "" : nama.trim();
		String namaStr = nama.toUpperCase();
		rolePlh = rolePlh == null || rolePlh.equals("undefined") || rolePlh.equals("") ? null	: rolePlh;
		if (!nama.equalsIgnoreCase("")) {
			query = query + "AND (userAdditional.user.namaPengguna like :nama) ";
		}
		if (rolePlh != null) {
			query = query + "AND userAdditional.role.nama =:rolePlh ";
		}
		if (status != null) {
			query = query + "AND userAdditional.isActive =:status ";
		}else {
			query = query + "order by userAdditional.created DESC, userAdditional.updated DESC ";
		}
			
		Query q  = em.createQuery(query);
		
		if (!nama.equalsIgnoreCase("")) {
			q.setParameter("nama", "%" + namaStr + "%");
		}
		if (rolePlh != null) {
			q.setParameter("rolePlh", rolePlh);
		}
		if (status != null) {
			q.setParameter("status", status);
		}
		 
		return (Long) q.getSingleResult();
	}
	
	public UserAdditional getUserAdditionalByNippPlh(String nippPlh) {
		Query q = em.createNamedQuery("UserAdditional.findByNippPlh");
		q.setParameter("nippPlh", nippPlh);

		try {
			return (UserAdditional) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}
	
	public UserAdditional getUserAdditionalMax() {
		Query q = em.createNamedQuery("UserAdditional.findUserAdditionalMax");

		try {
			return (UserAdditional) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}

	public UserAdditional insertUserAdditional(UserAdditional userAdditional, Token token) {
		userAdditional.setCreated(new Date());
		userAdditional.setIsDelete(0);
		super.create(userAdditional, AuditHelper.OPERATION_CREATE, token);
		return userAdditional;
	}

	public UserAdditional updateUserAdditional(UserAdditional userAdditional, Token token) {
		userAdditional.setUpdated(new Date());
		super.edit(userAdditional, AuditHelper.OPERATION_UPDATE, token);
		return userAdditional;
	}

	public UserAdditional deleteUserAdditional(int userAdditionalId, Token token) {
		UserAdditional userAdditional = super.find(userAdditionalId);
		userAdditional.setIsDelete(1);
		userAdditional.setDeleted(new Date());
		super.edit(userAdditional, AuditHelper.OPERATION_DELETE, token);
		return userAdditional;
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
