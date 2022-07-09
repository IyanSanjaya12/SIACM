package id.co.promise.procurement.rating;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import id.co.promise.procurement.budgetdanrealisasi.BudgetDanRealisasiSession;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.catalog.session.CatalogKontrakSession;
import id.co.promise.procurement.catalog.session.CatalogSession;
import id.co.promise.procurement.deliveryreceived.DeliveryReceivedSession;
import id.co.promise.procurement.entity.BidangUsaha;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.entity.PurchaseOrderItem;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.BidangUsahaSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.vendor.VendorSession;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Stateless
@Path(value = "/procurement/rating/delivery-receipt/")
@Produces(MediaType.APPLICATION_JSON)
public class RatingDeliveryReceiptServices {
	
	final static Logger log = Logger.getLogger(RatingDeliveryReceiptServices.class);
	
	@EJB
	private DeliveryReceivedSession deliveryReceivedSession;
	
	@EJB
	private CatalogSession catalogSession;
	@EJB
	private CatalogKontrakSession catalogKontrakSession;
	@EJB
	private BidangUsahaSession bidangUsahaSession;
	@EJB
	private VendorSession vendorSession;
	@EJB
	private PurchaseOrderItemSession purchaseOrderItemSession;
	@EJB
	TokenSession tokenSession;
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	UserSession userSession;
	
	@EJB
	BudgetDanRealisasiSession budgetDanRealisasiSession;

	@Path("/get-vendor-list")
	@GET
	public List<Vendor> getVendorList() {
		return vendorSession.getList();
	}

	@Path("/get-bidang-usaha-list")
	@GET
	public List<BidangUsaha> getBidangUsahaList() {
		return bidangUsahaSession.getBidangUsahaList();
	}

	@Path("/get-list")
	@POST
	public Response getListPagination(@FormParam("search") String search, @FormParam("startDate") Date startDate, @FormParam("endDate") Date endDate,
			@FormParam("status") String status, @FormParam("sort") String sort, @FormParam("pageNo") Integer pageNo,
			@FormParam("pageSize") Integer pageSize, @HeaderParam("Authorization") String token) throws ParseException {

		Token tokenObj = tokenSession.findByToken(token);
		RoleUser roleUser = roleUserSession.getByToken(tokenObj);
		
		Calendar c = Calendar.getInstance();
		if (endDate != null) {
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
		}
		Date currentDatePlusOne = c.getTime();
		

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", deliveryReceivedSession.getListPagination(search, startDate, currentDatePlusOne, status, pageNo, pageSize, sort, roleUser));
		map.put("jmlData", deliveryReceivedSession.getTotalList(search, startDate, currentDatePlusOne, status, pageNo, pageSize, roleUser));
		map.put("role", roleUser.getRole().getCode());
		return Response.ok(map).build();
	}

