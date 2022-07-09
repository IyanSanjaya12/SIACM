package id.co.promise.procurement.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_RFI")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_RFI", initialValue = 1, allocationSize = 1)
public class RFI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RFI_ID")
	private Integer id;
	
	@Column(name = "RFI_NO")
	private String noRFI;
	
	@OneToOne
	@JoinColumn(name = "PURCHASE_REQUEST_ID")
	private PurchaseRequest purchaseRequest;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DELIVERY_QUOTE_DATE")
	private Date deliveryQuoteDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "POST_DATE")
	private Date postDate;
	
	@Column(name = "CONTACT_PERSON_NAME")
	private String contactPersonName;
	
	@Column(name = "CONTACT_PERSON_EMAIL")
	private String contactPersonEmail;
	
	@Column(name = "CONTACT_PERSON_PHONE")
	private String contactPersonPhone;
	
	@Column(name = "TERM_CONDITION", columnDefinition="VARCHAR(2000)", length = 2000)
	private String termCondition;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private RFIStatus rfiStatus;

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
	
	//fetch = FetchType.EAGER, cascade = CascadeType.ALL
	@OneToMany()
	@JoinColumn(name = "RFI_ID")
	private Set<RFIVendor> rfiVendorList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PurchaseRequest getPurchaseRequest() {
		return purchaseRequest;
	}

	public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
		this.purchaseRequest = purchaseRequest;
	}

	public Date getDeliveryQuoteDate() {
		return deliveryQuoteDate;
	}

	public void setDeliveryQuoteDate(Date deliveryQuoteDate) {
		this.deliveryQuoteDate = deliveryQuoteDate;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonEmail() {
		return contactPersonEmail;
	}

	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}

	public String getContactPersonPhone() {
		return contactPersonPhone;
	}

	public void setContactPersonPhone(String contactPersonPhone) {
		this.contactPersonPhone = contactPersonPhone;
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

	public String getTermCondition() {
		return termCondition;
	}

	public void setTermCondition(String termCondition) {
		this.termCondition = termCondition;
	}

	public String getNoRFI() {
		return noRFI;
	}

	public void setNoRFI(String noRFI) {
		this.noRFI = noRFI;
	}

	public RFIStatus getRfiStatus() {
		return rfiStatus;
	}

	public void setRfiStatus(RFIStatus rfiStatus) {
		this.rfiStatus = rfiStatus;
	}

	public Set<RFIVendor> getRfiVendorList() {
		return rfiVendorList;
	}

	public void setRfiVendorList(Set<RFIVendor> rfiVendorList) {
		this.rfiVendorList = rfiVendorList;
	}
	
}
