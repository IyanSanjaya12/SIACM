package id.co.promise.procurement.purchaserequestverification;

import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface PurchaseRequestVerificationWSDLImpl {
	public List<PurchaseVerificationShippingDTO> findShippingByPR(@WebParam(name="prid")int prid);
	public PurchaseRequestItem updatePurchaseRequestItem(@WebParam(name="pritemid")int prItemId,
			@WebParam(name="itemId")int itemId,
			@WebParam(name="vendorId")int vendorId,
			@WebParam(name="isFromCatalog")int isFromCatalog,
			@WebParam(name="Authorization") String token);
	public PurchaseRequest updatePurchaseRequestStatus(@WebParam(name="prId")int prId, @WebParam(name="processId")int processId, @WebParam(name="Authorization") String token);
}
