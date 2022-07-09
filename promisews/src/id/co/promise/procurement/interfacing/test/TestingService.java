package id.co.promise.procurement.interfacing.test;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import erp.entity.ItemInterfacingConsume;
import erp.entity.PurchaseOrderInterfacingExpose;
import erp.entity.VendorInterfacingExpose;
import erp.interfacing.entity.GetDeliveryReceiptERPRequest;
import erp.interfacing.entity.GetInvoicePaymentERPRequest;
import erp.interfacing.entity.GetMasterItemERPRequest;
import erp.service.BookingOrderInterfacingService;
import erp.service.DeliveryReceiptInterfacingService;
import erp.service.InvoicePaymentInterfacingService;
import erp.service.ItemInterfacingService;
import erp.service.PurchaseOrderInterfacingService;
import erp.service.PurchaseRequestInterfacingService;
import erp.service.UserInterfacingService;
import erp.service.VendorInterfacingService;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.CostCenterSap;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.fileupload.ForTest;
import id.co.promise.procurement.master.CostCenterSapSession;
import id.co.promise.procurement.master.CostCenterSapStagingSession;
import id.co.promise.procurement.master.DashboardSession;
import id.co.promise.procurement.master.GLAccountSapSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.PurGroupSapSession;
import id.co.promise.procurement.master.StoreLocSapSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.vendor.VendorSession;
import sap.interfacing.soap.po.SapPoFunction;
import sap.interfacing.soap.pr.SapPrFunction;
import sap.interfacing.soap.rfq.SapRfqFunction;

@Stateless
@Path(value = "/procurement/interfacing/testing-service")
@Produces(MediaType.APPLICATION_JSON)
public class TestingService {
	
	final static Logger log = Logger.getLogger(TestingService.class);
	
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	
	@EJB
	private BookingOrderInterfacingService bookingOrderInterfacingConsume ;
	
	@EJB
	private PurchaseOrderInterfacingService purchaseOrderInterfacingService;
	
	@EJB
	private PurchaseOrderSession purchaseOrderSession;
	
	@EJB
	private PurchaseRequestInterfacingService purchaseRequestInterfacingService;
	
	@EJB
	private UserInterfacingService userInterfacingService;
	

	
	@EJB
	SapRfqFunction sapRfqFunction;
	

	@EJB
	SapPrFunction sapPrFunction;
	
	@EJB
	SapPoFunction sapPoFunction;
	
	@EJB private CatalogSession catalogSession;
	
	@EJB UserInterfacingService userSession;
	
	@EJB ItemInterfacingService itemInterfacingService;
	
	@EJB VendorInterfacingService vendorInterfacingService;
	
	@EJB
	private DeliveryReceivedSession deliveryReceivedSession;
	
	@EJB
	private VendorSession vendorSession;
	
	@EJB
	private JabatanSession jabatanSession;
	
	@EJB
	private OrganisasiSession organisasiSession;
	
	@EJB
	private InvoicePaymentInterfacingService invoicePaymentInterfacingService;
	
	@EJB
	private DeliveryReceiptInterfacingService deliveryReceiptInterfacingService;
	
	@EJB
	private DashboardSession dashboardSession;
	
	@EJB
	EmailNotificationSession emailNotificationSession;
	
	@EJB 
	ItemSession itemSession;
	
	@EJB 
	CostCenterSapSession costCenterSapSession;
	
	@EJB 
	CostCenterSapStagingSession costCenterSapStagingSession;
	
	@EJB
	PurGroupSapSession purGroupSapSession;
	
	@EJB
	StoreLocSapSession storeLocSapSession;
	
	@EJB
	GLAccountSapSession gLAccountSapSession;
	
