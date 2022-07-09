package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.entity.DokumenPekerjaan;
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
@Path(value = "/procurement/perencanaan/dokumenPekerjaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class DokumenPekerjaanServices {

	@EJB
	private DokumenPekerjaanSession dokumenPekerjaanSession;

	@EJB
	private PerencanaanSession perencanaanSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getList")
	@GET
	public List<DokumenPekerjaan> getList(){
		return dokumenPekerjaanSession.getList();
	}
	
	@Path("/getId/{id}")
	@GET
	public DokumenPekerjaan getId(@PathParam("id") int id) {
		return dokumenPekerjaanSession.get(id);
	}
	
	@Path("/getByPerencanaan/{perencanaanId}")
	@GET
	public List<DokumenPekerjaan> getDokumenPekerjaanByPerencanaan(
			@PathParam("perencanaanId") int perencanaanId) {
		return dokumenPekerjaanSession.getDokumenPekerjaanByPerencanaan(perencanaanId);
	}
	
	@Path("/insert")
	@POST
	public DokumenPekerjaan create(@FormParam("perencanaan") Integer perencanaan,
			@FormParam("fileName") String fileName,
			@FormParam("realFileName") String realFileName,
			@FormParam("fileSize") Long fileSize, @HeaderParam("Authorization")String token) {
		
		DokumenPekerjaan dokumenPekerjaan = new DokumenPekerjaan();
		dokumenPekerjaan.setPerencanaan(perencanaanSession.find(perencanaan));
		dokumenPekerjaan.setFileName(fileName);
		dokumenPekerjaan.setRealFileName(realFileName);
		dokumenPekerjaan.setFileSize(fileSize);
		dokumenPekerjaan.setUserId(0);
		dokumenPekerjaanSession.insert(dokumenPekerjaan, tokenSession.findByToken(token));
		return dokumenPekerjaan;
	}

	@Path("/update")
	@POST
	public DokumenPekerjaan update(@FormParam("id") Integer id,
			@FormParam("perencanaan") Integer perencanaan,
			@FormParam("fileName") String fileName,
			@FormParam("realFileName") String realFileName,
			@FormParam("fileSize") Long fileSize,
			@HeaderParam("Authorization") String token) {

		DokumenPekerjaan dokumenPekerjaan = dokumenPekerjaanSession.find(id);
		dokumenPekerjaan.setPerencanaan(perencanaanSession.find(perencanaan));
		dokumenPekerjaan.setFileName(fileName);
		dokumenPekerjaan.setRealFileName(realFileName);
		dokumenPekerjaan.setFileSize(fileSize);
		dokumenPekerjaan.setUserId(0);
		dokumenPekerjaanSession.update(dokumenPekerjaan, tokenSession.findByToken(token));
		return dokumenPekerjaan;
	}

	@Path("/delete/{id}")
	@GET
	public DokumenPekerjaan delete(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {

		return dokumenPekerjaanSession.delete(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public DokumenPekerjaan deleteRow(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {

		return dokumenPekerjaanSession.deleteRow(id, tokenSession.findByToken(token));
	}
}
