package id.co.promise.procurement.master;

import java.util.List;

import id.co.promise.procurement.entity.JenisTermin;
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
@Path("/procurement/master/jenisTerminServices")
@Produces(MediaType.APPLICATION_JSON)
public class JenisTerminServices {
	@EJB
	JenisTerminSession jenisTerminSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getId/{id}")
	@GET
	public JenisTermin getJenisTermin(@PathParam("id")int id){
		return jenisTerminSession.getJenisTermin(id);
	}
	
	@Path("/getList")
	@GET
	public List<JenisTermin> getJenisTerminList(){
		return jenisTerminSession.getJenisTerminList();
	}
	
	@Path("/created")
	@POST
	public JenisTermin insertJenisTermin(@FormParam("nama")String nama,
			@FormParam("keterangan")String keterangan,
			@HeaderParam("Authorization") String token){
		JenisTermin x = new JenisTermin();
		x.setNama(nama);
		x.setKeterangan(keterangan);
		x.setUserId(0);
		jenisTerminSession.insertJenisTermin(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/updated")
	@POST
	public JenisTermin updateJenisTermin(@FormParam("id")Integer id,
			@FormParam("nama")String nama,
			@FormParam("keterangan")String keterangan,
			@HeaderParam("Authorization") String token){
		JenisTermin x = jenisTerminSession.getJenisTermin(id);
		x.setNama(nama);
		x.setKeterangan(keterangan);
		jenisTerminSession.updateJenisTermin(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/delete/{id}")
	@GET
	public JenisTermin deleteJenisTermin(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return jenisTerminSession.deleteJenisTermin(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public JenisTermin deleteRowJenisTermin(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return jenisTerminSession.deleteRowJenisTermin(id, tokenSession.findByToken(token));
	}
}
