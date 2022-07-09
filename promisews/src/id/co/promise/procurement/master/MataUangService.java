package id.co.promise.procurement.master;

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

import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/master/mata-uang")
@Produces(MediaType.APPLICATION_JSON)
public class MataUangService {
	@EJB
	private MataUangSession mataUangSession;
	@EJB
	TokenSession tokenSession;

	/* @Path("/getMataUang/{id}")
	@GET
	public MataUang getMataUang(@PathParam("id") int mataUangId) {
		return mataUangSession.getMataUang(mataUangId);
	} */

	@Path("/get-list")
	@GET
	public List<MataUang> getMataUanglist() {
		return mataUangSession.getMataUanglist();
	}


	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(MataUang matauang,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = mataUangSession.checkNamaMataUang(matauang.getNama(), toDo, matauang.getId());
		Boolean isSaveKode = mataUangSession.checkKodeMataUang(matauang.getKode(), toDo, matauang.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Mata Uang Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (!isSaveKode) {
			String errorKode = "Kode Mata Uang Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorKode", errorKode);
		}

		if (isSaveNama && isSaveKode) {
			mataUangSession.insertMataUang(matauang, tokenSession.findByToken(token));
		}

		map.put("mataUang", matauang);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(MataUang matauang,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = mataUangSession.checkNamaMataUang(matauang.getNama(), toDo, matauang.getId());
		Boolean isSaveKode = mataUangSession.checkKodeMataUang(matauang.getKode(), toDo, matauang.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Mata Uang Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (!isSaveKode) {
			String errorKode = "Kode Mata Uang Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorKode", errorKode);
		}

		if (isSaveNama && isSaveKode) {
			mataUangSession.updateMataUang(matauang, tokenSession.findByToken(token));
		}

		map.put("mataUang", matauang);

		return map;
	}
	
	@Path("/delete")
	@POST
	public MataUang deleteMataUang(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return mataUangSession.deleteMataUang(id, tokenSession.findByToken(token));
	}
}
