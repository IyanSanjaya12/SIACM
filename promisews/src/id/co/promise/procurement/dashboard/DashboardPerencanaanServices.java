package id.co.promise.procurement.dashboard;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/DashboardPerencanaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardPerencanaanServices {
	@EJB
	TokenSession tokenSession;
}
