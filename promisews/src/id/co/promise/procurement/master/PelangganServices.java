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
import id.co.promise.procurement.entity.siacm.Pelanggan;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/master/pelanggan")
@Produces(MediaType.APPLICATION_JSON)
public class PelangganServices {
	@EJB
	PelangganSession pelangganSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getPelangganList")
	@GET
	public List<Pelanggan> getPelangganList() {
		return pelangganSession.getpelangganList();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Pelanggan pelanggan, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			if(pelanggan.getId() != null) {
				pelangganSession.update(pelanggan, token);
			}else {
				pelangganSession.insert(pelanggan, token);
			}
			return Response.ok(pelanggan).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Pelanggan pelanggan, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			pelangganSession.delete(pelanggan.getId(), token);
			
			return Response.ok(pelanggan).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
