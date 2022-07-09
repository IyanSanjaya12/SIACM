package id.co.promise.procurement.security;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import erp.service.UserInterfacingService;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.LoginAttempt;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.TokenRemember;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.UserAdditional;
import id.co.promise.procurement.entity.UserToken;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ParameterSession;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.master.RoleJabatanSession;
import id.co.promise.procurement.master.RoleSession;
import id.co.promise.procurement.master.UserAdditionalSession;
import id.co.promise.procurement.utils.AesUtil;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.KeyGenHelper;
import id.co.promise.procurement.utils.ParamContext;
import id.co.promise.procurement.vendor.VendorSession;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/procurement/loginservices")
@Produces(MediaType.APPLICATION_JSON)
public class LoginServices {
	
	final static Logger logger = Logger.getLogger(LoginServices.class);
	
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	@EJB
	UserSession userSession;
	
	@EJB
	JabatanSession jabatanSession;
	
	@EJB
	RoleJabatanSession roleJabatanSession;
	
	@EJB
	OrganisasiSession organisasiSession;
	
	@EJB
	VendorSession vendorSession;

	@EJB
	RoleSession roleSession;
	@EJB
	RoleUserSession roleUserSession;

	@EJB
	TokenSession tokenSession;

	@EJB
	TokenRememberSession tokenRememberSession;
	
	@EJB
	ParameterSession parameterSession;
	
	@EJB
	LoginAttemptSession loginAttemptSession;
	
	@EJB
	SyncSession syncSession;
	
	@EJB
	UserInterfacingService userInterfacingService;
	
	@EJB
	UserAdditionalSession userAdditionalSession;
	
	@EJB ProcedureSession procedureSession;
	
	@Resource private UserTransaction userTransaction;

