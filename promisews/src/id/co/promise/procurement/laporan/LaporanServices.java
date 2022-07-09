package id.co.promise.procurement.laporan;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import id.co.promise.procurement.DTO.LaporanEvaluasiKinerjaVendorDTO;
import id.co.promise.procurement.DTO.LaporanItemKatalogPoDTO;
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
import id.co.promise.procurement.purchaseorder.PurchaseOrderSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestSession;
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
@Path(value = "/procurement/laporan")
@Produces(MediaType.APPLICATION_JSON)
public class LaporanServices {
	final static Logger log = Logger.getLogger(LaporanServices.class);
	
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
	BudgetDanRealisasiSession budgetDanRealisasiSession;
	@EJB
	PurchaseRequestSession purchaseRequestSession;
	@EJB
	PurchaseOrderSession purchaseOrderSession;
	@EJB
	DeliveryReceivedSession deliveryReceivedSession;
	@EJB
	UserSession userSession;


	@Path("/get-vendor-list")
	@GET
	public List<Vendor> getVendorList() {
		return vendorSession.getVendorListForReportCatalog();
	}

	@Path("/get-bidang-usaha-list")
	@GET
	public List<BidangUsaha> getBidangUsahaList() {
		return bidangUsahaSession.getBidangUsahaList();
	}

	@Path("/get-list")
	@POST
	public Response getListPagination(@FormParam("search") String search, @FormParam("vendorName") String vendorName,
			@FormParam("orderKeyword") String orderKeyword, @FormParam("sort") String sort, @FormParam("pageNo") Integer pageNo,
			@FormParam("pageSize") Integer pageSize, @HeaderParam("Authorization") String token) {
		try {
			vendorName = vendorName == null || vendorName.equals("") || vendorName.equals("undefined") ? null : vendorName;

		} catch (Exception e) {
			// kalo undefined
			vendorName = null;

		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", catalogSession.getListPagination(vendorName, orderKeyword, pageNo, pageSize, token));
		map.put("jmlData", catalogSession.getTotalList(vendorName, orderKeyword, pageNo, pageSize, token));
		return Response.ok(map).build();
	}

	@SuppressWarnings("rawtypes")
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
			Label itemEbs = new Label(3, 2, "Nama Item", cellFormat);
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

				//KAI - 20201223 - [19451] 
				DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
		        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

		        formatRp.setCurrencySymbol("Rp. ");
		        formatRp.setMonetaryDecimalSeparator(',');
		        formatRp.setGroupingSeparator('.');

		        kursIndonesia.setDecimalFormatSymbols(formatRp);
		        
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
				sheet.addCell(new Label(7, cellRow, (kursIndonesia.format(catalog.getHarga())), cellFormatRight)); //KAI - 20201223 - [19451]
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
			@FormParam("pageSize") Integer pageSize, @FormParam("startDate") Date startDate, @FormParam("endDate") Date endDate) {

		try {
			vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("") ? null : vendorName;

		} catch (Exception e) {
			// kalo undefined
			vendorName = null;

		}
		Date sysdate = new Date();
		Calendar calender = Calendar.getInstance();
		if (endDate != null) {
			calender.setTime(endDate);
			calender.add(Calendar.DATE, 1);
		}
		Date currentDatePlusOne = calender.getTime();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", vendorSession.getListPagination(search, vendorName, orderKeyword, pageNo, pageSize, asc, sysdate, startDate, currentDatePlusOne));
		map.put("jmlData", vendorSession.getTotalList(search, vendorName, pageNo, pageSize, sysdate, startDate, currentDatePlusOne));
		return Response.ok(map).build();
	}

