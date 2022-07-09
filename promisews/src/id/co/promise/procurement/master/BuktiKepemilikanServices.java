package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.BuktiKepemilikan;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

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
@Path(value = "/procurement/master/BuktiKepemilikanServices")
@Produces(MediaType.APPLICATION_JSON)
public class BuktiKepemilikanServices {

	@EJB
	BuktiKepemilikanSession buktiKepemilikanSession;
	@EJB
	TokenSession tokenSession;

	@Path("/getById/{id}")
	@GET
	public BuktiKepemilikan getBuktiKepemilikan(@PathParam("id") int id) {
		return buktiKepemilikanSession.getBuktiKepemilikan(id);
	}

	@Path("/getList")
	@GET
	public List<BuktiKepemilikan> getBuktiKepemilikanList() {
		return buktiKepemilikanSession.getBuktiKepemilikanList();
	}

	@Path("/create")
	@POST
	public BuktiKepemilikan insertBuktiKepemilikan(
			@FormParam("nama") String nama,
			@HeaderParam("Authorization") String token) {
		BuktiKepemilikan bk = new BuktiKepemilikan();
		bk.setNama(nama);
		bk.setUserId(0);
		buktiKepemilikanSession.insertBuktiKepemilikan(bk,
				tokenSession.findByToken(token));
		return bk;
	}

	@Path("/update")
	@POST
	public BuktiKepemilikan updateBuktiKepemilikan(@FormParam("id") Integer id,
			@FormParam("nama") String nama,
			@HeaderParam("Authorization") String token) {
		BuktiKepemilikan bk = buktiKepemilikanSession.find(id);
		bk.setNama(nama);
		buktiKepemilikanSession.updateBuktiKepemilikan(bk,
				tokenSession.findByToken(token));
		return bk;
	}

	@Path("/delete/{id}")
	@GET
	public BuktiKepemilikan deleteBuktiKepemilikan(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return buktiKepemilikanSession.deleteBuktiKepemilikan(id,
				tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public BuktiKepemilikan deleteRowBuktiKepemilikan(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return buktiKepemilikanSession.deleteRowBuktiKepemilikan(id,
				tokenSession.findByToken(token));
	}
}
