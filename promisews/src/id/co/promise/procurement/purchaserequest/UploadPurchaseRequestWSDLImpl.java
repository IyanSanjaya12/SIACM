package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.UploadPurchaseRequest;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface UploadPurchaseRequestWSDLImpl {
	public List<UploadPurchaseRequest> getListByPurchaseRequest(@WebParam(name="purchaseRequestId") int purchaseRequestId) ;
}
