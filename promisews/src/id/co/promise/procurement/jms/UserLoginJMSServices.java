package id.co.promise.procurement.jms;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value = "/procurement/userLoginSenderJMSServices")
@Produces(MediaType.APPLICATION_JSON)
public class UserLoginJMSServices {
	@EJB
	private UserLoginJMSSession userLoginSenderJMSSession;
	
	@Path("/sender/{message}")
	@GET
	public String sendMessage(@PathParam("message")String message){
		userLoginSenderJMSSession.sendMessage(message);
		return new String("Message sending : "+message);
	}
	@Path("/consumer")
	@GET
	public String getMessage(){
		return userLoginSenderJMSSession.getMessage();
	}
}
