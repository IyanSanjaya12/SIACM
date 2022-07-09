package id.co.promise.procurement.master;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import id.co.promise.procurement.entity.Threshold;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.Constant;

@Stateless
@Path(value = "/procurement/master/threshold")
@Produces(MediaType.APPLICATION_JSON)
public class ThresholdService {
	@EJB
	private ThresholdSession thresholdSession;
	@EJB
	TokenSession tokenSession;

	/* @Path("/getMataUang/{id}")
	@GET
	public MataUang getMataUang(@PathParam("id") int mataUangId) {
		return mataUangSession.getMataUang(mataUangId);
	} */

	@Path("/get-list")
	@POST
	public List<Threshold> getThresholdlist() {
		List <Threshold> thresholdList =  thresholdSession.getThresholdlist();
		return thresholdList;
	}
	
	@Path("/get-list-by-type")
	@POST
	public List<Threshold> getThresholdlistByType(String type) {
		Integer typeValue = Constant.TYPE_THRESHOLD_PUSAT_VALUE;
		if (type.equalsIgnoreCase(Constant.TYPE_THRESHOLD_CABANG)) {
			typeValue = Constant.TYPE_THRESHOLD_CABANG_VALUE;
		}
		List <Threshold> thresholdList =  thresholdSession.getThresholdlistByType(typeValue);
		
		return thresholdList;
	}
	
}
