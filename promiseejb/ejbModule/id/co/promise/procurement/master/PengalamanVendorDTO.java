package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.PengalamanVendor;
import id.co.promise.procurement.entity.PengalamanVendorDraft;


public class PengalamanVendorDTO {
	private PengalamanVendor pengalamanVendor;
	private Integer loginId;
	private Integer status;
	private PengalamanVendorDraft pengalamanVendorDraft;
	
	public PengalamanVendor getPengalamanVendor() {
		return pengalamanVendor;
	}
	public void setPengalamanVendor(PengalamanVendor pengalamanVendor) {
		this.pengalamanVendor = pengalamanVendor;
	}
	public Integer getLoginId() {
		return loginId;
	}
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public PengalamanVendorDraft getPengalamanVendorDraft() {
		return pengalamanVendorDraft;
	}
	public void setPengalamanVendorDraft(PengalamanVendorDraft pengalamanVendorDraft) {
		this.pengalamanVendorDraft = pengalamanVendorDraft;
	}
	

}
