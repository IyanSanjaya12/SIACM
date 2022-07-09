/**
 * fdf
 */
package id.co.promise.procurement.master;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path(value = "/procurement/master/purch-org")
@Produces(MediaType.APPLICATION_JSON)
public class PurchOrgServices {
	final static Logger log = Logger.getLogger(PurchOrgServices.class);

	@EJB private PurchOrgSession purchOrgSession;
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();

	@EJB TokenSession tokenSession;


	@Path("/get-list-by-organisasi/{organisasiId}")
	@GET
	public Response getListByOrganisasi(@PathParam("organisasiId") Integer organisasiId) {
		try {
			return Response.status(Status.CREATED).entity(purchOrgSession.getListByOrganisasi(organisasiId)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	}
	
	@Path("/getAllCode")
	@GET
	public Response getListByOrganisasi() {
		try {
			return Response.status(Status.CREATED).entity(purchOrgSession.getAllCode()).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	} 
	
	
}
