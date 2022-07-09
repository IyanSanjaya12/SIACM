package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


import org.hibernate.annotations.ColumnDefault;

import id.co.promise.procurement.catalog.entity.Catalog;

@Entity
@Table(name = "T4_PR_ITEM")
@NamedQueries({ @NamedQuery(name = "PurchaseRequestItem.find", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0"),
		@NamedQuery(name = "PurchaseRequestItem.findByPurchaseRequest", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.purchaserequest.id=:purchaserequestId"),
		@NamedQuery(name = "PurchaseRequestItem.findByPurchaseRequestId", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.purchaserequest.id=:purchaseRequestId"),
		@NamedQuery(name = "PurchaseRequestItem.findByPurchaseRequestJoinId", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.purchaserequestjoin.id=:purchaseRequestId"),
		@NamedQuery(name = "PurchaseRequestItem.findByPurchaseRequestAndItem", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.purchaserequest.id=:purchaseRequestId and x.item.id=:itemId"),
		@NamedQuery(name = "PurchaseRequestItem.findByPRAndVendor", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.vendor = :vendor and x.purchaserequest = :purchaserequest"),
		@NamedQuery(name = "PurchaseRequestItem.findByPRGroupByVendor", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.purchaserequest.id=:purchaseRequestId and x.vendor <> null group by x.vendor"),
		@NamedQuery(name = "PurchaseRequestItem.findByPurchaseRequestAndItemAndVendor", query = "SELECT x FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.purchaserequest.id=:purchaseRequestId and x.item.id=:itemId and x.vendor.id = :vendorId"),
		@NamedQuery(name = "PurchaseRequestItem.groupByPurchaseRequestId", query = "SELECT x.slaDeliveryTime FROM PurchaseRequestItem x WHERE x.isDelete = 0 and x.purchaserequest.id=:purchaseRequestId GROUP BY x.addressBook.id ORDER BY x.id ASC"),

})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T4_PR_ITEM", initialValue = 1, allocationSize = 1)
public class PurchaseRequestItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PURCHASE_REQUEST_ITEM_ID")
	private Integer id;

	// KAI - 20210203 - #20867
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PURCHASE_REQUEST_ID", referencedColumnName = "PURCHASE_REQUEST_ID")
	private PurchaseRequest purchaserequest;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

	@OneToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "CATALOG_ID", referencedColumnName = "CATALOG_ID")
	private Catalog catalog;

	@OneToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;

	@Column(name = "ITEM_NAME")
	private String itemname;

	@Column(name = "KODE")
	private String kode;

	@Column(name = "VENDORNAME")
	private String vendorname;

	@ColumnDefault( value = "0" )
	@Column(name = "QUANTITY")
	private Double quantity;

	@ColumnDefault( value = "0" )
	@Column(name = "QUANTITY_BALANCE")
	private Double quantitybalance;

	@ColumnDefault( value = "0" )
	@Column(name = "PRICE")
	private Double price;

	@Column(name = "COST_CENTER")
	private String costcenter;

	@Column(name = "STATUS")
	private String status;

	@ColumnDefault( value = "0" )
	@Column(name = "TOTAL")
	private Double total;

	@ColumnDefault( value = "0" )
	@Column(name = "ONGKOS_KIRIM")
	private Double ongkosKirim;

	@ColumnDefault( value = "0" )
	@Column(name = "ASURANSI")
	private Double asuransi;

	@ColumnDefault( value = "0" )
	@Column(name = "DISCOUNT")
	private Double discount;

	@ColumnDefault( value = "0" )
	@Column(name = "NORMAL_PRICE")
	private Double normalPrice;

	@Column(name = "UNIT")
	private String unit;
	
	@Lob
	@Column(name = "SPECIFICATION")
	private String specification;

	@Column(name = "PATH")
	private String path;

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

	@ManyToOne
	@JoinColumn(name = "PR_JOIN_ID", referencedColumnName = "PURCHASE_REQUEST_ID")
	private PurchaseRequest purchaserequestjoin;

	@ColumnDefault( value = "0" )
	@Column(name = "PRICE_JOIN")
	private Double priceafterjoin;

	@ColumnDefault( value = "0" )
	@Column(name = "QTY_JOIN")
	private Double qtyafterjoin;
	
	@ManyToOne
	@JoinColumn(name = "GL_ACCOUNT_SAP_ID", referencedColumnName = "GL_ACCOUNT_SAP_ID")
	private GLAccountSap gLAccountSap ;
	
	@ManyToOne
	@JoinColumn(name = "COSTCENTER_SAP_ID", referencedColumnName = "COSTCENTER_SAP_ID")
	private CostCenterSap costCenterSap ;
	
	@ManyToOne
	@JoinColumn(name = "PUR_GROUP_SAP_ID", referencedColumnName = "PUR_GROUP_SAP_ID")
	private PurGroupSap purGroupSap ;
	
	@ManyToOne
	@JoinColumn(name = "STORE_LOC_SAP_ID", referencedColumnName = "STORE_LOC_SAP_ID")
	private StoreLocSap storeLocSap;
	
	@Column(name = "ACCTASSCAT")
	private String acctasscat;
	
	@Column(name = "PREQ_NAME")
	private String preqName;
	
	// KAI - 20210204 - #20867
	@Column(name = "ALAMAT")
	private String alamat;

	@ManyToOne
	@JoinColumn(name = "ADDRESS_BOOK_ID", referencedColumnName = "ADDRESS_BOOK_ID", nullable = true)
	private AddressBook addressBook;
	
	// KAI - 27/10/2021 - https://project.promise.co.id/issues/25140
	@Column(name = "SLA_DELIVERY_TIME")
	private Integer slaDeliveryTime;
	
	@Transient
	private Double grandTotal;

	@Transient
	private Date estimatedDeliveryTime;

	public String getAcctasscat() {
		return acctasscat;
	}

	public void setAcctasscat(String acctasscat) {
		this.acctasscat = acctasscat;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PurchaseRequest getPurchaserequest() {
		return purchaserequest;
	}

	public void setPurchaserequest(PurchaseRequest purchaserequest) {
		this.purchaserequest = purchaserequest;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getQuantitybalance() {
		return quantitybalance;
	}

	public void setQuantitybalance(Double quantitybalance) {
		this.quantitybalance = quantitybalance;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(String costcenter) {
		this.costcenter = costcenter;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getOngkosKirim() {
		return ongkosKirim;
	}

	public void setOngkosKirim(Double ongkosKirim) {
		this.ongkosKirim = ongkosKirim;
	}

	public Double getAsuransi() {
		return asuransi;
	}

	public void setAsuransi(Double asuransi) {
		this.asuransi = asuransi;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getNormalPrice() {
		return normalPrice;
	}

	public void setNormalPrice(Double normalPrice) {
		this.normalPrice = normalPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public PurchaseRequest getPurchaserequestjoin() {
		return purchaserequestjoin;
	}

	public void setPurchaserequestjoin(PurchaseRequest purchaserequestjoin) {
		this.purchaserequestjoin = purchaserequestjoin;
	}

	public Double getPriceafterjoin() {
		return priceafterjoin;
	}

	public void setPriceafterjoin(Double priceafterjoin) {
		this.priceafterjoin = priceafterjoin;
	}

	public Double getQtyafterjoin() {
		return qtyafterjoin;
	}

	public void setQtyafterjoin(Double qtyafterjoin) {
		this.qtyafterjoin = qtyafterjoin;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Date getEstimatedDeliveryTime() {
		return estimatedDeliveryTime;
	}

	public void setEstimatedDeliveryTime(Date estimatedDeliveryTime) {
		this.estimatedDeliveryTime = estimatedDeliveryTime;
	}

	public GLAccountSap getgLAccountSap() {
		return gLAccountSap;
	}

	public void setgLAccountSap(GLAccountSap gLAccountSap) {
		this.gLAccountSap = gLAccountSap;
	}

	public CostCenterSap getCostCenterSap() {
		return costCenterSap;
	}

	public void setCostCenterSap(CostCenterSap costCenterSap) {
		this.costCenterSap = costCenterSap;
	}

	public PurGroupSap getPurGroupSap() {
		return purGroupSap;
	}

	public void setPurGroupSap(PurGroupSap purGroupSap) {
		this.purGroupSap = purGroupSap;
	}

	public StoreLocSap getStoreLocSap() {
		return storeLocSap;
	}

	public void setStoreLocSap(StoreLocSap storeLocSap) {
		this.storeLocSap = storeLocSap;
	}

	public String getPreqName() {
		return preqName;
	}

	public void setPreqName(String preqName) {
		this.preqName = preqName;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public AddressBook getAddressBook() {
		return addressBook;
	}

	public void setAddressBook(AddressBook addressBook) {
		this.addressBook = addressBook;
	}

	public Integer getSlaDeliveryTime() {
		return slaDeliveryTime;
	}

	public void setSlaDeliveryTime(Integer slaDeliveryTime) {
		this.slaDeliveryTime = slaDeliveryTime;
	}

	@Override
	public String toString() {
		return "PurchaseRequestItem [id=" + id + ", purchaserequest=" + purchaserequest + ", vendor=" + vendor + ", item=" + item
				+ ", mataUang=" + mataUang + ", itemname=" + itemname + ", kode=" + kode + ", vendorname=" + vendorname + ", quantity="
				+ quantity + ", quantitybalance=" + quantitybalance + ", price=" + price + ", costcenter=" + costcenter + ", status="
				+ status + ", total=" + total + ", unit=" + unit + ", specification=" + specification + ", path=" + path + ", created="
				+ created + ", updated=" + updated + ", deleted=" + deleted + ", userId=" + userId + ", isDelete=" + isDelete
				+ ", purchaserequestjoin=" + purchaserequestjoin + ", priceafterjoin=" + priceafterjoin + ", qtyafterjoin=" + qtyafterjoin
				+ "]";
	}

}
