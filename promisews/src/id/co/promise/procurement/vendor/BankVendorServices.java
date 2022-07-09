package id.co.promise.procurement.vendor;

import id.co.promise.procurement.DTO.BankVendorDTO;
import id.co.promise.procurement.approval.ApprovalProcessServices;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.ApprovalProcessType;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.Bank;
import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BankVendorDraft;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Tahapan;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

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

@Stateless
@Path(value="/procurement/vendor/BankVendorServices")
@Produces(MediaType.APPLICATION_JSON)
public class BankVendorServices {
	@EJB private BankVendorSession bankVendorSession;
	@EJB private VendorSession vendorSession;
	@EJB private MataUangSession mataUangSession;
	@EJB private TokenSession tokenSession;
	@EJB private BankVendorDraftSession bankVendorDraftSession;
	
	
	final static Logger log = Logger.getLogger(BankVendorServices.class);
	
	@Path("/getBankVendorByVendorId/{vendorId}")
	@GET
	public List<BankVendor> getBankVendorByVendorId(@PathParam("vendorId") int vendorId){
		return bankVendorSession.getBankVendorByVendorId(vendorId);
	}
	
	
	
	@Path("/getBankVendorByVendorId/{vendorId}/{mataUangId}")
	@GET
	public BankVendor getBankVendorByVendorId(@PathParam("vendorId") int vendorId,
			@PathParam("mataUangId") int mataUangId){
		MataUang mataUang = mataUangSession.find(mataUangId);
		Vendor vendor = vendorSession.find(vendorId);
		
		return bankVendorSession.getBankVendorBySequence(mataUang, vendor);
	}
	
	/*@Path("/createBankVendor")
	@POST
	public Response createBankVendor (BankVendorDTO bankVendorDTO,
			@HeaderParam("Authorization") String token
			) throws ParseException{
		
		
		Vendor vendor = vendorSession.find(bankVendorDTO.getVendorId());
		
		List<BankVendor> bvList = bankVendorSession.getBankVendorByVendorId(vendor.getId());
		
		for(BankVendor bv : bvList) {
			bankVendorSession.deleteRowBankVendor(bv.getId(), tokenSession.findByToken(token));
		}
		
		List<BankVendor> bankVendorList = new ArrayList<BankVendor>();

		for(BankVendor bankVendor : bankVendorDTO.getBankVendor()) {
			
			BankVendor bankVendorNew = new BankVendor();
			
			bankVendorNew.setVendor(vendor);
			
			if (bankVendor.getNamaBank() != null && bankVendor.getAlamatBank().length() > 0) {
				bankVendorNew.setNamaBank(bankVendor.getNamaBank());
			}
			
			if (bankVendor.getCabangBank() != null && bankVendor.getCabangBank().length() > 0) {
				bankVendorNew.setCabangBank(bankVendor.getCabangBank());
			}
			
			if (bankVendor.getAlamatBank() != null && bankVendor.getAlamatBank().length() > 0) {
				bankVendorNew.setAlamatBank(bankVendor.getAlamatBank());
			}
			
			if (bankVendor.getKota() != null && bankVendor.getKota().length() > 0) {
				bankVendorNew.setKota(bankVendor.getKota());
			}
			
			if (bankVendor.getNegara() != null && bankVendor.getNegara().length() > 0) {
				bankVendorNew.setNegara(bankVendor.getNegara());
			}
			
			if (bankVendor.getNomorRekening() != null && bankVendor.getNomorRekening().length() > 0) {
				bankVendorNew.setNomorRekening(bankVendor.getNomorRekening());
			}
			
			if (bankVendor.getNamaNasabah() != null && bankVendor.getNamaNasabah().length() > 0) {
				bankVendorNew.setNamaNasabah(bankVendor.getNamaNasabah());
			}
			
			if (bankVendor.getMataUang() != null) {
				bankVendorNew.setMataUang(bankVendor.getMataUang());
			}
			
			Integer bankVendorBnkt = bankVendorSession.getBankVendorByBnktAndSequence(bankVendor.getMataUang(), vendor);
			
			if (bankVendorBnkt == 0) {
				String sequence = "1";
				bankVendorNew.setBnkt(bankVendor.getMataUang().getKode() + sequence);
				bankVendorNew.setBnktSequence(1);
			} else {
				bankVendorBnkt++;
				String sequence = bankVendorBnkt.toString();
				bankVendorNew.setBnkt(bankVendor.getMataUang().getKode() + sequence);
				bankVendorNew.setBnktSequence(bankVendorBnkt);
			}
			
			bankVendorNew.setIsDelete(0);
			
			bankVendorSession.insertBankVendor(bankVendorNew, tokenSession.findByToken(token));
			
			bankVendorList.add(bankVendorNew);
			
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bankVendor", bankVendorList);	
		
		return Response.ok(result).build();
	}*/
	
