package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.JenisKontrak;

@Stateless
@Path(value = "/procurement/master/JenisKontrakServices")
@Produces(MediaType.APPLICATION_JSON)
public class JenisKontrakServices {
	@EJB
	private JenisKontrakSession jenisKontrakSession;
	
	@Path("/findAll")
	@GET
	public List<JenisKontrak> findAll(){
		return jenisKontrakSession.findAll();
	}
}
