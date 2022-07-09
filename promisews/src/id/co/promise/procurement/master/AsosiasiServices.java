package id.co.promise.procurement.master;


import id.co.promise.procurement.entity.Asosiasi;
import id.co.promise.procurement.entity.VendorPIC;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value = "/procurement/master/AsosiasiServices")
@Produces(MediaType.APPLICATION_JSON)
public class AsosiasiServices {

	@EJB
	AsosiasiSession asosiasiSession;
	
	@Path("/getById/{id}")
	@GET
	public Asosiasi getAsosiasi(@PathParam("id") int id) {
		return asosiasiSession.getAsosiasi(id);
	}

	@Path("/getList")
	@GET
	public List<Asosiasi> getAsosiasiList() {
		return asosiasiSession.getAsosiasiList();
	}
	
	@Path("/create")
	@POST
	public Asosiasi createAsosiasi (@FormParam("nama") String nama){
		
		Asosiasi asosiasi = new Asosiasi();
		asosiasi.setNama(nama);
		asosiasi.setUserId(0);
		asosiasiSession.create(asosiasi);
		
		return asosiasi;
	}
	
	@Path("/update")
	@POST
	public Asosiasi updateAsosiasi (@FormParam("id") Integer id,
			@FormParam("nama") String nama){
		Asosiasi asosiasi = asosiasiSession.find(id);
		asosiasi.setNama(nama);
		asosiasi.setUserId(0);
		asosiasiSession.edit(asosiasi);
		
		return asosiasi;
	}
	
	@Path("/delete/{id}")
	@GET
	public void deleteRowAsosiasi (@PathParam("id") Integer id){
		asosiasiSession.remove(asosiasiSession.find(id));
	}

}
