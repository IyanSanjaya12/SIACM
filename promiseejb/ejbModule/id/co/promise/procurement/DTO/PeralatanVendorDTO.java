package id.co.promise.procurement.DTO;

import id.co.promise.procurement.entity.PeralatanVendor;
import id.co.promise.procurement.entity.PeralatanVendorDraft;

public class PeralatanVendorDTO {
	private PeralatanVendorDraft peralatanVendorDraft;
	private PeralatanVendor peralatanVendor;
	private String toDo;
	private Integer status;
	
	private Integer loginId;
	public PeralatanVendorDraft getPeralatanVendorDraft() {
		return peralatanVendorDraft;
	}
	public void setPeralatanVendorDraft(PeralatanVendorDraft peralatanVendorDraft) {
		this.peralatanVendorDraft = peralatanVendorDraft;
	}
	public PeralatanVendor getPeralatanVendor() {
		return peralatanVendor;
	}
	public void setPeralatanVendor(PeralatanVendor peralatanVendor) {
		this.peralatanVendor = peralatanVendor;
	}
	public String getToDo() {
		return toDo;
	}
	public void setToDo(String toDo) {
		this.toDo = toDo;
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
	
	
	
}
