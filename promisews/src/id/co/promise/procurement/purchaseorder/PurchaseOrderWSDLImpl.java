/*package id.co.promise.procurement.purchaseorder;

import java.util.List;

import id.co.promise.procurement.dto.PurchaseOrderDtl;
import id.co.promise.procurement.entity.PurchaseOrder;
import id.co.promise.procurement.entity.Vendor;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface PurchaseOrderWSDLImpl {
	public PurchaseOrder getPurchaseOrder(@WebParam(name="id") int id);
	public Vendor getPurchaseOrderVendor(@WebParam(name="id") int id);
	public List<PurchaseOrder> getPurchaseOrderList();
	public int countByStatus(@WebParam(name="status") String status);
	public Response getPurchaseOrderDTOList(@WebParam(name="pageNumber") Integer pageNumber, @WebParam(name="numberOfRowPerPage") Integer numberOfRowPerPage,
			@WebParam(name="searchingKeyWord") String searchingKeyWord, @WebParam(name="Authorization") String token);
	public Response getPurchaseRequestByNumber(@WebParam(name="prNumber") String prNumber);
	public Response getAfcoByCompanyName(@WebParam(name="companyName") String companyName);
	public PurchaseOrder insertPurchaseOrder(@WebParam(name="poNumber") String poNumber, @WebParam(name="purchaseRequest") Integer purchaseRequest, @WebParam(name="afco") Integer afco,
			@WebParam(name="addressBook") Integer addressBook, @WebParam(name="pengadaan") Integer pengadaan, @WebParam(name="termAndCondition") Integer termAndCondition, @WebParam(name="notes") String notes,
			@WebParam(name="downPayment") Double downPayment, @WebParam(name="discount") Double discount, @WebParam(name="tax") Double tax, @WebParam(name="totalCost") Double totalCost,
			@WebParam(name="status") String status, @WebParam(name="Authorization") String token);
	public Response getPurchaseOrderDTOListByPoId(@WebParam(name="poId") Integer poId, @WebParam(name="Authorization") String token);
	public PurchaseOrder updatePurchaseOrder(@WebParam(name="id") int id, @WebParam(name="poNumber") String poNumber, @WebParam(name="purchaseRequest") Integer purchaseRequest, @WebParam(name="afco") Integer afco,
			@WebParam(name="addressBook") Integer addressBook, @WebParam(name="pengadaan") Integer pengadaan, @WebParam(name="termAndCondition") Integer termAndCondition, @WebParam(name="notes") String notes,
			@WebParam(name="downPayment") Double downPayment, @WebParam(name="discount") Double discount, @WebParam(name="tax") Double tax, @WebParam(name="totalCost") Double totalCost,
			@WebParam(name="status") String status, @WebParam(name="Authorization") String token) ;
	public PurchaseOrder deletePurchaseOrder(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public PurchaseOrder deleteRowPurchaseOrder(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public Response createPO(String jsonString, @WebParam(name="Authorization") String token);
	public Response updatePO(String jsonString, @WebParam(name="Authorization") String token);
	public PurchaseOrder updateStatusApproval(@WebParam(name="id") Integer id, @WebParam(name="nextApproval") String nextApproval, @WebParam(name="status") Integer status,
			@WebParam(name="Authorization") String token);
	public Vendor getPurchaseOrderVendorById(@WebParam(name="id") int id);
	public List<PurchaseOrder> getPurchaseOrderListByVendorForPerformance(@WebParam(name="id") Integer id);
	public PurchaseOrderDtl getPurchaseOrderDetailById(Integer id) ;
	public PurchaseOrderDtl updatePurchaseOrderDtl(PurchaseOrderDtl purchaseOrderDtl, @WebParam(name="Authorization") String authorization);
	public Response createPurchaseRequestServicesFromContract(String jsonRequest, @WebParam(name="Authorization") String authorization) ;
}
*/