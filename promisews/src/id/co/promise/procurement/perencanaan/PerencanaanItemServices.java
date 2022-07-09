package id.co.promise.procurement.perencanaan;

import id.co.promise.procurement.entity.PerencanaanItem;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.MataUangSession;
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
@Path(value = "/procurement/perencanaan/perencanaanItemServices")
@Produces(MediaType.APPLICATION_JSON)
public class PerencanaanItemServices {

	@EJB
	private PerencanaanItemSession perencanaanItemSession;

	@EJB
	private PerencanaanSession perencanaanSession;

	@EJB
	private ItemSession itemSession;

	@EJB
	private MataUangSession mataUangSession;

	@EJB
	TokenSession tokenSession;

	@Path("/getList")
	@GET
	public List<PerencanaanItem> getList() {
		return perencanaanItemSession.getList();
	}

	@Path("/getId/{id}")
	@GET
	public PerencanaanItem getId(@PathParam("id") int id) {
		return perencanaanItemSession.get(id);
	}

	@Path("/getListByPerencanaanIdAndItemType/{perencanaanId}/{itemTypeId}")
	@GET
	public List<PerencanaanItem> getListPerencanaanItemByPerencanaanAndItemType(
			@PathParam("perencanaanId") int perencanaanId,
			@PathParam("itemTypeId") int itemTypeId) {
		return perencanaanItemSession
				.getListPerencanaanItemByPerencanaanAndItemType(perencanaanId,
						itemTypeId);
	}

	@Path("/getListByPerencanaanIdAndItemId/{perencanaanId}/{itemId}")
	@GET
	public List<PerencanaanItem> getListByPerencanaanAndItem(
			@PathParam("perencanaanId") int perencanaanId,
			@PathParam("itemId") int itemId) {
		return perencanaanItemSession.getListByPerencanaanAndItem(
				perencanaanId, itemId);
	}

	@Path("/insert")
	@POST
	public PerencanaanItem create(
			@FormParam("perencanaan") Integer perencanaan,
			@FormParam("item") Integer item,
			@FormParam("jumlah") Integer jumlah,
			@FormParam("nilai") Double nilai, @FormParam("sisa") Double sisa,
			@FormParam("mataUang") Integer mataUang,
			@HeaderParam("Authorization") String token) {

		PerencanaanItem pr = new PerencanaanItem();
		pr.setPerencanaan(perencanaanSession.find(perencanaan));
		pr.setItem(itemSession.find(item));
		pr.setJumlah(jumlah);
		pr.setNilai(nilai);
		pr.setSisa(sisa);
		pr.setMataUang(mataUangSession.getMataUang(mataUang));
		pr.setUserId(0);
		perencanaanItemSession.insertPerencanaanItem(pr,
				tokenSession.findByToken(token));
		return pr;
	}

	@Path("/update")
	@POST
	public PerencanaanItem update(@FormParam("id") Integer id,
			@FormParam("perencanaan") Integer perencanaan,
			@FormParam("item") Integer item,
			@FormParam("jumlah") Integer jumlah,
			@FormParam("nilai") Double nilai, @FormParam("sisa") Double sisa,
			@FormParam("mataUang") Integer mataUang,
			@HeaderParam("Authorization") String token) {

		PerencanaanItem pr = perencanaanItemSession.find(id);
		pr.setPerencanaan(perencanaanSession.find(perencanaan));
		pr.setItem(itemSession.find(item));
		pr.setJumlah(jumlah);
		pr.setNilai(nilai);
		pr.setSisa(sisa);
		pr.setMataUang(mataUangSession.getMataUang(mataUang));
		pr.setUserId(0);
		perencanaanItemSession.updatePerencanaanItem(pr,
				tokenSession.findByToken(token));
		return pr;
	}

	@Path("/delete/{id}")
	@GET
	public PerencanaanItem delete(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return perencanaanItemSession.deletePerencanaanItem(id,
				tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public PerencanaanItem deleteRow(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return perencanaanItemSession.deleteRowPerencanaanItem(id,
				tokenSession.findByToken(token));
	}
}
