package id.co.promise.procurement.DTO;


import id.co.promise.procurement.entity.KeuanganVendor;
import id.co.promise.procurement.entity.KeuanganVendorDraft;



public class KeuanganVendorDTO {
	private Integer loginId;
	private KeuanganVendorDraft keuanganVendorDraft;
	private KeuanganVendor keuanganVendor;
	private String toDo;
	private Integer status;
	
	public Integer getLoginId() {
		return loginId;
	}
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	public KeuanganVendorDraft getKeuanganVendorDraft() {
		return keuanganVendorDraft;
	}
	public void setKeuanganVendorDraft(KeuanganVendorDraft keuanganVendorDraft) {
		this.keuanganVendorDraft = keuanganVendorDraft;
	}
	public KeuanganVendor getKeuanganVendor() {
		return keuanganVendor;
	}
	public void setKeuanganVendor(KeuanganVendor keuanganVendor) {
		this.keuanganVendor = keuanganVendor;
	}
	public String getToDo() {
		return toDo;
	}
	public void setToDo(String toDo) {
		this.toDo = toDo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	

}
