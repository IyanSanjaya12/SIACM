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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

/**
 * @author User
 *
 */
@Entity
@Table(name = "T5_PURCHASE_ORDER_TERM")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_PURCHASE_ORDER_TERM", initialValue = 1, allocationSize = 1)
public class PurchaseOrderTerm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PO_TERM_ID")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "PURCHASE_ORDER_ID", referencedColumnName = "PURCHASE_ORDER_ID", nullable = true)
	private PurchaseOrder purchaseOrder;

	@ManyToOne
	@JoinColumn(name = "TERM_AND_CONDITION_ID", referencedColumnName = "TERM_AND_CONDITION_ID")
	private TermAndCondition termandcondition;
	
	@ColumnDefault( value = "0" )
	@Column(name = "PO_TERM_VALUE")
	private Double poTermValue;

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
	
	/**
	 * 1 = percentage
	 * 2 = value
	 */
	@ColumnDefault( value = "2" )
	@Column(name = "PO_TERM_TYPE")
	private Integer poTermType;

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

	public TermAndCondition getTermandcondition() {
		return termandcondition;
	}

	public void setTermandcondition(TermAndCondition termandcondition) {
		this.termandcondition = termandcondition;
	}

	public Double getPoTermValue() {
		return poTermValue;
	}

	public void setPoTermValue(Double poTermValue) {
		this.poTermValue = poTermValue;
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

	public Integer getPoTermType() {
		return poTermType;
	}

	public void setPoTermType(Integer poTermType) {
		this.poTermType = poTermType;
	}
	
	
}
