package id.co.promise.procurement.DTO;

import java.util.Date;


public class LaporanEvaluasiKinerjaVendorDTO {
	private String boNumber;
	private String poNumberEbs;
	private String prNumber;
	private String vendorName;
	private Date approvedDate;
	private Date dateReceived;
	private Integer rating;
	private String komen;
	private Integer id;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBoNumber() {
		return boNumber;
	}
	public String getPoNumberEbs() {
		return poNumberEbs;
	}
	public String getVendorName() {
		return vendorName;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public Date getDateReceived() {
		return dateReceived;
	}
	public Integer getRating() {
		return rating;
	}
	public void setBoNumber(String boNumber) {
		this.boNumber = boNumber;
	}
	public void setPoNumberEbs(String poNumberEbs) {
		this.poNumberEbs = poNumberEbs;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getPrNumber() {
		return prNumber;
	}
	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}
	public String getKomen() {
		return komen;
	}
	public void setKomen(String komen) {
		this.komen = komen;
	}
	
	
}
