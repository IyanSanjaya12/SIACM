package id.co.promise.procurement.email;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.MasterSMTPServer;

@Stateless
@Path(value = "/procurement/email/master-smtp-server")
@Produces(MediaType.APPLICATION_JSON)
public class MasterSMTPServerService {
	
	@EJB
	MasterSMTPServerSession masterSMTPSession;
	
	@Path("/insert")
	@POST
	public MasterSMTPServer insert( MasterSMTPServer smtpServer, 
			@HeaderParam("Authorization") String token){
		masterSMTPSession.create(smtpServer);
		return smtpServer;
	}
	
	@Path("/update")
	@POST
	public MasterSMTPServer update( MasterSMTPServer smtpServer,
			@HeaderParam("Authorization") String token){
		masterSMTPSession.edit(smtpServer);
		return smtpServer;
	}

	/*@Path("/create")
	@POST
	public MasterSMTPServer createMasterSMTPServer(
			@FormParam("host") String host,
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("port") int port, @FormParam("ssl") int sslEnable,
			@FormParam("emailFrom") String emailFrom) {
		MasterSMTPServer mss = new MasterSMTPServer();
		mss.setPassword(password);
		mss.setUsername(username);
		mss.setPort(port);
		mss.setHost(host);
		mss.setSsl(sslEnable == 1 ? true : false);
		mss.setEmailFrom(emailFrom);
		mss.setCreated(new Date());
		masterSMTPSession.create(mss);
		return mss;
	}

	@Path("/edit")
	@POST
	public MasterSMTPServer editMasterSMTPServer(@FormParam("id") int id,
			@FormParam("host") String host,
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("port") int port, @FormParam("ssl") int sslEnable,
			@FormParam("emailFrom") String emailFrom) {
		MasterSMTPServer mss = masterSMTPSession.find(id);
		mss.setId(id);
		mss.setPassword(password);
		mss.setUsername(username);
		mss.setPort(port);
		mss.setHost(host);
		mss.setSsl(sslEnable == 1 ? true : false);
		mss.setEmailFrom(emailFrom);
		mss.setUpdated(new Date());
		masterSMTPSession.edit(mss);
		return mss;
	}*/
	
	@Path("/get-by-id/{id}")
	@GET
	public MasterSMTPServer getById(@PathParam("id")int id){
		return masterSMTPSession.find(id);
	}
	
	@Path("/get-list")
	@GET
	public List<MasterSMTPServer> getList(){
		return masterSMTPSession.getMasterSMTPServerList();
	}
	
}
