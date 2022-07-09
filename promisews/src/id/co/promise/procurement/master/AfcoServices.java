/**
 * fdf
 */
package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

/**
 * @author User
 *
 */
@Stateless
@Path(value = "/procurement/master/AfcoServices")
@Produces(MediaType.APPLICATION_JSON)
public class AfcoServices {
	final static Logger log = Logger.getLogger(AfcoServices.class);
	final static CustomResponseMessage message = CustomResponse
			.getCustomResponseMessage();
	@EJB
	private AfcoSession afcoSession;

	@EJB
	private OrganisasiSession organisasiSession;

	@EJB
	TokenSession tokenSession;

	@Path("/getAfco/{id}")
	@GET
	public Afco getAfco(@PathParam("id") int id) {
		return afcoSession.getAfco(id);
	}
	

	@Path("/getAfcoByOrganisasi/{id}")
	@GET
	public Afco getAfcoByOrganisasi(@PathParam("id") int id) {
		return afcoSession.findByOrganisasiId(id);
	}
	
	@Path("/getAfcoByOrganisasiParentId/{id}")
	@GET
	public Afco getAfcoByOrganisasiParentId(@PathParam("id") int id) {
		return afcoSession.findByOrganisasiParentId(id);
	}

	@Path("/getAfcoList")
	@GET
	public List<Afco> getAfcoList() {
		return afcoSession.getAfcoList();
	}
		
	@Path("/getAfcoByOrganisasiUserId/{userId}")
	@GET
	public List<Afco> getAfcoByOrganisasiUserId(@PathParam("userId") int userId) {
		return afcoSession.getAfcoByOrganisasiUserId(userId);
	}
	
	@Path("/getAfcoByOrganisasiIdAbstract/{organisasiId}")
	@GET
	public Afco getAfcoByOrganisasiIdAbstract(@PathParam("organisasiId") int organisasiId) {
		return afcoSession.getAfcoByOrganisasiIdAbstract(organisasiId);
	}
	
	@Path("/insert")
	@POST
	public Afco insert(AfcoDTO afcoDTO, String token) {
		
		Afco afco = afcoDTO.getAfco();
		afco.setOrganisasi(organisasiSession.find(afcoDTO.getOrganisasiId()));
		afcoSession.insertAfco(afco, tokenSession.findByToken(token));
		return afco;
	}
	
	@Path("/update")
	@POST
	public Afco update(AfcoDTO afcoDTO, String token) {
		Afco afco = afcoDTO.getAfco();
		afco.setOrganisasi(organisasiSession.find(afcoDTO.getOrganisasiId()));
		afcoSession.updateAfco(afco, tokenSession.findByToken(token));
		return afco;
	}
	
	@Path("/delete")
	@POST
	public Afco delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return afcoSession.deleteAfco(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRowAfco/{id}")
	@GET
	public Afco deleteRowAfco(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return afcoSession.deleteRowAfco(id, tokenSession.findByToken(token));
	}

	@Path("/getAfcoByCompanyName")
	@POST
	public Response getAfcoByCompanyName(
			@FormParam("companyName") String companyName,
			@HeaderParam("Authorization") String token) {
		try {
			return Response
					.status(201)
					.entity(afcoSession.getAfcoByCompanyName("%" + companyName
							+ "%")).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	}
	
	@Path("/getAfcoByToken")
	@GET
	public Response getAfcoByToken(@HeaderParam("Authorization")String token){
		try {
			return Response.ok(afcoSession.getAfcoByToken(tokenSession.findByToken(token))).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(message.error(e.getMessage())).build();
		}
	}
}
