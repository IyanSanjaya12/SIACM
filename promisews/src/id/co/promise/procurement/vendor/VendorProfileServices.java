package id.co.promise.procurement.vendor;

import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.master.KualifikasiVendorSession;
import id.co.promise.procurement.security.TokenSession;

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

@Stateless
@Path(value="/procurement/vendor/VendorProfileServices")
@Produces(MediaType.APPLICATION_JSON)
public class VendorProfileServices {
	@EJB private VendorProfileSession vendorProfileSession;
	@EJB private VendorSession vendorSession;
	@EJB private KualifikasiVendorSession kualifikasiVendorSession;
	@EJB private TokenSession tokenSession;
	
	@Path("/getVendorProfileByVendorId/{vendorId}")
	@GET
	public List<VendorProfile> getVendorProfileByVendorId(@PathParam("vendorId") int vendorId){
		return vendorProfileSession.getVendorProfileByVendorId(vendorId);
	}
	
	@Path("/getVendorProfileByNoPKP/{nomorPKP}")
	@GET
	public List<VendorProfile> getVendorProfileByNoPKP(@PathParam("nomorPKP") String nomorPKP){
		return vendorProfileSession.getVendorProfileByNoPKP(nomorPKP);
	}
	
	@Path("/getVendorProfileByNoNPWP/{nomorNPWP}")
	@GET
	public List<VendorProfile> getVendorProfileByNoNPWP(@PathParam("nomorNPWP") String nomorNPWP){
		return vendorProfileSession.getVendorProfileByNoNPWP(nomorNPWP);
	}
	
