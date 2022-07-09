package id.co.promise.procurement.master;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import id.co.promise.procurement.entity.Kontrak;
import id.co.promise.procurement.kontrakmanajemen.KontrakSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@Path("/procurement/master/KontrakServices")
@Produces(MediaType.APPLICATION_JSON)
public class KontrakServices {
	private DateFormat formatIndonesia = new SimpleDateFormat("dd-MM-yyyy");
	
	@EJB
	KontrakSession kontrakSession;
	@EJB
	MataUangSession mataUangSession;
	@EJB
	VendorSession vendorSession;
	@EJB
	OrganisasiSession organisasiSession;
	
	@EJB
	TokenSession tokenSession;
	
	@Path("/getId/{id}")
	@GET
	public Kontrak getKontrak(@PathParam("id")int id){
		return kontrakSession.getKontrak(id);
	}
	
	@Path("/getList")
	@GET
	public List<Kontrak> getKontrakList(){
		return kontrakSession.getKontrakList();
	}
	
	@Path("/getKontrakListByPengadaanId/{pengadaanId}")
	@GET
	public List<Kontrak> getKontrakListByPengadaanId(@PathParam("pengadaanId")int pengadaanId){
		return kontrakSession.getKontrakListByPengadaanId(pengadaanId);
	}
	
	@Path("/getKontrakListByVendorId/{vendorId}")
	@GET
	public List<Kontrak> getKontrakListByVendorId(@PathParam("vendorId")int vendorId){
		return kontrakSession.getKontrakListByVendorId(vendorId);
	}
	
