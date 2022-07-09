package id.co.promise.procurement.master;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import id.co.promise.procurement.entity.TimPanitia;
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
@Path("/procurement/master/timPanitiaServices")
@Produces(MediaType.APPLICATION_JSON)
public class TimPanitiaServices {
	@EJB
	TimPanitiaSession timPanitiaSession;
	@EJB
	PanitiaSession panitiaSession;
	@EJB
	PembuatKeputusanSession pembuatKeputusanSession;
	
	@EJB
	TokenSession tokenSession;
	
	private SimpleDateFormat formatIndonesia = new SimpleDateFormat("yyyy-MM-dd");
	
	@Path("/getId/{id}")
	@GET
	public TimPanitia getPanitiaPengadaanId(@PathParam("id")int id){
		return timPanitiaSession.getPanitiaPengadaanId(id);
	}
	
	@Path("/getList")
	@GET
	public List<TimPanitia> getPanitiaPengadaanList(){
		return timPanitiaSession.getPanitiaPengadaanList();
	}
	
	@Path("/getByPanitiaList/{id}")
	@GET
	public List<TimPanitia> getPanitiaPengadaanByPengadaanList(@PathParam("id")int id){
		return timPanitiaSession.getPanitiaPengadaanByPanitiaList(id);
	}
	
	@Path("/create")
	@POST
	public TimPanitia insertPanitiaPengadaan(@FormParam("nama")String nama,
			@FormParam("nomorSk")String nomorSk,
			@FormParam("namaKeputusan")String namaKeputusan,
			@FormParam("penanggungJawab")String penanggungJawab,
			@FormParam("tanggalSk")String tanggalSk,
			@FormParam("panitia")Integer panitiaId,
			@FormParam("pembuatKeputusan")Integer pembuatKeputusanId,
			@HeaderParam("Authorization") String token){
		TimPanitia x = new TimPanitia();
		x.setNama(nama);
		x.setNomorSk(nomorSk);
		x.setNamaKeputusan(namaKeputusan);
		x.setPenanggungJawab(penanggungJawab);
		try {
			x.setTanggalSk(formatIndonesia.parse(tanggalSk));
		} catch (ParseException e) {}
		x.setPanitia(panitiaSession.find(panitiaId));
		x.setPembuatKeputusan(pembuatKeputusanSession.find(pembuatKeputusanId));
		x.setUserId(0);
		timPanitiaSession.insertPanitiaPengadaan(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/update")
	@POST
	public TimPanitia updatePanitiaPengadaan(@FormParam("id")Integer id,
			@FormParam("nama")String nama,
			@FormParam("nomorSk")String nomorSk,
			@FormParam("namaKeputusan")String namaKeputusan,
			@FormParam("penanggungJawab")String penanggungJawab,
			@FormParam("tanggalSk")String tanggalSk,
			@FormParam("panitia")Integer panitiaId,
			@FormParam("pembuatKeputusan")Integer pembuatKeputusanId,
			@HeaderParam("Authorization") String token){
		TimPanitia x = timPanitiaSession.getPanitiaPengadaanId(id);
		x.setNama(nama);
		x.setNomorSk(nomorSk);
		x.setNamaKeputusan(namaKeputusan);
		x.setPenanggungJawab(penanggungJawab);
		try {
			x.setTanggalSk(formatIndonesia.parse(tanggalSk));
		} catch (ParseException e) {}
		x.setPanitia(panitiaSession.find(panitiaId));
		x.setPembuatKeputusan(pembuatKeputusanSession.find(pembuatKeputusanId));
		timPanitiaSession.updatePanitiaPengadaan(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/delete/{id}")
	@GET
	public TimPanitia deletePanitiaPengadaan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return timPanitiaSession.deletePanitiaPengadaan(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public TimPanitia deleteRowPanitiaPengadaan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return timPanitiaSession.deleteRowPanitiaPengadaan(id, tokenSession.findByToken(token));
	}
}
