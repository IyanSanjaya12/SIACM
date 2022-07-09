package id.co.promise.procurement.master;


import id.co.promise.procurement.entity.Bank;
import id.co.promise.procurement.entity.Wilayah;
import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.Token;
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
import javax.ws.rs.core.Response;

@Stateless
@Path(value = "/procurement/master/bank")
@Produces(MediaType.APPLICATION_JSON)
public class BankService {

	@EJB
	BankSession bankSession;
	@EJB
	WilayahSession wilayahSession;
	@EJB
	TokenSession tokenSession;
	
	@Path("/get-by-id/{id}")
	@GET
	public Bank getBank(@PathParam("id") int id) {
		return bankSession.getBank(id);
	}

	@Path("/get-list")
	@GET
	public List<Bank> getBankList() {
		return bankSession.getBankList();
	}
	
	@Path("/get-propinsi-list")
	@GET
	public List<Wilayah> getPropinsiList() {
		return wilayahSession.getPropinsiList();
	}
	
	@Path("/get-kota-list/{propinsiId}")
	@GET
	public List<Wilayah> getKotaList(
			@PathParam("propinsiId") Integer propinsiId) {
		return wilayahSession.getKotaList(propinsiId);
	}
	
	/* @Path("/getBankByNamaBank")
	@POST
	public Map getBankByNamaBank(@FormParam("namaBank") String namaBank,
			@FormParam("namaKantor") String namaKantor,
			@HeaderParam("Authorization") String token) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		Bank bank = bankSession.getBankByNamaBank(namaBank, namaKantor);
		String status = "";
		if (bank != null) {
			status = "exist";
			map.put("bank", bank);
			map.put("bankId", bank.getId());
			map.put("status", status);
		}
		
		return map;
	}*/
	
	@Path("/get-bank-by-pagination")
	@POST 
	public Response getBankByPagination(
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder
			//@HeaderParam("Authorization") String token
			){
		//Token objToken = tokenSession.findByToken(token);
		//Integer userId = objToken.getUser().getId();
		
		String tempKeyword = "%" + keyword + "%";
		long countData = bankSession.getBankListCount(tempKeyword);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);		
		result.put("data", bankSession.getBankListWithPagination(start, length, tempKeyword, columnOrder, tipeOrder));
		