	@SuppressWarnings("rawtypes")
	@Path(value = "/reAuthentificationByToken")
	@POST
	public Map reAuthentificationByToken(@FormParam("strToken") String strToken,@FormParam("roleCode") String roleCode) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		UserToken userToken = new UserToken();
		User user = userSession.getActiveAndValidUserByToken(strToken);
		if (user != null) {

			userToken.setToken(strToken);
			userToken.setUser(user);

			List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(user.getId());
			map.put("roleUserList", roleUserList);
		
		}
		if(roleCode!= null){
			String[] appCodeList = KeyGenHelper.decrypt(Constant.ENCRYPTION_KEY_FOR_PASS, roleCode).split(",");
			map.put("appCodeList", appCodeList);
		}
		map.put("userToken", userToken);
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Path(value = "/getAuthentification")
	@POST
	public Map getAuthentification(
			@FormParam("auth") String auth,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws Exception {

		UserToken userToken = new UserToken();
		Map<Object, Object> map = new HashMap<Object, Object>();
		String status = "";
		
		/* Login Security Encryption - Start */
		String captcha = null;
		String username = null;
		String password = null;
		Boolean rememberMe = null;
		Boolean firstLogin = null;
		
		Integer iterationCount = Constant.LOGIN_ITERATION_COUNT;
		Integer keySize = Constant.LOGIN_KEY_SIZE;
		String salt = Constant.LOGIN_SALT;
		String iv = Constant.LOGIN_IV;
		String passphrase = Constant.LOGIN_PASSPHRASE;
		
		String generateKey = KeyGenHelper.finalEncrypt(passphrase);
		
		if (generateKey != null) {
			try {
				AesUtil aesUtil = new AesUtil(keySize, iterationCount);
				
				String string = aesUtil.decrypt(salt, iv, passphrase, auth);
				
				String [] arrString = string.split("#PRM-MMI#");
				
				username 	= arrString[1];
				password 	= arrString[3];
				captcha  	= arrString[5];
				rememberMe 	= Boolean.parseBoolean(arrString[7]);
				firstLogin 	= Boolean.parseBoolean(arrString[9]);
				
			} catch (Exception e) {
				map.put("status_login", "Failed");
				status = "Username atau password salah";
				logger.error("{status_login : Failed, username : " + username + ", url : /user/getAuthentification}");
			}

		} else {
			map.put("status_login", "Failed");
			status = "Login token salah";
			logger.error("{status_login : Failed, username : " + username + ", url : /user/getAuthentification}");
		}
		/* Login Security Encryption - End */

		/* Astra login by email */
		
		User users = userSession.getUserByEmail(username);
		if ( users == null ) {
			users = userSession.getUserByUsername(username);
		}
		
		Boolean isPass = true;
		Integer diffMinute = 0;
		
//		Integer parameterLoginTimeout = ParamContext.getParameterIntegerByName("LOGIN_ATTEMPT_TIMEOUT");
		Integer parameterLoginAttempt = ParamContext.getParameterIntegerByName("LOGIN_ATTEMPT");
		Integer parameterLogin = parameterLoginAttempt;
		
		String attemptMessage = "";
		Integer attempts = 0;
		
		Boolean checkCaptcha = parameterSession.getParameterBooleanByName("IS_CAPTCHA_VALIDATOR");
		
		if(users != null) {
			diffMinute = loginAttemptSession.getMinuteBetween(users);
			loginAttemptSession.deleteLoginAttemptUser(users, diffMinute);
		}
		
		String validateCaptcha = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String requestCaptcha = captcha;
		
		// prevent null when session timeout
		if (validateCaptcha == null) {
			validateCaptcha = randomAlphaNumeric(5);
		}

		/** bypass captcha **/
		if (!checkCaptcha) {
			requestCaptcha = validateCaptcha;
		}
		
		if (requestCaptcha.equals(validateCaptcha)) {
			
			if (users == null) {
				map.put("status_login", "Failed");
				status = "username atau password salah, silahkan coba lagi";
				isPass = false;
				logger.error("{status_login : Failed, username : " + username + ", url : /user/getAuthentification}");
			} else {
				
					Boolean checkLoginInterfacing = ParamContext.getParameterBooleanByName("IS_INTERFACING_LOGIN_ENABLE");
					
					Boolean next = true;
					
					List <RoleUser> checkRoleUserList = roleUserSession.getRoleUserByUserId(users.getId());
					for (RoleUser checkRoleUser : checkRoleUserList ) {
						if ( checkRoleUser.getRole().getIsSso() == 0 ) { //Admin Support
							checkLoginInterfacing = false;
							if (users.getEmail() != null) {
								if (users.getEmail().equalsIgnoreCase(username)) {
									next = false;
									map.put("status_login", "Failed");
									status = "username atau password salah, silahkan coba lagi";
									isPass = false;
									userSession.updateUser(users, null);
									logger.error(status);
								}
							}
						}
					}
					
					if ( !checkLoginInterfacing ) {
							if (users.getPassword() == null  || !users.getPassword().equals(KeyGenHelper.finalEncrypt(password))  ) {
								next = false;
								map.put("status_login", "Failed");
								status = "username atau password salah, silahkan coba lagi";
								isPass = false;
								userSession.updateUser(users, null);
								logger.error(status);

							}	
					}

					String url;
					Boolean isValidInterface=true;
					
					if(users.getIsPlh() != null && users.getIsPlh() == 1 ) {
						UserAdditional userAdditional = userAdditionalSession.getUserAdditionalByNippPlh(username);

						Date today = new Date();
						Calendar date = Calendar.getInstance();
						today = date.getTime();
						long tanggalMulai=0;
						long tanggalAkhir = 0;
						if(userAdditional.getStartDate() !=null) {
							tanggalMulai = userAdditional.getStartDate().getTime();
						}
						if(userAdditional.getEndDate()!=null) {
							tanggalAkhir = userAdditional.getEndDate().getTime();
						}
						long nowEnd		= today.getTime() - tanggalAkhir;
						long nowStart 	= today.getTime() - tanggalMulai;
						long endNow 	= tanggalAkhir - today.getTime();
						long startNow	= tanggalMulai - today.getTime();
						
						long diffNowEnd 	= nowEnd / (24 * 60 * 60 * 1000);
						long diffNowStart 	= nowStart / (24 * 60 * 60 * 1000);
						long diffEndNow 	= endNow / (24 * 60 * 60 * 1000);
						long diffStartNow 	= startNow / (24 * 60 * 60 * 1000);
						
						if(userAdditional.getIsActive() != 1) {
							status = "User PLH sudah tidak aktif";
							isPass = false;
							logger.error(status);
							next = false;
						}
//						else if((diffNowEnd > 0 && diffNowStart > 0) || (diffEndNow > 0 && diffStartNow > 0)) {
//							status = "User PLH sudah tidak aktif";
//							isPass = false;
//							logger.error(status);
//							next = false;
//						}
						else {
							userTransaction.begin();
							procedureSession.execute(Constant.SYNC_IN_UP_ADD_PLH_TO_CM, userAdditional.getUser().getUserId());
							userTransaction.commit();
						}
					}else {
							if ( checkLoginInterfacing ) {
								
								if (username.substring(0, 3).equalsIgnoreCase("V00")) {
									
									//url = Constant.EPROC_BACKEND_ADDRESS + "/ChkLogin";
									url = Constant.INTERFACING_BACKEND_ADDRESS_EPROC + "/postLoginVendor";
									
									isValidInterface = userInterfacingService.getAutentificationInterface(url,username, password, Constant.URL_TYPE_VENDOR);
									
									if (!isValidInterface ) {
										
										map.put("status_login", "Failed");
										status = "Silahkan hubungi Admin, Vendor tidak terdaftar di E-Proc ";
		
										userSession.updateUser(users, null);
										logger.error(status);
										
										next = false; 
										
									}
									
								} else {
									
									//url = Constant.EOFFICE_BACKEND_ADDRESS + "/ChkLoginPgn";
									url = Constant.INTERFACING_BACKEND_ADDRESS_EOFFICE + "/postLoginUser";
									isValidInterface = userInterfacingService.getAutentificationInterface(url, username, password, Constant.URL_TYPE_INTERNAL_USER);
									if (!isValidInterface ) {
										map.put("status_login", "Failed");
										status = "Silahkan hubungi Admin, User tidak terdaftar di E-Office";
		
										userSession.updateUser(users, null);
										logger.error(status);
										next = false; 
									}
								}
							}
							Boolean isDVP = false;
							if (isValidInterface) {
								List <RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(users.getId());
								for (RoleUser roleUser : roleUserList ) {
									if ( roleUser.getRole().getCode().equalsIgnoreCase(Constant.ROLE_CODE_PENGGUNA_DVP) ) {
										isDVP = true;
									}
								}
								
								if(roleUserList.size()==0) {
									map.put("status_login", "Failed");
									status = "Silahkan hubungi Admin, User tidak memiliki hak akses";
		
									userSession.updateUser(users, null);
									logger.error(status);
		
									next = false;
								} 
							}
							
							 if (isDVP) {
									if ( users.getFlagLoginEbs() == null || users.getFlagLoginEbs() == 0) {
										map.put("status_login", "Failed");
										status = "NIP anda belum terdaftar pada EBS, silahkan hubungi admin untuk melakukan pendaftaran pengguna di EBS";
		
										userSession.updateUser(users, null);
										logger.error(status);
		
										next = false;
									}
									
								}
					}

					if (users.getBlacklist() != null) {
						if (users.getBlacklist().equals(2)) {
							map.put("status_login", "Failed");
							status = "Account terblacklist";

							userSession.updateUser(users, null);
							logger.error(status);

							next = false;
						}
					}
					
					if(users.getReject() != null) {
						if (users.getReject().equals(1)) {
							map.put("status_login", "Failed");
							status = "Account direject";

							userSession.updateUser(users, null);
							logger.error(status);

							next = false;
						}					
					}
					
					LoginAttempt loginAttempt = loginAttemptSession.getLoginAttemptByUser(users);
					if(loginAttempt != null) {
						if(loginAttempt.getSequence() == parameterLogin) {
							map.put("status_login", "Failed");
							status = "Kesempatan anda untuk login habis, silahkan coba dalam " + diffMinute + " menit lagi";
							attempts = loginAttempt.getSequence();
							
							next = false;
						}
					}

					if (next == true) {

						//Vendor vendor = vendorSession.getVendorByUserId(users.getId());
						
						if(loginAttempt != null) {
							loginAttemptSession.deleteRowLoginAttempt(loginAttempt.getId());
						}
						
						/* LOGIN SUKSES VENDOR */
						String ipAddress = request.getRemoteAddr();
						Date loginDate = new Date();
						userTransaction.begin();
						if ( checkLoginInterfacing ) {
								userToken = userSession.getAuthentification(username, rememberMe, firstLogin, loginDate, ipAddress);
						} else {
								userToken = userSession.getAuthentification(username, password, rememberMe, firstLogin, loginDate, ipAddress);
						}
						
						/*
						 * UPDATE LOGIN TIME AT USER.. TOKEN.LOGIN_TIME &
						 * USER.LOGIN_TIME MUST EQUALS!
						 */

						users.setLoginTime(loginDate);

						userSession.updateUser(users, null);
						map.put("status_login", "Success");
						map.put("token", userToken.getToken());
						
						List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(users.getId());
						userTransaction.commit();
						map.put("roleUserList", roleUserList);

						/* SINGLE_SIGN_ON */
						String appCodeArray[] = new String[roleUserList.size()];
//						for (int i = 0; i < roleUserList.size(); i++) {
//							appCodeArray[i] = roleUserList.get(i).getRole().getAppCode();
//							if(roleUserList.get(i).getRole().getAppCode().equalsIgnoreCase("CM")) {
//								
//								procedureSession.execute(Constant.SYNC_SSO_CATALOG_CM, userToken.getUser().getUsername(), userToken.getToken());	
//							}
//						}
						Cookie cookie = new Cookie(Constant.COOKIES_KEY, userToken.getToken());
						cookie.setPath("/");
						response.addCookie(cookie);

						String appCodeString = Arrays.toString(appCodeArray);
						appCodeString = appCodeString.replace("\"", "").replace("[", "").replace("]", "").trim();
						appCodeString = KeyGenHelper.finalEncrypt(appCodeString);
						Cookie appCookie = new Cookie("JPROMISEAPPLIST", appCodeString);
						appCookie.setPath("/");
						response.addCookie(appCookie);
						
						
						//clear session
						try {
							Cookie forumCookie = new Cookie("JSESSIONID", "");
							forumCookie.setValue(null);
							forumCookie.setMaxAge(0);
							forumCookie.setPath("/forum");
							response.addCookie(forumCookie);
						} catch (Exception e) {
							e.printStackTrace();
						}
						/* END SINGLE SIGN-ON */

						status = "User :" + userToken.getUser().getUsername();

						logger.info(status);
						
					/* if ( !checkLoginInterfacing ) {
						}
					} */

				} 
					
			}
			
			
		} else {
			map.put("status_login", "Failed");
			status = "Kaptcha salah. Masukkan kaptcha dengan benar";
		}
		
		

		if(!isPass) {
			if(users != null) {
				LoginAttempt loginAttempt = loginAttemptSession.getLoginAttemptByUser(users);
				if(loginAttempt != null) {
					
					if(loginAttempt.getSequence() == parameterLogin) {
						status = "Kesempatan anda untuk login habis, silahkan coba dalam " + diffMinute + " menit lagi";
					}
					
					if(loginAttempt.getSequence() < parameterLogin) {
						Integer sequence = loginAttempt.getSequence()+1;
						loginAttempt.setSequence(sequence);
						loginAttemptSession.updateLoginAttempt(loginAttempt);
					}
					
					attempts = loginAttempt.getSequence();
					
				} else {
					LoginAttempt loginAttempts = new LoginAttempt();
					loginAttempts.setUser(users);
					loginAttempts.setSequence(1);
					loginAttemptSession.insertLoginAttempt(loginAttempts);
					
					attempts = loginAttempts.getSequence();
				}
				
			}
		}
		
		map.put("attempts", attempts);
		map.put("message", status);
		map.put("userToken", userToken);
		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@Path(value = "/getAuthentificationByToken")
	@GET
	public Map getAuthentificationByToken(@Context HttpServletRequest request, 
			@Context HttpServletResponse response) throws Exception {

		Map<Object, Object> map = new HashMap<Object, Object>();
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				//Integer userId = Integer.parseInt(cookie.getValue());
				TokenRemember tokenRemember = tokenRememberSession.findByToken(cookie.getValue());
				
				if(tokenRemember != null) {
					Date today = new Date();
					long diff = today.getTime() - tokenRemember.getLoginTime().getTime();
					long diffMinutes = diff / (60 * 1000) % 60;
					
					if (diffMinutes <= 10) {
						map.put("username", tokenRemember.getUser().getEmail());
						map.put("password", KeyGenHelper.finalDecrypt(tokenRemember.getUser().getPassword()));
					} else {
						map.put("username", null);
						map.put("password", null);
					}
					
				}

			}
		} else {
			map.put("username", null);
			map.put("password", null);
		}
		
		
		return map;
	}

	@Path("/dologout/{token}")
	@GET
	public User getLogOut(@PathParam("token") String token, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		// ** clean cookies
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				
				Token tokenize = tokenSession.findByToken(cookie.getValue());
				if (tokenize != null) {
					TokenRemember tokenRemember = tokenRememberSession.findByToken(cookie.getValue());
					if (tokenRemember != null) {
						tokenRememberSession.deleteTokenRemember(tokenRemember.getId(), tokenize);
					}
					
				}
				
				cookie.setValue(null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		try {
			Cookie forumCookie = new Cookie("JSESSIONID", "");
			forumCookie.setValue(null);
			forumCookie.setMaxAge(0);
			forumCookie.setPath("/forum");
			response.addCookie(forumCookie);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userSession.getLogOut(token);
	}
	
	@Path("/cleanAllCookies")
	@GET
	public User clean(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		// ** clean cookies **//*
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setValue(null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}

		return new User();
	}
	
	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING
					.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

}
