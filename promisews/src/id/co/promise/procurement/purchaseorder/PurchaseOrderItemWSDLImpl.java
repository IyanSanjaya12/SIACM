/*package id.co.promise.procurement.purchaseorder;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.PurchaseOrderItem;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.core.Response;

@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface PurchaseOrderItemWSDLImpl {
	public PurchaseOrderItem getPurchaseOrderItem(@WebParam(name="id") int id);
	public List<PurchaseOrderItem> getPurchaseOrderItemList();
	public PurchaseOrderItem insertPurchaseOrderItem(
			@WebParam(name="purchaseOrder") Integer purchaseOrder,
			@WebParam(name="vendorName") String vendorName,
			@WebParam(name="itemName") String itemName,
			@WebParam(name="quantityPurchaseRequest") Double quantityPurchaseRequest,
			@WebParam(name="quantitySend") Double quantitySend,
			@WebParam(name="unitPrice") Double unitPrice,
			@WebParam(name="totalUnitPrices") Double totalUnitPrices,
			@WebParam(name="subTotal") Double subTotal,
			@WebParam(name="deliveryTime") Date deliveryTime,
			@WebParam(name="status") String status,
			@WebParam(name="Authorization") String token) ;
	public PurchaseOrderItem updatePurchaseOrderItem(
			@WebParam(name="id") int id,
			@WebParam(name="purchaseOrder") Integer purchaseOrder,
			@WebParam(name="vendorName") String vendorName,
			@WebParam(name="itemName") String itemName,
			@WebParam(name="quantityPurchaseRequest") Double quantityPurchaseRequest,
			@WebParam(name="quantitySend") Double quantitySend,
			@WebParam(name="unitPrice") Double unitPrice,
			@WebParam(name="totalUnitPrices") Double totalUnitPrices,
			@WebParam(name="subTotal") Double subTotal,
			@WebParam(name="deliveryTime") Date deliveryTime,
			@WebParam(name="status") String status,
			@WebParam(name="Authorization") String token);
	public PurchaseOrderItem deletePurchaseOrderItem(@WebParam(name="id") int id,
			@WebParam(name="Authorization") String token);
	public PurchaseOrderItem deleteRowPurchaseOrderItem(
			@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public Response findAllPOWithPOItem (
			@WebParam(name="pageNumber") Integer pageNumber,
			@WebParam(name="numberOfRowPerPage") Integer numberOfRowPerPage,
			@WebParam(name="searchingKeyWord") String searchingKeyWord,
			@WebParam(name="sortKeyWord") String sortKeyWord,
			@WebParam(name="searchingVendor") String searchingVendor,
			@WebParam(name="searchingItem") String searchingItem,
			@WebParam(name="Authorization") String token) throws Exception;
	public Response findAllPOWithPOItemByVendorName (
			@WebParam(name="pageNumber") Integer pageNumber,
			@WebParam(name="numberOfRowPerPage") Integer numberOfRowPerPage,
			@WebParam(name="userId") Integer userId,
			@WebParam(name="searchingKeyWord") String searchingKeyWord,
			@WebParam(name="sortKeyWord") String sortKeyWord,
			@WebParam(name="Authorization") String token) throws Exception;
	public List<PurchaseOrderItem> getPurchaseOrderItemListByShipping(@WebParam(name="id") Integer id) ;
}
*/