/*
* File: ApprovalProcessDTO.java
* This class is created to handle all processing involved
* in a shopping cart.
*
* Copyright (c) 2017 Mitra Mandiri Informatika
* Jl. Tebet Raya no. 11 B Jakarta Selatan
* All Rights Reserved.
*
* This software is the confidential and proprietary
* information of Mitra Mandiri Informatika ("Confidential
* Information").
*
* You shall not disclose such Confidential Information and
* shall use it only in accordance with the terms of the
* license agreement you entered into with MMI.
*
* Date 				Author 			Version 	Changes
* Jan 24, 2017 7:19:27 PM 	Mamat 		1.0 		Created
* method
*/
package id.co.promise.procurement.approval;

import java.util.List;



import id.co.promise.procurement.entity.ApprovalProcess;
import id.co.promise.procurement.entity.DokumenRegistrasiVendorDraft;
import id.co.promise.procurement.entity.VendorSKDDraft;

/**
 * @author Mamat
 *
 */
public class ApprovalProcessDTO {
	
	private Integer id;
	private String note;
	private Integer status;
	private Integer approvalProcessId;
	
	private ApprovalProcess approvalProcess;
	private String keteranganIndex;
	
	List<DokumenRegistrasiVendorDraft> dokumenRegistrasiVendorDraftList;
	VendorSKDDraft vendorSKDDraft;
	/**
	 * @return the approvalProcess
	 */
	public ApprovalProcess getApprovalProcess() {
		return approvalProcess;
	}
	/**
	 * @param approvalProcess the approvalProcess to set
	 */
	public void setApprovalProcess(ApprovalProcess approvalProcess) {
		this.approvalProcess = approvalProcess;
	}
	/**
	 * @return the keteranganIndex
	 */
	public String getKeteranganIndex() {
		return keteranganIndex;
	}
	/**
	 * @param keteranganIndex the keteranganIndex to set
	 */
	public void setKeteranganIndex(String keteranganIndex) {
		this.keteranganIndex = keteranganIndex;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getApprovalProcessId() {
		return approvalProcessId;
	}
	public void setApprovalProcessId(Integer approvalProcessId) {
		this.approvalProcessId = approvalProcessId;
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
	
	
}
