package id.co.promise.procurement.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;


public class SsoHelper {
	final static Logger log = Logger.getLogger(SsoHelper.class);
	private Connection dbConnection=null;
	
	private String SSO_DB_URL; /*192.168.75.11:1433*//*180.250.19.240:5001*/
	private String SSO_DB_DRIVER;
	private String SSO_DB_USER;
	private String SSO_DB_PASS;
	
	public static void setCookie(HttpServletResponse response, String strToken, final String[] appCodeArray)
			throws Exception {
		Cookie cookie = new Cookie(Constant.COOKIES_KEY, strToken);
		cookie.setPath("/");
		response.addCookie(cookie);

		String appCodeString = Arrays.toString(appCodeArray);
		appCodeString = appCodeString.replace("\"", "").replace("[", "")
				.replace("]", "").trim();
		appCodeString = KeyGenHelper.finalEncrypt(appCodeString);
		Cookie appCookie = new Cookie("JPROMISEAPPLIST", appCodeString);
		appCookie.setPath("/");
		response.addCookie(appCookie);
	}
	
	public static String getCookiest(String key, HttpServletRequest request){
		String result = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(key)){
					result = cookie.getValue();
				}
			}
		}
		return result;
	}

	public static void cleanCookies(HttpServletRequest request,
			HttpServletResponse response) {
		// ** clean cookies
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setValue(null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		;
	}
	
	public SsoHelper(){
		/*List<Parameter00> param = parameterService.getParameterList(new Parameter00());
		ParamContext.setAppParam(param);*/
		
		SSO_DB_URL = ParamContext.getParameterStringByName("SSO_DB_URL");
		SSO_DB_DRIVER = ParamContext.getParameterStringByName("SSO_DB_DRIVER");
		SSO_DB_USER = ParamContext.getParameterStringByName("SSO_DB_USER");
		SSO_DB_PASS = ParamContext.getParameterStringByName("SSO_DB_PASS");

	}
	
	/*
	 * Create dblink connection
	 */
	private void createConnection(String DB_URL, String DB_USER, String DB_PASS, String DB_DRIVER){
		try {
			if (DB_URL == null){
				log.error("Sync failed, Unknown host address");
			}else{
		        Class.forName(DB_DRIVER);
				//dbConnection = DriverManager.getConnection(DB_URL, KeyGenHelper.decrypt(Constant.ENCRYPTION_KEY_FOR_PASS,DB_USER), KeyGenHelper.decrypt(Constant.ENCRYPTION_KEY_FOR_PASS,DB_PASS));
				dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				if (dbConnection != null) {
					//System.out.println("Connected to DB");
				}
			}
		} catch (Exception e) {
			log.error("could not make a connection");
			log.error(e.getStackTrace());
			e.printStackTrace();
		}
	}
	
	private void closeConnection(){
		try {
			if (dbConnection != null && !dbConnection.isClosed()){
				//System.out.println("Closing db connection");
				dbConnection.close();
				dbConnection=null;
			}
		} catch (Exception e) {
			//System.out.println("Could not close the connection");
			log.error(e.getStackTrace());
		}
	}

	public static void main(String[] argv) throws Exception {
		//ssoDoLogin("suadmin", "817181811811");
	}
	
	public void doUpdateLoginLog(String strToken) {

		try {
			if (dbConnection == null || dbConnection.isClosed()){
				createConnection(SSO_DB_URL, SSO_DB_USER, SSO_DB_PASS, SSO_DB_DRIVER);
			}
			
			if (dbConnection!=null && !dbConnection.isClosed()) {
				try(CallableStatement proc_stmt = dbConnection.prepareCall("{ call doUpdateLoginLog(?) }")) {
					proc_stmt.setString(1, strToken); 
					proc_stmt.execute();					
					doInsertProcedureLog("doUpdateLoginLog", 1, "Success");
				}
		 		//System.out.println("SUKSES!");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("Sql Can't Processed ");
			doInsertProcedureLog("doUpdateLoginLog", 0, e.toString());
			e.printStackTrace();
		}
		closeConnection();
	}

	public boolean doInsertProcedureLog (String LPName, Integer LPStatus, String LPMessages) {

		try {
			if (dbConnection == null || dbConnection.isClosed()){
				createConnection(SSO_DB_URL, SSO_DB_USER, SSO_DB_PASS, SSO_DB_DRIVER);
			}
			
			if (dbConnection!=null && !dbConnection.isClosed()){
				try(CallableStatement proc_stmt1 = dbConnection
						.prepareCall("{ call doInsertProcedureLog (?,?,?,?) }")){
					
					proc_stmt1.setString(1, LPName);
					proc_stmt1.setInt(2, LPStatus);
					proc_stmt1.setString(3, LPMessages);
					proc_stmt1.setString(4, "CT");
					proc_stmt1.executeQuery();
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
		return true;
	}
}