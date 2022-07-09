package id.co.promise.procurement.security;

import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import com.mifmif.common.regex.Generex;

import id.co.promise.procurement.approval.UserDTO;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.RoleSession;
import id.co.promise.procurement.master.UserMasterDTO;
import id.co.promise.procurement.utils.KeyGenHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path(value = "/procurement/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
	final static Logger logger = Logger.getLogger(UserService.class);

	@EJB
	UserSession userSession;
	@EJB
	RoleSession roleSession;
	@EJB
	RoleUserSession roleUserSession;
	@EJB
	VendorSession vendorSession;
	@EJB
	TokenSession tokenSession;
	@EJB
	OrganisasiSession organisasiSession;
	@EJB
	private EmailNotificationSession emailNotificationSession;

	@Path("/get-user/{id}")
	@GET
	public User getUser(@PathParam("id") int id) {
		return userSession.getUser(id);
	}

	@Path("/get-role-user-by-role-id-and-name/{id}/{nama}")
	@GET
	public List<RoleUser> getRoleUser(@PathParam("id") int id, @PathParam("nama") String nama) {
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByRoleIdAndnameList(id, nama);
		List<RoleUser> roleUserListNew = new ArrayList<RoleUser>();
		for (RoleUser roleUser : roleUserList) {
			User user = roleUser.getUser();
			user.setPassword(new String("#!mask=="));
			roleUser.setUser(user);
			roleUserListNew.add(roleUser);
		}
		return roleUserListNew;
	}

	@Path("/get-list-user-non-vendor")
	@GET
	public List<User> getListUserNonVendor() {
		return userSession.getListUserNonVendor();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/generate")
	@POST
	public UserMasterDTO generate(UserMasterDTO userMasterDTO, @HeaderParam("Authorization") String token)
			throws Exception {
		/*
		 * String password = "";
		 * 
		 * Random random = new Random(); for (int i = 0; i < 8 ; i++) { int length =
		 * random.nextInt(9); password += length; }
		 */

		Generex generex = new Generex("[0-9][a-z]{5}");
		String password = generex.random();
		Date date = new Date();
		Date lastGenerate = userMasterDTO.getUser().getLastGeneratePassword();

		long diff = date.getTime() - lastGenerate.getTime();

		/*
		 * long diffSeconds = diff / 1000 % 60; long diffHours = diff / (60 * 60 * 1000)
		 * % 24; long diffDays = diff / (24 * 60 * 60 * 1000);
		 */

		long diffMinutes = diff / (60 * 1000) % 60;

		if (lastGenerate != null) {
			if (diffMinutes <= 15) {
				userMasterDTO = null;
				return userMasterDTO;
			}
		}
		emailNotificationSession.getMailGeneratorUserGeneratePassword(userMasterDTO.getUser(), password);
		String encryptedPassword = KeyGenHelper.finalEncrypt(password);
		userMasterDTO.getUser().setPassword(encryptedPassword);
		userMasterDTO.getUser().setLastGeneratePassword(date);
		userSession.updateUser(userMasterDTO.getUser(), tokenSession.findByToken(token));

		return userMasterDTO;
	}

	@Path("/update-user-data")
	@POST
	public UserMasterDTO updateDataUser(UserMasterDTO userMasterDTO, @HeaderParam("Authorization") String token)
			throws Exception {
		Integer userId = userMasterDTO.getUser().getId();
		User user = userSession.find(userId);
		if (!user.getPassword().equals(userMasterDTO.getUser().getPassword())) {
			userMasterDTO.getUser().setPassword(KeyGenHelper.finalEncrypt(userMasterDTO.getUser().getPassword()));
		} else {
			userMasterDTO.getUser().setPassword(userMasterDTO.getUser().getPassword());
		}
		userSession.updateUser(userMasterDTO.getUser(), tokenSession.findByToken(token));
		Vendor vendor = userMasterDTO.getVendor();

		if (vendor != null) {// kalau user yang ganti password akan masuk ke sini
			vendorSession.getVendorByUserId(userId);// vendor.setNama(namaPengguna);
			vendorSession.updateVendor(vendor, tokenSession.findByToken(token));
		}
		return userMasterDTO;
	}

	@Path("/get-user-and-vendor/{userId}")
	@GET
	public UserMasterDTO getUserAndVendor(@PathParam("userId") Integer userId) {
		return userSession.getUserAndVendor(userId);
	}

	@Path("/get-role-user/{userId}")
	@GET
	public List<RoleUser> getRoleUser(@PathParam("userId") Integer userId) {
		return roleUserSession.getRoleUserByUserId(userId);
	}

	@Path("/get-role-user-vendor/{userId}")
	@GET
	public Boolean getRoleUserVendor(@PathParam("userId") Integer userId) {
		Boolean hasil = false;
		List<RoleUser> roleUser = roleUserSession.getRoleUserByUserId(userId);
		if (roleUser.size() > 0) {
			if (roleUser.get(0).getRole().getNama().equals("VENDOR")) {
				hasil = true;
			}
		}
		return hasil;
	}

	@Path("/get-role")
	@GET
	public List<Role> getRole() {
		return roleSession.findAll();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(UserMasterDTO userMasterDTO, @HeaderParam("Authorization") String token) throws Exception {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveUsername = userSession.checkUsername(userMasterDTO.getUser().getUsername(), toDo,
				userMasterDTO.getUser().getId());
		Boolean isSaveEmail = userSession.checkEmail(userMasterDTO.getUser().getEmail(), toDo,
				userMasterDTO.getUser().getId());

		if (!isSaveUsername) {
			String errorUsername = "promise.procurement.master.user.error.username_sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorUsername", errorUsername);
		}

		if (!isSaveEmail) {
			String errorEmail = "promise.procurement.master.user.error.email_sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorEmail", errorEmail);
		}

		if (isSaveUsername && isSaveEmail) {
			User user = userMasterDTO.getUser();
			user.setPassword(KeyGenHelper.finalEncrypt(userMasterDTO.getUser().getPassword()));
			userSession.insertUser(user, tokenSession.findByToken(token));

			// ssoHelper.doInsertULogin(user.getEmail(), user.getUsername(),
			// user.getNamaPengguna(), 1,);

			for (Role role : userMasterDTO.getRoleList()) {

				if (role.getAppCode().equalsIgnoreCase("PM")) {
					RoleUser roleUser = new RoleUser();
					roleUser.setUser(userMasterDTO.getUser());
					roleUser.setRole(role);
					roleUser.setOrganisasi(organisasiSession.find(userMasterDTO.getOrganisasiId()));
					roleUserSession.insertRoleUser(roleUser);
				}

				if (role.getAppCode().equalsIgnoreCase("CM")) {
					RoleUser roleUser = new RoleUser();
					roleUser.setUser(userMasterDTO.getUser());
					roleUser.setRole(role);
					roleUser.setOrganisasi(organisasiSession.find(userMasterDTO.getOrganisasiId()));
					roleUserSession.insertRoleUser(roleUser);
				}
			}
		}
		map.put("userMasterDTO", userMasterDTO);
		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(UserMasterDTO userMasterDTO, @HeaderParam("Authorization") String token) throws Exception {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveUsername = userSession.checkUsername(userMasterDTO.getUser().getUsername(), toDo,
				userMasterDTO.getUser().getId());
		Boolean isSaveEmail = userSession.checkEmail(userMasterDTO.getUser().getEmail(), toDo,
				userMasterDTO.getUser().getId());

		if (!isSaveUsername) {
			String errorUsername = "Username Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorUsername", errorUsername);
		}

		if (!isSaveEmail) {
			String errorEmail = "Email Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorEmail", errorEmail);
		}

		if (isSaveUsername && isSaveEmail) {
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(userMasterDTO.getUser().getId());
			for (RoleUser roleUser : roleUserList) {
				roleUserSession.deleteRoleUser(roleUser.getId());
			}
			User user = userMasterDTO.getUser();
			user.setPassword(KeyGenHelper.finalEncrypt(userMasterDTO.getUser().getPassword()));
			userSession.updateUser(user, tokenSession.findByToken(token));

			for (Role role : userMasterDTO.getRoleList()) {

				if (role.getAppCode().equalsIgnoreCase("PM")) {
					RoleUser roleUser = new RoleUser();
					roleUser.setUser(userMasterDTO.getUser());
					roleUser.setRole(role);
					roleUser.setOrganisasi(organisasiSession.find(userMasterDTO.getOrganisasiId()));
					roleUserSession.insertRoleUser(roleUser);
				}

				if (role.getAppCode().equalsIgnoreCase("CM")) {
					RoleUser roleUser = new RoleUser();
					roleUser.setUser(userMasterDTO.getUser());
					roleUser.setRole(role);
					roleUser.setOrganisasi(organisasiSession.find(userMasterDTO.getOrganisasiId()));
					roleUserSession.insertRoleUser(roleUser);
				}

			}

		}

		map.put("userMasterDTO", userMasterDTO);
		return map;
	}

	@SuppressWarnings({})
	@Path("/delete")
	@POST
	public UserMasterDTO delete(UserMasterDTO userMasterDTO, @HeaderParam("Authorization") String token) {
		userSession.deleteUser(userMasterDTO.getUser().getId(), tokenSession.findByToken(token));
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(userMasterDTO.getUser().getId());
		for (RoleUser roleUser : roleUserList) {
			roleUserSession.deleteRoleUser(roleUser.getId());
		}
		return userMasterDTO;
	}

	@Path("/get-by-username/{email}")
	@GET
	public List<User> getUserByUserName(@PathParam("email") String email) {
		return userSession.getUserByUserName(email);
	}

	@Path("/get-by-real-username/{username}")
	@GET
	public List<User> getUserByRealUserName(@PathParam("username") String email) {
		return userSession.getUserByUserName(email);
	}

	@Path("/get-online-user")
	@GET
	public int getOnlineUser() {
		return userSession.getOnlineUser();
	}

	@Path("/recover")
	@POST
	public Response recover(@FormParam("email") String email) {
		try {
			return Response.ok(emailNotificationSession.getMailLoginRecover(email)).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@Path("/recover/reset")
	@POST
	public Response recoverReset(@FormParam("hash") String emailHash) {
		try {
			return Response.ok(emailNotificationSession.getMailLoginRecoverResetPassword(emailHash)).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@SuppressWarnings("rawtypes")
	@Path("/get-detail-user-list")
	@GET
	public List getDetailUserList() {
		return userSession.getDetailUserList();
	}

	@SuppressWarnings("rawtypes")
	@Path("/get-all-list")
	@GET
	public List getAllList() {
		return userSession.getAllList();
	}

	@Path("/get-list-by-user-id-and-app-code/{userId}/{appCode}")
	@GET
	public List<RoleUser> getListByUserIdAndAppCode(@PathParam("userId") int userId,
			@PathParam("appCode") String appCode) {
		return roleUserSession.getRoleUserByUserIdAndAppCode(userId, appCode);
	}

	@Path("/getUserByUsername/{username}")
	@GET
	public User getUserByUsername2(@PathParam("username") String username) {
		return userSession.getUserByUsername(username);
	}

	@Path("/get-list-user-by-organisasi/{organisasiId}")
	@GET
	public List<UserDTO> getListUserByOrganisasi(@PathParam("organisasiId") Integer organisasiId) {
	  List<Organisasi> organisasiList = organisasiSession.getAllParentListByChild(organisasiId);
	  List<Object[]> objList = new ArrayList<Object[]>();
	  objList.addAll(userSession.getUserListByOrganisasiIdList(organisasiList));
	  List<UserDTO> userDtoList = new ArrayList<UserDTO>();
	  for(Object[] obj : objList) {
			/* perubahan KAI 25/11/2020 */
		  UserDTO userDtoTamp = new UserDTO(roleUserSession.getByUserId((Integer)obj[1]));		  
		  userDtoList.add(userDtoTamp);
	  }
	  return userDtoList;
	}
}
