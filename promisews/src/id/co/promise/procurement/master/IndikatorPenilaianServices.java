package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.IndikatorPenilaian;
import id.co.promise.procurement.security.TokenSession;

import java.text.SimpleDateFormat;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value = "/procurement/master/IndikatorPenilaianServices")
@Produces(MediaType.APPLICATION_JSON)
public class IndikatorPenilaianServices {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@EJB
	private IndikatorPenilaianSession IndikatorPenilaianSession;
	@EJB
	TokenSession tokenSession;

	@Path("/getIndikatorPenilaianList")
	@GET
	public List<IndikatorPenilaian> getIndikatorPenilaianList() {
		return IndikatorPenilaianSession.getIndikatorPenilaianList();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(IndikatorPenilaian indikatorPenilaian,
			@HeaderParam("Authorization") String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		Boolean isSaveKode = IndikatorPenilaianSession .checkKodeIndikatorPenilaian(indikatorPenilaian.getiPCode(), toDo, indikatorPenilaian.getiPId());
		if (!isSaveKode) {
			String errorKode = "Kode Indikator Penilaian Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorKode", errorKode);
		}
		if (isSaveKode) {
			indikatorPenilaian.setiPUserId(0);
			IndikatorPenilaianSession.createIndikatorPenilaian(indikatorPenilaian, tokenSession.findByToken(token));
		}
		map.put("indikatorPenilaian", indikatorPenilaian);
		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(IndikatorPenilaian indikatorPenilaian,
			@HeaderParam("Authorization") String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
		Boolean isSaveKode = IndikatorPenilaianSession.checkKodeIndikatorPenilaian(indikatorPenilaian.getiPCode(), toDo, indikatorPenilaian.getiPId());
		if (!isSaveKode) {
			String errorKode = "Kode Indikator Penilaian Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorKode", errorKode);
		}
		if (isSaveKode) {
			indikatorPenilaian.setiPUserId(0);
			IndikatorPenilaianSession.editIndikatorPenilaian(
					indikatorPenilaian, tokenSession.findByToken(token));
		}
		map.put("indikatorPenilaian", indikatorPenilaian);
		return map;
	}

	@Path("/delete")
	@POST
	public IndikatorPenilaian deleteIndikatorPenilaian(
			@FormParam("iPId") int id,
			@HeaderParam("Authorization") String token) {
		return IndikatorPenilaianSession.deleteIndikatorPenilaian(id, tokenSession.findByToken(token));
	}
	
}
