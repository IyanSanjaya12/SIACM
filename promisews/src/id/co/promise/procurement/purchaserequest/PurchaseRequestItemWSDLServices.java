/*package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.JsonObject;
import id.co.promise.procurement.vendor.VendorSession;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

@Stateless
@WebService(serviceName="PurchaseRequest", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaserequest.PurchaseRequestItemWSDLImpl")
public class PurchaseRequestItemWSDLServices implements
		PurchaseRequestItemWSDLImpl {
	
	final static Logger log = Logger.getLogger(PurchaseRequestItemWSDLServices.class);
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

	public PurchaseRequestItem getPurchaseRequestItemById(int id) {
		return purchaseRequestItemSession.getPurchaseRequestItem(id);
	}

	public List<PurchaseRequestItem> getPurchaseRequestItemList() {
		return purchaseRequestItemSession.getPurchaseRequestItemlist();
	}

	public List<PurchaseRequestItem> getPurchaseRequestItemListByPurchaseRequestId(int purchaserequest) {
		return purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestId(purchaserequest);
	}
	
	public List<PurchaseRequestItem> getPurchaseRequestItemListByPurchaseRequestJoinId(int purchaserequest) {
		List<PurchaseRequestItem> hasilList = new ArrayList<PurchaseRequestItem>();
		List<Integer> itemIdList = new ArrayList<Integer>();
		List<PurchaseRequestItem> prItemList = purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequestJoinId(purchaserequest);
		if(prItemList != null && prItemList.size() > 0){
			for(PurchaseRequestItem prItem : prItemList) {
				if(hasilList.size() > 0) {
					for(PurchaseRequestItem bandingPrItem : hasilList) {
						int retVal = Double.compare(bandingPrItem.getPrice(), prItem.getPrice());
						if(retVal > 0 || retVal < 0) {
							if(Double.compare(prItem.getPrice(), prItem.getPriceafterjoin()) == 0) {
								if(!itemIdList.contains(prItem.getItem().getId())) {
									itemIdList.add(prItem.getItem().getId());
									hasilList.add(prItem);
									break;
								}
							}
						}
					}
				} else {
					if(Double.compare(prItem.getPrice(), prItem.getPriceafterjoin()) == 0) {
						itemIdList.add(prItem.getItem().getId());
						hasilList.add(prItem);
					}
				}
			}
		}
		return hasilList;
	}

	public PurchaseRequestItem getByPurchaseRequestItemId(int purchaserequest) {
		return purchaseRequestItemSession.getPurchaseRequestItem(purchaserequest);
	}

	public PurchaseRequestItem createPurchaseRequestItem(Integer purchaserequest, Integer item, String itemname,
			String kode, String vendorName, Double quantity, Double price, String unit,
			String costcenteritem,  String status,  Integer matauang,  Integer vendor,
			String specification,  String total,  String pathfile,  String token) {

		PurchaseRequestItem x = new PurchaseRequestItem();
		x.setPurchaserequest(purchaseRequestSession.find(purchaserequest));
		x.setVendorname(vendorName);

		log.info("item = " + item);

		 Gak bakalan bisa contsarint table item dipaksa ke table catalog 
		
		 * if (item!=null) if(vendorName != null) { Catalog catalog =
		 * catalogSession.find(item); if(catalog != null) {
		 * x.setItem(itemSession.find(catalog.getItem().getId())); } } else {
		 * x.setItem(itemSession.find(item)); }
		 

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

	public PurchaseRequestItem createPurchaseRequestItemFreeText(Integer purchaserequest, String itemname,
			 Double quantity,  Double price,  String unit,  String specification,
			 String pathfile,  String status,  String costcenteritem,  String total,
			 String token) {

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
		x.setUserId(0);
		purchaseRequestItemSession.createPurchaseRequestItem(x, tokenSession.findByToken(token));
		return x;
	}

	public PurchaseRequestItem updatePurchaseRequestItem(Integer id, Integer purchaserequest, Integer item,
			String itemname, String kode, String vendorName,  Double quantity,
			Double price, String unit, String costcenter, String status,
			Integer matauang, String specification,  String token) {
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

	public PurchaseRequestItem deletePurchaseRequestItem(int id, String token) {
		return purchaseRequestItemSession.deletePurchaseRequestItem(id, tokenSession.findByToken(token));
	}

	public PurchaseRequestItem deleteRowPurchaseRequestItem(int id, String token) {
		return purchaseRequestItemSession.deleteRowPurchaseRequestItem(id, tokenSession.findByToken(token));
	}

	@SuppressWarnings("unused")
	public boolean deleteByPR(int id, String token) {
		PurchaseRequest pr = purchaseRequestSession.find(id);
		// Delete Shipping
		boolean shipping = shipPRSession.deleteShippingToPRByPR(id, tokenSession.findByToken(token));
		return purchaseRequestItemSession.deletePurchaseRequestItemByPrId(id, tokenSession.findByToken(token));

	}
	
	@SuppressWarnings("unused")
	public boolean deleteRowByPR(int id, String token) {
		PurchaseRequest pr = purchaseRequestSession.find(id);
		// Delete Shipping
		boolean shipping = shipPRSession.deleteRowShippingToPRByPR(id, tokenSession.findByToken(token));
		return purchaseRequestItemSession.deleteRowPurchaseRequestItemByPrId(id, tokenSession.findByToken(token));

	}

	public Response getPurchaseRequestItemListByPR(Integer prId, String token) {
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
			return Response.status(201).entity(purchaseRequestItemSession.getPurchaseRequestItemByPurchaseRequest(purchaseRequestSession.get(prId))).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public Response getPurchaseRequestItemByPRAndVendor(Integer prId, Integer vendorId, String token) {

		try {
			return Response.status(201).entity(purchaseRequestItemSession.getPurchaseRequestItemByPRAndVendor(purchaseRequestSession.find(prId), vendorSession.find(vendorId))).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// kirim 500 Internal Server Error
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}

	public JsonObject<PurchaseRequestItem> getPurchaseRequestItemPaggingList(String start, String draw, String length, String search, String order, String column, String token) {

		String start = httpServletRequest.getParameter("start");
		String draw = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");

		List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();

		List<Object[]> arrObjList = purchaseRequestItemSession.getPurchaseRequestItemPaggingList(Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order);

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

	public JsonObject<PurchaseRequestItem> getPurchaseRequestItemPaggingListForPrConsolidation(
			String start, String draw, String length, String search, String order, String column, String itemId, String prId, String token) {

		String start = httpServletRequest.getParameter("start");
		String draw = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");
		String itemId = httpServletRequest.getParameter("param[itemId]");
		String prId = httpServletRequest.getParameter("param[prId]");
		
		List<Integer> itemList = new ArrayList<Integer>();
		if(itemId != null) {
			String[] parts = itemId.split(",");
			for(int i=0;i<parts.length;i++) {
				itemList.add(Integer.valueOf(parts[i].trim()));
			}
		}
		
		List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();
		List<Object[]> arrObjList = new ArrayList<Object[]>();
		Integer size = 0;
		Double grandTotal = new Double(0);

		if(itemId != null) {
			arrObjList = purchaseRequestItemSession.getPurchaseRequestItemPaggingListForJoinPr(Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order, itemList, prId);
			for (Object[] obj1 : arrObjList) {
				grandTotal += (Double) obj1[5];
			}
		} else {
			arrObjList = purchaseRequestItemSession.getPurchaseRequestItemPaggingListForPrConsolidation(Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order);
		}

		for (Object[] obj : arrObjList) {
			PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
			purchaseRequestItem.setKode((String) obj[0]);
			purchaseRequestItem.setItemname((String) obj[1]);
			purchaseRequestItem.setQuantity((Double) obj[2]);
			purchaseRequestItem.setUnit((String) obj[3]);
			purchaseRequestItem.setPrice((Double) obj[4]);
			purchaseRequestItem.setTotal((Double) obj[5]);
			
			if(itemId != null) {
				purchaseRequestItem.setGrandTotal(grandTotal);
			}

			Item item = new Item();
			item.setId((Integer) obj[6]);
			item.setKode((String) obj[0]);
			item.setNama((String) obj[1]);

			purchaseRequestItem.setItem(item);

			purchaseRequestItemList.add(purchaseRequestItem);
		}

		if(itemId != null) {
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

	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItem(Integer id, String token) {

		return purchaseRequestItemSession.getPurchaseRequestItemVerifiedListByItem(id);
	}
	
	public List<PurchaseRequestItem> getPurchaseRequestItemVerifiedListByItemAndPrId(
			Integer id,  Integer prId,String token) {

		return purchaseRequestItemSession.getPurchaseRequestItemVerifiedListByItemAndPrId(id, prId);
	}
}
*/