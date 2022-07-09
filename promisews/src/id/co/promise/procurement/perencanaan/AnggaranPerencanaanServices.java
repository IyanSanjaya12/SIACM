package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.entity.AnggaranPerencanaan;
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
@Path(value = "/procurement/perencanaan/anggaranPerencanaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class AnggaranPerencanaanServices {

	@EJB
	private AnggaranPerencanaanSession anggaranPerencanaanSession;

	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;
	
	@EJB
	private PerencanaanSession perencanaanSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getList")
	@GET
	public List<AnggaranPerencanaan> getList(){
		return anggaranPerencanaanSession.getList();
	}
	
	@Path("/getId/{id}")
	@GET
	public AnggaranPerencanaan getId(@PathParam("id") int id) {
		return anggaranPerencanaanSession.get(id);
	}
	
	@Path("/getListByAlokasiAnggaranId/{alokasiAnggaranId}")
	@GET
	public List<AnggaranPerencanaan> getListZC01AnggaranPerencanaanByAlokasiAnggaranId(
			@PathParam("alokasiAnggaranId")int alokasiAnggaranId){
		return anggaranPerencanaanSession.getListZC01AnggaranPerencanaanByAlokasiAnggaranId(alokasiAnggaranId);
	}
	
	@Path("/getSingleByAlokasiAnggaranId/{alokasiAnggaranId}")
	@GET
	public AnggaranPerencanaan getListSingleResultByAlokasiAnggaranId(
			@PathParam("alokasiAnggaranId")int alokasiAnggaranId){
		return anggaranPerencanaanSession.getListSingleResultByAlokasiAnggaranId(alokasiAnggaranId);
	}
	
	@Path("/insert")
	@POST
	public AnggaranPerencanaan insert(
			@FormParam("alokasiAnggaran")Integer alokasiAnggaran,
			@FormParam("perencanaan")Integer perencanaan,
			@HeaderParam("Authorization")String token) {
		
		AnggaranPerencanaan zc01AnggaranPerencanaan = new AnggaranPerencanaan();
		zc01AnggaranPerencanaan.setAlokasiAnggaran(alokasiAnggaranSession.find(alokasiAnggaran));
		zc01AnggaranPerencanaan.setPerencanaan(perencanaanSession.find(perencanaan));		
		zc01AnggaranPerencanaan.setUserId(0);
		anggaranPerencanaanSession.insert(zc01AnggaranPerencanaan, tokenSession.findByToken(token));
		return zc01AnggaranPerencanaan;
	}
	
	@Path("/update")
	@POST
	public AnggaranPerencanaan update(
			@FormParam("id")Integer id,			
			@FormParam("alokasiAnggaran")Integer alokasiAnggaran,
			@FormParam("perencanaan")Integer perencanaan,
			@HeaderParam("Authorization")String token) {

		AnggaranPerencanaan anggaranPerencanaan = anggaranPerencanaanSession.find(id);
		anggaranPerencanaan.setAlokasiAnggaran(alokasiAnggaranSession.find(alokasiAnggaran));
		anggaranPerencanaan.setPerencanaan(perencanaanSession.find(perencanaan));		
		anggaranPerencanaan.setUserId(0);
		anggaranPerencanaanSession.update(anggaranPerencanaan, tokenSession.findByToken(token));
		return anggaranPerencanaan;
	}
	
	@Path("/delete/{id}")
	@GET
	public AnggaranPerencanaan delete(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return anggaranPerencanaanSession.delete(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public AnggaranPerencanaan deleteRow(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return anggaranPerencanaanSession.deleteRow(id, tokenSession.findByToken(token));
	}
	
}
