package id.co.promise.procurement.master;


import id.co.promise.procurement.entity.AlasanBlacklist;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path(value = "/procurement/master/AlasanBlacklistServices")
@Produces(MediaType.APPLICATION_JSON)
public class AlasanBlacklistServices {

	@EJB
	AlasanBlacklistSession alasanBlacklistSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getById/{id}")
	@GET
	public AlasanBlacklist getAlasanBlacklist(@PathParam("id") int id) {
		return alasanBlacklistSession.getAlasanBlacklist(id);
	}

	@Path("/getList")
	@GET
	public List<AlasanBlacklist> getAlasanBlacklistList() {
		return alasanBlacklistSession.getAlasanBlacklistList();
	}
	
	@Path("/getAlasanBlacklistByPagination")
	@POST
	public JsonObject<AlasanBlacklist> getAlasanBlacklistByPagination(
			@Context HttpServletRequest httpServletRequest, @HeaderParam("Authorization") String token) {

		String start = httpServletRequest.getParameter("start");
		String draw = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");

		List<AlasanBlacklist> alasanBlacklistList = new ArrayList<AlasanBlacklist>();

		List<Object[]> arrObjList = alasanBlacklistSession.getAlasanBlacklistByPagination(Integer.valueOf(start),
				Integer.valueOf(length), search, Integer.valueOf(column), order);

		for (Object[] obj : arrObjList) {
			AlasanBlacklist alasanBlacklist = new AlasanBlacklist();
			alasanBlacklist.setAlasanBlacklistId((Integer) obj[0]);
			alasanBlacklist.setAlasanBlacklistKeterangan((String) obj[1]);
			alasanBlacklist.setAlasanBlacklistJmlWaktu((Integer) obj[2]);
			alasanBlacklist.setAlasanBlacklistJkWaktu((String) obj[3]);

			alasanBlacklistList.add(alasanBlacklist);
		}

		Integer size = alasanBlacklistSession.getAlasanBlacklistPaggingSize(search);

		JsonObject<AlasanBlacklist> jo = new JsonObject<AlasanBlacklist>();
		jo.setData(alasanBlacklistList);
		jo.setRecordsTotal(size);
		jo.setRecordsFiltered(size);
		jo.setDraw(draw);
		return jo;
	}

	@Path("/insert")
	@POST
	public AlasanBlacklist insert(
			AlasanBlacklist alasanBlacklist,
			@HeaderParam("Authorization") String token) { 
		alasanBlacklistSession.insertAlasanBlacklist(alasanBlacklist, tokenSession.findByToken(token));
		return alasanBlacklist;
	}

	@Path("/update")
	@POST
	public AlasanBlacklist updateAlasanBlacklist(
			AlasanBlacklist alasanBlacklist,
			@HeaderParam("Authorization") String token) { 
		alasanBlacklistSession.updateAlasanBlacklist(alasanBlacklist, tokenSession.findByToken(token));
		return alasanBlacklist;
	}

	@Path("/delete/{id}")
	@GET
	public AlasanBlacklist deleteAlasanBlacklist(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return alasanBlacklistSession.deleteAlasanBlacklist(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public AlasanBlacklist deleteRowAlasanBlacklist(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return alasanBlacklistSession.deleteRowAlasanBlacklist(id, tokenSession.findByToken(token));
	}
	
}
