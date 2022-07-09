package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.ShippingTo;
import id.co.promise.procurement.purchaseorder.ShippingToSession;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/procurement/ShippingToServices")
@Produces(MediaType.APPLICATION_JSON)
public class ShippingToServices {
	
	@EJB private ShippingToSession shippingToSession;
	@EJB private TokenSession tokenSession;
	
	@Path("/findShippingByPO/{id}")
	@GET
	public List<ShippingTo> findShippingByPO(@PathParam("id")int id){
		List<ShippingTo> list = shippingToSession.findShippingByPO(id);
		return list;
	} 
}
