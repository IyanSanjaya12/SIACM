package id.co.promise.procurement.master;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import id.co.promise.procurement.entity.Dashboard;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value="/procurement/master/role")
@Produces(MediaType.APPLICATION_JSON)
public class RoleService {

	@EJB private RoleSession roleSession;
	
	@EJB private TokenSession tokenSession;
	
	@EJB private DashboardSession dashboardSession;
	
	/*@Path("/get-master-email-notification/{id}")
	@GET
	public Role getRole(@PathParam("id")int id){
		return roleSession.getRole(id);
	}*/
	
	@Path("/get-list")
	@GET
	public List<Role> getRolelist(){
		return roleSession.getRolelist();
	}
	
	@Path("/get-dashboard-list")
	@GET
	public List<Dashboard> getDashboardList(){
		return dashboardSession.getDashboardList();
	}
	
	@Path("/get-list-non-vendor")
	@GET
	public List<Role> getRolelistNonVendor(){
		return roleSession.getRolelistNonVendor();
	}
	
	@Path("/getRoleListPm")
	@GET
	public List<Role> getRolelistPm(){
		return roleSession.getRoleByAppCodePm();
	}
	
	@Path("/getRoleListCm")
	@GET
	public List<Role> getRolelistCm(){
		return roleSession.getRoleByAppCodeCm();
	}	
	
	@Path("/get-role-by-application/{appCode}")
	@GET
	public List<Role> getRoleByApplication(@PathParam("appCode") String appCode){
		return roleSession.getRoleByApplication(appCode);
	}
	
	/*@Path("/getRoleByKode")
	@POST
	public Integer getRoleByKode(
			@FormParam("id") Integer id,
			@FormParam("code") String kode,
			@HeaderParam("Authorization") String token){
		
		List<Role> roleList = new ArrayList<Role>();
		Integer flagExsist 	= 0;
		roleList 			= roleSession.getRoleByKode(id,kode);
		if(roleList.size() > 0){
			flagExsist = 1; //if exsist set one(1).
		}
		
		return flagExsist;
	}*/
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(Role role, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		
		Boolean isSaveKode =
		roleSession.checkKodeRole(role.getCode(), toDo, role.getId());
		
		if(!isSaveKode){
			String errorKode = "Kode Role Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorKode", errorKode);
		}
		 
		if(isSaveKode) {
			roleSession.insertRole(role,tokenSession.findByToken(token));
		}
		
		map.put("Role", role);

		return map;
	}
		
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(Role role, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
		
		Boolean isSaveKode =
		roleSession.checkKodeRole(role.getCode(), toDo, role.getId());
		
		if(!isSaveKode){
			 String errorKode = "Kode Role Tidak Boleh Sama";
			   Boolean isValid = false;
			   map.put("isValid", isValid);
			   map.put("errorKode", errorKode);
		}
		 
		if(isSaveKode) {
			  roleSession.updateRole(role,tokenSession.findByToken(token));
			  }
		
		map.put("Role", role);

		return map;	
	}
		
	@Path("/delete")
	@POST
	public Role deleteRole(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return roleSession.deleteRole(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public Role deleteRowRole(@PathParam("id") int id,@HeaderParam("Authorization") String token) {
		return roleSession.deleteRowRole(id, tokenSession.findByToken(token));
	}
}