	@Path("/editBankVendor")
	@POST
	public BankVendor editBankVendor (
			@FormParam("id") int bankVendorId,
			@FormParam("vendor") int vendorId, 
			@FormParam("namaBank") String namaBank, 
			@FormParam("cabangBank") String cabangBank, 
			@FormParam("alamatBank") String alamatBank, 
			@FormParam("kota") String kota, 
			@FormParam("negara") String negara, 
			@FormParam("nomorRekening") String nomorRekening, 
			@FormParam("namaNasabah") String namaNasabah, 
			@FormParam("mataUang") int mataUangId,
			@HeaderParam("Authorization") String token
			){
		BankVendor bankVendor = bankVendorSession.find(bankVendorId);
		
		if (vendorId > 0) {
			bankVendor.setVendor(vendorSession.find(vendorId));
		}
		
		if (namaBank != null && namaBank.length() > 0) {
			bankVendor.setNamaBank(namaBank);
		}
		
		if (cabangBank != null && cabangBank.length() > 0) {
			bankVendor.setCabangBank(cabangBank);
		}
		
		if (alamatBank != null && alamatBank.length() > 0) {
			bankVendor.setAlamatBank(alamatBank);
		}
		
		if (kota != null && kota.length() > 0) {
			bankVendor.setKota(kota);
		}
		
		if (negara != null && negara.length() > 0) {
			bankVendor.setNegara(negara);
		}
		
		if (nomorRekening != null && nomorRekening.length() > 0) {
			bankVendor.setNomorRekening(nomorRekening);
		}
		
		if (namaNasabah != null && namaNasabah.length() > 0) {
			bankVendor.setNamaNasabah(namaNasabah);
		}
		
		MataUang mataUang = mataUangSession.find(mataUangId);
		Vendor vendor = vendorSession.find(vendorId);
		BankVendor bankVendorSequence = bankVendorSession.getBankVendorBySequence(mataUang, vendor);
		
		if (mataUangId > 0) {
			bankVendor.setMataUang(mataUang);
		}
		
		String bnkt = bankVendor.getBnkt().substring(0, bankVendor.getBnkt().length() - 1);
		
		if(mataUang.getKode().equalsIgnoreCase(bnkt)) {
			bankVendor.setBnkt(bankVendor.getBnkt());
			bankVendor.setBnktSequence(bankVendor.getBnktSequence());
		
		} else {
			if (bankVendorSequence == null) {
				String sequence = "1";
				bankVendor.setBnkt(mataUang.getKode()+sequence);
				bankVendor.setBnktSequence(1);
			} else {
				//DOLLAR 1
				
				Integer sequence = bankVendorSequence.getBnktSequence();
				sequence++;
				String sequenceString = sequence.toString();
				bankVendor.setBnkt(mataUang.getKode()+sequenceString);
				bankVendor.setBnktSequence(sequence);
			}
			
		}
		
		bankVendorSession.updateBankVendor(bankVendor, tokenSession.findByToken(token));
		
		return bankVendor;
	}