	/*
	 * @Path("/get-BO")
	 * 
	 * @POST public PurchaseRequestInterfacing test(Object bookingOrderInterfacing)
	 * { log.info(bookingOrderInterfacing); PurchaseRequestInterfacing
	 * purchaseRequestInterfacing = new PurchaseRequestInterfacing();
	 * purchaseRequestInterfacing.setREQUISITION_NUMBER("tes");
	 * purchaseRequestInterfacing.setORG_ID(1);
	 * purchaseRequestInterfacing.setBO_ID(123); DateFormat dateFormat = new
	 * SimpleDateFormat("dd-mm-yyyy hh:mm:ss"); String approveDate =
	 * dateFormat.format(new Date());
	 * purchaseRequestInterfacing.setAPPROVED_DATE(approveDate);
	 * purchaseRequestInterfacing.setDESCRIPTION("Tes Description");
	 * purchaseRequestInterfacing.setFLAG_PROCESS("Tes Flag");
	 * purchaseRequestInterfacing.setPREPARER_ID(1);
	 * purchaseRequestInterfacing.setSTATUS_PROCESS("Sukses");
	 * 
	 * ArrayList purchaseRequestLineList = new ArrayList();
	 * 
	 * PurchaseRequestLineInterfacing purchaseRequestLineInterfacing = new
	 * PurchaseRequestLineInterfacing();
	 * 
	 * purchaseRequestLineInterfacing.setITEM_DESCRIPTION("Item Des");
	 * purchaseRequestLineInterfacing.setLINE_NUMBER("lineNumber");
	 * purchaseRequestLineInterfacing.setQUANTITY(5);
	 * purchaseRequestLineInterfacing.setUNIT_PRICE(50000);
	 * purchaseRequestLineInterfacing.setUOM("KILO");
	 * purchaseRequestLineList.add(purchaseRequestLineInterfacing);
	 * purchaseRequestInterfacing.setPurchaseRequestLineList(purchaseRequestLineList
	 * );
	 * 
	 * 
	 * 
	 * 
	 * //BookingOrderInterfacing bookingOrderInterfacing = gson.fromJson(post,
	 * BookingOrderInterfacing.class); // deserializes json into target2
	 * //System.out.print(bookingOrderInterfacing); return
	 * purchaseRequestInterfacing; }
	 */
	@Path("/send-BO")
	@POST
	public Boolean test(@FormParam("id") Integer id) {
		PurchaseRequest purchaseRequest = purchaseRequestSession.find(id);
		
		try {
			bookingOrderInterfacingConsume.postBookingOrder(purchaseRequest, null);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		//BookingOrderInterfacing bookingOrderInterfacing = gson.fromJson(post, BookingOrderInterfacing.class); // deserializes json into target2
		//System.out.print(bookingOrderInterfacing);
		return true;
	}
	
	@Path("/send-PR")
	@POST
	public Boolean test1(@FormParam("id") Integer id) {
		PurchaseOrder purchaseRequest = purchaseOrderSession.find(id);
		
		try {
			purchaseOrderInterfacingService.postPurchaseOrder(purchaseRequest, 0.0, null);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		//BookingOrderInterfacing bookingOrderInterfacing = gson.fromJson(post, BookingOrderInterfacing.class); // deserializes json into target2
		//System.out.print(bookingOrderInterfacing);
		return true;
	}
	
	@Path("/get-po")
	@POST
	public Response getwe(Object purchaseRequestInterfacingSend){
		
		//PurchaseRequestInterfacingSend prInterfacingSend=(PurchaseRequestInterfacingSend) purchaseRequestInterfacingSend;
	//	log.info("Hasil : "+prInterfacingSend);
		PurchaseOrderInterfacingExpose purchaseOrderInterfacing=new PurchaseOrderInterfacingExpose();
//		Date appDate=new SimpleDateFormat("dd-mm-yyyy hh:mm:ss").parse(prInterfacingSend.getBoApproveDate());
		purchaseOrderInterfacing.setOrgCode("test");
		
		return Response.ok(purchaseOrderInterfacing);
	}
	
	@Path("/test3")
	@POST
	public Response get(Object purchaseRequestInterfacingSend){
		try {
			purchaseRequestInterfacingService.getIncompletePRStatus();
		} catch (SQLException e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
//	@Path("/get-jabatan/{childId}")
//	@GET
//	public List<Object> getListByOrganisasi(@PathParam("childId") Integer childId) {
//		List<Organisasi> organisasiList = organisasiSession.getParentListByChild(childId);
//		List<Object[]> objList = jabatanSession.getJabatanAndOrganisasiByOrganisasiIdList(organisasiList);
//		return objList.stream() 
//                .distinct() 
//                .collect(Collectors.toList());
//	}
	
	@Path("/get0")
	@POST
	public Response get0(Object purchaseRequestInterfacingSend) throws Exception{
		try {
			userSession.getUserInterfacing();
			userSession.getNIPActive();
			
		} catch (SQLException e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	@Path("/get1")
	@POST
	public Response get1(@QueryParam("lastUpdateDate") String lastUpdateDate ,@QueryParam("creationDate") String creationDate) throws Exception{
		try {
			GetMasterItemERPRequest getMasterItemERPRequest= new GetMasterItemERPRequest();
			getMasterItemERPRequest.setLastUpdateDate(lastUpdateDate);
			getMasterItemERPRequest.setCreationDate(creationDate);
			itemInterfacingService.getItemInterfacing(getMasterItemERPRequest);
			
		} catch (SQLException e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	@Path("/get2")
	@POST
	public Response get2(Object purchaseRequestInterfacingSend) throws Exception{
		try {
			purchaseRequestInterfacingService.getIncompletePRStatus();	
		} catch (SQLException e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	@Path("/get3")
	@GET
	public Response get3() throws Exception{
		try {
			schedulerSyncVendor();	
		} catch (SQLException e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	@Path("/get4")
	@POST
	public Response get4(VendorInterfacingExpose vendorInterfacingExpose) throws Exception{
		vendorInterfacingService.test(vendorInterfacingExpose);
		return Response.ok();
	}
	
	@Path("/get5/{id}")
	@GET
	public List<Map> getVendorByUserId(@PathParam("id") int id) {
		return deliveryReceivedSession.getReportByVendor(vendorSession.find(id));
	}
	
	@Path("/get6")
	@GET
	public List<Map> getDR(@QueryParam("lastUpdateDate") String lastUpdateDate ,@QueryParam("creationDate") String creationDate) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
		GetDeliveryReceiptERPRequest getDeliveryReceiptERPRequest= new GetDeliveryReceiptERPRequest();
		getDeliveryReceiptERPRequest.setLastUpdateDate(lastUpdateDate);
		getDeliveryReceiptERPRequest.setCreationDate(creationDate);
		deliveryReceiptInterfacingService.getDeliveryReceipt(getDeliveryReceiptERPRequest);
		return null;
	}
	
	@Path("/get7")
	@GET
	public List<Map> getInvoice(@QueryParam("lastUpdateDate") String lastUpdateDate ,@QueryParam("creationDate") String creationDate) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
		GetInvoicePaymentERPRequest getInvoicePaymentERPRequest= new GetInvoicePaymentERPRequest();
		getInvoicePaymentERPRequest.setLastUpdateDate(lastUpdateDate);
		getInvoicePaymentERPRequest.setCreationDate(creationDate);
		invoicePaymentInterfacingService.getInvoicePayment(getInvoicePaymentERPRequest);
		return null;
	}
	
	@Path("/get8")
	@GET
	public Response getSyncItem() throws Exception{
		try {
			schedulerSyncItem();
		} catch (SQLException e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	@Path("/get9")
	@GET
	public Response getIncompletePOStatus() throws Exception{
		try {
			purchaseOrderInterfacingService.getIncompletePOStatus();
		} catch (SQLException e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	@Path("/getMailTest")
	@GET
	public Response getMailTest() throws Exception{
		try {
			emailNotificationSession.getMailTest();
		} catch (Exception e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	@Path("/getExpenseAccount")
	@GET
	public Response getExpenseAccount() throws Exception{
		try {
			schedulerGetExpenseAccount();
		} catch (Exception e) {
			log.info(">> Failed"); 
			e.printStackTrace();
		}
		return Response.ok();
	}

	
	public void getUserInterfacingFromEOffice() throws Exception {
		userSession.getUserInterfacing();
		schedulerSyncItem();
		schedulerSyncVendor();
		
		log.info("####################################################");
		log.info("############# SCHEDULER USER #######################");
		log.info("####################################################");
		
		userSession.getNIPActive();
		
		log.info("####################################################");
		log.info("############# SCHEDULER USER END ###################");
		log.info("####################################################");
    }
	
	public void schedulerSyncItem() throws SQLException{
		log.info("####################################################");
		log.info("############# SCHEDULER ITEM #######################");
		log.info("####################################################");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
//		ItemInterfacingConsume itemInterfacingConsume= new ItemInterfacingConsume();
//		itemInterfacingConsume.setLastUpdateDate(today);
//		itemInterfacingConsume.setCreationDate(today);
		
		GetMasterItemERPRequest getMasterItemERPRequest = new GetMasterItemERPRequest();
		getMasterItemERPRequest.setLastUpdateDate(today);
		getMasterItemERPRequest.setCreationDate(today);
		itemInterfacingService.getItemInterfacing(getMasterItemERPRequest);
		ItemInterfacingConsume itemInterfacingConsume= new ItemInterfacingConsume();
		itemInterfacingConsume.setLastUpdateDate(today);
		itemInterfacingConsume.setCreationDate(today);
		log.info("########################################################");
		log.info("############# SCHEDULER ITEM END #######################");
		log.info("########################################################");
	}
	
	public void schedulerSyncVendor() throws SQLException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException{
		log.info("######################################################");
		log.info("############## SCHEDULER VENDOR ######################");
		log.info("######################################################");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today= sdf.format(new Date());
		vendorInterfacingService.getVendorListActive();
		//vendorInterfacingService.getVendorInterfacing( today, today );
		
		log.info("##########################################################");
		log.info("############## SCHEDULER VENDOR END ######################");
		log.info("##########################################################");
	}
	
	public void schedulerPrPo() throws SQLException {

		purchaseRequestInterfacingService.getIncompletePRStatus();
		purchaseOrderInterfacingService.getIncompletePOStatus();
	}
	
	@Path("/get-user")
	@POST
	public Response getUser() throws SQLException{
		
		userSession.getUserInterfacing();
	
		//List<User> userList = userSession.getList();
		//for ( User user : userList ) {
		//	ssoHelper.doInsertULogin(user.getId());
		//}
		
		//purchaseRequestInterfacingService.getIncompletePRStatus();
		//purchaseOrderInterfacingService.getIncompletePOStatus();
		
		return Response.ok();
	}
	
	public void schedulerGetExpenseAccount() throws Exception{
		log.info("####################################################");
		log.info("######## SCHEDULER GET EXPENSE ACCOUNT - START #########");
		log.info("####################################################");
		List<Item> itemList = itemSession.getItemListByExpenseAccount();
		for(Item item : itemList) {
			bookingOrderInterfacingConsume.getExpenseAccount(item.getKode());
		}
		log.info("########################################################");
		log.info("########### SCHEDULER GET EXPENSE ACCOUNT - END ###########");
		log.info("########################################################");
	}	
	
//	@Path("/getAppInfo")
//	@GET
//	public AppInfo getAppInfo() throws SQLException, JSONException{
//		AppInfo appInfo = new AppInfo();
//		try {
//			FileReader reader = new FileReader("/root/.jenkins/workspace/app-build-info.json");
//			JsonParser jsonParser = new JsonParser();
//			Object obj = jsonParser.parse(reader);
//			
//			Gson gson = new Gson();
//			appInfo = gson.fromJson(obj.toString(), AppInfo.class);
//						
//			log.info(appInfo);
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return appInfo;
//	}
	
	
	//@Path("/get-calculator")
	//@GET
	/*
	 * public Response getCalculator() throws SQLException{
	 * 
	 * URL wsdlURL = Calculator.WSDL_LOCATION; //
	 * http://www.dneonline.com/calculator.asmx?wsdl final QName SERVICE_NAME = new
	 * QName("http://tempuri.org/", "Calculator");
	 * 
	 * 
	 * Calculator ss = new Calculator(wsdlURL, SERVICE_NAME); CalculatorSoap port =
	 * ss.getCalculatorSoap();
	 * 
	 * int A = 6; int B = 2; int hasil = port.add(A, B); log.info("hasil="
	 * + hasil);
	 * 
	 * return Response.ok(); }
	 */
	
	
	
	
	@Path("/get-rfq")
	@GET
	public Response<?> getRfq() throws SQLException{
		
		Response sapForm  = sapRfqFunction.submitRfq(null,null,null);
		
		return null;
	}
	

	@Path("/get-pr")
	@GET
	public Response<?> getPr2(Integer id) throws SQLException{
		

		Response response  = sapPrFunction.submitPr(null, null);


		return response;
	}
	
	@Path("/get-cost-center-list")
	@GET
	public List<CostCenterSap> getCost() throws SQLException{
		
		List<CostCenterSap> costCenterSapStagingList  = new ArrayList<>();//costCenterSapStagingSession.getStagingList();

		List<String> tempCodeList = new ArrayList<>();
		Token token = new Token();
		
		//kalau sudah ada, di update. kalau tidak ada di, insert
		for (CostCenterSap costCenterSapStaging : costCenterSapStagingList ) {
			tempCodeList.add(costCenterSapStaging.getCode());
			CostCenterSap cekCostCenterSap = costCenterSapSession.getByCode(costCenterSapStaging.getCode());
			
			CostCenterSap costCenterSapNew = new CostCenterSap();
			
			if (cekCostCenterSap != null ) {
				costCenterSapNew.setId(cekCostCenterSap.getId());
				costCenterSapNew.setCreated(cekCostCenterSap.getCreated());
				costCenterSapNew.setCode(costCenterSapStaging.getCode());
				costCenterSapNew.setDescription(costCenterSapStaging.getDescription());
				costCenterSapSession.update(costCenterSapNew, token);
			} else {
				costCenterSapNew.setCreated(new Date());
				costCenterSapNew.setCode(costCenterSapStaging.getCode());
				costCenterSapNew.setDescription(costCenterSapStaging.getDescription());
				costCenterSapSession.insert(costCenterSapNew, null);
			}
		}
		
		//softdelete yang tidak ada di staging
		costCenterSapSession.deleteByCodeList(tempCodeList);
		return costCenterSapStagingList;
	}
	
	@Path("/get-sync-master-sap")
	@GET
	public List<CostCenterSap> getSync() throws SQLException{
		
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
		
		
		return null;
	}

@SuppressWarnings({ "unused", "rawtypes" })
	@Path("/get-po")
	@GET
	public Response<?> getPo() throws SQLException{
		PurchaseOrder purchaseOrder = purchaseOrderSession.getPurchaseOrder(43);
		Response sapForm  = sapPoFunction.submitPo(purchaseOrder, null);
		
		return sapForm;
	}
	
}
