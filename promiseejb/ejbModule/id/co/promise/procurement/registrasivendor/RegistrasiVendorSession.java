package id.co.promise.procurement.registrasivendor;

import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.BuktiKepemilikan;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.KondisiPeralatanVendor;
import id.co.promise.procurement.entity.KualifikasiVendor;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SubBidangUsaha;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.master.BuktiKepemilikanSession;
import id.co.promise.procurement.master.JabatanSession;
import id.co.promise.procurement.master.KondisiPeralatanVendorSession;
import id.co.promise.procurement.master.KualifikasiVendorSession;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.SubBidangUsahaSession;
import id.co.promise.procurement.vendor.BankVendorSession;
import id.co.promise.procurement.vendor.DokumenRegistrasiVendorSession;
import id.co.promise.procurement.vendor.KeuanganVendorSession;
import id.co.promise.procurement.vendor.PengalamanVendorSession;
import id.co.promise.procurement.vendor.PeralatanVendorSession;
import id.co.promise.procurement.vendor.SegmentasiVendorSession;
import id.co.promise.procurement.vendor.VendorPICSession;
import id.co.promise.procurement.vendor.VendorProfileSession;
import id.co.promise.procurement.vendor.VendorSession;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

@Singleton
@LocalBean
public class RegistrasiVendorSession {

	@EJB
	VendorSession vendorSession;

	@EJB
	VendorProfileSession vendorProfileSession;

	@EJB
	KualifikasiVendorSession kualifikasiVendorSession;

	@EJB
	JabatanSession jabatanSession;

	@EJB
	VendorPICSession vendorPicSession;

	@EJB
	MataUangSession mataUangSession;

	@EJB
	BankVendorSession bankVendorSession;

	@EJB
	SubBidangUsahaSession subBidangUsahaSession;

	@EJB
	SegmentasiVendorSession segmentasiVendorSession;

	@EJB
	DokumenRegistrasiVendorSession dokumenRegistrasiVendorSession;

	@EJB
	KondisiPeralatanVendorSession kondisiPeralatanVendorSession;

	@EJB
	BuktiKepemilikanSession buktiKepemilikanSession;

	@EJB
	PeralatanVendorSession peralatanVendorSession;

	@EJB
	KeuanganVendorSession keuanganVendorSession;

	@EJB
	PengalamanVendorSession pengalamanVendorSession;

	public Vendor insertVendor(Vendor vendor, Token token) {
		vendorSession.insertVendor(vendor, token);
		return vendor;
	}

	public Vendor findVendor(Integer id) {
		return vendorSession.find(id);
	}

	public VendorProfile insertVendorProfile(VendorProfile vendorProfile,
			Token token) {
		return vendorProfileSession.insertVendorProfile(vendorProfile, token);
	}

	public List<VendorProfile> getVendorProfileByNoPKP(String nomorPKP) {
		return vendorProfileSession.getVendorProfileByNoPKP(nomorPKP);
	}

	public KualifikasiVendor findKualifikasiVendor(Integer id) {
		return kualifikasiVendorSession.find(id);
	}

	public Jabatan findJabatan(Integer id) {
		return jabatanSession.find(id);
	}

	public VendorPIC insertVendorPIC(VendorPIC vendorPIC, Token token) {
		return vendorPicSession.insertVendorPIC(vendorPIC, token);
	}

	public MataUang findMataUang(Integer id) {
		return mataUangSession.find(id);
	}

	public BankVendor insertBankVendor(BankVendor bankVendor, Token token) {
		return bankVendorSession.insertBankVendor(bankVendor, token);
	}

	public SubBidangUsaha findSubBidangUsaha(Integer id) {
		return subBidangUsahaSession.find(id);
	}

	public SegmentasiVendor insertSegmentasiVendor(
			SegmentasiVendor segmentasiVendor, Token token) {
		return segmentasiVendorSession.insertSegmentasiVendor(segmentasiVendor,
				token);
	}

	public DokumenRegistrasiVendor insertDokumenRegistrasiVendor(
			DokumenRegistrasiVendor dokumenRegistrasiVendor, Token token) {
		return dokumenRegistrasiVendorSession.insertDokumenRegistrasiVendor(
				dokumenRegistrasiVendor, token);
	}

	public KondisiPeralatanVendor findKondisiPeralatanVendor(Integer id) {
		return kondisiPeralatanVendorSession.find(id);
	}

	public BuktiKepemilikan findBuktiKepemilikan(Integer id) {
		return buktiKepemilikanSession.find(id);
	}

	public PeralatanVendor insertPeralatanVendor(
			PeralatanVendor peralatanVendor, Token token) {
		return peralatanVendorSession.insertPeralatanVendor(peralatanVendor,
				token);
	}

	public KeuanganVendor insertKeuanganVendor(KeuanganVendor keuanganVendor,
			Token token) {
		return keuanganVendorSession
				.insertKeuanganVendor(keuanganVendor, token);
	}

	public PengalamanVendor insertPengalamanVendor(
			PengalamanVendor pengalamanVendor, Token token) {
		return pengalamanVendorSession.insertPengalamanVendor(pengalamanVendor,
				token);
	}

}
