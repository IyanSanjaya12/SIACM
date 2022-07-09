package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.ShippingToPR;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.security.TokenSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(serviceName="PurchaseRequest", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaserequest.ShippingToPRWSDLImpl")
public class ShippingToPRWSDLServices implements ShippingToPRWSDLImpl {
	@EJB
	private ShippingToPRSession stpSession;
	@EJB
	private AddressBookSession addressBookSession;
	@EJB
	private PurchaseRequestItemSession priSession;
	@EJB
	private TokenSession tokenSession;
	
	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public List<ShippingToPR> findByPR(int prId){
		return stpSession.findShippingByPR(prId);
	}
	
	public ShippingToPR insert(int addressBookId, int prItemId,
			int quantity,
			String prNumber,
			String fullName,
			String addressLabel,
			String streetAddress,
			String telephone1,
			int shippingGroup,
			 String deliveryTime, String token){
		
		ShippingToPR stp = new ShippingToPR();
		if(addressBookId>0){
			stp.setAddressBook(addressBookSession.find(addressBookId));
		}		
		if(prItemId>0){
			stp.setPurchaseRequestItem(prItemId);
		}
		
		Date dDeliveryTime = null;
		try {
			dDeliveryTime = sdf.parse(deliveryTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		stp.setQuantity(new Double(quantity));
		stp.setPrNumber(prNumber);
		stp.setFullName(fullName);
		stp.setAddressLabel(addressLabel);
		stp.setStreetAddress(streetAddress);
		stp.setTelephone1(telephone1);
		stp.setShippingGroup(shippingGroup);
		stp.setDeliveryTime(dDeliveryTime);
		stpSession.inserShippingToPR(stp, tokenSession.findByToken(token));
		return stp;
	}
}
