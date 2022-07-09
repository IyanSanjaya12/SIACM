package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_SALES_ORDER")
@NamedQueries({
	@NamedQuery(name = "SalesOrder.find", query = "SELECT a FROM SalesOrder a WHERE a.isDelete = 0")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_SALES_ORDER", initialValue = 1, allocationSize = 1)
public class SalesOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SALES_ORDER_ID")
	private Integer id;
	
	
	//dicomment soalnya desain ngg jelas bnget
	/*@Column(name="SO_NUMBER")
	private String soNumber;*/

	/*@OneToOne 
	@JoinColumn(name = "BILL_TO_ADDRESS_ID", referencedColumnName = "ADDRESS_BOOK_ID")
	private AddressBook addressBookBillTo;
	
	@OneToOne 
	@JoinColumn(name = "SHIP_TO_ADDRESS_ID", referencedColumnName = "ADDRESS_BOOK_ID")
	private AddressBook addressBookShipTo;*/
	
	@Column(name="BILL_TO_NAME")
	private String billToName;

	@Column(name="BILL_TO_ADDRESS")
	private String billToAddress;
	
	@Column(name="BILL_TO_TELP")
	private String billToTelp;
	
	@Column(name="SHIP_TO_NAME")
	private String shipToName;

	@Column(name="SHIP_TO_ADDRESS")
	private String shipToAddress;
	
	@Column(name="SHIP_TO_TELP")
	private String shipToTelp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USER_CREATED")
	private Integer userCreate;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@ColumnDefault( value = "0" )
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name="SO_NUMBER")
	private String soNumber;
	
	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}

	@Transient
	private RoleUser roleUSer;

	public RoleUser getRoleUSer() {
		return roleUSer;
	}

	public void setRoleUSer(RoleUser roleUSer) {
		this.roleUSer = roleUSer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
/*	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}*/

	/*public AddressBook getAddressBookBillTo() {
		return addressBookBillTo;
	}

	public void setAddressBookBillTo(AddressBook addressBookBillTo) {
		this.addressBookBillTo = addressBookBillTo;
	}

	public AddressBook getAddressBookShipTo() {
		return addressBookShipTo;
	}

	public void setAddressBookShipTo(AddressBook addressBookShipTo) {
		this.addressBookShipTo = addressBookShipTo;
	}
*/
	public String getBillToName() {
		return billToName;
	}

	public void setBillToName(String billToName) {
		this.billToName = billToName;
	}

	public String getBillToAddress() {
		return billToAddress;
	}

	public void setBillToAddress(String billToAddress) {
		this.billToAddress = billToAddress;
	}

	public String getBillToTelp() {
		return billToTelp;
	}

	public void setBillToTelp(String billToTelp) {
		this.billToTelp = billToTelp;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public String getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	public String getShipToTelp() {
		return shipToTelp;
	}

	public void setShipToTelp(String shipToTelp) {
		this.shipToTelp = shipToTelp;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(Integer userCreate) {
		this.userCreate = userCreate;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
