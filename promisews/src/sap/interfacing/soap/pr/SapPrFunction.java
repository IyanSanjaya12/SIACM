package sap.interfacing.soap.pr;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.ParamContext;
import id.co.promise.procurement.utils.Utils;
import sap.interfacing.soap.pr.ZFMCRTPR.GTACCOUNTS;
import sap.interfacing.soap.pr.ZFMCRTPR.GTITEM;

@Stateless
@LocalBean
public class SapPrFunction {
	
	@EJB
	SyncSession syncSession;
	
	@EJB
	PurchaseRequestSession purchaseRequestSession;
	
	@EJB
	PurchaseRequestItemSession purchaseRequestItemSession;
	
	@EJB
	OrganisasiSession organisasiSession;

	private static Log logger = LogFactory.getLog(SapPrFunction.class);

	public Response submitPr(PurchaseRequest purchaseRequest, Token token) throws SQLException {

		ZFMCRTPR zFMCRTPR = new ZFMCRTPR();
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(token);
		try {
			/** processing sap **/
			SICRTPROBService sICRTPROBService = new SICRTPROBService();
			SICRTPROB sICRTPROBOut = sICRTPROBService.getHTTPPort();
			
	  	    Utils.connectionSAP(sICRTPROBOut);
	  	    
			logger.info("############# SAP PURCHASE REQUEST SUBMIT ##############");
			
						GTITEM gTITEM =  new GTITEM();
						List<ZSTPRITEM> zSTPRITEMList = gTITEM.getItem();
						
						GTACCOUNTS gTACCOUNTS =  new GTACCOUNTS();
						List<ZSTPRACC> zSTPRACCList = gTACCOUNTS.getItem();			
						
						// Item					
						
						//BigDecimal bd1 = new BigDecimal(10);
						//BigDecimal bd2 = new BigDecimal(10000);
						SimpleDateFormat sapdateFormat = new SimpleDateFormat("yyyy-MM-dd");
						/*String datePr = sapdateFormat.format(purchaseRequest.getPostdate());
						
						Integer slaDeliveryTime = purchaseRequest.getSlaDeliveryTime();
						Date currentDatePlusOne =  new Date();
						 if(slaDeliveryTime !=null) {
							 Date currentDate = new Date();
							 Calendar calender = Calendar.getInstance();
							 calender.setTime(currentDate);
							 calender.add(Calendar.DATE, slaDeliveryTime);
							currentDatePlusOne = calender.getTime();
						 }	
						 String estimated = sapdateFormat.format(currentDatePlusOne);*/
						
						List<PurchaseRequestItem> pRItemList = new ArrayList<PurchaseRequestItem>();
						pRItemList = purchaseRequestItemSession.getByPurchaseRequest(purchaseRequest);
						Integer preqItemNo = 10;
						for (PurchaseRequestItem purchaseRequestItem : pRItemList ) {
							ZSTPRITEM item = new ZSTPRITEM();
							DecimalFormat df = new DecimalFormat("00000");
							String preqItem = df.format(preqItemNo);
							item.setPREQITEM(preqItem);
							item.setACCTASSCAT(purchaseRequestItem.getAcctasscat());
							item.setPURGROUP(purchaseRequestItem.getPurGroupSap().getCode());
							item.setPREQNAME(purchaseRequestItem.getPreqName());
							item.setMATERIAL(purchaseRequestItem.getItem().getKode());/* item.setMATERIAL("50000013"); */ 
							item.setSHORTTEXT(purchaseRequestItem.getItemname());
							BigDecimal qty = new BigDecimal(purchaseRequestItem.getQuantity());
							item.setQUANTITY(qty);
							item.setUNIT(purchaseRequestItem.getItem().getSatuanId().getCode());  // item.setUNIT("ST"); *Wajib Sesuai
							
							BigDecimal price = new BigDecimal(purchaseRequestItem.getPrice());
							item.setPREQPRICE(price);
							
							
							//datePr = "2020-11-16";
							//estimated = "2020-12-31";
							String datePr = sapdateFormat.format(purchaseRequest.getPostdate());
							Integer slaDeliveryTime = purchaseRequestItem.getSlaDeliveryTime();
							Date currentDatePlusOne =  new Date();
							 if(slaDeliveryTime !=null) {
								 Date currentDate = new Date();
								 Calendar calender = Calendar.getInstance();
								 calender.setTime(currentDate);
								 calender.add(Calendar.DATE, slaDeliveryTime);
								currentDatePlusOne = calender.getTime();
							 }	
							 String estimated = sapdateFormat.format(currentDatePlusOne);
							 
							item.setPREQDATE(datePr);
							item.setDELIVDATE(estimated);
							
							item.setPLANT(organisasi.getCode()); //organisasi.getCode() *Wajib Sesuai
							item.setSTORELOC(purchaseRequestItem.getStoreLocSap().getCode());
							item.setMATLGROUP("ZOS");
							item.setCURRENCY(purchaseRequestItem.getMataUang().getKode()); // item.setCURRENCY("IDR"); 
							item.setTRACKINGNO("");
							zSTPRITEMList.add(item);
							
							ZSTPRACC accounts = new ZSTPRACC();
							accounts.setPREQITEM(preqItem);
							accounts.setQUANTITY(qty);
							accounts.setGLACCOUNT(purchaseRequestItem.getgLAccountSap().getCode());
							accounts.setBUSAREA(purchaseRequestItem.getPurchaserequest().getPurchOrg().getCode()); // organisasi.getCode() *Wajib Sesuai
							accounts.setCOSTCENTER(purchaseRequestItem.getCostCenterSap().getCode());
							accounts.setSERIALNO("");
							zSTPRACCList.add(accounts);
							
							preqItemNo += 10;
						}
						/*
						item.setPREQITEM("00010"); 
						item.setACCTASSCAT("K");
						item.setPURGROUP("PBJ");
						item.setPREQNAME("Mansar");
						item.setMATERIAL("50000013");
						item.setSHORTTEXT("Printer Laser");
						item.setQUANTITY(bd1);
						item.setUNIT("PC");
						item.setPREQPRICE(bd2);
						item.setPREQDATE("2020-11-16");
						item.setDELIVDATE("2020-12-31");
						item.setPLANT("B010");
						item.setSTORELOC("KB10");
						item.setMATLGROUP("ZOS");
						item.setCURRENCY("IDR");
						item.setTRACKINGNO("");
						*/
					
						//list acount
						/* ZSTPRACC accounts = new ZSTPRACC();
						accounts.setPREQITEM("00010");
						accounts.setQUANTITY(bd1);
						accounts.setGLACCOUNT("4112221410");
						accounts.setBUSAREA("B010");
						accounts.setCOSTCENTER("21014000");
						accounts.setSERIALNO("");
						zSTPRACCList.add(accounts); */

						
						//Header
						/* zFMCRTPR.setGVATTACHMENT("1 Bundel");
						zFMCRTPR.setGVDOCTYPE("ZPR1");
						zFMCRTPR.setGVHEADERNOTE("Pembelian ATK");
						zFMCRTPR.setGVINTERMSOF("Berdasarkan NOTA xxx");
						zFMCRTPR.setGVREQUISITIONER("Mansar");
						zFMCRTPR.setGVTESTRUN(""); */
						
						
						
						zFMCRTPR.setGVATTACHMENT(purchaseRequest.getGvAttachment());
						zFMCRTPR.setGVDOCTYPE(purchaseRequest.getGvDoctype());
						zFMCRTPR.setGVHEADERNOTE(purchaseRequest.getGvHeadnote());
						zFMCRTPR.setGVINTERMSOF(purchaseRequest.getGvIntermsoft());
						zFMCRTPR.setGVREQUISITIONER(purchaseRequest.getGvRequisitioner());
						String gvTestRun = "";
//						if (Constant.IS_DEVELOPMENT_MODE) {
//							gvTestRun = "X";
//						}
						zFMCRTPR.setGVTESTRUN(gvTestRun);
						
						zFMCRTPR.setGTITEM(gTITEM);
						zFMCRTPR.setGTACCOUNTS(gTACCOUNTS);
						
						sap.interfacing.soap.pr.ZFMCRTPRResponse zFMCRTPRResponse = sICRTPROBOut.siCRTPROB(zFMCRTPR);
						
						
						String returnString = zFMCRTPRResponse.getGVRETURN();
						String returnNumber = zFMCRTPRResponse.getGVNUMBER();
						sap.interfacing.soap.pr.ZFMCRTPRResponse.GTITEM returnItem = zFMCRTPRResponse.getGTITEM();
						sap.interfacing.soap.pr.ZFMCRTPRResponse.GTACCOUNTS returnAccount = zFMCRTPRResponse.getGTACCOUNTS();
						
						if (zFMCRTPRResponse != null) {
							if (zFMCRTPRResponse.getGVNUMBER() != null && !zFMCRTPRResponse.getGVNUMBER().equals("#       1")) {
								purchaseRequest.setPrnumber(returnNumber);
								purchaseRequest.setStatus(Constant.PR_STATUS_READY_TO_CREATE_PO);
								purchaseRequestSession.update(purchaseRequest, token);
								
							} else {
								purchaseRequest.setStatus(Constant.PR_STATUS_PROCESSING);
								purchaseRequestSession.update(purchaseRequest, token);
								syncSession.createXml(SICRTPROBService.WSDL_LOCATION.toString(), zFMCRTPR, zFMCRTPRResponse, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PR);
								logger.info("############# END SAP PURCHASE REQUEST ##############");
								return Response.error(returnString);
							}
							
						}
						
			syncSession.createXml(SICRTPROBService.WSDL_LOCATION.toString(), zFMCRTPR, zFMCRTPRResponse, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PR);
			logger.info("############# END SAP PURCHASE REQUEST ##############");
			return Response.ok("SUCCESS");		
		} catch (Exception e) {
			//e.printStackTrace();
			purchaseRequest.setStatus(Constant.PR_STATUS_PROCESSING);
			purchaseRequestSession.update(purchaseRequest, token);
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			String error = errors.toString();
			syncSession.createXml(SICRTPROBService.WSDL_LOCATION.toString(), zFMCRTPR, error, Constant.LOG_METHOD_GET, Constant.LOG_SERVICE_NAME_GET_PR);
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
