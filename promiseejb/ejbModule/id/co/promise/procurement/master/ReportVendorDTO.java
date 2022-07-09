package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.VendorProfile;

public class ReportVendorDTO {
	
	VendorProfile vendorProfile;
	Organisasi businessArea;
	
	public VendorProfile getVendorProfile() {
		return vendorProfile;
	}
	public void setVendorProfile(VendorProfile vendorProfile) {
		this.vendorProfile = vendorProfile;
	}
	public Organisasi getBusinessArea() {
		return businessArea;
	}
	public void setBusinessArea(Organisasi businessArea) {
		this.businessArea = businessArea;
	}
	
}
