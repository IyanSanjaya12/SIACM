package id.co.promise.procurement.master;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalTahapanSession;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.ApprovalProcessVendor;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestItemSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path(value = "/procurement/master/approvalProcessVendor")
@Produces(MediaType.APPLICATION_JSON)
public class ApprovalProcessVendorService {
	@EJB
	private ApprovalProcessVendorSession approvalProcessVendorSession;

	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	
	@EJB
	private PurchaseOrderSession purchaseOrderSession;

	@EJB
	TokenSession tokenSession;
	
	@EJB
	ApprovalProcessSession approvalProcessSession;

	@EJB
	VendorSession vendorSession;
	
	@EJB
	UserSession userSession;

	@EJB
	JabatanSession jabatanSession;
	
	@EJB
	EmailNotificationSession emailNotificationSession;
	
	@EJB
	OrganisasiSession organisasiSession;
	
	@EJB
	ApprovalTahapanSession approvalTahapanSession;
	
	@EJB
	PurchaseRequestItemSession purchaseRequestItemSession;
	
	@EJB
	CatalogSession catalogSession;
	
	@Path("/getApprovalProcessVendor/{id}")
	@GET
	public ApprovalProcessVendor getItem(@PathParam("id") int id) {
		return approvalProcessVendorSession.getApprovalProcessVendor(id);
	}

	@Path("/get-list")
	@POST
	public List<ApprovalProcessVendor> getApprovalProcessVendorList(Integer bookingOrderId) {
		return approvalProcessVendorSession.getApprovalProcessVendorList(bookingOrderId);
	}

	@Path("/getStatus1List")
	@POST
	public List<ApprovalProcessVendor> getApprovalProcessVendorStatus1List(@HeaderParam("Authorization") String token, Integer bookingOrderId) {
		return approvalProcessVendorSession.getApprovalProcessVendorStatus1List(token, bookingOrderId);
	}

	@Path("/getApprovalConfirmationVendorList")
	@POST
	public List<ApprovalProcessVendor> getApprovalConfirmationVendorList(Integer valueId) {
		return approvalProcessVendorSession.getApprovalConfirmationVendorList(valueId);
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/doProcess")
	@POST
	public id.co.promise.procurement.entity.api.Response testProcess(@FormParam("status") Integer status,
			@FormParam("note") String note, @FormParam("bookingOrderId") Integer bookingOrderId, @FormParam("slaDeliveryTime") Integer slaDeliveryTime,
			@HeaderParam("Authorization") String tokenStr, @Context UriInfo path) throws SQLException, Exception {

		Token tokenObj = tokenSession.findByToken(tokenStr);
		User userLogin = tokenObj.getUser();
		Vendor vendor = vendorSession.getVendorByUserId(userLogin.getId());
		String isApprovalTahapan = Constant.LOG_SUCCESS;
		
		String statusEmail ="";
		List<ApprovalProcessVendor> approvalProcessVendorList = approvalProcessVendorSession.getApprovalProcessVendorStatus1List(tokenStr, bookingOrderId);
		ApprovalProcessVendor approvalProcessVendor = approvalProcessVendorList.get(0);
		
		PurchaseRequest purchaseRequest = purchaseRequestSession.get(bookingOrderId);
//		purchaseRequest.setSlaDeliveryTime(slaDeliveryTime);
		
		id.co.promise.procurement.entity.api.Response response = new Response<>();
		approvalProcessVendor.setId(approvalProcessVendor.getId());
		if (approvalProcessVendor != null) {
			if (status == 3) {
				String tahapan = Constant.TAHAPAN_APPROVAL_BO;
				ApprovalTahapan approvalTahapan = new ApprovalTahapan();
				Organisasi organisasi = organisasiSession.getOrganisasi(purchaseRequest.getOrganisasi().getId());
				approvalTahapan = approvalTahapanSession.getApprovalTahapanByOrganisasiAndTahapan(organisasi.getId(), tahapan);		
				
				if (approvalTahapan != null) {
					approvalProcessVendor.setStatus(Constant.PR_STATUS_PROCESSING);
					purchaseRequest.setStatus(Constant.PR_STATUS_AWAITING_USER_APPROVAL);
					approvalProcessVendor.setKeterangan(note);
					approvalProcessVendorSession.updateApprovalProcessVendor(approvalProcessVendor, tokenObj);
					purchaseRequestSession.update(purchaseRequest, tokenObj);
					approvalProcessSession.doCreateApproval(purchaseRequest.getId(), Constant.TAHAPAN_APPROVAL_BO, tokenObj, purchaseRequest.getTotalHarga());
				} else {
					isApprovalTahapan = Constant.LOG_ERORR;
					return response.error(organisasi.getNama());
				}
					
			
			} else {
				approvalProcessVendor.setStatus(Constant.PR_STATUS_REJECT);
				purchaseRequest.setStatus(Constant.PR_STATUS_VENDOR_REJECTED);
				statusEmail = "Ditolak";
				approvalProcessVendor.setKeterangan(note);
				
				/* perubahan KAI 19/01/2021 */
//				Integer tampQty = new Integer(0);
//				Double qtyDouble = new Double(0);
//				List<PurchaseRequestItem> prItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaseRequest.getId());
//				for(PurchaseRequestItem prItem : prItemList) {
//					Catalog catalogTamp = new Catalog();
//					catalogTamp = prItem.getCatalog();
//					qtyDouble = prItem.getQuantity();
//					tampQty =catalogTamp.getCurrentStock() + (Integer)qtyDouble.intValue();
//					catalogTamp.setCurrentStock(tampQty);
//					
//					catalogSession.updateCatalog(catalogTamp, tokenObj);
//				}
				
				approvalProcessVendorSession.updateApprovalProcessVendor(approvalProcessVendor, tokenObj);
				purchaseRequestSession.update(purchaseRequest, tokenObj);
				if(purchaseRequest.getRequestorUserId() != null) {
					User user = userSession.getUser(purchaseRequest.getRequestorUserId());
					emailNotificationSession.getMailPersetujuanBO(purchaseRequest, statusEmail, note, vendor.getNama(), user.getEmail());
				}
			}
			
		}

		return response.ok(isApprovalTahapan);
	}
	
	@SuppressWarnings("rawtypes")
	public id.co.promise.procurement.entity.api.Response doCreateApprovalProcessVendor(
			@HeaderParam("Authorization") String tokenStr,
			PurchaseRequest purchaseRequest, Vendor vendor) throws SQLException, Exception {
			id.co.promise.procurement.entity.api.Response response = null;
		try {
			Token tempToken = tokenSession.findByToken(tokenStr);
			User user=tempToken.getUser();
			User userByVendor =userSession.getUser(vendor.getUser());
			ApprovalProcessVendor approvalProcessVendor = new ApprovalProcessVendor();
			approvalProcessVendor.setCreated(new Date());
			approvalProcessVendor.setIsDelete(Constant.ZERO_VALUE);
			approvalProcessVendor.setUserId(user.getId());
			approvalProcessVendor.setKeterangan(null);
			approvalProcessVendor.setSequence(Constant.ONE_VALUE);
			approvalProcessVendor.setStatus(Constant.ONE_VALUE);
			approvalProcessVendor.setUser(userByVendor);
			approvalProcessVendor.setPurchaseRequestId(purchaseRequest.getId());

			//update purchaseRequest
			purchaseRequest.setStatus(Constant.PR_STATUS_AWAITING_VENDOR_APPROVAL);
			purchaseRequestSession.insert(purchaseRequest, tempToken);
			approvalProcessVendorSession.insertApprovalProcessVendor(approvalProcessVendor, tempToken);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	
}
