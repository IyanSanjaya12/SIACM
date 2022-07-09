package id.co.promise.procurement.master;

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
import javax.ws.rs.core.Response.StatusType;

import id.co.promise.procurement.entity.WebServicesManager;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/master/webservicesmanager")
@Produces(MediaType.APPLICATION_JSON)
public class WebServicesManagerServices {
	@EJB
	private TokenSession tokenSession;
	@EJB
	private WebServicesManagerSession wsmSession;

	@Path("/findall")
	@GET
	public List<WebServicesManager> findAll() {
		return wsmSession.findAll();
	}

	@Path("/findWithPagging")
	@POST
	public Response findWithPagging(@FormParam("search[value]") String keyword, @FormParam("start") Integer start,
			@FormParam("length") Integer length, @FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder, @FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token) {
		String tempKeyword = "%" + keyword + "%";
		try {
			return Response.ok(wsmSession.findWithPagging(start, length, tempKeyword, columnOrder, tipeOrder, draw))
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@Path("/find/{id}")
	@GET
	public WebServicesManager findById(@PathParam("id") int id) {
		return wsmSession.findById(id);
	}

	@Path("/insert")
	@POST
	public WebServicesManager insertWebServiceManager(@HeaderParam("Authorization") String token,
			@FormParam("services") String services, @FormParam("function") String function,
			@FormParam("path") String path, @FormParam("isPublic") int isPublic) {
		WebServicesManager wsm = new WebServicesManager();
		wsm.setFunction(function);
		wsm.setIsPublic((isPublic==1?true:false));
		wsm.setPath(path);
		wsm.setServices(services);
		wsmSession.insertWebServicesManager(wsm, tokenSession.findByToken(token));
		return wsm;
	}

	@Path("/edit")
	@POST
	public WebServicesManager insertWebServiceManager(@HeaderParam("Authorization") String token,
			@FormParam("services") String services, @FormParam("function") String function,
			@FormParam("path") String path, @FormParam("isPublic") boolean isPublic, @FormParam("id") int id) {
		WebServicesManager wsm = wsmSession.findById(id);
		wsm.setFunction(function);
		wsm.setIsPublic(isPublic);
		wsm.setPath(path);
		wsm.setServices(services);
		wsmSession.updateWebServicesManager(wsm, tokenSession.findByToken(token));
		return wsm;
	}

	@Path("/delete/{id}")
	@GET
	public WebServicesManager deleteWebServicesManager(@HeaderParam("Authorization") String token,
			@PathParam("id") int id) {
		WebServicesManager wsm = wsmSession.deleteWebServicesManager(id, tokenSession.findByToken(token));
		return wsm;
	}
}
