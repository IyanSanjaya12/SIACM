package id.co.promise.procurement.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.promise.procurement.entity.Menu;
import id.co.promise.procurement.entity.RoleMenu;
import id.co.promise.procurement.master.RoleSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.CacheManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value="/procurement/menu/role-menu")
@Produces(MediaType.APPLICATION_JSON)
public class RoleMenuService {

	@EJB
	private RoleMenuSession roleMenuSession;
	
	@EJB
	private MenuSession menuSession;
	
	@EJB
	private RoleSession roleSession;
	
	@EJB
	TokenSession tokenSession;
	
	/*
	 * @Path("/get-role-menu/{id}")
	 * 
	 * @GET public RoleMenu getRoleMenu(@PathParam("id")int id){ return
	 * roleMenuSession.getRoleMenu(id); }
	 */
	
	@Path("/get-list-by-role/{roleId}")
	@GET
	public List<RoleMenu> getRoleMenuListByRole(@PathParam("roleId")int roleId){
		return roleMenuSession.getRoleMenuListByRole(roleId);
	}
	
	@Path("/get-root-list-by-role/{roleId}")
	@GET
	public List<RoleMenu> getRoleMenuRootListByRole(@PathParam("roleId")int roleId){
		return roleMenuSession.getRoleMenuRootListByRole(roleId);
	}
	
	@Path("/get-list")
	@GET
	public List<RoleMenu> getRoleMenulist(){
		return roleMenuSession.getRoleMenuList();
	}
	
	/*@Path("/insert")
	@POST
	public RoleMenu insertRoleMenu(
			@FormParam("role")Integer role,
			@FormParam("menu")Integer menu,
			@HeaderParam("Authorization") String token){
		RoleMenu rm = new RoleMenu();
		rm.setRole(roleSession.find(role));
		rm.setMenu(menuSession.find(menu));
		rm.setUserId(0);
		roleMenuSession.insertRoleMenu(rm, tokenSession.findByToken(token));
		return rm;
	}
	
	@Path("/update")
	@POST
	public RoleMenu updateRoleMenu(
			@FormParam("id")Integer id,
			@FormParam("role")Integer role,
			@FormParam("menu")Integer menu,
			@HeaderParam("Authorization") String token){
		RoleMenu rm = roleMenuSession.find(id);
		rm.setRole(roleSession.find(role));
		rm.setMenu(menuSession.find(menu));
		roleMenuSession.updateRoleMenu(rm, tokenSession.findByToken(token));
		return rm;
	}

	@Path("/delete/{id}")
	@GET
	public RoleMenu deleteRoleMenu(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return roleMenuSession.deleteRoleMenu(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public RoleMenu deleteRowRoleMenu(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return roleMenuSession.deleteRowRoleMenu(id, tokenSession.findByToken(token));
	}*/
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Path("/insert")
	@POST
	public Map insert(RoleMenu roleMenu,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
	  
		Boolean isSaveNama = roleMenuSession.checkNamaRoleMenu(roleMenu.getRole().getId(), roleMenu.getMenu().getId(), toDo, roleMenu.getId());
	  
		if(!isSaveNama) {
			String errorMenuNama = "Role ini Sudah Mempunyai Menu Tersebut";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMenuNama", errorMenuNama);
		}
	 
		if(isSaveNama) {
			roleMenuSession.insertRoleMenu(roleMenu, tokenSession.findByToken(token));
		}
		
		String key = "mn_" + roleMenu.getRole().getCode().toLowerCase();
		List<Menu> menuList = (List<Menu>) CacheManager.getByKey(key, List.class);
		if (menuList != null) {
			CacheManager.deleteByKey(key);
		}
	  
		map.put("roleMenu", roleMenu);
	  
		return map;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Path("/update")
	@POST
	public Map update(RoleMenu roleMenu,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
	  
		Boolean isSaveNama = roleMenuSession.checkNamaRoleMenu(roleMenu.getRole().getId(), roleMenu.getMenu().getId(), toDo, roleMenu.getId());
	  
		if(!isSaveNama) {
			String errorMenuNama = "Role ini Sudah Mempunyai Menu Tersebut";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMenuNama", errorMenuNama);
		}
	 
		if(isSaveNama) {
			roleMenuSession.updateRoleMenu(roleMenu, tokenSession.findByToken(token));
		}
	  
		map.put("roleMenu", roleMenu);
	  
		String key = "mn_" + roleMenu.getRole().getCode().toLowerCase();
		List<Menu> menuList = (List<Menu>) CacheManager.getByKey(key, List.class);
		if (menuList != null) {
			CacheManager.deleteByKey(key);
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Path("/delete")
	@POST
	public RoleMenu deleteRoleMenu(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		
		RoleMenu roleMenu = roleMenuSession.deleteRoleMenu(id, tokenSession.findByToken(token));
		String key = "mn_" + roleMenu.getRole().getCode().toLowerCase();
		List<Menu> menuList = (List<Menu>) CacheManager.getByKey(key, List.class);
		if (menuList != null) {
			CacheManager.deleteByKey(key);
		}
		
		return roleMenu;
	}
	
	@SuppressWarnings("unchecked")
	@Path("/deleteRow/{id}")
	@GET
	public RoleMenu deleteRowRoleMenu(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		RoleMenu roleMenu = roleMenuSession.deleteRowRoleMenu(id, tokenSession.findByToken(token));
		String key = "mn_" + roleMenu.getRole().getCode().toLowerCase();
		List<Menu> menuList = (List<Menu>) CacheManager.getByKey(key, List.class);
		if (menuList != null) {
			CacheManager.deleteByKey(key);
		}
		return roleMenu;
	}
	
}
