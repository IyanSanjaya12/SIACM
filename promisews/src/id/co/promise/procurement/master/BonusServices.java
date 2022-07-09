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
import id.co.promise.procurement.entity.siacm.Bonus;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/master/bonus")
@Produces(MediaType.APPLICATION_JSON)
public class BonusServices {
	@EJB
	BonusSession bonusSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getBonusList")
	@GET
	public List<Bonus> getBonusList() {
		return bonusSession.getbonusList();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Bonus bonus, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			if(bonus.getId() != null) {
				bonusSession.update(bonus, token);
			}else {
				bonusSession.insert(bonus, token);
			}
			return Response.ok(bonus).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Bonus bonus, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			bonusSession.delete(bonus.getId(), token);
			
			return Response.ok(bonus).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
