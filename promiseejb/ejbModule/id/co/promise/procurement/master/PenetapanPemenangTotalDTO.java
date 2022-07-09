package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.PengumumanPengadaan;


public class PenetapanPemenangTotalDTO {
	
	private Integer vendorId;
	private Integer pengadaanId;
	private Integer penetapanPemenangTotalId;
	private Integer jenisKontrakId;
	private PengumumanPengadaan pengumumanPengadaan;
	
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public Integer getPengadaanId() {
		return pengadaanId;
	}
	public void setPengadaanId(Integer pengadaanId) {
		this.pengadaanId = pengadaanId;
	}
	public Integer getJenisKontrakId() {
		return jenisKontrakId;
	}
	public void setJenisKontrakId(Integer jenisKontrakId) {
		this.jenisKontrakId = jenisKontrakId;
	}
	public Integer getPenetapanPemenangTotalId() {
		return penetapanPemenangTotalId;
	}
	public void setPenetapanPemenangTotalId(Integer penetapanPemenangTotalId) {
		this.penetapanPemenangTotalId = penetapanPemenangTotalId;
	}
	public PengumumanPengadaan getPengumumanPengadaan() {
		return pengumumanPengadaan;
	}
	public void setPengumumanPengadaan(PengumumanPengadaan pengumumanPengadaan) {
		this.pengumumanPengadaan = pengumumanPengadaan;
	}
	
}
