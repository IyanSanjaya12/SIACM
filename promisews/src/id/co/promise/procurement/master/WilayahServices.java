package id.co.promise.procurement.master;


import id.co.promise.procurement.entity.Wilayah;
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
@Path(value="/procurement/master/WilayahServices")
@Produces(MediaType.APPLICATION_JSON)
public class WilayahServices {

	@EJB
	WilayahSession wilayahSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getList")
	@GET
	public List<Wilayah> getList() {
		return wilayahSession.getList();
	}
	
	@Path("/getPropinsiList")
	@GET
	public List<Wilayah> getPropinsiList() {
		return wilayahSession.getPropinsiList();
	}
	
	@Path("/getKotaList/{propinsiId}")
	@GET
	public List<Wilayah> getKotaList(
			@PathParam("propinsiId") Integer propinsiId) {
		return wilayahSession.getKotaList(propinsiId);
	}
	
	@Path("/create")
	@POST
	public Wilayah insertWilayah(@FormParam("lokasi_kode")String lokasi_kode,
			@FormParam("lokasi_nama")String lokasi_nama,
			@FormParam("lokasi_propinsi")Integer lokasi_propinsi,
			@FormParam("lokasi_kabupatenkota")Integer lokasi_kabupatenkota,
			@FormParam("lokasi_kecamatan")Integer lokasi_kecamatan,
			@FormParam("lokasi_kelurahan")Integer lokasi_kelurahan,
			@HeaderParam("Authorization") String token) {
		Wilayah wilayah = new Wilayah();
		wilayah.setLokasi_kode(lokasi_kode);
		wilayah.setLokasi_nama(lokasi_nama);
		wilayah.setLokasi_propinsi(lokasi_propinsi);
		wilayah.setLokasi_kabupatenkota(lokasi_kabupatenkota);
		wilayah.setLokasi_kelurahan(lokasi_kelurahan);
		wilayah.setLokasi_kecamatan(lokasi_kecamatan);
		wilayah.setUserId(0);
		wilayahSession.insertWilayah(wilayah, tokenSession.findByToken(token));
		return wilayah;
	}

	@Path("/update")
	@POST
	public Wilayah updateWilayah(@FormParam("id") Integer id,
			@FormParam("lokasi_kode")String lokasi_kode,
			@FormParam("lokasi_nama")String lokasi_nama,
			@FormParam("lokasi_propinsi")Integer lokasi_propinsi,
			@FormParam("lokasi_kabupatenkota")Integer lokasi_kabupatenkota,
			@FormParam("lokasi_kecamatan")Integer lokasi_kecamatan,
			@FormParam("lokasi_kelurahan")Integer lokasi_kelurahan,
			@HeaderParam("Authorization") String token) {
		Wilayah wilayah = wilayahSession.find(id);
		wilayah.setLokasi_kode(lokasi_kode);
		wilayah.setLokasi_nama(lokasi_nama);
		wilayah.setLokasi_propinsi(lokasi_propinsi);
		wilayah.setLokasi_kabupatenkota(lokasi_kabupatenkota);
		wilayah.setLokasi_kelurahan(lokasi_kelurahan);
		wilayah.setLokasi_kecamatan(lokasi_kecamatan);
		wilayah.setUserId(0);
		wilayahSession.updateWilayah(wilayah, tokenSession.findByToken(token));
		return wilayah;
	}

	@Path("/delete/{id}")
	@GET
	public Wilayah deleteWilayah(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return wilayahSession.deleteWilayah(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public Wilayah deleteRowWilayah(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return wilayahSession.deleteRowWilayah(id, tokenSession.findByToken(token));
	}
}
