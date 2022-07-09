package id.co.promise.procurement.deliveryreceived;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.DeliveryReceivedLog;
import id.co.promise.procurement.entity.User;
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

/**
 * @author F.H.K
 *
 */

@Stateless
@Path(value = "/procurement/deliveryreceived/DeliveryReceivedLogServices")
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryReceivedLogServices {

	final static Logger log = Logger.getLogger(DeliveryReceivedLogServices.class);
	final static CustomResponseMessage message = CustomResponse
			.getCustomResponseMessage();
	
	@EJB
	private DeliveryReceivedLogSession deliveryReceivedLogSession;
	
	@EJB
	private DeliveryReceivedSession deliveryReceivedSession;
	
	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB
	private PurchaseOrderSession purchaseOrderSession;
	
	@EJB
	private UserSession userSession;
	
	@EJB
	TokenSession tokenSession;
	
	/*
	 * @Path("/findAll")
	 * 
	 * @POST public Response findAll () {
	 * 
	 * DeliveryReceivedLog drLog = new DeliveryReceivedLog(); drLog.setIsDelete(0);;
	 * 
	 * List<DeliveryReceivedLog> drLogList =
	 * deliveryReceivedLogSession.getDeliveryReceivedLogByCriteria(drLog);
	 * if(drLogList != null&& drLogList.size() > 0) { return
	 * Response.ok(drLogList).build(); }
	 * 
	 * return Response.noContent().build(); }
	 * 
	 * @Path("/insert")
	 * 
	 * @POST public DeliveryReceivedLog insert(
	 * 
	 * @FormParam("deliveryReceived") Integer deliveryReceived,
	 * 
	 * @FormParam("pass") Double pass,
	 * 
	 * @FormParam("failed") Double failed,
	 * 
	 * @FormParam("dateReceived") Date dateReceived,
	 * 
	 * @FormParam("comment") String comment,
	 * 
	 * @FormParam("attachRealName") String attachRealName,
	 * 
	 * @FormParam("userId") Integer userId,
	 * 
	 * @HeaderParam("Authorization") String token) {
	 * 
	 * DeliveryReceivedLog deliveryReceivedLog = new DeliveryReceivedLog();
	 * 
	 * if(deliveryReceived != null) {
	 * deliveryReceivedLog.setDeliveryReceived(deliveryReceivedSession.find(
	 * deliveryReceived)); }
	 * 
	 * if(pass != null) { deliveryReceivedLog.setPass(pass); }
	 * 
	 * if(failed != null) { deliveryReceivedLog.setFailed(failed); }
	 * 
	 * if(dateReceived != null) { deliveryReceivedLog.setDateReceived(dateReceived);
	 * }
	 * 
	 * if(comment != null) { deliveryReceivedLog.setDescription(comment); }
	 * 
	 * if(attachRealName != null) {
	 * deliveryReceivedLog.setAttachRealName(attachRealName); }
	 * 
	 * if(userId != null) { deliveryReceivedLog.setUserId(userId); }
	 * 
	 * deliveryReceivedLogSession.insertDeliveryReceivedLog(deliveryReceivedLog,
	 * tokenSession.findByToken(token)); return deliveryReceivedLog; }
	 * 
	 * @Path("/update")
	 * 
	 * @POST public DeliveryReceivedLog update(
	 * 
	 * @FormParam("drLogId") Integer drLogId,
	 * 
	 * @FormParam("deliveryReceived") Integer deliveryReceived,
	 * 
	 * @FormParam("pass") Double pass,
	 * 
	 * @FormParam("failed") Double failed,
	 * 
	 * @FormParam("dateReceived") Date dateReceived,
	 * 
	 * @FormParam("comment") String comment,
	 * 
	 * @FormParam("attachRealName") String attachRealName,
	 * 
	 * @FormParam("userId") Integer userId,
	 * 
	 * @HeaderParam("Authorization") String token) {
	 * 
	 * DeliveryReceivedLog deliveryReceivedLog =
	 * deliveryReceivedLogSession.find(drLogId);
	 * 
	 * if(deliveryReceived != null) {
	 * deliveryReceivedLog.setDeliveryReceived(deliveryReceivedSession.find(
	 * deliveryReceived)); }
	 * 
	 * if(pass != null) { deliveryReceivedLog.setPass(pass); }
	 * 
	 * if(failed != null) { deliveryReceivedLog.setFailed(failed); }
	 * 
	 * if(dateReceived != null) { deliveryReceivedLog.setDateReceived(dateReceived);
	 * }
	 * 
	 * if(comment != null) { deliveryReceivedLog.setDescription(comment); }
	 * 
	 * if(attachRealName != null) {
	 * deliveryReceivedLog.setAttachRealName(attachRealName); }
	 * 
	 * if(userId != null) { deliveryReceivedLog.setUserId(userId); }
	 * 
	 * deliveryReceivedLogSession.updateDeliveryReceivedLog(deliveryReceivedLog,
	 * tokenSession.findByToken(token)); return deliveryReceivedLog; }
	 * 
	 * @Path("/delete/{id}")
	 * 
	 * @POST public DeliveryReceivedLog delete(@PathParam("id") int id,
	 * 
	 * @HeaderParam("Authorization") String token) { return
	 * deliveryReceivedLogSession.deleteDeliveryReceivedLog(id,
	 * tokenSession.findByToken(token)); }
	 * 
	 * @Path("/deleteRow/{id}")
	 * 
	 * @POST public DeliveryReceivedLog deleteRow(
	 * 
	 * @PathParam("id") int id, @HeaderParam("Authorization") String token) { return
	 * deliveryReceivedLogSession.deleteRowDeliveryReceivedLog(id,
	 * tokenSession.findByToken(token)); }
	 * 
	 * @Path("/finfDeliveryReceivedLogBy")
	 * 
	 * @POST public Response finfDeliveryReceivedLogBy(
	 * 
	 * @FormParam("purchaseOrder") Integer purchaseOrder,
	 * 
	 * @FormParam("purchaseOrderItem") Integer purchaseOrderItem,
	 * 
	 * @FormParam("startPage") Integer startPage,
	 * 
	 * @FormParam("endPage") Integer endPage) {
	 * 
	 * List<Object> hasilList = new ArrayList<Object>();
	 * 
	 * DeliveryReceived dr = new DeliveryReceived();
	 * dr.setPurchaseOrder(purchaseOrderSession.find(purchaseOrder));
	 * dr.setPurchaseOrderItem(purchaseOrderItemSession.find(purchaseOrderItem));
	 * 
	 * List<DeliveryReceived> drList =
	 * deliveryReceivedSession.getDeliveryReceivedByCriteria(dr, null); if(drList !=
	 * null && drList.size() > 0) { DeliveryReceivedLog drLog = new
	 * DeliveryReceivedLog(); drLog.setDeliveryReceived(drList.get(0));
	 * 
	 * List<DeliveryReceivedLog> dataDR =
	 * deliveryReceivedLogSession.getDeliveryReceivedLogByCriteria(drLog);
	 * List<DeliveryReceivedLog> drLogList =
	 * deliveryReceivedLogSession.getDeliveryReceivedLogByCriteriaPaging(drLog,
	 * startPage, endPage); if(drLogList != null && drLogList.size() > 0) {
	 * for(DeliveryReceivedLog drLogs : drLogList) { Map<Object, Object> map = new
	 * HashMap<Object, Object>(); User user =
	 * userSession.find(drList.get(0).getUserId()); map.put("panjangData",
	 * dataDR.size()); map.put("drLog", drLogs); map.put("user",
	 * user.getUsername()); hasilList.add(map); }
	 * 
	 * return Response.ok(hasilList).build(); } }
	 * 
	 * return Response.ok().build(); }
	 */
}
