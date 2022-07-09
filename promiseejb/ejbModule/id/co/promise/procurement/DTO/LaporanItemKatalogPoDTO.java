package id.co.promise.procurement.DTO;

import java.util.Date;

public class LaporanItemKatalogPoDTO {

	private String description;
	private String deskripsiIND;
	private String nama;
	private String boNumber;
	private String poNumber;
	private Date approvedDate;
	private Double quantity;
	private Double price;
	private Double total;
	private Integer rating;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDeskripsiIND() {
		return deskripsiIND;
	}
	public void setDeskripsiIND(String deskripsiIND) {
		this.deskripsiIND = deskripsiIND;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getBoNumber() {
		return boNumber;
	}
	public void setBoNumber(String boNumber) {
		this.boNumber = boNumber;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
}
