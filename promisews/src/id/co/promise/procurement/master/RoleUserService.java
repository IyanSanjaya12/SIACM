package id.co.promise.procurement.master;


import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;

import java.util.ArrayList;
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
import javax.ws.rs.core.Response;

@Stateless
@Path(value = "/procurement/master/roleUserServices")
@Produces(MediaType.APPLICATION_JSON)
public class RoleUserService {
	@EJB
	private RoleUserSession roleUserSession;
	@EJB
	private TokenSession tokenSession; 
	@EJB
	private UserSession userSession;
	@EJB private OrganisasiSession organisasiSession;
	
	@Path("/getList")
	@GET
	public List <RoleUser> getRoleUserList(){
		return roleUserSession.getList();
	}
	
	@Path("/getRoleListByToken")
	@GET
	public List <Role> getRoleListByOrganisasi(@HeaderParam("Authorization") String token){
		Token tokenObj = tokenSession.findByToken(token);
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(tokenObj);
		List <Role> roleList = roleUserSession.getRoleListByOrganisasi(organisasi);;
		return roleList;
	}
	
	@Path("/getListByToken")
	@GET
	public List<RoleUser> getListByToken(
			@HeaderParam("Authorization") String token) {

		List<RoleUser> roleUserList = new ArrayList<RoleUser>();

		User user = userSession.getActiveAndValidUserByToken(token); 

		if (user != null) {
			roleUserList = roleUserSession.getRoleUserByUserId(user.getId());
		}
		
		return roleUserList;
	}
	
	@Path("/get-list-by-user-id-and-app-code/{userId}/{appCode}")
	@GET
	public List<RoleUser> getListByUserIdAndAppCode(
			@PathParam("userId") int userId,
			@PathParam("appCode") String appCode) {
		return roleUserSession.getRoleUserByUserIdAndAppCode(userId, appCode);
	}
	
	@Path("/getListByUserId/{userId}")
	@GET
	public List<RoleUser> getListByUserId(@PathParam("userId") int userId) {
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(userId);
		 return roleUserList;
	}
	
	@Path("/deletebyUserId/{id}")
	@GET
	public void deletebyUserId(
			@PathParam("id") int userId){
			roleUserSession.deletebyUserId(userId);;
	}
	
	@Path("/getListByParentUserIdAndRoleId/{parentUserId}/{roleIdPm}/{roleIdCm}")
	@GET
	public List<RoleUser> getListByParentUserIdAndRoleId(@PathParam("parentUserId") Integer parentUserId, @PathParam("roleIdPm") Integer roleIdPm, @PathParam("roleIdCm") Integer roleIdCm) {
		List<Integer> roleIdList = new ArrayList<Integer>();
		if(roleIdPm != null) {
			roleIdList.add(roleIdPm);
		}
		if(roleIdCm != null) {
			roleIdList.add(roleIdCm);
		}
		
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByParentUserIdAndRoleId(parentUserId, roleIdList);
		 return roleUserList;
	}
	
	@Path("/getUserListByOrganisasi")
	@POST
	public Response getUserListPagination(
			@FormParam("pageNo") Integer pageNo, 
			@FormParam("pageSize") Integer pageSize, 
			@FormParam("nama") String nama,
			@FormParam("organisasiId") Integer organisasiId) {
		
		List<RoleUser> roleUserList = roleUserSession.getUserListPagination(pageNo, pageSize, nama, organisasiId);
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("dataUserList", roleUserList);
		map.put("jmlData", roleUserSession.getuserTotalList(nama, organisasiId));

		
		return Response.ok(map).build();
	}
	
}
