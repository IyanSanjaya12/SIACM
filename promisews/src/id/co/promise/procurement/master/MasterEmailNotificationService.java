package id.co.promise.procurement.master;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import id.co.promise.procurement.entity.MasterEmailNotification;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value="/procurement/master/email-notification")
@Produces(MediaType.APPLICATION_JSON)
public class MasterEmailNotificationService {

	@EJB
	private MasterEmailNotificationSession masterEmailNotificationSession;
	@EJB
	private TahapanSession tahapanSession;
	@EJB
	private RoleSession roleSession;
	@EJB
	TokenSession tokenSession;
	
	/*@Path("/get-master-email-notification/{id}")
	@GET
	public MasterEmailNotification getMasterEmailNotification(@PathParam("id")int id){
		return masterEmailNotificationSession.getMasterEmailNotification(id);
	}*/
	
	@Path("/get-list")
	@GET
	public List<MasterEmailNotification> getMasterEmailNotificationlist(){
		return masterEmailNotificationSession.getMasterEmailNotificationlist();
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(MasterEmailNotification masterEmailNotification,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
	  
		Boolean isSaveNama = masterEmailNotificationSession.checkNamaMasterEmailNotification(masterEmailNotification.getNama(), toDo, masterEmailNotification.getId());
	  
		if(!isSaveNama) {
			String errorNama = "Nama Email Notification Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
	  
		if(isSaveNama) {
			masterEmailNotification.setCreated(new Date());
			masterEmailNotification.setUserId(0);
			masterEmailNotificationSession.insertMasterEmailNotification(masterEmailNotification, tokenSession.findByToken(token));
		}	
	  
		map.put("masterEmailNotification",masterEmailNotification);
		return map;
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(MasterEmailNotification masterEmailNotification,
			@HeaderParam("Authorization") String token) {
	  
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
	  
		Boolean isSaveNama = masterEmailNotificationSession.checkNamaMasterEmailNotification(masterEmailNotification.getNama(), toDo, masterEmailNotification.getId());
	  
		if(!isSaveNama) {
			String errorNama = "Nama Email Notification Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
	  
		if(isSaveNama) {
			masterEmailNotification.setUpdated(new Date());
			masterEmailNotification.setUserId(0);
			masterEmailNotificationSession.updateMasterEmailNotification(masterEmailNotification, tokenSession.findByToken(token));
		}
	  
		map.put("masterEmailNotification",masterEmailNotification);
		return map;
	}
	

	@Path("/delete")
	@POST
	public MasterEmailNotification deleteMasterEmailNotification(@FormParam("id")int id,
			@HeaderParam("Authorization") String token){
		return masterEmailNotificationSession.deleteMasterEmailNotification(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public MasterEmailNotification deleteRowMasterEmailNotification(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return masterEmailNotificationSession.deleteRowMasterEmailNotification(id, tokenSession.findByToken(token));
	}
}
