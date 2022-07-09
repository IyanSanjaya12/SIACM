package id.co.promise.procurement.report;

import id.co.promise.procurement.utils.ParamContext;

import java.sql.*;

import org.jboss.logging.Logger;

public class DBUtilities {
	final static Logger log = Logger.getLogger(DBUtilities.class);
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public DBUtilities() throws SQLException, ClassNotFoundException {
        try {
        	log.info("-> create new connection!");
        	
        	String dbURL = ParamContext.getParameterStringByName("JASPER_DB_URL");
        	String dbUSERNAME = ParamContext.getParameterStringByName("JASPER_DB_USERNAME");
        	String dbPASSWORD = ParamContext.getParameterStringByName("JASPER_DB_PASSWORD");
            //connection = DriverManager.getConnection("jdbc:sqlserver://<URL>;database=<DBNAME>", "<DBUSERNAME>", "<DBPASSWORD>");
        	Class.forName("oracle.jdbc.OracleDriver");
        	connection = DriverManager.getConnection(dbURL, dbUSERNAME, dbPASSWORD);

        } catch (SQLException ex) {
            log.error("The following error has occured: " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void ExecuteSQLStatement(String sql_stmt) {
        try {
            statement = connection.createStatement();

            statement.executeUpdate(sql_stmt);
        } catch (SQLException ex) {
            log.error("The following error has occured: " + ex.getMessage());
        }
    }
}