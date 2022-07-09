package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.KualifikasiPengadaan;
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
@Path(value = "/procurement/master/KualifikasiPengadaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class KualifikasiPengadaanServices {
	@EJB
	private KualifikasiPengadaanSession kualifikasiPengadaanSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getKualifikasiPengadaan/{id}")
	@GET
	public KualifikasiPengadaan getKualifikasiPengadaan(@PathParam("id")int id){
		return kualifikasiPengadaanSession.getKualifikasiPengadaan(id);
	}
	
	@Path("/getKualifikasiPengadaanList")
	@GET
	public List<KualifikasiPengadaan> getKualifikasiPengadaanList(){
		return kualifikasiPengadaanSession.getKualifikasiPengadaanList();
	}
	
	@Path("/insertKualifikasiPengadaan")
	@POST
	public KualifikasiPengadaan insertKualifikasiPengadaan(@FormParam("nama")String nama,
			@HeaderParam("Authorization") String token){
		KualifikasiPengadaan kp = new KualifikasiPengadaan();
		kp.setNama(nama);
		kp.setCreated(new Date());
		kualifikasiPengadaanSession.insertKualifikasiPengadaan(kp, tokenSession.findByToken(token));
		return kp;
	}
	
	@Path("/updateKualifikasiPengadaan")
	@POST
	public KualifikasiPengadaan updateKualifikasiPengadaan(@FormParam("id")int id,
			@FormParam("nama")String nama,
			@HeaderParam("Authorization") String token){
		KualifikasiPengadaan kp = kualifikasiPengadaanSession.find(id);
		kp.setNama(nama);
		kp.setUpdated(new Date());
		kualifikasiPengadaanSession.updateKualifikasiPengadaan(kp, tokenSession.findByToken(token));
		return kp;		
	}
	
	@Path("/deleteKualifikasiPengadaan/{id}")
	@GET
	public KualifikasiPengadaan deleteKualifikasiPengadaan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return kualifikasiPengadaanSession.deleteKualifikasiPengadaan(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRowKualifikasiPengadaan/{id}")
	@GET
	public KualifikasiPengadaan deleteRowKualifikasiPengadaan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return kualifikasiPengadaanSession.deleteRowKualifikasiPengadaan(id, tokenSession.findByToken(token));
	}
}
