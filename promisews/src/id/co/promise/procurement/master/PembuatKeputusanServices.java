package id.co.promise.procurement.master;


import id.co.promise.procurement.entity.PembuatKeputusan;
import id.co.promise.procurement.security.TokenSession;

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

@Stateless
@Path("/procurement/master/pembuatKeputusanServices")
@Produces(MediaType.APPLICATION_JSON)
public class PembuatKeputusanServices {
	@EJB
	PembuatKeputusanSession pembuatKeputusanSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getId/{id}")
	@GET
	public PembuatKeputusan getPembuatKeputusan(@PathParam("id")int id){
		return pembuatKeputusanSession.getPembuatKeputusanId(id);
	}
	
	@Path("/getList")
	@GET
	public List<PembuatKeputusan> getPembuatKeputusanList(){
		return pembuatKeputusanSession.getPembuatKeputusanList();
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(PembuatKeputusan pembuatkeputusan,
			@HeaderParam("Authorization") String token) {  
		Map<Object, Object> map = new HashMap <Object, Object>();
		String toDo = "insert";
		Boolean isSaveNama = pembuatKeputusanSession.checkNamaPembuatKeputusan(pembuatkeputusan.getNama(),toDo,pembuatkeputusan.getId());
		if(!isSaveNama) {
		   String errorNama = "promise.procurement.master.pembuat_keputusan.error.nama_sama";
		   Boolean isValid = false;
		   map.put("isValid", isValid);
		   map.put("errorNama", errorNama);
		}
		if(isSaveNama) {
		   pembuatkeputusan.setUserId(0);
		   pembuatKeputusanSession.insertPembuatKeputusan(pembuatkeputusan, tokenSession.findByToken(token));
		}
		map.put("pembuatKeputusan", pembuatkeputusan);
		return map;
	}
	
	
	@SuppressWarnings({"rawtypes"})
	@Path("/update")
	@POST
	public Map update(PembuatKeputusan pembuatkeputusan,
			@HeaderParam("Authorization") String token) {  
		Map<Object, Object> map = new HashMap <Object, Object>();
		String toDo = "update";
		Boolean isSaveNama = pembuatKeputusanSession.checkNamaPembuatKeputusan(pembuatkeputusan.getNama(),toDo,pembuatkeputusan.getId());
		if(!isSaveNama) {
		   String errorNama = "promise.procurement.master.pembuat_keputusan.error.nama_sama";
		   Boolean isValid = false;
		   map.put("isValid", isValid);
		   map.put("errorNama", errorNama);
		}
		if(isSaveNama) {
			pembuatkeputusan.setUserId(0);
			pembuatKeputusanSession.updatePembuatKeputusan(pembuatkeputusan, tokenSession.findByToken(token));
		}
		map.put("pembuatKeputusan", pembuatkeputusan);
		return map;
	}
	
	@Path("/delete")
	@POST
	public PembuatKeputusan deletePembuatKeputusan(@FormParam("id") int id,
			@HeaderParam("Authorization") String token){
		return pembuatKeputusanSession.deletePembuatKeputusan(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public PembuatKeputusan deleteRowPembuatKeputusan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return pembuatKeputusanSession.deleteRowPembuatKeputusan(id, tokenSession.findByToken(token));
	}
}
