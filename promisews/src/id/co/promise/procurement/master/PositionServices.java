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
import id.co.promise.procurement.entity.siacm.Position;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/master/position")
@Produces(MediaType.APPLICATION_JSON)
public class PositionServices {
	@EJB
	PositionSession positionSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getPositionList")
	@GET
	public List<Position> getPositionList() {
		return positionSession.getpositionList();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Position position, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			if(position.getId() != null) {
				positionSession.update(position, token);
			}else {
				positionSession.insert(position, token);
			}
			return Response.ok(position).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Position position, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			positionSession.delete(position.getId(), token);
			
			return Response.ok(position).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
