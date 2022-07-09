package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.entity.Perencanaan;
import id.co.promise.procurement.security.TokenSession;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Path("/procurement/perencanaan/perencanaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class PerencanaanServices {

	@EJB
	PerencanaanSession perencanaanSession;
	
	@EJB
	TokenSession tokenSession;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	@Path("/getList")
	@GET
	public List<Perencanaan> getList() {
		return perencanaanSession.getList();
	}
	
	@Path("/getPerencanaanId/{id}")
	@GET
	public Perencanaan getId(@PathParam("id") int id) {
		return perencanaanSession.get(id);
	}
	
	@Path("/insert")
	@POST
	public Perencanaan create(
			@FormParam("nama") String nama,
			@FormParam("nomor") String nomor,
			@FormParam("tanggalMemo") String tanggalMemo,
			@FormParam("keterangan") String keterangan,
			@HeaderParam("Authorization")String token) {
		
		Perencanaan pr = new Perencanaan();
		pr.setNama(nama);
		pr.setNomor(nomor);
		try {
			pr.setTanggalMemo(sdf.parse(tanggalMemo));
		} catch (Exception e) {

		}
		pr.setKeterangan(keterangan);
		pr.setUserId(tokenSession.findByToken(token).getUser().getId());
		perencanaanSession.create(pr, tokenSession.findByToken(token));
		return pr;
	}
	
	@Path("/update")
	@POST
	public Perencanaan update(
			@FormParam("id") Integer id,
			@FormParam("nama") String nama,
			@FormParam("nomor") String nomor,
			@FormParam("tanggalMemo") String tanggalMemo,
			@FormParam("keterangan") String keterangan,
			@HeaderParam("Authorization")String token) {

		Perencanaan pr = perencanaanSession.find(id);
		pr.setNama(nama);
		pr.setNomor(nomor);
		try {
			pr.setTanggalMemo(sdf.parse(tanggalMemo));
		} catch (Exception e) {

		}
		pr.setKeterangan(keterangan);
		pr.setUserId(0);
		perencanaanSession.update(pr, tokenSession.findByToken(token));
		return pr;
	}
	
	@Path("/delete/{id}")
	@GET
	public Perencanaan delete(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return perencanaanSession.delete(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public Perencanaan deleteRow(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return perencanaanSession.deleteRow(id, tokenSession.findByToken(token));
	}
}
