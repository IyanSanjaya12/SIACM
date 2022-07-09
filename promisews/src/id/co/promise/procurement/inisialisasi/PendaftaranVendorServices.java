package id.co.promise.procurement.inisialisasi;

import id.co.promise.procurement.entity.PendaftaranVendor;
import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.PerolehanPengadaanTotal;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.exception.CustomResponse;
import id.co.promise.procurement.exception.CustomResponseMessage;
import id.co.promise.procurement.purchaseorder.PurchaseOrderServices;
import id.co.promise.procurement.security.TokenSession;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

@Stateless
@Path(value = "/procurement/inisialisasi/pendaftaranVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class PendaftaranVendorServices {

	final static Logger log = Logger.getLogger(PurchaseOrderServices.class);
	final static CustomResponseMessage message = CustomResponse.getCustomResponseMessage();
	
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@EJB
	private PendaftaranVendorSession pendaftaranVendorSession;
	
	@EJB	
	private ItemPengadaanSession itemPengadaanSession;
	
	@EJB
	TokenSession tokenSession;

	@Path("/getPendaftaranVendor/{id}")
	@GET
	public PendaftaranVendor getPendaftaranVendor(@PathParam("id") int id) {
		return pendaftaranVendorSession.getPendaftaranVendor(id);
	}
	
	@Path("/getPendaftaranVendorByVendorUserIdTahapan/{userId}/{tahapanId}")
	@GET
	public List<PendaftaranVendor> getPendaftaranVendorByVendorUserIdTahapan(@PathParam("userId")int userId, @PathParam("tahapanId")int tahapanId){
		return pendaftaranVendorSession.getPendaftaranVendorByVendorUserIdTahapan(userId, tahapanId);
	}

	@Path("/getPendaftaranVendorByVendorUserIdAndPengadaanId/{userId}/{pengadaanId}")
	@GET
	public PendaftaranVendor getPendaftaranVendorByVendorUserIdAndPengadaanId(
			@PathParam("userId") int userId,
			@PathParam("pengadaanId") int pengadaanId) {
		return pendaftaranVendorSession
				.getPendaftaranVendorByVendorUserIdAndPengadaanId(userId,
						pengadaanId);
	}

	@Path("/getPendaftaranVendorByVendorUserId/{userId}")
	@GET
	public List<PendaftaranVendor> getPendaftaranVendorByVendorUserId(
			@PathParam("userId") int userId) {
		return pendaftaranVendorSession.getPendaftaranVendorByVendorUserId(userId);
	}
	
	@Path("/getSedangMengikutiPengadaanByVendorUserId/{userId}")
	@GET
	public List<PendaftaranVendor> getSedangMengikutiPengadaanByVendorUserId(
			@PathParam("userId") int userId) {
		return pendaftaranVendorSession.getSedangMengikutiPengadaanByVendorUserId(userId);
	}
	
	//service baru
	/*@Path("/getPernahMengikutiPengadaanByVendorUserId/{userId}")
	@GET
	public List<PendaftaranVendor> getPernahMengikutiPengadaanByVendorUserId(
			@PathParam("userId") int userId) {
		   
		    List<PendaftaranVendor> pvList = pendaftaranVendorSession.getPendaftaranVendorByVendorUserId(userId);
		    
		    List<PendaftaranVendor> pendaftaranvendorList = new ArrayList<PendaftaranVendor>();
		    	for (PendaftaranVendor pv : pvList){
		    	if(= )
		    }
		   
		    
		    return pvList;
	}*/
	
	//baru
	@Path("/getPernahMengikutiPengadaanByVendorUserId/{userId}")
	@GET
	public List<PerolehanPengadaanTotal> getPernahMengikutiPengadaanByVendorUserId(
			@PathParam("userId") int userId) {
		return pendaftaranVendorSession.getPernahMengikutiPengadaanByVendorUserId(userId);
	}
	
	
	@Path("/getPendaftaranVendorList")
	@GET
	public List<PendaftaranVendor> getPendaftaranVendorList() {
		return pendaftaranVendorSession.getPendaftaranVendorList();
	}

	@Path("/getPendaftaranVendorByPengadaan/{pengadaanId}")
	@GET
	public List<PendaftaranVendor> getPendaftaranVendorByPengadaan(
			@PathParam("pengadaanId") int pengadaanId) {
		return pendaftaranVendorSession
				.getPendaftaranVendorByPengadaan(pengadaanId);
	}
	
	@Path("/getPendaftaranVendorByVendorDateTahapan/{userId}/{tahapanId}")
	@GET
	public List<PendaftaranVendor> getPendaftaranVendorByVendorDateTahapan(
			@PathParam("userId") int userId,@PathParam("tahapanId") int tahapanId) {
		return pendaftaranVendorSession
				.getPendaftaranVendorByVendorDateTahapan(userId, tahapanId);
	}
	
	@Path("/getPendaftaranVendorByVendorDateTahapan/{tahapanId}")
	@GET
	public List<PendaftaranVendor> getPembukaanPenawaranListByPanitiaV2(
			@PathParam("tahapanId") int tahapanId,
			@HeaderParam("Authorization") String token) {
		User userLogin = tokenSession.findByToken(token).getUser();
		if (userLogin != null) {
			return pendaftaranVendorSession
					.getPendaftaranVendorByVendorDateTahapan(userLogin.getId(), tahapanId);
		}
		return null;
	}


	@Path("/insertPendaftaranVendor")
	@POST
	public PendaftaranVendor insertPendaftaranVendor(
			@FormParam("tanggalDaftar") String tanggalDaftar,
			@FormParam("nomorPendaftaran") String nomorPendaftaran,
			@FormParam("pengadaanId") int pengadaanId,
			@FormParam("vendorId") int vendorId,
			@HeaderParam("Authorization") String token) {
		PendaftaranVendor pv = new PendaftaranVendor();
		List<PendaftaranVendor> pvList = pendaftaranVendorSession.findByVendorIdAndPengadaanId(vendorId, pengadaanId);
		if(pvList.size() <= 0){
			try {
				if (tanggalDaftar.equals("")) {
					pv.setTanggalDaftar(new Date());
				} else {
					pv.setTanggalDaftar(format.parse(tanggalDaftar));
				}
				pv.setNomorPendaftaran(nomorPendaftaran);
				Pengadaan pengadaan = new Pengadaan();
				pengadaan.setId(pengadaanId);
				Vendor vendor = new Vendor();
				vendor.setId(vendorId);
				pv.setPengadaan(pengadaan);
				pv.setVendor(vendor);
				pv.setUserId(0);
				pendaftaranVendorSession.insertPendaftaranVendor(pv, tokenSession.findByToken(token));
			} catch (ParseException e) {
				e.printStackTrace();
			}	
		}else{
			pv = pvList.get(0);
		}
		
		return pv;
	}

	@Path("/updatePendaftaranVendor")
	@POST
	public PendaftaranVendor updatePendaftaranVendor(
			@FormParam("id") Integer id,
			@FormParam("tanggalDaftar") String tanggalDaftar,
			@FormParam("nomorPendaftaran") String nomorPendaftaran,
			@FormParam("pengadaanId") int pengadaanId,
			@FormParam("vendorId") int vendorId,
			@HeaderParam("Authorization") String token) {
		PendaftaranVendor pv = pendaftaranVendorSession.find(id);
		try {
			pv.setTanggalDaftar(format.parse(tanggalDaftar));
			pv.setNomorPendaftaran(nomorPendaftaran);
			Pengadaan pengadaan = new Pengadaan();
			pengadaan.setId(pengadaanId);
			Vendor vendor = new Vendor();
			vendor.setId(vendorId);
			pv.setPengadaan(pengadaan);
			pv.setVendor(vendor);
			pendaftaranVendorSession.updatePendaftaranVendor(pv, tokenSession.findByToken(token));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pv;
	}

	@Path("/deletePendaftaranVendor/{id}")
	@GET
	public PendaftaranVendor deletePendaftaranVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return pendaftaranVendorSession.deletePendaftaranVendor(id, tokenSession.findByToken(token));
	}

	@Path("/deleteRowPendaftaranVendor/{id}")
	@GET
	public PendaftaranVendor deleteRowPendaftaranVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return pendaftaranVendorSession.deleteRowPendaftaranVendor(id, tokenSession.findByToken(token));
	}
	
	@Path("/getPendaftaranVendorWithPagination")
	@POST
	public Response getPendaftaranVendorWithPagination(
			@FormParam("userId") Integer userId,
			@FormParam("pageNumber") Integer pageNumber, 
			@FormParam("numberOfRowPerPage") Integer numberOfRowPerPage,
			@FormParam("searchingKeyWord") String searchingKeyWord, 
			@HeaderParam("Authorization") String token) {
		
		try {
			if (pageNumber == null || numberOfRowPerPage == null || pageNumber == 0 || numberOfRowPerPage == 0) {
				pageNumber = 1;
				numberOfRowPerPage = 10;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(message.error(e.getMessage())).build();
		}

		try {
			Integer endIndex = pageNumber * numberOfRowPerPage;
			Integer startIndex = endIndex - numberOfRowPerPage + 1;
			return Response.status(201).entity(pendaftaranVendorSession.getPendaftaranVendorWithPagination(userId, startIndex, endIndex, searchingKeyWord)).build();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message.error(e.getMessage())).build();
		}
	}
}
