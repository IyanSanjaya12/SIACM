package id.co.promise.procurement.vendor;

import id.co.promise.procurement.DTO.PeralatanVendorDTO;
import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.PeralatanVendorDraft;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.BuktiKepemilikanSession;
import id.co.promise.procurement.master.KondisiPeralatanVendorSession;
import id.co.promise.procurement.security.TokenSession;

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
@Path(value="/procurement/vendor/PeralatanVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class PeralatanVendorServices {
	@EJB private PeralatanVendorSession peralatanVendorSession;
	@EJB private PeralatanVendorDraftSession peralatanVendorDraftSession;
	@EJB private VendorSession vendorSession;
	@EJB private KondisiPeralatanVendorSession kondisiSession;
	@EJB private BuktiKepemilikanSession buktiKepemilikanSession;
	@EJB private TokenSession tokenSession;
	
	@Path("/getPeralatanVendorByVendorId/{vendorId}")
	@GET
	public List<PeralatanVendor> getPeralatanVendorByVendorId(@PathParam("vendorId") int vendorId){
		return peralatanVendorSession.getPeralatanVendorByVendorId(vendorId);
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getperalatanvendorbyvendorid")
	@GET
	  public Map getperalatanvendorbyvendorid(
	      @HeaderParam("Authorization") String token) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    User user =tokenSession.findByToken(token).getUser();
	    Vendor vendor = vendorSession.getVendorByUserId(user.getId());
	    List<PeralatanVendorDraft> peralatanVendorDraftList = peralatanVendorDraftSession.getPeralatanVendorDraftByVendorId(vendor.getId());
	    if(peralatanVendorDraftList.size() > 0){
	      map.put("peralatanVendorList",peralatanVendorDraftList);
	      map.put("status",1);
	    }else{
	      List<PeralatanVendor> peralatanVendorList = peralatanVendorSession.getPeralatanVendorByVendorId(vendor.getId());
	      map.put("peralatanVendorList",peralatanVendorList);
	      map.put("status",0);
	    }	    
	    return map;
	}
	
	@Path("/insert")
	@POST
	public PeralatanVendorDTO insertPeralatanVendorDraft (PeralatanVendorDTO peralatanVendorDTO,@HeaderParam("Authorization") String token){
		
		Vendor vendor=vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		if(peralatanVendorDTO.getStatus()==0){
			List<PeralatanVendor> peralatanVendorList=peralatanVendorSession.getPeralatanVendorByVendorId(vendor.getId());
			for(PeralatanVendor peralatanVendor :peralatanVendorList){
				PeralatanVendorDraft peralatanVendorDraftTemp= new PeralatanVendorDraft();
				peralatanVendorDraftTemp.setBuktiKepemilikan(peralatanVendor.getBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileNameBuktiKepemilikan(peralatanVendor.getFileNameBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileNameGambarPeralatan(peralatanVendor.getFileNameGambarPeralatan());
				peralatanVendorDraftTemp.setFileSizeBuktiKepemilikan(peralatanVendor.getFileSizeBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileSizeGambarPeralatan(peralatanVendor.getFileSizeGambarPeralatan());
				peralatanVendorDraftTemp.setJenis(peralatanVendor.getJenis());
				peralatanVendorDraftTemp.setJumlah(peralatanVendor.getJumlah());
				peralatanVendorDraftTemp.setKapasitas(peralatanVendor.getKapasitas());
				peralatanVendorDraftTemp.setKondisi(peralatanVendor.getKondisi());
				peralatanVendorDraftTemp.setLokasi(peralatanVendor.getLokasi());
				peralatanVendorDraftTemp.setMerk(peralatanVendor.getLokasi());
				peralatanVendorDraftTemp.setPathFileBuktiKepemilikan(peralatanVendor.getPathFileBuktiKepemilikan());
				peralatanVendorDraftTemp.setPathFileGambarPeralatan(peralatanVendor.getPathFileGambarPeralatan());
				peralatanVendorDraftTemp.setTahunPembuatan(peralatanVendor.getTahunPembuatan());
				peralatanVendorDraftTemp.setUserId(peralatanVendor.getUserId());
				peralatanVendorDraftTemp.setVendor(peralatanVendor.getVendor());
				peralatanVendorDraftTemp.setPeralatanVendor(peralatanVendor);
				peralatanVendorDraftSession.insertPeralatanVendorDraft(peralatanVendorDraftTemp,tokenSession.findByToken(token));
			}
		}
		peralatanVendorDTO.getPeralatanVendorDraft().setId(null);
		peralatanVendorDTO.getPeralatanVendorDraft().setIsDelete(0);
		peralatanVendorDTO.getPeralatanVendorDraft().setVendor(vendor);
		peralatanVendorDraftSession.insertPeralatanVendorDraft(peralatanVendorDTO.getPeralatanVendorDraft(), tokenSession.findByToken(token));
		
		return peralatanVendorDTO;
		
	}
	
	@Path("/update")
	@POST
	public PeralatanVendorDTO updatePeralatanVendorDraft (PeralatanVendorDTO peralatanVendorDTO,@HeaderParam("Authorization") String token){
		
		Vendor vendor=vendorSession.getVendorByUserId(peralatanVendorDTO.getLoginId());
		PeralatanVendorDraft peralatanVendorDraft= new PeralatanVendorDraft();
		
		if(peralatanVendorDTO.getStatus()==0){
			List<PeralatanVendor> peralatanVendorList=peralatanVendorSession.getPeralatanVendorByVendorId(vendor.getId());
			for(PeralatanVendor peralatanVendor :peralatanVendorList){
				PeralatanVendorDraft peralatanVendorDraftTemp= new PeralatanVendorDraft();
				peralatanVendorDraftTemp.setBuktiKepemilikan(peralatanVendor.getBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileNameBuktiKepemilikan(peralatanVendor.getFileNameBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileNameGambarPeralatan(peralatanVendor.getFileNameGambarPeralatan());
				peralatanVendorDraftTemp.setFileSizeBuktiKepemilikan(peralatanVendor.getFileSizeBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileSizeGambarPeralatan(peralatanVendor.getFileSizeGambarPeralatan());
				peralatanVendorDraftTemp.setJenis(peralatanVendor.getJenis());
				peralatanVendorDraftTemp.setJumlah(peralatanVendor.getJumlah());
				peralatanVendorDraftTemp.setKapasitas(peralatanVendor.getKapasitas());
				peralatanVendorDraftTemp.setKondisi(peralatanVendor.getKondisi());
				peralatanVendorDraftTemp.setLokasi(peralatanVendor.getLokasi());
				peralatanVendorDraftTemp.setMerk(peralatanVendor.getLokasi());
				peralatanVendorDraftTemp.setPathFileBuktiKepemilikan(peralatanVendor.getPathFileBuktiKepemilikan());
				peralatanVendorDraftTemp.setPathFileGambarPeralatan(peralatanVendor.getPathFileGambarPeralatan());
				peralatanVendorDraftTemp.setTahunPembuatan(peralatanVendor.getTahunPembuatan());
				peralatanVendorDraftTemp.setUserId(peralatanVendor.getUserId());
				peralatanVendorDraftTemp.setVendor(peralatanVendor.getVendor());
				peralatanVendorDraftTemp.setPeralatanVendor(peralatanVendor);
				peralatanVendorDraftSession.insertPeralatanVendorDraft(peralatanVendorDraftTemp,tokenSession.findByToken(token));
				
				Integer peralatanVendorDraftId = peralatanVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(peralatanVendorDTO.getPeralatanVendor().getId().equals(peralatanVendor.getId()))
				{
					peralatanVendorDraft.setId(peralatanVendorDraftId);//set id bankvendordraft untuk melakukan update
				}
			}
		}else //update jika data yang di ambil dari data draft
		{
			peralatanVendorDraft = peralatanVendorDTO.getPeralatanVendorDraft();
			peralatanVendorDraft.setVendor(vendor);
			peralatanVendorDraft.setIsDelete(0);
			peralatanVendorDraftSession.updatePeralatanVendorDraft(peralatanVendorDraft, tokenSession.findByToken(token));
		}
		return peralatanVendorDTO;
		
	}
	
	@Path("/delete")
	@POST
	public void deletePeralatanVendorDraft(@FormParam("id") int id,@FormParam("status") int status,
			@HeaderParam("Authorization") String token) {
		
		Vendor vendor=vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		PeralatanVendorDraft peralatanVendorDraft = new PeralatanVendorDraft();
		if (status==0){
			List<PeralatanVendor> peralatanVendorList=peralatanVendorSession.getPeralatanVendorByVendorId(vendor.getId());
			for(PeralatanVendor peralatanVendor :peralatanVendorList){
				PeralatanVendorDraft peralatanVendorDraftTemp= new PeralatanVendorDraft();
				peralatanVendorDraftTemp.setBuktiKepemilikan(peralatanVendor.getBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileNameBuktiKepemilikan(peralatanVendor.getFileNameBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileNameGambarPeralatan(peralatanVendor.getFileNameGambarPeralatan());
				peralatanVendorDraftTemp.setFileSizeBuktiKepemilikan(peralatanVendor.getFileSizeBuktiKepemilikan());
				peralatanVendorDraftTemp.setFileSizeGambarPeralatan(peralatanVendor.getFileSizeGambarPeralatan());
				peralatanVendorDraftTemp.setJenis(peralatanVendor.getJenis());
				peralatanVendorDraftTemp.setJumlah(peralatanVendor.getJumlah());
				peralatanVendorDraftTemp.setKapasitas(peralatanVendor.getKapasitas());
				peralatanVendorDraftTemp.setKondisi(peralatanVendor.getKondisi());
				peralatanVendorDraftTemp.setLokasi(peralatanVendor.getLokasi());
				peralatanVendorDraftTemp.setMerk(peralatanVendor.getLokasi());
				peralatanVendorDraftTemp.setPathFileBuktiKepemilikan(peralatanVendor.getPathFileBuktiKepemilikan());
				peralatanVendorDraftTemp.setPathFileGambarPeralatan(peralatanVendor.getPathFileGambarPeralatan());
				peralatanVendorDraftTemp.setTahunPembuatan(peralatanVendor.getTahunPembuatan());
				peralatanVendorDraftTemp.setUserId(peralatanVendor.getUserId());
				peralatanVendorDraftTemp.setVendor(peralatanVendor.getVendor());
				peralatanVendorDraftTemp.setPeralatanVendor(peralatanVendor);
				peralatanVendorDraftSession.insertPeralatanVendorDraft(peralatanVendorDraftTemp,tokenSession.findByToken(token));
				
				Integer bankVendorDraftId = peralatanVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(peralatanVendor.getId().equals(id))
				{
					peralatanVendorDraft.setId(bankVendorDraftId);//set id bankvendordraft untuk melakukan update
				}
			}
			peralatanVendorDraftSession.deletePeralatanVendorDraft(peralatanVendorDraft.getId(), tokenSession.findByToken(token));
		}else{
			peralatanVendorDraftSession.deletePeralatanVendorDraft(id, tokenSession.findByToken(token));
		}
	}

	@Path("/deleteRowPeralatanVendor/{id}")
	@GET
	public void deleteRowPeralatanVendor(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		peralatanVendorSession.deleteRowPeralatanVendor(id, tokenSession.findByToken(token));
	}
	
	/*@Path("/delete")
	@POST
	public PeralatanVendor deletePeralatanVendor(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return peralatanVendorSession.deletePeralatanVendor(id, tokenSession.findByToken(token));
	}*/

}
