package id.co.promise.procurement.approval;

import id.co.promise.procurement.entity.Approval;
import id.co.promise.procurement.entity.ApprovalLevel;
import id.co.promise.procurement.entity.ApprovalType;

import java.io.Serializable;

public class MasterApprovalDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer approvalId;
	private String approvalName;
	// private ApprovalType approvalType;
	// private Date approvalCreated;
	// private Date approvalUpdated;
	// private Date approvalDeleted;
	// private Integer approvalIsDelete;
	private Integer approvalTypeId;
	private String approvalTypeName;
	private Integer approvalLevelId;
	private Integer approvalLevelGroupId;
	private String approvalLevelGroupName;
	// private User approvalLevelUser;
	private Integer approvalLevelUserId;
	private String approvalLevelUserName;
	private Integer approvalLevelSequence;
	private Double approvalLevelThreshold;

	public MasterApprovalDTO(Approval approval, ApprovalType approvalType,
			ApprovalLevel approvalLevel) {
		this.approvalId = approval.getId();
		this.approvalName = approval.getName();
		this.approvalTypeId = approvalType.getId();
		this.approvalTypeName = approvalType.getName();
		this.approvalLevelId = approvalLevel.getId();
		this.approvalLevelUserId = approvalLevel.getUser().getId();
		this.approvalLevelUserName = approvalLevel.getUser().getNamaPengguna();
		this.approvalLevelSequence = approvalLevel.getSequence();
		this.approvalLevelThreshold = approvalLevel.getThreshold();
		this.approvalLevelGroupId = approvalLevel.getGroup().getId();
		this.approvalLevelGroupName = approvalLevel.getGroup().getNama();
	}

	public Integer getApprovalId() {
		return approvalId;
	}

	public String getApprovalName() {
		return approvalName;
	}

	public Integer getApprovalTypeId() {
		return approvalTypeId;
	}

	public String getApprovalTypeName() {
		return approvalTypeName;
	}

	public Integer getApprovalLevelId() {
		return approvalLevelId;
	}

	public Integer getApprovalLevelGroupId() {
		return approvalLevelGroupId;
	}

	public String getApprovalLevelGroupName() {
		return approvalLevelGroupName;
	}

	public Integer getApprovalLevelUserId() {
		return approvalLevelUserId;
	}

	public String getApprovalLevelUserName() {
		return approvalLevelUserName;
	}

	public Integer getApprovalLevelSequence() {
		return approvalLevelSequence;
	}

	public Double getApprovalLevelThreshold() {
		return approvalLevelThreshold;
	}

}
