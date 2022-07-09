package sap.interfacing.soap.rfq;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;
import sap.interfacing.soap.pr.SICRTPROBService;
import sap.interfacing.soap.rfq.ZFMCRTRFQ2.GTSEQNUM;
import sap.interfacing.soap.rfq.ZFMCRTRFQ2Response.GTRETURN;

@Stateless
@LocalBean
public class SapRfqFunction {
	
	@EJB
	SyncSession syncSession;
	
	@EJB
	PurchaseOrderItemSession purchaseOrderItemSession;

	private static Log logger = LogFactory.getLog(SapRfqFunction.class);
	
	@EJB
	PurchaseOrderSession purchaseOrderSession;

	public Response submitRfq(PurchaseOrder purchaseOrder, Double grandTotal, Token token) throws SQLException {

		Response response = new Response();
		ZFMCRTRFQ2 zFMCRTRFQ2 = new ZFMCRTRFQ2();
		try {
			/** processing sap **/
			RFQCreationOutService rfqCreationOutService = new RFQCreationOutService();
			RFQCreationOut rFQCreationOut = rfqCreationOutService.getHTTPPort();
	  	    Utils.connectionSAP(rFQCreationOut);
	  	    
			logger.info("############# SAP PURCHASE ORDER SUBMIT ##############");
			
						GTSEQNUM gTSEQNUM = new GTSEQNUM();
						List<ZSTRFQSEQ> zSTRFQSEQList = gTSEQNUM.getItem();
						
						SimpleDateFormat sapdateFormat = new SimpleDateFormat("yyyy-MM-dd");
						
						//String datePo = sapdateFormat.format("2020-07-10"); //purchaseOrderAddEditDTO.getPoDate()
						String datePo = "2020-07-10";
						
						List<PurchaseOrderItem> poItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(purchaseOrder.getId());
						String vendorRefId = "";
//						Integer slaDeliveryTime = purchaseOrder.getPurchaseRequest().getSlaDeliveryTime();
//						Date currentDatePlusOne =  new Date();
//						 if(slaDeliveryTime !=null) {
//							 Date currentDate = new Date();
//							 Calendar calender = Calendar.getInstance();
//							 calender.setTime(currentDate);
//							 calender.add(Calendar.DATE, slaDeliveryTime);
//							currentDatePlusOne = calender.getTime();
//						 }	
//						 String estimated = sapdateFormat.format(currentDatePlusOne);
						 String estimated = "2020-07-13";
						
						
						Integer prLine = 10;
						for (PurchaseOrderItem poItem : poItemList) {
							
							vendorRefId = poItem.getVendor().getRefId();
							DecimalFormat df = new DecimalFormat("00000");
							String noPrLine = df.format(prLine);
							
							
							ZSTRFQSEQ zSTRFQSEQ = new ZSTRFQSEQ();
							zSTRFQSEQ.setBANFN("2100053907"); //purchaseOrderAddEditDTO.getPrNumber().toString()
							zSTRFQSEQ.setBNFPO(noPrLine); 
							BigDecimal bigDecimal = new BigDecimal(poItem.getTotalUnitPrices());
							zSTRFQSEQ.setNETPR(bigDecimal);
							zSTRFQSEQ.setEXTROW("0000000000");
							zSTRFQSEQ.setPACKNO("0000000000");
							zSTRFQSEQ.setWAERS("IDR");
							zSTRFQSEQList.add(zSTRFQSEQ);
							prLine +=prLine;
							
						}
						
						
						
						//Header
						
						zFMCRTRFQ2.setGVRFQDATE(datePo); 
						zFMCRTRFQ2.setGVQUODEADLINE(estimated);
						zFMCRTRFQ2.setGVNUMBER("2100053907"); // purchaseOrderAddEditDTO.getPrNumber().toString()
						zFMCRTRFQ2.setGVLIFNR("RK92943"); // vendorRefId
						zFMCRTRFQ2.setGVEKORG("A010"); // Kode Plan sap
						zFMCRTRFQ2.setGTSEQNUM(gTSEQNUM);
						
						/*
						ZSTRFQSEQ zSTRFQSEQ = new ZSTRFQSEQ();
						zSTRFQSEQ.setBANFN("2100053907"); //purchaseOrderAddEditDTO.getPrNumber().toString()
						zSTRFQSEQ.setBNFPO("00010"); 
						BigDecimal bigDecimal = new BigDecimal(0.0d);
						zSTRFQSEQ.setNETPR(bigDecimal);
						zSTRFQSEQ.setEXTROW("0000000000");
						zSTRFQSEQ.setPACKNO("0000000000");
						zSTRFQSEQ.setWAERS("IDR");
						zSTRFQSEQList.add(zSTRFQSEQ);
						
						//Header
						ZFMCRTRFQ2 zFMCRTRFQ2 = new ZFMCRTRFQ2();
						zFMCRTRFQ2.setGVRFQDATE("2020-07-10"); 
						zFMCRTRFQ2.setGVQUODEADLINE("2020-07-13");
						zFMCRTRFQ2.setGVNUMBER("2100053907"); // purchaseOrderAddEditDTO.getPrNumber().toString()
						zFMCRTRFQ2.setGVLIFNR("RK92943"); // vendorRefId
						zFMCRTRFQ2.setGVEKORG("A010"); // Kode Plan sap
						zFMCRTRFQ2.setGTSEQNUM(gTSEQNUM); */
						
						ZFMCRTRFQ2Response zFMCRTRFQ2Response = new ZFMCRTRFQ2Response();
						zFMCRTRFQ2Response = rFQCreationOut.rfqCreateReqSync(zFMCRTRFQ2);
						GTRETURN gTRETURN = zFMCRTRFQ2Response.getGTRETURN();
						String gvtxtreturn = zFMCRTRFQ2Response.getGVRETURNTEXT();
						String gvtxtreturn2 = zFMCRTRFQ2Response.getGVRFQNUM();
						sap.interfacing.soap.rfq.ZFMCRTRFQ2Response.GTSEQNUM gvtxtreturn3 = zFMCRTRFQ2Response.getGTSEQNUM();
						logger.info("############# END SAP PURCHASE ORDERT ##############");
						
						if ( gvtxtreturn2 != null ) {
							purchaseOrder.setPoNumber(zFMCRTRFQ2Response.getGVRFQNUM());
							purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_PO_COMPLETE);
							purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
							response.setStatusText(Constant.LOG_SUCCESS);
						} else {
							purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_FAILED_SENDING_PO_TO_SAP);
							response.setStatusText(Constant.LOG_ERORR);
							purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
						}
						// create log
						syncSession.createXml(RFQCreationOutService.WSDL_LOCATION.toString(), zFMCRTRFQ2, zFMCRTRFQ2Response, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PO);
						return Response.ok("SUCCESS");

		} catch (Exception e) {
			//KAI - 20201125
			purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_FAILED_SENDING_PO_TO_SAP);
			purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String error = errors.toString();
			syncSession.createXml(SICRTPROBService.WSDL_LOCATION.toString(), zFMCRTRFQ2, error, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PO);
			return Response.error(e);

		}
		
	}

}
