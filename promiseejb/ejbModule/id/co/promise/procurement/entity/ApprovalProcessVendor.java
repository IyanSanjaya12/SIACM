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

@Entity
@NamedQueries({
    @NamedQuery(name="ApprovalProcessVendor.find", query="SELECT approvalProcessVendor FROM ApprovalProcessVendor approvalProcessVendor WHERE "
    		+ "approvalProcessVendor.isDelete = 0 AND approvalProcessVendor.status != 1 AND approvalProcessVendor.purchaseRequestId =:purchaseRequest"),
    @NamedQuery(name="ApprovalProcessVendor.findByStatus", query="SELECT approvalProcessVendor FROM ApprovalProcessVendor approvalProcessVendor WHERE "
    		+ "approvalProcessVendor.isDelete = 0 AND approvalProcessVendor.status = 1 AND approvalProcessVendor.user.id =:userId AND "
    		+ "approvalProcessVendor.purchaseRequestId =:purchaseRequest"),
    @NamedQuery(name="ApprovalProcessVendor.findByPurchaseRequestId", query="SELECT approvalProcessVendor FROM ApprovalProcessVendor approvalProcessVendor WHERE "
    		+ "approvalProcessVendor.isDelete = 0 AND approvalProcessVendor.status != 1 AND approvalProcessVendor.purchaseRequestId =:valueId"),
    @NamedQuery(name="ApprovalProcessVendor.getApprovalProcessVendorByPurchaseRequestId", query="SELECT approvalProcessVendor FROM ApprovalProcessVendor approvalProcessVendor WHERE " 
    		+ "approvalProcessVendor.isDelete = 0 AND approvalProcessVendor.purchaseRequestId =:purchaseRequestId")
})
@Table(name = "T3_APPROVAL_PROCESS_VENDOR")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_APPROVAL_PROCESS_VENDOR", initialValue = 1, allocationSize = 1)
public class ApprovalProcessVendor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPROVAL_PROCESS_VENDOR_ID")
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;
	
	@Column(name="ISDELETE")
	private Integer isDelete;
	
	@Column(name="USERID")
	private Integer userId;

	@Column(name = "KETERANGAN")
	private String keterangan;
	
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;
	
	@Column(name = "PURCHASE_REQUEST_ID")
	private Integer purchaseRequestId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getPurchaseRequestId() {
		return purchaseRequestId;
	}

	public void setPurchaseRequestId(Integer purchaseRequestId) {
		this.purchaseRequestId = purchaseRequestId;
	}
	
}
