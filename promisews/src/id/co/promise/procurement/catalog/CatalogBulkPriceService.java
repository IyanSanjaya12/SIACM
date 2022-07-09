package id.co.promise.procurement.catalog;

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

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogBulkPrice;
import id.co.promise.procurement.catalog.session.CatalogBulkPriceSession;
import id.co.promise.procurement.entity.ApprovalProcessVendor;
import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value="/procurment/catalog/catalog-bulk-price")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogBulkPriceService {
	@EJB
	private TokenSession tokenSession;
	
	@EJB
	private CatalogBulkPriceSession catalogBulkSession;
	
	@Path("/get-list")
	@GET
	public List<CatalogBulkPrice> getCatalogBulkPriceList() {
		return catalogBulkSession.getCatalogBulkPriceList();
	}
	
	@Path("/get-list-by-id/{id}")
	@GET
	public List<CatalogBulkPrice> geCatalogBulkPriceListById(@PathParam("id")Integer id){
		return catalogBulkSession.getCatalogBulkPriceListById(id);
	}
	
	@Path("/get-list-by-catalog")
	@GET
	public List<CatalogBulkPrice> geCatalogBulkPriceListByCatalog(Catalog catalog){
		return catalogBulkSession.getCatalogBulkPriceListByCatalog(catalog);
	}
	
	@Path("/get-list-by-catalog-id")
	@POST
	public List<CatalogBulkPrice> geCatalogBulkPriceListByCatalogId(Integer catalogId){
		return catalogBulkSession.getCatalogBulkPriceListByCatalogId(catalogId);
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(CatalogBulkPrice catalogBulkPrice,
					@HeaderParam("Authorizontal") String token) {
		
		Map<Object, Object> map=new HashMap<Object, Object>();
		String toDo="insert";
		catalogBulkSession.insertCatalogBulkPrice(catalogBulkPrice, tokenSession.findByToken(token));
		map.put("catalogBulkPrice",catalogBulkPrice );
		return map;
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(CatalogBulkPrice catalogBulkPrice,
					@HeaderParam("Authorizontal") String token) {
		
		Map<Object, Object> map=new HashMap<Object, Object>();
		String toDo="update";
		catalogBulkSession.updateCatalogBulkPrice(catalogBulkPrice, tokenSession.findByToken(token));
		map.put("catalogBulkPrice",catalogBulkPrice );
		return map;
	}
	
	@Path("/delete")
	@POST
	public CatalogBulkPrice delete(@FormParam("id") int id, 
			@HeaderParam("Authorizontal") String token) {
		
		return catalogBulkSession.deleteCatalogBulkPrice(id, tokenSession.findByToken(token));
	}
	
	@Path("/delete-row")
	@POST
	public CatalogBulkPrice deleteRow(@FormParam("id") int id, 
			@HeaderParam("Authorizontal") String token) {
		
		return catalogBulkSession.deleteRowCatalogBulkPrice(id, tokenSession.findByToken(token));
	}
	
	@Path("/getDataDiskon")
	@POST
	public List<CatalogBulkPrice> getDataDiskonList(@FormParam("catalogId") Integer catalogId) {
		return catalogBulkSession.getCatalogBulkPriceListByCatalogId(catalogId);
	}
	
}