	@Path("/createVendorProfile")
	@POST
	public VendorProfile createVendorProfile (
			@FormParam("vendor") int vendorId, 
			@FormParam("jenisPajakPerusahaan") int jenisPajakPerusahaan, 
			@FormParam("nomorPKP") String nomorPKP, 
			@FormParam("kualifikasiVendor") int kualifikasiVendorId,
			@FormParam("title") String title,
			@FormParam("unitTerdaftar") String unitTerdaftar,
			@FormParam("bussinessArea") int bussinessAreaId,
			@FormParam("tipePerusahaan") String tipePerusahaan, 
			@FormParam("namaPerusahaan") String namaPerusahaan,
			@FormParam("npwpPerusahaan") String npwpPerusahaan,
			@FormParam("namaNPWP") String namaNPWP,
			@FormParam("alamatNPWP") String alamatNPWP,
			@FormParam("provinsiNPWP") String provinsiNPWP,
			@FormParam("kotaNPWP") String kotaNPWP,
			@FormParam("namaSingkatan") String namaSingkatan, 
			@FormParam("alamat") String alamat, 
			@FormParam("kota") String kota, 
			@FormParam("kodePos") String kodePos, 
			@FormParam("poBox") String poBox, 
			@FormParam("provinsi") String provinsi, 
			@FormParam("telephone") String telephone, 
			@FormParam("faksimile") String faksimile, 
			@FormParam("email") String email, 
			@FormParam("website") String website, 			
			@FormParam("namaContactPerson") String namaContactPerson, 
			@FormParam("hpContactPerson") String hpContactPerson, 
			@FormParam("emailContactPerson") String emailContactPerson, 
			@FormParam("ktpContactPerson") String ktpContactPerson,
			@FormParam("jumlahKaryawan") String jumlahKaryawan,
			@FormParam("noAktaPendirian") String noAktaPendirian,
			@FormParam("tanggalBerdiri") Date tanggalBerdiri,
			@FormParam("nomorKKContactPerson") String noKK,
			@HeaderParam("Authorization") String token
			){
		VendorProfile vendorProfile = new VendorProfile();
		
		if (vendorId > 0) {
			vendorProfile.setVendor(vendorSession.find(vendorId));
		}
		
		if (jenisPajakPerusahaan > 0) {
			vendorProfile.setJenisPajakPerusahaan(jenisPajakPerusahaan);
		}
		
		if (nomorPKP != null && nomorPKP.length() > 0) {
			vendorProfile.setNomorPKP(nomorPKP);
		}
		
		if (kualifikasiVendorId > 0) {
			vendorProfile.setKualifikasiVendor(kualifikasiVendorSession.find(kualifikasiVendorId));
		}
		//------------------------------tambahan-------------------------
		
		if (title != null && title.length() > 0) {
			vendorProfile.setTitle(title);
		}
		if (bussinessAreaId > 0) {
			vendorProfile.setBussinessArea(bussinessAreaId);
		}
		
		if (namaNPWP != null && namaNPWP.length() > 0) {
			vendorProfile.setNamaNPWP(namaNPWP);
		}
		
		if (alamatNPWP != null && alamatNPWP.length() > 0) {
			vendorProfile.setAlamatNPWP(alamatNPWP);
		}
		
		if (provinsiNPWP != null && provinsiNPWP.length() > 0) {
			vendorProfile.setProvinsiNPWP(provinsiNPWP);
		}
		
		if (kotaNPWP != null && kotaNPWP.length() > 0) {
			vendorProfile.setKotaNPWP(kotaNPWP);
		}
		
		if (noAktaPendirian != null && noAktaPendirian.length() > 0) {
			vendorProfile.setNoAktaPendirian(noAktaPendirian);
		}
		
		if (noKK != null && noKK.length()> 0) {
			vendorProfile.setNoKK(noKK);
		}
		
		//-----------------------------------------------------------------
		
		if (namaPerusahaan != null && namaPerusahaan.length() > 0) {
			vendorProfile.setNamaPerusahaan(namaPerusahaan);
		}
		
		if (npwpPerusahaan != null && npwpPerusahaan.length() > 0) {
			vendorProfile.setNpwpPerusahaan(npwpPerusahaan);
		}
		
		if (namaPerusahaan != null && namaPerusahaan.length() > 0) {
			vendorProfile.setNamaPerusahaan(namaPerusahaan);
		}
		
		if (unitTerdaftar != null && unitTerdaftar.length() > 0) {
			vendorProfile.setUnitTerdaftar(unitTerdaftar);
		}
		
		if (tipePerusahaan != null && tipePerusahaan.length() > 0) {
			vendorProfile.setTipePerusahaan(tipePerusahaan);
		}
		
		if (namaSingkatan != null && namaSingkatan.length() > 0) {
			vendorProfile.setNamaSingkatan(namaSingkatan);
		}
		
		if (alamat != null && alamat.length() > 0) {
			vendorProfile.setAlamat(alamat);
		}
		
		if (kota != null && kota.length() > 0) {
			vendorProfile.setKota(kota);
		}
		
		if (kodePos != null && kodePos.length() > 0) {
			vendorProfile.setKodePos(kodePos);
		}
		
		if (poBox != null && poBox.length() > 0) {
			vendorProfile.setPoBox(poBox);
		}
		
		if (provinsi != null && provinsi.length() > 0) {
			vendorProfile.setProvinsi(provinsi);
		}
		
		if (telephone != null && telephone.length() > 0) {
			vendorProfile.setTelephone(telephone);
		}
		
		if (faksimile != null && faksimile.length() > 0) {
			vendorProfile.setFaksimile(faksimile);
		}
		
		if (email != null && email.length() > 0) {
			vendorProfile.setEmail(email);
		}
		
		if (website != null && website.length() > 0) {
			vendorProfile.setWebsite(website);
		}
		
		if (namaContactPerson != null && namaContactPerson.length() > 0) {
			vendorProfile.setNamaContactPerson(namaContactPerson);
		}
		
		if (hpContactPerson != null && hpContactPerson.length() > 0) {
			vendorProfile.setHpContactPerson(hpContactPerson);
		}
		
		if (emailContactPerson != null && emailContactPerson.length() > 0) {
			vendorProfile.setEmailContactPerson(emailContactPerson);
		}
		
		if (ktpContactPerson != null && ktpContactPerson.length() > 0) {
			vendorProfile.setKtpContactPerson(ktpContactPerson);
		}
		
		if (jumlahKaryawan != null && jumlahKaryawan.length() > 0) {
			vendorProfile.setJumlahKaryawan(jumlahKaryawan);
		}
		
		if (tanggalBerdiri != null) {
			vendorProfile.setTanggalBerdiri(tanggalBerdiri);
		}
		
		vendorProfile.setCreated(new Date());
		vendorProfile.setIsDelete(0);
		
		vendorProfileSession.insertVendorProfile(vendorProfile, tokenSession.findByToken(token));
		
		return vendorProfile;
	}
	
