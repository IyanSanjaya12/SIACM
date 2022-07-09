package id.co.promise.procurement.approval;

import id.co.promise.procurement.entity.ApprovalLevel;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

@Stateless
@Path(value = "/procurement/approval/approvalLevelServices")
@Produces(MediaType.APPLICATION_JSON)
public class ApprovalLevelServices {
	
	final static Logger log = Logger.getLogger(ApprovalLevelServices.class); 
	
	@EJB
	private ApprovalSession approvalSession;
	@EJB
	private ApprovalLevelSession approvalLevelSession;
 	
	@Path("/findByApprovalId/{id}")
	@GET
	public List<ApprovalLevel> findByApprovalId(@PathParam("id") Integer id) {
		 return approvalLevelSession.findByApprovalId(id);
	}

	 
}
