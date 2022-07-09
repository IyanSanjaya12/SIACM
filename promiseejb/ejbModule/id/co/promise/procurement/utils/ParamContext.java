package id.co.promise.procurement.utils;

import id.co.promise.procurement.entity.Aksi;
import id.co.promise.procurement.entity.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.jboss.logging.Logger;

import id.co.promise.procurement.entity.Parameter;

/**
 * @author : Reinhard Since : Nov 22, 2015
 */

public class ParamContext {

	final static Logger logger = Logger.getLogger(ParamContext.class);

	private static HashMap<Integer, List<String>> roleServiceList = new HashMap<Integer, List<String>>();
	private static List<String> publicServiceList;
	private static List<Parameter> parameterList;
	private static List<Aksi> allServices;
	private static Connection connection;
	private static List<String> noMenuServiceList;

	public static List<String> getNoMenuServiceList() {
		return noMenuServiceList;
	}

	public static void setNoMenuServiceList(List<String> noMenuServiceList) {
		ParamContext.noMenuServiceList = noMenuServiceList;
	}

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		ParamContext.connection = connection;
	}

	public static HashMap<Integer, List<String>> getRoleServiceList() {
		return roleServiceList;
	}

	public static void setRoleServiceList(
			HashMap<Integer, List<String>> roleService) {
		roleServiceList = roleService;
	}

	public static List<String> getPublicServiceList() {
		return publicServiceList;
	}

	public static void setPublicServiceList(List<String> publicServices) {
		ParamContext.publicServiceList = publicServices;
	}

	public static List<Aksi> getAllServices() {
		return allServices;
	}

	public static void setAllServices(List<Aksi> allServices) {
		ParamContext.allServices = allServices;
	}

	public static void reloadParameterWithJDBC() {
		logger.info("##### reloadParameterWithJDBC #####");
		Statement stmt = null;
		try {

			stmt = connection.createStatement();
			String query = "select NAMA, TIPE, NILAI FROM PARAMETER WHERE ISDELETE = 0";
			ResultSet rs = stmt.executeQuery(query);
			parameterList = new ArrayList<Parameter>();
			while (rs.next()) {
				Parameter p = new Parameter();
				p.setNama(rs.getString("NAMA"));
				p.setTipe(rs.getString("TIPE"));
				p.setNilai(rs.getString("NILAI"));
				parameterList.add(p);
				logger.info("=> Load parameter, Nama: " + p.getNama()
						+ ", Nilai: " + p.getNilai());

			}
		} catch (SQLException e) {
			logger.info("***** ERROR on reloadParameterWithJDBC! *****");
			logger.error(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*public static void reloadSecurityServicesWithJDBC() {
		logger.info("");
		logger.info("##### reloadSecurityServicesWithJDBC #####");
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			List<String> publicServiceStrings = new ArrayList<String>();
			String query = "select AKSI_ID, PATH FROM T1_AKSI WHERE ISDELETE = 0 AND ISPUBLIC = 1";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				publicServiceStrings.add(rs.getString("PATH"));
			}

			logger.info("=> Load publicServices : "
					+ publicServiceStrings.size() + " services loaded!");

			 Load Role 
			query = "SELECT * FROM T1_ROLE AS ROLE WHERE ROLE.ISDELETE = 0";
			List<Role> roleList = new ArrayList<Role>();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("ROLE_ID"));
				role.setNama(rs.getString("NAMA"));
				roleList.add(role);
			}

			logger.info("=> Load roleServices... ");
			HashMap<Integer, List<String>> roleAksiMap = new HashMap<Integer, List<String>>();
			query = "SELECT DISTINCT AKSI.* FROM T1_AKSI AS AKSI INNER JOIN T2_MENU_AKSI AS MENU_AKSI ON AKSI.AKSI_ID = MENU_AKSI.AKSI_ID INNER JOIN T2_ROLE_MENU AS ROLE_MENU ON ROLE_MENU.MENU_ID = MENU_AKSI.MENU_ID WHERE AKSI.ISDELETE =0 AND MENU_AKSI.ISDELETE =0 AND ROLE_MENU.ISDELETE =0 AND ROLE_MENU.ROLE_ID = :role_id";

			for (Role role : roleList) {
				List<String> pathList = new ArrayList<String>();

				rs = stmt.executeQuery(query.replace(":role_id", role.getId()
						.toString()));

				while (rs.next()) {
					pathList.add(rs.getString("PATH"));
				}
				roleAksiMap.put(role.getId(), pathList);

				logger.info("===> ROLE :  " + role.getNama() + " : "
						+ pathList.size() + " services loaded!");
			}

			 Set ParamContext Value 
			ParamContext.setPublicServiceList(publicServiceStrings);
			ParamContext.setRoleServiceList(roleAksiMap);

		} catch (SQLException e) {
			logger.info("***** ERROR on reloadSecurityServicesWithJDBC! *****");
			System.out.println(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}*/
	
	public static void reloadSecurityServicesWithJDBC() {
		logger.info("");
		logger.info("##### reloadSecurityServicesWithJDBC #####");
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			logger.info("");
			
			/* Load All Services */
			allServices = new ArrayList<Aksi>();
			String query = "select AKSI_ID, PATH, ISPUBLIC FROM T1_AKSI WHERE ISDELETE = 0 AND (ISPUBLIC IS NULL OR ISPUBLIC = 0 )";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Aksi aksi = new Aksi();
				aksi.setId(rs.getInt("AKSI_ID"));
				aksi.setPath(rs.getString("PATH"));
				allServices.add(aksi);
			}
			
			//sorting
			Collections.sort(allServices, ParamContext.getAksiCompByPath());
			
			for (Aksi aksi : allServices) {
				logger.info("aksi = " + aksi.getPath());
			}
			logger.info("=> Load allServices : " + allServices.size() + " services loaded!");

			
			/* Load Public Services */
			List<String> publicServiceStrings = new ArrayList<String>();
			query = "select AKSI_ID, PATH FROM T1_AKSI WHERE ISDELETE = 0 AND ISPUBLIC = 1";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				publicServiceStrings.add(rs.getString("PATH")+"/");
			}
			logger.info("=> Load publicServices : "
					+ publicServiceStrings.size() + " services loaded!");
			logger.info("");
			
			/* Load NoMenu Services */
			List<String> noMenuServiceStrings = new ArrayList<String>();
			query = "select AKSI_ID, PATH FROM T1_AKSI WHERE ISDELETE = 0 AND ISPUBLIC = 0";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				noMenuServiceStrings.add(rs.getString("PATH")+"/");
			}


			
			/* Load Services by role */
			query = "SELECT ROLE.*  FROM T1_ROLE ROLE WHERE ROLE.APP_CODE ='PM' AND  ROLE.ISDELETE = 0";
			List<Role> roleList = new ArrayList<Role>();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("ROLE_ID"));
				role.setNama(rs.getString("NAMA"));
				roleList.add(role);
			}

			logger.info("=> Load roleServices... ");
			HashMap<Integer, List<String>> roleAksiMap = new HashMap<Integer, List<String>>();
			query = "SELECT DISTINCT AKSI.* FROM T1_AKSI AKSI INNER JOIN T2_MENU_AKSI MENU_AKSI ON AKSI.AKSI_ID = MENU_AKSI.AKSI_ID INNER JOIN T2_ROLE_MENU ROLE_MENU ON ROLE_MENU.MENU_ID = MENU_AKSI.MENU_ID WHERE AKSI.ISDELETE =0 AND MENU_AKSI.ISDELETE =0 AND ROLE_MENU.ISDELETE =0 AND ROLE_MENU.ROLE_ID = :role_id";

			for (Role role : roleList) {
				List<String> pathList = new ArrayList<String>();

				rs = stmt.executeQuery(query.replace(":role_id", role.getId()
						.toString()));

				while (rs.next()) {
					pathList.add(rs.getString("PATH")+"/");
				}
				roleAksiMap.put(role.getId(), pathList);

				logger.info("===> ROLE :  " + role.getNama() + " : "
						+ pathList.size() + " services loaded!");
			}
			logger.info("");
			/* Set ParamContext Value */
			ParamContext.setPublicServiceList(publicServiceStrings);
			ParamContext.setRoleServiceList(roleAksiMap);
			ParamContext.setNoMenuServiceList(noMenuServiceStrings);

		} catch (SQLException e) {
			logger.info("***** ERROR on reloadSecurityServicesWithJDBC! *****");
			logger.error(e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	//compare desc
	public static Comparator<Aksi> getAksiCompByPath() {
		Comparator<Aksi> comp = new Comparator<Aksi>() {
			@Override
			public int compare(Aksi s1, Aksi s2) {
				 if (s2.getPath().length() > s1.getPath().length()) {
			         return 1;
			      } else if (s2.getPath().length() < s1.getPath().length()) {
			         return -1;
			      }
				return s2.getPath().compareTo(s1.getPath());
			}
		
		};
		return comp;
	}

	public static String getParameterStringByName(String paramName) {
		if (parameterList.size() == 0) {
			logger.warn("Parameter has not been loaded !");
			return null;
		} else {
			for (Parameter param : parameterList) {
				if (param.getNama().equalsIgnoreCase(paramName)) {
					return param.getNilai();
				}
			}
			logger.warn("No parameter has been found by name " + paramName);
			return null;
		}
	}

	/* Return Boolean parameter value by name */
	public static Boolean getParameterBooleanByName(String paramName) {
		String val = getParameterStringByName(paramName);
		return val == null ? false : (val.equalsIgnoreCase("TRUE") ? true
				: false);
	}

	/* Return Integer parameter value by name */
	public static Integer getParameterIntegerByName(String paramName) {
		String val = getParameterStringByName(paramName);
		return val == null ? null : Integer.parseInt(val);
	}
}
