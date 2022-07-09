package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.JenisPajak;
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
@Path(value = "/procurement/master/JenisPajakServices")
@Produces(MediaType.APPLICATION_JSON)
public class JenisPajakServices {

	@EJB
	JenisPajakSession jenisPajakSession;
	@EJB
	TokenSession tokenSession;

	@Path("/getJenisPajakList")
	@GET
	public List<JenisPajak> getJenisPajakList() {
		return jenisPajakSession.getJenisPajakList();
	}

	@Path("/createJenisPajak")
	@POST
	public JenisPajak inserJenisPajak(@FormParam("nama") String nama,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("prosentase") Double prosentase,
			@HeaderParam("Authorization") String token) {
		JenisPajak object = new JenisPajak();
		object.setNama(nama);
		object.setDeskripsi(deskripsi);
		object.setProsentase(prosentase);
		object.setUserId(0);
		jenisPajakSession.insertJenisPajak(object, tokenSession.findByToken(token));
		return object;
	}

	@Path("/updateJenisPajak")
	@POST
	public JenisPajak updateJenisPajak(@FormParam("id") Integer id,
			@FormParam("nama") String nama,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("prosentase") Double prosentase,
			@HeaderParam("Authorization") String token) {
		JenisPajak object = jenisPajakSession.find(id);
		object.setNama(nama);
		object.setDeskripsi(deskripsi);
		object.setProsentase(prosentase);
		jenisPajakSession.updateJenisPajak(object, tokenSession.findByToken(token));
		return object;
	}

	@Path("/deleteJenisPajak/{id}")
	@GET
	public JenisPajak deleteJenisPajak(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return jenisPajakSession.deleteJenisPajak(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRowJenisPajak/{id}")
	@GET
	public JenisPajak deleteRowJenisPajak(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return jenisPajakSession.deleteRowJenisPajak(id, tokenSession.findByToken(token));
	}
}
