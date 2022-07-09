/*package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.PurchaseRequest;

import java.util.Date;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.core.Response;


@WebService(targetNamespace = "http://promise.co.id/wsdl")
public interface PurchaseRequestWSDLImpl {
	public List<PurchaseRequest> getList();
	public PurchaseRequest getPurchaseRequestById(@WebParam(name="id") int id);
	public int countByStatus(@WebParam(name="status") int status, @WebParam(name="Authorization") String token) ;
	public int countAll(@WebParam(name="Authorization") String token);
	public PurchaseRequest insert( @WebParam(name="prnumber") String prnumber, @WebParam(name="department") String department, @WebParam(name="departmentId") int departmentId,
			@WebParam(name="costcenter") String costcenter, @WebParam(name="totalcost") String totalcost, @WebParam(name="daterequired") String daterequired, @WebParam(name="approvalId") String approvalId,
			@WebParam(name="approvalIsNew") boolean approvalIsNew, @WebParam(name="approvalUser") int approvalUser, @WebParam(name="apparovalOrganisasi") int apparovalOrganisasi,
			@WebParam(name="nextapproval") String nextapproval, @WebParam(name="procurementprocess") String procurementprocess, @WebParam(name="termandcondition") String termandcondition,
			@WebParam(name="description") String description, @WebParam(name="tahapanId") String tahapanId, @WebParam(name="fileuploadList") String fileuploadList,@WebParam(name="isJoin") Integer isJoin, @WebParam(name="Authorization") String token) ;
	public PurchaseRequest update(@WebParam(name="id") Integer id, @WebParam(name="prnumber") String prnumber, @WebParam(name="department") String department, @WebParam(name="departmentId") int departmentId,
			@WebParam(name="costcenter") String costcenter, @WebParam(name="totalcost") String totalcost, @WebParam(name="daterequired") String daterequired,
			@WebParam(name="approvalIsChange") boolean approvalIsChange, @WebParam(name="approvalProcessTypeId") int approvalProcessTypeId, @WebParam(name="approvalId") String approvalId,
			@WebParam(name="approvalIsNew") boolean approvalIsNew, @WebParam(name="approvalUser") int approvalUser, @WebParam(name="apparovalOrganisasi") int apparovalOrganisasi,
			@WebParam(name="nextapproval") String nextapproval, @WebParam(name="procurementprocess") String procurementprocess, @WebParam(name="termandcondition") String termandcondition,
			@WebParam(name="description") String description, @WebParam(name="tahapanId") String tahapanId, @WebParam(name="fileuploadList") String fileuploadList, @WebParam(name="isJoin") Integer isJoin, @WebParam(name="Authorization") String token) ;
	public PurchaseRequest delete(@WebParam(name="id") int id, @WebParam(name="Authorization") String token) ;
	public PurchaseRequest deleteRow(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
	public Response getPurchaseRequestListByPRNumberWithPagination( @WebParam(name="pageNumber") Integer pageNumber, 
			@WebParam(name="numberOfRowPerPage") Integer numberOfRowPerPage,
			@WebParam(name="searchingKeyWord") String searchingKeyWord,
			@WebParam(name="isJoin") Integer joinStatus,
			@WebParam(name="Authorization") String token);
	public Response getPurchaseRequestVerificationListByPRNumberWithPagination(@WebParam(name="pageNumber") Integer pageNumber, @WebParam(name="numberOfRowPerPage") Integer numberOfRowPerPage,
			@WebParam(name="searchingKeyWord") String searchingKeyWord, @WebParam(name="Authorization") String token);
	public Response indexPageList(@WebParam(name="keywordSearch") String keywordSearch, @WebParam(name="pageNo") int pageNo, @WebParam(name="pageSize") int pageSize, @WebParam(name="orderKeyword") String orderKeyword);
	public Response createPurchaseRequest(String jsonString, @WebParam(name="Authorization") String token);
	public PurchaseRequest updateStatusApproval(@WebParam(name="id") Integer id, @WebParam(name="nextApproval") String nextApproval, @WebParam(name="status") Integer status,
			@WebParam(name="Authorization") String token);
	public List<PurchaseRequest> getVerifiedPurchaseRequest(@WebParam(name="Authorization") String token);
	public List<PurchaseRequest> getVerifiedPurchaseRequestByNumber(@WebParam(name="keyword") String keyword, @WebParam(name="Authorization") String token);
	public Response insertPRJoin( @WebParam(name="prNumber") String prnumber,
			@WebParam(name="prGrandTotal") String totalcost, 
			@WebParam(name="prDate") Date daterequired,
			@WebParam(name="prDeskripsi") String description,
			@WebParam(name="prItemList") String itemsId,
			@WebParam(name="prQtyJoin") Double qtyJoin,
			@WebParam(name="Authorization") String token);
	public PurchaseRequest deleteJoin(@WebParam(name="id") int id, @WebParam(name="Authorization") String token);
}
*/