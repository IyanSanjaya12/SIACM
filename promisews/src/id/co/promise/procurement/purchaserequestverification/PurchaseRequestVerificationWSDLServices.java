package id.co.promise.procurement.purchaserequestverification;

import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.security.TokenSession;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(serviceName="PurchaseRequestVerification", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaserequestverification.PurchaseRequestVerificationWSDLImpl")
public class PurchaseRequestVerificationWSDLServices implements PurchaseRequestVerificationWSDLImpl{
	@EJB
	private PurchaseVerificationSession pvSession;
	@EJB
	private TokenSession tokenSession;
	
	public List<PurchaseVerificationShippingDTO> findShippingByPR(int prid){
		return pvSession.findShippingByPR(prid);
	}
	
	public PurchaseRequestItem updatePurchaseRequestItem(int prItemId,
			int itemId,
			int vendorId,int isFromCatalog, 
			 String token){		
		return pvSession.updatePurchaseRequestItem(prItemId, itemId, vendorId, isFromCatalog, tokenSession.findByToken(token));
	}
	
	public PurchaseRequest updatePurchaseRequestStatus(int prId, int processId, String token){
		return pvSession.updatePurchaseRequestStatus(prId, processId, tokenSession.findByToken(token));
	}
}
