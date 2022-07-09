/**
 * fdf
 */
package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.ShippingMethod;
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
@Path(value = "/procurement/master/ShippingMethodServices")
@Produces(MediaType.APPLICATION_JSON)
public class ShippingMethodServices {
	
	@EJB
	private ShippingMethodSession shippingMethodSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getShippingMethod/{id}")
	@GET
	public ShippingMethod getShippingMethod(@PathParam("id") int id) {
		return shippingMethodSession.getShippingMethod(id);
	}
	
	@Path("getShippingMethodList")
	@GET
	public List<ShippingMethod> getShippingMetdhodList() {
		return shippingMethodSession.getShippingMethodList();
	}
	
	@Path("insertShippingMethod")
	@POST
	public ShippingMethod insertShippingMethod(@FormParam("shippingType") String shippingType,
			@FormParam("shippingFee") Double shippingFee,
			@HeaderParam("Authorization") String token) {
		ShippingMethod shippingMethod = new ShippingMethod();
		shippingMethod.setShippingType(shippingType);
		shippingMethod.setShippingFee(shippingFee);
		shippingMethod.setCreated(new Date());
		shippingMethodSession.insertShippingMethod(shippingMethod, tokenSession.findByToken(token));
		return shippingMethod;
	}
	
	@Path("/updateShippingMethod")
	@POST
	public ShippingMethod updateShippingMethod(@FormParam("id") int id,
			@FormParam("shippingType") String shippingType,
			@FormParam("shippingFee") Double shippingFee,
			@HeaderParam("Authorization") String token) {
		ShippingMethod shippingMethod = shippingMethodSession.find(id);
		shippingMethod.setShippingType(shippingType);
		shippingMethod.setShippingFee(shippingFee);
		shippingMethod.setUpdated(new Date());
		shippingMethodSession.updateShippingMethod(shippingMethod, tokenSession.findByToken(token));
		return shippingMethod;
	}
	
	@Path("/deleteShippingMethod/{id}")
	@GET
	public ShippingMethod deleteShippingMethod(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return shippingMethodSession.deleteShippingMethod(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRowShippingMethod/{id}")
	@GET
	public ShippingMethod deleteRowShippingMethod(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return shippingMethodSession.deleteRowShippingMethod(id, tokenSession.findByToken(token));
	}

}
