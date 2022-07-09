/**
 * fdf
 */
package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.PaymentMethod;
import id.co.promise.procurement.security.TokenSession;

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
@Path(value = "/procurement/master/PaymentMethodServices")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentMethodServices {
	
	@EJB
	private PaymentMethodSession paymentMethodSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getPaymentMethod/{id}")
	@GET
	public PaymentMethod getPaymentMethod(@PathParam("id") int id) {
		return paymentMethodSession.getPaymentMethod(id);
	}
	
	@Path("/getPaymentMethodList")
	@GET
	public List<PaymentMethod> getPaymentMethodList() {
		return paymentMethodSession.getPaymentMethodList();
	}
	
	@Path("/insertPaymentMethod")
	@POST
	public PaymentMethod insertPaymentMethod(@FormParam("paymentType") String paymentType,
			@FormParam("adminFee") Double adminFee,
			@HeaderParam("Authorization") String token) {
		PaymentMethod paymentMethod = new PaymentMethod();
		paymentMethod.setPaymentType(paymentType);
		paymentMethod.setAdminFee(adminFee);
		paymentMethod.setCreated(new Date());
		paymentMethodSession.insertPaymentMethod(paymentMethod, tokenSession.findByToken(token));
		return paymentMethod;
	}
	
	@Path("/updatePaymentMethod")
	@POST
	public PaymentMethod updatePaymentMethod(@FormParam("id") int id,
			@FormParam("paymentType") String paymentType,
			@FormParam("adminFee") Double adminFee,
			@HeaderParam("Authorization") String token) {
		PaymentMethod paymentMethod = paymentMethodSession.find(id);
		paymentMethod.setPaymentType(paymentType);
		paymentMethod.setAdminFee(adminFee);
		paymentMethod.setUpdated(new Date());
		paymentMethodSession.updatePaymentMethod(paymentMethod, tokenSession.findByToken(token));
		return paymentMethod;
	}
	
	@Path("/deletePaymentMethod/{id}")
	@POST
	public PaymentMethod deletePaymentMethod(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return paymentMethodSession.deletePaymentMethod(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRowPaymentMethod/{id}")
	@POST
	public PaymentMethod deleteRowPaymentMethod(@FormParam("id") int id,
			@HeaderParam("aAuthorization") String token) {
		return paymentMethodSession.deleteRowPaymentMethod(id, tokenSession.findByToken(token));
	}

}
