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
@Table(name = "T4_RFQ_VENDOR")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T4_RFQ_VENDOR", initialValue = 1, allocationSize = 1)
public class RequestQuotationVendor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RFQ_VENDOR_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "POSTING_QUOTE")
	private Date postingQuote;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private RequestQuotationStatus requestQuotationStatus;

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
	@JoinColumn(name = "RFQ_VENDOR_ID")
	private Set<RequestQuotationItem> requestQuotationItemList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getPostingQuote() {
		return postingQuote;
	}

	public void setPostingQuote(Date postingQuote) {
		this.postingQuote = postingQuote;
	}

	public Set<RequestQuotationItem> getRequestQuotationItemList() {
		return requestQuotationItemList;
	}

	public void setRequestQuotationItemList(
			Set<RequestQuotationItem> requestQuotationItemList) {
		this.requestQuotationItemList = requestQuotationItemList;
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

	public RequestQuotationStatus getRequestQuotationStatus() {
		return requestQuotationStatus;
	}

	public void setRequestQuotationStatus(
			RequestQuotationStatus requestQuotationStatus) {
		this.requestQuotationStatus = requestQuotationStatus;
	}
	
}
