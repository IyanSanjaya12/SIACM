/*package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.entity.PurchaseOrderItem;
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
import javax.jws.WebService;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

@Stateless
@WebService(serviceName="PurchaseOrder", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaseorder.PurchaseOrderItemWSDLImpl")
public class PurchaseOrderItemWSDLServices implements PurchaseOrderItemWSDLImpl {
	final static Logger log = Logger.getLogger(PurchaseOrderItemWSDLServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	
	@EJB private PurchaseOrderItemSession purchaseOrderItemSession;

	@EJB private PurchaseOrderSession purchaseOrderSession;
	
	@EJB private VendorSession vendorSession;

	@EJB TokenSession tokenSession;

	public PurchaseOrderItem getPurchaseOrderItem(@PathParam("id") int id) {
		return purchaseOrderItemSession.getPurchaseOrderItem(id);
	}

	public List<PurchaseOrderItem> getPurchaseOrderItemList() {
		return purchaseOrderItemSession.getPurchaseOrderItemList();
	}

	public PurchaseOrderItem insertPurchaseOrderItem(
			Integer purchaseOrder,
			String vendorName,
			String itemName,
			Double quantityPurchaseRequest,
			Double quantitySend,
			Double unitPrice,
			Double totalUnitPrices,
			Double subTotal,
			Date deliveryTime,
			String status,
			String token) {
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

	public PurchaseOrderItem updatePurchaseOrderItem(
			int id,
			Integer purchaseOrder,
			String vendorName,
			String itemName,
			Double quantityPurchaseRequest,
			Double quantitySend,
			Double unitPrice,
			Double totalUnitPrices,
			Double subTotal,
			Date deliveryTime,
			String status,
			String token) {
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

	public PurchaseOrderItem deletePurchaseOrderItem(int id,
			String token) {
		return purchaseOrderItemSession.deletePurchaseOrderItem(id,
				tokenSession.findByToken(token));
	}

	public PurchaseOrderItem deleteRowPurchaseOrderItem(
			int id, String token) {
		return purchaseOrderItemSession.deleteRowPurchaseOrderItem(id,
				tokenSession.findByToken(token));
	}

	public Response findAllPOWithPOItem (
			Integer pageNumber,
			Integer numberOfRowPerPage,
			String searchingKeyWord,
			String sortKeyWord,
			String searchingVendor,
			String searchingItem,
			String token) throws Exception {
		
		List<Object> hasilList = new ArrayList<Object>();
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
			
			po = purchaseOrderSession.getPODTOListWithRange(startIndex, endIndex, searchingKeyWord, sortKeyWord,searchingVendor,searchingItem);
			
			for(PurchaseOrderDTO pos : po.getPurchaseOrderDTOList()){
				map = new HashMap<Object, Object>();
				
				List<PurchaseOrderItemDTO> poItemList = purchaseOrderItemSession.getPurchaseOrderItemDTOByPoId(pos.getId());
				map.put("po", pos);
				map.put("jmlData", po.getJmlData());
				if(poItemList != null) {
					map.put("poitem", poItemList);
				}
				hasilList.add(map);
			}
			
			return Response.ok(hasilList).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build(); // kirim 500 Internal Server Error
		}
	}
	
	@SuppressWarnings("unchecked")
	public Response findAllPOWithPOItemByVendorName (
			Integer pageNumber,
			Integer numberOfRowPerPage,
			Integer userId,
			String searchingKeyWord,
			String sortKeyWord,
			String token) throws Exception {
		
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
			
			*//** get vendor ID **//*
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
					if(poItemList.get(0).getVendorId().intValue() == vendorId.intValue()) {
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

	public List<PurchaseOrderItem> getPurchaseOrderItemListByShipping(@PathParam("id") Integer id) {
		return purchaseOrderItemSession.getPurchaseOrderItemListByShipping(id);
	}
}
*/