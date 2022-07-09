/**
 * fdf
 */
package id.co.promise.procurement.master;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.security.TokenSession;

/**
 * @author User
 *
 */
@Stateless
@Path(value = "/procurement/master/store-loc")
@Produces(MediaType.APPLICATION_JSON)
public class StoreLocSapServices {
	final static Logger log = Logger.getLogger(StoreLocSapServices.class);

	@EJB private StoreLocSapSession storeLocSapSession;
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();

	@EJB TokenSession tokenSession;


	@Path("/get-list")
	@GET
	public Response getList(
			@HeaderParam("Authorization") String token) {
			User user= tokenSession.findByToken(token).getUser();
		try {
			return Response.status(201).entity(storeLocSapSession.getList()).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	} 
	
	
}
