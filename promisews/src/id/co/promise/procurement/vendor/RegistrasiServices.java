package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.security.TokenSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value="/procurement/vendor/RegistrasiServices")
@Produces(MediaType.APPLICATION_JSON)
public class RegistrasiServices {
	@EJB
	RegistrasiSession regSession;
	@EJB
	TokenSession tokenSession;
	@Path("/getEmailIsValid/{email}")
	@GET
	public Boolean getEmailIsValid(@PathParam("email")String email){
		return regSession.getEmailIsValid(email);
	}
	
	@Path("/register")
	@POST
	public User registrasiUserVendor(@FormParam("email")String email, 
			@FormParam("password")String password,
			@HeaderParam("Authorization") String token){
		return regSession.registrasiUserVendor(email, password, tokenSession.findByToken(token));
	}
}
