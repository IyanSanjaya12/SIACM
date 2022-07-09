package id.co.promise.procurement.utils;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;

import org.jboss.logging.Logger;

/**
 * 
 * Project Name : promiseews Package Name : id.co.promise.procurement.utils File
 * Name : StartupBean.java Description :
 * 
 * @author : Reinhard Since : Nov 22, 2015
 *
 */
@Startup
@Singleton
public class StartupBean extends HttpServlet {

	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(StartupBean.class);

	@PostConstruct
	public void initialize() {
		logger.info("");
		logger.info("################## StartupBean #################");
		javax.naming.Context ic;
		try {
			ic = new javax.naming.InitialContext();
			javax.naming.Context ctx = (javax.naming.Context) ic
					.lookup("java:");
			javax.sql.DataSource ds = (javax.sql.DataSource) ctx
					.lookup("jboss/promiseAllDS");
			try {
				java.sql.Connection con = ds.getConnection();

				logger.info("############## CONNECTION SUCCESS ##############");
				logger.info("############## JBOSS STARTED ##############");
				logger.info("");
				ParamContext.setConnection(con);
				ParamContext.reloadParameterWithJDBC();
				ParamContext.reloadSecurityServicesWithJDBC();
				
			/*Sample untuk getparameter*/
			/*Boolean isSecurityDisabled = ParamContext.getParameterBooleanByName("SECURITY_DISABLED");
			String SMTPServer = ParamContext.getParameterStringByName("SMTP_ADDRESS");
			Integer SMTPPort = ParamContext.getParameterIntegerByName("SMTP_PORT");
			
			System.out.println(">>>>>> isSecurityDisabled " + isSecurityDisabled );
			System.out.println(">>>>>> SMTPServer         " + SMTPServer );
			System.out.println(">>>>>> SMTPPort           " + SMTPPort );*/
					
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.info("************ CONNECTION ERROR ************");
				e.printStackTrace();
			}
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
