package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.KualifikasiVendor;
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
@Path(value = "/procurement/master/kualifikasi-vendor")
@Produces(MediaType.APPLICATION_JSON)
public class KualifikasiVendorService {
	
	@EJB
	private KualifikasiVendorSession kualifikasiVendorSession;
	@EJB
	TokenSession tokenSession;

	/*@Path("/get-kualifikasi-vendor/{id}")
	@GET
	public KualifikasiVendor getKualifikasiVendor(@PathParam("id") int id) {
		return kualifikasiVendorSession.getKualifikasiVendor(id);
	}*/

	@Path("/get-list")
	@GET
	public List<KualifikasiVendor> getKualifikasiVendorList() {
		return kualifikasiVendorSession.getKualifikasiVendorList();
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(KualifikasiVendor kualifikasiVendor,
			@HeaderParam("Authorization") String token) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		Boolean isSaveNama = kualifikasiVendorSession.checkNamaKualifikasiVendor(kualifikasiVendor.getNama(), toDo,kualifikasiVendor.getId());
		if (!isSaveNama) {
			String errorNama = "promise.procurement.master.kualifikasi_vendor.error.nama_sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
		if (isSaveNama) {
			kualifikasiVendor.setUserId(0);
			kualifikasiVendorSession.insertKualifikasiVendor(kualifikasiVendor, tokenSession.findByToken(token));
		}
		map.put("kualifikasiVendor", kualifikasiVendor);
		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(KualifikasiVendor kualifikasiVendor,
			@HeaderParam("Authorization") String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";
		Boolean isSaveNama = kualifikasiVendorSession.checkNamaKualifikasiVendor(kualifikasiVendor.getNama(), toDo, kualifikasiVendor.getId());
		if (!isSaveNama) {
			String errorNama = "promise.procurement.master.kualifikasi_vendor.error.nama_sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}
		if (isSaveNama) {
			kualifikasiVendor.setUserId(0);
			kualifikasiVendorSession.updateKualifikasiVendor(kualifikasiVendor,tokenSession.findByToken(token));
		}
		map.put("kualifikasiVendor", kualifikasiVendor);
		return map;
	}

	@Path("/delete")
	@POST
	public KualifikasiVendor deleteKualifikasiVendor(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return kualifikasiVendorSession.deleteKualifikasiVendor(id, tokenSession.findByToken(token));
	}

	@Path("/delete-row-kualifikasi-vendor/{id}")
	@GET
	public KualifikasiVendor deleteRowKualifikasiVendor(
			@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		return kualifikasiVendorSession.deleteRowKualifikasiVendor(id, tokenSession.findByToken(token));
	}
}
