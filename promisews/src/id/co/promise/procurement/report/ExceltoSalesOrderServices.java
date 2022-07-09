package id.co.promise.procurement.report;

import id.co.promise.procurement.entity.SalesOrder;
import id.co.promise.procurement.entity.SalesOrderItem;
import id.co.promise.procurement.master.SalesOrderItemSession;
import id.co.promise.procurement.master.SalesOrderSession;
import id.co.promise.procurement.utils.ParamContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
@Path(value = "/procurement/report/salesorder")
public class ExceltoSalesOrderServices {
	
	@EJB
	SalesOrderItemSession salesOrderItemSession;
	
	@EJB
	SalesOrderSession salesOrderSession;
	
	public ExceltoSalesOrderServices() {
	}

	@Path("/print/{salesOrderid}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@GET
	public Response createSalesOrder(@PathParam("salesOrderid") Integer salesOrderid) throws SQLException {/*

		java.sql.Connection conn = ParamContext.getConnection();

		
		 * javax.naming.Context ic; try { ic = new
		 * javax.naming.InitialContext(); javax.naming.Context ctx =
		 * (javax.naming.Context) ic.lookup("java:"); javax.sql.DataSource ds =
		 * (javax.sql.DataSource) ctx.lookup("jboss/promise_bni_core");
		 * java.sql.Connection con = ds.getConnection(); }
		 * 
		 * catch (NamingException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 

		File outputFile;
		byte[] buff = null;
		
		try {
			   File fileName = File.createTempFile("Report-SO", ".xls");
	    	   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	           Date date =new Date();
			   String absolutePath = fileName.getAbsolutePath();
			   String namaFile = fileName.getName();

			   System.out.println(" absolutePath = " + absolutePath
					+ " , namaFile = " + namaFile);

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
	           
	           WritableSheet wsheet = wworkbook.createSheet("Confirmation Order", 0);
	           CellView autoSizeCellView = new CellView();
	           autoSizeCellView.setAutosize(true);
	           wsheet.setColumnView(0, 5);
	           wsheet.setColumnView(1, 25);
	           wsheet.setColumnView(2, 35);
	           wsheet.setColumnView(3, 10);
	           wsheet.setColumnView(4, 25);
	           wsheet.setColumnView(5, 25);
	          
	           
	           wsheet.addCell(new Label(0,11,"No",cellFormat));
	           wsheet.addCell(new Label(1,11,"ID Product",cellFormat));
	           wsheet.addCell(new Label(2,11,"Description",cellFormat));
	           wsheet.addCell(new Label(3,11,"Qty",cellFormat));
	           wsheet.addCell(new Label(4,11,"Price",cellFormat));
	           wsheet.addCell(new Label(5,11,"Sub Total",cellFormat));
	           
	           conn = ParamContext.getConnection();
	           
	           SalesOrder salesOrder = salesOrderSession.getSalesOrder(salesOrderid); 

	           //List<SalesOrderItem> salesOrderItemList = salesOrderItemSession.getSalesOrderItemBySalesOrderId(salesOrderid);
	           int x=0; int y=11; 
	           NumberFormat decimalNo = new NumberFormat("#.0"); 
	           WritableCellFormat numberFormat = new WritableCellFormat(decimalNo);
	           
	           Double grandTotal = 0.0;
	           for (SalesOrderItem salesOrderItem : salesOrderItemList) {
	        	   x++;
	                y=y+1;
	                wsheet.addCell(new Label(0,y,String.valueOf(x),cellFormatColumn));
	                wsheet.addCell(new Label(1,y,salesOrderItem.getCatalog().getKodeProduk(),cellFormatColumn));
	                
	                String des = salesOrderItem.getCatalog().getDeskripsiIND();
	                des.replaceAll("\\<.*?\\>", "");
	                wsheet.addCell(new Label(2,y,des,cellFormatColumn));
	                wsheet.addCell(new Label(3,y,String.valueOf(salesOrderItem.getQty()),cellFormatColumn));
	                
	                String harga = String.format("Rp.%,.0f", salesOrderItem.getCatalog().getHarga()).replaceAll(",", ".")+",00";
	                
	                wsheet.addCell(new Label(4,y,String.valueOf(harga),cellFormatColumn));
	                
	                String jumlah = String.format("Rp.%,.0f", salesOrderItem.getQty()*salesOrderItem.getCatalog().getHarga()).replaceAll(",", ".")+",00";
	               
	                wsheet.addCell(new Label(5,y,String.valueOf( jumlah),cellFormatColumn));
	                
	                grandTotal = grandTotal + (salesOrderItem.getQty()*salesOrderItem.getCatalog().getHarga());
	           }
	           
	           wsheet.addCell(new Label(4,x+12,"Grand Total",cellFormatColumn));
	           
	           String grTotal = String.format("Rp.%,.0f",grandTotal).replaceAll(",", ".")+",00";
	           
	           wsheet.addCell(new Label(5,x+12,String.valueOf(grTotal),cellFormatColumn));
	           
	           wsheet.addCell(new Label(0,x+15,String.valueOf(dateFormat.format(date))));
	           
	           WritableFont cellFontHeader = new WritableFont(WritableFont.ARIAL, 13);
	           cellFontHeader.setBoldStyle(WritableFont.BOLD);
	           WritableCellFormat cellFormatHeader = new WritableCellFormat(cellFontHeader);
	           wsheet.addCell(new Label(3,0,"Confirmation Order",cellFormatHeader));
	          // wsheet.addCell(new Label(3,1,"CO Number : " + salesOrder.getSoNumber()));
	           wsheet.addCell(new Label(0,1,"PT. Astra Internasional "));
	           wsheet.addCell(new Label(0,2,"Sunter II - Jakarta Utara"));
	           wsheet.addCell(new Label(0,3,"Jakarta 14330, Indonesia"));
	           
	           wsheet.addCell(new Label(1,5,"Bill To :"));
	           wsheet.addCell(new Label(1,6,salesOrder.getBillToName()));
	           wsheet.addCell(new Label(1,7,salesOrder.getBillToAddress()));
	           wsheet.addCell(new Label(1,8,salesOrder.getBillToTelp()));
	           
	           wsheet.addCell(new Label(4,5,"Ship To :"));
	           wsheet.addCell(new Label(4,6,salesOrder.getShipToName()));
	           wsheet.addCell(new Label(4,7,salesOrder.getShipToAddress()));
	           wsheet.addCell(new Label(4,8,salesOrder.getShipToTelp()));
	           
	           
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
	*/
		
	return null;
	}
}
