package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T5_APPROVAL_TAHAPAN_DTL")
@NamedQueries({
		@NamedQuery(name = "ApprovalTahapanDtl.getList", query = "SELECT approvalTahapan FROM ApprovalTahapanDetail approvalTahapan WHERE approvalTahapan.isDelete = 0"),
		@NamedQuery(name = "ApprovalTahapanDetail.getListByApprovalTahapanId", query = "SELECT approvalTahapanDetail FROM ApprovalTahapanDetail approvalTahapanDetail WHERE approvalTahapanDetail.approvalTahapan.id =:id AND approvalTahapanDetail.isDelete = 0"),
		@NamedQuery(name = "ApprovalTahapanDetail.deleteByApprovalTahapanId", query = "update ApprovalTahapanDetail approvalTahapanDetail set approvalTahapanDetail.isDelete = 1 WHERE approvalTahapanDetail.approvalTahapan.id =:approvalTahapanId ")
		
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_APPROVAL_TAHAPAN_DTL", initialValue = 1, allocationSize = 1)
public class ApprovalTahapanDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPROVAL_TAHAPAN_DTL_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "APPROVAL_ID", referencedColumnName = "APPROVAL_ID")
	private Approval approval;
	
	@ManyToOne
	@JoinColumn(name="APPROVAL_TAHAPAN_ID", referencedColumnName="APPROVAL_TAHAPAN_ID")
	private ApprovalTahapan approvalTahapan;
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@ManyToOne
	@JoinColumn(name="THRESHOLD_ID", referencedColumnName="THRESHOLD_ID")
	private Threshold threshold;
	
	@Column(name = "TYPE")
	private Integer tipe;
	
	@Column(name = "END_RANGE")
	private Double endRange;
	
	public Integer getTipe() {
		return tipe;
	}

	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
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

	public ApprovalTahapan getApprovalTahapan() {
		return approvalTahapan;
	}

	public void setApprovalTahapan(ApprovalTahapan approvalTahapan) {
		this.approvalTahapan = approvalTahapan;
	}

	public Threshold getThreshold() {
		return threshold;
	}

	public void setThreshold(Threshold threshold) {
		this.threshold = threshold;
	}

	public Double getEndRange() {
		return endRange;
	}

	public void setEndRange(Double endRange) {
		this.endRange = endRange;
	}
	
}


