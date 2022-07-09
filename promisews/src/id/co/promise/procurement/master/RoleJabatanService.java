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

import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleJabatan;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;

@Stateless
@Path(value="/procurement/master/role-jabatan")
@Produces(MediaType.APPLICATION_JSON)
public class RoleJabatanService {

	@EJB
	private RoleJabatanSession roleJabatanSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB
	private UserSession userSession;
	
	@EJB 
	ProcedureSession procedureSession;
	@EJB
	private RoleSession roleSession;
	
	
	@Path("/get-list")
	@GET
	public List<RoleJabatan> getlist(){
		return roleJabatanSession.getList();
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(RoleJabatan roleJabatan,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		roleJabatanSession.insert(roleJabatan, tokenSession.findByToken(token));
		
		List<User> userList = userSession.getUserListByJabatanId(roleJabatan.getJabatan().getId());
			
		for (User user : userList ) {
			Role role = roleSession.find(roleJabatan.getRole().getId());
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), role.getAppCode());
			if (roleUserList.size() > 0 ) {
				for (RoleUser roleUserOld : roleUserList ) {
					roleUserSession.deleteRoleUser(roleUserOld.getId());
					RoleUser roleUser = new RoleUser();
					roleUser.setRole(roleJabatan.getRole());
					roleUser.setUser(user);
					roleUser.setOrganisasi(roleUserOld.getOrganisasi());
					roleUserSession.insertRoleUser(roleUser);
					procedureSession.execute(Constant.DO_INSERT_U_LOGIN, user.getId(),roleUserOld.getOrganisasi().getId());
					
				}	
			}
		}
		
		
		map.put("roleJabatan", roleJabatan);
		return map;
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(RoleJabatan roleJabatan,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		roleJabatanSession.update(roleJabatan, tokenSession.findByToken(token));
		
		List<User> userList = userSession.getUserListByJabatanId(roleJabatan.getJabatan().getId());
		
		
		for (User user : userList ) {
			Role role = roleSession.find(roleJabatan.getRole().getId());
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), role.getAppCode());
			for (RoleUser roleUserOld : roleUserList ) {
				roleUserSession.deleteRoleUser(roleUserOld.getId());
				RoleUser roleUser = new RoleUser();
				roleUser.setRole(roleJabatan.getRole());
				roleUser.setUser(user);
				roleUser.setOrganisasi(roleUserOld.getOrganisasi());
				roleUserSession.insertRoleUser(roleUser);
				procedureSession.execute(Constant.DO_INSERT_U_LOGIN, user.getId(),roleUserOld.getOrganisasi().getId());
				
			}	
		}

		map.put("roleJabatan", roleJabatan);
	  
		return map;
	}
	
	@Path("/delete")
	@POST
	public Map deleteRoleJabatan(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			
		RoleJabatan roleJabatan = roleJabatanSession.find(id);
		List<User> userList = userSession.getUserListByJabatanId(roleJabatan.getJabatan().getId());
		
		
		for (User user : userList ) {
			Role role = roleSession.find(roleJabatan.getRole().getId());
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserIdAndAppCode(user.getId(), role.getAppCode());
			for (RoleUser roleUserOld : roleUserList ) {
				roleUserSession.deleteRoleUser(roleUserOld.getId());
				procedureSession.execute(Constant.DO_INSERT_U_LOGIN, user.getId(),roleUserOld.getOrganisasi().getId());
				
			}	
		}

		 roleJabatanSession.delete(id, tokenSession.findByToken(token));
		 map.put("roleJabatan", roleJabatan);
		 return map;
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public RoleJabatan deleteRowRoleJabatan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return roleJabatanSession.deleteRow(id, tokenSession.findByToken(token));
	}
	
}
