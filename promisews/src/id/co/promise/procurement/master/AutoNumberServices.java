package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.AutoNumber;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;
import java.util.Date;

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
@Path(value = "procurement/master/autonumberservices")
@Produces(MediaType.APPLICATION_JSON)

public class AutoNumberServices {
	
	@EJB
	private AutoNumberSession autoNumberSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getautonumberbytype/{type}")
	@GET
		public List<AutoNumber> getAutoNumberByType(@PathParam("type")String type) {
		return autoNumberSession.getAutoNumberByType(type);
	}
	
	@Path("/getautonumberlist")
	@GET
		public List<AutoNumber> getAutoNumberByList(){
	    return autoNumberSession.getAutoNumberByList();
	}
	
	@Path("/insert")
	@POST
	public AutoNumber insertAutoNumber(
			@FormParam("format")String format,
			@HeaderParam("Authorization") String token)
	{
		AutoNumber autonumber = new AutoNumber();
		autonumber.setFormat(format);
		autoNumberSession.insertAutoNumber(autonumber, tokenSession.findByToken(token));
		return autonumber;
	}
	
	@Path("/update")
	@POST
	public AutoNumber updateAutoNumber(
			@FormParam("id")Integer id,
			@FormParam("format")String format,
			@HeaderParam("Authorization") String token)
	{
		AutoNumber autonumber = autoNumberSession.find(id);
		autonumber.setFormat(format);
		return autoNumberSession.updateAutoNumber(autonumber, tokenSession.findByToken(token));
	}
	
	@Path("/delete")
	@POST
	public AutoNumber deleteAutoNumber(@FormParam("id")int id,
			@HeaderParam("Authorization") String token){
		return autoNumberSession.deleteAutoNumber(id, tokenSession.findByToken(token));
	}
	
}