	@Path("/deleteRowBankVendor/{id}")
	@GET
	public void deleteRowBankVendor(
			@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		bankVendorSession.deleteRowBankVendor(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteBankVendor/{id}")
	@GET
	public void deleteBankVendor(
			@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		bankVendorSession.deleteBankVendor(id, tokenSession.findByToken(token));
	}
	
	@Path("/getbankvendorbybnkt/{mataUang}/{vendor}")
	@GET
	public long getbankvendorbybnkt(
			@PathParam("mataUang") Integer mataUangId,
			@PathParam("vendor") Integer vendorId) {
		MataUang mataUang = mataUangSession.find(mataUangId);
		Vendor vendor = vendorSession.find(vendorId);
		
		return bankVendorSession.getBankVendorByBnktAndSequence(mataUang, vendor);
	}
	
	@SuppressWarnings("rawtypes")
	@Path("/getbankvendorbyvendorid")
	@GET
	public Map getbankvendorbyvendorid(
			@HeaderParam("Authorization") String token) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user =tokenSession.findByToken(token).getUser();
		
		Vendor vendor = vendorSession.getVendorByUserId(user.getId());
		
		List<BankVendorDraft> bankVendorDraftList = bankVendorDraftSession.getBankVendorDraftByVendorId(vendor.getId());
		if(bankVendorDraftList.size() > 0){
			map.put("bankVendorList",bankVendorDraftList);
			map.put("status",1);
		}else{
			List<BankVendor> bankVendorList = bankVendorSession.getBankVendorByVendorId(vendor.getId());
			map.put("bankVendorList",bankVendorList);
			map.put("status",0);
		}
		
		
		return map;
	}
	
	@Path("/insert")
	@POST
	public BankVendorDTO insertBankVendorDraft (BankVendorDTO bankVendorDTO,
			@HeaderParam("Authorization") String token
			) throws ParseException{
		
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		
		if(bankVendorDTO.getStatus()==0){
			List<BankVendor> bankVendorList = bankVendorSession.getBankVendorByVendorId(vendor.getId());
			
			for(BankVendor bankVendor : bankVendorList) {
				BankVendorDraft bankVendorDraftTemp = new BankVendorDraft();
				bankVendorDraftTemp.setAlamatBank(bankVendor.getAlamatBank());
				bankVendorDraftTemp.setBankVendor(bankVendor);
				bankVendorDraftTemp.setBnkt(bankVendor.getBnkt());
				bankVendorDraftTemp.setBnktSequence(bankVendor.getBnktSequence());
				bankVendorDraftTemp.setCabangBank(bankVendor.getCabangBank());
				bankVendorDraftTemp.setCreated(bankVendor.getCreated());
				bankVendorDraftTemp.setKota(bankVendor.getKota());
				bankVendorDraftTemp.setMataUang(bankVendor.getMataUang());
				bankVendorDraftTemp.setNamaBank(bankVendor.getNamaBank());
				bankVendorDraftTemp.setNamaNasabah(bankVendor.getNamaNasabah());
				bankVendorDraftTemp.setNegara(bankVendor.getNegara());
				bankVendorDraftTemp.setNomorRekening(bankVendor.getNomorRekening());
				bankVendorDraftTemp.setVendor(bankVendor.getVendor());
				bankVendorDraftSession.insertBankVendorDraft(bankVendorDraftTemp, tokenSession.findByToken(token));
				
			}
		}
		
		
		bankVendorDTO.getBankVendorDraft().setVendor(vendor);
		Integer bankVendorBnkt = bankVendorSession.getBankVendorByBnktAndSequence(bankVendorDTO.getBankVendorDraft().getMataUang(), vendor);
		
		if (bankVendorBnkt == 0) {
			String sequence = "1";
			bankVendorDTO.getBankVendorDraft().setBnkt(bankVendorDTO.getBankVendorDraft().getMataUang().getKode() + sequence);
			bankVendorDTO.getBankVendorDraft().setBnktSequence(1);
		} else {
			bankVendorBnkt++;
			String sequence = bankVendorBnkt.toString();
			bankVendorDTO.getBankVendorDraft().setBnkt(bankVendorDTO.getBankVendorDraft().getMataUang().getKode() + sequence);
			bankVendorDTO.getBankVendorDraft().setBnktSequence(bankVendorBnkt);
		}
		bankVendorDTO.getBankVendorDraft().setId(null);
		bankVendorDTO.getBankVendorDraft().setIsDelete(0);
		bankVendorDraftSession.insertBankVendorDraft(bankVendorDTO.getBankVendorDraft(), tokenSession.findByToken(token));
		
		return	bankVendorDTO;
		
	}
	
	@Path("/update")
	@POST
	public BankVendorDTO updateBankVendorDraft (BankVendorDTO bankVendorDTO,
			@HeaderParam("Authorization") String token
			) throws ParseException{
		
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		BankVendorDraft bankVendorDraft= new BankVendorDraft();
		
		if(bankVendorDTO.getStatus()==0){// jika edit pertama kali yang datanya di ambil dari data master
			List<BankVendor> bankVendorList = bankVendorSession.getBankVendorByVendorId(vendor.getId());
			
			for(BankVendor bankVendor : bankVendorList) {//duplicat data master dan memasukan ke table draft
				BankVendorDraft bankVendorDraftTemp = new BankVendorDraft();
				bankVendorDraftTemp.setAlamatBank(bankVendor.getAlamatBank());
				bankVendorDraftTemp.setBankVendor(bankVendor);
				bankVendorDraftTemp.setBnkt(bankVendor.getBnkt());
				bankVendorDraftTemp.setBnktSequence(bankVendor.getBnktSequence());
				bankVendorDraftTemp.setCabangBank(bankVendor.getCabangBank());
				bankVendorDraftTemp.setCreated(bankVendor.getCreated());
				bankVendorDraftTemp.setKota(bankVendor.getKota());
				bankVendorDraftTemp.setMataUang(bankVendor.getMataUang());
				bankVendorDraftTemp.setNamaBank(bankVendor.getNamaBank());
				bankVendorDraftTemp.setNamaNasabah(bankVendor.getNamaNasabah());
				bankVendorDraftTemp.setNegara(bankVendor.getNegara());
				bankVendorDraftTemp.setNomorRekening(bankVendor.getNomorRekening());
				bankVendorDraftTemp.setVendor(bankVendor.getVendor());
				bankVendorDraftSession.insertBankVendorDraft(bankVendorDraftTemp, tokenSession.findByToken(token)).getId();
				Integer bankVendorDraftId = bankVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(bankVendorDTO.getBankVendor().getId().equals(bankVendor.getId()))
				{
					bankVendorDraft.setId(bankVendorDraftId);//set id bankvendordraft untuk melakukan update
					bankVendorDraft.setCreated(bankVendor.getCreated());
				}
			}
			
			bankVendorDraft.setVendor(vendor);
			bankVendorDraft.setAlamatBank(bankVendorDTO.getBankVendor().getAlamatBank());
			bankVendorDraft.setBankVendor(bankVendorDTO.getBankVendor());
			bankVendorDraft.setCabangBank(bankVendorDTO.getBankVendor().getCabangBank());		
			bankVendorDraft.setKota(bankVendorDTO.getBankVendor().getKota());
			bankVendorDraft.setMataUang(bankVendorDTO.getBankVendor().getMataUang());
			bankVendorDraft.setNamaBank(bankVendorDTO.getBankVendor().getNamaBank());
			bankVendorDraft.setNamaNasabah(bankVendorDTO.getBankVendor().getNamaNasabah());
			bankVendorDraft.setNegara(bankVendorDTO.getBankVendor().getNegara());
			bankVendorDraft.setNomorRekening(bankVendorDTO.getBankVendor().getNomorRekening());
			Integer bankVendorBnkt = bankVendorSession.getBankVendorByBnktAndSequence(bankVendorDraft.getMataUang(), vendor);
			
			if (bankVendorBnkt == 0) {
				String sequence = "1";
				bankVendorDraft.setBnkt(bankVendorDraft.getMataUang().getKode() + sequence);
				bankVendorDraft.setBnktSequence(1);
			} else {
				bankVendorBnkt++;
				String sequence = bankVendorBnkt.toString();
				bankVendorDraft.setBnkt(bankVendorDraft.getMataUang().getKode() + sequence);
				bankVendorDraft.setBnktSequence(bankVendorBnkt);
			}
			bankVendorDraft.setIsDelete(0);
			bankVendorDraftSession.updateBankVendorDraft(bankVendorDraft, tokenSession.findByToken(token));
		}else{//update jika data yang di ambil dari data draft
		
		bankVendorDraft = bankVendorDTO.getBankVendorDraft();
		bankVendorDraft.setVendor(vendor);
		Integer bankVendorBnkt = bankVendorSession.getBankVendorByBnktAndSequence(bankVendorDraft.getMataUang(), vendor);
		
		if (bankVendorBnkt == 0) {
			String sequence = "1";
			bankVendorDraft.setBnkt(bankVendorDraft.getMataUang().getKode() + sequence);
			bankVendorDraft.setBnktSequence(1);
		} else {
			bankVendorBnkt++;
			String sequence = bankVendorBnkt.toString();
			bankVendorDraft.setBnkt(bankVendorDraft.getMataUang().getKode() + sequence);
			bankVendorDraft.setBnktSequence(bankVendorBnkt);
		}
		bankVendorDraft.setIsDelete(0);
		bankVendorDraftSession.updateBankVendorDraft(bankVendorDraft, tokenSession.findByToken(token));
		
		
		}
		return	bankVendorDTO;
	}
	
	@Path("/delete")
	@POST
	public void deleteBankVendorDraft(@FormParam("id") int id,@FormParam("status") int status,
			@HeaderParam("Authorization") String token) {
		
		Vendor vendor= vendorSession.getVendorByUserId(tokenSession.findByToken(token).getUser().getId());
		BankVendorDraft bankVendorDraft= new BankVendorDraft();
		
		if(status==0){// jika edit pertama kali yang datanya di ambil dari data master
			List<BankVendor> bankVendorList = bankVendorSession.getBankVendorByVendorId(vendor.getId());
			
			for(BankVendor bankVendor : bankVendorList) {//duplicat data master dan memasukan ke table draft
				BankVendorDraft bankVendorDraftTemp = new BankVendorDraft();
				bankVendorDraftTemp.setAlamatBank(bankVendor.getAlamatBank());
				bankVendorDraftTemp.setBankVendor(bankVendor);
				bankVendorDraftTemp.setBnkt(bankVendor.getBnkt());
				bankVendorDraftTemp.setBnktSequence(bankVendor.getBnktSequence());
				bankVendorDraftTemp.setCabangBank(bankVendor.getCabangBank());
				bankVendorDraftTemp.setCreated(bankVendor.getCreated());
				bankVendorDraftTemp.setKota(bankVendor.getKota());
				bankVendorDraftTemp.setMataUang(bankVendor.getMataUang());
				bankVendorDraftTemp.setNamaBank(bankVendor.getNamaBank());
				bankVendorDraftTemp.setNamaNasabah(bankVendor.getNamaNasabah());
				bankVendorDraftTemp.setNegara(bankVendor.getNegara());
				bankVendorDraftTemp.setNomorRekening(bankVendor.getNomorRekening());
				bankVendorDraftTemp.setVendor(bankVendor.getVendor());
				bankVendorDraftSession.insertBankVendorDraft(bankVendorDraftTemp, tokenSession.findByToken(token)).getId();
				Integer bankVendorDraftId = bankVendorDraftTemp.getId();// mengambil id draft yang mempunyai bankvendor yang sama
				if(bankVendor.getId().equals(id))
				{
					bankVendorDraft.setId(bankVendorDraftId);//set id bankvendordraft untuk melakukan update
				}
				
			}
			
			bankVendorDraftSession.deleteBankVendorDraft(bankVendorDraft.getId(), tokenSession.findByToken(token));
		}else
		{
			bankVendorDraftSession.deleteBankVendorDraft(id, tokenSession.findByToken(token));	
		}
		
	}
	
	
	
	
}
