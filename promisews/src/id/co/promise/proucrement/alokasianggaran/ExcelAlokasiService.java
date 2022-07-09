package id.co.promise.proucrement.alokasianggaran;

import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.JenisAnggaran;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;
import id.co.promise.procurement.entity.PurchaseRequestImportDTO;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.master.JenisAnggaranSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.purchaserequest.PurchaseRequestServices;
import id.co.promise.procurement.security.TokenSession;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

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
@Path("/procurement/ExcelAlokasiServices")
@Produces(MediaType.APPLICATION_JSON)
public class ExcelAlokasiService {
	
	final static Logger log = Logger.getLogger(ExcelAlokasiService.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	final Gson gson;
	
	@EJB
	private TokenSession tokenSession;
	@EJB
	private AlokasiAnggaranSession alokasiAnggaranSession;
	@EJB
	private JenisAnggaranSession jenisAnggaranSession;
	@EJB
	private OrganisasiSession organisasiSession;
	@EJB
	private MataUangSession mataUangSession;
	
	public ExcelAlokasiService() {
		// BuatParsingJSONDateTypeAdapter
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new BuatParsingJSONDateTypeAdapter());
		gson = builder.create();
	}
	
	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response upload(MultipartFormDataInput input, @HeaderParam("Authorization") String token) {

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
				returnMsg = importAlokasiFromExcel(workbook, token);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String json = "{\"msg\":\"" + returnMsg + "\"}";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}
	
	private List<Row> getValidRows(Sheet sheet)
	{
		
		int jumlahRow = sheet.getLastRowNum();
		
		List<Row> rows = new ArrayList<Row>();
		for (int i = 1; i <= jumlahRow; i++) {
			
			if(sheet.getRow(i) != null){
				Row row = sheet.getRow(i);
				String cell1 = (row.getCell(0)).toString();
				String cell2 = (row.getCell(1)).toString();
				
				String str1 = cell1.trim();
				String str2 = cell2.trim();
			
				if (!str1.isEmpty() && !str2.isEmpty()) {
					rows.add(row);	
				}
				
			}
			
			else{
				break;
				
			}
		}
		
		return rows;
		
	}
	
	protected String importAlokasiFromExcel(Workbook workbook, String strToken) {
		
		List<AlokasiAnggaran> alokasiAnggaranList = new ArrayList<AlokasiAnggaran>();

		Token token = tokenSession.findByToken(strToken);

		// COST Alocation in sheet 0
		Sheet anggaranSheet = workbook.getSheetAt(0);
		
		int jumlahRow = anggaranSheet.getLastRowNum();
		log.info("Get Cost Allocation sheet. Number of row = " + jumlahRow);
		
		String oldAccNumber="";
		for (int a = 1; a <= jumlahRow; a++) {

			Row row = anggaranSheet.getRow(a);

			AlokasiAnggaran alokasiAnggaran = new AlokasiAnggaran();
			
			/* Account Code */
			Cell cell = row.getCell(0);
			log.info("CELL Account Code = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {

				String accNumber = cell.getStringCellValue().trim();
				
				/* validate if account code exist */
				List<AlokasiAnggaran> aaList = alokasiAnggaranSession.findByNomorDraft(accNumber);
				if (aaList.size() > 0) {
					return "ERROR!, Cost Allocation with Number " + accNumber + " already exist!";
				}
				if(oldAccNumber.equals(accNumber)){
					return "ERROR!, Account Code must be different";
				}

				alokasiAnggaran.setNomorDraft(accNumber);
				oldAccNumber = accNumber;
			} else {
				return "ERROR!, Account Code is Not String";
			}
			
			// Account Name
			cell = row.getCell(1);
			log.info("CELL Account Name = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				alokasiAnggaran.setAccountName(cell.getStringCellValue());
			} else {
				return "ERROR!, CA Account Name is Not String";
			}
			
			// Tahun Anggaran
			cell = row.getCell(2);
			log.info("CELL Tahun Anggaran = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if ((""+((int)cell.getNumericCellValue())).length() > 4){
					return "ERROR!, CA Tahun Anggaran maksimal 4 digit, "+cell.getNumericCellValue()+" not allowed";
				}
				alokasiAnggaran.setTahunAnggaran((int) cell.getNumericCellValue());
			} else {
				return "ERROR!, CA Tahun Anggaran  is Not Number";
			}
			
			// Jenis Anggaran
			cell = row.getCell(3);
			log.info("CELL Jenis Anggaran = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				
				List<JenisAnggaran> jnsAgrnList = jenisAnggaranSession.findAll();
				boolean jnsAngrnNotFound = true;
	        	if(jnsAgrnList != null && jnsAgrnList.size() > 0) {
	        		for(JenisAnggaran jAgrn : jnsAgrnList) {
	        			if(jAgrn.getNama().toUpperCase().equals(cell.getStringCellValue().toUpperCase())) {
	        				alokasiAnggaran.setJenisAnggaran(jAgrn);
	        				jnsAngrnNotFound = false;
	        				break;
	        			}
	        		}
	        	}

				if (jnsAngrnNotFound) return "ERROR!, CA Jenis Anggaran "+cell.getStringCellValue()+"  is Not Found";
			} else {
				return "ERROR!, CA Jenis Anggaran  is Not String";
			}
						
			// Jumlah
			cell = row.getCell(4);
			log.info("CELL jumlah anggaran = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				
				alokasiAnggaran.setJumlah(cell.getNumericCellValue());
				alokasiAnggaran.setSisaAnggaran(cell.getNumericCellValue());
				alokasiAnggaran.setBookingAnggaran((double) 0);
			} else {
				return "ERROR!, CA Jumlah Anggaran  is Not Number";
			}
			
			// Organisasi
			cell = row.getCell(5);
			log.info("CELL TYPE Organisasi = " + Constant.CELLSTRINGTYPE.get((cell.getCellType())));
			if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType() == Cell.CELL_TYPE_STRING) {
				boolean organisasiNotFound = true;
				List<Organisasi> orgList = organisasiSession.findAll();
	        	if(orgList != null && orgList.size() > 0) {
	        		for(Organisasi org : orgList) {
	        			if(org.getNama().toUpperCase().equals(cell.getStringCellValue().toUpperCase())) {
	        				alokasiAnggaran.setBiroPengadaan(org);
	        				organisasiNotFound = false;
	        				break;
	        			}
	        		}
	        	}
	        	
	        	if (organisasiNotFound) return "ERROR!, Organisasi "+cell.getStringCellValue()+" is Not Found";

			} else {
				return "ERROR!, Organisasi is Not String";
			}
			
			alokasiAnggaran.setStatus(0);
        	alokasiAnggaran.setMataUang(mataUangSession.find(1));
        	alokasiAnggaran.setTanggalDraft(new Date());
        	
        	alokasiAnggaranList.add(alokasiAnggaran);
		}
		
		
		for (AlokasiAnggaran alokasiAnggaran : alokasiAnggaranList){
			alokasiAnggaranSession.createAlokasiAnggaran(alokasiAnggaran, token);
		}
		
		return alokasiAnggaranList.size() + " cost allocation imported successfully!";
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
