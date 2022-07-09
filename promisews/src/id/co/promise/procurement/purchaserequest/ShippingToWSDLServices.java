package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.ShippingTo;
import id.co.promise.procurement.purchaseorder.ShippingToSession;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(serviceName="PurchaseRequest", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaserequest.ShippingToWSDLImpl")
public class ShippingToWSDLServices {
	@EJB private ShippingToSession shippingToSession;
	@EJB private TokenSession tokenSession;
	
	public List<ShippingTo> findShippingByPO(int id){
		List<ShippingTo> list = shippingToSession.findShippingByPO(id);
		return list;
	} 
}
