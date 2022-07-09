package id.co.promise.procurement.kontrakmanajemen;

import id.co.promise.procurement.entity.TerminKontrak;
import id.co.promise.procurement.master.JenisTerminSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.security.TokenSession;

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

@Stateless
@Path(value = "/procurement/TerminKontrakServices")
@Produces(MediaType.APPLICATION_JSON)
public class TerminKontrakServices {
	
	private SimpleDateFormat formatIndonesia = new SimpleDateFormat("yyyy-MM-dd");
	
	@EJB private TerminKontrakSession terminKontrakSession;	
	@EJB private KontrakSession kontrakSession;	
	@EJB private JenisTerminSession jenisTerminSession;
	@EJB private MataUangSession mataUangSession;
	@EJB TokenSession tokenSession;
	
	@Path("/getList")
	@GET
	public List<TerminKontrak> getTerminKontrakList(){
		return terminKontrakSession.getTerminKontrakList();
	}

	@Path("/getTerminKontrakByVendorList/{id}")
	@GET
	public List<TerminKontrak> getTerminKontrakByVendorList(@PathParam("id")Integer kontrakId) {
		return terminKontrakSession.getTerminKontrakByVendor(kontrakId);
	}

	@Path("/create")
	@POST
	public TerminKontrak create(
			@FormParam("kontrak") Integer kontrakId,
			@FormParam("tanggal") String tanggal,
			@FormParam("jenisTermin") Integer jenisTerminId,
			@FormParam("keterangan") String keterangan,
			@FormParam("mataUang") Integer mataUangId,
			@FormParam("nilai") Double nilai,
			@FormParam("nilaiTukar") Double nilaiTukar,
			@HeaderParam("Authorization") String token) {
		TerminKontrak terminKontrak = new TerminKontrak();
		
		terminKontrak.setKontrak(kontrakSession.find(kontrakId));
		if (tanggal != null) {
			try {
				terminKontrak.setTanggal(formatIndonesia.parse(tanggal));
			} catch (ParseException e) {}
		}
		if (jenisTerminId != null) {
			terminKontrak.setJenisTermin(jenisTerminSession.find(jenisTerminId));
		}
		terminKontrak.setKeterangan(keterangan);
		if (mataUangId != null) {
			terminKontrak.setMataUang(mataUangSession.find(mataUangId));
		}
		terminKontrak.setNilai(nilai);
		terminKontrak.setNilaiTukar(nilaiTukar);
		terminKontrak.setUserId(0);
		
		terminKontrakSession.insertTerminKontrak(terminKontrak, tokenSession.findByToken(token));
		
		return terminKontrak;
	}

	@Path("/update")
	@POST
	public TerminKontrak update(
			@FormParam("id") Integer terminKontrakId,
			@FormParam("kontrak") Integer kontrakId,
			@FormParam("tanggal") String tanggal,
			@FormParam("jenisTermin") Integer jenisTerminId,
			@FormParam("keterangan") String keterangan,
			@FormParam("mataUang") Integer mataUangId,
			@FormParam("nilai") Double nilai,
			@FormParam("nilaiTukar") Double nilaiTukar,
			@HeaderParam("Authorization") String token) {
		TerminKontrak terminKontrak = terminKontrakSession.find(terminKontrakId);
		
		terminKontrak.setKontrak(kontrakSession.find(kontrakId));
		if (tanggal != null) {
			try {
				terminKontrak.setTanggal(formatIndonesia.parse(tanggal));
			} catch (ParseException e) {}
		}
		if (jenisTerminId != null) {
			terminKontrak.setJenisTermin(jenisTerminSession.find(jenisTerminId));
		}
		terminKontrak.setKeterangan(keterangan);
		if (mataUangId != null) {
			terminKontrak.setMataUang(mataUangSession.find(mataUangId));
		}
		terminKontrak.setNilai(nilai);
		terminKontrak.setNilaiTukar(nilaiTukar);
		
		terminKontrakSession.updateTerminKontrak(terminKontrak, tokenSession.findByToken(token));
		
		return terminKontrak;
	}
	
	@Path("/delete/{id}")
	@GET
	public TerminKontrak deleteTerminKontrak(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return terminKontrakSession.deleteTerminKontrak(id, tokenSession.findByToken(token));
	}
	
	@Path("/deleteRow/{id}")
	@GET
	public TerminKontrak deleteRowTerminKontrak(@PathParam("id")int id,
			@HeaderParam("Authorization") String token){
		return terminKontrakSession.deleteRowTerminKontrak(id, tokenSession.findByToken(token));
	}
}