package id.co.promise.procurement.scheduler;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import org.jboss.logging.Logger;

import erp.service.BookingOrderInterfacingService;
import erp.service.DeliveryReceiptInterfacingService;
import erp.service.InvoicePaymentInterfacingService;
import erp.service.ItemInterfacingService;
import erp.service.PurchaseOrderInterfacingService;
import erp.service.PurchaseRequestInterfacingService;
import erp.service.UserInterfacingService;
import erp.service.VendorInterfacingService;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.master.CostCenterSapSession;
import id.co.promise.procurement.master.GLAccountSapSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.PurGroupSapSession;
import id.co.promise.procurement.master.StoreLocSapSession;

@Singleton
public class SchedulerPM {
	final static Logger log = Logger.getLogger(SchedulerPM.class);
	@EJB private CatalogSession catalogSession;
	
	@EJB UserInterfacingService userSession;
	
	@EJB PurchaseRequestInterfacingService purchaseRequestInterfacingService;
	
	@EJB PurchaseOrderInterfacingService purchaseOrderInterfacingService;
	
	@EJB ItemInterfacingService itemInterfacingService;
	
	@EJB VendorInterfacingService vendorInterfacingService;
	
	@EJB DeliveryReceiptInterfacingService deliveryReceiptInterfacingService;
	
	@EJB InvoicePaymentInterfacingService invoicePaymentInterfacingService;
	
	@EJB ItemSession itemSession;
	
	@EJB BookingOrderInterfacingService bookingOrderInterfacingService;
	
	@EJB 
	CostCenterSapSession costCenterSapSession;
	
	@EJB
	PurGroupSapSession purGroupSapSession;
	
	@EJB
	StoreLocSapSession storeLocSapSession;
	
