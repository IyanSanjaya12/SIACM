package id.co.promise.procurement.utils;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.jboss.logging.Logger;

import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;

/**
 * 
 * Project Name : promiseews
 * Package Name : id.co.promise.procurement.utils
 * File Name    : SecurityHelper.java
 * Description  : 
 * @author      : Reinhard 
 * Since        : Nov 22, 2015
 *
 */

public class SecurityHelper {

	final static Logger logger = Logger.getLogger(SecurityHelper.class);
	
	final static String ERROR_MSG_LOGIN_REQUIRED = "PRMS-ERR-001: Login required!";
	final static String ERROR_MSG_INSUFFICIENT_PRIVILEGE = "PRMS-ERR-002: insufficient privilege!";
	final static String ERROR_MSG_SESSION_TIMEOUT = "PRMS-ERR-004: Session Timeout !";
	@EJB
	static ProcedureSession procedureSession;
	
	public  static String checkAccess(String paramPath, String strToken,
			RoleUserSession roleUserSession, TokenSession tokenSession) {
		
		String path = paramPath + "/";
		/*logger.info("path = " + path);*/
		
		Boolean isPublicService = false;
		for (String serviceStr : ParamContext.getPublicServiceList()) {
			if (path.indexOf(serviceStr) == 0) {
				isPublicService = true;
				break;
			}
		}

		String rejectMsg = null;

		RoleUser roleuser = null;
		Role role  = null;
		
		/*Security Check*/
		if (!ParamContext.getParameterBooleanByName("IS_SECURITY_DISABLED") && !isPublicService ) {
			
			/*=== CHECK PRIVATE SERVICE ACCESSED BY GUEST == */
			if (strToken == null) {
				if (!ParamContext.getParameterBooleanByName("IS_SECURITY_DISABLED")) {
					rejectMsg = ERROR_MSG_LOGIN_REQUIRED;
					return rejectMsg;
				}
			}
			
			roleuser = roleUserSession.getRoleUserListByTokenAnAppCode(strToken,"PM").get(0);
			role = roleuser.getRole();
			
			/*=== CHECK PREVILLAGE == */
			/*logger.info("==> Checking privilege for role: " + role.getNama());*/
			
			List<String> roleServicesList = ParamContext.getRoleServiceList().get(role.getId());
			List<String> noMenuServiceList = ParamContext.getNoMenuServiceList();
			
			for (String service : noMenuServiceList) {
				roleServicesList.add(service);
			}
		
			
			Boolean isUserAllowed = false;
			for (String userServiceStr : roleServicesList)
			{
				/*logger.info(("compare " + userServiceStr + " ---- " + path ));*/
				if (path.indexOf(userServiceStr) == 0) {
					isUserAllowed = true;
					break;
				}
			}
			
			if (!isUserAllowed) {
				rejectMsg = ERROR_MSG_INSUFFICIENT_PRIVILEGE;
				return rejectMsg;
			}
			/*=== END CHECK PREVILLAGE == */
			
		}
		
		/* CHECK SESSION TIMEOUT */
		Date now = new Date();
	    if (strToken != null) {
	      Token token = tokenSession.findByToken(strToken);
	      if (token != null) {
	        if (token.getLastActive() != null) {
	          long minutesFromLastActivity = getMinutesIdle(now.getTime(), token.getLastActive().getTime());
	          if (minutesFromLastActivity >= ParamContext.getParameterIntegerByName(Constant.PARAMETER_SESSION_TIMEOUT).longValue()) {
	            logger.info("{isSessionTimeout : " + (minutesFromLastActivity >= ParamContext.getParameterIntegerByName(Constant.PARAMETER_SESSION_TIMEOUT).longValue()) + "}");
	            return ERROR_MSG_SESSION_TIMEOUT;
	          }
	        }
	      }
	    }

		/* IF EVERYTHING OK, UPDATE LAST ACTIVITY */
		if (strToken != null) {
			Token token = tokenSession.findByToken(strToken);
			token.setLastActive(now);
			tokenSession.edit(token);
			SsoHelper sso = new SsoHelper();
			/* sso.doUpdateLoginLog(strToken); */
		}
		return rejectMsg;

	}
	
	
	private static String cleanPathString (String path)
	/**
	 * Membersihkan String dari parameter dan '/' di sebelah kanan
	 * @sample 
	 * INPUT  : /procurement/master/organisasi/get-list-by-parent-id/0/2/
	 * 			/procurement/master/organisasi/get-list-by-parent-id/0/2
	 * 			/procurement/master/organisasi/get-list-by-parent-id/0
	 *          /procurement/master/organisasi/get-list-by-parent-id/
	 *          /procurement/master/organisasi/get-list-by-parent-id
	 *          
	 * OUTPUT : /procurement/master/organisasi/get-list-by-parent-id
	 */          
	
	{
		
	   String str = "";
	   String[] strs = path.split("/");
	   int ctr = 0;
	   for (String s : strs)
	   {
		   if ( isNumeric(s))
		   {
			   break;
		   }
		   else
		   {
				if (ctr!=0)	{ str += ("/" + s); };
				ctr ++;
		   }
	   }
		   
	   return str;
	}
	
	public static long getMinutesIdle(long currentTime, long lastActiveTime) {
		return (currentTime - lastActiveTime) / (1000 * 60);
	}
	
	public static Boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
}

