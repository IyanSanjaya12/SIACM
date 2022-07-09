package id.co.promise.procurement.catalog;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.SalesOrder;
import id.co.promise.procurement.entity.SalesOrderItem;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.master.SalesOrderItemSession;
import id.co.promise.procurement.master.SalesOrderSession;
import id.co.promise.procurement.security.TokenSession;

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
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value = "/procurement/salesorderitem/SalesOrderItemServices")
@Produces(MediaType.APPLICATION_JSON)
public class SalesOrderItemServices {

	@EJB
	private SalesOrderItemSession salesOrderItemSession;
	
	@EJB
	private SalesOrderSession salesOrderSession;
	
	@EJB
	private CatalogSession catalogSession;
	
	@EJB
	private TokenSession tokenSession;
	
	@Path("/findAll")
	@GET
	public List<SalesOrderItem> findAll () {
		return salesOrderItemSession.findAll();
	}	

	@Path("/getSalesOrderItemBySalesOrderIdAndVendorId/{id}/{vId}")
	@GET
	public List<SalesOrderItem> getSalesOrderItemBySalesOrderIdAndVendorId (@PathParam("id")int id, @PathParam("vId")int vId) {
		return salesOrderItemSession.getSalesOrderItemBySalesOrderIdAndVendorId(id,vId);
	}
	
	@Path("/create")
	@POST
	public SalesOrderItem insert(
			@FormParam("salesOrderId") Integer salesOrderId,
			@FormParam("catalogId") Integer catalogId,
			@FormParam("qty") Integer qty,
			@HeaderParam("Authorization") String token) {
		
		SalesOrderItem salesOrderItem = new SalesOrderItem();
		
		SalesOrder salesOrder = salesOrderSession.getSalesOrder(salesOrderId);
		
		Catalog catalog = catalogSession.getCatalog(catalogId);
		
		salesOrderItem.setSalesOrder(salesOrder);
		salesOrderItem.setCatalog(catalog);
		salesOrderItem.setQty(new Double(qty));
		
		salesOrderItemSession.insertSalesOrderItem(salesOrderItem, tokenSession.findByToken(token));
		
		return salesOrderItem;
	}
	
	@Path("/getSalesOrderItemByVendorId/{id}")
	@GET
	public List<SalesOrderItem> getSalesOrderItemByVendorId (@PathParam("id")int id) {
		return salesOrderItemSession.getSalesOrderItemByVendorId(id);
	}
	
	@Path("/getSalesOrderItemBySalesOrderIdAndByVendorId/{salesOrderId}/{vendorId}")
	@GET
	public List<SalesOrderItem> getSalesOrderItemBySalesOrderIdAndByVendorId (
				@PathParam("salesOrderId")int salesOrderId,
				@PathParam("vendorId")int vendorId) {
		return salesOrderItemSession.getSalesOrderItemBySalesOrderIdAndByVendorId(salesOrderId,vendorId);
	}
	
	@Path("/getSalesOrderItemList")
	@GET
	public List<SalesOrderItem> getSalesOrderItemList () {
		return salesOrderItemSession.getSalesOrderItemList();
	}
	
	@Path("/deleteSalesOrderItemBySalesOrderItemSoNumber")
	@POST
	public SalesOrderItem deleteSalesOrderItemBySalesOrderId (@FormParam("soNumber") String soNumber,
			@HeaderParam("Authorization") String token) {
				
		return salesOrderItemSession.deleteSalesOrderItemBySalesOrderItemSoNumber(soNumber,tokenSession.findByToken(token));
	}
	
	
	@Path("/getSalesOrderItemListByUserId/{id}")
	@GET
	public List<SalesOrderItem> getSalesOrderItemListByUserId (@PathParam("id")int id) {
		return salesOrderItemSession.getSalesOrderItemListByUserId(id);
	}
	
	
	
	
}
