package id.co.promise.procurement.master;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.promise.procurement.entity.JenisPenawaran;
import id.co.promise.procurement.entity.JenisPengadaan;
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
@Path(value = "/procurement/master/JenisPenawaranServices")
@Produces(MediaType.APPLICATION_JSON)
public class JenisPenawaranServices {

	@EJB
	private JenisPenawaranSession jenisPenawaranSession;
	@EJB
	TokenSession tokenSession;

	@Path("/getJenisPenawaran/{id}")
	@GET
	public JenisPenawaran getJenisPenawaran(@PathParam("id") int id) {
		return jenisPenawaranSession.getJenisPenawaran(id);
	}

	@Path("/getJenisPenawaranList")
	@GET
	public List<JenisPenawaran> getJenisPenawaranList() {
		return jenisPenawaranSession.getJenisPenawaranList();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(JenisPenawaran jenispenawaran,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = jenisPenawaranSession.checkNamaJenisPenawaran(jenispenawaran.getNama(), toDo, jenispenawaran.getId());

		if (!isSaveNama) {
			String errorMessageNama = "Nama Jenis Penawaran Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMessageNama", errorMessageNama);
		}

		if (isSaveNama) {
			jenisPenawaranSession.insertJenisPenawaran(jenispenawaran, tokenSession.findByToken(token));
		}

		map.put("jenisPenawaran", jenispenawaran);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(JenisPenawaran jenispenawaran,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = jenisPenawaranSession.checkNamaJenisPenawaran(jenispenawaran.getNama(), toDo, jenispenawaran.getId());

		if (!isSaveNama) {
			String errorMessageNama = "Nama Jenis Penawaran Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMessageNama", errorMessageNama);
		}

		if (isSaveNama) {
			jenisPenawaranSession.updateJenisPenawaran(jenispenawaran, tokenSession.findByToken(token));
		}

		map.put("jenisPenawaran", jenispenawaran);

		return map;
	}

	@Path("/delete")
	@POST
	public JenisPenawaran delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return jenisPenawaranSession.deleteJenisPenawaran(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@POST
	public JenisPenawaran deleteRow(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return jenisPenawaranSession.deleteRowJenisPenawaran(id, tokenSession.findByToken(token));
	}
}
