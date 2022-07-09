package id.co.promise.procurement.vendor;

import id.co.promise.procurement.DTO.DokumenRegistrasiVendorDTO;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.DokumenRegistrasiVendorDraft;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.VendorSKDDraft;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.JsonObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Stateless
@Path(value="/procurement/vendor/DokumenRegistrasiVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class DokumenRegistrasiVendorServices {
	@EJB private DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;
	@EJB private DokumenRegistrasiVendorDraftSession dokumenRegistrasiVendorDraftSession;
	@EJB private VendorSession vendorSession;
	@EJB private VendorSKDSession vendorSKDSession;	
	@EJB private VendorSKDDraftSession vendorSKDDraftSession;
	@EJB private TokenSession tokenSession;
	
	@Path("/getDokumenRegistrasiVendorByVendorId/{vendorId}")
	@GET
	public List<DokumenRegistrasiVendor> getDokumenRegistrasiVendorByVendorId(@PathParam("vendorId") int vendorId){
		return dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorId(vendorId);
	}
	
	@Path("/getDokumenVendorSkdByVendorId/{vendorId}")
	@GET
	public List<VendorSKD> getDokumenVendorSkdByVendorId(@PathParam("vendorId") int vendorId){
		return vendorSKDSession.getVendorSkdByVendorId(vendorId);
	}
	@Path("/getDokumenRegistrasiVendorByVendorIdAndSubject/{vendorId}/{subject}")
	@GET
	public List<DokumenRegistrasiVendor> getDokumenRegistrasiVendorByVendorIdAndSubject(
			@PathParam("vendorId") int vendorId,
			@PathParam("subject") String subject){
		return dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorIdAndSubject(vendorId, subject);
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getdokumenregistrasivendorbyvendorid")
	@GET
	  public Map getdokumenregistrasivendorbyvendorid(
	      @HeaderParam("Authorization") String token) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    User user =tokenSession.findByToken(token).getUser();
	    Vendor vendor = vendorSession.getVendorByUserId(user.getId());
	    List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList = dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorId(vendor.getId());
	  
	    if(dokumenRegistrasiVendorDraftList.size() > 0){
	      map.put("dokumenRegistrasiVendorList",dokumenRegistrasiVendorDraftList);
	      List<VendorSKDDraft> vendorSKDDraftList= vendorSKDDraftSession.getVendorSkdDraftByVendorId(vendor.getId());
	      if(vendorSKDDraftList.size() > 0){
	    	  map.put("vendorSKDList",vendorSKDDraftList);
	      }
	      map.put("status",1);
	      map.put("SKBHistoryList", dokumenRegistrasiVendorSession.getHistoryDokumenRegistrasiVendor(vendor.getId(),"Surat Keterangan Bebas"));
	    }else{
	      List<DokumenRegistrasiVendor> dokumenRegistrasiVendorList = dokumenRegistrasiVendorSession.getDokumenRegistrasiVendorByVendorId(vendor.getId());
	      List<VendorSKD> vendorSKDList = vendorSKDSession.getVendorSkdByVendorId(vendor.getId());
	      map.put("vendorSKDList",vendorSKDList);
	      map.put("dokumenRegistrasiVendorList",dokumenRegistrasiVendorList);
	      map.put("SKBHistoryList", dokumenRegistrasiVendorSession.getHistoryDokumenRegistrasiVendor(vendor.getId(),"Surat Keterangan Bebas"));
	      map.put("status",0);
	    }	
	  
	    return map;
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getvendorskdbyvendorid")
	@GET
	  public Map getvendorskdbyvendorid(
	      @HeaderParam("Authorization") String token) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    User user =tokenSession.findByToken(token).getUser();
	    Vendor vendor = vendorSession.getVendorByUserId(user.getId());
	    List<VendorSKDDraft> vendorSKDDraftList= vendorSKDDraftSession.getVendorSkdDraftByVendorId(vendor.getId());
	    if(vendorSKDDraftList.size() > 0){
	      map.put("vendorSKDList",vendorSKDDraftList);
	      map.put("status",1);
	    }else{
	      List<VendorSKD> vendorSKDList = vendorSKDSession.getVendorSkdByVendorId(vendor.getId());
	      map.put("vendorSKDList",vendorSKDList);
	      map.put("status",0);
	    }	    
	    return map;
	}
	
	@Path("/insert")
	@POST
	public DokumenRegistrasiVendorDTO insertDokumenRegistrasiVendorDraft (DokumenRegistrasiVendorDTO dokumenRegistrasiVendorDTO,@HeaderParam("Authorization") String token){	
		Vendor vendor=vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		if(dokumenRegistrasiVendorDTO.getStatus()==0){
		/*insert data dokumen registrasi*/	
			List <DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList=dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorId(vendor.getId());
			if(dokumenRegistrasiVendorDraftList.size()>0){
					
			}else{
				for(DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft: dokumenRegistrasiVendorDTO.getDokumenRegistrasiVendorDraftList()){
					DokumenRegistrasiVendor dokumenRegistrasiVendor= new DokumenRegistrasiVendor();
					
					dokumenRegistrasiVendor.setId(dokumenRegistrasiVendorDraft.getId());
					dokumenRegistrasiVendorDraft.setId(null);
					dokumenRegistrasiVendorDraft.setVendor(vendor);
					dokumenRegistrasiVendorDraft.setIsDuplicate(0);
					dokumenRegistrasiVendorDraftSession.insertDokumenRegistrasiVendorDraft(dokumenRegistrasiVendorDraft, tokenSession.findByToken(token));
				}
			}
			/*insert data skd*/
			List <VendorSKDDraft> vendorSKDDraftList=vendorSKDDraftSession.getVendorSkdDraftByVendorId(vendor.getId());
			if(vendorSKDDraftList.size()>0){
			}else{
				VendorSKDDraft vendorSKDDraft= dokumenRegistrasiVendorDTO.getVendorSKDDraft();
				vendorSKDDraft.setId(null);
				vendorSKDDraft.setIsDelete(0);
				vendorSKDDraft.setVendor(vendor);
				vendorSKDDraftSession.insertVendorSKDDraft(vendorSKDDraft, tokenSession.findByToken(token));
			}	
		}
		else{
			List <DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList=dokumenRegistrasiVendorDraftSession.getDokumenRegistrasiVendorDraftByVendorId(vendor.getId());
			dokumenRegistrasiVendorDraftSession.deleteRowDokumenRegistrasiVendorDraftByVendor(dokumenRegistrasiVendorDraftList.get(0).getVendor().getId(), tokenSession.findByToken(token));
			for(DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft: dokumenRegistrasiVendorDTO.getDokumenRegistrasiVendorDraftList()){
				DokumenRegistrasiVendor dokumenRegistrasiVendor= new DokumenRegistrasiVendor();
				dokumenRegistrasiVendor.setId(dokumenRegistrasiVendorDraft.getId());
				dokumenRegistrasiVendorDraft.setId(null);
				dokumenRegistrasiVendorDraft.setVendor(vendor);
				dokumenRegistrasiVendorDraft.setIsDuplicate(0);
				dokumenRegistrasiVendorDraftSession.insertDokumenRegistrasiVendorDraft(dokumenRegistrasiVendorDraft, tokenSession.findByToken(token));
			}
			VendorSKDDraft vendorSKDDraft=vendorSKDDraftSession.getVendorSkdDraftByVendor(vendor.getId());
			if(vendorSKDDraft!=null){
				VendorSKDDraft vendorSKDDraftTemp= dokumenRegistrasiVendorDTO.getVendorSKDDraft();
				vendorSKDDraftTemp.setVendor(vendor);
				vendorSKDDraftTemp.setIsDelete(0);	
				vendorSKDDraftTemp.setId(vendorSKDDraft.getId());
				vendorSKDDraftSession.updateVendorSKDDraft(vendorSKDDraftTemp, tokenSession.findByToken(token));
			}else{
				VendorSKDDraft vendorSKDDraftTemp= dokumenRegistrasiVendorDTO.getVendorSKDDraft();
				vendorSKDDraftTemp.setId(null);
				vendorSKDDraftTemp.setIsDelete(0);
				vendorSKDDraftTemp.setVendor(vendor);
				vendorSKDDraftSession.insertVendorSKDDraft(vendorSKDDraftTemp, tokenSession.findByToken(token));
			}	
		}
		
		
		return dokumenRegistrasiVendorDTO;
	}
	

	@Path("/insertDokumenRegistrasiVendor")
	@POST
	public DokumenRegistrasiVendor insertDokumenRegistrasiVendor(DokumenRegistrasiVendor dokumenRegistrasiVendor, @HeaderParam("Authorization") String token) {
		dokumenRegistrasiVendor.setCreated(new Date());
		dokumenRegistrasiVendor.setIsDelete(0);
		dokumenRegistrasiVendorSession.insertDokumenRegistrasiVendor(dokumenRegistrasiVendor, tokenSession.findByToken(token));
		return dokumenRegistrasiVendor;
	}
	
	@Path("/checkNomorDokumen")
	@POST
	public ValidDTO checkNomorDokumen(DokumenRegistrasiVendor dokumenRegistrasiVendor, @HeaderParam("Authorization") String token) {
		ValidDTO validDTO = new ValidDTO();
		int vendorId= dokumenRegistrasiVendor.getVendor().getId();
		List<DokumenRegistrasiVendor>dokumenSKBVendorList = dokumenRegistrasiVendorSession.getAllByVendorAndSubject(vendorId,"Surat Keterangan Bebas");
		if(dokumenSKBVendorList.size()>0){
			validDTO = dokumenRegistrasiVendorSession.checkNomorDokumen(dokumenRegistrasiVendor);
		}else{
			validDTO.setValid(true);
		}
			
		return validDTO;
	}
	
	@Path("/updateDokumenRegistrasiVendor")
	@POST
	public DokumenRegistrasiVendor updateDokumenRegistrasiVendor(DokumenRegistrasiVendor dokumenRegistrasiVendor, @HeaderParam("Authorization") String token) {
		DokumenRegistrasiVendor dokumen = dokumenRegistrasiVendorSession.find(dokumenRegistrasiVendor.getId());
		if (dokumenRegistrasiVendor.getFileName() == null) {
			dokumenRegistrasiVendor.setFileName(dokumen.getFileName());
		}
		
		if (dokumenRegistrasiVendor.getFileSize() == null) {
			dokumenRegistrasiVendor.setFileSize(dokumen.getFileSize());
		}
		
		if (dokumenRegistrasiVendor.getPathFile() == null) {
			dokumenRegistrasiVendor.setPathFile(dokumen.getPathFile());
		}
		
		if (dokumenRegistrasiVendor.getStatusCeklist() == null) {
			dokumenRegistrasiVendor.setStatusCeklist(dokumen.getStatusCeklist());
		}
		dokumenRegistrasiVendor.setCreated(dokumen.getCreated());
		dokumenRegistrasiVendor.setIsDelete(0);
		dokumenRegistrasiVendorSession.updateDokumenRegistrasiVendor(dokumenRegistrasiVendor, tokenSession.findByToken(token));
		return dokumenRegistrasiVendor;
	}
	
	@Path("/insertVendorSKD")
	@POST
	public VendorSKD insertVendorSKD(VendorSKD vendorSKD, @HeaderParam("Authorization") String token) {
		vendorSKD.setCreated(new Date());
		vendorSKD.setIsDelete(0);
		vendorSKDSession.insertVendorSKD(vendorSKD, tokenSession.findByToken(token));
		return vendorSKD;
	}
	
	@Path("/editVendorSKD")
	@POST
	public VendorSKD editVendorSKD(VendorSKD vendorSKD, @HeaderParam("Authorization") String token) {
		VendorSKD vendorskd = vendorSKDSession.find(vendorSKD.getId());
		if (vendorSKD.getFileName() == null) {
			vendorSKD.setFileName(vendorskd.getFileName());
		}
		
		if (vendorSKD.getFileSize() == null) {
			vendorSKD.setFileSize(vendorskd.getFileSize());
		}
		
		if (vendorSKD.getPathFile() == null) {
			vendorSKD.setPathFile(vendorskd.getPathFile());
		}
		
		if (vendorSKD.getStatusCeklist() == null) {
			vendorSKD.setStatusCeklist(vendorskd.getStatusCeklist());
		}
		vendorSKD.setCreated(vendorskd.getCreated());
		vendorSKD.setIsDelete(0);
		vendorSKDSession.updateVendorSKD(vendorSKD, tokenSession.findByToken(token));
		return vendorSKD;
	}
			

	@Path("/deleteRowDokumenRegistrasiVendor/{id}")
	@GET
	public void deleteRowDokumenRegistrasiVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		dokumenRegistrasiVendorSession.deleteRowDokumenRegistrasiVendor(id, tokenSession.findByToken(token));
	}

	
	@Path("/getRekananDokumenExpiredPaggingList")
	@POST
	public JsonObject<DokumenRegistrasiVendor> getRekananDokumenExpiredPaggingList(
		@Context HttpServletRequest httpServletRequest, @HeaderParam("Authorization") String token){
		
		String start = httpServletRequest.getParameter("start");
		String draw	 = httpServletRequest.getParameter("draw");
		String length = httpServletRequest.getParameter("length");
		String search = httpServletRequest.getParameter("search[value]");
		String order = httpServletRequest.getParameter("order[0][dir]");
		String column = httpServletRequest.getParameter("order[0][column]");
		
		List<DokumenRegistrasiVendor> objList = dokumenRegistrasiVendorSession.getRekananDokumenExpiredPaggingList(Integer.valueOf(start), Integer.valueOf(length), search, Integer.valueOf(column), order);
		 
		Integer size = dokumenRegistrasiVendorSession.getRekananDokumenExpiredPaggingSize(search);
		
		JsonObject<DokumenRegistrasiVendor> jo = new JsonObject<DokumenRegistrasiVendor>();
		jo.setData(objList);
		jo.setRecordsTotal(size);
		jo.setRecordsFiltered(size);
		jo.setDraw(draw);
		return jo;
	}
}
