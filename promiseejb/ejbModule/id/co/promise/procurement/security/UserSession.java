package id.co.promise.procurement.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.TokenRemember;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.UserToken;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.RoleJabatanSession;
import id.co.promise.procurement.master.RoleSession;
import id.co.promise.procurement.master.UserMasterDTO;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.KeyGenHelper;
import id.co.promise.procurement.utils.ParamContext;
import id.co.promise.procurement.utils.SecurityHelper;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class UserSession extends AbstractFacadeWithAudit<User> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	TokenSession tokenSession;

	@EJB
	RoleJabatanSession roleJabatanSession;

	@EJB
	JabatanSession jabatanSession;

	@EJB
	SyncSession syncSession;
	@EJB
	OrganisasiSession organisasiSession;
	@EJB
	RoleSession roleSession;
	@EJB
	VendorSession vendorSession;
	@EJB
	RoleUserSession roleUserSession;
	@EJB
	TokenRememberSession tokenRememberSession;

	final static Logger logger = Logger.getLogger(UserSession.class);
	static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

	public UserSession() {
		super(User.class);
	}

	public User getUser(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<User> getUserList() {
		Query q = em.createNamedQuery("User.find");
		return q.getResultList();
	}

	// get user non vendor
	@SuppressWarnings("unchecked")
	public List<User> getListUserNonVendor() {
		Query q = em.createNamedQuery("User.findNonVendor");
		return q.getResultList();
	}

	public User registrasiUserVendor(String email, String password, Token token) {
		User user = new User();

		user.setEmail(email);
		user.setUsername(email);
		try {
			user.setPassword(KeyGenHelper.finalEncrypt(password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setIsDelete(0);
		user.setCreated(new Date());
		super.create(user, AuditHelper.OPERATION_CREATE, token);

		Vendor vendor = new Vendor();
		vendor.setUser(user.getId());
		vendor.setEmail(email);
		vendor.setIsDelete(0);
		vendor.setCreated(new Date());
		vendorSession.create(vendor, AuditHelper.OPERATION_CREATE, token);

		RoleUser roleUser = new RoleUser();
		roleUser.setUser(user);
		Role role = new Role();
		role.setId(2); // vendor
		roleUser.setRole(role);
		roleUser.setIsDelete(0);
		roleUser.setCreated(new Date());
		roleUserSession.create(roleUser);

		return user;
	}
	/*
	 * public User updateUser(User user, Token token) { user.setUpdated(new Date());
	 * super.edit(user, AuditHelper.OPERATION_UPDATE, token); return user; }
	 */

	/*
	 * public User updateDataUser(int userId, String namaVendor, String
	 * namaPengguna, String email, String passwordOld, String passwordNew, Token
	 * token) { User user = new User(); try { user = super.find(userId);
	 * user.setUpdated(new Date()); user.setNamaPengguna(namaPengguna);
	 * user.setEmail(email); if (user.getPassword().equalsIgnoreCase(
	 * KeyGenHelper.finalEncrypt(passwordOld))) {
	 * user.setPassword(KeyGenHelper.finalEncrypt(passwordNew)); } super.edit(user,
	 * AuditHelper.OPERATION_UPDATE, token);
	 * 
	 * Vendor vendor = vendorSession.getVendorByUserId(userId);
	 * //vendor.setNama(namaPengguna); vendor.setUpdated(new Date());
	 * vendorSession.edit(vendor, AuditHelper.OPERATION_UPDATE, token); } catch
	 * (Exception e) { logger.info("User = " + userId); }
	 * 
	 * return user; }
	 */

	public User getActiveAndValidUserByToken(String strToken) {
		Token token = tokenSession.findByToken(strToken);

		User user = null;

		if (token != null) {
			// check is user never logout
			if (token.getLogout() == null) {
				// check is user hasnt session timeout
				java.util.Date now = new java.util.Date();
				long minutesFromLastActivity = SecurityHelper.getMinutesIdle(now.getTime(),
						token.getLastActive().getTime());
				if (minutesFromLastActivity < ParamContext.getParameterIntegerByName("SESSION_TIMEOUT")) {
					user = token.getUser();
				}
			}
		}

		return user;
	}

	public User getUserByEmail(String email) {
		Query q = em.createNamedQuery("User.findByEmail");
		q.setParameter("email", email);

		try {
			return (User) q.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}

	public User getPasswordByUser(Integer userId, String password) throws Exception {
		User user = super.find(userId);
		if (user.getPassword().equalsIgnoreCase(KeyGenHelper.finalEncrypt(password))) {
			return user;
		}
		return null;
	}

	public User getLogOut(String token) {
		Token t = tokenSession.findByToken(token);
		t.setLogout(new Date());
		tokenSession.edit(t);
		return t.getUser();
	}

	@SuppressWarnings("unchecked")
	public UserToken getAuthentification(String username, Boolean rememberMe, Boolean firstLogin,
			Date date, String ipAddress) throws Exception {
		UserToken userToken = null;

		/*
		 * Query q =
		 * em.createNamedQuery("User.getUserByUsername").setParameter("username" ,
		 * username);
		 */

		/* astra, user login by email */
		Query q = em.createNamedQuery("User.getUserByUsername").setParameter("username", username);

		List<User> listUser = q.getResultList();
		if (listUser.size() > 0) {
			User user = listUser.get(0);
			//if (user.getPassword().equals(KeyGenHelper.finalEncrypt(password))) {
				Token token = new Token();

				token.setUser(user);

				token.setCreated(date);

				token.setToken(KeyGenHelper.finalEncrypt(user.getUsername() + "" + date.getTime()));

				token.setLoginTime(date);
				token.setLastActive(date);
				token.setLoginIp(ipAddress);

				token.setLogout(null);
				tokenSession.create(token);

				TokenRemember tokenRemember = new TokenRemember();
				if (rememberMe && firstLogin) {
					tokenRemember.setCreated(date);
					tokenRemember.setRememberMe(1);
					tokenRemember.setToken(token.getToken());
					tokenRemember.setLoginTime(date);
					tokenRemember.setUser(user);
					tokenRememberSession.createTokenRemember(tokenRemember, token);
				} else if (!firstLogin) {
					TokenRemember token2 = tokenRememberSession.findByUser(user);
					token2.setToken(token.getToken());
					tokenRememberSession.editTokenRemember(token2, token);
				}

				userToken = new UserToken();
				userToken.setToken(KeyGenHelper.finalEncrypt(user.getUsername() + "" + date.getTime()));
				userToken.setUser(user);
			//}
		}
		return userToken;
	}
	
	@SuppressWarnings("unchecked")
	public UserToken getAuthentification(String username, String password, Boolean rememberMe, Boolean firstLogin,
			Date date, String ipAddress) throws Exception {
		UserToken userToken = null;

		/*
		 * Query q =
		 * em.createNamedQuery("User.getUserByUsername").setParameter("username" ,
		 * username);
		 */

		/* astra, user login by email */
		Query q = em.createNamedQuery("User.getUserByUsername").setParameter("username", username);

		List<User> listUser = q.getResultList();
		if (listUser.size() > 0) {
			User user = listUser.get(0);
			if (user.getPassword().equals(KeyGenHelper.finalEncrypt(password))) {
				Token token = new Token();

				token.setUser(user);

				token.setCreated(date);

				token.setToken(KeyGenHelper.finalEncrypt(user.getUsername() + "" + date.getTime()));

				token.setLoginTime(date);
				token.setLastActive(date);
				token.setLoginIp(ipAddress);

				token.setLogout(null);
				tokenSession.create(token);

				TokenRemember tokenRemember = new TokenRemember();
				if (rememberMe && firstLogin) {
					tokenRemember.setCreated(date);
					tokenRemember.setRememberMe(1);
					tokenRemember.setToken(token.getToken());
					tokenRemember.setLoginTime(date);
					tokenRemember.setUser(user);
					tokenRememberSession.createTokenRemember(tokenRemember, token);
				} else if (!firstLogin) {
					TokenRemember token2 = tokenRememberSession.findByUser(user);
					token2.setToken(token.getToken());
					tokenRememberSession.editTokenRemember(token2, token);
				}

				userToken = new UserToken();
				userToken.setToken(KeyGenHelper.finalEncrypt(user.getUsername() + "" + date.getTime()));
				userToken.setUser(user);
			}
		}
		return userToken;
	}
	
	@SuppressWarnings("unchecked")
	public UserToken getAuthentificationByEmail(String username, String password, Boolean rememberMe, Boolean firstLogin,
			Date date, String ipAddress) throws Exception {
		UserToken userToken = null;

		/*
		 * Query q =
		 * em.createNamedQuery("User.getUserByUsername").setParameter("username" ,
		 * username);
		 */

		/* astra, user login by email */
		Query q = em.createNamedQuery("User.findByEmail").setParameter("email", username);

		List<User> listUser = q.getResultList();
		if (listUser.size() > 0) {
			User user = listUser.get(0);
			if (user.getPassword().equals(KeyGenHelper.finalEncrypt(password))) {
				Token token = new Token();

				token.setUser(user);

				token.setCreated(date);

				token.setToken(KeyGenHelper.finalEncrypt(user.getUsername() + "" + date.getTime()));

				token.setLoginTime(date);
				token.setLastActive(date);
				token.setLoginIp(ipAddress);

				token.setLogout(null);
				tokenSession.create(token);

				TokenRemember tokenRemember = new TokenRemember();
				if (rememberMe && firstLogin) {
					tokenRemember.setCreated(date);
					tokenRemember.setRememberMe(1);
					tokenRemember.setToken(token.getToken());
					tokenRemember.setLoginTime(date);
					tokenRemember.setUser(user);
					tokenRememberSession.createTokenRemember(tokenRemember, token);
				} else if (!firstLogin) {
					TokenRemember token2 = tokenRememberSession.findByUser(user);
					token2.setToken(token.getToken());
					tokenRememberSession.editTokenRemember(token2, token);
				}

				userToken = new UserToken();
				userToken.setToken(KeyGenHelper.finalEncrypt(user.getUsername() + "" + date.getTime()));
				userToken.setUser(user);
			}
		}
		return userToken;
	}


	public Token getAuthentification(String token) {
		return tokenSession.getAuthentification(token);
	}

	public User insertUser(User user, Token token) {
		user.setCreated(new Date());
		if(user.getIsDelete() == null) {
			user.setIsDelete(0);
		}
		user.setUserId(0);
		super.create(user, AuditHelper.OPERATION_CREATE, token);
		return user;
	}

	public User updateUser(User user, Token token) {
		user.setUpdated(new Date());
		if(user.getIsDelete() == null) {
			user.setIsDelete(0);
		}
		super.edit(user, AuditHelper.OPERATION_UPDATE, token);
		return user;
	}

	public User deleteUser(int id, Token token) {
		User user = super.find(id);
		user.setDeleted(new Date());
		user.setIsDelete(1);
		super.edit(user, AuditHelper.OPERATION_DELETE, token);
		return user;
	}

	public User deleteUserClasic(int id, Token token) {
		User user = super.find(id);
		user.setDeleted(new Date());
		user.setIsDelete(1);
		super.edit(user, AuditHelper.OPERATION_DELETE, token);
		return user;
	}

	public User deleteRow(int id, Token token) {
		User user = super.find(id);
		super.remove(user, AuditHelper.OPERATION_ROW_DELETE, token);
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUserByUserName(String email) {
		Query q = em.createNamedQuery("User.findByEmail");
		q.setParameter("email", email);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<User> getUserByRealUserName(String username) {
		Query q = em.createNamedQuery("User.getUserByUsername");
		q.setParameter("username", username);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUserListByJabatanId(Integer jabatanId) {
		Query q = em.createNamedQuery("User.getUserByJabatanId");
		q.setParameter("jabatanId", jabatanId);
		return q.getResultList();
	}

	public int getOnlineUser() {
		return tokenSession.getOnlineUser();
	}

	@SuppressWarnings("unchecked")
	public User getUserByUsername(String username) {
		Query q = em.createNamedQuery("User.getUserByUsername");
		q.setParameter("username", username);
		List<User> listUser = q.getResultList();
		User user = null;
		if (listUser.size() > 0) {
			user = listUser.get(0);
		}
		return user;
	}

	public UserMasterDTO getUserAndVendor(Integer userId) {
		User user = super.find(userId);
		UserMasterDTO userMasterDTO = new UserMasterDTO();
		userMasterDTO.setUser(user);
		userMasterDTO.setVendor(vendorSession.getVendorByUserId(userId));
		return userMasterDTO;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkUsername(String username, String toDo, Integer userId) {
		Query q = em.createNamedQuery("User.findByUsername");
		q.setParameter("username", username);
		List<User> userList = q.getResultList();
		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (userList != null && userList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (userList != null && userList.size() > 0) {
				if (userId.equals(userList.get(0).getId())) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAllList() {
		List allList = new ArrayList();
		List<Role> rolePm = roleSession.getRoleByAppCodePm();
		List<Role> roleCm = roleSession.getRoleByAppCodeCm();
		UserMasterDTO userMasterDto = new UserMasterDTO();
		String organisasi = organisasiSession.getOrganisasiAllJSON();
		userMasterDto.setOrganisasiString(organisasi);
		userMasterDto.setRoleCmList(roleCm);
		userMasterDto.setRolePmList(rolePm);
		allList.add(userMasterDto);
		return allList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getDetailUserList() {
		List detailUserList = new ArrayList();
		Query q = em.createNamedQuery("User.find");
		List<User> userList = q.getResultList();
		for (User user : userList) {
			UserMasterDTO userMasterDto = new UserMasterDTO();
			userMasterDto.setUser(user);
			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(user.getId());
			// userMasterDto.setOrganisasi(roleUserList.get(0).getOrganisasi());
			userMasterDto.setRoleUser(roleUserList);
			String roleUserString = "";
			int roleId = 0;
			for (RoleUser roleUser : roleUserList) {
				roleId = roleUser.getRole().getId();
				userMasterDto.setRoleId(roleId);
				roleUserString = roleUserString
						.concat(roleUser.getRole().getNama() + " (" + roleUser.getRole().getAppCode() + "), ");
				userMasterDto.setRoleUserString(roleUserString);
			}
			// userMasterDto.setRole(RoleList);
			detailUserList.add(userMasterDto);
		}
		return detailUserList;
	}

	@SuppressWarnings("unchecked")
	public Boolean checkEmail(String email, String toDo, Integer userId) {
		Query q = em.createNamedQuery("User.findByEmail");
		q.setParameter("email", email);
		List<User> userList = q.getResultList();
		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (userList != null && userList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (userList != null && userList.size() > 0) {
				if (userId.equals(userList.get(0).getId())) {
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
	
	@SuppressWarnings("unchecked")
	public User getByToken(Token token) {
		Query q = em.createNamedQuery("User.findByTokenUserId");
		q.setParameter("userId", token.getUser().getId());
		List<User> userList = q.getResultList();
		try {
			return userList.get(0);	
		}catch(Exception e) {
			return null;
		}
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
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserListByOrganisasiIdList( List<Organisasi> organisasiList ) {
		Query q = em.createNamedQuery("User.getUserListByOrganisasiIdList");
		q.setParameter("organisasiList", organisasiList);
		return q.getResultList();
	}

	/* KAI 22042021 [23186] */
	@SuppressWarnings("unchecked")
	public List<User> getDataIndexTableLogByTableUser() {
		String query = "SELECT"
					+  " DISTINCT"
					+  " 	u.NAMA_PENGGUNA,"
					+  " 	u.USER_ID"
					+  " FROM"
					+  " 	T1_USER u"
					+  " WHERE"
					+  "	u.ISDELETE = :isDelete";
		Query q = em.createNativeQuery(query);
		q.setParameter("isDelete", Constant.ZERO_VALUES);
		
		List<Object[]> object = q.getResultList();
		List<User> listUser = new ArrayList<User>();
		
		for(int i = 0; i<object.size(); i++) {
			User user = new User();
			user.setNamaPengguna(object.get(i)[0].toString());
			user.setId(Integer.parseInt(object.get(i)[1].toString()));
			listUser.add(user);
		}
		
		return listUser;
	}
}
