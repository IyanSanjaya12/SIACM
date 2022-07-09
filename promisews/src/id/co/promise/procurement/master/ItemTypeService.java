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

import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value = "/procurement/master/item-type")
@Produces(MediaType.APPLICATION_JSON)
public class ItemTypeService {

	@EJB
	private ItemTypeSession itemTypeSession;
	@EJB
	TokenSession tokenSession;

	@Path("/get-list")
	@GET
	public List<ItemType> getList() {
		return itemTypeSession.getItemTypeList();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(ItemType itemtype, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveNama = itemTypeSession.checkNamaItemType(itemtype.getNama(), toDo, itemtype.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Tipe Item Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			itemTypeSession.insertItemType(itemtype, tokenSession.findByToken(token));
		}

		map.put("itemType", itemtype);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(ItemType itemtype, @HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveNama = itemTypeSession.checkNamaItemType(itemtype.getNama(), toDo, itemtype.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Tipe Item Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			itemTypeSession.updateItemType(itemtype, tokenSession.findByToken(token));
		}

		map.put("itemType", itemtype);

		return map;
	}

	@Path("/delete")
	@POST
	public ItemType delete(@FormParam("id") int id, @HeaderParam("Authorization") String token) {
		return itemTypeSession.deleteItemType(id, tokenSession.findByToken(token));
	}

	@Path("/delete-row/{id}")
	@GET
	public ItemType deleteRowItemType(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return itemTypeSession.deleteRowItemType(id, tokenSession.findByToken(token));
	}

}
