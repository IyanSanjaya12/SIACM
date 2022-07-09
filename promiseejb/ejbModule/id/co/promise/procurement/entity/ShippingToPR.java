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
@Table(name = "T5_SHIPPING_TO_PR")
@NamedQueries({
		@NamedQuery(name = "ShippingToPR.find", query = "SELECT x FROM ShippingToPR x WHERE x.isDelete = 0"),
		@NamedQuery(name = "ShippingToPR.findShippingByPR", query = "SELECT o FROM ShippingToPR o WHERE o.isDelete=0 "
				+ "AND o.purchaseRequestItem.purchaseRequestItem IN :prItemid ORDER BY o.shippingGroup, o.purchaseRequestItem"),
		@NamedQuery(name = "ShippingToPR.findShippingByPRItemId", query = "SELECT o FROM ShippingToPR o WHERE o.isDelete=0 AND o.purchaseRequestItem=:prItemId"),
		@NamedQuery(name = "ShippingToPR.findByPRNumber", query = "SELECT o FROM ShippingToPR o WHERE o.isDelete=0 AND o.prNumber=:prNumber") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_SHIPPING_TO_PR", initialValue = 1, allocationSize = 1)
public class ShippingToPR {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SHIPPING_TO_PR_ID")
	private Integer id;

	@Column(name = "SHIPPING_GROUP")
	private Integer shippingGroup;

	@Column(name = "PURCHASE_REQUEST_ITEM_ID", nullable = true)
	private Integer purchaseRequestItem;

	@ColumnDefault( value = "0" )
	@Column(name = "QUANTITY")
	private Double quantity;

	@OneToOne
	@JoinColumn(name = "ADDRESS_BOOK_ID", referencedColumnName = "ADDRESS_BOOK_ID", nullable = true)
	private AddressBook addressBook;

	@Column(name = "PR_NUMBER")
	private String prNumber;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "STREET_ADDRESS")
	private String streetAddress;

	@Column(name = "ADDRESS_LABEL")
	private String addressLabel;

	@Column(name = "TELEPHONE_1")
	private String telephone1;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELIVERY_TIME")
	private Date deliveryTime;

	@Transient
	private PurchaseRequestItem purchaseRequestItemObject;

	public PurchaseRequestItem getPurchaseRequestItemObject() {
		return purchaseRequestItemObject;
	}

	public void setPurchaseRequestItemObject(PurchaseRequestItem purchaseRequestItemObject) {
		this.purchaseRequestItemObject = purchaseRequestItemObject;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the purchaseRequestItem
	 */
	public Integer getPurchaseRequestItem() {
		return purchaseRequestItem;
	}

	/**
	 * @param purchaseRequestItem
	 *            the purchaseRequestItem to set
	 */
	public void setPurchaseRequestItem(Integer purchaseRequestItem) {
		this.purchaseRequestItem = purchaseRequestItem;
	}

	/**
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public AddressBook getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the streetAddress
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * @param streetAddress
	 *            the streetAddress to set
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * @return the addressLabel
	 */
	public String getAddressLabel() {
		return addressLabel;
	}

	/**
	 * @param addressLabel
	 *            the addressLabel to set
	 */
	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}

	/**
	 * @return the telephone1
	 */
	public String getTelephone1() {
		return telephone1;
	}

	/**
	 * @param telephone1
	 *            the telephone1 to set
	 */
	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
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

	/**
	 * @return the shippingGroup
	 */
	public Integer getShippingGroup() {
		return shippingGroup;
	}

	/**
	 * @param shippingGroup
	 *            the shippingGroup to set
	 */
	public void setShippingGroup(Integer shippingGroup) {
		this.shippingGroup = shippingGroup;
	}

	@Override
	public String toString() {
		return "ShippingToPR [id=" + id + ", shippingGroup=" + shippingGroup + ", purchaseRequestItem=" + purchaseRequestItem + ", quantity=" + quantity + ", addressBook=" + addressBook
				+ ", prNumber=" + prNumber + ", fullName=" + fullName + ", streetAddress=" + streetAddress + ", addressLabel=" + addressLabel + ", telephone1=" + telephone1 + ", created=" + created
				+ ", updated=" + updated + ", deleted=" + deleted + ", userId=" + userId + ", isDelete=" + isDelete + ", deliveryTime=" + deliveryTime + "]";
	}

}
