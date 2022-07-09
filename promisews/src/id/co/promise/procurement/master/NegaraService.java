package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Negara;
import id.co.promise.procurement.security.TokenSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Path(value = "/procurement/master/negara")
@Produces(MediaType.APPLICATION_JSON)
public class NegaraService {

	@EJB
	NegaraSession negaraSession;

	@EJB
	TokenSession tokenSession;

	/* @Path("/getById/{id}")
	@GET
	public Negara getNegara(@PathParam("id") int id) {
		return negaraSession.getNegara(id);
	} */

	@Path("/get-list")
	@GET
	public List<Negara> getNegaraList() {
		return negaraSession.getNegaraList();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(Negara negara, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = negaraSession.checkNamaNegara(negara.getNama(),
				toDo, negara.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Negara Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			negaraSession.insertNegara(negara, tokenSession.findByToken(token));
		}

		map.put("Negara", negara);

		return map;
	}

	@SuppressWarnings("rawtypes")
	@Path("/update")
	@POST
	public Map update(Negara negara, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = negaraSession.checkNamaNegara(negara.getNama(), toDo, negara.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Negara Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			negaraSession.updateNegara(negara, tokenSession.findByToken(token));
		}

		map.put("Negara", negara);

		return map;
	}

	@Path("/delete")
	@POST
	public Negara deleteNegara(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return negaraSession.deleteNegara(id, tokenSession.findByToken(token));
	}

}
