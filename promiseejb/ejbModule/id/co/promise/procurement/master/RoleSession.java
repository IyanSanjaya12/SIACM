package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
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
public class RoleSession extends AbstractFacadeWithAudit<Role>{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public RoleSession() {
		super(Role.class);
	}

	public Role getRole(int id){
		return super.find(id);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Role> getRoleByApplication(String appCode) {
		Query query = em
				.createQuery("Select r from Role r where r.isDelete = 0 AND r.appCode=:appCode and r.code not in(:vendorRole)");
		query.setParameter("appCode", appCode);
		query.setParameter("vendorRole", Constant.VENDOR_ROLE);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRoleByAppCode(String appCode) {
		Query query = em.createNamedQuery("Role.findByAppCode");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRoleByAppCodePm() {
		Query query = em.createNamedQuery("Role.findByAppCodePm");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRoleByAppCodeCm() {
		Query query = em.createNamedQuery("Role.findByAppCodeCm");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRolelist(){
		Query q = em.createNamedQuery("Role.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRolelistNonVendor(){
		Query q = em.createNamedQuery("Role.findNonVendor");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRoleByKode(Integer id,String kode){
		String stringQuery 	= " SELECT t1 FROM Role t1 WHERE UPPER(t1.code) = :kode "
							+ " AND t1.isDelete = :isDelete ";
		if(id != null){
			stringQuery += "AND t1.id NOT IN (:id)";
		}
		Query query 		= em.createQuery(stringQuery);
		query.setParameter("kode",kode.toUpperCase());
		query.setParameter("isDelete",0);
		if(id != null){
			query.setParameter("id",id);
		}
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkKodeRole(String code, String toDo, Integer roleId) {
		Query q = em.createNamedQuery("Role.findByKode");
		q.setParameter("code", code);
		List<Role> roleList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (roleList != null && roleList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (roleList != null && roleList.size() > 0) {
				Integer roleIdFromData = roleList.get(0).getId();
				if (roleId.equals(roleIdFromData)){
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
	
	public Role insertRole(Role role, Token token){
		role.setCreated(new Date());
		role.setIsDelete(0);
		super.create(role, AuditHelper.OPERATION_CREATE, token);
		return role;
	}
	
	public Role updateRole(Role role, Token token){
		role.setIsDelete(0);
		role.setUpdated(new Date());
		super.edit(role, AuditHelper.OPERATION_UPDATE, token);
		return role;				
	}
	
	public Role deleteRole(int id, Token token){
		Role role = super.find(id);
		role.setIsDelete(1);
		role.setDeleted(new Date());
		super.edit(role, AuditHelper.OPERATION_DELETE, token);
		return role;
	}
	
	public Role deleteRowRole(int id, Token token){
		Role role = super.find(id);
		super.remove(role, AuditHelper.OPERATION_ROW_DELETE, token);
		return role;
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
