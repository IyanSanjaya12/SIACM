package id.co.promise.proucrement.alokasianggaran;

import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranPaginationDTO;
import id.co.promise.procurement.alokasianggaran.AlokasiAnggaranSession;
import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.JenisAnggaran;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PeriodeAnggaran;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.master.AutoNumberSession;
import id.co.promise.procurement.master.JenisAnggaranSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.master.PeriodeAnggaranSession;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.template.TemplateXls;
import id.co.promise.procurement.utils.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Stateless
@Path("/procurement/alokasianggaran/AlokasiAnggaranServices")
@Produces(MediaType.APPLICATION_JSON)
public class AlokasiAnggaranService {
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	@EJB AlokasiAnggaranSession alokasiAnggaranSession;

	@EJB PeriodeAnggaranSession periodeAnggaranSession;

	@EJB JenisAnggaranSession jenisAnggaranSession;

	@EJB OrganisasiSession organisasiSession;

	@EJB MataUangSession mataUangSession;

	@EJB TokenSession tokenSession;

	@EJB RoleUserSession roleUserSession;
	
	@EJB AutoNumberSession autoNumberSession;

	@Path("/get/{id}")
	@GET
	public AlokasiAnggaran getAlokasiAnggaran(@PathParam("id") int id) {
		return alokasiAnggaranSession.getAlokasiAnggaran(id);
	}

	@Path("/getList")
	@GET
	public List<AlokasiAnggaran> getList() {
		return alokasiAnggaranSession.getList();
	}

	@Path("/getListByUser")
	@GET
	public List<AlokasiAnggaran> getList(
			@HeaderParam("Authorization") String token) {
		Token objToken = tokenSession.findByToken(token);
		int userId = objToken.getUser().getId();
		return alokasiAnggaranSession.getList(userId);
	}

	@Path("/getListByIsUsed")
	@GET
	public List<AlokasiAnggaran> getListByIsUsed() {
		return alokasiAnggaranSession.getListByUsed();
	}
	
	@Path("/getAlokasiAnggaranByPaging")
	@POST
	public AlokasiAnggaranPaginationDTO getAlokasiAnggaranByPaging(
			@FormParam("search") String search,
			@FormParam("start") int startIndex, @FormParam("end") int endIndex,
			@HeaderParam("Authorization") String token) {
		return alokasiAnggaranSession.getAlokasiAnggaranByPaging(search,
				startIndex, endIndex, tokenSession.findByToken(token));
	}
	
	@Path("/getcoanumber")
	@GET
	public Response getPrNumber(@HeaderParam("Authorization") String token){
		Map<String, String> mapDTO = new HashMap<String, String>();
		mapDTO.put("coa", autoNumberSession.generateNumber("COA", tokenSession.findByToken(token)));
		return Response.ok(mapDTO).build();
	}

	@Path("/getAlokasiAnggaranByPagination")
	@POST 
	public Response getAlokasiAnggaranByPagination(
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token
			){
		Token objToken = tokenSession.findByToken(token);
		Integer userId = objToken.getUser().getId();
		
		String tempKeyword = "%" + keyword + "%";
		long countData = alokasiAnggaranSession.getAlokasiAnggaranListCount(tempKeyword,userId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);		
		result.put("data", alokasiAnggaranSession.getAlokasiAnggaranListWithPagination(start, length, tempKeyword,userId, columnOrder, tipeOrder));
		
		return Response.ok(result).build();
	}

	@Path("/findByBiroPengadaan")
	@GET
	public List<AlokasiAnggaran> findByBiroPengadaan(
			@HeaderParam("Authorization") String token) {
		Token objToken = tokenSession.findByToken(token);
		Integer userId = objToken.getUser().getId();
		List<RoleUser> roleUserList = roleUserSession
				.getRoleUserByUserId(userId);
		if (roleUserList.size() == 0) {
			return null;
		} else {
			return alokasiAnggaranSession.findByBiroPengadaan(roleUserList
					.get(0).getOrganisasi().getId());
		}
	}

