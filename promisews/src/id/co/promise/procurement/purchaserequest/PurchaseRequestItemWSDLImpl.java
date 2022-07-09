/*package id.co.promise.procurement.purchaserequest;

import java.util.List;

import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.utils.JsonObject;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.core.Response;

@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface PurchaseRequestItemWSDLImpl {
	public PurchaseRequestItem getPurchaseRequestItemById(@WebParam(name="id") int id);
	public List<PurchaseRequestItem> getPurchaseRequestItemList();
	public List<PurchaseRequestItem> getPurchaseRequestItemListByPurchaseRequestId(@WebParam(name="id") int purchaserequest);
	public List<PurchaseRequestItem> getPurchaseRequestItemListByPurchaseRequestJoinId(@WebParam(name="id") int purchaserequest);
	public PurchaseRequestItem getByPurchaseRequestItemId(@WebParam(name="id") int purchaserequest);
	public PurchaseRequestItem createPurchaseRequestItem(@WebParam(name="purchaserequestId") Integer purchaserequest, @WebParam(name="itemId") Integer item, @WebParam(name="itemname") String itemname,
			@WebParam(name="kode") String kode, @WebParam(name="vendorName") String vendorName, @WebParam(name="quantity") Double quantity, @WebParam(name="price") Double price, @WebParam(name="unit") String unit,
			@WebParam(name="costcenter") String costcenteritem, @WebParam(name="status") String status, @WebParam(name="mataUangId") Integer matauang, @WebParam(name="vendorId") Integer vendor,
			@WebParam(name="specification") String specification, @WebParam(name="total") String total, @WebParam(name="pathfile") String pathfile, @WebParam(name="Authorization") String token);
	public PurchaseRequestItem createPurchaseRequestItemFreeText(@WebParam(name="purchaserequestId") Integer purchaserequest, @WebParam(name="itemname") String itemname,
			@WebParam(name="quantity") Double quantity, @WebParam(name="price") Double price, @WebParam(name="unit") String unit, @WebParam(name="specification") String specification,
			@WebParam(name="pathfile") String pathfile, @WebParam(name="status") String status, @WebParam(name="costcenter") String costcenteritem, @WebParam(name="total") String total,
			@WebParam(name="Authorization") String token);
	public PurchaseRequestItem updatePurchaseRequestItem(@WebParam(name="id") Integer id, @WebParam(name="purchaseRequest") Integer purchaserequest, @WebParam(name="itemId") Integer item,
			@WebParam(name="itemname") String itemname, @WebParam(name="kode") String kode, @WebParam(name="vendorName") String vendorName, @WebParam(name="quantity") Double quantity,
			@WebParam(name="price") Double price, @WebParam(name="unit") String unit, @WebParam(name="costcenter") String costcenter, @WebParam(name="status") String status,
			@WebParam(name="mataUangId") Integer matauang, @WebParam(name="specification") String specification, @WebParam(name="Authorization") String token);
	public PurchaseRequestItem deletePurchaseRequestItem(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public PurchaseRequestItem deleteRowPurchaseRequestItem(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public boolean deleteByPR(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public boolean deleteRowByPR(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public Response getPurchaseRequestItemListByPR(@WebParam(name="prId") Integer prId, @WebParam(name="Authorization") String token);
	public Response getPurchaseRequestItemByPRAndVendor(@WebParam(name="prId") Integer prId, @WebParam(name="vendorId") Integer vendorId, @WebParam(name="Authorization") String token);
	public JsonObject<PurchaseRequestItem> getPurchaseRequestItemPaggingList(String start, String draw, String length, String search, String order, String column,
			@WebParam(name="Authorization") String token);
	public JsonObject<PurchaseRequestItem> getPurchaseRequestItemPaggingListForPrConsolidation(String start, String draw, String length, String search, String order, String column, String itemId, String prId,
			@WebParam(name="Authorization") String token);
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItem(@WebParam(name="id") Integer id, @WebParam(name="Authorization") String token);
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItemAndPrId(
			@WebParam(name="id") Integer id, 
			@WebParam(name="prId") Integer prId,
			@WebParam(name="Authorization") String token);
}
*/