package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.Parameter;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/master/parameter")
@Produces(MediaType.APPLICATION_JSON)
public class ParameterService {
	@EJB
	ParameterSession ParameterSession;

	@EJB
	TokenSession tokenSession;

	/*
	 * @Path("/getByName/{name}")
	 * 
	 * @GET public Parameter getParameter(@PathParam("name") String name) { return
	 * ParameterSession.getParameterByName(name); }
	 */

	@Path("/get-list")
	@GET
	public List<Parameter> getParameterList() {
		return ParameterSession.getParameterList();
	}
	
	@Path("/get-name/{name}")
	@GET
	public Parameter getParameterByName(@PathParam("name") String name) {
		return ParameterSession.getParameterByName(name);
	}

	@Path("/update")
	@POST
	public Parameter updateParameter(Parameter parameter,
			@HeaderParam("Authorization") String token) {
		ParameterSession.updateParameter(parameter, tokenSession.findByToken(token));
		return parameter;
	}

}
