package id.co.promise.procurement.master;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Mod10Check;

import com.mifmif.common.regex.Generex;

import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.RoleJabatan;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.UserAdditional;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.KeyGenHelper;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/procurement/master/userAdditional")
@Produces(MediaType.APPLICATION_JSON)
public class UserAdditionalServices {
	@EJB
	private UserAdditionalSession userAdditionalSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	EmailNotificationSession emailNotificationSession;
	
	@EJB
	UserSession userSession;
	
	@EJB
	RoleJabatanSession roleJabatanSession;
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB 
	ProcedureSession procedureSession;

	@Resource private UserTransaction userTransaction;
	
	@Path("/getUserAdditional/{id}")
	@GET
	public UserAdditional getUserAdditional(@PathParam("id") int id) {
		return userAdditionalSession.getUserAdditional(id);
	}

	@Path("/get-list")
	@POST
	public Response getUserAdditionalListPagination(
			@FormParam("pageNo") Integer pageNo, 
			@FormParam("pageSize") Integer pageSize, 
			@FormParam("status") String status,
			@FormParam("rolePlh") String rolePlh, 
			@FormParam("nama") String nama) {
		
		Integer statusInt = null;
		
		if(!status.isEmpty()) {
			statusInt = Integer.parseInt(status);
		}
		
		List<UserAdditional> userAdditionalList = userAdditionalSession.getUserAdditionalListPagination(pageNo, pageSize, statusInt, rolePlh, nama);
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("dataList", userAdditionalList);
		map.put("jmlData", userAdditionalSession.getuserAdditionalTotalList(statusInt, rolePlh, nama));

		
		return Response.ok(map).build();
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getUserAdditionalMax")
	@GET
	public Map getUserAdditionalMax() {

		Map<Object, Object> map = new HashMap<Object, Object>();
		UserAdditional userAdditional = userAdditionalSession.getUserAdditionalMax();
		Date date = new Date();  
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");  
	    String strDate= sdf.format(date);  
	    String[] strTahun = strDate.split("-");
	    String tahunNew =  strTahun[2];
	    String kode = "0001";
	   
		if(userAdditional != null) {
			String[] kodePlh = userAdditional.getNippPlh().split("\\.");
			String tampTahunOld = kodePlh[1];
			if(tahunNew.equalsIgnoreCase(tampTahunOld)) {
				Integer number = Integer.parseInt(kodePlh[2]);
				number += 1;
				String strKode = String.format("%04d", number);
				map.put("tahun", tampTahunOld);
				map.put("kodePlh", strKode);
			}else {
				map.put("tahun", tahunNew);
				map.put("kodePlh", kode);
			}
		}else {
			map.put("tahun", tahunNew);
			map.put("kodePlh", kode);
		}
		return map;
	}
	
	@Path("/insert")
	@POST
	public UserAdditional insert(UserAdditional userAdditional, @HeaderParam("Authorization") String token) 
			throws Exception{
		
		userTransaction.begin();
		User user = new User();
		user.setUsername(userAdditional.getNippPlh());
		user.setIsPlh(Constant.IS_PLH);
		user.setParentUserId(userAdditional.getUser().getId());
		user.setJabatan(userAdditional.getJabatan());
		user.setNamaPengguna(userAdditional.getUser().getNamaPengguna());
		user.setEmail(userAdditional.getUser().getEmail());
		if(userAdditional.getUser().getIdUserEOffice() != null) {
			user.setIdUserEOffice(userAdditional.getUser().getIdUserEOffice());
		}if(userAdditional.getUser().getIdPerusahaan() != null) {
			user.setIdPerusahaan(userAdditional.getUser().getIdPerusahaan());
		}if(userAdditional.getUser().getFlagLoginEbs() != null) {
			user.setFlagLoginEbs(userAdditional.getUser().getFlagLoginEbs());
		}if(userAdditional.getUser().getKode() != null) {
			user.setKode(userAdditional.getUser().getKode());
		}
		//generate password
		Generex generex = new Generex("[0-9][a-z]{7}");
	 	String password = generex.random();
	 	Date date = new Date();
	 	Date lastGenerate = userAdditional.getUser().getLastGeneratePassword();
	 	
	 	if(lastGenerate != null) {
	 		long diff = date.getTime() - lastGenerate.getTime();
	 		long diffMinutes = diff / (60 * 1000) % 60;
	 		if ( diffMinutes <= 15 ) {
	 			userAdditional = null;
	 			return userAdditional;
	 		}	 
	 	}
	 	
	 	String encryptedPassword = KeyGenHelper.finalEncrypt(password);
	 	user.setPassword(encryptedPassword);
	 	user.setLastGeneratePassword(date);
		 
		User user2 = userSession.insertUser(user, tokenSession.findByToken(token));
		UserAdditional userAdditional2 = userAdditionalSession.insertUserAdditional(userAdditional, tokenSession.findByToken(token));
		List<RoleJabatan> roleJabatanList = roleJabatanSession.getListByJabatanId(user2.getJabatan().getId());
		RoleUser roleUser1 = roleUserSession.getByUserId(userAdditional.getUser().getId());
		
		for (RoleJabatan roleJabatan : roleJabatanList) {
			RoleUser roleUser = new RoleUser();
			roleUser.setOrganisasi(roleUser1.getOrganisasi());
			roleUser.setRole(roleJabatan.getRole());
			roleUser.setUser(user2);
			
			roleUserSession.insertRoleUser(roleUser);
		}
		userTransaction.commit();
		
		if(userAdditional2 != null) {
			String email = user2.getEmail();
			if(email != null) {
				emailNotificationSession.getMailBerhasilDaftar(user2.getUsername(), email, password, user2, userAdditional.getOrganisasi().getNama());
			}
		}
		procedureSession.execute(Constant.DO_INSERT_U_LOGIN, user2.getId(), userAdditional2.getOrganisasi().getId());
		procedureSession.execute(Constant.SYNC_IN_UP_ADD_PLH_TO_CM, user2.getId());
		
		return userAdditional;
	}
	
	@Path("/update")
	@POST
	public UserAdditional update(UserAdditional userAdditional, @HeaderParam("Authorization") String token) {
	  try {
			userTransaction.begin();
			User user = userSession.getUserByUsername(userAdditional.getNippPlh());
			user.setId(user.getId());
			if(userAdditional.getUser().getEmail()==null) {
				user.setEmail(user.getEmail());
			}else {
				user.setEmail(userAdditional.getUser().getEmail());	
			}
            userAdditional.setUser(userAdditional.getUser());
            
			userSession.updateUser(user, tokenSession.findByToken(token));
			userAdditionalSession.updateUserAdditional(userAdditional, tokenSession.findByToken(token));
			userTransaction.commit();

			procedureSession.execute(Constant.DO_INSERT_U_LOGIN, user.getId(), userAdditional.getOrganisasi().getId());
			procedureSession.execute(Constant.SYNC_IN_UP_ADD_PLH_TO_CM, user.getId());

	  } catch (Exception e) {
		e.printStackTrace();
	  }
	  return userAdditional;
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/resendMail/{username}/{email}")
	@GET
	public Map resendMail(@PathParam("username") String username, @PathParam("email") String email) throws Exception {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String strResponse = "";
		User user = userSession.getUserByUsername(username);
		UserAdditional userAdditional = userAdditionalSession.getUserAdditionalByNippPlh(username);
		if(user != null && userAdditional != null) {
			String password = user.getPassword();
			String decryptedPassword = KeyGenHelper.finalDecrypt(password);
			if(email != null) {
				emailNotificationSession.getMailBerhasilDaftar(user.getUsername(), email, decryptedPassword, user, userAdditional.getOrganisasi().getNama());
				strResponse = "SUCCESS";
				map.put("responseStr", strResponse);
			}
		}else {
			strResponse = "GAGAL";
			map.put("responseStr", strResponse);
		}
		return map;
	}

}
