/**
 * fdf
 */
package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

/**
 * @author User
 *
 */

@Entity
@Table(name = "T3_PURCHASE_ORDER")
@NamedQueries({ @NamedQuery(name = "PurchaseOrder.find", query = "SELECT x FROM PurchaseOrder x WHERE x.isDelete = 0"),
		@NamedQuery(name = "PurchaseOrder.getPOListByStatusList", query = "SELECT x FROM PurchaseOrder x WHERE x.isDelete = 0 and x.statusProses in (:statusList)"),
		@NamedQuery(name = "PurchaseOrder.countPOByStatusAndOrg", query = "SELECT COUNT(o) FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.status=:status AND o.purchaseRequest.organisasi.id IN :organisasiIDNewList"),
		@NamedQuery(name = "PurchaseOrder.getPOByPRId", query = "SELECT o FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.purchaseRequest.id=:prId"),
		@NamedQuery(name = "PurchaseOrder.countPOByStatus", query = "SELECT COUNT(o) FROM PurchaseOrder o WHERE o.isDelete = 0 AND o.status=:status"),
		@NamedQuery(name = "PurchaseOrder.getSumPerMonth", query = " SELECT coalesce(sum(o.purchaseOrder.totalCost), 0) FROM DeliveryReceived o WHERE o.isDelete = 0 AND "
				+ " (o.purchaseOrder.created between :startDate and :endDate) "),
		@NamedQuery(name = "PurchaseOrder.getCountByDate", query = " SELECT Count(o) FROM PurchaseOrder o WHERE o.isDelete = 0 AND "
				+ " (o.purchaseOrderDate between :startDate and :endDate) and o.poNumber is not null "),
		@NamedQuery(name = "PurchaseOrder.getCountByDateAndOrganizationId", query = " SELECT Count(o) FROM PurchaseOrder o WHERE o.isDelete = 0 AND "
				+ " (o.purchaseOrderDate between :startDate and :endDate) and o.poNumber is not null And o.purchaseRequest.organisasi.id =:organizationId "),
		@NamedQuery(name = "PurchaseOrder.findByPONumberEbs", query = "SELECT purchaseOrder FROM PurchaseOrder purchaseOrder WHERE purchaseOrder.poNumber =:poNumber AND purchaseOrder.isDelete = 0"),
		@NamedQuery(name = "PurchaseOrder.getPOByPRIdEbs" , query = "SELECT x FROM PurchaseOrder x WHERE x.purchaseRequest.prIdEbs =:prIdEbs AND x.isDelete = 0") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_PURCHASE_ORDER", initialValue = 1, allocationSize = 1)
public class PurchaseOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PURCHASE_ORDER_ID")
	private Integer id;

	@Column(name = "PO_ID_EBS")
	private Integer poIdEbs;

	@Column(name = "PO_NUMBER_SAP")
	private String poNumber;

	@OneToOne
	@JoinColumn(name = "PURCHASE_REQUEST_ID", referencedColumnName = "PURCHASE_REQUEST_ID")
	private PurchaseRequest purchaseRequest;

	@OneToOne
	@JoinColumn(name = "ADDRESS_BOOK_ID", referencedColumnName = "ADDRESS_BOOK_ID", nullable = true)
	private AddressBook addressBook;

	@OneToOne
	@JoinColumn(name = "PENGADAAN_ID", referencedColumnName = "PENGADAAN_ID", nullable = true)
	private Pengadaan pengadaan;

	@OneToOne
	@JoinColumn(name = "TERM_AND_CONDITION_ID", referencedColumnName = "TERM_AND_CONDITION_ID")
	private TermAndCondition termandcondition;

	@Column(name = "DEPARTMENT")
	private String department;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "STREET_ADDRESS")
	private String streetAddress;

	@Column(name = "ADDRESS_LABEL")
	private String addressLabel;

	@Column(name = "TELEPHONE_1")
	private String telephone1;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "PURCHASE_ORDER_DATE")
	private Date purchaseOrderDate;

	@Column(name = "RATING_DATE")
	private Date ratingDate;

	@Lob
	@Column(name = "NOTES")
	private String notes;

	@ColumnDefault( value = "0" )
	@Column(name = "DOWN_PAYMENT")
	private Double downPayment;

	@ColumnDefault( value = "0" )
	@Column(name = "DISCOUNT")
	private Double discount;

	@ColumnDefault( value = "0" )
	@Column(name = "TAX")
	private Double tax;

	@ColumnDefault( value = "0" )
	@Column(name = "SUB_TOTAL")
	private Double subTotal;

	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_COST")
	private Double totalCost;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "STATUS_EBS")
	private String statusEbs;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	@Column(name = "NEXT_APPROVAL")
	private String nextapproval;

	/**
	 * 0. Dihapus 1. Baru Dibuat 2. Sudah Diedit 3. Proses Approval 4. Hold 5.
	 * Reject 6. On Process 7. Done
	 */

	@ColumnDefault( value = "1" )
	@Column(name = "STATUS_PROSES")
	private Integer statusProses;

	@Column(name = "KONTRAK_FK")
	private Integer kontrakFk;

	@Column(name = "TERMIN_FK")
	private Integer terminFk;

	@OneToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID", nullable = true)
	private MataUang mataUang;

	/**
	 * 0 = belum direquest 1 = sukses 2 = gagal
	 */
	@ColumnDefault( value = "0" )
	@Column(name = "SYNC_STATUS")
	private Integer syncStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVED_DATE")
	private Date approvedDate;

	@Column(name = "VENDOR_NAME")
	private String vendorName;

	@Column(name = "DELIVERY_TIME")
	private Date deliveryTime;

	@Column(name = "RATING")
	private Integer rating;

	@Column(name = "KOMEN")
	private String komen;
	
	@Column(name = "REQUESTOR_USER_ID")
	private Integer requestorUserId;

	@Transient
	private Boolean isAvailable;

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public Date getRatingDate() {
		return ratingDate;
	}

	public void setRatingDate(Date ratingDate) {
		this.ratingDate = ratingDate;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	@Transient
	private Integer approvalId;

	public Integer getStatusProses() {
		return statusProses;
	}

	public void setStatusProses(Integer statusProses) {
		this.statusProses = statusProses;
	}

	public String getNextapproval() {
		return nextapproval;
	}

	public void setNextapproval(String nextapproval) {
		this.nextapproval = nextapproval;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public PurchaseRequest getPurchaseRequest() {
		return purchaseRequest;
	}

	public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
		this.purchaseRequest = purchaseRequest;
	}

	public AddressBook getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	}

	public Pengadaan getPengadaan() {
		return pengadaan;
	}

	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}

	public TermAndCondition getTermandcondition() {
		return termandcondition;
	}

	public void setTermandcondition(TermAndCondition termandcondition) {
		this.termandcondition = termandcondition;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	public void setPurchaseOrderDate(Date purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	public Double getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(Double downPayment) {
		this.downPayment = downPayment;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getAddressLabel() {
		return addressLabel;
	}

	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public Integer getKontrakFk() {
		return kontrakFk;
	}

	public void setKontrakFk(Integer kontrakFk) {
		this.kontrakFk = kontrakFk;
	}

	public Integer getTerminFk() {
		return terminFk;
	}

	public void setTerminFk(Integer terminFk) {
		this.terminFk = terminFk;
	}

	public Integer getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Integer approvalId) {
		this.approvalId = approvalId;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getPoIdEbs() {
		return poIdEbs;
	}

	public String getStatusEbs() {
		return statusEbs;
	}

	public void setStatusEbs(String statusEbs) {
		this.statusEbs = statusEbs;
	}

	public Integer getRating() {
		return rating;
	}

	public void setPoIdEbs(Integer poIdEbs) {
		this.poIdEbs = poIdEbs;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getKomen() {
		return komen;
	}

	public void setKomen(String komen) {
		this.komen = komen;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Integer getRequestorUserId() {
		return requestorUserId;
	}

	public void setRequestorUserId(Integer requestorUserId) {
		this.requestorUserId = requestorUserId;
	}

}
