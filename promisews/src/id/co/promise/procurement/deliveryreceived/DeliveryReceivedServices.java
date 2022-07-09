package id.co.promise.procurement.deliveryreceived;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import erp.interfacing.entity.DataReceiptListERPRequest;
import erp.interfacing.entity.PostDeliveryReceiptERPRequest;
import erp.service.DeliveryReceiptInterfacingService;
import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.DeliveryReceivedDetail;
import id.co.promise.procurement.entity.DeliveryReceivedFiles;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.jms.BillingRequestJMSSession;
import id.co.promise.procurement.master.DeliveryReceivedDetailSession;
import id.co.promise.procurement.master.DeliveryReceivedFilesSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.purchaseorder.DeliveryReceivedDetailDTO;
import id.co.promise.procurement.purchaseorder.DeliveryReceivedListPagination;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.vendor.VendorSession;

/**
 * @author F.H.K
 *
 */

@Stateless
@Path(value = "/procurement/deliveryreceived/DeliveryReceivedServices")
@TransactionManagement(TransactionManagementType.BEAN)
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryReceivedServices {

	final static Logger log = Logger.getLogger(DeliveryReceivedServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();

	@EJB
	private DeliveryReceivedSession deliveryReceivedSession;
	@EJB
	private DeliveryReceivedDetailSession deliveryReceivedDetailSession;
	@EJB
	private DeliveryReceivedFilesSession deliveryReceivedFilesSession;
	@EJB
	private DeliveryReceivedLogSession deliveryReceivedLogSession;
	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;
	@EJB
	private PurchaseOrderSession purchaseOrderSession;
	@EJB
	private ItemSession itemSession;
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;
	@EJB
	TokenSession tokenSession;
	@EJB
	BillingRequestJMSSession billingRequestJMSSession;
	@EJB
	RoleUserSession roleUserSession;
	@EJB
	ProcedureSession procedureSession;
	@EJB
	DeliveryReceiptInterfacingService deliveryReceiptInterfacingService;
	@EJB
	OrganisasiSession organisasiSession;
	@EJB
	private VendorSession vendorSession;
	@EJB
	private EmailNotificationSession emailNotificationSession;
	
	@Resource
	private UserTransaction userTransaction;

	/*
	 * @Path("/findAll")
	 * 
	 * @POST public Response findAll() {
	 * 
	 * String sortKeyWord = null; DeliveryReceived dr = new DeliveryReceived();
	 * dr.setIsDelete(Constant.ZERO_VALUE);
	 * 
	 * List<DeliveryReceived> drList =
	 * deliveryReceivedSession.getDeliveryReceivedByCriteria(dr, sortKeyWord); if
	 * (drList != null && drList.size() > 0) { DeliveryReceivedLog receivedLog = new
	 * DeliveryReceivedLog(); List<DeliveryReceivedLog> receivedLogList =
	 * deliveryReceivedLogSession.getDeliveryReceivedLogByCriteria(receivedLog);
	 * Integer index = 0; for (DeliveryReceived received : drList) { Double passNew
	 * = 0D; Double failedNew = 0D; for (DeliveryReceivedLog reLog :
	 * receivedLogList) { if (received.getId().intValue() ==
	 * reLog.getDeliveryReceived().getId().intValue()) { passNew += reLog.getPass();
	 * failedNew += reLog.getFailed(); } } drList.get(index).setPassLog(passNew);
	 * drList.get(index).setFailedLog(failedNew); index++; } return
	 * Response.ok(drList).build(); }
	 * 
	 * return Response.noContent().build(); }
	 * 
	 * @Path("/insert")
	 * 
	 * @POST public DeliveryReceived insert(@FormParam("purchaseOrder") Integer
	 * purchaseOrder, @FormParam("purchaseOrderItem") Integer
	 * purchaseOrderItem, @FormParam("pass") Double pass,
	 * 
	 * @FormParam("failed") Double failed, @FormParam("dateReceived") Date
	 * dateReceived, @FormParam("comment") String comment, @FormParam("userId")
	 * Integer userId,
	 * 
	 * @FormParam("attachRealName") String
	 * attachRealName, @FormParam("attachFileName") String
	 * attachFileName, @FormParam("attachFileSize") Long attachFileSize,
	 * 
	 * @HeaderParam("Authorization") String token) {
	 * 
	 * DeliveryReceived deliveryReceived = new DeliveryReceived();
	 * 
	 * if (purchaseOrder != null) {
	 * deliveryReceived.setPurchaseOrder(purchaseOrderSession.find(purchaseOrder));
	 * }
	 * 
	 * if (purchaseOrderItem != null) {
	 * deliveryReceived.setPurchaseOrderItem(purchaseOrderItemSession.find(
	 * purchaseOrderItem)); }
	 * 
	 * if (pass != null) { deliveryReceived.setPass(pass); }
	 * 
	 * if (failed != null) { deliveryReceived.setFailed(failed); }
	 * 
	 * if (dateReceived != null) { deliveryReceived.setDateReceived(dateReceived); }
	 * 
	 * if (comment != null) { deliveryReceived.setDescription(comment); }
	 * 
	 * if (attachRealName != null) {
	 * deliveryReceived.setAttachRealName(attachRealName); }
	 * 
	 * if (attachFileName != null) {
	 * deliveryReceived.setAttachFileName(attachFileName); }
	 * 
	 * if (attachFileSize != null) {
	 * deliveryReceived.setAttachFileSize(attachFileSize); }
	 * 
	 * if (userId != null) { deliveryReceived.setUserId(userId); }
	 * 
	 * deliveryReceivedSession.insertDeliveryReceived(deliveryReceived,
	 * tokenSession.findByToken(token));
	 * 
	 *//** when Purchase Order get finish in delivery insert **/
	/*
	 * if (deliveryReceived.getPurchaseOrder().getStatusProses().intValue() ==
	 * Constant.PO_STATUS_DONE) { FormBillingRequest formBillingRequest =
	 * createBillingMemo(deliveryReceived, token);
	 * 
	 *//** send to JMS **/
	/*
	 * Gson gson = new
	 * GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create(); String
	 * json = gson.toJson(formBillingRequest); Boolean isFinish =
	 * billingRequestJMSSession.sendMessage(json);
	 * 
	 * if (isFinish == true) { deliveryReceived.getPurchaseOrder().setSyncStatus(1);
	 * }else{ deliveryReceived.getPurchaseOrder().setSyncStatus(2); } }
	 * 
	 * return deliveryReceived; }
	 * 
	 * @Path("/update")
	 * 
	 * @POST public DeliveryReceived update(@FormParam("drId") Integer
	 * drId, @FormParam("pass") Double pass, @FormParam("failed") Double
	 * failed, @FormParam("dateReceived") Date dateReceived,
	 * 
	 * @FormParam("comment") String comment, @FormParam("userId") Integer
	 * userId, @FormParam("attachRealName") String
	 * attachRealName, @FormParam("attachFileName") String attachFileName,
	 * 
	 * @FormParam("attachFileSize") Long
	 * attachFileSize, @HeaderParam("Authorization") String token) {
	 * 
	 * DeliveryReceived deliveryReceived = deliveryReceivedSession.find(drId);
	 * 
	 * if (pass != null) { deliveryReceived.setPass(pass); }
	 * 
	 * if (failed != null) { deliveryReceived.setFailed(failed); }
	 * 
	 * if (dateReceived != null) { deliveryReceived.setDateReceived(dateReceived); }
	 * 
	 * if (comment != null) { deliveryReceived.setDescription(comment); }
	 * 
	 * if (attachRealName != null) {
	 * deliveryReceived.setAttachRealName(attachRealName); }
	 * 
	 * if (attachFileName != null) {
	 * deliveryReceived.setAttachFileName(attachFileName); }
	 * 
	 * if (attachFileSize != null) {
	 * deliveryReceived.setAttachFileSize(attachFileSize); }
	 * 
	 * if (userId != null) { deliveryReceived.setUserId(userId); }
	 * 
	 * deliveryReceivedSession.updateDeliveryReceived(deliveryReceived,
	 * tokenSession.findByToken(token));
	 * 
	 *//** when Purchase Order get finish **/
	/*
	 * if (deliveryReceived.getPurchaseOrder().getStatusProses().intValue() ==
	 * Constant.PO_STATUS_DONE) {
	 * 
	 *//** send to JMS **/
	/*
	 * FormBillingRequest formBillingRequest = createBillingMemo(deliveryReceived,
	 * token); Gson gson = new
	 * GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create(); String
	 * json = gson.toJson(formBillingRequest); Boolean isFinish =
	 * billingRequestJMSSession.sendMessage(json);
	 * 
	 * if (isFinish == true) { deliveryReceived.getPurchaseOrder().setSyncStatus(1);
	 * }else{ deliveryReceived.getPurchaseOrder().setSyncStatus(2); } }
	 * 
	 * return deliveryReceived; }
	 * 
	 * @Path("/delete/{id}")
	 * 
	 * @POST public DeliveryReceived delete(@PathParam("id") int
	 * id, @HeaderParam("Authorization") String token) { return
	 * deliveryReceivedSession.deleteDeliveryReceived(id,
	 * tokenSession.findByToken(token)); }
	 * 
	 * @Path("/deleteRow/{id}")
	 * 
	 * @POST public DeliveryReceived deleteRow(@PathParam("id") int
	 * id, @HeaderParam("Authorization") String token) { return
	 * deliveryReceivedSession.deleteRowDeliveryReceived(id,
	 * tokenSession.findByToken(token)); }
	 * 
	 * @Path("/findBy")
	 * 
	 * @POST public Response findBy(@FormParam("purchaseOrder") Integer
	 * purchaseOrder, @FormParam("purchaseOrderItem") Integer
	 * purchaseOrderItem, @FormParam("dateReceived") Date dateReceived) {
	 * 
	 * String sortKeyWord = null; DeliveryReceived dr = new DeliveryReceived(); if
	 * (purchaseOrder != null) {
	 * dr.setPurchaseOrder(purchaseOrderSession.find(purchaseOrder)); }
	 * 
	 * if (purchaseOrderItem != null) {
	 * dr.setPurchaseOrderItem(purchaseOrderItemSession.find(purchaseOrderItem)); }
	 * 
	 * if (dateReceived != null) { dr.setDateReceived(dateReceived); }
	 * 
	 * List<DeliveryReceived> drList =
	 * deliveryReceivedSession.getDeliveryReceivedByCriteria(dr, sortKeyWord); if
	 * (drList != null) { return Response.ok(drList).build(); } return
	 * Response.ok().build(); }
	 * 
	 * @Path("/getPaymentInfo")
	 * 
	 * @POST public Response getPaymentInfo(@FormParam("purchaseOrder") Integer
	 * purchaseOrder) {
	 * 
	 * String sortKeyWord = null; DeliveryReceived dr = new DeliveryReceived(); if
	 * (purchaseOrder != null) {
	 * dr.setPurchaseOrder(purchaseOrderSession.find(purchaseOrder)); }
	 * 
	 * List<DeliveryReceived> drList =
	 * deliveryReceivedSession.getDeliveryReceivedByCriteria(dr, sortKeyWord);
	 * List<DeliveryReceivedDTO> drDTOList= new ArrayList<DeliveryReceivedDTO>();
	 * 
	 * if (drList != null){
	 * 
	 * for(DeliveryReceived deliveryReceived:drList){ DeliveryReceivedDTO
	 * deliveryReceivedDTO= new DeliveryReceivedDTO();
	 * deliveryReceivedDTO.setDeliveryReceived(deliveryReceived);
	 * deliveryReceivedDTO.setTotalItem(deliveryReceivedLogSession.
	 * getTotalPassItemLog(deliveryReceived.getId()));
	 * drDTOList.add(deliveryReceivedDTO); } return Response.ok(drDTOList).build();
	 * } return Response.ok().build(); }
	 * 
	 * @POST
	 * 
	 * @Path("/readTemplateXlsDR")
	 * 
	 * @Consumes("multipart/form-data") public Response
	 * readExcell(MultipartFormDataInput input, @HeaderParam("Authorization") String
	 * token) {
	 * 
	 * Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
	 * List<InputPart> inputParts = uploadForm.get("file"); String returnMsg = null;
	 * 
	 * for (InputPart inputPart : inputParts) { MultivaluedMap<String, String>
	 * header = inputPart.getHeaders(); String fileName =
	 * TemplateXls.getFileNameTemplateXls(header); Workbook workbook = null; try {
	 * InputStream inputStream = inputPart.getBody(InputStream.class, null); if
	 * (fileName.endsWith("xlsx") || fileName.endsWith("xls")) { workbook = new
	 * XSSFWorkbook(inputStream); //analyseExcel(workbook, token); returnMsg =
	 * analyseExcel(workbook, token); }
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } }
	 * 
	 * String json = "{\"msg\":\"" + returnMsg + "\"}"; return Response.ok(json,
	 * MediaType.APPLICATION_JSON).build(); }
	 * 
	 * protected String analyseExcel(Workbook workbook, String token) { try { Sheet
	 * datatypeSheet = workbook.getSheetAt(0); Row row; for (int i = 1; i <=
	 * datatypeSheet.getLastRowNum(); i++) { row = datatypeSheet.getRow(i); Integer
	 * itemId = 0; Integer poId = 0; Integer poItemId = 0; DeliveryReceived received
	 * = new DeliveryReceived(); DeliveryReceivedLog receivedLog = new
	 * DeliveryReceivedLog(); String kodePo = null; String kodeItem = null; //cek
	 * kode PO if (row.getCell(Constant.TEMPLATE_DR_KODE_PO).toString()==null){
	 * return "ERROR!, PO Number is Empty!"; }else{ kodePo =
	 * row.getCell(Constant.TEMPLATE_DR_KODE_PO).toString(); if (kodePo.trim() ==
	 * null || kodePo.trim().isEmpty()){ return "ERROR!, PO Number is Empty!";
	 * 
	 * }else{ List<PurchaseOrder> listPO =
	 * purchaseOrderSession.getPurchaseOrderByPONum(kodePo); if (listPO != null &&
	 * listPO.size() > 0) { poId = listPO.get(0).getId(); }else{ return
	 * "ERROR!, PO with number "+kodePo+ " is Not Exist"; } } }
	 * 
	 * if(row.getCell(Constant.TEMPLATE_DR_KODE_ITEM).toString() == null){ return
	 * "ERROR!, Item Code is Empty!"; }else{ kodeItem =
	 * row.getCell(Constant.TEMPLATE_DR_KODE_ITEM).toString();
	 * 
	 * if (kodeItem.trim() == null || kodeItem.trim().isEmpty()){ return
	 * "ERROR!, Item Code is Empty!"; }else{ //cek item kode di tabel item
	 * List<Item> listItem = itemSession.getListByKode(kodeItem,token); if (listItem
	 * != null && listItem.size() > 0) {
	 * 
	 * List<PurchaseOrderItem> POItemList =
	 * purchaseOrderItemSession.getPurchaseOrderItemByPoId(poId); if (POItemList !=
	 * null && POItemList.size()>0){ //jika item id sesuai if
	 * (listItem.get(0).getId() == POItemList.get(0).getItem().getId()){ itemId =
	 * listItem.get(0).getId(); }else{ return "ERROR!, item with number "+kodeItem+
	 * " is Not Match with PO Number "+ kodePo; }
	 * 
	 * } else{ return "ERROR!, item with number "+kodeItem+ " is Not Exist"; }
	 * }else{ return "ERROR!, item with number "+kodeItem+ " is Not Exist"; }
	 * 
	 * } }
	 * 
	 * 
	 * //item pass Double pass =
	 * row.getCell(Constant.TEMPLATE_DR_PASS).getNumericCellValue();
	 * 
	 * //item failed Double failed =
	 * row.getCell(Constant.TEMPLATE_DR_FAILED).getNumericCellValue();
	 * 
	 * //item failed String comment =
	 * row.getCell(Constant.TEMPLATE_DR_COMMENT).toString();
	 * 
	 * 
	 * if (poId.intValue() != Constant.ZERO_VALUE && itemId.intValue() !=
	 * Constant.ZERO_VALUE) { List<PurchaseOrderItem> listPoItem =
	 * purchaseOrderItemSession.getPurchaseOrderItemByPoIdItemId(poId, itemId); if
	 * (listPoItem != null && listPoItem.size() > 0) { poItemId =
	 * listPoItem.get(0).getId(); } }
	 * 
	 * if (poId.intValue() != Constant.ZERO_VALUE && poItemId.intValue() !=
	 * Constant.ZERO_VALUE) { Integer passInt = pass.intValue(); Integer
	 * quantitySend =
	 * purchaseOrderItemSession.getPurchaseOrderItemByPoId(poId).get(0).
	 * getQuantitySend().intValue(); Integer receivedItem =
	 * deliveryReceivedLogSession.getSumPassedItem(poId); Integer sisa=
	 * quantitySend-receivedItem;
	 * 
	 * //jika pass dan failed yg diinput tidak sesuai dengan sisa receiveLog
	 * if((passInt+failed.intValue()) !=sisa){ return
	 * "ERROR!, pass/failed quantity is not valid!"; }
	 * 
	 * //jika pass kurang dari atau = sisa if(passInt<=sisa){
	 *//** set value for delivery receipt **/
	/*
	 * received.setPurchaseOrder(purchaseOrderSession.find(poId));
	 * received.setPurchaseOrderItem(purchaseOrderItemSession.find(poItemId));
	 * received.setPass(pass); received.setFailed(failed);
	 * received.setDescription(comment); received.setDateReceived(new Date());
	 * received.setIsDelete(Constant.ZERO_VALUE);
	 * received.setUserId(tokenSession.findByToken(token).getUser().getId());
	 * Integer drId = 0; List<DeliveryReceived> listDRExsist =
	 * deliveryReceivedSession.getDeliveryReceivedByPoIdItemId(poId, poItemId);
	 * 
	 * if (listDRExsist != null && listDRExsist.size() > 0) {
	 * received.setId(listDRExsist.get(0).getId()); drId =
	 * deliveryReceivedSession.updateDeliveryReceived(received,
	 * tokenSession.findByToken(token)).getId(); // update. } else { drId =
	 * deliveryReceivedSession.insertDeliveryReceived(received,
	 * tokenSession.findByToken(token)).getId(); // insert. }
	 * 
	 *//** when Purchase Order get finish **/
	/*
	 * if (received.getPurchaseOrder().getStatusProses().intValue() ==
	 * Constant.PO_STATUS_DONE) {
	 * 
	 *//** send to JMS **/
	/*
	 * FormBillingRequest formBillingRequest = createBillingMemo(received, token);
	 * Gson gson = new
	 * GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create(); String
	 * json = gson.toJson(formBillingRequest); Boolean isFinish =
	 * billingRequestJMSSession.sendMessage(json);
	 * 
	 * if (isFinish == true) { received.getPurchaseOrder().setSyncStatus(1); }else{
	 * received.getPurchaseOrder().setSyncStatus(2); } }
	 * 
	 *//** set value for delivery receipt log **/
	/*
	 * receivedLog.setDescription(comment); receivedLog.setDateReceived(new Date());
	 * receivedLog.setFailed(failed); receivedLog.setIsDelete(Constant.ZERO_VALUE);
	 * receivedLog.setPass(pass);
	 * receivedLog.setUserId(tokenSession.findByToken(token).getUser().getId());
	 * receivedLog.setUserId(3);
	 * receivedLog.setDeliveryReceived(deliveryReceivedSession.find(drId));
	 * deliveryReceivedLogSession.insertDeliveryReceivedLog(receivedLog,
	 * tokenSession.findByToken(token)); // insert. }else{ return
	 * "ERROR!, pass/failed quantity is not valid!"; }
	 * 
	 * } }
	 * 
	 * } catch (Exception e) { e.printStackTrace();
	 * System.out.println("ini nih errornya "+e); return
	 * "ERROR!, couldn't import delivery received"; } return
	 * " delivery receive imported successfully!"; }
	 * 
	 * public FormBillingRequest createBillingMemo(DeliveryReceived
	 * deliveryReceived, String token) {
	 * 
	 * FormBillingRequest formBillingRequest = new FormBillingRequest();
	 * 
	 *//** set billing memo values **/
	/*
	 * BillingMemoRequest billingMemoRequest = new BillingMemoRequest();
	 * billingMemoRequest.setBillMemoReqIsDeleted(0);
	 * billingMemoRequest.setBillMemoReqAfcoId(deliveryReceived.getPurchaseOrder().
	 * getPurchaseRequest().getOrganisasi().getId());
	 * 
	 * billingMemoRequest.setBillMemoReqDate(new Date());
	 * billingMemoRequest.setBillMemoReqPODate(new Date());
	 * 
	 * billingMemoRequest.setBillMemoReqFCFk(deliveryReceived.getPurchaseOrder().
	 * getKontrakFk());
	 * billingMemoRequest.setBillMemoReqPaymentPk(deliveryReceived.getPurchaseOrder(
	 * ).getTerminFk());
	 * billingMemoRequest.setBillMemoReqPOId(deliveryReceived.getPurchaseOrder().
	 * getId());
	 * billingMemoRequest.setBillMemoReqPONo(deliveryReceived.getPurchaseOrder().
	 * getPoNumber()); billingMemoRequest.setBillMemoReqStatus(0);
	 * 
	 * // //tambahan untuk billing keperluan biling memo Integer prId =
	 * deliveryReceived.getPurchaseOrder().getPurchaseRequest().getId();
	 * PurchaseRequest purchaseRequest = purchaseRequestSession.get(prId);
	 * 
	 * //account code == nomor draft String accountCode =
	 * purchaseRequest.getCostcenter();
	 * billingMemoRequest.setBillMemoAccountCode(accountCode);
	 * 
	 * //biropengadaan = departemen AlokasiAnggaran alokasiAnggaran =
	 * alokasiAnggaranSession.getAlokasiAnggaranByNomorDraft(accountCode);
	 * billingMemoRequest.setBillMemoDepartement(alokasiAnggaran.getBiroPengadaan().
	 * getNama());
	 * 
	 * //
	 * 
	 * //jika vendor null
	 * if(deliveryReceived.getPurchaseOrderItem().getVendor()==null){
	 * List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession
	 * .getPurchaseOrderItemByPoId(deliveryReceived.getId()); Vendor vendor = new
	 * Vendor(); for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
	 * if( purchaseOrderItem.getVendor()!= null ){ vendor =
	 * purchaseOrderItem.getVendor(); break; } }
	 * 
	 * //fix data for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList)
	 * { purchaseOrderItem.setItemName(purchaseOrderItem.getPurchaseRequestItem().
	 * getItemname());
	 * purchaseOrderItem.setQuantityPurchaseRequest(purchaseOrderItem.
	 * getPurchaseRequestItem().getQuantity()); purchaseOrderItem.setVendor(vendor);
	 * purchaseOrderItem.setVendorName(vendor.getNama());
	 * 
	 * purchaseOrderItemSession.updatePurchaseOrderItem(purchaseOrderItem, null); }
	 * 
	 * billingMemoRequest.setBillMemoReqVendorFk(vendor.getId());
	 * 
	 * } else { billingMemoRequest.setBillMemoReqVendorFk(deliveryReceived.
	 * getPurchaseOrderItem().getVendor().getId()); }
	 * billingMemoRequest.setBillMemoRevNotes(deliveryReceived.getPurchaseOrder().
	 * getNotes());
	 * 
	 * if(deliveryReceived.getPurchaseOrder().getMataUang() != null){
	 * billingMemoRequest.setBillMemoCurrency(deliveryReceived.getPurchaseOrder().
	 * getMataUang().getKode()); }else{
	 * billingMemoRequest.setBillMemoCurrency("IDR"); }
	 * 
	 * if (token == null) { billingMemoRequest.setBillMemoReqLoginRequester(1); }
	 * else {
	 * billingMemoRequest.setBillMemoReqLoginRequester(tokenSession.findByToken(
	 * token).getUser().getId()); }
	 * 
	 *//** get material & jasa **/
	/*
	 * List<PurchaseOrderItem> orderItemList =
	 * purchaseOrderItemSession.getPurchaseOrderItemByPoId(deliveryReceived.
	 * getPurchaseOrder().getId()); List<FinalContractItem> finalContractItemList =
	 * new ArrayList<FinalContractItem>(); List<FinalContractJasa>
	 * finalContractJasaList = new ArrayList<FinalContractJasa>(); for
	 * (PurchaseOrderItem item : orderItemList) { FinalContractItem
	 * finalContractItem = new FinalContractItem();
	 * finalContractItem.setfContractItemFCFk(deliveryReceived.getPurchaseOrder().
	 * getKontrakFk());
	 * finalContractItem.setfContractItemFPayTermFk(deliveryReceived.
	 * getPurchaseOrder().getTerminFk());
	 * finalContractItem.setfContractItemIsDeleted(0);
	 * finalContractItem.setfContractItemNm(deliveryReceived.getPurchaseOrderItem().
	 * getItemName()); finalContractItem.setfContractItemPk(null);
	 * finalContractItem.setfContractItemProcGrpFk(1);
	 *//** set untuk tipe pajak barang **/
	/*
	 * finalContractItem.setfContractItemQty(item.getQuantitySend());
	 * finalContractItem.setfContractItemRemain(item.getQuantitySend());
	 * finalContractItem.setfContractItemTotalPrice(item.getTotalUnitPrices());
	 * finalContractItem.setfContractItemUnitPrice(item.getUnitPrice());
	 * finalContractItemList.add(finalContractItem); }
	 * 
	 * formBillingRequest.setBillingMemoRequest(billingMemoRequest);
	 * formBillingRequest.setFinalContractItemList(finalContractItemList);
	 * formBillingRequest.setFinalContractJasaList(finalContractJasaList);
	 * 
	 * return formBillingRequest; }
	 * 
	 * @Path("/resyncDeliveryReceived/{id}")
	 * 
	 * @GET public FormBillingRequest resyncDeliveryReceived(@PathParam("id")
	 * Integer id, @HeaderParam("Authorization") String token) { FormBillingRequest
	 * formBillingRequest = new FormBillingRequest(); DeliveryReceived
	 * deliveryReceived = deliveryReceivedSession.find(id);
	 * 
	 * if(!deliveryReceived.getPurchaseOrder().getSyncStatus().equals(1)){
	 *//** when Purchase Order get finish in delivery insert **/
	/*
	 * if (deliveryReceived.getPurchaseOrder().getStatusProses().intValue() ==
	 * Constant.PO_STATUS_DONE) { formBillingRequest =
	 * createBillingMemo(deliveryReceived, token);
	 * 
	 *//** send to JMS **//*
							 * Gson gson = new
							 * GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create(); String
							 * json = gson.toJson(formBillingRequest); Boolean isFinish =
							 * billingRequestJMSSession.sendMessage(json);
							 * 
							 * if (isFinish == true) { deliveryReceived.getPurchaseOrder().setSyncStatus(1);
							 * }else{ deliveryReceived.getPurchaseOrder().setSyncStatus(2); } } } return
							 * formBillingRequest; }
							 */
	
	@Path("/get-list")
	@POST
	public Response getDeliveryReceivedListPagination(@FormParam("search") String search,
			@HeaderParam("Authorization") String authorization,
			@FormParam("orderKeyword") String orderKeyword, @FormParam("vendorName") String vendorName,
			@FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize) {
		
		Token token = tokenSession.findByToken(authorization);
		RoleUser roleUser = roleUserSession.getByToken(token);

		List<DeliveryReceivedListPagination> deliveryReceivedList = purchaseOrderSession.getDeliveryReceivedListPagination(search, orderKeyword, vendorName, pageNo, pageSize, roleUser);
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("jmlData", purchaseOrderSession.getDrTotalList(search, vendorName, orderKeyword, roleUser));
		map.put("dataList", deliveryReceivedList);

		
		return Response.ok(map).build();
	}
	
	@Path("/get-detail-process")
	@POST
	public Response getDeliveryReceivedDetailProcess(@FormParam("poId") Integer poId) {
		List<DeliveryReceivedDetailDTO> detailList = purchaseOrderSession.getDeliveryReceivedDetailProcessList(poId);
		List<DeliveryReceivedListPagination> detailDataList = purchaseOrderSession.getDeliveryReceivedDetailProcessData(poId);
		DeliveryReceivedListPagination detailData = detailDataList.get(0);
		Date penerimaan = new Date();
		Calendar c = Calendar.getInstance();
		if (detailData.getUpdated() != null && detailData.getSlaDeliveryTime() != null ) {
			c.setTime(detailData.getUpdated());
			c.add(Calendar.DATE, detailData.getSlaDeliveryTime());
		}
		Date estimasi = c.getTime();
		detailData.setEstimasi(estimasi);
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("detailData", detailData);
		map.put("detailList", detailList);

		
		return Response.ok(map).build();
	}
	
	@Path("/get-detail-received")
	@POST
	public Response getDeliveryReceivedDetailReceived(@FormParam("poId") Integer poId, @FormParam("drId") Integer drId) {
		
		List<DeliveryReceivedDetailDTO> detailList = purchaseOrderSession.getDeliveryReceivedDetailReceivedList(poId, drId);
		List<DeliveryReceivedListPagination> detailDataList = purchaseOrderSession.getDeliveryReceivedDetailReceivedData(poId, drId);
		DeliveryReceivedListPagination detailData = detailDataList.get(0);
		List<DeliveryReceivedFiles> detailFileList = purchaseOrderSession.getDeliveryReceivedDetailFileData(poId, drId);
		detailData.setDeliveryReceivedFiles(detailFileList);
		Calendar c = Calendar.getInstance();
		if (detailData.getUpdated() != null && detailData.getSlaDeliveryTime() != null ) {
			c.setTime(detailData.getUpdated());
			c.add(Calendar.DATE, detailData.getSlaDeliveryTime());
		}
		Date estimasi = c.getTime();
		detailData.setEstimasi(estimasi);
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("detailData", detailData);
		map.put("detailList", detailList);
		
		return Response.ok(map).build();
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(DeliveryReceivedListPagination deliveryReceivedListPagination, @HeaderParam("Authorization") String authorization, @Context UriInfo path ) throws Exception{
		userTransaction.begin();
		
		Token token = tokenSession.findByToken(authorization);
		Double totalReceiptAmount = 0D;
		Map<Object, Object> map = new HashMap<Object, Object>();
		DateFormat dateFormatInterfacing = new SimpleDateFormat("yyyy-MM-dd");
		DeliveryReceived deliveryReceived = new DeliveryReceived();
		List<DataReceiptListERPRequest> dataReceiptListERPRequestList = new ArrayList<DataReceiptListERPRequest>();
		PostDeliveryReceiptERPRequest postDeliveryReceiptERPRequest = new PostDeliveryReceiptERPRequest();
		
		PurchaseOrder purchaseOrder = purchaseOrderSession.find(deliveryReceivedListPagination.getPoId());
		PurchaseRequest purchaseRequest = purchaseOrder.getPurchaseRequest();

		//deliveryReceived
		for (Double total : deliveryReceivedListPagination.getDikirim()) {
			totalReceiptAmount = totalReceiptAmount + total;
		}
		deliveryReceived.setTotalReceiptAmount(totalReceiptAmount);
		deliveryReceived.setDateReceived(deliveryReceivedListPagination.getDateReceived());
		deliveryReceived.setPurchaseOrder(purchaseOrder);
		deliveryReceived.setUserId(token.getUser().getId());
		deliveryReceived.setRequestorUserId(token.getUser().getId());
		deliveryReceived.setDescription(deliveryReceivedListPagination.getDescription());
		deliveryReceived.setDeliveryOrderNum(deliveryReceivedListPagination.getDeliveryOrderNum());
		deliveryReceived.setIsFinish(Constant.DR_STATUS_ISFINISH_TRUE);
		deliveryReceived.setDeliveryOrderDate(deliveryReceivedListPagination.getDeliveryOrderDate());
		deliveryReceivedSession.insertDeliveryReceived(deliveryReceived, token);
		
		//deliveryReceivedDetail
		for (DeliveryReceivedDetailDTO drd : deliveryReceivedListPagination.getDeliveryReceivedDetailDTO()){
			DeliveryReceivedDetail deliveryReceivedDetail = new DeliveryReceivedDetail();
			deliveryReceivedDetail.setPoLineId(drd.getPurchaseOrderItem().getId());
			deliveryReceivedDetail.setQuantity(drd.getDikirim());
			deliveryReceivedDetail.setDeliveryReceived(deliveryReceived);
			deliveryReceivedDetail.setUserId(token.getUser().getId());
			deliveryReceivedDetailSession.insertDeliveryReceivedDetail(deliveryReceivedDetail, token);
			//listrequestinterfacing
			DataReceiptListERPRequest dataReceiptListERPRequest = new DataReceiptListERPRequest();
			dataReceiptListERPRequest.setQuantity(drd.getDikirim().toString());
			dataReceiptListERPRequest.setItemDescription(drd.getPurchaseOrderItem().getPurchaseRequestItem().getCatalog().getCatalogContractDetail().getItemDesc());
			dataReceiptListERPRequest.setItemCode(drd.getPurchaseOrderItem().getItem().getKode());
			dataReceiptListERPRequestList.add(dataReceiptListERPRequest);
		}

		//deliveryReceivedFiles
		for (DeliveryReceivedFiles drf : deliveryReceivedListPagination.getDeliveryReceivedFiles()) {
			DeliveryReceivedFiles deliveryReceivedFiles = new DeliveryReceivedFiles();
			deliveryReceivedFiles.setDeliveryReceived(deliveryReceived);
			deliveryReceivedFiles.setReceiptFileName(drf.getReceiptFileName());
			deliveryReceivedFilesSession.insertDeliveryReceivedFiles(deliveryReceivedFiles, token);
		}
		
		userTransaction.commit();
		
		Organisasi organisasiRootParent = organisasiSession.getRootParentByOrganisasi(purchaseRequest.getOrganisasi());
		
		//interfacingDeliveryReceipt
		postDeliveryReceiptERPRequest.setPoNumber(purchaseOrder.getPoNumber());
		postDeliveryReceiptERPRequest.setBoId(purchaseRequest.getId().toString());
		postDeliveryReceiptERPRequest.setReceivedDate(dateFormatInterfacing.format(deliveryReceivedListPagination.getDateReceived()));
		postDeliveryReceiptERPRequest.setExpectedReceivedDate(dateFormatInterfacing.format(deliveryReceivedListPagination.getEstimasi()));
		postDeliveryReceiptERPRequest.setOrgCode(organisasiRootParent.getCode());
		postDeliveryReceiptERPRequest.setPreparerNum(token.getUser().getKode());
		postDeliveryReceiptERPRequest.setShipmentNumber(deliveryReceivedListPagination.getDeliveryOrderNum());
		postDeliveryReceiptERPRequest.setShipmentDate(dateFormatInterfacing.format(deliveryReceivedListPagination.getDeliveryOrderDate()));
		postDeliveryReceiptERPRequest.setReceiptList(dataReceiptListERPRequestList);
		id.co.promise.procurement.entity.api.Response response = deliveryReceiptInterfacingService.postDeliveryReceiptInterfacing(postDeliveryReceiptERPRequest);
		if(response.getStatusText().equalsIgnoreCase("SUCCESS")){
			//purchaseRequest
			// KAI - 20201123
			//purchaseRequest.setStatus(Constant.PR_STATUS_PO_RECEIVED);
			purchaseRequestSession.update(purchaseRequest, token);
			//po
			purchaseOrder.setStatus("PO Received");
			purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
			//procedure
			procedureSession.execute(Constant.SYNC_IN_UP_DR_CAT_TO_CM, deliveryReceived.getId(), purchaseOrder.getPoNumber());
		}
		else {
			//purchaseRequest
			// KAI - 20201123
			//purchaseRequest.setStatus(Constant.PR_STATUS_PO_FAILED_SENDING_DR_TO_EBS);
			purchaseRequestSession.update(purchaseRequest, token);
			//po
			purchaseOrder.setStatus("Failed Sending DR To EBS");
			purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
			
		}
		map.put("response", response.getStatusText());
		return map;
	}
}
