package erp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import erp.interfacing.entity.DataOrgListERPResponse;
import erp.interfacing.entity.GetMasterItemERPRequest;
import erp.interfacing.entity.GetMasterItemERPResponse;
import id.co.promise.procurement.audit.SyncSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemOrganisasi;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.ItemOrganisasiSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.ItemTypeSession;
import id.co.promise.procurement.master.ProcedureSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.Utils;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
@Path(value = "/catalog/interfacing/itemInterfacingService")
@Produces(MediaType.APPLICATION_JSON)
public class ItemInterfacingService {
	
	final static Logger log = Logger.getLogger(ItemInterfacingService.class);
	@EJB
	private SyncSession syncSession;
	
	@EJB
	private ItemSession itemSession;
	
	@EJB
	private SatuanSession satuanSession;
	
	@EJB
	private AddressBookSession addressBookSession;
	
	@EJB
	private ItemOrganisasiSession itemOrganisasiSession;
	
	@EJB
	private ItemTypeSession itemTypeSession;
	
	@Resource private UserTransaction userTransaction;
	@EJB ProcedureSession procedureSession;
	
	
	@SuppressWarnings("unused")
	public Boolean SyncMasterItem(){
		// return true kalo suksess smua 
		// false kalau ada yg gagal
		// insert ke sync log gagal atau behasilnya
		
		return null;
	}

	@Path("/getMasterItem")
	@POST
	@SuppressWarnings("rawtypes")
	public Response getItemInterfacing(GetMasterItemERPRequest getMasterItemERPRequest) throws SQLException {
		String urlInterface = Constant.INTERFACING_BACKEND_ADDRESS_EBS + "/getMasterItem";
		Boolean isInsert = false;
		String externalId = "";

		try {
			ResponseObject responseObj = Utils.doPost(getMasterItemERPRequest, urlInterface);
			String responseString = responseObj.getResponseString();
			String statusCode = responseObj.getStatusCode();
			List<GetMasterItemERPResponse> getMasterItemERPResponses = new ArrayList<GetMasterItemERPResponse>();
			Integer count = 0;
			
			JSONObject jsonObj = new JSONObject(responseString);

			//Get Response Description from API (Interfacing)
			JSONObject jsonObjStatus = jsonObj.getJSONObject("status");
			String srcRespDesc = jsonObjStatus.getString("srcRespDesc");
			externalId = responseObj.getExternalId() != null ? responseObj.getExternalId() : "-";
			
			if (statusCode.equalsIgnoreCase("200")) {
				JSONArray jsonBody = jsonObj.getJSONArray("body");

				Gson gson = new Gson();
				getMasterItemERPResponses = gson.fromJson(jsonBody.toString(),
						new TypeToken<List<GetMasterItemERPResponse>>() {
						}.getType());

				for (GetMasterItemERPResponse getMasterItemERPResponse : getMasterItemERPResponses) 
				{
					if (getMasterItemERPResponse.getItemCode() != null) { 
						List<DataOrgListERPResponse> dataOrgListERPResponses = new ArrayList<DataOrgListERPResponse>();
						dataOrgListERPResponses = getMasterItemERPResponse.getOrgList();

						userTransaction.begin();
						Item item = itemSession.getByItemKodeEbs(getMasterItemERPResponse.getItemCode());
						Satuan satuan = satuanSession.getByCode(getMasterItemERPResponse.getUomCode());

						if (satuan == null) {
							satuan = new Satuan();
							satuan.setCode(getMasterItemERPResponse.getUomCode());
							satuan.setNama(getMasterItemERPResponse.getUomDesc());
							satuanSession.insertSatuan(satuan, null);
						}

						if (item == null) {
							item = new Item();
							isInsert = true;
						} else {
							isInsert = false;
						}

						item.setItemIdEbs(getMasterItemERPResponse.getItemId().toString());
						item.setKode(getMasterItemERPResponse.getItemCode());
						item.setDeskripsi(getMasterItemERPResponse.getDescription());
						item.setNama(getMasterItemERPResponse.getDescription());
						item.setSatuanId(satuan);

						if (!isInsert) {
							itemSession.updateItem(item, null);
						} else {
							itemSession.insertItem(item, null);
						}

						if (getMasterItemERPResponse.getOrgList() != null) {
							itemOrganisasiSession.deleteItemOrganisasiByItemId(item.getId());
							for (DataOrgListERPResponse dataOrgListERPResponse : dataOrgListERPResponses) {
								AddressBook addressBook = addressBookSession
										.getByAddressCodeEbs(dataOrgListERPResponse.getOrganizationCode());

								if (addressBook != null && item != null) {
									ItemOrganisasi itemOrganisasi = new ItemOrganisasi();
									addressBook.setAddressCodeEbs(dataOrgListERPResponse.getOrganizationCode());
									itemOrganisasi.setOrganisasi(addressBook.getOrganisasi());
									item.setKode(dataOrgListERPResponse.getItemCode());
									itemOrganisasi.setItem(item);

									itemOrganisasiSession.insertItemOrganisasi(itemOrganisasi, null);
								}
							}
						}
						userTransaction.commit();
						procedureSession.execute(Constant.DO_SYNC_ITEM, item.getKode());
						count ++;
					}else {
						log.info(">> List Not Found");
					}
				}
			} else {
				syncSession.create(urlInterface, getMasterItemERPRequest, Response.error("Error : " + srcRespDesc + " , " + responseString),
						Constant.LOG_METHOD_POST, Constant.LOG_SERVICE_NAME_GET_ITEM, externalId);
				return Response.error("Error : " + srcRespDesc);
			}
			// create log
			syncSession.create(urlInterface, getMasterItemERPRequest, Response.ok(getMasterItemERPResponses, count), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_ITEM, externalId);
			return Response.ok(getMasterItemERPResponses, count);
		} catch (Exception e) {
			// create log
			syncSession.create(urlInterface, getMasterItemERPRequest, Response.error(e), Constant.LOG_METHOD_GET,
					Constant.LOG_SERVICE_NAME_GET_ITEM, externalId);
			return Response.error(e);
		}

	}

}
