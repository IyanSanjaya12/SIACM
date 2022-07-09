package id.co.promise.procurement.utils;

import id.co.promise.procurement.security.UserSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

@Stateless
@Path(value = "/procurement")
@Produces(MediaType.APPLICATION_JSON)
public class SecurityCheckerServices {
	final static Logger logger = Logger
			.getLogger(SecurityCheckerServices.class);

	@EJB
	UserSession userSession;

	@Path("{url : .*}")
	@OPTIONS
	public Response getOption(@PathParam("url") String url) {		
		return Response.ok().status(200).entity("OPTIONS Connected OK").build();
	}
}