	@Path("/download-excel-item-katalog")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportItemKatalog(@FormParam("search") String search, @FormParam("vendorName") String vendorName,
			@FormParam("orderKeyword") String orderKeyword, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize)
			throws SQLException {
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report-Katalog-Item", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			String namaFile = fileName.getName();

			log.info(" absolutePath = " + absolutePath + " , namaFile = " + namaFile);

			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Item Katalog", 0);

			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Laporan Item Katalog", cellFormatHeader));

			WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 9);
			WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
			cellFormat.setBackground(Colour.GRAY_25);
			cellFormat.setAlignment(Alignment.CENTRE);
			cellFormat.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);

			WritableCellFormat cellFormatLeft = new WritableCellFormat(cellFont);
			cellFormatLeft.setAlignment(Alignment.LEFT);

			WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFont);
			cellFormatCenter.setAlignment(Alignment.CENTRE);

			WritableCellFormat cellFormatRight = new WritableCellFormat(cellFont);
			cellFormatRight.setAlignment(Alignment.RIGHT);
			cellFormatRight.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat numberCellFormat = new WritableCellFormat(NumberFormats.INTEGER);
			numberCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat priceCellFormat = new WritableCellFormat(NumberFormats.FLOAT);
			priceCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			sheet.setColumnView(0, 3);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);
			sheet.setColumnView(4, autoSizeCellView);
			sheet.setColumnView(5, autoSizeCellView);
			sheet.setColumnView(6, autoSizeCellView);
			sheet.setColumnView(7, autoSizeCellView);

			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label ktegoriCtlog = new Label(1, 2, "Kategeori Katalog", cellFormat);
			sheet.addCell(ktegoriCtlog);
			Label itemKtlog = new Label(2, 2, "Nama item katalog", cellFormat);
			sheet.addCell(itemKtlog);
			Label itemEbs = new Label(3, 2, "Item EBS", cellFormat);
			sheet.addCell(itemEbs);
			Label unit = new Label(4, 2, "Satuan", cellFormat);
			sheet.addCell(unit);
			Label vendor = new Label(5, 2, "Penyedia", cellFormat);
			sheet.addCell(vendor);
			Label currentStock = new Label(6, 2, "Jumlah stock", cellFormat);
			sheet.addCell(currentStock);
			Label price = new Label(7, 2, "Harga", cellFormat);
			sheet.addCell(price);

			int number = 1;
			int cellRow = 3;

			List<Object[]> result = catalogSession.getExcel(search, vendorName, orderKeyword, pageNo, pageSize);
			Iterator itr = result.iterator();

			while (itr.hasNext()) {

				Object[] obj = (Object[]) itr.next();
				CatalogCategory catalogCategory = (CatalogCategory) obj[0];
				Catalog catalog = (Catalog) obj[1];

				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow,
						(catalogCategory.getCategory() != null && catalogCategory.getCategory().getAdminLabel() != null)
								? catalogCategory.getCategory().getAdminLabel()
								: "-",
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow, (catalog.getNamaIND() != null) ? catalog.getNamaIND() : "-", numberCellFormat));
				sheet.addCell(new Label(3, cellRow,
						(catalog.getItem() != null && catalog.getItem().getNama() != null) ? catalog.getItem().getNama() : "-",
						numberCellFormat));
				sheet.addCell(new Label(4, cellRow, (catalog.getSatuan().getNama() != null) ? catalog.getSatuan().getNama() : "-",
						numberCellFormat));
				sheet.addCell(new Label(5, cellRow,
						(catalog.getVendor() != null && catalog.getVendor().getNama() != null) ? catalog.getVendor().getNama() : "-",
						numberCellFormat));
				sheet.addCell(new Label(6, cellRow, (catalog.getCurrentStock() != null ? catalog.getCurrentStock().toString() : "-"),
						cellFormatRight));
				sheet.addCell(new Label(7, cellRow, (catalog.getHarga().toString()), cellFormatRight));
				number++;
				cellRow++;
			}

			workbook.write();
			workbook.close();

			// get file
			outputFile = new File(absolutePath);
			java.nio.file.Path path = Paths.get(outputFile.toString());
			buff = Files.readAllBytes(path);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(buff).build();

	}

	@Path("/get-list-vendor")
	@POST
	public Response getListVendorPagination(@FormParam("search") String search, @FormParam("vendorName") String vendorName,
			@FormParam("orderKeyword") String orderKeyword, @FormParam("asc") String asc, @FormParam("pageNo") Integer pageNo,
			@FormParam("pageSize") Integer pageSize) {

		try {
			vendorName = vendorName == null || vendorName == "undefined" || vendorName == "" ? null : vendorName;

		} catch (Exception e) {
			// kalo undefined
			vendorName = null;

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", vendorSession.getListPagination(search, vendorName, orderKeyword, pageNo, pageSize, asc, null, null,null));
		map.put("jmlData", vendorSession.getTotalList(search, vendorName, pageNo, pageSize, null, null,null));
		return Response.ok(map).build();
	}

	@Path("/download-excel-vendor")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportVendor(@FormParam("search") String search, @FormParam("vendorName") String vendorName,
			@FormParam("orderKeyword") String orderKeyword, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize)
			throws SQLException {
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report Vendor", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Vendor", 0);

			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Reoort Vendor", cellFormatHeader));

			WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 9);
			WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
			cellFormat.setBackground(Colour.GRAY_25);
			cellFormat.setAlignment(Alignment.CENTRE);
			cellFormat.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);

			WritableCellFormat cellFormatLeft = new WritableCellFormat(cellFont);
			cellFormatLeft.setAlignment(Alignment.LEFT);

			WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFont);
			cellFormatCenter.setAlignment(Alignment.CENTRE);

			WritableCellFormat cellFormatRight = new WritableCellFormat(cellFont);
			cellFormatRight.setAlignment(Alignment.RIGHT);
			cellFormatRight.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat numberCellFormat = new WritableCellFormat(NumberFormats.INTEGER);
			numberCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat priceCellFormat = new WritableCellFormat(NumberFormats.FLOAT);
			priceCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			sheet.setColumnView(0, 3);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);

			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label ktegoriCtlog = new Label(1, 2, "Penyedia", cellFormat);
			sheet.addCell(ktegoriCtlog);
			Label itemKtlog = new Label(2, 2, "Bidang Usaha", cellFormat);
			sheet.addCell(itemKtlog);
			Label itemEbs = new Label(3, 2, "Status", cellFormat);
			sheet.addCell(itemEbs);

			int number = 1;
			int cellRow = 3;

			List<Object[]> result = vendorSession.getExcel(search, vendorName, orderKeyword, pageNo, pageSize);
			Iterator itr = result.iterator();

			while (itr.hasNext()) {

				Object[] obj = (Object[]) itr.next();
				SegmentasiVendor segmentasiVendor = (SegmentasiVendor) obj[1];
				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow,
						(segmentasiVendor.getVendor() != null && segmentasiVendor.getVendor().getNama() != null)
								? segmentasiVendor.getVendor().getNama()
								: "-",
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow,
						(segmentasiVendor.getSubBidangUsaha() != null
								&& segmentasiVendor.getSubBidangUsaha().getBidangUsaha().getNama() != null)
										? segmentasiVendor.getSubBidangUsaha().getBidangUsaha().getNama()
										: "-",
						numberCellFormat));
				sheet.addCell(new Label(3, cellRow,
						(segmentasiVendor.getVendor() != null && segmentasiVendor.getVendor().getStatus() != 1) ? "Tidak Aktif" : "Aktif",
						numberCellFormat));
				number++;
				cellRow++;
			}

			workbook.write();
			workbook.close();

			// get file
			outputFile = new File(absolutePath);
			java.nio.file.Path path = Paths.get(outputFile.toString());
			buff = Files.readAllBytes(path);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(buff).build();

	}

	@Path("/get-list-purchase-order")
	@POST
	public Response getListPurchaseOrderPagination(@FormParam("itemName") String itemName, @FormParam("vendorName") String vendorName,
			@FormParam("startDate") Date startDate, @FormParam("endDate") Date endDate, @FormParam("orderKeyword") String orderKeyword,
			@FormParam("asc") String asc, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize, @HeaderParam("Authorization") String token)
			throws ParseException {

		Calendar c = Calendar.getInstance();
		if (endDate != null) {
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
		}
		Date currentDatePlusOne = c.getTime();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", purchaseOrderItemSession.getListPagination(itemName, vendorName, startDate, currentDatePlusOne, orderKeyword,
				pageNo, pageSize, token));
		map.put("jmlData",
				purchaseOrderItemSession.getTotalList(itemName, vendorName, startDate, currentDatePlusOne, orderKeyword,
						pageNo, pageSize, token));

		return Response.ok(map).build();
	}

	@Path("/download-excel-purchase-order")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportPurchaseOrder(@FormParam("itemName") String itemName, @FormParam("vendorName") String vendorName,
			@FormParam("startDate") Date startDate, @FormParam("endDate") Date endDate, @FormParam("orderKeyword") String orderKeyword,
			@FormParam("asc") String asc, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize, @HeaderParam("Authorization") String token)
			throws SQLException {
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report Purchase Order", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Purchase Order", 0);

			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Reort Purchase Order", cellFormatHeader));

			WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 9);
			WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
			cellFormat.setBackground(Colour.GRAY_25);
			cellFormat.setAlignment(Alignment.CENTRE);
			cellFormat.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);

			WritableCellFormat cellFormatLeft = new WritableCellFormat(cellFont);
			cellFormatLeft.setAlignment(Alignment.LEFT);

			WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFont);
			cellFormatCenter.setAlignment(Alignment.CENTRE);

			WritableCellFormat cellFormatRight = new WritableCellFormat(cellFont);
			cellFormatRight.setAlignment(Alignment.RIGHT);
			cellFormatRight.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat numberCellFormat = new WritableCellFormat(NumberFormats.INTEGER);
			numberCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat priceCellFormat = new WritableCellFormat(NumberFormats.FLOAT);
			priceCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			sheet.setColumnView(0, 6);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);
			sheet.setColumnView(4, autoSizeCellView);
			sheet.setColumnView(5, autoSizeCellView);
			sheet.setColumnView(6, autoSizeCellView);

			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label ktegoriCtlog = new Label(1, 2, "No Po (EBS)", cellFormat);
			sheet.addCell(ktegoriCtlog);
			Label itemKtlog = new Label(2, 2, "Tanggal PO", cellFormat);
			sheet.addCell(itemKtlog);
			Label itemEbs = new Label(3, 2, "Nama Item Katalog", cellFormat);
			sheet.addCell(itemEbs);
			Label organizationName = new Label(4, 2, "Penyedia", cellFormat);
			sheet.addCell(organizationName);
			Label vendor = new Label(5, 2, "Pengguna", cellFormat);
			sheet.addCell(vendor);
			Label price = new Label(6, 2, "Total Harga", cellFormat);
			sheet.addCell(price);

			int number = 1;
			int cellRow = 3;
			List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getListPagination(itemName, vendorName, startDate,
					endDate, orderKeyword, pageNo, pageSize, token);

			for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow,
						((purchaseOrderItem.getPurchaseOrder() != null && purchaseOrderItem.getPurchaseOrder().getPoNumber() != null)
								? purchaseOrderItem.getPurchaseOrder().getPoNumber()
								: "-"),
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow,
						((purchaseOrderItem.getPurchaseOrder() != null && purchaseOrderItem.getPurchaseOrder().getApprovedDate() != null)
								? purchaseOrderItem.getPurchaseOrder().getApprovedDate().toString()
								: "-"),
						numberCellFormat));
				sheet.addCell(new Label(3, cellRow, ((purchaseOrderItem.getItemName() != null) ? purchaseOrderItem.getItemName() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(4, cellRow,
						((purchaseOrderItem.getVendor() != null && purchaseOrderItem.getVendor().getNama() != null)
								? purchaseOrderItem.getVendor().getNama()
								: "-"),
						numberCellFormat));
				sheet.addCell(new Label(5, cellRow,
						((purchaseOrderItem.getPurchaseOrder() != null
								&& purchaseOrderItem.getPurchaseOrder().getPurchaseRequest().getOrganisasi().getNama() != null)
										? purchaseOrderItem.getPurchaseOrder().getPurchaseRequest().getOrganisasi().getNama()
										: "-"),
						numberCellFormat));
				sheet.addCell(new Label(6, cellRow,
						((purchaseOrderItem.getTotalUnitPrices() != null) ? purchaseOrderItem.getTotalUnitPrices().toString() : "-"),
						priceCellFormat));
				number++;
				cellRow++;
			}

			workbook.write();
			workbook.close();

			// get file
			outputFile = new File(absolutePath);
			java.nio.file.Path path = Paths.get(outputFile.toString());
			buff = Files.readAllBytes(path);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(buff).build();

	}

	@Path("/get-list-contrack-catalog")
	@POST
	public Response getListContrackCatalogPagination(@FormParam("namaPekerjaan") String namaPekerjaan,
			@FormParam("vendorName") String vendorName, @FormParam("orderKeyword") String orderKeyword, @FormParam("sort") String sort,
			@FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize, @FormParam("startDate") Date startDate,
			@FormParam("endDate") Date endDate, @HeaderParam("Authorization") String tokenStr) {

		Calendar calender = Calendar.getInstance();
		if (endDate != null) {
			calender.setTime(endDate);
			calender.add(Calendar.DATE, 1);
		}

		Date currentDatePlusOne = calender.getTime();
		Token tokenObj = tokenSession.findByToken(tokenStr); 
		RoleUser roleUser =roleUserSession.getByToken(tokenObj);
		List<CatalogKontrak> catalogKontrakList = catalogKontrakSession.getReportCatalogKontrak(namaPekerjaan, vendorName, orderKeyword,
				pageNo, pageSize, sort, startDate, currentDatePlusOne, roleUser);
		for (CatalogKontrak catalogKontrak : catalogKontrakList) {
			Date dateNow = new Date();
			Date date = catalogKontrak.getTglAkhirKontrak();
			long difference = date.getTime() - dateNow.getTime();
			long value = difference / (24 * 60 * 60 * 1000);
			Integer reamingTime = (int) value;
			catalogKontrak.setReamingTime(reamingTime);

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jmlData", catalogKontrakSession.getTotalList(namaPekerjaan, vendorName, orderKeyword, pageNo, pageSize, sort, startDate,
				currentDatePlusOne, roleUser));
		map.put("dataList", catalogKontrakList);

		return Response.ok(map).build();
	}

	@Path("/download-excel-catalog-contract")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportCatalogContract(@FormParam("namaPekerjaan") String namaPekerjaan,
			@FormParam("vendorName") String vendorName, @FormParam("orderKeyword") String orderKeyword, @FormParam("sort") String sort,
			@FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize, @HeaderParam("Authorization") String tokenStr) throws SQLException {
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report Catalog Contract", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Catalog Contract", 0);

			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Report Catalog Contract", cellFormatHeader));

			WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 9);
			WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
			cellFormat.setBackground(Colour.GRAY_25);
			cellFormat.setAlignment(Alignment.CENTRE);
			cellFormat.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);

			WritableCellFormat cellFormatLeft = new WritableCellFormat(cellFont);
			cellFormatLeft.setAlignment(Alignment.LEFT);

			WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFont);
			cellFormatCenter.setAlignment(Alignment.CENTRE);

			WritableCellFormat cellFormatRight = new WritableCellFormat(cellFont);
			cellFormatRight.setAlignment(Alignment.RIGHT);
			cellFormatRight.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat numberCellFormat = new WritableCellFormat(NumberFormats.INTEGER);
			numberCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat priceCellFormat = new WritableCellFormat(NumberFormats.FLOAT);
			priceCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat currency = new WritableCellFormat(NumberFormats.ACCOUNTING_INTEGER);
			currency.setBorder(Border.ALL, BorderLineStyle.THIN);
			currency.setAlignment(Alignment.RIGHT);

			sheet.setColumnView(0, 6);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);
			sheet.setColumnView(4, autoSizeCellView);
			sheet.setColumnView(5, autoSizeCellView);
			sheet.setColumnView(6, autoSizeCellView);

			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label ktegoriCtlog = new Label(1, 2, "Nomor Kontrak", cellFormat);
			sheet.addCell(ktegoriCtlog);
			Label itemKtlog = new Label(2, 2, "Nama Pekerjaan", cellFormat);
			sheet.addCell(itemKtlog);
			Label itemEbs = new Label(3, 2, "Nilai Kontrak", cellFormat);
			sheet.addCell(itemEbs);
			Label organizationName = new Label(4, 2, "Penyedia", cellFormat);
			sheet.addCell(organizationName);
			Label vendor = new Label(5, 2, "Tanggal Akhir Kontrak", cellFormat);
			sheet.addCell(vendor);
			Label price = new Label(6, 2, "Sisa Waktu", cellFormat);
			sheet.addCell(price);

			int number = 1;
			int cellRow = 3;
			Token tokenObj = tokenSession.findByToken(tokenStr); 
			RoleUser roleUser =roleUserSession.getByToken(tokenObj);
			List<CatalogKontrak> catalogKontrakList = catalogKontrakSession.getReportCatalogKontrak(namaPekerjaan, vendorName, orderKeyword,
					pageNo, pageSize, sort, null, null, roleUser);

			for (CatalogKontrak catalogKontrak : catalogKontrakList) {
				Date dateNow = new Date();
				Date date = catalogKontrak.getTglAkhirKontrak();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String strDate = formatter.format(date);
				long difference = date.getTime() - dateNow.getTime();
				long endDate = difference / (24 * 60 * 60 * 1000);
				Integer reamingTime = (int) endDate;

				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow, ((catalogKontrak.getNomorKontrak() != null) ? catalogKontrak.getNomorKontrak() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow, ((catalogKontrak.getNamaPekerjaan() != null) ? catalogKontrak.getNamaPekerjaan() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(3, cellRow,
						((catalogKontrak.getNilaiKontrak() != null) ? catalogKontrak.getNilaiKontrak().toString() : "-"), currency));
				sheet.addCell(new Label(4, cellRow,
						((catalogKontrak.getVendor() != null && catalogKontrak.getVendor().getNama() != null)
								? catalogKontrak.getVendor().getNama()
								: "-"),
						numberCellFormat));
				sheet.addCell(new Label(5, cellRow, ((strDate != null) ? strDate : "-"), numberCellFormat));
				sheet.addCell(new Label(6, cellRow, ((reamingTime != null) ? reamingTime.toString() : "-"), cellFormatRight));

				number++;
				cellRow++;
			}

			workbook.write();
			workbook.close();

			// get file
			outputFile = new File(absolutePath);
			java.nio.file.Path path = Paths.get(outputFile.toString());
			buff = Files.readAllBytes(path);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(buff).build();

	}
	
	@Path("/get-budget-dan-realisasi")
	@POST
	public Response getBudgetDanRealisasiPagination(@FormParam("type") Integer type, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize,
			@FormParam("search") String search, @FormParam("sort") String sortingType, @FormParam("startDate") Date startDate,
			@FormParam("endDate") Date endDate) throws ParseException {

		Calendar c = Calendar.getInstance();
		if (endDate != null) {
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
		}
		Date currentDatePlusOne = c.getTime();
		List<DeliveryReceived> deliveryReceived = budgetDanRealisasiSession.getReportBudgetDanRealisasi(search, type, pageNo, pageSize, sortingType, startDate, currentDatePlusOne);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", deliveryReceived);
		map.put("jmlData", budgetDanRealisasiSession.getTotalList(type, search, sortingType, startDate, currentDatePlusOne));

		return Response.ok(map).build();
	}

	@Path("/download-excel-budget-dan-realisasi")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportBudgetDanRealisasi(@FormParam("search") String search,
			@FormParam("type") Integer type, @FormParam("sort") String sortingType,
			@FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize) throws SQLException {
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report Budget Dan Realisasi", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Budget Dan Realisasi", 0);

			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Report Budget Dan Realisasi", cellFormatHeader));

			WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 9);
			WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
			cellFormat.setBackground(Colour.GRAY_25);
			cellFormat.setAlignment(Alignment.CENTRE);
			cellFormat.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);

			WritableCellFormat cellFormatLeft = new WritableCellFormat(cellFont);
			cellFormatLeft.setAlignment(Alignment.LEFT);

			WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFont);
			cellFormatCenter.setAlignment(Alignment.CENTRE);

			WritableCellFormat cellFormatRight = new WritableCellFormat(cellFont);
			cellFormatRight.setAlignment(Alignment.RIGHT);
			cellFormatRight.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat numberCellFormat = new WritableCellFormat(NumberFormats.INTEGER);
			numberCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat priceCellFormat = new WritableCellFormat(NumberFormats.FLOAT);
			priceCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			WritableCellFormat currency = new WritableCellFormat(NumberFormats.ACCOUNTING_INTEGER);
			currency.setBorder(Border.ALL, BorderLineStyle.THIN);
			currency.setAlignment(Alignment.RIGHT);

			sheet.setColumnView(0, 6);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);
			sheet.setColumnView(4, autoSizeCellView);
			sheet.setColumnView(5, autoSizeCellView);
			sheet.setColumnView(6, autoSizeCellView);
			sheet.setColumnView(6, autoSizeCellView);
			sheet.setColumnView(7, autoSizeCellView);

			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label noPr = new Label(1, 2, "No. PR", cellFormat);
			sheet.addCell(noPr);
			Label tanggalPR = new Label(2, 2, "Tanggal PR", cellFormat);
			sheet.addCell(tanggalPR);
			Label noPo = new Label(3, 2, "No. PO", cellFormat);
			sheet.addCell(noPo);
			Label tanggalPo = new Label(4, 2, "Tanggal PO", cellFormat);
			sheet.addCell(tanggalPo);
			Label amountPO = new Label(5, 2, "Amount PO", cellFormat);
			sheet.addCell(amountPO);
			Label jumlahAmount = new Label(6, 2, "Jumlah Amount (Receipt)", cellFormat);
			sheet.addCell(jumlahAmount);
			Label sisaWaktu = new Label(7, 2, "Tanggal TTB", cellFormat);
			sheet.addCell(sisaWaktu);


			int number = 1;
			int cellRow = 3;
			List<DeliveryReceived> budgetDanRealisasiList = budgetDanRealisasiSession.getReportBudgetDanRealisasi(search, type, pageNo, pageSize, sortingType, null, null);

			for (DeliveryReceived budgetdanRealisasi : budgetDanRealisasiList) {
				Date dateReceived = budgetdanRealisasi.getDateReceived();
				Date approveDatePR = budgetdanRealisasi.getPurchaseOrder().getPurchaseRequest().getApprovedDate();
				Date approveDatePO = budgetdanRealisasi.getPurchaseOrder().getApprovedDate();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String strDateReceived = formatter.format(dateReceived);
				String strApproveDatePR = formatter.format(approveDatePR);
				String strApproveDatePO = formatter.format(approveDatePO);

				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow, ((budgetdanRealisasi.getPurchaseOrder().getPurchaseRequest().getPrnumber() != null) ? budgetdanRealisasi.getPurchaseOrder().getPurchaseRequest().getPrnumber() : "-"), numberCellFormat));
				sheet.addCell(new Label(2, cellRow, ((strApproveDatePR != null) ? strApproveDatePR : "-"), numberCellFormat));
				sheet.addCell(new Label(3, cellRow, ((budgetdanRealisasi.getPurchaseOrder().getPoNumber() != null) ? budgetdanRealisasi.getPurchaseOrder().getPoNumber() : "-"), numberCellFormat));
				sheet.addCell(new Label(4, cellRow, ((strApproveDatePO != null) ? strApproveDatePO : "-"), numberCellFormat));
				sheet.addCell(new Label(5, cellRow,
						((budgetdanRealisasi.getPurchaseOrder().getTotalCost() != null) ? budgetdanRealisasi.getPurchaseOrder().getTotalCost().toString() : "-"), currency));
				sheet.addCell(new Label(6, cellRow,
						((budgetdanRealisasi.getTotalReceiptAmount() != null) ? budgetdanRealisasi.getTotalReceiptAmount().toString() : "-"), currency));
				sheet.addCell(new Label(7, cellRow, ((strDateReceived != null) ? strDateReceived : "-"), numberCellFormat));
				number++;
				cellRow++;
			}

			workbook.write();
			workbook.close();

			// get file
			outputFile = new File(absolutePath);
			java.nio.file.Path path = Paths.get(outputFile.toString());
			buff = Files.readAllBytes(path);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(buff).build();

	}
}
