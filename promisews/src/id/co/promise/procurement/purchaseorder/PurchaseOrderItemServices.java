/**
 * fdf
 */
package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.vendor.VendorSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

/**
 * @author User
 *
 */
@Stateless
@Path(value = "/procurement/purchaseorder/PurchaseOrderItemServices")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseOrderItemServices {

	final static Logger log = Logger.getLogger(PurchaseOrderItemServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	
	@EJB private PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB private PurchaseOrderSession purchaseOrderSession;
	
	@EJB private VendorSession vendorSession;

	@EJB TokenSession tokenSession;

	@Path("/getPurchaseOrderItem/{id}")
	@GET
	public PurchaseOrderItem getPurchaseOrderItem(@PathParam("id") int id) {
		return purchaseOrderItemSession.getPurchaseOrderItem(id);
	}

	@Path("/getPurchaseOrderItemList")
	@GET
	public List<PurchaseOrderItem> getPurchaseOrderItemList() {
		return purchaseOrderItemSession.getPurchaseOrderItemList();
	}

	@Path("/insertPurchaseOrderItem")
	@POST
	public PurchaseOrderItem insertPurchaseOrderItem(
			@FormParam("purchaseOrder") Integer purchaseOrder,
			@FormParam("vendorName") String vendorName,
			@FormParam("itemName") String itemName,
			@FormParam("quantityPurchaseRequest") Double quantityPurchaseRequest,
			@FormParam("quantitySend") Double quantitySend,
			@FormParam("unitPrice") Double unitPrice,
			@FormParam("totalUnitPrices") Double totalUnitPrices,
			@FormParam("subTotal") Double subTotal,
			@FormParam("deliveryTime") Date deliveryTime,
			@FormParam("status") String status,
			@HeaderParam("Authorization") String token) {
		PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
		purchaseOrderItem.setPurchaseOrder(purchaseOrderSession
				.find(purchaseOrder));
		purchaseOrderItem.setVendorName(vendorName);
		purchaseOrderItem.setItemName(itemName);
		purchaseOrderItem.setQuantityPurchaseRequest(quantityPurchaseRequest);
		purchaseOrderItem.setQuantitySend(quantitySend);
		purchaseOrderItem.setUnitPrice(totalUnitPrices);
		purchaseOrderItem.setTotalUnitPrices(totalUnitPrices);
		purchaseOrderItem.setDeliveryTime(new Date());
		purchaseOrderItem.setStatus(status);
		purchaseOrderItem.setCreated(new Date());
		purchaseOrderItemSession.insertPurchaseOrderItem(purchaseOrderItem,
				tokenSession.findByToken(token));
		return purchaseOrderItem;
	}

	@Path("/updatePurchaseOrderItem")
	@POST
	public PurchaseOrderItem updatePurchaseOrderItem(
			@PathParam("id") int id,
			@FormParam("purchaseOrder") Integer purchaseOrder,
			@FormParam("vendorName") String vendorName,
			@FormParam("itemName") String itemName,
			@FormParam("quantityPurchaseRequest") Double quantityPurchaseRequest,
			@FormParam("quantitySend") Double quantitySend,
			@FormParam("unitPrice") Double unitPrice,
			@FormParam("totalUnitPrices") Double totalUnitPrices,
			@FormParam("subTotal") Double subTotal,
			@FormParam("deliveryTime") Date deliveryTime,
			@FormParam("status") String status,
			@HeaderParam("Authorization") String token) {
		PurchaseOrderItem purchaseOrderItem = purchaseOrderItemSession.find(id);
		purchaseOrderItem.setPurchaseOrder(purchaseOrderSession
				.find(purchaseOrder));
		purchaseOrderItem.setVendorName(vendorName);
		purchaseOrderItem.setItemName(itemName);
		purchaseOrderItem.setQuantityPurchaseRequest(quantityPurchaseRequest);
		purchaseOrderItem.setQuantitySend(quantitySend);
		purchaseOrderItem.setUnitPrice(totalUnitPrices);
		purchaseOrderItem.setTotalUnitPrices(totalUnitPrices);
		purchaseOrderItem.setDeliveryTime(new Date());
		purchaseOrderItem.setStatus(status);
		purchaseOrderItem.setUpdated(new Date());
		purchaseOrderItemSession.updatePurchaseOrderItem(purchaseOrderItem,
				tokenSession.findByToken(token));
		return purchaseOrderItem;
	}

	@Path("/deletePurchaseOrderItem/{id}")
	@POST
	public PurchaseOrderItem deletePurchaseOrderItem(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return purchaseOrderItemSession.deletePurchaseOrderItem(id,
				tokenSession.findByToken(token));
	}

	@Path("/deleteRowPurchaseOrderItem/{id}")
	@POST
	public PurchaseOrderItem deleteRowPurchaseOrderItem(
			@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return purchaseOrderItemSession.deleteRowPurchaseOrderItem(id,
				tokenSession.findByToken(token));
	}
	
	@SuppressWarnings("unchecked")
	@Path("/findAllPOWithPOItem")
	@POST
	public Response findAllPOWithPOItem (
			@FormParam("pageNumber") Integer pageNumber,
			@FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("searchingKeyWord") String searchingKeyWord,
			@FormParam("sortKeyWord") String sortKeyWord,
			@FormParam("searchingVendor") String searchingVendor,
			@FormParam("searchingItem") String searchingItem,
			@HeaderParam("Authorization") String token) throws Exception {
		
		List<Object> hasilList = new ArrayList<Object>();
		List<Object> paramsList = new ArrayList<Object>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		try {
			if (pageNumber == null || numberOfRowPerPage == null || pageNumber == 0 || numberOfRowPerPage == 0) {
				pageNumber 			= 1;
				numberOfRowPerPage 	= 10;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build(); // kirim 400 Bad Request
		}

		try {
			Integer endIndex 	= pageNumber * numberOfRowPerPage;
			Integer startIndex 	= endIndex - numberOfRowPerPage + 1;
			PurchaseOrderListPagination po;
			
			po = purchaseOrderSession.getPODTOListWithRange(1, 100, searchingKeyWord, sortKeyWord,searchingVendor,searchingItem);
			
			for(PurchaseOrderDTO pos : po.getPurchaseOrderDTOList()){
				map = new HashMap<Object, Object>();
				
				List<PurchaseOrderItemDTO> poItemList = purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(pos.getId());
				map.put("po", pos);
				if(poItemList != null) {
					map.put("poitem", poItemList);
				}
				hasilList.add(map);
			}
			
			for(int i=0;i<hasilList.size();i++){
				if(numberOfRowPerPage >= hasilList.size()) {
					Map<Object, Object> page1Map = (Map<Object, Object>) hasilList.get(i);
					page1Map.put("jmlData", hasilList.size());
					paramsList.add(page1Map);
				} else {
					Integer sekarang = i + 1;
					if(sekarang >= startIndex && sekarang <= endIndex) {
						Map<Object, Object> page2Map = (Map<Object, Object>) hasilList.get(i);
						page2Map.put("jmlData", hasilList.size());
						paramsList.add(page2Map);
					}
				}
			}
			
			return Response.ok(paramsList).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build(); // kirim 500 Internal Server Error
		}
	}
	
	@SuppressWarnings("unchecked")
	@Path("/findAllPOWithPOItemByVendorName")
	@POST
	public Response findAllPOWithPOItemByVendorName (
			@FormParam("pageNumber") Integer pageNumber,
			@FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("userId") Integer userId,
			@FormParam("searchingKeyWord") String searchingKeyWord,
			@FormParam("sortKeyWord") String sortKeyWord,
			@HeaderParam("Authorization") String token) throws Exception {
		
		List<Object> hasilList = new ArrayList<Object>();
		List<Object> paramsList = new ArrayList<Object>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		try {
			if (pageNumber == null || numberOfRowPerPage == null || pageNumber == 0 || numberOfRowPerPage == 0) {
				pageNumber = 1;
				numberOfRowPerPage = 10;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build(); // kirim 400 Bad Request
		}

		try {
			Integer endIndex = pageNumber * numberOfRowPerPage;
			Integer startIndex = endIndex - numberOfRowPerPage + 1;
			PurchaseOrderListPagination po = purchaseOrderSession.getPODTOListWithRange(1, 100, searchingKeyWord, sortKeyWord,null,null);
			
			/** get vendor ID **/
			Integer vendorId = 0;
			if(userId !=null){
				Vendor vendor = vendorSession.getVendorByUserId(userId);
				if(vendor !=null){
					vendorId = vendor.getId();
				}
			}
			
			for(PurchaseOrderDTO pos : po.getPurchaseOrderDTOList()){
				map = new HashMap<Object, Object>();
				
				List<PurchaseOrderItemDTO> poItemList = purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(pos.getId());
				if(poItemList != null && poItemList.size() > 0) {
					//jika vendor id null maka nilainya dibuat jadi 0
					int venId = (poItemList.get(0).getVendorId() == null ?0 : poItemList.get(0).getVendorId());
					if(venId == vendorId.intValue()) {
						map.put("po", pos);
						map.put("poitem", poItemList);
						hasilList.add(map);
					}
				}
			}
			
			for(int i=0;i<hasilList.size();i++){
				if(numberOfRowPerPage >= hasilList.size()) {
					Map<Object, Object> page1Map = (Map<Object, Object>) hasilList.get(i);
					page1Map.put("jmlData", hasilList.size());
					paramsList.add(page1Map);
				} else {
					Integer sekarang = i + 1;
					if(sekarang >= startIndex && sekarang <= endIndex) {
						Map<Object, Object> page2Map = (Map<Object, Object>) hasilList.get(i);
						page2Map.put("jmlData", hasilList.size());
						paramsList.add(page2Map);
					}
				}
			}
			return Response.ok(paramsList).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build(); // kirim 500 Internal Server Error
		}
	}

	@Path("/getPurchaseOrderItemListByShipping/{id}")
	@GET
	public List<PurchaseOrderItem> getPurchaseOrderItemListByShipping(@PathParam("id") Integer id) {
		return purchaseOrderItemSession.getPurchaseOrderItemListByShipping(id);
	}
	
	@Path("/getListByPoId/{poId}")
	@GET
	public List<PurchaseOrderItem> getByPoId(@PathParam("poId") int poId) {
		return purchaseOrderItemSession.getPurchaseOrderItemByPoId(poId);
	}
}
