package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.ShippingTo;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface ShippingToWSDLImpl {
	public List<ShippingTo> findShippingByPO(@WebParam(name="id")int id);
}
