package id.co.promise.procurement.kontrakmanajemen;

import id.co.promise.procurement.entity.Pengadaan;
import id.co.promise.procurement.entity.Vendor;

public class PengadaanVendorDTO {
	Pengadaan pengadaan;
	Vendor vendor;
	public Pengadaan getPengadaan() {
		return pengadaan;
	}
	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	
	
}
