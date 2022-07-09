package id.co.promise.procurement.email;

import id.co.promise.procurement.entity.EmailNotificationStatus;
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
@Path(value="/procurement/email/email-notification-status")
@Produces(MediaType.APPLICATION_JSON)
public class EmailNotificationStatusService {

	@EJB
	private EmailNotificationStatusSession emailNotificationStatusSession;
	@EJB
	TokenSession tokenSession;
	
	/*@Path("/get-email-notification-status/{id}")
	@GET
	public EmailNotificationStatus getEmailNotificationStatus(@PathParam("id")int id){
		return emailNotificationStatusSession.getEmailNotificationStatus(id);
	}*/
	
	@Path("/get-list")
	@GET
	public List<EmailNotificationStatus> getEmailNotificationStatuslist(){
		return emailNotificationStatusSession.getEmailNotificationStatuslist();
	}
	
	@Path("/insert")
	@POST
	public EmailNotificationStatus insertEmailNotificationStatus(
			@FormParam("statusAktif")Integer statusAktif,
			@HeaderParam("Authorization") String token){
		EmailNotificationStatus ens = new EmailNotificationStatus();
		switch (statusAktif) {
		case 0:
			ens.setStatusAktif(false);
			break;
		case 1:
			ens.setStatusAktif(true);
			break;
		default:
			ens.setStatusAktif(false);
			break;
		}
		ens.setUserId(0);
		emailNotificationStatusSession.insertEmailNotificationStatus(ens, tokenSession.findByToken(token));
		return ens;
	}
	
	@Path("/update")
	@POST
	public EmailNotificationStatus updateEmailNotificationStatus(
			@FormParam("id")Integer id,
			@FormParam("statusAktif")Integer statusAktif,
			@HeaderParam("Authorization") String token){
		EmailNotificationStatus ens = emailNotificationStatusSession.find(id);
		switch (statusAktif) {
		case 0:
			ens.setStatusAktif(false);
			break;
		case 1:
			ens.setStatusAktif(true);
			break;
		default:
			ens.setStatusAktif(false);
			break;
		}
		emailNotificationStatusSession.updateEmailNotificationStatus(ens, tokenSession.findByToken(token));
		return ens;
	}

	@Path("/delete/{id}")
	@GET
	public EmailNotificationStatus deleteEmailNotificationStatus(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return emailNotificationStatusSession.deleteEmailNotificationStatus(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public EmailNotificationStatus deleteRowEmailNotificationStatus(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return emailNotificationStatusSession.deleteRowEmailNotificationStatus(id, tokenSession.findByToken(token));
	}
}
