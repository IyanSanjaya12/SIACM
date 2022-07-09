package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_PURCHASE_REQUEST")
@NamedQueries({ @NamedQuery(name = "PurchaseRequest.find", query = "SELECT x FROM PurchaseRequest x WHERE x.isDelete = 0"),
		@NamedQuery(name = "PurchaseRequest.getPRListByStatusList", query = "SELECT x FROM PurchaseRequest x WHERE x.isDelete = 0 and x.status in (:statusList)"),
		@NamedQuery(name = "PurchaseRequest.getPRListByStatusListAndHavePrNumber", query = "SELECT x FROM PurchaseRequest x WHERE x.isDelete = 0 and x.status in (:statusList) and x.prnumber is not null"),
		@NamedQuery(name = "PurchaseRequest.findByPRNumber", query = "SELECT a FROM PurchaseRequest a WHERE  a.isDelete = 0 AND a.prnumber like :prnumber "
				+ "AND a.isDelete = 0"),
		@NamedQuery(name = "PurchaseRequest.countDataByPRNumber", query = "SELECT count(a) FROM PurchaseRequest a WHERE a.isDelete = 0 AND a.prnumber like :prnumber "
				+ "AND a.isDelete = 0"),
		@NamedQuery(name = "PurchaseRequest.countByStatus", query = "SELECT COUNT(pr) FROM PurchaseRequest pr WHERE pr.isDelete=0 AND pr.status=:status AND pr.userId=:userId AND pr.organisasi.id=:afcoId"),
		@NamedQuery(name = "PurchaseRequest.countByStatusList", query = "SELECT COUNT(pr) FROM PurchaseRequest pr WHERE pr.isDelete=0 AND pr.status IN (:status) AND pr.userId=:userId"),
		@NamedQuery(name = "PurchaseRequest.countAll", query = "SELECT COUNT(pr) FROM PurchaseRequest pr WHERE pr.isDelete=0  AND pr.userId=:userId AND pr.organisasi.id=:afcoId") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_PURCHASE_REQUEST", initialValue = 1, allocationSize = 1)
public class PurchaseRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PURCHASE_REQUEST_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID", nullable = true)
	private Organisasi organisasi;
	
	@ManyToOne
	@JoinColumn(name = "PURCH_ORG_ID", referencedColumnName = "PURCH_ORG_ID", nullable = true)
	private PurchOrg purchOrg;

	@Column(name = "DEPARTMENT")
	private String department;

	@Column(name = "PR_NUMBER_SAP")
	private String prnumber;

	@Column(name = "BO_NUMBER")
	private String boNumber;

	@Column(name = "STATUS_EBS")
	private String statusEbs;

	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_HARGA_DISCOUNT")
	private Double totalHargaDiscount;

	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_HARGA_ONGKIR")
	private Double totalHargaOngkir;

	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_HARGA_ASURANSI")
	private Double totalHargaAsuransi;

	@Column(name = "TERM_AND_CONDITION")
	private String termandcondition;

	@Column(name = "COST_CENTER")
	private String costcenter;

	@Column(name = "TOTAL_HARGA")
	private Double totalHarga;

	@Column(name = "TOTAL_COST_AFTER_APPROVE")
	private String totalCostAfterApprove;

	@Column(name = "DATEREQUIRED")
	private Date daterequired;

	@Column(name = "POSTDATE")
	private Date postdate;

	@Column(name = "NEXT_APPROVAL")
	private String nextapproval;

	@Column(name = "PROCUREMENT_PROCESS")
	private String procurementprocess;

	@Column(name = "DESCRIPTION")
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VERIFIED_DATE")
	private Date verifiedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVED_DATE")
	private Date approvedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "IS_JOIN")
	private Integer isJoin;

	@Column(name = "PR_ID_EBS")
	private Integer prIdEbs;

	@OneToOne
	@JoinColumn(name = "ORG_APPROVAL_PATH_ID", referencedColumnName = "ORG_APPROVAL_PATH_ID", nullable = true)
	private OrgApprovalPath orgApprovalPath;

	@Column(name = "LINK_LAMPIRAN_PR")
	private String linkLampiranPr;

	@Column(name = "LINK_LAMPIRAN_KONTRAK")
	private String linkLampiranKontrak;

//	@Column(name = "SLA_DELIVERY_TIME")
//	private Integer slaDeliveryTime;

	@Column(name = "REQUESTOR_USER_ID")
	private Integer requestorUserId;
	
	@Column(name = "INTERFACING_NOTES")
	@Lob
	private String interfacingNotes;
	
	@Column(name = "PUSPEL_CODE")
	private String puspelCode;
	
	@Column(name = "GV_DOCTYPE")
	private String gvDoctype;

	@Column(name = "GV_HEADERNOTE")
	private String gvHeadnote;

	@Column(name = "GV_INTERMSOF")
	private String gvIntermsoft;
	
	@Column(name = "GV_ATTACHMENT")
	private String gvAttachment;
	
	@Column(name = "REQUISITIONER")
	private String gvRequisitioner;
	
	@Column(name = "TESTRUN")
	private String gvTestRun;

	@Transient
	private Boolean isAvailable;
	
	@Transient
	private Integer rootParent;

	public Integer getId() {
		return id;
	}

	public Double getTotalHargaDiscount() {
		return totalHargaDiscount;
	}

	public void setTotalHargaDiscount(Double totalHargaDiscount) {
		this.totalHargaDiscount = totalHargaDiscount;
	}

	public Double getTotalHargaOngkir() {
		return totalHargaOngkir;
	}

	public void setTotalHargaOngkir(Double totalHargaOngkir) {
		this.totalHargaOngkir = totalHargaOngkir;
	}

	public Double getTotalHargaAsuransi() {
		return totalHargaAsuransi;
	}

	public void setTotalHargaAsuransi(Double totalHargaAsuransi) {
		this.totalHargaAsuransi = totalHargaAsuransi;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPrnumber() {
		return prnumber;
	}

	public void setPrnumber(String prnumber) {
		this.prnumber = prnumber;
	}

	public String getBoNumber() {
		return boNumber;
	}

	public void setBoNumber(String boNumber) {
		this.boNumber = boNumber;
	}

	public String getStatusEbs() {
		return statusEbs;
	}

	public void setStatusEbs(String statusEbs) {
		this.statusEbs = statusEbs;
	}

	public String getTermandcondition() {
		return termandcondition;
	}

	public void setTermandcondition(String termandcondition) {
		this.termandcondition = termandcondition;
	}

	public String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(String costcenter) {
		this.costcenter = costcenter;
	}

	public Double getTotalHarga() {
		return totalHarga;
	}

	public void setTotalHarga(Double totalHarga) {
		this.totalHarga = totalHarga;
	}

	public String getTotalCostAfterApprove() {
		return totalCostAfterApprove;
	}

	public void setTotalCostAfterApprove(String totalCostAfterApprove) {
		this.totalCostAfterApprove = totalCostAfterApprove;
	}

	public Date getDaterequired() {
		return daterequired;
	}

	public void setDaterequired(Date daterequired) {
		this.daterequired = daterequired;
	}

	public Date getPostdate() {
		return postdate;
	}

	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}

	public String getNextapproval() {
		return nextapproval;
	}

	public void setNextapproval(String nextapproval) {
		this.nextapproval = nextapproval;
	}

	public String getProcurementprocess() {
		return procurementprocess;
	}

	public void setProcurementprocess(String procurementprocess) {
		this.procurementprocess = procurementprocess;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(Integer isJoin) {
		this.isJoin = isJoin;
	}

	public Integer getPrIdEbs() {
		return prIdEbs;
	}

	public void setPrIdEbs(Integer prIdEbs) {
		this.prIdEbs = prIdEbs;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public OrgApprovalPath getOrgApprovalPath() {
		return orgApprovalPath;
	}

	public void setOrgApprovalPath(OrgApprovalPath orgApprovalPath) {
		this.orgApprovalPath = orgApprovalPath;
	}

	public String getLinkLampiranPr() {
		return linkLampiranPr;
	}

	public void setLinkLampiranPr(String linkLampiranPr) {
		this.linkLampiranPr = linkLampiranPr;
	}

	public String getLinkLampiranKontrak() {
		return linkLampiranKontrak;
	}

//	public Integer getSlaDeliveryTime() {
//		return slaDeliveryTime;
//	}
//
//	public void setSlaDeliveryTime(Integer slaDeliveryTime) {
//		this.slaDeliveryTime = slaDeliveryTime;
//	}

	public void setLinkLampiranKontrak(String linkLampiranKontrak) {
		this.linkLampiranKontrak = linkLampiranKontrak;
	}

	public Integer getRequestorUserId() {
		return requestorUserId;
	}

	public void setRequestorUserId(Integer requesttorUserId) {
		this.requestorUserId = requesttorUserId;
	}
	
	public Integer getRootParent() {
		return rootParent;
	}

	public void setRootParent(Integer rootParent) {
		this.rootParent = rootParent;
	}

	public String getInterfacingNotes() {
		return interfacingNotes;
	}

	public void setInterfacingNotes(String interfacingNotes) {
		this.interfacingNotes = interfacingNotes;
	}

	public String getPuspelCode() {
		return puspelCode;
	}

	public void setPuspelCode(String puspelCode) {
		this.puspelCode = puspelCode;
	}
	
	

	public String getGvDoctype() {
		return gvDoctype;
	}

	public void setGvDoctype(String gvDoctype) {
		this.gvDoctype = gvDoctype;
	}

	public String getGvHeadnote() {
		return gvHeadnote;
	}

	public void setGvHeadnote(String gvHeadnote) {
		this.gvHeadnote = gvHeadnote;
	}

	public String getGvIntermsoft() {
		return gvIntermsoft;
	}

	public void setGvIntermsoft(String gvIntermsoft) {
		this.gvIntermsoft = gvIntermsoft;
	}

	public String getGvAttachment() {
		return gvAttachment;
	}

	public void setGvAttachment(String gvAttachment) {
		this.gvAttachment = gvAttachment;
	}
	
	public String getGvRequisitioner() {
		return gvRequisitioner;
	}

	public void setGvRequisitioner(String gvRequisitioner) {
		this.gvRequisitioner = gvRequisitioner;
	}

	public String getGvTestRun() {
		return gvTestRun;
	}

	public void setGvTestRun(String gvTestRun) {
		this.gvTestRun = gvTestRun;
	}
	

	public PurchOrg getPurchOrg() {
		return purchOrg;
	}

	public void setPurchOrg(PurchOrg purchOrg) {
		this.purchOrg = purchOrg;
	}

	@Override
	public String toString() {
		return "PurchaseRequest [id=" + id + ", organisasi=" + organisasi + ", department=" + department + ", prnumber=" + prnumber
				+ ", termandcondition=" + termandcondition + ", costcenter=" + costcenter + ", totalcost=" + totalHarga + ", daterequired="
				+ daterequired + ", postdate=" + postdate + ", nextapproval=" + nextapproval + ", procurementprocess=" + procurementprocess
				+ ", description=" + description + ", created=" + created + ", updated=" + updated + ", deleted=" + deleted + ", userId="
				+ userId + ", isDelete=" + isDelete + ", status=" + status + "]";
	}


	

}