	@Path("/editVendorProfile")
	@POST
	public VendorProfile editVendorProfile (
			@FormParam("id") int vendorProfileId, 
			@FormParam("vendor") int vendorId, 
			@FormParam("jenisPajakPerusahaan") int jenisPajakPerusahaan, 
			@FormParam("nomorPKP") String nomorPKP, 
			@FormParam("kualifikasiVendor") int kualifikasiVendorId,
			@FormParam("title") String title,
			@FormParam("unitTerdaftar") String unitTerdaftar,
			@FormParam("bussinessArea") int bussinessAreaId,
			@FormParam("tipePerusahaan") String tipePerusahaan, 
			@FormParam("namaPerusahaan") String namaPerusahaan,
			@FormParam("npwpPerusahaan") String npwpPerusahaan,
			@FormParam("namaNPWP") String namaNPWP,
			@FormParam("alamatNPWP") String alamatNPWP,
			@FormParam("provinsiNPWP") String provinsiNPWP,
			@FormParam("kotaNPWP") String kotaNPWP,
			@FormParam("namaSingkatan") String namaSingkatan, 
			@FormParam("alamat") String alamat, 
			@FormParam("kota") String kota, 
			@FormParam("kodePos") String kodePos, 
			@FormParam("poBox") String poBox, 
			@FormParam("provinsi") String provinsi, 
			@FormParam("telephone") String telephone, 
			@FormParam("faksimile") String faksimile, 
			@FormParam("email") String email, 
			@FormParam("website") String website, 			
			@FormParam("namaContactPerson") String namaContactPerson, 
			@FormParam("hpContactPerson") String hpContactPerson, 
			@FormParam("emailContactPerson") String emailContactPerson, 
			@FormParam("ktpContactPerson") String ktpContactPerson,
			@FormParam("nomorKKContactPerson") String noKK,
			@FormParam("jumlahKaryawan") String jumlahKaryawan,
			@FormParam("noAktaPendirian") String noAktaPendirian,
			@FormParam("tanggalBerdiri") Date tanggalBerdiri,
			
			@HeaderParam("Authorization") String token
			){
		VendorProfile vendorProfile = vendorProfileSession.find(vendorProfileId);
		
		if (vendorId > 0) {
			vendorProfile.setVendor(vendorSession.find(vendorId));
		}
		
		if (nomorPKP != null && nomorPKP.length() > 0) {
			vendorProfile.setNomorPKP(nomorPKP);
		}
		
		if (kualifikasiVendorId > 0) {
			vendorProfile.setKualifikasiVendor(kualifikasiVendorSession.find(kualifikasiVendorId));
		}
		//------------------------------tambahan-------------------------
		
		if (title != null && title.length() > 0) {
			vendorProfile.setTitle(title);
		}
		if (bussinessAreaId > 0) {
			vendorProfile.setBussinessArea(bussinessAreaId);
		}
		
		if (namaNPWP != null && namaNPWP.length() > 0) {
			vendorProfile.setNamaNPWP(namaNPWP);
		}
		
		if (alamatNPWP != null && alamatNPWP.length() > 0) {
			vendorProfile.setAlamatNPWP(alamatNPWP);
		}
		
		if (provinsiNPWP != null && provinsiNPWP.length() > 0) {
			vendorProfile.setProvinsiNPWP(provinsiNPWP);
		}
		
		if (kotaNPWP != null && kotaNPWP.length() > 0) {
			vendorProfile.setKotaNPWP(kotaNPWP);
		}
		
		if (noAktaPendirian != null && noAktaPendirian.length() > 0) {
			vendorProfile.setNoAktaPendirian(noAktaPendirian);
		}
		
		if (noKK !=null && noKK.length()> 0) {
			vendorProfile.setNoKK(noKK);
		}
		
		
		//-----------------------------------------------------------------
		
		if (namaPerusahaan != null && namaPerusahaan.length() > 0) {
			vendorProfile.setNamaPerusahaan(namaPerusahaan);
		}
		
		if (npwpPerusahaan != null && npwpPerusahaan.length() > 0) {
			vendorProfile.setNpwpPerusahaan(npwpPerusahaan);
		}
		
		if (namaPerusahaan != null && namaPerusahaan.length() > 0) {
			vendorProfile.setNamaPerusahaan(namaPerusahaan);
		}
		
		if (unitTerdaftar != null && unitTerdaftar.length() > 0) {
			vendorProfile.setUnitTerdaftar(unitTerdaftar);
		}
		
		if (tipePerusahaan != null && tipePerusahaan.length() > 0) {
			vendorProfile.setTipePerusahaan(tipePerusahaan);
		}
		
		if (namaSingkatan != null && namaSingkatan.length() > 0) {
			vendorProfile.setNamaSingkatan(namaSingkatan);
		}
		
		if (alamat != null && alamat.length() > 0) {
			vendorProfile.setAlamat(alamat);
		}
		
		if (kota != null && kota.length() > 0) {
			vendorProfile.setKota(kota);
		}
		
		if (kodePos != null && kodePos.length() > 0) {
			vendorProfile.setKodePos(kodePos);
		}
		
		if (poBox != null && poBox.length() > 0) {
			vendorProfile.setPoBox(poBox);
		}
		
		if (provinsi != null && provinsi.length() > 0) {
			vendorProfile.setProvinsi(provinsi);
		}
		
		if (telephone != null && telephone.length() > 0) {
			vendorProfile.setTelephone(telephone);
		}
		
		if (faksimile != null && faksimile.length() > 0) {
			vendorProfile.setFaksimile(faksimile);
		}
		
		if (email != null && email.length() > 0) {
			vendorProfile.setEmail(email);
		}
		
		if (website != null && website.length() > 0) {
			vendorProfile.setWebsite(website);
		}
		
		if (namaContactPerson != null && namaContactPerson.length() > 0) {
			vendorProfile.setNamaContactPerson(namaContactPerson);
		}
		
		if (hpContactPerson != null && hpContactPerson.length() > 0) {
			vendorProfile.setHpContactPerson(hpContactPerson);
		}
		
		if (emailContactPerson != null && emailContactPerson.length() > 0) {
			vendorProfile.setEmailContactPerson(emailContactPerson);
		}
		
		if (ktpContactPerson != null && ktpContactPerson.length() > 0) {
			vendorProfile.setKtpContactPerson(ktpContactPerson);
		}
		
		if (jumlahKaryawan != null && jumlahKaryawan.length() > 0) {
			vendorProfile.setJumlahKaryawan(jumlahKaryawan);
		}
		
		if (tanggalBerdiri != null) {
			vendorProfile.setTanggalBerdiri(tanggalBerdiri);
		}
		
		if (title.equalsIgnoreCase("Company")){
			vendorProfile.setKtpContactPerson(null);
			vendorProfile.setNoKK(null);
		}

		vendorProfileSession.updateVendorProfile(vendorProfile, tokenSession.findByToken(token));
		
		return vendorProfile;
	}

	@Path("/deleteRowVendorProfile/{id}")
	@GET
	public void deleteRowVendorProfile(@PathParam("id") int id,
			@HeaderParam("Authorization") String token) {
		vendorProfileSession.deleteRowVendorProfile(id, tokenSession.findByToken(token));
	}

}
