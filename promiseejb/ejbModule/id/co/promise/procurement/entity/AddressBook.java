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

import id.co.promise.procurement.master.Provinsi;

/**
 * @author User
 *
 */

@Entity
@Table(name = "T1_ADDRESS_BOOK")
@NamedQueries({
	@NamedQuery(name="AddressBook.find", query="SELECT x FROM AddressBook x WHERE x = x and x.isDelete=0"),
	@NamedQuery(name="AddressBook.findActive", query="SELECT x FROM AddressBook x WHERE x = x and x.isDelete=0 and x.status=1"),
    @NamedQuery(name="AddressBook.findByName", query="SELECT x FROM AddressBook x WHERE x.addressLabel LIKE :addressLabel"),
    @NamedQuery(name="AddressBook.findByUserId", query="SELECT x FROM AddressBook x WHERE x.userId = :userId"),
    @NamedQuery(name="AddressBook.getById", query="SELECT x FROM AddressBook x WHERE x.id = :id AND x.isDelete = 0"),
    @NamedQuery(name="AddressBook.findListByOrganisasi", query="SELECT x FROM AddressBook x WHERE x.organisasi.id = :organisasiId and x.isDelete=0"),
    @NamedQuery(name = "AddressBook.findByAddressCodeEbs", query = "SELECT addressBook FROM AddressBook addressBook WHERE addressBook.addressCodeEbs =:addressCodeEbs AND addressBook.isDelete = 0"),
    @NamedQuery(name = "AddressBook.findByPrId", query = "SELECT DISTINCT adBook, prItem FROM AddressBook adBook, PurchaseRequestItem prItem "
			+ "WHERE adBook.isDelete = :isDelete AND prItem.addressBook.id = adBook.id AND prItem.purchaserequest.id = :prId ORDER BY adBook.id"),
    @NamedQuery(name="AddressBook.getAddressBookListByOrganisasiList", query="SELECT x FROM AddressBook x WHERE x.organisasi.id IN (:organisasiListId) and x.isDelete=0")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE", 
pkColumnValue = "T1_ADDRESS_BOOK", initialValue = 1, allocationSize = 1)
public class AddressBook {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ADDRESS_BOOK_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName="ORGANISASI_ID")
	private Organisasi organisasi;
	
	@Column(name="ADDRESS_LABEL")
	private String addressLabel;
	
	@Column(name="FULL_NAME")
	private String fullName;
	
	@Column(name="TELEPHONE")
	private String telephone;
	
	@Column(name="FAX")
	private String fax;
	
	@Column(name="STREET_ADDRESS")
	private String streetAddress;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="CITY")
	private String city;

	@Column(name="DISTRICT")
	private String district;
	
	@Column(name="SUBDISTRICT")
	private String subDistrict;
	
	@Column(name="POSTALCODE")
	private String postalCode;
	
	@Column(name="DEFAULT_BILLING_ADDRESS")
	private Boolean defaultBillingAddress;
	
	@Column(name="DEFAULT_SHIPPING_ADDRESS")
	private Boolean defaultShippingAddress;
	
	@Column(name="STATUS")
	private Boolean status;
	
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

	@Column(name = "ADDRESS_CODE_EBS")
	private String addressCodeEbs;
	
	@Column(name = "ADDRESS_BOOK_EBS")
	private String addressBookEbs;
	
	@OneToOne
	@JoinColumn(name = "PROVINSI_ID", referencedColumnName="PROVINSI_ID")
	private Provinsi provinsi;
		
	public String getAddressBookEbs() {
		return addressBookEbs;
	}

	public void setAddressBookEbs(String addressBookEbs) {
		this.addressBookEbs = addressBookEbs;
	}

	public String getAddressCodeEbs() {
		return addressCodeEbs;
	}

	public void setAddressCodeEbs(String addressCodeEbs) {
		this.addressCodeEbs = addressCodeEbs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	public String getAddressLabel() {
		return addressLabel;
	}

	public void setAddressLabel(String addressLabel) {
		this.addressLabel = addressLabel;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSubDistrict() {
		return subDistrict;
	}

	public void setSubDistrict(String subDistrict) {
		this.subDistrict = subDistrict;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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

	public Boolean getDefaultBillingAddress() {
		return defaultBillingAddress;
	}

	public void setDefaultBillingAddress(Boolean defaultBillingAddress) {
		this.defaultBillingAddress = defaultBillingAddress;
	}

	public Boolean getDefaultShippingAddress() {
		return defaultShippingAddress;
	}

	public void setDefaultShippingAddress(Boolean defaultShippingAddress) {
		this.defaultShippingAddress = defaultShippingAddress;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Provinsi getProvinsi() {
		return provinsi;
	}

	public void setProvinsi(Provinsi provinsi) {
		this.provinsi = provinsi;
	}	
}

