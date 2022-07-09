package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.ShippingToPR;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface ShippingToPRWSDLImpl {
	public List<ShippingToPR> findByPR(@WebParam(name="prid")int prId);
	public ShippingToPR insert(@WebParam(name="addressBookId")int addressBookId,
			@WebParam(name="prItemId")int prItemId,
			@WebParam(name="quantity")int quantity,
			@WebParam(name="prNumber")String prNumber,
			@WebParam(name="fullName")String fullName,
			@WebParam(name="addressLabel")String addressLabel,
			@WebParam(name="streetAddress")String streetAddress,
			@WebParam(name="telephone1")String telephone1,
			@WebParam(name="shippingGroup")int shippingGroup,
			@WebParam(name="deliveryTime") String deliveryTime,
			@WebParam(name="Authorization") String token);
}
