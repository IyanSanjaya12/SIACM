package id.co.promise.procurement.DTO;

import java.util.Date;

public class FormFinalContractItem {
	
	private Integer pengadaanId;
	
	private String noDrafKontrak;
	
	private Date tglKontrak;

	public Integer getPengadaanId() {
		return pengadaanId;
	}

	public void setPengadaanId(Integer pengadaanId) {
		this.pengadaanId = pengadaanId;
	}

	public String getNoDrafKontrak() {
		return noDrafKontrak;
	}

	public void setNoDrafKontrak(String noDrafKontrak) {
		this.noDrafKontrak = noDrafKontrak;
	}

	public Date getTglKontrak() {
		return tglKontrak;
	}

	public void setTglKontrak(Date tglKontrak) {
		this.tglKontrak = tglKontrak;
	}

}
