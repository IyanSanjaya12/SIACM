package sap.interfacing.soap.po;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import sap.interfacing.soap.po.ZFMCRTPO.GTITEMS;
import sap.interfacing.soap.po.ZFMCRTPO.GTSCHEDULES;

@Stateless
@LocalBean
public class SapPoFunction {
	
	@EJB
	SyncSession syncSession;
	
	@EJB
	PurchaseOrderSession purchaseOrderSession;
	
	@EJB
	PurchaseOrderItemSession purchaseOrderItemSession;

	private static Log logger = LogFactory.getLog(SapPoFunction.class);

	@SuppressWarnings({ "rawtypes", "unused" })
	public Response submitPo(PurchaseOrder purchaseOrder, Token token) throws SQLException {

		//SapForm form = new SapForm();
		ZFMCRTPO zFMCRTPO = new ZFMCRTPO();
		try {
			/** processing sap **/
			SICRTPOOBService sICRTPOOBService = new SICRTPOOBService();
			SICRTPOOB sICRTPOOBOut = sICRTPOOBService.getHTTPPort();
			
	  	    Utils.connectionSAP(sICRTPOOBOut);
	  	    
			logger.info("############# SAP PURCHASE REQUEST SUBMIT ##############");
			
					GTITEMS gTITEM =  new GTITEMS();
					List<ZSTPOITEM> zSTPOITEMList = gTITEM.getItem();
					
					GTSCHEDULES gTSCHEDULES =  new GTSCHEDULES();
					List<ZSTPOSCHEDULES> zSTPOSCHEDULESList = gTSCHEDULES.getItem();			
					String poGroup = null;
					String poOrg = null;
					String vndCode = null;
					// Item	
					List<PurchaseOrderItem> poItemList = purchaseOrderItemSession.getPurchaseOrderItemByPoId(purchaseOrder.getId());
					Integer poLine = 10;
					for(PurchaseOrderItem poItem : poItemList) {
						ZSTPOITEM item = new ZSTPOITEM();
						DecimalFormat df = new DecimalFormat("00000");
						String poLineNo = df.format(poLine);
						
						item.setPOITEM(poLineNo);
						item.setNETPRICE(new BigDecimal(poItem.getUnitPrice()));
						item.setTAXCODE(poItem.getTaxCode());
						zSTPOITEMList.add(item);
						
						//list schedules
						ZSTPOSCHEDULES schedule = new ZSTPOSCHEDULES();
						schedule.setPOITEM(poLineNo);
						schedule.setSERIALNO("1");
						schedule.setQUANTITY(new BigDecimal(poItem.getQuantityPurchaseRequest()));
						schedule.setPREQNO(poItem.getPurchaseRequestItem().getPurchaserequest().getPrnumber());
						schedule.setPREQITEM(poLineNo);
						zSTPOSCHEDULESList.add(schedule);							
						gTSCHEDULES.item = zSTPOSCHEDULESList;
						
						poOrg = poItem.getPurchaseRequestItem().getPurchaserequest().getPurchOrg().getCode();
						poGroup = poItem.getPurchaseRequestItem().getPurGroupSap().getCode();
						vndCode= poItem.getPurchaseRequestItem().getVendor().getRefId();
						
						poLine+=10;
					}
					//Header
					if(poItemList.size() > 0) {
						Date date = poItemList.get(0).getPurchaseRequestItem().getCatalog().getCatalogKontrak().getTglMulailKontrak();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String dateAfterFormat= formatter.format(date);
						
						zFMCRTPO.setGVTGLKONTRAK(dateAfterFormat);//
						zFMCRTPO.setGVCURRENCY(poItemList.get(0).getPurchaseRequestItem().getMataUang().getKode());//
						
						zFMCRTPO.setGVNOMORKONTRAK( poItemList.get(0).getPurchaseRequestItem().getPurchaserequest().getPrnumber()/* "2100054101" */);						
					}
					zFMCRTPO.setGVPURCHORG(poOrg);
					zFMCRTPO.setGVPURGROUP(poGroup);
					zFMCRTPO.setGVVENDOR("RK92942");
					if(vndCode != null) {
						zFMCRTPO.setGVVENDOR(vndCode);
					}
					zFMCRTPO.setGTITEMS(gTITEM);
					zFMCRTPO.setGTSCHEDULES(gTSCHEDULES);
						
//					zSTPRACCList.add(accounts);
//					ZSTPOITEM item = new ZSTPOITEM();
//					DecimalFormat df = new DecimalFormat("00000");
//					String poLineNo = df.format(poLine);
//					
//					item.setPOITEM("00010");
//					item.setNETPRICE(new BigDecimal(100000));
//					item.setTAXCODE("V0");
//					zSTPOITEMList.add(item);
//					
//					//list schedules
//					ZSTPOSCHEDULES schedule = new ZSTPOSCHEDULES();
//					schedule.setPOITEM("00010");
//					schedule.setSERIALNO("1");
//					schedule.setQUANTITY(new BigDecimal(10));
//					schedule.setPREQNO("2100054144");
//					schedule.setPREQITEM("00010");
//					gTSCHEDULES.item = zSTPOSCHEDULESList;
//					zSTPOSCHEDULESList.add(schedule);							
//						
//						//Header
//					if(poItemList.size() > 0) {
//					Date date = poItemList.get(0).getPurchaseRequestItem().getCatalog().getCatalogKontrak().getTglMulailKontrak();
//					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//					String dateAfterFormat= formatter.format(date);
//					
//					zFMCRTPO.setGVTGLKONTRAK("2021-03-29");//
//					zFMCRTPO.setGVCURRENCY("IDR");//
//					zFMCRTPO.setGVNOMORKONTRAK("2100054144");						
//					}
//					zFMCRTPO.setGVPURCHORG("A010");
//					zFMCRTPO.setGVPURGROUP("PBJ");
//					zFMCRTPO.setGVVENDOR("RK92942");
//					zFMCRTPO.setGTITEMS(gTITEM);
//					zFMCRTPO.setGTSCHEDULES(gTSCHEDULES);
						
						
						
						ZFMCRTPOResponse zFMCRTPoResponse = sICRTPOOBOut.siCRTPOOB(zFMCRTPO);
						
						String returnString = zFMCRTPoResponse.getGVRETURN();
						String returnNumber = zFMCRTPoResponse.getGVNUMBER();
						sap.interfacing.soap.po.ZFMCRTPOResponse.GTITEMS returnItem = zFMCRTPoResponse.getGTITEMS();
						sap.interfacing.soap.po.ZFMCRTPOResponse.GTSCHEDULES returnSchedules = zFMCRTPoResponse.getGTSCHEDULES();
						
			
			  if (zFMCRTPoResponse != null) { 
				  if (zFMCRTPoResponse.getGVNUMBER() != null) {
					  purchaseOrder.setPoNumber(returnNumber);
					  purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_PO_COMPLETE);
					  purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
				  } else {
					  purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_FAILED_SENDING_PO_TO_SAP);
					  purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token);
					  syncSession.createXml(SICRTPOOBService.WSDL_LOCATION.toString(), zFMCRTPO, zFMCRTPoResponse, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PO);
					  logger.info("############# END SAP PURCHASE REQUEST ##############"); 
					  return Response.error(returnString); 
				  }
			  }
			 
						
			syncSession.createXml(SICRTPOOBService.WSDL_LOCATION.toString(), zFMCRTPO, zFMCRTPoResponse, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PO);
			logger.info("############# END SAP PURCHASE REQUEST ##############");
			return Response.ok("SUCCESS");		
		} catch (Exception e) {
			e.printStackTrace();
			purchaseOrder.setStatus(Constant.PO_STATUS_TEXT_FAILED_SENDING_PO_TO_SAP);
			purchaseOrderSession.updatePurchaseOrder(purchaseOrder, token); 
			StringWriter errors = new StringWriter(); 
			e.printStackTrace(new PrintWriter(errors)); 
			String error = errors.toString();
			syncSession.createXml(SICRTPOOBService.WSDL_LOCATION.toString(), zFMCRTPO, error, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PO);
			String message = e.getCause().toString();
			Matcher matcher = Pattern.compile("http://(.+?)/").matcher(message);
			while(matcher.find()) {
				message = message.replace(matcher.group(1), "**********");
			}
			message = message.replace("=", "= ");
			return Response.error(e.getMessage()+" Because: "+message);
		}		
	}
}