	@Path("/getListByJenisNomorTahun/{jenis}/{nomor}/{tahun}")
	@GET
	public List<AlokasiAnggaran> getListByJenisNomorTahun(
			@PathParam("jenis") String jenis, @PathParam("nomor") String nomor,
			@PathParam("tahun") String tahun) {
		String nom = null;
		Integer jen = null;
		Integer tah = null;
		if (!jenis.equals("null")) {
			jen = Integer.parseInt(jenis);
		}
		if (!nomor.equals("null")) {
			try {
				nom = URLDecoder.decode(nomor, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (!tahun.equals("null")) {
			tah = Integer.parseInt(tahun);
		}
		return alokasiAnggaranSession.getByJenisNomorTahun(jen, nom, tah);
	}

	@Path("/getPeriodeList")
	@GET
	public List<PeriodeAnggaran> getPeriodeList() {
		return periodeAnggaranSession.getList();
	}

	@Path("/getJenisList")
	@GET
	public List<JenisAnggaran> getJenisList() {
		return jenisAnggaranSession.getList();
	}
	
	@Path("/getAlokasiAnggaranByNomorDraft/{nomorDraft}")
	@GET
	public AlokasiAnggaran getAlokasiAnggaranByNomorDraft(@PathParam("nomorDraft") String nomorDraft) {
		return alokasiAnggaranSession.getAlokasiAnggaranByNomorDraft(nomorDraft);
	}
	
	@Path("/getAlokasiAnggaranByAccountName")
	@POST
	public AlokasiAnggaran getAlokasiAnggaranByAccountName(@FormParam("accountName") String accountName) {
		return alokasiAnggaranSession.getAlokasiAnggaranByAccountName(accountName);
	}

	@Path("/create")
	@POST
	public AlokasiAnggaran createAlokasiAnggaran(
			@FormParam("nomorDraft") String nomorDraft,
			@FormParam("tanggalDraft") String tanggalDraft,
			@FormParam("accountName") String accountName,
			@FormParam("biroPengadaan") Integer biroPengadaan,
			@FormParam("tahunAnggaran") Integer tahunAnggaran,
			@FormParam("periodeAnggaran") Integer periodeAnggaran,
			@FormParam("jenisAnggaran") Integer jenisAnggaran,
			@FormParam("mataUang") Integer mataUang,
			@FormParam("jumlah") Double jumlah,
			@FormParam("status") Integer status,
			@FormParam("sisaAnggaran") Double sisaAnggaran,
			@FormParam("bookingAnggaran") Double bookingAnggaran,
			@FormParam("isUsed") Integer isUsed,
			@HeaderParam("Authorization") String token) {
		AlokasiAnggaran x = new AlokasiAnggaran();
		
		if ( bookingAnggaran == null ) {
			bookingAnggaran = (double) 0;
		}
		
		x.setNomorDraft(nomorDraft);
		x.setAccountName(accountName);
		try {
			x.setTanggalDraft(sdf.parse(tanggalDraft));
		} catch (Exception e) {
			x.setTanggalDraft(new Date());
		}
		if(biroPengadaan != null){
			x.setBiroPengadaan(organisasiSession.getOrganisasi(biroPengadaan));
		}
		x.setTahunAnggaran(tahunAnggaran);

		if (periodeAnggaran > 0){
			x.setPeriodeAnggaran(periodeAnggaranSession.getPeriodeAnggaran(periodeAnggaran));
		}

		x.setJenisAnggaran(jenisAnggaranSession.getJenisAnggaran(jenisAnggaran));
		x.setMataUang(mataUangSession.getMataUang(mataUang));
		x.setJumlah(jumlah);
		x.setBookingAnggaran(bookingAnggaran);
		x.setStatus(status);
		x.setSisaAnggaran(sisaAnggaran);
		x.setIsUsed(isUsed);
		alokasiAnggaranSession.createAlokasiAnggaran(x, tokenSession.findByToken(token));
		return x;
	}

	@Path("/update")
	@POST
	public AlokasiAnggaran upadteAlokasiAnggaran(@FormParam("id") Integer id,
			@FormParam("nomorDraft") String nomorDraft,
			@FormParam("accountName") String accountName,
			@FormParam("tanggalDraft") String tanggalDraft,
			@FormParam("biroPengadaan") Integer biroPengadaan,
			@FormParam("tahunAnggaran") Integer tahunAnggaran,
			@FormParam("periodeAnggaran") Integer periodeAnggaran,
			@FormParam("jenisAnggaran") Integer jenisAnggaran,
			@FormParam("mataUang") Integer mataUang,
			@FormParam("jumlah") Double jumlah,
			@FormParam("bookingAnggaran") Double bookingAnggaran,
			@FormParam("status") Integer status,
			@FormParam("sisaAnggaran") Double sisaAnggaran,
			@FormParam("isUsed") Integer isUsed,
			@HeaderParam("Authorization") String token) {
		AlokasiAnggaran x = alokasiAnggaranSession.getAlokasiAnggaran(id);
		x.setNomorDraft(nomorDraft);
		x.setAccountName(accountName);
		try {
			x.setTanggalDraft(sdf.parse(tanggalDraft));
		} catch (Exception e) {

		}
		x.setBiroPengadaan(organisasiSession.getOrganisasi(biroPengadaan));
		x.setTahunAnggaran(tahunAnggaran);
		if (periodeAnggaran > 0)
			x.setPeriodeAnggaran(periodeAnggaranSession
					.getPeriodeAnggaran(periodeAnggaran));
		x.setJenisAnggaran(jenisAnggaranSession.getJenisAnggaran(jenisAnggaran));
		x.setMataUang(mataUangSession.getMataUang(mataUang));
		x.setJumlah(jumlah);
		x.setStatus(status);
		x.setSisaAnggaran(sisaAnggaran);
		x.setBookingAnggaran(bookingAnggaran);
		x.setIsUsed(isUsed);
		alokasiAnggaranSession.updateAlokasiAnggaran(x,
				tokenSession.findByToken(token));
		return x;
	}

	@Path("/updateParentAndIsUsed")
	@POST
	public AlokasiAnggaran upadteAlokasiAnggaranParentAndIsUsed(
			@FormParam("id") Integer id, @FormParam("parent") Integer parent,
			@FormParam("isUsed") Integer isUsed,
			@HeaderParam("Authorization") String token) {
		AlokasiAnggaran x = alokasiAnggaranSession.getAlokasiAnggaran(id);
		x.setIsUsed(isUsed);
		x.setParentId(parent);
		alokasiAnggaranSession.updateAlokasiAnggaran(x,
				tokenSession.findByToken(token));
		return x;
	}

	@Path("/delete/{id}")
	@GET
	public AlokasiAnggaran delete(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return alokasiAnggaranSession.deleteAlokasiAnggaran(id, tokenSession.findByToken(token));
	}
	
	@Path("/findByAccountNameandByNomorDraff/{search}")
	@GET
	public List<AlokasiAnggaran> findByAccountNameandByNomorDraff(@PathParam("search") String search,
			@HeaderParam("Authorization") String token) {
		return alokasiAnggaranSession.findByAccountNameandByNomorDraff(search,
				tokenSession.findByToken(token));
	}
	
	@Path("/findByAccountNameandByNomorDraffAndOrganisasi")
	@POST
	public List<AlokasiAnggaran> findByAccountNameandByNomorDraffAndOrganisasi(@FormParam("search") String search,
			@HeaderParam("Authorization") String token) {
		return alokasiAnggaranSession.findByAccountNameandByNomorDraffAndOrganisasi(search,
				tokenSession.findByToken(token));
	}
	
	@Path("/findByAccountNameandByNomorDraffAndjenisAnggaran/{search}/{jenisAnggaranId}")
	@GET
	public List<AlokasiAnggaran> findByAccountNameandByNomorDraffAndjenisAnggaran(@PathParam("search") String search,
			@PathParam("jenisAnggaranId") int jenisAnggaranId,
			@HeaderParam("Authorization") String token) {
		Token objToken = tokenSession.findByToken(token);
		int userId = objToken.getUser().getId();
		return alokasiAnggaranSession.findByAccountNameandByNomorDraffAndjenisAnggaran(search, jenisAnggaranId,
				userId);
	}

	@Path("/getListByUserOrganisasi")
	@GET
	public List<AlokasiAnggaran> getListByUserOrganisasi(
			@HeaderParam("Authorization") String token) {
		
		Token objToken = tokenSession.findByToken(token);
		
		int userId = objToken.getUser().getId();
		
		return alokasiAnggaranSession.getListByUserOrganisasi(userId);
	}
	
	@Path("/getListByUserOrganisasi/{jenisAnggaranId}")
	@GET
	public List<AlokasiAnggaran> getListByUserOrganisasiAndJenisAnggaran(@PathParam("jenisAnggaranId") int jnsAnggaranId,
			@HeaderParam("Authorization") String token) {
		
		Token objToken = tokenSession.findByToken(token);
		
		int userId = objToken.getUser().getId();
		
		return alokasiAnggaranSession.getListByUserOrganisasiAndJenisAnggaran(userId, jnsAnggaranId);
	}
	
	@POST
	@Path("/readExcellAlokasi")
	@Consumes("multipart/form-data")
	public Response readExcell(MultipartFormDataInput input, @HeaderParam("Authorization") String token) {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts 				= uploadForm.get("file");
		Boolean hasil 							= false;

		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName 	= TemplateXls.getFileNameTemplateXls(header);
			Workbook workbook 	= null;
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
	        	if(fileName.endsWith("xlsx") || fileName.endsWith("xls")){
	        		workbook = new XSSFWorkbook(inputStream);
	        		hasil = analyseExcelForAlocation(workbook, token);	
	        	}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(hasil) {
			return Response.ok().build();
		} else {
			return Response.status(406).entity("File Upload Error and Not Acceptable").build();

		}
	}
	
	private Boolean analyseExcelForAlocation(Workbook workbook, String token) {
    	try{
    		Sheet datatypeSheet = workbook.getSheetAt(0);
	        Row row ;
	        for(int i=1;i<=datatypeSheet.getLastRowNum();i++){
	        	row = datatypeSheet.getRow(i);
	        	
	        	String nomorDraft 		= row.getCell(Constant.TEMPLATE_ALOCATION_DRAFT_NO).toString();
	        	String accountName 		= row.getCell(Constant.TEMPLATE_ALOCATION_ACCOUNT_NAME).toString();
	        	String tahunAnggaran 	= row.getCell(Constant.TEMPLATE_ALOCATION_TAHUN_ANGGARAN).toString();
	        	String jenisAnggaran 	= row.getCell(Constant.TEMPLATE_ALOCATION_JENIS_ANGGARAN).toString();
	        	String jumlah 			= row.getCell(Constant.TEMPLATE_ALOCATION_JUMLAH).toString();
	        	String namaOrganisasi 	= row.getCell(Constant.TEMPLATE_ALOCATION_UNIT).toString();
	        	
	        	AlokasiAnggaran anggaranNew = new AlokasiAnggaran();
	        	anggaranNew.setNomorDraft(nomorDraft);
	        	anggaranNew.setAccountName(accountName);
	        	if(tahunAnggaran.length() > 4) {
	        		tahunAnggaran = tahunAnggaran.substring(0, 4);
	        	}
	        	anggaranNew.setTahunAnggaran(Integer.parseInt(tahunAnggaran));
	        	anggaranNew.setTanggalDraft(new Date());
	        	anggaranNew.setJumlah(new Double(jumlah));
	        	anggaranNew.setBookingAnggaran(new Double(0));
	        	anggaranNew.setSisaAnggaran(new Double(jumlah));
	        	anggaranNew.setStatus(0);
	        	anggaranNew.setMataUang(mataUangSession.find(1));
	        	
	        	List<Organisasi> orgList = organisasiSession.findAll();
	        	if(orgList != null && orgList.size() > 0) {
	        		for(Organisasi org : orgList) {
	        			if(org.getNama().toUpperCase().equals(namaOrganisasi.toUpperCase())) {
	        				anggaranNew.setBiroPengadaan(org);
	        			}
	        		}
	        	}
	        	
	        	List<JenisAnggaran> jnsAgrnList = jenisAnggaranSession.findAll();
	        	if(jnsAgrnList != null && jnsAgrnList.size() > 0) {
	        		for(JenisAnggaran jAgrn : jnsAgrnList) {
	        			if(jAgrn.getNama().toUpperCase().equals(jenisAnggaran.toUpperCase())) {
	        				anggaranNew.setJenisAnggaran(jAgrn);
	        			}
	        		}
	        	}
	        	
	        	alokasiAnggaranSession.createAlokasiAnggaran(anggaranNew, tokenSession.findByToken(token));
	        }
	        
	        return true;
    	}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
