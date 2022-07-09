package id.co.promise.procurement.master;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.promise.procurement.entity.ItemGroup;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.security.TokenSession;

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
@Path(value = "/procurement/master/item-group")
@Produces(MediaType.APPLICATION_JSON)
public class ItemGroupService {
	@EJB
	private ItemGroupSession itemGroupSession;
	@EJB
	TokenSession tokenSession;

	
	@SuppressWarnings({"rawtypes"})
	 @Path("/insert")
	 @POST
	 public Map insert(ItemGroup itemGroup,
	   @HeaderParam("Authorization") String token) {
	  
	  Map<Object, Object> map = new HashMap<Object, Object>();
	  String toDo = "insert";
	  
	  Boolean isSaveNama = itemGroupSession.checkNamaItemGroup(itemGroup.getNama(), toDo, itemGroup.getId());
	  
	  if(!isSaveNama) {
	   String errorNama = "error";
	   Boolean isValid = false;
	   map.put("isValid", isValid);
	   map.put("errorNama", errorNama);
	  }
	  
	  if(isSaveNama) {
		  itemGroup.setCreated(new Date());
		  itemGroup.setUserId(0);
		  itemGroup.setIsDelete(0);
		  itemGroupSession.insertItemGroup(itemGroup, tokenSession.findByToken(token));
	  }
	  
	  map.put("itemGroup",itemGroup);
	  return map;
	 }
	
	@SuppressWarnings({"rawtypes"})
	 @Path("/update")
	 @POST
	 public Map update(ItemGroup itemGroup,
	   @HeaderParam("Authorization") String token) {
	  
	  Map<Object, Object> map = new HashMap<Object, Object>();
	  String toDo = "update";
	  
	  Boolean isSaveNama = itemGroupSession.checkNamaItemGroup(itemGroup.getNama(), toDo, itemGroup.getId());
	  
	  if(!isSaveNama) {
	   String errorNama = "error";
	   Boolean isValid = false;
	   map.put("isValid", isValid);
	   map.put("errorNama", errorNama);
	  }
	  
	  if(isSaveNama) {
		  itemGroup.setUserId(0);
		  itemGroup.setIsDelete(0);
		  itemGroupSession.updateItemGroup(itemGroup, tokenSession.findByToken(token));
	  }
	  
	  map.put("itemGroup",itemGroup);
	  return map;
	 }



	/*
	 @Path("/deleteItemGroup")
	  @POST 
	  public ItemGroup deleteItemGroup(@FormParam("id") int id,
	  
	  @HeaderParam("Authorization") String token) { return
	  itemGroupSession.deleteItemGroup(id,tokenSession.findByToken(token)); }
	 */
	
	@Path("/delete")
	@POST
	public ItemGroup delete(@FormParam("id")int id,
			@HeaderParam("Authorization") String token){
		return itemGroupSession.deleteItemGroup(id, tokenSession.findByToken(token));
	}

	@Path("/get-all")
	@GET
	public String itemGroupGetAll() {
		return itemGroupSession.itemGroupGetAll();
	}
	
	@Path("/get-item-group-by-nama/{nama}")
	@GET
	public ItemGroup getItemGroupByNama(@PathParam("nama") String nama) {
		return itemGroupSession.getItemGroupByNama(nama);
	}
	
}
