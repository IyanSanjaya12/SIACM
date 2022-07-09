package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;


import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalTahapan;
import id.co.promise.procurement.entity.ApprovalTahapanDetail;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Tahapan;

public class ApprovalTahapanDTO {
	
	private Tahapan tahapan;
	
	private Organisasi organisasi;
	
	private Organisasi unit;
	
	private String tipe;
	
	/* private List<Approval> approvalList; */
	
	private List<ApprovalTahapanDetail> approvalTahapanDetailList;
	
	private Integer isDelete; 
	
	private Date created;
	
	private Integer id; 

	
	public Organisasi getUnit() {
		return unit;
	}

	public void setUnit(Organisasi unit) {
		this.unit = unit;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

	public List<ApprovalTahapanDetail> getApprovalTahapanDetailList() {
		return approvalTahapanDetailList;
	}

	public void setApprovalTahapanDetailList(List<ApprovalTahapanDetail> approvalTahapanDetailList) {
		this.approvalTahapanDetailList = approvalTahapanDetailList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Tahapan getTahapan() {
		return tahapan;
	}

	public void setTahapan(Tahapan tahapan) {
		this.tahapan = tahapan;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	/*
	 * public List<Approval> getApprovalList() { return approvalList; }
	 * 
	 * public void setApprovalList(List<Approval> approvalList) { this.approvalList
	 * = approvalList; }
	 */

}
