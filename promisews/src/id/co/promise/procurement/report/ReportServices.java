package id.co.promise.procurement.report;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@SuppressWarnings("deprecation")
@Stateless
@Path(value = "/procurement/report/")
public class ReportServices {
	

	final static Logger logger = Logger.getLogger(ReportServices.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Path("printReport")
	@POST
	public Response printReport(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse)
			throws SQLException, ClassNotFoundException {

		String reportFileName = "";

		final String reportRootPath = httpServletRequest.getSession()
				.getServletContext().getRealPath("/")
				+ new File("/").toString()
				+ new File("WEB-INF", "jasper").toString()
				+ new File("/").toString();
		String reportImagePath = httpServletRequest.getSession()
				.getServletContext().getRealPath("/")
				+ new File("/").toString()
				+ new File("WEB-INF", "jasper").toString()
				+ new File("/").toString();
		String SUBREPORT_DIR = httpServletRequest.getSession()
				.getServletContext().getRealPath("/")
				+ new File("/").toString()
				+ new File("WEB-INF", "jasper").toString()
				+ new File("/").toString();
		Map parameterReq = httpServletRequest.getParameterMap();
		final Map parameter = new HashMap();

		parameter.put("reportImagePath", reportImagePath);
		parameter.put("SUBREPORT_DIR", SUBREPORT_DIR);

		Set keySet = parameterReq.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = httpServletRequest.getParameter(key);

			if (key.equals("reportFileName")) {
				reportFileName = value;
			} else {
				parameter.put(key, value);

				logger.info("Parameter report ( " + key + " = " + value + " )");
			}
		}

		try {

			byte[] bytes = null;
			File reportFile = new File(reportRootPath,
					new File(reportFileName).getName() + ".jasper");
			FileInputStream stream = new FileInputStream(reportFile);
			DBUtilities dbUtilities = new DBUtilities();
			logger.info("Report, connection.isClosed()? = "+ dbUtilities.getConnection());
			

			bytes = JasperRunManager.runReportToPdf(stream, parameter,
					dbUtilities.getConnection());

			OutputStream ouputStream = httpServletResponse.getOutputStream();
			ouputStream.write(bytes, 0, bytes.length);

			ResponseBuilder response = Response.ok((Object) stream);
			response.header("Content-Disposition", "filename=" + reportFileName
					+ ".pdf");

			ouputStream.flush();
			ouputStream.close();
			return response.build();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Path("printReportGet")
	@GET
	public Response printReportGet(
			@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse)
			throws SQLException, ClassNotFoundException {

		String reportFileName = "";

		final String reportRootPath = httpServletRequest.getSession()
				.getServletContext().getRealPath("/")
				+ new File("/").toString()
				+ new File("WEB-INF", "jasper").toString()
				+ new File("/").toString();
		String reportImagePath = httpServletRequest.getSession()
				.getServletContext().getRealPath("/")
				+ new File("/").toString()
				+ new File("WEB-INF", "jasper").toString()
				+ new File("/").toString();
		String SUBREPORT_DIR = httpServletRequest.getSession()
				.getServletContext().getRealPath("/")
				+ new File("/").toString()
				+ new File("WEB-INF", "jasper").toString()
				+ new File("/").toString();
		Map parameterReq = httpServletRequest.getParameterMap();
		final Map parameter = new HashMap();

		parameter.put("reportImagePath", reportImagePath);
		parameter.put("SUBREPORT_DIR", SUBREPORT_DIR);

		Set keySet = parameterReq.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = httpServletRequest.getParameter(key);

			if (key.equals("reportFileName")) {
				reportFileName = value;
			} else {
				parameter.put(key, value);

				logger.info("Parameter report ( " + key + " = " + value + " )");
			}
		}

		try {

			byte[] bytes = null;
			File reportFile = new File(reportRootPath,
					new File(reportFileName).getName() + ".jasper");
			FileInputStream stream = new FileInputStream(reportFile);
			
			DBUtilities dbUtilities = new DBUtilities();
			logger.info("Report, connection.isClosed()? = "+ dbUtilities.getConnection());

			bytes = JasperRunManager.runReportToPdf(stream, parameter,
					dbUtilities.getConnection());

			OutputStream ouputStream = httpServletResponse.getOutputStream();
			ouputStream.write(bytes, 0, bytes.length);

			ResponseBuilder response = Response.ok((Object) stream);
			response.header("Content-Disposition", "filename=" + reportFileName
					+ ".pdf");

			ouputStream.flush();
			ouputStream.close();
			// return response.build();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String formatMoney(double dVal) {
	    DecimalFormat df = new DecimalFormat("#,##0.00;-#,##0.00");
	    return df.format(dVal);
	  }
}
