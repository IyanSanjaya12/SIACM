package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.UploadPurchaseRequest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService(serviceName="PurchaseRequest", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaserequest.UploadPurchaseRequestWSDLImpl")
public class UploadPurchaseRequestWSDLServices implements
		UploadPurchaseRequestWSDLImpl {
	@EJB
	private UploadPurchaseRequestSession uploadPurchaseRequestSession;

	public List<UploadPurchaseRequest> getListByPurchaseRequest(int purchaseRequestId) {

		return uploadPurchaseRequestSession.getListByPurchaseRequest(purchaseRequestId);
	}
}
