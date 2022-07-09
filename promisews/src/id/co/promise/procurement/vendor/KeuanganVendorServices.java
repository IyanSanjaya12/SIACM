package id.co.promise.procurement.vendor;

import id.co.promise.procurement.DTO.KeuanganVendorDTO;
import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.KeuanganVendorDraft;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
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
@Path(value="/procurement/vendor/KeuanganVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class KeuanganVendorServices {
	@EJB private KeuanganVendorSession keuanganVendorSession;
	@EJB private KeuanganVendorDraftSession keuanganVendorDraftSession;
	@EJB private VendorSession vendorSession;
	@EJB private TokenSession tokenSession;
	
	@Path("/getKeuanganVendorByLoginId/{id}")
	@GET
	public List<KeuanganVendor> getKeuanganVendorByLoginId(@PathParam("id") int id){
		
		return keuanganVendorSession.getKeuanganVendorByVendorId(vendorSession.getVendorByUserId(id).getId());
	}
	
	@Path("/getKeuanganVendorByVendorId/{id}")
	@GET
	public List<KeuanganVendor> getKeuanganVendorByVendorId(@PathParam("id") int id){
		
		return keuanganVendorSession.getKeuanganVendorByVendorId(id);
	}
	
	
	@SuppressWarnings("rawtypes")
	@Path("/getkeuanganvendorbyvendorid")
	@GET
	public Map getkeuanganvendorbyvendorid(@HeaderParam("Authorization") String token) { 
	    Map<String, Object> map = new HashMap<String, Object>();
	    User user =tokenSession.findByToken(token).getUser();
	    Vendor vendor = vendorSession.getVendorByUserId(user.getId());
	    List<KeuanganVendorDraft> keuanganVendorDraftList = keuanganVendorDraftSession.getKeuanganVendorDraftByVendorId(vendor.getId());
	    if(keuanganVendorDraftList.size() > 0){
	      map.put("keuanganVendorList",keuanganVendorDraftList);
	      map.put("status",1);
	    }else{
	      List<KeuanganVendor> keuanganVendorList = keuanganVendorSession.getKeuanganVendorByVendorId(vendor.getId());
	      map.put("keuanganVendorList",keuanganVendorList);
	      map.put("status",0);
	    }	    
	    return map;
	}

	
	@Path("/insert")
	@POST
	public KeuanganVendorDTO insertKeuanganVendorDraft (KeuanganVendorDTO keuanganVendorDTO,@HeaderParam("Authorization") String token){
		Vendor vendor=vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		if(keuanganVendorDTO.getStatus()==0){
			List<KeuanganVendor> keuanganVendorList=keuanganVendorSession.getKeuanganVendorByVendorId(vendor.getId());
			for(KeuanganVendor keuanganVendor :keuanganVendorList){	
				KeuanganVendorDraft keuanganVendorDraftTemp = new KeuanganVendorDraft();
				keuanganVendorDraftTemp.setAktivaLainnya(keuanganVendor.getAktivaLainnya());
				keuanganVendorDraftTemp.setBank(keuanganVendor.getBank());
				keuanganVendorDraftTemp.setGedungGedung(keuanganVendor.getGedungGedung());
				keuanganVendorDraftTemp.setHutangDagang(keuanganVendor.getHutangDagang());
				keuanganVendorDraftTemp.setHutangJangkaPanjang(keuanganVendor.getHutangJangkaPanjang());
				keuanganVendorDraftTemp.setHutangLainnya(keuanganVendor.getHutangLainnya());
				keuanganVendorDraftTemp.setHutangPajak(keuanganVendor.getHutangPajak());
				keuanganVendorDraftTemp.setInventaris(keuanganVendor.getInventaris());
				keuanganVendorDraftTemp.setKas(keuanganVendor.getKas());
				keuanganVendorDraftTemp.setKekayaanBersih(keuanganVendor.getKekayaanBersih());
				keuanganVendorDraftTemp.setKeuanganVendor(keuanganVendor);
				keuanganVendorDraftTemp.setNamaAudit(keuanganVendor.getNamaAudit());
				keuanganVendorDraftTemp.setNomorAudit(keuanganVendor.getNomorAudit());
				keuanganVendorDraftTemp.setPekerjaanDalamProses(keuanganVendor.getPekerjaanDalamProses());
				keuanganVendorDraftTemp.setPeralatanDanMesin(keuanganVendor.getPeralatanDanMesin());
				keuanganVendorDraftTemp.setPersediaanBarang(keuanganVendor.getPersediaanBarang());
				keuanganVendorDraftTemp.setTahunKeuangan(keuanganVendor.getTahunKeuangan());
				keuanganVendorDraftTemp.setTanggalAudit(keuanganVendor.getTanggalAudit());
				keuanganVendorDraftTemp.setTotalAktiva(keuanganVendor.getTotalAktiva());
				keuanganVendorDraftTemp.setTotalAktivaLancar(keuanganVendor.getTotalAktivaLancar());
				keuanganVendorDraftTemp.setTotalAktivaTetap(keuanganVendor.getTotalAktivaTetap());
				keuanganVendorDraftTemp.setTotalHutangJangkaPendek(keuanganVendor.getTotalHutangJangkaPendek());
				keuanganVendorDraftTemp.setTotalPasiva(keuanganVendor.getTotalPasiva());
				keuanganVendorDraftTemp.setTotalPiutang(keuanganVendor.getTotalPiutang());
				keuanganVendorDraftTemp.setUserId(keuanganVendor.getUserId());
				keuanganVendorDraftTemp.setVendor(keuanganVendor.getVendor());
				keuanganVendorDraftSession.insertKeuanganVendorDraft(keuanganVendorDraftTemp, tokenSession.findByToken(token));	
			}
		}
		KeuanganVendorDraft keuanganVendorDraft = keuanganVendorDTO.getKeuanganVendorDraft();
		keuanganVendorDraft.setVendor(vendor);	
		keuanganVendorDraft.setId(null);
		keuanganVendorDraft.setIsDelete(0);
		keuanganVendorDraftSession.insertKeuanganVendorDraft(keuanganVendorDraft, tokenSession.findByToken(token));
		
		return keuanganVendorDTO;
	}
	
	@Path("/update")
	@POST
	public KeuanganVendorDTO updateKeuanganVendorDraft (KeuanganVendorDTO keuanganVendorDTO,@HeaderParam("Authorization") String token){
		Vendor vendor=vendorSession.getVendorByUserId(keuanganVendorDTO.getLoginId());
		KeuanganVendorDraft keuanganVendorDraft = new KeuanganVendorDraft();
		if(keuanganVendorDTO.getStatus()==0){
			List<KeuanganVendor> keuanganVendorList=keuanganVendorSession.getKeuanganVendorByVendorId(vendor.getId());
			for(KeuanganVendor keuanganVendor :keuanganVendorList){	
				KeuanganVendorDraft keuanganVendorDraftTemp = new KeuanganVendorDraft();
				keuanganVendorDraftTemp.setAktivaLainnya(keuanganVendor.getAktivaLainnya());
				keuanganVendorDraftTemp.setBank(keuanganVendor.getBank());
				keuanganVendorDraftTemp.setGedungGedung(keuanganVendor.getGedungGedung());
				keuanganVendorDraftTemp.setHutangDagang(keuanganVendor.getHutangDagang());
				keuanganVendorDraftTemp.setHutangJangkaPanjang(keuanganVendor.getHutangJangkaPanjang());
				keuanganVendorDraftTemp.setHutangLainnya(keuanganVendor.getHutangLainnya());
				keuanganVendorDraftTemp.setHutangPajak(keuanganVendor.getHutangPajak());
				keuanganVendorDraftTemp.setInventaris(keuanganVendor.getInventaris());
				keuanganVendorDraftTemp.setKas(keuanganVendor.getKas());
				keuanganVendorDraftTemp.setKekayaanBersih(keuanganVendor.getKekayaanBersih());
				keuanganVendorDraftTemp.setKeuanganVendor(keuanganVendor);
				keuanganVendorDraftTemp.setNamaAudit(keuanganVendor.getNamaAudit());
				keuanganVendorDraftTemp.setNomorAudit(keuanganVendor.getNomorAudit());
				keuanganVendorDraftTemp.setPekerjaanDalamProses(keuanganVendor.getPekerjaanDalamProses());
				keuanganVendorDraftTemp.setPeralatanDanMesin(keuanganVendor.getPeralatanDanMesin());
				keuanganVendorDraftTemp.setPersediaanBarang(keuanganVendor.getPersediaanBarang());
				keuanganVendorDraftTemp.setTahunKeuangan(keuanganVendor.getTahunKeuangan());
				keuanganVendorDraftTemp.setTanggalAudit(keuanganVendor.getTanggalAudit());
				keuanganVendorDraftTemp.setTotalAktiva(keuanganVendor.getTotalAktiva());
				keuanganVendorDraftTemp.setTotalAktivaLancar(keuanganVendor.getTotalAktivaLancar());
				keuanganVendorDraftTemp.setTotalAktivaTetap(keuanganVendor.getTotalAktivaTetap());
				keuanganVendorDraftTemp.setTotalHutangJangkaPendek(keuanganVendor.getTotalHutangJangkaPendek());
				keuanganVendorDraftTemp.setTotalPasiva(keuanganVendor.getTotalPasiva());
				keuanganVendorDraftTemp.setTotalPiutang(keuanganVendor.getTotalPiutang());
				keuanganVendorDraftTemp.setUserId(keuanganVendor.getUserId());
				keuanganVendorDraftTemp.setVendor(keuanganVendor.getVendor());
				keuanganVendorDraftSession.insertKeuanganVendorDraft(keuanganVendorDraftTemp, tokenSession.findByToken(token));	
				
				Integer keuanganVendorDraftId = keuanganVendorDraftTemp.getId();// mengambil id draft yang mempunyai keuanganvendor yang sama
				if(keuanganVendorDTO.getKeuanganVendor().getId().equals(keuanganVendor.getId()))
				{
					keuanganVendorDraft.setId(keuanganVendorDraftId);//set id keuanganvendordraft untuk melakukan update
					keuanganVendorDraft.setAktivaLainnya(keuanganVendorDTO.getKeuanganVendor().getAktivaLainnya());
					keuanganVendorDraft.setBank(keuanganVendorDTO.getKeuanganVendor().getBank());
					keuanganVendorDraft.setGedungGedung(keuanganVendorDTO.getKeuanganVendor().getGedungGedung());
					keuanganVendorDraft.setHutangDagang(keuanganVendorDTO.getKeuanganVendor().getHutangDagang());
					keuanganVendorDraft.setHutangJangkaPanjang(keuanganVendorDTO.getKeuanganVendor().getHutangJangkaPanjang());
					keuanganVendorDraft.setHutangLainnya(keuanganVendorDTO.getKeuanganVendor().getHutangLainnya());
					keuanganVendorDraft.setHutangPajak(keuanganVendorDTO.getKeuanganVendor().getHutangPajak());
					keuanganVendorDraft.setInventaris(keuanganVendorDTO.getKeuanganVendor().getInventaris());
					keuanganVendorDraft.setKas(keuanganVendorDTO.getKeuanganVendor().getKas());
					keuanganVendorDraft.setKekayaanBersih(keuanganVendorDTO.getKeuanganVendor().getKekayaanBersih());
					keuanganVendorDraft.setKeuanganVendor(keuanganVendorDTO.getKeuanganVendor());
					keuanganVendorDraft.setNamaAudit(keuanganVendorDTO.getKeuanganVendor().getNamaAudit());
					keuanganVendorDraft.setNomorAudit(keuanganVendorDTO.getKeuanganVendor().getNomorAudit());
					keuanganVendorDraft.setPekerjaanDalamProses(keuanganVendorDTO.getKeuanganVendor().getPekerjaanDalamProses());
					keuanganVendorDraft.setPeralatanDanMesin(keuanganVendorDTO.getKeuanganVendor().getPeralatanDanMesin());
					keuanganVendorDraft.setPersediaanBarang(keuanganVendorDTO.getKeuanganVendor().getPersediaanBarang());
					keuanganVendorDraft.setTahunKeuangan(keuanganVendorDTO.getKeuanganVendor().getTahunKeuangan());
					keuanganVendorDraft.setTanggalAudit(keuanganVendorDTO.getKeuanganVendor().getTanggalAudit());
					keuanganVendorDraft.setTotalAktiva(keuanganVendorDTO.getKeuanganVendor().getTotalAktiva());
					keuanganVendorDraft.setTotalAktivaLancar(keuanganVendorDTO.getKeuanganVendor().getTotalAktivaLancar());
					keuanganVendorDraft.setTotalAktivaTetap(keuanganVendorDTO.getKeuanganVendor().getTotalAktivaTetap());
					keuanganVendorDraft.setTotalHutangJangkaPendek(keuanganVendorDTO.getKeuanganVendor().getTotalHutangJangkaPendek());
					keuanganVendorDraft.setTotalPasiva(keuanganVendorDTO.getKeuanganVendor().getTotalPasiva());
					keuanganVendorDraft.setTotalPiutang(keuanganVendorDTO.getKeuanganVendor().getTotalPiutang());
					keuanganVendorDraft.setUserId(keuanganVendorDTO.getKeuanganVendor().getUserId());
					keuanganVendorDraft.setCreated(keuanganVendorDTO.getKeuanganVendor().getCreated());
					keuanganVendorDraft.setIsDelete(0);
					keuanganVendorDraft.setVendor(keuanganVendorDTO.getKeuanganVendor().getVendor());
					keuanganVendorDraftSession.updateKeuanganVendorDraft(keuanganVendorDraft, tokenSession.findByToken(token));
					
				}
			}
		} else 
		{
			keuanganVendorDraft = keuanganVendorDTO.getKeuanganVendorDraft();
			keuanganVendorDraft.setVendor(vendor);	
			keuanganVendorDraft.setIsDelete(0);
			keuanganVendorDraftSession.updateKeuanganVendorDraft(keuanganVendorDraft, tokenSession.findByToken(token));
		}
		return keuanganVendorDTO;
	}
	
	@Path("/delete")
	@POST
	public void deleteKeuanganVendorDraft(@FormParam("id") int id,@FormParam("status") int status, @HeaderParam("Authorization") String token) {
		Vendor vendor=vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		KeuanganVendorDraft keuanganVendorDraft = new KeuanganVendorDraft();
		if(status==0){
			List<KeuanganVendor> keuanganVendorList=keuanganVendorSession.getKeuanganVendorByVendorId(vendor.getId());
			for(KeuanganVendor keuanganVendor :keuanganVendorList){	
				KeuanganVendorDraft keuanganVendorDraftTemp = new KeuanganVendorDraft();
				keuanganVendorDraftTemp.setAktivaLainnya(keuanganVendor.getAktivaLainnya());
				keuanganVendorDraftTemp.setBank(keuanganVendor.getBank());
				keuanganVendorDraftTemp.setGedungGedung(keuanganVendor.getGedungGedung());
				keuanganVendorDraftTemp.setHutangDagang(keuanganVendor.getHutangDagang());
				keuanganVendorDraftTemp.setHutangJangkaPanjang(keuanganVendor.getHutangJangkaPanjang());
				keuanganVendorDraftTemp.setHutangLainnya(keuanganVendor.getHutangLainnya());
				keuanganVendorDraftTemp.setHutangPajak(keuanganVendor.getHutangPajak());
				keuanganVendorDraftTemp.setInventaris(keuanganVendor.getInventaris());
				keuanganVendorDraftTemp.setKas(keuanganVendor.getKas());
				keuanganVendorDraftTemp.setKekayaanBersih(keuanganVendor.getKekayaanBersih());
				keuanganVendorDraftTemp.setKeuanganVendor(keuanganVendor);
				keuanganVendorDraftTemp.setNamaAudit(keuanganVendor.getNamaAudit());
				keuanganVendorDraftTemp.setNomorAudit(keuanganVendor.getNomorAudit());
				keuanganVendorDraftTemp.setPekerjaanDalamProses(keuanganVendor.getPekerjaanDalamProses());
				keuanganVendorDraftTemp.setPeralatanDanMesin(keuanganVendor.getPeralatanDanMesin());
				keuanganVendorDraftTemp.setPersediaanBarang(keuanganVendor.getPersediaanBarang());
				keuanganVendorDraftTemp.setTahunKeuangan(keuanganVendor.getTahunKeuangan());
				keuanganVendorDraftTemp.setTanggalAudit(keuanganVendor.getTanggalAudit());
				keuanganVendorDraftTemp.setTotalAktiva(keuanganVendor.getTotalAktiva());
				keuanganVendorDraftTemp.setTotalAktivaLancar(keuanganVendor.getTotalAktivaLancar());
				keuanganVendorDraftTemp.setTotalAktivaTetap(keuanganVendor.getTotalAktivaTetap());
				keuanganVendorDraftTemp.setTotalHutangJangkaPendek(keuanganVendor.getTotalHutangJangkaPendek());
				keuanganVendorDraftTemp.setTotalPasiva(keuanganVendor.getTotalPasiva());
				keuanganVendorDraftTemp.setTotalPiutang(keuanganVendor.getTotalPiutang());
				keuanganVendorDraftTemp.setUserId(keuanganVendor.getUserId());
				keuanganVendorDraftTemp.setVendor(keuanganVendor.getVendor());
				keuanganVendorDraftSession.insertKeuanganVendorDraft(keuanganVendorDraftTemp, tokenSession.findByToken(token));	
				
				Integer keuanganVendorDraftId = keuanganVendorDraftTemp.getId();// mengambil id draft yang mempunyai keuanganvendor yang sama
				if(keuanganVendor.getId().equals(keuanganVendor.getId()))
				{
					keuanganVendorDraft.setId(keuanganVendorDraftId);//set id keuanganvendordraft untuk melakukan update
				}
			}
			keuanganVendorDraftSession.deleteKeuanganVendorDraft(keuanganVendorDraft.getId(), tokenSession.findByToken(token));
		} else 
		{
			keuanganVendorDraftSession.deleteKeuanganVendorDraft(id, tokenSession.findByToken(token));
		}
	}
}
