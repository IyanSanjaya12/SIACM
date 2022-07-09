package id.co.promise.procurement.approval;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.ApprovalTahapanDetail;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/approval/approval-tahapan-detail")
@Produces(MediaType.APPLICATION_JSON)
public class ApprovalTahapanDetailService {
	
	@EJB
	private ApprovalTahapanSession approvalTahapanSession;
	
	@EJB
	private ApprovalTahapanDetailSession approvalTahapanDetailSession;
	
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/get-list")
	@POST
	public List<ApprovalTahapanDetail> getApprovalTahapanList(Integer id){
	    return approvalTahapanDetailSession.getListByApprovalTahapanId(id);
	}

}
