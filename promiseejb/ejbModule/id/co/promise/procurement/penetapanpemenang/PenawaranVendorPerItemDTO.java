package id.co.promise.procurement.penetapanpemenang;

import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Vendor;

public class PenawaranVendorPerItemDTO {
	private Vendor vendor;
	private MataUang mataUang;
	private Double nilaiPenawaran;
	private Item item;
	private Double nilaiAdmin;
	private Double nilaiTeknis;
	private Double nilaiHarga;
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public MataUang getMataUang() {
		return mataUang;
	}
	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}
	public Double getNilaiPenawaran() {
		return nilaiPenawaran;
	}
	public void setNilaiPenawaran(Double nilaiPenawaran) {
		this.nilaiPenawaran = nilaiPenawaran;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Double getNilaiAdmin() {
		return nilaiAdmin;
	}
	public void setNilaiAdmin(Double nilaiAdmin) {
		this.nilaiAdmin = nilaiAdmin;
	}
	public Double getNilaiTeknis() {
		return nilaiTeknis;
	}
	public void setNilaiTeknis(Double nilaiTeknis) {
		this.nilaiTeknis = nilaiTeknis;
	}
	public Double getNilaiHarga() {
		return nilaiHarga;
	}
	public void setNilaiHarga(Double nilaiHarga) {
		this.nilaiHarga = nilaiHarga;
	}
	
	
}
