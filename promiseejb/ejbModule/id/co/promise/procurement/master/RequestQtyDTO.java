package id.co.promise.procurement.master;

import java.util.List;

public class RequestQtyDTO {
	
	
	private Integer catalogId;
	private Integer qty;
	private Integer harga;
	private Double diskon;
	private Integer organisasiId;
	private List<Integer> catalogIdList;
	private Integer addresBookId;
	
	public Integer getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Integer getHarga() {
		return harga;
	}
	public void setHarga(Integer harga) {
		this.harga = harga;
	}
	public Double getDiskon() {
		return diskon;
	}
	public void setDiskon(Double diskon) {
		this.diskon = diskon;
	}
	public Integer getOrganisasiId() {
		return organisasiId;
	}
	public void setOrganisasiId(Integer organisasiId) {
		this.organisasiId = organisasiId;
	}
	public List<Integer> getCatalogIdList() {
		return catalogIdList;
	}
	public void setCatalogIdList(List<Integer> catalogIdList) {
		this.catalogIdList = catalogIdList;
	}
	public Integer getAddresBookId() {
		return addresBookId;
	}
	public void setAddresBookId(Integer addresBookId) {
		this.addresBookId = addresBookId;
	}
	

	
	
}
