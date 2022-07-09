package id.co.promise.procurement.email;

import java.text.ParseException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import id.co.promise.procurement.entity.EmailNotification;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;

@Stateless
@Path(value="/procurement/email/emailNotificationServices")
@Produces(MediaType.APPLICATION_JSON)
public class EmailNotificationServices {
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	@EJB
	EmailNotificationSession emailNotificationSession;

	@Path("/emailSender")
	@GET
	public String emailSender() throws AddressException, MessagingException{
		emailNotificationSession.emailSender();
		return (new String("Execute emailSender"));
	}
	
	@Path("/emailGenerator")
	@GET
	public String emailGenerator() throws AddressException, MessagingException, ParseException{
		emailNotificationSession.getEmailGenerator();
		return (new String("Execute getEmailGenerator OK"));
	}
	
	/*penambahan modul KAI 22/12/2020 [21152]*/
	@Path("/getListEmailNotificationLog")
	@GET
	public List<EmailNotification> getListEmailNotificationLog() {
		List<EmailNotification> emailNotificationList = emailNotificationSession.getList();
		return emailNotificationList;
	}
	
	@Path("/resendEmailFromLog")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response resendEmailFromLog(EmailNotification emailNotification, @HeaderParam("Authorization") String token) throws Exception {
		try {
			emailNotificationSession.sendEmail(emailNotification.getEmailTo(), emailNotification.getSubject(), emailNotification.getMessage());
			return Response.status(201).entity(emailNotification).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
}
