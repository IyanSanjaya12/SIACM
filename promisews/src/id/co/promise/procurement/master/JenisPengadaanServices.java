package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.JenisPengadaan;
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
@Path(value = "/procurement/master/jenisPengadaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class JenisPengadaanServices {
	@EJB
	private JenisPengadaanSession jenisPengadaanSession;

	@EJB
	TokenSession tokenSession;

	@Path("/getJenisPengadaan/{id}")
	@GET
	public JenisPengadaan getJenisPengadaan(@PathParam("id") int id) {
		return jenisPengadaanSession.getJenisPengadaan(id);
	}

	@Path("/getJenisPengadaanList")
	@GET
	public List<JenisPengadaan> getJenisPengadaanList() {
		/*
		 * try { Thread.sleep(30000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		return jenisPengadaanSession.getJenisPengadaanList();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(JenisPengadaan jenispengadaan,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = jenisPengadaanSession.checkNamaJenisPengadaan(
				jenispengadaan.getNama(), toDo, jenispengadaan.getId());

		if (!isSaveNama) {
			String errorMessageNama = "Nama Jenis Pengadaan Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMessageNama", errorMessageNama);
		}

		if (isSaveNama) {
			jenisPengadaanSession.insertJenisPengadaan(jenispengadaan, tokenSession.findByToken(token));
		}

		map.put("jenisPengadaan", jenispengadaan);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(JenisPengadaan jenispengadaan,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = jenisPengadaanSession.checkNamaJenisPengadaan(jenispengadaan.getNama(), toDo, jenispengadaan.getId());

		if (!isSaveNama) {
			String errorMessageNama = "Nama Jenis Pengadaan Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMessageNama", errorMessageNama);
		}

		if (isSaveNama) {
			jenisPengadaanSession.updateJenisPengadaan(jenispengadaan, tokenSession.findByToken(token));
		}

		map.put("jenisPengadaan", jenispengadaan);

		return map;
	}

	@Path("/delete")
	@POST
	public JenisPengadaan deleteJenisPengadaan(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return jenisPengadaanSession.deleteJenisPengadaan(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public JenisPengadaan deleteRowJenisPengadaan(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return jenisPengadaanSession.deleteRowJenisPengadaan(id, tokenSession.findByToken(token));
	}
}
