package id.co.promise.procurement.master;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.promise.procurement.entity.ConditionalPrice;
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
@Path(value = "/procurement/master/conditionalPriceServices")
@Produces(MediaType.APPLICATION_JSON)
public class ConditionalPriceServices {

	@EJB
	private ConditionalPriceSession conditionalPriceSession;
	@EJB
	TokenSession tokenSession;

	@Path("/getById/{id}")
	@GET
	public ConditionalPrice getConditionalPrice(@PathParam("id") int id) {
		return conditionalPriceSession.getConditionalPrice(id);
	}

	@Path("/getByTipe/{tipe}")
	@GET
	public List<ConditionalPrice> getConditionalPriceListByTipe(
			@PathParam("tipe") int tipe) {
		return conditionalPriceSession.getConditionalPriceListByTipe(tipe);
	}

	@Path("/getList")
	@GET
	public List<ConditionalPrice> getConditionalPriceList() {
		return conditionalPriceSession.getConditionalPriceList();
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Path("/insert")
	@POST
	public Map insert(ConditionalPrice conditionalPrice,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "insert";

		Boolean isSaveKode = conditionalPriceSession.checkKodeConditionalPrice(conditionalPrice.getKode(), toDo, conditionalPrice.getId());

		if (!isSaveKode) {
			String errorMessageKode = "Kode Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMessageKode", errorMessageKode);
		}

		if (isSaveKode) {
			conditionalPriceSession.insertConditionalPrice(conditionalPrice, tokenSession.findByToken(token));
		}

		map.put("conditionalPrice", conditionalPrice);

		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	@Path("/update")
	@POST
	public Map update(ConditionalPrice conditionalPrice,
			@HeaderParam("Authorization") String token) {

		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "update";

		Boolean isSaveKode = conditionalPriceSession.checkKodeConditionalPrice(conditionalPrice.getKode(), toDo, conditionalPrice.getId());

		if (!isSaveKode) {
			String errorMessageKode = "Kode Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorMessageKode", errorMessageKode);
		}

		if (isSaveKode) {
			conditionalPriceSession.updateConditionalPrice(conditionalPrice, tokenSession.findByToken(token));
		}

		map.put("conditionalPrice", conditionalPrice);

		return map;
	}

	@Path("/delete")
	@POST
	public ConditionalPrice deleteConditionalPrice(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return conditionalPriceSession.deleteConditionalPrice(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@POST
	public ConditionalPrice deleteRowConditionalPrice(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return conditionalPriceSession.deleteRowConditionalPrice(id, tokenSession.findByToken(token));
	}
	
}
