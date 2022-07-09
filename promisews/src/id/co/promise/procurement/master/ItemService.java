package id.co.promise.procurement.master;

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
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemGroup;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/master/item")
@Produces(MediaType.APPLICATION_JSON)
public class ItemService {
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
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(ItemMasterDTO itemMasterDTO, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		
		Boolean isSaveKode =
		itemSession.checkKodeItem(itemMasterDTO.getItem().getKode(), toDo, itemMasterDTO.getItem().getId());
		
		if(!isSaveKode){
			 String errorKode = "Kode Item Tidak Boleh Sama";
			   Boolean isValid = false;
			   map.put("isValid", isValid);
			   map.put("errorKode", errorKode);
		}
		Item item = itemMasterDTO.getItem();
		
		if(isSaveKode) {
			
			ItemGroup itemGroup = itemGroupSession.find(itemMasterDTO.getItemGroupId());
			
			item.setItemGroupId(itemGroup);
			
			itemSession.insertItem(item,tokenSession.findByToken(token));
		}
		
		map.put("Item", item);

		return map;
	}
	
	/* untuk insert item di pr */
	@Path("/insert-item")
	@POST
	public Item insertItem(@FormParam("nama") String nama,
			@FormParam("kode") String kode,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("satuanId") Integer satuanId,
			@FormParam("itemTypeId") Integer itemTypeId,
			@FormParam("itemGroupId") Integer itemGroupId,
			@HeaderParam("Authorization") String token) {
		Item item = new Item();
		item.setNama(nama);
		item.setKode(kode);
		item.setDeskripsi(deskripsi);
		item.setSatuanId(satuanSession.find(satuanId));
		item.setItemType(itemTypeSession.find(itemTypeId));
		item.setItemGroupId(itemGroupSession.find(itemGroupId));
		item.setUserId(0);
		itemSession.insertItem(item, tokenSession.findByToken(token));
		return item;
	}
	
	@Path("/update")
	@POST
	public Map update(ItemMasterDTO itemMasterDTO, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
		
		Boolean isSaveKode =
		itemSession.checkKodeItem(itemMasterDTO.getItem().getKode(), toDo, itemMasterDTO.getItem().getId());
		
		if(!isSaveKode){
			 String errorKode = "Kode Item Tidak Boleh Sama";
			   Boolean isValid = false;
			   map.put("isValid", isValid);
			   map.put("errorKode", errorKode);
		}
		Item item = itemMasterDTO.getItem();
		
		if(isSaveKode) {
			ItemGroup itemGroup = itemGroupSession.find(itemMasterDTO.getItemGroupId());
			item.setItemGroupId(itemGroup);
			
			itemSession.updateItem(item,tokenSession.findByToken(token));
		}

		map.put("Item", item);

		return map;
	}
	
	/* untuk insert item di pr */
	@Path("/update-item")
	@POST
	public Item updateItem(@FormParam("id") int id,
			@FormParam("nama") String nama, @FormParam("kode") String kode,
			@FormParam("deskripsi") String deskripsi,
			@FormParam("satuanId") Integer satuanId,
			@FormParam("itemTypeId") Integer itemTypeId,
			@FormParam("itemGroupId") Integer itemGroupId,
			@HeaderParam("Authorization") String token) {
		Item item = itemSession.find(id);
		item.setNama(nama);
		item.setKode(kode);
		item.setDeskripsi(deskripsi);
		item.setSatuanId(satuanSession.find(satuanId));
		item.setItemType(itemTypeSession.find(itemTypeId));
		item.setItemGroupId(itemGroupSession.find(itemGroupId));
		itemSession.updateItem(item, tokenSession.findByToken(token));
		return item;
	}
	
	@Path("/delete")
	@POST
	public Item delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return itemSession.deleteItem(id, tokenSession.findByToken(token));
	}
	
	/*@Path("/deleteItem/{id}")
	@GET
	public Item deleteItem(@PathParam("id") int itemId,
			@HeaderParam("Authorization") String token) {
		return itemSession.deleteItem(itemId, tokenSession.findByToken(token));
	} 

	@Path("/deleteRowItem/{id}")
	@GET
	public Item deleteRowItem(@PathParam("id") int itemId,
			@HeaderParam("Authorization") String token) {
		return itemSession.deleteRowItem(itemId,
				tokenSession.findByToken(token));
	}*/
}
