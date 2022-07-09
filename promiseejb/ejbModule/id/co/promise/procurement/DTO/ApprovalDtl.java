package id.co.promise.procurement.DTO;

import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.Organisasi;

import java.util.List;

public class ApprovalDtl {
	
	private Approval approval;
	
	private List<ApprovalLevel> approvalLevelList;
	
	private Organisasi organisasi;

	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
	}

	public List<ApprovalLevel> getApprovalLevelList() {
		return approvalLevelList;
	}

	public void setApprovalLevelList(List<ApprovalLevel> approvalLevelList) {
		this.approvalLevelList = approvalLevelList;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

}
