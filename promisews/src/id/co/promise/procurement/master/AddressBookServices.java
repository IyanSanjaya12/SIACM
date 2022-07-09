/**
 * fdf
 */
package id.co.promise.procurement.master;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import id.co.promise.procurement.catalog.entity.CatalogFee;
import id.co.promise.procurement.catalog.session.CatalogFeeSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.JsonObject;

/**
 * @author User
 *
 */
@Stateless
@Path(value = "/procurement/master/AddressBookServices")
@Produces(MediaType.APPLICATION_JSON)
public class AddressBookServices {
	final static Logger log = Logger.getLogger(AddressBookServices.class);

	@EJB private AddressBookSession addressBookSession;
	@EJB private RoleUserSession roleUserSession;		
	@EJB private OrganisasiSession organisasiSession;
	@EJB private ProvinsiSession provinsiSession;
	@EJB private CatalogFeeSession catalogFeeSession;
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();

	@EJB TokenSession tokenSession;
	
	@EJB
	PurchaseOrderItemSession purchaseOrderItemSession;

	@Path("/saveAddressBook")
	@POST
	public AddressBook saveAddressBook(AddressBook addressBook, @HeaderParam("Authorization") String strToken) {
		Token token = tokenSession.findByToken(strToken);
		Organisasi organisasiAfco = organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId());
		if (organisasiAfco != null) {
			addressBook.setOrganisasi(organisasiAfco);
		}
		if (addressBook.getId() != null) {
			return addressBookSession.updateAddressBook(addressBook, token);
		} else {
			return addressBookSession.insertAddressBook(addressBook, token);
		}
	}
	
	@Path("/getAddressBook/{id}")
	@GET
	public AddressBook getAddressBook(@PathParam("id") int id) {
		AddressBook addressBook =  addressBookSession.getAddressBook(id);
		return addressBook;
	}

	@Path("/getList")
	@POST
	public JsonObject<AddressBook> getAddressBookList(
			@Context HttpServletRequest httpServletRequest,
			@HeaderParam("Authorization") String strToken
			) {
		String start = httpServletRequest.getParameter("start");
		String draw	 = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		
		//Integer vIndexSort = httpServletRequest.getParameter("order[0][column]");
		
		Integer vIndexSort = Integer.parseInt(httpServletRequest.getParameter("order[0][column]"));
		/*String orderField = null;
		if (vIndexSort != null && vIndexSort.length() > 0 && vIndexSort.equals("0") == false) {
			orderField = httpServletRequest.getParameter("columns["+vIndexSort+"][data]");
		}	*/	
		
		String keyword = (search != null && search.length() > 0) ? search : null;
		Token token = tokenSession.findByToken(strToken);
//		Organisasi organisasiAfco = organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId());
//		
//		Integer organisasiId = organisasiAfco.getId();
		
		JsonObject<AddressBook> jo = new JsonObject<AddressBook>();
		jo.setData(addressBookSession.getAddressBookPaggingListByOrganisasi(keyword, Integer.valueOf(start), Integer.valueOf(length), vIndexSort, order));
		Integer jmlData = addressBookSession.countAddressBookListByOrganisasi(keyword).intValue();
		jo.setRecordsTotal(jmlData);
		jo.setRecordsFiltered(jmlData);
		jo.setDraw(draw);
		
		return jo;
	}

	
	@Path("/getAddressBookListByToken")
	@GET
	public Response getAddressBookListByToken(
			@HeaderParam("Authorization") String token) {
			User user= tokenSession.findByToken(token).getUser();
			Organisasi organisasiAfco = organisasiSession.getAfcoOrganisasiByUserId(user.getId());
//			Organisasi rootOrg = organisasiSession.getRootParentByOrganisasi(organisasiAfco);
		try {
			return Response.status(201).entity(addressBookSession.getAddressBookListByOrganisasi(organisasiAfco.getId())).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
	
	@Path("/getAddressBookAllList")
	@GET
	public Response getAddressBookAllList(
			@HeaderParam("Authorization") String token) {
			User user= tokenSession.findByToken(token).getUser();
		try {
			return Response.status(201).entity(addressBookSession.getAddressBookList()).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	} 
	
	@Path("/insert")
	@POST
	public AddressBook insert(AddressBook addressBook,
			@HeaderParam("Authorization") String strToken){
			Token token = tokenSession.findByToken(strToken);
			Organisasi organisasiAfco = organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId());
			if (organisasiAfco != null) {
				addressBook.setOrganisasi(organisasiAfco);
			}
			return addressBookSession.insertAddressBook(addressBook, token);
		}
	
	@Path("/update")
	@POST
	public AddressBook update(AddressBook addressBook,
			@HeaderParam("Authorization") String token) {
		addressBookSession.updateAddressBook(addressBook, tokenSession.findByToken(token));
		return addressBook;
	}
	
	@Path("/delete")
	@POST
	public AddressBook delete(@FormParam("id") int id, 
			@HeaderParam("Authorization") String token) {
		return addressBookSession.deleteAddressBook(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow")
	@POST
	public AddressBook deleteRow(@FormParam("id") int id, 
			@HeaderParam("Authorization") String token) {
		return addressBookSession.deleteRowAddressBook(id, tokenSession.findByToken(token));
	}
	
	@Path("/getAllProvinsi")
	@GET
	public List<Provinsi> getAllProvinsi() {
		List<Provinsi> provinsiList =  provinsiSession.getProvinsiAll();
		return provinsiList;
	}
	
	@Path("/getAddressBookAllListActive")
	@GET
	public Response getAddressBookAllListActive(
			@HeaderParam("Authorization") String token) {
			User user= tokenSession.findByToken(token).getUser();
		try {
			return Response.status(201).entity(addressBookSession.getAddressBookListActive()).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
	@Path("/getAddressBookByname/{data}")
	@GET
	public Response getAddressBookByname(
			@HeaderParam("Authorization") String token,
			@PathParam("data") String nama) {
		
		try {
			List<AddressBook> addresBookList = addressBookSession.getAddressBookList();
			List<AddressBook> addresBookListNew = new ArrayList<AddressBook>();
			for(AddressBook addBook : addresBookList) {
				if(addBook.getAddressLabel().equalsIgnoreCase(nama)) {
					addresBookListNew.add(addBook);
				}
			}
			return Response.status(201).entity(addresBookListNew).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
	@Path("/getAddressBookByPurchaseRequestId/{prId}/{todo}")
	@GET
	public Response getAddressBookByPurchaseRequestId(@PathParam("prId") Integer prId, 
			@PathParam("todo") String todo, @HeaderParam("Authorization") String token) {
		
		try {
			List<AddressBookDTOForCreatePO> addressBookDTOForCreatePOList = addressBookSession.getAddressBookListByPurchaseRequestId(prId);
			Integer index =0;
			Integer index2 =0;
			List<AddressBookDTOForCreatePO> addressBookDTOForCreatePOListNew = new ArrayList<>();
			List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<>();
			for(AddressBookDTOForCreatePO addressBookDTOForCreatePO : addressBookDTOForCreatePOList) {
				PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
				AddressBookDTOForCreatePO addressBookDTOForCreatePONew = new AddressBookDTOForCreatePO();
				if(index > 0 && addressBookDTOForCreatePOList.get(index2 - 1).getAddressBook().getId().equals(addressBookDTOForCreatePO.getAddressBook().getId())) {
					purchaseRequestItem = addressBookDTOForCreatePO.getPurchaseRequestItem();
				}else {
					addressBookDTOForCreatePONew.setAddressBook(addressBookDTOForCreatePO.getAddressBook());
					purchaseRequestItem = addressBookDTOForCreatePO.getPurchaseRequestItem();
					if(todo.equalsIgnoreCase("detail")) {
						List<PurchaseOrderItem> poItemList = purchaseOrderItemSession.getPoItemByPrItemId(purchaseRequestItem.getId());
						if(poItemList.size() > 0) {
							addressBookDTOForCreatePONew.setDeliveryTime(poItemList.get(0).getDeliveryTime());
						}
						addressBookDTOForCreatePONew.setPurchaseOrderItemList(poItemList);
					}
					addressBookDTOForCreatePOListNew.add(addressBookDTOForCreatePONew);
					purchaseRequestItemList = new ArrayList<>();
					index ++;
				}
				purchaseRequestItemList.add(purchaseRequestItem);
				addressBookDTOForCreatePOListNew.get(index - 1).setPurchaseRequestItemList(purchaseRequestItemList);
				index2 ++;
			}
			Double grandTotal = new Double(0);
			for(AddressBookDTOForCreatePO adBookDTO : addressBookDTOForCreatePOListNew) {
				List<Double> subTotalList = new ArrayList<>();
				List<Double> ppnList = new ArrayList<>();
				Double subTotal = new Double(0);
				Double ppn = new Double(0);
				Double hasilAkhir = new Double(0);
				for(PurchaseRequestItem prItem : adBookDTO.getPurchaseRequestItemList() ) {
					subTotalList.add( (prItem.getQuantity() * prItem.getPrice()) + prItem.getOngkosKirim());
					ppnList.add(( (prItem.getQuantity() * prItem.getPrice()) + prItem.getOngkosKirim()) * 0.1/* atau 10% */);
					
					subTotal +=(prItem.getQuantity() * prItem.getPrice()) + prItem.getOngkosKirim();
					ppn += ((prItem.getQuantity() * prItem.getPrice()) + prItem.getOngkosKirim()) * 0.1;
					hasilAkhir += (((prItem.getQuantity() * prItem.getPrice()) + prItem.getOngkosKirim()) * 0.1) + ((prItem.getQuantity() * prItem.getPrice()) + prItem.getOngkosKirim());
				}
				adBookDTO.setSubTotalList(subTotalList);
				adBookDTO.setPpnList(ppnList);
				adBookDTO.setSubTotal(subTotal);
				adBookDTO.setPpn(ppn);
				adBookDTO.setHasilAkhir(hasilAkhir);
				grandTotal += hasilAkhir;
			}
			if(addressBookDTOForCreatePOListNew.size() > 0) {
				addressBookDTOForCreatePOListNew.get(0).setGrandTotal(grandTotal);
			}
			return Response.status(201).entity(addressBookDTOForCreatePOListNew).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
	
	@Path("/getAddressBookOrganisasiList")
	@POST
	public Response getAddressBookOrganisasiList(RequestQtyDTO requestQtyDTO,
			@HeaderParam("Authorization") String token) {
		try {
			List<CatalogFee> catalogFeeList = new ArrayList<>();
			List<Integer> organisasiIdList = new ArrayList<>();
			for(Integer catalogId : requestQtyDTO.getCatalogIdList()) {
				catalogFeeList.addAll(catalogFeeSession.getListCatalogFeeByCatalogId(catalogId));
			}
			for(CatalogFee catalogFee : catalogFeeList) {
				organisasiIdList.add(catalogFee.getOrganisasi().getId());
			}

			return Response.status(201).entity(addressBookSession.getAddressBookListByOrganisasiList(organisasiIdList)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
	
	@Path("/checkAddressBookByCatalog")
	@POST
	public Response checkAddressBookByCatalog(RequestQtyDTO requestQtyDTO,
			@HeaderParam("Authorization") String token) {
		try {
			List<CatalogFee> catalogFeeList = catalogFeeSession.getListCatalogFeeByCatalogId(requestQtyDTO.getCatalogId());
			AddressBook addresBook = addressBookSession.getAddressBook(requestQtyDTO.getAddresBookId());
			boolean isTrue = false;
			for(CatalogFee catalogFee : catalogFeeList) {
				if(catalogFee.getOrganisasi().getId().equals(addresBook.getOrganisasi().getId())) {
					isTrue = true;
				}
			}

			return Response.status(201).entity(isTrue).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
}
