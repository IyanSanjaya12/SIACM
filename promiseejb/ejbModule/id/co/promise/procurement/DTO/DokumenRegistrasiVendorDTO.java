package id.co.promise.procurement.DTO;

import java.util.List;

import id.co.promise.procurement.entity.DokumenRegistrasiVendorDraft;
import id.co.promise.procurement.entity.VendorSKD;
import id.co.promise.procurement.entity.VendorSKDDraft;


public class DokumenRegistrasiVendorDTO {
	private Integer loginId;
	private List <DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList;
	private VendorSKDDraft vendorSKDDraft;
	private VendorSKD vendorSKD;
	private DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft;
	private String toDo;
	private Integer status;
	
	
	public Integer getLoginId() {
		return loginId;
	}
	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}
	public List<DokumenRegistrasiVendorDraft> getDokumenRegistrasiVendorDraftList() {
		return dokumenRegistrasiVendorDraftList;
	}
	public void setDokumenRegistrasiVendorDraftList(
			List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList) {
		this.dokumenRegistrasiVendorDraftList = dokumenRegistrasiVendorDraftList;
	}
	public VendorSKDDraft getVendorSKDDraft() {
		return vendorSKDDraft;
	}
	public void setVendorSKDDraft(VendorSKDDraft vendorSKDDraft) {
		this.vendorSKDDraft = vendorSKDDraft;
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
	public DokumenRegistrasiVendorDraft getDokumenRegistrasiVendorDraft() {
		return dokumenRegistrasiVendorDraft;
	}
	public void setDokumenRegistrasiVendorDraft(
			DokumenRegistrasiVendorDraft dokumenRegistrasiVendorDraft) {
		this.dokumenRegistrasiVendorDraft = dokumenRegistrasiVendorDraft;
	}
	public VendorSKD getVendorSKD() {
		return vendorSKD;
	}
	public void setVendorSKD(VendorSKD vendorSKD) {
		this.vendorSKD = vendorSKD;
	}
	
	
}
