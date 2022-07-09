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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * @author User
 *
 */
@Entity
@Table(name = "T4_PURCHASE_ORDER_ITEM")
@NamedQueries({ @NamedQuery(name = "PurchaseOrderItem.find", query = "SELECT x FROM PurchaseOrderItem x WHERE x.isDelete = 0"),
				@NamedQuery(name = "PurchaseOrderItem.findByPOId", query = "SELECT x FROM PurchaseOrderItem x WHERE x.isDelete = 0 AND x.purchaseOrder.id=:purchaseOrderId"),
				@NamedQuery(name = "PurchaseOrderItem.findByNumberPO", query = "SELECT x FROM PurchaseOrderItem x WHERE x.isDelete = 0 AND x.purchaseOrder.poNumber=:poNumber")})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T4_PURCHASE_ORDER_ITEM", initialValue = 1, allocationSize = 1)
public class PurchaseOrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PURCHASE_ORDER_ITEM_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "PURCHASE_ORDER_ID", nullable = false)
	private PurchaseOrder purchaseOrder;
	
	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

	@Column(name = "VENDOR_NAME")
	private String vendorName;
	
	@OneToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	private Item item;
	
	@Column(name = "ITEM_NAME")
	private String itemName;
	
	@ColumnDefault( value = "0" )
	@Column(name = "QUANTITY_PURCHASE_REQUEST")
	private Double quantityPurchaseRequest;
	
	@ColumnDefault( value = "0" )
	@Column(name = "QUANTITY_SEND")
	private Double quantitySend;
	
	@ColumnDefault( value = "0" )
	@Column(name = "UNIT_PRICE")
	private Double unitPrice;
	
	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL_UNIT_PRICES")
	private Double totalUnitPrices;
	
	@Column(name = "DELIVERY_TIME")
	private Date deliveryTime;
	
	@Column(name = "STATUS")
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;
	
	@Column(name = "TAX_CODE")
	private String taxCode;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	/**
	 * 0. Dihapus
	 * 1. Baru Dibuat
	 * 2. Sudah Diedit
	 * 3. Proses Approval
	 * 4. Hold
	 * 5. Reject
	 * 6. On Process
	 * 7. Done
	 */
	
	@ColumnDefault( value = "1" )
	@Column(name = "STATUS_PROSES")
	private Integer statusProses;
	
	@ManyToOne()
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "SHIPPING_TO_ID", nullable=true)
	private ShippingTo shippingTo;
 
	@OneToOne
	@JoinColumn(name = "PURCHASE_REQUEST_ITEM_ID", nullable=true)
	private PurchaseRequestItem purchaseRequestItem;
	
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

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Double getQuantityPurchaseRequest() {
		return quantityPurchaseRequest;
	}

	public void setQuantityPurchaseRequest(Double quantityPurchaseRequest) {
		this.quantityPurchaseRequest = quantityPurchaseRequest;
	}

	public Double getQuantitySend() {
		return quantitySend;
	}

	public void setQuantitySend(Double quantitySend) {
		this.quantitySend = quantitySend;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getTotalUnitPrices() {
		return totalUnitPrices;
	}

	public void setTotalUnitPrices(Double totalUnitPrices) {
		this.totalUnitPrices = totalUnitPrices;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
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
	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getStatusProses() {
		return statusProses;
	}

	public void setStatusProses(Integer statusProses) {
		this.statusProses = statusProses;
	}

	public ShippingTo getShippingTo() {
		return shippingTo;
	}

	public void setShippingTo(ShippingTo shippingTo) {
		this.shippingTo = shippingTo;
	}

	public PurchaseRequestItem getPurchaseRequestItem() {
		return purchaseRequestItem;
	}

	public void setPurchaseRequestItem(PurchaseRequestItem purchaseRequestItem) {
		this.purchaseRequestItem = purchaseRequestItem;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	
	
}
