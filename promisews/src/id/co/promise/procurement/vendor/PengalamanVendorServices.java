package id.co.promise.procurement.vendor;

import java.text.ParseException;
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

import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PengalamanVendorDraft;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.PengalamanVendorDTO;
import id.co.promise.procurement.security.TokenSession;

@Stateless
@Path(value="/procurement/vendor/PengalamanVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class PengalamanVendorServices {
	@EJB private PengalamanVendorSession pengalamanVendorSession;
	@EJB private VendorSession vendorSession;
	@EJB private MataUangSession mataUangSession;
	@EJB private TokenSession tokenSession;
	@EJB private PengalamanVendorDraftSession pengalamanVendorDraftSession;
	
	@Path("/getPengalamanVendorByVendorId/{vendorId}")
	@GET
	public List<PengalamanVendor> getPengalamanVendorByVendorId(@PathParam("vendorId") int vendorId){
		return pengalamanVendorSession.getPengalamanVendorByVendorId(vendorId);
	}
	
	
	@Path("/getPengalamanVendorByVendorIdAndTipePengalaman/{vendorId}/{tipePengalaman}")
	@GET
	public List<PengalamanVendor> getPengalamanVendorByVendorIdAndTipePengalaman(
			@PathParam("vendorId") int vendorId,
			@PathParam("tipePengalaman") String tipePengalaman){
		return pengalamanVendorSession.getPengalamanVendorByVendorIdAndTipePengalaman(vendorId, tipePengalaman);
	}
	
	
	@Path("/editPengalamanVendor")
	@POST
	public PengalamanVendor editPengalamanVendor (
			@FormParam("id") int pengalamanVendorId, 
			@FormParam("vendor") int vendorId, 
			@FormParam("tipePengalaman") String tipePengalaman, 
			@FormParam("namaPekerjaan") String namaPekerjaan, 
			@FormParam("lokasiPekerjaan") String lokasiPekerjaan, 
			@FormParam("bidangUsaha") String bidangUsaha, 
			@FormParam("mulaiKerjasama") Date mulaiKerjasama,  
			@FormParam("nilaiKontrak") double nilaiKontrak,  
			@FormParam("mataUang") int mataUangId,  
			@FormParam("fileName") String fileName, 
			@FormParam("pathFile") String pathFile, 
			@FormParam("fileSize") long fileSize,
			@HeaderParam("Authorization") String token
			){
		PengalamanVendor pengalamanVendor = pengalamanVendorSession.find(pengalamanVendorId);
		
		if (vendorId > 0) {
			pengalamanVendor.setVendor(vendorSession.find(vendorId));
		}
		
		if (tipePengalaman != null && tipePengalaman.length() > 0) {
			pengalamanVendor.setTipePengalaman(tipePengalaman);
		}
		
		if (namaPekerjaan != null && namaPekerjaan.length() > 0) {
			pengalamanVendor.setNamaPekerjaan(namaPekerjaan);
		}
		
		if (lokasiPekerjaan != null && lokasiPekerjaan.length() > 0) {
			pengalamanVendor.setLokasiPekerjaan(lokasiPekerjaan);
		}
		
		if (bidangUsaha != null && bidangUsaha.length() > 0) {
			pengalamanVendor.setBidangUsaha(bidangUsaha);
		}
		
		if (mulaiKerjasama != null) {
			pengalamanVendor.setMulaiKerjasama(mulaiKerjasama);
		}
		
		if (nilaiKontrak > 0) {
			pengalamanVendor.setNilaiKontrak(nilaiKontrak);
		}
		
		if (mataUangId > 0) {
			pengalamanVendor.setMataUang(mataUangSession.find(mataUangId));
		}
		
		if (fileName != null && fileName.length() > 0) {
			pengalamanVendor.setFileName(fileName);
		}
		
		if (pathFile != null && pathFile.length() > 0) {
			pengalamanVendor.setPathFile(pathFile);
		}
		
		if (fileSize > 0) {
			pengalamanVendor.setFileSize(fileSize);
		}
		
		pengalamanVendorSession.updatePengalamanVendor(pengalamanVendor, tokenSession.findByToken(token));
		
		return pengalamanVendor;
	}

	@Path("/deleteRowPengalamanVendor/{id}")
	@GET
	public void deleteRowPengalamanVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		pengalamanVendorSession.deleteRowPengalamanVendor(id, tokenSession.findByToken(token));
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getpengalamanbyvendorid")
	@GET
	public Map getpengalamanbyvendorid(
			@HeaderParam("Authorization") String token) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user =tokenSession.findByToken(token).getUser();
		
		Vendor vendor = vendorSession.getVendorByUserId(user.getId());
		
		List<PengalamanVendorDraft> pengalamanVendorDraftList = pengalamanVendorDraftSession.getPengalamanVendorDraftById(vendor.getId());
		if(pengalamanVendorDraftList.size() > 0){
			map.put("pengalamanVendorList",pengalamanVendorDraftList);
			map.put("status",1);
		}else{
			List<PengalamanVendor> pengalamanVendorList = pengalamanVendorSession.getPengalamanVendorById(vendor.getId());
			map.put("pengalamanVendorList",pengalamanVendorList);
			map.put("status",0);
		}
		
		
		return map;
	}
	
	@Path("/insert")
	@POST
	public Map insertPengalamanVendorDraft (PengalamanVendorDTO pengalamanVendorDTO,
			@HeaderParam("Authorization") String token
			) throws ParseException{
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "add";
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		
		if(pengalamanVendorDTO.getStatus()==0){
			List<PengalamanVendor> pengalamanVendorList = pengalamanVendorSession.getPengalamanVendorById(vendor.getId());
			
			for(PengalamanVendor pengalamanVendor : pengalamanVendorList) {
				PengalamanVendorDraft pengalamanVendorDraftTemp = new PengalamanVendorDraft();
				pengalamanVendorDraftTemp.setNamaPekerjaan(pengalamanVendor.getNamaPekerjaan());
				pengalamanVendorDraftTemp.setLokasiPekerjaan(pengalamanVendor.getLokasiPekerjaan());
				pengalamanVendorDraftTemp.setBidangUsaha(pengalamanVendor.getBidangUsaha());
				pengalamanVendorDraftTemp.setCreated(pengalamanVendor.getCreated());
				pengalamanVendorDraftTemp.setFileName(pengalamanVendor.getFileName());
				pengalamanVendorDraftTemp.setFileSize(pengalamanVendor.getFileSize());
				pengalamanVendorDraftTemp.setMataUang(pengalamanVendor.getMataUang());
				pengalamanVendorDraftTemp.setMulaiKerjasama(pengalamanVendor.getMulaiKerjasama());
				pengalamanVendorDraftTemp.setNilaiKontrak(pengalamanVendor.getNilaiKontrak());
				pengalamanVendorDraftTemp.setPathFile(pengalamanVendor.getPathFile());
				pengalamanVendorDraftTemp.setVendor(vendor);
				pengalamanVendorDraftTemp.setUpdated(pengalamanVendor.getUpdated());
				pengalamanVendorDraftTemp.setPengalamanVendor(pengalamanVendor);
				pengalamanVendorDraftTemp.setTipePengalaman(pengalamanVendor.getTipePengalaman());
				pengalamanVendorDraftSession.insertPengalamanVendorDraft(pengalamanVendorDraftTemp, tokenSession.findByToken(token));
				
			}
		}
		
		
		
		pengalamanVendorDTO.getPengalamanVendorDraft().setId(null);
		
		Boolean isSaveNama = pengalamanVendorDraftSession.checkNamaPengalamaan(pengalamanVendorDTO.getPengalamanVendorDraft().getNamaPekerjaan(),pengalamanVendorDTO.getPengalamanVendorDraft().getTipePengalaman(), toDo, pengalamanVendorDTO.getPengalamanVendorDraft().getId());

		if (!isSaveNama) {
			String errorNama = "Nama Pekerjaan  Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			pengalamanVendorDTO.getPengalamanVendorDraft().setIsDelete(0);
			pengalamanVendorDTO.getPengalamanVendorDraft().setVendor(vendor);
			pengalamanVendorDraftSession.insertPengalamanVendorDraft(pengalamanVendorDTO.getPengalamanVendorDraft(), tokenSession.findByToken(token));
		}
		map.put("pengalamanVendor", pengalamanVendorDTO.getPengalamanVendorDraft());
		
		
		
		
		return	map;
		
	}
	
	@Path("/update")
	@POST
	public Map updatePengalamanVendorDraft (PengalamanVendorDTO pengalamanVendorDTO,
			@HeaderParam("Authorization") String token
			) throws ParseException{
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		String toDo = "edit";
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		PengalamanVendorDraft pengalamanVendorDraft= new PengalamanVendorDraft();
		
		if(pengalamanVendorDTO.getStatus()==0){// jika edit pertama kali yang datanya di ambil dari data master
			List<PengalamanVendor> pengalamanVendorList = pengalamanVendorSession.getPengalamanVendorByVendorId(vendor.getId());
			
			for(PengalamanVendor pengalamanVendor : pengalamanVendorList) {
				PengalamanVendorDraft pengalamanVendorDraftTemp = new PengalamanVendorDraft();
				pengalamanVendorDraftTemp.setNamaPekerjaan(pengalamanVendor.getNamaPekerjaan());
				pengalamanVendorDraftTemp.setLokasiPekerjaan(pengalamanVendor.getLokasiPekerjaan());
				pengalamanVendorDraftTemp.setBidangUsaha(pengalamanVendor.getBidangUsaha());
				pengalamanVendorDraftTemp.setCreated(pengalamanVendor.getCreated());
				pengalamanVendorDraftTemp.setFileName(pengalamanVendor.getFileName());
				pengalamanVendorDraftTemp.setFileSize(pengalamanVendor.getFileSize());
				pengalamanVendorDraftTemp.setMataUang(pengalamanVendor.getMataUang());
				pengalamanVendorDraftTemp.setMulaiKerjasama(pengalamanVendor.getMulaiKerjasama());
				pengalamanVendorDraftTemp.setNilaiKontrak(pengalamanVendor.getNilaiKontrak());
				pengalamanVendorDraftTemp.setPathFile(pengalamanVendor.getPathFile());
				pengalamanVendorDraftTemp.setVendor(vendor);
				pengalamanVendorDraftTemp.setUpdated(pengalamanVendor.getUpdated());
				pengalamanVendorDraftTemp.setPengalamanVendor(pengalamanVendor);
				pengalamanVendorDraftTemp.setTipePengalaman(pengalamanVendor.getTipePengalaman());
				pengalamanVendorDraftSession.insertPengalamanVendorDraft(pengalamanVendorDraftTemp, tokenSession.findByToken(token));
				
				Integer pengalamanVendorDraftId = pengalamanVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(pengalamanVendorDTO.getPengalamanVendor().getId().equals(pengalamanVendor.getId()))
				{
					pengalamanVendorDraft.setId(pengalamanVendorDraftId);//set id bankvendordraft untuk melakukan update
					pengalamanVendorDraft.setCreated(pengalamanVendor.getCreated());
					
				}
				
			}
			
			
			pengalamanVendorDraft.setNamaPekerjaan(pengalamanVendorDTO.getPengalamanVendor().getNamaPekerjaan());
			pengalamanVendorDraft.setLokasiPekerjaan(pengalamanVendorDTO.getPengalamanVendor().getLokasiPekerjaan());
			pengalamanVendorDraft.setBidangUsaha(pengalamanVendorDTO.getPengalamanVendor().getBidangUsaha());
			pengalamanVendorDraft.setFileName(pengalamanVendorDTO.getPengalamanVendor().getFileName());
			pengalamanVendorDraft.setFileSize(pengalamanVendorDTO.getPengalamanVendor().getFileSize());
			pengalamanVendorDraft.setMataUang(pengalamanVendorDTO.getPengalamanVendor().getMataUang());
			pengalamanVendorDraft.setMulaiKerjasama(pengalamanVendorDTO.getPengalamanVendor().getMulaiKerjasama());
			pengalamanVendorDraft.setNilaiKontrak(pengalamanVendorDTO.getPengalamanVendor().getNilaiKontrak());
			pengalamanVendorDraft.setPathFile(pengalamanVendorDTO.getPengalamanVendor().getPathFile());
			pengalamanVendorDraft.setVendor(vendor);		
			pengalamanVendorDraft.setPengalamanVendor(pengalamanVendorDTO.getPengalamanVendor());
			pengalamanVendorDraft.setTipePengalaman(pengalamanVendorDTO.getPengalamanVendor().getTipePengalaman());
			pengalamanVendorDraft.setIsDelete(0);
			pengalamanVendorDraftSession.updatePengalamanVendorDraft(pengalamanVendorDraft, tokenSession.findByToken(token));
		}else{//update jika data yang di ambil dari data draft
		
		pengalamanVendorDraft = pengalamanVendorDTO.getPengalamanVendorDraft();
		
		
		Boolean isSaveNama = pengalamanVendorDraftSession.checkNamaPengalamaan(pengalamanVendorDraft.getNamaPekerjaan(),pengalamanVendorDraft.getTipePengalaman(), toDo, pengalamanVendorDraft.getId());

		if (!isSaveNama) {
			String errorNama = "Nama Pekerjaan  Tidak Boleh Sama";
			Boolean isValid = false;
			map.put("isValid", isValid);
			map.put("errorNama", errorNama);
		}

		if (isSaveNama) {
			pengalamanVendorDraft.setIsDelete(0);
			pengalamanVendorDraft.setVendor(vendor);
			pengalamanVendorDraftSession.updatePengalamanVendorDraft(pengalamanVendorDraft, tokenSession.findByToken(token));
		}
		map.put("pengalamanVendor", pengalamanVendorDraft);

		}
		return	map;
	}
	
	@Path("/delete")
	@POST
	public void deletePengalamanVendorDraft(@FormParam("id") int id,@FormParam("status") int status,
			@HeaderParam("Authorization") String token) {
		
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		PengalamanVendorDraft pengalamanVendorDraft= new PengalamanVendorDraft();
		
		if(status==0){// jika edit pertama kali yang datanya di ambil dari data master
			List<PengalamanVendor> pengalamanVendorList = pengalamanVendorSession.getPengalamanVendorByVendorId(vendor.getId());
			
			for(PengalamanVendor pengalamanVendor : pengalamanVendorList) {
				PengalamanVendorDraft pengalamanVendorDraftTemp = new PengalamanVendorDraft();
				pengalamanVendorDraftTemp.setNamaPekerjaan(pengalamanVendor.getNamaPekerjaan());
				pengalamanVendorDraftTemp.setLokasiPekerjaan(pengalamanVendor.getLokasiPekerjaan());
				pengalamanVendorDraftTemp.setBidangUsaha(pengalamanVendor.getBidangUsaha());
				pengalamanVendorDraftTemp.setCreated(pengalamanVendor.getCreated());
				pengalamanVendorDraftTemp.setFileName(pengalamanVendor.getFileName());
				pengalamanVendorDraftTemp.setFileSize(pengalamanVendor.getFileSize());
				pengalamanVendorDraftTemp.setMataUang(pengalamanVendor.getMataUang());
				pengalamanVendorDraftTemp.setMulaiKerjasama(pengalamanVendor.getMulaiKerjasama());
				pengalamanVendorDraftTemp.setNilaiKontrak(pengalamanVendor.getNilaiKontrak());
				pengalamanVendorDraftTemp.setPathFile(pengalamanVendor.getPathFile());
				pengalamanVendorDraftTemp.setVendor(vendor);
				pengalamanVendorDraftTemp.setUpdated(pengalamanVendor.getUpdated());
				pengalamanVendorDraftTemp.setPengalamanVendor(pengalamanVendor);
				pengalamanVendorDraftTemp.setTipePengalaman(pengalamanVendor.getTipePengalaman());
				pengalamanVendorDraftSession.insertPengalamanVendorDraft(pengalamanVendorDraftTemp, tokenSession.findByToken(token));
				
				Integer pengalamanVendorDraftId = pengalamanVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(pengalamanVendor.getId().equals(id))
				{
					pengalamanVendorDraft.setId(pengalamanVendorDraftId);//set id bankvendordraft untuk melakukan update
					
					
				}
				
			}
			
			pengalamanVendorDraftSession.deletePengalamanVendorDraft(pengalamanVendorDraft.getId(), tokenSession.findByToken(token));
		}else
		{
			pengalamanVendorDraftSession.deletePengalamanVendorDraft(id, tokenSession.findByToken(token));	
		}
		
	}

}
