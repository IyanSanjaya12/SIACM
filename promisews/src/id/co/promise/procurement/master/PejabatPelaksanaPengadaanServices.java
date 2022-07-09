package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

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
@Path("/procurement/master/pejabatPelaksanaPengadaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class PejabatPelaksanaPengadaanServices {
	@EJB
	PejabatPelaksanaPengadaanSession pejabatPelaksanaPengadaanSession;
	@EJB
	PanitiaSession panitiaSession;
	@EJB
	RoleUserSession roleUserSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getId/{id}")
	@GET
	public PejabatPelaksanaPengadaan getPelaksanaPengadaanId(@PathParam("id")int id){
		return pejabatPelaksanaPengadaanSession.getPelaksanaPengadaanId(id);
	}
	
	@Path("/getList")
	@GET
	public List<PejabatPelaksanaPengadaan> getPelaksanaPengadaanList(){
		return pejabatPelaksanaPengadaanSession.getPelaksanaPengadaanList();
	}
	
	@Path("/getByPanitiaList/{id}")
	@GET
	public List<PejabatPelaksanaPengadaan> getPelaksanaPengadaanByPanitiaList(@PathParam("id")int id){
		return pejabatPelaksanaPengadaanSession.getPelaksanaPengadaanByPanitiaList(id);
	}
	
	@Path("/create")
	@POST
	public PejabatPelaksanaPengadaan insertPelaksanaPengadaan(@FormParam("nama")String nama,
			@FormParam("panitia")Integer panitiaId,
			@FormParam("pic")Integer picId,
			@HeaderParam("Authorization") String token){
		PejabatPelaksanaPengadaan x = new PejabatPelaksanaPengadaan();
		x.setNama(nama);
		x.setPanitia(panitiaSession.find(panitiaId));
		x.setPic(roleUserSession.find(picId));
		x.setUserId(0);
		pejabatPelaksanaPengadaanSession.insertPelaksanaPengadaan(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/update")
	@POST
	public PejabatPelaksanaPengadaan updatePelaksanaPengadaan(@FormParam("id")Integer id,
			@FormParam("nama")String nama,
			@FormParam("panitia")Integer panitiaId,
			@FormParam("pic")Integer picId,
			@HeaderParam("Authorization") String token){
		PejabatPelaksanaPengadaan x = pejabatPelaksanaPengadaanSession.getPelaksanaPengadaanId(id);
		x.setNama(nama);
		x.setPanitia(panitiaSession.find(panitiaId));
		x.setPic(roleUserSession.find(picId));
		pejabatPelaksanaPengadaanSession.updatePelaksanaPengadaan(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/delete/{id}")
	@GET
	public PejabatPelaksanaPengadaan deletePelaksanaPengadaan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return pejabatPelaksanaPengadaanSession.deletePelaksanaPengadaan(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public PejabatPelaksanaPengadaan deleteRowPelaksanaPengadaan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return pejabatPelaksanaPengadaanSession.deleteRowPelaksanaPengadaan(id, tokenSession.findByToken(token));
	}
}
