package id.co.promise.procurement.master;

import java.util.List;

import id.co.promise.procurement.entity.AnggotaPanitia;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.RoleUserSession;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

@Stateless
@Path("/procurement/master/anggotaPanitiaServices")
@Produces(MediaType.APPLICATION_JSON)
public class AnggotaPanitiaServices {
	@EJB
	AnggotaPanitiaSession anggotaPanitiaSession;
	@EJB
	RoleUserSession roleUserSession;
	@EJB
	TimPanitiaSession timPanitiaSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getId/{id}")
	@GET
	public AnggotaPanitia getAnggotaPanitiaId(@PathParam("id")int id){
		return anggotaPanitiaSession.getAnggotaPanitiaId(id);
	}
	
	@Path("/getList")
	@GET
	public List<AnggotaPanitia> getAnggotaPanitiaList(){
		return anggotaPanitiaSession.getAnggotaPanitiaList();
	}
	
	@Path("/getByTimPanitiaList/{id}")
	@GET
	public List<AnggotaPanitia> getAnggotaPanitiaByPanitiaList(@PathParam("id")int id){
		return anggotaPanitiaSession.getAnggotaPanitiaByTimPanitiaList(id);
	}
	
	@Path("/getByUserList/{id}")
	@GET
	public List<AnggotaPanitia> getAnggotaPanitiaByUserList(@PathParam("id")int id){
		return anggotaPanitiaSession.getAnggotaPanitiaByRoleUserList(id);
	}
	
	@Path("/create")
	@POST
	public AnggotaPanitia insertAnggotaPanitia(@FormParam("kdPosisi")Integer kdPosisi,
			@FormParam("pic")Integer picId,
			@FormParam("timPanitia")Integer timPanitiaId,@HeaderParam("Authorization") String token){
		AnggotaPanitia x = new AnggotaPanitia();
		x.setKdPosisi(kdPosisi);
		x.setPic(roleUserSession.getRoleUserByUserId(picId).get(0));
		x.setTimPanitia(timPanitiaSession.find(timPanitiaId));
		x.setUserId(0);
		anggotaPanitiaSession.insertAnggotaPanitia(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/update")
	@POST
	public AnggotaPanitia updateAnggotaPanitia(@FormParam("id")Integer id,
			@FormParam("kdPosisi")Integer kdPosisi,
			@FormParam("pic")Integer picId,
			@FormParam("timPanitia")Integer timPanitiaId,@HeaderParam("Authorization") String token){
		AnggotaPanitia x = anggotaPanitiaSession.find(id);
		if (kdPosisi != null && kdPosisi > 0) {
			x.setKdPosisi(kdPosisi);
		}
		x.setPic(roleUserSession.getRoleUserByUserId(picId).get(0));
		x.setTimPanitia(timPanitiaSession.find(timPanitiaId));
		anggotaPanitiaSession.updateAnggotaPanitia(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/deleteByTeamId/{teamId}")
	@GET
	public Response deleteByTeamId(@PathParam("teamId")int teamId,
			@HeaderParam("Authorization") String tokenStr){
		Token token = tokenSession.findByToken(tokenStr);
		try{
			//get team
			List<AnggotaPanitia> agtPanitiaList = anggotaPanitiaSession.getAnggotaPanitiaByTimPanitiaList(teamId);
			for (AnggotaPanitia anggotaPanitia : agtPanitiaList) {
				anggotaPanitiaSession.deleteAnggotaPanitia(anggotaPanitia.getId(), token);
			}
			return Response.ok().build();
		}catch(Exception e){
			return Response.status(Status.BAD_REQUEST).build();
		}
		
	}
	
	@Path("/delete/{id}")
	@GET
	public AnggotaPanitia deleteAnggotaPanitia(@PathParam("id")int id,@HeaderParam("Authorization") String token){
		return anggotaPanitiaSession.deleteAnggotaPanitia(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public AnggotaPanitia deleteRowAnggotaPanitia(@PathParam("id")int id,@HeaderParam("Authorization") String token){
		return anggotaPanitiaSession.deleteRowAnggotaPanitia(id, tokenSession.findByToken(token));
	}
}
