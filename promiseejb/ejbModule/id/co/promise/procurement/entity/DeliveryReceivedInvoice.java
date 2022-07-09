package id.co.promise.procurement.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

/**
 * @author F.H.K
 *
 */
@Entity
@Table(name = "T6_DR_INVOICE")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T6_DR_INVOICE", initialValue = 1, allocationSize = 1)
public class DeliveryReceivedInvoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DELIVERY_RECEIVED_INVOICE_ID")
	private Integer id;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name = "PURCHASE_ORDER_ID", nullable = false)
	private PurchaseOrder purchaseOrder;
	
	@Column(name = "ATTACH_REAL_NAME")
	private String attachRealName; // nama file di server setelah encrypt

	@Column(name = "ATTACH_FILE_NAME")
	private String attachFileName; // nama file di server sebelum encrypt
	
	@Column(name = "ATTACH_FILE_SIZE")
	private Long attachFileSize;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_RECEIVED")
	private Date dateReceived;
	
	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

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

	public String getAttachRealName() {
		return attachRealName;
	}

	public void setAttachRealName(String attachRealName) {
		this.attachRealName = attachRealName;
	}

	public String getAttachFileName() {
		return attachFileName;
	}

	public void setAttachFileName(String attachFileName) {
		this.attachFileName = attachFileName;
	}

	public Long getAttachFileSize() {
		return attachFileSize;
	}

	public void setAttachFileSize(Long attachFileSize) {
		this.attachFileSize = attachFileSize;
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
}
