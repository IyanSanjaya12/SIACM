package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.SegmentasiVendor;
import id.co.promise.procurement.entity.SegmentasiVendorDraft;


public class SegmentasiVendorDTO {
	private SegmentasiVendor segmentasiVendor;
	private Integer loginId;
	private Integer status;
	private SegmentasiVendorDraft segmentasiVendorDraft;
	
	
	public SegmentasiVendor getSegmentasiVendor() {
		return segmentasiVendor;
	}
	public void setSegmentasiVendor(SegmentasiVendor segmentasiVendor) {
		this.segmentasiVendor = segmentasiVendor;
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
	public SegmentasiVendorDraft getSegmentasiVendorDraft() {
		return segmentasiVendorDraft;
	}
	public void setSegmentasiVendorDraft(SegmentasiVendorDraft segmentasiVendorDraft) {
		this.segmentasiVendorDraft = segmentasiVendorDraft;
	}
	
	
	

}