	@Path("/create")
	@POST
	public Kontrak insertKontrak(@FormParam("nomor")String nomor,
			@FormParam("nomorSuratPenawaran")String nomorSuratPenawaran,
			@FormParam("tglSuratPenawaran")String tglSuratPenawaran,
			@FormParam("nomorKeputusanSuratPenawaran")String nomorKeputusanSuratPenawaran,
			@FormParam("tglKeputusanSuratPenawaran")String tglKeputusanSuratPenawaran,
			@FormParam("nilai")Double nilai,
			@FormParam("tglMulaiKontrak")String tglMulaiKontrak,
			@FormParam("tglSelesaiKontrak")String tglSelesaiKontrak,
			@FormParam("nilaiKursUSD")Double nilaiKursUSD,
			@FormParam("tglBatasPengiriman")String tglBatasPengiriman,
			@FormParam("namaJaminanPelaksana")String namaJaminanPelaksana,
			@FormParam("nilaiJaminanPelaksana")Double nilaiJaminanPelaksana,
			@FormParam("tglBatasJaminan")String tglBatasJaminan,
			@FormParam("statusKontrak")String statusKontrak,
			@FormParam("mataUang")Integer mataUangId,
			@FormParam("pengadaan")Integer pengadaanId,
			@FormParam("vendor")Integer vendorId,
			@FormParam("organisasi")Integer organisasiId,
			@HeaderParam("Authorization") String token){
		Kontrak x = new Kontrak();
		x.setNomor(nomor);
		x.setNomorSuratPenawaran(nomorSuratPenawaran);
		if(tglSuratPenawaran != null){
			try{
				x.setTglSuratPenawaran(formatIndonesia.parse(tglSuratPenawaran));
			}catch(ParseException e){}
		}
		x.setNomorKeputusanSuratPenawaran(nomorKeputusanSuratPenawaran);
		if(tglKeputusanSuratPenawaran != null){
			try{
				x.setTglKeputusanSuratPenawaran(formatIndonesia.parse(tglKeputusanSuratPenawaran));
			}catch(ParseException e){}
		}
		x.setNilai(nilai);
		if(tglMulaiKontrak != null){
			try{
				x.setTglMulaiKontrak(formatIndonesia.parse(tglMulaiKontrak));
			}catch(ParseException e){}
		}
		if(tglSelesaiKontrak != null){
			try{
				x.setTglSelesaiKontrak(formatIndonesia.parse(tglSelesaiKontrak));
			}catch(ParseException e){}
		}
		x.setNilaiKursUSD(nilaiKursUSD);
		if(tglBatasPengiriman != null){
			try{
				x.setTglBatasPengiriman(formatIndonesia.parse(tglBatasPengiriman));
			}catch(ParseException e){}
		}
		x.setNamaJaminanPelaksana(namaJaminanPelaksana);
		x.setNilaiJaminanPelaksana(nilaiJaminanPelaksana);
		if(tglBatasJaminan != null){
			try{
				x.setTglBatasJaminan(formatIndonesia.parse(tglBatasJaminan));
			}catch(ParseException e){}
		}
		x.setStatusKontrak(statusKontrak);
		x.setMataUang(mataUangSession.find(mataUangId));
		x.setVendor(vendorSession.find(vendorId));
		x.setOrganisasi(organisasiSession.find(organisasiId));
		x.setUserId(0);
		kontrakSession.insertKontrak(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/update")
	@POST
	public Kontrak updateKontrak(@FormParam("id")Integer id,
			@FormParam("nomor")String nomor,
			@FormParam("nomorSuratPenawaran")String nomorSuratPenawaran,
			@FormParam("tglSuratPenawaran")String tglSuratPenawaran,
			@FormParam("nomorKeputusanSuratPenawaran")String nomorKeputusanSuratPenawaran,
			@FormParam("tglKeputusanSuratPenawaran")String tglKeputusanSuratPenawaran,
			@FormParam("nilai")Double nilai,
			@FormParam("tglMulaiKontrak")String tglMulaiKontrak,
			@FormParam("tglSelesaiKontrak")String tglSelesaiKontrak,
			@FormParam("nilaiKursUSD")Double nilaiKursUSD,
			@FormParam("tglBatasPengiriman")String tglBatasPengiriman,
			@FormParam("namaJaminanPelaksana")String namaJaminanPelaksana,
			@FormParam("nilaiJaminanPelaksana")Double nilaiJaminanPelaksana,
			@FormParam("tglBatasJaminan")String tglBatasJaminan,
			@FormParam("statusKontrak")String statusKontrak,
			@FormParam("mataUang")Integer mataUangId,
			@FormParam("pengadaan")Integer pengadaanId,
			@FormParam("vendor")Integer vendorId,
			@FormParam("organisasi")Integer organisasiId,
			@HeaderParam("Authorization") String token){
		Kontrak x = kontrakSession.getKontrak(id);
		x.setNomor(nomor);
		x.setNomorSuratPenawaran(nomorSuratPenawaran);
		if(tglSuratPenawaran != null){
			try{
				x.setTglSuratPenawaran(formatIndonesia.parse(tglSuratPenawaran));
			}catch(ParseException e){}
		}
		x.setNomorKeputusanSuratPenawaran(nomorKeputusanSuratPenawaran);
		if(tglKeputusanSuratPenawaran != null){
			try{
				x.setTglKeputusanSuratPenawaran(formatIndonesia.parse(tglKeputusanSuratPenawaran));
			}catch(ParseException e){}
		}
		x.setNilai(nilai);
		if(tglMulaiKontrak != null){
			try{
				x.setTglMulaiKontrak(formatIndonesia.parse(tglMulaiKontrak));
			}catch(ParseException e){}
		}
		if(tglSelesaiKontrak != null){
			try{
				x.setTglSelesaiKontrak(formatIndonesia.parse(tglSelesaiKontrak));
			}catch(ParseException e){}
		}
		x.setNilaiKursUSD(nilaiKursUSD);
		if(tglBatasPengiriman != null){
			try{
				x.setTglBatasPengiriman(formatIndonesia.parse(tglBatasPengiriman));
			}catch(ParseException e){}
		}
		x.setNamaJaminanPelaksana(namaJaminanPelaksana);
		x.setNilaiJaminanPelaksana(nilaiJaminanPelaksana);
		if(tglBatasJaminan != null){
			try{
				x.setTglBatasJaminan(formatIndonesia.parse(tglBatasJaminan));
			}catch(ParseException e){}
		}
		x.setStatusKontrak(statusKontrak);
		x.setMataUang(mataUangSession.find(mataUangId));
		x.setVendor(vendorSession.find(vendorId));
		x.setOrganisasi(organisasiSession.find(organisasiId));
		x.setUserId(0);
		kontrakSession.updateKontrak(x, tokenSession.findByToken(token));
		return x;
	}
	
	@Path("/delete/{id}")
	@GET
	public Kontrak deleteKontrak(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return kontrakSession.deleteKontrak(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public Kontrak deleteRowKontrak(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return kontrakSession.deleteRowKontrak(id, tokenSession.findByToken(token));
	}
}
