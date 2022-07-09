package id.co.promise.procurement.master;


import id.co.promise.procurement.entity.KondisiPeralatanVendor;
import id.co.promise.procurement.security.TokenSession;

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
@Path(value = "/procurement/master/KondisiServices")
@Produces(MediaType.APPLICATION_JSON)
public class KondisiPeralatanVendorServices {

	@EJB
	KondisiPeralatanVendorSession kondisiSession;
	
	@EJB
	TokenSession tokenSession;

	@Path("/getById/{id}")
	@GET
	public KondisiPeralatanVendor getKondisiPeralatanVendor(@PathParam("id") int id) {
		return kondisiSession.getKondisiPeralatanVendor(id);
	}

	@Path("/getList")
	@GET
	public List<KondisiPeralatanVendor> getKondisiPeralatanVendorList() {
		return kondisiSession.getKondisiPeralatanVendorList();
	}

	@Path("/create")
	@POST
	public KondisiPeralatanVendor insertKondisiPeralatanVendor(@FormParam("nama") String nama,
			@HeaderParam("Authorization") String token) {
		KondisiPeralatanVendor kondisi = new KondisiPeralatanVendor();
		kondisi.setNama(nama);
		kondisi.setUserId(0);
		kondisiSession.insertKondisiPeralatanVendor(kondisi, tokenSession.findByToken(token));
		return kondisi;
	}

	@Path("/update")
	@POST
	public KondisiPeralatanVendor updateKondisiPeralatanVendor(@FormParam("id") Integer id,
			@FormParam("nama") String nama,
			@HeaderParam("Authorization") String token) {
		KondisiPeralatanVendor kondisi = kondisiSession.find(id);
		kondisi.setNama(nama);
		kondisiSession.updateKondisiPeralatanVendor(kondisi, tokenSession.findByToken(token));
		return kondisi;
	}

	@Path("/delete/{id}")
	@GET
	public KondisiPeralatanVendor deleteKondisiPeralatanVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return kondisiSession.deleteKondisiPeralatanVendor(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public KondisiPeralatanVendor deleteRowKondisiPeralatanVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return kondisiSession.deleteRowKondisiPeralatanVendor(id, tokenSession.findByToken(token));
	}
}
