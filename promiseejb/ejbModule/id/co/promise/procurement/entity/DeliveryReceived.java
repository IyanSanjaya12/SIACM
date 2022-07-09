package id.co.promise.procurement.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

/**
 * @author F.H.K
 *
 */
@Entity
@Table(name = "T5_DELIVERY_RECEIVED")
@NamedQueries({ @NamedQuery(name = "DeliveryReceived.getCountByDate", query = " SELECT Count(o) FROM DeliveryReceived o WHERE o.isDelete = 0 AND "
		+ " (o.created between :startDate and :endDate) "),
	 @NamedQuery(name = "DeliveryReceived.getCountByDateAndOrganizationId", query = " SELECT Count(o) FROM DeliveryReceived o WHERE o.isDelete = 0 AND "
				+ "(o.created between :startDate and :endDate) And purchaseOrder.purchaseRequest.organisasi.id =:organizationId ")

})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_DELIVERY_RECEIVED", initialValue = 1, allocationSize = 1)
public class DeliveryReceived implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DELIVERY_RECEIVED_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "PURCHASE_ORDER_ID", nullable = false)
	private PurchaseOrder purchaseOrder;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_RECEIVED")
	private Date dateReceived;
	
	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISFINISH")
	private Integer isFinish;
	
	@Column(name = "TOTAL_RECEIPT_AMOUNT")
	private Double totalReceiptAmount;
	
	@Column(name = "DELIVERY_RECEIPT_NUM")
	private String deliveryReceiptNum;
	
	@Column(name = "DESCRIPTION", columnDefinition="VARCHAR(2000)", length = 2000)
	private String description;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;
	
	@Column(name = "REQUESTOR_USER_ID")
	private Integer requestorUserId;
	
	@Column(name = "DELIVERY_ORDER_NUM")
	private String deliveryOrderNum;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELIVERY_ORDER_DATE")
	private Date deliveryOrderDate;
	
	@Transient
	private Double passLog;
	
	@Transient
	private Double failedLog;

	public String getDeliveryReceiptNum() {
		return deliveryReceiptNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDeliveryReceiptNum(String deliveryReceiptNum) {
		this.deliveryReceiptNum = deliveryReceiptNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
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

	public Integer getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}

	public Double getPassLog() {
		return passLog;
	}

	public void setPassLog(Double passLog) {
		this.passLog = passLog;
	}

	public Double getFailedLog() {
		return failedLog;
	}

	public void setFailedLog(Double failedLog) {
		this.failedLog = failedLog;
	}

	public Double getTotalReceiptAmount() {
		return totalReceiptAmount;
	}

	public void setTotalReceiptAmount(Double totalReceiptAmount) {
		this.totalReceiptAmount = totalReceiptAmount;
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

	public Integer getRequestorUserId() {
		return requestorUserId;
	}

	public void setRequestorUserId(Integer requestorUserId) {
		this.requestorUserId = requestorUserId;
	}

	public String getDeliveryOrderNum() {
		return deliveryOrderNum;
	}

	public void setDeliveryOrderNum(String deliveryOrderNum) {
		this.deliveryOrderNum = deliveryOrderNum;
	}

	public Date getDeliveryOrderDate() {
		return deliveryOrderDate;
	}

	public void setDeliveryOrderDate(Date deliveryOrderDate) {
		this.deliveryOrderDate = deliveryOrderDate;
	}
	
}
