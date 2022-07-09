package id.co.promise.procurement.utils.generator;
/**
 * @author : Reinhard
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.ParamContext;

@Stateless
@Path(value = "/procurement/generatorservices")
@Produces(MediaType.TEXT_PLAIN)
public class GeneratorServices {

	final static Logger logger = Logger.getLogger(GeneratorServices.class);
	
	@Path("/js/settingController/{appVersion}")
	@GET
	public String get(@Context HttpServletRequest httpServletRequest,
		      @Context HttpServletResponse httpServletResponse, @PathParam("appVersion")String appVersion) {
	
		
		String content = "";
		InputStream input = getClass().getResourceAsStream("templates/settings.controller.js.template");
	  
	    Scanner fileScanner = new Scanner(new InputStreamReader(input));

	    while (fileScanner.hasNextLine()){
	    	content +=(fileScanner.nextLine() +"\r\n ");
	    }
	    fileScanner.close();
	    
	    //############################################################################
	    // buka comment dan overide semua nilai parameter dari database yang mau dioveride di sini
	     //ParamContext.setParameterStringByName("BACKEND_ROOT_EKSTERNAL", "http://localhost:8080");
	     //ParamContext.setParameterStringByName("BACKEND_ROOT_INTERNAL", "http://localhost:8080");
	     //ParamContext.setParameterStringByName("UPLOAD_FILE_PATH", "D:\\UploadAstra\\");
	    //############################################################################
	    
	    
	    //check IPADDRESS before replace
	    
	    //Uncomment untuk Koneksi lewat local
		String backendAddressRoot = "http://localhost:8082"; 
		String cmAppLink = "http://localhost:8082/cm/dashboard.promise";
		String pmAppLink = "http://localhost:18989/kai/dashboard.promise"; //"http://localhost:18989/kai/dashboard.promise";
		String interfacing = "FALSE";
		 
	    
	    //Comment untuk koneksi lewat local
//		String backendAddressRoot =ParamContext.getParameterStringByName("BASE_IP_ADDRESS"); 
//		String cmAppLink = ParamContext.getParameterStringByName("CM_URL");
		/*perubahan KAI 24/11/2020*/
//		String pmAppLink = ParamContext.getParameterStringByName("PM_URL") + ParamContext.getParameterStringByName("REDIRECTING_PM");
//		String interfacing =ParamContext.getParameterStringByName("INTERFACING");
		 
	    
	    String fileUploadSize = ParamContext.getParameterStringByName("FILE_UPLOAD_SIZE");
	    String fileTypeImg = ParamContext.getParameterStringByName("FILE_TYPE_IMAGE");
	    String fileTypeDoc = ParamContext.getParameterStringByName("FILE_TYPE_DOC");
	    String isInsertSecurityServices = ParamContext.getParameterStringByName("IS_INSERT_SECURITY_SERVICES");
	    
	    content = content.replace(":backendAddressIP", backendAddressRoot);
	    content = content.replace(":cmAppLink", cmAppLink);
	    content = content.replace(":pmAppLink", pmAppLink);
	    content = content.replace(":fileUploadSize", fileUploadSize);
	    content = content.replace(":fileTypeImg", fileTypeImg);
	    content = content.replace(":fileTypeDoc", fileTypeDoc);
	    content = content.replace(":salt", Constant.LOGIN_SALT);
	    content = content.replace(":iv", Constant.LOGIN_IV);
	    content = content.replace(":passphrase", Constant.LOGIN_PASSPHRASE);
	    content = content.replace(":interfacing", interfacing);
	    content = content.replace(":isInsertSecurityServices", isInsertSecurityServices);
	    content = content.replace(":applicationVersion", appVersion);
	    
	    logger.info(">>> backendAddressRoot = " + backendAddressRoot);
	    logger.info(">>> cmAppLink          = " + cmAppLink);
	    logger.info(">>> pmAppLink          = " + pmAppLink);
	    
	   
	    return content;
	}

}
