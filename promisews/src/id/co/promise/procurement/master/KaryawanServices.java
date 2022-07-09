package id.co.promise.procurement.master;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.map.HashedMap;

import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.siacm.Karyawan;
import id.co.promise.procurement.entity.siacm.Penjualan;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.Constant;

@Stateless
@Path(value = "/master/karyawan")
@Produces(MediaType.APPLICATION_JSON)
public class KaryawanServices {
	@EJB
	KaryawanSession karyawanSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/getKaryawanList")
	@GET
	public List<Karyawan> getKaryawanList() {
		return karyawanSession.getkaryawanList();
	}
	
	@Path("/save")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Karyawan karyawan, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			if(karyawan.getId() != null) {
				karyawanSession.update(karyawan, token);
			}else {
				karyawanSession.insert(karyawan, token);
			}
			return Response.ok(karyawan).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(Karyawan karyawan, @HeaderParam("Authorization") String strToken ) {
		Token token = tokenSession.findByToken(strToken);
		try {
			karyawanSession.delete(karyawan.getId(), token);
			
			return Response.ok(karyawan).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("/getPenjualanListByJabatan")
	@GET
	public Response getPenjualanListByJabatan(@HeaderParam("Authorization") String token) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pembantuMekanikList", karyawanSession.getkaryawanListByJabatan(Constant.KODE_JABATAN_PEMBANTU_MEKANIK));
			map.put("mekanikList", karyawanSession.getkaryawanListByJabatan(Constant.KODE_JABATAN_MEKANIK));
			map.put("kepalaMekanikList", karyawanSession.getkaryawanListByJabatan(Constant.KODE_JABATAN_KEPALA_MEKANIK));
			return Response.ok(map).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
