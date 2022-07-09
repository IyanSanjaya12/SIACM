package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Mobil;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/master/mobil")
@Produces(MediaType.APPLICATION_JSON)
public class MobilService {
	@EJB
	MobilSession mobilSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getMobilList")
	@GET
	public List<Mobil> getMobilList() {
		return mobilSession.getMobilList();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Mobil mobil, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			if(mobil.getId() != null) {
				mobilSession.update(mobil, token);
			}else {
				mobilSession.insert(mobil, token);
			}
			return Response.ok(mobil).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Mobil mobil, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			mobilSession.delete(mobil.getId(), token);
			
			return Response.ok(mobil).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