	@EJB
	GLAccountSapSession gLAccountSapSession;
	
	
	/*@Lock(LockType.READ)
	@TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
	//@Schedule(hour = "5", dayOfWeek="*", persistent = false)
	@Schedule(hour = "15", minute = "00", persistent = false)
	public void syncSAP() throws Exception {
		
				log.info("################### SYNC START ######################");
				log.info("#####################################################");
				log.info("############ COST CENTER - START ################");
				costCenterSapSession.getSyncList();
				log.info("############ COST CENTER - END ################");
				log.info("#####################################################");
				log.info("############ PUR GROUP - START ################");
				purGroupSapSession.getSyncList();
				log.info("############ PUR GROUP - END ################");
				log.info("#####################################################");
				log.info("############ STORE LOC - START ################");
				storeLocSapSession.getSyncList();
				log.info("############ STORE LOC  - END ################");
				log.info("#####################################################");
				log.info("############ GL ACCOUNT - START ################");
				gLAccountSapSession.getSyncList();
				log.info("############ GL ACCOUNT - END ################");
				log.info("#####################################################");
				log.info("################### SYNC END ########################");
		
	} */
	
	
	/*@Lock(LockType.READ)
	@TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
	@Schedule(hour = "6	,20", dayOfWeek="*", persistent = false)
	public void scheduleInsertAxiqoeItem() throws Exception {
		Boolean statusOn = ParamContext.getParameterBooleanByName("STATUS_SYNC");
		if(Constant.AXIQOE_SYNC_INPROGRESS == false && statusOn){
			Constant.AXIQOE_SYNC_INPROGRESS = true;
			int startPages = 1;
			int totalPages = catalogSession.getTotalPagesAxiqoeToday();
			catalogSession.insertCatalogFromAxiqoeToPromiseToday(startPages, totalPages);
		}else{
			log.info("Scheduler OFF: StatusOn = "+statusOn +"InProgress: "+Constant.AXIQOE_SYNC_INPROGRESS  );
		}
    }
    */
	
/*	@Lock(LockType.READ)
	@TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
	//@Schedule(hour = "5", dayOfWeek="*", persistent = false)
	@Schedule(hour = "23", minute = "0", persistent = false)
	public void scheduleCatalog() throws Exception {
		
		if(!Constant.DEVELOPMENT_MODE) {
			log.info("####################################################");
			log.info("############ SCHEDULER USER - START ################");
			log.info("####################################################");
			userSession.getUserInterfacing();
			log.info("####################################################");
			log.info("############ SCHEDULER USER - END ##################");
			log.info("####################################################");
		}
		
		log.info("####################################################");
		log.info("########### SCHEDULER USER EBS - START #############");
		log.info("####################################################");
		userSession.getNIPActive();
		log.info("####################################################");
		log.info("########### SCHEDULER USER EBS - END ###############");
		log.info("####################################################");
		
		schedulerSyncItem();
		schedulerSyncVendor();
//		schedulerSyncDeliveryReceived();//Sudah tidak perlu lagi (Request Oman)
		schedulerSyncInvoicePayment();
		
		log.info("####################################################");
		log.info("####### SCHEDULER GET INCOMPLETE PR - START ########");
		log.info("####################################################");
		purchaseRequestInterfacingService.getIncompletePRStatus();
		log.info("####################################################");
		log.info("####### SCHEDULER GET INCOMPLETE PR- END ###########");
		log.info("####################################################");
		
		log.info("####################################################");
		log.info("####### SCHEDULER GET INCOMPLETE PO - START ########");
		log.info("####################################################");
		purchaseOrderInterfacingService.getIncompletePOStatus();
		log.info("####################################################");
		log.info("####### SCHEDULER GET INCOMPLETE Po- END ###########");
		log.info("####################################################");
		
//		schedulerGetExpenseAccount(); //Comment sementara untuk deploy ke production, karena CR blm boleh naik production
    }
	
	public void schedulerSyncItem() throws SQLException{
		log.info("####################################################");
		log.info("############# SCHEDULER ITEM - START ###############");
		log.info("####################################################");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
		GetMasterItemERPRequest getMasterItemERPRequest= new GetMasterItemERPRequest();
		getMasterItemERPRequest.setLastUpdateDate(today);
		getMasterItemERPRequest.setCreationDate(today);
		itemInterfacingService.getItemInterfacing(getMasterItemERPRequest);
		log.info("####################################################");
		log.info("############# SCHEDULER ITEM -  END ################");
		log.info("####################################################");
	}
	
	public void schedulerSyncVendor() throws SQLException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException{
		log.info("######################################################");
		log.info("############## SCHEDULER VENDOR - START ##############");
		log.info("######################################################");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String today= sdf.format(new Date());
		vendorInterfacingService.getVendorListActive();
		
		log.info("######################################################");
		log.info("############## SCHEDULER VENDOR - END ################");
		log.info("######################################################");
	}
	
	public void schedulerSyncDeliveryReceived() throws SQLException{
		log.info("####################################################");
		log.info("############# SCHEDULER DELIVERY RECEIVED ###########");
		log.info("####################################################");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
		GetDeliveryReceiptERPRequest getDeliveryReceiptERPRequest= new GetDeliveryReceiptERPRequest();
		getDeliveryReceiptERPRequest.setLastUpdateDate(today);
		getDeliveryReceiptERPRequest.setCreationDate(today);
		deliveryReceiptInterfacingService.getDeliveryReceipt(getDeliveryReceiptERPRequest);
		log.info("########################################################");
		log.info("############# SCHEDULER DELIVERY RECEIVED END ##########");
		log.info("########################################################");
	}
	
	public void schedulerSyncInvoicePayment() throws SQLException{
		log.info("####################################################");
		log.info("######## SCHEDULER INVOICE PAYMENT - START #########");
		log.info("####################################################");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
		GetInvoicePaymentERPRequest getInvoicePaymentERPRequest= new GetInvoicePaymentERPRequest();
		getInvoicePaymentERPRequest.setLastUpdateDate(today);
		getInvoicePaymentERPRequest.setCreationDate(today);
		invoicePaymentInterfacingService.getInvoicePayment(getInvoicePaymentERPRequest);
		log.info("########################################################");
		log.info("########### SCHEDULER INVOICE PAYMENT - END ###########");
		log.info("########################################################");
	}
	
	public void schedulerGetExpenseAccount() throws Exception{
		log.info("####################################################");
		log.info("######## SCHEDULER GET EXPENSE ACCOUNT - START #########");
		log.info("####################################################");
		List<Item> itemList = itemSession.getItemListByExpenseAccount();
		for(Item item : itemList) {
			bookingOrderInterfacingService.getExpenseAccount(item.getKode());
		}
		log.info("########################################################");
		log.info("########### SCHEDULER GET EXPENSE ACCOUNT - END ###########");
		log.info("########################################################");
	}	
	*/
	
}
