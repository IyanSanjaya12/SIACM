package id.co.promise.procurement.template;

import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.session.CategorySession;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.utils.StylePOI;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Stateless
@Path("/templateXls")
public class TemplateXls {
	
	@EJB private CategorySession categorySession;
	@EJB private OrganisasiSession organisasiSession;
	
	@GET
	@Path("/templateCatalog")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response templateCatalog(
			@Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                
                XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet 	  = workbook.createSheet("Catalog");
		        sheet.setZoom(75);
		        String[] titles = { "Material", "Vendor", "Nama Produk","Deskripsi Produk",
		        					"Kode Produk","Satuan","Harga","Mata Uang","Berat","Satuan Berat",
		        					"Panjang","Satuan Panjang","Lebar","Satuan Lebar","Tinggi","Satuan Tinggi","Category","Stock"
		        				  };
		        Row headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titles.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titles[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*50);
		        }
		        
		        workbook.write(output);
            }
        };

        ResponseBuilder response = Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.header("content-disposition", "attachment; filename=\"templateCatalog.xlsx");
        return response.build();
	}
	
	@GET
	@Path("/templateDR")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response templateDR(
			@Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                
                XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet 	  = workbook.createSheet("Delivery Received");
		        sheet.setZoom(75);
		        
		        String[] titles = { "KODE PO", "KODE ITEM", "PASS","FAILED","COMMENT"};
		        Row headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titles.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titles[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*50);
		        }
		        
		        workbook.write(output);
            }
        };

        ResponseBuilder response = Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.header("content-disposition", "attachment; filename=\"templateDR.xlsx");
        return response.build();
	}
	
	@GET
	@Path("/templateAtt")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response templateAtt(
			@Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                
                XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet 	  = workbook.createSheet("Attribute");
		        sheet.setZoom(100);
		        
		        String[] titles = { "nama", "input type", "option 1", "option 2", "Silahkan Teruskan(exp: option 3)"};
		        Row headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titles.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titles[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*24);
		            if(i == (titles.length - 1))
		            	sheet.setColumnWidth(i,256*50);
		        }
		        
		        workbook.write(output);
            }
        };
        
        ResponseBuilder response = Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.header("content-disposition", "attachment; filename=\"templateAtt.xlsx");
        return response.build();
	}
	
	@GET
	@Path("/templateAttGroup")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response templateAttGroup(
			@Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                
                XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet 	  = workbook.createSheet("Attribute Group");
		        sheet.setZoom(100);
		        
		        String[] titles = { "nama attribute group", "nama attribute panel", "nama attribute", "input type", "option 1", "option 2", "Silahkan Teruskan(exp: option 3)"};
		        Row headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titles.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titles[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*24);
		            if(i == (titles.length - 1))
		            	sheet.setColumnWidth(i,256*50);
		        }
		        
		        workbook.write(output);
            }
        };
        
        ResponseBuilder response = Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.header("content-disposition", "attachment; filename=\"templateAttGroup.xlsx");
        return response.build();
	}
	
	@GET
	@Path("/templatePurchaseRequest")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response templatePurchaseRequest(
			@Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                
                XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet 	  = workbook.createSheet("PR Header");
		        sheet.setZoom(100);
		        
		        String[] titles = { "PR Number", "Required Date", "Cost Center", "Department", "Term Condition", "Description" };
		        Row headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titles.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titles[i]);
		            cell.setCellType(Cell.CELL_TYPE_STRING);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*24);
		        }
		        
		        // sheet 2
		        sheet 	  = workbook.createSheet("PR Items");
		        sheet.setZoom(100);
		        
		        String[] titleSheet2 = { "PR Number", "Item Code", "Qty", "Price", "UOM"};
		        headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titleSheet2.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titleSheet2[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*24);
		        }
		        
		        // sheet 3
		        sheet 	  = workbook.createSheet("PR Shipping");
		        sheet.setZoom(100);
		        
		        String[] titleSheet3 = { "PR Number", "Address Label", "Item Code", "Qty"};
		        headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titleSheet3.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titleSheet3[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*24);
		        }
		        
		        OutputStream stream = httpServletResponse.getOutputStream();
		        workbook.write(stream);
            }
        };
        return Response.ok(stream).build();
	}
	
	@GET
	@Path("/templateCategory")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response templateCategory(
			@Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                
                XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet 	  = workbook.createSheet("Category");
		        sheet.setZoom(100);
		        
		        String[] titles = { "Deskripsi", "Translate Indonesia", "Translate Inggris", "Parent Category (Jika Cabang dari Category Lain)"};
		        Row headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titles.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titles[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*24);
		            if(i == (titles.length - 1))
		            	sheet.setColumnWidth(i,256*50);
		        }
		        
		        // Sheet Manual Input
		        XSSFSheet juknis 	  = workbook.createSheet("Petunjuk Teknis");
		        // Header
		        String[] juknisTitles = { "Kolom", "Tipe", "Keterangan", "Contoh"};
		        addColumnSheet(workbook, juknis, juknisTitles, 0, "header");
		        // Isi
		        String[] juknisIsi01 = { "Deskripsi", "Bebas (alfanumerik)", "Keterangan atau deskripsi dari kategori tersebut", "Kategori yang berhubungan dengan perkebunan"};
		        addColumnSheet(workbook, juknis, juknisIsi01, 1, null);
		        String[] juknisIsi02 = { "Translate Indonesia", "Bebas (alfanumerik)", "Label kategori dalam bahasa indonesia", "Perkebunan"};
		        addColumnSheet(workbook, juknis, juknisIsi02, 2, null);
		        String[] juknisIsi03 = { "Translate Inggris", "Bebas (alfanumerik)", "Label kategori dalam bahasa inggris", "Agriculture"};
		        addColumnSheet(workbook, juknis, juknisIsi03, 3, null);
		        String[] juknisIsi04 = { "Parent Category", "Bebas (alfanumerik)", "Kategori tempat kategori ini bernaung", "Pertanian"};
		        addColumnSheet(workbook, juknis, juknisIsi04, 4, null);
		        
		        // Sheet Parent Category
		        XSSFSheet kategori 	  = workbook.createSheet("List Avalaible Category");
		        // Header
		        String[] kategoriTitles = { "Kategori"};
		        addColumnSheet(workbook, kategori, kategoriTitles, 0, "header");
		        // Isi
		        List<Category> categoryList = categorySession.getCategoryList();
		        if(categoryList != null && categoryList.size() > 0) {
		        	Integer col = 1;
		        	for(Category cat : categoryList) {
		        		String[] kategoriIsi = { cat.getTranslateInd() };
				        addColumnSheet(workbook, kategori, kategoriIsi, col, null);
				        col += 1;
		        	}
		        }
		        
		        workbook.write(output);
            }
        };

        ResponseBuilder response = Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.header("content-disposition", "attachment; filename=\"templateCategory.xlsx");
        return response.build();
	}
	
	@GET
	@Path("/templateAlokasi")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response templateAlokasi(
			@Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                
            	XSSFWorkbook workbook = new XSSFWorkbook();
            	
            	// Sheet Template Data
		        XSSFSheet sheet 	  = workbook.createSheet("Anggaran");
		        sheet.setZoom(100);
		        
		        String[] titles = { "Nomor Draft", "Nama Akun", "Tahun Anggaran", "Jenis Anggaran", "Jumlah", "Organisasi/Unit"};
		        Row headerRow 	= sheet.createRow(0);
		        headerRow.setHeightInPoints(12.75f);
		        for (int i = 0; i < titles.length; i++) {
		            Cell cell = headerRow.createCell(i);
		            cell.setCellValue(titles[i]);
		            cell.setCellStyle(StylePOI.createStyles(workbook).get("header"));
		            sheet.setColumnWidth(i,256*24);
		            if(i == (titles.length - 1))
		            	sheet.setColumnWidth(i,256*50);
		        }
		        
		        // Sheet Manual Input
		        XSSFSheet juknis 	  = workbook.createSheet("Petunjuk Teknis");
		        // Header
		        String[] juknisTitles = { "Kolom", "Tipe", "Keterangan", "Contoh"};
		        addColumnSheet(workbook, juknis, juknisTitles, 0, "header");
		        // Isi
		        String[] juknisIsi01 = { "Nomor Draft", "Bebas (alfanumerik)", "Cost Center atau nomor COA / budget yang dipakai", "COA-FIN0001-01"};
		        addColumnSheet(workbook, juknis, juknisIsi01, 1, null);
		        String[] juknisIsi02 = { "Nama Akun", "Bebas (alfanumerik)", "Nama Akun dari Cost Center", "FINANCE-AHM"};
		        addColumnSheet(workbook, juknis, juknisIsi02, 2, null);
		        String[] juknisIsi03 = { "Tahun Anggaran", "Angka (numerik)", "Tahun anggaran akun tersebut akan digunakan", "2017"};
		        addColumnSheet(workbook, juknis, juknisIsi03, 3, null);
		        String[] juknisIsi04 = { "Jenis Anggaran", "Pilih: CAPEX atau OPEX", "Akun tersebut termasuk ke dalam CAPEX/OPEX", "CAPEX"};
		        addColumnSheet(workbook, juknis, juknisIsi04, 4, null);
		        String[] juknisIsi05 = { "Jumlah", "Numerik", "Jumlah Angka rupiah dari budget awal", "9999999999"};
		        addColumnSheet(workbook, juknis, juknisIsi05, 5, null);
		        String[] juknisIsi06 = { "Organisasi/Unit", "Pilih dari list Organisasi", "Organisasi/Unit/Departemen budget tersebut di peruntukkan. Harus sesuai dengan nama departemen yang ada", "PT Pelabuhan Indonesia II (Persero)"};
		        addColumnSheet(workbook, juknis, juknisIsi06, 6, null);
		        
		        // Sheet List Afco
		        XSSFSheet afco 	  = workbook.createSheet("List Organisasi");
		        // Header
		        String[] afcoTitles = { "Departemen"};
		        addColumnSheet(workbook, afco, afcoTitles, 0, "header");
		        // Isi
		        List<Organisasi> OrgList = organisasiSession.getOrganisasiAll();
		        if(OrgList != null && OrgList.size() > 0) {
		        	Integer col = 1;
		        	for(Organisasi org : OrgList) {
		        		String[] afcoIsi = { org.getNama() };
				        addColumnSheet(workbook, afco, afcoIsi, col, null);
				        col += 1;
		        	}
		        }
		        
		        workbook.write(output);
            }
        };

        ResponseBuilder response = Response.ok(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.header("content-disposition", "attachment; filename=\"templateAlokasi.xlsx");
        return response.build();
	}
	
	private void addColumnSheet(XSSFWorkbook workbook, XSSFSheet sheet, String[] titles, Integer row, String type) {
		sheet.setZoom(100);
		
		Row headerRow 	= sheet.createRow(row);
        headerRow.setHeightInPoints(12.75f);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(titles[i]);
            if(type != null)
            	cell.setCellStyle(StylePOI.createStyles(workbook).get(type));
            sheet.setColumnWidth(i,256*26);
        }
	}
	
	public static String getFileNameTemplateXls(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

}
