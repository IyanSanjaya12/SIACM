package id.co.promise.procurement.menu;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.RoleMenu;
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
public class RoleMenuSession extends AbstractFacadeWithAudit<RoleMenu> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	public RoleMenuSession() {
		super(RoleMenu.class);
	}
	
	public RoleMenu getRoleMenu(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleMenu> getRoleMenuList(){
		Query q = em.createNamedQuery("RoleMenu.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleMenu> getRoleMenuListByRole(int roleId){
		Query q = em.createNamedQuery("RoleMenuListByRole.find")
				.setParameter("roleId", roleId);
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleMenu> getRoleMenuRootListByRole(int roleId){
		Query q = em.createNamedQuery("RoleMenuRootListByRole.find")
			.setParameter("roleId", roleId);
		
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaRoleMenu(Integer roleId, Integer menuId, String toDo, Integer roleMenuId) {
		Query q = em.createNamedQuery("RoleMenuListByRole.findByRoleIdAndMenuId");
		q.setParameter("roleId", roleId);
		q.setParameter("menuId", menuId);
		List<RoleMenu> roleMenuList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (roleMenuList != null && roleMenuList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (roleMenuList != null && roleMenuList.size() > 0) {
				if (roleMenuId.equals(roleMenuList.get(0).getId())) {
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
	
	public RoleMenu insertRoleMenu(RoleMenu rm, Token token){
		rm.setCreated(new Date());
		rm.setIsDelete(0);
		super.create(rm, AuditHelper.OPERATION_CREATE, token);
		return rm;
	}
	
	public RoleMenu updateRoleMenu(RoleMenu rm, Token token){
		rm.setUpdated(new Date());
		super.edit(rm, AuditHelper.OPERATION_UPDATE, token);
		return rm;
	}
	
	public RoleMenu deleteRoleMenu(int roleMenuId, Token token) {
		RoleMenu rm = super.find(roleMenuId);
		rm.setIsDelete(1);
		rm.setDeleted(new Date());
		super.edit(rm, AuditHelper.OPERATION_DELETE, token);
		return rm;
	}
	
	public RoleMenu deleteRowRoleMenu(int roleMenuId, Token token) {
		RoleMenu rm = super.find(roleMenuId);
		super.remove(rm, AuditHelper.OPERATION_ROW_DELETE, token);
		return rm;
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
}
