package id.co.promise.procurement.report;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.utils.ParamContext;
import id.co.promise.procurement.vendor.VendorSession;

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

import org.apache.poi.ss.usermodel.Cell;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Stateless
@Path(value = "/procurement/report/vendor")
public class ExcelVendorServices {
	
	@EJB
	VendorSession vendorSession;
	
	@Path("/print/{search}/{column}/{order}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@GET
	public Response printRekapHistoryVendor(@PathParam("search") String search, 
			@PathParam("column") String column,
			@PathParam("order") String order) throws SQLException {

		java.sql.Connection conn = ParamContext.getConnection();
		
		search = search.replaceAll("^\"|\"$", "");

		/*
		 * javax.naming.Context ic; try { ic = new
		 * javax.naming.InitialContext(); javax.naming.Context ctx =
		 * (javax.naming.Context) ic.lookup("java:"); javax.sql.DataSource ds =
		 * (javax.sql.DataSource) ctx.lookup("jboss/promise_bni_core");
		 * java.sql.Connection con = ds.getConnection(); }
		 * 
		 * catch (NamingException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		*/
		
		
		File outputFile;
		byte[] buff = null;
		
		try {
			   File fileName = File.createTempFile("Rekap-History-Vendor", ".xls");
	    	   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	           Date date =new Date();
			   String absolutePath = fileName.getAbsolutePath();
			   String namaFile = fileName.getName();

			   WritableWorkbook wworkbook;
	           wworkbook = Workbook.createWorkbook(new File(absolutePath));
	            // Create cell font and format
	           WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
	           cellFont.setBoldStyle(WritableFont.BOLD);
	           WritableFont cellFontNoBold = new WritableFont(WritableFont.ARIAL, 10);
	           
	           WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
	           cellFormat.setBackground(Colour.AQUA);
	           cellFormat.setAlignment(Alignment.CENTRE);
	           cellFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
	           
	           WritableCellFormat cellFormatColumn =new WritableCellFormat();
	           cellFormatColumn.setBorder(Border.ALL, BorderLineStyle.THIN);

	           WritableCellFormat cellFormatRight = new WritableCellFormat(cellFontNoBold);
	           cellFormatRight.setAlignment(Alignment.RIGHT);
	           
	           WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFontNoBold);
	           cellFormatCenter.setAlignment(Alignment.CENTRE);
	           
	           WritableSheet wsheet = wworkbook.createSheet("Rekap History Vendor", 0);
	           CellView autoSizeCellView = new CellView();
	           autoSizeCellView.setAutosize(true);
	           wsheet.setColumnView(0, 5);
	           wsheet.setColumnView(1, 40);
	           wsheet.setColumnView(2, 15);
	           wsheet.setColumnView(3, 35);
	           wsheet.setColumnView(4, 35);
	           wsheet.setColumnView(5, 25);
	          
	           wsheet.addCell(new Label(0,9,"No",cellFormat));
	           wsheet.addCell(new Label(1,9,"Nama Vendor",cellFormat));
	           wsheet.addCell(new Label(2,9,"Rating",cellFormat));
	           wsheet.addCell(new Label(3,9,"Pernah Mengikuti Pengadaan",cellFormat));
	           wsheet.addCell(new Label(4,9,"Sedang Mengikuti Pengadaan",cellFormat));
	           wsheet.addCell(new Label(5,9,"Menang Pengadaan",cellFormat));
	           
	           conn = ParamContext.getConnection();

	                      
	           int x=0; int y=9; 
	           NumberFormat decimalNo = new NumberFormat("#.0"); 
	           WritableCellFormat numberFormat = new WritableCellFormat(decimalNo);
	           
	           Double rating = 0.0;
	           List<Vendor> vendorList = vendorSession.getVendorListForExportExcel(search, column, order);
	           
	           for (Vendor vendor : vendorList) {
	        	   	x++;
	                y=y+1;
	                wsheet.addCell(new Label(0,y,String.valueOf(x),cellFormatColumn));
	                wsheet.addCell(new Label(1,y,vendor.getNama(),cellFormatColumn));
	                
	                
	                if(vendor.getRating() != null){
	                	rating = vendor.getRating();
	                }
	                
	                wsheet.addCell(new Label(2,y,String.valueOf(rating),cellFormatColumn));
	                wsheet.addCell(new Label(3,y,String.valueOf(vendor.getPengadaanCount()),cellFormatColumn));

	                wsheet.addCell(new Label(4,y,String.valueOf(vendor.getPengadaanRunningCount()),cellFormatColumn));
	                wsheet.addCell(new Label(5,y,String.valueOf(vendor.getWinnerCount()),cellFormatColumn));
	           }
	           
	           
	           
	           WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 13);
	           cellFontHeader.setBoldStyle(WritableFont.BOLD);
	           WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
	           cellFormatHeader.setAlignment(Alignment.CENTRE);
	           
	           wsheet.addCell(new Label(3,0,"Rekap History Vendor",cellFormatHeader));
	          // wsheet.addCell(new Label(3,1,"CO Number : " + salesOrder.getSoNumber()));
	        	wsheet.addCell(new Label(0,2,"PT Pelabuhan Indonesia II (Persero) "));
	           wsheet.addCell(new Label(0,3,"Jl. Pasoso No.1 - Tanjung Priok"));
	           wsheet.addCell(new Label(0,4,"Jakarta Utara 14310, Indonesia"));
	           wsheet.addCell(new Label(0,6,String.valueOf(dateFormat.format(date))));
	          
	           wworkbook.write();
	           wworkbook.close();
	              
	           // get file
	           outputFile = new File(absolutePath);
	           java.nio.file.Path path = Paths.get(outputFile.toString());
	           buff = Files.readAllBytes(path);
	           
	           
	           

		}  catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return Response.ok(buff).build();
	}
	
	@Path("/printPenilaianVendor/{namaVendor}/{bidangUsaha}/{subBidangUsaha}/{pernahdinilai}/{belumpernahdinilai}/{search}/{column}/{order}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@GET
	public Response printPenilaianVendor(@PathParam("namaVendor") String namaVendor,
			@PathParam("bidangUsaha") String bidangUsaha,
			@PathParam("subBidangUsaha") String subBidangUsaha,
			@PathParam("pernahdinilai") String pernahdinilai,
			@PathParam("belumpernahdinilai") String belumpernahdinilai,
			@PathParam("search") String search,
			@PathParam("column") String column,
			@PathParam("order") String order) throws SQLException {
		
		java.sql.Connection conn = ParamContext.getConnection();
		
		namaVendor = namaVendor.replaceAll("^\"|\"$", "");
		bidangUsaha = bidangUsaha.replaceAll("^\"|\"$", "");
		subBidangUsaha = subBidangUsaha.replaceAll("^\"|\"$", "");
		pernahdinilai = pernahdinilai.replaceAll("^\"|\"$", "");
		belumpernahdinilai = belumpernahdinilai.replaceAll("^\"|\"$", "");
		search = search.replaceAll("^\"|\"$", "");

		/*
		 * javax.naming.Context ic; try { ic = new
		 * javax.naming.InitialContext(); javax.naming.Context ctx =
		 * (javax.naming.Context) ic.lookup("java:"); javax.sql.DataSource ds =
		 * (javax.sql.DataSource) ctx.lookup("jboss/promise_bni_core");
		 * java.sql.Connection con = ds.getConnection(); }
		 * 
		 * catch (NamingException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		*/
		
		
		File outputFile;
		byte[] buff = null;
		
		try {
			   File fileName = File.createTempFile("Penilaian-Vendor", ".xls");
	    	   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	           Date date =new Date();
			   String absolutePath = fileName.getAbsolutePath();
			   String namaFile = fileName.getName();

			   WritableWorkbook wworkbook;
	           wworkbook = Workbook.createWorkbook(new File(absolutePath));
	            // Create cell font and format
	           WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
	           cellFont.setBoldStyle(WritableFont.BOLD);
	           WritableFont cellFontNoBold = new WritableFont(WritableFont.ARIAL, 10);
	           
	           WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
	           cellFormat.setBackground(Colour.AQUA);
	           cellFormat.setAlignment(Alignment.CENTRE);
	           cellFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
	           
	           WritableCellFormat cellFormatColumn =new WritableCellFormat();
	           cellFormatColumn.setBorder(Border.ALL, BorderLineStyle.THIN);

	           WritableCellFormat cellFormatRight = new WritableCellFormat(cellFontNoBold);
	           cellFormatRight.setAlignment(Alignment.RIGHT);
	           
	           WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFontNoBold);
	           cellFormatCenter.setAlignment(Alignment.CENTRE);
	           
	           WritableSheet wsheet = wworkbook.createSheet("Penilaian Vendor", 0);
	           CellView autoSizeCellView = new CellView();
	           autoSizeCellView.setAutosize(true);
	           wsheet.setColumnView(0, 5);
	           wsheet.setColumnView(1, 40);
	           wsheet.setColumnView(2, 30);
	           wsheet.setColumnView(3, 40);
	           wsheet.setColumnView(4, 25);
	          
	           wsheet.addCell(new Label(0,9,"No",cellFormat));
	           wsheet.addCell(new Label(1,9,"Nama Vendor",cellFormat));
	           wsheet.addCell(new Label(2,9,"Email",cellFormat));
	           wsheet.addCell(new Label(3,9,"Tanggal Terakhir Penilaian",cellFormat));
	           wsheet.addCell(new Label(4,9,"Rata-rata Rating",cellFormat));
	           
	           conn = ParamContext.getConnection();

	                      
	           int x=0; int y=9; 
	           NumberFormat decimalNo = new NumberFormat("#.0"); 
	           WritableCellFormat numberFormat = new WritableCellFormat(decimalNo);
	           
	           Double rating = 0.00;
	           List<Vendor> vendorList = vendorSession.getVendorPerformaForExcel(namaVendor, bidangUsaha, subBidangUsaha, pernahdinilai, belumpernahdinilai, search, column, order);
	           String datePerformance = "";
	           
	           for (Vendor vendor : vendorList) {
	        	   	x++;
	                y=y+1;
	                wsheet.addCell(new Label(0,y,String.valueOf(x),cellFormatColumn));
	                wsheet.addCell(new Label(1,y,vendor.getNama(),cellFormatColumn));
	                
	                
	                if(vendor.getPerformanceAvg() != null){
	                	rating = vendor.getPerformanceAvg();
	                }
	                
	                wsheet.addCell(new Label(2,y,vendor.getEmail(),cellFormatColumn));
	                
	                if(vendor.getPerformanceDate() != null) {
	                	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	 	                String datePeformanceVendor = df.format(vendor.getPerformanceDate());
	 	                wsheet.addCell(new Label(3,y,datePeformanceVendor,cellFormatColumn));
	                } else {
	                	wsheet.addCell(new Label(3,y,datePerformance,cellFormatColumn));
	                }
	                

	                wsheet.addCell(new Label(4,y,String.valueOf(rating),cellFormatColumn));
	           }
	           
	           
	           
	           WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 13);
	           cellFontHeader.setBoldStyle(WritableFont.BOLD);
	           WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
	           cellFormatHeader.setAlignment(Alignment.CENTRE);
	           
	           wsheet.addCell(new Label(2,0,"Penilaian Vendor",cellFormatHeader));
	          // wsheet.addCell(new Label(3,1,"CO Number : " + salesOrder.getSoNumber()));
	           wsheet.addCell(new Label(0,2,"PT Pelabuhan Indonesia II (Persero) "));
	           wsheet.addCell(new Label(0,3,"Jl. Pasoso No.1 - Tanjung Priok"));
	           wsheet.addCell(new Label(0,4,"Jakarta Utara 14310, Indonesia"));
	           wsheet.addCell(new Label(0,6,String.valueOf(dateFormat.format(date))));
	          
	           wworkbook.write();
	           wworkbook.close();
	              
	           // get file
	           outputFile = new File(absolutePath);
	           java.nio.file.Path path = Paths.get(outputFile.toString());
	           buff = Files.readAllBytes(path);
	           
	           
	           

		}  catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return Response.ok(buff).build();
	}
	

}
