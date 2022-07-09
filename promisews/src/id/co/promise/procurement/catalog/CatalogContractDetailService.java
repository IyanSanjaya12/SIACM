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
import javax.ws.rs.core.Response;

import id.co.promise.procurement.catalog.entity.CatalogContractDetail;
import id.co.promise.procurement.catalog.session.CatalogContractDetailSession;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemGroup;
import id.co.promise.procurement.master.ItemGroupSession;
import id.co.promise.procurement.master.ItemMasterDTO;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.ItemTypeSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/catalog/catalog-contract-detail-service")
@Produces(MediaType.APPLICATION_JSON)
public class CatalogContractDetailService {
	@EJB
	private ItemSession itemSession;
	@EJB
	private SatuanSession satuanSession;
	@EJB
	private ItemTypeSession itemTypeSession;
	@EJB
	private ItemGroupSession itemGroupSession;
	@EJB
	TokenSession tokenSession;
	
	@EJB
	CatalogContractDetailSession catalogContractDetailSession;

	@Path("/getItem/{id}")
	@GET
	public Item getItem(@PathParam("id") int itemId) {
		return itemSession.getItem(itemId);
	}

	@Path("/get-list")
	@GET
	public List<Item> getItemList() {
		return itemSession.getItemList();
	}

	/*@Path("/getItemByNameList/{nama}")
	@GET
	public List<Item> getItemByNameList(@PathParam("nama") String byName) {
		return itemSession.getItemByNameList(byName);
	}*/

	@Path("/get-list-by-kode/{kode}")
	@GET
	public List<Item> getListByKode(@PathParam("kode") String kode, @HeaderParam("Authorization") String token) {
		return itemSession.getListByKode(kode,token);
	}
	
	@Path("/get-list-by-kontrak")
	@POST
	public Response getListByKontrak(Integer catalogKontrakId, @HeaderParam("Authorization") String token) {
		try {
			List<Item> itemList=itemSession.getListByKontrak(catalogKontrakId, token);
			return Response.ok(itemList).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	@Path("/get-list-by-catalog-contract")
	@POST
	public Response getListByCatalogContract(Integer catalogContractId, @HeaderParam("Authorization") String token) {
		try {
			List<CatalogContractDetail> catalogContractDetailList=catalogContractDetailSession.getListByCatalogContractId(catalogContractId, token);
			return Response.ok(catalogContractDetailList).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	@Path("/get-by-item-type/{id}")
	@GET
	public List<Item> getByItemType(@PathParam("id")int id){
		return itemSession.getItemByType(id);
	}
	
	@Path("/get-by-kode/{kode}")
	@GET
	public Item getItemByKode(@PathParam("kode") String kode) {
		return itemSession.getItemByKode(kode);
	}
	

}
