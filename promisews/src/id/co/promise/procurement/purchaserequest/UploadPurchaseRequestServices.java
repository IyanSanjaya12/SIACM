package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.PurchaseRequestPengadaan;
import id.co.promise.procurement.entity.UploadPurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/procurement/UploadPurchaseRequestServices")
@Produces(MediaType.APPLICATION_JSON)
public class UploadPurchaseRequestServices {

	@EJB
	private UploadPurchaseRequestSession uploadPurchaseRequestSession;
	
	@EJB
	private PurchaseRequestSession purchaseRequestSession;

	@Path("/getListByPurchaseRequest/{purchaseRequestId}")
	@GET
	public List<UploadPurchaseRequest> getListByPurchaseRequest(@PathParam("purchaseRequestId") int purchaseRequestId) {

		return uploadPurchaseRequestSession.getListByPurchaseRequest(purchaseRequestId);
	}
	
/*	@Path("/getuploadpurchaserequestlist/{purchaseRequestId}")
	@POST
		public List<UploadPurchaseRequest> getUploadList(@PathParam("purchaseRequestId")int purchaseRequestId){
	    return uploadPurchaseRequestSession.getList(purchaseRequestId);
	}*/


}
