package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@NamedQueries({
	@NamedQuery(name="OrgApprovalPath.getByOrganisasiId", query="SELECT orgApprovalPath FROM OrgApprovalPath orgApprovalPath"
			+ " WHERE OrgApprovalPath.organisasi.id =:organisasiId and orgApprovalPath.isDelete = 0"),
	@NamedQuery(name="OrgApprovalPath.find", query="SELECT orgApprovalPath FROM OrgApprovalPath orgApprovalPath where orgApprovalPath.isDelete = 0"),
	@NamedQuery(name="OrgApprovalPath.getByAppPathId", query="SELECT orgApprovalPath FROM OrgApprovalPath orgApprovalPath where orgApprovalPath.isDelete = 0 "
			+ "And orgApprovalPath.approvalPathId = :approvalPathId"),
	@NamedQuery(name="OrgApprovalPath.getByAppPathName", query="SELECT orgApprovalPath FROM OrgApprovalPath orgApprovalPath where orgApprovalPath.isDelete = 0 "
			+ "And orgApprovalPath.approvalPathName = :approvalPathName")
})
@Table(name = "T2_ORG_APPROVAL_PATH")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_ORG_APPROVAL_PATH", initialValue = 1, allocationSize = 1)
public class OrgApprovalPath {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORG_APPROVAL_PATH_ID")
	private Integer id;
	
	@Column(name = "APPROVAL_PATH_ID")
	private Integer approvalPathId;

	@OneToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;
	
	@Column(name = "APPROVAL_PATH_NAME")
	private String approvalPathName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Column(name = "USERID")
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovalPathId() {
		return approvalPathId;
	}

	public void setApprovalPathId(Integer approvalPathId) {
		this.approvalPathId = approvalPathId;
	}

	public String getApprovalPathName() {
		return approvalPathName;
	}

	public void setApprovalPathName(String approvalPathName) {
		this.approvalPathName = approvalPathName;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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
	
	
}
