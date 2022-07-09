package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Barang;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/master/barang")
@Produces(MediaType.APPLICATION_JSON)
public class BarangService {
	@EJB
	BarangSession barangSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getBarangList")
	@GET
	public List<Barang> getBarangList() {
		return barangSession.getbarangList();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Barang barang, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			if(barang.getId() != null) {
				barangSession.update(barang, token);
			}else {
				barangSession.insert(barang, token);
			}
			return Response.ok(barang).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Barang barang, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			barangSession.delete(barang.getId(), token);
			
			return Response.ok(barang).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
