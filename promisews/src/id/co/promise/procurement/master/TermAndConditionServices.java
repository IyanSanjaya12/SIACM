/**
 * fdf
 */
package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.TermAndCondition;
import id.co.promise.procurement.security.TokenSession;

import java.util.Date;
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

/**
 * @author User
 *
 */
@Stateless
@Path(value = "/procurement/master/TermAndConditionServices")
@Produces(MediaType.APPLICATION_JSON)
public class TermAndConditionServices {
	
	@EJB
	private TermAndConditionSession termAndConditionSession;
	
	@EJB
	private TokenSession tokenSession;
	
	@Path("/getTermAndCondition/{id}")
	@GET
	public TermAndCondition getTermAndCondition(@PathParam("id") int id) {
		return termAndConditionSession.getTermAndCondition(id);
	}
	
	@Path("/getTermAndConditionList")
	@GET
	public List<TermAndCondition> getTermAndConditionList() {
		return termAndConditionSession.getTermAndConditionList();
	}
	
	@Path("/insertTermAndCondition")
	@POST
	public TermAndCondition insertTermAndCondition(@FormParam("termAndConditionType") String termAndConditionType,
			@HeaderParam("Authorization") String token) {
		TermAndCondition termAndCondition = new TermAndCondition();
		termAndCondition.setTermAndConditionType(termAndConditionType);
		termAndCondition.setCreated(new Date());
		termAndConditionSession.insertTermAndCondition(termAndCondition, tokenSession.findByToken(token));
		return termAndCondition;
	}
	
	@Path("/updateTermAndCondition")
	@POST
	public TermAndCondition updateTermAndCondition(@FormParam("id") int id,
		@FormParam("termAndConditionType") String termAndConditionType,
		@HeaderParam("Authorization") String token) {
			TermAndCondition termAndCondition = termAndConditionSession.find(id);
			termAndCondition.setTermAndConditionType(termAndConditionType);
			termAndCondition.setUpdated(new Date());
			termAndConditionSession.updateTermAndCondition(termAndCondition, tokenSession.findByToken(token));
			return termAndCondition;
		}
	
	@Path("/deleteTermAndCondition/{id}")
	@POST
	public TermAndCondition deleteTermAndCondition(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return termAndConditionSession.deleteTermAndCondition(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRowTermAndCondition/{id}")
	@POST
	public TermAndCondition deleteRowTermAndCondition(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return termAndConditionSession.deleteRowTermAndCondition(id, tokenSession.findByToken(token));
	}
}
