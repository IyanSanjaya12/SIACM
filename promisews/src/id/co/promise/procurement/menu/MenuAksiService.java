package id.co.promise.procurement.menu;

import id.co.promise.procurement.entity.MenuAksi;
import id.co.promise.procurement.entity.Token;
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
import javax.ws.rs.core.Response;

@Stateless
@Path(value="/procurement/menu/menu-aksi")
@Produces(MediaType.APPLICATION_JSON)
public class MenuAksiService {

	@EJB
	private MenuAksiSession menuAksiSession;
	
	@EJB
	private MenuSession menuSession;
	
	@EJB
	private AksiSession aksiSession;
	@EJB
	TokenSession tokenSession;
	@Path("/getMenuAksi/{id}")
	@GET
	public MenuAksi getMenuAksi(@PathParam("id")int id){
		return menuAksiSession.getMenuAksi(id);
	}
	
	/*
	 * @Path("/getMenuAksiList")
	 * 
	 * @GET public List<MenuAksi> getMenuAksilist(){ return
	 * menuAksiSession.getMenuAksiList(); }
	 */
	
	@Path("/find")
	@POST
	public Response find(@FormParam("search[value]") String keyword, @FormParam("start") Integer start,
			@FormParam("length") Integer length, @FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder, @FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token) {
		Token tokenObj = tokenSession.findByToken(token);
		String tempKeyword = "%" + keyword + "%";
		long countData = menuAksiSession.countFindByToken(tempKeyword);
		long countTotal = menuAksiSession.countTotalFindByToken(tokenObj);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countTotal);
		result.put("recordsFiltered", countData);
		result.put("data",
				menuAksiSession.findByToken(start, length, tempKeyword, tokenObj, columnOrder, tipeOrder));
		return Response.ok(result).build();
	}
	
	@Path("/insert")
	@POST
	public MenuAksi insertMenuAksi(MenuAksi menuAksi,
			@HeaderParam("Authorization") String token){
		menuAksi.setUserId(0);
		menuAksiSession.insertMenuAksi(menuAksi, tokenSession.findByToken(token));
		return menuAksi;
	}
	
	@Path("/update")
	@POST
	public MenuAksi updateMenuAksi(MenuAksi menuAksi,
			@HeaderParam("Authorization") String token){
		menuAksiSession.updateMenuAksi(menuAksi, tokenSession.findByToken(token));
		return menuAksi;
	}

	@Path("/delete")
	@POST
	public MenuAksi deleteMenuServis(@FormParam("id")int id,
			@HeaderParam("Authorization") String token){
		return menuAksiSession.deleteMenuAksi(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public MenuAksi deleteRowMenuAksi(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return menuAksiSession.deleteRowMenuAksi(id, tokenSession.findByToken(token));
	}
}
