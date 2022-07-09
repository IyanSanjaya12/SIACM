package id.co.promise.procurement.entity;

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


@Entity
@Table(name = "T6_DR_DETAIL")
@NamedQueries({
    @NamedQuery(name="DeliveryReceivedDetail.find", query="SELECT x FROM DeliveryReceivedDetail x WHERE x.isDelete = 0"),
    @NamedQuery(name="DeliveryReceivedDetail.findByPOLineId", query="SELECT drd FROM DeliveryReceivedDetail drd "
    		+ "WHERE drd.isDelete = 0 AND drd.deliveryReceived.id = :deliveryReceivedId AND"
    		+ " drd.poLineId = :poLineId"),
    @NamedQuery(name="DeliveryReceivedDetail.findByDeliveryId", query="SELECT drd FROM DeliveryReceivedDetail drd "
    		+ "WHERE drd.isDelete = 0 AND drd.deliveryReceived.id = :deliveryReceivedId")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T6_DR_DETAIL", initialValue = 1, allocationSize = 1)
public class DeliveryReceivedDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DR_DETAIL_ID")
	private Integer id;

	@Column(name = "PO_LINE_ID")
	private Integer poLineId;
	
	@Column(name = "QUANTITY")
	private Integer quantity;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "DELIVERY_RECEIVED_ID", referencedColumnName = "DELIVERY_RECEIVED_ID")
	private DeliveryReceived deliveryReceived;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;
	
	@Column(name="ISDELETE")
	private Integer isDelete;
	
	@Column(name="USERID")
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public DeliveryReceived getDeliveryReceived() {
		return deliveryReceived;
	}

	public void setDeliveryReceived(DeliveryReceived deliveryReceived) {
		this.deliveryReceived = deliveryReceived;
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
