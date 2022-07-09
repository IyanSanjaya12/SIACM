package id.co.promise.procurement.vendor;

import id.co.promise.procurement.DTO.BankVendorDTO;
import id.co.promise.procurement.entity.Aksi;
import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BankVendorDraft;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SegmentasiVendorDraft;
import id.co.promise.procurement.entity.SubBidangUsaha;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.SegmentasiVendorDTO;
import id.co.promise.procurement.master.SubBidangUsahaSession;
import id.co.promise.procurement.security.TokenSession;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

@Stateless
@Path(value="/procurement/vendor/SegmentasiVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class SegmentasiVendorServices {
	@EJB private SegmentasiVendorSession segmentasiVendorSession;
	@EJB private SegmentasiVendorDraftSession segmentasiVendorDraftSession;
	@EJB private VendorSession vendorSession;
	@EJB private SubBidangUsahaSession subBidangUsahaSession;
	@EJB private JabatanSession jabatanSession;
	@EJB private TokenSession tokenSession;
	
	@Path("/getSegmentasiVendorByVendorId/{vendorId}")
	@GET
	public List<SegmentasiVendor> getSegmentasiVendorByVendorId(@PathParam("vendorId") int vendorId){
		return segmentasiVendorSession.getSegmentasiVendorByVendorId(vendorId);
	}
	
	@Path("/getDataSegmentasiById/{id}")
	@GET
	public SegmentasiVendor getSegmentasiVendor(@PathParam("id") int id) {
		return segmentasiVendorSession.getSegmentasiVendor(id);
	}
	
	@Path("/getListVendorBySegmentasi")
	@POST
	public VendorListDTO getListVendorBySegmentasi(
			@FormParam("vendorId") Integer vendorId,
			@FormParam("maxBaris") Integer maxBaris,
			@FormParam("halamanKe") Integer halamanKe){
		
		String subBidUshList = "";
		List<SubBidangUsaha> subBidangList = new ArrayList<SubBidangUsaha>(); 
		VendorListDTO vLDTO = new VendorListDTO();
		if(vendorId != null) {
			List<SegmentasiVendor> segmentasiVendorList = segmentasiVendorSession.getSegmentasiVendorByVendorId(vendorId);
			for(int i=0; i<segmentasiVendorList.size(); i++) {
				subBidUshList = subBidUshList + String.valueOf(segmentasiVendorList.get(i).getSubBidangUsaha().getId());
				if(i < (segmentasiVendorList.size()-1)) {
					subBidUshList = subBidUshList + ",";
				}
			}
			vLDTO = segmentasiVendorSession.getVendorBySubBidangUsahaId(subBidUshList, maxBaris, halamanKe, vendorId);
		}
		return vLDTO;
	}
	
	@Path("/createSegmentasiVendor")
	@POST
	public SegmentasiVendor createSegmentasiVendor (
			@FormParam("vendor") int vendorId, 
			@FormParam("subBidangUsaha") int subBidangUsahaId,
			@FormParam("asosiasi") String asosiasi, 
			@FormParam("nomor") String nomor, 
			@FormParam("tanggalMulai") Date tanggalMulai, 
			@FormParam("tanggalBerakhir") Date tanggalBerakhir, 
			@FormParam("jabatan") int jabatanId, 
			@FormParam("email") String email,
			@HeaderParam("Authorization") String token
			){
		SegmentasiVendor segmentasiVendor = new SegmentasiVendor();
		
		if (vendorId > 0) {
			segmentasiVendor.setVendor(vendorSession.find(vendorId));
		}
		
		if (subBidangUsahaId > 0) {
			segmentasiVendor.setSubBidangUsaha(subBidangUsahaSession.find(subBidangUsahaId));
		}
		
		if (asosiasi != null && asosiasi.length() > 0) {
			segmentasiVendor.setAsosiasi(asosiasi);
		}
		
		if (nomor != null && nomor.length() > 0) {
			segmentasiVendor.setNomor(nomor);
		}
		
		if (tanggalMulai != null) {
			segmentasiVendor.setTanggalMulai(tanggalMulai);
		} else {
			segmentasiVendor.setTanggalMulai(new Date());
		}
		
		if (tanggalBerakhir != null) {
			segmentasiVendor.setTanggalBerakhir(tanggalBerakhir);
		} else {
			segmentasiVendor.setTanggalBerakhir(new Date());
		}
		
		if (jabatanId > 0) {
			segmentasiVendor.setJabatan(jabatanSession.find(jabatanId));
		}
		
		if (email != null && email.length() > 0) {
			segmentasiVendor.setEmail(email);
		}
		
		segmentasiVendor.setCreated(new Date());
		segmentasiVendor.setIsDelete(0);
		
		segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor, tokenSession.findByToken(token));
		
		return segmentasiVendor;
	}
	
	/*@Path("/insert")
	@POST
	public SegmentasiVendorDTO insert(SegmentasiVendorDTO segmentasiVendorDTO,
			@HeaderParam("Authorization") String token) {
		
		SegmentasiVendor segmentasiVendor = segmentasiVendorDTO.getSegmentasiVendor();
		
		segmentasiVendor.setVendor(vendorSession.getVendorByUserId(segmentasiVendorDTO.getLoginId()));
		
		segmentasiVendor.setSubBidangUsaha(subBidangUsahaSession.find(segmentasiVendorDTO.getSegmentasiVendor().getSubBidangUsaha().getId()));
		
		segmentasiVendor.setEmail(segmentasiVendor.getVendor().getEmail());
		
		segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor, tokenSession.findByToken(token));
		return segmentasiVendorDTO;
	}
	
	@Path("/update")
	@POST
	public SegmentasiVendorDTO update(SegmentasiVendorDTO segmentasiVendorDTO,
			@HeaderParam("Authorization") String token) {
		SegmentasiVendor segmentasiVendor = segmentasiVendorDTO.getSegmentasiVendor();
		
		Token tokenize = tokenSession.findByToken(token);
		String email =  tokenize.getUser().getEmail();
		
		segmentasiVendor.setVendor(vendorSession.getVendorByUserId(segmentasiVendorDTO.getLoginId()));
		
		segmentasiVendor.setSubBidangUsaha(subBidangUsahaSession.find(segmentasiVendorDTO.getSegmentasiVendor().getSubBidangUsaha().getId()));
		
		segmentasiVendor.setEmail(segmentasiVendor.getVendor().getEmail());
		segmentasiVendorSession.updateSegmentasiVendor(segmentasiVendor, tokenSession.findByToken(token));
		return segmentasiVendorDTO;
	}*/
	
	@Path("/editSegmentasiVendor")
	@POST
	public SegmentasiVendor editSegmentasiVendor (
			@FormParam("id") int segmentasiVendorId, 
			@FormParam("vendor") int vendorId, 
			@FormParam("subBidangUsaha") int subBidangUsahaId,
			@FormParam("asosiasi") String asosiasi, 
			@FormParam("nomor") String nomor, 
			@FormParam("tanggalMulai") Date tanggalMulai, 
			@FormParam("tanggalBerakhir") Date tanggalBerakhir, 
			@FormParam("jabatan") int jabatanId, 
			@FormParam("email") String email,
			@HeaderParam("Authorization") String token
			){
		SegmentasiVendor segmentasiVendor = segmentasiVendorSession.find(segmentasiVendorId);
		
		if (vendorId > 0) {
			segmentasiVendor.setVendor(vendorSession.find(vendorId));
		}
		
		if (subBidangUsahaId > 0) {
			segmentasiVendor.setSubBidangUsaha(subBidangUsahaSession.find(subBidangUsahaId));
		}
		
		if (asosiasi != null && asosiasi.length() > 0) {
			segmentasiVendor.setAsosiasi(asosiasi);
		}
		
		if (nomor != null && nomor.length() > 0) {
			segmentasiVendor.setNomor(nomor);
		}
		
		if (tanggalMulai != null) {
			segmentasiVendor.setTanggalMulai(tanggalMulai);
		} else {
			segmentasiVendor.setTanggalMulai(new Date());
		}
		
		if (tanggalBerakhir != null) {
			segmentasiVendor.setTanggalBerakhir(tanggalBerakhir);
		} else {
			segmentasiVendor.setTanggalBerakhir(new Date());
		}
		
		if (jabatanId > 0) {
			segmentasiVendor.setJabatan(jabatanSession.find(jabatanId));
		}
		
		if (email != null && email.length() > 0) {
			segmentasiVendor.setEmail(email);
		}
		
		segmentasiVendorSession.updateSegmentasiVendor(segmentasiVendor, tokenSession.findByToken(token));
		
		return segmentasiVendor;
	}
	
	

	@Path("/deleteRowSegmentasiVendor/{id}")
	@POST
	public void deleteRowSegmentasiVendor(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		segmentasiVendorSession.deleteRowSegmentasiVendor(id, tokenSession.findByToken(token));
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getsegmentasibyvendorid")
	@GET
	public Map getsegmentasibyvendorid(
			@HeaderParam("Authorization") String token) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user =tokenSession.findByToken(token).getUser();
		
		Vendor vendor = vendorSession.getVendorByUserId(user.getId());
		
		List<SegmentasiVendorDraft> segmentasiVendorDraftList = segmentasiVendorDraftSession.getSegmentasiVendorDraftByVendorId(vendor.getId());
		if(segmentasiVendorDraftList.size() > 0){
			map.put("segmentasiVendorList",segmentasiVendorDraftList);
			map.put("status",1);
		}else{
			List<SegmentasiVendor> segmentasiVendorList = segmentasiVendorSession.getSegmentasiVendorByVendorId(vendor.getId());
			map.put("segmentasiVendorList",segmentasiVendorList);
			map.put("status",0);
		}
		
		
		return map;
	}
	
	@Path("/insert")
	@POST
	public SegmentasiVendorDTO insertSegmentasiVendorDraft (SegmentasiVendorDTO segmentasiVendorDTO,
			@HeaderParam("Authorization") String token
			) throws ParseException{
		
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		
		if(segmentasiVendorDTO.getStatus()==0){
			List<SegmentasiVendor> segmentasiVendorList = segmentasiVendorSession.getSegmentasiVendorByVendorId(vendor.getId());
			
			for(SegmentasiVendor segmentasiVendor : segmentasiVendorList) {
				SegmentasiVendorDraft segmentasiVendorDraftTemp = new SegmentasiVendorDraft();
				segmentasiVendorDraftTemp.setAsosiasi(segmentasiVendor.getAsosiasi());
				segmentasiVendorDraftTemp.setJabatan(segmentasiVendor.getJabatan());
				segmentasiVendorDraftTemp.setEmail(segmentasiVendor.getEmail());
				segmentasiVendorDraftTemp.setNomor(segmentasiVendor.getNomor());
				segmentasiVendorDraftTemp.setSegmentasiVendor(segmentasiVendor);
				segmentasiVendorDraftTemp.setCreated(segmentasiVendor.getCreated());
				segmentasiVendorDraftTemp.setSubBidangUsaha(segmentasiVendor.getSubBidangUsaha());
				segmentasiVendorDraftTemp.setUserId(segmentasiVendor.getUserId());
				segmentasiVendorDraftTemp.setTanggalBerakhir(segmentasiVendor.getTanggalBerakhir());
				segmentasiVendorDraftTemp.setTanggalMulai(segmentasiVendor.getTanggalMulai());
				segmentasiVendorDraftTemp.setVendor(segmentasiVendor.getVendor());
				segmentasiVendorDraftTemp.setUpdated(segmentasiVendor.getUpdated());
				segmentasiVendorDraftSession.insertSegmentasiVendorDraft(segmentasiVendorDraftTemp, tokenSession.findByToken(token));
				
			}
		}
	
		segmentasiVendorDTO.getSegmentasiVendorDraft().setVendor(vendor);
		segmentasiVendorDTO.getSegmentasiVendorDraft().setId(null);
		segmentasiVendorDTO.getSegmentasiVendorDraft().setIsDelete(0);
		segmentasiVendorDraftSession.insertSegmentasiVendorDraft(segmentasiVendorDTO.getSegmentasiVendorDraft(), tokenSession.findByToken(token));
		
		return	segmentasiVendorDTO;
		
	}
	
	@Path("/update")
	@POST
	public SegmentasiVendorDTO updateSegmentasiVendorDraft (SegmentasiVendorDTO segmentasiVendorDTO,
			@HeaderParam("Authorization") String token
			) throws ParseException{
		
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		SegmentasiVendorDraft segmentasiVendorDraft= new SegmentasiVendorDraft();
		
		if(segmentasiVendorDTO.getStatus()==0){// jika edit pertama kali yang datanya di ambil dari data master
			List<SegmentasiVendor> segmentasiVendorList = segmentasiVendorSession.getSegmentasiVendorByVendorId(vendor.getId());
			
			for(SegmentasiVendor segmentasiVendor : segmentasiVendorList) {
				SegmentasiVendorDraft segmentasiVendorDraftTemp = new SegmentasiVendorDraft();
				segmentasiVendorDraftTemp.setAsosiasi(segmentasiVendor.getAsosiasi());
				segmentasiVendorDraftTemp.setJabatan(segmentasiVendor.getJabatan());
				segmentasiVendorDraftTemp.setEmail(segmentasiVendor.getEmail());
				segmentasiVendorDraftTemp.setNomor(segmentasiVendor.getNomor());
				segmentasiVendorDraftTemp.setSegmentasiVendor(segmentasiVendor);
				segmentasiVendorDraftTemp.setCreated(segmentasiVendor.getCreated());
				segmentasiVendorDraftTemp.setSubBidangUsaha(segmentasiVendor.getSubBidangUsaha());
				segmentasiVendorDraftTemp.setUserId(segmentasiVendor.getUserId());
				segmentasiVendorDraftTemp.setTanggalBerakhir(segmentasiVendor.getTanggalBerakhir());
				segmentasiVendorDraftTemp.setTanggalMulai(segmentasiVendor.getTanggalMulai());
				segmentasiVendorDraftTemp.setVendor(segmentasiVendor.getVendor());
				segmentasiVendorDraftTemp.setUpdated(segmentasiVendor.getUpdated());
				segmentasiVendorDraftSession.insertSegmentasiVendorDraft(segmentasiVendorDraftTemp, tokenSession.findByToken(token));
				
				Integer segmentasiVendorDraftId = segmentasiVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(segmentasiVendorDTO.getSegmentasiVendor().getId().equals(segmentasiVendor.getId()))
				{
					segmentasiVendorDraft.setId(segmentasiVendorDraftId);//set id bankvendordraft untuk melakukan update
					segmentasiVendorDraft.setCreated(segmentasiVendor.getCreated());
				}
				
			}
			
			
			segmentasiVendorDraft.setAsosiasi(segmentasiVendorDTO.getSegmentasiVendor().getAsosiasi());
			segmentasiVendorDraft.setEmail(segmentasiVendorDTO.getSegmentasiVendor().getEmail());
			segmentasiVendorDraft.setNomor(segmentasiVendorDTO.getSegmentasiVendor().getNomor());
			segmentasiVendorDraft.setSegmentasiVendor(segmentasiVendorDTO.getSegmentasiVendor());
			segmentasiVendorDraft.setSubBidangUsaha(segmentasiVendorDTO.getSegmentasiVendor().getSubBidangUsaha());
			segmentasiVendorDraft.setTanggalBerakhir(segmentasiVendorDTO.getSegmentasiVendor().getTanggalBerakhir());
			segmentasiVendorDraft.setTanggalMulai(segmentasiVendorDTO.getSegmentasiVendor().getTanggalMulai());
			segmentasiVendorDraft.setVendor(vendor);
			
			segmentasiVendorDraft.setIsDelete(0);
			segmentasiVendorDraftSession.updateSegmentasiVendorDraft(segmentasiVendorDraft, tokenSession.findByToken(token));
		}else{//update jika data yang di ambil dari data draft
		
		segmentasiVendorDraft = segmentasiVendorDTO.getSegmentasiVendorDraft();
		segmentasiVendorDraft.setVendor(vendor);
		
		segmentasiVendorDraft.setIsDelete(0);	
		segmentasiVendorDraftSession.updateSegmentasiVendorDraft(segmentasiVendorDraft, tokenSession.findByToken(token));
		
		
		}
		return	segmentasiVendorDTO;
	}
	@Path("/delete")
	@POST
	public void deleteSegmentasiVendorDraft(@FormParam("id") int id,@FormParam("status") int status,
			@HeaderParam("Authorization") String token) {
		
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		SegmentasiVendorDraft segmentasiVendorDraft= new SegmentasiVendorDraft();
		
		if(status==0){// jika edit pertama kali yang datanya di ambil dari data master
			List<SegmentasiVendor> segmentasiVendorList = segmentasiVendorSession.getSegmentasiVendorByVendorId(vendor.getId());
			
			for(SegmentasiVendor segmentasiVendor : segmentasiVendorList) {
				SegmentasiVendorDraft segmentasiVendorDraftTemp = new SegmentasiVendorDraft();
				segmentasiVendorDraftTemp.setAsosiasi(segmentasiVendor.getAsosiasi());
				segmentasiVendorDraftTemp.setJabatan(segmentasiVendor.getJabatan());
				segmentasiVendorDraftTemp.setEmail(segmentasiVendor.getEmail());
				segmentasiVendorDraftTemp.setNomor(segmentasiVendor.getNomor());
				segmentasiVendorDraftTemp.setSegmentasiVendor(segmentasiVendor);
				segmentasiVendorDraftTemp.setCreated(segmentasiVendor.getCreated());
				segmentasiVendorDraftTemp.setSubBidangUsaha(segmentasiVendor.getSubBidangUsaha());
				segmentasiVendorDraftTemp.setUserId(segmentasiVendor.getUserId());
				segmentasiVendorDraftTemp.setTanggalBerakhir(segmentasiVendor.getTanggalBerakhir());
				segmentasiVendorDraftTemp.setTanggalMulai(segmentasiVendor.getTanggalMulai());
				segmentasiVendorDraftTemp.setVendor(segmentasiVendor.getVendor());
				segmentasiVendorDraftTemp.setUpdated(segmentasiVendor.getUpdated());
				segmentasiVendorDraftSession.insertSegmentasiVendorDraft(segmentasiVendorDraftTemp, tokenSession.findByToken(token));
				
				Integer segmentasiVendorDraftId = segmentasiVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(segmentasiVendor.getId().equals(id))
				{
					segmentasiVendorDraft.setId(segmentasiVendorDraftId);//set id bankvendordraft untuk melakukan update
				}
				
			}
			
			segmentasiVendorDraftSession.deleteSegmentasiVendorDraft(segmentasiVendorDraft.getId(), tokenSession.findByToken(token));
		}else
		{
			segmentasiVendorDraftSession.deleteSegmentasiVendorDraft(id, tokenSession.findByToken(token));	
		}
		
	}

}
