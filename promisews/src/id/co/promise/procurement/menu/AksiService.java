package id.co.promise.procurement.menu;

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
import javax.ws.rs.core.Response;

import id.co.promise.procurement.entity.Aksi;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/menu/aksi")
@Produces(MediaType.APPLICATION_JSON)
public class AksiService {

	@EJB
	private AksiSession aksiSession;
	@EJB
	TokenSession tokenSession;

	@Path("/getAksi/{id}")
	@GET
	public Aksi getAksi(@PathParam("id") int id) {
		return aksiSession.getAksi(id);
	}

	@Path("/get-list")
	@GET
	public List<Aksi> getAksiList() {
		return aksiSession.getAksiList();
	}

	@Path("/get-public-aksi-list")
	@GET
	public List<Aksi> getPublicAksiList() {
		return aksiSession.getPublicAksiList();
	}
	
	@Path("/search-path/{max}/{path}")
	@GET
	public List<Aksi> searchPath(@PathParam("path")String path, @PathParam("max")int max){
		return aksiSession.searchPath(path, max);
	}
	
	@Path("/find")
	@POST
	public Response find(@FormParam("search[value]") String keyword, @FormParam("start") Integer start,
			@FormParam("length") Integer length, @FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder, @FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token) {
		Token tokenObj = tokenSession.findByToken(token);
		String tempKeyword = "%" + keyword + "%";
		long countData = aksiSession.countFindByToken(tempKeyword);
		long countTotal = aksiSession.countTotalFindByToken(tokenObj);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countTotal);
		result.put("recordsFiltered", countData);
		result.put("data", aksiSession.findByToken(start, length, tempKeyword, tokenObj, columnOrder, tipeOrder));
		return Response.ok(result).build();
	}
	
	@Path("/insert")
	@POST
	public Aksi insert(Aksi aksi,
			@HeaderParam("Authorization") String token) {
		aksiSession.insertAksi(aksi, tokenSession.findByToken(token));
		return aksi;
	}
	
	@Path("/update")
	@POST
	public Aksi update(Aksi aksi,
			@HeaderParam("Authorization") String token) {
		aksiSession.updateAksi(aksi, tokenSession.findByToken(token));
		return aksi;
	}

	@Path("/delete")
	@POST
	public Aksi deleteAksi(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return aksiSession.deleteAksi(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@POST
	public Aksi deleteRowAksi(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return aksiSession.deleteRowAksi(id, tokenSession.findByToken(token));
	}
}
