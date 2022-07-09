package id.co.promise.procurement.deliveryreceived;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.promise.procurement.entity.DeliveryReceivedInvoice;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderServices;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

@Stateless
@Path(value = "/procurement/deliveryreceived/DeliveryReceivedInvoiceServices")
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryReceivedInvoiceServices {

	final static Logger log = Logger.getLogger(DeliveryReceivedInvoiceServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	
	@EJB private DeliveryReceivedInvoiceSession deliveryReceivedInvoiceSession;
	
	@EJB private DeliveryReceivedSession deliveryReceivedSession;
	
	@EJB private PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB private PurchaseOrderSession purchaseOrderSession;
	
	@EJB private UserSession userSession;
	
	@EJB TokenSession tokenSession;
	
	@Path("/findAll")
	@POST
	public Response findAll () {
		
		DeliveryReceivedInvoice drIn = new DeliveryReceivedInvoice();
		drIn.setIsDelete(0);;
		
		List<DeliveryReceivedInvoice> drInList = deliveryReceivedInvoiceSession.getDeliveryReceivedInvoiceByCriteria(drIn);
		if(drInList != null&& drInList.size() > 0) {
			return Response.ok(drInList).build();
		}
		
		return Response.noContent().build();
	}
	
	@Path("/insert")
	@POST
	public DeliveryReceivedInvoice insert(
			@FormParam("purchaseOrder") Integer purchaseOrder,
			@FormParam("attachRealName") String attachRealName,
			@FormParam("attachFileName") String attachFileName,
			@FormParam("dateReceived") Date dateReceived,
			@FormParam("attachFileSize") Long attachFileSize,
			@FormParam("userId") Integer userId,
			@HeaderParam("Authorization") String token) {
		
		DeliveryReceivedInvoice deliveryReceivedInvoice = new DeliveryReceivedInvoice();
		
		if(purchaseOrder != null) {
			deliveryReceivedInvoice.setPurchaseOrder(purchaseOrderSession.find(purchaseOrder));
		}
		
		if(attachRealName != null) {
			deliveryReceivedInvoice.setAttachRealName(attachRealName);
		}
		
		if(attachFileName != null) {
			deliveryReceivedInvoice.setAttachFileName(attachFileName);
		}
		
		if(dateReceived != null) {
			deliveryReceivedInvoice.setDateReceived(dateReceived);
		}
		
		if(attachFileSize != null) {
			deliveryReceivedInvoice.setAttachFileSize(attachFileSize);
		}
		
		if(userId != null) {
			deliveryReceivedInvoice.setUserId(userId);
		}
		
		deliveryReceivedInvoiceSession.insertDeliveryReceivedInvoice(deliveryReceivedInvoice,tokenSession.findByToken(token));
		return deliveryReceivedInvoice;
	}
	
	@Path("/update")
	@POST
	public DeliveryReceivedInvoice update(
			@FormParam("drInId") Integer drInId,
			@FormParam("purchaseOrder") Integer purchaseOrder,
			@FormParam("attachRealName") String attachRealName,
			@FormParam("attachFileName") String attachFileName,
			@FormParam("dateReceived") Date dateReceived,
			@FormParam("attachFileSize") Long attachFileSize,
			@FormParam("userId") Integer userId,
			@HeaderParam("Authorization") String token) {
		
		DeliveryReceivedInvoice deliveryReceivedInvoice = deliveryReceivedInvoiceSession.find(drInId);
		
		if(purchaseOrder != null) {
			deliveryReceivedInvoice.setPurchaseOrder(purchaseOrderSession.find(purchaseOrder));
		}
		
		if(attachRealName != null) {
			deliveryReceivedInvoice.setAttachRealName(attachRealName);
		}
		
		if(attachFileName != null) {
			deliveryReceivedInvoice.setAttachFileName(attachFileName);
		}
		
		if(dateReceived != null) {
			deliveryReceivedInvoice.setDateReceived(dateReceived);
		}
		
		if(attachFileSize != null) {
			deliveryReceivedInvoice.setAttachFileSize(attachFileSize);
		}
		
		if(userId != null) {
			deliveryReceivedInvoice.setUserId(userId);
		}
		
		deliveryReceivedInvoiceSession.updateDeliveryReceivedInvoice(deliveryReceivedInvoice,tokenSession.findByToken(token));
		return deliveryReceivedInvoice;
	}
	
	@Path("/delete/{id}")
	@POST
	public DeliveryReceivedInvoice delete(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return deliveryReceivedInvoiceSession.deleteDeliveryReceivedInvoice(id,
				tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@POST
	public DeliveryReceivedInvoice deleteRow(
			@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return deliveryReceivedInvoiceSession.deleteRowDeliveryReceivedInvoice(id,tokenSession.findByToken(token));
	}
	
	@Path("/findBy")
	@POST
	public Response findBy(
			@FormParam("purchaseOrder") Integer purchaseOrder,
			@FormParam("dateReceived") Date dateReceived,
			@FormParam("startPage") Integer startPage,
			@FormParam("endPage") Integer endPage) {
		
		List<Object> hasilList = new ArrayList<Object>();
		
		DeliveryReceivedInvoice dr = new DeliveryReceivedInvoice();
		if(purchaseOrder != null) {
			dr.setPurchaseOrder(purchaseOrderSession.find(purchaseOrder));
		}
		
		if(dateReceived != null) {
			dr.setDateReceived(dateReceived);
		}
		
		List<DeliveryReceivedInvoice> panjangData = deliveryReceivedInvoiceSession.getDeliveryReceivedInvoiceByCriteria(dr);
		List<DeliveryReceivedInvoice> drList = deliveryReceivedInvoiceSession.getDeliveryReceivedInvoiceByCriteriaPaging(dr, startPage, endPage);
		
		if(drList != null && drList.size() > 0) {
			for(DeliveryReceivedInvoice drIn : drList) {
				Map<Object, Object> map = new HashMap<Object, Object>();
				
				map.put("jmlData", panjangData.size());
				map.put("invoice", drIn);
				
				hasilList.add(map);
			}
		}
		
		return Response.ok(hasilList).build();
	}
}
