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

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;

/**
 * 
 * Project Name : promisews Package Name : id.co.promise.procurement.master File
 * Name : TahapanServices.java Description :
 * 
 * @author : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com Since : Sep
 *         12, 2015
 *
 */
@Stateless
@Path(value = "/procurement/master/tahapan")
@Produces(MediaType.APPLICATION_JSON)
public class TahapanService {

	@EJB
	private TahapanSession tahapanSession;
	@EJB
	TokenSession tokenSession;
	@EJB
	RoleUserSession roleUserSession;

	@Path("/get/{id}")
	@GET
	public Tahapan get(@PathParam("id") int id) {
		return tahapanSession.get(id);
	}

	@Path("/get-list")
	@GET
	public List<Tahapan> getList() {
		return tahapanSession.getList();
	}
	
	@Path("/get-unregistered-list")
	@POST
	public List<Tahapan> getUnregisteredList(Organisasi organisasi) {
		return tahapanSession.getUnregisteredList(organisasi);
	}
	
	@SuppressWarnings({"rawtypes"})
	@Path("/insert")
	@POST
	public Map insert(Tahapan tahapan, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		
		Boolean isSaveNama = tahapanSession.checkNamaTahapan(tahapan.getNama(), toDo, tahapan.getId());
		
		if(!isSaveNama){
			String errorNama = "Nama Tahapan Tidak Boleh Sama";
	   		Boolean isValid = false;
	   		map.put("isValid", isValid);
	   		map.put("errorNama", errorNama);
		}
		 
		if(isSaveNama) {
			tahapanSession.insertTahapan(tahapan,tokenSession.findByToken(token));
		}
		
		map.put("Tahapan", tahapan);

		return map;
	}

	@SuppressWarnings("rawtypes")
	@Path("/update")
	@POST
	public Map update(Tahapan tahapan, String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";
		
		Boolean isSaveNama = tahapanSession.checkNamaTahapan(tahapan.getNama(), toDo, tahapan.getId());
		
		if(!isSaveNama){
			 String errorNama = "Nama Tahapan Tidak Boleh Sama";
			 Boolean isValid = false;
			 map.put("isValid", isValid);
			 map.put("errorNama", errorNama);
		}
		 
		if(isSaveNama) {
			tahapanSession.updateTahapan(tahapan,tokenSession.findByToken(token));
		}
		
		map.put("Tahapan", tahapan);

		return map;
	}

	@Path("/delete")
	@POST
	public Tahapan delete(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return tahapanSession.deleteTahapan(id, tokenSession.findByToken(token));
	}

}
