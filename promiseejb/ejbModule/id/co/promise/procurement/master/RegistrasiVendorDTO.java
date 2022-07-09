package id.co.promise.procurement.master;

import java.util.List;

import id.co.promise.procurement.entity.BankVendor;
import id.co.promise.procurement.entity.DokumenRegistrasiVendor;
import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.entity.VendorPIC;
import id.co.promise.procurement.entity.VendorProfile;
import id.co.promise.procurement.entity.VendorSKD;

import javax.persistence.Column;

public class RegistrasiVendorDTO {
	
	private User user;
	private Vendor vendor;
	private VendorProfile vendorProfile;
	private VendorSKD vendorSKD;
	private List<VendorPIC> vendorPICList;
	private List<BankVendor> bankVendorList;
	private List<SegmentasiVendor> segmentasiVendorList;
	private List<PeralatanVendor> peralatanVendorList;
	private List<KeuanganVendor> keuanganVendorList;
 	private List<PengalamanVendor> pengalamanVendorList;
 	private List<DokumenRegistrasiVendor>dokumenRegistrasiVendorList;
 	
 	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public VendorProfile getVendorProfile() {
		return vendorProfile;
	}
	public void setVendorProfile(VendorProfile vendorProfile) {
		this.vendorProfile = vendorProfile;
	}
	public VendorSKD getVendorSKD() {
		return vendorSKD;
	}
	public void setVendorSKD(VendorSKD vendorSKD) {
		this.vendorSKD = vendorSKD;
	}
	public List<VendorPIC> getVendorPICList() {
		return vendorPICList;
	}
	public void setVendorPICList(List<VendorPIC> vendorPICList) {
		this.vendorPICList = vendorPICList;
	}
	public List<BankVendor> getBankVendorList() {
		return bankVendorList;
	}
	public void setBankVendorList(List<BankVendor> bankVendorList) {
		this.bankVendorList = bankVendorList;
	}
	public List<SegmentasiVendor> getSegmentasiVendorList() {
		return segmentasiVendorList;
	}
	public void setSegmentasiVendorList(List<SegmentasiVendor> segmentasiVendorList) {
		this.segmentasiVendorList = segmentasiVendorList;
	}
	public List<PeralatanVendor> getPeralatanVendorList() {
		return peralatanVendorList;
	}
	public void setPeralatanVendorList(List<PeralatanVendor> peralatanVendorList) {
		this.peralatanVendorList = peralatanVendorList;
	}
	public List<KeuanganVendor> getKeuanganVendorList() {
		return keuanganVendorList;
	}
	public void setKeuanganVendorList(List<KeuanganVendor> keuanganVendorList) {
		this.keuanganVendorList = keuanganVendorList;
	}
	public List<PengalamanVendor> getPengalamanVendorList() {
		return pengalamanVendorList;
	}
	public void setPengalamanVendorList(List<PengalamanVendor> pengalamanVendorList) {
		this.pengalamanVendorList = pengalamanVendorList;
	}
	public List<DokumenRegistrasiVendor> getDokumenRegistrasiVendorList() {
		return dokumenRegistrasiVendorList;
	}
	public void setDokumenRegistrasiVendorList(
			List<DokumenRegistrasiVendor> dokumenRegistrasiVendorList) {
		this.dokumenRegistrasiVendorList = dokumenRegistrasiVendorList;
	}

}
