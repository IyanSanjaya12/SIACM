package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.MetodePengadaan;
import id.co.promise.procurement.entity.Negara;
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
@Path(value="/procurement/master/metodePengadaanServices")
@Produces(MediaType.APPLICATION_JSON)
public class MetodePengadaanServices {
	@EJB
	private MetodePengadaanSession metodePengadaanSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getMetodePengadaan/{id}")
	@GET
	public MetodePengadaan getMetodePengadaan(@PathParam("id")int id){
		return metodePengadaanSession.getMetodePengadaan(id);
	}
	
	@Path("/getMetodePengadaanList")
	@GET
	public List<MetodePengadaan> getMetodePengadaanList(){
		return metodePengadaanSession.getMetodePengadaanList();
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert (MetodePengadaan metodePengadaan, String token){
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		
		Boolean isSaveNama =
	    metodePengadaanSession.checkNamaMetodePengadaan (metodePengadaan.getNama(), toDo, metodePengadaan.getId());
		
		if(!isSaveNama){
			String errorNama = "Nama Metode Pengadaan Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
		
		if (isSaveNama){
			metodePengadaanSession.insertMetodePengadaan(metodePengadaan, tokenSession.findByToken(token));
		}
		
		map.put("MetodePengadaan", metodePengadaan);
		
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/update")
	@POST
	public Map update (MetodePengadaan metodePengadaan, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
		
		Boolean isSaveNama =
	    metodePengadaanSession.checkNamaMetodePengadaan (metodePengadaan.getNama(), toDo, metodePengadaan.getId());
		
		if(!isSaveNama){
			 String errorNama = "Nama Metode Pengadaan Tidak Boleh Sama";
			   Boolean isValid = false;
			   map.put("isValid", isValid);
			   map.put("errorNama", errorNama);
		}
		
		if (isSaveNama){
			metodePengadaanSession.updateMetodePengadaan(metodePengadaan, tokenSession.findByToken(token));
		}
		
		map.put("MetodePengadaan", metodePengadaan);
		
		return map;
	}
	
	@Path("/delete")
	@POST
	public MetodePengadaan deleteMetodePengadaan(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return metodePengadaanSession.deleteMetodePengadaan(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRowMetodePengadaan/{id}")
	@GET
	public MetodePengadaan deleteRowMetodePengadaan(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return metodePengadaanSession.deleteRowMetodePengadaan(id, tokenSession.findByToken(token));
	}
}
