package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.SubBidangUsaha;
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
@Path(value = "/procurement/master/sub-bidang-usaha")
@Produces(MediaType.APPLICATION_JSON)
public class SubBidangUsahaService {

	@EJB
	private SubBidangUsahaSession subBidangUsahaSession;

	@EJB
	private BidangUsahaSession bidangUsahaSession;

	@EJB
	TokenSession tokenSession;

	

	@Path("/get-list")
	@GET
	public List<SubBidangUsaha> getSubBidangUsahaList() {
		return subBidangUsahaSession.getSubBidangUsahaList();
	}

	@Path("/get-by-bidang-usaha-id/{id}")
	@GET
	public List<SubBidangUsaha> getSubBidangUsahaByBidangUsahaId(
			@PathParam("id") int bidangUsahaId) {
		return subBidangUsahaSession.getSubBidangUsahaByBidangUsahaId(bidangUsahaId);
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(SubBidangUsaha subBidangUsaha,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = subBidangUsahaSession.checkNamaSubBidangUsaha(subBidangUsaha.getNama(), toDo, subBidangUsaha.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Sub Bidang Usaha Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			subBidangUsahaSession.insertSubBidangUsaha(subBidangUsaha, tokenSession.findByToken(token));
		}

		map.put("subBidangUsaha", subBidangUsaha);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(SubBidangUsaha subBidangUsaha,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = subBidangUsahaSession.checkNamaSubBidangUsaha(subBidangUsaha.getNama(), toDo, subBidangUsaha.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Sub Bidang Usaha Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			subBidangUsahaSession.updateSubBidangUsaha(subBidangUsaha, tokenSession.findByToken(token));
		}

		map.put("subBidangUsaha", subBidangUsaha);

		return map;
	}

	@Path("/delete")
	@POST
	public SubBidangUsaha deleteSubBidangUsaha(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return subBidangUsahaSession.deleteSubBidangUsaha(id, tokenSession.findByToken(token));
	}

}