	@SuppressWarnings("rawtypes")
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
	public Response getListPurchaseOrderPagination(@FormParam("noPo") String noPo, @FormParam("vendorName") String vendorName,
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
		map.put("dataList", purchaseOrderItemSession.getListPagination(noPo, vendorName, startDate, currentDatePlusOne, orderKeyword,
				pageNo, pageSize, token));
		map.put("jmlData",
				purchaseOrderItemSession.getTotalList(noPo, vendorName, startDate, currentDatePlusOne, orderKeyword,
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
			sheet.addCell(new Label(0, 0, "Report Purchase Order", cellFormatHeader));

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
			//KAI - 12/01/2021 - [19451] 
			sheet.setColumnView(7, autoSizeCellView);

			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label noPo = new Label(1, 2, "No Po", cellFormat);
			sheet.addCell(noPo);
			Label tglPo = new Label(2, 2, "Tanggal PO", cellFormat);
			sheet.addCell(tglPo);
			//KAI - 12/01/2021 - [19451] 
			Label itemKtlog = new Label(3, 2, "Nama Item Katalog", cellFormat);
			sheet.addCell(itemKtlog);
			Label itemEbs = new Label(4, 2, "Nama Item", cellFormat);
			sheet.addCell(itemEbs);
			Label vendor = new Label(5, 2, "Penyedia", cellFormat);
			sheet.addCell(vendor);
			Label user = new Label(6, 2, "Pengguna", cellFormat);
			sheet.addCell(user);
			Label price = new Label(7, 2, "Total Harga", cellFormat);
			sheet.addCell(price);

			int number = 1;
			int cellRow = 3;
			List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemSession.getExel(itemName, vendorName, startDate,
					endDate, orderKeyword, pageNo, pageSize, token); 
			//KAI - 20201223 - [19451] 
			DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
	        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

	        formatRp.setCurrencySymbol("Rp. ");
	        formatRp.setMonetaryDecimalSeparator(',');
	        formatRp.setGroupingSeparator('.');

	        kursIndonesia.setDecimalFormatSymbols(formatRp);
	        //KAI - 12/01/2021 - [19451] 
			for (PurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {
				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow,
						((purchaseOrderItem.getPurchaseOrder() != null && purchaseOrderItem.getPurchaseOrder().getPoNumber() != null)
								? purchaseOrderItem.getPurchaseOrder().getPoNumber()
								: "-"),
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow,
						((purchaseOrderItem.getPurchaseOrder() != null && purchaseOrderItem.getPurchaseOrder().getPurchaseOrderDate() != null)
								? purchaseOrderItem.getPurchaseOrder().getPurchaseOrderDate().toString()
								: "-"),
						numberCellFormat));
				sheet.addCell(new Label(3, cellRow, ((purchaseOrderItem.getPurchaseRequestItem() != null) ? 
						purchaseOrderItem.getPurchaseRequestItem().getCatalog().getCatalogContractDetail().getItemDesc() : "-"), numberCellFormat));
				sheet.addCell(new Label(4, cellRow, ((purchaseOrderItem.getItem() != null) ? purchaseOrderItem.getItem().getNama() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(5, cellRow,
						((purchaseOrderItem.getVendor() != null && purchaseOrderItem.getVendor().getNama() != null)
								? purchaseOrderItem.getVendor().getNama()
								: "-"),
						numberCellFormat));
				sheet.addCell(new Label(6, cellRow,
						((purchaseOrderItem.getPurchaseOrder() != null
								&& purchaseOrderItem.getPurchaseOrder().getPurchaseRequest().getOrganisasi().getNama() != null)
										? purchaseOrderItem.getPurchaseOrder().getPurchaseRequest().getOrganisasi().getNama()
										: "-"),
						numberCellFormat));
				sheet.addCell(new Label(7, cellRow,
						((purchaseOrderItem.getTotalUnitPrices() != null) ? kursIndonesia.format(purchaseOrderItem.getTotalUnitPrices()) : "-"), //KAI - 20201223 - [19451] 
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
		Integer length=new Integer(0);
		if (!orderKeyword.equalsIgnoreCase("")) {
			if (orderKeyword.indexOf("asc") != -1) {
				length = orderKeyword.length()-4;
				orderKeyword=orderKeyword.substring(0, length);
				sort ="ASC";
			}else {
				length = orderKeyword.length()-5;
				orderKeyword=orderKeyword.substring(0, length);
				sort ="DESC";
			}			
		}
		Date currentDatePlusOne = calender.getTime();
		Token tokenObj = tokenSession.findByToken(tokenStr); 
		RoleUser roleUser =roleUserSession.getByToken(tokenObj);
		List<CatalogKontrak> catalogKontrakList = catalogKontrakSession.getReportCatalogKontrak(namaPekerjaan, vendorName, orderKeyword,
				pageNo, pageSize, sort, startDate, currentDatePlusOne,roleUser);
		for (CatalogKontrak catalogKontrak : catalogKontrakList) {
			if (catalogKontrak.getTglAkhirKontrak() != null) {
			Date dateNow = new Date();
			Date date = catalogKontrak.getTglAkhirKontrak();
			long difference = date.getTime() - dateNow.getTime();
			long value = difference / (24 * 60 * 60 * 1000);
			Integer reamingTime = (int) value;
			catalogKontrak.setReamingTime(reamingTime);
			}

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
			@FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize, 
			@FormParam("startDate") Date startDate, @FormParam("endDate") Date endDateFE, @HeaderParam("Authorization") String tokenStr) throws SQLException {
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
			sheet.setColumnView(7, autoSizeCellView);

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
			Label keterangan = new Label(7, 2, "Keterangan", cellFormat);
			sheet.addCell(keterangan);

			int number = 1;
			int cellRow = 3;
			Token tokenObj = tokenSession.findByToken(tokenStr); 
			RoleUser roleUser =roleUserSession.getByToken(tokenObj);
			Calendar calender = Calendar.getInstance();
			if (endDateFE != null) {
				calender.setTime(endDateFE);
				calender.add(Calendar.DATE, 1);
			}
			Date currentDatePlusOne = calender.getTime();
			
			List<CatalogKontrak> catalogKontrakList = catalogKontrakSession.getReportCatalogKontrak(namaPekerjaan, vendorName, orderKeyword,
					pageNo, pageSize, sort, startDate, currentDatePlusOne, roleUser);

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
				sheet.addCell(new Label(7, cellRow, ((catalogKontrak.getRefCatalogKontrakId() != null) ? "Addendum dari Kontrak Nomor "+catalogKontrak.getNomorKontrak() : "-"),
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
			@FormParam("type") Integer type, @FormParam("sort") String sortingType) throws SQLException {
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
			List<DeliveryReceived> budgetDanRealisasiList = budgetDanRealisasiSession.getExcelReportBudgetDanRealisasi(search, type, sortingType, null, null);

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

	@Path("/get-list-evaluasi-kinerja-vendor")
	@POST
	public Response getListEvaluasiKinerjaVendorPagination(@FormParam("vendorName") String vendorName,@FormParam("orderKeyword") String orderKeyword, @FormParam("sort") String sort,
			@FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize) {		
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jmlData", vendorSession.getTotalListEvaluasi(vendorName, orderKeyword));
		map.put("dataList",vendorSession.getReportEvaluasiKinerjaVendor(vendorName, orderKeyword,
				pageNo, pageSize, sort));

		return Response.ok(map).build();
	}

	@Path("/download-excel-evaluasi-kinerja-vendor")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportEvaluasiKinerjaVendor(@FormParam("vendorName") String vendorName, @FormParam("orderKeyword") String orderKeyword, @FormParam("sort") String sort) throws SQLException {
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report Evaluasi Kinerja Vendor", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Evaluasi Kinerja Vendor", 0);

			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Report Evaluasi Kinerja Vendor", cellFormatHeader));

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

			sheet.setColumnView(0, 3);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);
			
			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label vendor = new Label(1, 2, "Nama Penyedia", cellFormat);
			sheet.addCell(vendor);
			Label average = new Label(2, 2, "Average", cellFormat);
			sheet.addCell(average);
			Label penilaian = new Label(3, 2, "Penilaian", cellFormat);
			sheet.addCell(penilaian);

			Integer number = 1;
			Integer cellRow = 3;
			List<Vendor> evaluasiKinerjaVendorList = vendorSession.getExcelReportEvaluasiKinerjaVendor(vendorName, orderKeyword, sort);

			for (Vendor evaluasiKinerjaVendor : evaluasiKinerjaVendorList) {
				String nilai = "";
				if ( evaluasiKinerjaVendor.getRating() != null) 
				{
					if (evaluasiKinerjaVendor.getRating() <= 1) {
						nilai = "Sangat Kurang";
					}
					else if (evaluasiKinerjaVendor.getRating() <= 2) {
						nilai = "Kurang";
					}
					else if (evaluasiKinerjaVendor.getRating() <= 3)  {
						nilai = "Cukup";
					}
					else if (evaluasiKinerjaVendor.getRating() <= 4)  {
						nilai = "Baik";
					}
					else if (evaluasiKinerjaVendor.getRating() <= 5)  {
						nilai = "Sangat Baik";
					}
					else if (evaluasiKinerjaVendor.getRating() <= 6)  {
						nilai = "Sangat Memuaskan";
					}
				}
				else {
					nilai = "-";
				}

				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow, ((evaluasiKinerjaVendor.getNama() != null) ? evaluasiKinerjaVendor.getNama() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow, ((evaluasiKinerjaVendor.getRating() != null) ? evaluasiKinerjaVendor.getRating().toString() : "-"), currency));
				sheet.addCell(new Label(3, cellRow, nilai, numberCellFormat));
				
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
	
	@Path("/get-list-detail-evaluasi")
	@POST
	public Response getVendorByUserId(@FormParam("id") Integer id, @FormParam("poNumber") String poNumber, 
			@FormParam("prnumber") String prnumber, @FormParam("search") String search,
			@FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize, @FormParam("periode") Integer periode) {
		Vendor vendor = (Vendor) vendorSession.getVendor(id);
		Date now = new Date();
		Calendar calender = Calendar.getInstance();
		calender.setTime(now);
		if(periode == 1 ){ 
			calender.add(Calendar.MONTH, -1);
		  } 
		else if(periode == 2){ 
			calender.add(Calendar.MONTH, -3);
		  }
		else if(periode == 3){ 
			calender.add(Calendar.MONTH, -6);
		  }
		else if(periode == 4){ 
			calender.add(Calendar.MONTH, -12);
		  }
		Date filter = calender.getTime();
		if (periode == 0) {
			filter = null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jmlData", deliveryReceivedSession.getCountReportByVendor(vendor, search));
		map.put("dataList", deliveryReceivedSession.getReportByVendorDetail(vendor, poNumber, prnumber, search, pageNo, pageSize, filter));
		
		return Response.ok(map).build();
		
	}
	
	@Path("/download-excel-detail-evaluasi-kinerja-vendor")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportDetailEvaluasiKinerjaVendor(@FormParam("id") Integer id, @FormParam("poNumber") String poNumber, 
			@FormParam("prnumber") String prnumber, @FormParam("search") String search, @FormParam("periode") Integer periode) throws SQLException {
		Vendor vendor = (Vendor) vendorSession.getVendor(id);
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report Evaluasi Kinerja Vendor Detail", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Evaluasi Kinerja Vendor Detail", 0);

			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Report Evaluasi Kinerja Vendor Detail", cellFormatHeader));

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

			sheet.setColumnView(0, 9);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);
			sheet.setColumnView(4, autoSizeCellView);
			sheet.setColumnView(5, autoSizeCellView);
			sheet.setColumnView(6, autoSizeCellView);
			sheet.setColumnView(7, autoSizeCellView);
			sheet.setColumnView(8, autoSizeCellView);
			sheet.setColumnView(9, autoSizeCellView);
			
			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label nobo = new Label(1, 2, "No BO", cellFormat);
			sheet.addCell(nobo);
			Label nopo = new Label(2, 2, "No PO", cellFormat);
			sheet.addCell(nopo);
			Label nopr = new Label(3, 2, "No PR", cellFormat);
			sheet.addCell(nopr);
			Label namavendor = new Label(4, 2, "Nama Vendor", cellFormat);
			sheet.addCell(namavendor);
			Label tanggalorder = new Label(5, 2, "Tanggal Order", cellFormat);
			sheet.addCell(tanggalorder);
			Label tanggaldeliveryreceipt = new Label(6, 2, "Tanggal Delivery Receipt", cellFormat);
			sheet.addCell(tanggaldeliveryreceipt);
			Label rating = new Label(7, 2, "Rating", cellFormat);
			sheet.addCell(rating);
			Label penilaian = new Label(8, 2, "Penilaian", cellFormat);
			sheet.addCell(penilaian);
			Label komen = new Label(9, 2, "Komentar", cellFormat);
			sheet.addCell(komen);

			int number = 1;
			int cellRow = 3;
			Date now = new Date();
			Calendar calender = Calendar.getInstance();
			calender.setTime(now);
			if(periode == 1 ){ 
				calender.add(Calendar.MONTH, -1);
			  } 
			else if(periode == 2){ 
				calender.add(Calendar.MONTH, -3);
			  }
			else if(periode == 3){ 
				calender.add(Calendar.MONTH, -6);
			  }
			else if(periode == 4){ 
				calender.add(Calendar.MONTH, -12);
			  }
			Date filter = calender.getTime();
			if (periode == 0) {
				filter = null;
			}
			List<LaporanEvaluasiKinerjaVendorDTO> evaluasiKinerjaVendorDetailList = deliveryReceivedSession.getExcelReportByVendorDetail(vendor, poNumber, prnumber, search, filter);
			
			for (LaporanEvaluasiKinerjaVendorDTO laporanEvaluasiKinerjaVendorDTO : evaluasiKinerjaVendorDetailList) {

				Date approveDatePO = laporanEvaluasiKinerjaVendorDTO.getApprovedDate();
				Date deliveryReceipt = laporanEvaluasiKinerjaVendorDTO.getDateReceived();

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String strDeliveryReceipt = formatter.format(deliveryReceipt);
				String strApproveDatePO = formatter.format(approveDatePO);
				
				String penilaianDetail = "";
				
				if ( laporanEvaluasiKinerjaVendorDTO.getRating() != null) 
				{
					if (laporanEvaluasiKinerjaVendorDTO.getRating().equals(1)) {
						penilaianDetail = "Sangat Buruk";
					}
					else if (laporanEvaluasiKinerjaVendorDTO.getRating().equals(2)) {
						penilaianDetail = "Buruk";
					}
					else if (laporanEvaluasiKinerjaVendorDTO.getRating().equals(3))  {
						penilaianDetail = "Cukup";
					}
					else if (laporanEvaluasiKinerjaVendorDTO.getRating().equals(4))  {
						penilaianDetail = "Baik";
					}
					else if (laporanEvaluasiKinerjaVendorDTO.getRating().equals(5))  {
						penilaianDetail = "Sangat Baik";
					}
				}
				else {
					penilaianDetail = "-";
				}

				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow, ((laporanEvaluasiKinerjaVendorDTO.getBoNumber() != null) ? laporanEvaluasiKinerjaVendorDTO.getBoNumber() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow, ((laporanEvaluasiKinerjaVendorDTO.getPoNumberEbs() != null) ? laporanEvaluasiKinerjaVendorDTO.getPoNumberEbs() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(3, cellRow, ((laporanEvaluasiKinerjaVendorDTO.getPrNumber()!= null) ? laporanEvaluasiKinerjaVendorDTO.getPrNumber() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(4, cellRow, ((laporanEvaluasiKinerjaVendorDTO.getVendorName() != null) ? laporanEvaluasiKinerjaVendorDTO.getVendorName() : "-"), numberCellFormat));
				sheet.addCell(new Label(5, cellRow, ((strApproveDatePO != null) ? strApproveDatePO : "-"), numberCellFormat));
				sheet.addCell(new Label(6, cellRow, ((strDeliveryReceipt != null) ? strDeliveryReceipt : "-"), numberCellFormat));

				sheet.addCell(new Label(7, cellRow, ((laporanEvaluasiKinerjaVendorDTO.getRating() != null) ? laporanEvaluasiKinerjaVendorDTO.getRating().toString() : "-"), currency));
				sheet.addCell(new Label(8, cellRow, penilaianDetail, numberCellFormat));
				sheet.addCell(new Label(9, cellRow, ((laporanEvaluasiKinerjaVendorDTO.getKomen() != null) ? laporanEvaluasiKinerjaVendorDTO.getKomen() : "-"), numberCellFormat));

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
	
	@Path("/get-laporan-proses-pembelian")
	@POST
	public Response getProsesPembelian(@FormParam("search") String search, @FormParam("orderKeyword") String orderKeyword,
			@FormParam("sort") String sort, @FormParam("startDate") Date startDate, @FormParam("status") String status,
			@FormParam("endDate") Date endDate, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize, @HeaderParam("Authorization") String tokenStr) {	
		
		try {
			status = status == null || status.equals("undefined") || status.equals("") ? null : status;
			

		} catch (Exception e) {
			// kalo undefined
			status = null ;

		}
			Token tokenObj = tokenSession.findByToken(tokenStr); 
			RoleUser roleUser =roleUserSession.getByToken(tokenObj);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("jmlData", deliveryReceivedSession.getCountReportProsesPembelian(search, startDate, endDate, status, pageNo, pageSize, roleUser));
			map.put("dataList", deliveryReceivedSession.getReportProsesPembelian(search, orderKeyword, sort, startDate, endDate, status, pageNo, pageSize, roleUser));			
			return Response.ok(map).build();
			
		}
	
	@Path("/getPoItemJoinCatalogKontrak/{poId}")
	@GET
	public List<PurchaseOrderItem> getPoItemJoinCatalogKontrak(@PathParam("poId") Integer poId) {
		return purchaseOrderItemSession.getPoItemJoinCatalogKontrak(poId);
	}
	
	@Path("/get-vendor-list-catalog-contract")
	@GET
	public List<Vendor> getVendorListLaporanItemPO() {
		return vendorSession.getVendorListForCatalogContract();
	}
	
	@Path("/get-item-katalog-po")
	@POST
	public Response getItemKatalogPoPagination(@FormParam("type") Integer type, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize,
			@FormParam("search") String search, @FormParam("vendorName") String vendorName, @FormParam("startDate") Date startDate,
			@FormParam("endDate") Date endDate, @HeaderParam("Authorization") String token) throws ParseException {

		List<LaporanItemKatalogPoDTO> itemKatalogPo = catalogSession.getReportItemKatalogPo
				(search, type, pageNo, pageSize, vendorName, startDate, endDate, token);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dataList", itemKatalogPo);
		map.put("jmlData", catalogSession.getTotalList(search, type, pageNo, pageSize, vendorName, startDate, endDate, token));

		return Response.ok(map).build();
	}
	
	@Path("/download-excel-item-katalog-po")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@POST
	public Response downloadReportItemKatalogPo(@FormParam("type") Integer type, @FormParam("pageNo") Integer pageNo, @FormParam("pageSize") Integer pageSize,
			@FormParam("search") String search, @FormParam("vendorName") String vendorName, @FormParam("startDate") Date startDate,
			@FormParam("endDate") Date endDate, @HeaderParam("Authorization") String token) throws SQLException {
		File outputFile;
		byte[] buff = null;
		try {
			File fileName = File.createTempFile("Report Item Katalog PO", ".xls");
			String absolutePath = fileName.getAbsolutePath();
			String namaFile = fileName.getName();

			log.info(" absolutePath = " + absolutePath + " , namaFile = " + namaFile);
			
			WritableWorkbook workbook = Workbook.createWorkbook(new File(absolutePath));
			WritableSheet sheet = workbook.createSheet("Report Item Katalog PO", 0);
			CellView autoSizeCellView = new CellView();
			autoSizeCellView.setAutosize(true);

			WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 11);
			WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
			sheet.addCell(new Label(0, 0, "Report Item Katalog PO", cellFormatHeader));

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

			sheet.setColumnView(0, 9);
			sheet.setColumnView(1, autoSizeCellView);
			sheet.setColumnView(2, autoSizeCellView);
			sheet.setColumnView(3, autoSizeCellView);
			sheet.setColumnView(4, autoSizeCellView);
			sheet.setColumnView(5, autoSizeCellView);
			sheet.setColumnView(6, autoSizeCellView);
			sheet.setColumnView(7, autoSizeCellView);
			sheet.setColumnView(8, autoSizeCellView);
			sheet.setColumnView(9, autoSizeCellView);
			sheet.setColumnView(10, autoSizeCellView);
			
			Label lblNo = new Label(0, 2, "No", cellFormat);
			sheet.addCell(lblNo);
			Label lblKategory = new Label(1, 2, "Kategory Katalog", cellFormat);
			sheet.addCell(lblKategory);
			//KAI - 20201223 - [19451] hapus ratting
			Label produk = new Label(2, 2, "Nama Produk", cellFormat);
			sheet.addCell(produk);
			Label penyedia = new Label(3, 2, "Penyedia", cellFormat);
			sheet.addCell(penyedia);
			Label nobo = new Label(4, 2, "No. BO", cellFormat);
			sheet.addCell(nobo);
			Label nopo = new Label(5, 2, "No. PO", cellFormat);
			sheet.addCell(nopo);
			Label tanggalpo = new Label(6, 2, "Tanggal PO", cellFormat);
			sheet.addCell(tanggalpo);
			Label jumlah = new Label(7, 2, "Jumlah", cellFormat);
			sheet.addCell(jumlah);
			Label harga = new Label(8, 2, "Harga", cellFormat);
			sheet.addCell(harga);
			Label total = new Label(9, 2, "Total", cellFormat);
			sheet.addCell(total);
			//KAI - 20201223 - [19451] hapus ratting
			int number = 1;
			int cellRow = 3;

			List<LaporanItemKatalogPoDTO> laporanItemKatalogPoDTOList = catalogSession.getReportItemKatalogPo(search, type, pageNo, pageSize, vendorName, startDate, endDate, token);
			
			for (LaporanItemKatalogPoDTO laporanItemKatalogPoDTO : laporanItemKatalogPoDTOList) {

				Date approveDatePO = laporanItemKatalogPoDTO.getApprovedDate();

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String strApproveDatePO = formatter.format(approveDatePO);
				
				double price = laporanItemKatalogPoDTO.getPrice();
				double totalDouble = laporanItemKatalogPoDTO.getTotal();

		        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
		        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

		        formatRp.setCurrencySymbol("Rp. ");
		        formatRp.setMonetaryDecimalSeparator(',');
		        formatRp.setGroupingSeparator('.');

		        kursIndonesia.setDecimalFormatSymbols(formatRp);
				
				sheet.addCell(new Number(0, cellRow, number, numberCellFormat));
				sheet.addCell(new Label(1, cellRow, ((laporanItemKatalogPoDTO.getDescription() != null) ? laporanItemKatalogPoDTO.getDescription() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(2, cellRow, ((laporanItemKatalogPoDTO.getDeskripsiIND() != null) ? laporanItemKatalogPoDTO.getDeskripsiIND() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(3, cellRow, ((laporanItemKatalogPoDTO.getNama() != null) ? laporanItemKatalogPoDTO.getNama() : "-"),
						numberCellFormat));
				sheet.addCell(new Label(4, cellRow, ((laporanItemKatalogPoDTO.getBoNumber() != null) ? laporanItemKatalogPoDTO.getBoNumber() : "-"), numberCellFormat));
				sheet.addCell(new Label(5, cellRow, ((laporanItemKatalogPoDTO.getPoNumber() != null) ? laporanItemKatalogPoDTO.getPoNumber() : "-"), numberCellFormat));
				sheet.addCell(new Label(6, cellRow, ((strApproveDatePO != null) ? strApproveDatePO : "-"), numberCellFormat));
				sheet.addCell(new Label(7, cellRow, ((laporanItemKatalogPoDTO.getQuantity() != null) ? laporanItemKatalogPoDTO.getQuantity().toString() : "-"), numberCellFormat));
				sheet.addCell(new Label(8, cellRow, ((kursIndonesia.format(price))), currency));
				sheet.addCell(new Label(9, cellRow, ((kursIndonesia.format(totalDouble))), currency));
				//KAI - 20201223 - [19451] hapus ratting
				
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