		return Response.ok(result).build();
	}
	
	@Path("/get-bank-by-pagination-with-token")
	@POST 
	public Response getBankByPaginationWithToken(
			@FormParam("search[value]") String keyword,
			@FormParam("start") Integer start,
			@FormParam("length") Integer length,
			@FormParam("draw") Integer draw,
			@FormParam("order[0][column]") Integer columnOrder,
			@FormParam("order[0][dir]") String tipeOrder,
			@HeaderParam("Authorization") String token
			){
		Token objToken = tokenSession.findByToken(token);
		//Integer userId = objToken.getUser().getId();
		
		String tempKeyword = "%" + keyword + "%";
		long countData = bankSession.getBankListCount(tempKeyword);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("draw", draw);
		result.put("recordsTotal", countData);
		result.put("recordsFiltered", countData);		
		result.put("data", bankSession.getBankListWithPagination(start, length, tempKeyword, columnOrder, tipeOrder));
		
		return Response.ok(result).build();
	}
	
	@SuppressWarnings({"rawtypes"})
	 @Path("/get-edit-bank-wilayah-by-id/{id}")
	 @GET
	 public Map get(@PathParam("id") int id,
	   @HeaderParam("Authorization") String token) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		Bank bank = bankSession.getBank(id);
		List<Wilayah> propinsi = wilayahSession.getPropinsiByNama(bank.getPropinsi());
		List<Wilayah> kota = wilayahSession.getKotaByNama(bank.getKota());
		/*List<Wilayah> kota = wilayahSession.getKotaList(propinsi.get(0).getLokasi_propinsi());*/
		
		
		//get session untuk list provinsi
		
		//looping, compare dengan objek bank propinsi
		
		//get session untuk ambil wilayah list semua
		
		//looping, compare dengan objek bank kota
		
		
		map.put("bank", bank);
		map.put("propinsi", propinsi);
		map.put("kota", kota);
		
		return map;	
	}
	
	/*
	@Path("/insert")
	@POST
	public Bank insert(Bank bank,@HeaderParam("Authorization") String token) { 
		bankSession.insertBank(bank,tokenSession.findByToken(token));
		return bank;
	}*/
	
	@SuppressWarnings({"rawtypes"})
	 @Path("/insert")
	 @POST
	 public Map insert(Bank bank,
	   @HeaderParam("Authorization") String token) {
	  
	  Map<Object, Object> map = new HashMap<Object, Object>();
	/*  String toDo = "insert";*/
	  
	  /*Boolean isSaveNama = mataUangSession.checkNamaMataUang(matauang.getNama(), toDo, matauang.getId());*/
	 /* Boolean isSaveKodeBank = bankSession.checkKodeBank(bank.getKodeBank(), toDo, bank.getId());*/
	  
	 /* if(!isSaveKodeBank) {
	   String errorKodeBank = "Kode Bank Tidak Boleh Sama";
	   Boolean isValid = false;
	   map.put("isValid", isValid);
	   map.put("errorKodeBank", errorKodeBank);
	  }
	  */
	  /*if(!isSaveKode) {
	   String errorKode = "Kode Mata Uang Tidak Boleh Sama";
	   Boolean isValid = false;
	   map.put("isValid", isValid);
	   map.put("errorKode", errorKode);
	  }*/
	  bankSession.insertBank(bank, tokenSession.findByToken(token));
	  /*if(isSaveKodeBank && isSaveKode) {
	   
	  }*/
	  
	  map.put("bank", bank);
	  
	  return map;
	 }

	/*@Path("/create")
	@POST
	public Bank insertBank(@FormParam("kodeBank") String kodeBank,
			@FormParam("namaBank") String namaBank,
			@FormParam("statusKantor") String statusKantor,
			@FormParam("namaKantor") String namaKantor,
			@FormParam("alamat") String alamat,
			@FormParam("kota") String kota,
			@FormParam("propinsi") String propinsi,
			@FormParam("kodePos") String kodePos,
			@FormParam("telepon") String telepon,
			@HeaderParam("Authorization") String token) {
		Bank bank = new Bank();
		bank.setKodeBank(kodeBank);
		bank.setNamaBank(namaBank);
		bank.setStatusKantor(statusKantor);
		bank.setNamaKantor(namaKantor);
		bank.setAlamat(alamat);
		bank.setKota(kota);
		bank.setPropinsi(propinsi);
		bank.setKodePos(kodePos);
		bank.setTelepon(telepon);
		bank.setUserId(0);
		bankSession.insertBank(bank, tokenSession.findByToken(token));
		return bank;
	}*/

	@SuppressWarnings({"rawtypes"})
	 @Path("/update")
	 @POST
	 public Map update(Bank bank,
	   @HeaderParam("Authorization") String token) {
	  
	  Map<Object, Object> map = new HashMap<Object, Object>();
	  String toDo = "update";
	  
	  /*Boolean isSaveNama = mataUangSession.checkNamaMataUang(matauang.getNama(), toDo, matauang.getId());*/
	  Boolean isSaveKodeBank = bankSession.checkKodeBank(bank.getKodeBank(), toDo, bank.getId());
	  
	  if(!isSaveKodeBank) {
		   String errorKodeBank = "Kode Bank Tidak Boleh Sama";
		   Boolean isValid = false;
		   map.put("isValid", isValid);
		   map.put("errorKodeBank", errorKodeBank);
		  }
		  
		  /*if(!isSaveKode) {
		   String errorKode = "Kode Mata Uang Tidak Boleh Sama";
		   Boolean isValid = false;
		   map.put("isValid", isValid);
		   map.put("errorKode", errorKode);
		  }*/
		 
		  if(isSaveKodeBank/* && isSaveKode*/) {
		   bankSession.updateBank(bank, tokenSession.findByToken(token));
		  }
		  
		  map.put("bank", bank);
	  
	  return map;
	 }
	
	
	/*@Path("/update")
	@POST
	public Bank updateBank(@FormParam("id") Integer id,
			@FormParam("kodeBank") String kodeBank,
			@FormParam("namaBank") String namaBank,
			@FormParam("statusKantor") String statusKantor,
			@FormParam("namaKantor") String namaKantor,
			@FormParam("alamat") String alamat,
			@FormParam("kota") String kota,
			@FormParam("propinsi") String propinsi,
			@FormParam("kodePos") String kodePos,
			@FormParam("telepon") String telepon,
			@HeaderParam("Authorization") String token) {
		Bank bank = bankSession.find(id);
		bank.setKodeBank(kodeBank);
		bank.setNamaBank(namaBank);
		bank.setStatusKantor(statusKantor);
		bank.setNamaKantor(namaKantor);
		bank.setAlamat(alamat);
		bank.setKota(kota);
		bank.setPropinsi(propinsi);
		bank.setKodePos(kodePos);
		bank.setTelepon(telepon);
		bankSession.updateBank(bank, tokenSession.findByToken(token));
		return bank;
	}
*/
	@Path("/delete")
	@POST
	public Bank deleteBank(@FormParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return bankSession.deleteBank(id, tokenSession.findByToken(token));
	}

	/*@Path("/deleteRow/{id}")
	@GET
	public Bank deleteRowBank(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		return bankSession.deleteRowBank(id, tokenSession.findByToken(token));
	}*/
}
