package id.co.promise.procurement.purchaserequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.JsonObject;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path("/procurement/purchaseRequestItemServices")
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseRequestItemServices {
	final static Logger log = Logger.getLogger(PurchaseRequestItemServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	@EJB
	private VendorSession vendorSession;
	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;
	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	@EJB
	private ItemSession itemSession;
	@EJB
	private CatalogSession catalogSession;
	@EJB
	private MataUangSession mataUangSession;
	@EJB
	private ShippingToPRSession shipPRSession;
	@EJB
	TokenSession tokenSession;
	@EJB
	OrganisasiSession organisasiSession;

	@Path("/getId/{id}")
	@GET
	public PurchaseRequestItem getPurchaseRequestItem(@PathParam("id") int id) {
		return purchaseRequestItemSession.getPurchaseRequestItem(id);
	}

	@Path("/getList")
	@GET
	public List<PurchaseRequestItem> getPurchaseRequestItemList() {
		return purchaseRequestItemSession.getPurchaseRequestItemlist();
	}

	@Path("/getListByPurchaseRequestId/{id}")
	@GET
	public List<PurchaseRequestItem> getPurchaseRequestItemByPurchaseRequestId(@PathParam("id") int purchaserequest) {
		return purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaserequest);
	}
	
	@Path("/getListByPurchaseRequestId")
	@POST
	public List<PurchaseRequestItem> getListByPurchaseRequestId(Integer purchaserequestId) {
		List<PurchaseRequestItem> purchaseRequestItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaserequestId);		 
		for(PurchaseRequestItem purchaseRequestItem : purchaseRequestItemList ) {
			 Integer slaDeliveryTime = purchaseRequestItem.getSlaDeliveryTime();
			 if(slaDeliveryTime !=null) {
				 Date currentDate = new Date();
				 Calendar calender = Calendar.getInstance();
				 calender.setTime(currentDate);
				 calender.add(Calendar.DATE, slaDeliveryTime);
				 Date currentDatePlusOne = calender.getTime();
				 purchaseRequestItem.setEstimatedDeliveryTime(currentDatePlusOne);
			 }		
			 Integer rootParent = null;
			 /*KAI - 20201129 - #20666*/
			 Organisasi organisasi = organisasiSession.getAfcoOrganisasiByParentId(purchaseRequestItem.getPurchaserequest().getOrganisasi().getId());
			 if (organisasi != null) {
				 rootParent = organisasi.getId();
			 }
			 purchaseRequestItem.getPurchaserequest().setRootParent(rootParent);
		}
		return purchaseRequestItemList;
	}

	@Path("/getListByPurchaseRequestJoinId/{id}")
	@GET
	public List<PurchaseRequestItem> getPurchaseRequestItemByPurchaseRequestJoinId(
			@PathParam("id") int purchaserequest) {
		List<PurchaseRequestItem> hasilList = new ArrayList<PurchaseRequestItem>();
		List<Integer> itemIdList = new ArrayList<Integer>();
		List<PurchaseRequestItem> prItemList = purchaseRequestItemSession
				.getPurchaseRequestItemByPurchaseRequestJoinId(purchaserequest);
		if (prItemList != null && prItemList.size() > 0) {
			for (PurchaseRequestItem prItem : prItemList) {
				if (hasilList.size() > 0) {
					for (PurchaseRequestItem bandingPrItem : hasilList) {
						int retVal = Double.compare(bandingPrItem.getPrice(), prItem.getPrice());
						if (retVal > 0 || retVal < 0) {
							if (Double.compare(prItem.getPrice(), prItem.getPriceafterjoin()) == 0) {
								if (!itemIdList.contains(prItem.getItem().getId())) {
									itemIdList.add(prItem.getItem().getId());
									hasilList.add(prItem);
									break;
								}
							}
						}
					}
				} else {
					if (Double.compare(prItem.getPrice(), prItem.getPriceafterjoin()) == 0) {
						itemIdList.add(prItem.getItem().getId());
						hasilList.add(prItem);
					}
				}
			}
		}
		return hasilList;
	}

	@Path("/getByPurchaseRequestItemId/{id}")
	@GET
	public PurchaseRequestItem getByPurchaseRequestItemId(@PathParam("id") int purchaserequest) {
		return purchaseRequestItemSession.getPurchaseRequestItem(purchaserequest);
	}

	@Path("/insert")
	@POST
	public PurchaseRequestItem createPurchaseRequestItem(@FormParam("purchaserequestId") Integer purchaserequest,
			@FormParam("itemId") Integer item, @FormParam("itemname") String itemname, @FormParam("kode") String kode,
			@FormParam("vendorName") String vendorName, @FormParam("quantity") Double quantity,
			@FormParam("price") Double price, @FormParam("unit") String unit,
			@FormParam("costcenter") String costcenteritem, @FormParam("status") String status,
			@FormParam("mataUangId") Integer matauang, @FormParam("vendorId") Integer vendor,
			@FormParam("specification") String specification, @FormParam("total") String total,
			@FormParam("pathfile") String pathfile, @HeaderParam("Authorization") String token) {

		PurchaseRequestItem x = new PurchaseRequestItem();
		x.setPurchaserequest(purchaseRequestSession.find(purchaserequest));
		x.setVendorname(vendorName);

		log.info("item = " + item);

		/* Gak bakalan bisa contsarint table item dipaksa ke table catalog */
		/*
		 * if (item!=null) if(vendorName != null) { Catalog catalog =
		 * catalogSession.find(item); if(catalog != null) {
		 * x.setItem(itemSession.find(catalog.getItem().getId())); } } else {
		 * x.setItem(itemSession.find(item)); }
		 */

		if (item != null) {
			Item i = itemSession.find(item);
			if (i != null) {
				x.setItem(itemSession.find(item));
			}
		}

		x.setItemname(itemname);
		x.setKode(kode);

		x.setQuantity(quantity);
		x.setQuantitybalance(x.getQuantity());
		x.setPrice(price);
		x.setUnit(unit);
		x.setCostcenter(costcenteritem);
		x.setStatus(status);
		x.setMataUang(mataUangSession.find(matauang));

		if (vendor != null) {
			Vendor v = vendorSession.find(vendor);
			if (v != null) {
				x.setVendor(vendorSession.find(vendor));
			}

		}

		x.setSpecification(specification);
		x.setTotal(Double.parseDouble(total));
		x.setUserId(0);
		x.setPath(pathfile);
		purchaseRequestItemSession.createPurchaseRequestItem(x, tokenSession.findByToken(token));
		return x;
	}

	@Path("/insertPurchaseItemFreeText")
	@POST
	public PurchaseRequestItem createPurchaseRequestItemFreeText(
			@FormParam("purchaserequestId") Integer purchaserequest, @FormParam("itemname") String itemname,
			@FormParam("quantity") Double quantity, @FormParam("price") Double price, @FormParam("unit") String unit,
			@FormParam("specification") String specification, @FormParam("pathfile") String pathfile,
			@FormParam("status") String status, @FormParam("costcenter") String costcenteritem,
			@FormParam("total") String total, @FormParam("mataUangId") Integer matauang, @HeaderParam("Authorization") String token) {

		PurchaseRequestItem x = new PurchaseRequestItem();
		x.setPurchaserequest(purchaseRequestSession.find(purchaserequest));
		x.setItemname(itemname);
		x.setQuantity(quantity);
		x.setQuantitybalance(x.getQuantity());
		x.setPrice(price);
		x.setUnit(unit);
		x.setSpecification(specification);
		x.setPath(pathfile);
		x.setStatus(status);
		x.setCostcenter(costcenteritem);
		x.setTotal(Double.parseDouble(total));
		x.setMataUang(mataUangSession.find(matauang));
		x.setUserId(0);
		purchaseRequestItemSession.createPurchaseRequestItem(x, tokenSession.findByToken(token));
		return x;
	}

	@Path("/update")
	@POST
	public PurchaseRequestItem updatePurchaseRequestItem(@FormParam("id") Integer id,
			@FormParam("purchaseRequest") Integer purchaserequest, @FormParam("itemId") Integer item,
			@FormParam("itemname") String itemname, @FormParam("kode") String kode,
			@FormParam("vendorName") String vendorName, @FormParam("quantity") Double quantity,
			@FormParam("price") Double price, @FormParam("unit") String unit,
			@FormParam("costcenter") String costcenter, @FormParam("status") String status,
			@FormParam("mataUangId") Integer matauang, @FormParam("specification") String specification,
			@HeaderParam("Authorization") String token) {
		PurchaseRequestItem x = purchaseRequestItemSession.find(id);
		x.setPurchaserequest(purchaseRequestSession.find(purchaserequest));
		x.setItem(itemSession.find(item));
		x.setItemname(itemname);
		x.setVendorname(vendorName);
		x.setQuantity(quantity);
		x.setPrice(price);
		x.setUnit(unit);
		x.setCostcenter(costcenter);
		x.setStatus(status);
		x.setMataUang(mataUangSession.find(matauang));
		x.setSpecification(specification);
		purchaseRequestItemSession.updatePurchaseRequestItem(x, tokenSession.findByToken(token));
		return x;
	}

	@Path("/delete/{id}")
	@GET
	public PurchaseRequestItem deletePurchaseRequestItem(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return purchaseRequestItemSession.deletePurchaseRequestItem(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRow/{id}")
	@GET
	public PurchaseRequestItem deleteRowPurchaseRequestItem(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return purchaseRequestItemSession.deleteRowPurchaseRequestItem(id, tokenSession.findByToken(token));
	}

	@SuppressWarnings("unused")
	@Path("/deleteByPR/{id}")
	@GET
	public boolean deleteByPR(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		PurchaseRequest pr = purchaseRequestSession.find(id);
		// Delete Shipping
		boolean shipping = shipPRSession.deleteShippingToPRByPR(id, tokenSession.findByToken(token));
		return purchaseRequestItemSession.deletePurchaseRequestItemByPrId(id, tokenSession.findByToken(token));

	}

	@SuppressWarnings("unused")
	@Path("/deleteRowByPR/{id}")
	@GET
	public boolean deleteRowByPR(@PathParam("id") int id, @HeaderParam("Authorization") String token) {
		PurchaseRequest pr = purchaseRequestSession.find(id);
		// Delete Shipping
		boolean shipping = shipPRSession.deleteRowShippingToPRByPR(id, tokenSession.findByToken(token));
		return purchaseRequestItemSession.deleteRowPurchaseRequestItemByPrId(id, tokenSession.findByToken(token));

	}

	@Path("/getPurchaseRequestItemListByPR")
	@POST
	public Response getPurchaseRequestItemListByPR(@FormParam("prId") Integer prId,
			@HeaderParam("Authorization") String token) {
		try {
			if (!(prId instanceof Integer)) {
				throw new RuntimeException("Parameter bukan Numerik");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 400 Bad Request
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}
		try {
			return Response.status(201).entity(purchaseRequestItemSession
					.getPurchaseRequestItemByPurchaseRequest(purchaseRequestSession.get(prId))).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/getPurchaseRequestItemByPRAndVendor")
	@POST
	public Response getPurchaseRequestItemByPRAndVendor(@FormParam("prId") Integer prId,
			@FormParam("vendorId") Integer vendorId, @HeaderParam("Authorization") String token) {

		try {
			return Response.status(201).entity(purchaseRequestItemSession.getPurchaseRequestItemByPRAndVendor(
					purchaseRequestSession.find(prId), vendorSession.find(vendorId))).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	@Path("/getPurchaseRequestItemPaggingList")
	@POST
	public JsonObject<PurchaseRequestItem> getPurchaseRequestItemPaggingList(
			@Context HttpServletRequest httpServletRequest, @HeaderParam("Authorization") String token) {

		String start = httpServletRequest.getParameter("start");
		String draw = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");

		List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();

		List<Object[]> arrObjList = purchaseRequestItemSession.getPurchaseRequestItemPaggingList(Integer.valueOf(start),
				Integer.valueOf(length), search, Integer.valueOf(column), order);

		for (Object[] obj : arrObjList) {
			PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
			purchaseRequestItem.setKode((String) obj[0]);
			purchaseRequestItem.setItemname((String) obj[1]);
			purchaseRequestItem.setQuantity((Double) obj[2]);
			purchaseRequestItem.setUnit((String) obj[3]);
			purchaseRequestItem.setPrice((Double) obj[4]);
			purchaseRequestItem.setTotal((Double) obj[5]);

			Item item = new Item();
			item.setId((Integer) obj[6]);
			item.setKode((String) obj[0]);
			item.setNama((String) obj[1]);

			purchaseRequestItem.setItem(item);

			purchaseRequestItemList.add(purchaseRequestItem);
		}

		Integer size = purchaseRequestItemSession.getPurchaseRequestItemPaggingSize(search);

		JsonObject<PurchaseRequestItem> jo = new JsonObject<PurchaseRequestItem>();
		jo.setData(purchaseRequestItemList);
		jo.setRecordsTotal(size);
		jo.setRecordsFiltered(size);
		jo.setDraw(draw);
		return jo;
	}

	@Path("/getPurchaseRequestItemPaggingListForPrConsolidation")
	@POST
	public JsonObject<PurchaseRequestItem> getPurchaseRequestItemPaggingListForPrConsolidation(
			@Context HttpServletRequest httpServletRequest, @HeaderParam("Authorization") String token) {

		String start = httpServletRequest.getParameter("start");
		String draw = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");
		String itemId = httpServletRequest.getParameter("param[itemId]");
		String prId = httpServletRequest.getParameter("param[prId]");

		List<Integer> itemList = new ArrayList<Integer>();
		if (itemId != null) {
			String[] parts = itemId.split(",");
			for (int i = 0; i < parts.length; i++) {
				itemList.add(Integer.valueOf(parts[i].trim()));
			}
		}

		List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
		List<Object[]> arrObjList = new ArrayList<Object[]>();
		Integer size = 0;
		Double grandTotal = new Double(0);

		if (itemId != null) {
			arrObjList = purchaseRequestItemSession.getPurchaseRequestItemPaggingListForJoinPr(Integer.valueOf(start),
					Integer.valueOf(length), search, Integer.valueOf(column), order, itemList, prId);
			for (Object[] obj1 : arrObjList) {
				grandTotal += (Double) obj1[5];
			}
		} else {
			arrObjList = purchaseRequestItemSession.getPurchaseRequestItemPaggingListForPrConsolidation(
					Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order);
		}

		for (Object[] obj : arrObjList) {
			PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
			purchaseRequestItem.setKode((String) obj[0]);
			purchaseRequestItem.setItemname((String) obj[1]);
			purchaseRequestItem.setQuantity((Double) obj[2]);
			purchaseRequestItem.setUnit((String) obj[3]);
			purchaseRequestItem.setPrice((Double) obj[4]);
			purchaseRequestItem.setTotal((Double) obj[5]);

			if (itemId != null) {
				purchaseRequestItem.setGrandTotal(grandTotal);
			}

			Item item = new Item();
			item.setId((Integer) obj[6]);
			item.setKode((String) obj[0]);
			item.setNama((String) obj[1]);

			purchaseRequestItem.setItem(item);

			purchaseRequestItemList.add(purchaseRequestItem);
		}

		if (itemId != null) {
			size = purchaseRequestItemSession.getPurchaseRequestItemPaggingSizeForJoinPr(search, itemList, prId);
		} else {
			size = purchaseRequestItemSession.getPurchaseRequestItemPaggingSizeForPrConsolidation(search);
		}

		JsonObject<PurchaseRequestItem> jo = new JsonObject<PurchaseRequestItem>();
		jo.setData(purchaseRequestItemList);
		jo.setRecordsTotal(size);
		jo.setRecordsFiltered(size);
		jo.setDraw(draw);
		return jo;
	}

	@Path("/getPurchaseRequestItemVerifiedListByItem/{id}")
	@GET
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItem(@PathParam("id") Integer id,
			@HeaderParam("Authorization") String token) {

		return purchaseRequestItemSession.getPurchaseRequestItemVerifiedListByItem(id);
	}

	@Path("/getPurchaseRequestItemVerifiedListByItem/{id}/{prId}")
	@GET
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItemAndPrId(@PathParam("id") Integer id,
			@PathParam("prId") Integer prId, @HeaderParam("Authorization") String token) {

		return purchaseRequestItemSession.getPurchaseRequestItemVerifiedListByItemAndPrId(id, prId);
	}

	@Path("/getPurchaseRequestItemVerifiedListByItemAndPrJoinId/{id}/{prJointId}")
	@GET
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItemAndPrJoinId(@PathParam("id") Integer id,
			@PathParam("prJointId") Integer prJoinId) {
		return purchaseRequestItemSession.getPurchaseRequestItemVerifiedListByItemAndPrJoinId(id, prJoinId);
	}
	
	@Path("/getListByPrId/{prId}")
	@GET
	public List<PurchaseRequestItem> getByPrId(@PathParam("prId") int prId) {
		return purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(prId);
	}
	
	@Path("/getTotalProductSold")
	@POST
	public Integer getTotalProductSold(@FormParam("catalogId") Integer catalogId, @FormParam("itemId") Integer itemId) {
		 
		return purchaseRequestItemSession.getTotalProductSold(catalogId, itemId);
	}
}
