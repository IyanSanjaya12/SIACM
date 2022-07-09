/**
 * fdf
 */
package id.co.promise.procurement.master;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.security.TokenSession;

/**
 * @author User
 *
 */
@Stateless
@Path(value = "/procurement/master/gl-account")
@Produces(MediaType.APPLICATION_JSON)
public class GLAccountSapServices {
	final static Logger log = Logger.getLogger(GLAccountSapServices.class);

	@EJB private GLAccountSapSession gLAccountSapSession;
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();

	@EJB TokenSession tokenSession;


	@Path("/get-list")
	@GET
	public Response getList(
			@HeaderParam("Authorization") String token) {
		try {
			return Response.status(201).entity(gLAccountSapSession.getList()).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	} 
	
	@Path("/get-list-by-pagination")
	@POST 
	public Response getListByPagination(
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder
			){
		String tempKeyword = "%" + keyword + "%";
		long countData = gLAccountSapSession.getListCount(tempKeyword);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);		
		result.put("data", gLAccountSapSession.getListWithPagination(start, length, tempKeyword, columnOrder, tipeOrder));
		
		return Response.ok(result).build();
	}
	
	
}
