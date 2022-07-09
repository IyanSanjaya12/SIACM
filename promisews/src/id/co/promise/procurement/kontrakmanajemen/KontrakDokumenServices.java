package id.co.promise.procurement.kontrakmanajemen;

import id.co.promise.procurement.entity.KontrakDokumen;
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
@Path(value = "/procurement/KontrakDokumenServices")
@Produces(MediaType.APPLICATION_JSON)
public class KontrakDokumenServices {
	@EJB private KontrakDokumenSession kontrakDokumenSession;
	@EJB private KontrakSession kontrakSession;
	
	@EJB
	TokenSession tokenSession;

	@Path("/getKontrakDokumenListBykontrakId/{kontrakId}")
	@GET
	public List<KontrakDokumen> getKontrakDokumenListBykontrakId(@PathParam("kontrakId")Integer kontrakId) {
		return kontrakDokumenSession.getKontrakDokumenListBykontrakId(kontrakId);
	}

	@Path("/create")
	@POST
	public KontrakDokumen create(
			@FormParam("kontrak") Integer kontrakId,
			@FormParam("fileName") String fileName,
			@FormParam("pathFile") String pathFile,
			@FormParam("fileSize") long fileSize,
			@HeaderParam("Authorization") String token) {
		KontrakDokumen kontrakDokumen = new KontrakDokumen();
		
		kontrakDokumen.setKontrak(kontrakSession.find(kontrakId));
		
		kontrakDokumen.setFileName(fileName);
		kontrakDokumen.setPathFile(pathFile);
		kontrakDokumen.setFileSize(fileSize);
		
		kontrakDokumenSession.insertKontrakDokumen(kontrakDokumen, tokenSession.findByToken(token));
		
		return kontrakDokumen;
	}

	@Path("/update")
	@POST
	public KontrakDokumen update(
			@FormParam("id") Integer kontrakDokumenId,
			@FormParam("kontrak") Integer kontrakId,
			@FormParam("fileName") String fileName,
			@FormParam("pathFile") String pathFile,
			@FormParam("fileSize") long fileSize,
			@HeaderParam("Authorization") String token) {
		KontrakDokumen kontrakDokumen = kontrakDokumenSession.find(kontrakDokumenId);
		
		kontrakDokumen.setKontrak(kontrakSession.find(kontrakId));
		
		kontrakDokumen.setFileName(fileName);
		kontrakDokumen.setPathFile(pathFile);
		kontrakDokumen.setFileSize(fileSize);
		
		kontrakDokumenSession.updateKontrakDokumen(kontrakDokumen, tokenSession.findByToken(token));
		
		return kontrakDokumen;
	}
	
	@Path("/delete/{id}")
	@GET
	public KontrakDokumen deleteKontrakDokumen(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return kontrakDokumenSession.deleteKontrakDokumen(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public KontrakDokumen deleteRowKontrakDokumen(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return kontrakDokumenSession.deleteRowKontrakDokumen(id, tokenSession.findByToken(token));
	}

}