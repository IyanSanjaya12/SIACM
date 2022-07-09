/*package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.approval.ApprovalLevelSession;
import id.co.promise.procurement.approval.ApprovalProcessSession;
import id.co.promise.procurement.approval.ApprovalProcessTypeSession;
import id.co.promise.procurement.approval.ApprovalSession;
import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PRStatus;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestImportDTO;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.ShippingToPR;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.AddressBookDTO;
import id.co.promise.procurement.master.AddressBookSession;
import id.co.promise.procurement.master.AfcoSession;
import id.co.promise.procurement.master.ItemSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.TahapanSession;
import id.co.promise.procurement.master.TermAndConditionSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.template.TemplateXls;
import id.co.promise.procurement.utils.BuatParsingJSONDateTypeAdapter;
import id.co.promise.procurement.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Stateless
@WebService(serviceName="PurchaseRequest", 
targetNamespace = "http://promise.co.id/wsdl",
endpointInterface="id.co.promise.procurement.purchaserequest.ExcelPurchaseRequestWSDLImpl")
public class ExcelPurchaseRequestWSDLServices implements
		ExcelPurchaseRequestWSDLImpl {

	final static Logger log = Logger.getLogger(ExcelPurchaseRequestWSDLServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	final Gson gson;

	public ExcelPurchaseRequestWSDLServices() {
		// BuatParsingJSONDateTypeAdapter
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new BuatParsingJSONDateTypeAdapter());
		gson = builder.create();
	}

	@EJB
	private PurchaseRequestSession purchaseRequestSession;
	@EJB
	private PurchaseRequestItemSession purchaseRequestItemSession;
	@EJB
	private TokenSession tokenSession;
	@EJB
	private AfcoSession afcoSession;
	@EJB
	private TermAndConditionSession termAndConditionSession;
	@EJB
	private RoleUserSession roleUserSession;
	@EJB
	private TahapanSession tahapanSession;
	@EJB
	private ApprovalProcessTypeSession approvalProcessTypeSession;
	@EJB
	private ApprovalProcessSession approvalProcessSession;
	@EJB
	private ApprovalLevelSession approvalLevelSession;
	@EJB
	private ApprovalSession approvalSession;
	@EJB
	private OrganisasiSession organisasiSession;
	@EJB
	private UserSession userSession;
	@EJB
	private ItemSession itemSession;
	@EJB
	private AddressBookSession addressBookSession;
	@EJB
	private ShippingToPRSession shippingToPRSession;
	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;

	public Response upload( String token) {
		MultipartFormDataInput input = null;
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		String returnMsg = null;

		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName = TemplateXls.getFileNameTemplateXls(header);
			Workbook workbook = null;

			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				if (fileName.endsWith("xlsx")) {
					workbook = new XSSFWorkbook(inputStream);
				} else if (fileName.endsWith("xls")) {
					workbook = new HSSFWorkbook(inputStream);
				}
				log.info("fileNames = " + fileName);
				returnMsg = importPRFromExcel(workbook, token);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String json = "{\"msg\":\"" + returnMsg + "\"}";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

	protected String importPRFromExcel(Workbook workbook, String strToken) {
		List<PurchaseRequestImportDTO> purchaseRequestImportDTOList = new ArrayList<PurchaseRequestImportDTO>();

		Token token = tokenSession.findByToken(strToken);

		// PR HEADER in sheet 0
		Sheet prSheet = workbook.getSheetAt(0);
		Sheet itemSheet = workbook.getSheetAt(1);
		Sheet shippingSheet = workbook.getSheetAt(2);

		int jumlahRow = prSheet.getLastRowNum();
		log.info("Get PR sheet. Number of row = " + jumlahRow);

		for (int a = 1; a <= jumlahRow; a++) {

			Row row = prSheet.getRow(a);

			PurchaseRequestImportDTO purchaseRequestImportDTO = new PurchaseRequestImportDTO();
			PurchaseRequest purchaseRequest = new PurchaseRequest();

			 PR NUMBER 
			Cell cell = row.getCell(0);
			log.info("CELL TYPE PR NUMBER = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {

				String prNumber = cell.getStringCellValue().trim();
				 validate if pr number exist 
				List<PurchaseRequest> prList = purchaseRequestSession.getPurchaseRequestByNumber(prNumber);
				if (prList.size() > 0) {
					return "ERROR!, PR with Number " + prNumber + " already exist!";
				}

				purchaseRequest.setPrnumber(prNumber);
			} else {
				return "ERROR!, PR Number is Not String";
			}

			// Required Date
			cell = row.getCell(1);
			log.info("CELL TYPE Required Date = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
				purchaseRequest.setDaterequired(cell.getDateCellValue());
			} else {
				return "ERROR!, PR Required Date is Not Numeric";
			}

			// Cost Center
			cell = row.getCell(2);
			log.info("CELL TYPE Cost Center = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));

			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String nomorDraft = cell.getStringCellValue();
				List<AlokasiAnggaran> alokasiAnggaranList = alokasiAnggaranSession.findByNomorDraft(nomorDraft);
				if (alokasiAnggaranList.size() == 0) {
					return "ERROR! No cost center found with draft number " + nomorDraft + " for PR";
				}
				purchaseRequest.setCostcenter(cell.getStringCellValue());
			} else {
				return "ERROR!, PR Cost Center is Not String";
			}

			// Departement
			cell = row.getCell(3);
			log.info("CELL TYPE Departement = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				purchaseRequest.setDepartment(cell.getStringCellValue());
				List<Organisasi> organisasiList = organisasiSession.findByName(cell.getStringCellValue());
				if (organisasiList.size() == 0) {
					return "ERROR! No organisasi found with name " + cell.getStringCellValue() + " for PR";
				}

				Afco afco = afcoSession.findByOrganisasiId(organisasiList.get(0).getId());

				if (afco == null) {
					return "ERROR! No afco found with organisasi ID  value " + organisasiList.get(0).getId() + " for PR";
				}

				purchaseRequest.setAfco(afco);

			} else {
				return "ERROR!, PR Departement is Not String";
			}

			// TermAndCondition
			cell = row.getCell(4);
			log.info("CELL Term Of Condition = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				purchaseRequest.setTermandcondition(cell.getStringCellValue());
			} else {
				return "ERROR!, PR Term And Condition is Not String";
			}

			// Description
			cell = row.getCell(5);
			log.info("CELL TYPE Description = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				purchaseRequest.setDescription(cell.getStringCellValue());
			} else {
				return "ERROR!, PR Description is Not String";
			}

			// Status
			cell = row.getCell(6);
			log.info("CELL TYPE Status = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String strPrStatus = cell.getStringCellValue();
				PRStatus prStatus = Constant.getPrStatusByKey(strPrStatus.toUpperCase());
				if (prStatus == null) {
					return "ERROR! PR Status name: '" + strPrStatus + "' is invalid!";
				}

				purchaseRequest.setStatus(prStatus.getValue());

			} else {
				return "ERROR!, PR Status is Not String";
			}

			// Next Approval
			cell = row.getCell(7);
			log.info("CELL TYPE Next Approval = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String nextApproval = cell.getStringCellValue();
				List<Approval> approvalList = approvalSession.getNyName(nextApproval);
				if (approvalList.size() == 0) {
					return "ERROR! Approval name '" + nextApproval + "' is not found for PR!";
				}
				Approval approval = approvalList.get(0);
				purchaseRequestImportDTO.setApproval(approval);

				 Find Next Approval  Ikutin logic yg udah ada 

				List<ApprovalLevel> approvalLevelList = approvalLevelSession.findByApproval(approval);

				if (approvalLevelList.size() > 0) {
					ApprovalLevel approvalLevel = approvalLevelList.get(0);
					if (approvalLevel.getGroup() != null) {
						purchaseRequest.setNextapproval(approvalLevel.getGroup().getNama());
					} else if (approvalLevel.getUser() != null) {
						purchaseRequest.setNextapproval(approvalLevel.getUser().getNamaPengguna());
					} else if (approvalLevel.getRole() != null) {
						purchaseRequest.setNextapproval(approvalLevel.getRole().getNama());
					}

				}

			} else {
				return "ERROR!, PR Approval is Not String";
			}

			log.info("");
			log.info("##### PR Line OK, PR ==> " + purchaseRequest.toString());
			log.info("");

			purchaseRequest.setUserId(token.getUser().getId());
			purchaseRequestImportDTO.setPurchaseRequest(purchaseRequest);
			purchaseRequestImportDTOList.add(purchaseRequestImportDTO);
		}

		log.info("");
		log.info("######## ALL PR OK, PR SIZE = " + purchaseRequestImportDTOList.size());
		log.info("");

		for (PurchaseRequestImportDTO purchaseRequestImportDTO : purchaseRequestImportDTOList) {

			 ITEMS 

			 Getting item from sheet 
			List<Row> itemRows = getRowListByCell(itemSheet, purchaseRequestImportDTO.getPurchaseRequest().getPrnumber(), 0);

			if (itemRows.size() == 0) {
				return "ERROR!, PR " + purchaseRequestImportDTO.getPurchaseRequest().getPrnumber() + " has no items!";
			}

			 Get Items Data 
			List<PurchaseRequestItem> purchaseRequestItemList = new ArrayList<PurchaseRequestItem>();

			double totalCost = (double) 0;

			for (Row row : itemRows) {
				PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();

				 ITEM CODE 
				Cell cell = row.getCell(1);
				log.info("CELL TYPE ITEM CODE = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String itemCode = cell.getStringCellValue();
					List<Item> itemList = itemSession.getItemListByKodeEqual(itemCode);
					if (itemList.size() == 0) {
						return "ERROR!, No items found with code " + itemCode;
					}
					Item item = itemList.get(0);
					purchaseRequestItem.setItem(item);
					purchaseRequestItem.setItemname(item.getNama());
					purchaseRequestItem.setKode(item.getKode());

				} else {
					return "ERROR!, Item Code is Not String";
				}

				// Quantity
				cell = row.getCell(2);
				log.info("CELL TYPE ITEM QUANTITY = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					purchaseRequestItem.setQuantity(cell.getNumericCellValue());
					purchaseRequestItem.setQuantitybalance(cell.getNumericCellValue());

				} else {
					return "ERROR!, Item Quantity is Not NUMERIC";
				}

				// Price
				cell = row.getCell(3);
				log.info("CELL TYPE ITEM PRICE = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					purchaseRequestItem.setPrice(cell.getNumericCellValue());

				} else {
					return "ERROR!, Item Price is Not NUMERIC";
				}

				// UNIT
				cell = row.getCell(4);
				log.info("CELL TYPE ITEM UNIT = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));

				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					purchaseRequestItem.setUnit(cell.getStringCellValue());

				} else {
					return "ERROR!, Item Unit is Not STRING";
				}

				purchaseRequestItem.setCostcenter(purchaseRequestImportDTO.getPurchaseRequest().getCostcenter());
				purchaseRequestItem.setItemname(purchaseRequestItem.getItem().getNama());
				purchaseRequestItem.setKode(purchaseRequestItem.getItem().getKode());
				purchaseRequestItem.setMataUang(alokasiAnggaranSession.findByNomorDraft(purchaseRequestItem.getCostcenter()).get(0).getMataUang());
				purchaseRequestItem.setPurchaserequest(purchaseRequestImportDTO.getPurchaseRequest());
				purchaseRequestItem.setStatus("Pending");
				purchaseRequestItem.setTotal(purchaseRequestItem.getQuantity() * purchaseRequestItem.getPrice());
				purchaseRequestItem.setUnit(purchaseRequestItem.getItem().getSatuanId().getNama());
				purchaseRequestItemList.add(purchaseRequestItem);

				totalCost += purchaseRequestItem.getPrice() * purchaseRequestItem.getQuantity();
				log.info("");
				log.info("##### PR ITEMS OK, ITEMS  = " + purchaseRequestItem.toString());
				log.info("");

			}

			// Ikutin yang udah ada, nilai dalam long "tanpa koma"
			Long longTotalCost = (long) totalCost;

			purchaseRequestImportDTO.getPurchaseRequest().setTotalcost(longTotalCost.toString());
			purchaseRequestImportDTO.setPurchaseRequestItemList(purchaseRequestItemList);

			log.info("###################################################################");
			log.info("ALL PR Items OK, Items SIZE = " + purchaseRequestItemList.size());
			log.info("###################################################################");

			 SHIPPING 
			 Getting shipping from sheet 
			List<Row> shippingRows = getRowListByCell(shippingSheet, purchaseRequestImportDTO.getPurchaseRequest().getPrnumber(), 0);

			if (shippingRows.size() == 0) {
				return "ERROR!, PR " + purchaseRequestImportDTO.getPurchaseRequest().getPrnumber() + " has no shipping!";
			}

			 Get Shipping Data 
			List<ShippingToPR> shippingToPRList = new ArrayList<ShippingToPR>();
			for (Row row : shippingRows) {
				ShippingToPR shippingToPR = new ShippingToPR();
				shippingToPR.setPrNumber(purchaseRequestImportDTO.getPurchaseRequest().getPrnumber());

				// Shipping Afco
				Cell cell = row.getCell(1);
				log.info("CELL TYPE SHIPPING AFCO = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
				List<Afco> afcoList;
				String afcoCompanyName;
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					afcoCompanyName = cell.getStringCellValue();

					afcoList = afcoSession.getAfcoByCompanyName(afcoCompanyName);
					if (afcoList.size() == 0) {
						return "ERROR!, No Afco with Company Name " + afcoCompanyName + " in shipping!";
					}

					shippingToPR.setAddressLabel(afcoCompanyName);

				} else {
					return "ERROR!, Company Name is Not STRING";
				}

				// Shipping Address label
				cell = row.getCell(2);
				log.info("CELL TYPE SHIPPING ADRESS LABEL = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String addressLabel = cell.getStringCellValue();

					List<AddressBookDTO> addressBookDTOList = addressBookSession.getAddressBookListByAfco(afcoList.get(0).getId());
					if (addressBookDTOList.size() == 0) {

						return "ERROR!, No Address with Company Name " + afcoCompanyName + " for shipping!";
					}
					Boolean found = false;
					AddressBookDTO addressBookDTOFound = null;
					for (AddressBookDTO addressBookDTO : addressBookDTOList) {
						if (addressBookDTO.getAddressLabel().equals(addressLabel)) {
							found = true;
							addressBookDTOFound = addressBookDTO;
							break;
						}
					}
					if (!found) {
						return "ERROR!, No Address found with label " + addressLabel + " for shipping!";
					}
					AddressBook address = addressBookSession.find(addressBookDTOFound.getId());
					shippingToPR.setTelephone1(address.getTelephone());
					shippingToPR.setFullName(address.getFullName());
					shippingToPR.setAddressBook(address);

				} else {
					return "ERROR!, Shipping Address is Not STRING";
				}

				// Shipping Delivery Time
				cell = row.getCell(3);
				log.info("CELL TYPE Shipping Delivery Time = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
				if (cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)) {
					shippingToPR.setDeliveryTime(cell.getDateCellValue());
				} else {
					return "ERROR!, Shipping Delivery Time is Not Numeric";
				}

				// Shipping Item Code
				PurchaseRequestItem purchaseRequestItem = null;
				cell = row.getCell(4);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String itemCode = cell.getStringCellValue();
					Boolean isFound = false;
					for (PurchaseRequestItem prItem : purchaseRequestItemList) {
						if (prItem.getItem().getKode().equals(itemCode)) {
							isFound = true;
							purchaseRequestItem = prItem;
							break;
						}
					}

					if (isFound) {
						shippingToPR.setPurchaseRequestItemObject(purchaseRequestItem);
					} else {
						return "ERROR!, Item Code " + itemCode + " is not found in PR List";
					}

				} else {
					return "ERROR!, Item Code is Not STRING";
				}

				// Quantity
				cell = row.getCell(5);
				log.info("CELL SHIPPING ITEM QUANTITY = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					shippingToPR.setQuantity((int) cell.getNumericCellValue());
				} else {
					return "ERROR!, Shipping Item Quantity is Not NUMERIC";
				}

				log.info("");
				log.info("##### shippingToPR OK, shippingToPR  = " + shippingToPR.toString());
				log.info("");

				shippingToPRList.add(shippingToPR);
			}

			purchaseRequestImportDTO.setShippingToPRList(shippingToPRList);

			log.info("###################################################################");
			log.info("ALL shippingList OK, shipping SIZE = " + shippingToPRList.size());
			log.info("###################################################################");

		}

		log.info(" purchaseRequestList.size() =  " + purchaseRequestImportDTOList.size());

		 Saving all 

		 Find approval 

		for (PurchaseRequestImportDTO purchaseRequestImportDTO : purchaseRequestImportDTOList) {

			log.info("insert pr header " + purchaseRequestImportDTO.toString());
			PurchaseRequest insertedPR = purchaseRequestSession.insert(purchaseRequestImportDTO.getPurchaseRequest(), token);

			for (PurchaseRequestItem purchaseRequestItem : purchaseRequestImportDTO.getPurchaseRequestItemList()) {
				log.info("insert purchaseRequestItem " + purchaseRequestItem.toString());
				purchaseRequestItem.setPurchaserequest(insertedPR);
				purchaseRequestItem = purchaseRequestItemSession.createPurchaseRequestItem(purchaseRequestItem, token);
			}

			for (ShippingToPR shippingToPR : purchaseRequestImportDTO.getShippingToPRList()) {
				log.info("insert shippingToPR " + shippingToPR.toString());
				 updatePurchaseRequestItem before Insert 
				PurchaseRequestItem pItem = null;
				for (PurchaseRequestItem purchaseRequestItem : purchaseRequestImportDTO.getPurchaseRequestItemList()) {
					if (purchaseRequestItem.getItem().getKode().equals(shippingToPR.getPurchaseRequestItemObject().getItem().getKode())) {
						shippingToPR.setPurchaseRequestItem(purchaseRequestItem.getId());
						break;
					}

				}

			}

			// Grouping shipping by afco, address, deliverytime
			List<ShippingToPR> shippingToPRGroupedList = new ArrayList<ShippingToPR>();
			Integer groupIdx = 1;
			for (ShippingToPR shippingToPR : purchaseRequestImportDTO.getShippingToPRList()) {

				 find is it already in the list? 
				Boolean found = false;
				ShippingToPR foundGroupShipping = null;
				for (ShippingToPR groupedShipping : shippingToPRGroupedList) {
					if (groupedShipping.getAddressBook().equals(shippingToPR.getAddressBook()) && groupedShipping.getDeliveryTime().equals(shippingToPR.getDeliveryTime())) {
						found = true;
						foundGroupShipping = groupedShipping;
						break;
					}
				}

				if (found) {
					shippingToPR.setShippingGroup(foundGroupShipping.getShippingGroup());
				} else {
					shippingToPR.setShippingGroup(groupIdx);
					groupIdx++;
				}
				shippingToPRGroupedList.add(shippingToPR);

			}

			 Insert Shipping 
			for (ShippingToPR groupedShipping : shippingToPRGroupedList) {

				ShippingToPR insertedShippingToPR = shippingToPRSession.inserShippingToPR(groupedShipping, token);

			}

			// Create Approval Process

			Integer tahapanId = 26; // 26 = purchase request
			ApprovalProcessType apt = new ApprovalProcessType();
			apt.setValueId(insertedPR.getId());
			apt.setTahapan(tahapanSession.find(tahapanId)); // 26 purchase
															// request
			apt.setApproval(purchaseRequestImportDTO.getApproval());
			apt = approvalProcessTypeSession.create(apt, token);

			List<ApprovalLevel> aplList = approvalLevelSession.findByApproval(purchaseRequestImportDTO.getApproval());
			for (ApprovalLevel apl : aplList) {
				ApprovalProcess ap = new ApprovalProcess();
				ap.setApprovalProcessType(apt);
				ap.setApprovalLevel(apl);
				if (apl.getSequence() == 1) {
					ap.setStatus(1); // set status = aktif
				} else {
					ap.setStatus(0); // set status = non aktif (0)
				}
				// ap.setKeterangan();
				ap.setGroup(apl.getGroup());
				ap.setUser(apl.getUser());
				// ap.setRole(apl.getRole());
				ap.setThreshold(apl.getThreshold());
				ap.setSequence(apl.getSequence());
				approvalProcessSession.create(ap, token);
			}

		}

		return purchaseRequestImportDTOList.size() + " purchase request imported successfully!";
	}

	// cell number start from 0
	private List<Row> getRowListByCell(Sheet sheet, String str, Integer cellNumber) {
		List<Row> rows = new ArrayList<Row>();
		int jumlahRow = sheet.getLastRowNum();
		for (int a = 1; a <= jumlahRow; a++) {
			Row row = sheet.getRow(a);
			Cell cellKey = row.getCell(cellNumber);
			if (cellKey != null && cellKey.getCellType() != Cell.CELL_TYPE_BLANK && cellKey.getCellType() == Cell.CELL_TYPE_STRING) {
				String strKey = cellKey.getStringCellValue();
				if (strKey.equalsIgnoreCase(str)) {
					rows.add(row);
				}
			}
		}
		return rows;
	}
}
*/